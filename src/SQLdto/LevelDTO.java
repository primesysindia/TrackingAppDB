package SQLdto;

import java.io.Serializable;

public class LevelDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String getMessage() {
		return Message;
	}
	private String Rank,Status,Score;
	private String LevelNo,LevelDescription;
	
	
	public String getLevelNo() {
		return LevelNo;
	}
	public void setLevelNo(String levelNo) {
		LevelNo = levelNo;
	}
	public String getLevelDescription() {
		return LevelDescription;
	}
	public void setLevelDescription(String levelDescription) {
		LevelDescription = levelDescription;
	}
	public String getScore() {
		return Score;
	}
	public void setScore(String score) {
		Score = score;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getError() {
		return Error;
	}
	public String getRank() {
		return Rank;
	}
	public void setRank(String rank) {
		Rank = rank;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public void setError(String error) {
		Error = error;
	}
	private String Level,Question,Answers;
	private String Message,Error;
	public String getLevel() {
		return Level;
	}
	public void setLevel(String level) {
		Level = level;
	}
	public String getQuestion() {
		return Question;
	}
	public void setQuestion(String question) {
		Question = question;
	}
	public String getAnswers() {
		return Answers;
	}
	public void setAnswers(String answers) {
		Answers = answers;
	}


}
