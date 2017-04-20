package usr.work.service;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import usr.work.application.USRApplication;
import usr.work.bean.Device;
import usr.work.bean.User;
import usr.work.utils.HttpUtil;

public class OnlineService extends Service{

	public User user;
	
	private String mUrl = HttpUtil.URL_PRE+"GetDeviceList";
	private final Timer timer = new Timer();
	private TimerTask task = new TimerTask() {  
	    @Override  
	    public void run() {  
	    	String url = mUrl;
	    	Map<String, String> map =  HttpUtil.getSign(user);
	    	if(user.getAreaId()>0){
	    		url = url + "?areaId="+user.getAreaId();
	    		url = url + "&token=" + map.get("token")+"&timestamp="+map.get("timestamp")+"&sign="+map.get("sign");
	    	}else{
	    		url = url + "?token=" + map.get("token")+"&timestamp="+map.get("timestamp")+"&sign="+map.get("sign");
	    	}

	    	String content = HttpUtil.getStrFromUrl(url);

	    	if(!content.equals("")){
	    		JSONObject jsonObject =  JSON.parseObject(content);
	    		if(jsonObject.getIntValue("status")==200){
	    			JSONArray jDevices = jsonObject.getJSONArray("result");
	    			((USRApplication)getApplicationContext()).deviceList.clear();
			    	for(int i=0;i<jDevices.size();i++){
			    		Device device = jDevices.getObject(i, Device.class);
			    		if(device.getOnline()==1){
			    			((USRApplication)getApplicationContext()).deviceList.add(device);
			    		}
			    	}
			        
	    		}else{
	    			Log.i("syj", jsonObject.getString("error"));
	    		}
	    	}else{
	    		
	    	}
	    }  
	}; 
	
	
	@Override  
    public void onCreate() {  
        super.onCreate();  
        SharedPreferences preferences = getSharedPreferences("set", 0);
		String userStr = preferences.getString("user", "");
		user = JSON.parseObject(userStr, User.class);
    }  
  
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        timer.schedule(task, 0, 2000);
        return super.onStartCommand(intent, flags, startId); 
        
        
    }  
      
    @Override  
    public void onDestroy() {  
        timer.cancel();
    }  
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
