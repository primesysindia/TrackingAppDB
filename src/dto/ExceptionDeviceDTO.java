package dto;

public class ExceptionDeviceDTO {
	String device,device_name,start_battery_status,end_battery_status,Tripno,start_beat,
	expected_start_beat
	,end_beat,
	expected_end_beat,
	max_speed,
	kmcover,timestamp,parentid,section;

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public String getStart_battery_status() {
		return start_battery_status;
	}

	public void setStart_battery_status(String start_battery_status) {
		this.start_battery_status = start_battery_status;
	}

	public String getEnd_battery_status() {
		return end_battery_status;
	}

	public void setEnd_battery_status(String end_battery_status) {
		this.end_battery_status = end_battery_status;
	}

	public String getTripno() {
		return Tripno;
	}

	public void setTripno(String tripno) {
		Tripno = tripno;
	}

	public String getStart_beat() {
		return start_beat;
	}

	public void setStart_beat(String start_beat) {
		this.start_beat = start_beat;
	}

	public String getExpected_start_beat() {
		return expected_start_beat;
	}

	public void setExpected_start_beat(String expected_start_beat) {
		this.expected_start_beat = expected_start_beat;
	}

	public String getEnd_beat() {
		return end_beat;
	}

	public void setEnd_beat(String end_beat) {
		this.end_beat = end_beat;
	}

	public String getExpected_end_beat() {
		return expected_end_beat;
	}

	public void setExpected_end_beat(String expected_end_beat) {
		this.expected_end_beat = expected_end_beat;
	}

	public String getMax_speed() {
		return max_speed;
	}

	public void setMax_speed(String max_speed) {
		this.max_speed = max_speed;
	}

	public String getKmcover() {
		return kmcover;
	}

	public void setKmcover(String kmcover) {
		this.kmcover = kmcover;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

}
