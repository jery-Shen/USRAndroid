package usr.work;

import java.util.List;

import com.alibaba.fastjson.JSON;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import usr.work.DeviceDetailActivity.JsBridge;
import usr.work.bean.Device;

public class DeviceSetActivity extends Activity {

	WebView webView;
	TextView rightText;
	private TextView top_title;
	private Device device;
	private int areaId;
	private int deviceId;
	private ProgressBar loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_device_set);
		
		areaId = getIntent().getIntExtra("areaId", 0);
		deviceId = getIntent().getIntExtra("deviceId", 0);
		
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("设备"+deviceId);
		backBtn();
		
		rightText = (TextView) findViewById(R.id.right_text);
		rightText.setText("保存");
		rightText.setVisibility(View.VISIBLE);
		
		loading = (ProgressBar) findViewById(R.id.loading);
		
		device = getDeviceById(areaId,deviceId);
		
		rightText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				webView.loadUrl("javascript:saveDevice()");
			}
		});
		
		webView = (WebView) findViewById(R.id.webview);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				webView.loadUrl("javascript:onData('"+JSON.toJSONString(device)+"')");
			}
		});
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new JsBridge(), "JsBridge");
		
		webView.loadUrl("file:///android_asset/deviceSet.html"); 
		
	}
	
	private Device getDeviceById(int areaId,int deviceId){
		List<Device> deviceList = DeviceListActivity.mDataList;
		for(Device device:deviceList){
			if(device!=null&&device.getDeviceId()==deviceId&&device.getAreaId()==areaId){
				return device;
			}
		}
		return null;
	}
	
	private void backBtn() {
		findViewById(R.id.btn_exit).setVisibility(View.VISIBLE);
		findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	class JsBridge{
		
		@JavascriptInterface
		public void saveSuccess(){
			runOnUiThread(new Runnable() {
				public void run() {
					loading.setVisibility(View.INVISIBLE);
					DeviceSetActivity.this.finish();
				}
			});
			
        }
		
		@JavascriptInterface
		public void saveError(final String error){
			runOnUiThread(new Runnable() {
				public void run() {
					loading.setVisibility(View.INVISIBLE);
			        Toast.makeText(DeviceSetActivity.this, error, Toast.LENGTH_SHORT).show();
				}
			});
			
        }
		
		@JavascriptInterface
		public void showLoading(){
			runOnUiThread(new Runnable() {
				public void run() {
					loading.setVisibility(View.VISIBLE);
				}
			});
			
        }
		
		
	}
}
