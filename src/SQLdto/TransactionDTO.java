package SQLdto;

import java.io.Serializable;

public class TransactionDTO implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String UserId,QuizId,Category,SubCategory,Level,Evaluation,Time_taken,Score,Current_timestamp,QuestionAnsMap;

public String getQuestionAnsMap() {
	return QuestionAnsMap;
}

public void setQuestionAnsMap(String questionAnsMap) {
	QuestionAnsMap = questionAnsMap;
}

public String getUserId() {
	return UserId;
}

public void setUserId(String userId) {
	UserId = userId;
}

public String getQuizId() {
	return QuizId;
}

public void setQuizId(String quizId) {
	QuizId = quizId;
}

public String getCategory() {
	return Category;
}

public void setCategory(String category) {
	Category = category;
}

public String getSubCategory() {
	return SubCategory;
}

public void setSubCategory(String subCategory) {
	SubCategory = subCategory;
}

public String getLevel() {
	return Level;
}

public void setLevel(String level) {
	Level = level;
}

public String getEvaluation() {
	return Evaluation;
}

public void setEvaluation(String evaluation) {
	Evaluation = evaluation;
}

public String getTime_taken() {
	return Time_taken;
}

public void setTime_taken(String time_taken) {
	Time_taken = time_taken;
}

public String getScore() {
	return Score;
}

public void setScore(String score) {
	Score = score;
}

public String getCurrent_timestamp() {
	return Current_timestamp;
}

public void setCurrent_timestamp(String current_timestamp) {
	Current_timestamp = current_timestamp;
}


}
