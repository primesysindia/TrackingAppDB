package dto;

public class DepartmentsDTO {

	String name;
	int deptId;
	Boolean isApprove;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public Boolean getIsApprove() {
		return isApprove;
	}
	public void setIsApprove(Boolean isApprove) {
		this.isApprove = isApprove;
	}
}
