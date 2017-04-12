package usr.work;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSON;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import usr.work.bean.Device;
import usr.work.utils.HttpUtil;

public class DeviceDetailActivity extends Activity {

	
	TextView rightText;
	
	String url = HttpUtil.URL_PRE+"GetDeviceDetail";
	
	WebView webView;
	private TextView top_title;
	private Device device;
	private int areaId;
	private int deviceId;
	
	
	DecimalFormat df = new DecimalFormat("#.00");
	private final Timer timer = new Timer();
	private TimerTask task = new TimerTask() {  
	    @Override  
	    public void run() {  
	        Message message = new Message();  
	        message.what = 6;  
	        handler.sendMessage(message);  
	    }  
	}; 

	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==6){
				if(device!=null){
					device = getDeviceById(areaId,deviceId);
					webView.loadUrl("javascript:onData('"+JSON.toJSONString(device)+"')");
				}
				//Log.i("syj", JSON.toJSONString(device));
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_device_detail);
		
		rightText = (TextView) findViewById(R.id.right_text);
		rightText.setVisibility(View.VISIBLE);
		areaId = getIntent().getIntExtra("areaId", 0);
		deviceId = getIntent().getIntExtra("deviceId", 0);
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("设备"+deviceId);
		backBtn();
		
		device = getDeviceById(areaId,deviceId);
		timer.schedule(task, 500, 1000);
		
		rightText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(DeviceDetailActivity.this,DeviceSetActivity.class);
				intent.putExtra("areaId",areaId);
				intent.putExtra("deviceId",deviceId);
				startActivity(intent);
				
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
		webView.loadUrl("file:///android_asset/deviceDetail.html"); 
		
	}
	
	private Device getDeviceById(int areaId, int deviceId){
		List<Device> deviceList = DeviceListActivity.mDataList;
		for(Device device:deviceList){
			if(device!=null&&device.getDeviceId()==deviceId&&device.getAreaId()==areaId){
				return device;
			}
		}
		return null;
	}
	
	@Override
	protected void onDestroy() {
		timer.cancel();
		super.onDestroy();
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
		public void javaMethod(String p){
            Log.i("syj" , "JSHook.JavaMethod() called! + "+p);
        }
		
		@JavascriptInterface
		public void testMsg(){
           Log.i("syj", "kkkkkkkkkkkkkkk11111");
        }
	}

	
}
