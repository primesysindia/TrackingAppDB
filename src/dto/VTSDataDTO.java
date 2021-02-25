package dto;

public class VTSDataDTO {
	String VtsEnable,DistanceInterval,StudentId,Error,Message,Pin;
	
	public String getError() {
		return Error;
	}

	public String getPin() {
		return Pin;
	}

	public void setPin(String pin) {
		Pin = pin;
	}

	public void setError(String error) {
		Error = error;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getVtsEnable() {
		return VtsEnable;
	}

	public void setVtsEnable(String vtsEnable) {
		VtsEnable = vtsEnable;
	}

	public String getDistanceInterval() {
		return DistanceInterval;
	}

	public void setDistanceInterval(String distanceInterval) {
		DistanceInterval = distanceInterval;
	}

	public String getStudentId() {
		return StudentId;
	}

	public void setStudentId(String studentId) {
		StudentId = studentId;
	}

}
