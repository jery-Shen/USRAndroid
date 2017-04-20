package usr.work;

import com.alibaba.fastjson.JSON;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import usr.work.bean.User;

public class SetActivity extends Activity {

	private WebView webView;
	private TextView top_title;
	
	private User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set);
		
		SharedPreferences preferences = getSharedPreferences("set", 0);
		String userStr = preferences.getString("user", "");
		user = JSON.parseObject(userStr, User.class);
		
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("设置");
		backBtn();
		
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
				webView.loadUrl("javascript:onData('"+JSON.toJSONString(user)+"')");
			}
		});
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new JsBridge(), "JsBridge");
		webView.loadUrl("file:///android_asset/set.html"); 
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
		public void userInfo(){
			Intent intent = new Intent(SetActivity.this, UserActivity.class);
			startActivity(intent);
        }
		
		@JavascriptInterface
		public void updateHost(){
           
        }
		
		@JavascriptInterface
		public void about(){
           
        }
		
		@JavascriptInterface
		public void help(){
           
        }
		
		@JavascriptInterface
		public void logout(){
			SharedPreferences preferences = getSharedPreferences("set", 0);
			Editor editor = preferences.edit();
			editor.putString("user", "");
			editor.commit();
			setResult(9);
			SetActivity.this.finish();
        }
	}
}
