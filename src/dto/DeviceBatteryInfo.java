package dto;

public class DeviceBatteryInfo {
	String deviceId;
	long timestamp;
	Double network;
	Double batteryLevel;
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public Double getBatteryLevel() {
		return batteryLevel;
	}
	public void setBatteryLevel(Double batteryLevel) {
		this.batteryLevel = batteryLevel;
	}
	public Double getNetwork() {
		return network;
	}
	public void setNetwork(Double network) {
		this.network = network;
	}
	

}
