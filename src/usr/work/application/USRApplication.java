package usr.work.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import usr.work.bean.Device;

public class USRApplication extends Application{
	
	
	
	public List<Device> deviceList = new ArrayList<Device>();;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
	}
	
	public static USRApplication getApplication(Context context){
		return (USRApplication) context.getApplicationContext();
	}
	
	public Device getDevice(int deviceId) {
		if (deviceList!=null && deviceList.size() > 0) {
			for (Device device : deviceList) {
				if (device.getDeviceId() == deviceId) {
					return device;
				}
			}
		}
		return null;
	}
	
	public List<Device> getDeviceList() {
		List<Device> devices = new ArrayList<Device>();
		if (deviceList!=null && deviceList.size() > 0) {
			for (Device device : deviceList) {
				devices.add(device);
			}
		}
		return devices;
	}

	
}
