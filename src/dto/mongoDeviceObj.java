package dto;

public class mongoDeviceObj {

	int StudentId,ParentId;
	String DeviceId,simcardNo;
	public String getSimcardNo() {
		return simcardNo;
	}
	public void setSimcardNo(String simcardNo) {
		this.simcardNo = simcardNo;
	}
	public int getStudentId() {
		return StudentId;
	}
	public void setStudentId(int studentId) {
		StudentId = studentId;
	}
	public int getParentId() {
		return ParentId;
	}
	public void setParentId(int parentId) {
		ParentId = parentId;
	}
	public String getDeviceId() {
		return DeviceId;
	}
	public void setDeviceId(String deviceId) {
		DeviceId = deviceId;
	}
}
