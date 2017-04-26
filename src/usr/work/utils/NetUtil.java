package usr.work.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetUtil {
	public static boolean isNetworkAvailable(Context context) {   
        ConnectivityManager cm = (ConnectivityManager) context   
                .getSystemService(Context.CONNECTIVITY_SERVICE);   
        if (cm == null) {   
        } else {
        	return cm.getActiveNetworkInfo().isAvailable();
        }   
        return false;   
    }
	
	public static boolean isServerAvailable(){
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	

	

	
	public static WifiInfo getWifiInfo(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
		if (wifiManager.isWifiEnabled() && ipAddress != 0) {
			return wifiInfo;
		} else {
			return null;
		}
	}
	
	public static String getWifiIp(int ipAddress){
		String localIp = "192.168.0.1";
		if(ipAddress!=0){
        	localIp = ((ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"."   
            		+(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));
        }
		return localIp;
	}

	
	
}
