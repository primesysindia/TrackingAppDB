package dto;

import java.util.ArrayList;

public class DeviceIssueDTO {
	String issueTicketId,deviceId,deviceName,divisionName,updatedByName,issueOwner,issueTitle,contactPerson,issueTime,description,contactPersonMobNo,issueComment,createdAt,updatedAt;
	int	issueStatus,isseMasterId,issueId,priority,parentId,studentId,userLoginId,createdBy,updatedBy,isDeviceOn,isDeviceButtonOn,isBatteryOn,isGSMOn,isGpsOn,isImeiSIMCorrect;
	Boolean isSendMail;
	ArrayList<IssueFileInfoDTO> issueFileList;

	public Boolean getIsSendMail() {
		return isSendMail;
	}
	public void setIsSendMail(Boolean isSendMail) {
		this.isSendMail = isSendMail;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getIssueTicketId() {
		return issueTicketId;
	}
	public void setIssueTicketId(String issueTicketId) {
		this.issueTicketId = issueTicketId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public ArrayList<IssueFileInfoDTO> getIssueFileList() {
		return issueFileList;
	}
	public void setIssueFileList(ArrayList<IssueFileInfoDTO> issueFileList) {
		this.issueFileList = issueFileList;
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
	public String getUpdatedByName() {
		return updatedByName;
	}
	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}
	public String getIssueOwner() {
		return issueOwner;
	}
	public void setIssueOwner(String issueOwner) {
		this.issueOwner = issueOwner;
	}
	public String getIssueTitle() {
		return issueTitle;
	}
	public void setIssueTitle(String issueTitle) {
		this.issueTitle = issueTitle;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getIssueTime() {
		return issueTime;
	}
	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContactPersonMobNo() {
		return contactPersonMobNo;
	}
	public void setContactPersonMobNo(String contactPersonMobNo) {
		this.contactPersonMobNo = contactPersonMobNo;
	}
	public String getIssueComment() {
		return issueComment;
	}
	public void setIssueComment(String issueComment) {
		this.issueComment = issueComment;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public int getIssueStatus() {
		return issueStatus;
	}
	public void setIssueStatus(int issueStatus) {
		this.issueStatus = issueStatus;
	}
	public int getIsseMasterId() {
		return isseMasterId;
	}
	public void setIsseMasterId(int isseMasterId) {
		this.isseMasterId = isseMasterId;
	}
	public int getIssueId() {
		return issueId;
	}
	public void setIssueId(int issueId) {
		this.issueId = issueId;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
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
	public int getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(int userLoginId) {
		this.userLoginId = userLoginId;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public int getIsDeviceOn() {
		return isDeviceOn;
	}
	public void setIsDeviceOn(int isDeviceOn) {
		this.isDeviceOn = isDeviceOn;
	}
	public int getIsDeviceButtonOn() {
		return isDeviceButtonOn;
	}
	public void setIsDeviceButtonOn(int isDeviceButtonOn) {
		this.isDeviceButtonOn = isDeviceButtonOn;
	}
	public int getIsBatteryOn() {
		return isBatteryOn;
	}
	public void setIsBatteryOn(int isBatteryOn) {
		this.isBatteryOn = isBatteryOn;
	}
	public int getIsGSMOn() {
		return isGSMOn;
	}
	public void setIsGSMOn(int isGSMOn) {
		this.isGSMOn = isGSMOn;
	}
	public int getIsGpsOn() {
		return isGpsOn;
	}
	public void setIsGpsOn(int isGpsOn) {
		this.isGpsOn = isGpsOn;
	}
	public int getIsImeiSIMCorrect() {
		return isImeiSIMCorrect;
	}
	public void setIsImeiSIMCorrect(int isImeiSIMCorrect) {
		this.isImeiSIMCorrect = isImeiSIMCorrect;
	}

		
}
