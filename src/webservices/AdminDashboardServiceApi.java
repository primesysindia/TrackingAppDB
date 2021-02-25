package webservices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.json.JsonArray;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import model.APIController;

import com.google.gson.Gson;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import dao.Common;
import dto.AddDeviceDropDownInfo;
import dto.DepartmentsDTO;
import dto.DeviceCommandDTO;
import dto.DeviceCommandHistoryDTO;
import dto.DeviceDTO;
import dto.DeviceDataDTO;
import dto.DeviceExchangeDTO;
import dto.DeviceInfoGatheringDTO;
import dto.DeviceIssueDTO;
import dto.DevicePaymentDTO;
import dto.DevicePaymentInfoDetailsDTO;
import dto.FeatureAddressDetailsDTO;
import dto.IssueFileInfoDTO;
import dto.MailFormatDTO;
import dto.MessageObject;
import dto.RailwayDeptHierarchyDTO;
import dto.RailwayKeymanDTO;
import dto.RailwayPatrolManDTO;
import dto.ReportSummeryDTO;
import dto.UserDTO;
@Path("AdminDashboardServiceApi")
public class AdminDashboardServiceApi {
	
	@GET
	@Path("GetAllTrackUser")
	@Produces("application/json")
	public String GetAllTrackUser()
	{

		String feeds  = null;
		try 
		{
			ArrayList<UserDTO> msgData = new ArrayList<UserDTO>();
			APIController handler= new APIController();
			msgData = handler.GetAllTrackUser();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GetAllTrackUser-----"+feeds);

		return feeds;
	}
	
	@POST
	@Path("GetKeymanExistingBeat")
	@Produces("application/json")
	public String GetKeymanExistingBeat(@FormParam("parentId") int parentId,@FormParam("studentId") int studentId,
			@FormParam("beatId") int beatId,@FormParam("userLoginId") int userLoginId)
	{
		String feeds  = null;
		try 
		{
			ArrayList<RailwayKeymanDTO> msgData = new ArrayList<RailwayKeymanDTO>();
			APIController handler= new APIController();
			msgData = handler.GetKeymanExistingBeat(parentId,studentId,beatId,userLoginId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GetKeymanExistingBeat-----"+feeds);
		return feeds;
	}
	
	@POST
	@Path("SaveKeymanBeat")
	@Produces("application/json")
	public String SaveKeymanBeat(@FormParam("parentId") int parentId,@FormParam("studentId") int studentId,@FormParam("kmStart") double kmStart,
			@FormParam("kmEnd") double kmEnd,@FormParam("sectionName") String sectionName,@FormParam("deviceId") String deviceId,
			@FormParam("kmStartLat") double kmStartLat,@FormParam("kmStartLang") double kmStartLang,@FormParam("kmEndLat") double kmEndLat,
			@FormParam("kmEndLang") double kmEndLang,@FormParam("userLoginId") int userLoginId)
	{
		String feeds  = null;
		try 
		{
			//System.out.println("SaveKeymanBeat------"+parentId);
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.SaveKeymanBeat(parentId,studentId,kmStart,kmEnd,sectionName,
					deviceId,kmStartLat,kmStartLang,kmEndLat,kmEndLang,userLoginId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("SaveKeymanBeat-----"+feeds);
		return feeds;
	}

	
	@POST
	@Path("UpdateKeymanBeat")
	@Produces("application/json")
	public String UpdateKeymanBeat(@FormParam("parentId") int parentId,@FormParam("studentId") int studentId,@FormParam("kmStart") double kmStart,
		@FormParam("kmEnd") double kmEnd,@FormParam("sectionName") String sectionName,@FormParam("kmStartLat") double kmStartLat,@FormParam("kmStartLang") double kmStartLang,
		@FormParam("kmEndLat") double kmEndLat,@FormParam("kmEndLang") double kmEndLang,@FormParam("beatId")int beatId,@FormParam("userLoginId") int userLoginId,@FormParam("isApprove") Boolean isApprove)
	{
		String feeds  = null;
		try 
		{
			//System.out.println("UpdateKeymanBeat-----"+beatId);
			MessageObject msgData=new MessageObject();
			APIController handler= new APIController();
			msgData = handler.UpdateKeymanBeat(parentId,studentId,kmStart,kmEnd,sectionName,
					kmStartLat,kmStartLang,kmEndLat,kmEndLang,beatId,userLoginId,isApprove);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
		//System.out.println("UpdateKeymanBeat-----"+feeds);
		return feeds;
	}
	
	@POST
	@Path("SaveRailwayPatrolManTripTimeMaster")
	@Produces("application/json")
	public String SaveRailwayPatrolManTripTimeMaster(@FormParam("tripName") String tripName,@FormParam("tripStartTime") String tripStartTime,
			@FormParam("tripEndTime") String tripEndTime,@FormParam("userLoginId") int userLoginId,@FormParam("parentId") int parentId)
	{
		String feeds  = null;
		try 
		{
			//System.out.println("SaveRailwayPatrolManTripTimeMaster------"+tripName+"--tripStartTime-"+tripStartTime+"---tripEndTime--"+tripEndTime);
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.SaveRailwayPatrolManTripTimeMaster(tripName,tripStartTime,tripEndTime,userLoginId,parentId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
		//System.out.println("SaveRailwayPatrolManTripTimeMaster-----"+feeds);
		return feeds;
	}
	
	@POST
	@Path("GetSectionNames")
	@Produces("application/json")
	public String GetSectionNames(@FormParam("parentId") int parentId)
	{

		String feeds  = null;
		try 
		{	
			////System.out.println("GetSectionNames------"+parentId);
			ArrayList<String> msgData = new ArrayList<String>();
			APIController handler= new APIController();
			msgData = handler.GetSectionNames(parentId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
//		System.out.println("GetSectionNames-----"+feeds);

		return feeds;
	}

	@POST
	@Path("SavePatrolManBeat")
	@Produces("application/json")
	public String SavePatrolManBeat(@FormParam("patrolmanBeatData") String patrolmanBeatData
			){
		String feeds  = null;
		try 
		{	
			
			//System.out.println("SavePatrolManBeat------"+patrolmanBeatData);
			if(patrolmanBeatData!=null)
			{
				
			
			ArrayList<RailwayPatrolManDTO> BeatInfoList=new ArrayList<>();
			org.json.JSONObject jodata=new org.json.JSONObject(patrolmanBeatData);
			//System.out.println("SavePatrolManBeat---jodata---"+jodata);

			//substring(1, patrolmanBeatData.length()-1));
			JSONArray joTripdata=jodata.getJSONArray("tripsInfo");
			for (int i=0;i<joTripdata.length();i++){
				org.json.JSONObject trip=joTripdata.getJSONObject(i);
				RailwayPatrolManDTO dto=new RailwayPatrolManDTO();
				dto.setStudentId(jodata.getInt("studentId"));
				dto.setSectionName(jodata.getString("sectionName"));
				dto.setUserLoginId(jodata.getInt("userLoginId"));
				dto.setKmStart(trip.getDouble("kmStart"));
				dto.setKmEnd(trip.getDouble("kmEnd"));
				dto.setFk_TripMasterId(trip.getInt("fk_TripMasterId"));
				dto.setTotalKmCover(dto.getKmEnd()-dto.getKmStart());
				dto.setSeasonId(Integer.parseInt(jodata.getString("seasonId")));
				BeatInfoList.add(dto);
				
			}
			
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.SavePatrolManBeat(BeatInfoList);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("SavePatrolManBeat-----"+feeds);
		return feeds;
	}
	
	@POST
	@Path("GetPatrolManExistingBeat")
	@Produces("application/json")
	public String GetPatrolManExistingBeat(@FormParam("parentId") int parentId,@FormParam("studentId") int studentId,
			@FormParam("beatId") int beatId,@FormParam("userLoginId") int userLoginId)
	{
		String feeds  = null;
		try 
		{
			ArrayList<RailwayPatrolManDTO> msgData=new ArrayList<RailwayPatrolManDTO>();
			APIController handler= new APIController();
			msgData = handler.GetPatrolManExistingBeat(parentId,studentId,beatId,userLoginId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GetPatrolManExistingBeat-----"+feeds);
		return feeds;
	}

	@POST
	@Path("UpdatePatrolManBeat")
	@Produces("application/json")
	public String UpdatePatrolManBeat(@FormParam("beatId") int beatId,@FormParam("studentId") int studentId,@FormParam("fk_TripMasterId") int fk_TripMasterId,
		@FormParam("kmStart") double kmStart,@FormParam("kmEnd") double kmEnd,@FormParam("totalKmCover") double totalKmCover,
		@FormParam("sectionName") String sectionName,@FormParam("userLoginId") int userLoginId,@FormParam("isApprove") Boolean isApprove
		,@FormParam("seasonId") int seasonId)
	{
		String feeds  = null;
		try 
		{
//			System.out.println("UpdatePatrolManBeat-----"+beatId);
			MessageObject msgData=new MessageObject();
			APIController handler= new APIController();
			msgData = handler.UpdatePatrolManBeat(beatId,studentId,fk_TripMasterId,kmStart,kmEnd,totalKmCover,sectionName,userLoginId,isApprove,seasonId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("UpdatePatrolManBeat-----"+feeds);
		return feeds;
	}

	@POST
	@Path("GetRailwayPetrolmanTripsMaster")
	@Produces("application/json")
	public String GetRailwayPetrolmanTripsMaster(@FormParam("parentId") int parentId)
	{
		String feeds  = null;
		try 
		{
			ArrayList<RailwayPatrolManDTO> msgData=new ArrayList<RailwayPatrolManDTO>();
			APIController handler= new APIController();
			msgData = handler.GetRailwayPetrolmanTripsMaster(parentId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GetRailwayPetrolmanTripsMaster-----"+feeds);
		return feeds;
	}

	@POST
	@Path("SaveRailwayDeptHierarchy")
	@Produces("application/json")
	public String SaveRailwayDeptHierarchy(@FormParam("deptId") int deptId,@FormParam("deptName") String deptName,@FormParam("emailId") String emailId
			,@FormParam("mobileNo") String mobileNo,@FormParam("deptParentId") int deptParentId,@FormParam("studentsNo") String studentsNo,
			@FormParam("parentId") int parentId,@FormParam("hirachyParentId") int hirachyParentId,@FormParam("userLoginId") int userLoginId){
		String feeds  = null;
		try 
		{	
			//System.out.println("SaveRailwayDeptHierarchy------"+deptId);
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.SaveRailwayDeptHierarchy(deptId,deptName,emailId,mobileNo,deptParentId,studentsNo,parentId,hirachyParentId,userLoginId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("SaveRailwayDeptHierarchy-----"+feeds);
		return feeds;
	}

	@POST
	@Path("GetRailwayDeptHierarchy")
	@Produces("application/json")
	public String GetRailwayDeptHierarchy(@FormParam("parentId") int parentId,@FormParam("hierarchyId") int hirachyId,@FormParam("userLoginId") int userLoginId)
	{
		String feeds  = null;
		try 
		{
			ArrayList<RailwayDeptHierarchyDTO> msgData = new ArrayList<RailwayDeptHierarchyDTO>();
			APIController handler= new APIController();
			msgData = handler.GetRailwayDeptHierarchy(parentId,hirachyId,userLoginId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GetRailwayDeptHierarchy-----"+feeds);
		return feeds;
	}

	@POST
	@Path("UpdateRailwayDeptHierarchy")
	@Produces("application/json")
	public String UpdateRailwayDeptHierarchyt(@FormParam("hierarchyId") int hierarchyId,@FormParam("deptId") int deptId,
			@FormParam("deptName") String Name,@FormParam("mobileNo") String mobileNo,@FormParam("emailId") String emailId,
			@FormParam("parentId") int parentId,@FormParam("studentsNo") String studentsNo,@FormParam("deptParentId") int deptParentId,
			@FormParam("userLoginId") int userLoginId,@FormParam("isApprove") Boolean isApprove)
	{
		String feeds  = null;
		try 
		{
			System.out.println("UpdateRailwayDeptHierarchy-----"+deptId);
			MessageObject msgData=new MessageObject();
			APIController handler= new APIController();
			msgData = handler.UpdateRailwayDeptHierarchy(hierarchyId,deptId,Name,emailId,mobileNo,
					deptParentId,studentsNo,parentId,userLoginId,isApprove);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("UpdateRailwayDeptHierarchy-----"+feeds);
		return feeds;
	}

	@GET
	@Path("GetDepartments")
	@Produces("application/json")
	public String GetDepartments()
	{
		String feeds  = null;
		try 
		{
			ArrayList<DepartmentsDTO> msgData = new ArrayList<DepartmentsDTO>();
			APIController handler= new APIController();
			msgData = handler.GetDepartments();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GetDepartments-----"+feeds);
		return feeds;
	}
	
	@POST
	@Path("GetDeviceCommand")
	@Produces("application/json")
	public String GetDeviceCommand(@FormParam("userLoginId") int userLoginId)
	{
		String feeds  = null;
		try 
		{
			ArrayList<DeviceCommandDTO> msgData = new ArrayList<DeviceCommandDTO>();
			APIController handler= new APIController();
			msgData = handler.GetDeviceCommand(userLoginId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GetDeviceCommand-----"+feeds);
		return feeds;
	}

	@POST
	@Path("GetIssueMasetrList")
	@Produces("application/json")
	public String GetIssueMasetrList(@FormParam("userLoginId") int userLoginId)
	{
		String feeds  = null;
		try 
		{
			ArrayList<DeviceIssueDTO> devIssDTO=new ArrayList<DeviceIssueDTO>();
			APIController handler= new APIController();
			devIssDTO = handler.GetIssueMasetrList(userLoginId);
			Gson gson = new Gson();
			feeds = gson.toJson(devIssDTO);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GetIssueMasetrList-----"+feeds);
		return feeds;
	}
	
	@POST
	@Path("SaveDeviceIssue")
	@Produces("application/json")
	public String SaveDeviceIssue(@FormParam("isseMasterId") int isseMasterId,@FormParam("contactPerson") String contactPerson,
			@FormParam("contactPersonMobNo") String contactPersonMobNo,@FormParam("issueStatus") int issueStatus ,
			@FormParam("priority") int priority,@FormParam("parentId") int parentId,@FormParam("studentId") int studentId,
			@FormParam("issueComment") String issueComment,@FormParam("userLoginId") int userLoginId,@FormParam("isDeviceOn") int isDeviceOn,
			@FormParam("isDeviceButtonOn") int isDeviceButtonOn,@FormParam("isBatteryOn") int isBatteryOn,@FormParam("isGSMOn") int isGSMOn,
			@FormParam("isGpsOn") int isGpsOn,@FormParam("isImeiSIMCorrect") int isImeiSIMCorrect,@FormParam("fileList") String fileList){
		String feeds  = null;
		try 
		{	
			//System.out.println("SaveDeviceIssue------"+isseMasterId);
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.SaveDeviceIssue(isseMasterId,contactPerson,contactPersonMobNo,issueStatus,priority,parentId,
					studentId,issueComment,userLoginId,isDeviceOn,isDeviceButtonOn,isBatteryOn,isImeiSIMCorrect,isGSMOn,isGpsOn,fileList);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
//		//System.out.println("SaveDeviceIssue-----"+feeds);
		return feeds;
	}
	
	@POST
	@Path("UpdateDeviceIssue")
	@Produces("application/json")
	public String UpdateDeviceIssue(@FormParam("issueId") int issueId,@FormParam("issueStatus") int issueStatus,
			@FormParam("priority") int priority,@FormParam("updatedBy") int updatedBy,@FormParam("issueComment") String issueComment,
			@FormParam("isDeviceOn") int isDeviceOn,@FormParam("isDeviceButtonOn") int isDeviceButtonOn,@FormParam("isBatteryOn") int isBatteryOn,
			@FormParam("isGSMOn") int isGSMOn,@FormParam("isGpsOn") int isGpsOn,@FormParam("isImeiSIMCorrect") int isImeiSIMCorrect,@FormParam("contactNo") String contactNo)

	{
		String feeds  = null;
		try 
		{
			//System.out.println("UpdateDeviceIssue-----"+issueId);
			MessageObject msgData=new MessageObject();
			APIController handler= new APIController();
			msgData = handler.UpdateDeviceIssue(issueId,issueStatus,priority,updatedBy,issueComment,isDeviceOn,isDeviceButtonOn,isBatteryOn,isImeiSIMCorrect,isGSMOn,isGpsOn,contactNo);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("UpdateDeviceIssue-----"+feeds);
		return feeds;
	}

	@POST
	@Path("GetIssueDetails")
	@Produces("application/json")
	public String GetIssueDetails(@FormParam("studentId") int studentId,@FormParam("issueId") int issueId,@FormParam("userLoginId") int userLoginId,@FormParam("startTime") long startTime,@FormParam("endTime") long endTime)
	{
		String feeds  = null;
		try 
		{
			ArrayList<DeviceIssueDTO> devIssDTO=new ArrayList<DeviceIssueDTO>();
			APIController handler= new APIController();
			devIssDTO = handler.GetIssueDetails(studentId,issueId,userLoginId,startTime,endTime);
			Gson gson = new Gson();
			feeds = gson.toJson(devIssDTO);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
//		System.out.println("GetIssueDetails-----"+feeds);
		return feeds;
	}
	
	@POST
	@Path("UpdateDeviceExchange")
	@Produces("application/json")
	public String UpdateDeviceExchange(@FormParam("parentId") int parentId,@FormParam("studentId1") int studentId1,
			@FormParam("studentId2") int studentId2,@FormParam("isDeviceSimExchange") Boolean isDeviceSimExchange,@FormParam("userLoginId") int userLoginId)	{
		String feeds  = null;
		try 
		{
			ArrayList<DeviceExchangeDTO> devExcDTO=new ArrayList<DeviceExchangeDTO>();
			APIController handler= new APIController();
			devExcDTO = handler.UpdateDeviceExchange(parentId,studentId1,studentId2,isDeviceSimExchange,userLoginId);
			Gson gson = new Gson();
			feeds = gson.toJson(devExcDTO);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("UpdateDeviceExchange-----"+feeds);
		return feeds;
	}
	
	@POST
	@Path("GetDeviceExchange")
	@Produces("application/json")
	public String GetDeviceExchange(@FormParam("parentId") int parentId,@FormParam("userLoginId") int userLoginId)
	{
		String feeds  = null;
		try 
		{
			ArrayList<DeviceExchangeDTO> ExcDTO=new ArrayList<DeviceExchangeDTO>();
			APIController handler= new APIController();
			ExcDTO = handler.GetDeviceExchange(parentId,userLoginId);
			Gson gson = new Gson();
			feeds = gson.toJson(ExcDTO);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GetDeviceExchange-----"+feeds);
		return feeds;
	}
	
	@POST
	@Path("UpdateNewDeviceIMEI")
	@Produces("application/json")
	public String UpdateNewDeviceIMEI(@FormParam("simNo") String simNo,@FormParam("parentId") int parentId,
			@FormParam("studentId") int studentId,@FormParam("imeiNo") String imeiNo,@FormParam("firstName") String firstName)	{
			String feeds  = null;
			try 
			{
//				System.out.println("UpdateNewDeviceIMEI-----"+simNo);
				MessageObject msgData=new MessageObject();
				APIController handler= new APIController();
				msgData = handler.UpdateNewDeviceIMEI(simNo,parentId,studentId,imeiNo,firstName);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
			} catch (Exception e)
			{
				e.printStackTrace();
				//System.out.println(e.getMessage());
			}
//			System.out.println("UpdateNewDeviceIMEI-----"+feeds);
			return feeds;
	}
	
	@POST
	@Path("UpdateWholeDeviceExchange")
	@Produces("application/json")
	public String UpdateWholeDeviceExchange(@FormParam("parentId") int parentId,@FormParam("studentId1") int studentId1,
			@FormParam("studentId2") int studentId2,@FormParam("userLoginId") int userLoginId)	{
		String feeds  = null;
		try 
		{
			ArrayList<DeviceExchangeDTO> devExcDTO=new ArrayList<DeviceExchangeDTO>();
			APIController handler= new APIController();
			devExcDTO = handler.UpdateWholeDeviceExchange(parentId,studentId1,studentId2,userLoginId);
			Gson gson = new Gson();
			feeds = gson.toJson(devExcDTO);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("UpdateWholeDeviceExchange-----"+feeds);
		return feeds;
	}

	@POST
	@Path("GetDeviceInfo")
	@Produces("application/json")
	public String GetDeviceInfo(@FormParam("studentId") String studentId,@FormParam("userLoginId") int userLoginId,@FormParam("issueId") int issueId,@FormParam("imeiNo") String imeiNo) throws IOException
	{
		String feeds  = null;
		try 
		{
			System.err.println("Students----------"+studentId);
			DeviceDTO devDTO = new DeviceDTO();
			APIController handler= new APIController();
			devDTO = handler.GetDeviceInfo(studentId,userLoginId,issueId,imeiNo);
			Gson gson = new Gson();
			feeds = gson.toJson(devDTO);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GetDeviceInfo----"+feeds);
		return feeds;
	}

	@POST
	@Path("GetDeviceCommandHistory")
	@Produces("application/json")
	public String GetDeviceCommandHistory(@FormParam("deviceId") String deviceId,@FormParam("startTime") long startTime,@FormParam("endTime") long endTime)
	{
		String feeds  = null;
		try 
		{
			System.err.println("History----------");
			ArrayList<DeviceCommandHistoryDTO> devHDTO = new ArrayList<>();
			APIController handler= new APIController();
			devHDTO = handler.GetDeviceCommandHistory(deviceId,startTime,endTime);
			Gson gson = new Gson();
			feeds = gson.toJson(devHDTO);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GetDeviceCommandHistory----"+feeds);
		return feeds;
	}

	@POST
	@Path("GetRDPSInfo")
	@Produces("application/json")
	public String GetRDPSInfo(@FormParam("lat") Float lat,@FormParam("lan") Float lan,@FormParam("parentId") int parentId)
	{
		String feeds  = null;
		try 
		{
			ArrayList<FeatureAddressDetailsDTO> FeaDTO=new ArrayList<FeatureAddressDetailsDTO>();
			APIController handler= new APIController();
			FeaDTO = handler.GetRDPSInfo(lat,lan,parentId);
			Gson gson = new Gson();
			feeds = gson.toJson(FeaDTO);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GetRDPSInfo-----"+feeds);
		return feeds;
	}

	@POST
	@Path("GenerateSupportMail")
	@Produces("application/json")
	public String GenerateSupportMail(@FormParam("toMail") String toMail,@FormParam("subject") String subject,
			@FormParam("message") String message,@FormParam("ccMail") String ccMail)
	{
		//System.out.println("GenerateSupportMail-----");
		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GenerateSupportMail(toMail,subject,message,ccMail);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GenerateSupportMail-----"+feeds);
		return feeds;
	}
	
	@GET
	@Path("GetIssueMail")
	@Produces("application/json")
	public String GetIssueMail()
	{
		String feeds  = null;
		try 
		{
			ArrayList<MailFormatDTO> msgData = new ArrayList<MailFormatDTO>();
			APIController handler= new APIController();
			msgData = handler.GetIssueMail();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
//		System.out.println("GetIssueMail-----"+feeds);
		return feeds;
	}

	@POST
	@Path("GetBatteryDetailPowerOnOffInfo")
	@Produces("application/json")
	public String GetBatteryDetailPowerOnOffInfo(@FormParam("imeiNo") String deviceId,@FormParam("StartDateTime") String startdate,
			@FormParam("EndDateTime") String enddate)
	{
		String feeds  = null;
		try 
		{
			System.err.println("GetBatteryDetailPowerOnOffInfo----------------"+deviceId);
			 ArrayList<dto.DeviceBatteryDTO>  msgData = new ArrayList<>();
			APIController handler= new APIController();
			msgData = handler.GetBatteryDetailPowerOnOffInfo(deviceId,Long.parseLong(startdate),Long.parseLong(enddate));
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GetBatteryDetailPowerOnOffInfo----"+feeds);
		return feeds;
	}

	@POST
	@Path("GetDevicePaymentType")
	@Produces("application/json")
	public String GetDevicePaymentType(@FormParam("parentId") int parentId)
	{
		String feeds  = null;
		try 
		{
			ArrayList<DevicePaymentDTO> msgData = new ArrayList<DevicePaymentDTO>();
			APIController handler= new APIController();
			msgData = handler.GetDevicePaymentType(parentId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GetDevicePaymentType-----"+feeds);
		return feeds;
	}

	@POST
	@Path("MultipleDevicePayment")
	@Produces("application/json")
	public String MultipleDevicePayment(@FormParam("studentId") String studentId,@FormParam("parentId") int parentId,
			@FormParam("paymentTypeId") int paymentTypeId,@FormParam("currentStatus") String currentStatus)
	{
		String feeds  = null;
		try 
		{
			//System.out.println("MultipleDevicePayment------"+studentId);
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.MultipleDevicePayment(studentId,parentId,paymentTypeId,currentStatus);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);	
			
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("MultipleDevicePayment-----"+feeds);
		return feeds;
	}

	@POST
	@Path("DeleteKeymanBeat")
	@Produces("application/json")
	public String DeleteKeymanBeat(@FormParam("beatId") int beatId,@FormParam("userLoginId") int userLoginId)
	{
		String feeds  = null;
		try 
		{
			//System.out.println("DeleteKeymanBeat-----"+beatId);
			MessageObject msgData=new MessageObject();
			APIController handler= new APIController();
			msgData = handler.DeleteKeymanBeat(beatId,userLoginId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
		//System.out.println("DeleteKeymanBeat-----"+feeds);
		return feeds;
	}
	
	@POST
	@Path("DeletePatrolManBeat")
	@Produces("application/json")
	public String DeletePatrolManBeat(@FormParam("beatId") int beatId,@FormParam("userLoginId") int userLoginId)
	{
		String feeds  = null;
		try 
		{
			//System.out.println("DeletePatrolManBeat-----"+beatId);
			MessageObject msgData=new MessageObject();
			APIController handler= new APIController();
			msgData = handler.DeletePatrolManBeat(beatId,userLoginId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
		//System.out.println("DeletePatrolManBeat-----"+feeds);
		return feeds;
	}
	
	
//	@POST
//	@Path("DeletePatrolManBeat")
//	@Produces("application/json")
//	public String UploadIssueFile(@FormParam("fileName") String fileName,
//			@FormParam("fileContent") String fileContent,@FormParam("userLoginId") int userLoginId)
//	{
//		String feeds  = null;
//		try 
//		{
//			System.out.println("DeletePatrolManBeat-----"+beatId);
//			MessageObject msgData=new MessageObject();
//			APIController handler= new APIController();
//			msgData = handler.DeletePatrolManBeat(beatId,userLoginId);
//			Gson gson = new Gson();
//			feeds = gson.toJson(msgData);
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//			//System.out.println(e.getMessage());
//		}
//		System.out.println("DeletePatrolManBeat-----"+feeds);
//		return feeds;
//	}
//	
	
	@POST
	@Path("UploadIssueFile")
	@Produces("application/json")
	public String UploadIssueFile(@FormParam("fileName") String fileName,@FormParam("file") String photofile,
			@FormParam("userId") int userId,@FormParam("fileExtension") String fileExtention)
	{ 
		String feeds  = null;
		try 
		{
              //put image on server
              
				String filenameDB =fileName.trim().replace(" ","_").replace("."+fileExtention,"")+"_"+System.currentTimeMillis()+"."+fileExtention;

			String fileURL=Common.ServerIssueFileUploadPath+filenameDB;
				byte dearr[] = Base64.decodeBase64(photofile.getBytes());
				FileOutputStream fos = new FileOutputStream(new File(fileURL)); 
				fos.write(dearr); 
				fos.close();
				
				
			IssueFileInfoDTO msgData=new IssueFileInfoDTO();
			APIController handler= new APIController();
			msgData = handler.UploadIssueFile(filenameDB,fileURL,userId);
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
	@Path("DeleteFileUpload")
	@Produces("application/json")
	public String DeleteFileUpload(@FormParam("fileId") int fileId,@FormParam("userLoginId") int userLoginId)
	{
		String feeds  = null;
		try 
		{
			//System.out.println("DeleteFileUpload-----"+fileId);
			MessageObject msgData=new MessageObject();
			APIController handler= new APIController();
			msgData = handler.DeleteFileUpload(fileId,userLoginId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
		//System.out.println("DeleteFileUpload-----"+feeds);
		return feeds;
	}

	@GET
	@Path("GetDeviceGatheringInfo")
	@Produces("application/json")
	public String GetDeviceGatheringInfo()
	{
		String feeds  = null;
		try 
		{
			ArrayList<DeviceInfoGatheringDTO> msgData = new ArrayList<DeviceInfoGatheringDTO>();
			APIController handler= new APIController();
			msgData = handler.GetDeviceGatheringInfo();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);	
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
//		//System.out.println("GetDeviceGatheringInfo-----"+feeds);
		return feeds;
	}


	@POST
	@Path("getAllStudentsWithConnectedStatus")
	@Produces("application/json")
	public String getAllStudentsWithConnectedStatus(@FormParam("parentId") String parentId)
	{
		
		//System.out.println("getAllStudentsWithConnectedStatus-----"+parentId);

		String feeds  = null;
		try 
		{
			ArrayList<DeviceDataDTO> msgData = new ArrayList<DeviceDataDTO>();
			APIController handler= new APIController();
			msgData = handler.getAllStudentsWithConnectedStatus(parentId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
	//	//System.out.println("getLoginDetails-----"+feeds);

		return feeds;
	}
	@POST
	@Path("GetDevicePaymentInfoForAdmin")
	@Produces("application/json")
	public String GetDevicePaymentInfoForAdmin(@FormParam("ParentId") int parentId)
	{
		
		//System.out.println("GetDevicePaymentInfoForAdmin-----"+parentId);

		String feeds  = null;
		try 
		{
			ArrayList<DevicePaymentInfoDetailsDTO> msgData = new ArrayList<DevicePaymentInfoDetailsDTO>();
			APIController handler= new APIController();
			msgData = handler.GetDevicePaymentInfoForAdmin(parentId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
		////System.out.println("getLoginDetails-----"+feeds);

		return feeds;
	}

	@POST
	@Path("GetPendingDeviceCommandHistory")
	@Produces("application/json")
	public String GetPendingDeviceCommandHistory(@FormParam("DeviceId") String deviceId,@FormParam("StartTime")long startTime,
			@FormParam("EndTime") long endTime)
	{
		String feeds  = null;
		try 
		{
			//System.out.println("GetPendingDeviceCommandHistory-----"+deviceId);
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GetPendingDeviceCommandHistory();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
		//System.out.println("----"+feeds);
		return feeds;
	}
	
	
	@POST
	@Path("UpsertStudentMasterSQL")
	@Produces("application/json")
	public String UpsertStudentMasterSQL()
	{
		String feeds  = null;
		try 
		{
			//System.out.println("UpsertStudenntMasterSQL-----");
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.UpsertStudentMasterSQL();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
		//System.out.println("----"+feeds);
		return feeds;
	}
	
	@POST
	@Path("UpsertRailwayKeymanBeatPathSQL")
	@Produces("application/json")
	public String UpsertRailwayKeymanBeatPathSQL()
	
	{
		String feeds  = null;
		try 
		{
			//System.out.println("UpsertRailwayKeymanBeatPathSQL-----");
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.UpsertRailwayKeymanBeatPathSQL();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
		//System.out.println("----"+feeds);
		return feeds;
	}
	
	
	

	@POST
	@Path("GetClientEnteredIssueDetailsForAdmin")
	@Produces("application/json")
	public String GetClientEnteredIssueDetailsForAdmin(@FormParam("studentId") int studentId,@FormParam("issueId") int issueId,@FormParam("userLoginId") int userLoginId,@FormParam("startTime") long startTime,@FormParam("endTime") long endTime)
	{
		String feeds  = null;
		try 
		{
			ArrayList<DeviceIssueDTO> devIssDTO=new ArrayList<DeviceIssueDTO>();
			APIController handler= new APIController();
			devIssDTO = handler.GetClientEnteredIssueDetailsForAdmin(studentId,issueId,userLoginId,startTime,endTime);
			Gson gson = new Gson();
			feeds = gson.toJson(devIssDTO);
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
//		//System.out.println("GetIssueDetails-----"+feeds);
		return feeds;
	}

	@POST
	@Path("UpsertRailwayPatrolManBeatPathSQL")
	@Produces("application/json")
	public String UpsertRailwayPatrolManBeatPathSQL()
	
	{
		String feeds  = null;
		try 
		{
			//System.out.println("UpsertRailwayPatrolManBeatPathSQL-----");
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.UpsertRailwayPatrolManBeatPathSQL();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
		//System.out.println("----"+feeds);
		return feeds;
	}
	
	
	
	@POST
	@Path("UpsertRailwayPatrolManBeatMasterPathSQL")
	@Produces("application/json")
	public String UpsertRailwayPatrolManBeatMasterPathSQL()
	
	{
		String feeds  = null;
		try 
		{
			//System.out.println("UpsertRailwayPatrolManBeatMasterPathSQL-----");
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.UpsertRailwayPatrolManBeatMasterPathSQL();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
		//System.out.println("----"+feeds);
		return feeds;
	}

	@POST
	@Path("SaveKeymanBeatInBulk")
	@Produces("application/json")
	public String SaveKeymanBeatInBulk(@FormParam("KeymanBeatData") String KeymanBeatData)
	{
		String feeds  = null;
		try 
		{
			//System.out.println("SaveKeymanBeatInBulk------"+KeymanBeatData);
			

			if(KeymanBeatData!=null)
			{
				
			
			ArrayList<RailwayKeymanDTO> BeatInfoList=new ArrayList<>();
			org.json.JSONObject jodata=new org.json.JSONObject(KeymanBeatData);
			//System.out.println("KeymanBeatData---jodata---"+jodata);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:MM");

			//substring(1, patrolmanBeatData.length()-1));
			JSONArray joTripdata=jodata.getJSONArray("keymenformArray");
			for (int i=0;i<joTripdata.length();i++){
				org.json.JSONObject trip=joTripdata.getJSONObject(i);
				RailwayKeymanDTO dto=new RailwayKeymanDTO();
				dto.setStudentId(trip.getInt("studentId"));
				dto.setSectionName(trip.getString("sectionName"));
				dto.setUserLoginId(jodata.getInt("userLoginId"));
				dto.setKmStart(trip.getDouble("kmStart"));
				dto.setKmEnd(trip.getDouble("kmEnd"));
				dto.setParentId(jodata.getInt("parentId"));
				dto.setNameWhoInsert(jodata.getString("name"));
				dto.setMobWhoInsert(jodata.getString("contactNo"));
				dto.setEmailWhoInsert(jodata.getString("email"));
				dto.setStartTime( (int) ((sdf.parse(jodata.getString("start_time"))).getTime()/1000));
				dto.setEndTime((int) ((sdf.parse(jodata.getString("end_time"))).getTime()/1000));
				
				
			
				BeatInfoList.add(dto);
			}
			
			
			
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.SaveKeymanBeatInBulk(BeatInfoList);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
		//System.out.println("SaveKeymanBeat-----"+feeds);
		return feeds;
	}

	@POST
	@Path("GetKeymanExistingBeatByParent")
	@Produces("application/json")
	public String GetKeymanExistingBeatByParent(@FormParam("parentId") int parentId)
	{
		String feeds  = null;
		try 
		{
			ArrayList<RailwayKeymanDTO> msgData = new ArrayList<RailwayKeymanDTO>();
			APIController handler= new APIController();
			msgData = handler.GetKeymanExistingBeatByParent(parentId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
		//System.out.println("GetKeymanExistingBeatByParent-----"+feeds);
		return feeds;
	}	

	@POST
	@Path("UpdateRailwayKeymanBeatPathCopyApprove")
	@Produces("application/json")
	public String UpdateRailwayKeymanBeatPathCopyApprove(@FormParam("beatId") int beatId,@FormParam("userLoginId") int userLoginId)	{
		String feeds  = null;
		try 
		{
			MessageObject devExcDTO= new MessageObject();
			APIController handler= new APIController();
			devExcDTO = handler.UpdateRailwayKeymanBeatPathCopyApprove(beatId,userLoginId);
			Gson gson = new Gson();
			feeds = gson.toJson(devExcDTO);
		} catch (Exception e)
		{
			e.printStackTrace();
			////System.out.println(e.getMessage());
		}
		//System.out.println("UpdateRailwayKeymanBeatPathCopyApprove-----"+feeds);
		return feeds;
	}

	@POST
	@Path("RemoveDeviceAPI")
	@Produces("application/json")
	public String RemoveDeviceAPI(@FormParam("deviceId1") String deviceId1,@FormParam("userLoginId") int userLoginId)
	{
		String feeds  = null;
		try 
		{
			System.out.println("RemoveDevice-----"+deviceId1);
			MessageObject msgData=new MessageObject();
			APIController handler= new APIController();
			msgData = handler.RemoveDeviceAPI(deviceId1,userLoginId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		System.out.println("RemoveDeviceAPI-----"+feeds);
		return feeds;
	}
	
	@POST
	@Path("DeviceUnRegisterAPI")
	@Produces("application/json")
	public String DeviceRegisterAPI(@FormParam("imeiNo") String imeiNo,@FormParam("userLoginId") int userLoginId)	{
			String feeds  = null;
			try 
			{
				//System.out.println("DeviceUnRegisterAPI-----"+imeiNo);
				MessageObject msgData=new MessageObject();
				APIController handler= new APIController();
				msgData = handler.DeviceUnRegisterAPI(imeiNo,userLoginId);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
			} catch (Exception e)
			{
				e.printStackTrace();
				////System.out.println(e.getMessage());
			}
			//System.out.println("DeviceUnRegisterAPI-----"+feeds);
			return feeds;
	}

	
	@POST
	@Path("GetAddDeviceDropDownInfo")
	@Produces("application/json")
	public String GetAddDeviceDropDownInfo(@FormParam("userLoginId") int userLoginId)
	{
		String feeds  = null;
		try 
		{
			AddDeviceDropDownInfo msgData=new AddDeviceDropDownInfo();
			APIController handler= new APIController();
			msgData = handler.GetAddDeviceDropDownInfo(userLoginId);
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
	@Path("AddNewDevice")
	@Produces("application/json")
	public String AddNewDevice(@FormParam("StudentID") int StudentID,@FormParam("ParentId") int ParentId,@FormParam("FirstName") String FirstName,
			@FormParam("LastName") String LastName,@FormParam("Gender") String Gender,@FormParam("DeviceID") String DeviceID,
			@FormParam("Type") String Type,@FormParam("DeviceType") int DeviceType,@FormParam("DeviceSimNumber") String DeviceSimNumber,
			@FormParam("ActivationDate") String ActivationDate,@FormParam("PlanTypeID") int PlanTypeID,@FormParam("PaymentMode") int PaymentMode,
			@FormParam("CreditName") String CreditName,@FormParam("PaymentDate") String PaymentDate,@FormParam("TransactionID") String TransactionID,
			@FormParam("PayAmount") Double PayAmount,@FormParam("registerOutParameter") int registerOutParameter)	{
		String feeds  = null;
		try 
		{
			MessageObject msgData=new MessageObject();
			APIController handler= new APIController();
			msgData = handler.AddNewDevice(StudentID,ParentId,FirstName,LastName,Gender,DeviceID,Type,DeviceType,DeviceSimNumber,
					ActivationDate,PlanTypeID,PaymentMode,CreditName,PaymentDate,TransactionID,PayAmount,registerOutParameter);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		System.out.println("AddNewDevice-----"+feeds);
		return feeds;
	}
	
	@POST
	@Path("AddBulkDevices")
	@Produces("application/json")
	public String AddBulkDevices(@FormParam("ActivationDate") String ActivationDate,@FormParam("DeviceType") int DeviceType,
			@FormParam("PayAmount") Double PayAmount,@FormParam("PaymentDate") String PaymentDate,@FormParam("PaymentMode") int PaymentMode,
			@FormParam("PlanTypeID") int PlanTypeID,@FormParam("TransactionID") String TransactionID,@FormParam("Type") String Type,
			@FormParam("BulkData") String BulkData,@FormParam("CreditName") String CreditName,@FormParam("ParentId") int ParentId)	{
		String feeds  = null;
		try 
		{
			MessageObject msgData=new MessageObject();
			APIController handler= new APIController();
			msgData = handler.AddBulkDevices(ActivationDate,DeviceType,PayAmount,PaymentDate,
					PaymentMode,PlanTypeID,TransactionID,Type,BulkData,CreditName,ParentId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		System.out.println("AddBulkDevices-----"+feeds);
		return feeds;
	}
}

