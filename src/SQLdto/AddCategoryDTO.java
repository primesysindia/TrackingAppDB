package SQLdto;

public class AddCategoryDTO {
	String  Categoryid,CategoryName, PaymentTypeId,HalfYearly,MonthlyAmt,Quarterly,Yearly;
 String Message,Error,QuizId;
	public String getQuizId() {
	return QuizId;
}

public void setQuizId(String quizId) {
	QuizId = quizId;
}

	public String getMessage() {
	return Message;
}

public void setMessage(String message) {
	Message = message;
}

public String getError() {
	return Error;
}

public void setError(String error) {
	Error = error;
}

	public String getCategoryid() {
		return Categoryid;
	}

	public void setCategoryid(String categoryid) {
		Categoryid = categoryid;
	}

	public String getCategoryName() {
		return CategoryName;
	}

	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}

	public String getPaymentTypeId() {
		return PaymentTypeId;
	}

	public void setPaymentTypeId(String paymentTypeId) {
		PaymentTypeId = paymentTypeId;
	}

	public String getHalfYearly() {
		return HalfYearly;
	}

	public void setHalfYearly(String halfYearly) {
		HalfYearly = halfYearly;
	}

	public String getMonthlyAmt() {
		return MonthlyAmt;
	}

	public void setMonthlyAmt(String monthlyAmt) {
		MonthlyAmt = monthlyAmt;
	}

	public String getQuarterly() {
		return Quarterly;
	}

	public void setQuarterly(String quarterly) {
		Quarterly = quarterly;
	}

	public String getYearly() {
		return Yearly;
	}

	public void setYearly(String yearly) {
		Yearly = yearly;
	}
	
	
	
}
