package SQLdto;

import java.io.Serializable;

public class TransactionResultDTO implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String attempts,score,message,error;

public String getAttempts() {
	return attempts;
}

public void setAttempts(String attempts) {
	this.attempts = attempts;
}

public String getScore() {
	return score;
}

public void setScore(String score) {
	this.score = score;
}

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

}
