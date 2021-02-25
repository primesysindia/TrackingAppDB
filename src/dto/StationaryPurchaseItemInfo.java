package dto;

import java.sql.Connection;

public class StationaryPurchaseItemInfo {

	String AvialQty,sellingPrice,Quantity,Totalamount,Error,ItemTypeId;

	public String getItemTypeId() {
		return ItemTypeId;
	}

	public void setItemTypeId(String itemTypeId) {
		ItemTypeId = itemTypeId;
	}

	public String getError() {
		return Error;
	}

	public void setError(String error) {
		Error = error;
	}

	public String getAvialQty() {
		return AvialQty;
	}

	public void setAvialQty(String avialQty) {
		AvialQty = avialQty;
	}

	public String getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(String sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public String getQuantity() {
		return Quantity;
	}

	public void setQuantity(String quantity) {
		Quantity = quantity;
	}

	public String getTotalamount() {
		return Totalamount;
	}

	public void setTotalamount(String totalamount) {
		Totalamount = totalamount;
	}

	public StationaryPurchaseItemInfo StationaryPurchaseItemInfo(
			Connection con, String cat_id, String subcat_id, String class_id,
			String subject_id) {
		// TODO Auto-generated method stub
		return null;
	}
}
