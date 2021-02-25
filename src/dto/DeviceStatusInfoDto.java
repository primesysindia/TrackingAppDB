package dto;

import dto.FeatureAddressDetailsDTO;

public class DeviceStatusInfoDto {

	
	String Name ;
	Long DeviceID;
	public Long getDeviceID() {
		return DeviceID;
	}



	public void setDeviceID(Long deviceID) {
		DeviceID = deviceID;
	}

	String DeviceType,StudentId,parentId;
	int deviceOnStatus;


	String lat,lang,railLat,railLang,time;
	String lat_direction,lan_direction,address,speed;
	FeatureAddressDetailsDTO railFeatureDto;


	





	public String getName() {
		return Name;
	}



	public void setName(String name) {
		Name = name;
	}





	

	public String getDeviceType() {
		return DeviceType;
	}



	public void setDeviceType(String deviceType) {
		DeviceType = deviceType;
	}



	public String getStudentId() {
		return StudentId;
	}



	public void setStudentId(String studentId) {
		StudentId = studentId;
	}



	public String getParentId() {
		return parentId;
	}



	public void setParentId(String parentId) {
		this.parentId = parentId;
	}



	public FeatureAddressDetailsDTO getRailFeatureDto() {
		return railFeatureDto;
	}



	public void setRailFeatureDto(FeatureAddressDetailsDTO railFeatureDto) {
		this.railFeatureDto = railFeatureDto;
	}



	public int getDeviceOnStatus() {
		return deviceOnStatus;
	}



	public void setDeviceOnStatus(int deviceOnStatus) {
		this.deviceOnStatus = deviceOnStatus;
	}

	
	

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



	public String getLat() {
		return lat;
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



	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	
}