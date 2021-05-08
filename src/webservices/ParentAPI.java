package webservices;

import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import model.APIController;

import com.google.gson.Gson;

import dto.DevicelistDetails;
import dto.DriverEmpTaskSheduledDTO;
import dto.EmpAttendanceDTO;
import dto.LoginDto;
import dto.MainSliderImageDTO;
import dto.MessageObject;
import dto.PersonDetails;
import dto.TrackUserSighupDTO;
import dto.UserModuleDTO;
import dto.UserUtilityDTO;

@Path("ParentAPI")
public class ParentAPI {	
	@POST
	@Path("GetUserModuleList")
	@Produces("application/json")
	public String GetUserModuleList(@FormParam("UserId") String userid,@FormParam("RoleId") String roleid)
	{

		String feeds  = null;
		try 
		{
			ArrayList<UserModuleDTO> msgData = new ArrayList<UserModuleDTO>();
			APIController handler= new APIController();
			msgData = handler.GetUserModuleList(userid,roleid);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			///APIController.SaveApiReport("TrackMyFriend", "Get All friend for track",  "GetRequestedList",userid,
				//	 "Getting list of friend for tracking",  feeds,  "Java","null", "null");
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("GetUserNewModuleList")
	@Produces("application/json")
	public String GetUserNewModuleList(@FormParam("UserId") String userid,@FormParam("RoleId") String roleid)
	{

		String feeds  = null;
		try 
		{
			ArrayList<UserModuleDTO> msgData = new ArrayList<UserModuleDTO>();
			APIController handler= new APIController();
			msgData = handler.GetUserNewModuleList(userid,roleid);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		//	//APIController.SaveApiReport("TrackMyFriend", "Get All friend for track",  "GetRequestedList",userid "Getting list of friend for tracking",  feeds,  "Java","null", "null");
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	/*
	@POST
	@Path("GetLoginDetails")
	@Produces("application/json")
	public String GetLoginDetails(@FormParam("username") String username,@FormParam("password") String password,
			@FormParam("App_Device_Id") String appid,@FormParam("App_Emailid") String appmailid)
	{
		String feeds  = null;
		try 
		{
			ArrayList<LoginDto> msgData = new ArrayList<LoginDto>();
			APIController handler= new APIController();
			msgData = handler.GetLoginDetails(username,password,appid,appmailid);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			APIController.SaveApiReport("TrackMyFriend", "Get Login details",  "GetRequestedList","0",
					 "Getting  Login details",  feeds,  "Java","null", "null");
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	
	*/
	@GET
	@Path("GetMainSliderImage")
	@Produces("application/json")
	public String GetSliderImage()
	{
		String feeds  = null;
		try 
		{

			
			ArrayList<MainSliderImageDTO> msgData = null;
			APIController handler= new APIController();
			msgData = handler.GetMainSliderImage();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("GetEmpDay_status")
	@Produces("application/json")
	public String GetEmpDay_status(@FormParam("UserId") String userid,@FormParam("day") String day
			,@FormParam("month") String month,@FormParam("year") String year)
	{
		String feeds  = null;
		try 
		{

			
			EmpAttendanceDTO msgData = null;
			APIController handler= new APIController();
			msgData = handler.GetEmpDay_status(userid,day,month,year);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	@POST
	@Path("UpdateAttendance")
	@Produces("application/json")
	public String UpdateAttendance(@FormParam("UserId") String userid,@FormParam("StudentId") String studentid,@FormParam("day") String day
			,@FormParam("month") String month,@FormParam("year") String year,@FormParam("time") String time)
	{
		String feeds  = null;
		try 
		{

			
			MessageObject msgData = null;
			APIController handler= new APIController();
			msgData = handler.UpdateAttendance(userid,studentid,day,month,year,time);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	
	@POST
	@Path("SaveEmpLeaveApplication")
	@Produces("application/json")
	public String SaveEmpLeaveApplication(@FormParam("SelectedDates") String data){
		String feeds  = null;
		try 
		{

			
			MessageObject msgData = null;
			APIController handler= new APIController();
			msgData = handler.SaveEmpLeaveApplication(data);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	
	@POST
	@Path("GetEmpMonth_Attendance")
	@Produces("application/json")
	public String GetEmpMonth_Attendance(@FormParam("Student_Id") String stud_id,@FormParam("Month") String month,
			@FormParam("Year") String year,@FormParam("user_id") String user_id){
		String feeds  = null;
		try 
		{

			
			ArrayList<EmpAttendanceDTO> msgData = null;
			APIController handler= new APIController();
			msgData = handler.GetEmpMonth_Attendance(stud_id,month,year,user_id);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("GrantLeaveApplication")
	@Produces("application/json")
	public String GrantLeaveApplication(@FormParam("SelectedDates") String data){
		String feeds  = null;
		try 
		{

			
			MessageObject msgData = null;
			APIController handler= new APIController();
			msgData = handler.GrantLeaveApplication(data);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	@POST
	@Path("SaveEmpUpdateDayApplication")
	@Produces("application/json")
	public String SaveEmpUpdateDayApplication(@FormParam("SelectedDates") String data){
		String feeds  = null;
		try 
		{

			
			MessageObject msgData = null;
			APIController handler= new APIController();
			msgData = handler.SaveEmpUpdateDayApplication(data);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	
	@POST
	@Path("SaveEmpHoliday")
	@Produces("application/json")
	public String SaveEmpHoliday(@FormParam("SelectedDates") String data){
		String feeds  = null;
		try 
		{

			
			MessageObject msgData = null;
			APIController handler= new APIController();
			msgData = handler.SaveEmpHoliday(data);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	@POST
	@Path("CheckMailExist")
	@Produces("application/json")
	public String CheckMailExist(@FormParam("email") String email){
		String feeds  = null;
		try 
		{

			
			MessageObject msgData = null;
			APIController handler= new APIController();
			msgData = handler.CheckMailExist(email);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}

	@POST
	@Path("SaveTrackUserNew")
	@Produces("application/json")
	public String SaveTrackUserNew(@FormParam("name") String name,@FormParam("email") String email,
			@FormParam("gender") String gender,@FormParam("mob_no") String mob_no,@FormParam("address") String address){
		String feeds  = null;
		try 
		{
			
			TrackUserSighupDTO msgData = null;
			APIController handler= new APIController();
			msgData = handler.SaveTrackUserNew(name,email,gender,mob_no,address);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}

	@POST
	@Path("AddDevice")
	@Produces("application/json")
	public String SaveTrackUserNew(@FormParam("fname") String fname,@FormParam("lname") String lname,
			@FormParam("gender") String gender,@FormParam("mob_no") String mob_no,@FormParam("imei_no") String imei_no
			,@FormParam("device_type") String device_type,@FormParam("user_id") String user_id){
		String feeds  = null;
		try 
		{
			

			
			MessageObject msgData = null;
			APIController handler= new APIController();
			msgData = handler.AddDevice(fname,lname,gender,mob_no,imei_no,device_type,user_id);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("CheckIMEIExist")
	@Produces("application/json")
	public String CheckIMEIExist(@FormParam("imei_no") String imei_no){
		String feeds  = null;
		try 
		{

			
			MessageObject msgData = null;
			APIController handler= new APIController();
			msgData = handler.CheckIMEIExist(imei_no);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	
	@POST
	@Path("GetEmpDevicelist")
	@Produces("application/json")
	public String GetDevicelist(@FormParam("UserId") String user_id){
		String feeds  = null;
		try 
		{

			
			ArrayList<DevicelistDetails> msgData = null;
			APIController handler= new APIController();
			msgData = handler.GetEmpDevicelist(user_id);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	@POST
	@Path("SaveTaskToEmp")
	@Produces("application/json")
	public String SaveTaskToEmp(@FormParam("emp_user_id") String emp_user_id,@FormParam("user_id") String user_id,@FormParam("emp_car_id") String emp_car_id,
			@FormParam("day") String day,@FormParam("month") String month
			,@FormParam("year") String year,@FormParam("time") String time,@FormParam("address") String address
			,@FormParam("route") String route,@FormParam("row_id") String row_id,@FormParam("isedit") String isedit){
		String feeds  = null;
		try 
		{
			

			
			String msgData = null;
			APIController handler= new APIController();
			msgData = handler.SaveTaskToEmp(emp_user_id,user_id,emp_car_id,day,month,year,time,address,route,row_id,isedit);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	@POST
	@Path("GetDriverEmpTaskList")
	@Produces("application/json")
	public String GetDriverEmpTaskList(@FormParam("emp_user_id") String emp_user_id,
			@FormParam("day") String day,@FormParam("month") String month,@FormParam("year") String year){
		String feeds  = null;
		try 
		{
			

			
			ArrayList<DriverEmpTaskSheduledDTO> msgData = null;
			APIController handler= new APIController();
			msgData = handler.GetDriverEmpTaskList(emp_user_id,day,month,year);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	@POST
	@Path("GetDriverEmpDaywiseTaskList")
	@Produces("application/json")
	public String GetDriverEmpDaywiseTaskList(@FormParam("emp_user_id") String emp_user_id){
		String feeds  = null;
		try 
		{	ArrayList<DriverEmpTaskSheduledDTO> msgData = null;
			APIController handler= new APIController();
			msgData = handler.GetDriverEmpDaywiseTaskList(emp_user_id);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}

	@POST
	@Path("GetUserUtilityAPI")
	@Produces("application/json")
	public String GetUserUtilityAPI(@FormParam("UserId") int userid,@FormParam("RoleId") int roleid)
	{
		String feeds  = null;
		try 
		{
			ArrayList<UserUtilityDTO> msgData = new ArrayList<UserUtilityDTO>();
			APIController handler= new APIController();
			msgData = handler.GetUserUtilityAPI(userid,roleid);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return feeds;
	}
}
