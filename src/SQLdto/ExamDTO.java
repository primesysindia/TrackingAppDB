package SQLdto;

import java.util.ArrayList;

public class ExamDTO {

	String QuestionId,QuestionLabel,OptionId,OptionLabel,AnswerOptionId,ExamMasterId,Result,Category,
	ClassId,Age,SubCategory,QuizId,LevelNo,UserId,AnsType,SchoolId,RoleId;
 public String getRoleId() {
		return RoleId;
	}

	public void setRoleId(String roleId) {
		RoleId = roleId;
	}

public String getSchoolId() {
		return SchoolId;
	}

	public void setSchoolId(String schoolId) {
		SchoolId = schoolId;
	}

public String getUserId() {
		return UserId;
	}

	public String getAnsType() {
	return AnsType;
}

public void setAnsType(String ansType) {
	AnsType = ansType;
}

	public void setUserId(String userId) {
		UserId = userId;
	}

public String getResult() {
		return Result;
	}

	public String getCategory() {
	return Category;
}

public void setCategory(String category) {
	Category = category;
}

public String getClassId() {
	return ClassId;
}

public void setClassId(String classId) {
	ClassId = classId;
}

public String getAge() {
	return Age;
}

public void setAge(String age) {
	Age = age;
}

public String getSubCategory() {
	return SubCategory;
}

public void setSubCategory(String subCategory) {
	SubCategory = subCategory;
}

public String getQuizId() {
	return QuizId;
}

public void setQuizId(String quizId) {
	QuizId = quizId;
}


	public String getLevelNo() {
	return LevelNo;
}

public void setLevelNo(String levelNo) {
	LevelNo = levelNo;
}

	public void setResult(String result) {
		Result = result;
	}

public String getExamMasterId() {
		return ExamMasterId;
	}

	public void setExamMasterId(String examMasterId) {
		ExamMasterId = examMasterId;
	}

ArrayList<OptionDTO> list;
	public ArrayList<OptionDTO> getList() {
	return list;
}

public void setList(ArrayList<OptionDTO> olist) {
	this.list = olist;
}

	public String getQuestionId() {
		return QuestionId;
	}

	public void setQuestionId(String questionId) {
		QuestionId = questionId;
	}

	public String getQuestionLabel() {
		return QuestionLabel;
	}

	public void setQuestionLabel(String questionLabel) {
		QuestionLabel = questionLabel;
	}

	public String getOptionId() {
		return OptionId;
	}

	public void setOptionId(String optionId) {
		OptionId = optionId;
	}

	public String getOptionLabel() {
		return OptionLabel;
	}

	public void setOptionLabel(String optionLabel) {
		OptionLabel = optionLabel;
	}

	public String getAnswerOptionId() {
		return AnswerOptionId;
	}

	public void setAnswerOptionId(String answerOptionId) {
		AnswerOptionId = answerOptionId;
	}
}
