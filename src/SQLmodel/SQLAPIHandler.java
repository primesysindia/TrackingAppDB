package SQLmodel;

import java.sql.Connection;
import java.util.ArrayList;

import org.json.JSONString;

import dao.Database;
import dto.MessageObject;
import SQLdao.SQL_QuizDAO;
import SQLdto.AddCategoryDTO;
import SQLdto.CategoryDetailsDTO;
import SQLdto.ClassDTO;
import SQLdto.ExamDTO;
import SQLdto.LevelDTO;
import SQLdto.QuizMetadataDTO;
import SQLdto.SQL_QuizOverallDetailsDTO;
import SQLdto.TransactionResultDTO;
import SQLdto.UserPaymentDTO;

public class SQLAPIHandler {

	Connection connection=null;
	Database database=null;
	MessageObject msgObj;



public SQLAPIHandler() {
		
		try
		{
			connection = Database.getInstance().conn;
		//	mysqlconnection=database.Get_MysqlConnection();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
	}
	

	public ArrayList<CategoryDetailsDTO>  GetCategories(String user_id,String School_id, String roleid){
		ArrayList<CategoryDetailsDTO>  list=new ArrayList<CategoryDetailsDTO>();
		try {


			SQL_QuizDAO dao=new SQL_QuizDAO();
			list=dao.GetCategories(connection,user_id,School_id,roleid);
		} 
		catch (Exception e) {

			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;

	}
	public QuizMetadataDTO  GetMetaData(String School_id){
		QuizMetadataDTO  list= new QuizMetadataDTO();
		try {


			SQL_QuizDAO dao=new SQL_QuizDAO();
			list=dao.GetMetaData(connection,School_id);
		} 
		catch (Exception e) {

			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;

	}
	public  ArrayList<LevelDTO> GetLevelList(String userid,String quizid){
		ArrayList<LevelDTO>  list=new ArrayList<LevelDTO>();
		try {


			SQL_QuizDAO dao=new SQL_QuizDAO();
			list=dao.GetLevelList(connection,userid,quizid);
		} 
		catch (Exception e) {

			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;

	}
	public  ArrayList<ExamDTO> GetQuestions(String quizid,String userid,String category,String leveno){
		ArrayList<ExamDTO>  list=new ArrayList<ExamDTO>();
		try {


			SQL_QuizDAO dao=new SQL_QuizDAO();
			list=dao.GetQuestions(connection,quizid,userid,category,leveno);
		} 
		catch (Exception e) {

			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;

	}

	
	public  TransactionResultDTO Postscore(String JsonString){
		TransactionResultDTO list=new TransactionResultDTO();
		try {


			SQL_QuizDAO dao=new SQL_QuizDAO();
			list=dao.Postscore(connection,JsonString);
		} 
		catch (Exception e) {

			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;

	}
	public  ArrayList<LevelDTO> GetLevelwiseScoreRank(String Quiz_Id,String category,String userId,String schoolid){
		ArrayList<LevelDTO>  list=new ArrayList<LevelDTO> ();
		try {


			SQL_QuizDAO dao=new SQL_QuizDAO();
			list=dao.GetLevelwiseScoreRank(connection,Quiz_Id,category,userId,schoolid);
		} 
		catch (Exception e) {

			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;

	}
	public  ArrayList<SQL_QuizOverallDetailsDTO> GetOveralrankScore(String Quiz_Id,String category,String userId,String schoolid){
		ArrayList<SQL_QuizOverallDetailsDTO>  list=new ArrayList<SQL_QuizOverallDetailsDTO> ();
		try {


			SQL_QuizDAO dao=new SQL_QuizDAO();
			list=dao.GetOveralrankScore(connection,Quiz_Id,category,userId,schoolid);
		} 
		catch (Exception e) {

			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;

	}


	public ArrayList<SQL_QuizOverallDetailsDTO> UsersOverallScoreDetails(
			String quiz_Id, String category, String userId, String schoolid,
			String levelno) {
		ArrayList<SQL_QuizOverallDetailsDTO>  list=new ArrayList<SQL_QuizOverallDetailsDTO> ();
		try {


			SQL_QuizDAO dao=new SQL_QuizDAO();
			list=dao.UsersOverallScoreDetails(connection,quiz_Id,category,userId,schoolid,levelno);
		} 
		catch (Exception e) {

			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;

	}


	public UserPaymentDTO GetUserInfo(String parentId) {

		UserPaymentDTO list=new UserPaymentDTO();
		try {


			SQL_QuizDAO dao=new SQL_QuizDAO();
			list=dao.GetUserInfo(connection,parentId);
		}
		catch (Exception e) {

			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;

	
	}


	public ArrayList<AddCategoryDTO> AllCategoryForAdd(String SelectedClass) {
		// TODO Auto-generated method stub
		ArrayList<AddCategoryDTO>  list=new ArrayList<AddCategoryDTO> ();
		try {


			SQL_QuizDAO dao=new SQL_QuizDAO();
			list=dao.AllCategoryForAdd(connection,SelectedClass);
		} 
		catch (Exception e) {

			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;
	}


	
	public TransactionResultDTO UpdateQuizPayment(String parentId,
			String payment_type, String paymentId, String activeStatus,
			String selectedCategory, String totalAmount) {
		TransactionResultDTO list=new TransactionResultDTO();
		try {


			SQL_QuizDAO dao=new SQL_QuizDAO();
			list=dao.UpdateQuizPayment(connection,parentId,payment_type,paymentId,activeStatus,selectedCategory,totalAmount);
		} 
		catch (Exception e) {

			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;

	}


	public ArrayList<ClassDTO> AllClassForAdd() {
		ArrayList<ClassDTO> list=new ArrayList<ClassDTO> ();
		try {


			SQL_QuizDAO dao=new SQL_QuizDAO();
			list=dao.AllClassForAdd(connection);
		} 
		catch (Exception e) {
		}
		return list;

	}


	public TransactionResultDTO PostscoreApple(String result,String Qarry) {
		TransactionResultDTO list=new TransactionResultDTO();
		try {


			SQL_QuizDAO dao=new SQL_QuizDAO();
			list=dao.PostscoreApple(connection,result,Qarry);
		} 
		catch (Exception e) {

			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return list;
	}




}
