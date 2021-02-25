package dto;

import java.util.ArrayList;

public class ListDeviceNotconnectedDTO {
	String TotalActiveCount;
	String TotalDeactiveCount;

	ArrayList<Double> ListOfDevice;
	
	public String getTotalActiveCount() {
		return TotalActiveCount;
	}
	public void setTotalActiveCount(String totalActiveCount) {
		TotalActiveCount = totalActiveCount;
	}
	public String getTotalDeactiveCount() {
		return TotalDeactiveCount;
	}
	public void setTotalDeactiveCount(String totalDeactiveCount) {
		TotalDeactiveCount = totalDeactiveCount;
	}
	public ArrayList<Double> getListOfDevice() {
		return ListOfDevice;
	}
	public void setListOfDevice(ArrayList<Double> listOfDevice) {
		ListOfDevice = listOfDevice;
	}
	

}
