package dto;

public class DevicePaymentDTO {
	int id,AmountOfPlan,DaysOfplan,MultiplicationFactor;
	String PaymentType;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAmountOfPlan() {
		return AmountOfPlan;
	}
	public void setAmountOfPlan(int amountOfPlan) {
		AmountOfPlan = amountOfPlan;
	}
	public int getDaysOfplan() {
		return DaysOfplan;
	}
	public void setDaysOfplan(int daysOfplan) {
		DaysOfplan = daysOfplan;
	}
	public int getMultiplicationFactor() {
		return MultiplicationFactor;
	}
	public void setMultiplicationFactor(int multiplicationFactor) {
		MultiplicationFactor = multiplicationFactor;
	}
	public String getPaymentType() {
		return PaymentType;
	}
	public void setPaymentType(String paymentType) {
		PaymentType = paymentType;
	}
	
}
