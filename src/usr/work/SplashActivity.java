package usr.work;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
			startActivity(intent);
			SplashActivity.this.finish();
		}else{
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
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
			}, 1000);
			
		}
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		
		super.onNewIntent(intent);
	}
}
