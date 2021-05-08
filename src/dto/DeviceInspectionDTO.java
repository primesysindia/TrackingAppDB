package dto;

public class DeviceInspectionDTO {
	int userLoginId,studentId,inspectionId;
	String issueTitle,issueDescription,finalTestingReport,inspectdBy,contactPerson,inspectionDate,activeStatus,name;
	Boolean isReusable;
	public int getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(int userLoginId) {
		this.userLoginId = userLoginId;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public String getIssueTitle() {
		return issueTitle;
	}
	public void setIssueTitle(String issueTitle) {
		this.issueTitle = issueTitle;
	}
	public String getIssueDescription() {
		return issueDescription;
	}
	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}
	public String getFinalTestingReport() {
		return finalTestingReport;
	}
	public void setFinalTestingReport(String finalTestingReport) {
		this.finalTestingReport = finalTestingReport;
	}
	public String getInspectdBy() {
		return inspectdBy;
	}
	public void setInspectdBy(String inspectdBy) {
		this.inspectdBy = inspectdBy;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getInspectionDate() {
		return inspectionDate;
	}
	public void setInspectionDate(String inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	public String getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
	public String getName() {
		return name;
	}
	public void setName(String firstName) {
		this.name = firstName;
	}
	public int getInspectionId() {
		return inspectionId;
	}
	public void setInspectionId(int inspectionId) {
		this.inspectionId = inspectionId;
	}
	public Boolean getIsReusable() {
		return isReusable;
	}
	public void setIsReusable(Boolean isReusable) {
		this.isReusable = isReusable;
	}
    	
}
