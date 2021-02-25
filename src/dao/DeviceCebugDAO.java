package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.json.JsonObject;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.google.gson.JsonArray;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

import dto.DeactiveDeviceListDTO;
import dto.DeviceInfoDto;
import dto.GeofenceDTO;
import dto.ListDeviceNotconnectedDTO;
import dto.LocationDTO;
import dto.MessageObject;
import dto.TripInfoDto;
import dto.UserModuleDTO;

public class DeviceCebugDAO {
	private double HistoryDistance=0;
	private int j=0;
	private int totalCount=0;
	private DBObject dbObject1=null;
	private DBObject dbObject2;
	 private Calendar  calendar = Calendar.getInstance();
;
	    private int year, month, day;
	public MessageObject DeviceRegisterStatus(DB mongoconnection, String device) {


		MessageObject msgobj=new MessageObject();


		try{

			DBCollection table = mongoconnection.getCollection(Common.TABLE_DEVICE);  		
			BasicDBObject device_whereQuery = new BasicDBObject("device", Long.parseLong(device));
			
			DBCursor cursor = table.find(device_whereQuery);
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			System.out.println("Cursor Count of u-----"+cursor.size()+"  "+device_whereQuery);
			long total=cursor.size();
		int Triplocationcount=0;

				if(cursor.size()!=0) 
				{	
				System.err.println("*-----COunt---"+cursor.size());
				 
				msgobj.setError("false");
				msgobj.setMessage("Device added successfully.Database contain "+total+"  entries.");
				}else {
					msgobj.setError("true");
					msgobj.setMessage("Device not added successfully.");
				}

		}catch(Exception e){

			e.printStackTrace();
			msgobj.setError("true");
			msgobj.setMessage("Device not added successfully due to "+e.getMessage());
		}finally{

		}
		return msgobj;
	
	}

	public MessageObject CheckDeviceIsConnectToserver(DB mongoconnection,
			String device) {

		MessageObject msgobj=new MessageObject();


		try{

			DBCollection table = mongoconnection.getCollection(Common.TABLE_DEVICE);  		
			BasicDBObject device_whereQuery = new BasicDBObject("device", Long.parseLong(device));
			
			DBCursor cursor = table.find(device_whereQuery);
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			System.out.println("Cursor Count of u-----"+cursor.count()+"  "+device_whereQuery);
			long total=cursor.size();
		int Triplocationcount=0;

				if(cursor.size()!=0) 
				{	
						System.err.println("*-----COunt---"+cursor.size());
						 
						// while(cursor.hasNext()
						 DBObject dbObject1 = (DBObject) cursor.next();
						
						if (dbObject1.get("pid").equals("")||dbObject1.get("pid").equals(null)) {
							msgobj.setError("true");
							msgobj.setMessage("Device not Connected successfully to server.");
						}else {
							msgobj.setError("false");
							msgobj.setMessage("Device Connected successfully to server .");
							
						}
				}else {
					msgobj.setError("true");
					msgobj.setMessage("Device not added successfully.Please Try again.");
				}

		}catch(Exception e){

			e.printStackTrace();
			msgobj.setError("true");
			msgobj.setMessage("Device is not connected successfully due to "+e.getMessage());
		}finally{

		}
		return msgobj;
	}

	public ArrayList<LocationDTO> CheckDeviceLatestLocation(DB mongoconnection,
			String device) {
		ArrayList<LocationDTO> msgobj=new ArrayList<LocationDTO>();


		try{

			DBCollection table = mongoconnection.getCollection(Common.TABLE_LOCATION);  		
			BasicDBObject device_whereQuery = new BasicDBObject("device", Long.parseLong(device));
			DBCursor cursor = table.find(device_whereQuery).sort(new BasicDBObject("_id",-1)).limit(1);
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
			System.out.println("Cursor Count of u-----"+cursor.size()+"  "+device_whereQuery);
	
				if(cursor.size()!=0) 
				{	
						System.err.println("*-----Count---"+cursor.size());
						 
							DBObject dbObj= (DBObject) cursor.next();
						
							 BasicDBObject locobj2 = (BasicDBObject) dbObj.get("location");
							  
							    
							LocationDTO loc=new LocationDTO();
							loc.setDeviceImei(device);
							loc.setLang(locobj2.get("lon").toString());
							loc.setLat( locobj2.get("lat").toString());
							loc.setSpeed(dbObj.get("speed").toString());
							loc.setTime(dbObj.get("timestamp").toString());
							msgobj.add(loc);

						
						
				
				}

		}catch(Exception e){
			e.printStackTrace();
			/*msgobj.setError("true");
			msgobj.setMessage("Device not Connected successfully due to "+e.getMessage());*/
		}finally{

		}
		return msgobj;
	}

	public ArrayList<TripInfoDto> CheckTripReportGenerteOnserver(DB mongoconnection,
			String device, String startdate, String enddate) {
		MessageObject msgobj=new MessageObject();
		ArrayList<TripInfoDto>Triplist=new ArrayList<>();

		try{
			DBCollection table = mongoconnection.getCollection(Common.TABLE_TRIPREPORT);
			System.out.println("device==============----------"+device+" "+table.getFullName());
		
			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device",device);
			
			BasicDBObject timestamp_whereQuery = new BasicDBObject("source_info.timestamp", new BasicDBObject("$gte",Long.parseLong(startdate)).append("$lt",Long.parseLong(enddate )));
		
			
			BasicDBList And_Milage = new BasicDBList();
			And_Milage.add(timestamp_whereQuery);
			And_Milage.add(device_whereQuery);
			DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);
			
			
			
			DBCursor cursor = table.find(Total_Milage_query);
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
			
			System.err.println("*-----Qury---"+Total_Milage_query);

/*
				if(cursor.size()!=0) 
				{	
				System.err.println("*-----COunt---"+cursor.size());
				 
				msgobj.setError("false");
				msgobj.setMessage("Trip Report generated successfully.Database contain "+cursor.size()+"  entries.");
				}else {
					msgobj.setError("true");
					msgobj.setMessage("Trip Report not generated successfully.");
				}*/
			
			if(cursor.size()!=0) {
				
				System.err.println("*-----COunt---"+cursor.size());

				 while(cursor.hasNext()){
				
						TripInfoDto trip=new TripInfoDto();
					  DBObject dbObject1 = (DBObject) cursor.next();
						 
						BasicDBObject Sourceobj = (BasicDBObject) dbObject1.get("source_info");
						BasicDBObject Destobj = (BasicDBObject) dbObject1.get("dest_info");

						trip.setDevice(device);
						trip.setSrclat(Sourceobj.getString("lat"));
						trip.setSrclon(Sourceobj.getString("lon"));
						trip.setSrcspeed(Sourceobj.getString("speed"));

						trip.setSrctimestamp(Sourceobj.getString("timestamp"));

						trip.setDestlat(Destobj.getString("lat"));
						trip.setDestlon(Destobj.getString("lon"));
						trip.setDestspeed(Destobj.getString("speed"));

						trip.setDesttimestamp(Destobj.getString("timestamp"));

						trip.setReport_id(dbObject1.get("report_id").toString());
						trip.setTotalkm(dbObject1.get("totalkm").toString());
						trip.setMaxspeed(dbObject1.get("maxspeed").toString());
						trip.setAvgspeed(dbObject1.get("avgspeed").toString());

						trip.setDevicename(dbObject1.get("name").toString());
						//trip.setDevicename("vts");

							Triplist.add(trip);
						
						
					    
					} 
				    

			}

		}catch(Exception e){

			e.printStackTrace();
			msgobj.setError("true");
			msgobj.setMessage("Trip Report not generated successfully.due to "+e.getMessage());
		}
	
		return Triplist;
	}

	public ArrayList<GeofenceDTO> CheckGeofence(DB mongoconnection,
			String device) {
		ArrayList<GeofenceDTO> msgobj=new ArrayList<GeofenceDTO>();

		try{

			DBCollection table = mongoconnection.getCollection(Common.TABLE_DEVICE);  		
			BasicDBObject device_whereQuery = new BasicDBObject("device", Long.parseLong(device));
			
			DBCursor cursor = table.find(device_whereQuery);
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			System.out.println("Cursor Count of u-----"+cursor.count()+"  "+device_whereQuery);
			long total=cursor.size();
		int Triplocationcount=0;

				if(cursor.size()!=0) 
				{	
						System.err.println("*-----COunt---"+cursor.size());
						 
						 DBObject dbObject = (DBObject) cursor.next();
						
						 BasicDBList Geoarray= (BasicDBList) dbObject.get("geo_fences");
						 
						 if(Geoarray!=null){
								
								for(int i=0;i<Geoarray.size();i++){
									BasicDBObject jo=(BasicDBObject) Geoarray.get(i);
									BasicDBObject joalert_type =(BasicDBObject) jo.get("alert_type");
									BasicDBObject joinout = (BasicDBObject)joalert_type.get("in_out");
									BasicDBObject joalert = (BasicDBObject)joalert_type.get("alert");
									BasicDBObject jodetails = (BasicDBObject)jo.get("details");
									
									GeofenceDTO geo=new GeofenceDTO();
									geo.setDescr(jo.getString("descr"));
									geo.setEmail(joalert.getString("email"));
									geo.setEnable(jo.getString("enable"));
									geo.setId(jo.getString("id"));
									geo.setIn(joinout.getString("in"));
									geo.setLat(jodetails.getString("lat"));
									geo.setLan(jodetails.getString("lan"));
									geo.setName(jo.getString("name"));
									geo.setOut(joinout.getString("out"));
									geo.setPush_to_applan(joalert.getString("push_to_app"));
									geo.setRadius(jodetails.getString("radius"));
									geo.setSms(joalert.getString("sms"));
									geo.setStatus(jo.getString("status"));
									geo.setType(jodetails.getString("type"));
									
									msgobj.add(geo);
									
								}
						 }
						 
						
					
					
				}

		}catch(Exception e){

			e.printStackTrace();
			
		}finally{

		}
		return msgobj;
	}

	public MessageObject ClearTrackingPids(DB mongoconnection, String device) {
		// TODO Auto-generated method stub

		
	
		MessageObject msgobj=new MessageObject();


		try{

			DBCollection table = mongoconnection.getCollection(Common.TABLE_DEVICE);  		
			BasicDBObject device_whereQuery = new BasicDBObject("device", Long.parseLong(device));
			List<DBObject> Track_Pid_array = new ArrayList<DBObject>();

			BasicDBObject newtrackingpid_array = new BasicDBObject("track_pids",Track_Pid_array );
			BasicDBObject updateOperationDocument = new BasicDBObject("$set", newtrackingpid_array);

			WriteResult result = table.update(device_whereQuery, updateOperationDocument);
			System.err.println("*-----resultClearTrackingPids-------"+result.getN());
			if(result.getN()==1){
				msgobj.setError("false");
				msgobj.setMessage("Tracking pids  updated  successfully to server ."); 
				
			}else if(result.getN()==0) {
				msgobj.setError("true");
				msgobj.setMessage("Tracking pids  not updated successfully to server.Please ensure device IMEI no is added on server.");
			}

	
		}catch(Exception e){

			e.printStackTrace();
			msgobj.setError("true");
			msgobj.setMessage("Tracking pids  not updated successfully to server.due to "+e.getMessage());
		}finally{

		}
		return msgobj;
		
	}

	public MessageObject TrackLog(Connection con, String name) {
		// TODO Auto-generated method stub
		MessageObject msgobj=new MessageObject();
		String msg="";
		try (BufferedReader br = new BufferedReader(new FileReader(Common.Log_path+name+".txt"))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
			  

				System.out.println(sCurrentLine);
				msg=msg+sCurrentLine;
				msg.concat(System.lineSeparator());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		msgobj.setMessage(msg);
		msgobj.setError("false");
		
		return msgobj;
	}

	public MessageObject DeleteLogfile(Connection con, String name) throws IOException {
		// TODO Auto-generated method stub
		MessageObject msgobj=new MessageObject();
		String path=Common.Log_path+name+".txt";
		try {
			File inFile = new File(path);
			
			if(inFile.exists()){
			
			if(inFile.delete()){
			msgobj.setMessage(name+" file deleted sucessfully.");
			msgobj.setError("false");
			}else {
				msgobj.setMessage(name+" file not  deleted sucessfully.");
				msgobj.setError("true");
			}
			}else {
				msgobj.setMessage(name+" file not exist.");
				msgobj.setError("true");	
			}
			
		} catch (Exception x) {
			x.printStackTrace();
			msgobj.setMessage(name+" file not  deleted sucessfully.");
			msgobj.setError("true");
			
		}

		
		
		return msgobj;	
		
	}


	
	
	public MessageObject GenerateDevicePendingTripReport(Connection con,
			DB mongoconnection, String startdate, String enddate,
			String device) {
		ArrayList<DeviceInfoDto> UserList=new ArrayList<>();
		int SpeedcCheck=0;
		ArrayList<DBObject> TempLoctionList=new ArrayList<>();
		int TripTimeLimit=300;
		int trip=0;
		MessageObject msgObj=new MessageObject();
		
		long SignalCheckcount=0;
		  File GeneratePendingTripReport;
			 FileWriter writer = null;
			Calendar calendar = Calendar.getInstance();
			
			BufferedWriter bfwriter=null;
			calendar = Calendar.getInstance();
			ArrayList<Double> DeaciveListDevice=new ArrayList<Double>();
			 
			 try {
		            //Whatever the file path is.
				 GeneratePendingTripReport = new File(Common.Log_path+"Generate_PendingTripReport_log"+calendar.getTime()+".txt");
				 	//Create the file
				 	if (GeneratePendingTripReport.createNewFile()){
				 	System.out.println("File is created!");
				 	}else{
				 	System.out.println("File already exists.");
				 	}
				 	 
				 	//Write Content
				 	 writer = new FileWriter(GeneratePendingTripReport);
				 	 
				 	 
				 	 bfwriter = new BufferedWriter(writer);
				 	bfwriter.write("\nStart Generate Pending  TripReprt Log----at "+calendar.getTime());
		           
		        } catch (IOException e) {
		            System.err.println("Problem writing to the file statsTest.txt");
		        }catch (Exception e) {

		        	e.printStackTrace();
				}
			 

		try{
			PreparedStatement ps=con.prepareStatement("select FirstName,LastName ,DeviceID from StudentMaster where DeviceID=?");
			ps.setString(1, device);
			ResultSet rs=ps.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					DeviceInfoDto dto=new DeviceInfoDto();
					dto.setName(rs.getString(1)+rs.getString(2));
					dto.setDeviceID(rs.getString(3));
					
					UserList.add(dto);
					
					System.out.println("--------Report Req-----"+rs.getString(1)+rs.getString(2));
				}
			}
		}catch(Exception e){

			e.printStackTrace();
			msgObj.setError("True");
			msgObj.setMessage("Getting error in Generate Device Pending TripReport due to --"+e.getMessage());
		}
		
		
		try{
			PreparedStatement ps1=con.prepareStatement("select ActualData   from ApplicationMetaData where ApplicationMetaData.ID=5");
			ResultSet rs1=ps1.executeQuery();
			if (rs1!=null) {
				while (rs1.next()) {
					TripTimeLimit=Integer.parseInt(rs1.getString(1));

				}
			}
		}catch(Exception e){

			e.printStackTrace();
			msgObj.setError("True");
			msgObj.setMessage("Getting error in Generate Device Pending TripReport due to --"+e.getMessage());
		}
		
		
	  	
		try
		{
			if (UserList.size()>0) {

			bfwriter.write("\n-----------------------Report Generating  for---------------------------"
					+ "\n\n******************"+"StartTime: " + startdate + "  End Time: "+enddate+"********");
		
					for(int id=0;id<UserList.size();id++){
						
												trip=0;
												HistoryDistance=0;
													
													DBCollection table = mongoconnection.getCollection(Common.TABLE_LOCATION);  		
													BasicDBObject device_whereQuery = new BasicDBObject();
													device_whereQuery.put("device",Long.parseLong(UserList.get(id).getDeviceID()));
												//	device_whereQuery.put("device",Long.parseLong("355488020181042"));
							
													BasicDBObject timestamp_whereQuery = new BasicDBObject("timestamp", new BasicDBObject("$gte",Long.parseLong(startdate)).append("$lt",Long.parseLong(enddate)));
													BasicDBList And_Milage = new BasicDBList();
													And_Milage.add(timestamp_whereQuery);
													And_Milage.add(device_whereQuery);
													DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);
													

													DBCursor cursor = table.find(Total_Milage_query);
													cursor.sort(new BasicDBObject("timestamp", 1));

													cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
							
													System.out.println("Cursor Count of u-p11----"+cursor.size()+"  "+Total_Milage_query);
													long i=0;
													long total=cursor.size();
													Double MaxSpeed=0.0;
													Double TotalSpeed=0.0;
													int Triplocationcount=0;
							
													  if(cursor.size() > 0) {
															TempLoctionList.clear();
							
															System.err.println("*-----COunt---"+cursor.size());
						
														List<DBObject> listObjects = cursor.toArray();
														 LoginDAO logindao=new LoginDAO();        
														trip=logindao.Calculate_Trip((ArrayList<DBObject>) listObjects,mongoconnection,TripTimeLimit,UserList.get(id).getDeviceID(),UserList.get(id).getName());
												
												}
																

																
												
									
													bfwriter.write("\n------Trip Generate count is == "+trip+"  for  "+UserList.get(id).getName()+" , Device Id : "+UserList.get(id).getDeviceID());
													
														//bfwriter.write("\n------Trip Generate count is == "+trip);
												
												
												
					
						 
						}//for end
				}else {
					msgObj.setError("True");
					msgObj.setMessage("Device not found for TripReport. ");
				}
		} 
		catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgObj.setError("True");
			msgObj.setMessage("Getting error in Generate Device Pending TripReport due to --"+e.getMessage());
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgObj.setError("True");
			msgObj.setMessage("Getting error in Generate Device Pending TripReport due to --"+e.getMessage());
		}	   
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{

			try {

				if (bfwriter != null)
					bfwriter.close();

				if (writer != null)
					writer.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
				

		return msgObj;
	}
	
	public ListDeviceNotconnectedDTO GetListOfDeviceNotConnected(
			DB mongoconnection) {
		ListDeviceNotconnectedDTO dtp=new ListDeviceNotconnectedDTO();
		int total=0;

		try{

			DBCollection table = mongoconnection.getCollection(Common.TABLE_DEVICE);  		
			List cursor =  table.distinct("device");

			 total=cursor.size();

				if(cursor.size()!=0) 
				{	
				System.err.println("*-----Count-GetListOfDeviceNotConnected--------"+cursor.size());
				}
		}catch(Exception e){e.printStackTrace();}
		

		try{

			DBCollection table = mongoconnection.getCollection(Common.TABLE_DEVICE); 
			DBObject nulltype = new BasicDBObject("pid", null);  
			DBObject emptytype = new BasicDBObject("pid", "");  
			
			BasicDBList or = new BasicDBList();
			or.add(nulltype);
			or.add(emptytype);
			DBObject whereQuery = new BasicDBObject("$or", or);
		
			DBCursor cursor = table.find(whereQuery);
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			System.out.println("Cursor Count of u-----"+cursor.size()+"  "+whereQuery);
			int totaldeacttive=cursor.size();
			ArrayList<Double> ListDevice=new ArrayList<Double>();

		if(cursor.size()!=0) 
		{	
				System.err.println("*-----COunt---"+cursor.size());
				 
				
				 while(cursor.hasNext()){
					  DBObject dbObject = (DBObject) cursor.next();
						
					  ListDevice.add(Double.parseDouble(dbObject.get("device")+""));
						
					    
					} 
							 
							
						
						
				}
			
					dtp.setListOfDevice(ListDevice);
					dtp.setTotalActiveCount((total-totaldeacttive)+"");
					dtp.setTotalDeactiveCount(totaldeacttive+"");
			
			}catch(Exception e){
			
				e.printStackTrace();
				
			}finally{
			
			}

	
		
		return dtp;
	}
	
	public DeactiveDeviceListDTO GenerateDeactiveDeviceList(DB mongoconnection) {
		DeactiveDeviceListDTO dtp=new DeactiveDeviceListDTO();
		ArrayList<String> ListDevice=new ArrayList<String>();
		ArrayList<String> DeactiveListDevice=new ArrayList<String>();
		
		  year = calendar.get(Calendar.YEAR);
          month = calendar.get(Calendar.MONTH);
          day = calendar.get(Calendar.DAY_OF_MONTH);
		 long startdate = Long.valueOf(Common.getGMTTimeStampFromDate(day-7+ "-"+ String.valueOf(month+1) + "-" + year+" 00:00 am"));
		 long enddate = Long.valueOf(Common.getGMTTimeStampFromDate(day+ "-"+ String.valueOf(month+1) + "-" + year+" 11:59 pm"));

				
		int total=0;


		try{

			DBCollection table = mongoconnection.getCollection(Common.TABLE_DEVICE);  		
			List cursor  =    table.distinct("device");

			 total=cursor.size();
			NumberFormat myformatter = new DecimalFormat("###############");  
			System.err.println("*-----COunt---"+cursor.size()+" --------start--"+startdate+"-------Enddate--"+enddate);		
			for (int i = 0; i < cursor.size(); i++)
					  ListDevice.add(myformatter.format(cursor.get(i)));
				
				}catch(Exception e){
				
					e.printStackTrace();
					
				}
		
		/////////////---------------------------------------------////////////////////////////////////////

				try {											
							if (ListDevice.size()>0) {
								for (int id = 0; id < ListDevice.size(); id++) {
									DBCollection table = mongoconnection
											.getCollection(Common.TABLE_LOCATION);
									BasicDBObject device_whereQuery = new BasicDBObject();
									device_whereQuery.put("device",Long.parseLong(ListDevice.get(id)));
									
									
									BasicDBObject timestamp_whereQuery = new BasicDBObject(
											"timestamp", new BasicDBObject("$gte",startdate).append("$lt",enddate));
				
									
									BasicDBList And_Milage = new BasicDBList();
									And_Milage.add(timestamp_whereQuery);
									And_Milage.add(device_whereQuery);
									DBObject Total_Milage_query = new BasicDBObject("$and",
											And_Milage);
									
									DBCursor cursor = table.find(Total_Milage_query).sort(new BasicDBObject("$natural",-1)).limit(1);
									cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
				
									System.out.println("Cursor Count of u-p11----"+ cursor.size() + "  " + Total_Milage_query);
									
									if (cursor.size() == 0) 
										DeactiveListDevice.add(ListDevice.get(id));
									else {
										
									}
								}
								

							BasicDBObject Maindocument = new BasicDBObject();
							ObjectId objid = new ObjectId();
							Maindocument.put("report_id", "" + objid);
							Maindocument.put("totaldeactive_device", DeactiveListDevice.size()+"");
							Maindocument.put("totaldevice", ListDevice.size()+"");
							Maindocument.put("timestamp", Calendar.getInstance().getTimeInMillis());
							Maindocument.put("listofdeactive_device", DeactiveListDevice);

							DBCollection analysistable = mongoconnection
									.getCollection(Common.TABLE_USER_ANALYSIS);

							analysistable.insert(Maindocument);
							
							
							System.out.println("*************************"+DeactiveListDevice.size());
							dtp.setTotalDeactiveDevice(DeactiveListDevice.size()+"");
							dtp.setTotaldevice(ListDevice.size()+"");
							dtp.setListOfDevice(DeactiveListDevice);

							}
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MongoException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	   
				
					
		
		return dtp;
	}
	
	
	
	
	
	public DeactiveDeviceListDTO GetDeactiveDeviceList(DB mongoconnection) {
		DeactiveDeviceListDTO dtp=new DeactiveDeviceListDTO();
		DBCollection table = mongoconnection
				.getCollection(Common.TABLE_USER_ANALYSIS);
		BasicDBObject device_whereQuery = new BasicDBObject();
		
		
	
	
		DBCursor cursor = table.find().sort(new BasicDBObject("$natural",-1)).limit(1);
		cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

		System.out.println("Cursor Count of u-p11----"+ cursor.size() + "  ");
		
		if (cursor.size() != 0) 
		{
			 DBObject dbObject = (DBObject) cursor.next();
			 		dtp.setTotalDeactiveDevice(dbObject.get("totaldeactive_device").toString());
			 		dtp.setTotaldevice(dbObject.get("totaldevice").toString());
			 		dtp.setListOfDevice((ArrayList<String>) dbObject.get("listofdeactive_device"));
			 		dtp.setTimestamp(dbObject.get("timestamp").toString());
				
					
				}
		
		return dtp;
		}
			
	
	
	
	

	public MessageObject SendMsg_GetFetureExpireData(Connection con) {
	try{			

			

			java.sql.CallableStatement stmt= con.prepareCall("{call GetFetureExpireData()}");
	
			ResultSet rs=stmt.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					//  StudentID,MobileNo,ParentID,ParentID,Name ,Photo,Type,DeviceID,DayRemaining,ExpiryDate
					
					  //http://sms.hspsms.com/sendSMS?username=primesystech&message=hirupesh&sendername=MKTSMS&smstype=TRANS&numbers=9864814370&apikey=03b668ab-2626-4190-a03f-153697503b9a
					  
					  String msg="Dear customer,Your subscription of "+rs.getString("Name")+" is going to expire in "+rs.getString("DayRemaining")+" days."+
					  "To continue please renew your service.%0A%0A From,%0A MykiddyTracker Team.";
						System.out.println(msg);

						try {
							URL url = new URL("http://sms.hspsms.com/sendSMS?username=primesystech&message="+msg.replace(" ", "%20")+"&sendername=MKTSMS&smstype=TRANS&numbers="+rs.getString("MobileNo")+"&apikey=03b668ab-2626-4190-a03f-153697503b9a");
							// making connection
							HttpURLConnection conn = (HttpURLConnection) url.openConnection();
							conn.setRequestMethod("GET");
							conn.setRequestProperty("Accept", "application/json");
						/*	if (conn.getResponseCode() != 200||conn.getResponseCode() != 500) {
								throw new RuntimeException("Failed : HTTP error code : "
										+ conn.getResponseCode());
							}*/

							// Reading data's from url
						   BufferedReader br = new BufferedReader(new InputStreamReader(
								(conn.getInputStream())));

							String output;
							String out="";
							System.out.println("Output from Server .... \n");
							while ((output = br.readLine()) != null) {
								System.out.println(output);
								out+=output;
							}
							
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
	return null;
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

		public MessageObject Generate_Idcard_DevicePendingTripReport(
				Connection con, DB mongoconnection, String Starttime,
				String Endtime, String device) {
			
			ArrayList<DeviceInfoDto> UserList=new ArrayList<>();
			int SpeedcCheck=0;
			ArrayList<DBObject> TempLoctionList=new ArrayList<>();
			int TripTimeLimit=300;
			int trip=0;
			MessageObject msgObj=new MessageObject();
			
			long SignalCheckcount=0;
			  File GenerateIdcardPendingTripReport;
				 FileWriter writer = null;
				Calendar calendar = Calendar.getInstance();
				BufferedWriter bfwriter=null;
				calendar = Calendar.getInstance();
				ArrayList<Double> DeaciveListDevice=new ArrayList<Double>();
				 
				 try {
			            //Whatever the file path is.
					 GenerateIdcardPendingTripReport = new File(Common.Log_path+"Generate_PendingTripReport_log"+calendar.getTime()+".txt");
					 	//Create the file
					 	if (GenerateIdcardPendingTripReport.createNewFile()){
					 	System.out.println("File is created!");
					 	}else{
					 	System.out.println("File already exists.");
					 	}
					 	 
					 	//Write Content
					 	 writer = new FileWriter(GenerateIdcardPendingTripReport);
					 	 
					 	 
					 	 bfwriter = new BufferedWriter(writer);
					 	bfwriter.write("\nStart Generate Pending  TripReprt Log----at "+calendar.getTime());
			           
			        } catch (IOException e) {
			            System.err.println("Problem writing to the file statsTest.txt");
			        }catch (Exception e) {

			        	e.printStackTrace();
					}
				 

			try{
				PreparedStatement ps=con.prepareStatement("select FirstName,LastName ,DeviceID from StudentMaster where DeviceID=?");
				ps.setString(1, device);
				ResultSet rs=ps.executeQuery();
				if (rs!=null) {
					while (rs.next()) {
						DeviceInfoDto dto=new DeviceInfoDto();
						dto.setName(rs.getString(1)+rs.getString(2));
						dto.setDeviceID(rs.getString(3));
						
						UserList.add(dto);
						
						System.out.println("--------Report Req-----"+rs.getString(1)+rs.getString(2));
					}
				}
			}catch(Exception e){

				e.printStackTrace();
				msgObj.setError("True");
				msgObj.setMessage("Getting error in Generate Device Pending  IDcard TripReport due to --"+e.getMessage());
			}
			
			
			try{
				PreparedStatement ps1=con.prepareStatement("select ActualData from ApplicationMetaData where ApplicationMetaData.ID=5");
				ResultSet rs1=ps1.executeQuery();
				if (rs1!=null) {
					while (rs1.next()) {
						TripTimeLimit=Integer.parseInt(rs1.getString(1));

					}
				}
			}catch(Exception e){

				e.printStackTrace();
				msgObj.setError("True");
				msgObj.setMessage("Getting error in Generate Device Pending TripReport due to --"+e.getMessage());
			}
			
			
		  	
			try
			{
				bfwriter.write("\n----------TEStttt----IDcard---------Report Generating  for---------------------------"
						+ "\n\n******************"+"StartTime: " + Starttime + "  End Time: "+Endtime+"********");
			
					for(int id=0;id<UserList.size();id++){
							
													trip=0;
													HistoryDistance=0;
														
														DBCollection table = mongoconnection.getCollection(Common.TABLE_LOCATION);  		
														BasicDBObject device_whereQuery = new BasicDBObject();
														device_whereQuery.put("device",Long.parseLong(UserList.get(id).getDeviceID().trim()));
														//device_whereQuery.put("device",Long.parseLong("359751090004149"));
								
														BasicDBObject timestamp_whereQuery = new BasicDBObject("timestamp", new BasicDBObject("$gte",Long.parseLong(Starttime)).append("$lt",Long.parseLong(Endtime)));
														BasicDBList And_Milage = new BasicDBList();
														And_Milage.add(timestamp_whereQuery);
														And_Milage.add(device_whereQuery);
														DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);
														
														

														DBCursor cursor = table.find(Total_Milage_query);
														cursor.sort(new BasicDBObject("timestamp", 1));

														cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
								
														System.out.println("Cursor Count of u-p11----"+cursor.size()+"  "+Total_Milage_query);
														long i=0;
														long total=cursor.size();
														Double MaxSpeed=0.0;
														Double TotalSpeed=0.0;
														int Triplocationcount=0;
								
														  if(cursor.size() > 0) {
																TempLoctionList.clear();
								
																//System.err.println("*-----COunt---"+cursor.size());
							
															List<DBObject> listObjects = cursor.toArray();
															trip=Calculate_ID_CardTrip((ArrayList<DBObject>) listObjects,mongoconnection,TripTimeLimit,UserList.get(id).getDeviceID(),UserList.get(id).getName());
															//trip=Calculate_ID_CardTrip((ArrayList<DBObject>) listObjects,mongoconnection,TripTimeLimit,"359751090004149","MLDT 266");
															msgObj.setError("False");
															msgObj.setMessage(" Generate Device Pending  IDcard TripReport------"+trip);
															
													}																	
													
										
												//		bfwriter.write("\n------Trip Generate count is == "+trip+"  for  "+UserList.get(id).getName()+" , Device Id : "+UserList.get(id).getDeviceID());
														
															//bfwriter.write("\n------Trip Generate count is == "+trip);
													
													
													
						
							
								}//for end
			} 
			catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{

				try {

					if (bfwriter != null)
						bfwriter.close();

					if (writer != null)
						writer.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}
			}
				
				
			return msgObj;
		}

	
		private int Calculate_ID_CardTrip(ArrayList<DBObject> location_list,
				DB mongoconnection, int tripTimeLimit, String deviceID,
				String name) {

			Collections.reverse(location_list);
			long TotalLocationData=location_list.size();
			Double MaxSpeed=0.0;
			Double TotalSpeed=0.0;
			int Triplocationcount=0;
			Double SpeedcCheck=1.0;;
			ArrayList<DBObject> TempCalLoctionList=new ArrayList<>();
			int TripTimeLimit=tripTimeLimit;
			long SignalCheckcount=0;
			long Timeidiff=0;
			double HistoryDistanceKM=0;
			int TripCount=0;
			TempCalLoctionList.clear();
			Double center_lat=0.0;
			Double centre_lan = 0.0;
			int far_count=0;
			int stop_count=0;
			for(int i=0;i<TotalLocationData;i++)
			{
				
					
				SpeedcCheck=Double.parseDouble(location_list.get(i).get("speed").toString()); 
				
				////System.err.println("------Speedcheck-------"+i+"----"+Common.getDateCurrentTimeZone(Long.parseLong(location_list.get(i).get("timestamp")+""))+"---------"+SpeedcCheck);
				if(i==0){
				BasicDBObject centare = (BasicDBObject) location_list.get(i).get("location");
			     center_lat = centare.getDouble("lat");
			     centre_lan = centare.getDouble("lon");
				}
				
				if(i>0){
					BasicDBObject destination = (BasicDBObject) location_list.get(i).get("location");
				     double dest_lat = destination.getDouble("lat");
				     double dest_lan = destination.getDouble("lon");
					double distance_calculated = distance(center_lat,centre_lan,dest_lat,dest_lan, "K");
							if(distance_calculated<0.200)
							{
								stop_count++;
								/*System.out.println("***stop_count---*********"+stop_count+
										"--Time----"+
										Common.getDateCurrentTimeZone(Long.parseLong
												(location_list.get(i).get("timestamp").
														toString()))+"---Distcal--"+distance_calculated);
								*/
									//stop_count = 0;
								/*}
								else {
								//	far_count=0;
									BasicDBObject centare = (BasicDBObject) location_list.get(i).get("location");
								     center_lat = centare.getDouble("lat");
								     centre_lan = centare.getDouble("lon");
								}*/
							}else {
								
								far_count++;
							/*	System.err.println("***far_count---*********"
												+ far_count
												+ "--Time----"
												+ Common.getDateCurrentTimeZone(Long
														.parseLong(location_list
																.get(i)
																.get("timestamp")
																.toString()))+"  -----Distal--   "+distance_calculated);
								*/
				
								stop_count = 0;
								BasicDBObject centare = (BasicDBObject) location_list.get(i).get("location");
							     center_lat = centare.getDouble("lat");
							     centre_lan = centare.getDouble("lon");
							}
				
				}
				

				
					if(far_count>1)
					{
					TempCalLoctionList.add(location_list.get(i));
					//System.err.println("***TempLoctionList---*********"+i+"*******Size********--"+TempCalLoctionList.size());

					}
							
					if(SpeedcCheck>MaxSpeed)
					 MaxSpeed=SpeedcCheck;
								
								 
					TotalSpeed=TotalSpeed+SpeedcCheck;
				
				
									
								    
								if (i>0 && i<TotalLocationData-1&&far_count>1)
								{
									BasicDBObject locobj1 = (BasicDBObject) location_list.get(i).get("location");
								    double lat1 = locobj1.getDouble("lat");
								    double lng1 = locobj1.getDouble("lon");
								    
								    BasicDBObject locobj2 = (BasicDBObject) location_list.get(i+1).get("location");
								    double lat2 = locobj2.getDouble("lat");
								    double lng2 = locobj2.getDouble("lon");
								    
												    if(lat1!=lat2&&lng1!=lng2)
												    {
												    	double caldist = distance(lat1,lng1,lat2,lng2, "K");
								
														if (!Double.isNaN(caldist)) 
														{
															HistoryDistanceKM=HistoryDistanceKM+caldist;
															
														}
														////System.out.println("HistoryDistanceKM----"+HistoryDistanceKM);
													
												    }
												    
								}
				/*
								if (i>2 && i<TotalLocationData-3)
								{
									Long	srctime=Long.parseLong(TempLoctionList.get(TempLoctionList.size()-2).get("timestamp").toString());
									Long	desttime=Long.parseLong(TempLoctionList.get(TempLoctionList.size()-1).get("timestamp").toString());
										
								 Timeidiff=desttime-srctime;
									
								}*/
								
						/*		
								if(i>1&&stop_count>5&&TempCalLoctionList.size()>0) {
									
									if (Integer.parseInt(location_list.get(i-1).get("speed").toString())==0)
									{
										Long	srctime=Long.parseLong(location_list.get(i-1).get("timestamp").toString());
										Long	desttime=Long.parseLong(location_list.get(i).get("timestamp").toString());
											
										long diff = desttime - srctime;
					
										SignalCheckcount=SignalCheckcount+diff;
										//System.err.println("i-----"+i+"---------SpeedcCheck---"+SpeedcCheck+"-----SignalCheckcount---"+ SignalCheckcount+"----------Diff-----"+diff);
					
									}else {
										SignalCheckcount=0;
									}

									
								}*/
									
								    
								if((stop_count>=5&&TempCalLoctionList.size()>0&& HistoryDistanceKM>1)||(TempCalLoctionList.size()>0&&i==TotalLocationData-1))
								{
									
									//System.err.println("Stop Count-------------"+stop_count+"--- for-----------"+name);
									//System.err.println("TempCalLoctionListt-------------"+TempCalLoctionList.size()+"--- for-----------"+name);

								//System.out.println("********** Trip****Starttime***"+i+"**************"+Common.getDateCurrentTimeZone(Long.parseLong(TempCalLoctionList.get(0).get("timestamp").toString())));
								//System.out.println("********** Trip****EndTime******"+i+"***********"+Common.getDateCurrentTimeZone(Long.parseLong(TempCalLoctionList.get(TempCalLoctionList.size()-1).get("timestamp").toString())));
								
								Collections.reverse(TempCalLoctionList);

									
								////System.err.println("-----SignalCheckcount Got ---"+ SignalCheckcount+"   speedcheck=="+SpeedcCheck+"   Historydist---"+HistoryDistanceKM+"  MaxSpeed -"+MaxSpeed+"\n\n");

								
									BasicDBObject Maindocument = new BasicDBObject();
									ObjectId objid = new ObjectId();
									Maindocument.put("report_id","" + objid);
									Maindocument.put("device",deviceID);
									Maindocument.put("name",name);
									
									Maindocument.put("totalkm",HistoryDistanceKM);
									Maindocument.put("maxspeed", MaxSpeed);
									
									if (TempCalLoctionList.size()>0) {
										Maindocument.put("avgspeed",TotalSpeed/TempCalLoctionList.size());
									}else {
										Maindocument.put("avgspeed",0);
									}
									


									double locallat1,locallng1,locallat2,locallnt2;
									DBObject srcdbobj = TempCalLoctionList.get(0);
									
									 BasicDBList addresses = ( BasicDBList ) location_list.get(i).get( "status" );

									BasicDBObject locallocobj = (BasicDBObject) srcdbobj.get("location");
									 locallat1 = locallocobj.getDouble("lat");
									  locallng1 = locallocobj.getDouble("lon");
									    
									BasicDBObject Intial_Loc = new BasicDBObject();
								/*	Intial_Loc.put("lat", locallat1);
									Intial_Loc.put("lon", locallng1);*/
									 String latDBObj = (String) ((BasicDBObject) addresses.get("3")).get("lat_direction");
									 String lanDBObj = (String) ((BasicDBObject) addresses.get("2")).get("lon_direction");

								//	//System.out.println("addresses****************   "+latDBObj+"  "+lanDBObj);
		   
									 if (latDBObj.equalsIgnoreCase("N")&&lanDBObj.equalsIgnoreCase("E")) {

						                    Intial_Loc.put("lat", locallat1);
											Intial_Loc.put("lon", locallng1);
											Intial_Loc.put("src_address",Common.GetAddress(""+locallat1, ""+locallng1));


						                }else if (latDBObj.equalsIgnoreCase("N")&&lanDBObj.equalsIgnoreCase("W")) {

						                    Intial_Loc.put("lat", locallat1);
											Intial_Loc.put("lon", -locallng1);
											Intial_Loc.put("src_address",Common.GetAddress(""+locallat1, "-"+locallng1));

						                }
						                else if (latDBObj.equalsIgnoreCase("S")&&lanDBObj.equalsIgnoreCase("E")) {

					                    Intial_Loc.put("lat", -locallat1);
					                    Intial_Loc.put("lon", locallng1);
										Intial_Loc.put("src_address",Common.GetAddress("-"+locallat1, ""+locallng1));


						                }else if (latDBObj.equalsIgnoreCase("S")&&lanDBObj.equalsIgnoreCase("W")) {

						                    Intial_Loc.put("lat", -locallat1);
											Intial_Loc.put("lon", -locallng1);
											Intial_Loc.put("src_address",Common.GetAddress("-"+locallat1, "-"+locallng1));

						                }
									 
									Intial_Loc.put("speed",srcdbobj.get("speed"));
									Intial_Loc.put("timestamp",srcdbobj.get("timestamp"));
									Maindocument.put("source_info",Intial_Loc);
									
									DBObject destobj = TempCalLoctionList.get(TempCalLoctionList.size()-1);
									
									BasicDBObject destlocobj = (BasicDBObject) destobj.get("location");
									 locallat2 = destlocobj.getDouble("lat");
									  locallnt2 = destlocobj.getDouble("lon");
				
									BasicDBObject Dest_Loc = new BasicDBObject();
									/*Dest_Loc.put("lat", locallat2);
									Dest_Loc.put("lon", locallnt2);*/
									 if (latDBObj.equalsIgnoreCase("N")&&lanDBObj.equalsIgnoreCase("E")) {

										 Dest_Loc.put("lat", locallat2);
										 Dest_Loc.put("lon", locallnt2);
										 Dest_Loc.put("dest_address",Common.GetAddress(""+locallat2, ""+locallnt2));

						                }else if (latDBObj.equalsIgnoreCase("N")&&lanDBObj.equalsIgnoreCase("W")) {

						                	Dest_Loc.put("lat", locallat2);
						                	Dest_Loc.put("lon", -locallnt2);
											Dest_Loc.put("dest_address",Common.GetAddress(""+locallat2, "-"+locallnt2));

						                }
						                else if (latDBObj.equalsIgnoreCase("S")&&lanDBObj.equalsIgnoreCase("E")) {

						                	Dest_Loc.put("lat", -locallat2);
						                	Dest_Loc.put("lon", locallnt2);
											Dest_Loc.put("dest_address",Common.GetAddress("-"+locallat2, ""+locallnt2));

						                }else if (latDBObj.equalsIgnoreCase("S")&&lanDBObj.equalsIgnoreCase("W")) {

						                	Dest_Loc.put("lat", -locallat2);
						                	Dest_Loc.put("lon", -locallnt2);
											Dest_Loc.put("dest_address",Common.GetAddress("-"+locallat2, "-"+locallnt2));

						                }
									 
									Dest_Loc.put("speed",destobj.get("speed"));
									Dest_Loc.put("timestamp",destobj.get("timestamp"));
									Maindocument.put("dest_info",Dest_Loc);
				
									DBCollection Triptable = mongoconnection.getCollection(Common.TABLE_TRIPREPORT);
				
									Triptable.insert(Maindocument);
									HistoryDistanceKM = 0.0;
									TempCalLoctionList.clear();
									SignalCheckcount=0;
									MaxSpeed=0.0;
									TotalSpeed=0.0;
									Timeidiff=0;
									Triplocationcount=0;
									TripCount++;	
									far_count=0;
									stop_count=0;
									center_lat=locallat2;
									centre_lan=locallnt2;
									
								}else if (HistoryDistanceKM<1) {
									SignalCheckcount=0;
									MaxSpeed=0.0;
									TotalSpeed=0.0;
									Timeidiff=0;
									Triplocationcount=0;
									
									//TempCalLoctionList.clear();
								}						
								
					
				
				
			}
			
			return TripCount;
		
		}

		

		
	

}
