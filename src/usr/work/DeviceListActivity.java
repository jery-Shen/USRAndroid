package usr.work;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import usr.work.bean.Device;
import usr.work.bean.User;
import usr.work.utils.HttpUtil;
import usr.work.utils.ViewUtil;

public class DeviceListActivity extends Activity {

	ListView listView;
	public static User user;
	public static List<Device> mDataList;
	private PopupWindow popupwindow;
	int areaId;
	private ProgressBar loading;
	ImageView rightBtn;
	String mUrl = HttpUtil.URL_PRE+"GetDeviceList";
	
	DecimalFormat df = new DecimalFormat("#.00");
	private final Timer timer = new Timer();
	private TimerTask task = new TimerTask() {  
	    @Override  
	    public void run() {  
	    	String url = mUrl;
	    	Map<String, String> map =  HttpUtil.getSign(DeviceListActivity.this);
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
			    	mDataList.clear();
			    	for(int i=0;i<jDevices.size();i++){
			    		Device device = jDevices.getObject(i, Device.class);
			    		if(device.getOnline()==1){
			    			mDataList.add(device);
			    		}
			    	}
			        Message message = new Message();  
			        message.what = 6;  
			        handler.sendMessage(message);  
	    		}else{
	    			Log.i("syj", jsonObject.getString("error"));
	    		}
	    	}else{
	    		Message message = new Message();  
		        message.what = 11;  
		        handler.sendMessage(message); 
	    	}
	    }  
	}; 
	
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==6){
				if(mDataList!=null&&mDataList.size()>0){
					loading.setVisibility(View.INVISIBLE);
					listView.setAdapter(new MyAdapter(DeviceListActivity.this, R.id.listview, mDataList));
				}
				

			}else if(msg.what==11){
				//Toast.makeText(DeviceListActivity.this, "网络连接错误",Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_device_list);
		
		loading = (ProgressBar)findViewById(R.id.loading);
		listView = (ListView) findViewById(R.id.listview);
		rightBtn = (ImageView) findViewById(R.id.right_btn);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.wv_menu));
		SharedPreferences preferences = getSharedPreferences("set", 0);
		String userStr = preferences.getString("user", "");
		user = JSON.parseObject(userStr, User.class);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent(DeviceListActivity.this,DeviceDetailActivity.class);
				Device info = mDataList.get(position);
				intent.putExtra("areaId",info.getAreaId());
				intent.putExtra("deviceId",info.getDeviceId());
				
				startActivity(intent);
			}
		});
		
		initMenu();
		rightBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupwindow.isShowing()) {
					popupwindow.dismiss();
				} else {
					popupwindow.showAsDropDown(v, 0, ViewUtil.dpToPx(getResources(),6));
				}
			}
		});
		
		mDataList = new ArrayList<Device>();
		
		timer.schedule(task, 0, 20000);
		
		
	}
	
	
	private void initMenu() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		RelativeLayout popup_layout = (RelativeLayout) inflater.inflate(R.layout.popup_online_menu, null);

		popupwindow = new PopupWindow(popup_layout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		popup_layout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (popupwindow.isShowing()) {
					popupwindow.dismiss();
				}
				return false;
			}
		});
		popup_layout.findViewById(R.id.switch_wifi).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(DeviceListActivity.this, "开发中", Toast.LENGTH_SHORT).show();
				popupwindow.dismiss();
				
			}
		});
		popup_layout.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(DeviceListActivity.this, "开发中", Toast.LENGTH_SHORT).show();
				popupwindow.dismiss();
				
			}
		});
		popup_layout.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SharedPreferences preferences = getSharedPreferences("set", 0);
				Editor editor = preferences.edit();
				editor.putString("user", "");
				editor.commit();
				popupwindow.dismiss();
				Intent intent = new Intent(DeviceListActivity.this, LoginActivity.class);
				startActivity(intent);
				DeviceListActivity.this.finish();
			}
		});
		

	}
	
	@Override
	protected void onDestroy() {
		timer.cancel();
		super.onDestroy();
	}
	
	
	
	class MyAdapter extends ArrayAdapter<Device> {
		public MyAdapter(Context context, int resource, List<Device> objects) {
			super(context, resource, objects);

		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			final Device info = getItem(position);
			final ViewHolder holder;
			if (null == row) {
				LayoutInflater inflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.list_item, parent, false);
				holder = new ViewHolder();
				holder.content = (TextView) row.findViewById(R.id.content);
				holder.title = (TextView) row.findViewById(R.id.title);
				holder.time = (TextView) row.findViewById(R.id.time);
				holder.info = (TextView) row.findViewById(R.id.info);
				holder.des = (TextView) row.findViewById(R.id.des);
				row.setTag(holder);
			} else {
				holder = (ViewHolder) row.getTag();
			}
			int infoBar = info.getInfoBar();
			holder.info.setText(stringOfInfoBar(infoBar));
			if(infoBar==0){
				holder.info.setTextColor(Color.parseColor("#cccccc"));
			}else if(infoBar==1){
				holder.info.setTextColor(Color.parseColor("#1aad19"));
			}else{
				holder.info.setTextColor(Color.parseColor("#e64340"));
			}
			holder.title.setText("设备"+info.getDeviceId());
			holder.content.setText("温度:"+info.getTemp()+"，湿度:"+info.getHr()+"，压差:"+info.getDp());
			holder.des.setText("换气期数:"+info.getAirCount()+"，进风速度:"+df.format(info.getInWindSpeed()/100)+"，目标压差:"+info.getDpTarget()+"，控制模式:"+(info.getWorkMode()==0?"手动":"自动"));
			holder.time.setText(info.getUpdateTime());
			return row;
		}
	}
	
	class ViewHolder{
		public TextView title;
		public TextView content;
		public TextView time;
		public TextView info;
		public TextView des;
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
