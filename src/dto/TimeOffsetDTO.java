package dto;

public class TimeOffsetDTO {
	String offsetId      ,CountyName      ,OffsetActual      ,OffsetUponIndia,Flag;

	

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}

	public String getOffsetId() {
		return offsetId;
	}

	public void setOffsetId(String offsetId) {
		this.offsetId = offsetId;
	}

	public String getCountyName() {
		return CountyName;
	}

	public void setCountyName(String countyName) {
		CountyName = countyName;
	}

	public String getOffsetActual() {
		return OffsetActual;
	}

	public void setOffsetActual(String offsetActual) {
		OffsetActual = offsetActual;
	}

	public String getOffsetUponIndia() {
		return OffsetUponIndia;
	}

	public void setOffsetUponIndia(String offsetUponIndia) {
		OffsetUponIndia = offsetUponIndia;
	}

}
