package dto;

public class NearbyFeatureCodeWithDistanceDTO implements Comparable< NearbyFeatureCodeWithDistanceDTO >{
	Double startLat,startLan,featureDetailLat,featureDetailLan,distanceCalulated,kmDistance;
	int km,disMeter,featurecode,speed;
	public int getSpeed() {
		return speed;
	}
	public Double getKmDistance() {
		return kmDistance;
	}
	public void setKmDistance(Double kmDistance) {
		this.kmDistance = kmDistance;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getFeaturecode() {
		return featurecode;
	}
	public void setFeaturecode(int featurecode) {
		this.featurecode = featurecode;
	}
	long time;
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getFeatureDetail() {
		return featureDetail;
	}
	public void setFeatureDetail(String featureDetail) {
		this.featureDetail = featureDetail;
	}
	String section,featureDetail;
	public Double getStartLat() {
		return startLat;
	}
	public void setStartLat(Double startLat) {
		this.startLat = startLat;
	}
	public Double getStartLan() {
		return startLan;
	}
	public void setStartLan(Double startLan) {
		this.startLan = startLan;
	}
	public Double getFeatureDetailLat() {
		return featureDetailLat;
	}
	public void setFeatureDetailLat(Double featureDetailLat) {
		this.featureDetailLat = featureDetailLat;
	}
	public Double getFeatureDetailLan() {
		return featureDetailLan;
	}
	public void setFeatureDetailLan(Double featureDetailLan) {
		this.featureDetailLan = featureDetailLan;
	}
	public Double getDistanceCalulated() {
		return distanceCalulated;
	}
	public void setDistanceCalulated(Double distanceCalulated) {
		this.distanceCalulated = distanceCalulated;
	}
	public int getKm() {
		return km;
	}
	public void setKm(int km) {
		this.km = km;
	}
	public int getDisMeter() {
		return disMeter;
	}
	public void setDisMeter(int disMeter) {
		this.disMeter = disMeter;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	 @Override
	    public int compareTo(NearbyFeatureCodeWithDistanceDTO o) {
	        return this.getKmDistance().compareTo(o.getKmDistance());
	    }

}
