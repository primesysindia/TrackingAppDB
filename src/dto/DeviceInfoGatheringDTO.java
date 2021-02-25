package dto;

public class DeviceInfoGatheringDTO {

	String deviceName,divisionName,deviceSimNo,deviceIMEINo,trackerNo,activationDate,userName,deviceId,firstName,LastName;
	int	parentId,studentId;
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	public String getDeviceSimNo() {
		return deviceSimNo;
	}
	public void setDeviceSimNo(String deviceSimNo) {
		this.deviceSimNo = deviceSimNo;
	}
	public String getDeviceIMEINo() {
		return deviceIMEINo;
	}
	public void setDeviceIMEINo(String deviceIMEINo) {
		this.deviceIMEINo = deviceIMEINo;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	
	public String getTrackerNo() {
		return trackerNo;
	}
	public void setTrackerNo(String trackerNo) {
		this.trackerNo = trackerNo;
	}
	public String getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}

}
