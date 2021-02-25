package dto;

import java.util.ArrayList;

public class AddDeviceDropDownInfo {
	
	ArrayList<DeviceTypeDTO> deviceTypeList;
	ArrayList<UserDTO> userList;
	ArrayList<PaymentmodeDTO> paymentmodeList;
	ArrayList<PaymentPlanDTO> paymentPlanList;
	public ArrayList<DeviceTypeDTO> getDeviceTypeList() {
		return deviceTypeList;
	}
	public void setDeviceTypeList(ArrayList<DeviceTypeDTO> deviceTypeList) {
		this.deviceTypeList = deviceTypeList;
	}
	public ArrayList<UserDTO> getUserList() {
		return userList;
	}
	public void setUserList(ArrayList<UserDTO> userList) {
		this.userList = userList;
	}
	public ArrayList<PaymentmodeDTO> getPaymentmodeList() {
		return paymentmodeList;
	}
	public void setPaymentmodeList(ArrayList<PaymentmodeDTO> paymentmodeList) {
		this.paymentmodeList = paymentmodeList;
	}
	public ArrayList<PaymentPlanDTO> getPaymentPlanList() {
		return paymentPlanList;
	}
	public void setPaymentPlanList(ArrayList<PaymentPlanDTO> paymentPlanList) {
		this.paymentPlanList = paymentPlanList;
	}
	

}
