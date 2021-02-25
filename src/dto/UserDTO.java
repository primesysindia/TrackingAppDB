package dto;

public class UserDTO 
{	String Name;
	int parentId,rollId,studentCount;
	Boolean IsRailwayUser;
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getRollId() {
		return rollId;
	}
	public void setRollId(int rollId) {
		this.rollId = rollId;
	}
	public int getStudentCount() {
		return studentCount;
	}
	public void setStudentCount(int studentCount) {
		this.studentCount = studentCount;
	}
	public Boolean getIsRailwayUser() {
		return IsRailwayUser;
	}
	public void setIsRailwayUser(Boolean isRailwayUser) {
		IsRailwayUser = isRailwayUser;
	}
	
	

}
