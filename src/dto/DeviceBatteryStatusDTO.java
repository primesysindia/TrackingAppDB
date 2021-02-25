package dto;

public class DeviceBatteryStatusDTO {
	String device;
	Double startBatteryStatus,EndBatteryStatus;
	long startBatteryStatusTime,EndBatteryStatusTime;
	public String getDevice() {
		return device;
	}
	
	public Double getStartBatteryStatus() {
		return startBatteryStatus;
	}

	public void setStartBatteryStatus(Double startBatteryStatus) {
		this.startBatteryStatus = startBatteryStatus;
	}

	public Double getEndBatteryStatus() {
		return EndBatteryStatus;
	}

	public void setEndBatteryStatus(Double endBatteryStatus) {
		EndBatteryStatus = endBatteryStatus;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	
	public long getStartBatteryStatusTime() {
		return startBatteryStatusTime;
	}
	public void setStartBatteryStatusTime(long startBatteryStatusTime) {
		this.startBatteryStatusTime = startBatteryStatusTime;
	}
	public long getEndBatteryStatusTime() {
		return EndBatteryStatusTime;
	}
	public void setEndBatteryStatusTime(long endBatteryStatusTime) {
		EndBatteryStatusTime = endBatteryStatusTime;
	}

}
