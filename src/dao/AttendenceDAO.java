package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.json.JsonObject;

import org.json.JSONObject;

import net.sf.json.JSONArray;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import dto.MessageObject;
import dto.StationaryDataDTO;
import dto.StudentAttenInfo;
import dto.classDTO;

public class AttendenceDAO {

	public ArrayList<classDTO> Getclassofteacher(Connection con,
			String teacherid) {
		ArrayList<classDTO> msgObj=new ArrayList<classDTO>();
		try
		{	ResultSet rs=null;
		//Made changes in select for email;
			java.sql.CallableStatement stmt= con.prepareCall("{call GetClassIDForTeacherAPP(?)}");
		

			stmt.setString(1,teacherid);
			
			 rs = stmt.executeQuery();
				if(rs!=null)
				{
					while (rs.next()) {
						
						classDTO data=new classDTO();
						
						data.setClassId(rs.getString("ClassID"));
						data.setClassname(rs.getString("ClassName"));
						data.setSchool_id(rs.getString("school_id"));
					
						msgObj.add(data);

					}
					
					System.out.println("---class--------"+msgObj.toString());
					;
						

				}
				
				
		
		}catch(Exception e)	
		{	e.printStackTrace();}
		finally
		{
			try {
				con.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return msgObj;
	}

	public ArrayList<StudentAttenInfo> GetstudentOfTeacherApp(Connection con,
			String classid, String schoolid) {
		ArrayList<StudentAttenInfo> msgObj=new ArrayList<StudentAttenInfo>();
		if(CheckValidAttendence(con,classid,schoolid))
		{
				try
				{	ResultSet rs=null;
				//Made changes in select for email;
					java.sql.CallableStatement stmt= con.prepareCall("{call GetStudents(?,?)}");
				
		
					stmt.setString(1,schoolid);
					stmt.setString(2,classid);
		
					 rs = stmt.executeQuery();
						if(rs!=null)
						{
							while (rs.next()) {
								
								StudentAttenInfo data=new StudentAttenInfo();
								
								data.setStudID(rs.getString("StudentID"));
								data.setComment("Non");
								data.setType("Present");
								data.setStudName(rs.getString("FirstName")+" "+rs.getString("MiddleName")+" "+rs.getString("LastName"));
							
								
								
								msgObj.add(data);
		
							}
							
							System.out.println("---GetstudentOfTeacherApp--------"+msgObj.toString());
									
		
						}
										
				}catch(Exception e)	
				{	e.printStackTrace();}
				finally
				{
					try {
						con.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		}else {
			
		}
		
		return msgObj;
	}

	private boolean CheckValidAttendence(Connection con, String classid, String schoolid) {
		Boolean Valid=false;
		try
		{	ResultSet rs=null;
		//Made changes in select for email;
			java.sql.CallableStatement stmt= con.prepareCall("{call GetValidAttForApp(?,?,?,?,?)}");
			  Date date = new Date();			   
			    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
			    String day=simpleDateFormat.format(date);
			    simpleDateFormat = new SimpleDateFormat("MMM");
			    String  month=simpleDateFormat.format(date);
			    simpleDateFormat = new SimpleDateFormat("YYYY");
			    String year=simpleDateFormat.format(date);
			  
			stmt.setString(1,schoolid);
			stmt.setString(2,classid);
			stmt.setString(3,day);
			stmt.setString(4,month);
			stmt.setString(5,year);
			 rs = stmt.executeQuery();
				if(rs!=null)
					while (rs.next()) 
					{
						if(rs.getInt("cnt")==0)
							Valid=true;
						else 
							Valid=false;
					}
								
		}catch(Exception e)	
		{	e.printStackTrace();}
		
		return Valid;
	}

	@SuppressWarnings("deprecation")
	public MessageObject SaveAttendencetoServer(Connection con,
			org.json.JSONArray attendencelist, String teacherid, String schoolid, String classid) {
		   Calendar cal = Calendar.getInstance();
		   MessageObject msgObj=new MessageObject();
		   Date date = new Date();

		   java.sql.Date today=getCurrentJavaSqlDate();
		   
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
		    int day=Integer.parseInt(simpleDateFormat.format(date));
		    simpleDateFormat = new SimpleDateFormat("MMM");
		    String  month=simpleDateFormat.format(date);
		    simpleDateFormat = new SimpleDateFormat("YYYY");
		    int year=Integer.parseInt(simpleDateFormat.format(date));
		    try {
				
		
				for(int i=0;i<attendencelist.length();i++)
				{
					try
					{	
						JSONObject jo=attendencelist.getJSONObject(i);
		
						ResultSet rs=null;
					//Made changes in select for email;
						java.sql.CallableStatement stmt= con.prepareCall("{call SaveAttendance(?,?,?,?,?,?,?,?,?,?,?)}");
					
		
						stmt.setInt(1,Integer.parseInt(jo.getString("StudID")));
		
						stmt.setString(2,jo.getString("Type"));
						stmt.setString(3,jo.getString("Comment"));
						stmt.setInt(4,Integer.parseInt(classid));
						stmt.setInt(5,day);
						stmt.setString(6,month);
						stmt.setInt(7,year);
						stmt.setInt(8,Integer.parseInt(schoolid));
						stmt.setDate(9,today);
						stmt.setInt(10,Integer.parseInt(teacherid));
						stmt.setDate(11,today);
				
						
						int result = stmt.executeUpdate();
						
						if (result==0) {
							msgObj.setError("true");
							msgObj.setMessage("Attendence Failed");
						}else{
						//	System.err.println("Error=="+result);
							msgObj.setError("false"); 
							msgObj.setMessage("Attendence generated Successfully");
						}
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		
				}
		    } catch (Exception e) {
				e.printStackTrace();
		}finally
		{
			try {
				con.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(msgObj.toString());
		}
		return msgObj;
	}
	
	
	 public static java.sql.Date getCurrentJavaSqlDate() {
		    java.util.Date today = new java.util.Date();
		    return new java.sql.Date(today.getTime());
		  }


}
