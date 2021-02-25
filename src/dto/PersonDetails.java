package dto;

import java.io.Serializable;

public class PersonDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Name,Address,id,ContactNumber,EmailID,photo,status,Invi_Id,datediff, PaymentTypeId
;

	public String getDatediff() {
		return datediff;
	}

	public void setDatediff(String datediff) {
		this.datediff = datediff;
	}

	public String getPaymentTypeId() {
		return PaymentTypeId;
	}

	public void setPaymentTypeId(String paymentTypeId) {
		PaymentTypeId = paymentTypeId;
	}

	public String getInvi_Id() {
		return Invi_Id;
	}

	public void setInvi_Id(String invi_Id) {
		Invi_Id = invi_Id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContactNumber() {
		return ContactNumber;
	}

	public void setContactNumber(String contactNumber) {
		ContactNumber = contactNumber;
	}

	public String getEmailID() {
		return EmailID;
	}

	public void setEmailID(String emailID) {
		EmailID = emailID;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	
}
