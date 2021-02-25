package dto;

import java.io.Serializable;
import java.util.Comparator;

public class LocationDTO implements Serializable {

	String Lat,Lang,Time,Userid,photo,GroupId,DeviceImei,speed;
	String lat_direction,lan_direction,student_id;
	Long timestamp;
	double nearDist;

	

	public double getNearDist() {
		return nearDist;
	}

	public void setNearDist(double nearDist) {
		this.nearDist = nearDist;
	}

	public LocationDTO(String lat, String lang, String time, String userid,
			String photo, String groupId, String deviceImei, String speed,
			String lat_direction, String lan_direction, String student_id,
			Long timestamp) {
		super();
		Lat = lat;
		Lang = lang;
		Time = time;
		Userid = userid;
		this.photo = photo;
		GroupId = groupId;
		DeviceImei = deviceImei;
		this.speed = speed;
		this.lat_direction = lat_direction;
		this.lan_direction = lan_direction;
		this.student_id = student_id;
		this.timestamp = timestamp;
	}

	public LocationDTO() {
		// TODO Auto-generated constructor stub
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
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

	public String getDeviceImei() {
		return DeviceImei;
	}

	public void setDeviceImei(String deviceImei) {
		DeviceImei = deviceImei;
	}

	public String getGroupId() {
		return GroupId;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public void setGroupId(String groupId) {
		GroupId = groupId;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getLat() {
		return Lat;
	}

	public void setLat(String lat) {
		Lat = lat;
	}

	public String getLang() {
		return Lang;
	}

	public void setLang(String lang) {
		Lang = lang;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getUserid() {
		return Userid;
	}

	public void setUserid(String userid) {
		Userid = userid;
	}
	
	/*
	class TimeComparator implements Comparator<LocationDTO> {
	    public int compare(LocationDTO s1, LocationDTO s2) {
	        return Long.compare(s2.getTimestamp(), s1.getTimestamp());
	    }
	}*/
	
	 public static Comparator<LocationDTO> COMPARE_BY_TIME = new Comparator<LocationDTO>() {
	        public int compare(LocationDTO one, LocationDTO other) {
	            return one.timestamp.compareTo(other.timestamp);
	        }
	    };

	
}
