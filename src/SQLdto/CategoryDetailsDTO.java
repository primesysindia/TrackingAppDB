package SQLdto;

import java.io.Serializable;

public class CategoryDetailsDTO implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String UserId,School_Id,Quiz_Id,Category,Category_Image,Category_Desc,Created_by,ActiveStatus;
public String getActiveStatus() {
	return ActiveStatus;
}
public void setActiveStatus(String activeStatus) {
	ActiveStatus = activeStatus;
}
private String message,error;


public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public String getError() {
	return error;
}
public void setError(String error) {
	this.error = error;
}
public String getUserId() {
	return UserId;
}
public void setUserId(String userId) {
	UserId = userId;
}
public String getSchool_Id() {
	return School_Id;
}
public void setSchool_Id(String school_Id) {
	School_Id = school_Id;
}
public String getQuiz_Id() {
	return Quiz_Id;
}
public void setQuiz_Id(String quiz_Id) {
	Quiz_Id = quiz_Id;
}
public String getCategory() {
	return Category;
}
public void setCategory(String category) {
	Category = category;
}
public String getCategory_Image() {
	return Category_Image;
}
public void setCategory_Image(String category_Image) {
	Category_Image = category_Image;
}
public String getCategory_Desc() {
	return Category_Desc;
}
public void setCategory_Desc(String category_Desc) {
	Category_Desc = category_Desc;
}
public String getCreated_by() {
	return Created_by;
}
public void setCreated_by(String created_by) {
	Created_by = created_by;
}



}
