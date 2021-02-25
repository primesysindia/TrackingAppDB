package dto;

public class SosInfoDTO {
	String lat,lang,gpsDeviceName,time,voltage_level,gsm_signal_strength,deviceId,locationTime,speed;

	public String getLocationTime() {
		return locationTime;
	}

	public void setLocationTime(String locationTime) {
		this.locationTime = locationTime;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getGpsDeviceName() {
		return gpsDeviceName;
	}

	public void setGpsDeviceName(String gpsDeviceName) {
		this.gpsDeviceName = gpsDeviceName;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}


	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getVoltage_level() {
		return voltage_level;
	}

	public void setVoltage_level(String voltage_level) {
		this.voltage_level = voltage_level;
	}

	public String getGsm_signal_strength() {
		return gsm_signal_strength;
	}

	public void setGsm_signal_strength(String gsm_signal_strength) {
		this.gsm_signal_strength = gsm_signal_strength;
	}

}
