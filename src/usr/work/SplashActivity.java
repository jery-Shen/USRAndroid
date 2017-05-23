package usr.work;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;
import usr.work.bean.User;
import usr.work.utils.HttpUtil;

public class SplashActivity extends Activity {
	
	private String mUrl = HttpUtil.URL_PRE+"Login";
	
	private boolean pwdInvalid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
	
		SharedPreferences preferences = getSharedPreferences("set", 0);
		String userStr = preferences.getString("user", "");
		if(userStr.equals("")){
			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
			startActivity(intent);
			SplashActivity.this.finish();
		}else{
			User user = JSON.parseObject(userStr, User.class);
			pwdInvalid = false;
			new GetDataTask().execute(user.getUserName(),user.getUserPwd());
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if(!pwdInvalid){
						SharedPreferences preferences = getSharedPreferences("set", 0);
						int  mode = preferences.getInt("mode", 0);
						Intent intent = null;
						if(mode==0){
							intent = new Intent(SplashActivity.this, DeviceListActivity.class);
						}else{
							intent = new Intent(SplashActivity.this, DeviceListWifiActivity.class);
						}
						startActivity(intent);
						SplashActivity.this.finish();
					}
				}
			}, 1500);
		}
	}
	
	private class GetDataTask extends AsyncTask<String, Integer, String>{
		@Override
		protected String doInBackground(String... params) {
			String userName = params[0];
			String userPwd = params[1];
			String postParams = "userName="+userName+"&userPwd="+userPwd;
			String url = mUrl;
			String content = HttpUtil.postUrl(url, postParams);
			return content;
		}
		
		@Override
		protected void onPostExecute(String content) {
			if(!content.equals("")){
				JSONObject jsonObject = JSON.parseObject(content);
				if(jsonObject.getIntValue("status")==200){
					JSONObject res = jsonObject.getJSONObject("result");
					JSONObject jUser = res.getJSONObject("user");
					JSONArray jHostList = res.getJSONArray("hostList");
					SharedPreferences preferences = getSharedPreferences("set", 0);
					Editor editor = preferences.edit();
					editor.putString("user", jUser.toJSONString());
					editor.putString("hostList", jHostList.toJSONString());
					editor.commit();
				}else{
					pwdInvalid = true;
					SharedPreferences preferences = getSharedPreferences("set", 0);
					Editor editor = preferences.edit();
					editor.putString("user", "");
					editor.commit();
					Toast.makeText(SplashActivity.this, "密码失效，请重新登录",Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
				}
			}
			
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
}
