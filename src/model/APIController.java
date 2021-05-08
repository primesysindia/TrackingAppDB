package model;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.json.JsonArray;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONArray;




















































import SQLdao.SQL_QuizDAO;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.client.MongoDatabase;
import com.mysql.jdbc.log.Log;

import dao.AdminServiceDao;
import dao.AttendenceDAO;
import dao.Database;
import dao.DeviceCebugDAO;
import dao.DeviceOnOffInfoDto;
import dao.LoginDAO;
import dao.LoginServiceDao;
import dao.ParentDao;
import dao.RDPSUploadDAO;
import dao.ReportDAO;
import dao.StationarySpinnerdataDAO;
import dao.TaskManagementDAO;
import dto.AddDeviceDropDownInfo;
import dto.AddNewEmpDTO;
import dto.ApplicationMetaData;
import dto.AssociatedParentDTO;
import dto.DailyReportSummaryWithStoppageDTO;
import dto.DateRangeExceptionReportDTO;
import dto.DeactiveDeviceListDTO;
import dto.DepartmentsDTO;
import dto.DeviceBatteryDTO;
import dto.DeviceBatteryInfo;
import dto.DeviceCommandDTO;
import dto.DeviceCommandHistoryDTO;
import dto.DeviceDTO;
import dto.DeviceDataDTO;
import dto.DeviceExchangeDTO;
import dto.DeviceInfoGatheringDTO;
import dto.DeviceInspectionDTO;
import dto.DeviceIssueDTO;
import dto.DevicePaymentDTO;
import dto.DevicePaymentInfoDetailsDTO;
import dto.DeviceSimNo;
import dto.DeviceStatusDTO;
import dto.DeviceStatusInfoDto;
import dto.DevicelistDetails;
import dto.DriverEmpTaskSheduledDTO;
import dto.DriverEmployeeTaskDetailsDTO;
import dto.DeviceRegisterDTO;
import dto.EmpAttendanceDTO;
import dto.ExceptionDeviceDTO;
import dto.ExceptionReportFileDTO;
import dto.FamilyPaymentTypeDTO;
import dto.FeatureAddressDetailsDTO;
import dto.FeatureNearbyDTO;
import dto.GetEmployeeTaskManagementDto;
import dto.HistoryInfoDTO;
import dto.IssueFileInfoDTO;
import dto.LoginUserDto;
import dto.MailFormatDTO;
import dto.PatrolManBeatDTO;
import dto.RailDeviceInfoDto;
import dto.RailWayAddressDTO;
import dto.FrdlistDTO;
import dto.GeofenceDTO;
import dto.HistoryDTO;
import dto.ListDeviceNotconnectedDTO;
import dto.LocationDTO;
import dto.LoginDto;
import dto.MainSliderImageDTO;
import dto.MessageObject;
import dto.MilageDTO;
import dto.PersonDetails;
import dto.RailwayDeptHierarchyDTO;
import dto.RailwayKeymanDTO;
import dto.RailwayPatrolManDTO;
import dto.ReportDataAdminDTO;
import dto.ReportSummeryDTO;
import dto.SmsNotificationDTO;
import dto.SosDto;
import dto.SosInfoDTO;
import dto.StationaryDataDTO;
import dto.StationaryPurchaseItemInfo;
import dto.StationarySpinnerDTO;
import dto.StudentAttenInfo;
import dto.TaskMgntAddressDTO;
import dto.TrackUserSighupDTO;
import dto.TripInfoDto;
import dto.UserDTO;
import dto.UserModuleDTO;
import dto.UserUtilityDTO;
import dto.VTSDataDTO;
import dto.VehicalTrackingSMSCmdDTO;
import dto.WrongLocationDataDTO;
import dto.classDTO;

public class APIController {
	static Connection con=null;
	static Connection conSunMssql=null;

	static Database database=null;
	MessageObject msgObj;
	static DB mongoconnection=null;
	public Mongo mongo_instance=null;
	//static Connection mysqlconnection=null;
	// static variable single_instance of type Singleton
    private static APIController single_api_instance = null;
    
    
	public APIController() {
		
		try
		{
			con = Database.getInstance().conn;
			this.mongo_instance=Database.mongo_instance;
			mongoconnection=mongo_instance.getDB("tracking");	
			conSunMssql= Database.getInstance().connSunMssql;
			//mongoconnection=mongo_instance.getDB("tracking");
		//	mysqlconnection=database.Get_MysqlConnection();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
	}
	 // static method to create instance of Singleton class
    public static APIController getInstance()
    {
        if (single_api_instance == null)
        	single_api_instance = new APIController();
 
        return single_api_instance;
    }
	
	

   

	
	//Login

	public MessageObject validlogin(String  userid,String password) throws Exception {

		try { 
			LoginDAO dao=new LoginDAO();
			msgObj=dao.validlogin(con,userid,password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgObj;



	}
	public ArrayList<PersonDetails> GetRequestedList(String userid) throws Exception {
		ArrayList<PersonDetails> person=new ArrayList<PersonDetails>();
		try { 

			LoginDAO dao=new LoginDAO();
			person=dao.GetRequestedList(con,userid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return person;
	}
	public MessageObject registration(String name,String address,String contact,String emailid,String password) throws Exception {

		try { 
			LoginDAO dao=new LoginDAO();
			msgObj=dao.registration(con,name,address,contact,emailid,password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgObj;



	}

	public LocationDTO GetTrackInfo(String invited_id) {
		LocationDTO dtp=new LocationDTO();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetTrackInfo(con,invited_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}
	public MessageObject PushLocation(String id,String lat,String Lan,String time) {
		MessageObject dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.PushLocation(con,id,lat,Lan,time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}

	public ArrayList<FrdlistDTO> SearchFriendList(Integer id, String entity, Integer flag) {
		ArrayList<FrdlistDTO> dtp=new ArrayList<FrdlistDTO>();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.SearchFriendList(con,id,entity,flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}

	public MessageObject SendInvitation(Integer sendId, Integer getId) {
		MessageObject dtp=new MessageObject();

		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.SendInvitation(con,sendId,getId,mongoconnection);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}

	public ArrayList<PersonDetails> GetInvitationList(String id) {
		ArrayList<PersonDetails> person=new ArrayList<PersonDetails>();
		try { 

			LoginDAO dao=new LoginDAO();
			person=dao.GetInvitationList(con,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return person;
	}

	public MessageObject AcceptInvitation(String invi_id) {
		// TODO Auto-generated method stub
		
		MessageObject dtp=new MessageObject();

		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.AcceptInvitation(con,invi_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}

	public LocationDTO Getlocation(String invi_id, String user_id) throws Exception {
		LocationDTO dtp=new LocationDTO();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.Getlocation(con,invi_id,user_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}

	public MessageObject RejectInvitation(String invi_id) {
		// TODO Auto-generated method stub
		
		MessageObject dtp=new MessageObject();

		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.RejectInvitation(con,invi_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}

	public ArrayList<PersonDetails>  GetAcceptedtrackperson(String userid) {
		// TODO Auto-generated method stub
		
		ArrayList<PersonDetails> dtp=new ArrayList<PersonDetails> ();

		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetAcceptedtrackperson(con,userid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}

	public MessageObject BlockTrakRequest(String invi_id) {

		// TODO Auto-generated method stub
		
		MessageObject dtp=new MessageObject();

		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.BlockTrakRequest(con,invi_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	
	}

	public void PostLocation_Report(String invi_id, String user_id, String lat, String lang) {
		LocationDTO dtp=new LocationDTO();
		try { 
			LoginDAO dao=new LoginDAO();
			dao.PostLocation_Report(con,invi_id,user_id,lat,lang);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<StationarySpinnerDTO> GetStationarySpinnerdata(String flag, String id, String schoolid) {
		ArrayList<StationarySpinnerDTO> dtp=new ArrayList<StationarySpinnerDTO>();
		try { 
			StationarySpinnerdataDAO dao=new StationarySpinnerdataDAO();
			dtp=dao.GetStationarySpinnerdata(con,flag,id,schoolid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}

	public StationaryPurchaseItemInfo GetItemforSubject(String cat_id,
			String subcat_id, String class_id, String subject_id) {
		StationaryPurchaseItemInfo dtp=new StationaryPurchaseItemInfo();

		try { 
			StationarySpinnerdataDAO dao=new StationarySpinnerdataDAO();
			dtp=dao.GetItemforSubject(con,cat_id,subcat_id,class_id,subject_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}

	public StationaryPurchaseItemInfo GetItemforCustomer(String cat_id,
			String subcat_id, String mesure_id, String dimen_id, String color_id) {
		StationaryPurchaseItemInfo dtp=new StationaryPurchaseItemInfo();

		try { 
			StationarySpinnerdataDAO dao=new StationarySpinnerdataDAO();
			dtp=dao.GetItemforCustomer(con,cat_id,subcat_id,mesure_id,dimen_id,color_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}

	public MessageObject SaveStationaryPayment(String userid,
			String totalamount, String paymentmode, String paymentstatus,
			String orderid, String itemtypeid, String quantity,
			String itemamount) {
		MessageObject dtp=new MessageObject();

		try { 
			StationarySpinnerdataDAO dao=new StationarySpinnerdataDAO();
			dtp=dao.SaveStationaryPayment(con,userid, totalamount,paymentmode,paymentstatus,orderid,itemtypeid,quantity,itemamount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}


	public ArrayList<StationaryDataDTO> GetStationatydata(String userid) {
		ArrayList<StationaryDataDTO> dtp=null;
		try { 
			StationarySpinnerdataDAO dao=new StationarySpinnerdataDAO();
			dtp=dao.GetStationatydata(con,userid);
		} catch (Exception e) {
			e.printStackTrace();	
			}
		return dtp;
	}

	public ArrayList<classDTO> Getclassofteacher(String teacherid) {
		ArrayList<classDTO> dtp=null;
		try { 
			AttendenceDAO dao=new AttendenceDAO();
			dtp=dao.Getclassofteacher(con,teacherid);
		} catch (Exception e) {
e.printStackTrace();		}
		return dtp;
	}

	public ArrayList<StudentAttenInfo> GetstudentOfTeacherApp(String classid,
			String schoolid) {
		ArrayList<StudentAttenInfo> dtp=null;
		try { 
			AttendenceDAO dao=new AttendenceDAO();
			dtp=dao.GetstudentOfTeacherApp(con,classid,schoolid);
		} catch (Exception e) {
e.printStackTrace();		}
		return dtp;
	}

	public MessageObject SaveAttendencetoServer(JSONArray attendencelist, String teacherid, String schoolid, String classid) {
		MessageObject dtp=null;
		try { 
			AttendenceDAO dao=new AttendenceDAO();
			dtp=dao.SaveAttendencetoServer(con,attendencelist,teacherid,schoolid,classid);
		} catch (Exception e) {
e.printStackTrace();		}
		return dtp;
	}

	
	public static MessageObject SaveApiReport(String Module,
			String Event, String Apiname,String UserId,
			String Comment, String ObjectAceesed, String DevelopedBy,
			String CalledForm,String Version) {
        Connection conn=null;
		MessageObject msgObj = new MessageObject();
		java.sql.CallableStatement stmt;
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String now =dateFormat.format(cal.getTime());
		try {
			stmt = con.prepareCall("{call SaveApiReport(?,?,?,?,?,?,?,?,?)}");
			
			stmt.setString(1,Module);
			stmt.setString(2,Event);
			stmt.setString(3,Apiname);

			if (UserId==null) 
				stmt.setInt(4,0);
			else			
			stmt.setInt(4,Integer.parseInt(UserId));
			stmt.setString(5,Comment);
			stmt.setString(6,ObjectAceesed);
			stmt.setString(7,DevelopedBy);
			stmt.setString(8,CalledForm);
			stmt.setString(9,Version);
			
			int result = stmt.executeUpdate();
			
			if (result==0) {
				msgObj.setError("true");
				msgObj.setMessage("API Report Failed");
			}else{
				msgObj.setError("false"); 
				msgObj.setMessage("API Report generated Successfully");
			}
			//System.err.println("Report---------=="+msgObj.getMessage());

			
		}
			
		 catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if (conn!=null) {
					conn.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return msgObj;
	}

	public DeviceSimNo GetDeviceSimNo(String studentid, String command) {
		DeviceSimNo dtp=new DeviceSimNo();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetDeviceSimNo(con,studentid,command);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}

	public MessageObject PostDeviceSimno(String studentid, String simno) {
		MessageObject dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.PostDeviceSimno(con,studentid,simno);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}

	public DeviceSimNo GetCommandtoTrack(String studentid, String command) {
		DeviceSimNo dtp=new DeviceSimNo();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetCommandtoTrack(con,studentid,command);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public ArrayList<SosDto> GetSosList(String userid) {
		ArrayList<SosDto>  dtp=new ArrayList<SosDto> ();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetSosList(mongoconnection,userid);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}



	public MessageObject SaveSOS_onServer(String userid, String name,
			String number) {
		MessageObject  dtp=new MessageObject ();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.SaveSOS_onServer(mongoconnection,userid,name,number);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public MessageObject DeleteSOs(String sosid, String userid) {
		MessageObject  dtp=new MessageObject ();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.DeleteSOs(mongoconnection,sosid,userid);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public VTSDataDTO GetstudentVTSStatus(String studId) {
		VTSDataDTO  dtp=new VTSDataDTO ();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetstudentVTSStatus(con,studId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject PostEnginePin(String studId, String pin) {
		MessageObject  dtp=new MessageObject ();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.PostEnginePin(con,studId,pin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject GetMilage(String device, String startdate, String enddate) {
		MessageObject dtp =new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetMilage(mongoconnection,device,startdate,enddate);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public ArrayList<VehicalTrackingSMSCmdDTO> GetSMSItemlist(String studId) {
		ArrayList<VehicalTrackingSMSCmdDTO> dtp=new ArrayList<VehicalTrackingSMSCmdDTO>();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetSMSItemlist(con,studId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public DeviceSimNo GetSMSDeviceSimNo(String studentid) {
		DeviceSimNo dtp=new DeviceSimNo();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetSMSDeviceSimNo(con,studentid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public ArrayList<UserModuleDTO> GetUserModuleList(String userid, String roleid) {
		ArrayList<UserModuleDTO> dtp=new ArrayList<UserModuleDTO>();
		try { 
			ParentDao dao=new ParentDao();
			dtp=dao.GetUserModuleList(con,userid,roleid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public ArrayList<VehicalTrackingSMSCmdDTO> GetVehReportlist(String studId) {
		ArrayList<VehicalTrackingSMSCmdDTO> dtp=new ArrayList<VehicalTrackingSMSCmdDTO>();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetVehReportlist(con,studId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject PostNotificationData_Server(SmsNotificationDTO smsdata) {
		MessageObject  dtp=new MessageObject ();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.PostNotificationData_Server(con,smsdata);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject GetVehTotalKm(String device, String startdate, String enddate) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetMilage(mongoconnection,device,startdate,enddate);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public MessageObject GenerateTripReport() {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GenerateTripReport(con,mongoconnection);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}



	public MessageObject GenerateTripReportIdCard() {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GenerateTripReportIdCard(con,mongoconnection);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}

	public MessageObject GenerateTripReport_USDevice() {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GenerateTripReport_USDevice(con,mongoconnection);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	
	public MessageObject SendForgrtPassword(String email) {
		
		MessageObject msgObj=null;
		try { 
			LoginDAO dao=new LoginDAO();
			msgObj=dao.SendForgrtPassword(con,email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgObj;
	}



/*
	public ArrayList<LoginDto> GetLoginDetails(String username, String password,
			String appid, String appmailid) {
		ArrayList<LoginDto>  dtp=new ArrayList<LoginDto>();
		try { 
			ParentDao dao=new ParentDao();
			dtp=dao.GetLoginDetails(mysqlconnection,username,password,appid,appmailid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}

*/

	public ArrayList<MainSliderImageDTO> GetMainSliderImage() {
		ArrayList<MainSliderImageDTO> msgObj = null;

		try { 
			ParentDao dao=new ParentDao();
			msgObj=dao.GetMainSliderImage(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgObj;

	}




	public ArrayList<DeviceStatusDTO> Get_DeviceStatus(JSONArray devielist) {
		ArrayList<DeviceStatusDTO> msgObj=new ArrayList<DeviceStatusDTO>();

		try { 
			LoginDAO dao=new LoginDAO();
			msgObj=dao.Get_DeviceStatus(mongoconnection,devielist);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return msgObj;

	}




	public MessageObject GetTripReport(String device, String startdate,
			String enddate, String email) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetTripReport(con,mongoconnection,device,startdate,enddate,email);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
		
		
		
	}




	public MessageObject UpdateAllowFlag(String vtsSmsAllow,
			String aCCReportAllow, String accEOD, String aCCSqliteEnable,
			String email) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.UpdateAllowFlag(con,vtsSmsAllow,aCCReportAllow,accEOD,aCCSqliteEnable,email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
		
		
	}




	public MessageObject UpdateAllowRepotFlag(String allowFlag, String email) {
	MessageObject  dtp=new MessageObject();
	try { 
		LoginDAO dao=new LoginDAO();
		dtp=dao.UpdateAllowRepotFlag(con,allowFlag,email);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return dtp;
	}




	public MessageObject postGuestUserRequest(String name, String email,
			String contact, String msg, String type, String city) {
		MessageObject dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.postGuestUserRequest(con,name,email,contact,msg,type,city);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}



	public MessageObject GetMilage1(String device, String startdate, String enddate) {
		MessageObject dtp =new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetMilage_old(mongoconnection,device,startdate,enddate);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public MessageObject DropLocation() {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.DropLocation(mongoconnection);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public MessageObject DropTripReport() {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.DropTripReport(mongoconnection);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;

	}




	public MessageObject GeneratePendingTripReport(String startdate, String enddate) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GeneratePendingTripReport(con,mongoconnection,startdate,enddate);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public MessageObject DeviceRegisterStatus(String device) {
		MessageObject  dtp=new MessageObject();
		try { 
			DeviceCebugDAO dao=new DeviceCebugDAO();
			dtp=dao.DeviceRegisterStatus(mongoconnection,device);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public MessageObject CheckDeviceIsConnectToserver(String device) {
		MessageObject  dtp=new MessageObject();
		try { 
			DeviceCebugDAO dao=new DeviceCebugDAO();
			dtp=dao.CheckDeviceIsConnectToserver(mongoconnection,device);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public ArrayList<LocationDTO> CheckDeviceLatestLocation(String device) {
		 ArrayList<LocationDTO>  dtp=new  ArrayList<LocationDTO>();
		try { 
			DeviceCebugDAO dao=new DeviceCebugDAO();
			dtp=dao.CheckDeviceLatestLocation(mongoconnection,device);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public ArrayList<TripInfoDto> CheckTripReportGenerteOnserver(String device,
			String startdate, String enddate) {
		ArrayList<TripInfoDto>  dtp=new ArrayList<TripInfoDto>();
		try { 
			DeviceCebugDAO dao=new DeviceCebugDAO();
			dtp=dao.CheckTripReportGenerteOnserver(mongoconnection,device,startdate,enddate);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public ArrayList<GeofenceDTO> CheckGeofence(String device) {
		 ArrayList<GeofenceDTO>  dtp=new  ArrayList<GeofenceDTO>();
			try { 
				DeviceCebugDAO dao=new DeviceCebugDAO();
				dtp=dao.CheckGeofence(mongoconnection,device);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if (mongo_instance!=null) 
					mongo_instance.close();

			}
			return dtp;
	}




	public MessageObject ClearTrackingPids(String device) {
		MessageObject  dtp=new MessageObject();
		try { 
			DeviceCebugDAO dao=new DeviceCebugDAO();
			dtp=dao.ClearTrackingPids(mongoconnection,device);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public MessageObject NotifyToUserAboutExpiryDate() {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.NotifyToUserAboutExpiryDate(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject TrackLog(String name) {
		MessageObject  dtp=new MessageObject();
		try { 
			DeviceCebugDAO dao=new DeviceCebugDAO();
			dtp=dao.TrackLog(con,name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject DeleteLogfile(String name) throws Exception {	
		MessageObject  dtp=new MessageObject();

		try { 
			DeviceCebugDAO dao=new DeviceCebugDAO();
			dtp=dao.DeleteLogfile(con,name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public ArrayList<TripInfoDto> GetDirectTripReport(String device,
			String startdate, String enddate) {
		ArrayList<TripInfoDto>  dtp=new ArrayList<TripInfoDto>();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetDirectTripReport(con,mongoconnection,device,startdate,enddate);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public String GetDirectWebTripReport(String device,
			String startdate, String enddate) {
		String dtp="";
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetDirectWebTripReport(con,mongoconnection,device,startdate,enddate);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public ArrayList<FamilyPaymentTypeDTO> GetFamilyPaymentType() {
		ArrayList<FamilyPaymentTypeDTO> dtp=new ArrayList<>();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetFamilyPaymentType(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject UpdateFamilyPayment(String invitedIdsarray, String userId, String typeofpayment, String paymentId) {
		MessageObject  dtp=new MessageObject();

		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.UpdateFamilyPayment(con,invitedIdsarray,userId,typeofpayment,paymentId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public ApplicationMetaData GetApplicationMetaData() {
		ApplicationMetaData  dtp=new ApplicationMetaData();

		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetApplicationMetaData(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject GenerateDevicePendingTripReport(String startdate,
			String enddate, String device) {
		MessageObject  dtp=new MessageObject();
		try { 
			DeviceCebugDAO dao=new DeviceCebugDAO();
			dtp=dao.GenerateDevicePendingTripReport(con,mongoconnection,startdate,enddate,device);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public MessageObject GetTripReportOnMail(String device, String startdate,
			String enddate, String email) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetTripReportOnMail(con,mongoconnection,device,startdate,enddate,email);
		} catch (Exception e) {
				e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public MessageObject GetPaytm_ChecksumGeneration(String orderId, String custId, String amount, String mobno, String email) {
		// TODO Auto-generated method stub
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetPaytm_ChecksumGeneration(orderId,custId,amount,mobno,email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
		}




	public ArrayList<VehicalTrackingSMSCmdDTO> GetMyKiddySMSItemlist(
			String studId) {
		// TODO Auto-generated method stub
		ArrayList<VehicalTrackingSMSCmdDTO> dtp=new ArrayList<VehicalTrackingSMSCmdDTO>();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetMyKiddySMSItemlist(con,studId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
		
	}




	public ListDeviceNotconnectedDTO GetListOfDeviceNotConnected() {
		// TODO Auto-generated method stub
			ListDeviceNotconnectedDTO dtp=new ListDeviceNotconnectedDTO();
		try { 
			DeviceCebugDAO dao=new DeviceCebugDAO();
			dtp=dao.GetListOfDeviceNotConnected(mongoconnection);
		} catch (Exception e) {

			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
		}




	public DeactiveDeviceListDTO GenerateDeactiveDeviceList() {
		DeactiveDeviceListDTO dtp=new DeactiveDeviceListDTO();
		try { 
			DeviceCebugDAO dao=new DeviceCebugDAO();
			dtp=dao.GenerateDeactiveDeviceList(mongoconnection);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
		}




	public DeactiveDeviceListDTO GetDeactiveDeviceList() {
		// TODO Auto-generated method stub
		DeactiveDeviceListDTO dtp=new DeactiveDeviceListDTO();
		try { 
			DeviceCebugDAO dao=new DeviceCebugDAO();
			dtp=dao.GetDeactiveDeviceList(mongoconnection);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
		}




	public ArrayList<HistoryDTO> GetHistorydata(String student_id,
			String startdate) {
		ArrayList<HistoryDTO> dtp=new ArrayList<HistoryDTO>();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetHistorydata(mongoconnection,student_id,startdate);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				/*if (con!=null) 
					con.close();
*/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public ArrayList<UserModuleDTO> GetUserNewModuleList(String userid,
			String roleid) {
		ArrayList<UserModuleDTO> dtp=new ArrayList<UserModuleDTO>();
		try { 
			ParentDao dao=new ParentDao();
			dtp=dao.GetUserNewModuleList(con,userid,roleid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public EmpAttendanceDTO GetEmpDay_status(String userid,
			String day, String month, String year) {
		EmpAttendanceDTO dtp=new EmpAttendanceDTO();
		try { 
			ParentDao dao=new ParentDao();
			dtp=dao.GetEmpDay_status(con,userid,day,month,year);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject UpdateAttendance(String userid,String studentid, String day,
			String month, String year, String time) {
		MessageObject dtp=new MessageObject();
		try { 
			ParentDao dao=new ParentDao();
			dtp=dao.UpdateAttendance(con,userid, studentid,day,month,year,time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject SaveEmpLeaveApplication(String data) {
		MessageObject dtp=new MessageObject();
		try { 
			ParentDao dao=new ParentDao();
			dtp=dao.SaveEmpLeaveApplication(con,data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public ArrayList<EmpAttendanceDTO> GetEmpMonth_Attendance(String stud_id,
			String month, String year, String user_id) {
		 ArrayList<EmpAttendanceDTO>  dtp=new ArrayList<>();
		try { 
			ParentDao dao=new ParentDao();
			dtp=dao.GetEmpMonth_Attendance(con,stud_id,month,year,user_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject GrantLeaveApplication(String data) {
		MessageObject dtp=new MessageObject();
		try { 
			ParentDao dao=new ParentDao();
			dtp=dao.GrantLeaveApplication(con,data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject SaveEmpUpdateDayApplication(String data) {
		MessageObject dtp=new MessageObject();
		try { 
			ParentDao dao=new ParentDao();
			dtp=dao.SaveEmpUpdateDayApplication(con,data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject SaveEmpHoliday(String data) {
		MessageObject dtp=new MessageObject();
		try { 
			ParentDao dao=new ParentDao();
			dtp=dao.SaveEmpHoliday(con,data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject SendMsg_GetFetureExpireData() {
		MessageObject dtp=new MessageObject();
		try { 
			DeviceCebugDAO dao=new DeviceCebugDAO();
			dtp=dao.SendMsg_GetFetureExpireData(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject CheckMailExist(String email) {
		MessageObject dtp=new MessageObject();
		try { 
			ParentDao dao=new ParentDao();
			dtp=dao.CheckMailExist(con,email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public TrackUserSighupDTO SaveTrackUserNew(String name, String email,
			String gender, String mob_no, String address) {
		TrackUserSighupDTO dtp=new TrackUserSighupDTO();
		try { 
			ParentDao dao=new ParentDao();
			dtp=dao.SaveTrackUserNew(con,name,email,gender,mob_no,address);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject AddDevice(String fname, String lname, String gender,
			String mob_no, String imei_no, String device_type, String user_id) {
		MessageObject dtp=new MessageObject();
		try { 
			ParentDao dao=new ParentDao();
			dtp=dao.AddDevice(con,fname,lname,gender,mob_no,imei_no,device_type,user_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject CheckIMEIExist(String imei_no) {
		MessageObject dtp=new MessageObject();
		try { 
			ParentDao dao=new ParentDao();
			dtp=dao.CheckIMEIExist(con,imei_no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject GenerateEmpTripReport() {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GenerateEmpTripReport(con,mongoconnection);
		} catch (Exception e) {
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public ArrayList<TripInfoDto> GetDirectEmpTripReport(String device,
			String startdate, String enddate) {
		// TODO Auto-generated method stub
		ArrayList<TripInfoDto>  dtp=new ArrayList<TripInfoDto>();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetDirectEmpTripReport(mongoconnection,con,device,startdate,enddate);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
		}




	public MessageObject GetEmpTripReport(String device, String startdate,
			String enddate, String email) {
		
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetEmpTripReport(mongoconnection, con, device,startdate,enddate,email);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
		
		
	}




	public ArrayList<DevicelistDetails> GetEmpDevicelist(String user_id) {
		ArrayList<DevicelistDetails>  dtp=new ArrayList<DevicelistDetails> ();
		try { 
			ParentDao dao=new ParentDao();
			dtp=dao.GetEmpDevicelist(con,user_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public MessageObject PostGcmKeyToServer(String gcm_key, String user_id, String name) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.PostGcmKeyToServer(mongoconnection, con, gcm_key,user_id,name);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
		
		
	}




	public String SaveTaskToEmp(String emp_user_id, String user_id,
			String emp_car_id, String day, String month,
			String year, String time, String address, String route, String row_id, String isedit) {
		String  dtp="";
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.SaveTaskToEmp( con, mongoconnection,emp_user_id,user_id,emp_car_id,day,month,year,time,address,route,row_id,isedit);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();

				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
		
	}




	public ArrayList<DriverEmpTaskSheduledDTO> GetDriverEmpTaskList(
			String emp_user_id, String day, String month, String year) {
		ArrayList<DriverEmpTaskSheduledDTO>  dtp=new ArrayList<DriverEmpTaskSheduledDTO>();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetDriverEmpTaskList( con, mongoconnection,emp_user_id,day,month,year);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public ArrayList<DriverEmpTaskSheduledDTO> GetDriverEmpDaywiseTaskList(
			String emp_user_id) {
		ArrayList<DriverEmpTaskSheduledDTO>  dtp=new ArrayList<DriverEmpTaskSheduledDTO>();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetDriverEmpDaywiseTaskList( con, mongoconnection,emp_user_id);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public MessageObject CheckDriverOnRoute() {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.CheckDriverOnRoute(mongoconnection, con);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public MessageObject GetWeekalyTripReportOnMail(String device,
			String startdate, String enddate, String email) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetWeekalyTripReportOnMail(con,mongoconnection,device,startdate,enddate,email);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public MessageObject GeneratePendingTripReport_USDevice(String startdate, String enddate) throws Exception {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GeneratePendingTripReport_USDevice(con,mongoconnection,startdate,enddate);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}



	
	public ArrayList<HistoryDTO>  GetAllDeviceLocation(String parentId, String studentId) {
		ArrayList<HistoryDTO>  dtp=new ArrayList<HistoryDTO>();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.GetAllDeviceLocation(con,mongoconnection,parentId,studentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public ArrayList<GeofenceDTO> GetGeofenceHistory(String device_imei,
			String startdate, String enddate) {
		ArrayList<GeofenceDTO>  dtp=new ArrayList<GeofenceDTO>();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.GetGeofenceHistory(con,mongoconnection,device_imei,startdate,enddate);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}




	public ArrayList<AssociatedParentDTO> GetAssociatedParent(String userId) {
		ArrayList<AssociatedParentDTO>  dtp=new ArrayList<AssociatedParentDTO>();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.GetAssociatedParent(con,userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dtp;
	}




	public ArrayList<HistoryDTO> GetOptimizedAllDeviceLocation(String parentId) {
		ArrayList<HistoryDTO>  dtp=new ArrayList<HistoryDTO>();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.GetOptimizedAllDeviceLocation(con,mongoconnection,parentId);
			
		} catch (Exception e) {

			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				con.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	
	
	public MessageObject SetBlindLocation(String devices) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.SetBlindLocation(mongoconnection,devices);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	public MessageObject addMultipleDeviceFromText(String devices, String sim_no, String parentId, String name, int startCount) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.addMultipleDeviceFromText(con,devices,sim_no,parentId,name,startCount);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			/*try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

		}
		return dtp;
	}
	public MessageObject assigndeviceToMongodb(String parentId) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.assigndeviceToMongodb(con,mongoconnection,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	
	
	public MessageObject makePaymentToDevice(String startStudentId,
			String endStudentId) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.makePaymentToDevice(con,startStudentId,endStudentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			

		}
		return dtp;
	}
	
	public MessageObject addTrainAddress(String filename, String railWay,
			String division, String station_From, String station_TO,
			String chainage, String trolley, String line, String mode,
			String kiloMeter, String distance, String latitude,
			String longitude, String feature_Code, String feature_Detail,
			String ParentId, String section) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.addTrainAddress(mongoconnection,filename,railWay,division,station_From,
					station_TO,chainage,trolley,
					line,mode,kiloMeter,distance,latitude,longitude,feature_Code,feature_Detail,ParentId,section);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	
	
	
	public ArrayList<RailWayAddressDTO> GetFeatureAddress(String parentId) {
		ArrayList<RailWayAddressDTO>  dtp=new ArrayList<RailWayAddressDTO>();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetFeatureAddress(con,mongoconnection,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
		
	}
	public MessageObject addRanchiTrainAddress(String railWay, String division,
			String section, String latitude, String longitude,
			String feature_Code, String feature_Detail, String parentId) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.addRanchiTrainAddress(mongoconnection,railWay,division,section,
					latitude,longitude,feature_Code,feature_Detail,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	
	
	public ArrayList<RailWayAddressDTO> GetKmFeatureAddress(String parentId) {
		ArrayList<RailWayAddressDTO>  dtp=new ArrayList<RailWayAddressDTO>();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GetKmFeatureAddress(mongoconnection,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	
	
	public ArrayList<RailDeviceInfoDto> GenerateGPSHolder10MinData(String startdate, String pastMin, String parentId) {
		ArrayList<RailDeviceInfoDto>  dtp=new ArrayList<RailDeviceInfoDto>();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.GenerateGPSHolder10MinData(mongoconnection,con,startdate,pastMin,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	
	public ArrayList<RailDeviceInfoDto> GenerateGPSDeviceOnData(String startdate, String parentId
			) {
		ArrayList<RailDeviceInfoDto>  dtp=new ArrayList<RailDeviceInfoDto>();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.GenerateGPSDeviceOnData(mongoconnection,con,startdate,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	public ArrayList<RailDeviceInfoDto> GenerateGPSDeviceOFFData(
			String startdate, String parentId) {
		ArrayList<RailDeviceInfoDto>  dtp=new ArrayList<RailDeviceInfoDto>();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.GenerateGPSDeviceOFFData(mongoconnection,con,startdate,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	
	
	public MessageObject ExportRailAddressToSql(String parentId) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.ExportRailAddressToSql(mongoconnection,con,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	public MessageObject RenewMultiplePaymentToDevice(String startStudentId,
			String endStudentId) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.RenewMultiplePaymentToDevice(con,startStudentId,endStudentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	
	public MessageObject FeaturecodeNearby( String featurecode, String near_bydist, String parentId) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.FeaturecodeNearby(con,mongoconnection,featurecode,near_bydist,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	
	
	public ArrayList<FeatureNearbyDTO> GetFeaturecodeNearby(String startdate,
			String featurecode, String parentId) {
		ArrayList<FeatureNearbyDTO>  dtp=new ArrayList<FeatureNearbyDTO>();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.GetFeaturecodeNearby(con,mongoconnection,startdate,featurecode,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	public ArrayList<SosInfoDTO> GetSosData(String startdate, String parentId) {
		ArrayList<SosInfoDTO>  dtp=new ArrayList<SosInfoDTO>();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.GetSosData(con,mongoconnection,startdate,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	public ArrayList<SosInfoDTO> GetBatteryData(String parentId) {
		ArrayList<SosInfoDTO>  dtp=new ArrayList<SosInfoDTO>();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.GetBatteryData(con,mongoconnection,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	public ArrayList<DeviceStatusInfoDto> GetCurrentAllDeviceStaus() {
		ArrayList<DeviceStatusInfoDto>  dtp=new ArrayList<DeviceStatusInfoDto>();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.GetCurrentAllDeviceStaus(mongoconnection,con);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	public MessageObject Generate_Idcard_DevicePendingTripReport(
			String startdate, String enddate, String device) {
		
		MessageObject  dtp=new MessageObject();
		try { 
			DeviceCebugDAO dao=new DeviceCebugDAO();
			dtp=dao.Generate_Idcard_DevicePendingTripReport(con,mongoconnection,startdate,enddate,device);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	public LoginUserDto getLoginDetails(String username, String password) {

		
		LoginUserDto  dtp=new LoginUserDto();
		try { 
			LoginServiceDao dao=new LoginServiceDao();
			dtp=dao.getLoginDetails(con,username,password);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
		
	}
	
	
	public ArrayList<DeviceDataDTO> getTrackInfo(String parentId) {
		ArrayList<DeviceDataDTO>  dtp=new ArrayList<DeviceDataDTO>();
		try { 
			LoginServiceDao dao=new LoginServiceDao();
			dtp=dao.getTrackInfo(con,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			

		}
		return dtp;
	}
	


	public MessageObject assignSimNumberSqlToMongodb( ) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.assignSimNumberSqlToMongodb(con,mongoconnection);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	public MessageObject trainAlertForGangman(Long timestamp, int speed,
			Double lat, Double lan, String train_device) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.trainAlertForGangman(con,mongoconnection,timestamp,speed,lat,lan,train_device);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	public MessageObject SendGPSDeviceOnOffData(String parentId) {
		MessageObject dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.SendGPSDeviceOnOffData(mongoconnection,con,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	public MessageObject SendGPSTripReportData(String parentId) {
		MessageObject dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.SendGPSTripReportData(mongoconnection,con,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	
	public MessageObject UpdateNameOfDevices(String name, String idno, String parentId) {
		MessageObject dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.UpdateNameOfDevices(con,name,idno,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	
	
	public ArrayList<DevicelistDetails> getTrackInfoForCustomUser(
			String parentId) {
		ArrayList<DevicelistDetails>  dtp=new ArrayList<DevicelistDetails> ();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.getTrackInfoForCustomUser(con,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	public MessageObject saveTrackCustomhierarchyUserNew(String name,
			String mobileNo, String emailId, String parentId) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.saveTrackCustomhierarchyUserNew(con,name,mobileNo,emailId,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	public MessageObject SaveRailwayPetrolmanBeatPath(String studentID,
			String kmFromTo, String totalKmCover, String sheetNo,
			String sectionName, String totalTrip) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.SaveRailwayPetrolmanBeatPath(con,studentID,kmFromTo,totalKmCover,sheetNo,sectionName,totalTrip);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	public MessageObject GenerateExceptionReportPetrolmanBeatPath(String parentId, int day, Boolean isSendMail, int seasonId, int timeTolerance, Double distanceTolerance) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GenerateExceptionReportPetrolmanBeatPath(con,mongoconnection,parentId,day,isSendMail,seasonId,timeTolerance,distanceTolerance);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				
				if (con!=null) 
					con.close();
				if (mongo_instance!=null) 
					mongo_instance.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	public MessageObject SaveRailwayKeymanBeatPath(String studentID,
			String kmFrom, String kmTo, String sectionName, String parentId) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.SaveRailwayKeymanBeatPath(con,studentID,kmFrom,kmTo,sectionName,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	public MessageObject GenerateExceptionReportKeyManBeatPath(String parentId) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO();
			dtp=dao.GenerateExceptionReportKeyManBeatPath(con,mongoconnection,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		try {
				
//				if (con!=null) 
//					con.close();
				if (mongo_instance!=null) 
					mongo_instance.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return dtp;
	}
	public MessageObject UpdateDevicesSimNo(String simno, String idno,
			String parentId) {
		MessageObject dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.UpdateDevicesSimNo(con,simno,idno,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	public MessageObject UpdateParentName(String name, String parentEmail) {
		MessageObject dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.UpdateParentName(con,name,parentEmail);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	public MessageObject addVaransiTrainAddressFromCSV(String filename,
			String railWay, String division, String station_From,
			String station_TO, String chainage, String trolley, String line,
			String mode, ArrayList<String> kiloMeter,
			ArrayList<String> distance, ArrayList<String> latitude,
			ArrayList<String> longitude, ArrayList<String> feature_Code,
			ArrayList<String> feature_Detail, String parentId, ArrayList<String> section_Detail) {


		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.addVaransiTrainAddressFromCSV(mongoconnection,filename,railWay,division,station_From,
					station_TO,chainage,trolley,
					line,mode,kiloMeter,distance,latitude,longitude,feature_Code,feature_Detail,parentId,section_Detail);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	
	public MessageObject AlertForStartWorkWithLowBatteryPatrolman(String parentId) {

		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.AlertForStartWorkWithLowBatteryPatrolman(con,mongoconnection,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}

	public MessageObject AlertForKeymanWorkStatusReport(String parentId) {

		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.AlertForKeymanWorkStatusReport(con,mongoconnection,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	public ArrayList<DevicePaymentInfoDetailsDTO> getDevicePaymentInfoForUsers(
			String userId) {
		 ArrayList<DevicePaymentInfoDetailsDTO>  dtp=new  ArrayList<DevicePaymentInfoDetailsDTO>();
		try { 
			LoginServiceDao dao=new LoginServiceDao ();
			dtp=dao.getDevicePaymentInfoForUsers(con,userId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	public MessageObject AlertForInactiveDevicePartolMan(String parentId) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.AlertForInactiveDevicePartolMan(con,mongoconnection,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	
	public MessageObject AlertForInactiveDeviceKeyMan(String parentId) {
		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.AlertForInactiveDeviceKeyMan(con,mongoconnection,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	
	public MessageObject UpdateSectionNameInStudent(String sectionName,
			String studentIds) {
		MessageObject dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.UpdateSectionNameInStudent(con,sectionName,studentIds);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}

	
	public MessageObject AlertForPatrolManTotalBeatNotCover(String parentId) {

		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.AlertForPatrolManTotalBeatNotCover(con,mongoconnection,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}
	public MessageObject AlertForKeyManTotalBeatNotCover(String parentId) {

		MessageObject  dtp=new MessageObject();
		try { 
			LoginDAO dao=new LoginDAO ();
			dtp=dao.AlertForKeyManTotalBeatNotCover(con,mongoconnection,parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			try {
				if (mongo_instance!=null) 
					mongo_instance.close();
				if (con!=null) 
					con.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dtp;
	}


	//code added by Bhavana
		public MessageObject SendFeedback(String email,String satisfy,String usage,String aspect,String compare,String like,String suggestion) {
			MessageObject msgObj=null;
			try { 
				LoginServiceDao dao=new LoginServiceDao();
				msgObj=dao.SendFeedback(email,satisfy,usage,aspect,compare,like,suggestion);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return msgObj;
		}//end code added by Bhavana
		public MessageObject UpdateDevicesIMEIAndSimNo(String simno,
				String imeiNo, String idno, String parentId) {
			MessageObject dtp=new MessageObject();
			try { 
				LoginDAO dao=new LoginDAO ();
				dtp=dao.UpdateDevicesIMEIAndSimNo(con,simno,imeiNo,idno,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				
				try {
					
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public HistoryInfoDTO GetHistoryData(String student_id, String startdate, String enddate) {


			HistoryInfoDTO dtp=new HistoryInfoDTO();
			try { 
				LoginDAO dao=new LoginDAO();
				dtp=dao.GetHistorydata(mongoconnection,student_id,startdate,enddate);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					/*if (con!=null) 
						con.close();
	*/
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return dtp;
		}
		
		//code added by Bhavana
		public MessageObject SaveNewEmp(AddNewEmpDTO empDetail) {
			MessageObject msgObj=null;
			try { 
				TaskManagementDAO dao=new TaskManagementDAO();
				msgObj=dao.saveEmpDetails(con,empDetail);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return msgObj;
		}//end code added By Bhavana
		
		//code added by Bhavana
		public ArrayList<GetEmployeeTaskManagementDto> GetEmployeeDetails(int parentId){
			ArrayList<GetEmployeeTaskManagementDto> data = new ArrayList<GetEmployeeTaskManagementDto>();
			try { 
				TaskManagementDAO dao=new TaskManagementDAO();
				data = dao.GetEmployeeDetails(con,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return data;
		}
		//end code added by Bhavana
		public ArrayList<HistoryDTO> GenerateLocationIPReport() {
			ArrayList<HistoryDTO> dtp=new ArrayList<HistoryDTO>();
			try { 
				LoginDAO dao=new LoginDAO();
				dtp=dao.GenerateLocationIPReport(mongoconnection);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					/*if (con!=null) 
						con.close();
	*/
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
			return dtp;
		}
		public MessageObject GenerateExceptionReportPetrolmanForVaranasi(
				String parentId) {
			MessageObject dtp=new MessageObject();
			try { 
				LoginDAO dao=new LoginDAO();
				dtp=dao.GenerateExceptionReportPetrolmanForVaranasi(con,mongoconnection,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return dtp;
		}
		
		
		public MessageObject AlertForPatrolmanWorkStatusReportForVaranasi(String parentId) {

			MessageObject  dtp=new MessageObject();
			try { 
				LoginDAO dao=new LoginDAO ();
				dtp=dao.AlertForPatrolmanWorkStatusReportForVaranasi(con,mongoconnection,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<ExceptionReportFileDTO> getExceptionReportFile(
				String parentId, String timestamp) {
			ArrayList<ExceptionReportFileDTO> data = new ArrayList<ExceptionReportFileDTO>();
			try { 
				LoginDAO dao=new LoginDAO();
				data = dao.getExceptionReportFile(con,parentId,timestamp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return data;
		}
	
		
		public ArrayList<ExceptionDeviceDTO> GetExceptionDeviceList(
				String parentId, String startTimestamp, String endTimestamp) {
			ArrayList<ExceptionDeviceDTO> data = new ArrayList<ExceptionDeviceDTO>();
			try { 
				LoginDAO dao=new LoginDAO();
				data = dao.GetExceptionDeviceList(con,mongoconnection,parentId,startTimestamp,endTimestamp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return data;
		}
		
		public ArrayList<DateRangeExceptionReportDTO> getDeviceRangeExceptionReport(
				String parentId, String reportType, int startTimestamp, int endTimestamp) {
				ArrayList<DateRangeExceptionReportDTO> data = new ArrayList<DateRangeExceptionReportDTO>();
				try {
					LoginDAO dao=new LoginDAO();
					data = dao.getDeviceRangeExceptionReport(con,parentId,reportType,startTimestamp,endTimestamp);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return data;
		}
		public MessageObject GenerateExceptionReportKeyManBeatPathJabalpur(
				String parentId) {
			MessageObject  dtp=new MessageObject();
			try { 
				LoginDAO dao=new LoginDAO();
				dtp=dao.GenerateExceptionReportKeyManBeatPathJabalpur(con,mongoconnection,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
			}
		public MessageObject SendWrongLocationEmail(String deviceId,

				String lat, String lan, String speed) {
			MessageObject  dtp=new MessageObject();

			try { 
				LoginDAO dao=new LoginDAO();
				dtp=dao.SendWrongLocationEmail(deviceId,lat,lan,speed,con);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				
				try {
					
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
			}
		
		
		public MessageObject ExportKeyManBeatToRDPSData(String parentId) {
			MessageObject  dtp=new MessageObject();
			try { 
				LoginDAO dao=new LoginDAO();
				dtp=dao.ExportKeyManBeatToRDPSData(con,mongoconnection,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<RailDeviceInfoDto> TodayDeviceStatus( String parentId) {
			ArrayList<RailDeviceInfoDto>  dtp=new ArrayList<RailDeviceInfoDto>();
			try { 
				LoginDAO dao=new LoginDAO ();
				dtp=dao.TodayDeviceStatus(mongoconnection,con,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<RailDeviceInfoDto> DeviceONOffStatus(String startdate,
				String parentId) {
			ArrayList<RailDeviceInfoDto>  dtp=new ArrayList<RailDeviceInfoDto>();
			try { 
				LoginDAO dao=new LoginDAO ();
				dtp=dao.DeviceONOffStatus(mongoconnection,startdate,con,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public MessageObject InsertPatrolmanBeat(String device,
				String section, String km, String parentId) {
			MessageObject  dtp=new MessageObject();

			try { 
				LoginDAO dao=new LoginDAO();
				dtp=dao.InsertPatrolmanBeat(device,section,km,parentId,con,mongoconnection);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public ArrayList<MessageObject> BackupNremoveWrongLoc(ArrayList<WrongLocationDataDTO> locationData) {
			ArrayList<MessageObject> data = new ArrayList<MessageObject>();
			try { 
				LoginDAO dao=new LoginDAO();
				data = dao.BackupNremoveWrongLoc(mongoconnection,locationData);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return data;
		}
		public MessageObject GenerateExceptionReportPatrolManBeatPathJabalpur(
				String parentId) {
			MessageObject  dtp=new MessageObject();
			try { 
				LoginDAO dao=new LoginDAO();
				dtp=dao.GenerateExceptionReportPatrolManBeatPathJabalpur(con,mongoconnection,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
			}
		public MessageObject saveAddressOfEmpTask(TaskMgntAddressDTO newEmpData) {
			MessageObject msgObj=null;
			try { 
				TaskManagementDAO dao=new TaskManagementDAO();
				msgObj=dao.saveAddressOfEmpTask(con,newEmpData);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return msgObj;
		}
		public ArrayList<TaskMgntAddressDTO> GetAddressOfEmpTask(int parentId) {
			ArrayList<TaskMgntAddressDTO> data = new ArrayList<TaskMgntAddressDTO>();
			try { 
				TaskManagementDAO dao=new TaskManagementDAO();
				data = dao.GetAddressOfEmpTask(con,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return data;
		}
		public MessageObject DeleteAddressOfEmpTask(int addressId) {
			MessageObject data = new MessageObject();
			try { 
				TaskManagementDAO dao=new TaskManagementDAO();
				data = dao.DeleteAddressOfEmpTask(con,addressId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return data;
		}
		
		public MessageObject saveDriverEmployeeTask(
				DriverEmployeeTaskDetailsDTO data) {
			MessageObject obj = new MessageObject();
			try { 
				TaskManagementDAO dao=new TaskManagementDAO();
				obj = dao.saveDriverEmployeeTask(con,data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return obj;
		}
		public ArrayList<DriverEmployeeTaskDetailsDTO> GetEmployeeTask(
				int driver_id, long startTime, long endTime) {
			ArrayList<DriverEmployeeTaskDetailsDTO> data = new ArrayList<DriverEmployeeTaskDetailsDTO>();
			try { 
				TaskManagementDAO dao=new TaskManagementDAO();
				data = dao.GetEmployeeTask(con,driver_id,startTime,endTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return data;
			
		}
		public MessageObject deleteEmployeeTask(int taskId) {
			MessageObject data = new MessageObject();

			try { 
				TaskManagementDAO dao=new TaskManagementDAO();
				data = dao.deleteEmployeeTask(con,taskId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return data;
		}
		public MessageObject copyEmployeeTask(String taskId, int parentId) {
				MessageObject obj = new MessageObject();
				try { 
					TaskManagementDAO dao=new TaskManagementDAO();
					obj = dao.copyEmployeeTask(con,taskId,parentId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return obj;
		}
		public MessageObject deleteEmployee(int empId) {

			MessageObject data = new MessageObject();

			try { 
				TaskManagementDAO dao=new TaskManagementDAO();
				data = dao.deleteEmployee(con,empId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return data;
		}
		
		public MessageObject addRanchiTrainAddressFromCSV(String filename,

				String railWay, String division, String station_From,
				String station_TO, String chainage, String trolley, String line,
				String mode, ArrayList<String> kiloMeter,
				ArrayList<String> distance, ArrayList<String> latitude,
				ArrayList<String> longitude, ArrayList<String> feature_Code,
				ArrayList<String> feature_Detail, String parentId, ArrayList<String> section_Detail) {


			MessageObject  dtp=new MessageObject();
			try { 
				LoginDAO dao=new LoginDAO ();
				dtp=dao.addRanchiTrainAddressFromCSV(mongoconnection,filename,railWay,division,station_From,
						station_TO,chainage,trolley,
						line,mode,kiloMeter,distance,latitude,longitude,feature_Code,feature_Detail,parentId,section_Detail);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public MessageObject setSectionNameToRailwaykeymanBeat(String parentId) {
			MessageObject  dtp=new MessageObject();
			try { 
				LoginDAO dao=new LoginDAO ();
				dtp=dao.setSectionNameToRailwaykeymanBeat(mongoconnection,con,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public MessageObject GenerateExceptionReportKeyManBeatPathRanchi(
				String parentId) {
			MessageObject  dtp=new MessageObject();
			try { 
				LoginDAO dao=new LoginDAO();
				dtp=dao.GenerateExceptionReportKeyManBeatPathRanchi(con,mongoconnection,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
//					
//					if (con!=null) 
//						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
			}
		public MessageObject GenerateExceptionReportPatrolManBeatPathRanchi(
				String parentId) {
			MessageObject  dtp=new MessageObject();
			try { 
				LoginDAO dao=new LoginDAO();
				dtp=dao.GenerateExceptionReportPatrolManBeatPathRanchi(con,mongoconnection,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
			}
		public MessageObject addSamstipurSlowModeTrainAddressFromCSV(
				String filename, String railWay, String division,
				String station_From, String station_TO, String chainage,
				String trolley, String line, String mode,
				ArrayList<String> kiloMeter, ArrayList<String> distance,
				ArrayList<String> latitude, ArrayList<String> longitude,
				ArrayList<String> feature_Code,
				ArrayList<String> feature_Detail, String parentId,
				ArrayList<String> section_Detail) {
			MessageObject  dtp=new MessageObject();
			try { 
				RDPSUploadDAO dao=new RDPSUploadDAO ();
				dtp=dao.addSamstipurSlowModeTrainAddressFromCSV(mongoconnection,filename,railWay,division,station_From,
						station_TO,chainage,trolley,
						line,mode,kiloMeter,distance,latitude,longitude,feature_Code,feature_Detail,parentId,section_Detail);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject GenerateExceptionReportKeyManBeatPathWholeDay(
				String parentId,int day, Boolean isSendMail, long startTime1, long startTime2, long endTime1, long endTime2) {
			MessageObject  dtp=new MessageObject();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GenerateExceptionReportKeyManBeatPathWholeDay(con,mongoconnection,parentId,day,isSendMail,startTime1,startTime2,endTime1,endTime2);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					
					/*if (con!=null) 
						con.close();*/
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return dtp;
		}
		
		public MessageObject GenerateExceptionReportKeyManBeatPathWholeDayNewLogic(
				String parentId,int day, Boolean isSendMail, long startTime1, long startTime2, long endTime1, long endTime2, Boolean exceptionSummary, Double distancetoleranceInKm) {
			MessageObject  dtp=new MessageObject();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GenerateExceptionReportKeyManBeatPathWholeDayNewLogic(con,mongoconnection,parentId,day,isSendMail,startTime1,
						startTime2,endTime1,endTime2,exceptionSummary,distancetoleranceInKm);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					
					/*if (con!=null) 
						con.close();*/
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return dtp;
		}
		
		public MessageObject GenerateExceptionReportPatrolManBeatPathAsKeymanNewLogic(
				String parentId,int day, Boolean isSendMail, long startTime1, long startTime2, long endTime1, long endTime2) {
			MessageObject  dtp=new MessageObject();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GenerateExceptionReportPatrolManBeatPathAsKeymanNewLogic(con,mongoconnection,parentId,day,isSendMail,startTime1,startTime2,endTime1,endTime2);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					
					/*if (con!=null) 
						con.close();*/
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return dtp;
		}
		public MessageObject GenerateExceptionReportPatrolManBeatPathAsKeyman(
				String parentId, int day, Boolean isSendMail, int dutyStartTime, int dutyEndTime) {
			MessageObject  dtp=new MessageObject();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GenerateExceptionReportPatrolManBeatPathAsKeyman(con,mongoconnection,parentId,day,isSendMail,dutyStartTime,dutyEndTime);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return dtp;
		}
		
		
		public MessageObject SaveHierarchyWithCreateLogin(
				String[] nameArray, String[] mobileNoArray,
				String[] emailIdArray, String[] parenrtIdArray,
				String[] studentsArray) {
			MessageObject  dtp=new MessageObject();
			try { 
				RDPSUploadDAO dao=new RDPSUploadDAO();
				dtp=dao.SaveHierarchyWithCreateLogin(con,mongoconnection,nameArray,mobileNoArray,emailIdArray,
						parenrtIdArray,studentsArray);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return dtp;
		}
		public MessageObject GenerateZoneWiseReport(String zoneId, int day,
				Boolean isSendMail) {
			MessageObject  dtp=new MessageObject();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GenerateZoneWiseReport(con,mongoconnection,zoneId,day,isSendMail);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return dtp;
		}
		public MessageObject insertGeoFenceLocationInTodayLocation(
				) {
//			
//			String device, String startLat, String startLan,
//			String fenceDist
			MessageObject  dtp=new MessageObject();
			try { 
				LoginDAO dao=new LoginDAO ();
				dtp=dao.insertGeoFenceLocationInTodayLocation(con,mongoconnection);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<UserDTO> GetAllTrackUser() {
			ArrayList<UserDTO>  dtp=new ArrayList<UserDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao ();
				dtp=dao.GetAllTrackUser(con);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<RailwayKeymanDTO> GetKeymanExistingBeat(int parentId, int studentId, int beatId,int userLoginId) {
			ArrayList<RailwayKeymanDTO>  dtp=new ArrayList<RailwayKeymanDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetKeymanExistingBeat(con,parentId,studentId,beatId,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject SaveKeymanBeat(int parentId,int studentId, double kmStart, double kmEnd, String sectionName,
				String deviceId, double kmStartLat, double kmStartLang,double kmEndLat, double kmEndLang,int userLoginId) {
			
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.SaveKeymanBeat(con,parentId,studentId,kmStart,kmEnd,sectionName,
						deviceId,kmStartLat,kmStartLang,kmEndLat,kmEndLang,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject UpdateKeymanBeat(int parentId,int studentId, double kmStart, double kmEnd, String sectionName,
				double kmStartLat, double kmStartLang,double kmEndLat, double kmEndLang, int beatId,int userLoginId,Boolean isApprove) {
				MessageObject  dtp=new MessageObject();
				try { 
					AdminServiceDao dao=new AdminServiceDao();
					dtp=dao.UpdateKeymanBeat(con,parentId,studentId,kmStart,kmEnd,sectionName,
							kmStartLat,kmStartLang,kmEndLat,kmEndLang,beatId,userLoginId,isApprove);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					try {
						if (mongo_instance!=null) 
							mongo_instance.close();
						if (con!=null) 
							con.close();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return dtp;
			}
		
		public MessageObject SaveRailwayPatrolManTripTimeMaster(String tripName,String tripStartTime,
				String tripEndTime,int userLoginId, int parentId) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.SaveRailwayPatrolManTripTimeMaster(con,tripName,tripStartTime,tripEndTime,userLoginId,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<String> GetSectionNames(int parentId) {
			ArrayList<String>  dtp=new ArrayList<String>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetSectionNames(con,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}	
		
		public MessageObject SavePatrolManBeat(ArrayList<RailwayPatrolManDTO> Beatlist) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.SavePatrolManBeat(con,Beatlist);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public ArrayList<RailwayPatrolManDTO> GetPatrolManExistingBeat(int parentId, int studentId, int beatId,int userLoginId) {
			ArrayList<RailwayPatrolManDTO>  dtp=new ArrayList<RailwayPatrolManDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetPatrolManExistingBeat(con,parentId,studentId,beatId,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;

		}
		public MessageObject UpdatePatrolManBeat(int beatId, int studentId,int fk_TripMasterId, double kmStart, double kmEnd,
				double totalKmCover, String sectionName, int userLoginId,Boolean isApprove, int seasonId) {

			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.UpdatePatrolManBeat(con,beatId,studentId
						,fk_TripMasterId,kmStart,kmEnd,totalKmCover,sectionName,userLoginId,isApprove,seasonId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;

		}
	
		public ArrayList<RailwayPatrolManDTO> GetRailwayPetrolmanTripsMaster(int parentId) {
			
			ArrayList<RailwayPatrolManDTO>  dtp=new ArrayList<RailwayPatrolManDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetRailwayPetrolmanTripsMaster(con,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject SaveRailwayDeptHierarchy(int deptId,String deptName, String emailId, String mobileNo,
				int deptParentId, String studentsNo, int parentId,int hirachyParentId, int userLoginId) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.SaveRailwayDeptHierarchy(con,deptId,deptName,emailId,mobileNo,deptParentId,studentsNo,parentId,hirachyParentId,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<RailwayDeptHierarchyDTO> GetRailwayDeptHierarchy(int parentId, int hirachyId, int userLoginId) {
			ArrayList<RailwayDeptHierarchyDTO>  dtp=new ArrayList<RailwayDeptHierarchyDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetRailwayDeptHierarchy(con,parentId,hirachyId,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject UpdateRailwayDeptHierarchy(int hierarchyId,int deptId, String Name, String emailId, String mobileNo,
				int deptParentId, String studentsNo, int parentId,int userLoginId, Boolean isApprove) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.UpdateRailwayDeptHierarchy(con,hierarchyId,deptId,Name,emailId,mobileNo,deptParentId,
						studentsNo,parentId,userLoginId,isApprove);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<DepartmentsDTO> GetDepartments() {
			ArrayList<DepartmentsDTO>  dtp=new ArrayList<DepartmentsDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao ();
				dtp=dao.GetDepartments(con);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<DeviceCommandDTO> GetDeviceCommand(int userLoginId) {
			ArrayList<DeviceCommandDTO>  dtp=new ArrayList<DeviceCommandDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetDeviceCommand(con,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<DeviceIssueDTO> GetIssueMasetrList(int userLoginId) {
			ArrayList<DeviceIssueDTO>  dtp=new ArrayList<DeviceIssueDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetIssueMasetrList(con,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject SaveDeviceIssue(int isseMasterId,String contactPerson, String contactPersonMobNo,
				int issueStatus, int priority, int parentId, int studentId,String issueComment, int userLoginId, int isDeviceOn, int isDeviceButtonOn, int isBatteryOn, int isImeiSIMCorrect, int isGSMOn, int isGpsOn, String fileList) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.SaveDeviceIssue(con,isseMasterId,contactPerson,contactPersonMobNo,issueStatus,priority,parentId,
						studentId,issueComment,userLoginId,isDeviceOn,isDeviceButtonOn,isBatteryOn,isImeiSIMCorrect,isGSMOn,isGpsOn,fileList);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject UpdateDeviceIssue(int issueId, int issueStatus,
				int priority, int updatedBy, String issueComment, int isDeviceOn, int isDeviceButtonOn, int isBatteryOn, int isImeiSIMCorrect, int isGSMOn, int isGpsOn, String contactNo) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.UpdateDeviceIssue(con,issueId,issueStatus,priority,updatedBy,issueComment,isDeviceOn,isDeviceButtonOn,isBatteryOn,isImeiSIMCorrect,isGSMOn,isGpsOn,contactNo);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<DeviceIssueDTO> GetIssueDetails(int studentId,int issueId, int userLoginId, long startTime, long endTime) {
			ArrayList<DeviceIssueDTO>  dtp=new ArrayList<DeviceIssueDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetIssueDetails(con,studentId,issueId,userLoginId,startTime,endTime);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<DeviceExchangeDTO> UpdateDeviceExchange(int parentId, int studentId1,int studentId2, Boolean isDeviceSimExchange, int userLoginId) {
			ArrayList<DeviceExchangeDTO>  dtp=new ArrayList<DeviceExchangeDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.UpdateDeviceExchange(mongoconnection,con,parentId,studentId1,studentId2,isDeviceSimExchange,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<DeviceExchangeDTO> GetDeviceExchange(int parentId,int userLoginId) {
			ArrayList<DeviceExchangeDTO>  dtp=new ArrayList<DeviceExchangeDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetDeviceExchange(con,parentId,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject UpdateNewDeviceIMEI(String simNo, int parentId,int studentId, String imeiNo, String firstName) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.UpdateNewDeviceIMEI(mongoconnection,con,simNo,parentId,studentId,imeiNo,firstName);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<DeviceExchangeDTO> UpdateWholeDeviceExchange(int parentId, int studentId1, int studentId2, int userLoginId) {
			ArrayList<DeviceExchangeDTO>  dtp=new ArrayList<DeviceExchangeDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.UpdateWholeDeviceExchange(mongoconnection,con,parentId,studentId1,studentId2,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public DeviceDTO GetDeviceInfo(String studentId, int userLoginId,int issueId, String imeiNo) {
			DeviceDTO dtp=new DeviceDTO();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetDeviceInfo(mongoconnection,con,studentId,userLoginId,issueId,imeiNo);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public ArrayList<DeviceCommandHistoryDTO> GetDeviceCommandHistory(String deviceId, long startTime, long endTime) {
			ArrayList<DeviceCommandHistoryDTO> dtp=new ArrayList<>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetDeviceCommandHistory(mongoconnection,deviceId,startTime,endTime);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<FeatureAddressDetailsDTO> GetRDPSInfo(Float lat,Float lan, int parentId) {
			ArrayList<FeatureAddressDetailsDTO>  dtp=new ArrayList<FeatureAddressDetailsDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetRDPSInfo(con,lat,lan,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;

		}
		public MessageObject GenerateSupportMail(String toMail, String subject,String message, String ccMail) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GenerateSupportMail(mongoconnection,toMail,subject,message,ccMail);
				} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<MailFormatDTO> GetIssueMail() {
			ArrayList<MailFormatDTO>  dtp=new ArrayList<MailFormatDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao ();
				dtp=dao.GetIssueMail(con);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<DeviceBatteryInfo> GetBatteryStatusInfo(String student_id,
				Long startdate, Long enddate) {


			 ArrayList<DeviceBatteryInfo>  dtp=new ArrayList<>();
			try { 
				LoginDAO dao=new LoginDAO();
				dtp=dao.GetBatteryStatusInfo(mongoconnection,student_id,startdate,enddate);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					/*if (con!=null) 
						con.close();
	*/
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return dtp;
		}
	
		public ArrayList<DeviceBatteryDTO> GetBatteryDetailPowerOnOffInfo(String deviceId, Long startdate, Long enddate) {
			 ArrayList<DeviceBatteryDTO>  dtp=new ArrayList<>();
				try { 
					AdminServiceDao dao=new AdminServiceDao();
					dtp=dao.GetBatteryDetailPowerOnOffInfo(mongoconnection,deviceId,startdate,enddate);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					try {
						if (mongo_instance!=null) 
							mongo_instance.close();
						/*if (con!=null) 
							con.close();
		*/
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				return dtp;
		}
		public ArrayList<DevicePaymentDTO> GetDevicePaymentType(int parentId) {
			ArrayList<DevicePaymentDTO>  dtp=new ArrayList<DevicePaymentDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao ();
				dtp=dao.GetDevicePaymentType(con,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public MessageObject MultipleDevicePayment(String studentId,int parentId, int paymentTypeId, String currentStatus) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.MultipleDevicePayment(con,studentId,parentId,paymentTypeId,currentStatus);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}		
		
		public MessageObject GenerateExceptionReportKeyManBeatPathWholeDayByBeatLatLang(
				String parentId,int day, Boolean isSendMail, long startTime1, long startTime2, long endTime1, long endTime2) {
			MessageObject  dtp=new MessageObject();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GenerateExceptionReportKeyManBeatPathWholeDayByBeatLatLang(con,mongoconnection,parentId,day,isSendMail,startTime1,startTime2,endTime1,endTime2);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					
					/*if (con!=null) 
						con.close();*/
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return dtp;
		}
		
		public MessageObject GenerateExceptionReportUSFD(String parentId,int day, Boolean isSendMail, int roleId, String devicetype) {
			MessageObject  dtp=new MessageObject();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GenerateExceptionReportUSFD(con,mongoconnection,parentId,day,isSendMail,roleId,devicetype);
				} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public MessageObject DeleteKeymanBeat(int beatId, int userLoginId) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.DeleteKeymanBeat(con,beatId,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject DeletePatrolManBeat(int beatId,int userLoginId) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.DeletePatrolManBeat(con,beatId,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
	
		public IssueFileInfoDTO UploadIssueFile(String fileName, String fileURL, int userId) {
			IssueFileInfoDTO  dtp=new IssueFileInfoDTO();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.UploadIssueFile(con,fileName,fileURL,userId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject DeleteFileUpload(int fileId, int userLoginId) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.DeleteFileUpload(con,fileId,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public ArrayList<DeviceInfoGatheringDTO> GetDeviceGatheringInfo() {
			ArrayList<DeviceInfoGatheringDTO>  dtp=new ArrayList<DeviceInfoGatheringDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao ();
				dtp=dao.GetDeviceGatheringInfo(con);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<DeviceDataDTO> getAllStudentsWithConnectedStatus(
				String parentId) {
			ArrayList<DeviceDataDTO>  dtp=new ArrayList<DeviceDataDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao ();
				dtp=dao.getAllStudentsWithConnectedStatus(con,mongoconnection,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public MessageObject GenerateExceptionReportKotaKeyMan(String parentId,int day, Boolean isSendMail, long startTime1, long startTime2, long endTime1, long endTime2, Boolean exceptionSummary, Double distancetoleranceInKm) {
			MessageObject  dtp=new MessageObject();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GenerateExceptionReportKotaKeyMan(con,mongoconnection,parentId,day,isSendMail,startTime1,
						startTime2,endTime1,endTime2,exceptionSummary,distancetoleranceInKm,conSunMssql);
				} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		
		public MessageObject GenerateExceptionReportPetrolmanBeatPathNewLogic(
				String parentId, int day, Boolean isSendMail, int seasonId) {
			MessageObject  dtp=new MessageObject();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GenerateExceptionReportPetrolmanBeatPathNewLogic(con,mongoconnection,parentId,day,isSendMail,seasonId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return dtp;
		}
		public MessageObject GenerateExceptionForGateMitra(String parentId,
				int day, Boolean isSendMail, long startTime1, long startTime2,
				long endTime1, long endTime2, String nameStartWith,
				Double distancetoleranceInKm) {
			MessageObject  dtp=new MessageObject();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GenerateExceptionForGateMitra(con,mongoconnection,parentId,day,isSendMail,startTime1,
						startTime2,endTime1,endTime2,nameStartWith,distancetoleranceInKm,conSunMssql);
				} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (conSunMssql!=null) 
						conSunMssql.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
	
		public MessageObject GenerateExceptionReportPetrolmanBeatPathWithWholeTimestampConsider(
				String parentId, int day, Boolean isSendMail, int seasonId, int timeTolerance, Double distanceTolerance) {
			MessageObject  dtp=new MessageObject();
			try { 
				LoginDAO dao=new LoginDAO();
				dtp=dao.GenerateExceptionReportPetrolmanBeatPathWithWholeTimestampConsider(con,
						mongoconnection,parentId,day,isSendMail,seasonId,timeTolerance,distanceTolerance);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return dtp;
		}
		public MessageObject DumpLocationDataToSqlThroughCSV(
				int day, long startTime, long endTime,
				boolean isDumpMOngoDbToCsv, boolean isCsvToSql, boolean isMappedRdps) {
			MessageObject  dtp=new MessageObject();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.DumpLocationDataToSqlThroughCSV(con,mongoconnection,day,startTime,
						endTime,conSunMssql,isDumpMOngoDbToCsv,isCsvToSql,isMappedRdps);
				} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					if (con!=null) 
						con.close();
					
					if (conSunMssql!=null) 
						conSunMssql.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public ArrayList<DailyReportSummaryWithStoppageDTO> GetReportSummeryInfo(int parentId,
				long timeStamp, int roleId) {
			ArrayList<DailyReportSummaryWithStoppageDTO>  dtp=new ArrayList<DailyReportSummaryWithStoppageDTO>();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GetReportSummeryInfo(con,parentId,timeStamp,conSunMssql,roleId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

					if (conSunMssql!=null)
						conSunMssql.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}


		public MessageObject GenerateExceptionReportKotaKeyManFromRdpsMapping(String parentId,int day, Boolean isSendMail, long startTime1, long startTime2, long endTime1, long endTime2, Boolean exceptionSummary, Double distancetoleranceInKm) {
			MessageObject  dtp=new MessageObject();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GenerateExceptionReportKotaKeyManFromRdpsMapping(con,mongoconnection,parentId,day,isSendMail,startTime1,
						startTime2,endTime1,endTime2,exceptionSummary,distancetoleranceInKm,conSunMssql);
				} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (conSunMssql!=null) 
						conSunMssql.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		
		public MessageObject GenerateSaveDailySummaryOfDeviceByParentIdWithCursorMappedRdps(
				String parentId, int day) {
			MessageObject  dtp=new MessageObject();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GenerateSaveDailySummaryOfDeviceByParentIdWithCursorMappedRdps(con,mongoconnection,parentId,day,conSunMssql);
				} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (conSunMssql!=null) 
						conSunMssql.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public ArrayList<DeviceOnOffInfoDto> DeviceONOffStatusAPI(long startdate, int parentId) {
			ArrayList<DeviceOnOffInfoDto>  dtp=new ArrayList<DeviceOnOffInfoDto>();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.DeviceONOffStatusAPI(con,mongoconnection,startdate,parentId,conSunMssql);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject GenerateExceptionReportKeyManBeatFromReportSummary(
				String parentId, int day, Boolean isSendMail, long startTime1,
				long startTime2, long endTime1, long endTime2,
				Boolean exceptionSummary, Double distancetoleranceInKm) {
			MessageObject  dtp=new MessageObject();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GenerateExceptionReportKeyManBeatFromReportSummary(con,mongoconnection,parentId,day,isSendMail,startTime1,
						startTime2,endTime1,endTime2,exceptionSummary,distancetoleranceInKm,conSunMssql);
				} catch (Exception e) {
				e.printStackTrace();
			}finally{
			try {
					if (con!=null) 
						con.close();
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (conSunMssql!=null) 
						conSunMssql.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public ArrayList<DevicePaymentInfoDetailsDTO> GetDevicePaymentInfoForAdmin(
				int parentId) {
			 ArrayList<DevicePaymentInfoDetailsDTO>  dtp=new  ArrayList<DevicePaymentInfoDetailsDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao ();
				dtp=dao.GetDevicePaymentInfoForAdmin(con,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				
				try {
					
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
	
		public MessageObject GetPendingDeviceCommandHistory() {
			MessageObject  dtp=new MessageObject();
				try { 
					AdminServiceDao dao=new AdminServiceDao();
					dtp=dao.GetPendingDeviceCommandHistory(mongoconnection);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					

				}
				return dtp;
		}
		
		public ArrayList<RailwayKeymanDTO> GetDeviceBeat(int studentId) {
			ArrayList<RailwayKeymanDTO>  dtp=new ArrayList<RailwayKeymanDTO>();
			try { 
				LoginDAO dao=new LoginDAO();
				dtp=dao.GetDeviceBeat(con,studentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject GetNotificationForPaymentSubscriptionAPI() {
			MessageObject  dtp=new MessageObject();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GetNotificationForPaymentSubscriptionAPI(con);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject UpsertStudentMasterSQL() {
			MessageObject  dtp=new MessageObject();

			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.UpsertStudentMasterSQL(con,conSunMssql );
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					
					if (con!=null) 
						con.close();
					if (conSunMssql!=null) 
						conSunMssql.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject UpsertRailwayKeymanBeatPathSQL() {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.UpsertRailwayKeymanBeatPathSQL(con,conSunMssql );
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (con!=null) 
						con.close();
					if (conSunMssql!=null) 
						conSunMssql.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<DeviceIssueDTO> GetClientEnteredIssueDetailsForAdmin(
				int studentId, int issueId, int userLoginId, long startTime,
				long endTime) {
			ArrayList<DeviceIssueDTO>  dtp=new ArrayList<DeviceIssueDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetClientEnteredIssueDetailsForAdmin(con,studentId,issueId,userLoginId,startTime,endTime);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
	
		public ArrayList<DailyReportSummaryWithStoppageDTO> GetDailySummaryOfDeviceFromReportSummaryOfPatrolMan(
				int parentId, long timeStamp, int roleId) {
			ArrayList<DailyReportSummaryWithStoppageDTO>  dtp=new ArrayList<DailyReportSummaryWithStoppageDTO>();
			try { 
				ReportDAO dao=new ReportDAO();
				dtp=dao.GetDailySummaryOfDeviceFromReportSummaryOfPatrolMan(con,parentId,timeStamp,conSunMssql,roleId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

					if (conSunMssql!=null)
						conSunMssql.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject UpsertRailwayPatrolManBeatPathSQL() {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.UpsertRailwayPatrolManBeatPathSQL(con,conSunMssql );
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (con!=null) 
						con.close();
					if (conSunMssql!=null) 
						conSunMssql.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public MessageObject UpsertRailwayPatrolManBeatMasterPathSQL() {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.UpsertRailwayPatrolManBeatMasterPathSQL(con,conSunMssql );
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (con!=null) 
						con.close();
					if (conSunMssql!=null) 
						conSunMssql.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject SaveKeymanBeatInBulk(ArrayList<RailwayKeymanDTO> beatInfoList) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.SaveKeymanBeatInBulk(con,beatInfoList);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public ArrayList<RailwayKeymanDTO> GetKeymanExistingBeatByParent(
				int parentId) {
			ArrayList<RailwayKeymanDTO>  dtp=new ArrayList<RailwayKeymanDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetKeymanExistingBeatByParent(con,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public MessageObject UpdateRailwayKeymanBeatPathCopyApprove(int beatId,
				int userLoginId, int existingBeatId) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.UpdateRailwayKeymanBeatPathCopyApprove(con,beatId,userLoginId,existingBeatId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public MessageObject RemoveDeviceAPI(String deviceId1, int userLoginId) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.RemoveDeviceAPI(con,deviceId1,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public MessageObject DeviceUnRegisterAPI(String imeiNo,int userLoginId) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.DeviceUnRegisterAPI(mongoconnection,imeiNo,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public AddDeviceDropDownInfo GetAddDeviceDropDownInfo(int userLoginId) {
			AddDeviceDropDownInfo  dtp=new AddDeviceDropDownInfo();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetAddDeviceDropDownInfo(con,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject AddNewDevice(int StudentID, int ParentId, String FirstName, String LastName, String Gender, 
				String DeviceID, String Type, int DeviceType, String DeviceSimNumber, String ActivationDate, int PlanTypeID, 
				int PaymentMode, String CreditName, String PaymentDate, String TransactionID, Double PayAmount, int registerOutParameter,String DeviceSimIMEINumber) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.AddNewDevice(mongoconnection,con,StudentID,ParentId,FirstName,LastName,Gender,DeviceID,Type,DeviceType,DeviceSimNumber,
						ActivationDate,PlanTypeID,PaymentMode,CreditName,PaymentDate,TransactionID,PayAmount,registerOutParameter,DeviceSimIMEINumber);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public MessageObject AddBulkDevices(String ActivationDate, int DeviceType,Double PayAmount,
				String PaymentDate,int PaymentMode, int PlanTypeID,String TransactionID, String Type,
				String BulkData,String CreditName,int ParentId,String DeviceSimIMEINumber) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.AddBulkDevices(mongoconnection,con,ActivationDate,DeviceType,PayAmount,PaymentDate,
						PaymentMode,PlanTypeID,TransactionID,Type,BulkData,CreditName,ParentId,DeviceSimIMEINumber);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject AddPatrolmanBeatBulk(int parentId,int userLoginId, String name, String contactNo,
				int seasonId,String emailId, String patrolmenFormArray) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.AddPatrolmanBeatBulk(mongoconnection,con,parentId,userLoginId,name,contactNo,seasonId,emailId,patrolmenFormArray);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<PatrolManBeatDTO> GetPatrolMenBeatAPI(int StudentId,int SeasonId) {
			ArrayList<PatrolManBeatDTO>  dtp=new ArrayList<PatrolManBeatDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetPatrolMenBeatAPI(con,StudentId,SeasonId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public ArrayList<PatrolManBeatDTO> GetPatrolmanExistingBeatByParent(int parentId, int seasonId) { 
			ArrayList<PatrolManBeatDTO>  dtp=new ArrayList<PatrolManBeatDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetPatrolMenExistingBeatByParent(con,parentId,seasonId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public MessageObject UpdateRailwayPatrolmanBeatPathCopyApprove(int beatId, int userloginId) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.UpdateRailwayPatrolmanBeatPathCopyApprove(con,beatId,userloginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<RailwayKeymanDTO> GetKeymanExistingBeatToApproveByParent(
				int parentId) {
			ArrayList<RailwayKeymanDTO>  dtp=new ArrayList<RailwayKeymanDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetKeymanExistingBeatToApproveByParent(con,parentId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<PatrolManBeatDTO> GetPatrolMenExistingBeatByParent(int parentId, int seasonId) { 
			ArrayList<PatrolManBeatDTO>  dtp=new ArrayList<PatrolManBeatDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetPatrolMenExistingBeatByParent(con,parentId,seasonId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}	
		public ArrayList<PatrolManBeatDTO> GetPatrolmanExistingBeatToApproveByParent(int parentId, int seasonId) { 
			ArrayList<PatrolManBeatDTO>  dtp=new ArrayList<PatrolManBeatDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetPatrolmanExistingBeatToApproveByParent(con,parentId,seasonId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public MessageObject GetPendingDeviceCommandHistoryByDevice(String deviceId) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetPendingDeviceCommandHistoryByDevice(mongoconnection,deviceId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				

			}
			return dtp;
	}
		public ArrayList<ReportDataAdminDTO> GetReportAdminAPI(int parentId, int day) {
			ArrayList<ReportDataAdminDTO>  dtp=new ArrayList<ReportDataAdminDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetReportAdminAPI(con,parentId,day);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public MessageObject DeviceRegisterAPI(String imeiNo) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.DeviceRegisterAPI(mongoconnection,con,imeiNo);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public MessageObject SaveDeviceInspectionReportAPI(int studentId,String issueTitle, String issueDescription,String finalTestingReport,
				String inspectdBy,String contactPerson,String inspectionDate,int userLoginId, int inspectionId, Boolean isReusable) {
			MessageObject  dtp=new MessageObject();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.SaveDeviceInspectionReportAPI(con,studentId,issueTitle,issueDescription,finalTestingReport,
						inspectdBy,contactPerson,inspectionDate,userLoginId,inspectionId,isReusable);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		
		public ArrayList<DeviceInspectionDTO> GetDeviceInspectionReportAPI(int userLoginId) {
			ArrayList<DeviceInspectionDTO>  dtp=new ArrayList<DeviceInspectionDTO>();
			try { 
				AdminServiceDao dao=new AdminServiceDao();
				dtp=dao.GetDeviceInspectionReportAPI(con,userLoginId);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (mongo_instance!=null) 
						mongo_instance.close();
					if (con!=null) 
						con.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return dtp;
		}
		public ArrayList<UserUtilityDTO> GetUserUtilityAPI(int userid,int roleid) {
			ArrayList<UserUtilityDTO> dtp=new ArrayList<UserUtilityDTO>();
			try { 
				ParentDao dao=new ParentDao();
				dtp=dao.GetUserUtilityAPI(con,userid,roleid);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dtp;
		}
		
}