package usr.work;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		SharedPreferences preferences = getSharedPreferences("set", 0);
		String userStr = preferences.getString("user", "");
		if(userStr.equals("")){
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			this.finish();
		}else{
			Intent intent = new Intent(this, DeviceListActivity.class);
			startActivity(intent);
			this.finish();
		}
		
		
		
		
		
	}
}
