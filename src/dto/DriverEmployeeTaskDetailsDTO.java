package dto;

public class DriverEmployeeTaskDetailsDTO {
	
	private int TaskId,DriverId,StudentId_Carid,StartAddressId,EndAddressId,ActiveStatus,parentId;
	private String  startAddress,endAddress,deviceName,driverName,driverMobNo;
	public void setStartTime(long startTime) {
		StartTime = startTime;
	}
	public String getStartAddress() {
		return startAddress;
	}
	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}
	public String getEndAddress() {
		return endAddress;
	}
	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}
	public void setEndTime(long endTime) {
		EndTime = endTime;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	private long StartTime,EndTime;

	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverMobNo() {
		return driverMobNo;
	}
	public void setDriverMobNo(String driverMobNo) {
		this.driverMobNo = driverMobNo;
	}
	public int getTaskId() {
		return TaskId;
	}
	public void setTaskId(int taskId) {
		TaskId = taskId;
	}
	public int getDriverId() {
		return DriverId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public void setDriverId(int driverId) {
		DriverId = driverId;
	}
	public int getStudentId_Carid() {
		return StudentId_Carid;
	}
	public void setStudentId_Carid(int studentId_Carid) {
		StudentId_Carid = studentId_Carid;
	}
	public int getStartAddressId() {
		return StartAddressId;
	}
	public void setStartAddressId(int startAddressId) {
		StartAddressId = startAddressId;
	}
	public int getEndAddressId() {
		return EndAddressId;
	}
	public void setEndAddressId(int endAddressId) {
		EndAddressId = endAddressId;
	}
	public int getActiveStatus() {
		return ActiveStatus;
	}
	public void setActiveStatus(int activeStatus) {
		ActiveStatus = activeStatus;
	}
	public long getStartTime() {
		return StartTime;
	}
	public long getEndTime() {
		return EndTime;
	}
	

}
