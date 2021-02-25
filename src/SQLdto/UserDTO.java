package SQLdto;

public class UserDTO {
String username,password,app_device_id,app_email_id,Role_ID,RegistrationType,UserID,ClassName,userType,
MobileNo,EmailID,Student_Count,school_id,app_id,Photo,error;
boolean user_exist;
public String getError() {
	return error;
}
public void setError(String error) {
	this.error = error;
}
public String getApp_device_id() {
	return app_device_id;
}
public void setApp_device_id(String app_device_id) {
	this.app_device_id = app_device_id;
}
public String getApp_email_id() {
	return app_email_id;
}
public void setApp_email_id(String app_email_id) {
	this.app_email_id = app_email_id;
}
public String getRole_ID() {
	return Role_ID;
}
public void setRole_ID(String role_ID) {
	Role_ID = role_ID;
}
public String getRegistrationType() {
	return RegistrationType;
}
public void setRegistrationType(String registrationType) {
	RegistrationType = registrationType;
}
public String getUserID() {
	return UserID;
}
public void setUserID(String userID) {
	UserID = userID;
}
public String getClassName() {
	return ClassName;
}
public void setClassName(String className) {
	ClassName = className;
}
public String getUserType() {
	return userType;
}
public void setUserType(String userType) {
	this.userType = userType;
}
public String getMobileNo() {
	return MobileNo;
}
public void setMobileNo(String mobileNo) {
	MobileNo = mobileNo;
}
public String getEmailID() {
	return EmailID;
}
public void setEmailID(String emailID) {
	EmailID = emailID;
}
public String getStudent_Count() {
	return Student_Count;
}
public void setStudent_Count(String student_Count) {
	Student_Count = student_Count;
}
public String getSchool_id() {
	return school_id;
}
public void setSchool_id(String school_id) {
	this.school_id = school_id;
}
public String getApp_id() {
	return app_id;
}
public void setApp_id(String app_id) {
	this.app_id = app_id;
}
public String getPhoto() {
	return Photo;
}
public void setPhoto(String photo) {
	Photo = photo;
}

public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public boolean isUser_exist() {
	return user_exist;
}
public void setUser_exist(boolean user_exist) {
	this.user_exist = user_exist;
}




}
