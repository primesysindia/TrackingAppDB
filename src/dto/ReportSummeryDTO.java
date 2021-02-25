package dto;

public class ReportSummeryDTO {
	String date,deviceName,allocatedStartTime,actualStartTime,allocatedEndTime,actualEndTime,remarks,expectedStartKm,actuatStartKm,expectedEndKm,actuatEndKm;
	int maxSpeed,avgSpeed;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getAllocatedStartTime() {
		return allocatedStartTime;
	}
	public void setAllocatedStartTime(String allocatedStartTime) {
		this.allocatedStartTime = allocatedStartTime;
	}
	public String getActualStartTime() {
		return actualStartTime;
	}
	public void setActualStartTime(String actualStartTime) {
		this.actualStartTime = actualStartTime;
	}
	public String getAllocatedEndTime() {
		return allocatedEndTime;
	}
	public void setAllocatedEndTime(String allocatedEndTime) {
		this.allocatedEndTime = allocatedEndTime;
	}
	public String getActualEndTime() {
		return actualEndTime;
	}
	public void setActualEndTime(String actualEndTime) {
		this.actualEndTime = actualEndTime;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getExpectedStartKm() {
		return expectedStartKm;
	}
	public void setExpectedStartKm(String expectedStartKm) {
		this.expectedStartKm = expectedStartKm;
	}
	public String getActuatStartKm() {
		return actuatStartKm;
	}
	public void setActuatStartKm(String actuatStartKm) {
		this.actuatStartKm = actuatStartKm;
	}
	public String getExpectedEndKm() {
		return expectedEndKm;
	}
	public void setExpectedEndKm(String expectedEndKm) {
		this.expectedEndKm = expectedEndKm;
	}
	public String getActuatEndKm() {
		return actuatEndKm;
	}
	public void setActuatEndKm(String actuatEndKm) {
		this.actuatEndKm = actuatEndKm;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public int getAvgSpeed() {
		return avgSpeed;
	}
	public void setAvgSpeed(int avgSpeed) {
		this.avgSpeed = avgSpeed;
	}
	
	
}
