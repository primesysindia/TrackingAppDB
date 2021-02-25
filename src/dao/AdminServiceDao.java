package dao;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.mongodb.client.model.Aggregates.*;

import java.util.Arrays;
import java.util.List;

import javax.json.JsonArray;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Utility.SendEmail;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.ConnectionString;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

import dto.AddDeviceDropDownInfo;
import dto.DailyReportSummaryWithStoppageDTO;
import dto.DepartmentsDTO;
import dto.DeviceBatteryDTO;
import dto.DeviceCommandDTO;
import dto.DeviceCommandHistoryDTO;
import dto.DeviceDTO;
import dto.DeviceDataDTO;
import dto.DeviceExchangeDTO;
import dto.DeviceInfoGatheringDTO;
import dto.DeviceIssueDTO;
import dto.DevicePaymentDTO;
import dto.DevicePaymentInfoDetailsDTO;
import dto.DeviceStatusInfoDto;
import dto.DeviceTypeDTO;
import dto.FeatureAddressDetailsDTO;
import dto.IssueFileInfoDTO;
import dto.MailFormatDTO;
import dto.MessageObject;
import dto.PaymentPlanDTO;
import dto.PaymentmodeDTO;
import dto.RailwayDeptHierarchyDTO;
import dto.RailwayKeymanDTO;
import dto.RailwayPatrolManDTO;
import dto.RailwayPatrolManripMasterDTO;
import dto.ReportSummeryDTO;
import dto.StudentMasterDTO;
import dto.UserDTO;

public class AdminServiceDao {
	
	private static DecimalFormat df2 = new DecimalFormat(".##");

	public ArrayList<UserDTO> GetAllTrackUser(Connection con) {

		ArrayList<UserDTO> p = new ArrayList<UserDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetTrackUserOnly()}");
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					UserDTO UDTO = new UserDTO();
					UDTO.setName(rs.getString("Name"+""));
					UDTO.setParentId(rs.getInt("Id"));
					UDTO.setRollId(rs.getInt("Role_ID"));
					UDTO.setStudentCount(rs.getInt("StudentCount"));
				//	UDTO.setIsRailwayUser(rs.getBoolean("IsRailwayUser"));		
					p.add(UDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public ArrayList<RailwayKeymanDTO> GetKeymanExistingBeat(Connection con, int parentId, int studentId, int beatId,int userLoginId) {

		ArrayList<RailwayKeymanDTO> p = new ArrayList<RailwayKeymanDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetKeymanExistingBeat(?,?,?,?)}");
			ps.setInt(1, parentId);
			ps.setInt(2, studentId);
			ps.setInt(3, beatId);
			ps.setInt(4, userLoginId);
			//System.out.println("Parent-"+parentId+"---Student----"+studentId+"---userLoginId---"+userLoginId);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					RailwayKeymanDTO Raildto= new RailwayKeymanDTO();
					Raildto.setParentId(parentId);
					Raildto.setBeatId(rs.getInt("Id"));
					Raildto.setStudentId(rs.getInt("studentId"));
					Raildto.setKmStart(rs.getDouble("KmStart"));
					Raildto.setKmEnd(rs.getDouble("KmEnd"));
					Raildto.setSectionName(rs.getString("SectionName").trim());
					Raildto.setDeviceId(rs.getString("DeviceID"));
					Raildto.setStart_Lat(rs.getDouble("kmStartLat"));
					Raildto.setStart_Lon(rs.getDouble("kmStartLang"));
					Raildto.setEnd_Lat(rs.getDouble("kmEndLat"));
					Raildto.setEnd_Lon(rs.getDouble("kmEndLang"));
					System.err.println(rs.getInt("Id"));
					Raildto.setIsApprove(rs.getBoolean("ApprovedStatus"));
					p.add(Raildto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public MessageObject SaveKeymanBeat(Connection con,int parentId,int studentId,double kmStart,double kmEnd,String sectionName,
			String deviceId,Double kmStartLat,Double kmStartLang,Double kmEndLat,Double kmEndLang,int userLoginId){

		MessageObject msgo = new MessageObject();
		String Photo = "";
		
		
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call SaveRailwayKeymanBeatPathWithLocation(?,?,?,?,?,?,?,?,?,?)}");
			ps.setInt(1, studentId);
			ps.setDouble(2, kmStart);
			ps.setDouble(3, kmEnd);
			ps.setString(4, sectionName);
			ps.setInt(5, parentId);
			ps.setDouble(6, kmStartLat);
			ps.setDouble(7, kmStartLang);
			ps.setDouble(8, kmEndLat);
			ps.setDouble(9, kmEndLang);
			ps.setInt(10, userLoginId);
						
			
//			System.out.println("Parent-"+parentId+"---Student----"+studentId+"---KmStart---"+kmStart+"---KmEnd---"+kmEnd+"---SectionName---"+sectionName+"---DeviceId---"+
//			deviceId+"---KmStartLat---"+kmStartLat+"---KmStartLang---"+kmStartLang+"---KmEndLat---"+kmEndLat+"---KmEndLang"+kmEndLang+"---userLoginId---"+userLoginId);
			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Beat was  not Inserted");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Beat was Inserted Successfully");
	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgo;
	}

	public MessageObject UpdateKeymanBeat(Connection con,int parentId, int studentId, double kmStart, double kmEnd,String sectionName,
			double kmStartLat,double kmStartLang, double kmEndLat, double kmEndLang, int beatId,int userLoginId,Boolean isApprove) {

			MessageObject msgo = new MessageObject();
			String Photo = "";
			
			
			try {
				java.sql.CallableStatement ps = con.prepareCall("{call UpdateRailwayKeymanBeatPathWithLocation(?,?,?,?,?,?,?,?,?,?,?,?)}");
				ps.setInt(1, beatId);
				ps.setInt(2, studentId);
				ps.setDouble(3, kmStart);
				ps.setDouble(4, kmEnd);
				ps.setString(5, sectionName);
				ps.setInt(6, parentId);
				ps.setDouble(7, kmStartLat);
				ps.setDouble(8, kmStartLang);
				ps.setDouble(9, kmEndLat);
				ps.setDouble(10, kmEndLang);
				ps.setInt(11, userLoginId);
				ps.setBoolean(12, isApprove);
//				
//				System.out.println("Parent-"+parentId+"---Student----"+studentId+"---KmStart---"+kmStart+"---KmEnd---"+kmEnd+
//					"---SectionName---"+sectionName+"---userLoginId---"+userLoginId+"---KmStartLat---"+kmStartLat+"---KmStartLang---"
//						+kmStartLang+"---KmEndLat---"+kmEndLat+"---KmEndLang"+kmEndLang+"---BeatId---"+beatId);

				int result = ps.executeUpdate();
				if (result == 0) {
					msgo.setError("true");
					msgo.setMessage("Beat was  not Updated");
				} else {
					// //System.err.println("Error=="+result);
					msgo.setError("false");
					msgo.setMessage("Beat was Updated Successfully");
		
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return msgo;

		}
	
	public MessageObject SaveRailwayPatrolManTripTimeMaster(Connection con,String tripName,
			String tripStartTime,String tripEndTime,int userLoginId, int parentId) {
		MessageObject msgo = new MessageObject();
		String Photo = "";
				
		try {
			
			String[] startTimeArray=tripStartTime.split(":");
			String[] endTimeArray=tripEndTime.split(":");
			        
			      int  startTimeStamp = 0 + (60 * Integer.parseInt(startTimeArray[1])) + (3600 * Integer.parseInt(startTimeArray[0]));
			      int  endTimeStamp = 0 + (60 * Integer.parseInt(endTimeArray[1])) + (3600 * Integer.parseInt(endTimeArray[0]));

			      System.err.println("startTimeStamp--"+startTimeStamp+"--endTimeStamp--"+endTimeStamp);
			      System.err.println("startTimeArray--"+startTimeArray[0]+"--startTimeArray--"+startTimeArray[1]);

			     int startTimeStampMapping=startTimeStamp;

			      if(startTimeStamp>endTimeStamp || startTimeStamp>43200) 
			    	  startTimeStampMapping=startTimeStamp-86400;
			    

			java.sql.CallableStatement ps = con.prepareCall("{call SaveRailwayPatrolManTripTimeMaster(?,?,?,?,?,?)}");
			ps.setString(1, tripName);
			ps.setString(2, tripStartTime+"-"+tripEndTime);
			if(startTimeStamp>endTimeStamp)
			ps.setInt(4, 86400-( startTimeStamp-endTimeStamp));
			else
				ps.setInt(4, endTimeStamp-startTimeStamp);

			ps.setInt(3, startTimeStampMapping);
			ps.setInt(5, userLoginId);
			ps.setInt(6, parentId);
			
			//System.out.println("TripName-"+tripName+"---TripStartTime----"+tripStartTime+"---TripEndTime---"+tripEndTime+"---userLoginId---"+userLoginId+"---parentId---"+parentId);
			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Time was  not Inserted");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Time was Inserted Successfully");
	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgo;
	}

	public ArrayList<String> GetSectionNames(Connection con,int parentId) {
		ArrayList<String> p = new ArrayList<String>();
		
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetSectionNames(?)}");
			ps.setInt(1, parentId);
			
			//System.out.println("---ParentId---"+parentId);
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

				p.add(rs.getString("Section").trim()+"");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public MessageObject SavePatrolManBeat(Connection con, ArrayList<RailwayPatrolManDTO> Beatlist) {

		MessageObject msgo = new MessageObject();
		String Photo = "";
		
		for (int i=0;i<Beatlist.size();i++)
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call SaveRailwayPetrolmanBeatPathWithLocation(?,?,?,?,?,?,?,?,?)}");
			ps.setInt(1, Beatlist.get(i).getStudentId());
			ps.setInt(2, Beatlist.get(i).getFk_TripMasterId());
			ps.setString(3, Beatlist.get(i).getKmStart()+"-"+Beatlist.get(i).getKmEnd());
			ps.setDouble(4, Beatlist.get(i).getKmStart());
			ps.setDouble(5, Beatlist.get(i).getKmEnd());
			ps.setDouble(6, Beatlist.get(i).getTotalKmCover());
			ps.setString(7, Beatlist.get(i).getSectionName());
			ps.setInt(8, Beatlist.get(i).getUserLoginId());
			ps.setInt(9, Beatlist.get(i).getSeasonId());
			  
		int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Beat was  not Inserted");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Beat was Inserted Successfully");
	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgo;
	}

	public ArrayList<RailwayPatrolManDTO> GetPatrolManExistingBeat(Connection con, int parentId, int studentId, int beatId,int userLoginId) {
		ArrayList<RailwayPatrolManDTO> p = new ArrayList<RailwayPatrolManDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetPatrolManExistingBeat(?,?,?,?)}");
			ps.setInt(1, parentId);
			ps.setInt(2, studentId);
			ps.setInt(3, beatId);
			ps.setInt(4, userLoginId);
			//System.out.println("---Parent---"+parentId+"---Student----"+studentId+"---beatId---"+beatId+"---userLoginId---"+userLoginId);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					RailwayPatrolManDTO Railpmandto= new RailwayPatrolManDTO();
					Railpmandto.setBeatId(rs.getInt("Id"));
					Railpmandto.setStudentId(rs.getInt("studentId"));
					Railpmandto.setFk_TripMasterId(rs.getInt("fk_TripMasterId"));
					Railpmandto.setDeviceID(rs.getString("deviceId"));
					Railpmandto.setKmFromTo(rs.getString("kmFromTo").trim());
					Railpmandto.setKmStart(rs.getDouble("kmStart"));
					Railpmandto.setKmStartLat(rs.getDouble("kmStartLat"));
					Railpmandto.setKmStartLang(rs.getDouble("kmStartLang"));
					Railpmandto.setKmEnd(rs.getDouble("kmEnd"));
					Railpmandto.setKmEndLat(rs.getDouble("kmEndLat"));
					Railpmandto.setKmEndLang(rs.getDouble("kmEndLang"));
					Railpmandto.setTotalKmCover(rs.getDouble("totalKmCover"));
					Railpmandto.setSectionName(rs.getString("SectionName").trim());
					Railpmandto.setTripName(rs.getString("tripName").trim());
					Railpmandto.setSeasonId(rs.getInt("SeasonId"));
					
					int seconds = rs.getInt("TripStartTimeAdd" );
			        int p1 = seconds % 60;
			        int p2 = seconds / 60;
			        int p3 = p2 % 60;
			        p2 = p2 / 60;
			        
					Railpmandto.setTripStartTime(p2+":"+p3);
					 seconds = rs.getInt("TripStartTimeAdd" )+rs.getInt("TripSpendTimeIntervalAdd" );
			         p1 = seconds % 60;
			         p2 = seconds / 60;
			         p3 = p2 % 60;
			        p2 = p2 / 60;
					Railpmandto.setTripEndTime(p2+":"+p3);
					Railpmandto.setTripSpendTimeIntervalAdd(rs.getString("TripSpendTimeIntervalAdd").trim());
					Railpmandto.setTripTimeShedule(rs.getString("TripTimeShedule").trim());
					Railpmandto.setTripStartTimeAdd(rs.getString("TripStartTimeAdd").trim());
					Railpmandto.setParentId(parentId);
					Railpmandto.setIsApprove(rs.getBoolean("ApprovedStatus"));
					p.add(Railpmandto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public MessageObject UpdatePatrolManBeat(Connection con, int beatId,int studentId, int fk_TripMasterId, double kmStart, double kmEnd,
			double totalKmCover, String sectionName, int userLoginId,Boolean isApprove,int seasonId) {
		
		MessageObject msgo = new MessageObject();
		String Photo = "";	
		
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call UpdateRailwayPatrolBeatPathWithLocation(?,?,?,?,?,?,?,?,?,?,?)}");
			ps.setInt(1, beatId);
			ps.setInt(2, studentId);
			ps.setInt(3, fk_TripMasterId);
			ps.setString(4, kmStart+"-"+kmEnd);
			ps.setDouble(5, kmStart);
			ps.setDouble(6, kmEnd);
			ps.setDouble(7, totalKmCover);
			ps.setString(8, sectionName);
			ps.setInt(9, userLoginId);
			ps.setBoolean(10, isApprove);
			ps.setInt(11, seasonId);
//			System.out.println("---BeatId---"+beatId+"---Student----"+studentId+"---fk_TripMasterId---"+fk_TripMasterId+"---KmStart---"+kmStart+
//			"---KmEnd---"+kmEnd+"---totalKmCover---"+totalKmCover+"---SectionName---"+sectionName+"---userLoginId---"+userLoginId);

			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Beat was  not Updated");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Beat was Updated Successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgo;
	}

	public ArrayList<RailwayPatrolManDTO> GetRailwayPetrolmanTripsMaster(Connection con, int parentId) {
		
		ArrayList<RailwayPatrolManDTO> p = new ArrayList<RailwayPatrolManDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetRailwayPetrolmanTripsMaster(?)}");
			ps.setInt(1, parentId);
			
			//System.out.println("---Parent---"+parentId);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					RailwayPatrolManDTO Railpmandto= new RailwayPatrolManDTO();
					Railpmandto.setFk_TripMasterId(rs.getInt("Id"));
					Railpmandto.setTripName(rs.getString("TripName").trim());
					Railpmandto.setTripTimeShedule(rs.getString("TripTimeShedule").trim());
					Railpmandto.setTripStartTimeAdd(rs.getString("TripStartTimeAdd").trim());
					Railpmandto.setTripSpendTimeIntervalAdd(rs.getString("TripSpendTimeIntervalAdd").trim());
					Railpmandto.setParentId(parentId);
					p.add(Railpmandto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public MessageObject SaveRailwayDeptHierarchy(Connection con, int deptId,String deptName, String emailId, String mobileNo,
			int deptParentId, String studentsNo, int parentId,int hirachyParentId, int userLoginId) {
		
		MessageObject msgo = new MessageObject();
		String Photo = "";
		
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call SaveTrackCustomhierarchyUserNew_Add_in_hierachy_AdminDashbord(?,?,?,?,?,?,?,?)}");
			ps.setInt(1, deptId);
			ps.setString(2, deptName);
			ps.setString(3, mobileNo);
			ps.setString(4, emailId);
			ps.setInt(5, parentId);
			ps.setString(6, studentsNo);
			ps.setInt(7, deptParentId);
			ps.setInt(8, userLoginId);
			  
//			System.out.println("---deptId----"+deptId+"---deptName---"+deptName+"---mobileNo---"+mobileNo+"---emailId---"+emailId+
//			"---parentId---"+parentId+"---studentsNo---"+studentsNo+"---deptParentId---"+deptParentId+"---hirachyParentId---"+hirachyParentId+"---createdBy---"+userLoginId);
			
			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Hierarchy was  not Inserted");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Hierarchy was Inserted Successfully");
	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgo;
	}

	public ArrayList<RailwayDeptHierarchyDTO> GetRailwayDeptHierarchy(Connection con,int parentId, int hirachyId,int userLoginId) {
		
		ArrayList<RailwayDeptHierarchyDTO> p = new ArrayList<RailwayDeptHierarchyDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetCustomhierarchyUser(?,?,?)}");
			ps.setInt(1, parentId);
			ps.setInt(2, hirachyId);
			ps.setInt(3, userLoginId);
			//System.out.println("---Parent---"+parentId+"---hierarchyId---"+hirachyId+"---userLoginId---"+userLoginId);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					RailwayDeptHierarchyDTO Rdpth= new RailwayDeptHierarchyDTO();
					Rdpth.setHirachyId(rs.getInt("Id"));
					Rdpth.setDeptId(rs.getInt("DeptId"));
					Rdpth.setDesignation((rs.getString("Designation")+"").trim());
					Rdpth.setDeptName((rs.getString("DeptName")+"").trim());
					Rdpth.setEmailId(rs.getString("EmailId").trim());
					Rdpth.setMobileNo((rs.getString("MobileNo")+"").trim());
					Rdpth.setDeptParentId(rs.getInt("DeptParentId"));
					Rdpth.setDeptParentName((rs.getString("DeptParentName")+"").trim());
					Rdpth.setStudentsNo((rs.getString("StudentsNo")+"").trim());
					Rdpth.setParentId(rs.getInt("ParentId"));
					Rdpth.setParentName(rs.getString("ParentName"));
					Rdpth.setHirachyParentId(rs.getInt("HirachyParentId"));
					Rdpth.setIsApprove(rs.getBoolean("ApprovedStatus"));
					Rdpth.setUserPassword(rs.getString("UserPassword"));
					/*Rdpth.setCreatedAt(rs.getInt("CreatedAt"));
					Rdpth.setCreatedBy(rs.getInt("CreatedBy"));
					Rdpth.setUpdatedAt(rs.getInt("UpdatedAt"));
					Rdpth.setUpdatedBy(rs.getInt("UpdatedBy"));
					Rdpth.setActiveStatus(rs.getString("ActiveStatus"));
*/
					p.add(Rdpth);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
		
	}

	public MessageObject UpdateRailwayDeptHierarchy(Connection con,int hierarchyId,int deptId,String Name, String emailId, String mobileNo,
			int deptParentId, String studentsNo, int parentId,int userLoginId,Boolean isApprove) {

		MessageObject msgo = new MessageObject();
		String Photo = "";
		
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call UpdateCustomhierarchyUserNew_Add_in_hierachy_AdminDashbord(?,?,?,?,?,?,?,?,?,?)}");
			ps.setInt(1, hierarchyId);
			ps.setInt(2, deptId);
			ps.setString(3, Name);
			ps.setString(4, mobileNo);
			ps.setString(5, emailId);
			ps.setInt(6, parentId);
			ps.setString(7, studentsNo);
			ps.setInt(8, deptParentId);
			ps.setInt(9, userLoginId);
			ps.setBoolean(10, isApprove);
			
			//System.out.println("---hierarchyId----"+hierarchyId+"---deptId---"+deptId+"---Name---"+Name+
//			"---mobileNo---"+mobileNo+"---emailId---"+emailId+"---parentId---"+parentId+
//			"---studentsNo---"+studentsNo+"---deptParentId---"+deptParentId+"---userLoginId---"+userLoginId);
			
			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Hierarchy was  not Updated");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Hierarchy was Updated Successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgo;
	}

	public ArrayList<DepartmentsDTO> GetDepartments(Connection con) {
		
		ArrayList<DepartmentsDTO> p = new ArrayList<DepartmentsDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetDepartments()}");
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					DepartmentsDTO DeptDTO = new DepartmentsDTO();
					DeptDTO.setName(rs.getString("Name"));
					DeptDTO.setDeptId(rs.getInt("deptId"));
					p.add(DeptDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public ArrayList<DeviceCommandDTO> GetDeviceCommand(Connection con,int userLoginId) {
		ArrayList<DeviceCommandDTO> p = new ArrayList<DeviceCommandDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetDeviceCommand(?)}");
			ps.setInt(1, userLoginId);
			//System.out.println("---userLoginId---"+userLoginId);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					DeviceCommandDTO DeviceDTO= new DeviceCommandDTO();
					DeviceDTO.setId(rs.getInt("Id"));
					DeviceDTO.setTitle(rs.getString("Title").trim());
					DeviceDTO.setCommand(rs.getString("Command").trim());
					DeviceDTO.setReply(rs.getString("Reply").trim());
					DeviceDTO.setDescription(rs.getString("Description").trim());
					DeviceDTO.setIsCustom(rs.getBoolean("isCustom"));
					DeviceDTO.setActiveStatus(rs.getString("ActiveStatus"));
					//Raildto.setIsApprove(rs.getBoolean("ApprovedStatus"));
					p.add(DeviceDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public ArrayList<DeviceIssueDTO> GetIssueMasetrList(Connection con,int userLoginId) {
		ArrayList<DeviceIssueDTO> p = new ArrayList<DeviceIssueDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetIssueMasetrList(?)}");
			ps.setInt(1, userLoginId);
						
			//System.out.println("---UserLoginId---"+userLoginId);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					DeviceIssueDTO issueDto= new DeviceIssueDTO();
					issueDto.setIsseMasterId(rs.getInt("Id"));
					issueDto.setIssueTitle(rs.getString("IssueTitle").toString());
					issueDto.setDescription(rs.getString("issueDesc").toString());
					p.add(issueDto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public MessageObject SaveDeviceIssue(Connection con, int isseMasterId,String contactPerson, String contactPersonMobNo, int issueStatus,
			int priority, int parentId, int studentId,String issueComment, int userLoginId, int isDeviceOn, int isDeviceButtonOn, int isBatteryOn, int isImeiSIMCorrect, int isGSMOn, int isGpsOn, String fileList) {
		MessageObject msgo = new MessageObject();
		String Photo = "";
		
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call SaveIssueDetails(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			ps.setInt(1, isseMasterId);
			ps.setString(2, contactPerson);
			ps.setString(3, contactPersonMobNo);
			ps.setInt(4, issueStatus);
			ps.setInt(5, priority);
			ps.setInt(6, userLoginId);
			ps.setInt(7, parentId);
			ps.setInt(8, studentId);
			ps.setString(9, issueComment);
			ps.setInt(10, isDeviceOn);
			ps.setInt(11, isDeviceButtonOn);
			ps.setInt(12, isBatteryOn);
			ps.setInt(13, isImeiSIMCorrect);
			ps.setInt(14, isGSMOn);
			ps.setInt(15, isGpsOn);
			ps.setString(16, fileList);
			ps.registerOutParameter(17,Types.INTEGER);

			
			//System.out.println("---isseMasterId----"+isseMasterId+"---contactPerson---"+contactPerson+"---contactPersonMobNo---"+contactPersonMobNo+"---issueStatus---"+issueStatus+
//			"---priority---"+priority+"---parentId---"+parentId+"---studentId---"+studentId+"---issueComment---"+issueComment+"---userLoginId---"+userLoginId+
//			"---isDeviceOn----"+isDeviceOn+"---isDeviceButtonOn---"+isDeviceButtonOn+"---isBatteryOn---"+isBatteryOn+"---isImeiSIMCorrect---"+isImeiSIMCorrect+
//			"---isGSMOn---"+isGSMOn+"---isGpsOn---"+isGpsOn+"--fileList--"+fileList);
			
			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Issue was  not Inserted");
			} else {
				msgo.setError("false");
				msgo.setMessage("Issue was Inserted Successfully");
				
				msgo.setId("PT"+String.format("%06d", ps.getInt(17)));

				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgo;
	}

	public MessageObject UpdateDeviceIssue(Connection con,int issueId,int issueStatus,int priority,int updatedBy,String issueComment, int isDeviceOn, int isDeviceButtonOn, int isBatteryOn, int isImeiSIMCorrect, int isGSMOn, int isGpsOn, String contactNo) {
		MessageObject msgo = new MessageObject();
		String Photo = "";	
		
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call UpdateIssueDetail(?,?,?,?,?,?,?,?,?,?,?,?)}");
			ps.setInt(1, issueId);
			ps.setInt(2, issueStatus);
			ps.setInt(3, priority);
			ps.setInt(4, updatedBy);
			ps.setString(5, issueComment);
			ps.setInt(6, isBatteryOn);
			ps.setInt(7, isDeviceOn);
			ps.setInt(8, isImeiSIMCorrect);
			ps.setInt(9, isGSMOn);
			ps.setInt(10, isDeviceButtonOn);
			ps.setInt(11, isGpsOn);
			ps.setString(12, contactNo);
//			
//			System.out.println("---issueId---"+issueId+"---issueStatus----"+issueStatus+"---priority---"+priority
//			+"---updatedBy---"+updatedBy+"---issueComment---"+issueComment+"---isDeviceOn----"+isDeviceOn+"---isDeviceButtonOn---"+isDeviceButtonOn+"---isBatteryOn---"+isBatteryOn+"---isImeiSIMCorrect---"+isImeiSIMCorrect+
//			"---isGSMOn---"+isGSMOn+"---isGpsOn---"+isGpsOn+"---contactNo---"+contactNo);

			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Issue was  not Updated");
			} else {
				msgo.setError("false");
				msgo.setMessage("Issue was Updated Successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgo;
	}

	public ArrayList<DeviceIssueDTO> GetIssueDetails(Connection con,int studentId, int issueId, int userLoginId, long startTime, long endTime) {
		ArrayList<DeviceIssueDTO> p = new ArrayList<DeviceIssueDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetIssueDetails(?,?,?,?,?)}");
			ps.setInt(1, studentId);
			ps.setInt(2, issueId);
			ps.setInt(3, userLoginId);
			if (startTime>0) {
				ps.setString(4,Common.getDateFromLong_in_mm_dd_yyyy_hh_MM(startTime,"start"));
				ps.setString(5,Common.getDateFromLong_in_mm_dd_yyyy_hh_MM(endTime,"end"));
			}else{
				ps.setString(4,"0");
				ps.setString(5,"0");
			}
			 
			
			//System.out.println("---UserLoginId---"+userLoginId+"---studentId---"+studentId+"---issueId---"+issueId+"---startTime---"+startTime+"---endTime---"+endTime);


			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					DeviceIssueDTO issueDto= new DeviceIssueDTO();
					issueDto.setIssueId(rs.getInt("IssueId"));
					issueDto.setIssueTitle(rs.getString("IssueTitle").toString().trim());
					issueDto.setContactPerson(rs.getString("ContactPerson").toString().trim());
					issueDto.setContactPersonMobNo(rs.getString("ContactPersonMobNo").toString().trim());
					issueDto.setIssueTime(rs.getString("IssueTime").toString().trim());
					issueDto.setIssueStatus(rs.getInt("IssueStatus"));
					issueDto.setIsSendMail(rs.getBoolean("IsSendmail"));
					issueDto.setPriority(rs.getInt("Priority"));
					issueDto.setCreatedBy(rs.getInt("CreatedBy"));
					issueDto.setCreatedAt(rs.getString("CreatedAt"));
					issueDto.setUpdatedBy(rs.getInt("UpdatedBy"));
					issueDto.setUpdatedAt(rs.getString("UpdatedAt"));
					issueDto.setParentId(rs.getInt("ParentId"));
					issueDto.setUpdatedBy(rs.getInt("StudentId"));
					issueDto.setIssueOwner(rs.getString("issueOwner").toString());
					issueDto.setIssueComment(rs.getString("IssueComment").toString().trim());
					issueDto.setIsBatteryOn(rs.getInt("IsBatteryOn"));
					issueDto.setIsDeviceOn(rs.getInt("IsDeviceOn"));
					issueDto.setIsImeiSIMCorrect(rs.getInt("IsImeiSIMCorrect"));
					issueDto.setIsGSMOn(rs.getInt("IsGSMOn"));
					issueDto.setIsDeviceButtonOn(rs.getInt("IsDeviceButtonOn"));
					issueDto.setIsGpsOn(rs.getInt("IsGpsOn"));
					issueDto.setUpdatedByName(rs.getString("UpdatedByName"));
					issueDto.setDeviceName(rs.getString("DeviceName"));
					issueDto.setDivisionName(rs.getString("DivisionName"));
					issueDto.setIssueTicketId("PT"+String.format("%06d", rs.getInt("IssueId")));
					
					ArrayList<IssueFileInfoDTO> issueFileList=new ArrayList<>();
					//get Issue related gfile
					try {
						java.sql.CallableStatement ps1 = con.prepareCall("{call GetIssueFileList(?)}");
						ps1.setInt(1, rs.getInt("IssueId"));	
						
						//

						ResultSet rs1 = ps1.executeQuery();
						if (rs1 != null) {
							while (rs1.next()) {
								//System.out.println("-GetIssueFileName-----"+rs1.getString("FileName"));
								IssueFileInfoDTO fileObj=new IssueFileInfoDTO();
								
								fileObj.setFileId(rs1.getInt("FileId"));
								fileObj.setFileName(rs1.getString("FileName"));
								fileObj.setFileUrl(rs1.getString("FilePath"));
								issueFileList.add(fileObj);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				 
					issueDto.setIssueFileList(issueFileList);
					p.add(issueDto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public ArrayList<DeviceExchangeDTO> UpdateDeviceExchange(DB mongoconnection, Connection con, int parentId,int studentId1,int studentId2,Boolean isDeviceSimExchange, int userLoginId) {
		ArrayList<DeviceExchangeDTO> p = new ArrayList<DeviceExchangeDTO>();
		String Photo = "";	
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call UpdateDeivceExchange(?,?,?,?,?)}");
			ps.setInt(1, parentId);
			ps.setInt(2, studentId1);
			ps.setInt(3, studentId2);
			ps.setBoolean(4, isDeviceSimExchange);
			ps.setInt(5, userLoginId);
			
//			System.out.println("---parentId---"+parentId+"---studentId1----"+studentId1+"---studentId2---"+studentId2
//			+"---isDeviceSimExchange---"+isDeviceSimExchange+"---userLoginId---"+userLoginId);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					DeviceExchangeDTO DExchDto= new DeviceExchangeDTO();
					DExchDto.setId(rs.getInt("Id"));
					DExchDto.setStudentId1(rs.getInt("StudentId1"));
					DExchDto.setStudentId2(rs.getInt("StudentId2"));
					DExchDto.setName1(rs.getString("Name1").toString());
					DExchDto.setName2(rs.getString("Name2").toString());
					DExchDto.setBeforeDeviceId1(rs.getString("BeforeDeviceId1").toString().trim());
					DExchDto.setBeforeDeviceId2(rs.getString("BeforeDeviceId2").toString().trim());
					DExchDto.setBeforDeviceSimNo1(rs.getString("BeforeDeviceSimNo1").toString().trim());
					DExchDto.setBeforDeviceSimNo2(rs.getString("BeforeDeviceSimNo2").toString().trim());
					DExchDto.setAfterDeviceId1(rs.getString("AfterDeviceId1").toString().trim());
					DExchDto.setAfterDeviceId2(rs.getString("AfterDeviceId2").toString().trim());
					DExchDto.setAfterDeviceSimNo1(rs.getString("AfterDeviceSimNo1").toString().trim());
					DExchDto.setAfterDeviceSimNo2(rs.getString("AfterDeviceSimNo2").toString().trim());
					DExchDto.setParentId(rs.getInt("ParentId"));
					DExchDto.setCreatedAt(rs.getString("CreatedAt").toString());
					DExchDto.setCreatedBy(rs.getInt("CreatedBy"));
					//DExchDto.setActiveStatus(rs.getBoolean("activeStatus"));
					DExchDto.setMessage("Device Exchange Sucessfully");
					DExchDto.setError("false");
					p.add(DExchDto);
					}
					DBCollection table = mongoconnection.getCollection(Common.TABLE_DEVICE);

					BasicDBObject newDocument = new BasicDBObject();
					newDocument.append("$set",new BasicDBObject().append("student",studentId1).append("parent",parentId)
								.append("blind", true));

					BasicDBObject searchQuery = new BasicDBObject().append("device",Long.parseLong(p.get(0).getAfterDeviceId1()));
					table.update(searchQuery, newDocument, true, false);
		

					newDocument = new BasicDBObject();
					newDocument.append("$set",new BasicDBObject().append("student",studentId2).append("parent",parentId).append("blind", true));

					searchQuery = new BasicDBObject().append("device",Long.parseLong(p.get(0).getAfterDeviceId2()));

					//table.update(searchQuery, newDocument);
					table.update(searchQuery, newDocument, true, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;

	}

	public ArrayList<DeviceExchangeDTO> GetDeviceExchange(Connection con,int parentId, int userLoginId) {
		ArrayList<DeviceExchangeDTO> p = new ArrayList<DeviceExchangeDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetDeviceExchangeListByParentId(?,?)}");
			ps.setInt(1, parentId);
			ps.setInt(2, userLoginId);
						
			//System.out.println("---parentId---"+parentId+"---UserLoginId---"+userLoginId);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					DeviceExchangeDTO ExcDto= new DeviceExchangeDTO();
					ExcDto.setId(rs.getInt("Id"));
					ExcDto.setStudentId1(rs.getInt("StudentId1"));
					ExcDto.setStudentId2(rs.getInt("StudentId2"));
					ExcDto.setName1(rs.getString("Name1").toString());
					ExcDto.setName2(rs.getString("Name2").toString());
					ExcDto.setBeforeDeviceId1(rs.getString("BeforeDeviceId1").toString().trim());
					ExcDto.setBeforeDeviceId2(rs.getString("BeforeDeviceId2").toString().trim());
					ExcDto.setBeforDeviceSimNo1(rs.getString("BeforeDeviceSimNo1").toString().trim());
					ExcDto.setBeforDeviceSimNo2(rs.getString("BeforeDeviceSimNo2").toString().trim());
					ExcDto.setAfterDeviceId1(rs.getString("AfterDeviceId1").toString().trim());
					ExcDto.setAfterDeviceId2(rs.getString("AfterDeviceId2").toString().trim());
					ExcDto.setAfterDeviceSimNo1(rs.getString("AfterDeviceSimNo1").toString().trim());
					ExcDto.setAfterDeviceSimNo2(rs.getString("AfterDeviceSimNo2").toString().trim());
					ExcDto.setParentId(rs.getInt("ParentId"));
					ExcDto.setCreatedAt(rs.getString("CreatedAt").toString());
					ExcDto.setCreatedBy(rs.getInt("CreatedBy"));
					//DExchDto.setActiveStatus(rs.getBoolean("activeStatus"));
					p.add(ExcDto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public MessageObject UpdateNewDeviceIMEI(DB mongoconnection,Connection con, String simNo, int parentId,
			int studentId, String imeiNo, String firstName) {
		MessageObject msgo = new MessageObject();
		String Photo = "";	
		
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call UpdateDevicesIMEIAndSimNo(?,?,?,?,?)}");
			ps.setString(1, simNo);
			ps.setInt(2, studentId);
			ps.setInt(3, parentId);
			ps.setString(4, imeiNo);
			ps.setString(5, firstName);
//			System.out.println("---simNo---"+simNo+"---parentId----"+parentId+"---studentId---"+studentId
//			+"---imeiNo---"+imeiNo);

			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("New Device was  not Updated");
			} else {
				msgo.setError("false");
				msgo.setMessage("New Device was Updated Successfully");
			}	
			DBCollection table = mongoconnection.getCollection(Common.TABLE_DEVICE);

			BasicDBObject newDocument = new BasicDBObject();
			newDocument.append("$set",new BasicDBObject().append("student",studentId).append("parent",parentId)
					.append("blind", true));

			BasicDBObject searchQuery = new BasicDBObject().append("device",Long.parseLong(imeiNo.trim()));

			table.update(searchQuery, newDocument, true, false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgo;
	}

	public ArrayList<DeviceExchangeDTO> UpdateWholeDeviceExchange(DB mongoconnection, Connection con, int parentId, int studentId1,int studentId2, int userLoginId) {
		ArrayList<DeviceExchangeDTO> p = new ArrayList<DeviceExchangeDTO>();
		String Photo = "";	
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call UpdateDeivceExchangeNameAndBeat(?,?,?,?)}");
			ps.setInt(1, parentId);
			ps.setInt(2, studentId1);
			ps.setInt(3, studentId2);
			ps.setInt(4, userLoginId);
			
//			System.out.println("---parentId---"+parentId+"---studentId1----"+studentId1+"---studentId2---"+studentId2
//			+"---userLoginId---"+userLoginId);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					DeviceExchangeDTO DExchDto= new DeviceExchangeDTO();
					DExchDto.setId(rs.getInt("Id"));
					DExchDto.setStudentId1(rs.getInt("StudentId1"));
					DExchDto.setStudentId2(rs.getInt("StudentId2"));
					DExchDto.setName1(rs.getString("Name1").toString());
					DExchDto.setName2(rs.getString("Name2").toString());
					DExchDto.setBeforeDeviceId1(rs.getString("BeforeDeviceId1").toString().trim());
					DExchDto.setBeforeDeviceId2(rs.getString("BeforeDeviceId2").toString().trim());
					DExchDto.setBeforDeviceSimNo1(rs.getString("BeforeDeviceSimNo1").toString().trim());
					DExchDto.setBeforDeviceSimNo2(rs.getString("BeforeDeviceSimNo2").toString().trim());
					DExchDto.setAfterDeviceId1(rs.getString("AfterDeviceId1").toString().trim());
					DExchDto.setAfterDeviceId2(rs.getString("AfterDeviceId2").toString().trim());
					DExchDto.setAfterDeviceSimNo1(rs.getString("AfterDeviceSimNo1").toString().trim());
					DExchDto.setAfterDeviceSimNo2(rs.getString("AfterDeviceSimNo2").toString().trim());
					DExchDto.setParentId(rs.getInt("ParentId"));
					DExchDto.setCreatedAt(rs.getString("CreatedAt").toString());
					DExchDto.setCreatedBy(rs.getInt("CreatedBy"));
					//DExchDto.setActiveStatus(rs.getBoolean("activeStatus"));
					DExchDto.setMessage("Device Exchange Name and Beat Sucessfully");
					DExchDto.setError("false");
					p.add(DExchDto);
					}
				DBCollection table = mongoconnection.getCollection(Common.TABLE_DEVICE);

				BasicDBObject newDocument = new BasicDBObject();
				newDocument.append("$set",new BasicDBObject().append("student",studentId1).append("parent",parentId)
								.append("blind", true));

				BasicDBObject searchQuery = new BasicDBObject().append("device",Long.parseLong(p.get(0).getAfterDeviceId1()));
				table.update(searchQuery, newDocument, true, false);
		

				 newDocument = new BasicDBObject();
				newDocument.append("$set",new BasicDBObject().append("student",studentId2).append("parent",parentId).append("blind", true));

				 searchQuery = new BasicDBObject().append("device",Long.parseLong(p.get(0).getAfterDeviceId2()));

				//table.update(searchQuery, newDocument);
				table.update(searchQuery, newDocument, true, false);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public DeviceDTO GetDeviceInfo(DB mongoconnection, Connection con,String studentId, int userLoginId, int issueId, String imeiNo) throws SQLException {
		DeviceDTO dtp = new DeviceDTO();
		try {
			//System.out.println("student---"+studentId+"--imei no--"+imeiNo);
			///Get device LOcation information
			dtp.setActivationDate("NA");
			if (imeiNo.length() > 0) {
				DBCollection table = mongoconnection.getCollection(Common.TABLE_LOCATION);
				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device", Long.parseLong(imeiNo));
				DBCursor cursor = table.find(device_whereQuery).sort(
				new BasicDBObject("timestamp", -1)).limit(1);
				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
				
				//System.out.println("Cursor Count of u-p11----" + cursor.size() + "  " + device_whereQuery);
			
				if(cursor.size()!=0)
				{
					while (cursor.hasNext())
					{
						DBObject bascObject = (DBObject) cursor.next();
						BasicDBObject dbObject = (BasicDBObject) bascObject.get("location");
		
						System.err.println("Loacftion-------"+new Gson().toJson(dbObject));
						dtp.setLan(Double.parseDouble(dbObject.get("lon").toString()));
						dtp.setLat(Double.parseDouble(dbObject.get("lat").toString()));
						dtp.setImeiNo(bascObject.get("device").toString());
						dtp.setLocationTime(Long.parseLong(bascObject.get("timestamp").toString()));
					}
				}	
			}
						
			///Get Device Battery and Range fro mongo db
				DBCollection table = mongoconnection.getCollection(Common.TABLE_ALERT_MSG);

				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device", Long.parseLong(imeiNo));
				DBCursor cursor = table.find(device_whereQuery);
				cursor.sort(new BasicDBObject("timestamp", -1)).limit(1);
				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
	
				System.out.print("getDeviceBattery Count of u-p---"+ cursor.size() + "  " + device_whereQuery);
				long total = cursor.count();

				if (cursor.size() != 0) 
				{				
					while (cursor.hasNext()) 
					{
						DBObject dbObject = (DBObject) cursor.next();
						
						dtp.setDeviceBattery(df2.format(((Double.parseDouble(dbObject.get("voltage_level")+"")) / 6) * 100)+" % ");
						dtp.setDeviceBatteryTime(Long.parseLong(dbObject.get("timestamp") + ""));
						dtp.setDeviceRange(df2.format(((Double.parseDouble(dbObject.get("gsm_signal_strength")+"")) / 4) * 100)+" % ");

						System.err.println("*-getKeyManStartBatteryStatus----timestamp---"
						+ Common.getDateCurrentTimeZone(Long.parseLong(dbObject.get("timestamp").toString())));	
					}
				}
			
			////-----------------------------------------------
			
			//Get Device BEat device info
			ArrayList<RailwayPatrolManDTO> beatInfoList=new ArrayList<>();
			
			java.sql.CallableStatement ps = con.prepareCall("{call GetDeviceBeatInfoFromStudent(?)}");
			ps.setInt(1, Integer.parseInt(studentId));
			
			//System.out.println("---Student----"+studentId);

			ResultSet rs = ps.executeQuery();
			if (rs != null) 
			{
				while (rs.next()) 
				{
					RailwayPatrolManDTO Railpmandto= new RailwayPatrolManDTO();
					Railpmandto.setKmStart(rs.getDouble("startKm"));
					Railpmandto.setKmEnd(rs.getDouble("kmEnd"));
					Railpmandto.setSectionName(rs.getString("SectionName").trim());
					Railpmandto.setDeviceType(rs.getString("deviceType").toString());
					dtp.setActivationDate(rs.getString("ActivationDate"));
					dtp.setPaymentStatus(rs.getString("PaymentStatus"));
					dtp.setExpiryDate(rs.getString("ExpiryDate"));
					beatInfoList.add(Railpmandto);
				}
			}
			dtp.setBeatInfoList(beatInfoList);
			///---------------------------------------------
			
			///Get Issue list of device
			 ArrayList<DeviceIssueDTO> issueList=new ArrayList<>();
				
				java.sql.CallableStatement ps1 = con.prepareCall("{call GetIssueDetails(?,?,?,?,?)}");
				ps1.setInt(1, Integer.parseInt(studentId));
				ps1.setInt(2, issueId);
				ps1.setInt(3, userLoginId);
				ps1.setString(4,"0");
				ps1.setString(5,"0");	
				//System.out.println("---UserLoginId---"+userLoginId+"---studentId---"+studentId+"---issueId---"+issueId);

				ResultSet rs1 = ps1.executeQuery();
				if (rs1 != null) {
					while (rs1.next()) {

						DeviceIssueDTO issueDto= new DeviceIssueDTO();
						issueDto.setIssueId(rs1.getInt("IssueId"));
						issueDto.setIssueTitle(rs1.getString("IssueTitle").toString());
						issueDto.setContactPerson(rs1.getString("ContactPerson").toString());
						issueDto.setContactPersonMobNo(rs1.getString("ContactPersonMobNo").toString());
						issueDto.setIssueTime(rs1.getString("IssueTime").toString());
						issueDto.setIssueStatus(rs1.getInt("IssueStatus"));
						issueDto.setIsSendMail(rs1.getBoolean("IsSendmail"));
						issueDto.setPriority(rs1.getInt("Priority"));
						issueDto.setCreatedBy(rs1.getInt("CreatedBy"));
						issueDto.setCreatedAt(rs1.getString("CreatedAt"));
						issueDto.setUpdatedBy(rs1.getInt("UpdatedBy"));
						issueDto.setUpdatedAt(rs1.getString("UpdatedAt"));
						issueDto.setParentId(rs1.getInt("ParentId"));
						issueDto.setUpdatedBy(rs1.getInt("StudentId"));
						issueDto.setIssueOwner(rs1.getString("issueOwner").toString());
						issueDto.setIssueComment(rs1.getString("IssueComment").toString());
						issueDto.setIsBatteryOn(rs1.getInt("IsBatteryOn"));
						issueDto.setIsDeviceOn(rs1.getInt("IsDeviceOn"));
						issueDto.setIsImeiSIMCorrect(rs1.getInt("IsImeiSIMCorrect"));
						issueDto.setIsGSMOn(rs1.getInt("IsGSMOn"));
						issueDto.setIsDeviceButtonOn(rs1.getInt("IsDeviceButtonOn"));
						issueDto.setIsGpsOn(rs1.getInt("IsGpsOn"));
						issueDto.setUpdatedByName(rs1.getString("UpdatedByName"));
						issueDto.setDeviceName(rs1.getString("DeviceName"));
						issueDto.setDivisionName(rs1.getString("DivisionName"));
						issueDto.setIssueTicketId("PT"+String.format("%06d", rs1.getInt("IssueId")));

						issueList.add(issueDto);
			}
		}
	 dtp.setIssueList(issueList);

			 ///----------------------------------------
			 
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dtp;
	}
	
	public ArrayList<DeviceCommandHistoryDTO> GetDeviceCommandHistory(
			DB mongoconnection, String deviceId, long startTime, long endTime) {
		ArrayList<DeviceCommandHistoryDTO>cmdList=new ArrayList<>();

		try{
		
			DBCollection table = mongoconnection.getCollection(Common.TABLE_COMMAND_HISTORY);
			BasicDBObject device_whereQuery = new BasicDBObject();
			
			if(Long.parseLong(deviceId) > 0){
			device_whereQuery.put("device", Long.parseLong(deviceId));
			}
			BasicDBObject timestamp_whereQuery = new BasicDBObject();

			if(startTime>0&&endTime>0){
				 timestamp_whereQuery = new BasicDBObject("timestamp", new BasicDBObject("$gte",
						startTime).append("$lte",endTime));
			}
			
			BasicDBList And_Milage = new BasicDBList();
			And_Milage.add(timestamp_whereQuery);
			And_Milage.add(device_whereQuery);
			DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

			DBCursor cursor = table.find(Total_Milage_query).sort(
			new BasicDBObject("timestamp", -1));
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
					
					
			//System.out.println("Cursor Count of u-p11----" + cursor.size() + "  " + Total_Milage_query);
				
			if(cursor.size()!=0)
			{
				while (cursor.hasNext())
				{
					DeviceCommandHistoryDTO dtp=new DeviceCommandHistoryDTO();

					DBObject bascObject = (DBObject) cursor.next();
	
//					System.err.println("history-------"+new Gson().toJson(bascObject));
					dtp.setName(bascObject.get("name")+"");
					dtp.setDeviceId(bascObject.get("device")+"");
					dtp.setCommand(bascObject.get("command")+"");
					dtp.setCommandDeliveredMsg(bascObject.get("delivered_msg")+"");
					dtp.setDeviceResponseTime(bascObject.get("device_response_time")+"");

					dtp.setDeviceCommandResponse(bascObject.get("device_response")+"");
					dtp.setLogin_name(bascObject.get("login_name")+"");
					dtp.setTimestamp(bascObject.get("timestamp")+"");
					dtp.setCommandId(bascObject.get("_id")+"");
					if (bascObject.containsField("student_id"))
						dtp.setStudentId((int) bascObject.get("student_id"));
					else dtp.setStudentId(0);


					cmdList.add(dtp);
				}
			}
		}catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cmdList;
	}


	public ArrayList<FeatureAddressDetailsDTO> GetRDPSInfo(Connection con,Float lat, Float lan, int parentId) {
		ArrayList<FeatureAddressDetailsDTO> p = new ArrayList<FeatureAddressDetailsDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetFeatureCodeswithDistForAdminAPI(?,?,?)}");
			ps.setFloat(1, lat);
			ps.setFloat(2, lan);
			ps.setInt(3, parentId);	
			
			//System.out.println("---lat---"+lat+"---lan---"+lan+"---parentId---"+parentId);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					FeatureAddressDetailsDTO FeaDto= new FeatureAddressDetailsDTO();
					FeaDto.setFeatureDetail(rs.getString("FeatureDetail").toString().trim());
					FeaDto.setKiloMeter(rs.getString("kiloMeter").toString().trim());
					FeaDto.setDistance(rs.getString("Distance").toString().trim());
					FeaDto.setLatitude(rs.getString("Latitude").toString().trim());
					FeaDto.setLongitude(rs.getString("Longitude").toString().trim());
					FeaDto.setFeatureCode(rs.getString("FeatureCode").toString().trim());
					FeaDto.setFeature_image(rs.getString("Images").toString().trim().replace("~", ""));
					FeaDto.setSection(rs.getString("Section").toString().trim());
					FeaDto.setBlockSection(rs.getString("BlockSection"));
					FeaDto.setParentId(rs.getInt("ParentID"));
					FeaDto.setNearByDistance(rs.getString("NearByDistance").toString().trim());
					p.add(FeaDto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;

	}

	public MessageObject GenerateSupportMail(DB mongoconnection, String toMail,String subject, String message, String ccMail) {
		MessageObject msgo = new MessageObject();
		//System.out.println("---toMail--"+toMail+"--subject--"+subject+"--message--"+message+"--ccMail--"+ccMail);
		
		SendEmail mail = new SendEmail();
		Boolean sentMail= mail.sendIssueMailToClient(toMail,subject,message,ccMail);
		
		if(sentMail){
			msgo.setError("false");
			msgo.setMessage("Message Sent Successfully");
		}
		else {
			msgo.setError("true");
			msgo.setMessage("Message Not sent");
			
		}
		 return msgo;
	}

	public ArrayList<MailFormatDTO> GetIssueMail(Connection con) {
		ArrayList<MailFormatDTO> p = new ArrayList<MailFormatDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetIssueMailFormat()}");
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					MailFormatDTO MailDTO = new MailFormatDTO();
					MailDTO.setMailId(rs.getInt("MailID"));
					MailDTO.setTempName(rs.getString("MailTempName")+" ");
					MailDTO.setSubject(rs.getString("Subject")+" ");
					MailDTO.setMessage(rs.getString("Paragraph1") + rs.getString("Paragraph2"));
					p.add(MailDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;

	}

	
	public ArrayList<DeviceBatteryDTO> GetBatteryDetailPowerOnOffInfo(DB mongoconnection,String deviceId,
			Long startdate, Long enddate) {
		 ArrayList<DeviceBatteryDTO> poweronoffList=new ArrayList<>();
		try {
			DBCollection table = mongoconnection.getCollection(Common.TABLE_ALERT_MSG);

			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device", Long.parseLong(deviceId));
			
			DBCursor cursor = table.find(device_whereQuery);
			cursor.sort(new BasicDBObject("timestamp", -1));//.limit(1);
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			 System.out.print("GetBatteryDetailPowerOnOffInfo Count of u-p---"+ cursor.size() + "  " + device_whereQuery);
			long total = cursor.count();

			if (cursor.size() != 0) {
				while (cursor.hasNext()) {

					DBObject dbObject = (DBObject) cursor.next();
					if (!dbObject.get("status").toString().equalsIgnoreCase("normal")) {
						
						DeviceBatteryDTO dto=new DeviceBatteryDTO();
						dto.setStatus(dbObject.get("status")+"");
						dto.setTimestamp(Long.parseLong(dbObject.get("timestamp")+""));
						dto.setNetwork(Integer.parseInt(dbObject.get("gsm_signal_strength")+""));
						poweronoffList.add(dto);
					}
					
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return poweronoffList;
	}

	public ArrayList<DevicePaymentDTO> GetDevicePaymentType(Connection con,int parentId) {
		ArrayList<DevicePaymentDTO> p = new ArrayList<DevicePaymentDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetPaymentTypesByParentId(?)}");
			ps.setInt(1, parentId);	
			
			//System.out.println("-GetPaymentTypes--parentId---"+parentId);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
									
					DevicePaymentDTO DevPayDTO = new DevicePaymentDTO();
					DevPayDTO.setId(rs.getInt("Id"));
					DevPayDTO.setPaymentType(rs.getString("PaymentType"));
					DevPayDTO.setAmountOfPlan(rs.getInt("AmountOfPlan"));
					DevPayDTO.setDaysOfplan(rs.getInt("DaysOfplan"));
					DevPayDTO.setMultiplicationFactor(rs.getInt("MultiplicationFactor"));
					p.add(DevPayDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public MessageObject MultipleDevicePayment(Connection con, String studentId, int parentId, int paymentTypeId, String currentStatus) {
		String[] studentId_array = studentId.split(",");
		MessageObject msgo = new MessageObject();
		
		try {
			System.err.println("studentId--"+studentId+"---"+parentId+"--"+paymentTypeId);

			for (int j = 0; j < studentId_array.length; j++) {

				java.sql.CallableStatement psaddDevice = con.prepareCall("{call SaveMultipleDevicePayment(?,?,?,?,?,?,?,?)}");
				psaddDevice.setInt(1, 1);
				psaddDevice.setInt(2,Integer.parseInt(studentId_array[j]));
				psaddDevice.setInt(3, 1);
				psaddDevice.setString(4, "00000000");
				psaddDevice.setInt(5, 0);
				// psaddDevice.setInt(6, 6); // free for month
				// psaddDevice.setInt(6, 7);//FRee for quater
				// psaddDevice.setInt(6, 5);//FRee for Year
				psaddDevice.setInt(6, paymentTypeId);// FRee for six month
				psaddDevice.setString(7, currentStatus);
				psaddDevice.setInt(8, 0);
				//System.out.println("------studentList----------"+ Integer.parseInt(studentId_array[j]));
				int result = psaddDevice.executeUpdate();

			}
			msgo.setError("false");
			msgo.setMessage("Payment added successfully.");

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("Payment not added successfully");
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("Payment not added successfully");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("Payment not added successfully");
		}
		return msgo;
	}

	public MessageObject DeleteKeymanBeat(Connection con, int beatId,int userLoginId) {
		MessageObject msgo = new MessageObject();
		String Photo = "";
		
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call DeleteRailwayKeymanBeatPath(?,?)}");
			ps.setInt(1, beatId);
			ps.setInt(2, userLoginId);
			//System.out.println("---BeatId---"+beatId+"---userLoginId---"+userLoginId);

			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Beat was  not Deleted");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Beat was Deleted Successfully");
	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgo;

	}

	public MessageObject DeletePatrolManBeat(Connection con, int beatId,int userLoginId) {
		MessageObject msgo = new MessageObject();
		String Photo = "";	
		
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call DeleteRailwayPatrolBeatPath(?,?)}");
			ps.setInt(1, beatId);
			ps.setInt(2, userLoginId);
			//System.out.println("---BeatId---"+beatId+"---userLoginId---"+userLoginId);

			
			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Beat was  not Deleted");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Beat was Deleted Successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgo;
	}

	public IssueFileInfoDTO UploadIssueFile(Connection con, String fileName,
			String fileURL, int userId) {
		IssueFileInfoDTO msg = new IssueFileInfoDTO();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call SaveAdminIssueFile(?,?,?,?)}");
			ps.setString(1, fileName);	
			ps.setString(2, fileURL);	
			ps.setInt(3, userId);
			 //Registering the type of the OUT parameters
			ps.registerOutParameter(4,Types.INTEGER);
			//System.out.println("-UploadIssueFile-----"+fileURL);

			int result = ps.executeUpdate();
			System.err.println("Error=="+result);
			if (result == 0) {
				
				msg.setFileId(0);
				msg.setFileName(fileName);
				msg.setFileUrl(fileURL);
				
			} else {
				// //System.err.println("Error=="+result);
			
				msg.setFileId(ps.getInt(4));
				msg.setFileName(fileName);
				msg.setFileUrl(fileURL);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	public MessageObject DeleteFileUpload(Connection con, int fileId,int userLoginId) {
		MessageObject msgo = new MessageObject();
		String Photo = "";	
		
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call DeleteIssueFile(?,?)}");
			ps.setInt(1, fileId);
			ps.setInt(2, userLoginId);
			
			//System.out.println("--DeleteFileUpload-fileId---"+fileId+"---userLoginId---"+userLoginId);

			
			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("File was  not Deleted");
			} else {
//				 System.err.println("filePath url--=="+ps.getString(3)+"");
				
				try {
					java.sql.CallableStatement ps1 = con.prepareCall("{call GetIssueFileName(?)}");
					ps1.setInt(1, fileId);	
					
					//

					ResultSet rs1 = ps1.executeQuery();
					if (rs1 != null) {
						while (rs1.next()) {
							//System.out.println("-GetIssueFileName-----"+rs1.getString("FileName"));
							 try  
							 {         
							 File f= new File(Common.ServerIssueFileUploadPath+rs1.getString("FileName"));           //file to be delete  
							 if(f.delete())                      //returns Boolean value  
							 {  
							 //System.out.println(f.getName() + " deleted");   //getting and printing the file name  
							 }  
							 else  
							 {  
							 //System.out.println("failed");  
							 }  
							 }  
							 catch(Exception e)  
							 {  
							 e.printStackTrace();  
							 } 
							
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			 
				msgo.setError("false");
				msgo.setMessage("File was Deleted Successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgo;
	}

	
	public ArrayList<DeviceInfoGatheringDTO> GetDeviceGatheringInfo(Connection con) {
		ArrayList<DeviceInfoGatheringDTO> p = new ArrayList<DeviceInfoGatheringDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetAllDeviceInfo()}");
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					
					DeviceInfoGatheringDTO DevInfoDTO = new DeviceInfoGatheringDTO();
					DevInfoDTO.setActivationDate(rs.getString("ActivationDate"));
					DevInfoDTO.setUserName(rs.getString("userName"));
					DevInfoDTO.setStudentId(rs.getInt("StudentId"));
					DevInfoDTO.setParentId(rs.getInt("ParentId"));
					DevInfoDTO.setDeviceName(rs.getString("deviceName"));
					DevInfoDTO.setDeviceId(rs.getString("deviceId"));
					DevInfoDTO.setDeviceSimNo(rs.getString("deviceSimNumber"));
					DevInfoDTO.setFirstName(rs.getString("firstName"));
					DevInfoDTO.setLastName(rs.getString("Lastname"));
					p.add(DevInfoDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}


	
	public ArrayList<DeviceDataDTO> getAllStudentsWithConnectedStatus(
			Connection con, DB mongoconnection, String parentId) {
		// TODO Auto-generated method stub
		ArrayList<DeviceDataDTO> AllDeviceDataList=new ArrayList<>();
		
		try{
			java.sql.CallableStatement ps= con.prepareCall("{call GetTrackInfo(?)}");
			ps.setString(1,parentId );
			ResultSet rs=ps.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					
					DeviceDataDTO dto=new DeviceDataDTO();
					dto.setStudent_id(rs.getString("StudentID"));
					dto.setImei_no(rs.getString("DeviceID"));
					dto.setName(""+rs.getString("Name"));
					dto.setPath(rs.getString("Photo"));
					//dto.setExpiary_date(rs.getString("ExpiryDate"));
					dto.setShowGoogleAddress(rs.getString("ShowGoogleAddress"));
					dto.setExpiary_date("");
					dto.setStatus(rs.getString("ActivationStatus"));
					dto.setRemaining_days_to_expire(rs.getString("ActivationStatus"));
					dto.setType(rs.getString("Type"));
					dto.setSimNo(rs.getString("DeviceSimNumber").toString());
					dto.setConnected(getDeviceIsConnected(dto.getImei_no(),mongoconnection));
					AllDeviceDataList.add(dto);
					
				
					
					
				}
			}
		}catch(Exception e){

			e.printStackTrace();
		}
		
		return AllDeviceDataList;
	}

	private Boolean getDeviceIsConnected(String imei_no, DB mongoconnection) {
		Boolean isDeviceConnect=false;
		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_DEVICE);
			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device", Long.parseLong(imei_no));
			DBCursor cursor = table.find(device_whereQuery);
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

//			 System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+device_whereQuery);

			if (cursor.size() != 0) {

//				 System.err.println("*-getAllStudentsWithConnectedStatus----COunt---"+cursor.size());
						while (cursor.hasNext()) {

							DBObject dbObject = (DBObject) cursor.next();
//							 System.err.println("*-getDeviceIsConnected----dbObject---"+dbObject);

							if (dbObject.containsKey("pid")&&dbObject.get("pid")!=null&&dbObject.get("pid").toString().length()>0&& 
									!dbObject.get("pid").equals("null"))
								isDeviceConnect=true;
							
	

					}

			}
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return isDeviceConnect;

	}
	public MessageObject GetPendingDeviceCommandHistory(
			DB mongoconnection) throws ParseException {
		MessageObject mObj=new MessageObject();
		ArrayList<DeviceCommandHistoryDTO>cmdList=new ArrayList<>();

	
		//Get Command List Which are pendings
		try{
	
			String doc = mongoconnection.doEval("getPendingCommandHistory()").toString();
		   org.json.JSONObject jobject = new org.json.JSONObject(doc);
		   org.json.JSONArray jRetvalArray=jobject.getJSONArray("retval");
		
		   for (int i = 0; i < jRetvalArray.length(); i++) {
			   org.json.JSONObject joObjLastComment=jRetvalArray.getJSONObject(i);
			   org.json.JSONObject joObj=joObjLastComment.getJSONObject("lastComment");

			   if (joObj.getString("delivered_msg").equals("device_is_not_connected"))
			   {
				
			
				   DeviceCommandHistoryDTO dtp=new DeviceCommandHistoryDTO();			   
				   dtp.setName(joObj.getString("name")+"");
				   dtp.setDeviceId(joObj.getLong("device")+"");
				   dtp.setCommand(joObj.getString("command")+"");
				   dtp.setCommandDeliveredMsg(joObj.getString("delivered_msg")+"");
//					dtp.setDeviceCommandResponse(joObj.getString("device_response")+"");
				   dtp.setLogin_name(joObj.getString("login_name")+"");
				   dtp.setTimestamp(joObj.getLong("timestamp")+"");
				   if (joObj.has("student_id"))
					dtp.setStudentId((int) joObj.getInt("student_id"));
				   else dtp.setStudentId(0);

				cmdList.add(dtp);
				
			   }
		}
			 
			 
		/*	DBCursor cursor = table.find(Total_Milage_query).sort(
			new BasicDBObject("timestamp", -1));
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);	
					
			//System.out.println("Cursor Count of u-p11----"  + "  " + Total_Milage_query);
				
			if(cursor.size()!=0)
			{
				while (cursor.hasNext())
				{
					DeviceCommandHistoryDTO dtp=new DeviceCommandHistoryDTO();

					DBObject bascObject = (DBObject) cursor.next();
	
//					System.err.println("history-------"+new Gson().toJson(bascObject));
					dtp.setName(bascObject.get("name")+"");
					dtp.setDeviceId(bascObject.get("device")+"");
					dtp.setCommand(bascObject.get("command")+"");
					dtp.setCommandDeliveredMsg(bascObject.get("delivered_msg")+"");
					dtp.setDeviceResponseTime(bascObject.get("device_response_time")+"");

					dtp.setDeviceCommandResponse(bascObject.get("device_response")+"");
					dtp.setLogin_name(bascObject.get("login_name")+"");
					dtp.setTimestamp(bascObject.get("timestamp")+"");
//					dtp.setCommandId(bascObject.get("_id")+"");
					if (bascObject.containsField("student_id"))
						dtp.setStudentId((int) bascObject.get("student_id"));
					else dtp.setStudentId(0);

					cmdList.add(dtp);
				}
			}*/
		}catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	 } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
		System.err.println("cmdList-------"+cmdList.size());

		// establish a connection 
        try
        { 
			Socket socket = new Socket("114.143.99.170", 4545); 
            //System.out.println("Connected"); 
 
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            //send Data to socket
           for (DeviceCommandHistoryDTO deviceCommandHistoryDTO : cmdList)
            {
            	if (getDeviceIsConnected(deviceCommandHistoryDTO.getDeviceId(),mongoconnection)) {
            		JSONObject joObj=new JSONObject();
    				joObj.put("event", "send_command");
    				joObj.put("student_id", deviceCommandHistoryDTO.getStudentId());
    				JSONObject joObjData=new JSONObject();
    				joObjData.put("command", deviceCommandHistoryDTO.getCommand());
    				joObjData.put("device", deviceCommandHistoryDTO.getDeviceId());
    				joObjData.put("deviceName",deviceCommandHistoryDTO.getName());
    				joObjData.put("loginName", deviceCommandHistoryDTO.getLogin_name()+"_AutoResend");
    				joObj.put("data", joObjData);
    				
    	
    		        //System.out.println("Sending string to the ServerSocket");

    		        // write the message we want to send
    		        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

    	            pw.println(joObj);
    	            pw.flush();pw.close();

					//System.out.println("Device cmd Resend "+deviceCommandHistoryDTO.getDeviceId()+"--"+deviceCommandHistoryDTO.getCommand());
				}else{
					//System.out.println("Device Is not connected "+deviceCommandHistoryDTO.getDeviceId());
				}
				
			}
            
            


           socket.close();
        } 
        catch(UnknownHostException u) 
        { 
            //System.out.println(u); 
        } 
        catch(IOException i) 
        { 
            //System.out.println(i); 
        } 
  
		
		
		return mObj;
	}
/*	public MessageObject GetPendingDeviceCommandHistory(
			DB mongoconnection) {
		MessageObject mObj=new MessageObject();
		ArrayList<DeviceCommandHistoryDTO>cmdList=new ArrayList<>();
		BasicDBList commandList=new BasicDBList();

		try {
			DBCollection CommandTable=mongoconnection.getCollection(Common.TABLE_COMMANDTOBESEND);		
				DBCursor cursor = CommandTable.find();
				
				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
				if(cursor.size()!=0)
				{
					while (cursor.hasNext())
					{
						DBObject bascObject = (DBObject) cursor.next();							
						commandList=(BasicDBList) bascObject.get("command_to_be_resend");	

					}
				}

		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		//Get Command List Which are pendings
		try{
			BasicDBObject CommandInQuery = new BasicDBObject();
			CommandInQuery.put("$in", commandList);
			BasicDBObject timestamp_whereQuery = new BasicDBObject();
//			timestamp_whereQuery = new BasicDBObject("timestamp", new BasicDBObject("$gte",
//						 (System.currentTimeMillis()/1000)-518400).append("$lte",System.currentTimeMillis()/1000));

			timestamp_whereQuery = new BasicDBObject("timestamp", new BasicDBObject("$gte",
					 (System.currentTimeMillis()/1000)-12000).append("$lte",System.currentTimeMillis()/1000));
	
			
			BasicDBList andMatch = new BasicDBList();
			andMatch.add(new BasicDBObject("command", CommandInQuery));
			andMatch.add(timestamp_whereQuery);
			
			DBObject match = new BasicDBObject("$match", new BasicDBObject("$and", andMatch)); 
			DBObject sort = new BasicDBObject("$sort", new BasicDBObject("timestamp", -1));
//			 DBObject project = new BasicDBObject("$project", new BasicDBObject("department", 1)
//			 .append("amount", 1));
//			
			 DBObject project = new BasicDBObject("$project", new BasicDBObject());

			 DBObject group = new BasicDBObject("$group", new BasicDBObject("_id", new BasicDBObject("command","$command").
					 append("device","$device")).append("lastComment", new BasicDBObject("$first","$$CURRENT" )));
			 
			 
			 MongoClient mongo = new MongoClient();
			 DB db = mongo.getDB("tracking");
			 DBCollection coll = db.getCollection(Common.TABLE_COMMAND_HISTORY);
			 

			 AggregationOutput output = coll.aggregate(match,project,group,sort).forEach(printBlock);

			for (DBObject result : output.results()) {
			 //System.out.println(result);
			 
			}
//			 com.mongodb.client.MongoClient mongoClient = MongoClients.create("mongodb://114.143.99.170:19209");
//			 MongoClient  mongoClient = new MongoClient("114.143.99.170", 19209);

//			 MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://114.143.99.170:19209"));

			 
//			 MongoClient mongoClient = MongoClients.create();
			 MongoDatabase database = mongo.getDatabase("tracking");
			 MongoCollection<Document> collection = database.getCollection(Common.TABLE_COMMAND_HISTORY);
			 
			 AggregateIterable<Document> aggregateIterable = collection.aggregate(
					  Arrays.asList(
					          Aggregates.match((Bson) match),
					          Aggregates.group((Bson) group),
					          Aggregates.sort((Bson) sort)
					          
					  )
					);
			
			for (Document result : aggregateIterable) {
			    //System.out.println(result.toJson());
			}
			
			
			


// CommandResult doc = mongoconnection.command(command);
 String doc = mongoconnection.doEval("getPendingCommandHistory()").toString();

 
 System.err.println("getPendingCommandHistory---"+doc);
			 
			DBCursor cursor = table.find(Total_Milage_query).sort(
			new BasicDBObject("timestamp", -1));
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);	
					
			//System.out.println("Cursor Count of u-p11----"  + "  " + Total_Milage_query);
				
			if(cursor.size()!=0)
			{
				while (cursor.hasNext())
				{
					DeviceCommandHistoryDTO dtp=new DeviceCommandHistoryDTO();

					DBObject bascObject = (DBObject) cursor.next();
	
//					System.err.println("history-------"+new Gson().toJson(bascObject));
					dtp.setName(bascObject.get("name")+"");
					dtp.setDeviceId(bascObject.get("device")+"");
					dtp.setCommand(bascObject.get("command")+"");
					dtp.setCommandDeliveredMsg(bascObject.get("delivered_msg")+"");
					dtp.setDeviceResponseTime(bascObject.get("device_response_time")+"");

					dtp.setDeviceCommandResponse(bascObject.get("device_response")+"");
					dtp.setLogin_name(bascObject.get("login_name")+"");
					dtp.setTimestamp(bascObject.get("timestamp")+"");
					dtp.setCommandId(bascObject.get("_id")+"");
					if (bascObject.containsField("student_id"))
						dtp.setStudentId((int) bascObject.get("student_id"));
					else dtp.setStudentId(0);

					cmdList.add(dtp);
				}
			}
		}catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
		System.err.println("cmdList-------"+cmdList.size());

		// establish a connection 
        try
        { 
			Socket socket = new Socket("114.143.99.170", 4545); 
            //System.out.println("Connected"); 
  
//            // takes input from terminal 
//            DataInputStream input = new DataInputStream(System.in); 
//  
            // sends output to the socket 
        //    DataOutputStream  dataOutputStream = new DataOutputStream(socket.getOutputStream());
            
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

           

       
            
            
            //send Data to socket
            for (DeviceCommandHistoryDTO deviceCommandHistoryDTO : cmdList)
            {
            	if (getDeviceIsConnected(deviceCommandHistoryDTO.getDeviceId(),mongoconnection)) {
            		JSONObject joObj=new JSONObject();
    				joObj.put("event", "send_command");
    				joObj.put("student_id", deviceCommandHistoryDTO.getStudentId());
    				JSONObject joObjData=new JSONObject();
    				joObjData.put("command", deviceCommandHistoryDTO.getCommand());
    				joObjData.put("device", deviceCommandHistoryDTO.getDeviceId());
    				joObjData.put("deviceName",deviceCommandHistoryDTO.getName());
    				joObjData.put("loginName", deviceCommandHistoryDTO.getLogin_name()+"_Resend");
    				joObj.put("data", joObjData);
    				
    	
    		        //System.out.println("Sending string to the ServerSocket");

    		        // write the message we want to send
    		        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

    	            pw.println(joObj);
    	            pw.flush();pw.close();

					//System.out.println("Device cmd Resend "+deviceCommandHistoryDTO.getDeviceId()+"--"+deviceCommandHistoryDTO.getCommand());
				}else{
					//System.out.println("Device Is not connected "+deviceCommandHistoryDTO.getDeviceId());
				}
				
			}
            
            


          //  socket.close();
        } 
        catch(UnknownHostException u) 
        { 
            //System.out.println(u); 
        } 
        catch(IOException i) 
        { 
            //System.out.println(i); 
        } 
  
		
		
		return mObj;
	}*/

	public ArrayList<DevicePaymentInfoDetailsDTO> GetDevicePaymentInfoForAdmin(
			Connection con, int parentId) {
ArrayList<DevicePaymentInfoDetailsDTO> AllDeviceDataList=new ArrayList<>();
		
		try{
			java.sql.CallableStatement ps= con.prepareCall("{call GetDevicePaymentInfoForAdmin(?)}");
			ps.setInt(1,parentId);
			ResultSet rs=ps.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					DevicePaymentInfoDetailsDTO dto=new DevicePaymentInfoDetailsDTO();
					dto.setStudentID(rs.getString("StudentID"));
					dto.setUserPayId(rs.getString("UserPayId"));
					dto.setDeviceID(""+rs.getString("DeviceID"));
					dto.setPaymentPlanID(rs.getString("PaymentPlanID"));
					//dto.setExpiary_date(rs.getString("ExpiryDate"));

					dto.setPaymentType("PaymentType");
					dto.setCountryID(rs.getString("CountryID"));
					dto.setPayRenDate(rs.getString("PayRenDate"));
					dto.setPaymentMode(rs.getString("PaymentMode"));
					dto.setChequeNo(rs.getString("ChequeNo"));
					dto.setCashAmount(rs.getString("CashAmount"));
					dto.setTransactionNo("TransactionNo");
					dto.setExpiryDate(rs.getString("ExpiryDate"));
					dto.setFullName(rs.getString("FullName"));
					dto.setName(rs.getString("Name"));
					
					dto.setEmailID("EmailID");
					dto.setMobileNo(rs.getString("MobileNo"));
					dto.setPlanName(rs.getString("PlanName"));
					dto.setPlanType(rs.getString("PlanType"));
					dto.setCountryName("CountryName");
					if(rs.getInt("DiffDate")>=0)
						dto.setDiffDate(rs.getString("DiffDate"));
					else 
						dto.setDiffDate("0");
					
					AllDeviceDataList.add(dto);
					
				
					
					
				}
			}
		}catch(Exception e){

			e.printStackTrace();
		}
		
		return AllDeviceDataList;
	}

	public MessageObject UpsertStudentMasterSQL(Connection con,Connection conSunMssql) {
		MessageObject msgo = new MessageObject();
		ArrayList<StudentMasterDTO> StudentList=new ArrayList<>();
		try{
			java.sql.CallableStatement ps= con.prepareCall("{call GetStudentMasterDataToSyncUp()}");
			ResultSet rs=ps.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
													
					StudentMasterDTO dto=new StudentMasterDTO();
					dto.setStudentId(rs.getInt("StudentID"));
					dto.setFirstName(rs.getString("FirstName"));
					dto.setMiddleName(rs.getString("MiddleName"));
					dto.setLastName(rs.getString("LastName"));
					dto.setGender(rs.getString("Gender"));
					dto.setAdmissionDate(rs.getString("AdmissionDate"));
					dto.setParentID(rs.getInt("ParentID"));
					dto.setClassId(rs.getString("ClassID"));
					dto.setDeviceId(rs.getString("DeviceID"));
					dto.setDob(rs.getString("dob"));												
					dto.setPrimContactNo(rs.getString("Prim_ContactNo"));
					dto.setPrimEmailId(rs.getString("Prim_EmailID"));
					dto.setAddress(rs.getString("Address"));
					dto.setStreet(rs.getString("street"));
					dto.setCity(rs.getString("City"));
					dto.setState(rs.getString("State"));
					dto.setPhoto(rs.getString("Photo"));
					dto.setTimeOffset(rs.getInt("TimeOffset"));
					dto.setActiveStatus(rs.getString("ActiveStatus"));
					dto.setCreatedBy(rs.getString("CreatedBy"));
					dto.setUpdatedDate(rs.getString("UpdatedDate"));
					dto.setParentName1(rs.getString("ParentName1"));
					dto.setRelation1(rs.getString("Relation1"));
					dto.setSchoolId(rs.getInt("school_id"));
					dto.setParentName2(rs.getString("ParentName2"));
					dto.setRelation2(rs.getString("Relation2"));
					dto.setType(rs.getString("Type"));
					dto.setDeviceType(rs.getInt("DeviceType"));
					dto.setDeviceSimNumber(rs.getString("DeviceSimNumber"));
					dto.setvSEnabled(rs.getString("VSEnabled"));
					dto.setvSCallback(rs.getString("VSCallback"));
					dto.setTripReportAllow(rs.getInt("TripReportAllow"));
					dto.setActivationDate(rs.getString("ActivationDate"));
					dto.setActivationStatus(rs.getInt("ActivationStatus"));
					dto.setPlanType(rs.getInt("PlanType"));
					dto.setPaymentMode(rs.getInt("PaymentMode"));
					dto.setCreditName(rs.getString("CreditName"));
					dto.setPaymentDate(rs.getString("PaymentDate"));
					dto.setTransactionID(rs.getString("TransactionID"));
					dto.setPayAmount(rs.getInt("PayAmount"));
					dto.setShowGoogleAddress(rs.getInt("ShowGoogleAddress"));
					dto.setSection(rs.getString("Section"));
					dto.setDeviceType(rs.getInt("DeviceNameType"));		
					StudentList.add(dto);
				}
			
			}
		}catch(Exception e){

			e.printStackTrace();
		}
		System.err.println("Student list--"+StudentList.size());
		for (StudentMasterDTO stud : StudentList) {
			try {
				//System.out.println("Student---"+stud.getStudentId());
				java.sql.CallableStatement ps1 = conSunMssql.prepareCall("{call UpsertStudentMasterDataToSyncUp(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
				
				ps1.setInt(1, stud.getStudentId());
				ps1.setString(2, stud.getFirstName());
				ps1.setString(3, stud.getMiddleName());
				ps1.setString(4, stud.getLastName());
				ps1.setString(5, stud.getGender());
				ps1.setString(6, stud.getAddress());
				ps1.setInt(7, stud.getParentID());
				ps1.setString(8, stud.getClassId());
				ps1.setString(9, stud.getDeviceId());
				ps1.setString(10, stud.getDob());
				ps1.setString(11, stud.getPrimContactNo());
				ps1.setString(12, stud.getPrimEmailId());
				ps1.setString(13, stud.getAddress());
				ps1.setString(14, stud.getStreet());
				ps1.setString(15, stud.getCity());
				ps1.setString(16, stud.getState());
				ps1.setString(17, stud.getPhoto());
				ps1.setInt(18, stud.getTimeOffset());
				ps1.setString(19, stud.getActiveStatus());
				ps1.setString(20, stud.getCreatedBy());
				ps1.setString(21, stud.getUpdatedDate());
				ps1.setString(22, stud.getParentName1());
				ps1.setString(23, stud.getRelation1());
				ps1.setInt(24, stud.getSchoolId());
				ps1.setString(25, stud.getParentName2());
				ps1.setString(26, stud.getRelation2());
				ps1.setString(27, stud.getType());
				ps1.setInt(28, stud.getDeviceType());
				ps1.setString(29, stud.getDeviceSimNumber());
				ps1.setString(30, stud.getvSEnabled());
				ps1.setString(31, stud.getvSCallback());
				ps1.setInt(32, stud.getTripReportAllow());
				ps1.setString(33, stud.getActivationDate());
				ps1.setInt(34, stud.getActivationStatus());
				ps1.setInt(35, stud.getPlanType());
				ps1.setInt(36, stud.getPaymentMode());
				ps1.setString(37, stud.getCreditName());
				ps1.setString(38, stud.getPaymentDate());
				ps1.setString(39, stud.getTransactionID());
				ps1.setInt(40, stud.getPayAmount());
				ps1.setInt(41, stud.getShowGoogleAddress());
				ps1.setString(42, stud.getSection());
				ps1.setInt(43, stud.getDeviceNameType());
			

				int result = ps1.executeUpdate();
				if (result == 0) {
					msgo.setError("true");
					msgo.setMessage("Upsert Student not Inserted");
				} else {
					// //System.err.println("Error=="+result);
					msgo.setError("false");
					//System.out.println("Student---"+stud.getStudentId()+" update succesfylly");
					msgo.setMessage("Upsert Student Inserted Successfully");
				}
				
				
		
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return msgo;
	}

	public MessageObject UpsertRailwayKeymanBeatPathSQL(Connection con,Connection conSunMssql) {
		MessageObject msgo = new MessageObject();
		ArrayList<RailwayKeymanDTO> KeymanList=new ArrayList<>();
		try{
			java.sql.CallableStatement ps= con.prepareCall("{call GetRailwayKeymanBeatPathToSyncUp()}");
			ResultSet rs=ps.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
													
					RailwayKeymanDTO dto=new RailwayKeymanDTO();
					dto.setId(rs.getInt("Id"));
					dto.setStudentId(rs.getInt("StudentId"));
					dto.setKmStart(rs.getDouble("KmStart"));
					dto.setKmEnd(rs.getDouble("KmEnd"));
					dto.setSectionName(rs.getString("SectionName"));
					dto.setCreatedBy(rs.getInt("CreatedBy"));
					dto.setCreatedAt(rs.getString("CreatedAt"));
					dto.setUpdatedBy(rs.getInt("UpdatedBy"));
					dto.setUpdatedAt(rs.getString("UpdatedAt"));
					dto.setActiveStatus(rs.getInt("ActiveStatus"));
					dto.setParentId(rs.getInt("parentId"));
					dto.setStart_Lat(rs.getDouble("Start_Lat"));
					dto.setStart_Lon(rs.getDouble("Start_Lon"));
					dto.setEnd_Lat(rs.getDouble("End_Lat"));
					dto.setEnd_Lon(rs.getDouble("End_Lon"));
					dto.setIsApprove(rs.getBoolean("ApprovedStatus"));;
					dto.setIsBeatPoleAvilableRDPS(rs.getBoolean("IsBeatPoleAvilableRDPS"));
					KeymanList.add(dto);
				}							
			}
		}catch(Exception e){

			e.printStackTrace();
		}
		System.err.println("KeyMan list--"+KeymanList.size());
		for (RailwayKeymanDTO keylist : KeymanList) {
			try {
				//System.out.println("KeyMan---"+keylist.getId());
				java.sql.CallableStatement ps1 = conSunMssql.prepareCall("{call UpsertRailwayKeymanBeatPathDataToSyncUp(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
				
				ps1.setInt(1, keylist.getId());
				ps1.setInt(2, keylist.getStudentId());			
				ps1.setDouble(3, keylist.getKmStart());
				ps1.setDouble(4, keylist.getKmEnd());
				ps1.setString(5, keylist.getSectionName());
				ps1.setInt(6, keylist.getCreatedBy());
				ps1.setString(7, keylist.getCreatedAt());
				ps1.setInt(8, keylist.getUpdatedBy());
				ps1.setString(9, keylist.getUpdatedAt());
				ps1.setInt(10, keylist.getActiveStatus());
				ps1.setInt(11, keylist.getParentId());
				ps1.setDouble(12, keylist.getStart_Lat());
				ps1.setDouble(13, keylist.getStart_Lon());
				ps1.setDouble(14, keylist.getEnd_Lat());
				ps1.setDouble(15, keylist.getEnd_Lon());
				ps1.setBoolean(16, keylist.getIsApprove());
				ps1.setBoolean(17, keylist.getIsBeatPoleAvilableRDPS());
				int result = ps1.executeUpdate();
				if (result == 0) {
					msgo.setError("true");
					msgo.setMessage("Upsert Railway Keyman Beat not Inserted");
				} else {
					// //System.err.println("Error=="+result);
					msgo.setError("false");
					//System.out.println("KeyMan---"+keylist.getId()+" update succesfylly");
					msgo.setMessage("Upsert Railway Keyman Beat Inserted Successfully");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return msgo; 
	}

	public ArrayList<DeviceIssueDTO> GetClientEnteredIssueDetailsForAdmin(
			Connection con, int studentId, int issueId, int userLoginId,
			long startTime, long endTime) {
		ArrayList<DeviceIssueDTO> p = new ArrayList<DeviceIssueDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetClientEnteredIssueDetailsForAdmin(?,?,?,?,?)}");
			ps.setInt(1, studentId);
			ps.setInt(2, issueId);
			ps.setInt(3, userLoginId);
			if (startTime>0) {
				ps.setString(4,Common.getDateFromLong_in_mm_dd_yyyy_hh_MM(startTime,"start"));
				ps.setString(5,Common.getDateFromLong_in_mm_dd_yyyy_hh_MM(endTime,"end"));
			}else{
				ps.setString(4,"0");
				ps.setString(5,"0");
			}
			 
			
			//System.out.println("---UserLoginId---"+userLoginId+"---studentId---"+studentId+"---issueId---"+issueId+"---startTime---"+startTime+"---endTime---"+endTime);


			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					DeviceIssueDTO issueDto= new DeviceIssueDTO();
					issueDto.setIssueId(rs.getInt("IssueId"));
					issueDto.setIssueTitle(rs.getString("IssueTitle").toString().trim());
					issueDto.setContactPerson(rs.getString("ContactPerson").toString().trim());
					issueDto.setContactPersonMobNo(rs.getString("ContactPersonMobNo").toString().trim());
					issueDto.setIssueTime(rs.getString("IssueTime").toString().trim());
					issueDto.setIssueStatus(rs.getInt("IssueStatus"));
					issueDto.setIsSendMail(rs.getBoolean("IsSendmail"));
					issueDto.setPriority(rs.getInt("Priority"));
					issueDto.setCreatedBy(rs.getInt("CreatedBy"));
					issueDto.setCreatedAt(rs.getString("CreatedAt"));
					issueDto.setUpdatedBy(rs.getInt("UpdatedBy"));
					issueDto.setUpdatedAt(rs.getString("UpdatedAt"));
					issueDto.setParentId(rs.getInt("ParentId"));
					issueDto.setUpdatedBy(rs.getInt("StudentId"));
					issueDto.setIssueOwner(rs.getString("issueOwner").toString());
					issueDto.setIssueComment(rs.getString("IssueComment").toString().trim());
					issueDto.setIsBatteryOn(rs.getInt("IsBatteryOn"));
					issueDto.setIsDeviceOn(rs.getInt("IsDeviceOn"));
					issueDto.setIsImeiSIMCorrect(rs.getInt("IsImeiSIMCorrect"));
					issueDto.setIsGSMOn(rs.getInt("IsGSMOn"));
					issueDto.setIsDeviceButtonOn(rs.getInt("IsDeviceButtonOn"));
					issueDto.setIsGpsOn(rs.getInt("IsGpsOn"));
					issueDto.setUpdatedByName(rs.getString("UpdatedByName"));
					issueDto.setDeviceName(rs.getString("DeviceName"));
					issueDto.setDivisionName(rs.getString("DivisionName"));
					issueDto.setIssueTicketId("PT"+String.format("%06d", rs.getInt("IssueId")));
					issueDto.setDeviceId(rs.getString("deviceId"));
					ArrayList<IssueFileInfoDTO> issueFileList=new ArrayList<>();
					//get Issue related gfile
					try {
						java.sql.CallableStatement ps1 = con.prepareCall("{call GetIssueFileList(?)}");
						ps1.setInt(1, rs.getInt("IssueId"));	
						
						//

						ResultSet rs1 = ps1.executeQuery();
						if (rs1 != null) {
							while (rs1.next()) {
								//System.out.println("-GetIssueFileName-----"+rs1.getString("FileName"));
								IssueFileInfoDTO fileObj=new IssueFileInfoDTO();
								
								fileObj.setFileId(rs1.getInt("FileId"));
								fileObj.setFileName(rs1.getString("FileName"));
								fileObj.setFileUrl(rs1.getString("FilePath"));
								issueFileList.add(fileObj);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				 
					issueDto.setIssueFileList(issueFileList);
					p.add(issueDto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public MessageObject UpsertRailwayPatrolManBeatPathSQL(Connection con,
			Connection conSunMssql) {
		MessageObject msgo = new MessageObject();
		ArrayList<RailwayPatrolManDTO> PatrolmanList=new ArrayList<>();
		try{
			java.sql.CallableStatement ps= con.prepareCall("{call GetRailwayPatrolBeatPathToSyncUp()}");
			ResultSet rs=ps.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
													
					RailwayPatrolManDTO dto=new RailwayPatrolManDTO();
					dto.setId(rs.getInt("Id"));
					dto.setStudentId(rs.getInt("StudentId"));
					dto.setFk_TripMasterId(rs.getInt("fk_TripMasterId"));
					dto.setKmFromTo(rs.getString("kmFromTo"));
					dto.setKmStart(rs.getDouble("KmStart"));
					dto.setKmEnd(rs.getDouble("KmEnd"));
					dto.setSheetNo(rs.getInt("SheetNo"));

					dto.setSectionName(rs.getString("SectionName"));
					dto.setTotalKmCover(Math.abs(dto.getKmEnd()-dto.getKmStart()));
					dto.setSeasonId(rs.getInt("SeasonId"));
					dto.setCreatedBy(rs.getInt("CreatedBy"));
					dto.setCreatedAt(rs.getString("CreatedAt"));
					dto.setUpdatedBy(rs.getInt("UpdatedBy"));
					dto.setUpdatedAt(rs.getString("UpdatedAt"));
					dto.setActiveStatus(rs.getInt("ActiveStatus"));
					dto.setParentId(rs.getInt("parentId"));
	
					dto.setIsApprove(rs.getBoolean("ApprovedStatus"));;
					PatrolmanList.add(dto);
				}							
			}
		}catch(Exception e){

			e.printStackTrace();
		}
		System.err.println("PAtrolman list--"+PatrolmanList.size());
		for (RailwayPatrolManDTO keylist : PatrolmanList) {
			try {
				//System.out.println("PAtrolman---"+keylist.getId());
				java.sql.CallableStatement ps1 = conSunMssql.prepareCall("{call UpsertRailwayPatrolmanBeatPathDataToSyncUp(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
				
				ps1.setInt(1, keylist.getId());
				ps1.setInt(2, keylist.getStudentId());	
				ps1.setInt(3, keylist.getFk_TripMasterId());
				ps1.setString(4, keylist.getKmFromTo());		
				ps1.setDouble(5, keylist.getKmStart());
				ps1.setDouble(6, keylist.getKmEnd());
				ps1.setDouble(7, keylist.getTotalKmCover());
				ps1.setInt(8, keylist.getSheetNo());
				ps1.setString(9, keylist.getSectionName());
				ps1.setInt(10, keylist.getCreatedBy());
				ps1.setString(11, keylist.getCreatedAt());
				ps1.setInt(12, keylist.getUpdatedBy());
				ps1.setString(13, keylist.getUpdatedAt());
				ps1.setInt(14, keylist.getActiveStatus());
				ps1.setBoolean(15, keylist.getIsApprove());

				ps1.setInt(16, keylist.getParentId());
				ps1.setInt(17, keylist.getSeasonId());
				
				int result = ps1.executeUpdate();
				if (result == 0) {
					msgo.setError("true");
					msgo.setMessage("Upsert Railway Patrolman Beat not Inserted");
				} else {
					// //System.err.println("Error=="+result);
					msgo.setError("false");
					//System.out.println("KeyMan---"+keylist.getId()+" update succesfylly");
					msgo.setMessage("Upsert Railway Patrolman Beat Inserted Successfully");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return msgo; 
	}

	public MessageObject UpsertRailwayPatrolManBeatMasterPathSQL(
			Connection con, Connection conSunMssql) {
		MessageObject msgo = new MessageObject();
		ArrayList<RailwayPatrolManripMasterDTO> PatrolmanList=new ArrayList<>();
		try{
			java.sql.CallableStatement ps= con.prepareCall("{call GetRailwayPatrolBeatMasterPathToSyncUp()}");
			ResultSet rs=ps.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
													
					RailwayPatrolManripMasterDTO dto=new RailwayPatrolManripMasterDTO();
					dto.setId(rs.getInt("Id"));
					dto.setTripName(rs.getString("TripName"));

					dto.setTripTimeShedule(rs.getString("TripTimeShedule"));

					dto.setTripStartTimeAdd(rs.getInt("TripStartTimeAdd"));
					dto.setTripSpendTimeIntervalAdd(rs.getInt("TripSpendTimeIntervalAdd"));
					
					dto.setCreatedBy(rs.getInt("CreatedBy"));
					dto.setCreatedAt(rs.getString("CreatedAt"));
					dto.setUpdatedBy(rs.getInt("UpdatedBy"));
					dto.setUpdatedAt(rs.getString("UpdatedAt"));
					dto.setActiveStatus(rs.getInt("ActiveStatus"));
					dto.setParentId(rs.getInt("parentId"));
	
					PatrolmanList.add(dto);
				}							
			}
		}catch(Exception e){

			e.printStackTrace();
		}
		System.err.println("RailwayPatrolManripMasterDTO list--"+PatrolmanList.size());
		for (RailwayPatrolManripMasterDTO keylist : PatrolmanList) {
			try {
				//System.out.println("KeyMan---"+keylist.getId());
				java.sql.CallableStatement ps1 = conSunMssql.prepareCall("{call UpsertRailwayPatrolmanBeatMasterDataToSyncUp(?,?,?,?,?,?,?,?,?,?,?)}");
				
				ps1.setInt(1, keylist.getId());
				ps1.setString(2, keylist.getTripName());		
				ps1.setString(3, keylist.getTripTimeShedule());		

				ps1.setInt(4, keylist.getTripStartTimeAdd());	
				ps1.setInt(5, keylist.getTripSpendTimeIntervalAdd());
				
				ps1.setInt(6, keylist.getCreatedBy());
				ps1.setString(7, keylist.getCreatedAt());
				ps1.setInt(8, keylist.getUpdatedBy());
				ps1.setString(9, keylist.getUpdatedAt());
				ps1.setInt(10, keylist.getActiveStatus());

				ps1.setInt(11, keylist.getParentId());
				
				int result = ps1.executeUpdate();
				if (result == 0) {
					msgo.setError("true");
					msgo.setMessage("Upsert Railway Patrolman Beat Master not  Inserted");
				} else {
					// //System.err.println("Error=="+result);
					msgo.setError("false");
					//System.out.println("KeyMan---"+keylist.getId()+" update succesfylly");
					msgo.setMessage("Upsert Railway Patrolman Beat Master Inserted Successfully");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return msgo; 
	}

	public MessageObject SaveKeymanBeatInBulk(Connection con, ArrayList<RailwayKeymanDTO> beatInfoList) {

		MessageObject msgo = new MessageObject();
		String Photo = "";
		
		for (int i = 0; i < beatInfoList.size(); i++) 		
		{
			try {
				
				RailwayKeymanDTO joBeat = beatInfoList.get(i);
				System.err.println(joBeat.getStartTime()+"--"+ joBeat.getEndTime());
				System.err.println(joBeat.getKmStart()+"--"+ joBeat.getKmEnd());

			java.sql.CallableStatement ps = con.prepareCall("{call SaveRailwayKeymanBeatPathWithLocationInBulk(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			ps.setInt(1,  joBeat.getStudentId());
			ps.setDouble(2,  joBeat.getKmStart());
			ps.setDouble(3,  joBeat.getKmEnd());
			ps.setString(4, joBeat.getSectionName());
			ps.setInt(5,joBeat.getParentId() );
			ps.setDouble(6, 0.0);
			ps.setDouble(7, 0.0);
			ps.setDouble(8, 0.0);
			ps.setDouble(9, 0.0);
			ps.setInt(10, (int) joBeat.getUserLoginId());
			ps.setString(11, joBeat.getNameWhoInsert());
			ps.setString(12, joBeat.getMobWhoInser());
			ps.setString(13, joBeat.getEmailWhoInsert());
			ps.setInt(14,  joBeat.getStartTime());
			ps.setInt(15,  joBeat.getEndTime());

			
			
			
			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Beat was  not Inserted");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Beat was Inserted Successfully");
	
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return msgo;
	}

	public ArrayList<RailwayKeymanDTO> GetKeymanExistingBeatByParent(
			Connection con, int parentId) {

		ArrayList<RailwayKeymanDTO> p = new ArrayList<RailwayKeymanDTO>();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetKeymanExistingBeatByParent(?)}");
			ps.setInt(1, parentId);
			//System.out.println("Parent-"+parentId);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					RailwayKeymanDTO Raildto= new RailwayKeymanDTO();
					Raildto.setParentId(parentId);
					Raildto.setBeatId(rs.getInt("Id"));
					Raildto.setStudentId(rs.getInt("studentId"));
					Raildto.setKmStart(rs.getDouble("KmStart"));
					Raildto.setKmEnd(rs.getDouble("KmEnd"));
					Raildto.setSectionName(rs.getString("SectionName").trim());
					Raildto.setDeviceId(rs.getString("DeviceID"));
					Raildto.setStart_Lat(rs.getDouble("kmStartLat"));
					Raildto.setStart_Lon(rs.getDouble("kmStartLang"));
					Raildto.setEnd_Lat(rs.getDouble("kmEndLat"));
					Raildto.setEnd_Lon(rs.getDouble("kmEndLang"));
					System.err.println(rs.getInt("Id"));
					Raildto.setIsApprove(rs.getBoolean("ApprovedStatus"));
					Raildto.setEmailWhoInsert(rs.getString("EmailWhosInsert").trim());
					Raildto.setMobWhoInsert(rs.getString("MobWhoInsert").trim());
					Raildto.setNameWhoInsert(rs.getString("NameWhosInsert").trim());
					Raildto.setStartTime(rs.getInt("StartTime"));
					Raildto.setDevicename(rs.getString("DeviceName").trim());

					Raildto.setEndTime(rs.getInt("Endtime"));
					Raildto.setApprovedDate(rs.getString("ApprovedDate"));
					Raildto.setDevicename(rs.getString("DeviceName"));
					p.add(Raildto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public MessageObject UpdateRailwayKeymanBeatPathCopyApprove(Connection con,
			int beatId, int userLoginId) {
		MessageObject msgo = new MessageObject();
		String Photo = "";
		
		
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call UpdateRailwayKeymanBeatPathCopyApprove(?,?)}");
			ps.setInt(1, beatId);			
			ps.setInt(2, userLoginId);
			
		
			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Beat was not aprroved");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Beat was aprroved Successfully");
	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgo;
	}

	
	public MessageObject RemoveDeviceAPI(Connection con, String deviceId1,int userLoginId) {
		MessageObject msgo = new MessageObject();
		String Photo = "";	
		
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call DisableDeviceFromUser(?,?)}");
			ps.setString(1, deviceId1);
			ps.setInt(2, userLoginId);
			//System.out.println("---imeiNo---"+imeiNo+"---userLoginId---"+userLoginId);

			
			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Device was  not Removed");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Device was Removed Successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgo;
	}

	public MessageObject DeviceUnRegisterAPI(DB mongoconnection, String imeiNo, int userLoginId) {
		MessageObject msg = new MessageObject();
		try {
			DBCollection collection = mongoconnection.getCollection(Common.TABLE_DEVICE);
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("device", imeiNo);
			WriteResult cursor = collection.remove(whereQuery);

			// System.out.print("WriteResult  of u-p11----"+cursor.getError()+"  "+timestamp_whereQuery);

			if (cursor.getN() == 0) {
				msg.setError("False");
				msg.setMessage(" Device was  Unregister Suceesfully");
			} 
			else {
				msg.setError("True");
				msg.setMessage("Device was  Register Suceesfully " + "  Got an erorr---"+ cursor.getN());
				}

		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return msg;
	}


	public AddDeviceDropDownInfo GetAddDeviceDropDownInfo(Connection con,
			int userLoginId) {
		AddDeviceDropDownInfo data = new AddDeviceDropDownInfo();
		ArrayList<DeviceTypeDTO> deviceTypeList=new ArrayList<>();
		ArrayList<UserDTO> userList=new ArrayList<>();
		ArrayList<PaymentmodeDTO> paymentmodeList=new ArrayList<>();
		ArrayList<PaymentPlanDTO> paymentPlanList=new ArrayList<>();
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetPaymentPlan(?,?)}");
			ps.setInt(1, 1);
			ps.setInt(2, 1);
			
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					
					PaymentPlanDTO dto = new PaymentPlanDTO();
					dto.setId(rs.getInt("PaymentPlanId"));
					dto.setPlanName(rs.getString("PlanName"));
					dto.setAmount(rs.getDouble("ChargesOfPlan"));
					dto.setPlanDesc(rs.getString("Description"));
					
					paymentPlanList.add(dto);
				}
			}
			
			data.setPaymentPlanList(paymentPlanList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetPaymentMode()}");
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					
					PaymentmodeDTO dto = new PaymentmodeDTO();
					dto.setId(rs.getInt("ID"));
					dto.setPaymentMode(rs.getString("PayNameOfMode"));
					
					
					paymentmodeList.add(dto);
				}
			}
			
			data.setPaymentmodeList(paymentmodeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetDeviceType()}");
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					
					DeviceTypeDTO dto = new DeviceTypeDTO();
					dto.setId(rs.getInt("Id"));
					dto.setType(rs.getString("DeviceType"));
					dto.setTypeDesc(rs.getString("Description"));
					
					deviceTypeList.add(dto);
				}
			}
			
			data.setDeviceTypeList(deviceTypeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call GetAllUsersByNameAndUsersName()}");
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					
					UserDTO dto = new UserDTO();
					dto.setName(rs.getString("FilterName"));
					dto.setParentId(rs.getInt("Link_ID"));
					dto.setRollId(rs.getInt("Role_ID"));
					
					userList.add(dto);
				}
			}
			
			data.setUserList(userList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}

		public MessageObject AddNewDevice(DB mongoconnection,Connection con, int StudentID, int ParentId, String FirstName, String LastName, String Gender, 
			String DeviceID, String Type, int DeviceType, String DeviceSimNumber, String ActivationDate, int PlanTypeID, 
			int PaymentMode, String CreditName, String PaymentDate, String TransactionID, Double PayAmount, int registerOutParameter) {
		MessageObject msg = new MessageObject();
		String Photo = "";
		try {
			java.sql.CallableStatement ps = con.prepareCall("{call SaveAddDeviceWithAdminAndSubadmin(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			ps.setInt(1,StudentID);
			ps.setInt(2, ParentId);
			ps.setString(3, FirstName);
			ps.setString(4, LastName);
			ps.setString(5, Gender);
			ps.setString(6, DeviceID);
			ps.setString(7, Type);
			ps.setInt(8, DeviceType);
			ps.setString(9, DeviceSimNumber);
			ps.setString(10, ActivationDate);
			ps.setInt(11, PlanTypeID);
			ps.setInt(12, PaymentMode);
			ps.setString(13, CreditName);
			ps.setString(14, PaymentDate);
			ps.setString(15, TransactionID);
			ps.setDouble(16, PayAmount);
			ps.registerOutParameter(17, java.sql.Types.INTEGER);
			
						
			/*System.out.println("---studentID---"+studentID+"---parentId---"+parentId+"---firstName---"+firstName+"---lastName---"+lastName+
					"---gender---"+gender+"---deviceID---"+deviceID+"---type---"+type+"---deviceType---"+"---deviceSimNumber---"+deviceSimNumber+
					"---activationDate---"+activationDate+"---planTypeID---"+planTypeID+"---paymentMode---"+paymentMode+"---creditName---"+creditName+
					"---paymentDate---"+paymentDate+"---transactionID---"+transactionID+"---payAmount---"+payAmount);*/
			
			Boolean rs = ps.execute();
			System.out.println("rs " + rs);

			System.out.println("MANAGER ID: " + ps.getInt(17));
			
			//int result = ps.executeUpdate();
			if (ps.getInt(17)> 0) {
				msg.setError("false");
				msg.setMessage("Device Added Successfully");
			} else {
				// //System.err.println("Error=="+result);
				msg.setError("true");
				msg.setMessage("Device was not Added Successfully");
			}

			DBCollection table = mongoconnection.getCollection(Common.TABLE_DEVICE);
			
			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("Device", Long.parseLong(DeviceID));
			Maindocument.put("StudentId", ps.getInt(17));
			Maindocument.put("ParentId", ParentId);
			table.insert(Maindocument);


		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

		public MessageObject AddBulkDevices(DB mongoconnection,Connection con,String ActivationDate,int DeviceType,
			Double PayAmount,String PaymentDate,int PaymentMode,int PlanTypeID,String TransactionID,String Type,
			String BulkData, String CreditName,int ParentId) {
		MessageObject msg = new MessageObject();
		try{
		org.json.JSONArray jodata=new org.json.JSONArray(BulkData);
		System.out.println("AddBulkDevices---jodata---"+jodata);

		//substring(1, patrolmanBeatData.length()-1));
//		JSONArray joTripdata=jodata.getJSONArray("tripsInfo");
		for (int i=0;i<jodata.length();i++)
		{
			

					try {
						org.json.JSONObject data=jodata.getJSONObject(i); 
						
						java.sql.CallableStatement ps = con.prepareCall("{call SaveAddDeviceWithAdminAndSubadmin(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
						ps.setInt(1,0);
						ps.setInt(2, ParentId);
						ps.setString(3, data.getString("Firstname"));
						ps.setString(4, data.getString("Lastname"));
						ps.setString(5, "M");
						ps.setString(6, data.getLong("DeviceIMEI")+"");
						ps.setString(7, Type);
						ps.setInt(8, DeviceType);
						ps.setString(9, data.getLong("SimIMEI")+"");
						ps.setString(10, ActivationDate);
						ps.setInt(11, PlanTypeID);
						ps.setInt(12, PaymentMode);
						ps.setString(13, CreditName);
						ps.setString(14, PaymentDate);
						ps.setString(15, TransactionID);
						ps.setDouble(16, PayAmount);
						ps.registerOutParameter(17, java.sql.Types.INTEGER);
						
									
						/*System.out.println("---studentID---"+studentID+"---parentId---"+parentId+"---firstName---"+firstName+"---lastName---"+lastName+
								"---gender---"+gender+"---deviceID---"+deviceID+"---type---"+type+"---deviceType---"+"---deviceSimNumber---"+deviceSimNumber+
								"---activationDate---"+activationDate+"---planTypeID---"+planTypeID+"---paymentMode---"+paymentMode+"---creditName---"+creditName+
								"---paymentDate---"+paymentDate+"---transactionID---"+transactionID+"---payAmount---"+payAmount);*/
						
						Boolean rs = ps.execute();
						System.out.println("rs " + rs);

						System.out.println("MANAGER ID: " + ps.getInt(17));
						
						//int result = ps.executeUpdate();
						if (ps.getInt(17)> 0) {
							msg.setError("false");
							msg.setMessage("Device Added Successfully");
						} else {
							// //System.err.println("Error=="+result);
							msg.setError("true");
							msg.setMessage("Device was not Added Successfully");
						}

						DBCollection table = mongoconnection.getCollection(Common.TABLE_DEVICE);
						
						BasicDBObject Maindocument = new BasicDBObject();
						Maindocument.put("Device", data.getLong("DeviceIMEI"));
						Maindocument.put("StudentId", ps.getInt(17));
						Maindocument.put("ParentId", ParentId);
						table.insert(Maindocument);


					} catch (Exception e) {
						e.printStackTrace();
					}
		}
		}catch (Exception e) {          
		e.printStackTrace();    }    
		return msg;
	}
}




