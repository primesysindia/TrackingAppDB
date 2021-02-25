package SQLdto;

public class UserProfileDTO {
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

String UserId,Name,Gender,MobileNo,Address,Photo,EmailID;
String error,message;

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

public String getGender() {
	return Gender;
}

public void setGender(String gender) {
	Gender = gender;
}

public String getMobileNo() {
	return MobileNo;
}

public void setMobileNo(String mobileNo) {
	MobileNo = mobileNo;
}

public String getAddress() {
	return Address;
}

public void setAddress(String address) {
	Address = address;
}

public String getPhoto() {
	return Photo;
}

public void setPhoto(String photo) {
	Photo = photo;
}

public String getEmailID() {
	return EmailID;
}

public void setEmailID(String emailID) {
	EmailID = emailID;
}


}
