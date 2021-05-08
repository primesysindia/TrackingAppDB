package dto;

public class PatrolManBeatDTO {
	int tripId,fk_TripMasterId,seasonId,studentId,BeatId;
	String kmStart,kmEnd,startTime,endTime,sectionName;
	/**
	 * @return
	 */
	public int getTripId() {
		return tripId;
	}
	public int getBeatId() {
		return BeatId;
	}
	public void setBeatId(int beatId) {
		BeatId = beatId;
	}
	public void setTripId(int tripId) {
		this.tripId = tripId;
	}
	public int getFk_TripMasterId() {
		return fk_TripMasterId;
	}
	public void setFk_TripMasterId(int fk_TripMasterId) {
		this.fk_TripMasterId = fk_TripMasterId;
	}
	public int getSeasonId() {
		return seasonId;
	}
	public void setSeasonId(int seasonId) {
		this.seasonId = seasonId;
	}
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public String getKmStart() {
		return kmStart;
	}
	public void setKmStart(String kmStart) {
		this.kmStart = kmStart;
	}
	public String getKmEnd() {
		return kmEnd;
	}
	public void setKmEnd(String kmEnd) {
		this.kmEnd = kmEnd;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	
}
