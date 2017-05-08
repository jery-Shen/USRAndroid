package usr.work;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import usr.work.utils.HttpUtil;


/**
 * A login screen that offers login via email/password.

 */
public class LoginActivity extends Activity{



   
    private EditText mUserName;
    private EditText mUserPwd;
    private ProgressBar loading;
    
    

    private ScrollView mScrollView;
    private View content;
    private int keyHeight = 0;      //软件盘弹起后所占高度
    
    private String mUrl = HttpUtil.URL_PRE+"Login";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);


        // Set up the login form.
        mUserName = (EditText) findViewById(R.id.username);
        mUserPwd = (EditText) findViewById(R.id.userpwd);
        loading = (ProgressBar) findViewById(R.id.loading);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        content = findViewById(R.id.content);
        int screenHeight = this.getResources().getDisplayMetrics().heightPixels;
        keyHeight = screenHeight / 3;
        
        findViewById(R.id.loginbtn).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				String userName = mUserName.getText().toString().trim();
				String userPwd = mUserPwd.getText().toString().trim();
				
				if(userName.equals("")||userPwd.equals("")){
					Toast.makeText(LoginActivity.this, "用户名密码不能为空",Toast.LENGTH_SHORT).show();
				}else{
					loading.setVisibility(View.VISIBLE);
					new GetDataTask().execute(userName,userPwd);
					
				}

			}
		});
        
        initListener();
    }
    
    private void initListener() {
        /**
         * 禁止键盘弹起的时候可以滚动
         */
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        mScrollView.addOnLayoutChangeListener(new ViewGroup.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom != 0 && oldBottom != 0 && (oldBottom - bottom > keyHeight)) {
                    int dist = content.getBottom() - bottom;
                    if (dist>0){
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(content, "translationY", 0.0f, -dist);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                      
                    }
                   
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    if ((content.getBottom() - oldBottom)>0){
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(content, "translationY", content.getTranslationY(), 0);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                       
                    }
                   
                }
            }
        });
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
			loading.setVisibility(View.INVISIBLE);
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
					Intent intent = new Intent(LoginActivity.this, DeviceListActivity.class);
					startActivity(intent);
					LoginActivity.this.finish();
				}else{
					Toast.makeText(LoginActivity.this, jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
					
				}
			}else{
				Toast.makeText(LoginActivity.this, "网络连接错误",Toast.LENGTH_SHORT).show();
			}
			
		}
	}

	

   


}
