package webservices;

import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.APIController;
import Utility.JavaHashGenerator;
import Utility.SendEmail;

import com.google.gson.Gson;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.paytm.pg.merchant.CheckSumServiceHelper;
import com.sun.jersey.api.representation.Form;

import dao.Common;
import dao.LoginDAO;
import dto.ApplicationMetaData;
import dto.AssociatedParentDTO;
import dto.DateRangeExceptionReportDTO;
import dto.DeviceSimNo;
import dto.DeviceStatusDTO;
import dto.DeviceStatusInfoDto;
import dto.DevicelistDetails;
import dto.ExceptionDeviceDTO;
import dto.ExceptionReportFileDTO;
import dto.FamilyPaymentTypeDTO;
import dto.FeatureNearbyDTO;
import dto.HistoryInfoDTO;
import dto.RailDeviceInfoDto;
import dto.RailWayAddressDTO;
import dto.FrdlistDTO;
import dto.GeofenceDTO;
import dto.HistoryDTO;
import dto.LocationDTO;
import dto.MessageObject;
import dto.MilageDTO;
import dto.PaytmCheckStatusDTO;
import dto.PersonDetails;
import dto.RailwayKeymanDTO;
import dto.SmsNotificationDTO;
import dto.SosDto;
import dto.SosInfoDTO;
import dto.TripInfoDto;
import dto.VTSDataDTO;
import dto.VehicalTrackingSMSCmdDTO;
import dto.WrongLocationDataDTO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

@Path("UserServiceAPI")
public class TrackService<DeviceBatteryInfo> {

	private double HistoryDistance=0;
	private int j=0;
	private int totalCount=0;
	@POST
	@Path("GetRequestedList")
	@Produces("application/json")
	public String GetRequestedList(@FormParam("userid") String username)
	{

		String feeds  = null;
		try 
		{
			ArrayList<PersonDetails> msgData = new ArrayList<PersonDetails>();
			APIController handler= new APIController();
			msgData = handler.GetRequestedList(username);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		/*	APIController.SaveApiReport("TrackMyFriend", "Get All friend for track",  "GetRequestedList",username,
					 "Getting list of friend for tracking",  feeds,  "Java","null", "null");*/
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		System.out.println("GetRequestedList-----"+feeds);

		return feeds;
	}



	@POST
	@Path("GetTrackInfo")
	@Produces("application/json")
	public String GetTrackInfo(@FormParam("InvitedId") String invited_id)
	{
		String feeds  = null;
		try 
		{
			System.err.print("--");
			LocationDTO msgData = new LocationDTO();
			APIController handler= new APIController();
			msgData = handler.GetTrackInfo(invited_id);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		//	System.out.println("location data"+feeds);
			//APIController.SaveApiReport("TrackMyFriend", "Get Location of Selected",  "GetTrackInfo",invited_id,
			//		 "Get Lat and long of perticulat person",  feeds,  "Java","null", "null");
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	@POST
	@Path("Getlocation")
	@Produces("application/json")
	public String Getlocation(@FormParam("UserId") String Invi_id,@FormParam("UserId") String user_id)
	{
		String feeds  = null;
		try 
		{
			LocationDTO msgData = new LocationDTO();
			APIController handler= new APIController();
			msgData = handler.Getlocation(Invi_id,user_id);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		
			//APIController.SaveApiReport("TrackMyFriend", "Get Location of Selected",  "GetTrackInfo",Invi_id,
			//		 "Get Lat and long of perticulat person",  feeds,  "Java","null", "null");
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	
	@POST
	@Path("PushLocation")
	@Produces("application/json")
	public String PushLocation(@FormParam("id") String id,@FormParam("Lat") String lat,@FormParam("Lang") String lan,@FormParam("Time") String time)
	{
		String feeds  = null;
		try 
		{

			System.err.print("--");
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.PushLocation(id,lat,lan,time);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//System.out.println(feeds);
			
			
			//APIController.SaveApiReport("TrackMyFriend", "post location on server",  "PushLocation",id,
				//	 "post device location on server for tracking info",  feeds,  "Java","null", "null");
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
/*	@POST
	@Path("/PushLocation")
	@Produces("application/json")
	public String Search(@FormParam("id") String id,@FormParam("lat") String lat,@FormParam("lang") String Lan,@FormParam("time") String time)
	{
		String feeds  = null;
		try 
		{
			System.err.print("--");
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.PushLocation(id,lat,Lan,time);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//System.out.println(feeds);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}*/
	
	

	@POST
	@Path("/SearchFriendList")
	@Produces("application/json")
	public String SearchFriendList(@FormParam("UserId") Integer id,@FormParam("Entity") String entity,@FormParam("Flag") Integer flag)
	{
		String feeds  = null;
		try 
		{
			ArrayList<FrdlistDTO> msgData = new ArrayList<FrdlistDTO>();
			APIController handler= new APIController();
			msgData = handler.SearchFriendList(id,entity,flag);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

			//APIController.SaveApiReport("TrackMyFriend", "Search friend",  "SearchFriendList",id+"",
			//		 "Search friend for addind in to our tracklist",  feeds,  "Java","null", "null");
			} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}

	
	@POST
	@Path("/SendInvitation")
	@Produces("application/json")
	public String SendInvitation(@FormParam("SenderId")Integer SendId,@FormParam("GettingId") Integer GetId)
	{
		String feeds  = null;
		try 
		{
			System.err.print("--");
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.SendInvitation(SendId,GetId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//APIController.SaveApiReport("TrackMyFriend", "SendInvitation",  "SendInvitation",SendId+"",
				//	 "send invitation for tracking",  feeds,  "Java","null", "null");		} catch (Exception e)
		}catch (Exception e2) {
			e2.printStackTrace();
			System.out.println(e2.getMessage());
		}

		return feeds;
	}
	@POST
	@Path("GetInvitationList")
	@Produces("application/json")
	public String GetInvitationList(@FormParam("userid") String id)
	{
		String feeds  = null;
		try 
		{
			ArrayList<PersonDetails> msgData = new ArrayList<PersonDetails>();
			APIController handler= new APIController();
			msgData = handler.GetInvitationList(id);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//APIController.SaveApiReport("TrackMyFriend", "GetInvitationList",  "GetInvitationList",id,
			//		 "Get pending Invitation for accept or denay",  feeds,  "Java","null", "null");
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("AcceptInvitation")
	@Produces("application/json")
	public String AcceptInvitation(@FormParam("Invited_PId") String Invi_id)
	{
		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.AcceptInvitation(Invi_id);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//APIController.SaveApiReport("TrackMyFriend", "AcceptInvitation",  "AcceptInvitation",Invi_id,
			//		 "Accept Invitation to allow track. ",  feeds,  "Java","null", "null");
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("RejectInvitation")
	@Produces("application/json")
	public String RejectInvitation(@FormParam("Invited_PId") String Invi_id)
	{
		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.RejectInvitation(Invi_id);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//APIController.SaveApiReport("TrackMyFriend", "RejectInvitation",  "RejectInvitation",Invi_id,
				//	 " Reject Invitation to allow track. ",  feeds,  "Java","null", "null");
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("GetAcceptedtrackperson")
	@Produces("application/json")
	public String GetAcceptedtrackperson(@FormParam("UserId") String userid)
	{
		String feeds  = null;
		try 
		{
			ArrayList<PersonDetails> msgData = new ArrayList<PersonDetails>();
			APIController handler= new APIController();
			msgData = handler.GetAcceptedtrackperson(userid);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

			//APIController.SaveApiReport("TrackMyFriend", "GetAcceptedtrackperson",  "GetAcceptedtrackperson",userid,
				//	 "Get  Acceptedtrackperson to block them.(Disallow to track)",  feeds,  "Java","null", "null");
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("BlockTrakRequest")
	@Produces("application/json")
	public String BlockTrakRequest(@FormParam("Invited_Id") String Invi_id)
	{
		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.BlockTrakRequest(Invi_id);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

			//APIController.SaveApiReport("TrackMyFriend", "BlockTrakRequest",  "BlockTrakRequest",Invi_id,
					// "Block TrakRequest of perticulat person",  feeds,  "Java","null", "null");
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	

	@POST
	@Path("PostLocation_Report")
	@Produces("application/json")
	public String PostLocation_Report(@FormParam("TrackpersonId") String Invi_id,@FormParam("UserId") String user_id,
			@FormParam("Lat") String lat,@FormParam("Lang") String lang)
	{
		String feeds  = null;
		try 
		{
			LocationDTO msgData = new LocationDTO();
			APIController handler= new APIController();
			handler.PostLocation_Report(Invi_id,user_id,lat,lang);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

			//APIController.SaveApiReport("TrackMyFriend", "PostLocation_Report",  "PostLocation_Report",user_id,
				//	"Post Location_Report of traking person ",  feeds,  "Java","null", "null");
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	@POST
	@Path("GetDeviceSimNo")
	@Produces("application/json")
	public String GetDeviceSimNo(@FormParam("StudentID") String studentid,
			@FormParam("ComandType") String command)
	{
		String feeds  = null;
		try 
		{
			DeviceSimNo msgData = new DeviceSimNo();
			APIController handler= new APIController();
			msgData=handler.GetDeviceSimNo(studentid,command);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

			//APIController.SaveApiReport("Track my child", "GetDeviceSimNo",  "GetDeviceSimNo",studentid,
			//		"Get Device SimNo",  feeds,  "Java","null", "null");
			////System.out.println(feeds);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	@POST
	@Path("PostDeviceSimno")
	@Produces("application/json")
	public String PostDeviceSimno(@FormParam("StudentID") String studentid,
			@FormParam("Simno") String simno)
	{
		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData=handler.PostDeviceSimno(studentid,simno);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

			//APIController.SaveApiReport("Track my child", "Change device sim number",  "PostDeviceSimno",studentid,
			//		"Change device sim number",  feeds,  "Java","null", "null");
			//System.out.println(feeds);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("GetCommandtoTrack")
	@Produces("application/json")
	public String GetCommandtoTrack(@FormParam("StudentID") String studentid,
			@FormParam("ComandType") String command)
	{
		String feeds  = null;
		try 
		{
			DeviceSimNo msgData = new DeviceSimNo();
			APIController handler= new APIController();
			msgData=handler.GetCommandtoTrack(studentid,command);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

			//APIController.SaveApiReport("Track my child", "GetCommandtoTrack",  "GetCommandtoTrack",studentid,
			//		"Get Device Get CommandtoTrack",  feeds,  "Java","null", "null");
			//System.out.println(feeds);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("GetSosList")
	@Produces("application/json")
	public String GetSosList(@FormParam("UserId") String userid
			)
	{
		String feeds  = null;
		try 
		{
			ArrayList<SosDto> msgData = new ArrayList<SosDto>();
			APIController handler= new APIController();
			msgData=handler.GetSosList(userid);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//System.out.println(feeds);
			//APIController.SaveApiReport("SOS", "Get Sos List",  "GetSosList",userid,
			//		"Get Get SosList",  feeds,  "Java","null", "null");
			//System.out.println(feeds);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	
	@POST
	@Path("SaveSOS_onServer")
	@Produces("application/json")
	public String SaveSOS_onServer(@FormParam("UserId") String userid,@FormParam("Name") String name,@FormParam("Number") String number
			)
	{
		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData=handler.SaveSOS_onServer(userid,name,number);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//System.out.println(feeds);
			//APIController.SaveApiReport("SOS", "Get Sos List",  "GetSosList",userid,
			//		"Get Get SosList",  feeds,  "Java","null", "null");
			//System.out.println(feeds);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	@POST
	@Path("DeleteSOs")
	@Produces("application/json")
	public String DeleteSOs(@FormParam("SosId") String sosid,@FormParam("UserId") String userid)
	{
		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData=handler.DeleteSOs(sosid,userid);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//System.out.println(feeds);
			//APIController.SaveApiReport("SOS", " Sos DeleteSOs",  "DeleteSOs",userid,
				//	"Get  Delete SOS",  feeds,  "Java","null", "null");
			//System.out.println(feeds);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	@POST
	@Path("GetstudentVTSStatus")
	@Produces("application/json")
	public String GetstudentVTSStatus(@FormParam("StudentID") String studId)
	{
		String feeds  = null;
		try 
		{
			VTSDataDTO msgData = new VTSDataDTO();
			APIController handler= new APIController();
			msgData=handler.GetstudentVTSStatus(studId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//System.out.println(feeds);
			//APIController.SaveApiReport("GetstudentVTSStatus", " GetstudentVTSStatus",  "GetstudentVTSStatus",studId,
				///	"Get  student VTS Status and data",  feeds,  "Java","null", "null");
			//System.out.println(feeds);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("PostEnginePin")
	@Produces("application/json")
	public String PostEnginePin(@FormParam("StudentID") String studId,@FormParam("Pin") String pin)
	{
		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData=handler.PostEnginePin(studId,pin);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//System.out.println(feeds);
			//APIController.SaveApiReport("PostEnginePin", " PostEnginePin",  "PostEnginePin",studId,
				//	"post Post EnginePin",  feeds,  "Java","null", "null");
			//System.out.println(feeds);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	
	@POST
	@Path("GetSMSItemlist")
	@Produces("application/json")
	public String GetSMSItemlist(@FormParam("StudentID") String studId)
	{
		String feeds  = null;
		try 
		{
			ArrayList<VehicalTrackingSMSCmdDTO> msgData = new ArrayList<VehicalTrackingSMSCmdDTO>();
			APIController handler= new APIController();
			msgData=handler.GetSMSItemlist(studId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//System.out.println(feeds);
			//APIController.SaveApiReport("GetSMSItemlist", " GetSMSItemlist",  "GetSMSItemlist",studId,
				//	"GetSMSItemlist  ",  feeds,  "Java","null", "null");
			//System.out.println(feeds);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("GetSMSDeviceSimNo")
	@Produces("application/json")
	public String GetSMSDeviceSimNo(@FormParam("StudentID") String studentid)
	{
		String feeds  = null;
		try 
		{
			DeviceSimNo msgData = new DeviceSimNo();
			APIController handler= new APIController();
			msgData=handler.GetSMSDeviceSimNo(studentid);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

			//APIController.SaveApiReport("Track my child", "GetDeviceSimNo",  "GetDeviceSimNo",studentid,
				//	"Get Device SimNo",  feeds,  "Java","null", "null");
			//System.out.println(feeds);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("GetVehReportlistCheck")
	@Produces("application/json")
	public String GetVehReportlist(@FormParam("StudentID") String studId)
	{
		System.out.println("GetVehReportlist======"+studId);

		String feeds  = null;
		try 
		{
			ArrayList<VehicalTrackingSMSCmdDTO> msgData = new ArrayList<VehicalTrackingSMSCmdDTO>();
			APIController handler= new APIController();
			msgData=handler.GetVehReportlist(studId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//System.out.println(feeds);
			//APIController.SaveApiReport("GetSMSItemlist", " GetSMSItemlist",  "GetSMSItemlist",studId,
				///	"GetSMSItemlist  ",  feeds,  "Java","null", "null");
			//System.out.println(feeds);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("PostNotificationData_Server")
	@Produces("application/json")
	public String PostNotificationData_Server(@FormParam("NTitle") String NTitle,@FormParam("NType") String NType,@FormParam("LatDir") String LatDir
			,@FormParam("LanDir") String LanDir,@FormParam("Lat") String Lat,@FormParam("Lang") String Lang,
			@FormParam("ImieNo") String ImieNo,@FormParam("UserId") String UserId)
	{
        SmsNotificationDTO smsdata=new SmsNotificationDTO();

        smsdata.setNotify_Title(NTitle);
        smsdata.setNotify_Type(NType);
        smsdata.setLatDir(LatDir);
        smsdata.setLangDir(LanDir);
        smsdata.setLat(Lat);
        smsdata.setLang(Lang);
        smsdata.setImeiNo(ImieNo);
        smsdata.setUserId(UserId);
		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData=handler.PostNotificationData_Server(smsdata);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//System.out.println(feeds);
			//System.out.println(feeds);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
   
	
	@POST
	@Path("GetVehTotalKm")
	@Produces("application/json")
	public String GetVehTotalKm(@FormParam("DeviceImieNo") String device,@FormParam("StartDateTime") String startdate
			,@FormParam("EndDateTime") String enddate
			)
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.GetMilage(device,startdate,enddate);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			System.out.println("Your Total distance fror device IMEI = "+device+" is " +feeds+" Km");
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("GetDailyMilageReport")
	@Produces("application/json")
	public String GetDailyMilageReport(@FormParam("DeviceImieNo") String device,@FormParam("StartDateTime") String startdate
			,@FormParam("EndDateTime") String enddate
			)
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.GetMilage(device,startdate,enddate);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			System.out.println("Your Total distance fror device IMEI = "+device+" is " +feeds+" Km");
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	@POST
	@Path("GetVehMonthlyTotalKm")
	@Produces("application/json")
	public String GetVehMonthlyTotalKm(@FormParam("DeviceImieNo") String device,@FormParam("StartDateTime") String startdate
			,@FormParam("EndDateTime") String enddate,@FormParam("MailId") String mailid,@FormParam("DeviceName") String devicename
			)
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.GetMilage(device,startdate,enddate);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			System.out.println("Your Total distance fror device IMEI = "+device+" is " +feeds+" Km");
			SendEmail mail=new SendEmail();
			mail.sendMonthlyEmail(msgData.getMessage(),mailid,devicename,startdate,enddate);
		
			
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("GetMonthalyMilageReport")
	@Produces("application/json")
	public String GetMonthalyMilageReport(@FormParam("DeviceImieNo") String device,@FormParam("StartDateTime") String startdate
			,@FormParam("EndDateTime") String enddate,@FormParam("DeviceName") String devicename
			)
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.GetMilage(device,startdate,enddate);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			System.out.println("Your Total distance fror device IMEI = "+device+" is " +feeds+" Km");
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}

	@POST
	@Path("GenerateTripReport")
	@Produces("application/json")
	public String GenerateTripReport()
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.GenerateTripReport();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}

	@POST
	@Path("GenerateTripReportIdCard")
	@Produces("application/json")
	public String GenerateTripReportIdCard()
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.GenerateTripReportIdCard();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	@POST
	@Path("GenerateTripReport_USDevice")
	@Produces("application/json")
	public String GenerateTripReport_USDevice()
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.GenerateTripReport_USDevice();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}

	@POST
	@Path("GetTripReport")
	@Produces("application/json")
	public String GetTripReport(@FormParam("DeviceImieNo") String device,@FormParam("StartDateTime") String startdate
			,@FormParam("EndDateTime") String enddate,@FormParam("Email") String email)
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.GetTripReport(device,startdate,enddate,email);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("GetEmpTripReport")
	@Produces("application/json")
	public String GetEmpTripReport(@FormParam("DeviceImieNo") String device,@FormParam("StartDateTime") String startdate
			,@FormParam("EndDateTime") String enddate,@FormParam("Email") String email)
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.GetEmpTripReport(device,startdate,enddate,email);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("GetDirectTripReport")
	@Produces("application/json")
	public String GetDirectTripReport(@FormParam("DeviceImieNo") String device,@FormParam("StartDateTime") String startdate
			,@FormParam("EndDateTime") String enddate)
	{
		String feeds  = null;
		try 
		{
			j=0;
			ArrayList<TripInfoDto> msgData = new ArrayList<TripInfoDto>();

			APIController handler= new APIController();
			msgData=handler.GetDirectTripReport(device,startdate,enddate);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		System.out.println("Trip report-------"+feeds);

		return feeds;
	}
	@POST
	@Path("GetDirectWebTripReport")
	@Produces("application/json")
	public String GetDirectWebTripReport(@FormParam("DeviceImieNo") String device,@FormParam("StartDateTime") String startdate
			,@FormParam("EndDateTime") String enddate)
	{
		String feeds  = null;
		try 
		{
			j=0;
			String msgData = "";

			APIController handler= new APIController();
			msgData=handler.GetDirectWebTripReport(device,startdate,enddate);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println(feeds);

		return feeds;
	}
	
	@POST
	@Path("GetTripReportOnMail")
	@Produces("application/json")
	public String GetTripReportOnMail(@FormParam("DeviceImieNo") String device,@FormParam("StartDateTime") String startdate
			,@FormParam("EndDateTime") String enddate,@FormParam("Email") String email)
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.GetTripReportOnMail(device,startdate,enddate,email);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("GetVehTotalKm1")
	@Produces("application/json")
	public String GetVehTotalKm1(@FormParam("DeviceImieNo") String device,@FormParam("StartDateTime") String startdate
			,@FormParam("EndDateTime") String enddate
			)
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.GetMilage1(device,startdate,enddate);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			System.out.println("Your Total distance fror device IMEI = "+device+" is " +feeds+" Km");
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
	@POST
	@Path("GetVehMonthlyTotalKm1")
	@Produces("application/json")
	public String GetVehMonthlyTotalKm1(@FormParam("DeviceImieNo") String device,@FormParam("StartDateTime") String startdate
			,@FormParam("EndDateTime") String enddate,@FormParam("MailId") String mailid,@FormParam("DeviceName") String devicename
			)
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.GetMilage1(device,startdate,enddate);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			System.out.println("Your Total distance fror device IMEI = "+device+" is " +feeds+" Km");
			SendEmail mail=new SendEmail();
			mail.sendMonthlyEmail(msgData.getMessage(),mailid,devicename,startdate,enddate);
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("GetMilage")
	@Produces("application/json")
	public String GetMilage(@FormParam("DeviceImei") String device,@FormParam("StartDate") String startdate
			,@FormParam("EndDate") String enddate
			)
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.GetMilage(device,startdate,enddate);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			System.out.println("Your Total distance fror device IMEI = "+device+" is " +feeds+" Km");			
		
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return "Your Total distance fror device IMEI = "+device+" is " +feeds+" Km";
	}
	
	@POST
	@Path("send_mail")
	@Produces("application/json")
	public String send_mail(@FormParam("send_mail") String send_mail,@FormParam("name") String name
			,@FormParam("username") String username,@FormParam("email") String email,@FormParam("password") String password)
	{
		String feeds  = null;
		try 
		{
			DigestUtils.md5Hex("ruPESH");


		//	System.out.println(send_mail+" "+name+" "+username+" "+email+" "+password+" "+DigestUtils.md5Hex("ruPESH"));
			SendEmail mail=new SendEmail();
			mail.sendSignupEmail(name,username,email,password);

			APIController handler= new APIController();
			MessageObject msgData = new MessageObject();
			msgData.setError("false");
			msgData.setMessage("You are registered succesfully.Please check your Mail-Id for Username and Password!");
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("SendForgetPassword")
	@Produces("application/json")
	public String SendForgrtPassword(@FormParam("Email_Id") String email)
			
	{
		
		String feeds  = null;
		try 
		{

			MessageObject msgData = null;
			APIController handler= new APIController();
			msgData = handler.SendForgrtPassword(email);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	@POST
	@Path("Get_DeviceStatus")
	@Produces("application/json")
	public String Get_DeviceStatus(@FormParam("Devicelist") JSONArray devielist)
	{
		String feeds  = null;
		try 
		{
			System.out.println("=========Drvicelist="+devielist);
		
			ArrayList<DeviceStatusDTO> msgData = new ArrayList<>();
			APIController handler= new APIController();
			msgData = handler.Get_DeviceStatus(devielist);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	

	@POST
	@Path("UpdateAllowFlag")
	@Produces("application/json")
	public String UpdateAllowFlag(@FormParam("VtsSmsAllow") String VtsSmsAllow,@FormParam("ACCReportAllow") String ACCReportAllow,
			@FormParam("AccEOD") String AccEOD,@FormParam("ACCSqliteEnable") String ACCSqliteEnable, @FormParam("Email_Id") String email)

	{
		
		String feeds  = null;
		try 
		{

			MessageObject msgData = null;
			APIController handler= new APIController();
			msgData = handler.UpdateAllowFlag(VtsSmsAllow,ACCReportAllow,AccEOD,ACCSqliteEnable,email);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	@POST
	@Path("UpdateAllowRepotFlag")
	@Produces("application/json")
	public String UpdateAllowRepotFlag(@FormParam("AllowFlag") String AllowFlag,@FormParam("Email_Id") String email
			)

	{
		
		String feeds  = null;
		try 
		{

			MessageObject msgData = null;
			APIController handler= new APIController();
			msgData = handler.UpdateAllowRepotFlag(AllowFlag,email);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	    @POST
	@Path("postGuestUserRequest")
	@Produces("application/json")
	public String postGuestUserRequest(@FormParam("name") String name,
			@FormParam("email") String email,@FormParam("contact") String contact,
			@FormParam("message") String msg,@FormParam("registrationType") String type,
			@FormParam("City") String city)
	{
		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData=handler.postGuestUserRequest(name,email,contact,msg,type,city);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

			SendEmail mail=new SendEmail();
			mail.sendGuestUserEmail(name,email,contact,msg,type,city);
			//System.out.println(feeds);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
   @POST
	@Path("JavaHashGeneratorAndroidAPI")
	@Produces("application/json")
	public String JavaHashGeneratorAndroidAPI(@FormParam("data") String jsonobj)
	{
    	
    	
    	/* {key=dRQuiA, merchantId=4928174, txnid=0nf71489561316502, amount=10.0, 
    	SURL=https://test.payumoney.com/mobileapp/payumoney/success.php, 
    		FURL=https://test.payumoney.com/mobileapp/payumoney/failure.php, 
    			productInfo=product_name, email=piyush.jain@payu.in,
    			firstName=piyush, phone=8882434664, udf1=, udf2=, udf3=, udf4=, udf5=}*/
    	
    	Gson gson = new Gson();
    	String jsonobj1 = gson.toJson(jsonobj);
		System.out.println("JavaHashGenerator======"+jsonobj1);
		
    	String feeds  = null;

    	try {
    		
    		System.out.println("JavaHashGenerator======"+jsonobj);

			JSONObject jo=new JSONObject(jsonobj);
			MessageObject msgData = new MessageObject();
			JavaHashGenerator jhash=new JavaHashGenerator();
			feeds=jhash.getHashes(jo.getString("txnid"), jo.getString("amount"),jo.getString("productInfo"),jo.getString("firstname"),jo.getString("email"),jo.getString("user_credentials"),
					jo.getString("udf1"),jo.getString("udf2"),jo.getString("udf3"),jo.getString("udf4"),jo.getString("udf5"),jo.getString("offerKey"),jo.getString("cardBin"));
					

		
			//System.out.println(feeds);
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	

		return feeds;
	}
    
   
    //FOr IOS Generate Hash key for Payumoney 
   
    @POST
  	@Path("JavaHashGeneratorAPI")
  	@Produces("application/json")
  	public String JavaHashGeneratorAPI(@FormParam("key") String key,@FormParam("amount") String amount,@FormParam("txnid") String txnid,
  			@FormParam("productinfo") String productinfo,@FormParam("email") String email,@FormParam("firstname") String firstname)
  	{
      	String feeds  = null;

      	try {
      		System.err.println(key+" "+amount+" "+txnid+" "+productinfo+" "+email+" "+firstname);
      		
  			MessageObject msgData = new MessageObject();
  			JavaHashGenerator jhash=new JavaHashGenerator();
  			feeds=jhash.getHashes(key,amount,txnid,productinfo,email,firstname);
  			//System.out.println(feeds);
  			
  		} catch (Exception e1) {
  			// TODO Auto-generated catch block
  			e1.printStackTrace();
  		}
      	

  		return feeds;
  	}
	
    @POST
	@Path("DropLocation")
	@Produces("application/json")
	public String DropLocation()
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.DropLocation();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
	
    @POST
  	@Path("DropTripReport")
  	@Produces("application/json")
  	public String DropTripReport()
  	{
  		String feeds  = null;
  		try 
  		{
  			j=0;
  			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
  			MessageObject msgData =new MessageObject();

  			APIController handler= new APIController();
  			msgData=handler.DropTripReport();
  			Gson gson = new Gson();
  			feeds = gson.toJson(msgData);
  			
  		
  			
  		} catch (Exception e)
  		{
  			e.printStackTrace();
  			//System.out.println(e.getMessage());
  		}

  		return feeds;
  	}
  	
    
    @POST
  	@Path("GeneratePendingTripReport")
  	@Produces("application/json")
  	public String GeneratePendingTripReport(@FormParam("StartDateTime") String startdate
			,@FormParam("EndDateTime") String enddate)
  	{
  		String feeds  = null;
  		try 
  		{
  			j=0;
  			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
  			MessageObject msgData =new MessageObject();

  			APIController handler= new APIController();
  			msgData=handler.GeneratePendingTripReport(startdate,enddate);
  			Gson gson = new Gson();
  			feeds = gson.toJson(msgData);
  			
  		
  			
  		} catch (Exception e)
  		{
  			e.printStackTrace();
  			//System.out.println(e.getMessage());
  		}

  		return feeds;
  	}
 
    @POST
	@Path("GeneratePendingTripReport_USDevice")
	@Produces("application/json")
	public String GeneratePendingTripReport_USDevice(@FormParam("StartDateTime") String startdate
			,@FormParam("EndDateTime") String enddate)
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.GeneratePendingTripReport_USDevice(startdate,enddate);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}

    @POST
	@Path("NotifyToUserAboutExpiryDate")
	@Produces("application/json")
	public String NotifyToUserAboutExpiryDate()
	{
		String feeds  = null;
		try 
		{
			j=0;
			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.NotifyToUserAboutExpiryDate();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
	
    @POST
	@Path("GetFamilyPaymentType")
	@Produces("application/json")
	public String GetFamilyPaymentType()
	{
		String feeds  = null;
		try 
		{
			j=0;
			ArrayList<FamilyPaymentTypeDTO> msgData = new ArrayList<FamilyPaymentTypeDTO>();

			APIController handler= new APIController();
			msgData=handler.GetFamilyPaymentType();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
    
    
    @POST
	@Path("UpdateFamilyPayment")
	@Produces("application/json")
	public String UpdateFamilyPayment(@FormParam("InvitedIds") String InvitedIds,@FormParam("UserId") String UserId
			,@FormParam("typeofpayment") String typeofpayment,@FormParam("paymentId") String paymentId)
	{
		String feeds  = null;
		try 
		{

			
			j=0;
			MessageObject msgData =new MessageObject();

			APIController handler= new APIController();
			msgData=handler.UpdateFamilyPayment(InvitedIds,UserId,typeofpayment,paymentId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
    
    
    @POST
  	@Path("GetApplicationMetaData")
  	@Produces("application/json")
  	public String GetApplicationMetaData()
    {
  		String feeds  = null;
  		try 
  		{
  			
  			
  			j=0;
  			ApplicationMetaData msgData =new ApplicationMetaData();

  			APIController handler= new APIController();
  			msgData=handler.GetApplicationMetaData();
  			Gson gson = new Gson();
  			feeds = gson.toJson(msgData);
  			
  		
  			
  		} catch (Exception e)
  		{
  			e.printStackTrace();
  			//System.out.println(e.getMessage());
  		}

  		return feeds;
  	}
      
      
    @POST
  	@Path("GetPaytm_ChecksumGeneration")
  	@Produces("application/json")
  	public String GetReferralCode(@FormParam("OrderId") String orderId,@FormParam("UserId") String custId
			,@FormParam("Amount") String amount,@FormParam("MobNo") String mobno,@FormParam("Email") String email)
	{
  		String feeds  = null;
  		try 
  		{ 
  		 
  			
  			j=0;
			MessageObject msgData =new MessageObject();

  			APIController handler= new APIController();
  			msgData=handler.GetPaytm_ChecksumGeneration(orderId,custId,amount,mobno,email);
  			Gson gson = new Gson();
  			feeds = gson.toJson(msgData);
  			
  		
  			
  		} catch (Exception e)
  		{
  			e.printStackTrace();
  			//System.out.println(e.getMessage());
  		}

  		return feeds;
  	}
    
    @POST
	@Path("GetMyKiddySMSItemlist")
	@Produces("application/json")
	public String GetMyKiddySMSItemlist(@FormParam("StudentID") String studId)
	{
		String feeds  = null;
		try 
		{
			ArrayList<VehicalTrackingSMSCmdDTO> msgData = new ArrayList<VehicalTrackingSMSCmdDTO>();
			APIController handler= new APIController();
			msgData=handler.GetMyKiddySMSItemlist(studId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//System.out.println(feeds);
			//APIController.SaveApiReport("GetMyKiddySMSItemlist", " GetMyKiddySMSItemlist",  "GetMyKiddySMSItemlist",studId,
			//		"GetMyKiddySMSItemlist  ",  feeds,  "Java","null", "null");
			//System.out.println(feeds);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}

		return feeds;
	}
    
    @POST
	@Path("GetHistorydata")
	@Produces("application/json")
	public String GetHistorydata(@FormParam("StudentId") String student_id,@FormParam("StartDateTime") String startdate)
	{
		String feeds  = null;
		try 
		{
			System.err.println("Student----------------"+student_id);
			ArrayList<HistoryDTO> msgData = new ArrayList<HistoryDTO>();
			APIController handler= new APIController();
			msgData = handler.GetHistorydata(student_id,startdate);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			//APIController.SaveApiReport("GetHistorydata", "GetHistorydata",  "GetHistorydata","0",
					// "Get GetHistorydatanay",  feeds,  "Java","null", "null");
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		System.out.println("History----"+feeds);

		return feeds;
	}
	
    
    @POST
  	@Path("CheckPaytmTractionStatus")
  	@Produces("application/json")
  	public String CheckPaytmTractionStatus(@FormParam("OrderId") String orderId)
  	{
  		String feeds  = null;
  		try 
  		{
  			ArrayList<HistoryDTO> msgData = new ArrayList<HistoryDTO>();
  			APIController handler= new APIController();
  			
  			Gson gson = new Gson();
  		/*	jo.put("MID", Common.MercahntKey);
  			jo.put("ORDERID", );
  			jo.put("CHECKSUMHASH", "--");*/
  			
  			
  			
  			
  				TreeMap<String,String> paramMap = new TreeMap<String,String>();
		
			
			paramMap.put("MID" ,Common.MID);
			paramMap.put("ORDER_ID" , orderId);
			
	/*		paramMap.put("MID" ,"klbGlV59135347348753");
			paramMap.put("ORDERID" ,"ORDER48886809916");*/
			
			
		String checkSum =  CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(Common.MercahntKey, paramMap);

		PaytmCheckStatusDTO pay=new PaytmCheckStatusDTO();
  			
  			pay.setMID(Common.MID);
  			pay.setORDERID(orderId);
  			pay.setCHECKSUMHASH(checkSum);

  			
  			try {
				URL url = new URL("https://pguat.paytm.com/oltp/HANDLER_INTERNAL/getTxnStatus?JsonData="+URLEncoder.encode(gson.toJson(pay), "UTF-8"));
					System.err.println("URL-----"+url);
				// making connection
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Accept", "application/json");
				if (conn.getResponseCode() != 200||conn.getResponseCode() != 500) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
				}

				// Reading data's from url
			   BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

				String output;
				String out="";
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					//System.out.println(output);
					out+=output;
				}
			
				feeds=out;
					
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  			
  			//APIController.SaveApiReport("CheckPaytmTractionStatus", "CheckPaytmTractionStatus",  "CheckPaytmTractionStatus","0",
  				//	 "Get CheckPaytmTractionStatus",  feeds,  "Java","null", "null");
  		} catch (Exception e)
  		{
  			e.printStackTrace();
  			//System.out.println(e.getMessage());
  		}

  		return feeds;
  	}

	   @POST
	   	@Path("GenerateEmpTripReport")
	   	@Produces("application/json")
	   	public String GenerateEmpTripReport()
	   	{
	   		String feeds  = null;
	   		try 
	   		{
	   			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
	   			MessageObject msgData =new MessageObject();

	   			APIController handler= new APIController();
	   			msgData=handler.GenerateEmpTripReport();
	   			Gson gson = new Gson();
	   			feeds = gson.toJson(msgData);
	   			
	   		System.err.println("GenerateDevicePendingTripReport-----"+feeds);
	   			
	   		} catch (Exception e)
	   		{
	   			e.printStackTrace();
	   			//System.out.println(e.getMessage());
	   		}

	   		return feeds;
	   	}
	   
	   @POST
		@Path("GetDirectEmpTripReport")
		@Produces("application/json")
		public String GetDirectEmpTripReport(@FormParam("DeviceImieNo") String device,@FormParam("StartDateTime") String startdate
				,@FormParam("EndDateTime") String enddate)
		{
			String feeds  = null;
			try 
			{
				j=0;
				ArrayList<TripInfoDto> msgData = new ArrayList<TripInfoDto>();

				APIController handler= new APIController();
				msgData=handler.GetDirectEmpTripReport(device,startdate,enddate);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			System.out.println("Trip report-------"+feeds);

			return feeds;
		}
	   

	   @POST
		@Path("PostGcmKeyToServer")
		@Produces("application/json")
		public String PostGcmKeyToServer(@FormParam("gcm_key") String gcm_key,@FormParam("name") String name,@FormParam("user_id") String user_id)
		{
			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject();

				APIController handler= new APIController();
				msgData=handler.PostGcmKeyToServer(gcm_key,user_id,name);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
				//Common.SendGCMPushNotofication("hiiiiiiiiiiii");
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			System.out.println("Trip report-------"+feeds);

			return feeds;
		}
	   
	   ///check Driver start task or not and if not then send sms
	   		@POST
	 		@Path("CheckDriverOnRoute")
	 		@Produces("application/json")
	 		public String CheckDriverOnRoute()
	 		{
	 			String feeds  = null;
	 			try 
	 			{
	 				j=0;
	 				MessageObject msgData = new MessageObject();

	 				APIController handler= new APIController();
	 				msgData=handler.CheckDriverOnRoute();
	 				Gson gson = new Gson();
	 				feeds = gson.toJson(msgData);
	 				
	 			
	 				
	 			} catch (Exception e)
	 			{
	 				e.printStackTrace();
	 				//System.out.println(e.getMessage());
	 			}
	 			System.out.println("Trip report-------"+feeds);

	 			return feeds;
	 		}
	 	   


	private double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * 60 * 1.1515;
		  if (unit.equals("K")) {
		    dist = dist * 1.609344;
		  } else if (unit.equals("N")) {
		  dist = dist * 0.8684;
		   }
		  return (dist);
		}
		/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
		/*::  This function converts decimal degrees to radians             :*/
		/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
		private double deg2rad(double deg) {
		  return (deg * Math.PI / 180.0);
		}
		/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
		/*::  This function converts radians to decimal degrees             :*/
		/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
		private double rad2deg(double rad) {
		  return (rad * 180.0 / Math.PI);
		}



	
	
	
	



	 void Getdistance(double src_lat,double src_long, double dest_lat, double dest_long) {

			String url="http://maps.googleapis.com/maps/api/directions/json?origin="+src_lat+","+src_long+"&destination="+dest_lat+","+dest_long+"&sensor=false";

        //Instantiate an HttpClient
        HttpClient client = new HttpClient();

        //Instantiate a GET HTTP method
        PostMethod method = new PostMethod(url);
        method.setRequestHeader("Content-type",
                "text/xml; charset=ISO-8859-1");

     

        method.setQueryString(new NameValuePair[]{});

        try{
            int statusCode = client.executeMethod(method);

            System.out.println("Status Code = "+statusCode);
            System.out.println("QueryString>>> "+method.getQueryString());
            System.out.println("Status Text>>>"
                  +HttpStatus.getStatusText(statusCode));

            //Get data as a String
            System.out.println(method.getResponseBodyAsString());

            //OR as a byte array
            byte [] res  = method.getResponseBody();

            //write to file
            FileOutputStream fos= new FileOutputStream("donepage.html");
            fos.write(res);

            //release connection
            method.releaseConnection();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    
}
	
	 @POST
		@Path("GetWeekalyTripReportOnMail")
		@Produces("application/json")
		public String GetWeekalyTripReportOnMail(@FormParam("DeviceImieNo") String device,@FormParam("StartDateTime") String startdate
				,@FormParam("EndDateTime") String enddate,@FormParam("MailId") String email)
		{
			String feeds  = null;
			try 
			{
				j=0;
				//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
				MessageObject msgData =new MessageObject();

				APIController handler= new APIController();
				msgData=handler.GetWeekalyTripReportOnMail(device,startdate,enddate,email);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
				


				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}

			return feeds;
		}
	
	 	@POST
		@Path("GetAllDeviceLocation")
		@Produces("application/json")
		public String GetAllDeviceLocation(@FormParam("ParentId") String ParentId,@FormParam("StudentId") String studentId)
		{
			String feeds  = null;
			try 
			{
				j=0;
				ArrayList<HistoryDTO>  msgData = new ArrayList<HistoryDTO> ();

				APIController handler= new APIController();
				msgData=handler.GetAllDeviceLocation(ParentId,studentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
	 
	 
	 @POST
		@Path("GetOptimizedAllDeviceLocation")
		@Produces("application/json")
		public String GetOptimizedAllDeviceLocation(@FormParam("ParentId") String ParentId)
		{
			System.err.println("GetOptimizedAllDeviceLocation-------ParentId--------"+ParentId);

			String feeds  = null;
			try 
			{
				j=0;
				ArrayList<HistoryDTO>  msgData = new ArrayList<HistoryDTO> ();

				APIController handler= new APIController();
				msgData=handler.GetOptimizedAllDeviceLocation(ParentId);
		//	msgData=APIController.getInstance().GetOptimizedAllDeviceLocation(ParentId);

				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			////System.out.println(feeds);

			return feeds;
		}
	 
	
	
	 	@POST
		@Path("GetGeofenceHistory")
		@Produces("application/json")
		public String GetGeofenceHistory(@FormParam("DeviceImieNo") String device_imei,@FormParam("StartDateTime") String startdate,
				@FormParam("EndDateTime") String enddate)
		{
			String feeds  = null;
			try 
			{
				j=0;
				ArrayList<GeofenceDTO>  msgData = new ArrayList<GeofenceDTO> ();

				APIController handler= new APIController();
				msgData=handler.GetGeofenceHistory(device_imei,startdate,enddate);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
	 	
	 	
	 	@POST
			@Path("GetAssociatedParent")
			@Produces("application/json")
		public String GetAssociatedParent(@FormParam("UserId") String userId)
			{
				String feeds  = null;
				try 
				{
					j=0;
					ArrayList<AssociatedParentDTO>  msgData = new ArrayList<AssociatedParentDTO> ();

					APIController handler= new APIController();
					msgData=handler.GetAssociatedParent(userId);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
					
				
					
				} catch (Exception e)
				{
					e.printStackTrace();
					//System.out.println(e.getMessage());
				}
				//System.out.println(feeds);

				return feeds;
			}
	 	
	 		@POST
			@Path("SetBlindLocation")
			@Produces("application/json")
		public String SetBlindLocation(@FormParam("IMEI_NO") String devices)
			{
			System.out.println("SetBlindLocation------"+devices);

				String feeds  = null;
				try 
				{
					j=0;
					MessageObject msgData = new MessageObject ();

					APIController handler= new APIController();
					msgData=handler.SetBlindLocation(devices);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
					
				
					
				} catch (Exception e)
				{
					e.printStackTrace();
					//System.out.println(e.getMessage());
				}
				//System.out.println(feeds);

				return feeds;
			}
	 		
	 	@POST
		@Path("addMultipleDeviceFromText")
		@Produces("application/json")
		public String addMultipleDeviceFromText(@FormParam("IMEI_NO") String devices,@FormParam("SimCard") String sim_no,@FormParam("parentId") String parentId,
				@FormParam("Name") String name,@FormParam("DeviceCountStartFrom") int startCount)
		{
		System.out.println("addMultipleDeviceFromText------"+devices);

			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject ();

				APIController handler= new APIController();
				msgData=handler.addMultipleDeviceFromText(devices,sim_no,parentId,name,startCount);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
	 	
	 	
		@POST
		@Path("assigndeviceToMongodb")
		@Produces("application/json")
		public String assigndeviceToMongodb(@FormParam("parentId") String parentId)
		{
		System.out.println("assigndeviceToMongodb------"+parentId);

			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject ();

				APIController handler= new APIController();
				msgData=handler.assigndeviceToMongodb(parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
	 	
	 	
	 	

		@POST
		@Path("makePaymentToDevice")
		@Produces("application/json")
		public String makePaymentToDevice(@FormParam("startStudentId") String startStudentId,@FormParam("endStudentId") String endStudentId)
		{
		System.out.println("makePaymentToDevice------"+startStudentId+"===="+endStudentId);

			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject ();

				APIController handler= new APIController();
				msgData=handler.makePaymentToDevice(startStudentId,endStudentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}

	
		@POST
		@Path("GetFeatureAddress")
		@Produces("application/json")
		public String GetFeatureAddress(@FormParam("ParentId") String parentId)
		{
			String feeds  = null;
			try 
			{
				System.err.println("GetFeatureAddress--parentId-"+parentId);
				j=0;
				ArrayList<RailWayAddressDTO>  msgData = new ArrayList<RailWayAddressDTO> ();

				APIController handler= new APIController();
				msgData=handler.GetFeatureAddress(parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			////System.out.println(feeds);

			return feeds;
		}
		
		@POST
		@Path("GetKmFeatureAddress")
		@Produces("application/json")
		public String GetKmFeatureAddress(@FormParam("ParentId") String parentId)
		{
			String feeds  = null;
			try 
			{
				j=0;
				ArrayList<RailWayAddressDTO>  msgData = new ArrayList<RailWayAddressDTO> ();

				APIController handler= new APIController();
				msgData=handler.GetKmFeatureAddress(parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
		
		
		
		@POST
		@Path("GenerateGPSHolder10MinData")
		@Produces("application/json")
		public String GenerateGPSHolder10MinData(@FormParam("StartDateTime") String startdate,
				@FormParam("pastMin") String pastMin,
				@FormParam("ParentId") String parentId)
		{
			String feeds  = null;
			try 
			{
				j=0;
				ArrayList<RailDeviceInfoDto>  msgData = new ArrayList<RailDeviceInfoDto>();

				APIController handler= new APIController();
				msgData=handler.GenerateGPSHolder10MinData(startdate,pastMin,parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
		
		
		@POST
		@Path("GenerateGPSDeviceOnData")
		@Produces("application/json")
		public String GenerateGPSDeviceOnData(@FormParam("StartDateTime") String startdate,
				@FormParam("ParentId") String parentId)
		{
			String feeds  = null;
			try 
			{
				j=0;
				ArrayList<RailDeviceInfoDto>  msgData = new ArrayList<RailDeviceInfoDto>();

				APIController handler= new APIController();
				msgData=handler.GenerateGPSDeviceOnData(startdate,parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
		
		@POST
		@Path("GenerateGPSDeviceOFFData")
		@Produces("application/json")
		public String GenerateGPSDeviceOFFData(@FormParam("StartDateTime") String startdate
				,@FormParam("ParentId") String parentId)
		{
			String feeds  = null;
			try 
			{
				j=0;
				ArrayList<RailDeviceInfoDto>  msgData = new ArrayList<RailDeviceInfoDto>();

				APIController handler= new APIController();
				msgData=handler.GenerateGPSDeviceOFFData(startdate,parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
		
		@POST
		@Path("ExportRailAddressToSql")
		@Produces("application/json")
		public String ExportRailAddressToSql(@FormParam("parentId") String parentId)
		{
			String feeds  = null;
			try 
			{
				j=0;
				//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
				MessageObject msgData =new MessageObject();

				APIController handler= new APIController();
				msgData=handler.ExportRailAddressToSql(parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}

			return feeds;
		}
		

		@POST
		@Path("RenewMultiplePaymentToDevice")
		@Produces("application/json")
		public String RenewMultiplePaymentToDevice(@FormParam("startStudentId") String startStudentId,@FormParam("endStudentId") String endStudentId)
		{
		System.out.println("RenewMultiplePaymentToDevice------"+startStudentId+"===="+endStudentId);

			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject ();

				APIController handler= new APIController();
				msgData=handler.RenewMultiplePaymentToDevice(startStudentId,endStudentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}

		
		@POST
		@Path("GenerateFeaturecodeNearby")
		@Produces("application/json")
		public String FeaturecodeNearby(@QueryParam("ParentId") String parentId,@QueryParam("Featurecode") String featurecode
				,@QueryParam("NearByDistance") String near_bydist)
		{
			String feeds  = null;
			try 
			{
				j=0;
				//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
				MessageObject msgData =new MessageObject();

				APIController handler= new APIController();
				msgData=handler.FeaturecodeNearby(featurecode,near_bydist,parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}

			return feeds;
		}
		
		@POST
		@Path("GetFeaturecodeNearby")
		@Produces("application/json")
		public String GetFeaturecodeNearby(@FormParam("StartDateTime") String
				startdate,@FormParam("Featurecode") String featurecode,@FormParam("ParentId") String parentId
				)
		{
			System.out.println(startdate+"---------------"+featurecode);

			String feeds  = null;
			try 
			{
				j=0;
				ArrayList<FeatureNearbyDTO> msgData = new ArrayList<FeatureNearbyDTO>();

				APIController handler= new APIController();
				msgData=handler.GetFeaturecodeNearby(startdate,featurecode,parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}

			return feeds;
		}
			
		@POST
		@Path("GetSosData")
		@Produces("application/json")
		public String GetSosData(@FormParam("StartDateTime") String startdate,@FormParam("ParentId") String parentId
				)
		{
			System.out.println(startdate+"---------------"+parentId);

			String feeds  = null;
			try 
			{
				j=0;
				ArrayList<SosInfoDTO> msgData = new ArrayList<SosInfoDTO>();

				APIController handler= new APIController();
				msgData=handler.GetSosData(startdate,parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}

			return feeds;
		}	
		
		
		@POST
		@Path("GetBatteryData")
		@Produces("application/json")
		public String GetBatteryData(@FormParam("ParentId") String parentId
				)
		{
			System.out.println("---------------"+parentId);

			String feeds  = null;
			try 
			{
				j=0;
				ArrayList<SosInfoDTO> msgData = new ArrayList<SosInfoDTO>();

				APIController handler= new APIController();
				msgData=handler.GetBatteryData(parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}

			return feeds;
		}	
		
		
		@POST
		@Path("GetCurrentAllDeviceStaus")
		@Produces("application/json")
		public String GetCurrentAllDeviceStaus()
		{
			
			

			String feeds  = null;
			try 
			{
				j=0;
				ArrayList<DeviceStatusInfoDto>  msgData = new ArrayList<DeviceStatusInfoDto>();

				APIController handler= new APIController();
				msgData=handler.GetCurrentAllDeviceStaus();
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
		

		@POST
		@Path("assignSimNumberSqlToMongodb")
		@Produces("application/json")
		public String assignSimNumberSqlToMongodb()
		{
		System.out.println("assignSimNumberSqlToMongodb------");

			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject ();

				APIController handler= new APIController();
				msgData=handler.assignSimNumberSqlToMongodb();
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
	 	
	 	
		@GET
		@Path("trainAlertForGangman")
		@Produces("application/json")
		public String trainAlertForGangman(@QueryParam("timestamp") Long timestamp,
				@QueryParam("speed") int speed,
				@QueryParam("lat") Double lat,@QueryParam("lan") Double lan,@QueryParam("train_device") String train_device)
		{
		System.out.println("trainAlertForGangman------");

			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject ();

				APIController handler= new APIController();
				msgData=handler.trainAlertForGangman(timestamp,speed,lat,lan,train_device);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
		
		@POST
		@Path("SendGPSDeviceOnOffData")
		@Produces("application/json")
		public String SendGPSDeviceOnOffData(@QueryParam("ParentId") String parentId
				)
		{
			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject();

				APIController handler= new APIController();
				msgData=handler.SendGPSDeviceOnOffData(parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
		

		@POST
		@Path("SendGPSTripReportData")
		@Produces("application/json")
		public String SendGPSTripReportData(@QueryParam("ParentId") String parentId)
		{
			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject();

				APIController handler= new APIController();
				msgData=handler.SendGPSTripReportData(parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
		
		@POST
		@Path("UpdateNameOfDevices")
		@Produces("application/json")
		public String UpdateNameOfDevices(@FormParam("Name") String name,@FormParam("ParentId")
		String parentId,@FormParam("IdNo") String idno)
		{
			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject();

				APIController handler= new APIController();
				msgData=handler.UpdateNameOfDevices(name,idno,parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
		
		@POST
		@Path("UpdateDevicesSimNo")
		@Produces("application/json")
		public String UpdateDevicesSimNo(@FormParam("Simno") String simno,
				@FormParam("ParentId") String parentId,@FormParam("IdNo") String idno)
		{
			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject();

				APIController handler= new APIController();
				msgData=handler.UpdateDevicesSimNo(simno,idno,parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
		
		@POST
		@Path("getTrackInfoForCustomUser")
		@Produces("application/json")
		public String getTrackInfoForCustomUser(@FormParam("ParentId") String parentId)
		{
			
			//System.out.println("getTrackInfoForCustomUser-----"+parentId);

			String feeds  = null;
			try 
			{
				ArrayList<DevicelistDetails> msgData = new ArrayList<DevicelistDetails>();
				APIController handler= new APIController();
				msgData = handler.getTrackInfoForCustomUser(parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
		
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
		//	System.out.println("getLoginDetails-----"+feeds);

			return feeds;
		}

		@POST
		@Path("saveTrackCustomhierarchyUserNew")
		@Produces("application/json")
		public String saveTrackCustomhierarchyUserNew(@FormParam("Name") String name,
				@FormParam("MobileNo") String mobileNo,@FormParam("EmailId") String emailId,@FormParam("ParentId") String parentId)
		{
			
			System.out.println("saveTrackCustomhierarchyUserNew-----"+name+"=="+mobileNo+"--"+emailId);

			String feeds  = null;
			try 
			{
				MessageObject msgData = new MessageObject();
				APIController handler= new APIController();
				msgData = handler.saveTrackCustomhierarchyUserNew(name,mobileNo,emailId,parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
		
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			System.out.println("getLoginDetails-----"+feeds);

			return feeds;
		}
		
		@POST
		@Path("SaveRailwayKeymanBeatPath")
		@Produces("application/json")
		public String SaveRailwayKeymanBeatPath(@FormParam("StudentID") String StudentID,
				@FormParam("KmFrom") String KmFrom,@FormParam("KmTo") String KmTo,
				@FormParam("SectionName") String SectionName,@FormParam("ParentId") String parentId)
		{
			
			
			System.out.println("ParentId--"+parentId);
			//System.out.println("SaveRailwayKeymanBeatPath-----"+StudentID+"=="+KmFrom+"--"+KmTo+"-"+SectionName);

			String feeds  = null;
			try 
			{
				MessageObject msgData = new MessageObject();
				APIController handler= new APIController();
				msgData = handler.SaveRailwayKeymanBeatPath(StudentID,KmFrom,KmTo,SectionName,parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
		
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			System.out.println("getLoginDetails-----"+feeds);

			return feeds;
		}
		
		@POST
		@Path("GenerateExceptionReportPetrolmanBeatPath")
		@Produces("application/json")
		public String GenerateExceptionReportPetrolmanBeatPath(@QueryParam("ParentId") String parentId
				,@QueryParam("Day") int day,@QueryParam("isSendMail") Boolean isSendMail,@QueryParam("seasonId") int seasonId,
				@QueryParam("TimeToleranceInSec") int timeTolerance,@QueryParam("DistanceToleranceInKm") Double distanceTolerance)
		{
			
			
			
			System.out.println("GenerateExceptionReportPetrolmanBeatPath-----");

			String feeds  = null;
			try 
			{
				MessageObject msgData = new MessageObject();
				APIController handler= new APIController();
				msgData = handler.GenerateExceptionReportPetrolmanBeatPath(parentId,day,isSendMail,seasonId,timeTolerance,distanceTolerance);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
		
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			System.out.println("GenerateExceptionReportPetrolmanBeatPath-----"+feeds);

			return feeds;
		}
		
		@POST
		@Path("GenerateExceptionReportKeyManBeatPath")
		@Produces("application/json")
		public String GenerateExceptionReportKeyManBeatPath(@QueryParam("ParentId") String parentId)
		{
			
			
			
			System.out.println("GenerateExceptionReportKeyManBeatPath-----");

			String feeds  = null;
			try 
			{
				MessageObject msgData = new MessageObject();
				APIController handler= new APIController();
				msgData = handler.GenerateExceptionReportKeyManBeatPath(parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
		
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			System.out.println("GenerateExceptionReportKeyManBeatPath-----"+feeds);

			return feeds;
		}
	
		
		@POST
		@Path("UpdateParentName")
		@Produces("application/json")
		public String UpdateParentName(@FormParam("Name") String name,@FormParam("Email") String parentEmail)
		{
			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject();

				APIController handler= new APIController();
				msgData=handler.UpdateParentName(name,parentEmail);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
		
		@POST
		@Path("AlertForStartWorkWithLowBatteryPatrolman")
		@Produces("application/json")
		public String AlertForStartWorkWithLowBatteryPatrolman(@QueryParam("ParentId") String parentId)
		{
			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject();

				APIController handler= new APIController();
				msgData=handler.AlertForStartWorkWithLowBatteryPatrolman(parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
		
		@POST
		@Path("AlertForKeymanWorkStatusReport")
		@Produces("application/json")
		public String AlertForKeymanWorkStatusReport(@QueryParam("ParentId") String parentId)
		{
			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject();
				APIController handler= new APIController();
				msgData=handler.AlertForKeymanWorkStatusReport(parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
		
		@POST
		@Path("AlertForInactiveDevicePartolMan")
		@Produces("application/json")
		public String AlertForInactiveDevicePartolMan(@QueryParam("ParentId") String parentId)
		{
			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject();

				APIController handler= new APIController();
				msgData=handler.AlertForInactiveDevicePartolMan(parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
			//System.out.println(feeds);

			return feeds;
		}
		@POST
		@Path("AlertForInactiveDeviceKeyMan")
		@Produces("application/json")
		public String AlertForInactiveDeviceKeyMan(@QueryParam("ParentId") String parentId)
		{
			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject();

				APIController handler= new APIController();
				msgData=handler.AlertForInactiveDeviceKeyMan(parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				////System.out.println(e.getMessage());
			}
			////System.out.println(feeds);

			return feeds;
		}
		
		public static String[] degreesToDecimal(String degMinSec) {
		    String[] result = new String[2];
		    Pattern p = Pattern.compile("(\\d+).*?(\\d+).*?(\\d+).*?([N|S|E|W]).*?(\\d+).*?(\\d+).*?(\\d+).*?([N|S|E|W]).*?");
		    Matcher m = p.matcher(degMinSec);

		    if (m.find()) {
		        int degLat = Integer.parseInt(m.group(1));
		        int minLat = Integer.parseInt(m.group(2));
		        int secLat = Integer.parseInt(m.group(3));
		        String dirLat = m.group(4);
		        int degLon = Integer.parseInt(m.group(5));
		        int minLon = Integer.parseInt(m.group(6));
		        int secLon = Integer.parseInt(m.group(7));
		        String dirLon = m.group(8);

		        DecimalFormat formatter = new DecimalFormat("#.#####", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		        formatter.setRoundingMode(RoundingMode.DOWN);

		        result[0] = formatter.format(degLat + minLat / 60.0 + secLat / 3600.0) + " " + dirLat;
		        result[1] = formatter.format(degLon + minLon / 60.0 + secLon / 3600.0) + " " + dirLon;
		    }
		    return result;
		}
		
		
		@POST
		@Path("UpdateSectionNameInStudent")
		@Produces("application/json")
		public String UpdateSectionNameInStudent(@FormParam("SectionName") String SectionName,@FormParam("StudentIds") String StudentIds)
		{
			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject();

				APIController handler= new APIController();
				msgData=handler.UpdateSectionNameInStudent(SectionName,StudentIds);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				////System.out.println(e.getMessage());
			}
			////System.out.println(feeds);

			return feeds;
		}
		
		@POST
		@Path("AlertForPatrolManTotalBeatNotCover")
		@Produces("application/json")
		public String AlertForPatrolManTotalBeatNotCover(@QueryParam("ParentId") String parentId)
		{
			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject();

				APIController handler= new APIController();
				msgData=handler.AlertForPatrolManTotalBeatNotCover(parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				////System.out.println(e.getMessage());
			}
			////System.out.println(feeds);

			return feeds;
		}
		
		@POST
		@Path("AlertForKeyManTotalBeatNotCover")
		@Produces("application/json")
		public String AlertForKeyManTotalBeatNotCover(@QueryParam("ParentId") String parentId)
		{
			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject();

				APIController handler= new APIController();
				msgData=handler.AlertForKeyManTotalBeatNotCover(parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				////System.out.println(e.getMessage());
			}
			////System.out.println(feeds);

			return feeds;
		}

		@POST
		@Path("UpdateDevicesIMEIAndSimNo")
		@Produces("application/json")
		public String UpdateDevicesIMEIAndSimNo(@FormParam("Simno") String simno,@FormParam("IMEINo") String imeiNo,
				@FormParam("ParentId") String parentId,@FormParam("IdNo") String idno)
		{
			String feeds  = null;
			try 
			{
				j=0;
				MessageObject msgData = new MessageObject();

				APIController handler= new APIController();
				msgData=handler.UpdateDevicesIMEIAndSimNo(simno,imeiNo,idno,parentId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
			
				
			} catch (Exception e)
			{
				e.printStackTrace();
				////System.out.println(e.getMessage());
			}
			////System.out.println(feeds);

			return feeds;
		}
		@POST
		@Path("GetHistoryInfo")
		@Produces("application/json")
		public String GetHistoryInfo(@FormParam("Imei_no") String student_id,@FormParam("StartDateTime") String startdate,
				@FormParam("EndDateTime") String enddate)
		{
			String feeds  = null;
			try 
			{
				System.err.println("Student----------------"+student_id);
				HistoryInfoDTO msgData = new HistoryInfoDTO();
				APIController handler= new APIController();
				msgData = handler.GetHistoryData(student_id,startdate,enddate);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				//APIController.SaveApiReport("GetHistorydata", "GetHistorydata",  "GetHistorydata","0",
						// "Get GetHistorydatanay",  feeds,  "Java","null", "null");
			} catch (Exception e)
			{
				e.printStackTrace();
				////System.out.println(e.getMessage());
			}
			//System.out.println("History----"+feeds);

			return feeds;
		}
		
		
		 @GET
			@Path("GenerateLocationIPReport")
			@Produces("application/json")
		 public String GenerateLocationIPReport()

			{
				String feeds  = null;
				try 
				{
					ArrayList<HistoryDTO> msgData = new ArrayList<HistoryDTO>();
					APIController handler= new APIController();
					msgData = handler.GenerateLocationIPReport();
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
					
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				//System.out.println("History----"+feeds);

				return feeds;
			}
			
			
			@POST
			@Path("GenerateExceptionReportPetrolmanForVaranasi")
			@Produces("application/json")
		public String GenerateExceptionReportPetrolmanForVaranasi(@QueryParam("ParentId") String parentId)
			{
				
				
				
				//System.out.println("GenerateExceptionReportPetrolmanForVaranasi-----");

				String feeds  = null;
				try 
				{
					MessageObject msgData = new MessageObject();
					APIController handler= new APIController();
					msgData = handler.GenerateExceptionReportPetrolmanForVaranasi(parentId);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
			
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				//System.out.println("GenerateExceptionReportPetrolmanForVaranasi-----"+feeds);

				return feeds;
			}
			
			
			@POST
			@Path("AlertForPatrolmanWorkStatusReportForVaranasi")
			@Produces("application/json")
		public String AlertForPatrolmanWorkStatusReportForVaranasi(@QueryParam("ParentId") String parentId)
			{
				String feeds  = null;
				try 
				{
					j=0;
					MessageObject msgData = new MessageObject();

					APIController handler= new APIController();
					msgData=handler.AlertForPatrolmanWorkStatusReportForVaranasi(parentId);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
					
				
					
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				////System.out.println(feeds);

				return feeds;
			}
		
			@POST
			@Path("getExceptionReportFile")
			@Produces("application/json")
		public String getExceptionReportFile(@FormParam("ParentId") String parentId,@FormParam("Timestamp") String timestamp)
			{
				
				//System.out.println("getTrackInfoForCustomUser-----"+parentId);

				String feeds  = null;
				try 
				{
					ArrayList<ExceptionReportFileDTO> msgData = new ArrayList<ExceptionReportFileDTO>();
					APIController handler= new APIController();
					msgData = handler.getExceptionReportFile(parentId,timestamp);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
			
				} catch (Exception e)
				{
					e.printStackTrace();
					//System.out.println(e.getMessage());
				}
			//	System.out.println("getLoginDetails-----"+feeds);

				return feeds;
			}

			@POST
			@Path("GetExceptionDeviceList")
			@Produces("application/json")
		public String GetExceptionDeviceList(@FormParam("ParentId") String parentId,@FormParam("StartTimestamp") String startTimestamp
					,@FormParam("EndTimestamp") String endTimestamp)
			{
				
				////System.out.println("getTrackInfoForCustomUser-----"+parentId);

				String feeds  = null;
				try 
				{
					ArrayList<ExceptionDeviceDTO> msgData = new ArrayList<ExceptionDeviceDTO>();
					APIController handler= new APIController();
					msgData = handler.GetExceptionDeviceList(parentId,startTimestamp,endTimestamp);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
			
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				//System.out.println("GetExceptionDeviceList-----"+feeds);

				return feeds;
			}
			

			@POST
			@Path("getDeviceRangeExceptionReport")
			@Produces("application/json")
		public String getDeviceRangeExceptionReport(@FormParam("ParentId") String parentId,@FormParam("reportType") String reportType,@FormParam("StartTimestamp") int startTimestamp
								,@FormParam("EndTimestamp") int endTimestamp)
						{
			  String feeds  = null;
			  try { 
			  ArrayList<DateRangeExceptionReportDTO> msgData = new
			  ArrayList<DateRangeExceptionReportDTO>(); APIController handler= new
			  APIController(); msgData =
			  handler.getDeviceRangeExceptionReport(parentId,reportType,startTimestamp,
			  endTimestamp); Gson gson = new Gson(); feeds = gson.toJson(msgData);
			  } catch (Exception e) { 
			  e.printStackTrace();
			  ////System.out.println(e.getMessage()); 
			  }
			  return feeds;
		}

			@POST
			@Path("GenerateExceptionReportKeyManBeatPathJabalpur")
			@Produces("application/json")
		public String GenerateExceptionReportKeyManBeatPathJabalpur(@QueryParam("ParentId") String parentId)
			{
				
				
				
				//System.out.println("GenerateExceptionReportKeyManBeatPathJabalpur-----");

				String feeds  = null;
				try 
				{
					MessageObject msgData = new MessageObject();
					APIController handler= new APIController();
					msgData = handler.GenerateExceptionReportKeyManBeatPathJabalpur(parentId);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
			
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				//System.out.println("GenerateExceptionReportKeyManBeatPathJabalpur-----"+feeds);

				return feeds;
			}
			
			@GET
			@Path("SendWrongLocationEmail")
			@Produces("application/json")
		public String SendWrongLocationEmail(@QueryParam("DeviceId") String deviceId,@QueryParam("Lat") String lat,
					@QueryParam("Lan") String lan,@QueryParam("Speed") String speed)
			{
				
				
				
				//System.out.println("SendWrongLocationEmail-----");

				String feeds  = null;
				try 
				{
					MessageObject msgData = new MessageObject();
					APIController handler= new APIController();
					msgData = handler.SendWrongLocationEmail(deviceId,lat,lan,speed);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
			
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}

				return feeds;
			}
			@POST
			@Path("ExportKeyManBeatToRDPSData")
			@Produces("application/json")
		public String ExportKeyManBeatToRDPSData(@FormParam("ParentId") String parentId)

			{
				
				
				
				//System.out.println("ExportKeyManBeatToRDPSData-----");

				String feeds  = null;
				try 
				{
					MessageObject msgData = new MessageObject();
					APIController handler= new APIController();
					msgData = handler.ExportKeyManBeatToRDPSData(parentId);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
			
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				//System.out.println("ExportKeyManBeatToRDPSData-----"+feeds);

				return feeds;
			}
			
			
			
			@POST
			@Path("TodayDeviceStatus")
			@Produces("application/json")
		public String TodayDeviceStatus(
					@FormParam("ParentId") String parentId)
			{
				String feeds  = null;
				try 
				{
					j=0;
					ArrayList<RailDeviceInfoDto>  msgData = new ArrayList<RailDeviceInfoDto>();

					APIController handler= new APIController();
					msgData=handler.TodayDeviceStatus(parentId);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
					
				
					
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				////System.out.println(feeds);

				return feeds;
			}
		
			@POST
			@Path("DeviceONOffStatus")
			@Produces("application/json")
		public String DeviceONOffStatus(@FormParam("StartDateTime") String startdate,
					@FormParam("ParentId") String parentId)
			{
				String feeds  = null;
				try 
				{
					j=0;
					ArrayList<RailDeviceInfoDto>  msgData = new ArrayList<RailDeviceInfoDto>();

					APIController handler= new APIController();
					msgData=handler.DeviceONOffStatus(startdate,parentId);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
					
				
					
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				////System.out.println(feeds);

				return feeds;
			}
			
			@POST
			@Path("InsertPatrolmanBeat")
			@Produces("application/json")
		public String InsertPatrolmanBeat(@FormParam("Device") String device,@FormParam("Section") String section,@FormParam("Km") String km,
					@FormParam("ParentId") String parentId)
			{
				String feeds  = null;
				try 
				{
					j=0;
					MessageObject  msgData = new MessageObject();

					APIController handler= new APIController();
					msgData=handler.InsertPatrolmanBeat(device,section,km,parentId);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
					
				
					
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				////System.out.println(feeds);

				return feeds;
			}
			@POST
			@Path("BackupNremoveWrongLoc")
			@Produces("application/json")
		public String LocationContentAppropriate(@FormParam("imeiNo") String imeiNo,@FormParam("StartTimestamp") String startTimestamp

					,@FormParam("EndTimestamp") String endTimestamp)
			{
				//System.out.println("BackupNremoveWrongLoc-----"+imeiNo);
				ArrayList<WrongLocationDataDTO> locationData = new ArrayList<WrongLocationDataDTO>();
				String feeds  = null;
				try 
				{
					String[] ImeiNos = imeiNo.split(",");
					String[] StartTimeStamps = startTimestamp.split(",");
					String[] EndTimeStamps = endTimestamp.split(",");
					for(int i=0;i<ImeiNos.length;i++) {
						WrongLocationDataDTO data = new WrongLocationDataDTO();
						data.setImeiNo(ImeiNos[i]);
						data.setStartTimestamp(StartTimeStamps[i]);
						data.setEndTimestamp(EndTimeStamps[i]);
						locationData.add(data);
					}
					ArrayList<MessageObject> msgData = new ArrayList<MessageObject>();
					APIController handler= new APIController();
					msgData = handler.BackupNremoveWrongLoc(locationData);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				////System.out.println("BackupNremoveWrongLoc-----"+feeds);

				return feeds;
			}
			
			
			
			
			@POST
			@Path("GenerateExceptionReportPatrolManBeatPathJabalpur")
			@Produces("application/json")
		public String GenerateExceptionReportPatrolManBeatPathJabalpur(@QueryParam("ParentId") String parentId)

			
			{
				
				
				
				//System.out.println("GenerateExceptionReportPatrolManBeatPathJabalpur-----");

				String feeds  = null;
				try 
				{
					MessageObject msgData = new MessageObject();
					APIController handler= new APIController();
					msgData = handler.GenerateExceptionReportPatrolManBeatPathJabalpur(parentId);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
			
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				//System.out.println("GenerateExceptionReportPatrolManBeatPathJabalpur-----"+feeds);

				return feeds;
			}
			
			
			
			
			
			
			@POST
			@Path("setSectionNameToRailwaykeymanBeat")
			@Produces("application/json")
		public String setSectionNameToRailwaykeymanBeat(@FormParam("ParentId") String ParentId)
			{
				
				MessageObject msgData = new MessageObject ();
				
				APIController handler= new APIController();
				msgData=handler.setSectionNameToRailwaykeymanBeat(ParentId);
				Gson gson = new Gson();
				String feeds = gson.toJson(msgData);
				
				return feeds;
				
			}
				
			
			@POST
			@Path("GenerateExceptionReportKeyManBeatPathRanchi")
			@Produces("application/json")
		public String GenerateExceptionReportKeyManBeatPathRanchi(@QueryParam("ParentId") String parentId)
			{
				
				
				
				//System.out.println("GenerateExceptionReportKeyManBeatPathRanchi-----");

				String feeds  = null;
				try 
				{
					MessageObject msgData = new MessageObject();
					APIController handler= new APIController();
					msgData = handler.GenerateExceptionReportKeyManBeatPathRanchi(parentId);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
			
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				//System.out.println("GenerateExceptionReportKeyManBeatPathRanchi-----"+feeds);

				return feeds;
			}
			@POST
			@Path("GenerateExceptionReportPatrolManBeatPathRanchi")
			@Produces("application/json")
		public String GenerateExceptionReportPatrolManBeatPathRanchi(@QueryParam("ParentId") String parentId)
			{
				
				
				

				String feeds  = null;
				try 
				{
					MessageObject msgData = new MessageObject();
					APIController handler= new APIController();
					msgData = handler.GenerateExceptionReportPatrolManBeatPathRanchi(parentId);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
			
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				//System.out.println("GenerateExceptionReportPatrolManBeatPathRanchi-----"+feeds);

				return feeds;
			}
			
			
			@POST
			@Path("insertGeoFenceLocationInTodayLocation")
			@Produces("application/json")
		public String insertGeoFenceLocationInTodayLocation()
			{
//				@FormParam("DeviceIMEI") String device,
//				@FormParam("startLat") String startLat,@FormParam("startLan") String startLan,
//				@FormParam("fenceDist") String fenceDist
			//System.out.println("insertGeoFenceLocationInTodayLocation------");

				String feeds  = null;
				try 
				{
					j=0;
					MessageObject msgData = new MessageObject ();

					APIController handler= new APIController();
					msgData=handler.insertGeoFenceLocationInTodayLocation();//device,startLat,startLan,fenceDist
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
					
				
					
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				////System.out.println(feeds);

				return feeds;
			}
		 	
			@POST
			@Path("GetBatteryStatusInfo")
			@Produces("application/json")
		public String GetBatteryStatusInfo(@FormParam("imeiNo") String student_id,@FormParam("StartDateTime") String startdate,
					@FormParam("EndDateTime") String enddate)
			{
				String feeds  = null;
				try 
				{
					System.err.println("GetBatteryStatusInfo----------------"+student_id);
					 ArrayList<dto.DeviceBatteryInfo>  msgData = new ArrayList<>();
					APIController handler= new APIController();
					msgData = handler.GetBatteryStatusInfo(student_id,Long.parseLong(startdate),Long.parseLong(enddate));
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
					//APIController.SaveApiReport("GetHistorydata", "GetHistorydata",  "GetHistorydata","0",
							// "Get GetHistorydatanay",  feeds,  "Java","null", "null");
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				//System.out.println("History----"+feeds);

				return feeds;
			}
			
			@POST
			@Path("GenerateExceptionReportPetrolmanBeatPathWithWholeTimestampConsider")
			@Produces("application/json")
		public String GenerateExceptionReportPetrolmanBeatPathWithWholeTimestampConsider(@QueryParam("ParentId") String parentId
					,@QueryParam("Day") int day,@QueryParam("isSendMail") Boolean isSendMail,@QueryParam("seasonId") int seasonId,
					@QueryParam("TimeToleranceInSec") int timeTolerance,@QueryParam("DistanceToleranceInKm") Double distanceTolerance)
			{
				
				
				
				//System.out.println("GenerateExceptionReportPetrolmanBeatPathWithWholeTimestampConsider-----");

				String feeds  = null;
				try 
				{
					MessageObject msgData = new MessageObject();
					APIController handler= new APIController();
					msgData = handler.GenerateExceptionReportPetrolmanBeatPathWithWholeTimestampConsider(parentId
							,day,isSendMail,seasonId,timeTolerance,distanceTolerance);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
			
				} catch (Exception e)
				{
					e.printStackTrace();
					////System.out.println(e.getMessage());
				}
				System.out.println("GenerateExceptionReportPetrolmanBeatPath-----"+feeds);

				return feeds;
			}
			
			@POST
			@Path("GetDeviceBeat")
			@Produces("application/json")
		public String GetDeviceBeat(@FormParam("studentId") int studentId)
			{
				String feeds  = null;
				try 
				{
					ArrayList<RailwayKeymanDTO> msgData = new ArrayList<RailwayKeymanDTO>();
					APIController handler= new APIController();
					msgData = handler.GetDeviceBeat(studentId);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
				} catch (Exception e)
				{
					e.printStackTrace();
					//System.out.println(e.getMessage());
				}
				return feeds;
			}	
			
}
