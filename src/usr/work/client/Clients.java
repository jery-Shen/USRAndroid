package usr.work.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import android.util.Log;
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
//		for(Host host:this.hostList){
//			Log.i("syj", "deviceId:"+host.getDeviceId());
//			Log.i("syj", "deviceIp:"+host.getIp());
//		}
		
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
		Log.i("syj", ipHead+"255");
		//InetAddress address = InetAddress.getByName("10.10.13.245");
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
					Log.i("syj", msg);
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
						//System.out.println("unReceiveTime:"+deviceSocket.getUnReceiveTime());
						deviceSocket.setUnReceiveTime(deviceSocket.getUnReceiveTime()-1);
						if(deviceSocket.getUnReceiveTime()<0 && !deviceSocket.isSending()){ //如果在线服务器不在扫描则扫描
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
	
	public boolean sendUpdate(int deviceId,List<byte[]> sendQueue){
		DeviceSocket deviceSocket = getDeviceSocket(deviceId);
		if(deviceSocket!=null){
			deviceSocket.setSending(true);
			sleepLong();
			if(deviceSocket.getUnReceiveTime()<0){
				for(byte[] bytes:sendQueue){
					byte[] crcBytes = CRC.getCRC(bytes);
					System.out.println(new Date().toLocaleString()+",deviceId:"+deviceId+",send:"+Hex.printHexString(crcBytes));
					deviceSocket = getDeviceSocket(deviceId);
					sendOne(crcBytes, deviceSocket);
					sleep();
				}
				sleep();
				deviceSocket.setSending(false);
				return true;
			}else{
				deviceSocket.setSending(false);
				return false;
			}
		}
		return false;
	}
	
	private DeviceSocket getDeviceSocket(int deviceId){
		synchronized (dsockets) {
			if (dsockets.size() > 0) {
				for (DeviceSocket deviceSocket : dsockets) {	
					if(deviceSocket.getDeviceId()==deviceId){
						return deviceSocket;
					}
				}
			}
		}
		return null;
	}
	
	private void sleep(){
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sleepLong(){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean updateDevice(int deviceId, Map<String, Object> paramMap){
		
		List<byte[]> sendQueue = new ArrayList<byte[]>();
		for (Map.Entry<String, Object> entry : paramMap.entrySet()) { 
			if(entry.getKey().equals("tempUpLimit")){
				int tempUpLimit = Integer.parseInt(entry.getValue().toString());
				byte[] bytes = new byte[]{(byte) deviceId,0x06,0x03,0x79,0x00,(byte) tempUpLimit};
				sendQueue.add(bytes);
			}
			if(entry.getKey().equals("tempDownLimit")){
				int tempDownLimit = Integer.parseInt(entry.getValue().toString());
				byte[] bytes = new byte[]{(byte) deviceId,0x06,0x03,0x7a,0x00,(byte) tempDownLimit};
				sendQueue.add(bytes);
			}
			if(entry.getKey().equals("hrUpLimit")){
				int hrUpLimit = Integer.parseInt(entry.getValue().toString());
				byte[] bytes = new byte[]{(byte) deviceId,0x06,0x03,0x7b,0x00,(byte) hrUpLimit};
				sendQueue.add(bytes);
			}
			if(entry.getKey().equals("hrDownLimit")){
				int hrDownLimit = Integer.parseInt(entry.getValue().toString());
				byte[] bytes = new byte[]{(byte) deviceId,0x06,0x03,0x7c,0x00,(byte) hrDownLimit};
				sendQueue.add(bytes);
			}
			if(entry.getKey().equals("dpUpLimit")){
				int dpUpLimit = Integer.parseInt(entry.getValue().toString());
				byte[] bytes = new byte[]{(byte) deviceId,0x06,0x03,0x7d,0x00,(byte) dpUpLimit};
				sendQueue.add(bytes);
			}
			if(entry.getKey().equals("dpDownLimit")){
				int dpDownLimit = Integer.parseInt(entry.getValue().toString());
				byte[] bytes = new byte[]{(byte) deviceId,0x06,0x03,0x7e,0x00,(byte) dpDownLimit};
				sendQueue.add(bytes);
			}
			if(entry.getKey().equals("tempAlarmClose")){
				int tempAlarmClose = Integer.parseInt(entry.getValue().toString());
				byte[] bytes = new byte[]{(byte) deviceId,0x06,0x03,0x7f,0x00,(byte) tempAlarmClose};
				sendQueue.add(bytes);
			}
			if(entry.getKey().equals("hrAlarmClose")){
				int hrAlarmClose = Integer.parseInt(entry.getValue().toString());
				byte[] bytes = new byte[]{(byte) deviceId,0x06,0x03,(byte) 0x80,0x00,(byte) hrAlarmClose};
				sendQueue.add(bytes);
			}
			if(entry.getKey().equals("dpAlarmClose")){
				int dpAlarmClose = Integer.parseInt(entry.getValue().toString());
				byte[] bytes = new byte[]{(byte) deviceId,0x06,0x03,(byte) 0x81,0x00,(byte) dpAlarmClose};
				sendQueue.add(bytes);
			}
			if(entry.getKey().equals("inWindAlarmClose")){
				int inWindAlarmClose = Integer.parseInt(entry.getValue().toString());
				byte[] bytes = new byte[]{(byte) deviceId,0x06,0x03,(byte) 0x82,0x00,(byte) inWindAlarmClose};
				sendQueue.add(bytes);
			}
			
	    } 
		return sendUpdate(deviceId, sendQueue);
	}
	
	public boolean hasDevice(){
		if(dsockets.size()>0){
			return true;
		}
		return false;
	}

}
