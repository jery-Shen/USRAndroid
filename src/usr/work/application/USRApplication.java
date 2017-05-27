package usr.work.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import usr.work.bean.Device;

public class USRApplication extends Application{
	
	private static USRApplication application;
	
	public List<Device> deviceList = new ArrayList<Device>();;
	
	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		
	}
	
	public static USRApplication getApplication(){
		return application;
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
