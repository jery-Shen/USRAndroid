package usr.work;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import usr.work.bean.User;
import usr.work.utils.HttpUtil;

public class UpdatePwdActivity extends Activity {
	
	public User user;
	private TextView top_title;
    private EditText mOldPwd;
    private EditText mNewPwd;
    private ProgressBar loading;
    
    private String mUrl = HttpUtil.URL_PRE+"UpdateUserPwd";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_update_pwd);
		
		SharedPreferences preferences = getSharedPreferences("set", 0);
		String userStr = preferences.getString("user", "");
		user = JSON.parseObject(userStr, User.class);
		
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("修改密码");
		
		mOldPwd = (EditText) findViewById(R.id.oldpwd);
		mNewPwd = (EditText) findViewById(R.id.newpwd);
        loading = (ProgressBar) findViewById(R.id.loading);
        
        backBtn();
        
        findViewById(R.id.savebtn).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				String oldPwd = mOldPwd.getText().toString().trim();
				String newPwd = mNewPwd.getText().toString().trim();
				
				if(oldPwd.equals("")||newPwd.equals("")){
					Toast.makeText(UpdatePwdActivity.this, "请填写密码",Toast.LENGTH_SHORT).show();
				}else if(!user.getUserPwd().equals(oldPwd)){
					Toast.makeText(UpdatePwdActivity.this, "旧密码错误",Toast.LENGTH_SHORT).show();
					
				}else if(newPwd.length()<6){
					Toast.makeText(UpdatePwdActivity.this, "新密码长度必须大于6位",Toast.LENGTH_SHORT).show();
					
				}else{
					loading.setVisibility(View.VISIBLE);
					new GetDataTask().execute(user.getUserName(),user.getUserPwd(),newPwd);
				}

			}
		});
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
	
	private class GetDataTask extends AsyncTask<String, Integer, String>{
		@Override
		protected String doInBackground(String... params) {
			String userName = params[0];
			String userPwd = params[1];
			String newPwd = params[2];
			user.setUserPwd(newPwd);
			String postParams = "userName="+userName+"&userPwd="+userPwd+"&newPwd="+newPwd;
			String url = mUrl;
			SharedPreferences preferences = getSharedPreferences("set", 0);
			String userStr = preferences.getString("user", "");
			User user = JSON.parseObject(userStr, User.class);
			Map<String, String> map =  HttpUtil.getSign(user);
			url = url + "?token=" + map.get("token")+"&timestamp="+map.get("timestamp")+"&sign="+map.get("sign");
			String content = HttpUtil.postUrl(url, postParams);
			return content;
		}
		
		@Override
		protected void onPostExecute(String content) {
			loading.setVisibility(View.INVISIBLE);
			if(!content.equals("")){
				JSONObject jsonObject = JSON.parseObject(content);
				if(jsonObject.getIntValue("status")==200){
					SharedPreferences preferences = getSharedPreferences("set", 0);
					Editor editor = preferences.edit();
					editor.putString("user", JSON.toJSONString(user));
					editor.commit();
					UpdatePwdActivity.this.finish();
					Toast.makeText(UpdatePwdActivity.this, "修改成功",Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(UpdatePwdActivity.this, jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
					
				}
			}else{
				Toast.makeText(UpdatePwdActivity.this, "网络连接错误",Toast.LENGTH_SHORT).show();
			}
			
		}
	}
}
