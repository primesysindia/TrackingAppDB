package dto;

import java.util.ArrayList;

import com.mongodb.DBObject;

public class ExceptionReortsTrip {
	PoleNearByLocationDto tripStart,tripEnd;
	int locationSize,maxSpeed;
	Long maxSpeedTime;
	private ArrayList<DBObject> locationListObjects;
	public ArrayList<DBObject> getLocationListObjects() {
		return locationListObjects;
	}
	public void setLocationListObjects(ArrayList<DBObject> locationListObjects) {
		this.locationListObjects = locationListObjects;
	}
	public Long getMaxSpeedTime() {
		return maxSpeedTime;
	}
	public void setMaxSpeedTime(Long maxSpeedTime) {
		this.maxSpeedTime = maxSpeedTime;
	}
	String SectionName;
	public String getSectionName() {
		return SectionName;
	}
	public void setSectionName(String sectionName) {
		SectionName = sectionName;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	String deviceStartTime,deviceEndTime;
	public String getDeviceStartTime() {
		return deviceStartTime;
	}
	public void setDeviceStartTime(String deviceStartTime) {
		this.deviceStartTime = deviceStartTime;
	}
	public String getDeviceEndTime() {
		return deviceEndTime;
	}
	public void setDeviceEndTime(String deviceEndTime) {
		this.deviceEndTime = deviceEndTime;
	}
	public int getLocationSize() {
		return locationSize;
	}
	public void setLocationSize(int locationSize) {
		this.locationSize = locationSize;
	}
	int tripNo;
	public PoleNearByLocationDto getTripStart() {
		return tripStart;
	}
	public void setTripStart(PoleNearByLocationDto tripStart) {
		this.tripStart = tripStart;
	}
	public PoleNearByLocationDto getTripEnd() {
		return tripEnd;
	}
	public void setTripEnd(PoleNearByLocationDto tripEnd) {
		this.tripEnd = tripEnd;
	}
	public int getTripNo() {
		return tripNo;
	}
	public void setTripNo(int tripNo) {
		this.tripNo = tripNo;
	}

}
