package usr.work;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import usr.work.application.USRApplication;
import usr.work.bean.Device;
import usr.work.bean.User;
import usr.work.utils.HttpUtil;
import usr.work.utils.Md5;

public class DeviceSetActivity extends Activity {

	private WebView webView;
	private ImageView rightBtn;
	private TextView top_title;
	private ProgressBar loading;
	
	private Device device;
	private int areaId;
	private int deviceId;
	
	private String mUrl = HttpUtil.URL_PRE+"UpdateDevice";
	
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
		
		rightBtn = (ImageView) findViewById(R.id.right_btn);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.wv_save_white));
		
		loading = (ProgressBar) findViewById(R.id.loading);
		
		device = getDeviceById(areaId,deviceId);
		
		rightBtn.setOnClickListener(new View.OnClickListener() {
			
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
		List<Device> deviceList = ((USRApplication)getApplicationContext()).deviceList;
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
	
	private class UpdateDeviceTask extends AsyncTask<String, Integer, String>{
		@Override
		protected String doInBackground(String... params) {
			String paramsStr = params[0];
			Map<String, Object> paramMap = JSON.parseObject(paramsStr,new TypeReference<Map<String, Object>>(){} );
			StringBuffer postParams = new StringBuffer();
			String timestamp = System.currentTimeMillis()/1000+"";
			for (Map.Entry<String, Object> entry : paramMap.entrySet()) {  
				postParams.append(entry.getKey() + "=" + entry.getValue());  
				postParams.append("&");  
		    } 
			postParams.append("timestamp="+timestamp);
			Log.i("syj", postParams.toString());

			String url = mUrl;
			
			SharedPreferences preferences = getSharedPreferences("set", 0);
			String userStr = preferences.getString("user", "");
			User user = JSON.parseObject(userStr, User.class);
			Map<String, String> map =  HttpUtil.getSign(user);
			
			url = url + "?token=" + map.get("token")+"&timestamp="+map.get("timestamp")+"&sign="+map.get("sign");
			
			String content = HttpUtil.postUrl(url, postParams.toString());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return content;
		}
		
		@Override
		protected void onPostExecute(String content) {
			loading.setVisibility(View.INVISIBLE);
			if(!content.equals("")){
				JSONObject jsonObject = JSON.parseObject(content);
				if(jsonObject.getIntValue("status")==200){
					DeviceSetActivity.this.setResult(8);
					DeviceSetActivity.this.finish();
				}else{
					Toast.makeText(DeviceSetActivity.this, jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
					
				}
			}else{
				Toast.makeText(DeviceSetActivity.this, "网络连接错误",Toast.LENGTH_SHORT).show();
			}
			
		}
	}
	
	class JsBridge{
		
		@JavascriptInterface
		public void updateDevice(String paramsStr){
			runOnUiThread(new Runnable() {
				public void run() {
					loading.setVisibility(View.VISIBLE);
				}
			});
			new UpdateDeviceTask().execute(paramsStr);
				
        }
		
		@JavascriptInterface
		public void noChange(){
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
