package dto;



public class ApplicationMetaData {

	String PayUkey	,
		PayUsalt	,
		PayUtxnid	,
		PaymentURL	,
		PayU_surl	,
		PayU_furl	,
		GOOGLE_PROJECT_ID	,
		MESSAGE_KEY	,
		MapKey	;

	public String getPayUkey() {
		return PayUkey;
	}

	public void setPayUkey(String payUkey) {
		PayUkey = payUkey;
	}

	public String getPayUsalt() {
		return PayUsalt;
	}

	public void setPayUsalt(String payUsalt) {
		PayUsalt = payUsalt;
	}

	public String getPayUtxnid() {
		return PayUtxnid;
	}

	public void setPayUtxnid(String payUtxnid) {
		PayUtxnid = payUtxnid;
	}

	public String getPaymentURL() {
		return PaymentURL;
	}

	public void setPaymentURL(String paymentURL) {
		PaymentURL = paymentURL;
	}

	public String getPayU_surl() {
		return PayU_surl;
	}

	public void setPayU_surl(String payU_surl) {
		PayU_surl = payU_surl;
	}

	public String getPayU_furl() {
		return PayU_furl;
	}

	public void setPayU_furl(String payU_furl) {
		PayU_furl = payU_furl;
	}

	public String getGOOGLE_PROJECT_ID() {
		return GOOGLE_PROJECT_ID;
	}

	public void setGOOGLE_PROJECT_ID(String gOOGLE_PROJECT_ID) {
		GOOGLE_PROJECT_ID = gOOGLE_PROJECT_ID;
	}

	public String getMESSAGE_KEY() {
		return MESSAGE_KEY;
	}

	public void setMESSAGE_KEY(String mESSAGE_KEY) {
		MESSAGE_KEY = mESSAGE_KEY;
	}

	public String getMapKey() {
		return MapKey;
	}

	public void setMapKey(String mapKey) {
		MapKey = mapKey;
	}
}
