package SQLdao;

import java.io.Writer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;





import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import SQLdto.AddCategoryDTO;
import SQLdto.CategoryDetailsDTO;
import SQLdto.ClassDTO;
import SQLdto.ExamDTO;
import SQLdto.LevelDTO;
import SQLdto.OptionDTO;
import SQLdto.QuizMetadataDTO;
import SQLdto.SQL_QuizOverallDetailsDTO;
import SQLdto.TransactionResultDTO;
import SQLdto.UserPaymentDTO;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dao.Common;
import dto.PersonDetails;

public class SQL_QuizDAO {




	public ArrayList<CategoryDetailsDTO> GetCategories(Connection connection,
			String user_id, String school_id, String roleid) {
		ArrayList<CategoryDetailsDTO> list=new ArrayList<CategoryDetailsDTO>();
		try{

		/*	System.err.println("Userid: -------hhh------>"+user_id);
			PreparedStatement pcal=connection.prepareStatement("select cat.Categor
			yName as categoryname,usr.ExpiryDate,usr.Category_Desc as categorydesc,cat.Photo as categoryImage,usr.Created_by as createdby,usr.Quiz_Id as quizid,usr.School_Id as schoolid from dbo.Category cat inner join UserQuizMap usr on usr.Category = cat.id where usr.UserId = (select UserID from UserLoginMaster where Link_ID=?) ");
			pcal.setString(1, user_id);
			ResultSet rsResultSet=pcal.executeQuery();
			*/
			
			java.sql.CallableStatement cal=connection.prepareCall("{ call GetUserCategories(?) }");
			cal.setInt(1, Integer.parseInt(user_id));
			
			ResultSet rsResultSet =cal.executeQuery();
		
		if(rsResultSet!=null)	{	
				while (rsResultSet.next()) {


					CategoryDetailsDTO dto=new CategoryDetailsDTO();
					dto.setCategory(""+rsResultSet.getString("categoryname"));
					dto.setCategory_Desc(""+rsResultSet.getString("categorydesc"));
					 int day=Integer.parseInt(rsResultSet.getString("DiffDate"));
					 if(day>0)
						 dto.setActiveStatus("Active");
					 else
						 dto.setActiveStatus("Deactive");
					dto.setCategory_Image(""+rsResultSet.getString("categoryImage"));
					dto.setCreated_by(""+rsResultSet.getString("createdby"));
					dto.setQuiz_Id(""+rsResultSet.getString("quizid"));
					dto.setSchool_Id(""+rsResultSet.getString("schoolid"));
					dto.setUserId(""+user_id);
					
					System.out.println("categoryyyy== "+dto.getCategory()+"  "+day);
					list.add(dto);

				}
				System.err.println("GetCategory list: --------"+list.toString());


			}




		}catch(Exception e){

			e.printStackTrace();
			CategoryDetailsDTO dto=new CategoryDetailsDTO();
			dto.setMessage("Data Not Found");
			dto.setError("false");
			list.add(dto);
		}
		return list;
	}

	
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.dd.MM");

	public static long getDayCount(String start, String end) {
	  long diff = -1;
	  try {
	    java.util.Date dateStart = simpleDateFormat.parse(start);
	    java.util.Date dateEnd = simpleDateFormat.parse(end);

	    //time is always 00:00:00 so rounding should help to ignore the missing hour when going from winter to summer time as well as the extra hour in the other direction
	    diff = Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);
	  } catch (Exception e) {
	    //handle the exception according to your own situation
	  }
	  return diff;
	}
	
	
	
	public QuizMetadataDTO GetMetaData(Connection connection,
			String school_id) {
		QuizMetadataDTO list=new QuizMetadataDTO();
		try{


			PreparedStatement pcal=connection.prepareStatement("SELECT * FROM QuizMetadata WHERE school_id = ? ");
			pcal.setString(1, school_id);
			ResultSet rsResultSet=pcal.executeQuery();
			if (rsResultSet!=null) {
				while (rsResultSet.next()) {



					list.setBonus_time_interval(rsResultSet.getString("bonus_time_interval"));
					list.setMaximum_marks(rsResultSet.getString("maximum_marks"));
					list.setMinimum_marks_question(rsResultSet.getString("minimum_marks_question"));
					list.setPassing_marks(rsResultSet.getString("passing_marks"));
					list.setPer_second_bonus_marks(rsResultSet.getString("per_second_bonus_marks"));
					list.setShowanw_time_interval(rsResultSet.getString("showanw_time_interval"));
					list.setShownext_question_interval(rsResultSet.getString("shownext_question_interval"));

				}

			} else{
				System.err.println("Error: ------------->");

				list.setMessage("Data Not Found");
				list.setError("false");

			}




		}catch(Exception e){

			list.setMessage("Data Not Found");
			list.setError("Error :"+e.getMessage());

		}
		return list;
	}



	public ArrayList<LevelDTO> GetLevelList(Connection connection,
			String user_id, String quizid) throws SQLException {
		ArrayList<LevelDTO> list=new ArrayList<LevelDTO>();
		try{
			//System.err.println("Error: ------------->"+quizid);
			PreparedStatement pcal=connection.prepareStatement("select M.LevelNo as leveno,M.LevelDescription as decs from QuizMaster Q inner join LevelMapping M on Q.QuizId = M.QuizId Where M.QuizId = ?");
			pcal.setString(1, quizid);
			ResultSet rsResultSet=pcal.executeQuery();
			if (rsResultSet!=null) {
				while (rsResultSet.next()) {


					LevelDTO dto=new LevelDTO();
					dto.setLevel(""+rsResultSet.getString("leveno"));
					dto.setLevelDescription(""+rsResultSet.getString("decs"));
					list.add(dto);
				}

			} else{
				System.err.println("Error: ------------->");
				LevelDTO dto=new LevelDTO();
				dto.setMessage("Data Not Found");
				dto.setError("true");
				list.add(dto);
			}
		}catch(Exception e){
			LevelDTO dto=new LevelDTO();
			dto.setMessage("Error: "+e.getMessage());
			dto.setError("true");
			list.add(dto);
		}


		return list;
	}
	/***
	 * 
	 * 
	 */
	public ArrayList<ExamDTO> GetQuestions(Connection connection,
			String quizid, String userid,String category,String leveno) throws SQLException {
		ArrayList<ExamDTO> list=new ArrayList<ExamDTO>();
		try{

			//1.Level 


			String id=null;
			ArrayList<String> arr_question=new ArrayList<String>();
			PreparedStatement pps=connection.prepareStatement("select Id from  LevelMapping where QuizId = ? and LevelNo = ?  ");
			pps.setString(1,quizid);
			pps.setString(2, leveno);
			ResultSet rs =pps.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					id=rs.getString("Id");
				}
			}
	//		System.out.println(id);
			PreparedStatement pps1=connection.prepareStatement("select QuestionId from QuizDetails where LevelId = ? order by NEWID()");
			pps1.setString(1, id);
			ResultSet rs1=pps1.executeQuery();
			if (rs1!=null) {
				while (rs1.next()) {

					arr_question.add(rs1.getString("QuestionId"));
				}
			}
		//	System.out.println(arr_question.toString());
			Iterator<String> itm=arr_question.iterator();
			while (itm.hasNext()) {
				PreparedStatement pps2=connection.prepareStatement("Select * from QuestionMaster where QuestionId = ? ");
				pps2.setString(1, itm.next());
				ResultSet rs2=pps2.executeQuery();
				if (rs2!=null) {
					while(rs2.next()) {
						//System.out.println("Inside data 2222 "+cursor1.next());
						ExamDTO dto=new ExamDTO();

						//						BasicDBObject obj2 = (BasicDBObject) cursor1.curr();
						dto.setQuestionId(rs2.getString("QuestionId"));
						dto.setQuestionLabel(rs2.getString("QuestionLabel"));
						dto.setAnswerOptionId(rs2.getString("CorrectAnswer"));
						dto.setAnsType(rs2.getString("TypeOfAns"));
						//	BasicBSONList optionArray = (BasicBSONList) obj2.get("QuestionAnsOption");
						ArrayList<OptionDTO> olist=new ArrayList<OptionDTO>();

						for (int i=1;i<5;i++) {
							OptionDTO option=new OptionDTO();
							option.setOptionLabel(rs2.getString("OptionLabel"+i));
							olist.add(option);
							dto.setList(olist);

						}

						list.add(dto);
					}

				}


			}

		}catch(Exception e){

		}
		System.err.println(list.toString());
		return list;
	}

	public TransactionResultDTO Postscore(Connection connection,String JsonObject) 
			throws JSONException, SQLException{
		TransactionResultDTO msg=new TransactionResultDTO();
	
	//	{"Category": "Maths", "Level": "10", "QuizId": "3", "Score": "134", "SubCategory": " ", "Time_Taken": "time", "UserId": "505313", "Evaluation": " ", "Current_Timestamp": "46565", "QuestionAnsMap":"[{\"ques_eval\":\"false\",\"ques_marks\":\"false\",\"Answer\":\"Less than\",\"QuestionId\":\"1\"},{\"ques_eval\":\"false\",\"ques_marks\":\"false\",\"Answer\":\"1\",\"QuestionId\":\"8\"},{\"ques_eval\":\"false\",\"ques_marks\":\"false\",\"Answer\":\"0\",\"QuestionId\":\"7\"},{\"ques_eval\":\"false\",\"ques_marks\":\"false\",\"Answer\":\"Less than\",\"QuestionId\":\"13\"},{\"ques_eval\":\"false\",\"ques_marks\":\"false\",\"Answer\":\"5\",\"QuestionId\":\"10\"}]"}

		int UserId;      
		String Category;         
		String SubCategory;          
		String TimeTaken;    
		int Level_No; 
		int Score; 
		String Question; 
		String Ans="";
		int QuizId=0;  
		String Evaluation = null;
		int marks;

		JSONObject jp=new JSONObject(JsonObject);
		UserId=Integer.parseInt(jp.getString("UserId"));
		Category=jp.getString("Category");
		SubCategory=jp.getString("SubCategory");
		TimeTaken=jp.getString("Time_Taken");
		Level_No=Integer.parseInt(jp.getString("Level"));
		Score=Integer.parseInt(jp.getString("Score"));
	//	System.err.println("Data Before Splitting :");
		Question=jp.getString("QuestionAnsMap").replaceAll("\\\\", "");
	//	System.err.println("Data After Splitting :"+Question);
		QuizId=Integer.parseInt(jp.getString("QuizId"));


		java.sql.CallableStatement cal=connection.prepareCall("{ call SaveQuizData(?,?,?,?,?,?,?,?,?) }");
		cal.setInt(1, UserId);
		cal.setString(2,Category);
		cal.setString(3,SubCategory);
		cal.setString(4,TimeTaken );
		cal.setInt(5, Level_No);	
		cal.setInt(6,Score );	
		cal.setInt(7, 1);
		cal.setString(8, Ans);
		cal.setInt(9,QuizId);

		int rs =cal.executeUpdate();


		/****
		 * 
		 *  {      "Current_Timestamp":"46565",     "Evaluation":"",     "Level":"1",     "QuestionAnsMap":[         {             "Question":"Mumbai",             "Answer":"Vishakapatnam" ,"Marks":"12","Evaluation":"p"        }     ],     "Category":"ami",     "QuizId":"1",     "Score":"521",     "SubCategory":"2",     "Time_Taken":"time",     "UserId":"505313"  }
		 */
		/*
		org.json.JSONArray jarra=new org.json.JSONArray(Question);*/
		org.json.JSONArray jarra=new org.json.JSONArray(Question);
		for (int i = 0; i < jarra.length(); i++) {
			JSONObject jo=jarra.getJSONObject(i);

			java.sql.CallableStatement cal1=connection.prepareCall("{ call SaveQuizTransactionDetails (?,?,?,?,?,?,?,?,?,?,?) }");
			cal1.setInt(1, UserId);
			cal1.setString(2,Category);
			cal1.setString(3,SubCategory);
			cal1.setString(4,TimeTaken );
			cal1.setInt(5, Level_No);	
			cal1.setInt(6,Score );	
			cal1.setInt(7, Integer.parseInt(jo.getString("QuestionId")));
			cal1.setString(8, jo.getString("Answer"));
			cal1.setInt(9,QuizId);
			cal1.setString(10, jo.getString("ques_eval"));
			cal1.setInt(11, 20);	
			int rs1 =cal1.executeUpdate();
		}

		msg.setMessage("Your playing Next Level "+Level_No);
		msg.setError("false");

		//return attempts & Score
		msg.setScore(ComputeAttempts(connection,UserId,Level_No,Category));


		/*	}catch(Exception e){
			msg.setError("true");
			msg.setMessage("Error :"+e.getMessage());
		}*/

		return msg;

	}

	private String ComputeAttempts(Connection connection, int userId,
			int level_No, String category) {
		String attempt = null;
		ArrayList<String> listr=new ArrayList<String>();

		try{
			PreparedStatement pca=connection.prepareStatement("select Score from QuizzTransaction where UserId = ? and Category = ? and LevelNo = ? ");
			pca.setInt(1, userId);
			pca.setString(2, category);
			pca.setInt(3, level_No);
			ResultSet rsal=pca.executeQuery();
			if (rsal!=null) {
				while (rsal.next()) {
					listr.add(rsal.getString("Score"));
				}
			}

			attempt=listr.toString();
		}catch(Exception e){

		}

		// TODO Auto-generated method stub
		return attempt;
	}

	public ArrayList<LevelDTO> GetLevelwiseScoreRank(Connection connection,String Quiz_Id,String category,String userId,String schoolid){
		ArrayList<LevelDTO> level=new ArrayList<LevelDTO>();
		try{		//System.out.println("GetLevelwiseScoreRank  ---"+Quiz_Id+" "+category+" "+userId+" "+schoolid);

			PreparedStatement pstat=connection.prepareStatement("select distinct LevelNo from QuizzTransaction where UserId = ?  and QuizId = ?");
			pstat.setString(1, userId);
			pstat.setInt(2, Integer.parseInt(Quiz_Id));
			ResultSet rs=pstat.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					LevelDTO dto=new LevelDTO();
					dto.setLevelNo(""+rs.getString("LevelNo"));
					dto.setRank(""+ComputeRank(connection,Quiz_Id,category,userId,rs.getString("LevelNo"),schoolid));
					dto.setScore(""+ComputeScore(connection,Quiz_Id,category,userId,rs.getString("LevelNo"),schoolid));
					dto.setStatus(""+ComputeStatus(connection,Quiz_Id,category,userId,rs.getString("LevelNo"),schoolid));
					level.add(dto);	
				}

			}else{
				LevelDTO dto=new LevelDTO();
				dto.setError("false");
				dto.setMessage("NO Level Info Found");
				level.add(dto);	
			}
		}catch(Exception e){
			System.err.println("Error: "+e.getMessage());
			e.printStackTrace();
		}
		System.err.println("Result : "+level);
		return level;
	}


	public ArrayList<SQL_QuizOverallDetailsDTO> GetOveralrankScore(Connection connection,String Quiz_Id,String category,String userId,String schoolid){
		ArrayList<SQL_QuizOverallDetailsDTO> level=new ArrayList<SQL_QuizOverallDetailsDTO>();
		try{
			/***
			 **
			 */
			String UserID;
			String Name;
			String Photo =Common.Photo;
			/****
			 * 
			 * Get Profile Photo From ParentMaster
			 * 
			 */

			PreparedStatement psphoto=connection.prepareStatement("select Photo from ProfileMaster where UserId = ?");
			psphoto.setString(1, userId);
			ResultSet rs =psphoto.executeQuery();
			
			
			if (!rs.isBeforeFirst() ) {    
				 System.out.println("No data photo"); 
			
				

				PreparedStatement  ps = connection.prepareStatement("select Name as name from ParentMaster where ParentID  =? ");
				ps.setString(1, userId);
				ResultSet rsTest=ps.executeQuery();
				if (rsTest!=null) {
					while (rsTest.next()) {
						SQL_QuizOverallDetailsDTO sqlquizDetails=new SQL_QuizOverallDetailsDTO();
						//UserID=rsTest.getString("Photo");
						Name=rsTest.getString("name");
						String rank=OverallRank(connection,userId);
						String score=OverallScore(connection,userId);
					
						System.out.println("------OvRank------"+rank+"  "+score);
						sqlquizDetails.setName(Name);
						sqlquizDetails.setRank(rank);
						sqlquizDetails.setScore(score);
						sqlquizDetails.setProimage(Photo+"");

						level.add(sqlquizDetails);
					}
				}

				
				
				
				
			}else{
			
			
			
			if (rs!=null) {
				while (rs.next()) {


					Photo=rs.getString("Photo");


				}
			}

		// GET USERID & Name
			 
			PreparedStatement  ps = connection.prepareStatement("select Photo as photo,Name as name from ProfileMaster where UserId =? ");
			ps.setString(1, userId);
			ResultSet rsTest=ps.executeQuery();
			if (rsTest!=null) {
				while (rsTest.next()) {
					SQL_QuizOverallDetailsDTO sqlquizDetails=new SQL_QuizOverallDetailsDTO();
					//UserID=rsTest.getString("Photo");
					Name=rsTest.getString("name");
					String rank=OverallRank(connection,userId);
					String score=OverallScore(connection,userId);
				
					System.out.println("------OvRank------"+rank+"  "+score);
					sqlquizDetails.setName(Name);
					sqlquizDetails.setProimage(Photo+"");
					sqlquizDetails.setRank(rank);
					sqlquizDetails.setScore(score);
					level.add(sqlquizDetails);
				}
			}


		}

		}catch(Exception e){
			e.printStackTrace();
		}


		return level;
	}





	private String OverallRank(Connection connection, String userId) {
		// TODO Auto-generated method stub
		int rank=0,i=0;
		boolean ASC = true;
		boolean DESC = false;
		Gson gson=new Gson();
		Map< String, Integer> has = null;
		has=new HashMap<String, Integer>();
		try{
			//PreparedStatement poverall=connection.prepareStatement("select OverAllScore,UserId from QuizOverAllRank ORDER BY OverAllScore desc");

			
			
			java.sql.CallableStatement stmt= connection.prepareCall("{call GetOvRanklists()}");
		
			ResultSet rsoverall = stmt.executeQuery();
			
			if (rsoverall!=null)
				System.out.println(rsoverall.toString());
			if (!rsoverall.isBeforeFirst() ) {    
				 System.out.println("No data"); 
				 rank=0;
				} 
				else {


					while (rsoverall.next()) {
						if (rsoverall.getString("UserId").equals(userId)) {
							rank=rsoverall.getInt("OvRank");
							break;
						}
					}
				}

			
			
					
			return rank+"";
		}catch(Exception e){
			e.printStackTrace();
		}
		return rank+"";
	}

	private String OverallScore(Connection connection, String userId) {
		int Overall_score=0;

		try{
			PreparedStatement poverall=connection.prepareStatement("select OverAllScore from [dbo].[QuizOverAllRank] Where UserId=?");
			poverall.setString(1, userId);
			ResultSet rsoverall=poverall.executeQuery();
			if (rsoverall!=null ){
				while (rsoverall.next()) {

					Overall_score=rsoverall.getInt("OverAllScore");
					System.err.println("maxscore  --"+Overall_score);

				}

			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return Overall_score+"";
	}

	private String ComputeStatus(Connection connection, String quiz_Id,
			String category, String userId, String levelno,String SchooldId) {
		String status="F";
		int threhold=0;
		int maxScore=0;

		try{
			//Get Passing Marks
			PreparedStatement pstatement=connection.prepareStatement("select passing_marks from QuizMetadata where school_id = ?");
			pstatement.setString(1, SchooldId);
			ResultSet rs=pstatement.executeQuery();
		//	System.out.println("ComputeStatus   =++++++"+pstatement.toString());
			if (rs!=null && rs.next()) {
				threhold=rs.getInt("passing_marks");
			}

			//Get Maximum score
			PreparedStatement pscore=connection.prepareStatement("select max(Score) as score from QuizzTransaction where LevelNo =  ? and QuizId = ? and Category =?");
			pscore.setString(1, levelno);
			pscore.setString(2, quiz_Id);
			pscore.setString(3, category);
			ResultSet rsScore=pscore.executeQuery();
			if (rsScore!=null) {
				if(rsScore.next()) {
					maxScore=rsScore.getInt("score");
				}
			}
System.out.println("Compute ststus--"+"Threshold="+threhold+"  "+maxScore);
			//check Passing Status:
			if (maxScore>=threhold) {
				status="P";
			}else{
				status="F";
			}
		}catch(Exception e){
			e.printStackTrace();
		}


		return status;
	}

	private String ComputeScore(Connection connection, String quiz_Id,
			String category, String userId, String levelno,String schoolid) {
		String max=null;
		try{
			PreparedStatement pscore=connection.prepareStatement("select max(Score) as score from QuizzTransaction where LevelNo =  ? and QuizId = ? and UserId =?");
			pscore.setString(1, levelno);
			pscore.setString(2, quiz_Id);
			pscore.setString(3, userId);
			ResultSet rsScore=pscore.executeQuery();
			if (rsScore!=null) {
				if(rsScore.next()) {
					max=rsScore.getString("score");
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		System.err.println(max);
		return max;
	}

	private String ComputeRank(Connection connection, String quiz_Id,
			String category, String userId, String levelno,String schoolid) {
		String rank="";
		
		System.out.println("println ComputeRank "+  quiz_Id+
			category+userId+levelno+ schoolid);
		try{
			ArrayList<String> st=new ArrayList<String>();
			PreparedStatement prank=connection.prepareStatement("select distinct UserId,max(Score) as maxscore from QuizzTransaction where QuizId = ? and levelno =? group by UserId order by max(Score) desc");
			//	PreparedStatement prank=connection.prepareStatement("SELECT DISTINCT LevelNo, MAX(Score) AS maxscore FROM  QuizzTransaction where QuizId = ? and levelno =? GROUP BY LevelNo");

			prank.setString(1, quiz_Id);
			prank.setString(2, levelno);
			ResultSet rsprank=prank.executeQuery();
			if (rsprank!=null) {
				while (rsprank.next()) {
					st.add(rsprank.getString("UserId"));
				}
			}
			//System.err.println(st.toString());
			for (int i = 0; i < st.size(); i++) {
				if (st.get(i).equals(userId)) {

					rank=(i+1)+"";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return rank;
	}


	//To calculate rankg 
	/**
	 * 
	 * Sort HashMAp
	 * @param unsortMap
	 * @param order
	 * @return
	 */
	private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
	{

		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, Integer>>()
				{
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2)
			{
				if (order)
				{
					return o1.getValue().compareTo(o2.getValue());
				}
				else
				{
					return o2.getValue().compareTo(o1.getValue());

				}
			}
				});

		// Maintaining insertion order with the help of LinkedList
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : list)
		{
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}
	
	
	

	public ArrayList<SQL_QuizOverallDetailsDTO> UsersOverallScoreDetails(
			Connection connection, String quiz_Id, String category,
			String userId, String schoolid, String levelno) {

		ArrayList<SQL_QuizOverallDetailsDTO> level=new ArrayList<SQL_QuizOverallDetailsDTO>();
		try{
			/***
			 **
			 */
			String UserID;
			String Name;
			String Photo =Common.Photo;
			/****
			 * 
			 * Get Profile Photo From ParentMaster
			 * 
			 * 
			 */

			
			PreparedStatement psphoto=connection.prepareStatement("select Photo from ProfileMaster where UserId = ?");
			psphoto.setString(1, userId);
			ResultSet rs =psphoto.executeQuery();
			
			
			if (!rs.isBeforeFirst() ) {    
				 System.out.println("No data photo"); 
			
				

				PreparedStatement  ps = connection.prepareStatement("select Name as name from ParentMaster where ParentID  =? ");
				ps.setString(1, userId);
				ResultSet rsTest=ps.executeQuery();
				if (rsTest!=null) {
					while (rsTest.next()) {
						SQL_QuizOverallDetailsDTO sqlquizDetails=new SQL_QuizOverallDetailsDTO();
						//UserID=rsTest.getString("Photo");
						Name=rsTest.getString("name");
						String rank=OverallRank(connection,userId);
						String score=OverallScore(connection,userId);
					
						System.out.println("------OvRank------"+rank+"  "+score);
						sqlquizDetails.setName(Name);
						sqlquizDetails.setRank(rank);
						sqlquizDetails.setScore(score);
						sqlquizDetails.setLevelrank(ComputeRank(connection, quiz_Id, category, userId, levelno, schoolid));
						sqlquizDetails.setProimage(Photo+"");
						System.out.println("------levelRank------"+sqlquizDetails.getLevelrank());

						level.add(sqlquizDetails);
					}
				}

				
				
				
				
			}
			else{
			if (rs!=null) {
				while (rs.next()) {


					Photo=rs.getString("Photo");


				}
			}


			/*****************88888888888888888888888888888888
			 * GET USERID & Name
			 */




			PreparedStatement  ps = connection.prepareStatement("select Photo as photo,Name as name from ProfileMaster where UserId =? ");
			ps.setString(1, userId);
			ResultSet rsTest=ps.executeQuery();
			if (rsTest!=null) {
				while (rsTest.next()) {
					SQL_QuizOverallDetailsDTO sqlquizDetails=new SQL_QuizOverallDetailsDTO();
					//UserID=rsTest.getString("Photo");
					Name=rsTest.getString("name");
					String rank=OverallRank(connection,userId);
					String score=OverallScore(connection,userId);
				
					System.out.println("------OvRank------"+rank+"  "+score);
					sqlquizDetails.setName(Name);
					sqlquizDetails.setRank(rank);
					sqlquizDetails.setScore(score);
					sqlquizDetails.setLevelrank(ComputeRank(connection, quiz_Id, category, userId, levelno, schoolid));
					sqlquizDetails.setProimage(Photo+"");
					System.out.println("------levelRank------"+sqlquizDetails.getLevelrank());

					level.add(sqlquizDetails);
				}
			}



			}
		}catch(Exception e){
			e.printStackTrace();
		}


		return level;
	
	}

	public UserPaymentDTO GetUserInfo(Connection connection,
			String parentId) {
		UserPaymentDTO list=new UserPaymentDTO();
		try{


			PreparedStatement pcal=connection.prepareStatement("select QuizPaymentMaster.UserId,ProfileMaster.EmailID,ProfileMaster.Name," +
					"ProfileMaster.MobileNo,QuizPaymentMaster.Status,QuizPaymentMaster.ExpiryDate from QuizPaymentMaster," +
					"ProfileMaster where QuizPaymentMaster.UserId=ProfileMaster.UserId and" +
					" QuizPaymentMaster.UserId=?  ");
			pcal.setString(1, parentId);
			ResultSet rs=pcal.executeQuery();
			
			if (!rs.isBeforeFirst() ) {    
				/* System.out.println("No data"); 
				 System.out.println("S111111111111111");*/

					PreparedStatement psc1=connection.prepareStatement("select ProfileMaster.UserId,ProfileMaster.EmailID,ProfileMaster.Name,ProfileMaster.MobileNo from ProfileMaster where ProfileMaster.UserId=?");
					psc1.setString(1, parentId);
					
					ResultSet rs1=psc1.executeQuery();
					if (rs1!=null) {
						while (rs1.next()) {

							list.setEmailID(rs1.getString("EmailID"));
							list.setName(rs1.getString("Name"));
							list.setUserId(rs1.getString("UserId"));
							list.setStatus("Deactive");
							
							list.setMobileno(rs1.getString("MobileNo"));
							list.setExpirydate("First Time");

						}
					
				}
				 
				}else  {
				while (rs.next()) {
					
					list.setEmailID(rs.getString("EmailID"));
					list.setName(rs.getString("Name"));
					list.setUserId(rs.getString("UserId"));
					if (rs.getString("Status").equals("0")) 
						list.setStatus("Active");
					else
						list.setStatus("Deactive");
					
					list.setMobileno(rs.getString("MobileNo"));
					list.setExpirydate(rs.getString("ExpiryDate"));



				}

			}/* else{

				list.setMessage("Data Not Found");
				list.setError("false");

			}*/




		}catch(Exception e){

			list.setMessage("Data Not Found");
			list.setError("Error :"+e.getMessage());

		}
		System.err.println("list: ------------->"+list.getError());

		return list;
	}
	
	

	public ArrayList<AddCategoryDTO> AllCategoryForAdd(Connection connection,
			String SelectedClass) {
		ArrayList<AddCategoryDTO> list=new ArrayList<AddCategoryDTO>();
		try{

			PreparedStatement pcal=connection.prepareStatement("select Category.Id as Categoryid,QuizPaymentType.QuizId,Category.CategoryName,QuizPaymentType.Id as PaymentTypeId,QuizPaymentType.ClassID,QuizPaymentType.HalfYearly,QuizPaymentType.MonthlyAmt,QuizPaymentType.Quarterly,QuizPaymentType.Yearly from Category,QuizPaymentType where Category.Id=QuizPaymentType.CategoryId and QuizPaymentType.ClassID=?");
			pcal.setString(1,SelectedClass);
			ResultSet rsResultSet=pcal.executeQuery();
			

			if (rsResultSet!=null) {
					
				
				while (rsResultSet.next()) {


					AddCategoryDTO dto=new AddCategoryDTO();
					dto.setCategoryid(rsResultSet.getString("Categoryid"));
					dto.setCategoryName(rsResultSet.getString("CategoryName"));
					dto.setHalfYearly(rsResultSet.getString("HalfYearly"));
					dto.setMonthlyAmt(rsResultSet.getString("MonthlyAmt"));
					dto.setPaymentTypeId(rsResultSet.getString("PaymentTypeId"));
					dto.setQuarterly(rsResultSet.getString("Quarterly"));
					dto.setYearly(rsResultSet.getString("Yearly"));
				dto.setQuizId(rsResultSet.getString("QuizId"));
					list.add(dto);

				}
				System.err.println("Category: --------"+list.toString());


			}




		}catch(Exception e){
			System.err.println("Error: ------------->");
			e.printStackTrace();
			AddCategoryDTO dto=new AddCategoryDTO();
			dto.setMessage("Data Not Found");
			dto.setError("false");
			list.add(dto);
		}
		return list;
	}
	
	
//	12-06 21:27:09.855: I/System.out(12788): Update quiz req--[ParentId=506535, typeofpayment=1, paymentId=1110408170, ActiveStatus=Deactive, SelectedCategory=[{"CategoryName":"Math","Categoryid":"8","HalfYearly":"60.00","MonthlyAmt":"30.00","PaymentTypeId":"3","Quarterly":"200.00","Yearly":"350.00"}]]


	public TransactionResultDTO UpdateQuizPayment(Connection connection,
			String parentId, String payment_type, String paymentId,
			String activeStatus, String selectedCategory, String totalAmount) {
		TransactionResultDTO msgObj = new TransactionResultDTO();

	/*	JSONObject  jo_req =new JSONObject(selectedCategory);
		int order_id=0;
		int resultDetails=0;
		*/
		

		
		try
		{
			java.sql.CallableStatement cal=connection.prepareCall("{ call SaveQuizPayment(?,?,?,?,?) }");
			cal.setInt(1, Integer.parseInt(parentId));
			cal. setString(2,paymentId);
			cal.setDouble(3,Double.parseDouble(totalAmount));
			cal.setInt(4,Integer.parseInt(payment_type));
			cal.setInt(5,Integer.parseInt(parentId));	
			
			int rs =cal.executeUpdate();
			
			Gson gson = new Gson();
			String data="[{\"A\":\"a\",\"B\":\"b\",\"C\":\"c\",\"D\":\"d\",\"E\":\"e\",\"F\":\"f\",\"G\":\"g\"}]";
			JsonParser jsonParser = new JsonParser();
			JsonArray jsonArray = (JsonArray) jsonParser.parse(selectedCategory);
			
			//JsonObject[] arrayReceipients = (JsonObject[]) JSONArray.toArray (JSONArray.fromObject(selectedCategory));
		//	System.out.println (arrayReceipients [0]); // u3848
		
			for (int i = 0; i < jsonArray.size(); i++) {
				
				JsonObject jo=(JsonObject) jsonArray.get(i);
				System.out.println("----------category--"+jo.get("Categoryid"));

				java.sql.CallableStatement cal1=connection.prepareCall("{ call SaveQuizPaymentDetail(?,?,?,?,?) }");
				cal1.setInt(1, Integer.parseInt(parentId));
				cal1.setInt(2,Integer.parseInt(jo.get("Categoryid").getAsString()));
				cal1.setInt(3,Integer.parseInt(jo.get("QuizId").getAsString()));
				System.err.println("Payment TYpe id "+payment_type);

				cal1.setInt(4,Integer.parseInt(payment_type.toString()));
				cal1.setInt(5,Integer.parseInt(parentId));

				
				int rs1 =cal1.executeUpdate();

			}
			
			msgObj.setError("false");
			msgObj.setMessage("Record Sucesfully Inserted");
		
		}catch(Exception e)
		{
			e.printStackTrace();
			msgObj.setError("true");
			msgObj.setMessage("Excpetion while Push Record");
		}
		try {
			connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		return msgObj;
	}

	public ArrayList<ClassDTO> AllClassForAdd(Connection connection) {

		ArrayList<ClassDTO> Classlist=new ArrayList<ClassDTO>();
		try{		//System.out.println("GetLevelwiseScoreRank  ---"+Quiz_Id+" "+category+" "+userId+" "+schoolid);

			PreparedStatement pstat=connection.prepareStatement("SELECT Distinct QuizPaymentType.ClassID ,ClassMaster.ClassName FROM QuizPaymentType" +
					" inner join ClassMaster on QuizPaymentType.ClassID=ClassMaster.ClassID");
			ResultSet rs=pstat.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					ClassDTO dto=new ClassDTO();
					dto.setClassId(rs.getString("ClassID"));
					dto.setClassName(rs.getString("ClassName"));
					Classlist.add(dto);	
				}

			}else{
				ClassDTO dto=new ClassDTO();
				dto.setError("false");
				dto.setMessage("NO Level Info Found");
				Classlist.add(dto);	
			}
		}catch(Exception e){
			System.err.println("Error: "+e.getMessage());
			e.printStackTrace();
		}
		System.err.println("Classlist : "+Classlist);
		return Classlist;
	
	}





	public TransactionResultDTO PostscoreApple(Connection connection,
			String result, String qarry) throws JSONException, SQLException {

		TransactionResultDTO msg=new TransactionResultDTO();
	
	//	{"Category": "Maths", "Level": "10", "QuizId": "3", "Score": "134", "SubCategory": " ", "Time_Taken": "time", "UserId": "505313", "Evaluation": " ", "Current_Timestamp": "46565", "QuestionAnsMap":"[{\"ques_eval\":\"false\",\"ques_marks\":\"false\",\"Answer\":\"Less than\",\"QuestionId\":\"1\"},{\"ques_eval\":\"false\",\"ques_marks\":\"false\",\"Answer\":\"1\",\"QuestionId\":\"8\"},{\"ques_eval\":\"false\",\"ques_marks\":\"false\",\"Answer\":\"0\",\"QuestionId\":\"7\"},{\"ques_eval\":\"false\",\"ques_marks\":\"false\",\"Answer\":\"Less than\",\"QuestionId\":\"13\"},{\"ques_eval\":\"false\",\"ques_marks\":\"false\",\"Answer\":\"5\",\"QuestionId\":\"10\"}]"}

		int UserId;      
		String Category;         
		String SubCategory;          
		String TimeTaken;    
		int Level_No; 
		int Score; 
		String Question; 
		String Ans="";
		int QuizId=0;  
		String Evaluation = null;
		int marks;

		JSONObject jp=new JSONObject(result);
		UserId=Integer.parseInt(jp.getString("UserId"));
		Category=jp.getString("Category");
		SubCategory=jp.getString("SubCategory");
		TimeTaken=jp.getString("Time_Taken");
		Level_No=Integer.parseInt(jp.getString("Level"));
		Score=Integer.parseInt(jp.getString("Score"));
	//	System.err.println("Data Before Splitting :");
		Question=qarry.replaceAll("\\\\", "");
	//	System.err.println("Data After Splitting :"+Question);
		QuizId=Integer.parseInt(jp.getString("QuizId"));


		java.sql.CallableStatement cal=connection.prepareCall("{ call SaveQuizData(?,?,?,?,?,?,?,?,?) }");
		cal.setInt(1, UserId);
		cal.setString(2,Category);
		cal.setString(3,SubCategory);
		cal.setString(4,TimeTaken );
		cal.setInt(5, Level_No);	
		cal.setInt(6,Score );	
		cal.setInt(7, 1);
		cal.setString(8, Ans);
		cal.setInt(9,QuizId);

		int rs =cal.executeUpdate();


		/****
		 * 
		 *  {      "Current_Timestamp":"46565",     "Evaluation":"",     "Level":"1",     "QuestionAnsMap":[         {             "Question":"Mumbai",             "Answer":"Vishakapatnam" ,"Marks":"12","Evaluation":"p"        }     ],     "Category":"ami",     "QuizId":"1",     "Score":"521",     "SubCategory":"2",     "Time_Taken":"time",     "UserId":"505313"  }
		 */
		/*
		org.json.JSONArray jarra=new org.json.JSONArray(Question);*/
		
		
		org.json.JSONArray jarra=new org.json.JSONArray(Question);
		for (int i = 0; i < jarra.length(); i++) {
			JSONObject jo=jarra.getJSONObject(i);

			java.sql.CallableStatement cal1=connection.prepareCall("{ call SaveQuizTransactionDetails (?,?,?,?,?,?,?,?,?,?,?) }");
			cal1.setInt(1, UserId);
			cal1.setString(2,Category);
			cal1.setString(3,SubCategory);
			cal1.setString(4,TimeTaken );
			cal1.setInt(5, Level_No);	
			cal1.setInt(6,Score );	
			cal1.setInt(7, Integer.parseInt(jo.getString("QuestionId")));
			cal1.setString(8, jo.getString("Answer"));
			cal1.setInt(9,QuizId);
			cal1.setString(10, jo.getString("ques_eval"));
			cal1.setInt(11, 20);	
			int rs1 =cal1.executeUpdate();
		}

		msg.setMessage("Your playing Next Level "+Level_No);
		msg.setError("false");

		//return attempts & Score
		msg.setScore(ComputeAttempts(connection,UserId,Level_No,Category));


		/*	}catch(Exception e){
			msg.setError("true");
			msg.setMessage("Error :"+e.getMessage());
		}*/

		return msg;

	
	}

}
