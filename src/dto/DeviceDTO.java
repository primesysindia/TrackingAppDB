package dto;

import java.util.ArrayList;

public class DeviceDTO {
	Double lat,lan,timestamp,deviceId;
	int studentId;
	long locationTime,deviceBatteryTime;
	String imeiNo,deviceBattery,deviceRange,ActivationDate;
	ArrayList<RailwayPatrolManDTO> beatInfoList;
	ArrayList<DeviceIssueDTO> issueList;
	String PaymentStatus, ExpiryDate;

	public String getPaymentStatus() {
		return PaymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		PaymentStatus = paymentStatus;
	}
	public String getExpiryDate() {
		return ExpiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		ExpiryDate = expiryDate;
	}
	public String getActivationDate() {
		return ActivationDate;
	}
	public void setActivationDate(String activationDate) {
		ActivationDate = activationDate;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLan() {
		return lan;
	}
	public void setLan(Double lan) {
		this.lan = lan;
	}
	public Double getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Double timestamp) {
		this.timestamp = timestamp;
	}
	public Double getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Double deviceId) {
		this.deviceId = deviceId;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public long getLocationTime() {
		return locationTime;
	}
	public void setLocationTime(long locationTime) {
		this.locationTime = locationTime;
	}
	public long getDeviceBatteryTime() {
		return deviceBatteryTime;
	}
	public void setDeviceBatteryTime(long deviceBatteryTime) {
		this.deviceBatteryTime = deviceBatteryTime;
	}
	public String getImeiNo() {
		return imeiNo;
	}
	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}
	public ArrayList<RailwayPatrolManDTO> getBeatInfoList() {
		return beatInfoList;
	}
	public void setBeatInfoList(ArrayList<RailwayPatrolManDTO> beatInfoList) {
		this.beatInfoList = beatInfoList;
	}
	public ArrayList<DeviceIssueDTO> getIssueList() {
		return issueList;
	}
	public void setIssueList(ArrayList<DeviceIssueDTO> issueList) {
		this.issueList = issueList;
	}
	public String getDeviceBattery() {
		return deviceBattery;
	}
	public void setDeviceBattery(String deviceBattery) {
		this.deviceBattery = deviceBattery;
	}
	public String getDeviceRange() {
		return deviceRange;
	}
	public void setDeviceRange(String deviceRange) {
		this.deviceRange = deviceRange;
	}

}
