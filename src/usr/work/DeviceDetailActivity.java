package usr.work;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSON;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import usr.work.application.USRApplication;
import usr.work.bean.Device;
import usr.work.utils.HttpUtil;

public class DeviceDetailActivity extends Activity {

	
	private ImageView rightBtn;
	private WebView webView;
	private TextView top_title;
	
	private Device device;
	private int areaId;
	private int deviceId;
	
	
	DecimalFormat df = new DecimalFormat("0.00");
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
					if(device.getInfoBar()==0){
						findViewById(R.id.tao_top_spc).setBackgroundColor(Color.parseColor("#aaaaaa"));
					}else if(device.getInfoBar()==1){
						findViewById(R.id.tao_top_spc).setBackgroundColor(Color.parseColor("#128bed"));
					}else{
						findViewById(R.id.tao_top_spc).setBackgroundColor(Color.parseColor("#e64340"));
					}
					
					webView.loadUrl("javascript:onData('"+JSON.toJSONString(device).replace("\\", "\\\\")+"')");
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
		rightBtn = (ImageView) findViewById(R.id.right_btn);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.wv_edit_white));
		areaId = getIntent().getIntExtra("areaId", 0);
		deviceId = getIntent().getIntExtra("deviceId", 0);
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("ivc智控"+deviceId);
		backBtn();
		
		device = getDeviceById(areaId,deviceId);
		if(device.getInfoBar()==0){
			findViewById(R.id.tao_top_spc).setBackgroundColor(Color.parseColor("#aaaaaa"));
		}else if(device.getInfoBar()==1){
			findViewById(R.id.tao_top_spc).setBackgroundColor(Color.parseColor("#128bed"));
		}else{
			findViewById(R.id.tao_top_spc).setBackgroundColor(Color.parseColor("#e64340"));
		}
		timer.schedule(task, 500, 1000);
		
		rightBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(DeviceDetailActivity.this,DeviceSetActivity.class);
				intent.putExtra("areaId",areaId);
				intent.putExtra("deviceId",deviceId);
				startActivityForResult(intent, 1);
				
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
				webView.loadUrl("javascript:onData('"+JSON.toJSONString(device).replace("\\", "\\\\")+"')");
			}
		});
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new JsBridge(), "JsBridge");
		webView.loadUrl("file:///android_asset/deviceDetail.html"); 
		
	}
	
	private Device getDeviceById(int areaId, int deviceId){
		List<Device> deviceList = ((USRApplication)getApplicationContext()).deviceList;
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==8){
			if(device!=null){
				device = getDeviceById(areaId,deviceId);//把device传过来
				webView.loadUrl("javascript:onData('"+JSON.toJSONString(device).replace("\\", "\\\\")+"')");
			}
		}
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
