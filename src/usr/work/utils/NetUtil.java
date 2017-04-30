package usr.work.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.alibaba.fastjson.JSON;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class NetUtil {
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
		} else {
			return cm.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}

	public static boolean isServerAvailable() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public static int Online(int areaId) {
		int OnLineStatus = 0;
		String url = HttpUtil.URL_PRE + "OnLine";
		if (areaId != 0) {
			url += "?areaId=" + areaId;
		}
		String content = HttpUtil.getStrFromUrl(url);
		if (content != null && !content.equals("")) {
			OnLineStatus = JSON.parseObject(content).getIntValue("status");
		}
		return OnLineStatus;
	}

	public static  boolean Online() {
		String result = null;

		try {
			String ip = "www.baidu.com";// 除非百度挂了，否则用这个应该没问题~
			Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping3次
			// 读取ping的内容，可不加。
			InputStream input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer stringBuffer = new StringBuffer();
			String content = "";
			while ((content = in.readLine()) != null) {
				stringBuffer.append(content);
			}
			Log.i("TTT", "result content : " + stringBuffer.toString());
			int status = p.waitFor();
			if (status == 0) {
				result = "successful~";
				return true;
			} else {
				result = "failed~ cannot reach the IP address";
			}
		} catch (IOException e) {
			result = "failed~ IOException";
		} catch (InterruptedException e) {
			result = "failed~ InterruptedException";
		} finally {
			Log.i("TTT", "result = " + result);
		}

		return false;

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

	public static String getWifiIp(int ipAddress) {
		String localIp = "192.168.0.1";
		if (ipAddress != 0) {
			localIp = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "." + (ipAddress >> 16 & 0xff) + "."
					+ (ipAddress >> 24 & 0xff));
		}
		return localIp;
	}

}
