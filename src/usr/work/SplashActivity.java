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
	
	private final Timer timer = new Timer();
	int i=0;
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
				i++;
				Log.i("syj", "haha");
				Toast.makeText(SplashActivity.this, "HI"+i, Toast.LENGTH_SHORT).show();
				NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

				PendingIntent contentIndent = PendingIntent.getActivity(SplashActivity.this, 0, new Intent(SplashActivity.this,SplashActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);  

		        Notification noti = new Notification.Builder(SplashActivity.this)
		        		.setContentIntent(contentIndent)
		                .setContentTitle("New mail from ")
		                .setContentText("bbb"+i)
		                .setWhen(1000)
		                .setSmallIcon(R.drawable.wv_back)

		                .build();
		        nm.notify(5, noti);
		        
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		SharedPreferences preferences = getSharedPreferences("set", 0);
		String userStr = preferences.getString("user", "");
		
		//timer.schedule(task, 0, 3000);
		
		
        
        
        
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
