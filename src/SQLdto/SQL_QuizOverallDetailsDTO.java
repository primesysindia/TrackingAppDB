package SQLdto;

import java.io.Serializable;

public class SQL_QuizOverallDetailsDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name,proimage,rank,score,levelrank;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLevelrank() {
		return levelrank;
	}
	public void setLevelrank(String levelrank) {
		this.levelrank = levelrank;
	}
	public String getProimage() {
		return proimage;
	}
	public void setProimage(String proimage) {
		this.proimage = proimage;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
	
	
}
