package dto;

public class ZoneWiseReportPreDataDTO {
	String divisionParentId,TotalDeviceCount,KeymanCount,PatrolManCount,OtherDeviceCount,StudentIds,IMEI_NO,OffDevices,BeatNotCoverDevice,BeatCoverSucesfully;
 String divisionName,zoneName,zoneMailId;
	public String getZoneMailId() {
	return zoneMailId;
}

public void setZoneMailId(String zoneMailId) {
	this.zoneMailId = zoneMailId;
}

	public String getZoneName() {
	return zoneName;
}

public void setZoneName(String zoneName) {
	this.zoneName = zoneName;
}

	public String getDivisionName() {
	return divisionName;
}

public void setDivisionName(String divisionName) {
	this.divisionName = divisionName;
}

	public String getDivisionParentId() {
		return divisionParentId;
	}

	public void setDivisionParentId(String divisionParentId) {
		this.divisionParentId = divisionParentId;
	}

	public String getTotalDeviceCount() {
		return TotalDeviceCount;
	}

	public void setTotalDeviceCount(String totalDeviceCount) {
		TotalDeviceCount = totalDeviceCount;
	}

	public String getKeymanCount() {
		return KeymanCount;
	}

	public void setKeymanCount(String keymanCount) {
		KeymanCount = keymanCount;
	}

	public String getPatrolManCount() {
		return PatrolManCount;
	}

	public void setPatrolManCount(String patrolManCount) {
		PatrolManCount = patrolManCount;
	}

	public String getOtherDeviceCount() {
		return OtherDeviceCount;
	}

	public void setOtherDeviceCount(String otherDeviceCount) {
		OtherDeviceCount = otherDeviceCount;
	}

	public String getStudentIds() {
		return StudentIds;
	}

	public void setStudentIds(String studentIds) {
		StudentIds = studentIds;
	}

	public String getIMEI_NO() {
		return IMEI_NO;
	}

	public void setIMEI_NO(String iMEI_NO) {
		IMEI_NO = iMEI_NO;
	}

	public String getOffDevices() {
		return OffDevices;
	}

	public void setOffDevices(String offDevices) {
		OffDevices = offDevices;
	}

	public String getBeatNotCoverDevice() {
		return BeatNotCoverDevice;
	}

	public void setBeatNotCoverDevice(String beatNotCoverDevice) {
		BeatNotCoverDevice = beatNotCoverDevice;
	}

	public String getBeatCoverSucesfully() {
		return BeatCoverSucesfully;
	}

	public void setBeatCoverSucesfully(String beatCoverSucesfully) {
		BeatCoverSucesfully = beatCoverSucesfully;
	}
	

}
