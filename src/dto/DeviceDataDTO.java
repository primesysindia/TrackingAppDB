package dto;


import java.io.Serializable;

public class DeviceDataDTO implements Serializable {
	/**
	 * 
	 */
	String student_id;
	String remaining_days_to_expire;
	
	String name;
	String path;
	String type;
	String imei_no;
	String expiary_date;
	String status,ShowGoogleAddress;
	String simNo;
	boolean isConnected;
	
	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public String getShowGoogleAddress() {
		return ShowGoogleAddress;
	}

	public void setShowGoogleAddress(String showGoogleAddress) {
		ShowGoogleAddress = showGoogleAddress;
	}

	public String getRemaining_days_to_expire() {
		return remaining_days_to_expire;
	}

	public void setRemaining_days_to_expire(String remaining_days_to_expire) {
		this.remaining_days_to_expire = remaining_days_to_expire;
	}

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	

	public String getExpiary_date() {
		return expiary_date;
	}

	public void setExpiary_date(String expiary_date) {
		this.expiary_date = expiary_date;
	}


	public String getStatus() {
		return status;
	}

	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImei_no() {
		return imei_no;
	}

	public void setImei_no(String imei_no) {
		this.imei_no = imei_no;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	private static final long serialVersionUID = 1L;

	public String getSimNo() {
		return simNo;
	}

	public void setSimNo(String simNo) {
		this.simNo = simNo;
	}

	
}
