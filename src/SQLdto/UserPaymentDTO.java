package SQLdto;

public class UserPaymentDTO {

String UserId,Name,EmailID,Expirydate,Status,Mobileno;
public String getMobileno() {
	return Mobileno;
}
public void setMobileno(String mobileno) {
	Mobileno = mobileno;
}
public String getUserId() {
	return UserId;
}
public void setUserId(String userId) {
	UserId = userId;
}
public String getName() {
	return Name;
}
public void setName(String name) {
	Name = name;
}
public String getEmailID() {
	return EmailID;
}
public void setEmailID(String emailID) {
	EmailID = emailID;
}
public String getExpirydate() {
	return Expirydate;
}
public void setExpirydate(String expirydate) {
	Expirydate = expirydate;
}
public String getStatus() {
	return Status;
}
public void setStatus(String status) {
	Status = status;
}
public String getError() {
	return error;
}
public void setError(String error) {
	this.error = error;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
String error,message;

}
