package webservices;

import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import model.APIController;

import com.google.gson.Gson;

import dao.DeviceOnOffInfoDto;
import dto.DailyReportSummaryWithStoppageDTO;
import dto.MessageObject;
import dto.ReportSummeryDTO;
@Path("ReportAPI")

public class ReportAPI {
	
	@POST
	@Path("GenerateExceptionReportKeyManBeatPathWholeDay")
	@Produces("application/json")
	public String GenerateExceptionReportKeyManBeatPathWholeDay(@QueryParam("ParentId") String parentId,@QueryParam("Day") int day,
		@QueryParam("isSendMail") Boolean isSendMail,@QueryParam("startTime1") long startTime1,@QueryParam("startTime2") long startTime2,
			@QueryParam("endTime1") long endTime1,@QueryParam("endTime2") long endTime2)
	{
		
		
		
		//System.out.println("GenerateExceptionReportKeyManBeatPathWholeDay-----");

		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GenerateExceptionReportKeyManBeatPathWholeDay(parentId,day,isSendMail,startTime1,startTime2,endTime1,endTime2);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GenerateExceptionReportKeyManBeatPathWholeDay-----"+feeds);

		return feeds;
	}

	@POST
	@Path("GenerateExceptionReportKeyManBeatPathWholeDayNewLogic")
	@Produces("application/json")
	public String GenerateExceptionReportKeyManBeatPathWholeDayNewLogic(@QueryParam("ParentId") String parentId,@QueryParam("Day") int day,
		@QueryParam("isSendMail") Boolean isSendMail,@QueryParam("startTime1") long startTime1,@QueryParam("startTime2") long startTime2,
			@QueryParam("endTime1") long endTime1,@QueryParam("endTime2") long endTime2,@QueryParam("exceptionSummary") Boolean exceptionSummary,
			@QueryParam("distancetoleranceInKm") Double distancetoleranceInKm)
	{
		
		
		
		//System.out.println("GenerateExceptionReportKeyManBeatPathWholeDayNewLogic-----");

		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GenerateExceptionReportKeyManBeatPathWholeDayNewLogic(parentId,day,
					isSendMail,startTime1,startTime2,endTime1,
					endTime2,exceptionSummary,distancetoleranceInKm);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GenerateExceptionReportKeyManBeatPathWholeDay-----"+feeds);

		return feeds;
	}
	@POST
	@Path("GenerateExceptionReportPatrolManBeatPathAsKeyman")
	@Produces("application/json")
	public String GenerateExceptionReportPatrolManBeatPathAsKeyman(@QueryParam("ParentId") String parentId,
			@QueryParam("Day") int day,@QueryParam("isSendMail") Boolean isSendMail,@QueryParam("dutyStartTime") int dutyStartTime,
			@QueryParam("dutyEndTime") int dutyEndTime)
	{
		
		
		
		//System.out.println("GenerateExceptionReportPatrolManBeatPathAsKeyman-----");

		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GenerateExceptionReportPatrolManBeatPathAsKeyman(parentId,day,isSendMail,dutyStartTime,dutyEndTime);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GenerateExceptionReportPatrolManBeatPathAsKeyman-----"+feeds);

		return feeds;
	}
	@POST
	@Path("GenerateExceptionReportPatrolManBeatPathAsKeymanNewLogic")
	@Produces("application/json")
	public String GenerateExceptionReportPatrolManBeatPathAsKeymanNewLogic(@QueryParam("ParentId") String parentId,@QueryParam("Day") int day,
		@QueryParam("isSendMail") Boolean isSendMail,@QueryParam("startTime1") long startTime1,@QueryParam("startTime2") long startTime2,
			@QueryParam("endTime1") long endTime1,@QueryParam("endTime2") long endTime2)
	{
		
		
		
		//System.out.println("GenerateExceptionReportPatrolManBeatPathAsKeymanNewLogic-----");

		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GenerateExceptionReportPatrolManBeatPathAsKeymanNewLogic(parentId,day,isSendMail,startTime1,startTime2,endTime1,endTime2);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GenerateExceptionReportPatrolManBeatPathAsKeymanNewLogic-----"+feeds);

		return feeds;
	}
	@POST
	@Path("GenerateZoneWiseReport")
	@Produces("application/json")
	public String GenerateZoneWiseReport(@QueryParam("ZoneId") String zoneId,
			@QueryParam("Day") int day,@QueryParam("isSendMail") Boolean isSendMail)
	{
		
		
		
		//System.out.println("GenerateZoneWiseReport-----");

		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GenerateZoneWiseReport(zoneId,day,isSendMail);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GenerateZoneWiseReport-----"+feeds);

		return feeds;
	}


	@POST
	@Path("GenerateExceptionReportKeyManBeatPathWholeDayByBeatLatLang")
	@Produces("application/json")
	public String GenerateExceptionReportKeyManBeatPathWholeDayByBeatLatLang(@QueryParam("ParentId") String parentId,@QueryParam("Day") int day,
		@QueryParam("isSendMail") Boolean isSendMail,@QueryParam("startTime1") long startTime1,@QueryParam("startTime2") long startTime2,
			@QueryParam("endTime1") long endTime1,@QueryParam("endTime2") long endTime2)
	{
		
		
		
		//System.out.println("GenerateExceptionReportKeyManBeatPathWholeDayByBeatLatLang-----");

		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GenerateExceptionReportKeyManBeatPathWholeDay(parentId,day,isSendMail,startTime1,startTime2,endTime1,endTime2);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GenerateExceptionReportKeyManBeatPathWholeDay-----"+feeds);

		return feeds;
	}

	@POST
	@Path("GenerateExceptionReportUSFD")
	@Produces("application/json")
	public String GenerateExceptionReportUSFD(@QueryParam("ParentId") String parentId,@QueryParam("Day") int day,
	@QueryParam("isSendMail") Boolean isSendMail,@QueryParam("RoleId") int RoleId,@QueryParam("DeviceType") String devicetype)
	{
		
		//System.out.println("GenerateExceptionReportUSFD-----");

		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GenerateExceptionReportUSFD(parentId,day,isSendMail,RoleId,devicetype);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GenerateExceptionReportUSFD-----"+feeds);

		return feeds;
	}

	@POST
	@Path("GenerateExceptionReportKotaKeyMan")
	@Produces("application/json")
	public String GenerateExceptionReportKotaKeyMan(@QueryParam("ParentId") String parentId,@QueryParam("Day") int day,
			@QueryParam("isSendMail") Boolean isSendMail,@QueryParam("startTime1") long startTime1,@QueryParam("startTime2") long startTime2,
				@QueryParam("endTime1") long endTime1,@QueryParam("endTime2") long endTime2,
				@QueryParam("exceptionSummary") Boolean exceptionSummary,
				@QueryParam("distancetoleranceInKm") Double distancetoleranceInKm)
		{
	
		
		//System.out.println("GenerateExceptionReportKotaKeyMan-----");

		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GenerateExceptionReportKotaKeyMan(parentId,day,
					isSendMail,startTime1,startTime2,endTime1,
					endTime2,exceptionSummary,distancetoleranceInKm);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GenerateExceptionReportKotaKeyMan-----"+feeds);

		return feeds;
	}
	
	@POST
	@Path("GenerateExceptionReportPetrolmanBeatPathNewLogic")
	@Produces("application/json")
	public String GenerateExceptionReportPetrolmanBeatPathNewLogic(@QueryParam("ParentId") String parentId
			,@QueryParam("Day") int day,@QueryParam("isSendMail") Boolean isSendMail,@QueryParam("seasonId") int seasonId)
	{
		
		
		
		//System.out.println("GenerateExceptionReportPetrolmanBeatPathNewLogic-----");

		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GenerateExceptionReportPetrolmanBeatPathNewLogic(parentId,day,isSendMail,seasonId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GenerateExceptionReportPetrolmanBeatPath-----"+feeds);

		return feeds;
	}
	
	@POST
	@Path("GenerateExceptionForGateMitra")
	@Produces("application/json")
	public String GenerateExceptionForGateMitra(@QueryParam("ParentId") String parentId,@QueryParam("Day") int day,
			@QueryParam("isSendMail") Boolean isSendMail,@QueryParam("startTime1") long startTime1,@QueryParam("startTime2") long startTime2,
				@QueryParam("endTime1") long endTime1,@QueryParam("endTime2") long endTime2,
				@QueryParam("NameStartWith") String nameStartWith,
				@QueryParam("distancetoleranceInKm") Double distancetoleranceInKm)
		{
	
		
		//System.out.println("GenerateExceptionForGateMitra-----");

		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GenerateExceptionForGateMitra(parentId,day,
					isSendMail,startTime1,startTime2,endTime1,
					endTime2,nameStartWith,distancetoleranceInKm);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GenerateExceptionReportKotaKeyMan-----"+feeds);

		return feeds;
	}
	
	
	
	
	
	@POST
	@Path("DumpLocationDataToSqlThroughCSV")
	@Produces("application/json")
	public String DumpLocationDataToSqlThroughCSV(@QueryParam("Day") int day,
			@QueryParam("startTime") long startTime
				,@QueryParam("endTime") long endTime,@QueryParam("isDumpMongoDbToCsv") boolean isDumpMOngoDbToCsv,
				@QueryParam("isCsvToSql") boolean isCsvToSql,@QueryParam("isMappedRdps") boolean isMappedRdps)
		{
	
		
		//System.out.println("DumpLocationDataToSqlThroughCSV-----");

		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.DumpLocationDataToSqlThroughCSV(day,startTime,endTime,isDumpMOngoDbToCsv
,isCsvToSql,isMappedRdps);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
//		//System.out.println("GenerateExceptionReportKotaKeyMan-----"+feeds);

		return feeds;
	}
	@POST
	@Path("GenerateExceptionReportKotaKeyManFromRdpsMapping")
	@Produces("application/json")
	public String GenerateExceptionReportKotaKeyManFromRdpsMapping(@QueryParam("ParentId") String parentId,@QueryParam("Day") int day,
			@QueryParam("isSendMail") Boolean isSendMail,@QueryParam("startTime1") long startTime1,@QueryParam("startTime2") long startTime2,
				@QueryParam("endTime1") long endTime1,@QueryParam("endTime2") long endTime2,
				@QueryParam("exceptionSummary") Boolean exceptionSummary,
				@QueryParam("distancetoleranceInKm") Double distancetoleranceInKm)
		{
	
		
		//System.out.println("GenerateExceptionReportKotaKeyManFromRdpsMapping-----");

		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GenerateExceptionReportKotaKeyManFromRdpsMapping(parentId,day,
					isSendMail,startTime1,startTime2,endTime1,
					endTime2,exceptionSummary,distancetoleranceInKm);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GenerateExceptionReportKotaKeyManFromRdpsMapping-----"+feeds);

		return feeds;
	}
	
	@POST
	@Path("GenerateSaveDailySummaryOfDeviceByParentIdWithCursorMappedRdps")
	@Produces("application/json")
	public String GenerateSaveDailySummaryOfDeviceByParentIdWithCursorMappedRdps(@QueryParam("ParentId") String parentId,@QueryParam("Day") int day)
		{
	
		
		//System.out.println("GenerateSaveDailySummaryOfDeviceByParentIdWithCursorMappedRdps-----");

		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GenerateSaveDailySummaryOfDeviceByParentIdWithCursorMappedRdps(parentId,day);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GenerateExceptionReportKotaKeyManFromRdpsMapping-----"+feeds);

		return feeds;
	}
	@POST
	@Path("GetReportSummeryInfo")
	@Produces("application/json")
	public String GetReportSummeryInfo(@FormParam("parentId") int parentId,@FormParam("timeStamp") long timeStamp,
			@FormParam("roleId") int roleId)
	{
		String feeds  = null;
		try 
		{
			ArrayList<DailyReportSummaryWithStoppageDTO> msgData = new ArrayList<DailyReportSummaryWithStoppageDTO>();
			APIController handler= new APIController();
			msgData = handler.GetReportSummeryInfo(parentId,timeStamp,roleId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
//		//System.out.println("GetReportSummeryInfo-----"+feeds);
		return feeds;
	}

	@POST
	@Path("DeviceONOffStatusAPI")
	@Produces("application/json")
	public String DeviceONOffStatusAPI(@FormParam("StartDateTime") long startdate,@FormParam("ParentId") int parentId)
	{
		String feeds  = null;
		try 
		{
			//j=0;
			ArrayList<DeviceOnOffInfoDto>  msgData = new ArrayList<DeviceOnOffInfoDto>();
			APIController handler= new APIController();
			msgData=handler.DeviceONOffStatusAPI(startdate,parentId);
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
	@Path("GenerateExceptionReportKeyManBeatFromReportSummary")
	@Produces("application/json")
	public String GenerateExceptionReportKeyManBeatFromReportSummary(@QueryParam("ParentId") String parentId,@QueryParam("Day") int day,
			@QueryParam("isSendMail") Boolean isSendMail,@QueryParam("startTime1") long startTime1,@QueryParam("startTime2") long startTime2,
				@QueryParam("endTime1") long endTime1,@QueryParam("endTime2") long endTime2,
				@QueryParam("exceptionSummary") Boolean exceptionSummary,
				@QueryParam("distancetoleranceInKm") Double distancetoleranceInKm)
		{
	
		
		//System.out.println("GenerateExceptionReportKeyManBeatFromReportSummary-----");

		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GenerateExceptionReportKeyManBeatFromReportSummary(parentId,day,
					isSendMail,startTime1,startTime2,endTime1,
					endTime2,exceptionSummary,distancetoleranceInKm);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GenerateExceptionReportKeyManBeatFromReportSummary-----"+feeds);

		return feeds;
	}
	
	@POST
	@Path("GetNotificationForPaymentSubscriptionAPI")
	@Produces("application/json")
	public String GetDeviceBeat()
	{
		String feeds  = null;
		try 
		{
			MessageObject msgData = new MessageObject();
			APIController handler= new APIController();
			msgData = handler.GetNotificationForPaymentSubscriptionAPI();
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		//System.out.println("GetNotificationForPaymentSubscriptionAPI-----"+feeds);
		return feeds;
	}

	//Get Jabalpur Patrolman report GetPatrolmanReportSummeryInfo 

	@POST
	@Path("GetDailySummaryOfDeviceFromReportSummaryOfPatrolMan")
	@Produces("application/json")
	public String GetDailySummaryOfDeviceFromReportSummaryOfPatrolMan(@FormParam("parentId") int parentId,@FormParam("timeStamp") long timeStamp,@FormParam("roleId") int roleId)
	{
		String feeds  = null;
		try 
		{
			ArrayList<DailyReportSummaryWithStoppageDTO> msgData = new ArrayList<DailyReportSummaryWithStoppageDTO>();
			APIController handler= new APIController();
			msgData = handler.GetDailySummaryOfDeviceFromReportSummaryOfPatrolMan(parentId,timeStamp,roleId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
//		//System.out.println("GetReportSummeryInfo-----"+feeds);
		return feeds;
	}

}

