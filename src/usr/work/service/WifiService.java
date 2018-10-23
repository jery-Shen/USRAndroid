package usr.work.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSON;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import usr.work.application.USRApplication;
import usr.work.bean.Device;
import usr.work.bean.DeviceSocket;
import usr.work.bean.Host;
import usr.work.bean.User;
import usr.work.client.Clients;

public class WifiService extends Service{
	
	public User user;
	private final Timer timer = new Timer();
	private TimerTask task = new TimerTask() {  
	    @Override  
	    public void run() {  
	    	Clients.getInstance().scanServer(1);
	    	synDeviceList();
	    }  
	}; 
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public void synDeviceList(){
		List<DeviceSocket> dsockets = Clients.getInstance().dsockets;
		
		synchronized (dsockets){
			((USRApplication)getApplicationContext()).deviceList.clear();
			for (DeviceSocket deviceSocket : dsockets){
				Device device = deviceSocket.getDevice();
				if(device!=null){
					if(device.getTemp()>50){
						device.setTemp(device.getTemp()/10);
						device.setTempUpLimit(device.getTempUpLimit()/10);
						device.setTempDownLimit(device.getTempDownLimit()/10);
						device.setHr(device.getHr()/10);
						device.setHrUpLimit(device.getHrUpLimit()/10);
						device.setHrDownLimit(device.getHrDownLimit()/10);
						device.setDp(device.getDp()/10);
						device.setDpUpLimit(device.getDpUpLimit()/10);
						device.setDpDownLimit(device.getDpDownLimit()/10);
					}
					((USRApplication)getApplicationContext()).deviceList.add(device);
				}
			}
		}
	}
		
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("syj", "onStartCommand");
		SharedPreferences preferences = getSharedPreferences("set", 0);
		String userStr = preferences.getString("user", "");
		String hostListStr = preferences.getString("hostList", "[]");
		user = JSON.parseObject(userStr, User.class);
		List<Host> hostList = JSON.parseArray(hostListStr, Host.class);
		Clients.getInstance().setHostList(hostList);
		Clients.getInstance().initUdp(intent.getStringExtra("wifiIp"));
		Clients.getInstance().scanAndConnect();
		timer.schedule(task, 0, 1000);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		timer.cancel();
		Clients.getInstance().shutDown();
		USRApplication.getApplication().deviceList.clear();
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
