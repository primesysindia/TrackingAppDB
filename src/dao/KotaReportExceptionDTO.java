package dao;

public class KotaReportExceptionDTO {
	int timestamp,tripNo;
	String	parentId,sectionNmae,device,deviceName,startBatteryStatus,endBatteryStatus,startBeat,
	expectedStartBeat,endBeat,expectedEndBeat,maxSpeed,kmCover;
	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	public int getTripNo() {
		return tripNo;
	}
	public void setTripNo(int tripNo) {
		this.tripNo = tripNo;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getSectionNmae() {
		return sectionNmae;
	}
	public void setSectionNmae(String sectionNmae) {
		this.sectionNmae = sectionNmae;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getStartBatteryStatus() {
		return startBatteryStatus;
	}
	public void setStartBatteryStatus(String startBatteryStatus) {
		this.startBatteryStatus = startBatteryStatus;
	}
	public String getEndBatteryStatus() {
		return endBatteryStatus;
	}
	public void setEndBatteryStatus(String endBatteryStatus) {
		this.endBatteryStatus = endBatteryStatus;
	}
	public String getStartBeat() {
		return startBeat;
	}
	public void setStartBeat(String startBeat) {
		this.startBeat = startBeat;
	}
	public String getExpectedStartBeat() {
		return expectedStartBeat;
	}
	public void setExpectedStartBeat(String expectedStartBeat) {
		this.expectedStartBeat = expectedStartBeat;
	}
	public String getEndBeat() {
		return endBeat;
	}
	public void setEndBeat(String endBeat) {
		this.endBeat = endBeat;
	}
	public String getExpectedEndBeat() {
		return expectedEndBeat;
	}
	public void setExpectedEndBeat(String expectedEndBeat) {
		this.expectedEndBeat = expectedEndBeat;
	}
	public String getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(String maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public String getKmCover() {
		return kmCover;
	}
	public void setKmCover(String kmCover) {
		this.kmCover = kmCover;
	}
	
	
}
