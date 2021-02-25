package dto;

import java.util.ArrayList;
import java.util.LinkedList;

import dto.RailDeviceInfoDto;

public class RailMailSendInfoDto {
	
	ArrayList<String> EmailIds;
	ArrayList<RailDeviceInfoDto> DeviceIds;
	String ParentId;
	String Dept;
	int DeptId;
	String reportSendEmailId,reportSendEmailPassword;
	
		
	public String getReportSendEmailId() {
		return reportSendEmailId;
	}
	public void setReportSendEmailId(String reportSendEmailId) {
		this.reportSendEmailId = reportSendEmailId;
	}
	public String getReportSendEmailPassword() {
		return reportSendEmailPassword;
	}
	public void setReportSendEmailPassword(String reportSendEmailPassword) {
		this.reportSendEmailPassword = reportSendEmailPassword;
	}
	public int getDeptId() {
		return DeptId;
	}
	public void setDeptId(int deptId) {
		DeptId = deptId;
	}
	public String getDept() {
		return Dept;
	}
	public void setDept(String dept) {
		Dept = dept;
	}
	public ArrayList<String> getEmailIds() {
		return EmailIds;
	}
	public void setEmailIds(ArrayList<String> emailIds) {
		EmailIds = emailIds;
	}
	public ArrayList<RailDeviceInfoDto> getDeviceIds() {
		return DeviceIds;
	}
	public void setDeviceIds(ArrayList<RailDeviceInfoDto> deviceIds) {
		DeviceIds = deviceIds;
	}
	public String getParentId() {
		return ParentId;
	}
	public void setParentId(String parentId) {
		ParentId = parentId;
	}
	
	

}