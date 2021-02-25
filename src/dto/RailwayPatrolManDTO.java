package dto;

public class RailwayPatrolManDTO {
	String deviceType,sectionName,deviceID,tripName,kmFromTo,tripStartTime,tripEndTime,CreatedAt,UpdatedAt,tripSpendTimeIntervalAdd,tripStartTimeAdd,tripTimeShedule;
	int SheetNo,userLoginId,parentId,beatId,studentId,fk_TripMasterId,createdBy,seasonId,Id,UpdatedBy,ActiveStatus;
	Double kmStart,kmStartLat,kmStartLang,kmEnd,kmEndLat,kmEndLang,totalKmCover;
	Boolean isApprove;
	
	public int getSheetNo() {
		return SheetNo;
	}
	public void setSheetNo(int sheetNo) {
		SheetNo = sheetNo;
	}
	public int getActiveStatus() {
		return ActiveStatus;
	}
	public void setActiveStatus(int activeStatus) {
		ActiveStatus = activeStatus;
	}
	public String getCreatedAt() {
		return CreatedAt;
	}
	public void setCreatedAt(String createdAt) {
		CreatedAt = createdAt;
	}
	public String getUpdatedAt() {
		return UpdatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		UpdatedAt = updatedAt;
	}
	public int getUpdatedBy() {
		return UpdatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		UpdatedBy = updatedBy;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public int getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(int userLoginId) {
		this.userLoginId = userLoginId;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getTripName() {
		return tripName;
	}
	public void setTripName(String tripName) {
		this.tripName = tripName;
	}
	public String getKmFromTo() {
		return kmFromTo;
	}
	public void setKmFromTo(String kmFromTo) {
		this.kmFromTo = kmFromTo;
	}
	public String getTripStartTime() {
		return tripStartTime;
	}
	public void setTripStartTime(String tripStartTime) {
		this.tripStartTime = tripStartTime;
	}
	public String getTripEndTime() {
		return tripEndTime;
	}
	public void setTripEndTime(String tripEndTime) {
		this.tripEndTime = tripEndTime;
	}
	public String getTripSpendTimeIntervalAdd() {
		return tripSpendTimeIntervalAdd;
	}
	public void setTripSpendTimeIntervalAdd(String tripSpendTimeIntervalAdd) {
		this.tripSpendTimeIntervalAdd = tripSpendTimeIntervalAdd;
	}
	public String getTripStartTimeAdd() {
		return tripStartTimeAdd;
	}
	public void setTripStartTimeAdd(String tripStartTimeAdd) {
		this.tripStartTimeAdd = tripStartTimeAdd;
	}
	public String getTripTimeShedule() {
		return tripTimeShedule;
	}
	public void setTripTimeShedule(String tripTimeShedule) {
		this.tripTimeShedule = tripTimeShedule;
	}
	public int getBeatId() {
		return beatId;
	}
	public void setBeatId(int beatId) {
		this.beatId = beatId;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public int getFk_TripMasterId() {
		return fk_TripMasterId;
	}
	public void setFk_TripMasterId(int fk_TripMasterId) {
		this.fk_TripMasterId = fk_TripMasterId;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Double getKmStart() {
		return kmStart;
	}
	public void setKmStart(Double kmStart) {
		this.kmStart = kmStart;
	}
	public Double getKmStartLat() {
		return kmStartLat;
	}
	public void setKmStartLat(Double kmStartLat) {
		this.kmStartLat = kmStartLat;
	}
	public Double getKmStartLang() {
		return kmStartLang;
	}
	public void setKmStartLang(Double kmStartLang) {
		this.kmStartLang = kmStartLang;
	}
	public Double getKmEnd() {
		return kmEnd;
	}
	public void setKmEnd(Double kmEnd) {
		this.kmEnd = kmEnd;
	}
	public Double getKmEndLat() {
		return kmEndLat;
	}
	public void setKmEndLat(Double kmEndLat) {
		this.kmEndLat = kmEndLat;
	}
	public Double getKmEndLang() {
		return kmEndLang;
	}
	public void setKmEndLang(Double kmEndLang) {
		this.kmEndLang = kmEndLang;
	}
	public Double getTotalKmCover() {
		return totalKmCover;
	}
	public void setTotalKmCover(Double totalKmCover) {
		this.totalKmCover = totalKmCover;
	}
	public Boolean getIsApprove() {
		return isApprove;
	}
	public void setIsApprove(Boolean isApprove) {
		this.isApprove = isApprove;
	}
	public int getSeasonId() {
		return seasonId;
	}
	public void setSeasonId(int seasonId) {
		this.seasonId = seasonId;
	}
	
	
}
