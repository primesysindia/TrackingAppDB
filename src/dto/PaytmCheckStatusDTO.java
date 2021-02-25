package dto;

public class PaytmCheckStatusDTO {
	String MID,ORDERID,CHECKSUMHASH;
	public String getMID() {
		return MID;
	}

	public void setMID(String mID) {
		MID = mID;
	}

	public String getORDERID() {
		return ORDERID;
	}

	public void setORDERID(String oRDERID) {
		ORDERID = oRDERID;
	}

	public String getCHECKSUMHASH() {
		return CHECKSUMHASH;
	}

	public void setCHECKSUMHASH(String cHECKSUMHASH) {
		CHECKSUMHASH = cHECKSUMHASH;
	}

}
