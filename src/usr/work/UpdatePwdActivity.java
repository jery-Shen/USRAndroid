package usr.work;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import usr.work.utils.HttpUtil;

public class UpdatePwdActivity extends Activity {

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
				}else if(newPwd.length()<6){
					Toast.makeText(UpdatePwdActivity.this, "新密码长度必须大于6位",Toast.LENGTH_SHORT).show();
					
				}else{
//					loading.setVisibility(View.VISIBLE);
//					new GetDataTask().execute(userName,userPwd);
					Toast.makeText(UpdatePwdActivity.this, "成功",Toast.LENGTH_SHORT).show();
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
}
