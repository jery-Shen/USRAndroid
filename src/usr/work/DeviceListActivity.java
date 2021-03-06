package usr.work;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import usr.work.application.USRApplication;
import usr.work.bean.Device;
import usr.work.service.OnlineService;
import usr.work.utils.NotificationUtil;
import usr.work.utils.ViewUtil;

public class DeviceListActivity extends Activity {

	ListView listView;
	
	public List<Device> mDataList;
	
	private PopupWindow popupwindow;
	private ProgressBar loading;
	private ImageView rightBtn;

	private MyAdapter myAdapter;
	
	private long firstExitTime;
	
	DecimalFormat df = new DecimalFormat("0.00");
	private final Timer timer = new Timer();;
	private TimerTask task = new TimerTask() {  
	    @Override  
	    public void run() {  
	    	
	    	Message message = new Message();
	    	message.what = 6;
	    	handler.sendMessage(message);
	    }
	}; 
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==6){
				mDataList = USRApplication.getApplication().deviceList;
				if(mDataList!=null&&mDataList.size()>0){
					if(myAdapter==null){
						myAdapter = new MyAdapter(DeviceListActivity.this, R.id.listview, mDataList);
						listView.setAdapter(myAdapter);
						loading.setVisibility(View.INVISIBLE);
					}else{
						myAdapter.notifyDataSetChanged();
					}
				}
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
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
				Device info = mDataList.get(position);
				if(info.getOnline()==1){
					Intent intent = new Intent(DeviceListActivity.this,DeviceDetailActivity.class);
					intent.putExtra("areaId",info.getAreaId());
					intent.putExtra("deviceId",info.getDeviceId());
					startActivity(intent);
				}
			}
		});
		
		initMenu();
		rightBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupwindow.isShowing()) {
					popupwindow.dismiss();
				} else {
					popupwindow.showAsDropDown(v, 0, ViewUtil.dpToPx(getResources(),9));
				}
			}
		});
		
		startService(new Intent(this, OnlineService.class));

		timer.schedule(task, 1000, 2000);
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
		popup_layout.findViewById(R.id.switch_mode).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//Toast.makeText(DeviceListActivity.this, "开发中", Toast.LENGTH_SHORT).show();
				popupwindow.dismiss();
				Intent intent = new Intent(DeviceListActivity.this,DeviceListWifiActivity.class);
	            startActivity(intent);
				DeviceListActivity.this.finish();
				SharedPreferences preferences = getSharedPreferences("set", 0);
				Editor editor = preferences.edit();
				editor.putInt("mode", 1);
				editor.commit();
			}
		});
		popup_layout.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				popupwindow.dismiss();
				myAdapter = null;
				loading.setVisibility(View.VISIBLE);
			}
		});
		popup_layout.findViewById(R.id.instructe).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				popupwindow.dismiss();
				Intent intent = new Intent(DeviceListActivity.this,WebActivity.class);
	            intent.putExtra("title", "帮助系统");
	            intent.putExtra("url", "file:///android_asset/instructe.html");
	            startActivity(intent);
			}
		});
		popup_layout.findViewById(R.id.set).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				popupwindow.dismiss();
				
				Intent intent = new Intent(DeviceListActivity.this, SetActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		

	}

	@Override
	public void onBackPressed() {// 覆盖返回键
		long curTime = System.currentTimeMillis();
		if (curTime - firstExitTime < 1000) {// 两次按返回键的时间小于2秒就退出应用
			finish();
		} else {
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			firstExitTime = curTime;
		}
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		NotificationUtil.cancel(this);
		
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences preferences = getSharedPreferences("set", 0);
		String userStr = preferences.getString("user", "");
		if(userStr.equals("")){
			Intent intent = new Intent(DeviceListActivity.this, LoginActivity.class);
			startActivity(intent);
			DeviceListActivity.this.finish();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	
	
	@Override
	protected void onDestroy() {
		Log.i("syj", "DeviceListActivity:onDestroy");
		timer.cancel();
		stopService(new Intent(this, OnlineService.class));
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
				holder.info.setBackground(getResources().getDrawable(R.drawable.layer_border_disable));
			}else if(infoBar==1){
				holder.info.setTextColor(Color.parseColor("#1aad19"));
				holder.info.setBackground(getResources().getDrawable(R.drawable.layer_border_success));
			}else{
				holder.info.setTextColor(Color.parseColor("#e64340"));
				holder.info.setBackground(getResources().getDrawable(R.drawable.layer_border_warn));
			}
			if(info.getOnline()==0){
				holder.info.setTextColor(Color.parseColor("#cccccc"));
				holder.info.setBackground(getResources().getDrawable(R.drawable.layer_border_disable));
				holder.info.setText("失去连接");
			}
			holder.title.setText("智控"+info.getDeviceId());
			holder.content.setText("温度:"+info.getTemp()+"，湿度:"+info.getHr()+"，压差:"+info.getDp());
			holder.des.setText("换气次数:"+info.getAirCount()+"，进风速度:"+df.format(info.getInWindSpeed()/100)+"，目标压差:"+info.getDpTarget());
			holder.time.setText("");
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
