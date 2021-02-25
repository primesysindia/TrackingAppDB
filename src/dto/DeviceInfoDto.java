package dto;

public class DeviceInfoDto {

	
	String Name ,DeviceID;
	int DeviceType,StudentId;

	

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
