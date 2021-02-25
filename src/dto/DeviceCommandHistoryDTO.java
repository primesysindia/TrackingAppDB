package dto;

public class DeviceCommandHistoryDTO {
	String commandId,name,deviceId,command,commandDeliveredMsg,
	deviceCommandResponse,timestamp,deviceResponseTime,login_name;
	int StudentId;
	
	public int getStudentId() {
		return StudentId;
	}

	public void setStudentId(int studentId) {
		StudentId = studentId;
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	public String getName() {
		return name;
	}

	public String getDeviceResponseTime() {
		return deviceResponseTime;
	}

	public void setDeviceResponseTime(String deviceResponseTime) {
		this.deviceResponseTime = deviceResponseTime;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCommandDeliveredMsg() {
		return commandDeliveredMsg;
	}

	public void setCommandDeliveredMsg(String commandDeliveredMsg) {
		this.commandDeliveredMsg = commandDeliveredMsg;
	}

	public String getDeviceCommandResponse() {
		return deviceCommandResponse;
	}

	public void setDeviceCommandResponse(String deviceCommandResponse) {
		this.deviceCommandResponse = deviceCommandResponse;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
