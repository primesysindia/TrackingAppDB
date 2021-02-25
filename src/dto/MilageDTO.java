package dto;

public class MilageDTO {

	Double lat,lang;
	String deviceIMEI;
	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLang() {
		return lang;
	}

	public void setLang(Double lang) {
		this.lang = lang;
	}

	public String getDeviceIMEI() {
		return deviceIMEI;
	}

	public void setDeviceIMEI(String device) {
		this.deviceIMEI = device;
	}
}
