package usr.work.bean;

import java.io.DataOutputStream;
import java.net.Socket;

public class DeviceSocket {
	private int areaId;
	private int deviceId;
	private Socket socket;
	private DataOutputStream dataOut;
	private Device device;
	private boolean sending;
	private int UnReceiveTime;
	private int receiveCount;
	
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public DataOutputStream getDataOut() {
		return dataOut;
	}
	public void setDataOut(DataOutputStream dataOut) {
		this.dataOut = dataOut;
	}
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public int getUnReceiveTime() {
		return UnReceiveTime;
	}
	public void setUnReceiveTime(int unReceiveTime) {
		UnReceiveTime = unReceiveTime;
	}
	public boolean isSending() {
		return sending;
	}
	public void setSending(boolean sending) {
		this.sending = sending;
	}
	public int getReceiveCount() {
		return receiveCount;
	}
	public void setReceiveCount(int receiveCount) {
		this.receiveCount = receiveCount;
	}
	
	
	
}
