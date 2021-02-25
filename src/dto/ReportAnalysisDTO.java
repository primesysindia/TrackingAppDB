package dto;

import java.util.Comparator;

public class ReportAnalysisDTO    {
	private Double deviceLocationLat,deviceLocationLan,distDiffbetRDPSAndLocation,
	nearestRdpsKiloMeter,locationKmCalculated;
	private FeatureAddressDetailsDTO NearByRDPS;
	private Long deviceLocationTimestamp;
	private String rdpsFeatureDetail,deviceIMEI;
	public Double getLocationKmCalculated() {
		return locationKmCalculated;
	}
	public void setLocationKmCalculated(Double locationKmCalculated) {
		this.locationKmCalculated = locationKmCalculated;
	}
	public Double getDeviceLocationLat() {
		return deviceLocationLat;
	}
	public void setDeviceLocationLat(Double deviceLocationLat) {
		this.deviceLocationLat = deviceLocationLat;
	}
	public Double getDeviceLocationLan() {
		return deviceLocationLan;
	}
	public void setDeviceLocationLan(Double deviceLocationLan) {
		this.deviceLocationLan = deviceLocationLan;
	}
	
	public Double getNearestRdpsKiloMeter() {
		return nearestRdpsKiloMeter;
	}
	public void setNearestRdpsKiloMeter(Double nearestRdpsKiloMeter) {
		this.nearestRdpsKiloMeter = nearestRdpsKiloMeter;
	}
	public FeatureAddressDetailsDTO getNearByRDPS() {
		return NearByRDPS;
	}
	public void setNearByRDPS(FeatureAddressDetailsDTO nearByRDPS) {
		NearByRDPS = nearByRDPS;
	}
	public Double getDistDiffbetRDPSAndLocation() {
		return distDiffbetRDPSAndLocation;
	}
	public void setDistDiffbetRDPSAndLocation(Double distDiffbetRDPSAndLocation) {
		this.distDiffbetRDPSAndLocation = distDiffbetRDPSAndLocation;
	}
	public Long getDeviceLocationTimestamp() {
		return deviceLocationTimestamp;
	}
	public void setDeviceLocationTimestamp(Long deviceLocationTimestamp) {
		this.deviceLocationTimestamp = deviceLocationTimestamp;
	}
	public String getRdpsFeatureDetail() {
		return rdpsFeatureDetail;
	}
	public void setRdpsFeatureDetail(String rdpsFeatureDetail) {
		this.rdpsFeatureDetail = rdpsFeatureDetail;
	}
	public String getDeviceIMEI() {
		return deviceIMEI;
	}
	public void setDeviceIMEI(String deviceIMEI) {
		this.deviceIMEI = deviceIMEI;
	}
	
	/*Comparator for sorting the list by location 
	public static Comparator<ReportAnalysisDTO> deviceLocRDPSDistComparator = new Comparator<ReportAnalysisDTO>() {

		public int compare(ReportAnalysisDTO s1, ReportAnalysisDTO s2) {
			Double distdiffLOcationAndRDPS1= s1.getDistDiffbetRDPSAndLocation();
			Double distdiffLOcationAndRDPS2 = s2.getDistDiffbetRDPSAndLocation();

			//ascending order
			return distdiffLOcationAndRDPS1.compareTo(distdiffLOcationAndRDPS2);

			//descending order
			//return StoreName2.compareTo(StoreName1);
		}};*/

		
		/*Comparator for sorting the list by location */
		public static Comparator<ReportAnalysisDTO> RdpsStartToEndComparator = new Comparator<ReportAnalysisDTO>() {

			public int compare(ReportAnalysisDTO s1, ReportAnalysisDTO s2) {
				Double locationKmCalculated1= s1.getLocationKmCalculated();
				Double locationKmCalculated2 = s2.getLocationKmCalculated();

				//ascending order
				return locationKmCalculated1.compareTo(locationKmCalculated2);

				//descending order
				//return StoreName2.compareTo(StoreName1);
			}};
			
			/*Comparator for sorting the list by location */
			public static Comparator<ReportAnalysisDTO> deviceLocRDPSDistComparator = new Comparator<ReportAnalysisDTO>() {

				public int compare(ReportAnalysisDTO s1, ReportAnalysisDTO s2) {
					Double distdiffLOcationAndRDPS1= s1.getDistDiffbetRDPSAndLocation();
					Double distdiffLOcationAndRDPS2 = s2.getDistDiffbetRDPSAndLocation();

					//ascending order
					return distdiffLOcationAndRDPS1.compareTo(distdiffLOcationAndRDPS2);

					//descending order
					//return StoreName2.compareTo(StoreName1);
				}};

				
				/*Comparator for sorting the list by time */
				public static Comparator<ReportAnalysisDTO> timestampComparator = new Comparator<ReportAnalysisDTO>() {

					public int compare(ReportAnalysisDTO s1, ReportAnalysisDTO s2) {
						Long locationTimeStamp1= s1.getDeviceLocationTimestamp();
						Long locationTimeStamp2 = s2.getDeviceLocationTimestamp();

						//ascending order
						return locationTimeStamp1.compareTo(locationTimeStamp2);

						//descending order
						//return StoreName2.compareTo(StoreName1);
					}};
}
