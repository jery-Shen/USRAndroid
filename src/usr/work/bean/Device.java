package usr.work.bean;

public class Device {
	private int ID;
	private int online;
	private int areaId;
	private int deviceId;
	private String deviceIp;
	private String updateTime;
	private int temp;				//温度显示值
	private int tempUpLimit;		//温度上限
	private int tempDownLimit;		//温度下限
	private int tempOff;			//温度偏差
	private int tempReally;			//温度实测
	private int workMode;			//系统工作模式
	private int airCount;			//换气次数
	private int inWindSpeed;		//进风变频速度
	private int outWindSpeed;		//出风变频速度
	private int hr;					//湿度显示值
	private int hrUpLimit;			//湿度上限
	private int hrDownLimit;		//湿度下限
	private int hrOff;				//湿度偏差
	private int hrReally;			//湿度实测		
	private int communicateFalse;	//通讯连续错误
	private int communicateTrue;	//通讯连续正确
	private int infoBar;			//系统信息栏
	private int stateSwitch;		//系统开关状态
	private int dp;					//压差显示值			
	private int dpUpLimit;			//压差上限
	private int dpDownLimit;		//压差下限
	private int dpOff;				//压差偏差
	private int dpReally;			//压差实测
	private int dpTarget;			//压差目标值
	private int akpMode;			//正负压模式
	private int workHour;			//累计工作小时
	private int workSecond;			//累计工作秒数
	private int converterMax;		//变频器连续最高
	private int converterMin;		//变频器连续最低
	private int converterModel;		//变频器型号选择
	private int cycleError;			//延周期检错
	private int alarmCycle;			//连续报警周期数
	private int tempAlarmClose;		//温度报警关闭
	private int hrAlarmClose;		//湿度报警关闭
	private int dpAlarmClose;		//压差报警关闭
	private int inWindAlarmClose;	//进风速度上限报警关闭
	private int airSpeed10;			//10次换气速度
	private int airSpeed12;			//12次换气速度
	private int airSpeed14;			//14次换气速度
	private int airSpeed16;			//16次换气速度
	private int airSpeed18;			//18次换气速度
	private int airSpeed20;			//20次换气速度
	private int airSpeed22;			//22次换气速度
	private int airSpeed24;			//24次换气速度
	private int airSpeed26;			//26次换气速度
	private int airSpeed28;			//28次换气速度
	private int airSpeed30;			//30次换气速度
	private int airSpeed35;			//35次换气速度
	private int airSpeed40;			//40次换气速度
	private int airSpeed45;			//45次换气速度
	private int airSpeed50;			//50次换气速度

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

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

	public String getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public int getTemp() {
		return temp;
	}

	public void setTemp(int temp) {
		this.temp = temp;
	}

	public int getTempUpLimit() {
		return tempUpLimit;
	}

	public void setTempUpLimit(int tempUpLimit) {
		this.tempUpLimit = tempUpLimit;
	}

	public int getTempDownLimit() {
		return tempDownLimit;
	}

	public void setTempDownLimit(int tempDownLimit) {
		this.tempDownLimit = tempDownLimit;
	}

	public int getTempOff() {
		return tempOff;
	}

	public void setTempOff(int tempOff) {
		this.tempOff = tempOff;
	}

	public int getTempReally() {
		return tempReally;
	}

	public void setTempReally(int tempReally) {
		this.tempReally = tempReally;
	}

	public int getWorkMode() {
		return workMode;
	}

	public void setWorkMode(int workMode) {
		this.workMode = workMode;
	}

	public int getAirCount() {
		return airCount;
	}

	public void setAirCount(int airCount) {
		this.airCount = airCount;
	}

	public int getInWindSpeed() {
		return inWindSpeed;
	}

	public void setInWindSpeed(int inWindSpeed) {
		this.inWindSpeed = inWindSpeed;
	}

	public int getOutWindSpeed() {
		return outWindSpeed;
	}

	public void setOutWindSpeed(int outWindSpeed) {
		this.outWindSpeed = outWindSpeed;
	}

	public int getHr() {
		return hr;
	}

	public void setHr(int hr) {
		this.hr = hr;
	}

	public int getHrUpLimit() {
		return hrUpLimit;
	}

	public void setHrUpLimit(int hrUpLimit) {
		this.hrUpLimit = hrUpLimit;
	}

	public int getHrDownLimit() {
		return hrDownLimit;
	}

	public void setHrDownLimit(int hrDownLimit) {
		this.hrDownLimit = hrDownLimit;
	}

	public int getHrOff() {
		return hrOff;
	}

	public void setHrOff(int hrOff) {
		this.hrOff = hrOff;
	}

	public int getHrReally() {
		return hrReally;
	}

	public void setHrReally(int hrReally) {
		this.hrReally = hrReally;
	}

	public int getCommunicateFalse() {
		return communicateFalse;
	}

	public void setCommunicateFalse(int communicateFalse) {
		this.communicateFalse = communicateFalse;
	}

	public int getCommunicateTrue() {
		return communicateTrue;
	}

	public void setCommunicateTrue(int communicateTrue) {
		this.communicateTrue = communicateTrue;
	}

	public int getInfoBar() {
		return infoBar;
	}

	public void setInfoBar(int infoBar) {
		this.infoBar = infoBar;
	}

	public int getStateSwitch() {
		return stateSwitch;
	}

	public void setStateSwitch(int stateSwitch) {
		this.stateSwitch = stateSwitch;
	}

	public int getDp() {
		return dp;
	}

	public void setDp(int dp) {
		this.dp = dp;
	}

	public int getDpUpLimit() {
		return dpUpLimit;
	}

	public void setDpUpLimit(int dpUpLimit) {
		this.dpUpLimit = dpUpLimit;
	}

	public int getDpDownLimit() {
		return dpDownLimit;
	}

	public void setDpDownLimit(int dpDownLimit) {
		this.dpDownLimit = dpDownLimit;
	}

	public int getDpOff() {
		return dpOff;
	}

	public void setDpOff(int dpOff) {
		this.dpOff = dpOff;
	}

	public int getDpReally() {
		return dpReally;
	}

	public void setDpReally(int dpReally) {
		this.dpReally = dpReally;
	}

	public int getDpTarget() {
		return dpTarget;
	}

	public void setDpTarget(int dpTarget) {
		this.dpTarget = dpTarget;
	}

	public int getAkpMode() {
		return akpMode;
	}

	public void setAkpMode(int akpMode) {
		this.akpMode = akpMode;
	}

	public int getWorkHour() {
		return workHour;
	}

	public void setWorkHour(int workHour) {
		this.workHour = workHour;
	}

	public int getWorkSecond() {
		return workSecond;
	}

	public void setWorkSecond(int workSecond) {
		this.workSecond = workSecond;
	}

	public int getConverterMax() {
		return converterMax;
	}

	public void setConverterMax(int converterMax) {
		this.converterMax = converterMax;
	}

	public int getConverterMin() {
		return converterMin;
	}

	public void setConverterMin(int converterMin) {
		this.converterMin = converterMin;
	}

	public int getConverterModel() {
		return converterModel;
	}

	public void setConverterModel(int converterModel) {
		this.converterModel = converterModel;
	}

	public int getCycleError() {
		return cycleError;
	}

	public void setCycleError(int cycleError) {
		this.cycleError = cycleError;
	}

	public int getAlarmCycle() {
		return alarmCycle;
	}

	public void setAlarmCycle(int alarmCycle) {
		this.alarmCycle = alarmCycle;
	}

	public int getTempAlarmClose() {
		return tempAlarmClose;
	}

	public void setTempAlarmClose(int tempAlarmClose) {
		this.tempAlarmClose = tempAlarmClose;
	}

	public int getHrAlarmClose() {
		return hrAlarmClose;
	}

	public void setHrAlarmClose(int hrAlarmClose) {
		this.hrAlarmClose = hrAlarmClose;
	}

	public int getDpAlarmClose() {
		return dpAlarmClose;
	}

	public void setDpAlarmClose(int dpAlarmClose) {
		this.dpAlarmClose = dpAlarmClose;
	}

	public int getInWindAlarmClose() {
		return inWindAlarmClose;
	}

	public void setInWindAlarmClose(int inWindAlarmClose) {
		this.inWindAlarmClose = inWindAlarmClose;
	}

	public int getAirSpeed10() {
		return airSpeed10;
	}

	public void setAirSpeed10(int airSpeed10) {
		this.airSpeed10 = airSpeed10;
	}

	public int getAirSpeed12() {
		return airSpeed12;
	}

	public void setAirSpeed12(int airSpeed12) {
		this.airSpeed12 = airSpeed12;
	}

	public int getAirSpeed14() {
		return airSpeed14;
	}

	public void setAirSpeed14(int airSpeed14) {
		this.airSpeed14 = airSpeed14;
	}

	public int getAirSpeed16() {
		return airSpeed16;
	}

	public void setAirSpeed16(int airSpeed16) {
		this.airSpeed16 = airSpeed16;
	}

	public int getAirSpeed18() {
		return airSpeed18;
	}

	public void setAirSpeed18(int airSpeed18) {
		this.airSpeed18 = airSpeed18;
	}

	public int getAirSpeed20() {
		return airSpeed20;
	}

	public void setAirSpeed20(int airSpeed20) {
		this.airSpeed20 = airSpeed20;
	}

	public int getAirSpeed22() {
		return airSpeed22;
	}

	public void setAirSpeed22(int airSpeed22) {
		this.airSpeed22 = airSpeed22;
	}

	public int getAirSpeed24() {
		return airSpeed24;
	}

	public void setAirSpeed24(int airSpeed24) {
		this.airSpeed24 = airSpeed24;
	}

	public int getAirSpeed26() {
		return airSpeed26;
	}

	public void setAirSpeed26(int airSpeed26) {
		this.airSpeed26 = airSpeed26;
	}

	public int getAirSpeed28() {
		return airSpeed28;
	}

	public void setAirSpeed28(int airSpeed28) {
		this.airSpeed28 = airSpeed28;
	}

	public int getAirSpeed30() {
		return airSpeed30;
	}

	public void setAirSpeed30(int airSpeed30) {
		this.airSpeed30 = airSpeed30;
	}

	public int getAirSpeed35() {
		return airSpeed35;
	}

	public void setAirSpeed35(int airSpeed35) {
		this.airSpeed35 = airSpeed35;
	}

	public int getAirSpeed40() {
		return airSpeed40;
	}

	public void setAirSpeed40(int airSpeed40) {
		this.airSpeed40 = airSpeed40;
	}

	public int getAirSpeed45() {
		return airSpeed45;
	}

	public void setAirSpeed45(int airSpeed45) {
		this.airSpeed45 = airSpeed45;
	}

	public int getAirSpeed50() {
		return airSpeed50;
	}

	public void setAirSpeed50(int airSpeed50) {
		this.airSpeed50 = airSpeed50;
	}

	@Override
	public String toString() {
		return " temp:" + temp + "\n tempUpLimit:" + tempUpLimit + "\n tempDownLimit:" + tempDownLimit + "\n"
				+ " hr:" + hr + "\n hrUpLimit:" + hrUpLimit + "\n hrDownLimit:" + hrDownLimit + "\n"
				+ " dp:" + dp + "\n dpUpLimit:" + dpUpLimit + "\n dpDownLimit:" + dpDownLimit + "\n";
	}

}
