package dto;

public class FeatureAddressDetailsDTO {

	String kiloMeter,distance,NearByDistance,latitude,longitude,featureCode,featureDetail,feature_image,section,blockSection;
	int parentId;
	Double compareLat,compareLan,locationKmCalculated;
	public Double getLocationKmCalculated() {
		return locationKmCalculated;
	}

	public void setLocationKmCalculated(Double locationKmCalculated) {
		this.locationKmCalculated = locationKmCalculated;
	}

	public Double getCompareLat() {
		return compareLat;
	}

	public void setCompareLat(Double compareLat) {
		this.compareLat = compareLat;
	}

	public Double getCompareLan() {
		return compareLan;
	}

	public void setCompareLan(Double compareLan) {
		this.compareLan = compareLan;
	}

	public String getNearByDistance() {
		return NearByDistance;
	}

	public void setNearByDistance(String nearByDistance) {
		NearByDistance = nearByDistance;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getBlockSection() {
		return blockSection;
	}

	public void setBlockSection(String blockSection) {
		this.blockSection = blockSection;
	}

	public String getKiloMeter() {
		return kiloMeter;
	}

	public void setKiloMeter(String kiloMeter) {
		this.kiloMeter = kiloMeter;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	public String getFeatureDetail() {
		return featureDetail;
	}

	public void setFeatureDetail(String featureDetail) {
		this.featureDetail = featureDetail;
	}

	public String getFeature_image() {
		return feature_image;
	}

	public void setFeature_image(String feature_image) {
		this.feature_image = feature_image;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	
}
