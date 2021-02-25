package dto;

import java.util.ArrayList;

public class DailyReportSummaryWithStoppageDTO {
	long ReportOfDay,DeviceStartTime,DeviceEndTime,ExpectedStartTime,ExpectedEndTime,DeviceOnTime,DeviceOffTime;
	String DeviceId,Name,AllocatedBeat,Remark;
	double 					DeviceStartKm,	DeviceEndKm,ExpectedStartKm	,ExpectedEndKm,ExpectedKmCover,ActualKmCover	;
	int DeviceOvespeedCount,DeviceStoppageCount	,MaxSpeed	,AvgSpeed,
	LocationCount,StartTimeDiff,EndTimeDiff,
	AllocatedTrip,ActualTrip;
	
	public long getDeviceOnTime() {
		return DeviceOnTime;
	}
	public void setDeviceOnTime(long deviceOnTime) {
		DeviceOnTime = deviceOnTime;
	}
	public long getDeviceOffTime() {
		return DeviceOffTime;
	}
	public void setDeviceOffTime(long deviceOffTime) {
		DeviceOffTime = deviceOffTime;
	}
	ArrayList<DailyReportSummaryWithStoppageDTO> deviceDetailTripList;
	public ArrayList<DailyReportSummaryWithStoppageDTO> getDeviceDetailTripList() {
		return deviceDetailTripList;
	}
	public void setDeviceDetailTripList(
			ArrayList<DailyReportSummaryWithStoppageDTO> deviceDetailTripList) {
		this.deviceDetailTripList = deviceDetailTripList;
	}
	public String getAllocatedBeat() {
		return AllocatedBeat;
	}
	public void setAllocatedBeat(String allocatedBeat) {
		AllocatedBeat = allocatedBeat;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public int getAllocatedTrip() {
		return AllocatedTrip;
	}
	public void setAllocatedTrip(int allocatedTrip) {
		AllocatedTrip = allocatedTrip;
	}
	public int getActualTrip() {
		return ActualTrip;
	}
	public void setActualTrip(int actualTrip) {
		ActualTrip = actualTrip;
	}
	public double getExpectedKmCover() {
		return ExpectedKmCover;
	}
	public void setExpectedKmCover(double expectedKmCover) {
		ExpectedKmCover = expectedKmCover;
	}
	public double getActualKmCover() {
		return ActualKmCover;
	}
	public void setActualKmCover(double actualKmCover) {
		ActualKmCover = actualKmCover;
	}
	public int getStartTimeDiff() {
		return StartTimeDiff;
	}
	public void setStartTimeDiff(int startTimeDiff) {
		StartTimeDiff = startTimeDiff;
	}
	public int getEndTimeDiff() {
		return EndTimeDiff;
	}
	public void setEndTimeDiff(int endTimeDiff) {
		EndTimeDiff = endTimeDiff;
	}
	public int getMaxSpeed() {
		return MaxSpeed;
	}
	public void setMaxSpeed(int maxSpeed) {
		MaxSpeed = maxSpeed;
	}
	public int getAvgSpeed() {
		return AvgSpeed;
	}
	public void setAvgSpeed(int avgSpeed) {
		AvgSpeed = avgSpeed;
	}
	public int getLocationCount() {
		return LocationCount;
	}
	public void setLocationCount(int locationCount) {
		LocationCount = locationCount;
	}
	public long getReportOfDay() {
		return ReportOfDay;
	}
	public void setReportOfDay(long reportOfDay) {
		ReportOfDay = reportOfDay;
	}
	public long getDeviceStartTime() {
		return DeviceStartTime;
	}
	public void setDeviceStartTime(long deviceStartTime) {
		DeviceStartTime = deviceStartTime;
	}
	public long getDeviceEndTime() {
		return DeviceEndTime;
	}
	public void setDeviceEndTime(long deviceEndTime) {
		DeviceEndTime = deviceEndTime;
	}
	public long getExpectedStartTime() {
		return ExpectedStartTime;
	}
	public void setExpectedStartTime(long expectedStartTime) {
		ExpectedStartTime = expectedStartTime;
	}
	public long getExpectedEndTime() {
		return ExpectedEndTime;
	}
	public void setExpectedEndTime(long expectedEndTime) {
		ExpectedEndTime = expectedEndTime;
	}
	public String getDeviceId() {
		return DeviceId;
	}
	public void setDeviceId(String deviceId) {
		DeviceId = deviceId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public double getDeviceStartKm() {
		return DeviceStartKm;
	}
	public void setDeviceStartKm(double deviceStartKm) {
		DeviceStartKm = deviceStartKm;
	}
	public double getDeviceEndKm() {
		return DeviceEndKm;
	}
	public void setDeviceEndKm(double deviceEndKm) {
		DeviceEndKm = deviceEndKm;
	}
	public double getExpectedStartKm() {
		return ExpectedStartKm;
	}
	public void setExpectedStartKm(double expectedStartKm) {
		ExpectedStartKm = expectedStartKm;
	}
	public double getExpectedEndKm() {
		return ExpectedEndKm;
	}
	public void setExpectedEndKm(double expectedEndKm) {
		ExpectedEndKm = expectedEndKm;
	}
	public int getDeviceOvespeedCount() {
		return DeviceOvespeedCount;
	}
	public void setDeviceOvespeedCount(int deviceOvespeedCount) {
		DeviceOvespeedCount = deviceOvespeedCount;
	}
	public int getDeviceStoppageCount() {
		return DeviceStoppageCount;
	}
	public void setDeviceStoppageCount(int deviceStoppageCount) {
		DeviceStoppageCount = deviceStoppageCount;
	}
	
		
}
