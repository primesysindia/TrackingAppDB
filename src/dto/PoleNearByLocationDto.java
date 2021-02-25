package dto;

public class PoleNearByLocationDto {
	Double lat,lang;
	double minDistance,Total_distance;
	long maxSpeedTime;
	public long getMaxSpeedTime() {
		return maxSpeedTime;
	}
	public void setMaxSpeedTime(long maxSpeedTime) {
		this.maxSpeedTime = maxSpeedTime;
	}
	int speed,maxSpeed,Totalspeed,avgSpeed;
	public double getTotal_distance() {
		return Total_distance;
	}
	public void setTotal_distance(double total_distance) {
		Total_distance = total_distance;
	}
	public void setTotal_distance(int total_distance) {
		Total_distance = total_distance;
	}
	Long timestamp,tripStartExpectedTime,tripEndExpectedTime;
	public Long getTripStartExpectedTime() {
		return tripStartExpectedTime;
	}
	public void setTripStartExpectedTime(Long tripStartExpectedTime) {
		this.tripStartExpectedTime = tripStartExpectedTime;
	}
	public Long getTripEndExpectedTime() {
		return tripEndExpectedTime;
	}
	public void setTripEndExpectedTime(Long tripEndExpectedTime) {
		this.tripEndExpectedTime = tripEndExpectedTime;
	}
	String device;
	double startkmBeatActual,endKmBeatActual,startkmBeatExpected,endKmBeatExpected;
	public double getStartkmBeatActual() {
		return startkmBeatActual;
	}
	public void setStartkmBeatActual(double startkmBeatActual) {
		this.startkmBeatActual = startkmBeatActual;
	}
	public double getEndKmBeatActual() {
		return endKmBeatActual;
	}
	public void setEndKmBeatActual(double endKmBeatActual) {
		this.endKmBeatActual = endKmBeatActual;
	}
	public double getStartkmBeatExpected() {
		return startkmBeatExpected;
	}
	public void setStartkmBeatExpected(double startkmBeatExpected) {
		this.startkmBeatExpected = startkmBeatExpected;
	}
	public double getEndKmBeatExpected() {
		return endKmBeatExpected;
	}
	public void setEndKmBeatExpected(double endKmBeatExpected) {
		this.endKmBeatExpected = endKmBeatExpected;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public int getTotalspeed() {
		return Totalspeed;
	}
	public void setTotalspeed(int totalspeed) {
		Totalspeed = totalspeed;
	}
	public int getAvgSpeed() {
		return avgSpeed;
	}
	public void setAvgSpeed(int avgSpeed) {
		this.avgSpeed = avgSpeed;
	}
	
	public double getMinDistance() {
		return minDistance;
	}
	public void setMinDistance(double minDistance) {
		this.minDistance = minDistance;
	}
	public void setMinDistance(int minDistance) {
		this.minDistance = minDistance;
	}
 public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLang() {
		return lang;
	}
	public void setLang(Double lang) {
		this.lang = lang;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	

}
