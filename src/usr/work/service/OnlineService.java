package usr.work.service;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import usr.work.R;
import usr.work.application.USRApplication;
import usr.work.bean.Device;
import usr.work.bean.User;
import usr.work.utils.HttpUtil;
import usr.work.utils.NotificationUtil;

public class OnlineService extends Service {

	public User user;

	private String mUrl = HttpUtil.URL_PRE + "GetDeviceList";

	int timerCount;

	private final Timer timer = new Timer();
	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			timerCount++;

			String url = mUrl;
			Map<String, String> map = HttpUtil.getSign(user);
			if (user.getAreaId() > 0) {
				url = url + "?areaId=" + user.getAreaId();
				url = url + "&token=" + map.get("token") + "&timestamp=" + map.get("timestamp") + "&sign="
						+ map.get("sign");
			} else {
				url = url + "?token=" + map.get("token") + "&timestamp=" + map.get("timestamp") + "&sign="
						+ map.get("sign");
			}
			String content = HttpUtil.getStrFromUrl(url);
			// Log.i("syj", content);
			if (!content.equals("")) {
				JSONObject jsonObject = JSON.parseObject(content);
				if (jsonObject.getIntValue("status") == 200) {

					JSONArray jDevices = jsonObject.getJSONArray("result");
					for (int i = 0; i < jDevices.size(); i++) {
						Device device = jDevices.getObject(i, Device.class);
						Device localDevice = getDeviceById(device.getDeviceId());
						if (localDevice == null) {
							((USRApplication) getApplicationContext()).deviceList.add(device);
						} else {
							int index = ((USRApplication) getApplicationContext()).deviceList.indexOf(localDevice);
							((USRApplication) getApplicationContext()).deviceList.set(index, device);
							if (device.getInfoBar() != localDevice.getInfoBar() && device.getInfoBar() > 1) {
								String alarmMsg = stringOfInfoBar(device.getInfoBar());
								switch (device.getInfoBar()) {
								case 4:
									alarmMsg += "，当前" + device.getTemp() + "大于上限" + device.getTempUpLimit();
									break;
								case 5:
									alarmMsg += "，当前" + device.getTemp() + "小于下限" + device.getTempDownLimit();
									break;
								case 6:
									alarmMsg += "，当前" + device.getHr() + "大于上限" + device.getHrUpLimit();
									break;
								case 7:
									alarmMsg += "，当前" + device.getHr() + "小于下限" + device.getHrDownLimit();
									break;
								case 8:
									alarmMsg += "，当前" + device.getDp() + "大于上限" + device.getDpUpLimit();
									break;
								case 9:
									alarmMsg += "，当前" + device.getDp() + "小于下限" + device.getDpDownLimit();
									break;
								default:
									break;
								}

								NotificationUtil.wakeLock(OnlineService.this);
								NotificationUtil.pushAlarm(OnlineService.this, device.getDeviceId(), alarmMsg, 0);

							}
						}
					}
				} else {
					Log.i("syj", jsonObject.getString("error"));
				}
			} else {

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

	protected Device getDeviceById(int deviceId) {
		List<Device> deviceList = ((USRApplication) getApplicationContext()).deviceList;
		for (Device d : deviceList) {
			if (d.getDeviceId() == deviceId) {
				return d;
			}
		}
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		timer.schedule(task, 0, 2000);
		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public void onDestroy() {
		timer.cancel();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String stringOfInfoBar(int infoBar) {
		String infoBarStr = "";
		switch (infoBar) {
		case 0:
			infoBarStr = "待机状态，按开启键启动";
			break;
		case 1:
			infoBarStr = "工作正常，按关闭键停止";
			break;
		case 2:
			infoBarStr = "温度过低";
			break;
		case 3:
			infoBarStr = "断电报警";
			break;
		case 4:
			infoBarStr = "温度超高";
			break;
		case 5:
			infoBarStr = "温度过低";
			break;
		case 6:
			infoBarStr = "湿度超高";
			break;
		case 7:
			infoBarStr = "湿度过低";
			break;
		case 8:
			infoBarStr = "压差过高";
			break;
		case 9:
			infoBarStr = "压差过低";
			break;
		case 10:
			infoBarStr = "模拟量采集通讯故障";
			break;
		case 11:
			infoBarStr = "进风自动调节上限";
			break;
		case 12:
			infoBarStr = "进风自动调节下限";
			break;
		case 13:
			infoBarStr = "模拟量采集通讯故障";
			break;

		}
		return infoBarStr;
	}

}
