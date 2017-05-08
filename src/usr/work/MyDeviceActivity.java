package usr.work;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import usr.work.bean.Host;
import usr.work.bean.User;
import usr.work.utils.HttpUtil;

public class MyDeviceActivity extends Activity {

	private WebView webView;
	private TextView top_title;
	private ImageView rightBtn;
	private ProgressBar loading;
	
	private User user;
	private List<Host> hostList;
	
	
	private String mUrl = HttpUtil.URL_PRE + "GetHostList";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_device);
		
		SharedPreferences preferences = getSharedPreferences("set", 0);
		String userStr = preferences.getString("user", "");
		user = JSON.parseObject(userStr, User.class);
		String hostListStr = preferences.getString("hostList", "");
		hostList = JSON.parseArray(hostListStr, Host.class);
		
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("我的设备");
		backBtn();
		
		loading = (ProgressBar) findViewById(R.id.loading);
		rightBtn = (ImageView) findViewById(R.id.right_btn);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.wv_refresh_white));
		rightBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 loading.setVisibility(View.VISIBLE);
				 new GetDataTask().execute(user.getAreaId()+"");
				
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
				webView.loadUrl("javascript:onData('"+JSON.toJSONString(hostList)+"')");
			}
		});
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new JsBridge(), "JsBridge");
		webView.loadUrl("file:///android_asset/myDevice.html"); 
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
		public void save(){
           
        }
		
		@JavascriptInterface
		public void updatePwd(){
			
        }
	}
	
	private class GetDataTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			String areaId = params[0];
			String url = mUrl + "?areaId=" + areaId;
			SharedPreferences preferences = getSharedPreferences("set", 0);
			String userStr = preferences.getString("user", "");
			User user = JSON.parseObject(userStr, User.class);
			Map<String, String> map = HttpUtil.getSign(user);
			url = url + "&token=" + map.get("token") + "&timestamp=" + map.get("timestamp") + "&sign="
					+ map.get("sign");
			String content = HttpUtil.getStrFromUrl(url);
			return content;
		}

		@Override
		protected void onPostExecute(String content) {
			loading.setVisibility(View.INVISIBLE);
			if (!content.equals("")) {
				JSONObject jsonObject = JSON.parseObject(content);
				if (jsonObject.getIntValue("status") == 200) {
					JSONArray jHostList = jsonObject.getJSONArray("result");
					SharedPreferences preferences = getSharedPreferences("set", 0);
					Editor editor = preferences.edit();
					editor.putString("hostList", jHostList.toJSONString());
					editor.commit();
					Toast.makeText(MyDeviceActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
					webView.loadUrl("javascript:onData('"+ preferences.getString("hostList", "[]")+"')");
					Log.i("syj", preferences.getString("hostList", "[]"));
				} else {
					Toast.makeText(MyDeviceActivity.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();

				}
			} else {
				Toast.makeText(MyDeviceActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
			}

		}
	}
}
