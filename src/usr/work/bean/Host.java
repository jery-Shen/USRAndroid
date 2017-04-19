package usr.work.bean;

public class Host {
	private String ip;
	private int deviceId;
	private String mac;
	
	
	
	public Host(String ip, int deviceId, String mac) {
		super();
		this.ip = ip;
		this.deviceId = deviceId;
		this.mac = mac;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public boolean equals(Object obj) {
        if (obj instanceof Host) {
        	Host host = (Host) obj;
            
            return (mac.equals(host.mac));
        }
        return super.equals(obj);
    }
}
