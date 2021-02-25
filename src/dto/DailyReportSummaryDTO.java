package dto;

public class DailyReportSummaryDTO {

	private ReportDataSummary StartKm,EndKm,StartTime,EndTime;
	
	  public ReportDataSummary getStartKm() {
		return StartKm;
	}

	public void setStartKm(ReportDataSummary startKm) {
		StartKm = startKm;
	}

	public ReportDataSummary getEndKm() {
		return EndKm;
	}

	public void setEndKm(ReportDataSummary endKm) {
		EndKm = endKm;
	}

	public ReportDataSummary getStartTime() {
		return StartTime;
	}

	public void setStartTime(ReportDataSummary startTime) {
		StartTime = startTime;
	}

	public ReportDataSummary getEndTime() {
		return EndTime;
	}

	public void setEndTime(ReportDataSummary endTime) {
		EndTime = endTime;
	}

	 
}
