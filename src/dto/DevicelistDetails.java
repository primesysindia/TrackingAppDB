package dto;


import java.io.Serializable;

public class DevicelistDetails implements Serializable {
	/**
	 * 
	 */
	String StudentID,Name,Photo,Type,SeviceId,ImeiNo,DeviceID,
	ActivationStatus,ExpiryDate,Remaining_days_to_expire;
	public String getRemaining_days_to_expire() {
		return Remaining_days_to_expire;
	}

	public void setRemaining_days_to_expire(String remaining_days_to_expire) {
		Remaining_days_to_expire = remaining_days_to_expire;
	}

	public String getDeviceID() {
		return DeviceID;
	}

	public void setDeviceID(String deviceID) {
		DeviceID = deviceID;
	}

	public String getActivationStatus() {
		return ActivationStatus;
	}

	public void setActivationStatus(String activationStatus) {
		ActivationStatus = activationStatus;
	}

	public String getExpiryDate() {
		return ExpiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		ExpiryDate = expiryDate;
	}

	public String getStudentID() {
		return StudentID;
	}

	public String getImeiNo() {
		return ImeiNo;
	}

	public void setImeiNo(String imeiNo) {
		ImeiNo = imeiNo;
	}

	public void setStudentID(String studentID) {
		StudentID = studentID;
	}



	public String getExpiary_date() {
		return expiary_date;
	}

	public void setExpiary_date(String expiary_date) {
		this.expiary_date = expiary_date;
	}

	String expiary_date;

	public String getStatus() {
		return status;
	}

	public String getColor() {
		return color;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	String status;

	public void setColor(String color) {
		this.color = color;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	String color;


	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		Photo = photo;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getSeviceId() {
		return SeviceId;
	}

	public void setSeviceId(String seviceId) {
		SeviceId = seviceId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;

}
