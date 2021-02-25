package webservices;

import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import model.APIController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dto.DeactiveDeviceListDTO;
import dto.GeofenceDTO;
import dto.ListDeviceNotconnectedDTO;
import dto.LocationDTO;
import dto.MessageObject;
import dto.StationaryPurchaseItemInfo;
import dto.StationarySpinnerDTO;
import dto.TripInfoDto;

@Path("DeviceDebug")

public class DeviceDebug {

	//login service
		@POST
		@Path("DeviceRegisterStatus")
		@Produces("application/json")
		public String DeviceRegisterStatus(@FormParam("DeviceImieNo") String device)
		{
			String feeds  = null;
			try 
			{

				MessageObject msgData = null;
				APIController handler= new APIController();
				msgData = handler.DeviceRegisterStatus(device);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);

			
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			System.out.println("DeviceRegisterStatus  of u respo-----"+feeds);

			return feeds ;
		}


			@POST
			@Path("CheckDeviceIsConnectToserver")
			@Produces("application/json")
			public String CheckDeviceIsConnectToserver(@FormParam("DeviceImieNo") String device)
			{
				String feeds  = null;
				try 
				{

					MessageObject msgData = null;
					APIController handler= new APIController();
					msgData = handler.CheckDeviceIsConnectToserver(device);
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
			@Path("CheckDeviceLatestLocation")
			@Produces("application/json")
			public String CheckDeviceLatestLocation(@FormParam("DeviceImieNo") String device)
			{
				String feeds  = null;
				try 
				{

					ArrayList<LocationDTO> msgData = null;
					APIController handler= new APIController();
					msgData = handler.CheckDeviceLatestLocation(device);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
					
					
				} catch (Exception e)
				{
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
				System.out.println("CheckDeviceLatestLocation  of u respo-----"+feeds);

				return feeds;
			}
			
			


			@POST
			@Path("CheckTripReportGenerteOnserver")
			@Produces("application/json")
			public String CheckTripReportGenerteOnserver(@FormParam("DeviceImieNo") String device,@FormParam("StartDateTime") String startdate
					,@FormParam("EndDateTime") String enddate)
			{
				String feeds  = null;
				try 
				{
					//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
					ArrayList<TripInfoDto>  msgData =new ArrayList<>();

					APIController handler= new APIController();
					msgData=handler.CheckTripReportGenerteOnserver(device,startdate,enddate);
					Gson gson = new Gson();
					feeds = gson.toJson(msgData);
					
				
					
				} catch (Exception e)
				{
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
				System.out.println(feeds);

				return feeds;
			}
			
			
			@POST
			@Path("CheckGeofence")
			@Produces("application/json")
			public String CheckGeofence(@FormParam("DeviceImieNo") String device)
			{
				String feeds  = null;
				try 
				{

					ArrayList<GeofenceDTO> msgData = null;
					APIController handler= new APIController();
					msgData = handler.CheckGeofence(device);
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
			@Path("ClearTrackingPids")
			@Produces("application/json")
			public String ClearTrackingPids(@FormParam("DeviceImieNo") String device)
			{
				String feeds  = null;
				try 
				{

					MessageObject msgData = null;
					APIController handler= new APIController();
					msgData = handler.ClearTrackingPids(device);
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
			@Path("TrackLog")
			@Produces("application/json")
			public String TrackLog(@FormParam("ReportName") String name)
			{
				String feeds  = null;
				try 
				{

					MessageObject msgData = null;
					APIController handler= new APIController();
					msgData = handler.TrackLog(name);
					Gson gson =new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
					feeds = gson.toJson(msgData);
					
					
				} catch (Exception e)
				{
					e.printStackTrace();
					System.out.println(e.getMessage());
				}

				return feeds;
			}
			
			
			@POST
			@Path("DeleteLogfile")
			@Produces("application/json")
			public String DeleteLogfile(@FormParam("ReportName") String name)
			{
				String feeds  = null;
				try 
				{

					MessageObject msgData = null;
					APIController handler= new APIController();
					msgData = handler.DeleteLogfile(name);
					Gson gson =new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
					feeds = gson.toJson(msgData);
					
					
				} catch (Exception e)
				{
					e.printStackTrace();
					System.out.println(e.getMessage());
				}

				return feeds;
			}
			
			
			   @POST
			   	@Path("GenerateDevicePendingTripReport")
			   	@Produces("application/json")
			   	public String GenerateDevicePendingTripReport(@FormParam("StartDateTime") String startdate
			 			,@FormParam("EndDateTime") String enddate,@FormParam("DeviceImieNo") String Device)
			   	{
			   		String feeds  = null;
			   		try 
			   		{
			   			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			   			MessageObject msgData =new MessageObject();

			   			APIController handler= new APIController();
			   			msgData=handler.GenerateDevicePendingTripReport(startdate,enddate,Device);
			   			Gson gson = new Gson();
			   			feeds = gson.toJson(msgData);
			   			
			   		System.err.println("GenerateDevicePendingTripReport-----"+feeds);
			   			
			   		} catch (Exception e)
			   		{
			   			e.printStackTrace();
			   			System.out.println(e.getMessage());
			   		}

			   		return feeds;
			   	}
			    

			   @POST
			   	@Path("GetListOfDeviceNotConnected")
			   	@Produces("application/json")
			   	public String GetListOfDeviceNotConnected()
			   	{
			   		String feeds  = null;
			   		try 
			   		{
			   			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
			   			ListDeviceNotconnectedDTO msgData =new ListDeviceNotconnectedDTO();

			   			APIController handler= new APIController();
			   			msgData=handler.GetListOfDeviceNotConnected();
			   			Gson gson = new Gson();
			   			feeds = gson.toJson(msgData);
			   			
			   		System.err.println("GetListOfDeviceNotConnected-----"+feeds);
			   			
			   		} catch (Exception e)
			   		{
			   			e.printStackTrace();
			   			System.out.println(e.getMessage());
			   		}

			   		return feeds;
			   	}
			   
			   
			   @POST
			   	@Path("GenerateDeactiveDeviceList")
			   	@Produces("application/json")
			   	public String GenerateDeactiveDeviceList()
			   	{
			   		String feeds  = null;
			   		try 
			   		{
			   			DeactiveDeviceListDTO msgData =new DeactiveDeviceListDTO();

			   			APIController handler= new APIController();
			   			msgData=handler.GenerateDeactiveDeviceList();
			   			Gson gson = new Gson();
			   			feeds = gson.toJson(msgData);
			   			
			   		System.err.println("GetListOfDeviceNotConnected-----"+feeds);
			   			
			   		} catch (Exception e)
			   		{
			   			e.printStackTrace();
			   			System.out.println(e.getMessage());
			   		}

			   		return feeds;
			   	}
			   
			   @POST
			   	@Path("GetDeactiveDeviceList")
			   	@Produces("application/json")
			   	public String GetDeactiveDeviceList()
			   	{
			   		String feeds  = null;
			   		try 
			   		{
			   			DeactiveDeviceListDTO msgData =new DeactiveDeviceListDTO();

			   			APIController handler= new APIController();
			   			msgData=handler.GetDeactiveDeviceList();
			   			Gson gson = new Gson();
			   			feeds = gson.toJson(msgData);
			   			
			   		System.err.println("GetListOfDeviceNotConnected-----"+feeds);
			   			
			   		} catch (Exception e)
			   		{
			   			e.printStackTrace();
			   			System.out.println(e.getMessage());
			   		}

			   		return feeds;
			   	}
				@POST
				@Path("SendMsg_GetFetureExpireData")
				@Produces("application/json")
				public String SendMsg_GetFetureExpireData(){
					String feeds  = null;
					try 
					{

						
						MessageObject msgData = null;
						APIController handler= new APIController();
						msgData = handler.SendMsg_GetFetureExpireData();
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
				   	@Path("Generate_Idcard_DevicePendingTripReport")
				   	@Produces("application/json")
				   	public String Generate_Idcard_DevicePendingTripReport(@FormParam("StartDateTime") String startdate
				 			,@FormParam("EndDateTime") String enddate,@FormParam("DeviceImieNo") String Device)
				   	{
				   		String feeds  = null;
				   		try 
				   		{
				   			//ArrayList<MilageDTO> msgData = new ArrayList<MilageDTO>();
				   			MessageObject msgData =new MessageObject();
				   			
				   			APIController handler= new APIController();
				   			msgData=handler.Generate_Idcard_DevicePendingTripReport(startdate,enddate,Device);
				   			Gson gson = new Gson();
				   			feeds = gson.toJson(msgData);
				   			
				   		System.err.println("GenerateDevicePendingTripReport-----"+feeds);
				   			
				   		} catch (Exception e)
				   		{
				   			e.printStackTrace();
				   			System.out.println(e.getMessage());
				   		}

				   		return feeds;
				   	}
				    
			    
}
