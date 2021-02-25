package Utility;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import dao.Common;


public class JavaHashGenerator {
	
	
	
	private final String PAYMENT_HASH = "payment_hash";
	private final String GET_MERCHANT_IBIBO_CODES_HASH = "get_merchant_ibibo_codes_hash";
	private final String VAS_FOR_MOBILE_SDK_HASH = "vas_for_mobile_sdk_hash";
	private final String PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK_HASH = "payment_related_details_for_mobile_sdk_hash";
	private final String DELETE_USER_CARD_HASH = "delete_user_card_hash";
	private final String GET_USER_CARDS_HASH = "get_user_cards_hash";
	private final String EDIT_USER_CARD_HASH = "edit_user_card_hash";
	private final String SAVE_USER_CARD_HASH = "save_user_card_hash";
	private final String CHECK_OFFER_STATUS_HASH = "check_offer_status_hash";
	private final String CHECK_ISDOMESTIC_HASH = "check_isDomestic_hash";
	private final String VERIFY_PAYMENT_HASH = "verify_payment_hash";

	
	private final String STATUS = "status";
	private final String MESSAGE = "message";
	private final String  RESULT= "result";
	private final String  ERRORCODE= "errorCode";

	
	
	
	//FOr IOS

	public String getHashes(String key2, String amount, String txnid,
			String productinfo, String email, String firstname) {

		JSONObject response = new JSONObject();
		
		
		String ph = checkNull(Common.key) + "|" + checkNull(txnid) + "|" + checkNull(amount) + "|" + checkNull(productinfo)
				+ "|" + checkNull(firstname) + "|" + checkNull(email) + "|||||||||||" + Common.salt;
		String paymentHash = getSHA(ph);
		try {
			response.put(PAYMENT_HASH, paymentHash);
			response.put(STATUS, "0");
			response.put(MESSAGE, "Hash is:");
			response.put(RESULT, paymentHash);
			response.put(ERRORCODE, "");
		/*	response.put(GET_MERCHANT_IBIBO_CODES_HASH, generateHashString("get_merchant_ibibo_codes", "default"));
			response.put(VAS_FOR_MOBILE_SDK_HASH, generateHashString("vas_for_mobile_sdk", "default"));
			response.put(PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK_HASH,
					generateHashString("payment_related_details_for_mobile_sdk", "default"));*/
					
			//for verify payment (optional)
			if (!checkNull(txnid).isEmpty()) {
		/*	response.put(VERIFY_PAYMENT_HASH,
					generateHashString("verify_payment",txnid));*/
			}

		/*	if (!checkNull(user_credentials).isEmpty()) {
				response.put(DELETE_USER_CARD_HASH, generateHashString("delete_user_card", user_credentials));
				response.put(GET_USER_CARDS_HASH, generateHashString("get_user_cards", user_credentials));
				response.put(EDIT_USER_CARD_HASH, generateHashString("edit_user_card", user_credentials));
				response.put(SAVE_USER_CARD_HASH, generateHashString("save_user_card", user_credentials));
				response.put(PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK_HASH,
						generateHashString("payment_related_details_for_mobile_sdk", user_credentials));
			}

			// check_offer_status
			if (!checkNull(offerKey).isEmpty()) {
				response.put(CHECK_OFFER_STATUS_HASH, generateHashString("check_offer_status", offerKey));
			}

			// check_isDomestic
			if (!checkNull(cardBin).isEmpty()) {
				response.put(CHECK_ISDOMESTIC_HASH, generateHashString("check_isDomestic", cardBin));
			}*/

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			try {
				response.put(STATUS, "-1");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		}
	
		return response.toString();

	
	}
	
	
	
	//FOr ANDroid
	public String getHashes(String txnid, String amount, String productInfo, String firstname, String email,
			String user_credentials, String udf1, String udf2, String udf3, String udf4, String udf5, String offerKey,
			String cardBin) {
		JSONObject response = new JSONObject();

		String ph = checkNull(Common.key) + "|" + checkNull(txnid) + "|" + checkNull(amount) + "|" + checkNull(productInfo)
				+ "|" + checkNull(firstname) + "|" + checkNull(email) + "|" + checkNull(udf1) + "|" + checkNull(udf2)
				+ "|" + checkNull(udf3) + "|" + checkNull(udf4) + "|" + checkNull(udf5) + "||||||" + Common.salt;
		String paymentHash = getSHA(ph);
		try {
			response.put(PAYMENT_HASH, paymentHash);
			response.put(STATUS, "0");
			response.put(MESSAGE, "Hash is:");
			response.put(RESULT, paymentHash);
			response.put(ERRORCODE, "00");
			response.put(GET_MERCHANT_IBIBO_CODES_HASH, generateHashString("get_merchant_ibibo_codes", "default"));
			response.put(VAS_FOR_MOBILE_SDK_HASH, generateHashString("vas_for_mobile_sdk", "default"));
			response.put(PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK_HASH,
					generateHashString("payment_related_details_for_mobile_sdk", "default"));
					
			//for verify payment (optional)
			if (!checkNull(txnid).isEmpty()) {
			response.put(VERIFY_PAYMENT_HASH,
					generateHashString("verify_payment",txnid));
			}

			if (!checkNull(user_credentials).isEmpty()) {
				response.put(DELETE_USER_CARD_HASH, generateHashString("delete_user_card", user_credentials));
				response.put(GET_USER_CARDS_HASH, generateHashString("get_user_cards", user_credentials));
				response.put(EDIT_USER_CARD_HASH, generateHashString("edit_user_card", user_credentials));
				response.put(SAVE_USER_CARD_HASH, generateHashString("save_user_card", user_credentials));
				response.put(PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK_HASH,
						generateHashString("payment_related_details_for_mobile_sdk", user_credentials));
			}

			// check_offer_status
			if (!checkNull(offerKey).isEmpty()) {
				response.put(CHECK_OFFER_STATUS_HASH, generateHashString("check_offer_status", offerKey));
			}

			// check_isDomestic
			if (!checkNull(cardBin).isEmpty()) {
				response.put(CHECK_ISDOMESTIC_HASH, generateHashString("check_isDomestic", cardBin));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			try {
				response.put(STATUS, "-1");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		}
	
		return response.toString();

	}

	private String generateHashString(String command, String var1) {
		return getSHA(Common.key + "|" + command + "|" + var1 + "|" + Common.salt);
	}

	private String checkNull(String value) {
		if (value == null) {
			return "";
		} else {
			return value;
		}
	}

	private String getSHA(String str) {

		MessageDigest md;
		String out = "";
		try {
			md = MessageDigest.getInstance("SHA-512");
			md.update(str.getBytes());
			byte[] mb = md.digest();

			for (int i = 0; i < mb.length; i++) {
				byte temp = mb[i];
				String s = Integer.toHexString(new Byte(temp));
				while (s.length() < 2) {
					s = "0" + s;
				}
				s = s.substring(s.length() - 2);
				out += s;
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return out;

	}
	

}
