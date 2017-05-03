package usr.work.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import usr.work.DeviceListActivity;
import usr.work.DeviceListWifiActivity;
import usr.work.R;

public class NotificationUtil {
	
	static int count = 0;
	
	public static void pushAlarm(Context context,int deviceId,String msg,int mode){
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent;
		if(mode==0){
			intent = new Intent(context,DeviceListActivity.class);
		}else{
			intent = new Intent(context,DeviceListWifiActivity.class);
		}
		intent.putExtra("deviceId", deviceId);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,  
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification.Builder(context)
				.setDefaults((Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE))
				.setTicker("设备报警")
				.setContentTitle("设备"+deviceId)
			    .setContentText(msg)
			    .setSmallIcon(R.drawable.icon)
			    .setWhen(System.currentTimeMillis())
			    .setContentIntent(pendingIntent)
			    .build();
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(count,notification);
		
		count++;
	}
	
	public static void wakeLock(Context context){
		PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE); 
		WakeLock wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "Tag");
		wakeLock.acquire(1000);
	}
	
	public static void cancel(Context context){
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}
	
	
}
