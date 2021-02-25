package dto;

public class RailwayKeymanDTO {
	String Devicename, SectionName,DeviceId,TripName,TripStartTime,TripEndTime,CreatedAt,UpdatedAt,NameWhoInsert,MobWhoInsert,EmailWhoInsert,ApprovedDate;
	int id,CreatedBy,UpdatedBy,parentId,studentId,beatId,activeStatus,UserLoginId,startTime,EndTime;
	Double KmStart,KmEnd,Start_Lat,Start_Lon,End_Lat,End_Lon;
	Boolean isApprove,isBeatPoleAvilableRDPS;
	
	
	public String getDevicename() {
		return Devicename;
	}
	public void setDevicename(String devicename) {
		Devicename = devicename;
	}
	public String getApprovedDate() {
		return ApprovedDate;
	}
	public void setApprovedDate(String approvedDate) {
		ApprovedDate = approvedDate;
	}
	public String getEmailWhoInsert() {
		return EmailWhoInsert;
	}
	public void setEmailWhoInsert(String emailWhoInsert) {
		EmailWhoInsert = emailWhoInsert;
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getEndTime() {
		return EndTime;
	}
	public void setEndTime(int endTime) {
		EndTime = endTime;
	}
	public String getMobWhoInsert() {
		return MobWhoInsert;
	}
	public String getNameWhoInsert() {
		return NameWhoInsert;
	}
	public void setNameWhoInsert(String nameWhoInsert) {
		NameWhoInsert = nameWhoInsert;
	}
	public String getMobWhoInser() {
		return MobWhoInsert;
	}
	public void setMobWhoInsert(String mobWhoInsert) {
		MobWhoInsert = mobWhoInsert;
	}
	public int getUserLoginId() {
		return UserLoginId;
	}
	public void setUserLoginId(int userLoginId) {
		UserLoginId = userLoginId;
	}
	public String getSectionName() {
		return SectionName;
	}
	public void setSectionName(String sectionName) {
		SectionName = sectionName;
	}
	public String getDeviceId() {
		return DeviceId;
	}
	public void setDeviceId(String deviceId) {
		DeviceId = deviceId;
	}
	public String getTripName() {
		return TripName;
	}
	public void setTripName(String tripName) {
		TripName = tripName;
	}
	public String getTripStartTime() {
		return TripStartTime;
	}
	public void setTripStartTime(String tripStartTime) {
		TripStartTime = tripStartTime;
	}
	public String getTripEndTime() {
		return TripEndTime;
	}
	public void setTripEndTime(String tripEndTime) {
		TripEndTime = tripEndTime;
	}
	public int getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(int createdBy) {
		CreatedBy = createdBy;
	}
	public String getCreatedAt() {
		return CreatedAt;
	}
	public void setCreatedAt(String createdAt) {
		CreatedAt = createdAt;
	}
	public int getUpdatedBy() {
		return UpdatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		UpdatedBy = updatedBy;
	}
	public String getUpdatedAt() {
		return UpdatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		UpdatedAt = updatedAt;
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
	public int getBeatId() {
		return beatId;
	}
	public void setBeatId(int beatId) {
		this.beatId = beatId;
	}
	public Double getKmStart() {
		return KmStart;
	}
	public void setKmStart(Double kmStart) {
		KmStart = kmStart;
	}
	public Double getKmEnd() {
		return KmEnd;
	}
	public void setKmEnd(Double kmEnd) {
		KmEnd = kmEnd;
	}
	public Double getStart_Lat() {
		return Start_Lat;
	}
	public void setStart_Lat(Double start_Lat) {
		Start_Lat = start_Lat;
	}
	public Double getStart_Lon() {
		return Start_Lon;
	}
	public void setStart_Lon(Double start_Lon) {
		Start_Lon = start_Lon;
	}
	public Double getEnd_Lat() {
		return End_Lat;
	}
	public void setEnd_Lat(Double end_Lat) {
		End_Lat = end_Lat;
	}
	public Double getEnd_Lon() {
		return End_Lon;
	}
	public void setEnd_Lon(Double end_Lon) {
		End_Lon = end_Lon;
	}
	public Boolean getIsApprove() {
		return isApprove;
	}
	public void setIsApprove(Boolean isApprove) {
		this.isApprove = isApprove;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}
	public Boolean getIsBeatPoleAvilableRDPS() {
		return isBeatPoleAvilableRDPS;
	}
	public void setIsBeatPoleAvilableRDPS(Boolean isBeatPoleAvilableRDPS) {
		this.isBeatPoleAvilableRDPS = isBeatPoleAvilableRDPS;
	}
	
}
