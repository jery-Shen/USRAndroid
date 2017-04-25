package usr.work.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import android.util.Log;
import usr.work.bean.Device;
import usr.work.bean.DeviceSocket;
import usr.work.bean.Host;
import usr.work.utils.CRC;
import usr.work.utils.Hex;

public class Clients {
	
	
    private static Clients instance = null;  
    public static synchronized Clients getInstance() {  
        if (instance == null) {  
            instance = new Clients();  
        }  
        return instance;  
    }  
	
	public List<Host> hostList;

	public List<DeviceSocket> dsockets = new ArrayList<DeviceSocket>();;
	
	public JSONArray macMap;
	
	public boolean initUdpSuccess;
	
	private String wifiIp;
	
	DatagramSocket ds;
	
	
	
	private Clients() {
	
	}
	
	public void setHostList(List<Host> hostList){
		if(this.hostList==null||this.hostList.size()==0){
			this.hostList = hostList;
		}else{
			for(Host host:hostList){
				boolean exist = false;
				for(Host host2:this.hostList){
					if(host2.getDeviceId()==host.getDeviceId()){
						exist = true;
					}
				}
				if(!exist){
					this.hostList.add(host);
				}
			}
		}
		Log.i("syj", JSON.toJSONString(this.hostList));
		for(Host host:this.hostList){
			Log.i("syj", "deviceId:"+host.getDeviceId());
			Log.i("syj", "deviceIp:"+host.getIp());
		}
		
	}
	
	public void initUdp(String wifiIp){
		try{
			this.wifiIp = wifiIp;
			ds = new DatagramSocket();
			new receiveScanThread().start();
			initUdpSuccess = true;
		} catch (Exception e) {
			initUdpSuccess = false;
			e.printStackTrace();
		}
		
	}
	
	public void scanAndConnect(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					scan();
					Thread.sleep(500);
					connectServers();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
	}
	
	
	private void scan() throws Exception{
		byte[] bytes = "HF-A11ASSISTHREAD".getBytes();
		
		int lastPoint = wifiIp.lastIndexOf('.');
		String ipHead = wifiIp.substring(0, ++lastPoint);
		InetAddress address = InetAddress.getByName(ipHead+"255");
		DatagramPacket sendDp = new DatagramPacket(bytes,bytes.length,address,48899);
		ds.send(sendDp); 
	}
	
	class receiveScanThread extends Thread{
		@Override
		public void run() {
			
			try {
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				while(true){
					ds.receive(receivePacket);
					String msg = new String(receiveData).trim();
					System.out.println(msg);
					String ip = msg.split(",")[0];
					String mac = msg.split(",")[1];
					for(Host host:hostList){
						if(host.getMac().equals(mac)){
							host.setIp(ip);
							break;
						}
					}
				}
			} catch (Exception e) {
				
			} finally {
				ds.close();
			}
			
			
		}
	}
	
	public void shutDown(){
		ds.close();
		for (DeviceSocket deviceSocket : dsockets) {
			Socket socket = deviceSocket.getSocket();
			if(!socket.isClosed()){
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		dsockets.clear();
		hostList.clear();
	}
	

	
	

	public void connectServers() throws UnknownHostException, IOException {
		for(Host host:hostList){
			if(!isConnected(host.getDeviceId())&&host.getIp()!=null){
				
				Socket socket = new Socket(host.getIp(), 8090);
				DeviceSocket deviceSocket = new DeviceSocket();
				deviceSocket.setSocket(socket);
				deviceSocket.setDataOut(new DataOutputStream(socket.getOutputStream()));
				deviceSocket.setDeviceId(host.getDeviceId());
				deviceSocket.setUnReceiveTime(1);
				dsockets.add(deviceSocket);
				ClientThread clientThread = new ClientThread(deviceSocket, dsockets);
				clientThread.start();
				System.out.println(socket);
			}
		}
	}
	
	public boolean isConnected(int deviceId){
		for(DeviceSocket deviceSocket: dsockets){
			if(deviceSocket.getDeviceId()==deviceId){
				return true;
			}
		}
		return false;
	}
	

	

	public void scanServer(int scanNum) {
		synchronized (dsockets) {
			//System.out.println("current connects:"+dsockets.size());
			if (dsockets.size() > 0) {
				for (DeviceSocket deviceSocket : dsockets) {						
					int deviceId = deviceSocket.getDeviceId();
					if(deviceId!=0){
						deviceSocket.setUnReceiveTime(deviceSocket.getUnReceiveTime()-1);
						if(deviceSocket.getUnReceiveTime()<0){
							byte[] bytes = new byte[] { (byte) deviceId, 0x03, 0x02, 0x58, 0x00, 0x64 };
							byte[] crcBytes = CRC.getCRC(bytes);
							sendOne(crcBytes, deviceSocket);
							//System.out.println(Hex.printHexString(bytes));
						}
					}
				}
			}
		}
	}

	public void sendOne(byte[] bytes, DeviceSocket deviceSocket) {
		try {
			deviceSocket.getDataOut().write(bytes);
			deviceSocket.getDataOut().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
