package dto;

public class RailwayPatrolManripMasterDTO {

	int Id,TripStartTimeAdd,TripSpendTimeIntervalAdd,CreatedBy,UpdatedBy,ParentId,ActiveStatus;
	String TripName,TripTimeShedule,CreatedAt,UpdatedAt;
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getTripStartTimeAdd() {
		return TripStartTimeAdd;
	}
	public void setTripStartTimeAdd(int tripStartTimeAdd) {
		TripStartTimeAdd = tripStartTimeAdd;
	}
	public int getTripSpendTimeIntervalAdd() {
		return TripSpendTimeIntervalAdd;
	}
	public void setTripSpendTimeIntervalAdd(int tripSpendTimeIntervalAdd) {
		TripSpendTimeIntervalAdd = tripSpendTimeIntervalAdd;
	}
	public int getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(int createdBy) {
		CreatedBy = createdBy;
	}
	public int getUpdatedBy() {
		return UpdatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		UpdatedBy = updatedBy;
	}
	public int getParentId() {
		return ParentId;
	}
	public void setParentId(int parentId) {
		ParentId = parentId;
	}
	public int getActiveStatus() {
		return ActiveStatus;
	}
	public void setActiveStatus(int activeStatus) {
		ActiveStatus = activeStatus;
	}
	public String getTripName() {
		return TripName;
	}
	public void setTripName(String tripName) {
		TripName = tripName;
	}
	public String getTripTimeShedule() {
		return TripTimeShedule;
	}
	public void setTripTimeShedule(String tripTimeShedule) {
		TripTimeShedule = tripTimeShedule;
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
	
}

