package dto;

import java.util.ArrayList;

public class LowBatteryStatusReportDto {

	ArrayList<String> LowBatteryDevices;
	String sectionName;
	public ArrayList<String> getLowBatteryDevices() {
		return LowBatteryDevices;
	}
	public void setLowBatteryDevices(ArrayList<String> lowBatteryDevices) {
		LowBatteryDevices = lowBatteryDevices;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
}
