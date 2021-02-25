package dto;

public class MessageObject {
String message ,error;
String role,name;
String id;



public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getRole() {
	return role;
}

public void setRole(String role) {
	this.role = role;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

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

}
