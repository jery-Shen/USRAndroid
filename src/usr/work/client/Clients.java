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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import usr.work.bean.DeviceSocket;
import usr.work.bean.Host;
import usr.work.utils.CRC;

public class Clients {
	
	
    private static Clients instance = null;  
    public static synchronized Clients getInstance() {  
        if (instance == null) {  
            instance = new Clients();  
        }  
        return instance;  
    }  
	
	public List<Host> hostList;

	public List<DeviceSocket> dsockets;
	
	public JSONArray macMap;
	
	public boolean initUdpSuccess;
	
	private String wifiIp;
	
	DatagramSocket ds;
	
	
	
	private Clients() {
		hostList = new ArrayList<Host>();
		dsockets = new ArrayList<DeviceSocket>();
		macMap = new JSONArray();
		
//		hostList.add(new Host("192.168.0.104", 1, "A"));
//		hostList.add(new Host("192.168.0.105", 3, "B"));
		
		JSONObject jsonObject1 = new JSONObject();
		jsonObject1.put("areaId", 1);
		jsonObject1.put("deviceId", 1);
		jsonObject1.put("mac", "D8B04CB472EC");
		
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("areaId", 1);
		jsonObject2.put("deviceId", 2);
		jsonObject2.put("mac", "D8B04CB47B9C");
		
		JSONObject jsonObject3 = new JSONObject();
		jsonObject3.put("areaId", 1);
		jsonObject3.put("deviceId", 3);
		jsonObject3.put("mac", "D8B04CB485E0");
		
		macMap.add(jsonObject1);
		macMap.add(jsonObject2);
		macMap.add(jsonObject3);
	}
	
	public void initUdp(String wifiIp){
		try{
			this.wifiIp = wifiIp;
			ds = new DatagramSocket();
			new receiveScanThread().start();
			scanServer(5);
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
	
	
	public void scan() throws Exception{
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
					int deviceId = getDeviceIdByMac(mac);
					
					if(deviceId!=0){
						Host host = new Host(ip, deviceId, mac);
						if(!hostList.contains(host)){
							hostList.add(host);
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				ds.close();
			}
			
			
		}
	}
	
	public int getDeviceIdByMac(String mac){
		for(Object obj:macMap){
			JSONObject jo = (JSONObject) obj;
			if(mac.equals(jo.getString("mac"))){
				return jo.getIntValue("deviceId");
			}
		}
		return 0;
	}
	
	public boolean isConnected(int deviceId){
		for(DeviceSocket deviceSocket: dsockets){
			if(deviceSocket.getDeviceId()==deviceId){
				return true;
			}
		}
		return false;
	}

	public void connectServers() throws UnknownHostException, IOException {
		for(Host host:hostList){
			if(!isConnected(host.getDeviceId())){
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
	

	public void scanServer(int scanNum) {

		new Timer().schedule(new TimerTask() {
			public void run() {
				synchronized (dsockets) {
					System.out.println("current connects:"+dsockets.size());
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
		}, 2000, 2000);
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
