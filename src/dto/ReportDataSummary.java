package dto;

public class ReportDataSummary {
		private Double Kilometer,DistBetwLocAndRdps,CompareLat,CompareLan,diffFromExpected
	;
		private	String EventType	,FeatureDetail,DeviceId;
		int OverSpeedConsecutiveCount;
		public int getOverSpeedConsecutiveCount() {
			return OverSpeedConsecutiveCount;
		}
		public void setOverSpeedConsecutiveCount(int overSpeedConsecutiveCount) {
			OverSpeedConsecutiveCount = overSpeedConsecutiveCount;
		}
		Long StartOrEndTimestamp;
			public Double getDiffFromExpected() {
			return diffFromExpected;
		}
		public void setDiffFromExpected(Double diffFromExpected) {
			this.diffFromExpected = diffFromExpected;
		}
			public Long getStartOrEndTimestamp() {
			return StartOrEndTimestamp;
		}
		public void setStartOrEndTimestamp(Long startOrEndTimestamp) {
			StartOrEndTimestamp = startOrEndTimestamp;
		}
			public Double getKilometer() {
				return Kilometer;
			}
			public void setKilometer(Double kilometer) {
				Kilometer = kilometer;
			}
			public Double getDistBetwLocAndRdps() {
				return DistBetwLocAndRdps;
			}
			public void setDistBetwLocAndRdps(Double distBetwLocAndRdps) {
				DistBetwLocAndRdps = distBetwLocAndRdps;
			}
			public Double getCompareLat() {
				return CompareLat;
			}
			public void setCompareLat(Double compareLat) {
				CompareLat = compareLat;
			}
			public Double getCompareLan() {
				return CompareLan;
			}
			public void setCompareLan(Double compareLan) {
				CompareLan = compareLan;
			}
			public String getEventType() {
				return EventType;
			}
			public void setEventType(String eventType) {
				EventType = eventType;
			}
			public String getFeatureDetail() {
				return FeatureDetail;
			}
			public void setFeatureDetail(String featureDetail) {
				FeatureDetail = featureDetail;
			}
			public String getDeviceId() {
				return DeviceId;
			}
			public void setDeviceId(String deviceId) {
				DeviceId = deviceId;
			}
			
			
		 
	 
}
