package dto;

public class FamilyPaymentTypeDTO {

	String PaymentTypeId	,PaymentTypeName,	AmountOfPlan,	DaysOfplan,	MultiplicationFactor;

	public String getPaymentTypeId() {
		return PaymentTypeId;
	}

	public void setPaymentTypeId(String paymentTypeId) {
		PaymentTypeId = paymentTypeId;
	}

	public String getPaymentTypeName() {
		return PaymentTypeName;
	}

	public void setPaymentTypeName(String paymentTypeName) {
		PaymentTypeName = paymentTypeName;
	}

	public String getAmountOfPlan() {
		return AmountOfPlan;
	}

	public void setAmountOfPlan(String amountOfPlan) {
		AmountOfPlan = amountOfPlan;
	}

	public String getDaysOfplan() {
		return DaysOfplan;
	}

	public void setDaysOfplan(String daysOfplan) {
		DaysOfplan = daysOfplan;
	}

	public String getMultiplicationFactor() {
		return MultiplicationFactor;
	}

	public void setMultiplicationFactor(String multiplicationFactor) {
		MultiplicationFactor = multiplicationFactor;
	}
	
	
}
