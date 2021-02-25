package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import net.sf.json.JSONArray;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import dto.DevicelistDetails;
import dto.EmpAttendanceDTO;
import dto.LoginDto;
import dto.MainSliderImageDTO;
import dto.MessageObject;
import dto.TrackUserSighupDTO;
import dto.UserModuleDTO;
import dto.VehicalTrackingSMSCmdDTO;

public class ParentDao {

	public ArrayList<LoginDto> GetLoginDetails(Connection con,
			String username, String password, String appid, String appmailid) {
		ArrayList<LoginDto> msgdata=new ArrayList<LoginDto>();
		LoginDto respo=new 	LoginDto();
		try
		{	ResultSet rs=null;
			java.sql.CallableStatement stmt= con.prepareCall("{call CheckValidUser(?,?,?,?)}");
			
			stmt.setString(1,username);
			stmt.setString(2,password);
			stmt.setString(3,appid);
			stmt.setString(4,appmailid);
			

			 rs = stmt.executeQuery();
				if(rs!=null )
				{
					while ( rs.next()) {
																									
						respo.setRole_ID(rs.getString("Role_ID")+"");
						respo.setRegistrationType(rs.getString("RegistrationType")+"");
						respo.setUserID(rs.getString("UserID")+"");
						respo.setFenceAllow(rs.getString("FenceAllow"));
						respo.setLocationPushInterval(rs.getString("LocationPushInterval")+"");
						respo.setSOSAllowNo(rs.getString("SOSAllowNo")+"");
						respo.setVtsFuncAllow(rs.getString("VtsFuncAllow")+"");
						respo.setVtsSmsAllow(rs.getString("VtsSmsAllow")+"");
						respo.setSOSBtnCount(rs.getString("SOSBtnCount")+"");
						respo.setSOSBtnTimeSpan(rs.getString("SOSBtnTimeSpan")+"");
						respo.setDaysr(rs.getString("daysr")+"");
						respo.setUserName(rs.getString("userName")+"");
						respo.setClassName(rs.getString("ClassName")+"");
						respo.setUserType(rs.getString("userType")+"");
						respo.setMobileNo(rs.getString("MobileNo")+"");
						respo.setEmailID(rs.getString("EmailID")+"");
						respo.setStudent_Count(rs.getString("Student_Count")+"");
						respo.setSchool_id(rs.getString("school_id")+"");
						respo.setPhoto(rs.getString("Photo")+"");
						respo.setError("false");
						respo.setMsg("SuccessFully Login");

						msgdata.add(respo);
					}
				}else {
					respo.setError("True");
					respo.setMsg("Please Enter Valid Username or Password.");

					msgdata.add(respo);
				}
				
				
		
		
		}catch(Exception e)
		{		
			e.printStackTrace();
			respo.setError("True");
			respo.setMsg("Error While Establishing Connection To Server! Please Contact Admin.");

			msgdata.add(respo);
		}
		finally
		{
			try {
				con.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return msgdata;
	}

	
	public ArrayList<MainSliderImageDTO> GetMainSliderImage(Connection connection) {
		ArrayList<MainSliderImageDTO> Slider_list=new ArrayList<>();

		try
		{	ResultSet rs=null;
		//Made changes in select for email;
		
	
			int rs_starttime=0;
			java.sql.CallableStatement stmt= connection.prepareCall("{call GetMainSliderImage()}");
		rs = stmt.executeQuery();
		if(rs!=null )
		{
			//SELECT `Id`, `ShopId`, `SliderImage`, `Active`, `CreatedBy`, `CreatedAt` FROM `SliderMaster` WHERE 1
			while(rs.next()){
			MainSliderImageDTO msgObj = new MainSliderImageDTO();

			msgObj.setSliderId(rs.getString("Id"));
			msgObj.setShopId(rs.getString("ShopId"));
			msgObj.setSliderImage(rs.getString("SliderImage"));
			msgObj.setCreatedAt(rs.getString("CreatedAt"));
		
			Slider_list.add(msgObj);
		}
		}
		
		
		}catch(Exception e)
		{		
			System.err.println("getUser "+e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			
		}
		
		System.out.print(Slider_list.toString());
		return Slider_list;
	
		
	}
	
	
	public ArrayList<UserModuleDTO> GetUserModuleList(Connection con,
			String userid, String roleid) {
		ArrayList<UserModuleDTO> p=new ArrayList<UserModuleDTO>();
		
		try{			

			

			java.sql.CallableStatement stmt= con.prepareCall("{call GetUserModuleList(?,?)}");
			stmt.setString(1, userid);
			stmt.setString(2, roleid);

			ResultSet rs=stmt.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					UserModuleDTO person=new UserModuleDTO();
					person.setModuleId(""+rs.getString("ModuleId"));
					person.setModule(""+rs.getString("Module"));

					
					person.setModuleTitle(""+rs.getString("ModuleTitle"));
					person.setModuleDesc(""+rs.getString("ModuleDesc"));
					person.setModuleActivity(""+rs.getString("ModuleActivity"));
				//	person.setUpdatedAt(""+rs.getString("UpdatedAt"));
					person.setImageUrl(""+rs.getString("ImageUrl"));
					
					person.toString();
					
				
					p.add(person);

				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		System.out.println("S MOdule list----"+p.toString());
		return p;

	}


	public ArrayList<UserModuleDTO> GetUserNewModuleList(Connection con,
			String userid, String roleid) {
	ArrayList<UserModuleDTO> p=new ArrayList<UserModuleDTO>();
		
		try{			

			

			java.sql.CallableStatement stmt= con.prepareCall("{call GetUserNewModuleList(?,?)}");
			stmt.setString(1, userid);
			stmt.setString(2, roleid);

			ResultSet rs=stmt.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					UserModuleDTO person=new UserModuleDTO();
					person.setModuleId(""+rs.getString("ModuleId"));
					person.setModule(""+rs.getString("Module"));

					
					person.setModuleTitle(""+rs.getString("ModuleTitle"));
					person.setModuleDesc(""+rs.getString("ModuleDesc"));
					person.setModuleActivity(""+rs.getString("ModuleActivity"));
				//	person.setUpdatedAt(""+rs.getString("UpdatedAt"));
					person.setImageUrl(""+rs.getString("ImageUrl"));
					
					person.toString();
					
				
					p.add(person);

				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		System.out.println("S MOdule list----"+p.toString());
		return p;

	}


	public EmpAttendanceDTO GetEmpDay_status(Connection con, String userid,
			String day, String month, String year) {
		EmpAttendanceDTO obj=new EmpAttendanceDTO();
		
		try{			

	
			java.sql.CallableStatement stmt= con.prepareCall("{call GetEmpDay_status(?,?,?,?)}");
			
			stmt.setInt(1, Integer.parseInt(userid));
			stmt.setInt(2, Integer.parseInt(day));
			stmt.setString(3, month);
			stmt.setInt(4, Integer.parseInt(year));
			ResultSet rs=stmt.executeQuery();
			if (rs!=null&&rs.next()) {
				obj.setEmp_id(rs.getString("StudID")+"");
				obj.setAttendance_id(rs.getString("ID")+"");
				obj.setAtt_type(rs.getString("AttType")+"");
				obj.setType(rs.getString("Type")+"");
				if (rs.getString("StartTime")!=null) {
					obj.setIs_start("1");
				}else {
					obj.setIs_start("0");

				}
				if (rs.getString("EndTime")!=null) {
					obj.setIs_end("1");
				}else {
					obj.setIs_end("0");

				}
				obj.setStart_time(rs.getString("StartTime")+"");
				obj.setEnd_time(rs.getString("EndTime")+"");
		
				}else{
					
				}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		System.out.println("GetEmpDay_status ----"+new Gson().toJson(obj));
		return obj;

	}


	public MessageObject UpdateAttendance(Connection con, String userid,String studentid,
			String day, String month, String year, String time) {
		MessageObject msgo=new MessageObject();
		
		
		try{

			int result=0;
			java.sql.CallableStatement stmt= con.prepareCall("{call SaveEmpAttendance(?,?,?,?,?,?)}");
			stmt.setInt(1, Integer.parseInt(userid));
			stmt.setInt(2, Integer.parseInt(studentid));

			stmt.setInt(3, Integer.parseInt(day));
			stmt.setString(4, month);
			stmt.setInt(5, Integer.parseInt(year));
			stmt.setString(6, time);
		//	stmt.setInt(6, Integer.parseInt(type));
//


		        
			result=stmt.executeUpdate();

			if (result==0) {
				msgo.setError("true");
				msgo.setMessage("Attendance  is not updated successfully");
			}else{
		//		System.err.println("Error=="+result);
				msgo.setError("false"); 
				msgo.setMessage("Attendance is updated successfully");
			}
		}catch(Exception e){
			msgo.setError("true");
			msgo.setMessage("Error :"+e.getMessage());
			e.printStackTrace();
		}
	
	
	
	return msgo;
	
	}


	public MessageObject SaveEmpLeaveApplication(Connection con, String data) {
		MessageObject msgo=new MessageObject();

		try{
		org.json.JSONArray jarray=new org.json.JSONArray(data);
		System.out.println("SaveEmpLeaveApplication-----"+data);

		for(int i=0;i<jarray.length();i++){
		

			JSONObject jp=(JSONObject) jarray.get(i);
			int result=0;
			java.sql.CallableStatement stmt= con.prepareCall("{call SaveEmpLeaveApplication(?,?,?,?,?,?)}");
			stmt.setInt(1, Integer.parseInt(jp.getString("user_id")));
			stmt.setInt(2, Integer.parseInt(jp.getString("day")));
			stmt.setString(3, jp.getString("month"));
			stmt.setInt(4, Integer.parseInt(jp.getString("year")));
			stmt.setString(5, jp.getString("time"));
			stmt.setString(6, jp.getString("comment"));

		//	stmt.setInt(6, Integer.parseInt(type));

			result=stmt.executeUpdate();

			if (result==0) {
				msgo.setError("true");
				msgo.setMessage("Leave  is not updated successfully.Please Try again.");
			}else{
		//		System.err.println("Error=="+result);
				msgo.setError("false"); 
				msgo.setMessage("Leave is updated successfully.");
			}
		
		
		}
		
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return msgo;

	}


	public ArrayList<EmpAttendanceDTO> GetEmpMonth_Attendance(Connection con,
			String stud_id, String month, String year, String user_id) {
		ArrayList<EmpAttendanceDTO> list=new ArrayList<EmpAttendanceDTO>();
		
		try{			

	
			java.sql.CallableStatement stmt= con.prepareCall("{call GetEmpMonth_Attendance(?,?,?)}");
			
			stmt.setInt(1, Integer.parseInt(stud_id));
			stmt.setString(2, month);
			stmt.setInt(3, Integer.parseInt(year));
			ResultSet rs=stmt.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					EmpAttendanceDTO obj=new EmpAttendanceDTO();
					obj.setEmp_id(rs.getString("StudID"));
					obj.setAttendance_id(rs.getString("ID")+"");
					obj.setAtt_type(rs.getString("AttType")+"");
					obj.setType(rs.getString("Type")+"");
					obj.setComment(rs.getString("Comment")+"");
					obj.setDay(rs.getString("days")+"");
					obj.setMonth(rs.getString("months")+"");
					obj.setYear(rs.getString("years")+"");
					obj.setIs_grant(rs.getString("IsGrant")+"");
					obj.setCreated_by(rs.getString("CreatedBy")+"");
					obj.setCreated_at(rs.getString("CreatedAt")+"");
					list.add(obj);

				}
				
				

				java.sql.CallableStatement stmtholidy= con.prepareCall("{call GetEmpholiday(?,?,?)}");
				
				stmtholidy.setInt(1, Integer.parseInt(stud_id));
				stmtholidy.setString(2, month);
				stmtholidy.setInt(3, Integer.parseInt(year));
				ResultSet holiday_rs=stmtholidy.executeQuery();
				if (holiday_rs!=null) {
					while (holiday_rs.next()) {
						EmpAttendanceDTO obj=new EmpAttendanceDTO();
						obj.setEmp_id(stud_id);
						obj.setAttendance_id(holiday_rs.getString("ID")+"");
						obj.setAtt_type(holiday_rs.getString("AttType")+"");
						obj.setType(holiday_rs.getString("Type")+"");
						obj.setComment(holiday_rs.getString("Comment")+"");
						obj.setDay(holiday_rs.getString("days")+"");
						obj.setMonth(holiday_rs.getString("months")+"");
						obj.setYear(holiday_rs.getString("years")+"");
						obj.setIs_grant(holiday_rs.getString("IsGrant")+"");
						obj.setCreated_by(holiday_rs.getString("CreatedBy")+"");
						obj.setCreated_at(holiday_rs.getString("CreatedAt")+"");
						list.add(obj);

					}
				
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		System.out.println("GetEmpMonth_Attendance ----"+list.size());
		return list;

	}


	public MessageObject GrantLeaveApplication(Connection con, String data) {
		MessageObject msgo=new MessageObject();

		try{
		org.json.JSONArray jarray=new org.json.JSONArray(data);
		System.out.println("SaveEmpLeaveApplication-----"+data);

		for(int i=0;i<jarray.length();i++){
		

			JSONObject jp=(JSONObject) jarray.get(i);
			int result=0;
			java.sql.CallableStatement stmt= con.prepareCall("{call GrantLeaveApplication(?,?,?,?,?,?)}");
			stmt.setInt(1, Integer.parseInt(jp.getString("emp_id")));
			stmt.setInt(2, Integer.parseInt(jp.getString("user_id")));

			stmt.setInt(3, Integer.parseInt(jp.getString("day")));
			stmt.setString(4, jp.getString("month"));
			stmt.setInt(5, Integer.parseInt(jp.getString("year")));
			stmt.setString(6, jp.getString("time"));
			//stmt.setString(6, jp.getString("comment"));

		//	stmt.setInt(6, Integer.parseInt(type));

			result=stmt.executeUpdate();

			if (result==0) {
				msgo.setError("true");
				msgo.setMessage("Leave  is not grant successfully.Please Try again.");
			}else{
		//		System.err.println("Error=="+result);
				msgo.setError("false"); 
				msgo.setMessage("Leave is grant successfully.");
			}
		
		
		}
		
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return msgo;
	}


	public MessageObject SaveEmpUpdateDayApplication(Connection con, String data) {
		MessageObject msgo=new MessageObject();
		String att_type="";

		try{
		org.json.JSONArray jarray=new org.json.JSONArray(data);
		System.out.println("SaveEmpLeaveApplication-----"+data);

		for(int i=0;i<jarray.length();i++){
		

			JSONObject jp=(JSONObject) jarray.get(i);
			int result=0;
			java.sql.CallableStatement stmt= con.prepareCall("{call SaveEmpUpdateDayApplication(?,?,?,?,?,?,?,?)}");
			stmt.setInt(1, Integer.parseInt(jp.getString("emp_id")));
			stmt.setInt(2, Integer.parseInt(jp.getString("user_id")));

			stmt.setInt(3, Integer.parseInt(jp.getString("day")));
			stmt.setString(4, jp.getString("month"));
			stmt.setInt(5, Integer.parseInt(jp.getString("year")));
			stmt.setString(6, jp.getString("time"));
			stmt.setInt(7,  Integer.parseInt(jp.getString("att_day_status")));
			stmt.setString(8, jp.getString("att_day_status_name"));
			att_type=jp.getString("att_day_status_name")+"";
			//stmt.setString(6, jp.getString("comment"));

		//	stmt.setInt(6, Integer.parseInt(type));

			result=stmt.executeUpdate();

			if (result==0) {
				msgo.setError("true");
				msgo.setMessage("For selected date "+ att_type+" action  is not updated successfully.Please Try again.");
			}else{
		//		System.err.println("Error=="+result);
				msgo.setError("false"); 
				msgo.setMessage("For selected date "+ att_type+ " action is updated successfully.");
			}
		
		
		}
		
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return msgo;
	}


	public MessageObject SaveEmpHoliday(Connection con, String data) {
		MessageObject msgo=new MessageObject();

		try{
		org.json.JSONArray jarray=new org.json.JSONArray(data);
		System.out.println("SaveEmpLeaveApplication-----"+data);

		for(int i=0;i<jarray.length();i++){
		

			JSONObject jp=(JSONObject) jarray.get(i);
			int result=0;
			java.sql.CallableStatement stmt= con.prepareCall("{call SaveEmpHoliday(?,?,?,?,?,?)}");
			stmt.setInt(1, Integer.parseInt(jp.getString("user_id")));
			stmt.setInt(2, Integer.parseInt(jp.getString("day")));
			stmt.setString(3, jp.getString("month"));
			stmt.setInt(4, Integer.parseInt(jp.getString("year")));
			stmt.setString(5, jp.getString("time"));
			stmt.setString(6, jp.getString("comment"));

		//	stmt.setInt(6, Integer.parseInt(type));

			result=stmt.executeUpdate();

			if (result==0) {
				msgo.setError("true");
				msgo.setMessage("Holiday  is not updated successfully.Please Try again.");
			}else{
		//		System.err.println("Error=="+result);
				msgo.setError("false"); 
				msgo.setMessage("Holiday is updated successfully.");
			}
		
		
		}
		
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return msgo;

	}


	public MessageObject CheckMailExist(Connection con, String email) {
		MessageObject msgo=new MessageObject();

		try{
			
	java.sql.CallableStatement stmt= con.prepareCall("{call CheckEmailIdExist(?,?)}");
			
			stmt.setString(1, email);
			stmt.setInt(2, Integer.parseInt("0"));
			ResultSet rs=stmt.executeQuery();
		
			if (rs.next() )
			{ 
				msgo.setError("true");
				msgo.setMessage("Email is already exist.Please Try again.");
				
			} 
			else
			{ System.out.println("ResultSet in empty in Java");
			msgo.setError("flase");
			msgo.setMessage("Email is not exist.You can use it.");
			
			} 

		
		
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return msgo;

	}


	public TrackUserSighupDTO SaveTrackUserNew(Connection con, String name,
			String email, String gender, String mob_no, String address) {
		TrackUserSighupDTO msgo=new TrackUserSighupDTO();
		Random random=new Random();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
		System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
		String Password=name.substring(0,3)+String.format("%04d", random.nextInt(10000));
		try{
			
	java.sql.CallableStatement stmt= con.prepareCall("{call SaveTrackUserNew(?,?,?,?,?,?,?,?,?,?)}");

			stmt.setInt(1, Integer.parseInt("0000"));
			stmt.setString(2, name);
			stmt.setString(3, gender);
			stmt.setString(4, mob_no);
			stmt.setString(5, email);
			stmt.setString(6, address);
			stmt.setInt(7, Integer.parseInt("1"));
			stmt.setDate(8,  date);
			stmt.setString(9, Password);
			stmt.setInt(10, 7);

			int result = stmt.executeUpdate();

			if (result==0) {
				
				msgo.setError("true");
				msgo.setMsg("User is not register succesfully.Please Try again.");
				
			}else{
		//		System.err.println("Error=="+result);
				msgo.setError("false"); 
				msgo.setMsg("User register succesfully.You can use it.");
				msgo.setPassword(Password);
				msgo.setUsername(email);
			}
		
	
		
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return msgo;
	}


	public MessageObject AddDevice(Connection con, String fname, String lname,
			String gender, String mob_no, String imei_no, String device_type, String user_id) {
		MessageObject msgo= new MessageObject();
		try{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
	
			java.sql.CallableStatement stmt= con.prepareCall("{call SaveAddDeviceFromApp(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			
			
					stmt.setInt(1, Integer.parseInt("0"));
					stmt.setInt(2,  Integer.parseInt(user_id));
					stmt.setString(3, fname);
					stmt.setString(4, lname);
					stmt.setString(5, gender);
					stmt.setString(6, imei_no);
					stmt.setString(7, mob_no);
					stmt.setString(8,  "photo");
					stmt.setString(9, device_type);
					if(device_type.equalsIgnoreCase("Child"))
						stmt.setInt(10, 1);
					else if(device_type.equalsIgnoreCase("car"))
						stmt.setInt(10, 5);
					else if(device_type.equalsIgnoreCase("pet"))
						stmt.setInt(10, 4);
					
					stmt.setString(11, mob_no);
					stmt.setDate(12,  date);
					stmt.setInt(13, 2);
					stmt.setInt(14, 2);
					stmt.setString(15, "Added by APP");
					stmt.setDate(16,  date);
					stmt.setString(17, "Added by APP");
					stmt.setDouble(18, 149.00);
					System.err.println("stmt=="+stmt);
					int result = stmt.executeUpdate();
					
/*
					if (result.next() )
					{ 
						msgo.setError("false"); 
						msgo.setMessage("Device register succesfully.You can use it.");
					msgo.setId(result.getString("StudentId"));
					} 
					else
					{ System.out.println("ResultSet in empty in Java");
				
					msgo.setError("true");
					msgo.setMessage("Device is not register succesfully.Please Try again.");
					} 
*/
				if (result==0) {
						
						msgo.setError("true");
						msgo.setMessage("Device is not register successfully.Please Try again.");
						
					}else{
				//		System.err.println("Error=="+result);
						
						try
						{	ResultSet rs=null;
						//Made changes in select for email;
						
					
							int rs_starttime=0;
							java.sql.CallableStatement substmt= con.prepareCall("{call GetLatestStudentOfParent(?)}");
							substmt.setInt(1,  Integer.parseInt(user_id));

						rs = substmt.executeQuery();
						if(rs!=null&&rs.next() )
						{
							msgo.setError("false"); 
							msgo.setId(rs.getString("StudentId"));
							msgo.setMessage("Device register succesfully.You can use it.");							
						}
						
						
						
						}catch(Exception e)
						{		
							System.err.println("getUser "+e.getMessage());
							e.printStackTrace();
						}
						
						
						//msgo.setId();
					}
					
				
			
				
				}catch(Exception e){
					
					e.printStackTrace();
				}
		System.err.println("Ad device--------------"+new Gson().toJson(msgo));
				return msgo;
	}


	public MessageObject CheckIMEIExist(Connection con, String imei_no) {
		MessageObject msgo=new MessageObject();

		try{
			
	java.sql.CallableStatement stmt= con.prepareCall("{call CheckIMEIExist(?)}");
			
			stmt.setString(1, imei_no);
			ResultSet rs=stmt.executeQuery();
		
			if (rs.next())
			{ 
				msgo.setError("true");
				msgo.setMessage("IMEI NO is already exist.Please Try again.");
				
			} 
			else
			{	 System.out.println("ResultSet in empty in Java---");
				msgo.setError("flase");
				msgo.setMessage("IMEI NO is not exist.You can use it.");
			
			} 


		
		
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return msgo;

	}


	public ArrayList<DevicelistDetails> GetEmpDevicelist(Connection con,
			String user_id) {
	ArrayList<DevicelistDetails> msg=new ArrayList<DevicelistDetails>();
		
		try{			

			

			java.sql.CallableStatement stmt= con.prepareCall("{call GetEmpDevicelist(?)}");
			stmt.setString(1, user_id);

			ResultSet rs=stmt.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					DevicelistDetails dmDetails=new DevicelistDetails();
					
					  dmDetails.setStudentID(rs.getString("StudentID"));
		                dmDetails.setName(rs.getString("Name"));

		                dmDetails.setPhoto(rs.getString("Photo").replaceAll("~", "").trim());
		                dmDetails.setType(rs.getString("Type"));
		                dmDetails.setImeiNo(rs.getString("DeviceID"));

		
					
					
				
		                msg.add(dmDetails);

				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		System.out.println("S GetDevicelist list----"+msg.toString());
		return msg;
	}

}
