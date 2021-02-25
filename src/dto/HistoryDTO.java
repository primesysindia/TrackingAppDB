package dto;

import java.util.ArrayList;

public class HistoryDTO {
	
	String lat_direction,lan_direction,lat,lan,speed,timestamp,imei_no,expiary_date,address,batteryStatus,blindLocationGetTimestamp;
	int isBlind;
	public String getBlindLocationGetTimestamp() {
		return blindLocationGetTimestamp;
	}

	public void setBlindLocationGetTimestamp(String blindLocationGetTimestamp) {
		this.blindLocationGetTimestamp = blindLocationGetTimestamp;
	}

	public void setIsBlind(int isBlind) {
		this.isBlind = isBlind;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	String student_id;
	String status;
	String path;
	String type,name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImei_no() {
		return imei_no;
	}

	public void setImei_no(String imei_no) {
		this.imei_no = imei_no;
	}

	public String getExpiary_date() {
		return expiary_date;
	}

	public void setExpiary_date(String expiary_date) {
		this.expiary_date = expiary_date;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLan() {
		return lan;
	}

	public void setLan(String lan) {
		this.lan = lan;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getBatteryStatus() {
		return batteryStatus;
	}

	public void setBatteryStatus(String batteryStatus) {
		this.batteryStatus = batteryStatus;
	}


   

}
