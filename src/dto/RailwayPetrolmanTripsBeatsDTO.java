package dto;

import java.util.ArrayList;
import java.util.Set;


public class RailwayPetrolmanTripsBeatsDTO {
	String tripName,kmFromTo,initialDayStartTime,SectionName,tripTimeShedule,deviceId;
	Double kmStartLat,kmStartLang,kmEndLat,kmEndLang;
	//ArrayList<String>  kmStartLatSet,kmStartLangSet,kmEndLatSet,kmEndLangSet;

	
	public Double getKmStartLat() {
		return kmStartLat;
	}
	public void setKmStartLat(Double kmStartLat) {
		this.kmStartLat = kmStartLat;
	}
	public Double getKmStartLang() {
		return kmStartLang;
	}
	public void setKmStartLang(Double kmStartLang) {
		this.kmStartLang = kmStartLang;
	}
	public Double getKmEndLat() {
		return kmEndLat;
	}
	public void setKmEndLat(Double kmEndLat) {
		this.kmEndLat = kmEndLat;
	}
	public Double getKmEndLang() {
		return kmEndLang;
	}
	public void setKmEndLang(Double kmEndLang) {
		this.kmEndLang = kmEndLang;
	}
	
	/*public ArrayList<String> getKmStartLatSet() {
		return kmStartLatSet;
	}
	public void setKmStartLatSet(ArrayList<String> kmStartLatSet) {
		this.kmStartLatSet = kmStartLatSet;
	}
	public ArrayList<String> getKmStartLangSet() {
		return kmStartLangSet;
	}
	public void setKmStartLangSet(ArrayList<String> kmStartLangSet) {
		this.kmStartLangSet = kmStartLangSet;
	}
	public ArrayList<String> getKmEndLatSet() {
		return kmEndLatSet;
	}
	public void setKmEndLatSet(ArrayList<String> kmEndLatSet) {
		this.kmEndLatSet = kmEndLatSet;
	}
	public ArrayList<String> getKmEndLangSet() {
		return kmEndLangSet;
	}
	public void setKmEndLangSet(ArrayList<String> kmEndLangSet) {
		this.kmEndLangSet = kmEndLangSet;
	}*/
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	Double kmStart,kmEnd,totalKmCover;
	
	int studentId,fkTripMasterId,tripStartTimeAdd,tripSpendTimeIntervalAdd,sheetNo,Id;
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getTripName() {
		return tripName;
	}
	public void setTripName(String tripName) {
		this.tripName = tripName;
	}
	public String getKmFromTo() {
		return kmFromTo;
	}
	public void setKmFromTo(String kmFromTo) {
		this.kmFromTo = kmFromTo;
	}
	public String getInitialDayStartTime() {
		return initialDayStartTime;
	}
	public void setInitialDayStartTime(String initialDayStartTime) {
		this.initialDayStartTime = initialDayStartTime;
	}
	public String getSectionName() {
		return SectionName;
	}
	public void setSectionName(String sectionName) {
		SectionName = sectionName;
	}
	public String getTripTimeShedule() {
		return tripTimeShedule;
	}
	public void setTripTimeShedule(String tripTimeShedule) {
		this.tripTimeShedule = tripTimeShedule;
	}
	public Double getKmStart() {
		return kmStart;
	}
	public void setKmStart(Double kmStart) {
		this.kmStart = kmStart;
	}
	public Double getKmEnd() {
		return kmEnd;
	}
	public void setKmEnd(Double kmEnd) {
		this.kmEnd = kmEnd;
	}
	public Double getTotalKmCover() {
		return totalKmCover;
	}
	public void setTotalKmCover(Double totalKmCover) {
		this.totalKmCover = totalKmCover;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public int getFkTripMasterId() {
		return fkTripMasterId;
	}
	public void setFkTripMasterId(int fkTripMasterId) {
		this.fkTripMasterId = fkTripMasterId;
	}
	public int getTripStartTimeAdd() {
		return tripStartTimeAdd;
	}
	public void setTripStartTimeAdd(int tripStartTimeAdd) {
		this.tripStartTimeAdd = tripStartTimeAdd;
	}
	public int getTripSpendTimeIntervalAdd() {
		return tripSpendTimeIntervalAdd;
	}
	public void setTripSpendTimeIntervalAdd(int tripSpendTimeIntervalAdd) {
		this.tripSpendTimeIntervalAdd = tripSpendTimeIntervalAdd;
	}
	public int getSheetNo() {
		return sheetNo;
	}
	public void setSheetNo(int sheetNo) {
		this.sheetNo = sheetNo;
	}
	 
}
