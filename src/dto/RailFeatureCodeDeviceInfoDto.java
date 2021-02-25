package dto;

public class RailFeatureCodeDeviceInfoDto {

	
	public String getStart_lat() {
		return start_lat;
	}



	public void setStart_lat(String start_lat) {
		this.start_lat = start_lat;
	}



	public String getStart_lang() {
		return start_lang;
	}



	public void setStart_lang(String start_lang) {
		this.start_lang = start_lang;
	}



	public String getEnd_lat() {
		return end_lat;
	}



	public void setEnd_lat(String end_lat) {
		this.end_lat = end_lat;
	}



	public String getEnd_lang() {
		return end_lang;
	}



	public void setEnd_lang(String end_lang) {
		this.end_lang = end_lang;
	}

	String Name ,DeviceID;
	int DeviceType,StudentId,deviceOnStatus;
	public int getDeviceOnStatus() {
		return deviceOnStatus;
	}



	public void setDeviceOnStatus(int deviceOnStatus) {
		this.deviceOnStatus = deviceOnStatus;
	}

	public String getStartTime() {
		return startTime;
	}



	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}



	public String getEndTime() {
		return endTime;
	}



	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	String start_lat,start_lang,end_lat,end_lang,railLat,railLang,startTime,endTime;
	String lat_direction,lan_direction,address,speed;

	

	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getSpeed() {
		return speed;
	}



	public void setSpeed(String speed) {
		this.speed = speed;
	}



	
	public String getLat_direction() {
		return lat_direction;
	}



	public void setLat_direction(String lat_direction) {
		this.lat_direction = lat_direction;
	}



	public String getLan_direction() {
		return lan_direction;
	}



	public void setLan_direction(String lan_direction) {
		this.lan_direction = lan_direction;
	}


	public String getRailLat() {
		return railLat;
	}

	public void setRailLat(String railLat) {
		this.railLat = railLat;
	}

	public String getRailLang() {
		return railLang;
	}

	public void setRailLang(String railLang) {
		this.railLang = railLang;
	}

	

	public int getStudentId() {
		return StudentId;
	}

	public void setStudentId(int studentId) {
		StudentId = studentId;
	}

	public int getDeviceType() {
		return DeviceType;
	}

	public void setDeviceType(int deviceType) {
		DeviceType = deviceType;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDeviceID() {
		return DeviceID;
	}

	public void setDeviceID(String deviceID) {
		DeviceID = deviceID;
	}
}
