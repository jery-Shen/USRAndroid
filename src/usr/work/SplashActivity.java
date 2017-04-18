package usr.work;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class SplashActivity extends Activity {
	



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		

		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				SharedPreferences preferences = getSharedPreferences("set", 0);
				String userStr = preferences.getString("user", "");
				if(userStr.equals("")){
					Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
				}else{
					Intent intent = new Intent(SplashActivity.this, DeviceListActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
				}
				
			}
		}, 2000);
        
        
        
		
		
		
		
		
		
	}
}
