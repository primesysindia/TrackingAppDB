package dto;
/**
 * Generate DTO for Task Management Address 
 * 
 */
public class TaskMgntAddressDTO {
	String address_long,address_short ;
	Double lat,lan;

	int parentId,isDefault,address_id;

	public int getAddress_id() {
		return address_id;
	}

	public void setAddress_id(int address_id) {
		this.address_id = address_id;
	}

	public String getAddress_long() {
		return address_long;
	}

	public void setAddress_long(String address_long) {
		this.address_long = address_long;
	}

	public String getAddress_short() {
		return address_short;
	}

	public void setAddress_short(String address_short) {
		this.address_short = address_short;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLan() {
		return lan;
	}

	public void setLan(Double lan) {
		this.lan = lan;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

}
