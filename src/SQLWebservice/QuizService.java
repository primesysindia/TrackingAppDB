package SQLWebservice;

import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import model.APIController;

import org.json.JSONString;

import com.google.gson.Gson;

import SQLdto.AddCategoryDTO;
import SQLdto.CategoryDetailsDTO;
import SQLdto.ClassDTO;
import SQLdto.ExamDTO;
import SQLdto.LevelDTO;
import SQLdto.QuizMetadataDTO;
import SQLdto.SQL_QuizOverallDetailsDTO;
import SQLdto.TransactionResultDTO;
import SQLdto.UserPaymentDTO;
import SQLmodel.SQLAPIHandler;


@Path("/SQLQuiz")
public class QuizService {

	/*Author:Amit Patil
	 *Date:06.11.2015
	 * API:To GET CategoryList
	 * 
	 */
	@POST
	@Path("/GetCategories")
	public String GetCategories(@FormParam("UserId") String userId,@FormParam("School_id") String School_id,@FormParam("RoleId") String roleid){
		String feed=null;
		ArrayList<CategoryDetailsDTO> feedObject=new ArrayList<CategoryDetailsDTO>();
		SQLAPIHandler handler= new SQLAPIHandler();
		feedObject = handler.GetCategories(userId,School_id,roleid);
		Gson gson = new Gson();
		feed = gson.toJson(feedObject);
		APIController.SaveApiReport("Quiz", "GetCategories",  "GetCategories",userId,
				"Get Quiz Categoery",  feed,  "Java","null", "null");
		return feed;
	}
	/*Author:Amit Patil
	 *Date:06.11.2015
	 * API:To  GetMetadata
	 * 
	 */
	@POST
	@Path("/GetMetaData")
	public String GetMetaData(@FormParam("school_id") String School_id){
		String feed=null;
		QuizMetadataDTO feedObject=new QuizMetadataDTO();
		SQLAPIHandler handler= new SQLAPIHandler();
		feedObject = handler.GetMetaData(School_id);
		Gson gson = new Gson();
		feed = gson.toJson(feedObject);
		APIController.SaveApiReport("Quiz", "GetMetaData",  "GetMetaData",School_id,
				"Get Metadata of Quiz (total score,time interval etc)",  feed,  "Java","null", "null");
		return feed;
	}

	/*		Author:Amit Patil
	 *Date:06.11.2015
	 * API:To  GetMetadata
	 * 
	 */
	@POST
	@Path("/GetLevelList")
	public String GetLevelList(@FormParam("user_id") String user_id,@FormParam("quizid") String quizid){
		String feed=null;
		ArrayList<LevelDTO> feedObject=new ArrayList<LevelDTO>();
		SQLAPIHandler handler= new SQLAPIHandler();
		feedObject = handler.GetLevelList(user_id,quizid);
		Gson gson = new Gson();
		feed = gson.toJson(feedObject);
		APIController.SaveApiReport("Quiz", "GetLevelList",  "GetLevelList",user_id,
				"Get LevelList Quiz of Categoery",  feed,  "Java","null", "null");
		return feed;
	}


	/*		Author:Amit Patil
	 *Date:06.11.2015
	 * API:To  GetMetadata
	 * 
	 */
	@POST
	@Path("/GetQuestions")
	public String GetQuestions(@FormParam("QuizId") String quizid,@FormParam("UserId") String userid,@FormParam("Category") String category,@FormParam("LevelNo") String leveno){
		String feed=null;
		ArrayList<ExamDTO> feedObject=new ArrayList<ExamDTO>();
		SQLAPIHandler handler= new SQLAPIHandler();
		feedObject = handler.GetQuestions(quizid,userid,category,leveno);
		Gson gson = new Gson();
		feed = gson.toJson(feedObject);
		APIController.SaveApiReport("Quiz", "Get Questions",  "GetQuestions",userid,
				"Get Questions of selected  Categoery and level",  feed,  "Java","null", "null");
		return feed;
	}

	/*		
	 * 
	 * Author:Amit Patil
	 *Date:09.11.2015
	 * API:POST Score
	 * 
	 */
	@POST
	@Path("/Postscore")
	public String postscore(@FormParam("Result") String JoObject){
		String feed=null;

		System.out.println("-----------------------I N P U T-------------------------------"+JoObject);
		TransactionResultDTO feedObject=new TransactionResultDTO();
		SQLAPIHandler handler= new SQLAPIHandler();
		feedObject = handler.Postscore(JoObject);
		Gson gson = new Gson();
		feed = gson.toJson(feedObject);
		
		
		System.out.println("-----------------------O U T P U T-------------------------------"+feed);
		APIController.SaveApiReport("Quiz", "Postscore",  "Postscore",null,
				"Post score on server",  feed,  "Java","null", "null");
		return feed;
	}

	/*		
	 * 
	 * Author:Amit Patil
	 *Date:09.11.2015
	 * API:POST Score
	 * 
	 */
	@POST
	@Path("/GetLevelwiseScoreRank")
	public String GetLevelwiseRank(@FormParam("Quiz_Id") String Quiz_Id,@FormParam("Category") String category,@FormParam("UserId") String userId,@FormParam("SchoolId") String schoolid){
		String feed=null;
		
		System.out.println("GetLevelwiseScoreRank  ---"+Quiz_Id+" "+category+" "+userId+" "+schoolid);
		ArrayList<LevelDTO> list=new ArrayList<LevelDTO>();
		SQLAPIHandler handler= new SQLAPIHandler();
		list = handler.GetLevelwiseScoreRank(Quiz_Id,category,userId,schoolid);
		Gson gson = new Gson();
		feed = gson.toJson(list);
		System.err.println("GetLevelwiseScoreRank :"+feed);
		APIController.SaveApiReport("Quiz", "GetLevelwiseScoreRank",  "GetLevelwiseScoreRank",userId,
				"Get Levelwise Score Rank ",  feed,  "Java","null", "null");
		return feed;
	}

	/*		
	 * 
	 * Author:Amit Patil
	 *Date:09.11.2015
	 * API:POST Score
	 * 
	 */
	@POST
	@Path("/GetOveralrankScore")
	public String GetOveralrankScore(@FormParam("Quiz_Id") String Quiz_Id,@FormParam("Category") String category,@FormParam("UserId") String userId,@FormParam("SchoolId") String schoolid){
		String feed=null;
		ArrayList<SQL_QuizOverallDetailsDTO> list=new ArrayList<SQL_QuizOverallDetailsDTO>();
		SQLAPIHandler handler= new SQLAPIHandler();
		list = handler.GetOveralrankScore(Quiz_Id,category,userId,schoolid);
		Gson gson = new Gson();
		feed = gson.toJson(list);
		APIController.SaveApiReport("Quiz", "GetOveralrankScore",  "GetOveralrankScore",userId,
				"Get Overalrank and Score",  feed,  "Java","null", "null");
		return feed;
	}

	
	
	/*		
	 * 
	 * Author:Amit Patil
	 *Date:09.11.2015
	 * API:POST Score
	 * 
	 */	
	@POST
	@Path("/UsersOverallScoreDetails")
	public String UsersOverallScoreDetails(@FormParam("Quizid") String Quiz_Id,@FormParam("Category") String category,@FormParam("UserId") String userId,@FormParam("SchoolId") String schoolid,@FormParam("Levelno") String levelno){
		String feed=null;
		ArrayList<SQL_QuizOverallDetailsDTO> list=new ArrayList<SQL_QuizOverallDetailsDTO>();
		SQLAPIHandler handler= new SQLAPIHandler();
		list = handler.UsersOverallScoreDetails(Quiz_Id,category,userId,schoolid,levelno);
		Gson gson = new Gson();
		feed = gson.toJson(list);
		APIController.SaveApiReport("Quiz", "UsersOverallScoreDetails",  "UsersOverallScoreDetails",userId,
				"Get UsersOverall Score Details for showing on map",  feed,  "Java","null", "null");
		return feed;
	}
	
	
	
	@POST
	@Path("/GetUserInfo")
	public String GetUserInfo(@FormParam("ParentId") String ParentId){
		String feed=null;
		UserPaymentDTO list=new UserPaymentDTO();
		SQLAPIHandler handler= new SQLAPIHandler();
		list = handler.GetUserInfo(ParentId);
		Gson gson = new Gson();
		feed = gson.toJson(list);
		APIController.SaveApiReport("Quiz", "GetUserInfo",  "GetUserInfo",ParentId,
				"Get Get User Info",  feed,  "Java","null", "null");
		return feed;
	}
	
	@POST
	@Path("/UpdateQuizPayment")
	public String UpdateQuizPayment(@FormParam("ParentId") String ParentId,@FormParam("typeofpayment") String payment_type,@FormParam("paymentId") String paymentId,
		@FormParam("ActiveStatus") String ActiveStatus,@FormParam("SelectedCategory") String SelectedCategory,@FormParam("TotalAmount") String TotalAmount){
	String feed=null;
	TransactionResultDTO list=new TransactionResultDTO();
	SQLAPIHandler handler= new SQLAPIHandler();
	list = handler.UpdateQuizPayment(ParentId,payment_type,paymentId,ActiveStatus,SelectedCategory,TotalAmount);
	Gson gson = new Gson();
	feed = gson.toJson(list);
	APIController.SaveApiReport("Quiz", "UpdateQuizPayment",  "UpdateQuizPayment",ParentId,
			"Update Quiz Payment",  feed,  "Java","null", "null");
	return feed;
}
	



	@POST
	@Path("/AllCategoryForAdd")
	public String AllCategoryForAdd(@FormParam("SelectedClass") String SelectedClass){
		String feed=null;
	ArrayList<AddCategoryDTO> list=new ArrayList<AddCategoryDTO>();
		SQLAPIHandler handler= new SQLAPIHandler();
		list = handler.AllCategoryForAdd(SelectedClass);
		Gson gson = new Gson();
		feed = gson.toJson(list);
		APIController.SaveApiReport("Quiz", "Get AllCategoryForAdd",  "AllCategoryForAdd",SelectedClass,
				"Get All Category For Add at the time of paymentor purchase",  feed,  "Java","null", "null");
		return feed;
	}
	
	
	@POST
	@Path("/AllClassForAdd")
	public String AllClassForAdd(){
		String feed=null;
	ArrayList<ClassDTO> list=new ArrayList<ClassDTO>();
		SQLAPIHandler handler= new SQLAPIHandler();
		list = handler.AllClassForAdd();
		Gson gson = new Gson();
		feed = gson.toJson(list);
		APIController.SaveApiReport("Quiz", "Get AllClassForAdd",  "AllClassForAdd",null,
				"Get  All Class For Add at the time of payment or purchase",  feed,  "Java","null", "null");
		return feed;
	}	
	
	@POST
	@Path("/PostscoreApple")
	public String PostscoreApple(@FormParam("Result") String result,@FormParam("QuestionArray") String qarray){
		String feed=null;

		System.out.println("-----------------------I N P U T-------------------------------"+result);
		System.out.println("-----------------------I N P U T-------------------------------"+qarray);

		TransactionResultDTO feedObject=new TransactionResultDTO();
		SQLAPIHandler handler= new SQLAPIHandler();
		feedObject = handler.PostscoreApple(result,qarray);
		Gson gson = new Gson();
		feed = gson.toJson(feedObject);
		
		
		System.out.println("-----------------------O U T P U T-------"
				+ "------------------------"+feed);
		APIController.SaveApiReport("Quiz", "Post score of Apple Api",  "PostscoreApple",null,
				"Post score which come from apple Apple",  feed,  "Java","null", "null");
		return feed;
	}

	


	
}
