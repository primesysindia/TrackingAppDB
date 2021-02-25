package SQLdto;

import java.io.Serializable;

public class QuizMetadataDTO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String showanw_time_interval;
	private String shownext_question_interval;
	private String maximum_marks;
	private  String bonus_time_interval;
	private String   minimum_marks_question;
	private  String per_second_bonus_marks;
	private String passing_marks;
	private String message,error;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getPassing_marks() {
		return passing_marks;
	}
	public void setPassing_marks(String passing_marks) {
		this.passing_marks = passing_marks;
	}
	public String getPer_second_bonus_marks() {
		return per_second_bonus_marks;
	}
	public void setPer_second_bonus_marks(String per_second_bonus_marks) {
		this.per_second_bonus_marks = per_second_bonus_marks;
	}
	public String getShowanw_time_interval() {
		return showanw_time_interval;
	}
	public void setShowanw_time_interval(String showanw_time_interval) {
		this.showanw_time_interval = showanw_time_interval;
	}
	public String getShownext_question_interval() {
		return shownext_question_interval;
	}
	public void setShownext_question_interval(String shownext_question_interval) {
		this.shownext_question_interval = shownext_question_interval;
	}
	public String getMaximum_marks() {
		return maximum_marks;
	}
	public void setMaximum_marks(String maximum_marks) {
		this.maximum_marks = maximum_marks;
	}
	public String getBonus_time_interval() {
		return bonus_time_interval;
	}
	public void setBonus_time_interval(String bonus_time_interval) {
		this.bonus_time_interval = bonus_time_interval;
	}
	public String getMinimum_marks_question() {
		return minimum_marks_question;
	}
	public void setMinimum_marks_question(String minimum_marks_question) {
		this.minimum_marks_question = minimum_marks_question;
	}


}
