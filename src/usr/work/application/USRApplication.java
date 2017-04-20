package usr.work.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.util.Log;
import usr.work.bean.Device;

public class USRApplication extends Application{
	
	public List<Device> deviceList = new ArrayList<Device>();
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("syj", "USRApplication");
	}
}
