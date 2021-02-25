package dto;

import java.util.ArrayList;

public class DeactiveDeviceListDTO {
	String Totaldevice,TotalDeactiveDevice,Timestamp;
	public String getTotaldevice() {
		return Totaldevice;
	}
	public String getTimestamp() {
		return Timestamp;
	}
	public void setTimestamp(String timestamp) {
		Timestamp = timestamp;
	}
	public void setTotaldevice(String totaldevice) {
		Totaldevice = totaldevice;
	}
	public String getTotalDeactiveDevice() {
		return TotalDeactiveDevice;
	}
	public void setTotalDeactiveDevice(String totalDeactiveDevice) {
		TotalDeactiveDevice = totalDeactiveDevice;
	}
	public ArrayList<String> getListOfDevice() {
		return ListOfDevice;
	}
	public void setListOfDevice(ArrayList<String> listOfDevice) {
		ListOfDevice = listOfDevice;
	}
	ArrayList<String> ListOfDevice;


}
