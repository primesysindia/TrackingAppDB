package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import model.APIController;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Utility.SendEmail;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteResult;
import com.paytm.pg.merchant.CheckSumServiceHelper;

import dto.ApplicationMetaData;
import dto.AssociatedParentDTO;
import dto.DateRangeExceptionReportDTO;
import dto.DeviceBatteryInfo;
import dto.DeviceBatteryStatusDTO;
import dto.DeviceDataDTO;
import dto.DeviceInfoDto;
import dto.DeviceSimNo;
import dto.DeviceStatusDTO;
import dto.DeviceStatusInfoDto;
import dto.DevicelistDetails;
import dto.DriverEmpTaskSheduledDTO;
import dto.ExceptionDeviceDTO;
import dto.ExceptionKeymanDTO;
import dto.ExceptionReortsTrip;
import dto.ExceptionReportFileDTO;
import dto.FamilyPaymentTypeDTO;
import dto.FeatureAddressDetailsDTO;
import dto.FeatureNearbyDTO;
import dto.HistoryInfoDTO;
import dto.LowBatteryStatusReportDto;
import dto.PoleNearByLocationDto;
import dto.RailDeviceInfoDto;
import dto.RailFeatureCodeDeviceInfoDto;
import dto.RailMailSendInfoDto;
import dto.RailWayAddressDTO;
import dto.FrdlistDTO;
import dto.GeofenceDTO;
import dto.HistoryDTO;
import dto.LocationDTO;
import dto.MainSliderImageDTO;
import dto.MessageObject;
import dto.MilageDTO;
import dto.PersonDetails;
import dto.RailwayKeymanDTO;
import dto.RailwayPetrolmanTripsBeatsDTO;
import dto.SmsNotificationDTO;
import dto.SosDto;
import dto.SosInfoDTO;
import dto.TimeOffsetDTO;
import dto.TripInfoDto;
import dto.VTSDataDTO;
import dto.VehicalTrackingSMSCmdDTO;
import dto.WrongLocationDataDTO;
import dto.mongoDeviceObj;

import com.mongodb.ServerAddress;

public class LoginDAO {

	private double HistoryDistance = 0;
	private int j = 0;
	private int totalCount = 0;
	private DBObject dbObject1 = null;
	private DBObject dbObject2;
	private Calendar calendar = Calendar.getInstance();
	private int year = calendar.get(Calendar.YEAR);
	private int month = calendar.get(Calendar.MONTH);
	private int day = calendar.get(Calendar.DAY_OF_MONTH);
	ArrayList<LocationDTO> LocationListforFeature = new ArrayList<>();
	private static DecimalFormat df2 = new DecimalFormat(".##");

	public MessageObject validlogin(Connection con, String username,
			String password) throws SQLException {

		MessageObject msgObj = new MessageObject();
		/*
		 * try {
		 */ResultSet rs = null;
		// Made changes in select for email;
		PreparedStatement ps = con
				.prepareStatement("select id,Name,Role_id from usermaster where UserID= ? and Password=?");
		ps.setString(1, username);
		ps.setString(2, password);
		rs = ps.executeQuery();
		if (rs != null && rs.next()) {
			msgObj.setId(rs.getString("id"));
			msgObj.setName(rs.getString("Name"));
			msgObj.setRole(rs.getString("Role_id"));
			msgObj.setError("false");
			msgObj.setMessage("Login Successfully");
		} else {
			msgObj.setError("true");
			msgObj.setMessage("Invalid user");
		}
		// }
		/*
		 * catch(Exception e) { msgObj.setError("true");
		 * //System.err.println("getUser "+e.getMessage()); e.printStackTrace();
		 * } finally { try { con.close(); } catch (Exception e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * 
		 * }
		 */
		return msgObj;
	}

	public ArrayList<PersonDetails> GetRequestedList(Connection con,
			String userid) {

		ArrayList<PersonDetails> p = new ArrayList<PersonDetails>();
		String Photo = "";
		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetRequestedList(?)}");
			ps.setInt(1, Integer.parseInt(userid));

			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					PersonDetails person = new PersonDetails();
					person.setAddress("" + rs.getString("Address"));
					person.setContactNumber("" + rs.getString("MobileNo"));
					person.setEmailID("" + rs.getString("EmailID"));
					person.setId("" + rs.getString("InvitedId"));
					person.setInvi_Id("" + rs.getString("Id"));
					person.setName("" + rs.getString("Name"));
					if (rs.getString("Photo") != null)
						person.setPhoto(rs.getString("Photo"));
					else
						person.setPhoto(Common.Photo);

					if (rs.getString("paymentTypeId") != null)
						person.setPaymentTypeId(rs.getString("paymentTypeId"));
					else
						person.setPaymentTypeId("0");
					if (rs.getString("Datediff") != null)
						person.setDatediff(rs.getString("datediff"));
					else
						person.setDatediff("0");

					person.setStatus(rs.getString("Status"));
					p.add(person);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// //System.out.println("S55555555555"+p.toString());
		return p;

	}

	public MessageObject registration(Connection con, String name,
			String address, String contact, String emailid, String password) {
		MessageObject msgo = new MessageObject();
		try {

			// ////System.err.print(emailid);
			int result = 0;

			PreparedStatement psx = con
					.prepareStatement(
							"INSERT INTO usermaster(Name,Address,ContactNumber,EmailID,UserID,Password,Role_id) VALUES(?,?,?,?,?,?,?)",
							PreparedStatement.RETURN_GENERATED_KEYS);
			psx.setString(1, name);
			psx.setString(2, address);
			psx.setString(3, contact);
			psx.setString(4, emailid);
			psx.setString(5, emailid);
			psx.setString(6, password);
			psx.setInt(7, 2);

			result = psx.executeUpdate();

			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("User not resgistered");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("User registered Successfully");
			}
		} catch (Exception e) {
			msgo.setError("true");
			msgo.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}
		return msgo;
	}

	public LocationDTO GetTrackInfo(Connection con, String invited_id) {
		LocationDTO list = new LocationDTO();
		// //System.out.println("GetTrackInfo--dao "+invited_id);

		try {
			java.sql.CallableStatement ps = con
					.prepareCall("{call GetAppTrackInfo(?)}");
			ps.setString(1, invited_id);
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					/* LocationDTO dto=new LocationDTO(); */
					list.setLang(rs.getString("Longitude"));
					list.setLat(rs.getString("Latitude"));
					// list.setPhoto(""+rs.getString("photo"));
					list.setGroupId(rs.getString("GroupId"));
					list.setTime(rs.getString("Time"));
					list.setUserid(rs.getString("UserId"));

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		// //System.err.print("DATA sasa "+list.getLang());
		return list;
	}

	public MessageObject PushLocation(Connection con, String id, String lat,
			String lan, String time) {
		MessageObject msgo = new MessageObject();
		// System.out.println("Location Getting --"+lat+" "+lan+" "+id+" "+time);

		try {/*
			 * 
			 * int updateresultset=0; String sql=
			 * "UPDATE usermaster SET latitude= ?,longitude = ?,Time= ? WHERE usermaster.id = ? "
			 * ; // PreparedStatement ps=con.prepareStatement(
			 * "Update usermaster set latitude = ? , longitude = ? , Time  = ? where usermaster.id = ?"
			 * ); PreparedStatement ps =
			 * con.prepareStatement(sql,PreparedStatement
			 * .RETURN_GENERATED_KEYS);
			 * 
			 * ps.setString(1, lat); ps.setString(2, lan); ps.setString(3,
			 * time); ps.setString(4, id);
			 * 
			 * updateresultset=ps.executeUpdate();
			 * //System.err.println("---------------"+updateresultset); if
			 * (updateresultset==0) {
			 * 
			 * list.setMessage("Location Not Update"); list.setError("true");
			 * 
			 * }else{ list.setMessage("Location Update Successfully");
			 * list.setError("false");
			 * 
			 * }
			 */
			java.sql.CallableStatement ps = con
					.prepareCall("{call SavePushLocation(?,?,?,?)}");

			ps.setString(1, lat);
			ps.setString(2, lan);
			ps.setString(3, time);
			ps.setString(4, id);

			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Location not Inserted");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Location Inserted Successfully");
			}

			// System.err.println("result----PushLocation------"+result);

		} catch (Exception e) {

			msgo.setMessage("Error: " + e.getMessage());
			msgo.setError("true");
		} finally {

		}
		// //System.err.println("PushLocation"+msgo);
		return msgo;
	}

	public ArrayList<FrdlistDTO> SearchFriendList(Connection con, Integer id,
			String entity, Integer flag) {

		ArrayList<FrdlistDTO> list = new ArrayList<FrdlistDTO>();
		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetSearchFriendList(?,?,?)}");
			ps.setInt(1, id);

			ps.setString(2, "%" + entity + "%");
			ps.setInt(3, flag);

			ResultSet rs = ps.executeQuery();
			// //System.out.println("REsult frdb  "+rs+"");
			if (rs != null) {
				while (rs.next()) {
					FrdlistDTO fd = new FrdlistDTO();
					fd.setName(rs.getString("Name") + "");
					fd.setUserid(rs.getString("Id") + "");
					fd.setCity(rs.getString("City") + "");
					fd.setMobNo(rs.getString("MobileNo") + "");
					list.add(fd);
					// //System.err.print("DATA  "+fd.getMobNo());
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

	}

	public MessageObject SendInvitation(Connection con, Integer sendId,
			Integer getId, DB mongoconnection) {
		int invitationlimit = 0;
		int invitationcount = 0;
		// 3 la limit is outgoing
		MessageObject msgo = new MessageObject();

		try {

			ResultSet result;
			String Status = "";
			java.sql.CallableStatement ps = con
					.prepareCall("{call SendInvitation(?,?)}");
			ps.setInt(1, sendId);
			ps.setInt(2, getId);

			result = ps.executeQuery();
			// //System.err.println("--------------Result----"+result);

			if (result != null) {
				while (result.next()) {
					Status = result.getString("Result") + "";
					// //System.err.print("DATA  "+result.getString("Result")+"");

					if (Status.equals("3")) {

						msgo.setError("true");
						msgo.setMessage("Your number of request attempt crossed limit for this user.Please contact admin(contact@mykiddytracker.com)");
					} else if (Status.equals("1")) {

						// SendNotificationToUser(sendId,getId,"eeeee",mongoconnection);
						msgo.setError("false");
						msgo.setMessage("Request is sent successfully");
					} else {
						msgo.setError("true");
						msgo.setMessage("Request is not sent succesfully.Please try again.");
					}
				}
			} else {
				msgo.setError("true");
				msgo.setMessage("Request is not sent succesfully.Please try again.");
			}

		} catch (Exception e) {
			msgo.setError("true");
			msgo.setMessage("Request is not sent succesfully.Please try again.");
			e.printStackTrace();
		}

		return msgo;

	}

	private void SendNotificationToUser(Integer sendId, Integer getId,
			String userMessage, DB mongoconnection) {
		// TODO Auto-generated method stub
		ArrayList<String> devices = new ArrayList<>();
		MulticastResult result = null;
		try {

			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_DEVICE);
			BasicDBObject device_whereQuery = new BasicDBObject("parent", getId);
			DBCursor cursor = table.find(device_whereQuery)
					.sort(new BasicDBObject("_id", -1)).limit(1);
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
			// //System.out.println("Cursor Count of u-----"+cursor.size()+"  "+device_whereQuery);

			if (cursor.size() != 0) {
				// //System.err.println("*-----Count---"+cursor.size());

				DBObject dbObj = (DBObject) cursor.next();

				devices = (ArrayList<String>) dbObj.get("gcm_keys");

			}

		} catch (Exception e) {
			e.printStackTrace();
			/*
			 * msgobj.setError("true");
			 * msgobj.setMessage("Device not Connected successfully due to "
			 * +e.getMessage());
			 */
		} finally {

			// //System.err.println("Gcm Key==== "+devices);
		}

		try {
			/*
			 * BufferedReader br = new BufferedReader(new FileReader(
			 * "GCMRegId.txt")); regId = br.readLine(); br.close();
			 */
			Sender sender = new Sender(Common.GOOGLE_SERVER_KEY);
			Message message = new Message.Builder().timeToLive(30)
					.delayWhileIdle(true)
					.addData(Common.MESSAGE_KEY, userMessage).build();
			// regId="APA91bF2rvCbq4s_ieJh1nWeFP4vqxyYWcfJPBEuQUCDyFN3WjKGcD7yKDsNM7lNGVpSJ5etIAEigFEFg85Nm8TZiK-mIkcSc4p1G2sfPu18c8tsNJpRmiz0tk3ToEhdTCCZN1hkx0gO";
			// regId="APA91bHYX0HZRlsVfVpgHszbfjztFLTM28Azw48TMEiHfytg2OrLX1N-tlWisVHRylKtfQMgQyBBaiLqxcqYaZjizXX35ukPvy4zn4I1Qu5bG1fuWLH_ZVRsX1MseSvyJa4mZzCEbtvx";
			result = sender.send(message, devices, 1);

			for (int i = 0; i < result.getTotal(); i++) {
				Result r = result.getResults().get(i);

				if (r.getMessageId() != null) {
					String canonicalRegId = r.getCanonicalRegistrationId();
					if (canonicalRegId != null) {
						// devices.get(i) has more than on registration ID:
						// update database
						// //System.err.println("111111111");
					}
				} else {
					String error = r.getErrorCodeName();
					if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
						// application has been removed from devices.get(i) -
						// unregister database
						// //System.err.println("22222222222");

					}
				}
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
			/*
			 * request.setAttribute("pushStatus", "RegId required: " +
			 * ioe.toString());
			 */
		} catch (Exception e) {
			e.printStackTrace();
			/*
			 * request.setAttribute("pushStatus", e.toString());
			 */}

		// //System.err.println("GCM Result =="+result.toString());

	}

	public ArrayList<PersonDetails> GetInvitationList(Connection con, String id) {

		ArrayList<PersonDetails> p = new ArrayList<PersonDetails>();

		try {
			java.sql.CallableStatement psc = con
					.prepareCall("{call GetInvitationList(?)}");

			psc.setString(1, id);

			ResultSet rs = psc.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					PersonDetails person = new PersonDetails();
					person.setAddress("" + rs.getString("Address"));
					person.setContactNumber("" + rs.getString("MobileNo"));
					person.setEmailID("" + rs.getString("EmailID"));
					person.setId("" + rs.getString("UserId"));
					person.setName("" + rs.getString("Name"));
					person.setPhoto("" + rs.getString("Photo"));
					person.setStatus(rs.getString("Status"));
					person.setInvi_Id(rs.getString("id"));

					person.toString();

					p.add(person);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// //System.out.println("S"+p.toString());
		return p;

	}

	public MessageObject AcceptInvitation(Connection con, String invi_id) {

		// TODO Auto-generated method stub
		MessageObject msgo = new MessageObject();

		try {

			int result = 0;
			java.sql.CallableStatement psc = con
					.prepareCall("{call SaveAcceptInvitation(?)}");
			psc.setInt(1, Integer.parseInt(invi_id));

			result = psc.executeUpdate();

			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Request is not accepted");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Request is accepted successfully");
			}
		} catch (Exception e) {
			msgo.setError("true");
			msgo.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}

		return msgo;
	}

	public LocationDTO Getlocation(Connection con, String invi_id,
			String user_id) {

		LocationDTO list = new LocationDTO();
		// //System.out.println("Getlocation--dao "+invi_id);

		try {
			java.sql.CallableStatement ps = con
					.prepareCall("{call Getlocation(?,?)}");
			ps.setInt(1, Integer.parseInt(invi_id));
			ps.setInt(2, Integer.parseInt(user_id));

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					/* LocationDTO dto=new LocationDTO(); */
					list.setLang(rs.getString("Longitude"));
					list.setLat(rs.getString("Latitude"));
					// list.setPhoto(""+rs.getString("photo"));
					list.setGroupId(rs.getString("GroupId"));
					list.setTime(rs.getString("Time"));
					list.setUserid(rs.getString("UserId"));

				}

				/*
				 * PreparedStatement ps1=con.prepareStatement(
				 * "INSERT INTO Frd_Track_Report (TUserId,TrackerId,Lat,Lang,Time,TrackType,Address) VALUES(?,?,?,?,GETDATE(),2,?)"
				 * ,PreparedStatement.RETURN_GENERATED_KEYS); ps1.setString(1,
				 * user_id); ps1.setString(2, list.getUserid());
				 * ps1.setString(3, list.getLat()); ps1.setString(4,
				 * list.getLang()); ps1.setString(5, Common.GetAddress(
				 * list.getLat(), list.getLang()));
				 * 
				 * int rs1=ps1.executeUpdate();
				 */
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

		}
		// //System.err.print("DATA sasa "+list.getLang());
		return list;

	}

	public MessageObject RejectInvitation(Connection con, String invi_id) {

		// TODO Auto-generated method stub
		MessageObject msgo = new MessageObject();

		try {

			int result = 0;
			java.sql.CallableStatement ps = con
					.prepareCall("{call SaveRejectInvitation(?)}");
			ps.setInt(1, Integer.parseInt(invi_id));

			result = ps.executeUpdate();

			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Request is not rejected");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Request is rejected successfully");
			}
		} catch (Exception e) {
			msgo.setError("true");
			msgo.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}

		return msgo;
	}

	public ArrayList<PersonDetails> GetAcceptedtrackperson(Connection con,
			String userid) {

		ArrayList<PersonDetails> p = new ArrayList<PersonDetails>();
		String Photo = null;
		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetAcceptedtrackperson(?)}");
			ps.setInt(1, Integer.parseInt(userid));

			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					PersonDetails person = new PersonDetails();
					person.setAddress("" + rs.getString("Address"));
					person.setContactNumber("" + rs.getString("MobileNo"));
					person.setEmailID("" + rs.getString("EmailID"));
					person.setId("" + rs.getString("UserId"));
					person.setName("" + rs.getString("Name"));
					person.setPhoto("" + rs.getString("Photo"));
					person.setStatus(rs.getString("Status"));
					person.setInvi_Id(rs.getString("id"));

					person.toString();

					p.add(person);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// //System.out.println("S"+p.toString());
		return p;

	}

	public MessageObject BlockTrakRequest(Connection con, String invi_id) {
		// TODO Auto-generated method stub
		MessageObject msgo = new MessageObject();
		//

		try {

			int result = 0;

			java.sql.CallableStatement ps = con
					.prepareCall("{call SaveBlockTrakRequest(?)}");
			ps.setInt(1, Integer.parseInt(invi_id));

			result = ps.executeUpdate();

			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Request is not blocked");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Request is blocked successfully");
			}
		} catch (Exception e) {
			msgo.setError("true");
			msgo.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}

		return msgo;
	}

	public void PostLocation_Report(Connection con, String invi_id,
			String user_id, String lat, String lang) {

		PreparedStatement ps1;
		try {
			ps1 = con
					.prepareStatement(
							"INSERT INTO Frd_Track_Report (TUserId,TrackerId,Lat,Lang,Time,TrackType,Address) VALUES(?,?,?,?,GETDATE(),1,?)",
							PreparedStatement.RETURN_GENERATED_KEYS);
			ps1.setString(1, user_id);
			ps1.setString(2, invi_id);
			ps1.setString(3, lat);
			ps1.setString(4, lang);
			ps1.setString(5, Common.GetAddress(lat, lang).toString());

			int result = ps1.executeUpdate();

			if (result == 0) {
				// //System.err.println("Error=="+result);

				// //System.err.println("Location not inserted");
			} else {
				// //System.err.println("Location inserted successfully");

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public DeviceSimNo GetDeviceSimNo(Connection con, String studentid,
			String command) {
		// TODO Auto-generated method stub
		DeviceSimNo msgObj = new DeviceSimNo();

		try {
			ResultSet rs = null;
			// Made changes in select for email;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call GetDeviceSimNo(?,?)}");

			stmt.setString(1, studentid);
			stmt.setString(2, command);

			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				msgObj.setDeviceSimNumber(rs.getString("DeviceSimNumber") + "");

				msgObj.setCommnadType(rs.getString("CommnadType") + "");

				msgObj.setActualCommand(rs.getString("ActualCommand") + "");
				msgObj.setVSCallback(rs.getString("VSCallback") + "");
				msgObj.setVSEnabled(rs.getString("VSEnabled") + "");
				msgObj.setError("false");
				msgObj.setMessage("Sucessfull");
			} else {
				msgObj.setError("true");
				msgObj.setMessage("Invalid user");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return msgObj;
	}

	public MessageObject PostDeviceSimno(Connection con, String studentid,
			String simno) {
		// TODO Auto-generated method stub
		MessageObject msgo = new MessageObject();

		try {

			int result = 0;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call PostDeviceSimno(?,?)}");
			stmt.setString(1, studentid);
			stmt.setString(2, simno);

			result = stmt.executeUpdate();

			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Device number is not update successfully");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Device number is update successfully");
			}
		} catch (Exception e) {
			msgo.setError("true");
			msgo.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}

		return msgo;
	}

	public DeviceSimNo GetCommandtoTrack(Connection con, String studentid,
			String command) {
		DeviceSimNo msgObj = new DeviceSimNo();

		try {
			ResultSet rs = null;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call GetCommandtoTrack(?,?)}");

			stmt.setString(1, studentid);
			stmt.setString(2, command);

			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {

				msgObj.setCommnadType(rs.getString("CommnadType") + "");

				msgObj.setActualCommand(rs.getString("ActualCommand") + "");
				msgObj.setError("false");
				msgObj.setMessage("SucessFull");
			} else {
				msgObj.setError("true");
				msgObj.setMessage("Invalid user");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return msgObj;
	}

	public ArrayList<SosDto> GetSosList(DB mongoconnection, String userid) {
		ArrayList<SosDto> list = new ArrayList<SosDto>();

		DBCollection table = mongoconnection.getCollection(Common.TABLE_SOS);
		// //System.out.println("UserId==============----------"+userid+" "+table.getFullName());

		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("parent", userid);
		DBCursor cursor = table.find(whereQuery);
		// //System.out.print("Cursor Count of up----"+cursor.size()+whereQuery);

		while (cursor.hasNext()) {

			if (cursor.size() != 0) {

				DBObject dbObject = (DBObject) cursor.next();
				BasicDBList sosList = (BasicDBList) dbObject.get("sosno");
				for (int i = 0; i < sosList.size(); i++) {
					BasicDBObject sosObj = (BasicDBObject) sosList.get(i);
					// class_id=""+diaryObj.getString("ClassID");
					// //System.out.println("Cursor print----------"+sosObj.toString());
					SosDto d = new SosDto();
					d.setName(sosObj.getString("name"));
					d.setNumber(sosObj.getString("number"));
					d.setSosid(sosObj.getString("id"));
					list.add(d);
				}

			}
			/*
			 * else{
			 * 
			 * DiaryDTO diaryresponce = new DiaryDTO();
			 * 
			 * diaryresponce.setError("true");
			 * diaryresponce.setMsg("Diary Not Found");
			 * Responselist.add(diaryresponce); // return Responselist;
			 * 
			 * }
			 */

		}

		return list;
	}

	public MessageObject SaveSOS_onServer(DB mongoconnection, String userid,
			String name, String number) {

		MessageObject msgobj = new MessageObject();
		DBCollection sostable = mongoconnection.getCollection(Common.TABLE_SOS);

		// Create New Diary Id
		ObjectId id1 = new ObjectId();

		try {

			DBObject find = new BasicDBObject("parent", userid);

			// DBObject listItem = new BasicDBObject("sosno", new
			// BasicDBObject("name",name).append("number",number));
			DBObject listItem = new BasicDBObject("sosno", new BasicDBObject(
					"id", id1.toString()).append("name", name).append("number",
					number));

			DBObject updateQuery = new BasicDBObject("$push", listItem);
			sostable.update(find, updateQuery, true, false);

			msgobj.setError("false");
			msgobj.setMessage("SOS add sucessfully.");

		} catch (Exception e) {

			e.printStackTrace();
			msgobj.setError("true");
			msgobj.setMessage("SOS  not add sucessfully.");
		} finally {

		}
		return msgobj;

	}

	public MessageObject DeleteSOs(DB mongoconnection, String sosid,
			String userid) {

		MessageObject msgobj = new MessageObject();
		DBCollection sostable = mongoconnection.getCollection(Common.TABLE_SOS);

		try {

			/*
			 * DBObject find = new BasicDBObject("parent", userid);
			 * 
			 * BasicDBObject update = new BasicDBObject("sosno.id",sosid);
			 * sostable.update(find, new BasicDBObject("$pull", update));
			 */

			BasicDBObject sq = new BasicDBObject("parent", userid);
			BasicDBObject idoc = new BasicDBObject("id", sosid);
			BasicDBObject odoc = new BasicDBObject("sosno", idoc);
			BasicDBObject delq = new BasicDBObject("$pull", odoc);
			sostable.update(sq, delq);
			msgobj.setError("false");
			msgobj.setMessage("SOS delete sucessfully.");

		} catch (Exception e) {

			e.printStackTrace();
			msgobj.setError("true");
			msgobj.setMessage("SOS  not delete sucessfully.");
		} finally {

		}
		return msgobj;

	}

	public VTSDataDTO GetstudentVTSStatus(Connection con, String studId) {
		VTSDataDTO msgObj = new VTSDataDTO();

		try {
			ResultSet rs = null;
			// Made changes in select for email;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call GetstudentVTSData(?)}");

			stmt.setInt(1, Integer.parseInt(studId));

			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				msgObj.setDistanceInterval(rs.getString("DistInterval") + "");

				msgObj.setVtsEnable(rs.getString("VtsEnable") + "");

				msgObj.setStudentId(studId);
				msgObj.setPin(rs.getString("EnginePin") + "");
				msgObj.setError("false");
				msgObj.setMessage("Sucessfull");
			} else {
				msgObj.setError("true");
				msgObj.setMessage("Invalid user");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return msgObj;
	}

	public MessageObject PostEnginePin(Connection con, String studId, String pin) {

		// TODO Auto-generated method stub
		MessageObject msgo = new MessageObject();

		try {

			int result = 0;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call PostEnginePin(?,?)}");
			stmt.setInt(1, Integer.parseInt(studId));
			stmt.setString(2, pin);

			result = stmt.executeUpdate();

			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Engine pin is not update successfully");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Engine pin is update successfully");
			}
		} catch (Exception e) {
			msgo.setError("true");
			msgo.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}

		return msgo;

	}

	public MessageObject GetMilage_old(DB mongoconnection, String device,
			String startdate, String enddate) {
		ArrayList<MilageDTO> list = new ArrayList<MilageDTO>();
		MessageObject msgData = new MessageObject();

		DBCollection table = mongoconnection
				.getCollection(Common.TABLE_LOCATION);
		// //System.out.println("device==============----------"+device+" "+table.getFullName()+"startdate--- "+startdate+" end---- "+enddate);

		BasicDBObject device_whereQuery = new BasicDBObject();
		device_whereQuery.put("device", Long.parseLong(device));

		BasicDBObject timestamp_whereQuery = new BasicDBObject("timestamp",
				new BasicDBObject("$gte", Long.parseLong(startdate)).append(
						"$lt", Long.parseLong(enddate)));

		BasicDBList And_Milage = new BasicDBList();
		And_Milage.add(timestamp_whereQuery);
		And_Milage.add(device_whereQuery);
		DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

		DBCursor cursor = table.find(Total_Milage_query);
		cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

		// //System.out.print("Cursor Count of u-p----"+cursor.size()+"  "+Total_Milage_query);
		long i = 0;
		long total = cursor.size();

		if (cursor.size() != 0) {

			// //System.err.println("*-----COunt---"+cursor.size());

			while (cursor.hasNext()) {

				if (i == 0) {
					dbObject1 = (DBObject) cursor.next();
				}

				BasicDBObject locobj = (BasicDBObject) dbObject1
						.get("location");
				double lat1 = locobj.getDouble("lat");
				double lng1 = locobj.getDouble("lon");

				dbObject2 = (DBObject) cursor.next();

				BasicDBObject locobj2 = (BasicDBObject) dbObject2
						.get("location");
				double lat2 = locobj2.getDouble("lat");
				double lng2 = locobj2.getDouble("lon");

				dbObject1 = dbObject2;

				double caldist = distance(lat1, lng1, lat2, lng2, "K");

				if (!Double.isNaN(caldist)) {
					HistoryDistance = HistoryDistance + caldist;
					// //System.err.println("*-----HistoryDistance---"+i+"---*---"+device+"-----"+HistoryDistance);

				} else {
					// System.err.println("*-----HistoryDistance------*---==NaN");

				}
				i++;

			}

			if (i == total - 2) {

				String dist = HistoryDistance + " KM";

				if (HistoryDistance > 1000.0f) {
					float distance = (float) (HistoryDistance / 1000.0f);
					dist = distance + " KM";
				}
				// System.out.println("DIstance Calulated by===+i+=="+HistoryDistance);

			}

		}
		if (HistoryDistance != 0.0) {
			msgData.setError("false");
			msgData.setMessage(HistoryDistance + "");
			msgData.setName(device);
		} else {
			msgData.setError("true");
		}

		return msgData;
	}

	double distance(double lat1, double lon1, double lat2, double lon2,
			String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
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

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	public ArrayList<VehicalTrackingSMSCmdDTO> GetSMSItemlist(Connection con,
			String studId) {
		ArrayList<VehicalTrackingSMSCmdDTO> smslist = new ArrayList<VehicalTrackingSMSCmdDTO>();
		try {
			ResultSet rs = null;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call GetSMSItemlist()}");

			/*
			 * stmt.setString(1,studentid); stmt.setString(2,command);
			 */

			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					VehicalTrackingSMSCmdDTO msgObj = new VehicalTrackingSMSCmdDTO();

					msgObj.setCommnadType(rs.getString("CommnadType") + "");

					msgObj.setActualCommand(rs.getString("ActualCommand") + "");
					msgObj.setTitle(rs.getString("Discription"));
					msgObj.setAnsFromDevice(rs.getString("AnsFromDevice"));

					smslist.add(msgObj);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return smslist;
	}

	public DeviceSimNo GetSMSDeviceSimNo(Connection con, String studentid) {
		DeviceSimNo msgObj = new DeviceSimNo();

		try {
			ResultSet rs = null;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call GetSMSDeviceSimNo(?)}");

			stmt.setString(1, studentid);

			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {

				msgObj.setDeviceSimNumber(rs.getString("DeviceSimNumber") + "");
				msgObj.setError("false");
				msgObj.setMessage("SucessFull");
			} else {
				msgObj.setError("true");
				msgObj.setMessage("Invalid user");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return msgObj;
	}

	public ArrayList<VehicalTrackingSMSCmdDTO> GetVehReportlist(Connection con,
			String studId) {
		ArrayList<VehicalTrackingSMSCmdDTO> smslist = new ArrayList<VehicalTrackingSMSCmdDTO>();
		try {
			ResultSet rs = null;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call GetVehReportlist()}");

			/*
			 * stmt.setString(1,studentid); stmt.setString(2,command);
			 */

			// System.out.println("GetVehReportlist11111111111======"+studId);

			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					VehicalTrackingSMSCmdDTO msgObj = new VehicalTrackingSMSCmdDTO();

					msgObj.setCommnadType(rs.getString("CommnadType") + "");

					msgObj.setActualCommand(rs.getString("ActualCommand") + "");
					msgObj.setTitle(rs.getString("CommnadType"));
					msgObj.setAnsFromDevice(rs.getString("AnsFromDevice"));

					smslist.add(msgObj);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return smslist;
	}

	public MessageObject PostNotificationData_Server(Connection con,
			SmsNotificationDTO smsdata) {
		// TODO Auto-generated method stub
		MessageObject msgo = new MessageObject();

		try {

			int result = 0;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call PostNotificationData_Server(?,?,?,?,?,?,?,?)}");
			stmt.setString(1, smsdata.getNotify_Title());
			stmt.setString(2, smsdata.getNotify_Type());
			stmt.setString(3, smsdata.getLatDir());
			stmt.setString(4, smsdata.getLangDir());
			stmt.setString(5, smsdata.getLat());
			stmt.setString(6, smsdata.getLang());
			stmt.setString(7, smsdata.getImeiNo());
			stmt.setString(8, smsdata.getUserId());

			result = stmt.executeUpdate();

			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("NotificationData  is not update successfully");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("NotificationData is update successfully");
			}
		} catch (Exception e) {
			msgo.setError("true");
			msgo.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}

		return msgo;

	}

	public String GetVehTotalKm(DB mongoconnection, String device,
			String startdate, String enddate) {
		ArrayList<MilageDTO> list = new ArrayList<MilageDTO>();

		DBCollection table = mongoconnection
				.getCollection(Common.TABLE_LOCATION);
		// System.out.println("device==============----------"+device+" "+table.getFullName());

		BasicDBObject device_whereQuery = new BasicDBObject();
		device_whereQuery.put("device", Long.parseLong(device));

		BasicDBObject timestamp_whereQuery = new BasicDBObject("timestamp",
				new BasicDBObject("$gte", Long.parseLong(startdate)).append(
						"$lt", Long.parseLong(enddate)));

		BasicDBList And_Milage = new BasicDBList();
		And_Milage.add(timestamp_whereQuery);
		And_Milage.add(device_whereQuery);
		DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

		DBCursor cursor = table.find(Total_Milage_query);
		cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

		// System.out.print("Cursor Count of u-p----"+cursor.size()+"  "+Total_Milage_query);
		long i = 0;
		long total = cursor.size();

		if (cursor.size() != 0) {

			// System.err.println("*-----COunt---"+cursor.size());

			while (cursor.hasNext()) {

				if (i == 0) {
					dbObject1 = (DBObject) cursor.next();
				}

				BasicDBObject locobj = (BasicDBObject) dbObject1
						.get("location");
				double lat1 = locobj.getDouble("lat");
				double lng1 = locobj.getDouble("lon");

				dbObject2 = (DBObject) cursor.next();

				BasicDBObject locobj2 = (BasicDBObject) dbObject2
						.get("location");
				double lat2 = locobj2.getDouble("lat");
				double lng2 = locobj2.getDouble("lon");

				dbObject1 = dbObject2;

				double caldist = distance(lat1, lng1, lat2, lng2, "K");

				if (!Double.isNaN(caldist)) {
					HistoryDistance = HistoryDistance + caldist;
					// //System.err.println("*-----HistoryDistance---"+i+"---*---"+device+"-----"+HistoryDistance);

				} else {
					// //System.err.println("*-----HistoryDistance------*---==NaN");

				}
				i++;

			}

			if (i == total - 2) {

				String dist = HistoryDistance + " KM";

				if (HistoryDistance > 1000.0f) {
					float distance = (float) (HistoryDistance / 1000.0f);
					dist = distance + " KM";
				}
				// System.out.println("DIstance Calulated by===+i+=="+HistoryDistance);
			}

		}

		return HistoryDistance + "";
	}

	/*
	 * public MessageObject GenerateTripReport(Connection con, DB
	 * mongoconnection) { ArrayList<DeviceInfoDto> UserList=new ArrayList<>();
	 * Double SpeedcCheck=1.0;; ArrayList<DBObject> TempLoctionList=new
	 * ArrayList<>(); int TripLimeLimit=300; long SignalCheckcount=0; File
	 * tripfile; FileWriter writer = null; calendar = Calendar.getInstance();
	 * 
	 * try { //Whatever the file path is. tripfile = new
	 * File(Common.Log_path+"GenerateTripReport_log.txt"); //Create the file if
	 * (tripfile.createNewFile()){ //System.out.println("File is created!");
	 * }else{ //System.out.println("File already exists."); }
	 * 
	 * //Write Content writer = new FileWriter(tripfile);
	 * writer.write("\nStart Generate Daily TripReprt Log----at "
	 * +calendar.getTime());
	 * 
	 * } catch (IOException e) {
	 * //System.err.println("Problem writing to the file statsTest.txt"); }
	 * 
	 * int trip=0; calendar = Calendar.getInstance();
	 * 
	 * 
	 * try{ PreparedStatement ps=con.prepareStatement(
	 * "select FirstName,LastName ,DeviceID from StudentMaster where TripReportAllow=0"
	 * ); ResultSet rs=ps.executeQuery(); if (rs!=null) { while (rs.next()) {
	 * DeviceInfoDto dto=new DeviceInfoDto();
	 * dto.setName(rs.getString(1)+rs.getString(2));
	 * dto.setDeviceID(rs.getString(3));
	 * 
	 * UserList.add(dto);
	 * 
	 * //System.out.println("--------Report Req--Allow---"+rs.getString(1)+rs.
	 * getString(2)); } } }catch(Exception e){
	 * 
	 * e.printStackTrace(); }
	 * 
	 * 
	 * try{ PreparedStatement ps1=con.prepareStatement(
	 * "select ActualData   from ApplicationMetaData where ApplicationMetaData.ID=5"
	 * ); ResultSet rs1=ps1.executeQuery(); if (rs1!=null) { while (rs1.next())
	 * { TripLimeLimit=Integer.parseInt(rs1.getString(1));
	 * 
	 * } } }catch(Exception e){
	 * 
	 * e.printStackTrace(); }
	 * 
	 * year = calendar.get(Calendar.YEAR); month = calendar.get(Calendar.MONTH);
	 * day = calendar.get(Calendar.DAY_OF_MONTH); long Starttime =
	 * Long.valueOf(Common.getGMTTimeStampFromDate(day-1+ "-"+
	 * String.valueOf(month+1) + "-" + year+" 00:00 am")); long Endtime =
	 * Long.valueOf(Common.getGMTTimeStampFromDate(day+ "-"+
	 * String.valueOf(month+1) + "-" + year+" 00:00 am"));
	 * 
	 * long Starttime = 1503858600; long Endtime =1503945000;
	 * 
	 * //System.out.println("StartTime:"+Starttime+"  End Time:"+Endtime+
	 * "  UserList size: "+UserList.size());
	 * 
	 * 
	 * try { writer.write(
	 * "\n-----------------------Report Generating  for---------------------------"
	 * + "\n\n***********************************************"+"StartTime:" +
	 * Starttime + "  End Time:"+Endtime+
	 * "*****************************************************Day-------"
	 * +"-----------");
	 * 
	 * // for(int id=0;id<UserList.size();id++){ trip=0; DBCollection table =
	 * mongoconnection.getCollection(Common.TABLE_LOCATION); BasicDBObject
	 * device_whereQuery = new BasicDBObject(); //
	 * device_whereQuery.put("device"
	 * ,Long.parseLong(UserList.get(id).getDeviceID()));
	 * device_whereQuery.put("device",Long.parseLong("355488020822476"));
	 * BasicDBObject timestamp_whereQuery = new BasicDBObject("timestamp", new
	 * BasicDBObject("$gte",Starttime).append("$lt",Endtime)); BasicDBList
	 * And_Milage = new BasicDBList(); And_Milage.add(timestamp_whereQuery);
	 * And_Milage.add(device_whereQuery); DBObject Total_Milage_query = new
	 * BasicDBObject("$and", And_Milage);
	 * 
	 * DBCursor cursor = table.find(Total_Milage_query);
	 * cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
	 * 
	 * //System.out.println("Cursor Count of u-p11----"+cursor.size()+"  "+
	 * Total_Milage_query); long i=0; long total=cursor.size(); Double
	 * MaxSpeed=0.0; Double TotalSpeed=0.0; int Triplocationcount=0;
	 * 
	 * if(cursor.size()!=0) { TempLoctionList.clear();
	 * 
	 * //System.err.println("*-----COunt---"+cursor.size());
	 * 
	 * while(cursor.hasNext()){
	 * 
	 * if (i==0) { dbObject1 = (DBObject) cursor.next();
	 * TempLoctionList.add(dbObject1); }else {
	 * SpeedcCheck=Integer.parseInt(dbObject2.get("speed").toString());
	 * 
	 * } if(SpeedcCheck>MaxSpeed) MaxSpeed=SpeedcCheck; if(SpeedcCheck!=0)
	 * Triplocationcount++;
	 * 
	 * TotalSpeed=TotalSpeed+SpeedcCheck;
	 * 
	 * if(TempLoctionList.size()==0&& SpeedcCheck!=0){
	 * TempLoctionList.add(dbObject2);
	 * 
	 * }
	 * 
	 * BasicDBObject locobj = (BasicDBObject) dbObject1.get("location"); double
	 * lat1 = locobj.getDouble("lat"); double lng1 = locobj.getDouble("lon");
	 * 
	 * 
	 * 
	 * if(total>=2) dbObject2 = (DBObject) cursor.next();
	 * 
	 * BasicDBObject locobj2 = (BasicDBObject) dbObject2.get("location"); double
	 * lat2 = locobj2.getDouble("lat"); double lng2 = locobj2.getDouble("lon");
	 * 
	 * 
	 * DBObject source = dbObject1; DBObject destination = dbObject2;
	 * 
	 * dbObject1=dbObject2;
	 * 
	 * if(lat1!=lat2&&lng1!=lng2&&TempLoctionList.size()>0){ double caldist =
	 * distance(lat1,lng1,lat2,lng2, "K");
	 * 
	 * if (!Double.isNaN(caldist)) { HistoryDistance=HistoryDistance+caldist;
	 * 
	 * }
	 * 
	 * 
	 * 
	 * long src_time = Long .parseLong(source.get( "timestamp") .toString());
	 * long dest_time = Long .parseLong(destination.get( "timestamp")
	 * .toString());
	 * 
	 * if(SpeedcCheck==0){ long diff = dest_time - src_time;
	 * 
	 * SignalCheckcount=SignalCheckcount+diff;
	 * ////System.err.println("-----SignalCheckcount---"+ SignalCheckcount);
	 * 
	 * }
	 * 
	 * 
	 * if ((SpeedcCheck == 0&&
	 * SignalCheckcount>TripLimeLimit&&HistoryDistance>1&&
	 * MaxSpeed!=0)||i==total-2) {
	 * 
	 * //System.out.println("*******************************");
	 * 
	 * 
	 * //System.err.println("-----SignalCheckcount Got ---"+
	 * SignalCheckcount+"   speedcheck=="
	 * +SpeedcCheck+"   Historydist---"+HistoryDistance
	 * +"  MaxSpeed -"+MaxSpeed);
	 * 
	 * BasicDBObject Maindocument = new BasicDBObject(); ObjectId objid = new
	 * ObjectId(); Maindocument.put("report_id", "" + objid);
	 * Maindocument.put("device", UserList.get(id).getDeviceID());
	 * Maindocument.put("name", UserList.get(id).getName());
	 * Maindocument.put("device", "355488020822476"); Maindocument.put("name",
	 * "Rupesh"); Maindocument.put("totalkm", HistoryDistance);
	 * Maindocument.put("maxspeed", MaxSpeed); if (Triplocationcount!=0)
	 * Maindocument.put("avgspeed", TotalSpeed/Triplocationcount); else
	 * Maindocument.put("avgspeed", 0); DBObject srcdbobj =
	 * TempLoctionList.get(0);
	 * 
	 * BasicDBObject locallocobj = (BasicDBObject) srcdbobj.get("location");
	 * double locallat1 = locallocobj.getDouble("lat"); double locallng1 =
	 * locallocobj.getDouble("lon");
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * BasicDBObject Intial_Loc = new BasicDBObject(); Intial_Loc.put("lat",
	 * locallat1); Intial_Loc.put("lon", locallng1); Intial_Loc.put("speed",
	 * srcdbobj.get("speed")); Intial_Loc .put("timestamp",
	 * srcdbobj.get("timestamp"));
	 * 
	 * Maindocument.put("source_info", Intial_Loc);
	 * 
	 * BasicDBObject Dest_Loc = new BasicDBObject(); Dest_Loc.put("lat", lat2);
	 * Dest_Loc.put("lon", lng2); Dest_Loc.put("speed", destination
	 * .get("speed")); Dest_Loc.put( "timestamp", destination
	 * .get("timestamp"));
	 * 
	 * Maindocument.put("dest_info", Dest_Loc);
	 * 
	 * DBCollection Triptable = mongoconnection
	 * .getCollection(Common.TABLE_TRIPREPORT);
	 * 
	 * Triptable.insert(Maindocument); HistoryDistance = 0.0;
	 * TempLoctionList.clear(); SignalCheckcount=0; MaxSpeed=0; TotalSpeed=0;
	 * Triplocationcount=0; trip++;
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * i++;
	 * 
	 * 
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * if (i==total-2) {
	 * 
	 * String dist = HistoryDistance + " KM";
	 * 
	 * if (HistoryDistance > 1000.0f) { float distance = (float)
	 * (HistoryDistance / 1000.0f); dist = distance + " KM"; }
	 * //System.out.println("DIstance Calulated by===+i+=="+HistoryDistance);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * //
	 * writer.write("\n-----------------------Trip Generate coount is == "+trip
	 * +"  for  "
	 * +UserList.get(id).getName()+" , Device Id : "+UserList.get(id).
	 * getDeviceID ()); // writer.close();
	 * 
	 * // }//for end } catch (NumberFormatException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } catch (MongoException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch (IOException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); } finally{
	 * 
	 * }
	 * 
	 * 
	 * 
	 * return null; }
	 */

	public MessageObject GenerateTripReport(Connection con, DB mongoconnection) {
		ArrayList<DeviceInfoDto> UserList = new ArrayList<>();
		ArrayList<DBObject> TempLoctionList = new ArrayList<>();
		int TripTimeLimit = 300;
		File tripfile;
		int Deaactivecount = 0;
		FileWriter writer = null;
		BufferedWriter bfwriter = null;
		calendar = Calendar.getInstance();
		ArrayList<Double> DeaciveListDevice = new ArrayList<Double>();

		try {
			// Whatever the file path is.
			tripfile = new File(Common.Log_path + "GenerateTripReport_log"
					+ calendar.getTime() + ".txt");
			// Create the file
			if (tripfile.createNewFile()) {
				// System.out.println("File is created!");
			} else {
				// System.out.println("File already exists.");
			}

			// Write Content
			writer = new FileWriter(tripfile);

			bfwriter = new BufferedWriter(writer);
			bfwriter.write("\nStart Generate Daily TripReprt Log----at "
					+ calendar.getTime());

		} catch (IOException e) {
			// System.err.println("Problem writing to the file statsTest.txt");
		} catch (Exception e) {

			e.printStackTrace();
		}

		int trip = 0;
		calendar = Calendar.getInstance();

		try {

			java.sql.CallableStatement psc = con
					.prepareCall("{call GetDeviceListForTripReport()}");
			ResultSet rs = psc.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					DeviceInfoDto dto = new DeviceInfoDto();
					dto.setName(rs.getString(1) + rs.getString(2));
					dto.setDeviceID(rs.getString(3));
					dto.setDeviceType(rs.getInt(4));
					if (dto.getDeviceType() != 1) {
						UserList.add(dto);
						// System.out.println("--------Report Req--Allow---"+rs.getString(1)+rs.getString(2)+"-----"+rs.getInt(4));

					}

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		try {
			PreparedStatement ps1 = con
					.prepareStatement("select ActualData from ApplicationMetaData where ApplicationMetaData.ID=5");
			ResultSet rs1 = ps1.executeQuery();
			if (rs1 != null) {
				while (rs1.next()) {
					TripTimeLimit = Integer.parseInt(rs1.getString(1));

				}
			}
			// System.err.println("TripTimeLimit---------"+TripTimeLimit);
		} catch (Exception e) {

			e.printStackTrace();
		}

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long Starttime = Long.valueOf(Common.getGMTTimeStampFromDate(day - 1
				+ "-" + String.valueOf(month + 1) + "-" + year + " 00:00 am"));
		long Endtime = Long.valueOf(Common.getGMTTimeStampFromDate(day + "-"
				+ String.valueOf(month + 1) + "-" + year + " 00:00 am"));

		/*
		 * long Starttime = Long.valueOf(Common.getGMTTimeStampFromDate(day-2+
		 * "-"+ String.valueOf(month+1) + "-" + year+" 00:00 am")); long Endtime
		 * = Long.valueOf(Common.getGMTTimeStampFromDate(day-1+ "-"+
		 * String.valueOf(month+1) + "-" + year+" 00:00 am"));
		 */

		/*
		 * long Starttime = 1504809000; long Endtime =1504895400;
		 */

		// System.out.println("StartTime:"+Starttime+"  End Time:"+Endtime+"  UserList size: "+UserList.size());

		try {
			bfwriter.write("\n-----------------------Report Generating  for---------------------------"
					+ "\n\n******************"
					+ "StartTime: "
					+ Starttime
					+ "  End Time: " + Endtime + "********");

			for (int id = 0; id < UserList.size(); id++) {

				trip = 0;
				HistoryDistance = 0;

				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_LOCATION);
				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device",
						Long.parseLong(UserList.get(id).getDeviceID()));
				// device_whereQuery.put("device",Long.parseLong("355488020181042"));

				BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp",
						new BasicDBObject("$gte", Starttime).append("$lt",
								Endtime));
				BasicDBList And_Milage = new BasicDBList();
				And_Milage.add(timestamp_whereQuery);
				And_Milage.add(device_whereQuery);
				DBObject Total_Milage_query = new BasicDBObject("$and",
						And_Milage);

				DBCursor cursor = table.find(Total_Milage_query);
				cursor.sort(new BasicDBObject("timestamp", 1));

				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				// System.out.println("Cursor Count of u-p11----"+cursor.size()+"  "+Total_Milage_query);
				long i = 0;
				long total = cursor.size();
				Double MaxSpeed = 0.0;
				Double TotalSpeed = 0.0;
				int Triplocationcount = 0;

				if (cursor.size() > 0) {
					TempLoctionList.clear();

					// System.err.println("*-----COunt---"+cursor.size());

					List<DBObject> listObjects = cursor.toArray();
					trip = Calculate_Trip((ArrayList<DBObject>) listObjects,
							mongoconnection, TripTimeLimit, UserList.get(id)
									.getDeviceID(), UserList.get(id).getName());

				}

				bfwriter.write("\n------Trip Generate count is == " + trip
						+ "  for  " + UserList.get(id).getName()
						+ " , Device Id : " + UserList.get(id).getDeviceID());

				// bfwriter.write("\n------Trip Generate count is == "+trip);

			}// for end
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {

				if (bfwriter != null)
					bfwriter.close();

				if (writer != null)
					writer.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}

		return null;
	}

	public int Calculate_Trip(ArrayList<DBObject> location_list,
			DB mongoconnection, int tripTimeLimit, String DeviceId, String Name) {

		long TotalLocationData = location_list.size();
		Double MaxSpeed = 0.0;
		Double TotalSpeed = 0.0;
		int Triplocationcount = 0;
		Double SpeedcCheck = 1.0;
		ArrayList<DBObject> TempCalLoctionList = new ArrayList<>();
		int TripTimeLimit = tripTimeLimit;
		long SignalCheckcount = 0;
		long Timeidiff = 0;
		double HistoryDistanceKM = 0;
		int TripCount = 0;
		TempCalLoctionList.clear();
		for (int i = 0; i < TotalLocationData; i++) {

			SpeedcCheck = Double.parseDouble(location_list.get(i).get("speed")
					.toString());

			if (SpeedcCheck > 0.0)
				TempCalLoctionList.add(location_list.get(i));

			if (SpeedcCheck > MaxSpeed)
				MaxSpeed = SpeedcCheck;

			TotalSpeed = TotalSpeed + SpeedcCheck;

			if (i > 0 && i < TotalLocationData - 1 && SpeedcCheck > 0.0) {
				BasicDBObject locobj1 = (BasicDBObject) location_list.get(i)
						.get("location");
				double lat1 = locobj1.getDouble("lat");
				double lng1 = locobj1.getDouble("lon");

				BasicDBObject locobj2 = (BasicDBObject) location_list
						.get(i + 1).get("location");
				double lat2 = locobj2.getDouble("lat");
				double lng2 = locobj2.getDouble("lon");

				if (lat1 != lat2 && lng1 != lng2) {
					double caldist = distance(lat1, lng1, lat2, lng2, "K");

					if (!Double.isNaN(caldist) && caldist > 0.1) {
						HistoryDistanceKM = HistoryDistanceKM + caldist;

					}
					// //System.out.println("HistoryDistanceKM----"+HistoryDistanceKM);

				}

			}
			/*
			 * if (i>2 && i<TotalLocationData-3) { Long
			 * srctime=Long.parseLong(TempLoctionList
			 * .get(TempLoctionList.size()-2).get("timestamp").toString()); Long
			 * desttime
			 * =Long.parseLong(TempLoctionList.get(TempLoctionList.size()
			 * -1).get("timestamp").toString());
			 * 
			 * Timeidiff=desttime-srctime;
			 * 
			 * }
			 */

			if (i > 1 && SpeedcCheck == 0.0 && TempCalLoctionList.size() > 0) {

				if (Double.parseDouble(location_list.get(i - 1).get("speed")
						.toString()) == 0.0) {
					Long srctime = Long.parseLong(location_list.get(i - 1)
							.get("timestamp").toString());
					Long desttime = Long.parseLong(location_list.get(i)
							.get("timestamp").toString());

					long diff = desttime - srctime;

					SignalCheckcount = SignalCheckcount + diff;
					// //System.err.println("i-----"+i+"---------SpeedcCheck---"+SpeedcCheck+"-----SignalCheckcount---"+
					// SignalCheckcount+"----------Diff-----"+diff);

				} else {
					SignalCheckcount = 0;
				}

			}

			if ((SpeedcCheck == 0.0 && TempCalLoctionList.size() > 0
					&& HistoryDistanceKM > 1 && MaxSpeed > 0 && SignalCheckcount > TripTimeLimit)
					|| (TempCalLoctionList.size() > 0
							&& i == TotalLocationData - 1
							&& HistoryDistanceKM > 1 && MaxSpeed > 0)) {

				/*
				 * //System.out.println("********** Trip****Starttime***"+i+
				 * "**************"
				 * +Common.getDateCurrentTimeZone(Long.parseLong(
				 * TempCalLoctionList.get(0).get("timestamp").toString())));
				 * //System
				 * .out.println("********** Trip****EndTime******"+i+"***********"
				 * +
				 * Common.getDateCurrentTimeZone(Long.parseLong(TempCalLoctionList
				 * .
				 * get(TempCalLoctionList.size()-1).get("timestamp").toString())
				 * )); //System.err.println("-----SignalCheckcount Got ---"+
				 * SignalCheckcount
				 * +"   speedcheck=="+SpeedcCheck+"   Historydist---"
				 * +HistoryDistanceKM+"  MaxSpeed -"+MaxSpeed+"\n\n");
				 */

				BasicDBObject Maindocument = new BasicDBObject();
				ObjectId objid = new ObjectId();
				Maindocument.put("report_id", "" + objid);
				Maindocument.put("device", DeviceId);
				Maindocument.put("name", Name);

				Maindocument.put("totalkm", HistoryDistanceKM);
				Maindocument.put("maxspeed", MaxSpeed);
				if (TempCalLoctionList.size() > 0) {
					Maindocument.put("avgspeed", TotalSpeed
							/ TempCalLoctionList.size());
				} else {
					Maindocument.put("avgspeed", 0);
				}

				double locallat1, locallng1, locallat2, locallnt2;
				DBObject srcdbobj = TempCalLoctionList.get(0);

				BasicDBList addresses = (BasicDBList) location_list.get(i).get(
						"status");

				BasicDBObject locallocobj = (BasicDBObject) srcdbobj
						.get("location");
				locallat1 = locallocobj.getDouble("lat");
				locallng1 = locallocobj.getDouble("lon");

				BasicDBObject Intial_Loc = new BasicDBObject();
				/*
				 * Intial_Loc.put("lat", locallat1); Intial_Loc.put("lon",
				 * locallng1);
				 */
				String latDBObj = (String) ((BasicDBObject) addresses.get("3"))
						.get("lat_direction");
				String lanDBObj = (String) ((BasicDBObject) addresses.get("2"))
						.get("lon_direction");

				// //System.out.println("addresses****************   "+latDBObj+"  "+lanDBObj);

				if (latDBObj.equalsIgnoreCase("N")
						&& lanDBObj.equalsIgnoreCase("E")) {

					Intial_Loc.put("lat", locallat1);
					Intial_Loc.put("lon", locallng1);
					Intial_Loc.put("src_address",
							Common.GetAddress("" + locallat1, "" + locallng1));

				} else if (latDBObj.equalsIgnoreCase("N")
						&& lanDBObj.equalsIgnoreCase("W")) {

					Intial_Loc.put("lat", locallat1);
					Intial_Loc.put("lon", -locallng1);
					Intial_Loc.put("src_address",
							Common.GetAddress("" + locallat1, "-" + locallng1));

				} else if (latDBObj.equalsIgnoreCase("S")
						&& lanDBObj.equalsIgnoreCase("E")) {

					Intial_Loc.put("lat", -locallat1);
					Intial_Loc.put("lon", locallng1);
					Intial_Loc.put("src_address",
							Common.GetAddress("-" + locallat1, "" + locallng1));

				} else if (latDBObj.equalsIgnoreCase("S")
						&& lanDBObj.equalsIgnoreCase("W")) {

					Intial_Loc.put("lat", -locallat1);
					Intial_Loc.put("lon", -locallng1);
					Intial_Loc
							.put("src_address",
									Common.GetAddress("-" + locallat1, "-"
											+ locallng1));

				}

				Intial_Loc.put("speed", srcdbobj.get("speed"));
				Intial_Loc.put("timestamp", srcdbobj.get("timestamp"));
				Maindocument.put("source_info", Intial_Loc);

				DBObject destobj = TempCalLoctionList.get(TempCalLoctionList
						.size() - 1);

				BasicDBObject destlocobj = (BasicDBObject) destobj
						.get("location");
				locallat2 = destlocobj.getDouble("lat");
				locallnt2 = destlocobj.getDouble("lon");

				BasicDBObject Dest_Loc = new BasicDBObject();
				/*
				 * Dest_Loc.put("lat", locallat2); Dest_Loc.put("lon",
				 * locallnt2);
				 */
				if (latDBObj.equalsIgnoreCase("N")
						&& lanDBObj.equalsIgnoreCase("E")) {

					Dest_Loc.put("lat", locallat2);
					Dest_Loc.put("lon", locallnt2);
					Dest_Loc.put("dest_address",
							Common.GetAddress("" + locallat2, "" + locallnt2));

				} else if (latDBObj.equalsIgnoreCase("N")
						&& lanDBObj.equalsIgnoreCase("W")) {

					Dest_Loc.put("lat", locallat2);
					Dest_Loc.put("lon", -locallnt2);
					Dest_Loc.put("dest_address",
							Common.GetAddress("" + locallat2, "-" + locallnt2));

				} else if (latDBObj.equalsIgnoreCase("S")
						&& lanDBObj.equalsIgnoreCase("E")) {

					Dest_Loc.put("lat", -locallat2);
					Dest_Loc.put("lon", locallnt2);
					Dest_Loc.put("dest_address",
							Common.GetAddress("-" + locallat2, "" + locallnt2));

				} else if (latDBObj.equalsIgnoreCase("S")
						&& lanDBObj.equalsIgnoreCase("W")) {

					Dest_Loc.put("lat", -locallat2);
					Dest_Loc.put("lon", -locallnt2);
					Dest_Loc.put("dest_address",
							Common.GetAddress("-" + locallat2, "-" + locallnt2));

				}

				Dest_Loc.put("speed", destobj.get("speed"));
				Dest_Loc.put("timestamp", destobj.get("timestamp"));
				Maindocument.put("dest_info", Dest_Loc);

				DBCollection Triptable = mongoconnection
						.getCollection(Common.TABLE_TRIPREPORT);

				Triptable.insert(Maindocument);
				// System.out.println("HistoryDistanceKM--before clear--"+HistoryDistanceKM);

				HistoryDistanceKM = 0.0;
				TempCalLoctionList.clear();
				SignalCheckcount = 0;
				MaxSpeed = 0.0;
				TotalSpeed = 0.0;
				Timeidiff = 0;
				Triplocationcount = 0;
				TripCount++;

			} else if (HistoryDistanceKM < 1) {
				SignalCheckcount = 0;
				MaxSpeed = 0.0;
				TotalSpeed = 0.0;
				Timeidiff = 0;
				// HistoryDistanceKM=0;
				Triplocationcount = 0;
				// TempCalLoctionList.clear();
			}

		}

		return TripCount;
	}

	/*
	 * public MessageObject GenerateTripReportDemoTest(Connection con, DB
	 * mongoconnection) { ArrayList<DeviceInfoDto> UserList=new ArrayList<>();
	 * ArrayList<DBObject> TempLoctionList=new ArrayList<>(); int
	 * TripTimeLimit=300; File tripfile; int Deaactivecount=0; FileWriter writer
	 * = null; BufferedWriter bfwriter=null; calendar = Calendar.getInstance();
	 * ArrayList<Double> DeaciveListDevice=new ArrayList<Double>();
	 * 
	 * try { //Whatever the file path is. tripfile = new
	 * File(Common.Log_path+"GenerateTripReport_log"+calendar.getTime()+".txt");
	 * //Create the file if (tripfile.createNewFile()){
	 * //System.out.println("File is created!"); }else{
	 * //System.out.println("File already exists."); }
	 * 
	 * //Write Content writer = new FileWriter(tripfile);
	 * 
	 * 
	 * bfwriter = new BufferedWriter(writer);
	 * bfwriter.write("\nStart Generate Daily TripReprt Log----at "
	 * +calendar.getTime());
	 * 
	 * } catch (IOException e) {
	 * //System.err.println("Problem writing to the file statsTest.txt"); }catch
	 * (Exception e) {
	 * 
	 * e.printStackTrace(); }
	 * 
	 * int trip=0; calendar = Calendar.getInstance();
	 * 
	 * 
	 * try{ PreparedStatement ps=con.prepareStatement(
	 * "select FirstName,LastName ,DeviceID,DeviceType from StudentMaster where TripReportAllow=0"
	 * ); ResultSet rs=ps.executeQuery(); if (rs!=null) { while (rs.next()) {
	 * DeviceInfoDto dto=new DeviceInfoDto();
	 * dto.setName(rs.getString(1)+rs.getString(2));
	 * dto.setDeviceID(rs.getString(3)); dto.setDeviceType(rs.getInt(4));
	 * 
	 * if(dto.getDeviceType()==1) { UserList.add(dto);
	 * //System.out.println("--------Report Req--Allow---"
	 * +rs.getString(1)+rs.getString(2)+"-----"+rs.getInt(4));
	 * 
	 * }
	 * 
	 * } } }catch(Exception e){
	 * 
	 * e.printStackTrace(); }
	 * 
	 * 
	 * try{ PreparedStatement ps1=con.prepareStatement(
	 * "select ActualData from ApplicationMetaData where ApplicationMetaData.ID=5"
	 * ); ResultSet rs1=ps1.executeQuery(); if (rs1!=null) { while (rs1.next())
	 * { TripTimeLimit=Integer.parseInt(rs1.getString(1));
	 * 
	 * } }
	 * //System.err.println("TripTimeLimit--TEstttttttttttt-------"+TripTimeLimit
	 * ); }catch(Exception e){
	 * 
	 * e.printStackTrace(); }
	 * 
	 * year = calendar.get(Calendar.YEAR); month = calendar.get(Calendar.MONTH);
	 * day = calendar.get(Calendar.DAY_OF_MONTH); /* long Starttime =
	 * Long.valueOf(Common.getGMTTimeStampFromDate(day-1+ "-"+
	 * String.valueOf(month+1) + "-" + year+" 00:00 am")); long Endtime =
	 * Long.valueOf(Common.getGMTTimeStampFromDate(day+ "-"+
	 * String.valueOf(month+1) + "-" + year+" 00:00 am"));
	 */
	/*
	 * long Starttime = 1507487400; long Endtime =1507573800;
	 * 
	 * //System.out.println("StartTime:"+Starttime+"  End Time:"+Endtime+
	 * "  UserList size: "+UserList.size());
	 * 
	 * 
	 * 
	 * 
	 * 
	 * try { bfwriter.write(
	 * "\n----------TEStttt-------------Report Generating  for---------------------------"
	 * + "\n\n******************"+"StartTime: " + Starttime +
	 * "  End Time: "+Endtime+"********");
	 * 
	 * //for(int id=0;id<UserList.size();id++){
	 * 
	 * trip=0; HistoryDistance=0;
	 * 
	 * DBCollection table =
	 * mongoconnection.getCollection(Common.TABLE_LOCATION); BasicDBObject
	 * device_whereQuery = new BasicDBObject();
	 * //device_whereQuery.put("device",
	 * Long.parseLong(UserList.get(id).getDeviceID()));
	 * device_whereQuery.put("device",Long.parseLong("358740050508315"));
	 * 
	 * BasicDBObject timestamp_whereQuery = new BasicDBObject("timestamp", new
	 * BasicDBObject("$gte",Starttime).append("$lt",Endtime)); BasicDBList
	 * And_Milage = new BasicDBList(); And_Milage.add(timestamp_whereQuery);
	 * And_Milage.add(device_whereQuery); DBObject Total_Milage_query = new
	 * BasicDBObject("$and", And_Milage);
	 * 
	 * DBCursor cursor = table.find(Total_Milage_query);
	 * cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
	 * 
	 * //System.out.println("Cursor Count of u-p11----"+cursor.size()+"  "+
	 * Total_Milage_query); long i=0; long total=cursor.size(); Double
	 * MaxSpeed=0.0; Double TotalSpeed=0.0; int Triplocationcount=0;
	 * 
	 * if(cursor.size()!=0) { TempLoctionList.clear();
	 * 
	 * //System.err.println("*-----COunt---"+cursor.size());
	 * 
	 * ArrayList<DBObject> Location_list=new ArrayList<>();
	 * while(cursor.hasNext()){ Location_list.add(cursor.next()); }
	 * 
	 * //
	 * trip=Calculate_Trip(Location_list,mongoconnection,TripTimeLimit,UserList
	 * .get(id).getDeviceID(),UserList.get(id).getName());
	 * trip=Calculate_Trip(Location_list
	 * ,mongoconnection,TripTimeLimit,"358740050508315","TranquilBangalore");
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * //
	 * bfwriter.write("\n------Trip Generate count is == "+trip+"  for  "+UserList
	 * .get(id).getName()+" , Device Id : "+UserList.get(id).getDeviceID());
	 * 
	 * //bfwriter.write("\n------Trip Generate count is == "+trip);
	 * 
	 * 
	 * 
	 * 
	 * 
	 * // }//for end } catch (NumberFormatException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } catch (MongoException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch (IOException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); }catch (Exception
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); } finally{
	 * 
	 * try {
	 * 
	 * if (bfwriter != null) bfwriter.close();
	 * 
	 * if (writer != null) writer.close();
	 * 
	 * } catch (IOException ex) {
	 * 
	 * ex.printStackTrace();
	 * 
	 * } }
	 * 
	 * 
	 * 
	 * return null; }
	 */

	private int Calculate_ID_CardTrip(ArrayList<DBObject> location_list,
			DB mongoconnection, int tripTimeLimit, String deviceID, String name) {

		Collections.reverse(location_list);
		long TotalLocationData = location_list.size();
		Double MaxSpeed = 0.0;
		Double TotalSpeed = 0.0;
		int Triplocationcount = 0;
		Double SpeedcCheck = 1.0;
		;
		ArrayList<DBObject> TempCalLoctionList = new ArrayList<>();
		int TripTimeLimit = tripTimeLimit;
		long SignalCheckcount = 0;
		long Timeidiff = 0;
		double HistoryDistanceKM = 0;
		int TripCount = 0;
		TempCalLoctionList.clear();
		Double center_lat = 0.0;
		Double centre_lan = 0.0;
		int far_count = 0;
		int stop_count = 0;
		for (int i = 0; i < TotalLocationData; i++) {

			SpeedcCheck = Double.parseDouble(location_list.get(i).get("speed")
					.toString());

			// //System.err.println("------Speedcheck-------"+i+"----"+Common.getDateCurrentTimeZone(Long.parseLong(location_list.get(i).get("timestamp")+""))+"---------"+SpeedcCheck);
			if (i == 0) {
				BasicDBObject centare = (BasicDBObject) location_list.get(i)
						.get("location");
				center_lat = centare.getDouble("lat");
				centre_lan = centare.getDouble("lon");
			}

			if (i > 0) {
				BasicDBObject destination = (BasicDBObject) location_list
						.get(i).get("location");
				double dest_lat = destination.getDouble("lat");
				double dest_lan = destination.getDouble("lon");
				double distance_calculated = distance(center_lat, centre_lan,
						dest_lat, dest_lan, "K");
				if (distance_calculated < 0.200) {
					stop_count++;
					/*
					 * System.out.println("***stop_count---*********"+stop_count+
					 * "--Time----"+
					 * Common.getDateCurrentTimeZone(Long.parseLong
					 * (location_list.get(i).get("timestamp").
					 * toString()))+"---Distcal--"+distance_calculated);
					 */
					// stop_count = 0;
					/*
					 * } else { // far_count=0; BasicDBObject centare =
					 * (BasicDBObject) location_list.get(i).get("location");
					 * center_lat = centare.getDouble("lat"); centre_lan =
					 * centare.getDouble("lon"); }
					 */
				} else {

					far_count++;
					/*
					 * System.err.println("***far_count---*********" + far_count
					 * + "--Time----" + Common.getDateCurrentTimeZone(Long
					 * .parseLong(location_list .get(i) .get("timestamp")
					 * .toString()))+"  -----Distal--   "+distance_calculated);
					 */

					stop_count = 0;
					BasicDBObject centare = (BasicDBObject) location_list
							.get(i).get("location");
					center_lat = centare.getDouble("lat");
					centre_lan = centare.getDouble("lon");
				}

			}

			if (far_count > 1) {
				TempCalLoctionList.add(location_list.get(i));
				// System.err.println("***TempLoctionList---*********"+i+"*******Size********--"+TempCalLoctionList.size());

			}

			if (SpeedcCheck > MaxSpeed)
				MaxSpeed = SpeedcCheck;

			TotalSpeed = TotalSpeed + SpeedcCheck;

			if (i > 0 && i < TotalLocationData - 1 && far_count > 1) {
				BasicDBObject locobj1 = (BasicDBObject) location_list.get(i)
						.get("location");
				double lat1 = locobj1.getDouble("lat");
				double lng1 = locobj1.getDouble("lon");

				BasicDBObject locobj2 = (BasicDBObject) location_list
						.get(i + 1).get("location");
				double lat2 = locobj2.getDouble("lat");
				double lng2 = locobj2.getDouble("lon");

				if (lat1 != lat2 && lng1 != lng2) {
					double caldist = distance(lat1, lng1, lat2, lng2, "K");

					if (!Double.isNaN(caldist)) {
						HistoryDistanceKM = HistoryDistanceKM + caldist;

					}
					// //System.out.println("HistoryDistanceKM----"+HistoryDistanceKM);

				}

			}
			/*
			 * if (i>2 && i<TotalLocationData-3) { Long
			 * srctime=Long.parseLong(TempLoctionList
			 * .get(TempLoctionList.size()-2).get("timestamp").toString()); Long
			 * desttime
			 * =Long.parseLong(TempLoctionList.get(TempLoctionList.size()
			 * -1).get("timestamp").toString());
			 * 
			 * Timeidiff=desttime-srctime;
			 * 
			 * }
			 */

			/*
			 * if(i>1&&stop_count>5&&TempCalLoctionList.size()>0) {
			 * 
			 * if
			 * (Integer.parseInt(location_list.get(i-1).get("speed").toString(
			 * ))==0) { Long
			 * srctime=Long.parseLong(location_list.get(i-1).get("timestamp"
			 * ).toString()); Long
			 * desttime=Long.parseLong(location_list.get(i).get
			 * ("timestamp").toString());
			 * 
			 * long diff = desttime - srctime;
			 * 
			 * SignalCheckcount=SignalCheckcount+diff;
			 * //System.err.println("i-----"
			 * +i+"---------SpeedcCheck---"+SpeedcCheck
			 * +"-----SignalCheckcount---"+
			 * SignalCheckcount+"----------Diff-----"+diff);
			 * 
			 * }else { SignalCheckcount=0; }
			 * 
			 * 
			 * }
			 */

			if ((stop_count >= 5 && TempCalLoctionList.size() > 0 && HistoryDistanceKM > 1)
					|| (TempCalLoctionList.size() > 0 && i == TotalLocationData - 1)) {

				// System.err.println("Stop Count-------------"+stop_count+"--- for-----------"+name);
				// System.err.println("TempCalLoctionListt-------------"+TempCalLoctionList.size()+"--- for-----------"+name);

				// System.out.println("********** Trip****Starttime***"+i+"**************"+Common.getDateCurrentTimeZone(Long.parseLong(TempCalLoctionList.get(0).get("timestamp").toString())));
				// System.out.println("********** Trip****EndTime******"+i+"***********"+Common.getDateCurrentTimeZone(Long.parseLong(TempCalLoctionList.get(TempCalLoctionList.size()-1).get("timestamp").toString())));

				Collections.reverse(TempCalLoctionList);

				// //System.err.println("-----SignalCheckcount Got ---"+
				// SignalCheckcount+"   speedcheck=="+SpeedcCheck+"   Historydist---"+HistoryDistanceKM+"  MaxSpeed -"+MaxSpeed+"\n\n");

				BasicDBObject Maindocument = new BasicDBObject();
				ObjectId objid = new ObjectId();
				Maindocument.put("report_id", "" + objid);
				Maindocument.put("device", deviceID);
				Maindocument.put("name", name);

				Maindocument.put("totalkm", HistoryDistanceKM);
				Maindocument.put("maxspeed", MaxSpeed);

				if (TempCalLoctionList.size() > 0) {
					Maindocument.put("avgspeed", TotalSpeed
							/ TempCalLoctionList.size());
				} else {
					Maindocument.put("avgspeed", 0);
				}

				double locallat1, locallng1, locallat2, locallnt2;
				DBObject srcdbobj = TempCalLoctionList.get(0);

				BasicDBList addresses = (BasicDBList) location_list.get(i).get(
						"status");

				BasicDBObject locallocobj = (BasicDBObject) srcdbobj
						.get("location");
				locallat1 = locallocobj.getDouble("lat");
				locallng1 = locallocobj.getDouble("lon");

				BasicDBObject Intial_Loc = new BasicDBObject();
				/*
				 * Intial_Loc.put("lat", locallat1); Intial_Loc.put("lon",
				 * locallng1);
				 */
				String latDBObj = (String) ((BasicDBObject) addresses.get("3"))
						.get("lat_direction");
				String lanDBObj = (String) ((BasicDBObject) addresses.get("2"))
						.get("lon_direction");

				// //System.out.println("addresses****************   "+latDBObj+"  "+lanDBObj);

				if (latDBObj.equalsIgnoreCase("N")
						&& lanDBObj.equalsIgnoreCase("E")) {

					Intial_Loc.put("lat", locallat1);
					Intial_Loc.put("lon", locallng1);
					Intial_Loc.put("src_address",
							Common.GetAddress("" + locallat1, "" + locallng1));

				} else if (latDBObj.equalsIgnoreCase("N")
						&& lanDBObj.equalsIgnoreCase("W")) {

					Intial_Loc.put("lat", locallat1);
					Intial_Loc.put("lon", -locallng1);
					Intial_Loc.put("src_address",
							Common.GetAddress("" + locallat1, "-" + locallng1));

				} else if (latDBObj.equalsIgnoreCase("S")
						&& lanDBObj.equalsIgnoreCase("E")) {

					Intial_Loc.put("lat", -locallat1);
					Intial_Loc.put("lon", locallng1);
					Intial_Loc.put("src_address",
							Common.GetAddress("-" + locallat1, "" + locallng1));

				} else if (latDBObj.equalsIgnoreCase("S")
						&& lanDBObj.equalsIgnoreCase("W")) {

					Intial_Loc.put("lat", -locallat1);
					Intial_Loc.put("lon", -locallng1);
					Intial_Loc
							.put("src_address",
									Common.GetAddress("-" + locallat1, "-"
											+ locallng1));

				}

				Intial_Loc.put("speed", srcdbobj.get("speed"));
				Intial_Loc.put("timestamp", srcdbobj.get("timestamp"));
				Maindocument.put("source_info", Intial_Loc);

				DBObject destobj = TempCalLoctionList.get(TempCalLoctionList
						.size() - 1);

				BasicDBObject destlocobj = (BasicDBObject) destobj
						.get("location");
				locallat2 = destlocobj.getDouble("lat");
				locallnt2 = destlocobj.getDouble("lon");

				BasicDBObject Dest_Loc = new BasicDBObject();
				/*
				 * Dest_Loc.put("lat", locallat2); Dest_Loc.put("lon",
				 * locallnt2);
				 */
				if (latDBObj.equalsIgnoreCase("N")
						&& lanDBObj.equalsIgnoreCase("E")) {

					Dest_Loc.put("lat", locallat2);
					Dest_Loc.put("lon", locallnt2);
					Dest_Loc.put("dest_address",
							Common.GetAddress("" + locallat2, "" + locallnt2));

				} else if (latDBObj.equalsIgnoreCase("N")
						&& lanDBObj.equalsIgnoreCase("W")) {

					Dest_Loc.put("lat", locallat2);
					Dest_Loc.put("lon", -locallnt2);
					Dest_Loc.put("dest_address",
							Common.GetAddress("" + locallat2, "-" + locallnt2));

				} else if (latDBObj.equalsIgnoreCase("S")
						&& lanDBObj.equalsIgnoreCase("E")) {

					Dest_Loc.put("lat", -locallat2);
					Dest_Loc.put("lon", locallnt2);
					Dest_Loc.put("dest_address",
							Common.GetAddress("-" + locallat2, "" + locallnt2));

				} else if (latDBObj.equalsIgnoreCase("S")
						&& lanDBObj.equalsIgnoreCase("W")) {

					Dest_Loc.put("lat", -locallat2);
					Dest_Loc.put("lon", -locallnt2);
					Dest_Loc.put("dest_address",
							Common.GetAddress("-" + locallat2, "-" + locallnt2));

				}

				Dest_Loc.put("speed", destobj.get("speed"));
				Dest_Loc.put("timestamp", destobj.get("timestamp"));
				Maindocument.put("dest_info", Dest_Loc);

				DBCollection Triptable = mongoconnection
						.getCollection(Common.TABLE_TRIPREPORT);

				Triptable.insert(Maindocument);
				HistoryDistanceKM = 0.0;
				TempCalLoctionList.clear();
				SignalCheckcount = 0;
				MaxSpeed = 0.0;
				TotalSpeed = 0.0;
				Timeidiff = 0;
				Triplocationcount = 0;
				TripCount++;
				far_count = 0;
				stop_count = 0;
				center_lat = locallat2;
				centre_lan = locallnt2;

			} else if (HistoryDistanceKM < 1) {
				SignalCheckcount = 0;
				MaxSpeed = 0.0;
				TotalSpeed = 0.0;
				Timeidiff = 0;
				Triplocationcount = 0;

				// TempCalLoctionList.clear();
			}

		}

		return TripCount;

	}

	public MessageObject SendForgrtPassword(Connection con, String email) {

		MessageObject msgobj = new MessageObject();
		String name, pass;

		try {
			ResultSet rs = null;
			// Made changes in select for email;

			int rs_starttime = 0;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call GetForgetPassword(?)}");
			stmt.setString(1, email);

			// System.out.println("****************GetForgetPassword***************");

			rs = stmt.executeQuery();

			if (rs != null && rs.next()) {
				// System.out.println("****************GetForgetPassword***************"+rs.getString("Password"));

				name = rs.getString("Name");
				pass = rs.getString("Password");
				String username = rs.getString("UserName");

				msgobj.setMessage("Your credentials are sent to your email Id. Please check your mail");
				msgobj.setError("false");

				SendEmail se = new SendEmail();
				Boolean sent = se.SendForgetEmail(name, email, pass, username);
				/*
				 * if (sent) {
				 * msgobj.setMsg("Email Id is not Valid;Please try Again");
				 * msgobj.setError("false");
				 * 
				 * 
				 * }else { msgobj.setMsg(
				 * "Email Id not register.Please enter valid email Id.");
				 * msgobj.setError("true");
				 * 
				 * }
				 */

			} else {
				msgobj.setMessage("This Email Id Not registered Please try  another Email Id or Sign In.");
				msgobj.setError("true");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				con.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return msgobj;

	}

	public ArrayList<DeviceStatusDTO> Get_DeviceStatus(DB mongoconnection,
			JSONArray devielist) {

		ArrayList<DeviceStatusDTO> device_list = new ArrayList<>();

		ArrayList<MilageDTO> list = new ArrayList<MilageDTO>();

		DBCollection table = mongoconnection
				.getCollection(Common.TABLE_LOCATION);

		for (int i = 0; i < devielist.length(); i++) {
			long deviceimei = 0;

			DeviceStatusDTO dto = new DeviceStatusDTO();
			try {
				deviceimei = devielist.getLong(i);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device", deviceimei);

			DBCursor cursor = table.find(device_whereQuery).limit(1)
					.sort(new BasicDBObject("_id", -1));
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			// System.out.print("Cursor Count of u-p22----"+cursor.size()+"  "+device_whereQuery);

			if (cursor.size() != 0) {

				// System.err.println("*-----COunt---"+cursor.size());

				while (cursor.hasNext()) {

					DBObject dbObject = (DBObject) cursor.next();
					int speed = (int) dbObject.get("speed");
					int timestamp = (int) dbObject.get("timestamp");

					SimpleDateFormat sdf = new SimpleDateFormat(
							"dd-MM-yyyy hh:mm aa");
					Timestamp currenttimestamp = new Timestamp(
							System.currentTimeMillis());
					long datediff = Common.getGMTTimeStampFromDate((sdf
							.format(currenttimestamp))) - (timestamp);

					if (speed > 0 && datediff < Common.DeviceStatusTimeDiff) {
						dto.setDeviceID(deviceimei + "");
						dto.setStatus("Moving");
						dto.setColor(Common.GREEN);

					}

					else if (speed == 0
							&& datediff < Common.DeviceStatusTimeDiff) {
						dto.setDeviceID(deviceimei + "");
						dto.setStatus("Device On");
						dto.setColor(Common.BLUE);

					}

					else if (datediff > Common.DeviceStatusTimeDiff) {
						dto.setDeviceID(deviceimei + "");
						dto.setStatus("Device Off");
						dto.setColor(Common.RED);

					}

				}

			}

			device_list.add(dto);

		}

		// System.out.print(new Gson().toJson(device_list));
		return device_list;

	}

	public MessageObject GetTripReport(Connection con, DB mongoconnection,
			String device, String startdate, String enddate, String email) {

		String offset_otDevice = "GMT+05:30";
		// Common.GetDateTimeWithOffset(Long.parseLong("1519814215"), "-5:00");
		TimeOffsetDTO obj = new TimeOffsetDTO();
		String empname = "N/A";
		try {
			java.sql.CallableStatement ps = con
					.prepareCall("{call GetOffsetOfdevice(?)}");
			ps.setString(1, device);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					obj.setOffsetId("" + rs.getString("Id"));
					obj.setOffsetActual("" + rs.getString("OffsetActual"));
					obj.setOffsetUponIndia("" + rs.getString("OffsetUponIndia"));
					obj.setCountyName("" + rs.getString("CountyName"));
					obj.setFlag("" + rs.getString("flag"));

					offset_otDevice = obj.getOffsetActual();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (obj.getFlag() != null && obj.getFlag().equalsIgnoreCase("15")) {

			try {
				java.sql.CallableStatement ps = con
						.prepareCall("{call GetDriverUponTaskOnthisDay(?,?,?)}");
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(Long.parseLong(startdate));
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);

				ps.setInt(1, year);
				ps.setInt(2, month + 1);
				ps.setInt(3, day);

				ResultSet rs = ps.executeQuery();
				int i = 0;

				if (rs != null) {
					while (rs.next()) {
						if (i == 0) {
							empname = "" + rs.getString("Name");
						} else {
							empname.concat("/" + rs.getString("Name"));
						}

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

		}

		ArrayList<TripInfoDto> Triplist = new ArrayList<TripInfoDto>();
		MessageObject msg = new MessageObject();
		DBCollection table = mongoconnection
				.getCollection(Common.TABLE_TRIPREPORT);
		// System.out.println("device==============----------"+device+" "+table.getFullName());

		BasicDBObject device_whereQuery = new BasicDBObject();
		device_whereQuery.put("device", device);

		BasicDBObject timestamp_whereQuery = new BasicDBObject(
				"source_info.timestamp", new BasicDBObject("$gte",
						Long.parseLong(startdate)).append("$lt",
						Long.parseLong(enddate)));

		BasicDBList And_Milage = new BasicDBList();
		And_Milage.add(timestamp_whereQuery);
		And_Milage.add(device_whereQuery);
		DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

		DBCursor cursor = table.find(Total_Milage_query).sort(
				new BasicDBObject("source_info.timestamp", 1));
		;
		cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

		// System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+Total_Milage_query);
		long i = 0;
		long total = cursor.count();

		if (cursor.size() != 0) {

			// System.err.println("*-----COunt---"+cursor.size());

			while (cursor.hasNext()) {

				TripInfoDto trip = new TripInfoDto();
				dbObject1 = (DBObject) cursor.next();

				BasicDBObject Sourceobj = (BasicDBObject) dbObject1
						.get("source_info");
				BasicDBObject Destobj = (BasicDBObject) dbObject1
						.get("dest_info");

				trip.setDevice(device);
				trip.setSrclat(Sourceobj.getString("lat"));
				trip.setSrclon(Sourceobj.getString("lon"));
				trip.setSrcspeed(Sourceobj.getString("speed"));
				trip.setSrc_adress(Sourceobj.getString("src_address"));

				trip.setSrctimestamp(Sourceobj.getString("timestamp"));

				trip.setDestlat(Destobj.getString("lat"));
				trip.setDestlon(Destobj.getString("lon"));
				trip.setDestspeed(Destobj.getString("speed"));
				trip.setDest_address(Destobj.getString("dest_address"));

				trip.setDesttimestamp(Destobj.getString("timestamp"));

				trip.setReport_id(dbObject1.get("report_id").toString());
				trip.setTotalkm(dbObject1.get("totalkm").toString());
				trip.setMaxspeed(dbObject1.get("maxspeed").toString());
				trip.setAvgspeed(String.format("%.2f",
						dbObject1.get("avgspeed")));

				trip.setDevicename(dbObject1.get("name").toString());
				// trip.setDevicename("vts");

				Triplist.add(trip);

			}

		}
		SendEmail se = new SendEmail();
		Boolean sent = false;
		if (obj.getFlag() != null)
			sent = se.SendTripReportEmail(Triplist, email, offset_otDevice,
					empname, obj.getFlag());
		else {
			sent = se.SendTripReportEmail(Triplist, email, offset_otDevice,
					empname, "0");

		}

		if (sent) {
			msg.setError("false");
			msg.setMessage("Trip report sent successfully.");
		} else {
			msg.setError("true");
			msg.setMessage("Error in sending Trip Report.");
		}

		// System.err.println("Get Triipo Report=="+msg.getMessage());

		return msg;

	}

	public MessageObject UpdateAllowFlag(Connection con, String vtsSmsAllow,
			String aCCReportAllow, String accEOD, String aCCSqliteEnable,
			String email) {

		MessageObject msgo = new MessageObject();

		try {

			int result = 0;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call UpdateAllowFlag(?,?,?,?,?)}");
			stmt.setString(1, vtsSmsAllow);
			stmt.setString(2, aCCReportAllow);
			stmt.setString(3, accEOD);
			stmt.setString(4, aCCSqliteEnable);
			stmt.setString(5, email);

			result = stmt.executeUpdate();

			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Flag  is not update successfully");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Flag  is update successfully");
			}
		} catch (Exception e) {
			msgo.setError("true");
			msgo.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}

		return msgo;
	}

	public MessageObject UpdateAllowRepotFlag(Connection con, String allowFlag,
			String email) {

		MessageObject msgo = new MessageObject();

		try {

			int result = 0;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call UpdateAllowRepotFlag(?,?)}");
			stmt.setString(1, allowFlag);
			stmt.setString(2, email);

			result = stmt.executeUpdate();

			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Flag  is not update successfully");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("Flag  is update successfully");
			}
		} catch (Exception e) {
			msgo.setError("true");
			msgo.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}

		return msgo;
	}

	public MessageObject postGuestUserRequest(Connection con, String name,
			String email, String contact, String msg, String type, String city) {

		// TODO Auto-generated method stub
		MessageObject msgo = new MessageObject();

		try {

			int result = 0;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call SaveGuestUserMaster(?,?,?,?,?,?)}");
			stmt.setString(1, name);
			stmt.setString(2, email);
			stmt.setString(3, contact);
			stmt.setString(4, msg);
			stmt.setString(5, type);
			stmt.setString(6, city);
			result = stmt.executeUpdate();

			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("GusetUser is not update successfully");
			} else {
				// //System.err.println("Error=="+result);
				msgo.setError("false");
				msgo.setMessage("GusetUser  is update successfully");
			}
		} catch (Exception e) {
			msgo.setError("true");
			msgo.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}

		return msgo;

	}

	public MessageObject GetMilage(DB mongoconnection, String device,
			String startdate, String enddate) {
		ArrayList<MilageDTO> list = new ArrayList<MilageDTO>();
		MessageObject msgData = new MessageObject();
		DBObject dbObject = null;
		File GetMilagefile;
		FileWriter writer = null;
		calendar = Calendar.getInstance();

		try {
			// Whatever the file path is.
			GetMilagefile = new File(Common.Log_path + "GetMilageReport.txt");
			// Create the file
			if (GetMilagefile.createNewFile()) {
				// System.out.println("File is created!");
			} else {
				// System.out.println("File already exists.");
			}

			// Write Content
			writer = new FileWriter(GetMilagefile, true);
			writer.write("\n GetMilageReport Started for  " + device
					+ "  from :" + startdate + " to :" + enddate + " at "
					+ Calendar.getInstance().getTime());

		} catch (IOException e) {
			// System.err.println("Problem writing to the file statsTest.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}

		DBCollection table = mongoconnection
				.getCollection(Common.TABLE_TRIPREPORT);
		BasicDBObject device_whereQuery = new BasicDBObject();
		device_whereQuery.put("device", device);

		BasicDBObject timestamp_whereQuery = new BasicDBObject(
				"source_info.timestamp", new BasicDBObject("$gte",
						Long.parseLong(startdate)).append("$lt",
						Long.parseLong(enddate)));

		BasicDBList And_Milage = new BasicDBList();
		And_Milage.add(timestamp_whereQuery);
		And_Milage.add(device_whereQuery);
		DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

		DBCursor cursor = table.find(Total_Milage_query);
		cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

		// System.out.println("Cursor GetMilage1 Count of --"+cursor.size()+"  "+cursor.getQuery());
		long i = 0;
		long total = cursor.size();

		if (cursor.size() != 0) {

			// System.err.println("*----GetMilage1-COunt---"+cursor.size());

			while (cursor.hasNext()) {

				dbObject = (DBObject) cursor.next();

				double distKm = (double) dbObject.get("totalkm");

				HistoryDistance = HistoryDistance + distKm;

			}

		}
		if (HistoryDistance != 0.0) {
			msgData.setError("false");
			msgData.setMessage(HistoryDistance + "");
			msgData.setName(device);

			try {
				writer.write("\n GetMilageReport Completed for  " + device
						+ "  from :" + startdate + " to :" + enddate + " at "
						+ Calendar.getInstance().getTime() + "with Milage :"
						+ HistoryDistance);
				writer.write("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------------");

				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			msgData.setError("true");
			msgData.setMessage("Report not found for selected Date");
		}

		return msgData;
	}

	public MessageObject DropLocation(DB mongoconnection) throws IOException {

		MessageObject msg = new MessageObject();
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);

		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long Droptime = Long.valueOf(Common.getGMTTimeStampFromDate(day - 35
				+ "-" + String.valueOf(month + 1) + "-" + year + " 00:00 am"));

		File DropLocation;
		FileWriter writer = null;
		calendar = Calendar.getInstance();

		try {
			// Whatever the file path is.
			DropLocation = new File(Common.Log_path + "DropLocation.txt");
			// Create the file
			if (DropLocation.createNewFile()) {
				// System.out.println("DropLocation File is created!");
			} else {
				// System.out.println("DropLocation File already exists.");
			}

			// Write Content
			writer = new FileWriter(DropLocation, true);
			writer.write("\n  DropLocation   Log Started  at "
					+ Calendar.getInstance().getTime());

		} catch (IOException e) {
			// System.err.println("Problem writing to the file DropLocation.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.println("Droptime:-----------"+Droptime+"");

		try {
			DBCollection collection = mongoconnection
					.getCollection(Common.TABLE_LOCATION);
			BasicDBObject timestamp_whereQuery = new BasicDBObject();
			timestamp_whereQuery.put("timestamp", new BasicDBObject("$lt",
					Droptime));
			WriteResult cursor = collection.remove(timestamp_whereQuery);

			// System.out.print("WriteResult  of u-p11----"+cursor.getError()+"  "+timestamp_whereQuery);

			if (cursor.getN() > 0) {
				msg.setError("False");
				msg.setMessage(" Location Drop Suceesfully which is before "
						+ day + "-" + month + "-" + year);

				writer.write(" \n Location Drop Suceesfully which is before  "
						+ Droptime);
				writer.write(" \n------------------------------------------------------------------------------------");

			} else {
				msg.setError("True");
				msg.setMessage("Location not drop suceesfully which is before "
						+ day + "-" + month + "-" + year + "  Got an erorr---"
						+ cursor.getN());
				writer.write("\n Location not Drop Suceesfully which is before  "
						+ Droptime);

			}

			writer.close();

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			msg.setError("True");
			msg.setMessage("Location not drop suceesfully which is before "
					+ day + "-" + month + "-" + year);
			writer.write(" \n Location not Drop Suceesfully which is before  "
					+ Droptime + "due to " + e.getMessage());
			writer.write(" \n------------------------------------------------------------------------------------");

			writer.close();

		} finally {
		}

		return msg;
	}

	public MessageObject DropTripReport(DB mongoconnection) throws IOException {

		// TODO Auto-generated method stub
		MessageObject msg = new MessageObject();
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);

		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long Droptime = Long.valueOf(Common.getGMTTimeStampFromDate(day - 95
				+ "-" + String.valueOf(month + 1) + "-" + year + " 00:00 am"));
		File DropTripReport;
		FileWriter writer = null;
		calendar = Calendar.getInstance();

		try {
			// Whatever the file path is.
			DropTripReport = new File(Common.Log_path + "DropTripReport.txt");
			// Create the file
			if (DropTripReport.createNewFile()) {
				// System.out.println("DropTripReport File is created!");
			} else {
				// System.out.println("DropTripReport File already exists.");
			}

			// Write Content
			writer = new FileWriter(DropTripReport, true);
			writer.write("\n  DropTripReport   Log Started  at "
					+ Calendar.getInstance().getTime());

		} catch (IOException e) {
			// System.err.println("Problem writing to the file DropTripReport.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.println("Droptime:-----------"+Droptime+"");

		try {
			DBCollection collection = mongoconnection
					.getCollection(Common.TABLE_TRIPREPORT);
			BasicDBObject timestamp_whereQuery = new BasicDBObject();
			timestamp_whereQuery.put("source_info.timestamp",
					new BasicDBObject("$lt", Droptime));

			WriteResult cursor = collection.remove(timestamp_whereQuery);

			// System.out.print("WriteResult  of mongo  query----"+cursor.getError()+"  "+timestamp_whereQuery);

			if (cursor.getN() > 0) {
				msg.setError("False");
				msg.setMessage("Trip Report Drop Suceesfully which is before "
						+ day + "-" + month + "-" + year);
				writer.write("\n Trip Report Drop Suceesfully which is before  "
						+ Droptime);
				writer.write(" \n------------------------------------------------------------------------------------");

			} else {
				msg.setError("True");
				msg.setMessage("Trip Report not drop suceesfully which is before "
						+ day
						+ "-"
						+ month
						+ "-"
						+ year
						+ "  Got an erorr---"
						+ cursor.getN());
				writer.write("\n Trip Report  not Drop Suceesfully which is before  "
						+ Droptime);
				writer.write(" \n------------------------------------------------------------------------------------");

			}

			writer.close();

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			msg.setError("True");
			msg.setMessage("Trip Report not drop suceesfully which is before "
					+ day + "-" + month + "-" + year);
			writer.write("\n Trip Report  not Drop Suceesfully which is before  "
					+ Droptime + "due to " + e.getMessage());
			writer.close();

		} finally {
		}

		return msg;
	}

	/*
	 * public MessageObject GeneratePendingTripReport(Connection con, DB
	 * mongoconnection) throws IOException {
	 * 
	 * ArrayList<DeviceInfoDto> UserList=new ArrayList<>(); int SpeedcCheck=0;
	 * ArrayList<DBObject> TempLoctionList=new ArrayList<>(); int
	 * TripLimeLimit=300; long SignalCheckcount=0; calendar =
	 * Calendar.getInstance(); year = calendar.get(Calendar.YEAR); month =
	 * calendar.get(Calendar.MONTH); day = calendar.get(Calendar.DAY_OF_MONTH);
	 * File GeneratePendingTripReport; FileWriter writer = null; calendar =
	 * Calendar.getInstance();
	 * 
	 * try { //Whatever the file path is. GeneratePendingTripReport = new
	 * File(Common.Log_path+"GeneratePendingTripReport.txt"); //Create the file
	 * if (GeneratePending.createNewFile()){
	 * //System.out.println("GeneratePendingTripReport File is created!");
	 * }else{
	 * //System.out.println("GeneratePendingTripReport File already exists."); }
	 * 
	 * //Write Content writer = new FileWriter(GeneratePendingTripReport,true);
	 * writer.write("\n  Generate Pending TripReprt  Log Started  at "+
	 * Calendar.getInstance().getTime());
	 * 
	 * 
	 * } catch (IOException e) { //System.err.println(
	 * "Problem writing to the file GeneratePendingTripReport.txt");
	 * }catch(Exception e){ e.printStackTrace(); }
	 * 
	 * 
	 * 
	 * try{ PreparedStatement ps=con.prepareStatement(
	 * "SELECT FirstName,LastName ,DeviceID FROM UserLoginMaster inner join StudentMaster on UserLoginMaster.Link_ID=StudentMaster.ParentID "
	 * +
	 * "where ( ACCReportAllow=0 and [TripReportAllow]=1) and (StudentMaster.DeviceType=3 OR StudentMaster.DeviceType=5 OR StudentMaster.DeviceType=6 OR StudentMaster.DeviceType=7)"
	 * );
	 * 
	 * ResultSet rs=ps.executeQuery(); if (rs!=null) { while (rs.next()) {
	 * DeviceInfoDto dto=new DeviceInfoDto();
	 * dto.setName(rs.getString(1)+rs.getString(2));
	 * dto.setDeviceID(rs.getString(3));
	 * 
	 * UserList.add(dto);
	 * 
	 * //
	 * //System.out.println("--------Report Req-----"+rs.getString(1)+rs.getString
	 * (2)); } } }catch(Exception e){
	 * 
	 * e.printStackTrace(); }finally{
	 * //System.out.println("--------Count Of  Req-----"+UserList.size()+"");
	 * 
	 * }
	 * 
	 * 
	 * try{ PreparedStatement ps1=con.prepareStatement(
	 * "select ActualData   from ApplicationMetaData where ApplicationMetaData.ID=5"
	 * ); ResultSet rs1=ps1.executeQuery(); if (rs1!=null) { while (rs1.next())
	 * { TripLimeLimit=Integer.parseInt(rs1.getString(1));
	 * 
	 * } } }catch(Exception e){
	 * 
	 * e.printStackTrace(); }
	 * 
	 * 
	 * for (int imain = 35; imain >0; imain--) {
	 * 
	 * long Starttime = Long.valueOf(Common.getGMTTimeStampFromDate(day-imain +
	 * "-" + String.valueOf(month + 1) + "-" + year + " 00:00 am")); long
	 * Endtime =Long.valueOf(Common .getGMTTimeStampFromDate(day - (imain-1) +
	 * "-" + String.valueOf(month + 1) + "-" + year + " 00:00 am"));
	 * //System.out.println("StartTime:" + Starttime + "  End Time:"+
	 * Endtime+"-------------------"+imain);
	 * 
	 * writer.write(
	 * "\n -----------------------Report Generating  for---------------------------"
	 * + "\n\n***********************************************"+"StartTime:" +
	 * Starttime + "  End Time:"+Endtime+
	 * "*****************************************************Day-------"
	 * +imain+"-----------");
	 * 
	 * 
	 * 
	 * try { for (int id = 0; id < UserList.size(); id++) {
	 * 
	 * int trip=0; DBCollection table = mongoconnection
	 * .getCollection(Common.TABLE_LOCATION); BasicDBObject device_whereQuery =
	 * new BasicDBObject(); device_whereQuery.put("device",
	 * Long.parseLong(UserList.get(id).getDeviceID())); BasicDBObject
	 * timestamp_whereQuery = new BasicDBObject( "timestamp", new
	 * BasicDBObject("$gte", Starttime).append("$lt", Endtime)); BasicDBList
	 * And_Milage = new BasicDBList(); And_Milage.add(timestamp_whereQuery);
	 * And_Milage.add(device_whereQuery); DBObject Total_Milage_query = new
	 * BasicDBObject("$and", And_Milage);
	 * 
	 * DBCursor cursor = table.find(Total_Milage_query);
	 * cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
	 * 
	 * //System.out.println("Cursor Count of u-p11----"+ cursor.size() + "  " +
	 * Total_Milage_query); long i = 0; long total = cursor.size(); int MaxSpeed
	 * = 0; int TotalSpeed = 0; int Triplocationcount = 0;
	 * 
	 * if (cursor.size() != 0) {
	 * 
	 * //System.err.println("*-----COunt---" + cursor.size());
	 * 
	 * while (cursor.hasNext()) {
	 * 
	 * if (i == 0) { dbObject1 = (DBObject) cursor.next();
	 * TempLoctionList.add(dbObject1); } else { SpeedcCheck =
	 * Integer.parseInt(dbObject2 .get("speed").toString());
	 * 
	 * } if (SpeedcCheck > MaxSpeed) MaxSpeed = SpeedcCheck; if (SpeedcCheck !=
	 * 0) Triplocationcount++;
	 * 
	 * TotalSpeed = TotalSpeed + SpeedcCheck;
	 * 
	 * if (TempLoctionList.size() == 0 && SpeedcCheck != 0) {
	 * TempLoctionList.add(dbObject2);
	 * 
	 * }
	 * 
	 * BasicDBObject locobj = (BasicDBObject) dbObject1 .get("location"); double
	 * lat1 = locobj.getDouble("lat"); double lng1 = locobj.getDouble("lon");
	 * 
	 * if (total >= 2) dbObject2 = (DBObject) cursor.next();
	 * 
	 * BasicDBObject locobj2 = (BasicDBObject) dbObject2 .get("location");
	 * double lat2 = locobj2.getDouble("lat"); double lng2 =
	 * locobj2.getDouble("lon");
	 * 
	 * DBObject source = dbObject1; DBObject destination = dbObject2;
	 * 
	 * dbObject1 = dbObject2;
	 * 
	 * if (lat1 != lat2 && lng1 != lng2 && TempLoctionList.size() > 0) { double
	 * caldist = distance(lat1, lng1, lat2, lng2, "K");
	 * 
	 * if (!Double.isNaN(caldist)) { HistoryDistance = HistoryDistance +
	 * caldist;
	 * 
	 * }
	 * 
	 * long src_time = Long.parseLong(source.get( "timestamp").toString()); long
	 * dest_time = Long.parseLong(destination .get("timestamp").toString());
	 * 
	 * if (SpeedcCheck == 0) { long diff = dest_time - src_time;
	 * 
	 * SignalCheckcount = SignalCheckcount + diff;
	 * ////System.err.println("-----SignalCheckcount---"+ SignalCheckcount);
	 * 
	 * }
	 * 
	 * if ((SpeedcCheck == 0 && SignalCheckcount > TripLimeLimit &&
	 * HistoryDistance > 1 && MaxSpeed != 0) || i == total - 2) { //
	 * //System.err.println("-----SignalCheckcount Got ---"+ SignalCheckcount);
	 * 
	 * BasicDBObject Maindocument = new BasicDBObject(); ObjectId objid = new
	 * ObjectId(); //System.out .println("*******************************");
	 * Maindocument.put("report_id", "" + objid); Maindocument.put("device",
	 * UserList .get(id).getDeviceID()); Maindocument.put("name",
	 * UserList.get(id).getName()); Maindocument.put("totalkm",
	 * HistoryDistance); Maindocument.put("maxspeed", MaxSpeed); if
	 * (Triplocationcount != 0) Maindocument .put("avgspeed", TotalSpeed /
	 * Triplocationcount); else Maindocument.put("avgspeed", 0); DBObject
	 * srcdbobj = TempLoctionList .get(0);
	 * 
	 * BasicDBObject locallocobj = (BasicDBObject) srcdbobj .get("location");
	 * double locallat1 = locallocobj .getDouble("lat"); double locallng1 =
	 * locallocobj .getDouble("lon");
	 * 
	 * BasicDBObject Intial_Loc = new BasicDBObject(); Intial_Loc.put("lat",
	 * locallat1); Intial_Loc.put("lon", locallng1); Intial_Loc.put("speed",
	 * srcdbobj.get("speed")); Intial_Loc.put("timestamp",
	 * srcdbobj.get("timestamp"));
	 * 
	 * Maindocument.put("source_info", Intial_Loc);
	 * 
	 * BasicDBObject Dest_Loc = new BasicDBObject(); Dest_Loc.put("lat", lat2);
	 * Dest_Loc.put("lon", lng2); Dest_Loc.put("speed",
	 * destination.get("speed")); Dest_Loc.put("timestamp",
	 * destination.get("timestamp"));
	 * 
	 * Maindocument.put("dest_info", Dest_Loc);
	 * 
	 * DBCollection Triptable = mongoconnection
	 * .getCollection(Common.TABLE_TRIPREPORT);
	 * 
	 * Triptable.insert(Maindocument); HistoryDistance = 0.0;
	 * TempLoctionList.clear(); SignalCheckcount = 0; MaxSpeed = 0; TotalSpeed =
	 * 0; Triplocationcount = 0; trip++; }
	 * 
	 * }
	 * 
	 * i++;
	 * 
	 * }
	 * 
	 * }
	 * 
	 * if (i == total - 2) {
	 * 
	 * String dist = HistoryDistance + " KM";
	 * 
	 * if (HistoryDistance > 1000.0f) { float distance = (float)
	 * (HistoryDistance / 1000.0f); dist = distance + " KM"; }
	 * //System.out.println("DIstance Calulated by===+i+==" + HistoryDistance);
	 * 
	 * }
	 * 
	 * writer.write("\n -----------------------Trip Generate coount is == "+trip+
	 * "  for  "
	 * +UserList.get(id).getName()+" , Device Id : "+UserList.get(id).getDeviceID
	 * ());
	 * 
	 * writer.close(); }//for end } catch (NumberFormatException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch (MongoException
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); } finally {
	 * 
	 * }
	 * 
	 * }
	 * 
	 * 
	 * 
	 * return null;
	 * 
	 * 
	 * }
	 */
	public MessageObject GeneratePendingTripReport(Connection con,
			DB mongoconnection, String startdate, String enddate)
			throws IOException {
		ArrayList<DeviceInfoDto> UserList = new ArrayList<>();
		int SpeedcCheck = 0;
		ArrayList<DBObject> TempLoctionList = new ArrayList<>();
		int TripTimeLimit = 300;
		int trip = 0;
		MessageObject msgObj = new MessageObject();

		long SignalCheckcount = 0;
		File GeneratePendingTripReport;
		FileWriter writer = null;
		calendar = Calendar.getInstance();

		BufferedWriter bfwriter = null;
		calendar = Calendar.getInstance();
		ArrayList<Double> DeaciveListDevice = new ArrayList<Double>();

		try {
			// Whatever the file path is.
			GeneratePendingTripReport = new File(Common.Log_path
					+ "GenerateTripReport_log" + calendar.getTime() + ".txt");
			// Create the file
			if (GeneratePendingTripReport.createNewFile()) {
				// System.out.println("GeneratePendingTripReport File is created!");
			} else {
				// System.out.println("GeneratePendingTripReport File already exists.");
			}

			// Write Content
			writer = new FileWriter(GeneratePendingTripReport);

			bfwriter = new BufferedWriter(writer);
			bfwriter.write("\n  Generate Pending TripReprt  Log Started  at "
					+ Calendar.getInstance().getTime());

		} catch (IOException e) {
			// System.err.println("Problem writing to the file statsTest.txt");
		} catch (Exception e) {

			e.printStackTrace();
		}

		try {
			java.sql.CallableStatement psc = con
					.prepareCall("{call GetDeviceListForTripReport()}");
			ResultSet rs = psc.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					DeviceInfoDto dto = new DeviceInfoDto();

					dto.setName(rs.getString(1) + rs.getString(2));
					dto.setDeviceID(rs.getString(3));

					UserList.add(dto);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			// System.err.println("Problem writing to the file GeneratePendingTripReport.txt");

		}

		try {
			PreparedStatement ps1 = con
					.prepareStatement("select ActualData   from ApplicationMetaData where ApplicationMetaData.ID=5");
			ResultSet rs1 = ps1.executeQuery();
			if (rs1 != null) {
				while (rs1.next()) {
					TripTimeLimit = Integer.parseInt(rs1.getString(1));

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			// System.err.println("Problem writing to the file GeneratePendingTripReport.txt");

		}

		/*
		 * calendar = Calendar.getInstance(); year =
		 * calendar.get(Calendar.YEAR);
		 * 
		 * month = calendar.get(Calendar.MONTH); day =
		 * calendar.get(Calendar.DAY_OF_MONTH); long Starttime =
		 * Long.valueOf(Common.getGMTTimeStampFromDate(day-1+ "-"+
		 * String.valueOf(month+1) + "-" + year+" 00:00 am")); long Endtime =
		 * Long.valueOf(Common.getGMTTimeStampFromDate(day+ "-"+
		 * String.valueOf(month+1) + "-" + year+" 00:00 am"));
		 * 
		 * 
		 * logger.info(
		 * "-----------------------Report Generating  for---------------------------"
		 * + "\n\n***********************************************"+"StartTime:"
		 * + Starttime + "  End Time:"+Endtime+
		 * "*****************************************************Day-------"
		 * +"-----------");
		 * 
		 * 
		 * 
		 * //System.out.println("StartTime:"+Starttime+"  End Time:"+Endtime);
		 */

		try {
			bfwriter.write("\n-----------------------Report Generating  for---------------------------"
					+ "\n\n******************"
					+ "StartTime: "
					+ startdate
					+ "  End Time: " + enddate + "********");

			for (int id = 0; id < UserList.size(); id++) {

				trip = 0;
				HistoryDistance = 0;

				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_LOCATION);
				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device",
						Long.parseLong(UserList.get(id).getDeviceID()));
				// device_whereQuery.put("device",Long.parseLong("355488020181042"));

				BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp",
						new BasicDBObject("$gte", startdate).append("$lt",
								enddate));
				BasicDBList And_Milage = new BasicDBList();
				And_Milage.add(timestamp_whereQuery);
				And_Milage.add(device_whereQuery);
				DBObject Total_Milage_query = new BasicDBObject("$and",
						And_Milage);

				DBCursor cursor = table.find(Total_Milage_query);
				cursor.sort(new BasicDBObject("timestamp", 1));

				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				// System.out.println("Cursor Count of u-p11----"+cursor.size()+"  "+Total_Milage_query);
				long i = 0;
				long total = cursor.size();
				Double MaxSpeed = 0.0;
				Double TotalSpeed = 0.0;
				int Triplocationcount = 0;

				if (cursor.size() > 0) {
					TempLoctionList.clear();

					// System.err.println("*-----COunt---"+cursor.size());

					List<DBObject> listObjects = cursor.toArray();
					trip = Calculate_Trip((ArrayList<DBObject>) listObjects,
							mongoconnection, TripTimeLimit, UserList.get(id)
									.getDeviceID(), UserList.get(id).getName());

				}

				bfwriter.write("\n------Trip Generate count is == " + trip
						+ "  for  " + UserList.get(id).getName()
						+ " , Device Id : " + UserList.get(id).getDeviceID());

				// bfwriter.write("\n------Trip Generate count is == "+trip);

			}// for end
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {

				if (bfwriter != null)
					bfwriter.close();

				if (writer != null)
					writer.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}

		return null;
	}

	public MessageObject NotifyToUserAboutExpiryDate(Connection con) {
		// TODO Auto-generated method stub
		SendEmail se = new SendEmail();

		MessageObject msgObj = new MessageObject();
		// sentSms("8796456634","HiranjitfromRupesh","4");
		// Boolean
		// sent=se.SendNotifyAboutExpiryDateEmail("rupesh.p@mykiddytracker.com","Hi rupesh","4");

		try {
			ResultSet rs = null;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call GetPaymentNofityUsers()}");

			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {

				// EmailID,Name,UserID,Role_id,UserName,StudentID ,DiffDate
				// ,MobileNo

				int datediff = rs.getInt("DiffDate");
				// String
				// msg="Hi "+rs.getString("Name")+" !"+"Thanks for choosing  MyKiddyTracker App.Your subcription will expire in "+datediff
				// +" days,Please recharge your device.";
				String msg = "Dear "
						+ rs.getString("Name")
						+ " !"
						+ " Thanks for choosing  MyKiddyTracker app. Your subcription will expire in "
						+ datediff
						+ " days,Please recharge your device using this link www.mykiddytracker.com/payment";

				if (datediff == 1 || datediff == 2 || datediff == 3
						|| datediff == 5 || datediff == 10 || datediff == 15) {

					sentSms(rs.getString("MobileNo"), msg.replace(" ", "%20"),
							rs.getString("DiffDate"));

					Boolean sent = se.SendNotifyAboutExpiryDateEmail(
							rs.getString("EmailID"), rs.getString("Name"),
							rs.getString("DiffDate"));

				}

				msgObj.setError("false");
				msgObj.setMessage("Sucessfull");
			} else {
				msgObj.setError("true");
				msgObj.setMessage("Invalid user");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return msgObj;
	}

	private void sentSms(String monno, String msg, String dtaediff) {
		try {
			// System.err.println("inside  sentSms--------------------");
			final String USER_AGENT = "Mozilla/5.0";

			// Construct data
			String data = "http://sms.hspsms.com/sendSMS?username=primesystech&message="
					+ msg
					+ "&sendername=MKTSMS&smstype=TRANS&numbers="
					+ monno
					+ "&apikey=" + Common.HSPSMS_API_KEY;
			// System.err.println("inside  sentSms--------------------"+data);

			URL obj = new URL(data);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			// System.out.println("\nSending 'GET' request to URL : " + data);
			// System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			// System.out.println(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<TripInfoDto> GetDirectTripReport(Connection con,
			DB mongoconnection, String device, String startdate, String enddate) {

		ArrayList<TripInfoDto> Triplist = new ArrayList<TripInfoDto>();
		MessageObject msg = new MessageObject();
		String offset_otDevice = "GMT+05:30";
		TimeOffsetDTO obj = new TimeOffsetDTO();
		String empname = "N/A";
		DBCursor cursor = null;

		try {
			java.sql.CallableStatement ps = con
					.prepareCall("{call GetOffsetOfdevice(?)}");
			ps.setString(1, device);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					obj.setOffsetId("" + rs.getString("Id"));
					obj.setOffsetActual("" + rs.getString("OffsetActual"));
					obj.setOffsetUponIndia("" + rs.getString("OffsetUponIndia"));
					obj.setCountyName("" + rs.getString("CountyName"));
					obj.setFlag("" + rs.getString("flag"));

					offset_otDevice = obj.getOffsetActual();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (obj.getFlag() != null && obj.getFlag().equalsIgnoreCase("15")) {

			try {
				java.sql.CallableStatement ps = con
						.prepareCall("{call GetDriverUponTaskOnthisDay(?,?,?)}");
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(Long.parseLong(startdate));
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);

				ps.setInt(1, year);
				ps.setInt(2, month + 1);
				ps.setInt(3, day);

				ResultSet rs = ps.executeQuery();
				int i = 0;

				if (rs != null) {
					while (rs.next()) {
						if (i == 0) {
							empname = "" + rs.getString("Name");
						} else {
							empname.concat("/" + rs.getString("Name"));
						}

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

		}

		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_TRIPREPORT);
			// System.out.println("device==============----------"+device+" "+table.getFullName());

			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device", device);

			BasicDBObject timestamp_whereQuery = new BasicDBObject(
					"source_info.timestamp", new BasicDBObject("$gte",
							Long.parseLong(startdate)).append("$lt",
							Long.parseLong(enddate)));

			BasicDBList And_Milage = new BasicDBList();
			And_Milage.add(timestamp_whereQuery);
			And_Milage.add(device_whereQuery);
			DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

			cursor = table.find(Total_Milage_query).sort(
					new BasicDBObject("source_info.timestamp", 1));
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			// System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+Total_Milage_query);
			long i = 0;
			long total = cursor.count();

			if (cursor.size() != 0) {

				// System.err.println("*-GetDirectTripReport----COunt---"+cursor.size());

				while (cursor.hasNext()) {

					TripInfoDto trip = new TripInfoDto();
					dbObject1 = (DBObject) cursor.next();

					BasicDBObject Sourceobj = (BasicDBObject) dbObject1
							.get("source_info");
					BasicDBObject Destobj = (BasicDBObject) dbObject1
							.get("dest_info");

					trip.setDevice(device);
					trip.setSrclat(Sourceobj.getString("lat"));
					trip.setSrclon(Sourceobj.getString("lon"));
					trip.setSrcspeed(Sourceobj.getString("speed"));

					trip.setSrc_adress(Sourceobj.getString("src_address"));

					trip.setSrctimestamp(Sourceobj.getString("timestamp"));

					trip.setDestlat(Destobj.getString("lat"));
					trip.setDestlon(Destobj.getString("lon"));
					trip.setDestspeed(Destobj.getString("speed"));
					trip.setDest_address(Destobj.getString("dest_address"));

					trip.setDesttimestamp(Destobj.getString("timestamp"));

					trip.setReport_id(dbObject1.get("report_id").toString());
					trip.setTotalkm(dbObject1.get("totalkm").toString());
					trip.setMaxspeed(dbObject1.get("maxspeed").toString());
					trip.setAvgspeed(String.format("%.2f",
							dbObject1.get("avgspeed")));
					trip.setEmp_driver_name(empname);
					trip.setDevicename(dbObject1.get("name").toString());
					// trip.setDevicename("vts");

					Triplist.add(trip);

				}

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();

			}
		}

		return Triplist;

	}

	public String GetDirectWebTripReport(Connection con, DB mongoconnection,
			String device, String startdate, String enddate) {

		String offset_otDevice = "GMT+05:30";
		TimeOffsetDTO obj = new TimeOffsetDTO();
		String empname = "N/A";

		try {
			java.sql.CallableStatement ps = con
					.prepareCall("{call GetOffsetOfdevice(?)}");
			ps.setString(1, device);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					obj.setOffsetId("" + rs.getString("Id"));
					obj.setOffsetActual("" + rs.getString("OffsetActual"));
					obj.setOffsetUponIndia("" + rs.getString("OffsetUponIndia"));
					obj.setCountyName("" + rs.getString("CountyName"));
					obj.setFlag("" + rs.getString("flag"));

					offset_otDevice = obj.getOffsetActual();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (obj.getFlag() != null && obj.getFlag().equalsIgnoreCase("15")) {

			try {
				java.sql.CallableStatement ps = con
						.prepareCall("{call GetDriverUponTaskOnthisDay(?,?,?,?)}");
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(Long.parseLong(startdate));
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);

				ps.setInt(1, year);
				ps.setInt(2, month + 1);
				ps.setInt(3, day);
				ps.setString(4, device);

				ResultSet rs = ps.executeQuery();
				int i = 0;

				if (rs != null) {
					while (rs.next()) {
						if (i == 0) {
							empname = "" + rs.getString("Name");
						} else {
							empname.concat("/" + rs.getString("Name"));
						}

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

		}

		ArrayList<TripInfoDto> Triplist = new ArrayList<TripInfoDto>();
		String msg = "";
		DBCollection table = mongoconnection
				.getCollection(Common.TABLE_TRIPREPORT);
		System.out.println("device==============----------" + device + " "
				+ table.getFullName());

		BasicDBObject device_whereQuery = new BasicDBObject();
		device_whereQuery.put("device", device);

		BasicDBObject timestamp_whereQuery = new BasicDBObject(
				"source_info.timestamp", new BasicDBObject("$gte",
						Long.parseLong(startdate)).append("$lt",
						Long.parseLong(enddate)));

		BasicDBList And_Milage = new BasicDBList();
		And_Milage.add(timestamp_whereQuery);
		And_Milage.add(device_whereQuery);
		DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

		DBCursor cursor = table.find(Total_Milage_query).sort(
				new BasicDBObject("source_info.timestamp", 1));
		cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

		System.out.print("Cursor Count of u-p---" + cursor.size() + "  "
				+ Total_Milage_query);
		long i = 0;
		long total = cursor.count();

		if (cursor.size() != 0) {

			System.err.println("*-----COunt---" + cursor.size());

			while (cursor.hasNext()) {

				TripInfoDto trip = new TripInfoDto();
				dbObject1 = (DBObject) cursor.next();

				BasicDBObject Sourceobj = (BasicDBObject) dbObject1
						.get("source_info");
				BasicDBObject Destobj = (BasicDBObject) dbObject1
						.get("dest_info");

				trip.setDevice(device);
				trip.setSrclat(Sourceobj.getString("lat"));
				trip.setSrclon(Sourceobj.getString("lon"));
				trip.setSrcspeed(Sourceobj.getString("speed"));

				trip.setSrc_adress(Sourceobj.getString("src_address"));

				trip.setSrctimestamp(Sourceobj.getString("timestamp"));

				trip.setDestlat(Destobj.getString("lat"));
				trip.setDestlon(Destobj.getString("lon"));
				trip.setDestspeed(Destobj.getString("speed"));
				trip.setDest_address(Destobj.getString("dest_address"));

				trip.setDesttimestamp(Destobj.getString("timestamp"));

				trip.setReport_id(dbObject1.get("report_id").toString());
				trip.setTotalkm(dbObject1.get("totalkm").toString());
				trip.setMaxspeed(dbObject1.get("maxspeed").toString());
				trip.setAvgspeed(String.format("%.2f",
						dbObject1.get("avgspeed")));

				trip.setDevicename(dbObject1.get("name").toString());
				// trip.setDevicename("vts");

				Triplist.add(trip);

			}

			System.err.println("*-----TriplisTriplistTriplistt---"
					+ Triplist.size());

			if (Triplist.size() > 0) {
				SendEmail se = new SendEmail();
				if (obj.getFlag() != null) {
					msg = se.SendWebTripReportEmail(Triplist, offset_otDevice,
							obj.getFlag(), empname);

				} else {
					msg = se.SendWebTripReportEmail(Triplist, offset_otDevice,
							"0", empname);

				}
			}

		}

		return msg;

	}

	void SendNotificationToUser(String message, String key) {

	}

	public ArrayList<FamilyPaymentTypeDTO> GetFamilyPaymentType(Connection con) {

		ArrayList<FamilyPaymentTypeDTO> p = new ArrayList<FamilyPaymentTypeDTO>();
		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetFamilyPaymentType()}");

			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					FamilyPaymentTypeDTO person = new FamilyPaymentTypeDTO();
					person.setPaymentTypeId("" + rs.getString("ID"));
					person.setPaymentTypeName(""
							+ rs.getString("PaymentTypeName"));
					person.setAmountOfPlan("" + rs.getString("AmountOfPlan"));
					person.setDaysOfplan("" + rs.getString("DaysOfplan"));
					person.setMultiplicationFactor(""
							+ rs.getString("MultiplicationFactor"));

					p.add(person);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("S"+p.toString());
		return p;

	}

	public MessageObject UpdateFamilyPayment(Connection con, String invitedIds,
			String userId, String typeofpayment, String paymentId) {
		MessageObject msg = new MessageObject();

		try {

			int result = 0;
			java.sql.CallableStatement ps = con
					.prepareCall("{call UpdateFamilyPayment(?,?,?,?)}");
			ps.setString(1, invitedIds);
			ps.setInt(2, Integer.parseInt(userId));
			ps.setInt(3, Integer.parseInt(typeofpayment));
			ps.setInt(4, Integer.parseInt(paymentId));

			result = ps.executeUpdate();

			if (result == 0) {
				msg.setError("true");
				msg.setMessage("Payment is not made successfully");
			} else {
				// //System.err.println("Error=="+result);
				msg.setError("false");
				msg.setMessage("Payment is made successfully");
			}
		} catch (Exception e) {
			msg.setError("true");
			msg.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}
		return msg;

	}

	public ApplicationMetaData GetApplicationMetaData(Connection con) {
		ApplicationMetaData obj = new ApplicationMetaData();
		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetApplicationMetaData()}");

			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					obj.setGOOGLE_PROJECT_ID(rs.getString("GOOGLE_PROJECT_ID"));
					obj.setMapKey(rs.getString("MapKey"));
					obj.setMESSAGE_KEY(rs.getString("MESSAGE_KEY"));
					obj.setPaymentURL(rs.getString("PaymentURL"));
					obj.setPayU_furl(rs.getString("PayU_furl"));
					obj.setPayU_surl(rs.getString("PayU_surl"));
					obj.setPayUkey(rs.getString("PayUkey"));
					obj.setPayUsalt(rs.getString("PayUsalt"));
					obj.setPayUtxnid(rs.getString("PayUtxnid"));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return obj;
	}

	public MessageObject GetTripReportOnMail(Connection con,
			DB mongoconnection, String device, String startdate,
			String enddate, String email) {

		String offset_otDevice = "GMT+05:30";
		TimeOffsetDTO obj = new TimeOffsetDTO();
		String empname = "N/A";

		try {
			java.sql.CallableStatement ps = con
					.prepareCall("{call GetOffsetOfdevice(?)}");
			ps.setString(1, device);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					obj.setOffsetId("" + rs.getString("Id"));
					obj.setOffsetActual("" + rs.getString("OffsetActual"));
					obj.setOffsetUponIndia("" + rs.getString("OffsetUponIndia"));
					obj.setCountyName("" + rs.getString("CountyName"));
					obj.setFlag("" + rs.getString("flag"));

					offset_otDevice = obj.getOffsetActual();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (obj.getFlag().equalsIgnoreCase("15")) {

			try {
				java.sql.CallableStatement ps = con
						.prepareCall("{call GetDriverUponTaskOnthisDay(?,?,?)}");
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(Long.parseLong(startdate));
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);

				ps.setInt(1, year);
				ps.setInt(2, month + 1);
				ps.setInt(3, day);

				ResultSet rs = ps.executeQuery();
				int i = 0;

				if (rs != null) {
					while (rs.next()) {
						if (i == 0) {
							empname = "" + rs.getString("Name");
						} else {
							empname.concat("/" + rs.getString("Name"));
						}

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

		}
		ArrayList<TripInfoDto> Triplist = new ArrayList<TripInfoDto>();
		MessageObject msg = new MessageObject();
		DBCollection table = mongoconnection
				.getCollection(Common.TABLE_TRIPREPORT);
		// System.out.println("device==============----------"+device+" "+table.getFullName());

		BasicDBObject device_whereQuery = new BasicDBObject();
		device_whereQuery.put("device", device);

		BasicDBObject timestamp_whereQuery = new BasicDBObject(
				"source_info.timestamp", new BasicDBObject("$gte",
						Long.parseLong(startdate)).append("$lt",
						Long.parseLong(enddate)));

		BasicDBList And_Milage = new BasicDBList();
		And_Milage.add(timestamp_whereQuery);
		And_Milage.add(device_whereQuery);
		DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

		DBCursor cursor = table.find(Total_Milage_query).sort(
				new BasicDBObject("source_info.timestamp", 1));
		cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

		// System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+Total_Milage_query);
		long i = 0;
		long total = cursor.count();

		if (cursor.size() != 0) {

			// System.err.println("*-----COunt---"+cursor.size());

			while (cursor.hasNext()) {

				TripInfoDto trip = new TripInfoDto();
				dbObject1 = (DBObject) cursor.next();

				BasicDBObject Sourceobj = (BasicDBObject) dbObject1
						.get("source_info");
				BasicDBObject Destobj = (BasicDBObject) dbObject1
						.get("dest_info");

				trip.setDevice(device);
				trip.setSrclat(Sourceobj.getString("lat"));
				trip.setSrclon(Sourceobj.getString("lon"));
				trip.setSrcspeed(Sourceobj.getString("speed"));

				trip.setSrc_adress(Sourceobj.getString("src_address"));

				trip.setSrctimestamp(Sourceobj.getString("timestamp"));

				trip.setDestlat(Destobj.getString("lat"));
				trip.setDestlon(Destobj.getString("lon"));
				trip.setDestspeed(Destobj.getString("speed"));
				trip.setDest_address(Destobj.getString("dest_address"));

				trip.setDesttimestamp(Destobj.getString("timestamp"));

				trip.setReport_id(dbObject1.get("report_id").toString());
				trip.setTotalkm(dbObject1.get("totalkm").toString());
				trip.setMaxspeed(dbObject1.get("maxspeed").toString());
				trip.setAvgspeed(String.format("%.2f",
						dbObject1.get("avgspeed")));

				trip.setDevicename(dbObject1.get("name").toString());
				// trip.setDevicename("vts");

				Triplist.add(trip);

			}

		}
		SendEmail se = new SendEmail();
		Boolean sent = se.SendTripReportEmailAttachment(Triplist, email,
				offset_otDevice, obj.getFlag(), empname);

		if (sent) {
			msg.setError("false");
			msg.setMessage("Trip report sent successfully.");
		} else {
			msg.setError("true");
			msg.setMessage("Error in sending Trip Report.");
		}

		// System.err.println("Get Triipo Report=="+msg.getMessage());

		return msg;

	}

	public MessageObject GetPaytm_ChecksumGeneration(String orderId,
			String custId, String amount, String mobno, String email) {
		// TODO Auto-generated method stub
		MessageObject msg = new MessageObject();

		TreeMap<String, String> paramMap = new TreeMap<String, String>();

		paramMap.put("MID", Common.MID);
		paramMap.put("ORDER_ID", orderId);
		paramMap.put("CUST_ID", custId);
		paramMap.put("INDUSTRY_TYPE_ID", Common.INDUSTRY_TYPE_ID);
		paramMap.put("CHANNEL_ID", Common.CHANNLE_ID);
		paramMap.put("TXN_AMOUNT", amount);
		paramMap.put("WEBSITE", Common.WEBSITE);
		paramMap.put("EMAIL", email);
		paramMap.put("MOBILE_NO", mobno);
		paramMap.put("CALLBACK_URL", Common.CALLBACK_URL + orderId);
		try {
			// String checkSum =
			// CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(Common.MercahntKey,
			// paramMap);
			String checkSum = CheckSumServiceHelper.getCheckSumServiceHelper()
					.genrateCheckSum(Common.MercahntKey, paramMap);
			paramMap.put("CHECKSUMHASH", checkSum);

			 System.out.println("Paytm Payload: "+ paramMap);

			msg.setError("false");
			msg.setMessage(checkSum);
			msg.setName("CheckSum for Paytm from Server");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			msg.setError("true");
			msg.setName("CheckSum for Paytm from Server");
		}
		return msg;
	}

	public ArrayList<VehicalTrackingSMSCmdDTO> GetMyKiddySMSItemlist(
			Connection con, String studId) {
		// TODO Auto-generated method stub

		ArrayList<VehicalTrackingSMSCmdDTO> smslist = new ArrayList<VehicalTrackingSMSCmdDTO>();
		try {
			ResultSet rs = null;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call GetMyKiddySMSItemlist()}");

			/*
			 * stmt.setString(1,studentid); stmt.setString(2,command);
			 */

			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					VehicalTrackingSMSCmdDTO msgObj = new VehicalTrackingSMSCmdDTO();

					msgObj.setCommnadType(rs.getString("CommnadType") + "");

					msgObj.setActualCommand(rs.getString("ActualCommand") + "");
					msgObj.setTitle(rs.getString("Discription"));
					msgObj.setAnsFromDevice(rs.getString("AnsFromDevice"));

					smslist.add(msgObj);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return smslist;

	}

	public ArrayList<HistoryDTO> GetHistorydata(DB mongoconnection,
			String student_id, String startdate) {
		ArrayList<HistoryDTO> dtp = new ArrayList<HistoryDTO>();

		String DeviceIMIE_No = student_id;
		String selectedDate = Common.getDateFromLong_in_dd_mm_yyyy(Long
				.parseLong(startdate));
		long Endtime = Long.valueOf(Common.getGMTTimeStampFromDate(selectedDate
				+ " 11:59 pm"));

		System.out.println("GetHistorydata----" + startdate + "  " + Endtime);

		/*
		 * try{
		 * 
		 * DBCollection table =
		 * mongoconnection.getCollection(Common.TABLE_DEVICE); BasicDBObject
		 * device_whereQuery = new BasicDBObject("student",
		 * Long.parseLong(student_id));
		 * 
		 * DBCursor cursor = table.find(device_whereQuery);
		 * cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
		 * 
		 * //System.out.println("Device Where Cursor Count of u-----"+cursor.count
		 * ()+"  "+device_whereQuery);
		 * 
		 * 
		 * if(cursor.size()!=0) { NumberFormat myformatter = new
		 * DecimalFormat("###############");
		 * //System.err.println("*-----COunt---"
		 * +cursor.size()+" --------start--"+startdate+"---------");
		 * 
		 * DBObject dbObject1 = (DBObject) cursor.next();
		 * DeviceIMIE_No=dbObject1
		 * .get("device").toString();//myformatter.format(+"");
		 * 
		 * }
		 * 
		 * }catch(Exception e){
		 * 
		 * e.printStackTrace(); }finally{
		 * 
		 * }
		 */

		try {
			if (DeviceIMIE_No.length() > 0) {
				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_LOCATION);
				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device", Long.parseLong(DeviceIMIE_No));

				BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp", new BasicDBObject("$gte",
								Long.parseLong(startdate)).append("$lt",
								Endtime));

				BasicDBList And_Milage = new BasicDBList();
				And_Milage.add(timestamp_whereQuery);
				And_Milage.add(device_whereQuery);
				DBObject Total_Milage_query = new BasicDBObject("$and",
						And_Milage);

				DBCursor cursor = table.find(Total_Milage_query)
						.sort(new BasicDBObject("timestamp", 1)).limit(200);
				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				System.out.println("Cursor Count of u-p11----" + cursor.size()
						+ "  " + Total_Milage_query);

				if (cursor.size() != 0) {

					for (DBObject dbObject : cursor) {

						HistoryDTO obj = new HistoryDTO();
						DBObject dbObject_location = (DBObject) dbObject
								.get("location");

						obj.setLan(dbObject_location.get("lon").toString());
						obj.setLat(dbObject_location.get("lat").toString());

						BasicDBList status = (BasicDBList) dbObject
								.get("status");
						/*
						 * obj.setLan_direction(status.get(3).get("lon_direction"
						 * ).toString());
						 * obj.setLat_direction(status.get(4).get(
						 * "lat_direction").toString());
						 */
						obj.setLan_direction((status.get(2) + "").replace(
								"{ \"lon_direction\" : \"", ""));

						obj.setLan_direction(obj.getLan_direction().replace(
								"\"}", ""));

						obj.setLat_direction((status.get(3) + "").replace(
								"{ \"lat_direction\" : \"", ""));
						obj.setLat_direction(obj.getLat_direction().replace(
								"\"}", ""));

						obj.setSpeed(dbObject.get("speed").toString());
						obj.setTimestamp(dbObject.get("timestamp").toString());
						dtp.add(obj);
					}
				} else {

				}

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

	public MessageObject GenerateEmpTripReport(Connection con,
			DB mongoconnection) {
		// TODO Auto-generated method stub
		String enddate = null, startdate = null;
		ArrayList<DeviceInfoDto> UserList = new ArrayList<>();
		int SpeedcCheck = 0;
		ArrayList<DBObject> TempLoctionList = new ArrayList<>();
		int TripTimeLimit = 300;
		int trip = 0;
		MessageObject msgObj = new MessageObject();

		long SignalCheckcount = 0;
		File GenerateEmpTripReport;
		FileWriter writer = null;
		Calendar calendar = Calendar.getInstance();

		BufferedWriter bfwriter = null;
		calendar = Calendar.getInstance();
		ArrayList<Double> DeaciveListDevice = new ArrayList<Double>();

		try {
			// Whatever the file path is.
			GenerateEmpTripReport = new File(Common.Log_path
					+ "GenerateEmpTripReport_log" + calendar.getTime() + ".txt");
			// Create the file
			if (GenerateEmpTripReport.createNewFile()) {
				// System.out.println("File is created!");
			} else {
				// System.out.println("File already exists.");
			}

			// Write Content
			writer = new FileWriter(GenerateEmpTripReport);

			bfwriter = new BufferedWriter(writer);
			bfwriter.write("\nStart  Generate EmpTripReport  TripReprt Log----at "
					+ calendar.getTime());

		} catch (IOException e) {
			// System.err.println("Problem writing to the file statsTest.txt");
		} catch (Exception e) {

			e.printStackTrace();
		}

		try {
			// PreparedStatement
			// ps=con.prepareStatement("select FirstName,LastName ,DeviceID from StudentMaster where DeviceID=?");
			PreparedStatement ps = con
					.prepareStatement("select FirstName,LastName ,DeviceID from StudentMaster inner join UserLoginMaster on StudentID=EmployeeID where Role_ID=14");
			// ps.setString(1, device);
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					DeviceInfoDto dto = new DeviceInfoDto();
					dto.setName(rs.getString(1) + rs.getString(2));
					dto.setDeviceID(rs.getString(3));

					UserList.add(dto);

					// System.out.println("--------Report Req-----"+rs.getString(1)+rs.getString(2));
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			msgObj.setError("True");
			msgObj.setMessage("Getting error in Generate Device Pending TripReport due to --"
					+ e.getMessage());
		}

		try {
			PreparedStatement ps1 = con
					.prepareStatement("select ActualData   from ApplicationMetaData where ApplicationMetaData.ID=5");
			ResultSet rs1 = ps1.executeQuery();
			if (rs1 != null) {
				while (rs1.next()) {
					TripTimeLimit = Integer.parseInt(rs1.getString(1));

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			msgObj.setError("True");
			msgObj.setMessage("Getting error in Generate Device Emp TripReport due to --"
					+ e.getMessage());
		}

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long Starttime = Long.valueOf(Common.getGMTTimeStampFromDate(day - 1
				+ "-" + String.valueOf(month + 1) + "-" + year + " 00:00 am"));
		long Endtime = Long.valueOf(Common.getGMTTimeStampFromDate(day + "-"
				+ String.valueOf(month + 1) + "-" + year + " 00:00 am"));

		// System.err.println("StartTime: " + startdate +
		// "  End Time: "+enddate+"********");
		try {
			if (UserList.size() > 0) {

				bfwriter.write("\n-----------------------Report Generating  for---------------------------"
						+ "\n\n******************"
						+ "StartTime: "
						+ startdate
						+ "  End Time: " + enddate + "********");

				for (int id = 0; id < UserList.size(); id++) {

					SimpleDateFormat monthFormat = new SimpleDateFormat(
							"yyyy-MMM-dd");// Edit here depending on your
											// requirements

					String cur_date = monthFormat.format(new Date());
					String[] strmonth = cur_date.split("-");
					// System.err.println("cur_date: ------" +
					// strmonth[0]+"-"+strmonth[1]+"*"+strmonth[2] );

					ResultSet rs = null;
					java.sql.CallableStatement stmt = con
							.prepareCall("{call GetEmpDayTime(?,?,?,?)}");

					stmt.setString(1, UserList.get(id).getDeviceID());
					stmt.setInt(2, Integer.parseInt(strmonth[2]) - 1);
					stmt.setString(3, strmonth[1]);
					stmt.setInt(4, Integer.parseInt(strmonth[0]));

					rs = stmt.executeQuery();
					if (rs != null) {
						while (rs.next()) {
							startdate = rs.getString("StartTime") + "";
							enddate = rs.getString("EndTime") + "";

							if (startdate != null
									&& !startdate.equalsIgnoreCase("NULL")
									&& enddate != null
									&& !enddate.equalsIgnoreCase("NULL")) {
								SimpleDateFormat dateFormat = new SimpleDateFormat(
										"yyyy-MM-dd hh:mm:ss");
								startdate = (dateFormat.parse(startdate)
										.getTime() + "").substring(0, 10);
								enddate = (dateFormat.parse(enddate).getTime() + "")
										.substring(0, 10);
								// System.err.println("Coonvert---StartTime: " +
								// startdate +
								// "  End Time: "+enddate+"********");

							}
						}
					}

					if (startdate != null
							&& !startdate.equalsIgnoreCase("NULL")
							&& enddate != null
							&& !enddate.equalsIgnoreCase("NULL")) {

						trip = 0;
						HistoryDistance = 0;

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device",
								Long.parseLong(UserList.get(id).getDeviceID()));
						// device_whereQuery.put("device",Long.parseLong("355488020181042"));

						BasicDBObject timestamp_whereQuery = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										Long.parseLong(startdate)).append(
										"$lt", Long.parseLong(enddate)));
						BasicDBList And_Milage = new BasicDBList();
						And_Milage.add(timestamp_whereQuery);
						And_Milage.add(device_whereQuery);
						DBObject Total_Milage_query = new BasicDBObject("$and",
								And_Milage);

						DBCursor cursor = table.find(Total_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						// System.out.println("Cursor Count of u-p11----"+cursor.size()+"  "+Total_Milage_query);
						long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;

						if (cursor.size() > 0) {
							TempLoctionList.clear();

							// System.err.println("*-----COunt---"+cursor.size());

							List<DBObject> listObjects = cursor.toArray();
							trip = Calculate_EmpTrip(
									(ArrayList<DBObject>) listObjects,
									mongoconnection, TripTimeLimit, UserList
											.get(id).getDeviceID(), UserList
											.get(id).getName());

						}

						bfwriter.write("\n------Trip Generate count is == "
								+ trip + "  for  " + UserList.get(id).getName()
								+ " , Device Id : "
								+ UserList.get(id).getDeviceID());

						// bfwriter.write("\n------Trip Generate count is == "+trip);

					} else {
						msgObj.setError("True");
						msgObj.setMessage("Time not found for calutaing TripReport. ");

					}

				}// for end

			} else {
				msgObj.setError("True");
				msgObj.setMessage("Device not found for TripReport. ");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgObj.setError("True");
			msgObj.setMessage("Getting error in Generate Device Pending TripReport due to --"
					+ e.getMessage());
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgObj.setError("True");
			msgObj.setMessage("Getting error in Generate Device Pending TripReport due to --"
					+ e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

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

	public int Calculate_EmpTrip(ArrayList<DBObject> location_list,
			DB mongoconnection, int tripTimeLimit, String DeviceId, String Name) {

		long TotalLocationData = location_list.size();
		Double MaxSpeed = 0.0;
		Double TotalSpeed = 0.0;
		int Triplocationcount = 0;
		Double SpeedcCheck = 1.0;
		;
		ArrayList<DBObject> TempCalLoctionList = new ArrayList<>();
		int TripTimeLimit = tripTimeLimit;
		long SignalCheckcount = 0;
		long Timeidiff = 0;
		double HistoryDistanceKM = 0;
		int TripCount = 0;

		TempCalLoctionList.clear();
		for (int i = 0; i < TotalLocationData; i++) {
			long diff = 0;

			SpeedcCheck = Double.parseDouble(location_list.get(i).get("speed")
					.toString());
			// //System.err.println("------Speedcheck-------"+i+"----"+Common.getDateCurrentTimeZone(Long.parseLong(location_list.get(i).get("timestamp")+""))+"---------"+SpeedcCheck);

			if (SpeedcCheck != 0) {
				TempCalLoctionList.add(location_list.get(i));
				// //System.err.println("***TempLoctionList**************"+i+"*********************************");
			}

			if (SpeedcCheck > MaxSpeed)
				MaxSpeed = SpeedcCheck;

			TotalSpeed = TotalSpeed + SpeedcCheck;

			if (i > 0 && i < TotalLocationData - 1) {
				BasicDBObject locobj1 = (BasicDBObject) location_list.get(i)
						.get("location");
				double lat1 = locobj1.getDouble("lat");
				double lng1 = locobj1.getDouble("lon");

				BasicDBObject locobj2 = (BasicDBObject) location_list
						.get(i + 1).get("location");
				double lat2 = locobj2.getDouble("lat");
				double lng2 = locobj2.getDouble("lon");

				if (lat1 != lat2 && lng1 != lng2) {
					double caldist = distance(lat1, lng1, lat2, lng2, "K");

					if (!Double.isNaN(caldist)) {
						HistoryDistanceKM = HistoryDistanceKM + caldist;

					}
					// //System.out.println("HistoryDistanceKM----"+HistoryDistanceKM);

				}

			}
			/*
			 * if (i>2 && i<TotalLocationData-3) { Long
			 * srctime=Long.parseLong(TempLoctionList
			 * .get(TempLoctionList.size()-2).get("timestamp").toString()); Long
			 * desttime
			 * =Long.parseLong(TempLoctionList.get(TempLoctionList.size()
			 * -1).get("timestamp").toString());
			 * 
			 * Timeidiff=desttime-srctime;
			 * 
			 * }
			 */

			if (i > 1 && TempCalLoctionList.size() > 0) {

				Long srctime = Long.parseLong(location_list.get(i - 1)
						.get("timestamp").toString());
				Long desttime = Long.parseLong(location_list.get(i)
						.get("timestamp").toString());

				diff = desttime - srctime;

				SignalCheckcount = SignalCheckcount + diff;
				// System.err.println("i-----"+i+"---------SpeedcCheck---"+SpeedcCheck+"-----SignalCheckcount---"+
				// SignalCheckcount+"----------Diff-----"+diff+"------Time-----"+Common.getDateCurrentTimeZone(Long.parseLong(location_list.get(i).get("timestamp").toString())));

			}

			if ((TempCalLoctionList.size() > 0 && diff > TripTimeLimit)
					|| (TempCalLoctionList.size() > 0 && i == TotalLocationData - 1)) {

				// System.out.println("********** Trip****Starttime***"+i+"**************"+Common.getDateCurrentTimeZone(Long.parseLong(TempCalLoctionList.get(0).get("timestamp").toString())));
				// System.out.println("********** Trip****EndTime******"+i+"***********"+Common.getDateCurrentTimeZone(Long.parseLong(TempCalLoctionList.get(TempCalLoctionList.size()-2).get("timestamp").toString())));

				// System.err.println("-----SignalCheckcount Got ---"+
				// SignalCheckcount+"   speedcheck=="+SpeedcCheck+"   Historydist---"+HistoryDistanceKM+"  MaxSpeed -"+MaxSpeed+"\n\n");

				BasicDBObject Maindocument = new BasicDBObject();
				ObjectId objid = new ObjectId();
				Maindocument.put("report_id", "" + objid);
				Maindocument.put("device", DeviceId);
				Maindocument.put("name", Name);

				Maindocument.put("totalkm", HistoryDistanceKM);
				Maindocument.put("maxspeed", MaxSpeed);

				if (TempCalLoctionList.size() > 0) {
					Maindocument.put("avgspeed", TotalSpeed
							/ TempCalLoctionList.size());
				} else {
					Maindocument.put("avgspeed", 0);
				}

				DBObject srcdbobj = TempCalLoctionList.get(0);

				BasicDBObject locallocobj = (BasicDBObject) srcdbobj
						.get("location");
				double locallat1 = locallocobj.getDouble("lat");
				double locallng1 = locallocobj.getDouble("lon");

				BasicDBObject Intial_Loc = new BasicDBObject();
				Intial_Loc.put("lat", locallat1);
				Intial_Loc.put("lon", locallng1);
				Intial_Loc.put("speed", srcdbobj.get("speed"));
				Intial_Loc.put("timestamp", srcdbobj.get("timestamp"));
				Maindocument.put("source_info", Intial_Loc);

				DBObject destobj = TempCalLoctionList.get(TempCalLoctionList
						.size() - 2);

				BasicDBObject destlocobj = (BasicDBObject) destobj
						.get("location");
				double locallat2 = destlocobj.getDouble("lat");
				double locallnt2 = destlocobj.getDouble("lon");

				BasicDBObject Dest_Loc = new BasicDBObject();
				Dest_Loc.put("lat", locallat2);
				Dest_Loc.put("lon", locallnt2);
				Dest_Loc.put("speed", destobj.get("speed"));
				Dest_Loc.put("timestamp", destobj.get("timestamp"));
				Maindocument.put("dest_info", Dest_Loc);

				DBCollection Triptable = mongoconnection
						.getCollection(Common.TABLE_EMP_TRIPREPORT);

				Triptable.insert(Maindocument);
				HistoryDistanceKM = 0.0;
				TempCalLoctionList.clear();
				SignalCheckcount = 0;
				MaxSpeed = 0.0;
				TotalSpeed = 0.0;
				Timeidiff = 0;
				Triplocationcount = 0;
				TripCount++;

			} else if (HistoryDistanceKM < 1) {
				SignalCheckcount = 0;
				MaxSpeed = 0.0;
				TotalSpeed = 0.0;
				Timeidiff = 0;
				Triplocationcount = 0;
				// TempCalLoctionList.clear();
			}

		}

		return TripCount;
	}

	public ArrayList<TripInfoDto> GetDirectEmpTripReport(DB mongoconnection,
			Connection con, String device, String startdate, String enddate) {

		String driver_startdate = "", driver_enddate = "", driver_name = "";
		try {

			SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MMM-dd");// Edit
																				// here
																				// depending
																				// on
																				// your
																				// requirements
			Date df = new Date(Long.valueOf(startdate) * 1000);
			String cur_date = monthFormat.format(df);

			String[] strmonth = cur_date.split("-");
			// System.err.println("cur_date: ------" +
			// strmonth[0]+"-"+strmonth[1]+"*"+strmonth[2] );

			ResultSet rsdate = null;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call GetEmpDayTime(?,?,?,?)}");

			stmt.setString(1, device);
			stmt.setInt(2, Integer.parseInt(strmonth[2]));
			stmt.setString(3, strmonth[1]);
			stmt.setInt(4, Integer.parseInt(strmonth[0]));

			rsdate = stmt.executeQuery();
			if (rsdate != null) {
				while (rsdate.next()) {
					driver_startdate = rsdate.getString("StartTime") + "";
					driver_enddate = rsdate.getString("EndTime") + "";
					driver_name = rsdate.getString("DriverName") + "";

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<TripInfoDto> Triplist = new ArrayList<TripInfoDto>();
		MessageObject msg = new MessageObject();
		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_EMP_TRIPREPORT);
			// System.out.println("device==============----------"+device+" "+table.getFullName());

			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device", device);

			BasicDBObject timestamp_whereQuery = new BasicDBObject(
					"source_info.timestamp", new BasicDBObject("$gte",
							Long.parseLong(startdate)).append("$lt",
							Long.parseLong(enddate)));

			BasicDBList And_Milage = new BasicDBList();
			And_Milage.add(timestamp_whereQuery);
			And_Milage.add(device_whereQuery);
			DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

			DBCursor cursor = table.find(Total_Milage_query).sort(
					new BasicDBObject("source_info.timestamp", 1));
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			// System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+Total_Milage_query);
			long i = 0;
			long total = cursor.count();

			if (cursor.size() != 0) {

				// System.err.println("*-GetDirectTripReport----COunt---"+cursor.size());

				while (cursor.hasNext()) {

					TripInfoDto trip = new TripInfoDto();
					dbObject1 = (DBObject) cursor.next();

					BasicDBObject Sourceobj = (BasicDBObject) dbObject1
							.get("source_info");
					BasicDBObject Destobj = (BasicDBObject) dbObject1
							.get("dest_info");

					trip.setDevice(device);
					trip.setSrclat(Sourceobj.getString("lat"));
					trip.setSrclon(Sourceobj.getString("lon"));
					trip.setSrcspeed(Sourceobj.getString("speed"));

					trip.setSrc_adress(Sourceobj.getString("src_address"));

					trip.setSrctimestamp(Sourceobj.getString("timestamp"));

					trip.setDestlat(Destobj.getString("lat"));
					trip.setDestlon(Destobj.getString("lon"));
					trip.setDestspeed(Destobj.getString("speed"));
					trip.setDest_address(Destobj.getString("dest_address"));

					trip.setDesttimestamp(Destobj.getString("timestamp"));

					trip.setReport_id(dbObject1.get("report_id").toString());
					trip.setTotalkm(dbObject1.get("totalkm").toString());
					trip.setMaxspeed(dbObject1.get("maxspeed").toString());
					// trip.setAvgspeed(String.format("%.2f",
					// dbObject1.get("avgspeed")));
					trip.setAvgspeed(dbObject1.get("avgspeed").toString());

					trip.setDevicename(dbObject1.get("name").toString());
					trip.setDriver_start_time(driver_startdate);
					trip.setDriver_end_time(driver_enddate);
					trip.setDriver_name(driver_name);

					// trip.setDevicename("vts");

					Triplist.add(trip);

				}

				for (int j = 0; j < Triplist.size(); j++) {
					TripInfoDto item = Triplist.get(j);

					Triplist.get(j).setSrc_adress(
							Common.GetAddress(item.getSrclat(),
									item.getSrclon()));
					Triplist.get(j).setDest_address(
							Common.GetAddress(item.getDestlat(),
									item.getDestlon()));
				}

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Triplist;

	}

	public MessageObject GetEmpTripReport(DB mongoconnection, Connection con,
			String device, String startdate, String enddate, String email) {
		String driver_startdate = "", driver_enddate = "", driver_name = "";

		String offset_otDevice = "GMT+05:30";
		// Get device Timeoffset

		try {
			java.sql.CallableStatement ps = con
					.prepareCall("{call GetOffsetOfdevice(?)}");
			ps.setString(1, device);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					TimeOffsetDTO obj = new TimeOffsetDTO();
					obj.setOffsetId("" + rs.getString("Id"));
					obj.setOffsetActual("" + rs.getString("OffsetActual"));
					obj.setOffsetUponIndia("" + rs.getString("OffsetUponIndia"));
					obj.setCountyName("" + rs.getString("CountyName"));
					offset_otDevice = obj.getOffsetActual();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// //Get Emp Start time and end time
		try {

			SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MMM-dd");// Edit
																				// here
																				// depending
																				// on
																				// your
																				// requirements
			Date df = new Date(Long.valueOf(startdate) * 1000);
			String cur_date = monthFormat.format(df);
			String[] strmonth = cur_date.split("-");
			// System.err.println("cur_date: ------" +
			// strmonth[0]+"-"+strmonth[1]+"*"+strmonth[2] );

			ResultSet rsdate = null;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call GetEmpDayTime(?,?,?,?)}");

			stmt.setString(1, device);
			stmt.setInt(2, Integer.parseInt(strmonth[2]));
			stmt.setString(3, strmonth[1]);
			stmt.setInt(4, Integer.parseInt(strmonth[0]));

			rsdate = stmt.executeQuery();
			if (rsdate != null) {
				while (rsdate.next()) {
					driver_startdate = rsdate.getString("StartTime") + "";
					driver_enddate = rsdate.getString("EndTime") + "";
					driver_name = rsdate.getString("DriverName") + "";

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<TripInfoDto> Triplist = new ArrayList<TripInfoDto>();
		MessageObject msg = new MessageObject();
		DBCollection table = mongoconnection
				.getCollection(Common.TABLE_EMP_TRIPREPORT);
		// System.out.println("device==============----------"+device+" "+table.getFullName());

		BasicDBObject device_whereQuery = new BasicDBObject();
		device_whereQuery.put("device", device);

		BasicDBObject timestamp_whereQuery = new BasicDBObject(
				"source_info.timestamp", new BasicDBObject("$gte",
						Long.parseLong(startdate)).append("$lt",
						Long.parseLong(enddate)));

		BasicDBList And_Milage = new BasicDBList();
		And_Milage.add(timestamp_whereQuery);
		And_Milage.add(device_whereQuery);
		DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

		DBCursor cursor = table.find(Total_Milage_query).sort(
				new BasicDBObject("source_info.timestamp", 1));
		cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

		// System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+Total_Milage_query);
		long i = 0;
		long total = cursor.count();

		if (cursor.size() != 0) {

			// System.err.println("*-----COunt---"+cursor.size());

			while (cursor.hasNext()) {

				TripInfoDto trip = new TripInfoDto();
				dbObject1 = (DBObject) cursor.next();

				BasicDBObject Sourceobj = (BasicDBObject) dbObject1
						.get("source_info");
				BasicDBObject Destobj = (BasicDBObject) dbObject1
						.get("dest_info");

				trip.setDevice(device);
				trip.setSrclat(Sourceobj.getString("lat"));
				trip.setSrclon(Sourceobj.getString("lon"));
				trip.setSrcspeed(Sourceobj.getString("speed"));

				trip.setSrc_adress(Sourceobj.getString("src_address"));

				trip.setSrctimestamp(Sourceobj.getString("timestamp"));

				trip.setDestlat(Destobj.getString("lat"));
				trip.setDestlon(Destobj.getString("lon"));
				trip.setDestspeed(Destobj.getString("speed"));
				trip.setDest_address(Destobj.getString("dest_address"));

				trip.setDesttimestamp(Destobj.getString("timestamp"));

				trip.setReport_id(dbObject1.get("report_id").toString());
				trip.setTotalkm(dbObject1.get("totalkm").toString());
				trip.setMaxspeed(dbObject1.get("maxspeed").toString());
				trip.setAvgspeed(String.format("%.2f",
						dbObject1.get("avgspeed")));

				trip.setDevicename(dbObject1.get("name").toString());
				trip.setDriver_start_time(driver_startdate);
				trip.setDriver_end_time(driver_enddate);
				trip.setDriver_name(driver_name);

				// trip.setDevicename("vts");

				Triplist.add(trip);

			}

		}
		SendEmail se = new SendEmail();
		Boolean sent = se.SendTripReportEmailAttachment(Triplist, email,
				offset_otDevice, "14", "");

		if (sent) {
			msg.setError("false");
			msg.setMessage("Trip report sent successfully.");
		} else {
			msg.setError("true");
			msg.setMessage("Error in sending Trip Report.");
		}

		// System.err.println("Get Triipo Report=="+msg.getMessage());

		return msg;

	}

	public MessageObject PostGcmKeyToServer(DB mongoconnection, Connection con,
			String gcm_key, String user_id, String name) {

		// System.err.println("------------------"+"PostGcmKeyToServer");

		MessageObject msgobj = new MessageObject();
		DBCollection sostable = mongoconnection
				.getCollection(Common.TABLE_EMPGCMKEY);

		// Create New Diary Id
		ObjectId id1 = new ObjectId();

		try {

			DBObject find = new BasicDBObject("emp_user_id", user_id);

			// DBObject listItem = new BasicDBObject("sosno", new
			// BasicDBObject("name",name).append("number",number));
			DBObject listItem = new BasicDBObject("gcm_keys",
					new BasicDBObject("id", id1.toString())
							.append("name", name).append("key", gcm_key));

			DBObject updateQuery = new BasicDBObject("$push", listItem);
			sostable.update(find, updateQuery, true, false);

			msgobj.setError("false");
			msgobj.setMessage("GCM key add sucessfully.");

			// System.err.println(msgobj.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			msgobj.setError("true");
			msgobj.setMessage("GCM keynot add sucessfully.");
		} finally {

		}
		return msgobj;

	}

	public String SaveTaskToEmp(Connection con, DB mongoconnection,
			String emp_user_id, String user_id, String emp_car_id, String day,
			String month, String year, String time, String address,
			String route, String row_id, String isedit) {

		// System.err.println(emp_car_id+"-"+user_id+"-"+emp_car_id+"-"+time+"-"+route+"-"+row_id+"-"+isedit);
		MessageObject msgo = new MessageObject();

		SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mma");
		Date date = null;
		try {
			date = parseFormat.parse(time);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// System.out.println(parseFormat.format(date) + " = " +
		// displayFormat.format(date));

		try {
			java.sql.CallableStatement ps = con
					.prepareCall("{call SaveEmployeeTaskDetails(?,?,?,?,?,?,?,?,?,?,?,?)}");

			ps.setInt(1, Integer.parseInt(emp_user_id));
			ps.setInt(2, Integer.parseInt(user_id));
			ps.setInt(3, Integer.parseInt(emp_car_id));
			ps.setInt(4, Integer.parseInt(day));
			ps.setInt(5, Integer.parseInt(month));
			ps.setInt(6, Integer.parseInt(year));
			ps.setString(7, time);
			ps.setString(8, address);
			// ps.setString(9, year+"-"+month+"-"+day+" "+
			// displayFormat.format(date));
			ps.setString(9, year + "-" + month + "-" + day + " 00:00:00");

			ps.setString(10, route);
			ps.setInt(11, Integer.parseInt(row_id));
			ps.setInt(12, Integer.parseInt(isedit));

			/*
			 * // *** note that it's "yyyy-MM-dd hh:mm:ss" not
			 * "yyyy-mm-dd hh:mm:ss" SimpleDateFormat dt = new
			 * SimpleDateFormat("dd-MM-yyyy hh:mm:ss a"); Date date =
			 * dt.parse(report_start_time);
			 * 
			 * // *** same for the format String below SimpleDateFormat dt1 =
			 * new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			 * //System.out.println(dt1.format(date));
			 */

			int result = ps.executeUpdate();
			if (result == 0) {
				msgo.setError("true");
				msgo.setMessage("Task not added successfully");
			} else {
				msgo.setError("false");
				msgo.setMessage("Task added successfully.");

				JSONObject jomain = new JSONObject();
				jomain.put("emp_user_id", emp_user_id);
				jomain.put("emp_car_id", emp_car_id);

				jomain.put("user_id", user_id);
				jomain.put("time", time);
				jomain.put("address", address);
				jomain.put("message_type", "emp_task");
				jomain.put("day", day);
				jomain.put("month", month);
				jomain.put("year", year);
				JSONObject jodata = new JSONObject();
				jodata.put("data", jomain);

				SendTaskGCMNotification(mongoconnection, emp_user_id,
						emp_car_id, jodata);

				// SendTaskGCMNotification(mongoconnection,emp_user_id,emp_car_id,jodata);

			}

		} catch (Exception e) {

			msgo.setMessage("Error: " + e.getMessage());
			e.printStackTrace();
			msgo.setError("true");
		}
		// System.err.println("SaveTaskToEmp----------"+new
		// Gson().toJson(msgo));
		return msgo.getMessage();

	}

	private void SendTaskGCMNotification(DB mongoconnection,
			String emp_user_id, String emp_car_id, JSONObject userMessage) {

		String feeds = null;
		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_EMPGCMKEY);
			// System.out.println("UserId==============----------"+emp_user_id+" "+table.getFullName());

			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("emp_user_id", emp_user_id);
			DBCursor cursor = table.find(whereQuery);
			// System.out.println("Cursor Count of up----"+cursor.size()+whereQuery);

			while (cursor.hasNext()) {

				if (cursor.size() != 0) {

					DBObject dbObject = (DBObject) cursor.next();
					BasicDBList sosList = (BasicDBList) dbObject
							.get("gcm_keys");
					for (int i = 0; i < sosList.size(); i++) {
						BasicDBObject Obj = (BasicDBObject) sosList.get(i);
						// class_id=""+diaryObj.getString("ClassID");
						// System.out.println("Cursor print----------"+Obj.toString());
						// Obj.getString("name"));
						// sosObj.getString("id"));
						Common.SendGCMPushNotofication(Obj.getString("key"),
								userMessage.toString());
						;
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		}

	}

	public ArrayList<DriverEmpTaskSheduledDTO> GetDriverEmpTaskList(
			Connection con, DB mongoconnection, String emp_user_id, String day,
			String month, String year) {
		ArrayList<DriverEmpTaskSheduledDTO> p = new ArrayList<DriverEmpTaskSheduledDTO>();
		String Photo = "";
		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetDriverEmpTaskList(?,?,?,?)}");
			ps.setInt(1, Integer.parseInt(emp_user_id));
			ps.setInt(2, Integer.parseInt(day));
			ps.setInt(3, Integer.parseInt(month));
			ps.setInt(4, Integer.parseInt(year));

			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					DriverEmpTaskSheduledDTO person = new DriverEmpTaskSheduledDTO();
					person.setEmp_car_id("" + rs.getString("VehicleID"));
					person.setAddress("" + rs.getString("Address"));
					person.setTime("" + rs.getString("StartTime"));
					person.setCar_name("" + rs.getString("VehicleName"));
					person.setTask_id("" + rs.getString("Id"));

					p.add(person);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("S55555555555"+p.toString());
		return p;
	}

	public ArrayList<DriverEmpTaskSheduledDTO> GetDriverEmpDaywiseTaskList(
			Connection con, DB mongoconnection, String emp_user_id) {
		ArrayList<DriverEmpTaskSheduledDTO> p = new ArrayList<DriverEmpTaskSheduledDTO>();
		String Photo = "";
		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetDriverEmpDaywiseTaskList(?)}");
			ps.setInt(1, Integer.parseInt(emp_user_id));

			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					DriverEmpTaskSheduledDTO person = new DriverEmpTaskSheduledDTO();
					person.setEmp_car_id("" + rs.getString("VehicleID"));
					person.setAddress("" + rs.getString("Address"));
					person.setTime("" + rs.getString("StartTime"));
					person.setCar_name("" + rs.getString("VehicleName"));
					person.setTask_id("" + rs.getString("Id"));
					person.setDay_name("" + rs.getString("day_name"));
					person.setRoute("" + rs.getString("Route"));
					person.setReport_start_time(""
							+ rs.getString("ReportStartTime"));

					p.add(person);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("S55555555555"+p.toString());
		return p;
	}

	public MessageObject GenerateTripReport_USDevice(Connection con,
			DB mongoconnection) {

		ArrayList<DeviceInfoDto> UserList = new ArrayList<>();
		ArrayList<DBObject> TempLoctionList = new ArrayList<>();
		int TripTimeLimit = 300;
		File tripfile;
		int Deaactivecount = 0;
		FileWriter writer = null;
		BufferedWriter bfwriter = null;
		calendar = Calendar.getInstance();
		ArrayList<Double> DeaciveListDevice = new ArrayList<Double>();

		try {
			// Whatever the file path is.
			tripfile = new File(Common.Log_path + "GenerateTripReport_log"
					+ calendar.getTime() + ".txt");
			// Create the file
			if (tripfile.createNewFile()) {
				// System.out.println("File is created!");
			} else {
				// System.out.println("File already exists.");
			}

			// Write Content
			writer = new FileWriter(tripfile);

			bfwriter = new BufferedWriter(writer);
			bfwriter.write("\nStart Generate Daily TripReprt Log----at "
					+ calendar.getTime());

		} catch (IOException e) {
			// System.err.println("Problem writing to the file statsTest.txt");
		} catch (Exception e) {

			e.printStackTrace();
		}

		int trip = 0;
		calendar = Calendar.getInstance();

		try {

			java.sql.CallableStatement psc = con
					.prepareCall("{call Get_US_DeviceListForTripReport()}");
			ResultSet rs = psc.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					DeviceInfoDto dto = new DeviceInfoDto();
					dto.setName(rs.getString(1) + rs.getString(2));
					dto.setDeviceID(rs.getString(3));
					dto.setDeviceType(rs.getInt(4));
					if (dto.getDeviceType() != 1) {
						UserList.add(dto);
						System.out.println("--------Report Req--Allow---"
								+ rs.getString(1) + rs.getString(2) + "-----"
								+ rs.getInt(4));

					}

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		try {
			PreparedStatement ps1 = con
					.prepareStatement("select ActualData from ApplicationMetaData where ApplicationMetaData.ID=5");
			ResultSet rs1 = ps1.executeQuery();
			if (rs1 != null) {
				while (rs1.next()) {
					TripTimeLimit = Integer.parseInt(rs1.getString(1));

				}
			}
			// System.err.println("TripTimeLimit---------"+TripTimeLimit);
		} catch (Exception e) {

			e.printStackTrace();
		}

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long Starttime = Long.valueOf(Common.getUs_GMTTimeStampFromDate(day - 1
				+ "-" + String.valueOf(month + 1) + "-" + year + " 00:01 am"));
		long Endtime = Long.valueOf(Common.getUs_GMTTimeStampFromDate(day + "-"
				+ String.valueOf(month + 1) + "-" + year + " 00:01 am"));

		/*
		 * long Starttime = 1504809000; long Endtime =1504895400;
		 */

		// System.out.println("StartTime:"+Starttime+"  End Time:"+Endtime+"  UserList size: "+UserList.size());

		// for(int d=15;d>0;d--)
		try {

			/*
			 * long Starttime =
			 * Long.valueOf(Common.getUs_GMTTimeStampFromDate(day - d + "-" +
			 * String.valueOf(month + 1) + "-" + year + " 00:01 am")); long
			 * Endtime =
			 * Long.valueOf(Common.getUs_GMTTimeStampFromDate(day-(d-1) + "-" +
			 * String.valueOf(month + 1) + "-" + year + " 00:01 am"));
			 */
			/*
			 * bfwriter.write(
			 * "\n-----------------------Report Generating  for---------------------------"
			 * + "\n\n******************" + "StartTime: " + Starttime +
			 * "  End Time: " + Endtime + "********");
			 */

			for (int id = 0; id < UserList.size(); id++) {
				// for (int id = 5; id<=5; id++) {

				trip = 0;
				HistoryDistance = 0;

				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_LOCATION);
				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device",
						Long.parseLong(UserList.get(id).getDeviceID()));
				// device_whereQuery.put("device",Long.parseLong("355488020181042"));

				BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp",
						new BasicDBObject("$gte", Starttime).append("$lt",
								Endtime));
				BasicDBList And_Milage = new BasicDBList();
				And_Milage.add(timestamp_whereQuery);
				And_Milage.add(device_whereQuery);
				DBObject Total_Milage_query = new BasicDBObject("$and",
						And_Milage);

				DBCursor cursor = table.find(Total_Milage_query);
				cursor.sort(new BasicDBObject("timestamp", 1));

				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				// System.out.println("Cursor Count of u-p11----"+cursor.size()+"  "+Total_Milage_query);
				long i = 0;
				long total = cursor.size();
				Double MaxSpeed = 0.0;
				Double TotalSpeed = 0.0;
				int Triplocationcount = 0;

				if (cursor.size() > 0) {
					TempLoctionList.clear();

					// System.err.println("*-----COunt---"+cursor.size());

					List<DBObject> listObjects = cursor.toArray();
					trip = Calculate_Trip((ArrayList<DBObject>) listObjects,
							mongoconnection, TripTimeLimit, UserList.get(id)
									.getDeviceID(), UserList.get(id).getName());

				}

				/*
				 * bfwriter.write("\n------Trip Generate count is == " + trip +
				 * "  for  " + UserList.get(id).getName() + " , Device Id : " +
				 * UserList.get(id).getDeviceID());
				 */
				// bfwriter.write("\n------Trip Generate count is == "+trip);

			}// for end
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {

				if (bfwriter != null)
					bfwriter.close();

				if (writer != null)
					writer.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}

		return null;

	}

	public MessageObject CheckDriverOnRoute(DB mongoconnection, Connection con) {
		/*
		 * calendar = Calendar.getInstance(); year =
		 * calendar.get(Calendar.YEAR);
		 * TimeZone.setDefault(TimeZone.getTimeZone("GMT-5:30"));
		 * 
		 * month = calendar.get(Calendar.MONTH); day =
		 * calendar.get(Calendar.DAY_OF_MONTH);
		 * 
		 * String format = "dd-MM-yyyy hh:mm aa"; SimpleDateFormat
		 * sdfLocalFormat = new SimpleDateFormat(format);
		 * 
		 * //System.err.println("Time us----------"+new Date());
		 */

		ArrayList<DriverEmpTaskSheduledDTO> plist = new ArrayList<DriverEmpTaskSheduledDTO>();
		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetCheckDriverOnRoute()}");

			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					DriverEmpTaskSheduledDTO person = new DriverEmpTaskSheduledDTO();
					person.setEmp_car_id("" + rs.getString("VehicleID"));
					person.setAddress("" + rs.getString("Address"));
					person.setTime("" + rs.getString("StartTime"));
					person.setCar_name("" + rs.getString("VehicleName"));
					person.setTask_id("" + rs.getString("Id"));
					person.setRoute("" + rs.getString("Route"));
					person.setReport_start_time(""
							+ rs.getString("ReportStartTime"));

					plist.add(person);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.println(new Gson().toJson(plist));

		return null;
	}

	public MessageObject GetWeekalyTripReportOnMail(Connection con,
			DB mongoconnection, String device, String startdate,
			String enddate, String email) {
		String offset_otDevice = "GMT+05:30";
		TimeOffsetDTO obj = new TimeOffsetDTO();
		String empname = "N/A";

		try {
			java.sql.CallableStatement ps = con
					.prepareCall("{call GetOffsetOfdevice(?)}");
			ps.setString(1, device);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					obj.setOffsetId("" + rs.getString("Id"));
					obj.setOffsetActual("" + rs.getString("OffsetActual"));
					obj.setOffsetUponIndia("" + rs.getString("OffsetUponIndia"));
					obj.setCountyName("" + rs.getString("CountyName"));
					obj.setFlag("" + rs.getString("flag"));

					offset_otDevice = obj.getOffsetActual();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (obj.getFlag() != null && obj.getFlag().equalsIgnoreCase("15")) {

			try {
				java.sql.CallableStatement ps = con
						.prepareCall("{call GetDriverUponTaskOnthisDay(?,?,?,?)}");
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(Long.parseLong(startdate));
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);

				ps.setInt(1, year);
				ps.setInt(2, month + 1);
				ps.setInt(3, day);

				ps.setString(4, device);
				ResultSet rs = ps.executeQuery();
				int i = 0;

				if (rs != null) {
					while (rs.next()) {
						if (i == 0) {
							empname = "" + rs.getString("Name");
						} else {
							empname.concat("/" + rs.getString("Name"));
						}

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

		}

		ArrayList<TripInfoDto> Triplist = new ArrayList<TripInfoDto>();
		MessageObject msg = new MessageObject();
		DBCollection table = mongoconnection
				.getCollection(Common.TABLE_TRIPREPORT);
		// System.out.println("device==============----------"+device+" "+table.getFullName());

		BasicDBObject device_whereQuery = new BasicDBObject();
		device_whereQuery.put("device", device);

		BasicDBObject timestamp_whereQuery = new BasicDBObject(
				"source_info.timestamp", new BasicDBObject("$gte",
						Long.parseLong(startdate)).append("$lt",
						Long.parseLong(enddate)));

		BasicDBList And_Milage = new BasicDBList();
		And_Milage.add(timestamp_whereQuery);
		And_Milage.add(device_whereQuery);
		DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

		DBCursor cursor = table.find(Total_Milage_query).sort(
				new BasicDBObject("source_info.timestamp", 1));
		cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

		// System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+Total_Milage_query);
		long i = 0;
		long total = cursor.count();

		if (cursor.size() != 0) {

			// System.err.println("*-----COunt---"+cursor.size());

			while (cursor.hasNext()) {

				TripInfoDto trip = new TripInfoDto();
				dbObject1 = (DBObject) cursor.next();

				BasicDBObject Sourceobj = (BasicDBObject) dbObject1
						.get("source_info");
				BasicDBObject Destobj = (BasicDBObject) dbObject1
						.get("dest_info");

				trip.setDevice(device);
				trip.setSrclat(Sourceobj.getString("lat"));
				trip.setSrclon(Sourceobj.getString("lon"));
				trip.setSrcspeed(Sourceobj.getString("speed"));

				trip.setSrc_adress(Sourceobj.getString("src_address"));

				trip.setSrctimestamp(Sourceobj.getString("timestamp"));

				trip.setDestlat(Destobj.getString("lat"));
				trip.setDestlon(Destobj.getString("lon"));
				trip.setDestspeed(Destobj.getString("speed"));
				trip.setDest_address(Destobj.getString("dest_address"));

				trip.setDesttimestamp(Destobj.getString("timestamp"));

				trip.setReport_id(dbObject1.get("report_id").toString());
				trip.setTotalkm(dbObject1.get("totalkm").toString());
				trip.setMaxspeed(dbObject1.get("maxspeed").toString());
				trip.setAvgspeed(String.format("%.2f",
						dbObject1.get("avgspeed")));

				trip.setDevicename(dbObject1.get("name").toString());
				// trip.setDevicename("vts");

				Triplist.add(trip);

			}

		}
		SendEmail se = new SendEmail();
		Boolean sent;
		if (obj.getFlag() != null)
			sent = se.SendTripReportEmailAttachment(Triplist, email,
					offset_otDevice, obj.getFlag(), empname);
		else
			sent = se.SendTripReportEmailAttachment(Triplist, email,
					offset_otDevice, "0", empname);

		if (sent) {
			msg.setError("false");
			msg.setMessage("Trip report sent successfully.");
		} else {
			msg.setError("true");
			msg.setMessage("Error in sending Trip Report.");
		}

		// System.err.println("Get Triipo Report=="+msg.getMessage());

		return msg;

	}

	public MessageObject GeneratePendingTripReport_USDevice(Connection con,
			DB mongoconnection, String startdate, String enddate)
			throws IOException {
		ArrayList<DeviceInfoDto> UserList = new ArrayList<>();
		int SpeedcCheck = 0;
		ArrayList<DBObject> TempLoctionList = new ArrayList<>();
		int TripTimeLimit = 300;
		int trip = 0;
		MessageObject msgObj = new MessageObject();

		long SignalCheckcount = 0;
		File GeneratePendingTripReport;
		FileWriter writer = null;
		calendar = Calendar.getInstance();

		BufferedWriter bfwriter = null;
		calendar = Calendar.getInstance();
		ArrayList<Double> DeaciveListDevice = new ArrayList<Double>();

		try {
			// Whatever the file path is.
			GeneratePendingTripReport = new File(Common.Log_path
					+ "GenerateTripReport_log" + calendar.getTime() + ".txt");
			// Create the file
			if (GeneratePendingTripReport.createNewFile()) {
				// System.out.println("GeneratePendingTripReport File is created!");
			} else {
				// System.out.println("GeneratePendingTripReport File already exists.");
			}

			// Write Content
			writer = new FileWriter(GeneratePendingTripReport);

			bfwriter = new BufferedWriter(writer);
			bfwriter.write("\n  Generate Pending TripReprt  Log Started  at "
					+ Calendar.getInstance().getTime());

		} catch (IOException e) {
			// System.err.println("Problem writing to the file statsTest.txt");
		} catch (Exception e) {

			e.printStackTrace();
		}

		try {
			java.sql.CallableStatement psc = con
					.prepareCall("{call Get_US_DeviceListForTripReport()}");
			ResultSet rs = psc.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					DeviceInfoDto dto = new DeviceInfoDto();
					dto.setName(rs.getString(1) + rs.getString(2));
					dto.setDeviceID(rs.getString(3));

					UserList.add(dto);

					// System.out.println("--------Report Req-----"+rs.getString(1)+rs.getString(2));
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			// System.err.println("Problem writing to the file GeneratePendingTripReport.txt");

		}

		try {
			PreparedStatement ps1 = con
					.prepareStatement("select ActualData   from ApplicationMetaData where ApplicationMetaData.ID=5");
			ResultSet rs1 = ps1.executeQuery();
			if (rs1 != null) {
				while (rs1.next()) {
					TripTimeLimit = Integer.parseInt(rs1.getString(1));

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			// System.err.println("Problem writing to the file GeneratePendingTripReport.txt");

		}

		try {
			bfwriter.write("\n-----------------------Report Generating  for---------------------------"
					+ "\n\n******************"
					+ "StartTime: "
					+ startdate
					+ "  End Time: " + enddate + "********");

			for (int id = 0; id < UserList.size(); id++) {

				trip = 0;
				HistoryDistance = 0;

				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_LOCATION);
				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device",
						Long.parseLong(UserList.get(id).getDeviceID()));
				// device_whereQuery.put("device",Long.parseLong("355488020181042"));

				BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp",
						new BasicDBObject("$gte", startdate).append("$lt",
								enddate));
				BasicDBList And_Milage = new BasicDBList();
				And_Milage.add(timestamp_whereQuery);
				And_Milage.add(device_whereQuery);
				DBObject Total_Milage_query = new BasicDBObject("$and",
						And_Milage);

				DBCursor cursor = table.find(Total_Milage_query);
				cursor.sort(new BasicDBObject("timestamp", 1));

				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				// System.out.println("Cursor Count of u-p11----"+cursor.size()+"  "+Total_Milage_query);
				long i = 0;
				long total = cursor.size();
				Double MaxSpeed = 0.0;
				Double TotalSpeed = 0.0;
				int Triplocationcount = 0;

				if (cursor.size() > 0) {
					TempLoctionList.clear();

					// System.err.println("*-----COunt---"+cursor.size());

					List<DBObject> listObjects = cursor.toArray();
					trip = Calculate_Trip((ArrayList<DBObject>) listObjects,
							mongoconnection, TripTimeLimit, UserList.get(id)
									.getDeviceID(), UserList.get(id).getName());

				}

				bfwriter.write("\n------Trip Generate count is == " + trip
						+ "  for  " + UserList.get(id).getName()
						+ " , Device Id : " + UserList.get(id).getDeviceID());

				// bfwriter.write("\n------Trip Generate count is == "+trip);

			}// for end
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {

				if (bfwriter != null)
					bfwriter.close();

				if (writer != null)
					writer.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}

		return null;
	}

	public ArrayList<HistoryDTO> GetAllDeviceLocation(Connection con,
			DB mongoconnection, String parentId, String studentId) {
		ArrayList<DeviceDataDTO> AllDeviceDataList = new ArrayList<>();

		ArrayList<HistoryDTO> AllDeviceLocationDataList = new ArrayList<>();

		try {
			java.sql.CallableStatement ps = con
					.prepareCall("{call GetTrackInfo(?)}");
			ps.setString(1, parentId);
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					DeviceDataDTO dto = new DeviceDataDTO();
					dto.setStudent_id(rs.getString("StudentID"));
					dto.setImei_no(rs.getString("DeviceID"));
					dto.setName("" + rs.getString("Name"));
					dto.setPath(rs.getString("Photo"));
					dto.setExpiary_date(rs.getString("ExpiryDate"));
					dto.setStatus(rs.getString("ActivationStatus"));
					dto.setRemaining_days_to_expire(rs
							.getString("ActivationStatus"));
					dto.setType(rs.getString("Type"));
					AllDeviceDataList.add(dto);

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		// System.err.println("AllDeviceDataList---------------"+AllDeviceDataList.size());

		for (int i = 0; i < AllDeviceDataList.size(); i++) {

			DeviceDataDTO current_device = AllDeviceDataList.get(i);
			String student = AllDeviceDataList.get(i).getStudent_id()
					.toString();

			try {
				if (current_device.getImei_no().length() > 0
						&& Integer.parseInt(current_device.getStatus()) > 0) {
					DBCollection table = mongoconnection
							.getCollection(Common.TABLE_TODAYLOCATION);
					BasicDBObject device_whereQuery = new BasicDBObject();
					device_whereQuery.put("device",
							Long.parseLong(current_device.getImei_no()));

					DBCursor cursor = table.find(device_whereQuery)
							.sort(new BasicDBObject("timestamp", -1)).limit(1);
					cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

					// System.out.println("Cursor Count of u-p11----"+
					// cursor.size() + "  " + device_whereQuery);

					if (cursor.size() != 0) {

						for (DBObject dbObject : cursor) {

							HistoryDTO obj = new HistoryDTO();
							DBObject dbObject_location = (DBObject) dbObject
									.get("location");

							BasicDBList status = (BasicDBList) dbObject
									.get("status");
							/*
							 * obj.setLan_direction(status.get(3).get(
							 * "lon_direction").toString());
							 * obj.setLat_direction
							 * (status.get(4).get("lat_direction").toString());
							 */
							obj.setLan_direction((status.get(2) + "").replace(
									"{ \"lon_direction\" : \"", ""));

							obj.setLan_direction(obj.getLan_direction()
									.replace("\"}", ""));

							obj.setLat_direction((status.get(3) + "").replace(
									"{ \"lat_direction\" : \"", ""));
							obj.setLat_direction(obj.getLat_direction()
									.replace("\"}", ""));

							if (obj.getLat_direction().equalsIgnoreCase("N")
									&& obj.getLan_direction().equalsIgnoreCase(
											"E")) {

								obj.setLan(dbObject_location.get("lon")
										.toString());
								obj.setLat(dbObject_location.get("lat")
										.toString());

								if (student.equalsIgnoreCase(studentId)
										&& !studentId.equals("0"))
									obj.setAddress(Common.GetAddress(
											"" + obj.getLat(),
											"" + obj.getLan()));

							} else if (obj.getLat_direction().equalsIgnoreCase(
									"N")
									&& obj.getLan_direction().equalsIgnoreCase(
											"W")) {

								obj.setLan("-"
										+ dbObject_location.get("lon")
												.toString());
								obj.setLat(dbObject_location.get("lat")
										.toString());
								if (student.equalsIgnoreCase(studentId)
										&& !studentId.equals("0"))
									obj.setAddress(Common.GetAddress(
											"" + obj.getLat(),
											"" + obj.getLan()));

							} else if (obj.getLat_direction().equalsIgnoreCase(
									"S")
									&& obj.getLan_direction().equalsIgnoreCase(
											"E")) {

								obj.setLan(dbObject_location.get("lon")
										.toString());
								obj.setLat("-"
										+ dbObject_location.get("lat")
												.toString());
								if (student.equalsIgnoreCase(studentId)
										&& !studentId.equals("0"))
									obj.setAddress(Common.GetAddress(
											"" + obj.getLat(),
											"" + obj.getLan()));

							} else if (obj.getLat_direction().equalsIgnoreCase(
									"S")
									&& obj.getLan_direction().equalsIgnoreCase(
											"W")) {

								obj.setLan("-"
										+ dbObject_location.get("lon")
												.toString());
								obj.setLat("-"
										+ dbObject_location.get("lat")
												.toString());
								if (student.equalsIgnoreCase(studentId)
										&& !studentId.equals("0"))
									obj.setAddress(Common.GetAddress(
											"" + obj.getLat(),
											"" + obj.getLan()));

							}

							obj.setSpeed(dbObject.get("speed").toString());
							obj.setTimestamp(dbObject.get("timestamp")
									.toString());
							obj.setExpiary_date(current_device
									.getExpiary_date());
							obj.setStatus(current_device.getStatus());
							obj.setType(current_device.getType());
							obj.setPath(current_device.getPath());
							obj.setStudent_id(current_device.getStudent_id());
							obj.setName(current_device.getName());
							AllDeviceLocationDataList.add(obj);
						}
					} else {
						HistoryDTO obj = new HistoryDTO();

						obj.setExpiary_date(current_device
								.getExpiary_date());
						obj.setStatus(current_device.getStatus());
						obj.setType(current_device.getType());
						obj.setPath(current_device.getPath());
						obj.setStudent_id(current_device.getStudent_id());
						obj.setName(current_device.getName());
						AllDeviceLocationDataList.add(obj);

					}

				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return AllDeviceLocationDataList;

	}

	public ArrayList<GeofenceDTO> GetGeofenceHistory(Connection con,
			DB mongoconnection, String device_imei, String startdate,
			String enddate) {

		ArrayList<GeofenceDTO> list = new ArrayList<>();

		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_GEOFENCE);
			// System.out.println("device==============----------"+device_imei+" "+table.getFullName());

			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device", Long.parseLong(device_imei));

			BasicDBObject timestamp_whereQuery = new BasicDBObject("timestamp",
					new BasicDBObject("$gte", Long.parseLong(startdate))
							.append("$lt", Long.parseLong(enddate)));

			BasicDBList And_Milage = new BasicDBList();
			And_Milage.add(timestamp_whereQuery);
			And_Milage.add(device_whereQuery);
			DBObject geofence_query = new BasicDBObject("$and", And_Milage);

			DBCursor cursor = table.find(geofence_query).sort(
					new BasicDBObject("timestamp", 1));
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			// System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+
			// geofence_query);
			long i = 0;
			long total = cursor.count();

			if (cursor.size() != 0) {

				// System.err.println("*-GetGeofenceHistory----COunt---"+cursor.size());

				while (cursor.hasNext()) {

					GeofenceDTO obj = new GeofenceDTO();
					DBObject dbObject = (DBObject) cursor.next();

					BasicDBObject Sourceobj = (BasicDBObject) dbObject
							.get("geo_fence_data");

					BasicDBList status = (BasicDBList) dbObject.get("status");
					obj.setLan_direction((status.get(2) + "").replace(
							"{ \"lon_direction\" : \"", ""));

					obj.setLan_direction(obj.getLan_direction().replace("\"}",
							""));

					obj.setLat_direction((status.get(3) + "").replace(
							"{ \"lat_direction\" : \"", ""));
					obj.setLat_direction(obj.getLat_direction().replace("\"}",
							""));

					if (obj.getLat_direction().equalsIgnoreCase("N")
							&& obj.getLan_direction().equalsIgnoreCase("E")) {

						obj.setLan(Sourceobj.get("lon").toString());
						obj.setLat(Sourceobj.get("lat").toString());

						obj.setAddress(Common.GetAddress("" + obj.getLat(), ""
								+ obj.getLan()));

					} else if (obj.getLat_direction().equalsIgnoreCase("N")
							&& obj.getLan_direction().equalsIgnoreCase("W")) {

						obj.setLan("-" + Sourceobj.get("lon").toString());
						obj.setLat(Sourceobj.get("lat").toString());
						obj.setAddress(Common.GetAddress("" + obj.getLat(), ""
								+ obj.getLan()));

					} else if (obj.getLat_direction().equalsIgnoreCase("S")
							&& obj.getLan_direction().equalsIgnoreCase("E")) {

						obj.setLan(Sourceobj.get("lon").toString());
						obj.setLat("-" + Sourceobj.get("lat").toString());
						obj.setAddress(Common.GetAddress("" + obj.getLat(), ""
								+ obj.getLan()));

					} else if (obj.getLat_direction().equalsIgnoreCase("S")
							&& obj.getLan_direction().equalsIgnoreCase("W")) {

						obj.setLan("-" + Sourceobj.get("lon").toString());
						obj.setLat("-" + Sourceobj.get("lat").toString());
						obj.setAddress(Common.GetAddress("" + obj.getLat(), ""
								+ obj.getLan()));

					}

					obj.setSpeed(dbObject.get("speed").toString());
					obj.setTimestamp(dbObject.get("timestamp").toString());
					obj.setStatus(dbObject.get("geo_fence_status").toString());
					if (obj.getStatus().equalsIgnoreCase(
							"fence_status_not_identified"))
						obj.setStatus("In/Out");

					obj.setDevice(device_imei);

					list.add(obj);

				}

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;

	}

	public ArrayList<AssociatedParentDTO> GetAssociatedParent(Connection con,
			String userId) {

		ArrayList<AssociatedParentDTO> pObj = new ArrayList<AssociatedParentDTO>();
		String Photo = "";
		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetAssociatedParent(?)}");
			ps.setInt(1, Integer.parseInt(userId));

			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					AssociatedParentDTO person = new AssociatedParentDTO();
					person.setAddress("" + rs.getString("Address"));
					person.setMobileNo("" + rs.getString("MobileNo"));
					person.setEmailId("" + rs.getString("EmailID"));
					person.setUserId("" + rs.getString("UserId"));
					person.setName("" + rs.getString("Name"));
					person.setTotalChild("" + rs.getString("TotalChild"));
					pObj.add(person);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("S55555555555"+pObj.toString());
		return pObj;
	}

	public ArrayList<HistoryDTO> GetOptimizedAllDeviceLocation(Connection con,
			DB mongoconnection, String parentId) {
		ArrayList<DeviceDataDTO> AllDeviceDataList = new ArrayList<>();

		ArrayList<HistoryDTO> AllDeviceLocationDataList = new ArrayList<>();

		try {
			java.sql.CallableStatement ps = con
					.prepareCall("{call GetTrackInfo(?)}");
			ps.setString(1, parentId);
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					DeviceDataDTO dto = new DeviceDataDTO();
					dto.setStudent_id(rs.getString("StudentID"));
					dto.setImei_no(rs.getString("DeviceID"));
					dto.setName("" + rs.getString("Name"));
					dto.setPath(rs.getString("Photo"));
					dto.setStatus(rs.getString("ActivationStatus"));
					dto.setType(rs.getString("Type"));
					//if (rs.getInt("ActivationStatus") >= 0) {
						AllDeviceDataList.add(dto);

					//}

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		System.err.println("GetOptimizedAllDeviceLocation---------------"
				+ AllDeviceDataList.size()+"--"+new Gson().toJson(AllDeviceDataList));

		for (int i = 0; i < AllDeviceDataList.size(); i++) {

			DeviceDataDTO current_device = AllDeviceDataList.get(i);
			try {
				if (current_device.getImei_no().length() > 0&&Integer.parseInt(current_device.getStatus())>0) 
				{
					DBCollection table = mongoconnection
							.getCollection(Common.TABLE_TODAYLOCATION);
					BasicDBObject device_whereQuery = new BasicDBObject();
					device_whereQuery.put("device",
							Long.parseLong(current_device.getImei_no()));

					DBCursor cursor = table.find(device_whereQuery)
							.sort(new BasicDBObject("timestamp", -1)).limit(1);
					cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			//	System.out.println("Cursor Count of u-p11----"+
				//	 cursor.size() + "  " + device_whereQuery);

					if (cursor.size() != 0) {

						for (DBObject dbObject : cursor) {

							HistoryDTO obj = new HistoryDTO();
							DBObject dbObject_location = (DBObject) dbObject
									.get("location");

							BasicDBList status = (BasicDBList) dbObject
									.get("status");
							/*
							 * obj.setLan_direction(status.get(3).get(
							 * "lon_direction").toString());
							 * obj.setLat_direction
							 * (status.get(4).get("lat_direction").toString());
							 */
							obj.setLan_direction((status.get(2) + "").replace(
									"{ \"lon_direction\" : \"", ""));

							obj.setLan_direction(obj.getLan_direction()
									.replace("\"}", ""));

							obj.setLat_direction((status.get(3) + "").replace(
									"{ \"lat_direction\" : \"", ""));
							obj.setLat_direction(obj.getLat_direction()
									.replace("\"}", ""));

							if (obj.getLat_direction().equalsIgnoreCase("N")
									&& obj.getLan_direction().equalsIgnoreCase(
											"E")) {

								obj.setLan(dbObject_location.get("lon")
										.toString());
								obj.setLat(dbObject_location.get("lat")
										.toString());

								// obj.setAddress(Common.GetAddress(""+obj.getLat(),
								// ""+obj.getLan()));

							} else if (obj.getLat_direction().equalsIgnoreCase(
									"N")
									&& obj.getLan_direction().equalsIgnoreCase(
											"W")) {

								obj.setLan("-"
										+ dbObject_location.get("lon")
												.toString());
								obj.setLat(dbObject_location.get("lat")
										.toString());
								// obj.setAddress(Common.GetAddress(""+obj.getLat(),
								// ""+obj.getLan()));

							} else if (obj.getLat_direction().equalsIgnoreCase(
									"S")
									&& obj.getLan_direction().equalsIgnoreCase(
											"E")) {

								obj.setLan(dbObject_location.get("lon")
										.toString());
								obj.setLat("-"
										+ dbObject_location.get("lat")
												.toString());
								// obj.setAddress(Common.GetAddress(""+obj.getLat(),
								// ""+obj.getLan()));

							} else if (obj.getLat_direction().equalsIgnoreCase(
									"S")
									&& obj.getLan_direction().equalsIgnoreCase(
											"W")) {

								obj.setLan("-"
										+ dbObject_location.get("lon")
												.toString());
								obj.setLat("-"
										+ dbObject_location.get("lat")
												.toString());
								// obj.setAddress(Common.GetAddress(""+obj.getLat(),
								// ""+obj.getLan()));

							}

							obj.setSpeed(dbObject.get("speed").toString());
							obj.setTimestamp(dbObject.get("timestamp")
									.toString());
							
							System.out.println(dbObject.get("timestamp")+"---"+current_device.getImei_no());
							
							
							obj.setExpiary_date(current_device
									.getExpiary_date());
							obj.setStatus(current_device.getStatus());
							obj.setType(current_device.getType());
							obj.setPath(current_device.getPath());
							obj.setStudent_id(current_device.getStudent_id());
							obj.setName(current_device.getName());
							AllDeviceLocationDataList.add(obj);
						}
					} else {
						HistoryDTO obj = new HistoryDTO();

						obj.setExpiary_date(current_device
								.getExpiary_date());
						obj.setStatus(current_device.getStatus());
						obj.setType(current_device.getType());
						obj.setPath(current_device.getPath());
						obj.setStudent_id(current_device.getStudent_id());
						obj.setName(current_device.getName());
						AllDeviceLocationDataList.add(obj);

					}

				}else{
					HistoryDTO obj = new HistoryDTO();

					obj.setExpiary_date(current_device
							.getExpiary_date());
					obj.setStatus(current_device.getStatus());
					obj.setType(current_device.getType());
					obj.setPath(current_device.getPath());
					obj.setStudent_id(current_device.getStudent_id());
					obj.setName(current_device.getName());
					AllDeviceLocationDataList.add(obj);
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

			}
		}
		return AllDeviceLocationDataList;
	}

	private void insert_latestTodaylocation(DB mongoconnection) {
		try {
			JSONArray jarray = new JSONArray(
					"[351608081915485,351608085246366,351608085247091,351608085247380,351608085247398,351608085247406,351608085247414,351608085247422,351608085247489,351608085247497,351608085247596,351608085247604,351608085247612,351608085247885,351608085248248,351608085248339,351608085248347,351608085248354,351608085248362,351608085248370,351608085248826,351608085248867,351608085249329,351608085249931,351608085250053,351608085250350,351608085250368,351608085250376,351608085250418,351608085250426,351608085250434,351608085250855,351608085250939,351608085252083,351608085252091,351608085252117,351608085252133,351608085252448,351608085252745,351608085252810,351608085252844,351608085252851,351608085252869,351608085252893,351608085252919,351608085252935,351608085253750,351608085253792,351608085253826,351608085253859,351608085253933,355488020181042,355488020821176,355488020822476,355488020822807,355488020822903,355488020822947,355488020822949,355488020822951,355488020822953,355488020824724,355488020871628,355488020878788,355488020878978,355488020905392,355488020906090,355488020910630,355488020917695,355488020931164,355488020931188,355488020931204,355488020931220,355488020931224,355488020931250,355488020941882,355488020942385,355488020953646,355488020979837,355488020979839,355488020980231,355488020980240,355488020980597,355488020980603,355488020980645,355488020980650,355488020980651,355488020980660,355488020980667,355488020980810,355488020980863,355488020980938,355488020980945,355488020980947,355488020980948,355488020980949,355488020980951,355488020980992,355488020980993,355488020981008,355488020981009,355488020981016,355488020981019,355488020981293,358511020000729,358511020003079,358511020003087,358511020003095,358511020003103,358511020003111,358511020003137,358511020003145,358511020003152,358511020003178,358511020003236,358511020003251,358511020003285,358511020003293,358511020003327,358511020003343,358511020003350,358511020003459,358511020003483,358511020003517,358739052101046,358740050206704,358740050508430,358740050508786,358740050509701,358740050514826,865205030028947,865205030028962,865205030028970,865205030028988,865205030028996,865205030029028,865205030029036,865205030029051,865205030029077,865205030029085,865205030029093,865205030029101,865205030029119,865205030029150,865205030029176,865205030029184,865205030029192,865205030029242,865205030029259,865205030029267,865205030029283,865205030029291,865205030029317,865205030029325,865205030029333,865205030029366,865205030029374,865205030029382,865205030029390,865205030029408,865205030029416,865205030029424,865205030057995,865205030696354,865205030696396,865205030696404,865205030696453,865205030697295,865205030699440,865205030699523,865205030699895,865205030700149,865205030700164,865205030700198,865205030700420,865205030700602,865205031121436,865205031121469,865205031121725,865205031121733,865205031121741,865205031121949,865205031121998,865205031122012,865205031122277,865205031122301,865205031122327,865205031122558,865205031122574,865205031122665,865205031122673,865205031122681,865205031122731,865205031275505,865205031276388,865205031625816,865205031625931]");

			for (int i = 0; i < jarray.length(); i++) {
				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_LOCATION);
				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device", jarray.getLong(i));

				DBCursor cursor = table.find(device_whereQuery)
						.sort(new BasicDBObject("timestamp", -1)).limit(1);
				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				// System.out.println("Cursor Count of u-p11----"+ cursor.size()
				// + "  " + device_whereQuery);

				DBCollection today_table = mongoconnection
						.getCollection(Common.TABLE_TODAYLOCATION);
				if (cursor.size() != 0) {

					for (DBObject dbObject : cursor) {

						HistoryDTO obj = new HistoryDTO();
						DBObject dbObject_location = (DBObject) dbObject
								.get("location");

						today_table.insert(dbObject);
						// today_table.update(device_whereQuery,
						// dbObject_location,true,false);

					}
				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public MessageObject SetBlindLocation(DB mongoconnection, String devices) {

		MessageObject msgo = new MessageObject();
		String[] device_array = devices.split(",");
		// System.out.println("SetBlindLocation------"+device_array);
		try {

			for (int id = 0; id < device_array.length; id++) {
				System.out.println("SetBlindLocation------" + device_array[id]);

				HistoryDistance = 0;

				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_DEVICE);

				BasicDBObject newDocument = new BasicDBObject();
				newDocument.append("$set",
						new BasicDBObject().append("blind", true));

				BasicDBObject searchQuery = new BasicDBObject().append(
						"device", Long.parseLong(device_array[id].trim()));

				table.update(searchQuery, newDocument);
				// //System.out.println("SetBlindLocation------"+searchQuery+newDocument);

			}

			msgo.setError("false");
			msgo.setMessage("Task added successfully.");

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("Task not added successfully");
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("Task not added successfully");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("Task not added successfully");
		}

		return msgo;
	}

	public MessageObject addMultipleDeviceFromText(Connection con,
			String devices, String simno, String parentId, String name, int startCount) {

		MessageObject msgo = new MessageObject();
		String[] device_array = devices.split(",");
		String[] sim_array = simno.split(",");
		DecimalFormat formatter = new DecimalFormat("000");
		String Photo = "";
		try {

			for (int i = 0; i < device_array.length; i++) {

				java.sql.CallableStatement ps = con
						.prepareCall("{call SaveAddDevice(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
				ps.setInt(1, 0);
				ps.setInt(2, Integer.parseInt(parentId));
				ps.setString(3, name);
				ps.setString(4, "-" + formatter.format(i+startCount));
				ps.setString(5, "M");

				ps.setString(6, device_array[i].trim());
				ps.setString(7, sim_array[i].trim());
				ps.setString(8, "~/Images/StudentPhoto/Emp_One.png");
				ps.setString(9, "Child");
				ps.setInt(10, 1);
				ps.setString(11, sim_array[i].trim());
				ps.setString(12, "2020-08-18 00:00:00.000");
				ps.setInt(13, 1);
				ps.setInt(14, 2);
				ps.setString(15, "Western RailWays");

				ps.setString(16, "2020-08-18 00:00:00.000");
				ps.setString(17, "Bank transfer");
				ps.setDouble(18, 0.0);

				int result = ps.executeUpdate();

			}

			msgo.setError("false");
			msgo.setMessage("Task added successfully.");

		} catch (Exception e) {
			e.printStackTrace();

			msgo.setError("true");
			msgo.setMessage("Task not added successfully");
		}

		// System.out.println("S55555555555"+msgo.toString());
		return msgo;
	}

	public MessageObject assigndeviceToMongodb(Connection con,
			DB mongoconnection, String parentId) {
		MessageObject msgo = new MessageObject();
		ArrayList<mongoDeviceObj> deviceList = new ArrayList<>();
		/*
		 * try[],[],[] {
		 */ResultSet rs = null;
		// Made changes in select for email;
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("select ParentID,StudentID,DeviceID from StudentMaster where ParentID= ?");

			ps.setString(1, parentId);
			rs = ps.executeQuery();
			while (rs.next()) {
				mongoDeviceObj msgObj = new mongoDeviceObj();
				msgObj.setParentId(rs.getInt("ParentID"));
				msgObj.setStudentId(rs.getInt("StudentID"));
				msgObj.setDeviceId(rs.getString("DeviceID"));
				deviceList.add(msgObj);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("------device---size-------"+deviceList.size());

		try {

			for (int id = 0; id < deviceList.size(); id++) {

				System.out.println("------device----------" + id + "-------"
						+ deviceList.get(id).getDeviceId());

				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_DEVICE);

				BasicDBObject newDocument = new BasicDBObject();
				newDocument.append(
						"$set",
						new BasicDBObject()
								.append("student",
										deviceList.get(id).getStudentId())
								.append("parent",
										deviceList.get(id).getParentId())
								.append("blind", true));

				BasicDBObject searchQuery = new BasicDBObject()
						.append("device",
								Long.parseLong(deviceList.get(id).getDeviceId()
										.trim()));

				//table.update(searchQuery, newDocument);
				table.update(
					    searchQuery, 
					    newDocument, true, false);
				System.out.println("SetBlindLocation------" + searchQuery
						+ newDocument);

			}

			msgo.setError("false");
			msgo.setMessage("Task added successfully.");

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("Task not added successfully");
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("Task not added successfully");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("Task not added successfully");
		}

		return msgo;
	}

	public MessageObject makePaymentToDevice(Connection con,
			String startStudentId, String endStudentId) {
		MessageObject msgo = new MessageObject();
		ArrayList<mongoDeviceObj> deviceList = new ArrayList<>();
		/*
		 * try[],[],[] {
		 */ResultSet rs = null;
		// Made changes in select for email;
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("select ParentID,StudentID,DeviceID from StudentMaster  where StudentID>=? and StudentID<=?");

			ps.setInt(1, Integer.parseInt(startStudentId));
			ps.setInt(2, Integer.parseInt(endStudentId));

			rs = ps.executeQuery();
			while (rs.next()) {
				mongoDeviceObj msgObj = new mongoDeviceObj();
				msgObj.setParentId(rs.getInt("ParentID"));
				msgObj.setStudentId(rs.getInt("StudentID"));
				msgObj.setDeviceId(rs.getString("DeviceID").trim());
				deviceList.add(msgObj);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("------device---size-------"+deviceList.size());

		try {

			for (int i = 0; i < deviceList.size(); i++) {

				java.sql.CallableStatement psaddDevice = con
						.prepareCall("{call SaveDevicePayment(?,?,?,?,?,?,?,?)}");
				psaddDevice.setInt(1, 1);
				psaddDevice.setInt(2, deviceList.get(i).getStudentId());
				psaddDevice.setInt(3, 1);
				psaddDevice.setString(4, "00000000");
				psaddDevice.setInt(5, 0);
			//	psaddDevice.setInt(6, 7);// FRee for quater
				psaddDevice.setInt(6, 5);//FRee for Year

				// psaddDevice.setInt(6, 6); // free for month
				psaddDevice.setString(7, "No Payment");
				psaddDevice.setInt(8, 0);

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

	/*
	 * public MessageObject addTrainAddress(DB mongoconnection, String filename,
	 * String railWay, String division, String station_From, String station_TO,
	 * String chainage, String trolley, String line, String mode, String
	 * kiloMeter, String distance, String latitude, String longitude, String
	 * feature_Code, String feature_Detail, String ParentId) {
	 * 
	 * // TODO Auto-generated method stub String[]
	 * kiloMeter_array=kiloMeter.split(","); String[] distance_array=
	 * distance.split(","); String[] latitude_array=latitude.split(",");
	 * String[] longitude_array=longitude.split(","); String[]
	 * feature_Code_array=feature_Code.split(","); String[]
	 * feature_Detail_array=feature_Detail.split(","); String[]
	 * ParentId_array=ParentId.split(","); String
	 * Size=kiloMeter_array.length+"--"
	 * +distance_array.length+"--"+latitude_array
	 * .length+"--"+longitude_array.length+"--"+
	 * feature_Code_array.length+"--"+feature_Detail_array
	 * .length+"--"+ParentId_array.length+"--";
	 * 
	 * //System.err.println(Size);
	 * 
	 * MessageObject msgo=new MessageObject();
	 * 
	 * BasicDBList parentDbList=new BasicDBList();
	 * 
	 * for (int i = 0; i < ParentId_array.length; i++) {
	 * parentDbList.add(Integer.parseInt(ParentId_array[i].trim())); }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * try {
	 * 
	 * 
	 * BasicDBList addres_list = new BasicDBList();
	 * 
	 * DBCollection table =
	 * mongoconnection.getCollection(Common.TABLE_RAILADDRESS);
	 * 
	 * BasicDBObject Maindocument = new BasicDBObject(); ObjectId objid = new
	 * ObjectId(); Maindocument.put("address_id","" + objid);
	 * Maindocument.put("filename",filename.trim());
	 * Maindocument.put("ParentId", parentDbList);
	 * 
	 * 
	 * Maindocument.put("railway",railWay.trim());
	 * Maindocument.put("division",division.trim());
	 * Maindocument.put("station_from",station_From.trim());
	 * Maindocument.put("station_to",station_TO.trim());
	 * Maindocument.put("chainage",chainage.trim());
	 * Maindocument.put("trolley",trolley.trim());
	 * Maindocument.put("line",line.trim());
	 * Maindocument.put("mode",mode.trim());
	 * station_From=station_From.substring(station_From.indexOf("[") + 1);
	 * station_From=station_From.substring(0, station_From.indexOf("]"));
	 * 
	 * station_TO=station_TO.substring(station_TO.indexOf("[") + 1);
	 * station_TO=station_TO.substring(0, station_TO.indexOf("]")); for(int
	 * i=0;i<kiloMeter_array.length;i++){ BasicDBObject addresDocument = new
	 * BasicDBObject();
	 * addresDocument.put("kilometer",kiloMeter_array[i].trim());
	 * addresDocument.put("distance",distance_array[i].trim());
	 * addresDocument.put("latitude",latitude_array[i].trim());
	 * addresDocument.put("longitude",longitude_array[i].trim());
	 * addresDocument.put("feature_code",feature_Code_array[i].trim());
	 * 
	 * addresDocument.put("feature_detail",feature_Detail_array[i].trim().replace
	 * ("$", ",")); if (feature_Code_array[i].trim().equals("12"))
	 * addresDocument
	 * .put("feature_image","~/Images/FeatureCodePhoto/fc_12.png"); else if
	 * (feature_Code_array[i].trim().equals("150"))
	 * addresDocument.put("feature_image"
	 * ,"~/Images/FeatureCodePhoto/fc_150.png"); else if
	 * (feature_Code_array[i].trim().equals("4"))
	 * addresDocument.put("feature_image"
	 * ,"~/Images/FeatureCodePhoto/fc__4.png"); else if
	 * (feature_Code_array[i].trim().equals("11"))
	 * addresDocument.put("feature_image"
	 * ,"~/Images/FeatureCodePhoto/fc_11.png"); else if
	 * (feature_Code_array[i].trim().equals("2"))
	 * addresDocument.put("feature_image","~/Images/FeatureCodePhoto/fc_2.png");
	 * else if (feature_Code_array[i].trim().equals("24"))
	 * addresDocument.put("feature_image"
	 * ,"~/Images/FeatureCodePhoto/fc_24.png"); else
	 * addresDocument.put("feature_image"
	 * ,"~/Images/FeatureCodePhoto/ic_default_featurecode.png");
	 * 
	 * 
	 * 
	 * 
	 * addresDocument.put("section",station_From+"-"+station_TO);
	 * 
	 * addresDocument.put("block_section","");
	 * 
	 * 
	 * 
	 * //addresDocument.put("remark",remark_array[i].trim());
	 * addres_list.add(addresDocument);
	 * 
	 * } Maindocument.put("address_details",addres_list);
	 * 
	 * 
	 * table.insert(Maindocument);
	 * 
	 * msgo.setError("false");
	 * msgo.setMessage("Address added successfully.---"+Size);
	 * 
	 * } catch (NumberFormatException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); msgo.setError("true");
	 * msgo.setMessage("address_details not added successfully.---"+Size); }
	 * catch (MongoException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); msgo.setError("true");
	 * msgo.setMessage("address_details not added successfully.---"+Size);
	 * 
	 * }catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); msgo.setError("true");
	 * msgo.setMessage("address_details not added successfully.---"+Size); }
	 * return msgo;
	 * 
	 * }
	 */

	// Add address of jaipur

	public MessageObject addTrainAddress(DB mongoconnection, String filename,
			String railWay, String division, String station_From,
			String station_TO, String chainage, String trolley, String line,
			String mode, String kiloMeter, String distance, String latitude,
			String longitude, String feature_Code, String feature_Detail,
			String ParentId, String section) {
		// TODO Auto-generated method stub
		String[] kiloMeter_array = kiloMeter.split(",");
		String[] distance_array = distance.split(",");
		String[] latitude_array = latitude.split(",");
		String[] longitude_array = longitude.split(",");
		String[] feature_Code_array = feature_Code.split(",");
		String[] feature_Detail_array = feature_Detail.split(",");
		String[] ParentId_array = ParentId.split(",");
		String[] section_array = section.split(",");

		String Size = kiloMeter_array.length + "--" + distance_array.length
				+ "--" + latitude_array.length + "--" + longitude_array.length
				+ "--" + feature_Code_array.length + "--"
				+ feature_Detail_array.length + "--" + ParentId_array.length
				+ "--" + section_array.length;

		// System.err.println(Size);

		MessageObject msgo = new MessageObject();

		BasicDBList parentDbList = new BasicDBList();

		for (int i = 0; i < ParentId_array.length; i++) {
			parentDbList.add(Integer.parseInt(ParentId_array[i].trim()));
		}

		try {

			BasicDBList addres_list = new BasicDBList();

			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_RAILADDRESS);

			BasicDBObject Maindocument = new BasicDBObject();
			ObjectId objid = new ObjectId();
			Maindocument.put("address_id", "" + objid);
			Maindocument.put("filename", "NA");
			Maindocument.put("ParentId", parentDbList);
			Maindocument.put("railway", railWay.trim());
			Maindocument.put("division", division.trim());
			Maindocument.put("station_from", "");
			Maindocument.put("station_to", "");
			Maindocument.put("chainage", chainage);
			Maindocument.put("trolley", "");
			Maindocument.put("line", line);
			Maindocument.put("mode", "");
			Maindocument.put("section", trolley);

			for (int i = 0; i < kiloMeter_array.length; i++) {
				System.err.println("-------------" + i + "-----"
						+ Double.parseDouble(distance_array[i].trim()));
				BasicDBObject addresDocument = new BasicDBObject();
				addresDocument.put("kilometer", kiloMeter_array[i].trim());
				addresDocument.put("distance", distance_array[i].trim());
				addresDocument.put("latitude", latitude_array[i].trim());
				addresDocument.put("longitude", longitude_array[i].trim());
				// addresDocument.put("feature_code",feature_Code_array[i].trim());

				addresDocument.put("feature_detail", feature_Detail_array[i]
						.trim().replace("$", ","));

				if (feature_Detail_array[i].trim().toLowerCase()
						.contains("station")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_12.png");
					addresDocument.put("feature_code", "12");

				} else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("Keyman beat")
						|| feature_Code_array[i].trim().equals("120")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_120.png");
					addresDocument.put("feature_code", "120");

				}else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("rub/lhs")
						|| feature_Code_array[i].trim().equals("107")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_107.png");
					addresDocument.put("feature_code", "107");

				}else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("rub/lhs")
						|| feature_Code_array[i].trim().equals("107")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_107.png");
					addresDocument.put("feature_code", "107");

				} else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("minor bridge")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_113.png");
					addresDocument.put("feature_code", "113");

				} else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("bridge")
						|| feature_Code_array[i].trim().equals("42")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_42.png");
					addresDocument.put("feature_code", "42");

				} else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("gang/keyman")
						|| feature_Code_array[i].trim().equals("101")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_101.png");
					addresDocument.put("feature_code", "101");
				} else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("km start")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_150.png");
					addresDocument.put("feature_code", "150");

				} else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("new km")
						&& Double.parseDouble(distance_array[i].trim()) == 0) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_150.png");
					addresDocument.put("feature_code", "150");

				} /*
				 * else if (feature_Detail_array[i].trim().contains("KM") ||
				 * feature_Code_array[i].trim().equals("150")) {
				 * addresDocument.put("feature_image",
				 * "~/Images/FeatureCodePhoto/fc_150.png");
				 * addresDocument.put("feature_code", "150");
				 * 
				 * }
				 */else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("level")
						|| feature_Code_array[i].trim().equals("62")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc__4.png");
					addresDocument.put("feature_code", "4");

				}

				/*
				 * else if
				 * (feature_Detail_array[i].trim().contains("")||feature_Code_array
				 * [i].trim().equals("32")) {
				 * addresDocument.put("feature_image",
				 * "~/Images/FeatureCodePhoto/fc_11.png");
				 * addresDocument.put("feature_code","");
				 * 
				 * } else if
				 * (feature_Detail_array[i].trim().contains("")||feature_Code_array
				 * [i].trim().equals("2")) { addresDocument.put("feature_image",
				 * "~/Images/FeatureCodePhoto/fc_2.png");
				 * addresDocument.put("feature_code","2");
				 * 
				 * } else if
				 * (feature_Detail_array[i].trim().contains("")||feature_Code_array
				 * [i].trim().equals("24")) {
				 * addresDocument.put("feature_image",
				 * "~/Images/FeatureCodePhoto/fc_24.png");
				 * addresDocument.put("feature_code","24");
				 * 
				 * }
				 */
				else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("circular")
						|| feature_Code_array[i].trim().equals("102")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_102.png");
					addresDocument.put("feature_code", "102");

				} else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("curve")
						|| feature_Code_array[i].trim().equals("102")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_102.png");
					addresDocument.put("feature_code", "102");

				} else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("gang")
						|| feature_Code_array[i].trim().equals("103")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_103.png");
					addresDocument.put("feature_code", "103");

				} else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("switch")
						|| feature_Code_array[i].trim().equals("104")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_104.png");
					addresDocument.put("feature_code", "104");

				} else if (feature_Detail_array[i].trim().contains(
						"MISCELLANEOUS")
						|| feature_Code_array[i].trim().equals("105")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_105.png");
					addresDocument.put("feature_code", "105");

				} else if (feature_Detail_array[i].trim().contains(
						"Siding/Loop")
						|| feature_Code_array[i].trim().equals("106")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_106.png");
					addresDocument.put("feature_code", "106");

				} else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("section")
						|| feature_Code_array[i].trim().equals("108")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_108.png");
					addresDocument.put("feature_code", "108");

				} else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("cabin")
						|| feature_Code_array[i].trim().equals("109")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_109.png");
					addresDocument.put("feature_code", "109");

				} else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("fob")
						|| feature_Code_array[i].trim().equals("110")) {
					addresDocument
							.put("feature_image",
									"~/Images/FeatureCodePhoto/ic_default_featurecode.png");
					addresDocument.put("feature_code", "110");

				} else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("joint")
						|| feature_Code_array[i].trim().equals("112")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_112.png");
					addresDocument.put("feature_code", "112");

				} else if (feature_Detail_array[i].trim().toLowerCase()
						.contains("sej")
						|| feature_Code_array[i].trim().equals("115")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_115.png");
					addresDocument.put("feature_code", "115");

				} else {
					addresDocument
							.put("feature_image",
									"~/Images/FeatureCodePhoto/ic_default_featurecode.png");
					addresDocument.put("feature_code", "999");

				}

				addresDocument.put("section", section_array[i]);

				addresDocument.put("block_section", "");

				// addresDocument.put("remark",remark_array[i].trim());
				addres_list.add(addresDocument);

			}
			System.err.println("---------addres_list----" + addres_list.size());
			Maindocument.put("address_details", addres_list);

			table.insert(Maindocument);

			msgo.setError("false");
			msgo.setMessage("Address added successfully.---" + Size);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("address_details not added successfully.---" + Size);
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("address_details not added successfully.---" + Size);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("address_details not added successfully.---" + Size);
		}
		return msgo;

	}

	public ArrayList<RailWayAddressDTO> GetFeatureAddress(Connection con, DB mongoconnection,
			String parentId) {

		ArrayList<RailWayAddressDTO> RailAddresslist = new ArrayList<RailWayAddressDTO>();
		MessageObject msg = new MessageObject();
		String featureCodeToShow="";
		
		
		try {
			java.sql.CallableStatement ps = con
					.prepareCall("{call GetMappedFeatureCode(?)}");
			ps.setString(1, parentId);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					featureCodeToShow= rs.getString("FeatureCodeToShow");
				
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		DBCollection table = mongoconnection
				.getCollection(Common.TABLE_RAILADDRESS);
		System.out.println("parentId====GetFeatureAddress==========----------"
				+ parentId + " " + table.getFullName()+"---featureCodeToShow=="+featureCodeToShow);

		
		List<String>featureCodeToShow_array=Arrays.asList(featureCodeToShow.split(","));

		System.out.println("====featureCodeToShow_array==========----------"+
		new Gson().toJson(featureCodeToShow_array));
		
		
		BasicDBObject device_whereQuery = new BasicDBObject();
		device_whereQuery.put("ParentId", Integer.parseInt(parentId));
		DBCursor cursor = table.find(device_whereQuery);
		cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
		// System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+device_whereQuery);
		long i = 0;
		long total = cursor.count();

		if (cursor.size() != 0) {
			// System.err.println("*-----COunt---"+cursor.size());
			while (cursor.hasNext()) {
				RailWayAddressDTO obj = new RailWayAddressDTO();
				ArrayList<FeatureAddressDetailsDTO> FeatureAddressDetailslist = new ArrayList<FeatureAddressDetailsDTO>();
				BasicDBObject dbObject = (BasicDBObject) cursor.next();

			
				obj.setAddressId(dbObject.getString("address_id"));
				obj.setChainage(dbObject.getString("chainage"));
				obj.setDivision(dbObject.getString("division"));
				obj.setFileName(dbObject.getString("filename"));
				obj.setLine(dbObject.getString("line"));
				obj.setMode(dbObject.getString("mode"));
				obj.setRailWay(dbObject.getString("railway"));
				obj.setStationFrom(dbObject.getString("station_from"));
				obj.setStationTo(dbObject.getString("station_to"));
				obj.setTrolley(dbObject.getString("trolley"));

				BasicDBList dbAddressList = (BasicDBList) dbObject
						.get("address_details");
				System.err.println("Get dbAddressList size=="+dbAddressList.size());

				for (int j = 0; j < dbAddressList.size(); j++) {
					FeatureAddressDetailsDTO addressDto = new FeatureAddressDetailsDTO();

					BasicDBObject dbAddressObject = (BasicDBObject) dbAddressList
							.get(j);
					addressDto.setDistance(dbAddressObject
							.getString("distance") + "");
					addressDto.setFeature_image(dbAddressObject
							.getString("feature_image"));
					addressDto.setFeatureCode(dbAddressObject
							.getString("feature_code") + "");
					addressDto.setFeatureDetail(dbAddressObject
							.getString("feature_detail"));
					addressDto.setKiloMeter(dbAddressObject
							.getString("kilometer") + "");
					addressDto.setLatitude(dbAddressObject
							.getString("latitude") + "");
					addressDto.setLongitude(dbAddressObject
							.getString("longitude") + "");
					addressDto.setSection(dbAddressObject.getString("section")
							+ "");
					addressDto.setBlockSection(dbAddressObject
							.getString("block_section") + "");
					if(featureCodeToShow.length()>0)
					{
						if (dbAddressObject.getString("feature_code") != null&&
								featureCodeToShow_array.contains(dbAddressObject.getString("feature_code"))) 
						{
							FeatureAddressDetailslist.add(addressDto);

						}
						
					}else{
						if (dbAddressObject.getString("feature_code") != null&&
								!dbAddressObject.getString("feature_code").equalsIgnoreCase("999")) 
						FeatureAddressDetailslist.add(addressDto);

					}
					
/*
					if ((Integer.parseInt(parentId) == 534981||Integer.parseInt(parentId) == 545349)
							&& (dbAddressObject.getString("feature_code") != null)
							&& (dbAddressObject.getString("feature_code")
									.equalsIgnoreCase("12")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("150")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("4")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("42")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("101")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("103")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("107")

									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase(" ") 
											
									|| dbAddressObject
									.getString("feature_code")
									.equalsIgnoreCase("109")
									
									||dbAddressObject
									.getString("feature_code")
									.equalsIgnoreCase("120")
									||dbAddressObject
									.getString("feature_code")
									.equalsIgnoreCase("121"))
					)
									
								
					{
						FeatureAddressDetailslist.add(addressDto);
						
					}
					else if ((Integer.parseInt(parentId) != 534981)
							&& dbAddressObject.getString("feature_code") != null
							&& (dbAddressObject.getString("feature_code")
									.equalsIgnoreCase("12")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("150")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("4")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("11")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("2")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("24")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("24")

									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("101")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("102")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("103")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("104")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("105")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("106")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("107")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("108")
									|| dbAddressObject
											.getString("feature_code")
											.equalsIgnoreCase("109") 
								
									|| dbAddressObject
									.getString("feature_code")
									.equalsIgnoreCase("999")
									
									||dbAddressObject
									.getString("feature_code")
									.equalsIgnoreCase("120")
									
									||dbAddressObject
									.getString("feature_code")
									.equalsIgnoreCase("121")
									)
									
							) {
						FeatureAddressDetailslist.add(addressDto);

					}
*/
				}

				obj.setFeatureAddressDetail(FeatureAddressDetailslist);
				RailAddresslist.add(obj);

			}
		}

		// System.err.println("Get Triipo Report=="+msg.getMessage());

		return RailAddresslist;

	}

	public ArrayList<RailWayAddressDTO> GetKmFeatureAddress(DB mongoconnection,
			String parentId) {

		ArrayList<RailWayAddressDTO> RailAddresslist = new ArrayList<RailWayAddressDTO>();
		MessageObject msg = new MessageObject();
		DBCollection table = mongoconnection
				.getCollection(Common.TABLE_RAILADDRESS);
		// System.out.println("parentId===GetKmFeatureAddress==========----------"+parentId+" "+table.getFullName());

		BasicDBObject device_whereQuery = new BasicDBObject();
		device_whereQuery.put("ParentId", Integer.parseInt(parentId));
		DBCursor cursor = table.find(device_whereQuery).limit(200);
		;
		cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

		// System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+device_whereQuery);
		long i = 0;
		long total = cursor.count();

		if (cursor.size() != 0) {

			// //System.err.println("*--GetKmFeatureAddress---COunt---"+cursor.size());

			while (cursor.hasNext()) {

				RailWayAddressDTO obj = new RailWayAddressDTO();
				ArrayList<FeatureAddressDetailsDTO> FeatureAddressDetailslist = new ArrayList<FeatureAddressDetailsDTO>();

				BasicDBObject dbObject = (BasicDBObject) cursor.next();

				/*
				 * ;; BasicDBObject device_whereQuery = new BasicDBObject();
				 * device_whereQuery.put("ParentId",Integer.parseInt(parentId));
				 * 
				 * BasicDBList And_Milage = new BasicDBList();
				 * And_Milage.add(device_whereQuery);
				 * And_Milage.add(feature_whereQuery);
				 * 
				 * BasicDBObject feature_whereQuery
				 * 
				 * BasicDBList or_feature_code = new BasicDBList();
				 * 
				 * or_feature_code.add(new
				 * BasicDBObject().put("ParentId",Integer.parseInt(parentId))
				 * DBObject geofence_query = new BasicDBObject("$and",
				 * And_Milage);
				 */

				obj.setAddressId(dbObject.getString("address_id"));
				obj.setChainage(dbObject.getString("chainage"));
				obj.setDivision(dbObject.getString("division"));
				obj.setFileName(dbObject.getString("filename"));
				obj.setLine(dbObject.getString("line"));
				obj.setMode(dbObject.getString("mode"));
				obj.setRailWay(dbObject.getString("railway"));
				obj.setStationFrom(dbObject.getString("station_from"));
				obj.setStationTo(dbObject.getString("station_to"));
				obj.setTrolley(dbObject.getString("trolley"));

				BasicDBList dbAddressList = (BasicDBList) dbObject
						.get("address_details");
				// System.err.println("Get dbAddressList size=="+dbAddressList.size());

				for (int j = 0; j < dbAddressList.size(); j++) {
					FeatureAddressDetailsDTO addressDto = new FeatureAddressDetailsDTO();

					BasicDBObject dbAddressObject = (BasicDBObject) dbAddressList
							.get(j);
					addressDto.setDistance(dbAddressObject
							.getString("distance"));
					addressDto.setFeature_image(dbAddressObject
							.getString("feature_image"));
					addressDto.setFeatureCode(dbAddressObject
							.getString("feature_code"));
					addressDto.setFeatureDetail(dbAddressObject
							.getString("feature_detail"));
					addressDto.setKiloMeter(dbAddressObject
							.getString("kilometer"));
					addressDto.setLatitude(dbAddressObject
							.getString("latitude"));
					addressDto.setLongitude(dbAddressObject
							.getString("longitude"));
					addressDto.setSection(dbAddressObject.getString("section"));
					addressDto.setBlockSection(dbAddressObject
							.getString("block_section"));

					if (dbAddressObject.getString("feature_code")
							.equalsIgnoreCase("")) {
						FeatureAddressDetailslist.add(addressDto);

					}

				}

				obj.setFeatureAddressDetail(FeatureAddressDetailslist);
				RailAddresslist.add(obj);

			}
		}

		// System.err.println("Get GetKmFeatureAddress Report=="+msg.getMessage()+"--"+RailAddresslist.size());

		return RailAddresslist;

	}

	/*
	 * 
	 * for Ranchi public MessageObject addRanchiTrainAddress(DB mongoconnection,
	 * String railWay, String division, String section, String latitude, String
	 * longitude, String feature_Code, String feature_Detail, String parentId) {
	 * // TODO Auto-generated method stub String[]
	 * section_array=section.split(","); String[]
	 * latitude_array=latitude.split(","); String[]
	 * longitude_array=longitude.split(","); String[]
	 * feature_Code_array=feature_Code.split(","); String[]
	 * feature_Detail_array=feature_Detail.split(","); String[]
	 * ParentId_array=parentId.split(","); String
	 * Size=division+"--"+section_array
	 * .length+"--"+latitude_array.length+"--"+longitude_array.length+"--"+
	 * feature_Code_array
	 * .length+"--"+feature_Detail_array.length+"--"+ParentId_array.length+"--";
	 * 
	 * //System.err.println(Size);
	 * 
	 * MessageObject msgo=new MessageObject();
	 * 
	 * BasicDBList parentDbList=new BasicDBList();
	 * 
	 * for (int i = 0; i < ParentId_array.length; i++) {
	 * parentDbList.add(Integer.parseInt(ParentId_array[i].trim())); }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * try {
	 * 
	 * 
	 * BasicDBList addres_list = new BasicDBList();
	 * 
	 * DBCollection table =
	 * mongoconnection.getCollection(Common.TABLE_RAILADDRESS);
	 * 
	 * BasicDBObject Maindocument = new BasicDBObject(); ObjectId objid = new
	 * ObjectId(); Maindocument.put("address_id","" + objid);
	 * Maindocument.put("filename","NA"); Maindocument.put("ParentId",
	 * parentDbList); Maindocument.put("railway",railWay.trim());
	 * Maindocument.put("division",division.trim());
	 * Maindocument.put("station_from",""); Maindocument.put("station_to","");
	 * Maindocument.put("chainage",""); Maindocument.put("trolley","");
	 * Maindocument.put("line",""); Maindocument.put("mode","");
	 * 
	 * for(int i=0;i<feature_Code_array.length;i++){ BasicDBObject
	 * addresDocument = new BasicDBObject();
	 * addresDocument.put("kilometer","0"); addresDocument.put("distance","0");
	 * addresDocument.put("latitude",latitude_array[i].trim());
	 * addresDocument.put("longitude",longitude_array[i].trim());
	 * addresDocument.put("feature_code",feature_Code_array[i].trim());
	 * 
	 * addresDocument.put("feature_detail",feature_Detail_array[i].trim().replace
	 * ("$", ",")); if (feature_Code_array[i].trim().equals("12"))
	 * addresDocument
	 * .put("feature_image","~/Images/FeatureCodePhoto/fc_12.png"); else if
	 * (feature_Code_array[i].trim().equals("150"))
	 * addresDocument.put("feature_image"
	 * ,"~/Images/FeatureCodePhoto/fc_150.png"); else if
	 * (feature_Code_array[i].trim().equals("4"))
	 * addresDocument.put("feature_image"
	 * ,"~/Images/FeatureCodePhoto/fc__4.png"); else if
	 * (feature_Code_array[i].trim().equals("11"))
	 * addresDocument.put("feature_image"
	 * ,"~/Images/FeatureCodePhoto/fc_11.png"); else if
	 * (feature_Code_array[i].trim().equals("2"))
	 * addresDocument.put("feature_image","~/Images/FeatureCodePhoto/fc_2.png");
	 * else if (feature_Code_array[i].trim().equals("24"))
	 * addresDocument.put("feature_image"
	 * ,"~/Images/FeatureCodePhoto/fc_24.png"); else
	 * addresDocument.put("feature_image","~/Images/FeatureCodePhoto/fc_2.png");
	 * 
	 * 
	 * 
	 * 
	 * addresDocument.put("section",section);
	 * 
	 * addresDocument.put("block_section","");
	 * 
	 * 
	 * 
	 * //addresDocument.put("remark",remark_array[i].trim());
	 * addres_list.add(addresDocument);
	 * 
	 * } Maindocument.put("address_details",addres_list);
	 * 
	 * 
	 * table.insert(Maindocument);
	 * 
	 * msgo.setError("false");
	 * msgo.setMessage("Address added successfully.---"+Size);
	 * 
	 * } catch (NumberFormatException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); msgo.setError("true");
	 * msgo.setMessage("address_details not added successfully.---"+Size); }
	 * catch (MongoException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); msgo.setError("true");
	 * msgo.setMessage("address_details not added successfully.---"+Size);
	 * 
	 * }catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); msgo.setError("true");
	 * msgo.setMessage("address_details not added successfully.---"+Size); }
	 * return msgo;
	 * 
	 * }
	 */

	// for Ratlam Address

	public MessageObject addRanchiTrainAddress(DB mongoconnection,
			String railWay, String division, String section, String latitude,
			String longitude, String feature_Code, String feature_Detail,
			String parentId) {
		// TODO Auto-generated method stub
		String[] section_array = section.split(",");
		String[] latitude_array = latitude.split(",");
		String[] longitude_array = longitude.split(",");
		// String[] feature_Code_array=feature_Code.split(",");
		String[] feature_Detail_array = feature_Detail.split(",");
		
		String[] ParentId_array = parentId.split(",");
		String Size = division + "--" + section_array.length + "--"
				+ latitude_array.length + "--" + longitude_array.length + "--"
				+ "--" + feature_Detail_array.length + "--"
				+ ParentId_array.length + "--";

		// System.err.println(Size);

		MessageObject msgo = new MessageObject();

		BasicDBList parentDbList = new BasicDBList();

		for (int i = 0; i < ParentId_array.length; i++) {
			parentDbList.add(Integer.parseInt(ParentId_array[i].trim()));
		}

		try {

			BasicDBList addres_list = new BasicDBList();

			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_RAILADDRESS);

			BasicDBObject Maindocument = new BasicDBObject();
			ObjectId objid = new ObjectId();
			Maindocument.put("address_id", "" + objid);
			Maindocument.put("filename", "NA");
			Maindocument.put("ParentId", parentDbList);
			Maindocument.put("railway", railWay.trim());
			Maindocument.put("division", division.trim());
			Maindocument.put("station_from", "");
			Maindocument.put("station_to", "");
			Maindocument.put("chainage", "");
			Maindocument.put("trolley", "");
			Maindocument.put("line", "");
			Maindocument.put("mode", "");

			for (int i = 0; i < latitude_array.length; i++) {
				BasicDBObject addresDocument = new BasicDBObject();
				addresDocument.put("kilometer", "0");
				addresDocument.put("distance", "0");
				addresDocument.put("latitude", latitude_array[i].trim());
				addresDocument.put("longitude", longitude_array[i].trim());
				addresDocument.put("feature_code", "120");

				addresDocument.put("feature_detail", feature_Detail_array[i]
						.trim().replace("$", ","));
				addresDocument.put("feature_image",
						"~/Images/FeatureCodePhoto/fc_120.png");
				/*
				 * if (feature_Code_array[i].trim().equals("12"))
				 * addresDocument.
				 * put("feature_image","~/Images/FeatureCodePhoto/fc_12.png");
				 * else if (feature_Code_array[i].trim().equals("150"))
				 * addresDocument
				 * .put("feature_image","~/Images/FeatureCodePhoto/fc_150.png");
				 * else if (feature_Code_array[i].trim().equals("4"))
				 * addresDocument
				 * .put("feature_image","~/Images/FeatureCodePhoto/fc__4.png");
				 * else if (feature_Code_array[i].trim().equals("11"))
				 * addresDocument
				 * .put("feature_image","~/Images/FeatureCodePhoto/fc_11.png");
				 * else if (feature_Code_array[i].trim().equals("2"))
				 * addresDocument
				 * .put("feature_image","~/Images/FeatureCodePhoto/fc_2.png");
				 * else if (feature_Code_array[i].trim().equals("24"))
				 * addresDocument
				 * .put("feature_image","~/Images/FeatureCodePhoto/fc_24.png");
				 * else addresDocument.put("feature_image",
				 * "~/Images/FeatureCodePhoto/fc_2.png");
				 */

				addresDocument.put("section", section_array[i]);

				addresDocument.put("block_section", "");

				// addresDocument.put("remark",remark_array[i].trim());
				addres_list.add(addresDocument);

			}
			Maindocument.put("address_details", addres_list);

			table.insert(Maindocument);

			msgo.setError("false");
			msgo.setMessage("Address added successfully.---" + Size);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("address_details not added successfully.---" + Size);
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("address_details not added successfully.---" + Size);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("address_details not added successfully.---" + Size);
		}
		return msgo;

	}

	public ArrayList<RailDeviceInfoDto> GenerateGPSHolder10MinData(
			DB mongoconnection, Connection con, String startdate,
			String pastMin, String parentId) {
		ArrayList<RailDeviceInfoDto> UserList = new ArrayList<>();

		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetDeviceRailAddress(?)}");
			ps.setString(1, parentId);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					RailDeviceInfoDto dto = new RailDeviceInfoDto();
					dto.setStudentId(rs.getInt(1));
					dto.setName(rs.getString(2));
					dto.setDeviceID(rs.getString(3));
					UserList.add(dto);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("GenerateGPSHolder10MinData==============----------"
				+ " " + UserList.size());

		for (int i = 0; i < UserList.size(); i++)
			try {
				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_LOCATION);
				// //System.out.println("device==============----------"+" "+table.getFullName());

				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device",
						Long.parseLong(UserList.get(i).getDeviceID()));

				BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp", new BasicDBObject("$lt",
								Long.parseLong(startdate)).append("$gte", (Long
								.parseLong(startdate) - (Integer
								.parseInt(pastMin) * 60))));

				BasicDBList And_Milage = new BasicDBList();
				And_Milage.add(timestamp_whereQuery);
				And_Milage.add(device_whereQuery);
				DBObject query = new BasicDBObject("$and", And_Milage);

				DBCursor cursor = table.find(query)
						.sort(new BasicDBObject("timestamp", -1)).limit(1);
				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				// System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+query);
				// long total = cursor.count();

				if (cursor.size() != 0) {

					//System.err.println("*-GenerateGPSHolder10MinData----COunt---"+cursor.size());

					while (cursor.hasNext()) {

						DBObject dbObject = (DBObject) cursor.next();

						BasicDBObject Sourceobj = (BasicDBObject) dbObject
								.get("location");

						UserList.get(i).setLang(Sourceobj.get("lon") + "");
						UserList.get(i).setLat(Sourceobj.get("lat") + "");

						// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
						// ""+UserList.get(i).getLang()));
						UserList.get(i).setSpeed(dbObject.get("speed") + "");
						UserList.get(i).setTime(dbObject.get("timestamp") + "");
						UserList.get(i).setDeviceOnStatus(1);

						try {
							java.sql.CallableStatement ps = con
									.prepareCall("{call GetFeatureCodeswithDist(?,?,?)}");
							ps.setString(2, Sourceobj.get("lon") + "");
							ps.setString(1, Sourceobj.get("lat") + "");
							ps.setString(3, parentId);

							// System.err.println("yy ------"+UserList.get(i).getDeviceID()+"---"+new Gson().toJson(ps));;

							ResultSet rs = ps.executeQuery();

							if (rs != null) {

								while (rs.next()) {

									FeatureAddressDetailsDTO addressDto = new FeatureAddressDetailsDTO();
									addressDto.setDistance(rs
											.getString("Distance") + "");
									addressDto.setFeatureCode(rs
											.getString("FeatureCode") + "");
									addressDto.setFeature_image(rs
											.getString("Images") + "");
									addressDto.setFeatureDetail(rs
											.getString("FeatureDetail") + "");
									addressDto.setKiloMeter(rs
											.getString("kiloMeter") + "");
									addressDto.setLatitude(rs
											.getString("Latitude") + "");
									addressDto.setLongitude(rs
											.getString("Longitude") + "");
									addressDto.setSection(rs
											.getString("Section") + "");
									addressDto.setBlockSection(rs
											.getString("BlockSection") + "");
									addressDto.setNearByDistance(rs
											.getString("NearByDistance") + "");
									UserList.get(i).setRailFeatureDto(
											addressDto);

								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				} else {
					UserList.get(i).setDeviceOnStatus(0);

					DBCollection toadytable = mongoconnection
							.getCollection(Common.TABLE_TODAYLOCATION);
					BasicDBObject device_toadytablewhereQuery = new BasicDBObject();
					device_toadytablewhereQuery.put("device",
							Long.parseLong(UserList.get(i).getDeviceID()));

					DBCursor cursor_today = toadytable
							.find(device_toadytablewhereQuery)
							.sort(new BasicDBObject("timestamp", -1)).limit(1);
					cursor_today
							.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

					// System.err.println("Else ---query "+i+"==="+device_toadytablewhereQuery);
					// //System.out.println("cursor_today Count of u-p11----"+
					// cursor.size() + "  " + device_whereQuery);

					if (cursor_today.size() != 0) {

						for (DBObject dbObject : cursor_today) {

							DBObject dbObject_location = (DBObject) dbObject
									.get("location");

							UserList.get(i).setLang(
									dbObject_location.get("lon") + "");
							UserList.get(i).setLat(
									dbObject_location.get("lat") + "");
							UserList.get(i)
									.setSpeed(dbObject.get("speed") + "");
							UserList.get(i).setTime(
									dbObject.get("timestamp") + "");

						}
					} else {
						UserList.get(i).setLang("0");
						UserList.get(i).setLat("0");
						UserList.get(i).setSpeed("0");
						UserList.get(i).setTime("0");
					}

				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return UserList;

	}

	public ArrayList<RailDeviceInfoDto> GenerateGPSDeviceOnData(
			DB mongoconnection, Connection con, String startdate,
			String parentId) {
		MessageObject msgo = new MessageObject();
		ArrayList<RailDeviceInfoDto> UserList = new ArrayList<>();

		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetDeviceRailAddress(?)}");
			ps.setString(1, parentId);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					RailDeviceInfoDto dto = new RailDeviceInfoDto();
					dto.setStudentId(rs.getInt(1));
					dto.setName(rs.getString(2));
					dto.setDeviceID(rs.getString(3));

					UserList.add(dto);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < UserList.size(); i++)
			try {
				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_LOCATION);
				// //System.out.println("device==============----------"+" "+table.getFullName());

				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device",
						Long.parseLong(UserList.get(i).getDeviceID()));

				BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp", new BasicDBObject("$gte",
								Long.parseLong(startdate)).append("$lt",
								Long.parseLong(startdate) + 86400));

				BasicDBList And_Milage = new BasicDBList();
				And_Milage.add(timestamp_whereQuery);
				And_Milage.add(device_whereQuery);
				DBObject query = new BasicDBObject("$and", And_Milage);

				DBCursor cursor = table.find(query)
						.sort(new BasicDBObject("timestamp", 1)).limit(1);
				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				// //System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+query);
				long total = cursor.count();

				if (cursor.size() != 0) {

					// //System.err.println("*-GetGeofenceHistory----COunt---"+cursor.size());

					while (cursor.hasNext()) {

						DBObject dbObject = (DBObject) cursor.next();

						BasicDBObject Sourceobj = (BasicDBObject) dbObject
								.get("location");

						BasicDBList status = (BasicDBList) dbObject
								.get("status");
						UserList.get(i).setLan_direction(
								(status.get(2) + "").replace(
										"{ \"lon_direction\" : \"", ""));

						UserList.get(i).setLan_direction(
								UserList.get(i).getLan_direction()
										.replace("\"}", ""));

						UserList.get(i).setLat_direction(
								(status.get(3) + "").replace(
										"{ \"lat_direction\" : \"", ""));
						UserList.get(i).setLat_direction(
								UserList.get(i).getLat_direction()
										.replace("\"}", ""));

						if (UserList.get(i).getLat_direction()
								.equalsIgnoreCase("N")
								&& UserList.get(i).getLan_direction()
										.equalsIgnoreCase("E")) {

							UserList.get(i).setLang(Sourceobj.get("lon") + "");
							UserList.get(i).setLat(Sourceobj.get("lat") + "");
							// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
							// ""+UserList.get(i).getLang()));

						} else if (UserList.get(i).getLat_direction()
								.equalsIgnoreCase("N")
								&& UserList.get(i).getLan_direction()
										.equalsIgnoreCase("W")) {

							UserList.get(i).setLang(
									"-" + Sourceobj.get("lon") + "");
							UserList.get(i).setLat(Sourceobj.get("lat") + "");
							// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
							// ""+UserList.get(i).getLang()));

						} else if (UserList.get(i).getLat_direction()
								.equalsIgnoreCase("S")
								&& UserList.get(i).getLan_direction()
										.equalsIgnoreCase("E")) {

							UserList.get(i).setLang(Sourceobj.get("lon") + "");
							UserList.get(i).setLat(
									"-" + Sourceobj.get("lat") + "");
							// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
							// ""+UserList.get(i).getLang()));

						} else if (UserList.get(i).getLat_direction()
								.equalsIgnoreCase("S")
								&& UserList.get(i).getLan_direction()
										.equalsIgnoreCase("W")) {

							UserList.get(i).setLang(
									"-" + Sourceobj.get("lon") + "");
							UserList.get(i).setLat(
									"-" + Sourceobj.get("lat") + "");
							// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
							// ""+UserList.get(i).getLang()));

						}

						UserList.get(i).setSpeed(dbObject.get("speed") + "");
						UserList.get(i).setTime(dbObject.get("timestamp") + "");
						UserList.get(i).setDeviceOnStatus(1);

					}

				} else {
					UserList.get(i).setDeviceOnStatus(0);

				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return UserList;

	}

	public ArrayList<RailDeviceInfoDto> GenerateGPSDeviceOFFData(
			DB mongoconnection, Connection con, String startdate,
			String parentId) {

		MessageObject msgo = new MessageObject();
		ArrayList<RailDeviceInfoDto> UserList = new ArrayList<>();

		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetDeviceRailAddress(?)}");
			ps.setString(1, parentId);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					RailDeviceInfoDto dto = new RailDeviceInfoDto();
					dto.setStudentId(rs.getInt(1));
					dto.setName(rs.getString(2));
					dto.setDeviceID(rs.getString(3));

					UserList.add(dto);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < UserList.size(); i++)
			try {
				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_LOCATION);
				// System.out.println("device==============----------"+" "+table.getFullName());

				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device",
						Long.parseLong(UserList.get(i).getDeviceID()));

				BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp", new BasicDBObject("$gte",
								Long.parseLong(startdate)).append("$lt",
								Long.parseLong(startdate) + 86400));

				BasicDBList And_Milage = new BasicDBList();
				And_Milage.add(timestamp_whereQuery);
				And_Milage.add(device_whereQuery);
				DBObject query = new BasicDBObject("$and", And_Milage);

				DBCursor cursor = table.find(query)
						.sort(new BasicDBObject("timestamp", -1)).limit(1);
				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				// System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+query);
				long total = cursor.count();

				if (cursor.size() != 0) {

					// System.err.println("*-GetGeofenceHistory----COunt---"+cursor.size());

					while (cursor.hasNext()) {

						DBObject dbObject = (DBObject) cursor.next();

						BasicDBObject Sourceobj = (BasicDBObject) dbObject
								.get("location");

						BasicDBList status = (BasicDBList) dbObject
								.get("status");
						UserList.get(i).setLan_direction(
								(status.get(2) + "").replace(
										"{ \"lon_direction\" : \"", ""));

						UserList.get(i).setLan_direction(
								UserList.get(i).getLan_direction()
										.replace("\"}", ""));

						UserList.get(i).setLat_direction(
								(status.get(3) + "").replace(
										"{ \"lat_direction\" : \"", ""));
						UserList.get(i).setLat_direction(
								UserList.get(i).getLat_direction()
										.replace("\"}", ""));

						if (UserList.get(i).getLat_direction()
								.equalsIgnoreCase("N")
								&& UserList.get(i).getLan_direction()
										.equalsIgnoreCase("E")) {

							UserList.get(i).setLang(Sourceobj.get("lon") + "");
							UserList.get(i).setLat(Sourceobj.get("lat") + "");

							// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
							// ""+UserList.get(i).getLang()));

						} else if (UserList.get(i).getLat_direction()
								.equalsIgnoreCase("N")
								&& UserList.get(i).getLan_direction()
										.equalsIgnoreCase("W")) {

							UserList.get(i).setLang(
									"-" + Sourceobj.get("lon") + "");
							UserList.get(i).setLat(Sourceobj.get("lat") + "");
							// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
							// ""+UserList.get(i).getLang()));

						} else if (UserList.get(i).getLat_direction()
								.equalsIgnoreCase("S")
								&& UserList.get(i).getLan_direction()
										.equalsIgnoreCase("E")) {

							UserList.get(i).setLang(Sourceobj.get("lon") + "");
							UserList.get(i).setLat(
									"-" + Sourceobj.get("lat") + "");
							// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
							// ""+UserList.get(i).getLang()));

						} else if (UserList.get(i).getLat_direction()
								.equalsIgnoreCase("S")
								&& UserList.get(i).getLan_direction()
										.equalsIgnoreCase("W")) {

							UserList.get(i).setLang(
									"-" + Sourceobj.get("lon") + "");
							UserList.get(i).setLat(
									"-" + Sourceobj.get("lat") + "");
							// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
							// ""+UserList.get(i).getLang()));

						}

						UserList.get(i).setSpeed(dbObject.get("speed") + "");
						UserList.get(i).setTime(dbObject.get("timestamp") + "");
						UserList.get(i).setDeviceOnStatus(1);

					}

				} else {
					UserList.get(i).setDeviceOnStatus(0);

				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return UserList;

	}

	public MessageObject ExportRailAddressToSql(DB mongoconnection,
			Connection con, String parentId) {

		MessageObject msgo = new MessageObject();
		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_RAILADDRESS);
			// System.out.println("device==============----------"+" "+table.getFullName());

			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("ParentId", Integer.parseInt(parentId));

			DBCursor cursor = table.find(device_whereQuery);
				//	.sort(new BasicDBObject("timestamp", 1)).limit(500);
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			// System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+device_whereQuery);
			long total = cursor.count();

			if (cursor.size() != 0) {

				System.err.println("*-ExportRailAddressToSql----COunt---"
						+ cursor.size());
				int i = 0;
				while (cursor.hasNext()) {

					DBObject dbObject = (DBObject) cursor.next();

					BasicDBList parentlist = (BasicDBList) dbObject
							.get("ParentId");
					System.err.println("*-ExportRailAddressToSql----COunt---"
							+ cursor.size());

					// int parentId = (int) parentlist.get(0);
					BasicDBList address_details = (BasicDBList) dbObject
							.get("address_details");

					System.err.println("*-address_details----" + i + "---"
							+ address_details.size());

					for (int j = 0; j < address_details.size(); j++) {
						try {
							BasicDBObject Sourceobj = (BasicDBObject) address_details
									.get(j);

							int result = 0;

							if (!Sourceobj.getString("longitude").equals(
									"#VALUE!")
									&& !Sourceobj.getString("longitude")
											.contains("+")) {

								java.sql.CallableStatement ps = con
										.prepareCall("{call SaveRailExportfromMongo(?,?,?,?,?,?,?,?,?)}");
								ps.setDouble(1, Double.parseDouble(Sourceobj
										.getString("kilometer".trim())));
								ps.setDouble(
										2,
										Double.parseDouble(Sourceobj.getString(
												"distance").trim()));
								ps.setDouble(
										3,
										Double.parseDouble(Sourceobj.getString(
												"latitude").trim()));
								ps.setDouble(
										4,
										Double.parseDouble(Sourceobj.getString(
												"longitude").trim()));
								System.err.println("*-feature_code----" + i
										+ "---"
										+ Sourceobj.getString("feature_code"));
								if (Sourceobj.getString("feature_code") != null) {
									ps.setString(5,
											Sourceobj.getString("feature_code")
													.trim());

								} else {
									ps.setString(5, "0");

								}
								ps.setString(6,
										Sourceobj.getString("feature_detail")
												.trim());
								ps.setString(7, Sourceobj.getString("section")
										.trim());
								ps.setString(8,
										Sourceobj.getString("feature_image")
												.trim());
								ps.setInt(9, Integer.parseInt(parentId));

								result = ps.executeUpdate();

								if (result == 0) {
									msgo.setError("true");
									msgo.setMessage("Request is not successfully");
								} else {
									// //System.err.println("Error=="+result);
									msgo.setError("false");
									msgo.setMessage("Request is  successfully");
								}
							}
						} catch (Exception e) {
							msgo.setError("true");
							msgo.setMessage("Error :" + e.getMessage());
							e.printStackTrace();
						}

					}

					i++;

				}

			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgo;
	}

	public MessageObject RenewMultiplePaymentToDevice(Connection con,
			String startStudentId, String endStudentId) {
		MessageObject msgo = new MessageObject();
		ArrayList<mongoDeviceObj> deviceList = new ArrayList<>();
		/*
		 * try[],[],[] {
		 */ResultSet rs = null;
		// Made changes in select for email;
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("select ParentID,StudentID,DeviceID from StudentMaster  where StudentID>=? and StudentID<=?");

			ps.setInt(1, Integer.parseInt(startStudentId));
			ps.setInt(2, Integer.parseInt(endStudentId));

			rs = ps.executeQuery();
			while (rs.next()) {
				mongoDeviceObj msgObj = new mongoDeviceObj();
				msgObj.setParentId(rs.getInt("ParentID"));
				msgObj.setStudentId(rs.getInt("StudentID"));
				msgObj.setDeviceId(rs.getString("DeviceID"));
				deviceList.add(msgObj);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("------device---size-------"+deviceList.size());

		try {

			for (int i = 0; i < deviceList.size(); i++) {

				java.sql.CallableStatement psaddDevice = con
						.prepareCall("{call SaveMultipleDevicePayment(?,?,?,?,?,?,?,?)}");
				psaddDevice.setInt(1, 1);
				psaddDevice.setInt(2, deviceList.get(i).getStudentId());
				psaddDevice.setInt(3, 1);
				psaddDevice.setString(4, "00000000");
				psaddDevice.setInt(5, 0);
				// psaddDevice.setInt(6, 6); // free for month
				// psaddDevice.setInt(6, 7);//FRee for quater
				// psaddDevice.setInt(6, 5);//FRee for Year
				psaddDevice.setInt(6, 8);// FRee for six month

				psaddDevice.setString(7, "Active");
				psaddDevice.setInt(8, 0);
				System.out.println("------device----------"
						+ deviceList.get(i).getStudentId());
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

	public MessageObject FeaturecodeNearby(Connection con, DB mongoconnection,
			String featurecode, String near_bydist, String parentId) {
		MessageObject msgo = new MessageObject();
		ArrayList<RailFeatureCodeDeviceInfoDto> UserList = new ArrayList<>();

		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetDeviceRailAddress(?)}");
			ps.setString(1, parentId);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					RailFeatureCodeDeviceInfoDto dto = new RailFeatureCodeDeviceInfoDto();
					dto.setStudentId(rs.getInt(1));
					dto.setName(rs.getString(2));
					dto.setDeviceID(rs.getString(3));

					UserList.add(dto);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		long startdate = Long.valueOf(Common.getGMTTimeStampFromDate(day - 1
				+ "-" + String.valueOf(month + 1) + "-" + year + " 00:00 am"));

		ArrayList<FeatureAddressDetailsDTO> FeatureSignlsList = new ArrayList<>();

		for (int i = 0; i < UserList.size(); i++) {
			FeatureSignlsList.clear();https://www.google.com/maps/place
			LocationListforFeature.clear();

			try {
				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_LOCATION);
				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device",
						Long.parseLong(UserList.get(i).getDeviceID()));
				BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp",
						new BasicDBObject("$gte", startdate).append("$lt",
								startdate + 86400));

				BasicDBList And_Milage = new BasicDBList();
				And_Milage.add(timestamp_whereQuery);
				And_Milage.add(device_whereQuery);
				DBObject query = new BasicDBObject("$and", And_Milage);

				DBCursor cursor = table.find(query).sort(
						new BasicDBObject("timestamp", 1));
				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				// //System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+query);
				long total = cursor.count();

				if (cursor.size() != 0) {

					// System.err.println("*-Loaction----COunt---"+cursor.size());

					while (cursor.hasNext()) {

						DBObject dbObject = (DBObject) cursor.next();

						BasicDBObject Sourceobj = (BasicDBObject) dbObject
								.get("location");
						LocationDTO locDto = new LocationDTO();
						locDto.setLang(Sourceobj.get("lon") + "");
						locDto.setLat(Sourceobj.get("lat") + "");
						locDto.setSpeed(dbObject.get("speed") + "");
						locDto.setTime(dbObject.get("timestamp") + "");
						locDto.setTimestamp(Long.parseLong(dbObject
								.get("timestamp") + ""));
						locDto.setDeviceImei(dbObject.get("device") + "");
						locDto.setLan_direction(Common
								.getDateCurrentTimeZone(locDto.getTimestamp()));
						LocationListforFeature.add(locDto);
						UserList.get(i).setDeviceOnStatus(1);

					}

				} else {
					UserList.get(i).setDeviceOnStatus(0);

				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (UserList.get(i).getDeviceOnStatus() == 1)
				try {

					java.sql.CallableStatement ps = con
							.prepareCall("{call GetFeaturecodeSignals(?,?,?,?,?)}");
					ps.setString(1, LocationListforFeature.get(0).getLat());
					ps.setString(2, LocationListforFeature.get(0).getLang());
					ps.setString(
							3,
							LocationListforFeature.get(
									LocationListforFeature.size() - 1).getLat());
					ps.setString(
							4,
							LocationListforFeature.get(
									LocationListforFeature.size() - 1)
									.getLang());
					ps.setInt(5, Integer.parseInt(featurecode));
					// //System.err.println("yy ------"+new Gson().toJson(ps));;

					ResultSet rs = ps.executeQuery();

					if (rs != null) {
						while (rs.next()) {

							FeatureAddressDetailsDTO addressDto = new FeatureAddressDetailsDTO();
							addressDto.setDistance(rs.getString("Distance"));
							addressDto.setFeatureCode(rs
									.getString("FeatureCode"));
							addressDto.setFeature_image(rs.getString("Images"));

							addressDto.setFeatureDetail(rs
									.getString("FeatureDetail"));
							addressDto.setKiloMeter(rs.getString("kiloMeter"));
							addressDto.setLatitude(rs.getString("Latitude"));
							addressDto.setLongitude(rs.getString("Longitude"));
							addressDto.setSection(rs.getString("Section"));
							addressDto.setBlockSection(rs
									.getString("BlockSection"));

							FeatureSignlsList.add(addressDto);

						}
					}
					// Get distance from signal now
					for (int f = 0; f < FeatureSignlsList.size(); f++) {

						ArrayList<LocationDTO> nearbyLocFromPole = distancePole(
								Double.parseDouble(FeatureSignlsList.get(f)
										.getLatitude()),
								Double.parseDouble(FeatureSignlsList.get(f)
										.getLongitude()),
								Integer.parseInt(near_bydist));
						if (nearbyLocFromPole.size() > 20) {
							// System.err.println("nearbyLocFromPole ------"+new
							// Gson().toJson(nearbyLocFromPole));
							try {

								BasicDBList addres_list = new BasicDBList();

								for (LocationDTO obj : nearbyLocFromPole) {

									BasicDBObject locationDocument = new BasicDBObject();

									locationDocument.put("lat", obj.getLat());
									locationDocument.put("lon", obj.getLang());
									locationDocument.put("speed",
											obj.getSpeed());
									locationDocument.put("timestamp",
											obj.getTime());
									locationDocument.put("device",
											obj.getDeviceImei());
									addres_list.add(locationDocument);

								}

								DBCollection table = mongoconnection
										.getCollection(Common.TABLE_NEARBYFEATURE_RAILADDRESS);

								BasicDBObject Maindocument = new BasicDBObject();
								ObjectId objid = new ObjectId();
								Maindocument.put("address_id", "" + objid);
								Maindocument.put("device", Long
										.parseLong(UserList.get(i)
												.getDeviceID()));
								Maindocument.put("kilometer", Double
										.parseDouble(FeatureSignlsList.get(f)
												.getKiloMeter()));
								Maindocument.put("distance", Double
										.parseDouble(FeatureSignlsList.get(f)
												.getDistance()));
								Maindocument.put("latitude", Double
										.parseDouble(FeatureSignlsList.get(f)
												.getLatitude()));
								Maindocument.put("longitude", Double
										.parseDouble(FeatureSignlsList.get(f)
												.getLongitude()));
								Maindocument.put("feature_code",
										FeatureSignlsList.get(f)
												.getFeatureCode());
								Maindocument.put("timestamp", startdate);

								Maindocument.put("feature_detail",
										FeatureSignlsList.get(f)
												.getFeatureDetail());

								Maindocument.put("feature_image",
										FeatureSignlsList.get(f)
												.getFeature_image());

								Maindocument.put("section", FeatureSignlsList
										.get(f).getSection());

								Maindocument.put("Location_address_list",
										addres_list);

								table.insert(Maindocument);

								msgo.setError("false");
								msgo.setMessage("Address added successfully.---");

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								msgo.setError("true");
								msgo.setMessage("address_details not added successfully.---");
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								msgo.setError("true");
								msgo.setMessage("address_details not added successfully.---");

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								msgo.setError("true");
								msgo.setMessage("address_details not added successfully.---");
							}

						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			// System.err.println("FeatureSignlsList size------"+FeatureSignlsList.size());

			// FOr End
		}

		// //System.err.println("FeatureSignlsList ------"+new
		// Gson().toJson(FeatureSignlsList));

		return msgo;
	}

	public ArrayList<LocationDTO> distancePole(double Lat, double Lan,
			int near_bydist) {

		ArrayList<LocationDTO> nearLocationList = new ArrayList<>();
		double earthRadius = 3958.75;
		double minDistance = 0;
		int position = 0;
		final int R = 6371; // Radius of the earth

		for (int i = 0; i < LocationListforFeature.size(); i++) {
			double distDiff = distance(
					Lat,
					Lan,
					Double.parseDouble(LocationListforFeature.get(i).getLat()),
					Double.parseDouble(LocationListforFeature.get(i).getLang()),
					"K");

			// //System.out.println("-----------Dista rint-------"+Lat+" - "+Lan+" - "+list.get(i).getLat()+" - "+list.get(i).getLang());

			// //System.out.println("-----------Dista-------"+distDiff*1000);

			if (distDiff * 1000 < near_bydist) {
				LocationListforFeature.get(i).setNearDist(distDiff * 1000);
				nearLocationList.add(LocationListforFeature.get(i));
				LocationListforFeature.remove(i);
			}

		}
		// //System.out.println("-----------nearLocationList-------"+nearLocationList.size());

		return nearLocationList;

	}

	public ArrayList<FeatureNearbyDTO> GetFeaturecodeNearby(Connection con,
			DB mongoconnection, String startdate, String featurecode,
			String parentId) {
		ArrayList<RailFeatureCodeDeviceInfoDto> UserList = new ArrayList<>();
		ArrayList<FeatureNearbyDTO> dataList = new ArrayList<>();

		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetDeviceRailAddress(?)}");
			ps.setString(1, parentId);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					RailFeatureCodeDeviceInfoDto dto = new RailFeatureCodeDeviceInfoDto();
					dto.setStudentId(rs.getInt(1));
					dto.setName(rs.getString(2));
					dto.setDeviceID(rs.getString(3));

					UserList.add(dto);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < UserList.size(); i++) {
			try {
				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_NEARBYFEATURE_RAILADDRESS);
				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device",
						Long.parseLong(UserList.get(i).getDeviceID()));
				BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp", new BasicDBObject("$gte",
								Long.parseLong(startdate)).append("$lt",
								Long.parseLong(startdate) + 86400));

				BasicDBObject featurecode_whereQuery = new BasicDBObject();
				featurecode_whereQuery.put("feature_code", featurecode);

				BasicDBList And_Milage = new BasicDBList();
				And_Milage.add(timestamp_whereQuery);
				And_Milage.add(device_whereQuery);
				And_Milage.add(featurecode_whereQuery);

				DBObject query = new BasicDBObject("$and", And_Milage);

				DBCursor cursor = table.find(query).sort(
						new BasicDBObject("timestamp", 1));
				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				// //System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+query);
				long total = cursor.count();

				if (cursor.size() != 0) {

					// System.err.println("*-GetFeaturecodeNearby----COunt---"+cursor.size());

					while (cursor.hasNext()) {

						DBObject dbObject = (DBObject) cursor.next();

						BasicDBList addres_list = (BasicDBList) dbObject
								.get("Location_address_list");

						DBObject startdbObject = (DBObject) addres_list.get(0);
						DBObject enddbObject = (DBObject) addres_list
								.get(addres_list.size() - 1);
						long timeSpent = Long.parseLong(enddbObject
								.get("timestamp") + "")
								- Long.parseLong(startdbObject.get("timestamp")
										+ "");

						FeatureNearbyDTO locDto = new FeatureNearbyDTO();

						locDto.setStartLocation(startdbObject.get("lat") + ""
								+ "," + startdbObject.get("lon") + "");
						locDto.setEndLocation(enddbObject.get("lat") + "" + ","
								+ enddbObject.get("lon") + "");

						locDto.setTimespent(timeSpent * 0.0166667 + " min ");
						locDto.setDate(Long.parseLong(dbObject.get("timestamp")
								+ "")
								+ "");

						locDto.setFeatureDetail(dbObject.get("feature_detail")
								+ "");
						locDto.setSection(dbObject.get("section") + "");

						locDto.setGpsDeviceName(UserList.get(i).getName());
						locDto.setSignalLoaction((dbObject.get("latitude") + ""
								+ "," + dbObject.get("longitude") + ""));
						UserList.get(i).setDeviceOnStatus(1);
						dataList.add(locDto);
					}

				} else {
					UserList.get(i).setDeviceOnStatus(0);

				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// System.err.println("dataList  ------"+UserList.get(i).getName()+"-----"+new
			// Gson().toJson(dataList));

			// FOr End
		}

		return dataList;
	}

	public ArrayList<SosInfoDTO> GetSosData(Connection con, DB mongoconnection,
			String startdate, String parentId) {
		ArrayList<RailFeatureCodeDeviceInfoDto> UserList = new ArrayList<>();
		ArrayList<SosInfoDTO> sosDataList = new ArrayList<SosInfoDTO>();

		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetDeviceRailAddress(?)}");
			ps.setString(1, parentId);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					RailFeatureCodeDeviceInfoDto dto = new RailFeatureCodeDeviceInfoDto();
					dto.setStudentId(rs.getInt(1));
					dto.setName(rs.getString(2));
					dto.setDeviceID(rs.getString(3));

					UserList.add(dto);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < UserList.size(); i++) {
			try {
				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_ALERT_MSG);
				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device",
						Long.parseLong(UserList.get(i).getDeviceID()));
				BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp", new BasicDBObject("$gte",
								Long.parseLong(startdate)).append("$lt",
								Long.parseLong(startdate) + 86400));
				BasicDBObject statusQuery = new BasicDBObject("status",
						"sos_alarm");
				BasicDBList And_Milage = new BasicDBList();
				And_Milage.add(timestamp_whereQuery);
				And_Milage.add(device_whereQuery);
				And_Milage.add(statusQuery);
				DBObject query = new BasicDBObject("$and", And_Milage);
				DBCursor cursor = table.find(query).sort(
						new BasicDBObject("timestamp", 1));
				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				System.out.print("Cursor Count of u-p---" + cursor.size()
						+ "  " + query);
				long total = cursor.count();

				if (cursor.size() != 0) {

					System.err.println("*-GetSosData----COunt---"
							+ cursor.size());

					while (cursor.hasNext()) {

						SosInfoDTO sosObj = new SosInfoDTO();

						DBObject dbObject = (DBObject) cursor.next();
						sosObj.setGpsDeviceName(UserList.get(i).getName());
						sosObj.setGsm_signal_strength(dbObject
								.get("gsm_signal_strength") + "");
						sosObj.setVoltage_level(dbObject.get("voltage_level")
								+ "");
						sosObj.setTime(dbObject.get("timestamp") + "");
						sosObj.setDeviceId(UserList.get(i).getDeviceID());

						DBCollection toadytable = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_toadytablewhereQuery = new BasicDBObject();
						device_toadytablewhereQuery.put("device",
								Long.parseLong(UserList.get(i).getDeviceID()));
						BasicDBObject timestamp_loc_whereQuery = new BasicDBObject(
								"timestamp", new BasicDBObject("$lt",
										Long.parseLong(startdate)));

						BasicDBList and_query = new BasicDBList();
						and_query.add(device_toadytablewhereQuery);
						and_query.add(timestamp_loc_whereQuery);

						DBObject loc_and_query = new BasicDBObject("$and",
								and_query);
						DBCursor cursor_today = toadytable.find(loc_and_query)
								.sort(new BasicDBObject("timestamp", 1))
								.limit(1);
						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Else ---query " + i + "==="
								+ loc_and_query);
						System.out.println("cursor_today Count of u-p11----"
								+ cursor.size() + "  " + device_whereQuery);

						if (cursor_today.size() != 0) {

							while (cursor_today.hasNext()) {

								DBObject dbObjectmain = (DBObject) cursor_today
										.next();
								DBObject dbObject_location = (DBObject) dbObjectmain
										.get("location");
								System.err.println("dbObject_location---- "
										+ dbObjectmain + "==="
										+ dbObject_location);

								sosObj.setLang(dbObject_location.get("lon")
										+ "");
								sosObj.setLat(dbObject_location.get("lat") + "");
								sosObj.setSpeed(dbObjectmain.get("speed") + "");
								sosObj.setLocationTime(dbObjectmain
										.get("timestamp") + "");

							}
						}
						sosDataList.add(sosObj);
					}

				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// FOr End
		}
		// System.err.println("dataList  ------"+new
		// Gson().toJson(sosDataList));

		return sosDataList;
	}

	public ArrayList<SosInfoDTO> GetBatteryData(Connection con,
			DB mongoconnection, String parentId) {
		ArrayList<RailFeatureCodeDeviceInfoDto> UserList = new ArrayList<>();
		ArrayList<SosInfoDTO> sosDataList = new ArrayList<SosInfoDTO>();

		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetDeviceRailAddress(?)}");
			ps.setString(1, parentId);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					RailFeatureCodeDeviceInfoDto dto = new RailFeatureCodeDeviceInfoDto();
					dto.setStudentId(rs.getInt(1));
					dto.setName(rs.getString(2));
					dto.setDeviceID(rs.getString(3));

					UserList.add(dto);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < UserList.size(); i++) {
			try {
				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_ALERT_MSG);
				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device",
						Long.parseLong(UserList.get(i).getDeviceID()));

				DBCursor cursor = table.find(device_whereQuery)
						.sort(new BasicDBObject("_id", -1)).limit(1);
				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				System.out.print("CursorGetBatteryData Count of u-p---"
						+ cursor.size() + "  " + device_whereQuery);
				long total = cursor.count();

				if (cursor.size() != 0) {

					System.err.println("*-GetBatteryData----COunt---"
							+ cursor.size());

					while (cursor.hasNext()) {

						SosInfoDTO sosObj = new SosInfoDTO();

						DBObject dbObject = (DBObject) cursor.next();
						sosObj.setGpsDeviceName(UserList.get(i).getName());
						sosObj.setGsm_signal_strength(dbObject
								.get("gsm_signal_strength") + "");
						sosObj.setVoltage_level(dbObject.get("voltage_level")
								+ "");
						sosObj.setTime(dbObject.get("timestamp") + "");
						sosObj.setDeviceId(UserList.get(i).getDeviceID());

						sosDataList.add(sosObj);
					}

				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// FOr End
		}
		// System.err.println("dataList  ------"+new
		// Gson().toJson(sosDataList));

		return sosDataList;
	}

	public ArrayList<DeviceStatusInfoDto> GetCurrentAllDeviceStaus(
			DB mongoconnection, Connection con) {
		ArrayList<DeviceStatusInfoDto> deviceInfoList = new ArrayList<>();

		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_DEVICE);
			BasicDBObject device_whereQuery = new BasicDBObject();
			DBCursor cursor = table.find(device_whereQuery).sort(
					new BasicDBObject("timestamp", -1));
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			// //System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+query);
			long total = cursor.count();

			if (cursor.size() != 0) {

				// System.err.println("*-GetSosData----COunt---"+cursor.size());

				while (cursor.hasNext()) {

					DeviceStatusInfoDto sosObj = new DeviceStatusInfoDto();

					DBObject dbObject = (DBObject) cursor.next();
					// System.err.println("Else ---device-----------------"+dbObject.get("device"));

					sosObj.setDeviceID((Long) dbObject.get("device"));

					sosObj.setStudentId(dbObject.get("student") + "");
					sosObj.setParentId(dbObject.get("parent") + "");

					DBCollection toadytable = mongoconnection
							.getCollection(Common.TABLE_TODAYLOCATION);
					BasicDBObject device_toadytablewhereQuery = new BasicDBObject();
					device_toadytablewhereQuery.put("device",
							sosObj.getDeviceID());
					BasicDBObject timestamp_loc_whereQuery = new BasicDBObject(
							"timestamp", new BasicDBObject("$lt",
									calendar.getTimeInMillis()));

					BasicDBList and_query = new BasicDBList();
					and_query.add(device_toadytablewhereQuery);
					and_query.add(timestamp_loc_whereQuery);

					DBObject loc_and_query = new BasicDBObject("$and",
							and_query);
					DBCursor cursor_today = toadytable.find(loc_and_query)
							.sort(new BasicDBObject("timestamp", 1)).limit(1);
					cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

					// System.err.println("Else ---query "+i+"==="+loc_and_query);
					// //System.out.println("cursor_today Count of u-p11----"+
					// cursor.size() + "  " + device_whereQuery);

					if (cursor_today.size() != 0) {

						while (cursor_today.hasNext()) {

							DBObject dbObjectmain = (DBObject) cursor_today
									.next();
							DBObject dbObject_location = (DBObject) dbObjectmain
									.get("location");
							// System.err.println("dbObject_location---- "+dbObjectmain+"==="+dbObject_location);

							sosObj.setLang(dbObject_location.get("lon") + "");
							sosObj.setLat(dbObject_location.get("lat") + "");
							sosObj.setSpeed(dbObjectmain.get("speed") + "");
							sosObj.setTime(dbObjectmain.get("timestamp") + "");
							sosObj.setDeviceOnStatus(1);

						}
					} else {
						sosObj.setDeviceOnStatus(0);

					}

					deviceInfoList.add(sosObj);
				}

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return deviceInfoList;
	}

	public MessageObject GenerateTripReportIdCard(Connection con,
			DB mongoconnection) {
		ArrayList<DeviceInfoDto> UserList = new ArrayList<>();
		ArrayList<DBObject> TempLoctionList = new ArrayList<>();
		int TripTimeLimit = 300;
		File tripfile;
		int Deaactivecount = 0;
		FileWriter writer = null;
		calendar = Calendar.getInstance();
		ArrayList<Double> DeaciveListDevice = new ArrayList<Double>();

	/*	try {
			// Whatever the file path is.
			tripfile = new File(Common.Log_path
					+ "GenerateTripReportIdCard_log" + calendar.getTime()
					+ ".txt");
			// Create the file
			if (tripfile.createNewFile()) {
				// System.out.println("File is created!");
			} else {
				// System.out.println("File already exists.");
			}

			// Write Content
			writer = new FileWriter(tripfile);

			bfwriter = new BufferedWriter(writer);
			bfwriter.write("\nStart Generate Daily TripReprt Log----at "
					+ calendar.getTime());

		} catch (IOException e) {
			// System.err.println("Problem writing to the file statsTest.txt");
		} catch (Exception e) {

			e.printStackTrace();
		}*/

		int trip = 0;
		calendar = Calendar.getInstance();

		try {
			java.sql.CallableStatement psc = con
					.prepareCall("{call GetDeviceListForTripReport()}");
			ResultSet rs = psc.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					DeviceInfoDto dto = new DeviceInfoDto();
					dto.setName(rs.getString(1) + rs.getString(2));
					dto.setDeviceID(rs.getString(3));
					dto.setDeviceType(rs.getInt(4));

					if (dto.getDeviceType() == 1) {
						UserList.add(dto);
						System.out.println("--------Report Req--Allow---"+rs.getString(1)+rs.getString(2)+"-----"+rs.getInt(4));

					}

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);

		long Starttime = Long.valueOf(Common.getGMTTimeStampFromDate(day - 1
				+ "-" + String.valueOf(month + 1) + "-" + year + " 00:00 am"));
		long Endtime = Long.valueOf(Common.getGMTTimeStampFromDate(day + "-"
				+ String.valueOf(month + 1) + "-" + year + " 00:00 am"));

		//
		// long Starttime =1534530600 ;
		// long Endtime =1534617000;

		// System.out.println("StartTime:"+Starttime+"  End Time:"+Endtime+"  UserList size: "+UserList.size());
		System.out.println("--------UserList size---"+UserList);

		try {
//			bfwriter.write("\n----------TEStttt-------------Report Generating  for---------------------------"
//					+ "\n\n******************"
//					+ "StartTime: "
//					+ Starttime
//					+ "  End Time: " + Endtime + "********");

			for (int id = 0; id < UserList.size(); id++) {

				trip = 0;
				HistoryDistance = 0;

				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_LOCATION);
				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device",
						Long.parseLong(UserList.get(id).getDeviceID().trim()));
				// device_whereQuery.put("device",Long.parseLong("359751090004149"));

				BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp",
						new BasicDBObject("$gte", Starttime).append("$lt",
								Endtime));
				BasicDBList And_Milage = new BasicDBList();
				And_Milage.add(timestamp_whereQuery);
				And_Milage.add(device_whereQuery);
				DBObject Total_Milage_query = new BasicDBObject("$and",
						And_Milage);

				DBCursor cursor = table.find(Total_Milage_query);
				cursor.sort(new BasicDBObject("timestamp", 1));

				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				// System.out.println("Cursor Count of u-p11----"+cursor.size()+"  "+Total_Milage_query);
				long i = 0;
				long total = cursor.size();
				Double MaxSpeed = 0.0;
				Double TotalSpeed = 0.0;
				int Triplocationcount = 0;

				if (cursor.size() > 0) {
					TempLoctionList.clear();

					// System.err.println("*-----COunt---"+cursor.size());

					List<DBObject> listObjects = cursor.toArray();
					trip = Calculate_ID_CardTrip(
							(ArrayList<DBObject>) listObjects, mongoconnection,
							TripTimeLimit, UserList.get(id).getDeviceID(),
							UserList.get(id).getName());
					// trip=Calculate_ID_CardTrip((ArrayList<DBObject>)
					// listObjects,mongoconnection,TripTimeLimit,"359751090004149","MLDT 266");

				}

				// bfwriter.write("\n------Trip Generate count is == "+trip+"  for  "+UserList.get(id).getName()+" , Device Id : "+UserList.get(id).getDeviceID());

				// bfwriter.write("\n------Trip Generate count is == "+trip);

			}// for end
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			
		}

		return null;
	}

	public MessageObject assignSimNumberSqlToMongodb(Connection con,
			DB mongoconnection) {
		MessageObject msgo = new MessageObject();
		ArrayList<mongoDeviceObj> deviceList = new ArrayList<>();
		/*
		 * try[],[],[] {
		 */ResultSet rs = null;
		// Made changes in select for email;
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("select ParentID,StudentID,DeviceID,DeviceSimNumber from StudentMaster "
					+ "where DeviceSimNumber IS NOT NULL and  LEN(DeviceID)=15");

			rs = ps.executeQuery();
			while (rs.next()) {
				mongoDeviceObj msgObj = new mongoDeviceObj();
				msgObj.setParentId(rs.getInt("ParentID"));
				msgObj.setStudentId(rs.getInt("StudentID"));
				msgObj.setDeviceId(rs.getString("DeviceID"));
				msgObj.setSimcardNo(rs.getString("DeviceSimNumber"));

				deviceList.add(msgObj);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("------device---size-------" + deviceList.size());

		try {

			for (int id = 0; id < deviceList.size(); id++) {

				System.out.println("------device----------" + id + "-------"
						+ deviceList.get(id).getDeviceId());

				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_TODAYLOCATION);

				BasicDBObject newDocument = new BasicDBObject();
				newDocument.append("$set", new BasicDBObject().append(
						"device_sim_no", deviceList.get(id).getSimcardNo()));

				BasicDBObject searchQuery = new BasicDBObject()
						.append("device",
								Long.parseLong(deviceList.get(id).getDeviceId()
										.trim()));

				table.update(searchQuery, newDocument);
				
				System.out.println("assignSimNumberSqlToMongodb------"
						+ searchQuery + newDocument);

			}

			msgo.setError("false");
			msgo.setMessage("Task added successfully.");

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("Task not added successfully");
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("Task not added successfully");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("Task not added successfully");
		}

		return msgo;
	}

	public MessageObject trainAlertForGangman(Connection con,
			DB mongoconnection, Long timestamp, int speed, Double lat,
			Double lan, String train_device) {

		MessageObject msgo = new MessageObject();
		ArrayList<String> deviceList = new ArrayList<>();
		StringBuilder simNumberList = new StringBuilder();

		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_TODAYLOCATION);

			BasicDBObject match = new BasicDBObject();
			BasicDBObject matchQuery1 = new BasicDBObject("location.lat",
					new BasicDBObject("$gte", lat - 0.15).append("$lt",
							lat + 0.15));
			BasicDBObject matchQuery2 = new BasicDBObject("location.lon",
					new BasicDBObject("$gte", lan - 0.15).append("$lt",
							lan + 0.15));
			BasicDBObject matchQuery3 = new BasicDBObject("timestamp",
					new BasicDBObject("$gte", timestamp - 86400));

			BasicDBList matchQuerylist = new BasicDBList();
			matchQuerylist.add(matchQuery1);
			matchQuerylist.add(matchQuery2);
			matchQuerylist.add(matchQuery3);

			DBObject matchQuery = new BasicDBObject("$and", matchQuerylist);

			DBCursor cursor = table.find(matchQuery);
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			System.out.print("trainAlertForGangman Count of u-p---"
					+ cursor.size() + "  " + matchQuerylist);
			long total = cursor.count();

			if (cursor.size() != 0) {

				System.err.println("----trainAlertForGangman----COunt---"
						+ cursor.size());

				while (cursor.hasNext()) {
					DBObject dbObjectmain = (DBObject) cursor.next();
					BasicDBList tarin_device_call_time = (BasicDBList) dbObjectmain
							.get("device_call_time");

					BasicDBObject[] lightArr = tarin_device_call_time
							.toArray(new BasicDBObject[0]);
					for (BasicDBObject dbObj : lightArr) {

						long timeDiff = (new Date().getTime() / 1000)
								- dbObj.getLong("calltime");
						System.err.println("*-timeDiff-------" + timeDiff);

						if (timeDiff > 600
								&& dbObj.getString("train_device").equals(
										train_device)) {
							deviceList.add(dbObjectmain.get("device") + "");
							if (dbObjectmain.get("device_sim_no") != null)
								simNumberList.append(
										dbObjectmain.get("device_sim_no") + "")
										.append(",");
							// System.out.println("*-trainAlertForGangman-------"+dbObjectmain);
						}
					}

				}
				System.err.println("*-trainAlertForGangman----simNumberList---"
						+ simNumberList);
				System.err.println("*-trainAlertForGangman----device---"
						+ deviceList.size());

				if (simNumberList.length() > 0) {
					sentTrainAlertCall(deviceList, simNumberList, table,
							train_device);

				}
				msgo.setError("false");
				msgo.setMessage("Task added successfully.");

			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("Task not added successfully");
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("Task not added successfully");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("Task not added successfully");
		}
		return msgo;

	}

	private void sentTrainAlertCall(ArrayList<String> deviceList,
			StringBuilder simNumberList, DBCollection table, String train_device) {
		try {
			// System.err.println("inside  sentSms--------------------");
			final String USER_AGENT = "Mozilla/5.0";
			String simno = simNumberList.toString().replaceFirst(".$", "");
			// String simno=simNumberList.toString().substring(0,
			// simNumberList.toString().lastIndexOf(","));

			// Construct data
			// String data =
			// "http://sms.hspsms.com/sendSMS?username=primesystech&message="+msg+"&sendername=MKTSMS&smstype=TRANS&numbers="+monno+"&apikey="+Common.HSPSMS_API_KEY;
			String data = "http://sms.hspsms.com/SENDVOICEAPI?fileurl=http://sms.hspsms.com/UploadFile/3167120181115134025.mp3&numbers="
					+ simno
					+ "&apikey="
					+ Common.HSPSMS_CALLER_API_KEY
					+ "&username=primevoice&service=VOICETRANS&callerid=9762554098";

			System.err.println("inside  sentTrainAlertCall--------------------"
					+ data);

			URL obj = new URL(data);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			// System.out.println("\nSending 'GET' request to URL : " + data);
			// System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			for (String device : deviceList) {
				DBObject find = new BasicDBObject("device",
						Long.parseLong(device));

				BasicDBObject train = new BasicDBObject("train_device",
						train_device);
				BasicDBObject trainArray = new BasicDBObject(
						"device_call_time", train);

				BasicDBObject trainPull = new BasicDBObject("$pull", trainArray);
				table.update(find, trainPull);

				System.err.println(device + "-----trainPull Code : "
						+ trainPull);

				BasicDBObject trainPush = new BasicDBObject("train_device",
						train_device).append("", new Date().getTime());

				DBObject listItem = new BasicDBObject("device_call_time",
						new BasicDBObject("train_device", train_device).append(
								"calltime", new Date().getTime() / 1000));
				DBObject updateQuery = new BasicDBObject("$push", listItem);

				System.err.println(device + "--" + find + "---Push Code : "
						+ updateQuery);

				table.update(find, updateQuery, true, false);

			}

			// print result
			System.out.println("APi Call Respo------------          "
					+ response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MessageObject SendGPSDeviceOnOffData(DB mongoconnection,
			Connection con, String parentId) {
		MessageObject msgo = new MessageObject();

		ArrayList<RailMailSendInfoDto> mailSendInfo = new ArrayList<>();

		try {
			PreparedStatement ps = con
					.prepareStatement("select Id  FROM RailwayDepartMentHirachy where Id>=14");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				// System.err.println("New rs.getInt()===== "+rs.getInt("Id"));
				try {

					java.sql.CallableStatement ps_call = con
							.prepareCall("{call GetGpsDeviceToSendMail(?)}");
					ps_call.setInt(1, rs.getInt("Id"));
					ResultSet rs1 = ps_call.executeQuery();
					ArrayList<String> EmailIds = new ArrayList();
					ArrayList<String> DeviceIds;
					if (rs1 != null) {
						while (rs1.next()) {

							// /System.out.println("New next.===== "+rs1.getString("ChildStudent"));

							RailMailSendInfoDto dto = new RailMailSendInfoDto();
							EmailIds.add((rs1.getString("EmailId") + "").trim());
							dto.setDept(rs1.getString("DeptName").trim());

							ArrayList<RailDeviceInfoDto> deviceInfoList = new ArrayList<>();

							if (rs1.getString("ChildStudent") != null) {

								String[] device_no = rs1.getString(
										"ChildStudent").split(",");

								if (device_no.length > 0)
									for (int i = 0; i < device_no.length; i++) {

										System.err
												.println("GetDeviceIMEIT=device_no[i]==== "
														+ device_no[i]
														+ "-----"
														+ rs1.getString("ParentId"));

										try {

											ps_call = con
													.prepareCall("{call GetDeviceIMEITosendMail(?,?)}");
											ps_call.setString(1, "-"
													+ device_no[i]);
											ps_call.setString(2,
													rs1.getString("ParentId"));

											ResultSet rs3 = ps_call
													.executeQuery();

											if (rs3 != null) {
												while (rs3.next()) {

													RailDeviceInfoDto dto_info = new RailDeviceInfoDto();
													dto_info.setStudentId(rs3
															.getInt(1));
													dto_info.setName(rs3
															.getString(2));
													dto_info.setDeviceID(rs3
															.getString(3));

													deviceInfoList
															.add(dto_info);

												}
											}
											// System.err.println("deviceInfoList===== "+new
											// Gson().toJson(deviceInfoList));

										} catch (Exception e) {
											e.printStackTrace();
										}

									}// for end

							}

							if (rs1.getString("DeptParents") != null) {
								String[] ParentIds = rs1.getString(
										"DeptParents").split(",");

								for (String ParentId : ParentIds) {

									ps_call = con
											.prepareCall("{call GetEmailOfDeptParent(?)}");
									ps_call.setString(1, ParentId);

									ResultSet rs4 = ps_call.executeQuery();

									if (rs4 != null)
										while (rs4.next())
											EmailIds.add((rs4
													.getString("EmailId") + "")
													.trim());

								}

							}

							dto.setEmailIds(EmailIds);
							dto.setDeviceIds(deviceInfoList);
							dto.setParentId(rs1.getInt("ParentId") + "");

							mailSendInfo.add(dto);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.err.println("New mailSendInfo===== "
				+ new Gson().toJson(mailSendInfo));

		// tripColumnStyle.setFont(font);
		for (int j = 0; j < mailSendInfo.size(); j++) {

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();

			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);
			long startdate = Long.valueOf(Common.getGMTTimeStampFromDate(day
					- 1 + "-" + String.valueOf(month + 1) + "-" + year
					+ " 00:00 am"));
			long Endtime = Long.valueOf(Common.getGMTTimeStampFromDate(day
					+ "-" + String.valueOf(month + 1) + "-" + year
					+ " 00:00 am"));

			DBCollection collection = mongoconnection
					.getCollection(Common.TABLE_LOCATION);

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("device On Off Status"));

			XSSFCellStyle onStyle = workbook.createCellStyle();
			onStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
			onStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFCellStyle offStyle = workbook.createCellStyle();
			offStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
			offStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle.setFillForegroundColor(IndexedColors.CORAL
					.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0);
			row.setRowStyle(tripColumnStyle);

			cell.setCellValue("Name");
			cell = row.createCell(1);
			cell.setCellValue("Device Status");
			cell = row.createCell(2);
			cell.setCellValue("Start Time");
			cell = row.createCell(3);
			cell.setCellValue("Device Start  Latitude");
			cell = row.createCell(4);
			cell.setCellValue("Device Start  Longitude");
			cell = row.createCell(5);
			cell.setCellValue("End Time");
			cell = row.createCell(6);
			cell.setCellValue("Device End  Latitude");
			cell = row.createCell(7);
			cell.setCellValue("Device End  Longitude");
			cell = row.createCell(8);

			for (int i = 0; i < mailSendInfo.get(j).getDeviceIds().size(); i++)
				try {

					row = sheet.createRow(sheet.getLastRowNum() + 1);

					List<DBObject> aggregationInput = new ArrayList<DBObject>();

					// ///////////match strta/////////////////////////////////

					BasicDBObject matchQuery1 = new BasicDBObject("device",
							Long.parseLong(deviceInfoList.get(i).getDeviceID()));

					BasicDBObject matchQuery2 = new BasicDBObject("timestamp",
							new BasicDBObject("$gte", (startdate)).append(
									"$lt", (startdate) + 86400));

					BasicDBList matchQuerylist = new BasicDBList();
					matchQuerylist.add(matchQuery1);
					matchQuerylist.add(matchQuery2);
					DBObject matchQuery = new BasicDBObject("$and",
							matchQuerylist);

					DBCursor cursor = collection.find(matchQuery).sort(
							new BasicDBObject("timestamp", 1));

					cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
					System.err
							.println("----send mail----query---" + matchQuery);
					if (cursor.size() != 0) {

						System.err.println("----send mail----COunt---"
								+ cursor.size());
						DBObject firstObj = null, lastObj = null;
						int count = 0;
						while (cursor.hasNext()) {
							if (count == 0)
								firstObj = (DBObject) cursor.next();
							else
								lastObj = (DBObject) cursor.next();

							count++;

						}
						System.out.println("First Obj--send mail---" + firstObj
								+ "----");

						System.out.println("lastObj obj--send mail---"
								+ lastObj + "----");

						cell = row.createCell(0);
						cell.setCellValue(deviceInfoList.get(i).getName());
						cell = row.createCell(1);
						cell.setCellValue("On");

						// System.out.println("ttttt-----" + main_doc);

						cell = row.createCell(2);
						cell.setCellValue(Common.getDateCurrentTimeZone(Long
								.parseLong(firstObj.get("timestamp") + "")));
						cell = row.createCell(3);
						cell.setCellValue(((DBObject) firstObj.get("location"))
								.get("lat") + "");
						cell = row.createCell(4);
						cell.setCellValue(((DBObject) firstObj.get("location"))
								.get("lon") + "");
						cell = row.createCell(5);
						cell.setCellValue(Common.getDateCurrentTimeZone(Long
								.parseLong(lastObj.get("timestamp") + "")));
						cell = row.createCell(6);
						cell.setCellValue(((DBObject) lastObj.get("location"))
								.get("lat") + "");
						cell = row.createCell(7);
						cell.setCellValue(((DBObject) lastObj.get("location"))
								.get("lon") + "");

						row.getCell(1).setCellStyle(onStyle);

					} else {
						cell = row.createCell(0);
						cell.setCellValue(deviceInfoList.get(i).getName());
						cell = row.createCell(1);
						cell.setCellValue("Off");
						row.getCell(1).setCellStyle(offStyle);

					}

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MongoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			// System.out.println("ttttt--send mail---" +
			// deviceInfoList.size()+"----");

			if (deviceInfoList.size() > 0) {
				String outFileName = mailSendInfo.get(j).getDept() + "_"
						+ parentId + "_" + "Device_ON_OFF_Status_Report_"
						+ Common.getDateFromLong(startdate).replace(":", "-")
						+ ".xlsx";
				try {
					String file = Common.ServerDevice_ONOFF_FileUplod_Path
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));

					/*
					 * Set<PosixFilePermission> ownerWritable =
					 * PosixFilePermissions.fromString("rw-r--r--");
					 * FileAttribute<?> permissions =
					 * PosixFilePermissions.asFileAttribute(ownerWritable);
					 * Files.createFile(file_l.toPath(), permissions);
					 * 
					 * 
					 * FileOutputStream fos = new FileOutputStream(file_l);
					 */
					workbook.write(fos);
					fos.flush();
					fos.close();
					// String sendemail="rupesh.p@mykiddytracker.com";

					String sendemail = "";
					for (String emaString : mailSendInfo.get(j).getEmailIds()) {
						if (emaString.trim().length() > 0) {
							if (sendemail.length() == 0) {
								sendemail = emaString.trim();
							} else {
								sendemail = sendemail + "," + emaString.trim();

							}

						}
					}
					SendEmail mail = new SendEmail();

					mail.sendDeviceOnOffMailToDepartmentEmp(sendemail, file,
							mailSendInfo.get(j).getDept());

				} catch (Exception e) {

					e.printStackTrace();
				}

			}

		}

		return msgo;

	}

	public MessageObject SendGPSTripReportData(DB mongoconnection,
			Connection con, String parentId) {
		MessageObject msgo = new MessageObject();

		ArrayList<RailMailSendInfoDto> mailSendInfo = new ArrayList<>();

		try {
			PreparedStatement ps = con
					.prepareStatement("select Id  FROM RailwayDepartMentHirachy where Id>=14");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				System.err.println("New rs.getInt()===== " + rs.getInt("Id"));
				try {

					java.sql.CallableStatement ps_call = con
							.prepareCall("{call GetGpsDeviceToSendMail(?)}");
					ps_call.setInt(1, rs.getInt("Id"));
					ResultSet rs1 = ps_call.executeQuery();
					ArrayList<String> EmailIds = new ArrayList();
					ArrayList<String> DeviceIds;
					if (rs1 != null) {
						while (rs1.next()) {

							System.out.println("New next.===== "
									+ rs1.getString("ChildStudent"));

							RailMailSendInfoDto dto = new RailMailSendInfoDto();
							EmailIds.add((rs1.getString("EmailId") + "").trim());
							dto.setDept(rs1.getString("DeptName").trim());

							ArrayList<RailDeviceInfoDto> deviceInfoList = new ArrayList<>();

							if (rs1.getString("ChildStudent") != null) {

								String[] device_no = rs1.getString(
										"ChildStudent").split(",");

								if (device_no.length > 0)
									for (int i = 0; i < device_no.length; i++) {

										System.err
												.println("GetDeviceIMEIT=device_no[i]==== "
														+ device_no[i]
														+ "-----"
														+ rs1.getString("ParentId"));

										try {

											ps_call = con
													.prepareCall("{call GetDeviceIMEITosendMail(?,?)}");
											ps_call.setString(1, "-"
													+ device_no[i]);
											ps_call.setString(2,
													rs1.getString("ParentId"));

											ResultSet rs3 = ps_call
													.executeQuery();

											if (rs3 != null) {
												while (rs3.next()) {

													RailDeviceInfoDto dto_info = new RailDeviceInfoDto();
													dto_info.setStudentId(rs3
															.getInt(1));
													dto_info.setName(rs3
															.getString(2));
													dto_info.setDeviceID(rs3
															.getString(3));

													deviceInfoList
															.add(dto_info);

												}
											}
											// System.err.println("deviceInfoList===== "+new
											// Gson().toJson(deviceInfoList));

										} catch (Exception e) {
											e.printStackTrace();
										}

									}// for end

							}

							if (rs1.getString("DeptParents") != null) {
								String[] ParentIds = rs1.getString(
										"DeptParents").split(",");

								for (String ParentId : ParentIds) {

									ps_call = con
											.prepareCall("{call GetEmailOfDeptParent(?)}");
									ps_call.setString(1, ParentId);

									ResultSet rs4 = ps_call.executeQuery();

									if (rs4 != null)
										while (rs4.next())
											EmailIds.add((rs4
													.getString("EmailId") + "")
													.trim());

								}

							}

							dto.setEmailIds(EmailIds);
							dto.setDeviceIds(deviceInfoList);
							dto.setParentId(rs1.getInt("ParentId") + "");

							mailSendInfo.add(dto);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int j = 0; j < mailSendInfo.size(); j++) {

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();

			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);
			long startdate = Long.valueOf(Common.getGMTTimeStampFromDate(day
					- 1 + "-" + String.valueOf(month + 1) + "-" + year
					+ " 00:00 am"));
			long enddate = Long.valueOf(Common.getGMTTimeStampFromDate(day
					+ "-" + String.valueOf(month + 1) + "-" + year
					+ " 00:00 am"));

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Device trip report"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 20);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle.setFillForegroundColor(IndexedColors.CORAL
					.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			Row row = sheet.createRow(0);

			for (int i = 0; i < deviceInfoList.size(); i++) {
				try {
					row = sheet.createRow(sheet.getLastRowNum() + 1);

					row.createCell(5);
					row.getCell(5).setCellValue(
							"Trip Report   for  "
									+ deviceInfoList.get(i).getName() + "("
									+ deviceInfoList.get(i).getDeviceID()
									+ ")  Date :-"
									+ Common.getDateFromLong(startdate));
					row.setRowStyle(HeadingStyle);

					row = sheet.createRow(sheet.getLastRowNum() + 1);
					row.setRowStyle(tripColumnStyle);

					Cell cell = row.createCell(0);

					cell.setCellValue("Trip#");
					cell = row.createCell(1);
					cell.setCellValue("Start Time	");
					cell = row.createCell(2);
					cell.setCellValue("Start Address");
					cell = row.createCell(3);
					cell.setCellValue("End Time");
					cell = row.createCell(4);
					cell.setCellValue("End Address");
					cell = row.createCell(5);
					cell.setCellValue("Stoppage min");
					cell = row.createCell(6);
					cell.setCellValue("Average speed");
					cell = row.createCell(7);
					cell.setCellValue("Max Speed");
					cell = row.createCell(8);
					cell.setCellValue("Total Distance");

					// row = sheet.createRow(sheet.getLastRowNum()+1);

					List<DBObject> aggregationInput = new ArrayList<DBObject>();

					ArrayList<TripInfoDto> Triplist = new ArrayList<TripInfoDto>();
					String offset_otDevice = "GMT+05:30";
					TimeOffsetDTO obj = new TimeOffsetDTO();
					DBCursor cursor = null;

					try {

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_TRIPREPORT);

						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", deviceInfoList.get(i)
								.getDeviceID());

						BasicDBObject timestamp_whereQuery = new BasicDBObject(
								"source_info.timestamp", new BasicDBObject(
										"$gte", startdate).append("$lt",
										enddate));

						BasicDBList And_Milage = new BasicDBList();
						And_Milage.add(timestamp_whereQuery);
						And_Milage.add(device_whereQuery);
						DBObject Total_Milage_query = new BasicDBObject("$and",
								And_Milage);

						cursor = table.find(Total_Milage_query).sort(
								new BasicDBObject("source_info.timestamp", 1));
						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						// System.out.print("Cursor Count of u-p---"+
						// deviceInfoList.get(i).getDeviceID()+"----"+cursor.size());
						long total = cursor.count();

						if (cursor.size() != 0) {

							System.err
									.println("*-GetDirectTripReport----COunt---"
											+ cursor.size());
							int prev = 0;
							BasicDBObject prevDestDbobject = null;
							while (cursor.hasNext()) {

								TripInfoDto trip = new TripInfoDto();
								dbObject1 = (DBObject) cursor.next();

								BasicDBObject Sourceobj = (BasicDBObject) dbObject1
										.get("source_info");
								BasicDBObject Destobj = (BasicDBObject) dbObject1
										.get("dest_info");

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);

								cell.setCellValue(i + 1);
								cell = row.createCell(1);
								cell.setCellValue(Common.getDateCurrentTimeZone(Long
										.parseLong(Sourceobj
												.getString("timestamp"))));
								cell = row.createCell(2);
								cell.setCellValue(Sourceobj
										.getString("src_address"));
								cell = row.createCell(3);
								cell.setCellValue(Common.getDateCurrentTimeZone(Long
										.parseLong(Destobj
												.getString("timestamp"))));
								cell = row.createCell(4);
								cell.setCellValue(Destobj
										.getString("dest_address"));

								cell = row.createCell(5);

								if (prev > 0 && prevDestDbobject != null) {
									cell.setCellValue(((Long
											.parseLong(Sourceobj
													.getString("timestamp")) - Long
											.parseLong(prevDestDbobject
													.getString("timestamp"))) / 60)
											+ " min ");

								} else {
									cell.setCellValue("--");

								}

								cell = row.createCell(6);
								cell.setCellValue(String.format("%.2f",
										dbObject1.get("avgspeed")));
								cell = row.createCell(7);
								cell.setCellValue(dbObject1.get("maxspeed")
										.toString());
								cell = row.createCell(8);
								cell.setCellValue(dbObject1.get("totalkm")
										.toString());

								prevDestDbobject = Destobj;

								prev++;

							}

							row = sheet.createRow(sheet.getLastRowNum() + 1);
							row.createCell(5);

						} else {
							row = sheet.createRow(sheet.getLastRowNum() + 1);
							cell = row.createCell(5);
							cell.setCellValue("-------------Trip Report Not Found--------------------");
							row = sheet.createRow(sheet.getLastRowNum() + 1);
							row.createCell(5);

						}
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MongoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						if (cursor != null) {
							cursor.close();

						}
					}

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MongoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("ttttt--send mail---"
						+ deviceInfoList.size() + "----");

			}

			if (deviceInfoList.size() > 0) {
				String outFileName = mailSendInfo.get(j).getDept() + "_"
						+ "Device_Trip_Report_"
						+ Common.getDateFromLong(startdate).replace(":", "-")
						+ ".xlsx";
				try {
					String file = Common.ServerDevice_Trip_FileUplod_Path
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));

					/*
					 * Set<PosixFilePermission> ownerWritable =
					 * PosixFilePermissions.fromString("rw-r--r--");
					 * FileAttribute<?> permissions =
					 * PosixFilePermissions.asFileAttribute(ownerWritable);
					 * Files.createFile(file_l.toPath(), permissions);
					 * 
					 * 
					 * FileOutputStream fos = new FileOutputStream(file_l);
					 */

					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					String sendemail = "";
					for (String emaString : mailSendInfo.get(j).getEmailIds()) {
						if (emaString.trim().length() > 0) {
							if (sendemail.length() == 0) {
								sendemail = emaString.trim();
							} else {
								sendemail = sendemail + "," + emaString.trim();

							}

						}
					}
					SendEmail mail = new SendEmail();

					mail.sendDeviceTripMailToDepartmentEmp(sendemail, file,
							mailSendInfo.get(j).getDept());

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		}

		return msgo;
	}

	public MessageObject UpdateNameOfDevices(Connection con, String name,
			String idno, String parentId) {

		String[] name_array = name.split(",");
		String[] idno_array = idno.split(",");

		MessageObject msgo = new MessageObject();
		for (int i = 0; i < idno_array.length; i++) {

			try {

				// ////System.err.print(emailid);
				int result = 0;

				java.sql.CallableStatement ps = con
						.prepareCall("{call UpdateDeviceName(?,?,?)}");
				ps.setString(1, name_array[i].trim());
				ps.setString(2, idno_array[i].trim());
				ps.setInt(3, Integer.parseInt(parentId));
				System.err.println("UpdateNameOfDevices==" + ps
						+ name_array[i].trim() + "---" + idno_array[i].trim());

				result = ps.executeUpdate();

				if (result == 0) {
					msgo.setError("true");
					msgo.setMessage("User is not Update successfully");
				} else {
					// //System.err.println("Error=="+result);
					msgo.setError("false");
					msgo.setMessage("User is Update successfully");
				}

				/*
				 * psx.setString(1, name_array[i].trim()); psx.setInt(2,534981);
				 * psx.setString(3, idno_array[i].trim());
				 */

			} catch (Exception e) {
				msgo.setError("true");
				msgo.setMessage("Error :" + e.getMessage());
				e.printStackTrace();
			}
		}

		return msgo;

	}

	public ArrayList<DevicelistDetails> getTrackInfoForCustomUser(
			Connection con, String parentId) {
		ArrayList<DevicelistDetails> AllDeviceDataList = new ArrayList<>();

		try {
			java.sql.CallableStatement ps = con
					.prepareCall("{call GetTrackInfoForCustomUser(?)}");
			ps.setString(1, parentId);
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {

					DevicelistDetails dto = new DevicelistDetails();
					dto.setStudentID(rs.getString("StudentID"));
					dto.setDeviceID(rs.getString("DeviceID"));
					dto.setName("" + rs.getString("Name"));
					dto.setPhoto(rs.getString("Photo"));
					dto.setExpiary_date(rs.getString("ExpiryDate"));
					dto.setStatus(rs.getString("ActivationStatus"));
					dto.setRemaining_days_to_expire(rs
							.getString("ActivationStatus"));
					dto.setType(rs.getString("Type"));
					AllDeviceDataList.add(dto);

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return AllDeviceDataList;
	}

	public MessageObject saveTrackCustomhierarchyUserNew(Connection con,
			String name, String mobileNo, String emailId, String parentId) {

		MessageObject msgo = new MessageObject();

		String[] nameArray = name.split(",");
		String[] mobileNoArray = mobileNo.split(",");

		String[] emailIdArray = emailId.split(",");

		for (int i = 0; i < nameArray.length; i++)
			try {

				java.sql.CallableStatement ps = con
						.prepareCall("{call SaveTrackCustomhierarchyUserNew(?,?,?,?)}");

				ps.setString(1, nameArray[i].trim());
				ps.setString(2, mobileNoArray[i].trim());
				ps.setString(3, emailIdArray[i].trim());
				ps.setString(4, parentId);

				int result = ps.executeUpdate();
				if (result == 0) {
					msgo.setError("true");
					msgo.setMessage("SaveTrackCustomhierarchyUserNew not Inserted");
				} else {
					// //System.err.println("Error=="+result);
					msgo.setError("false");
					msgo.setMessage("SaveTrackCustomhierarchyUserNew Inserted Successfully");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return msgo;
	}

	public MessageObject SaveRailwayPetrolmanBeatPath(Connection con,
			String studentID, String kmFromTo, String totalKmCover,
			String sheetNo, String sectionName, String totalTrip) {

		MessageObject msgo = new MessageObject();

		String[] Students = studentID.split(",");
		String[] TotalKmCover = totalKmCover.split(",");

		String[] KmFromTo = kmFromTo.split(",");

		for (int j = 0; j < Students.length; j++) {
			String[] KmStartEnd = KmFromTo[j].split("-");

			// for(int i=1;i<=Integer.parseInt(totalTrip);i++)
			for (int i = 5; i <= 12; i++)

				try {

					// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
					java.sql.CallableStatement ps = con
							.prepareCall("{call SaveRailwayPetrolmanBeatPath(?,?,?,?,?,?,?,?)}");

					ps.setString(1, "-" + Students[j].trim());
					ps.setInt(2, i);
					ps.setString(3, KmFromTo[j].trim().toString());
					if (i % 2 == 0) {
						ps.setDouble(4, Double.parseDouble(KmStartEnd[1].trim()
								.toString()));
						ps.setDouble(5, Double.parseDouble(KmStartEnd[0].trim()
								.toString()));
					} else {
						ps.setDouble(4, Double.parseDouble(KmStartEnd[0].trim()
								.toString()));
						ps.setDouble(5, Double.parseDouble(KmStartEnd[1].trim()
								.toString()));
					}

					ps.setDouble(6, Double.parseDouble(TotalKmCover[j].trim()
							.toString()));
					// System.err.println("-----------TotalKmCover------------------"+
					// Double.parseDouble(TotalKmCover[j].trim().toString()));

					ps.setInt(7, Integer.parseInt(sheetNo));
					ps.setString(8, sectionName);

					int result = ps.executeUpdate();
					if (result == 0) {
						msgo.setError("true");
						msgo.setMessage("SaveRailwaypatrolmanBeatPath not Inserted");
					} else {
						// //System.err.println("Error=="+result);
						msgo.setError("false");
						msgo.setMessage("SaveRailwaypatrolmanBeatPath Inserted Successfully");
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("SaveRailwaypatrolmanBeatPath not Inserted");
				}
		}
		return msgo;
	}

	public MessageObject SaveRailwayKeymanBeatPath(Connection con,
			String studentID, String kmFrom, String kmTo, String sectionName,
			String parentId) {

		MessageObject msgo = new MessageObject();

		String[] Students = studentID.split(",");
		String[] kmFroms = kmFrom.split(",");

		String[] kmTos = kmTo.split(",");
		String[] sectionNames = sectionName.split(",");

		System.err.println("SaveRailwayKeymanBeatPath--"+Students.length);
		for (int j = 0; j < Students.length; j++) {

			// for(int i=1;i<=Integer.parseInt(totalTrip);i++)
			// for(int i=5;i<=12;i++)

			try {
				System.out.println("ParentId--"+parentId);

				// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
				java.sql.CallableStatement ps = con
						.prepareCall("{call SaveRailwayKeymanBeatPath(?,?,?,?,?)}");

				ps.setString(1, "-" + Students[j].trim());
				ps.setDouble(2,Double.parseDouble(kmFroms[j].trim().toString()));
				ps.setDouble(3, Double.parseDouble(kmTos[j].trim().toString()));

				ps.setString(4, sectionNames[j].trim());
				//ps.setString(4, "");

				ps.setInt(5, Integer.parseInt(parentId));

				int result = ps.executeUpdate();
				if (result == 0) {
					msgo.setError("true");
					msgo.setMessage("SaveRailwayKeymanBeatPath not Inserted");
				} else {
					// //System.err.println("Error=="+result);
					msgo.setError("false");
					msgo.setMessage("SaveRailwayKeymanBeatPath Inserted successfully");
				}
			} catch (Exception e) {
				e.printStackTrace();
				msgo.setError("true");
				msgo.setMessage("SaveRailwaypatrolmanBeatPath not Inserted");
			}
		}
		
		return msgo;
	}

	public MessageObject GenerateExceptionReportPetrolmanBeatPath(
			Connection con, DB mongoconnection, String parentId, int dayCount,
			Boolean isSendMail, int seasonId, int timeTolerance, Double distanceTolerance)
	{

		MessageObject msgo = new MessageObject();

		//for (int pastday = 74;pastday >45; pastday--) {
		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionReportpatrolmanBeatPath ---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();
		
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-dayCount;
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		

		long startdate = Long.valueOf(Common.getGMTTimeStampFromDate(day
				+ "-" + String.valueOf(month + 1) + "-" + year
				+ " 00:00 am"));
		long enddate = Long.valueOf(Common.getGMTTimeStampFromDate(day
				+ "-" + String.valueOf(month + 1) + "-" + year
				+ " 00:00 am"));


		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

		// ///////////////////////////////********************************/////////////////////////////
		// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("Patrolman Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"PatrolMan Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						6));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(6);
		cell.setCellValue("Start with Battery 30%-60%");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 800);

		// /////////////////////////All Section End//////////////////////////////////////////////////
		
		
		//*******////////// All Section Count AlertForKeymanWorkStatusReport*/////////////////////////

		XSSFWorkbook workbookPatrolmanWorkStatusReportCount = new XSSFWorkbook();
		XSSFSheet sheetPatrolmanWorkStatusReportCount = workbookPatrolmanWorkStatusReportCount
				.createSheet(WorkbookUtil
						.createSafeSheetName("PatrolMan Status Report with count"));
		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReportCount = workbookPatrolmanWorkStatusReportCount
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReportCount
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReportCount
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		 font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		 tripColumnStyleAlertForKeymanWorkStatusReport = workbookPatrolmanWorkStatusReportCount
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		XSSFCellStyle wrap_styleAlertForKeymanWorkStatusReportCount = workbookPatrolmanWorkStatusReportCount
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReportCount.setWrapText(true); // Set
															

		row = sheetPatrolmanWorkStatusReportCount.createRow(0);
		row = sheetPatrolmanWorkStatusReportCount
				.createRow(sheetPatrolmanWorkStatusReportCount.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetPatrolmanWorkStatusReportCount
				.createRow(sheetPatrolmanWorkStatusReportCount.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetPatrolmanWorkStatusReportCount
				.createRow(sheetPatrolmanWorkStatusReportCount.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"PatrolMan Work Status Count Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetPatrolmanWorkStatusReportCount
				.addMergedRegion(new CellRangeAddress(
						sheetPatrolmanWorkStatusReportCount.getLastRowNum(),
						sheetPatrolmanWorkStatusReportCount.getLastRowNum(), 0,
						4));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReportCount);

		row = sheetPatrolmanWorkStatusReportCount
				.createRow(sheetPatrolmanWorkStatusReportCount.getLastRowNum() + 1);

		 cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		/*cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);*/
		cell = row.createCell(1);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(3);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(4);
		cell.setCellValue("Total Device");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 800);
	/*	cell = row.createCell(6);
		cell.setCellValue("Section Status");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 500);*/

		// ///////////////////////////////////All Section COunt// End//////////////////////////////////////////////////////////

		int divDeviceOffCount=0;
		int divDeviceBeatNotCoverCount=0;
		int divDeviceBeatCoverCount=0;

		
		
		
		
		for (int j = 0; j < mailSendInfo.size(); j++) {
//	 for (int j =8; j <=8; j++) {
		//for (int j = mailSendInfo.size()-1; j <=mailSendInfo.size()-1;j++) {

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayPetrolmanTripsBeatsList = new ArrayList<>();
			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			Set<String> KeyMan60PercentBattery_Set = new HashSet<String>();

			ArrayList<Short> rowHeight = new ArrayList<>();
			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Patrolman Exception Trip Report"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 20);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			
			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);
			int keyMancount = 0;

			for (int i = 0; i < deviceInfoList.size(); i++) {
				if (deviceInfoList.get(i).getName().startsWith("P/")) {
//				if (deviceInfoList.get(i).getName().startsWith("P/")
//								&&deviceInfoList.get(i).getName().contains("-236")) {		

					 int deviceLoactionCount=0;
					
					Double startBatteryStatus = getStartBatteryStatus(
							mongoconnection, deviceInfoList.get(i).getDeviceID(),
							startdate);
					Double endBatteryStatus = getEndBatteryStatus(mongoconnection,
							deviceInfoList.get(i).getDeviceID(), startdate);
					

					RailwayPetrolmanTripsBeatsList
							.removeAll(RailwayPetrolmanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfPetrolmanWithSeasonId(?,?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());
						psgettrip.setInt(2, seasonId);
						ResultSet rsgertrip = psgettrip.executeQuery();
						System.err.println("GetTripSheduleOfPetrolmanWithSeasonId=----"+rsgertrip);
						
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));
								dto.setFkTripMasterId(rsgertrip
										.getInt("fk_TripMasterId"));
								dto.setKmFromTo(rsgertrip.getString("KmFromTo"));
								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));
								dto.setTotalKmCover(rsgertrip
										.getDouble("TotalKmCover"));
								dto.setInitialDayStartTime(rsgertrip
										.getString("InitailDayStratTime"));
								dto.setSheetNo(rsgertrip.getInt("SheetNo"));
								dto.setSectionName(rsgertrip
										.getString("SectionName"));
								dto.setTripName(rsgertrip.getString("TripName"));
								dto.setTripTimeShedule(rsgertrip
										.getString("TripTimeShedule"));
								dto.setTripStartTimeAdd(rsgertrip
										.getInt("TripStartTimeAdd"));
								dto.setTripSpendTimeIntervalAdd(rsgertrip
										.getInt("TripSpendTimeIntervalAdd"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));

								RailwayPetrolmanTripsBeatsList.add(dto);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}


					
					
					/*ArrayList totalLocationList=GetHistorydata(mongoconnection, deviceInfoList.get(i).getDeviceID()+"", DayStartWorkTime+"");

					if (totalLocationList.size()==0)
					{
						KeyManOffDevice_Set.add(deviceInfoList
								.get(i).getName());						
					}*/					

//					System.err.println("totalLocationList----size--"+totalLocationList.size());
					for (int r = 0; r < RailwayPetrolmanTripsBeatsList.size(); r++) {
//						for (int r = 0; r <=0; r++) {

						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayPetrolmanTripsBeatsList
								.get(r);
						
						/*long timeTolerance = railMailSendInfoDto
								.getTripSpendTimeIntervalAdd()/3;
						*/


						long triptStartTime = DayStartWorkTime
								+ railMailSendInfoDto.getTripStartTimeAdd()
								- timeTolerance;
						long triptEndTime = DayStartWorkTime
								+ railMailSendInfoDto.getTripStartTimeAdd()
								+ railMailSendInfoDto
										.getTripSpendTimeIntervalAdd()
								+ timeTolerance;

						 System.out.println("triptStartTime----"+"TRIP--"+r+"----"
						 + Common.getDateCurrentTimeZone(triptStartTime)
						 + "========triptEndTime----"
						 + Common.getDateCurrentTimeZone(triptEndTime));
						ArrayList<String> kmStartLatSet = new ArrayList<>(), kmStartLangSet = new ArrayList<>(), kmEndLatSet = new ArrayList<>(), kmEndLangSet = new ArrayList<>();

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.parseLong("355488020181042"));

						BasicDBObject timestamp_whereQuery = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										triptStartTime).append("$lt",
										triptEndTime));
						
						System.out.println("triptStartTime--"+triptStartTime+"--triptEndTime---"+triptEndTime);
						
						BasicDBList And_Milage = new BasicDBList();
						And_Milage.add(timestamp_whereQuery);
						And_Milage.add(device_whereQuery);
						DBObject Total_Milage_query = new BasicDBObject("$and",
								And_Milage);

						DBCursor cursor = table.find(Total_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + Total_Milage_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							// System.err.println("*--listObjects---COunt---" +
							// cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();
							deviceLoactionCount=listObjects.size();

						}
				

						Double minDistanceCal = 0.0;
						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --start-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = locationNearbyKMPatrolMan(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"P",triptStartTime,(triptStartTime+timeTolerance*2),railMailSendInfoDto.getTripSpendTimeIntervalAdd(),timeTolerance);
								
								tripStartLocation.setTripStartExpectedTime(triptStartTime+ timeTolerance);
								tripStartLocation.setTripEndExpectedTime(triptEndTime- timeTolerance);
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation = locationNearbyKMPatrolMan(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"P",(triptEndTime-timeTolerance*2),triptEndTime,railMailSendInfoDto.getTripSpendTimeIntervalAdd(),timeTolerance);

								tripEndLocation
										.setTripStartExpectedTime(triptStartTime
												+ timeTolerance);
								tripEndLocation
										.setTripEndExpectedTime(triptEndTime
												- timeTolerance);
								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								for (DBObject dbObjectSpeed : listObjects) {
									int SpeedcCheck = (int) dbObjectSpeed.get("speed");
									Long timeloc=Long.parseLong(dbObjectSpeed.get("timestamp")+"");
								
							
									if(SpeedcCheck>8&&(timeloc>triptStartTime+timeTolerance && timeloc<triptEndTime-timeTolerance)){
										exDto.setMaxSpeed(SpeedcCheck);
										exDto.setMaxSpeedTime(timeloc);
										}
									
									}

			
								exceptiionalTrip.add(exDto);
							} else {

								exceptiionalTrip.add(null);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exDto.setLocationSize(0);

							exceptiionalTrip.add(exDto);

						}

					}

					// //////////////////////////////////
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null
							) {
						StringBuilder remark = new StringBuilder();
						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								"Exception Report for "
										+ deviceInfoList.get(i).getName() + "("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")  Date :-"
										+ Common.getDateFromLong(startdate));
						row.getCell(0).setCellStyle(HeadingStyle);

						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 11));


						// Start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((startBatteryStatus / 6) * 100))
								+ " %");
						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 11));
						// End Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("End Battery status :- "
								+ df2.format(((endBatteryStatus / 6) * 100))
								+ " %");
						row.getCell(0).setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 11));
						
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");
						row.getCell(0).setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 11));

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						cell = row.createCell(0);

						cell.setCellValue("Trip#");
						cell.setCellStyle(tripColumnStyle);
						cell = row.createCell(1);
						cell.setCellValue("Start Time	");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue("Expected Start Time");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue("Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(4);
						cell.setCellValue("Expected Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(5);
						cell.setCellValue("End Time	");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(6);
						cell.setCellValue("Expected End Time");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(7);
						cell.setCellValue("End Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(8);
						cell.setCellValue("Expected end Beat");
						cell.setCellStyle(tripColumnStyle);


						/*
						 * cell = row.createCell(9);
						 * cell.setCellValue("Stoppage min"); cell =
						 * row.createCell(10);
						 * cell.setCellValue("Average speed");
						 */
						cell = row.createCell(9);
						cell.setCellValue("Max Speed");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(10);
						cell.setCellValue("Total Distance cover");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(11);
						cell.setCellValue("Trip Detail Remark");
						cell.setCellStyle(tripColumnStyle);
						
						for (ExceptionReortsTrip exceptionReortsTrip :  
							exceptiionalTrip) {
							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null) {
								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									String DetailRemark="";
									double kmcover = (exceptionReortsTrip
											.getTripStart()
											.getStartkmBeatActual() - exceptionReortsTrip
											.getTripEnd().getEndKmBeatActual());
									if (kmcover < 0)
										kmcover = -1 * kmcover;

									double expactedKmCover = exceptionReortsTrip
											.getTripStart()
											.getEndKmBeatExpected()
											- exceptionReortsTrip
													.getTripStart()
													.getStartkmBeatExpected();
									if (expactedKmCover < 0)
										expactedKmCover = -1 * expactedKmCover;
/*
									if (exceptionReortsTrip.getTripStart()
											.getTripStartExpectedTime()
											- exceptionReortsTrip
													.getTripStart()
													.getTimestamp() > timeTolerance)
										remark.append("\t "
												+ exceptionReortsTrip
														.getTripNo()
												+ " delayed.");
*/
									if (kmcover < expactedKmCover
											- distanceTolerance && exceptionReortsTrip.getTripStart().getTotal_distance()>1)
										{
										remark.append("\tTotal beats not completed in Trip "
										
												+ exceptionReortsTrip
														.getTripNo() + ".");
										DetailRemark="Total beat route distance not cover.";

										}
									else if( exceptionReortsTrip.getTripStart().getTotal_distance()<1)
										{
										remark.append("\tDevice was not moved in Trip "
												+ exceptionReortsTrip
														.getTripNo() + ".");
										DetailRemark="Device was not moved in Trip ";
										}
									
									
									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());

										cell = row.createCell(1);
										if (exceptionReortsTrip.getTripStart()
												.getTimestamp() > 0)
											cell.setCellValue(Common
													.getDateCurrentTimeZone(exceptionReortsTrip
															.getTripStart()
															.getTimestamp()));
										else if(exceptionReortsTrip.getTripStart().getTotal_distance()<1)
											{
											cell.setCellValue("Device was not moved");
											DetailRemark="Device was not moved in Trip ";

											}
										else
										{
										
										cell.setCellValue("Not found");
										if (exceptionReortsTrip.getTripStart()
											.getTimestamp()== 0)
//										remark.append("\nDevice not found near by beat at expected time may be cover beat early or late in Trip "+ exceptionReortsTrip.getTripNo());
											DetailRemark="Device not found near by beat at expected time may be beat cover early or late in Trip";

										
										}
										cell = row.createCell(2);
										cell.setCellValue(Common
												.getDateCurrentTimeZone(exceptionReortsTrip
														.getTripStart()
														.getTripStartExpectedTime()));

										cell = row.createCell(3);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));
										cell = row.createCell(4);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected()));

										cell = row.createCell(5);
										if (exceptionReortsTrip.getTripEnd()
												.getTimestamp() > 0)
											{
											cell.setCellValue(Common
													.getDateCurrentTimeZone(exceptionReortsTrip
															.getTripEnd()
															.getTimestamp()));
											if (exceptionReortsTrip
													.getTripEnd()
													.getTimestamp()<exceptionReortsTrip
													.getTripStart()
													.getTimestamp()) {
												
//												remark.append("\nDevice moved in opposite direction ie."
//														+ "end beat to start beat in Trip No "+ exceptionReortsTrip.getTripNo());
												DetailRemark="Device not found near by beat at expected time may be beat cover early or late in Trip";

												
											}
											
											
											}
										else if(exceptionReortsTrip.getTripStart().getTotal_distance()<1)
											{
											cell.setCellValue("Device was not moved");
											DetailRemark="Device was not moved";

											}
										else
											{
											
											cell.setCellValue("Not found");
											if (exceptionReortsTrip.getTripEnd()
												.getTimestamp()== 0&&exceptionReortsTrip.getTripStart()
												.getTimestamp()> 0)
//											remark.append("\nDevice not found near by beat at expected time may be beat cover beat early or late in Trip "+ exceptionReortsTrip.getTripNo());
												DetailRemark="Device not found near by beat at expected time may be beat cover early or late in Trip";
											
											
											}


										cell = row.createCell(6);
										cell.setCellValue(Common
												.getDateCurrentTimeZone(exceptionReortsTrip
														.getTripEnd()
														.getTripEndExpectedTime()));

										cell = row.createCell(7);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));
										cell = row.createCell(8);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatExpected()));

										/*
										 * cell = row.createCell(9);
										 * cell.setCellValue(""); cell =
										 * row.createCell(10);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 */
										cell = row.createCell(9);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart().getMaxSpeed());
										cell = row.createCell(10);
										cell.setCellValue(df2.format(kmcover));
										
									
										cell = row.createCell(11);
										cell.setCellValue(DetailRemark);
										
										if (DetailRemark.length()>0) {


											if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
											{
											KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
											KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
											
											}else{
												KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());

											}
											remark.append("\tTotal beats not completed in Trip "
													
												+ exceptionReortsTrip
														.getTripNo() + ".");
											
												row.getCell(3).setCellValue("-");
												row.getCell(7).setCellValue("-");
												row.getCell(10).setCellValue("-");

												
										}
										
										if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8&&exceptionReortsTrip.getMaxSpeed()>8) {
											KeyManOverspeedDevices_Set.add(deviceInfoList.get(i)
															.getName());
										}


										// System.out.println("  Here--*****************************************--------------------");
										//
										
										
										
										
										

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// Exception of location not found
					
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());
									// Exception of location not found
									if (exceptionReortsTrip != null
											&& exceptionReortsTrip
													.getLocationSize() == 0)
										try {
											// System.out.println(" **exceptionReortsTrip*******row******************"
											// +
											// "***-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size()+"-----"+exceptionReortsTrip.getLocationSize());

											if (deviceLoactionCount==0) {
												KeyManOffDevice_Set.add(deviceInfoList
														.get(i).getName());
												remark.append("\tDevice is Off");
												row = sheet
														.createRow(sheet.getLastRowNum() + 1);
												cell = row.createCell(0);
												cell.setCellValue(exceptionReortsTrip
														.getTripNo());
												cell.setCellStyle(loactionNotFoundColumnStyle);

												cell = row.createCell(1);
												cell.setCellValue("Device is Off : "
														+ exceptionReortsTrip.getTripNo()
														+ ".");
												cell.setCellStyle(loactionNotFoundColumnStyle);

											}else{
												remark.append("\tLocation not found for Trip no : "
														+ exceptionReortsTrip.getTripNo()
														+ ".");
												row = sheet
														.createRow(sheet.getLastRowNum() + 1);
												cell = row.createCell(0);
												cell.setCellValue(exceptionReortsTrip
														.getTripNo());
												cell.setCellStyle(loactionNotFoundColumnStyle);

												cell = row.createCell(1);
												cell.setCellValue("Location not found in shedule time for Trip no: "
														+ exceptionReortsTrip.getTripNo()
														+ ".");
												cell.setCellStyle(loactionNotFoundColumnStyle);
//												DetailRemark="Location not found in shedule time for Trip.";

												if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
												{
												KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
												KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
												}else{
													KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());

												}
											}
											
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						
							if (remark.toString().length() > 0)
							{
							remark_cell.setCellValue("Remark status :- "
									+ remark.toString());
							remark_cell.setCellStyle(error_remarkColumnStyle);

							if (!KeyManOffDevice_Set.contains(deviceInfoList.get(i)
											.getName())) {
								if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
								{
								KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
								KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
								}else{
									KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());

								}
								
							}
							
//							remark_cell.getRow().setHeight((short) ((Common.findWord(remark.toString(), "\n").size())*400));
						
							}
								
						else
							{
							if (exceptionReortsTrip.getTripStart()
									.getMaxSpeed() > 8&&exceptionReortsTrip.getMaxSpeed()>8) {
								KeyManOverspeedDevices_Set.add(deviceInfoList.get(i)
												.getName());
								remark_cell.setCellValue("\tRemark status :- Work done succesfully But found in Overspeed");

								remark_cell.setCellStyle(error_remarkColumnStyle);

							}else
							{
								remark_cell.setCellValue("Remark status :- All work done succesfully.");

							}
							
							if (!KeyManOffDevice_Set.contains(deviceInfoList.get(i)
									.getName()))
							KeyManCoverSucefullyDevices_Set
							.add(deviceInfoList.get(i)
									.getName());
							}
						}

						

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayPetrolmanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						/*
						 * if(railMailSendInfoDto.getKmStartLat()>0&&
						 * railMailSendInfoDto.getKmStartLang()>0
						 * &&railMailSendInfoDto
						 * .getKmEndLat()>0&&railMailSendInfoDto
						 * .getKmEndLang()>0)
						 */
						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null) {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0)
										.setCellValue(
												"Exception  Report for  "
														+ deviceInfoList.get(i)
																.getName()
														+ "("
														+ deviceInfoList.get(i)
																.getDeviceID()
														+ ")  Date :-"
														+ Common.getDateFromLong(startdate));
								row.setRowStyle(HeadingStyle);

								// Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Battery status :- Not found");
								row.setRowStyle(remarkColumnStyle);

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row.setRowStyle(remarkColumnStyle);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :- RDPS data not found.");
								
								if (!KeyManOffDevice_Set.contains(deviceInfoList.get(i)
										.getName()))
								{
									
									if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
									{
									KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
									KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
									}else{
										KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());

									}
								}

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0)
										.setCellValue(
												"Exception  Report for  "
														+ deviceInfoList.get(i)
																.getName()
														+ "("
														+ deviceInfoList.get(i)
																.getDeviceID()
														+ ")  Date :-"
														+ Common.getDateFromLong(startdate));
								row.setRowStyle(HeadingStyle);

								// Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Battery status :- Not found");
								row.setRowStyle(remarkColumnStyle);

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row.setRowStyle(remarkColumnStyle);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :-Trips not Shedule for this device.");

								if (deviceLoactionCount > 0) {
								if (!KeyManOffDevice_Set.contains(deviceInfoList.get(i)
										.getName()))
									KeyManExceptionalDevices_Set
									.add(deviceInfoList.get(i)
											.getName());
								}else {
									KeyManOffDevice_Set
									.add(deviceInfoList.get(i)
											.getName());
								}
								
								
								
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());
					
					if (startBatteryStatus < 3
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName()))
						KeyManLowBattery_Set.add(deviceInfoList.get(i)
								.getName());
					
					if ((startBatteryStatus==3||startBatteryStatus==4)
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName()))
						KeyMan60PercentBattery_Set.add(deviceInfoList.get(i)
								.getName());
					
				} else {
					// System.out.println("patrol Man found--------------------");
				keyMancount++;

				}
				
				
			}

			for (int s = 1; s < 15; s++) {
				sheet.autoSizeColumn(s);
			}

			if (deviceInfoList.size() > 0&&keyMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept().replace("/", "_") + "_"
						+ "Exception_Trip_Report_PartolMen"
						+ Common.getDateFromLong(startdate).replace(":", "-")	+ "_"+parentId
						+ ".xlsx";
				
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					
					if(isSendMail){
						String sendemail = "";
						for (String emaString : mailSendInfo.get(j).getEmailIds()) {
							sendemail = sendemail + "," + emaString.trim();
						}
						SendEmail mail = new SendEmail();
						mail.sendDeviceExceptionMailToJaipur(sendemail, file,
								mailSendInfo.get(j).getDept(),
								"Exception Trip Report of PatrolMan",
								"Exception Trip Report of PatrolMan", false
								,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());


					}
				
			} catch (Exception e) {

					e.printStackTrace();
				}

			}

		// /////ALl Section////////////////////////

			if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
					KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1){
				
			
			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReport);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			cell = row.createCell(1);
			cell.setCellValue(KeyManLowBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight
					.add((short) ((KeyManLowBattery_Set.toString()
							.replace("[", "").replace("]", "").toString()
							.length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(KeyManOffDevice_Set.toString().replace("[", "")
					.replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOffDevice_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));
			
			
			
			cell = row.createCell(6);
			cell.setCellValue(KeyMan60PercentBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); 
			
			short height_row = (short) ((KeyManLowBattery_Set.toString()
					.replace("[", "").replace("]", "").toString().length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);
			System.err.println("Row max height-----" + height_row);

			sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

			if (j == mailSendInfo.size() - 1) {
				String outFileNameAllSectionStatus = "All Section PartolMen status Report_"
						+ Common.getDateFromLong(startdate).replace(":", "-")	+ "_"+parentId
						+ ".xlsx";
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReport.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReport.close();
		
					if (isSendMail) {
						 String sendemail = getEmailIdForAllsectionReport(con,parentId);
							SendEmail mail = new SendEmail();
							System.err.println("Send mail All Section----" + sendemail);
								mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
										  "All Section"," PartolMen Work Status Report AllSection",
										  " PartolMen Work Status Report AllSection",true
											,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

						
					}
					
							
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}

			// /////////////////////*Alll Section////////////////////
			
			
			
			// /////ALl Section COunt////////////////////////
			
			
			 divDeviceOffCount=divDeviceOffCount+KeyManOffDevice_Set.size();
			 divDeviceBeatNotCoverCount=divDeviceBeatNotCoverCount+KeyManExceptionalDevices_Set.size();
			 divDeviceBeatCoverCount=divDeviceBeatCoverCount+KeyManCoverSucefullyDevices_Set.size();

			

			 if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
						KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1){
				 
			
			row = sheetPatrolmanWorkStatusReportCount
					.createRow(sheetPatrolmanWorkStatusReportCount
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	

			cell = row.createCell(1);
			cell.setCellValue(KeyManOffDevice_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	
			cell = row.createCell(2);
			cell.setCellValue(KeyManExceptionalDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			

			cell = row.createCell(3);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

			cell = row.createCell(4);
			cell.setCellValue((KeyManCoverSucefullyDevices_Set.size()+KeyManOffDevice_Set.size()+
					KeyManExceptionalDevices_Set.size())+"");
			
			
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			
	
			
			row.setHeight((short) 600);

			sheetPatrolmanWorkStatusReportCount.setColumnWidth(0, 3000);
			sheetPatrolmanWorkStatusReportCount.setColumnWidth(1, 3000);
			sheetPatrolmanWorkStatusReportCount.setColumnWidth(2, 3000);
			sheetPatrolmanWorkStatusReportCount.setColumnWidth(3, 3000);
			sheetPatrolmanWorkStatusReportCount.setColumnWidth(4, 3000);

			if (j == mailSendInfo.size() - 1) {
				
				//for total row
				row = sheetPatrolmanWorkStatusReportCount
						.createRow(sheetPatrolmanWorkStatusReportCount
								.getLastRowNum() + 1);
				row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				cell = row.createCell(0);
				cell.setCellValue("Total Device");

				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

				cell = row.createCell(1);
				cell.setCellValue(divDeviceOffCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				;

				cell = row.createCell(2);
				cell.setCellValue(divDeviceBeatNotCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				
				cell = row.createCell(3);
				cell.setCellValue(divDeviceBeatCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				

				cell = row.createCell(4);
				cell.setCellValue((divDeviceOffCount+divDeviceBeatCoverCount+
						divDeviceBeatNotCoverCount)+"");
				
				
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);


				
				row.setHeight((short) 600);

				sheetPatrolmanWorkStatusReportCount.setColumnWidth(0, 3000);
				sheetPatrolmanWorkStatusReportCount.setColumnWidth(1, 3000);
				sheetPatrolmanWorkStatusReportCount.setColumnWidth(2, 3000);
				sheetPatrolmanWorkStatusReportCount.setColumnWidth(3, 3000);
				sheetPatrolmanWorkStatusReportCount.setColumnWidth(4, 3000);
				
				String outFileNameAllSectionStatus = "All Section PatrolMan status device count Report_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_"+parentId+ ".xlsx";
				
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookPatrolmanWorkStatusReportCount.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookPatrolmanWorkStatusReportCount.close();

					String sendemail = getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					/*if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," PatrolMan Work Status device count Report AllSection",
								  " PatrolMan Work Status device count Report AllSection",true	
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					}*/
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			

				}
			 }

			// /////////////////////*Alll Section COunt end ////////////////////
			
			
			///Insert Datewise Saction Data
	if (isSendMail) 
	try {

					// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
					java.sql.CallableStatement ps = con
							.prepareCall("{call SaveDatewiseExceptionReport(?,?,?,?,?,?,?,?)}");

					
					
					ps.setInt(1, Integer.parseInt(parentId));
					ps.setString(2,DayStartWorkTime+"");
					ps.setString(3, KeyManLowBattery_Set.toString().replace("[", "")
							.replace("]", ""));

					ps.setString(4,KeyManOffDevice_Set.toString()
							.replace("[", "").replace("]", ""));

					ps.setString(5, KeyManExceptionalDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(6, KeyManOverspeedDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(7,mailSendInfo.get(j).getDept() );
					ps.setString(8, KeyManCoverSucefullyDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					int result = ps.executeUpdate();
					if (result == 0) {
						msgo.setError("true");
						msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
					} else {
						// //System.err.println("Error=="+result);
						msgo.setError("false");
						msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
				}
			}
		//}

		return msgo;

	}

	

	private int getNearByKMStartEnd(DBObject dbObject,
			ArrayList<String> kmStartLatSet, ArrayList<String> kmStartLangSet) {
		// TODO Auto-generated method stub

		BasicDBObject locobj = (BasicDBObject) dbObject.get("location");
		double lat1 = locobj.getDouble("lat");
		double lon1 = locobj.getDouble("lon");
		double earthRadius = 3958.75;
		double minDistance = 0;
		int position = 0;
		for (int i = 0; i < kmStartLatSet.size(); i++) {
			double lat2 = Double.parseDouble(kmStartLatSet.get(i));
			double lon2 = Double.parseDouble(kmStartLangSet.get(i));

			double latDiff = Math.toRadians(lat2 - lat1);
			double lngDiff = Math.toRadians(lon2 - lon1);
			double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
					+ Math.cos(Math.toRadians(lon1))
					* Math.cos(Math.toRadians(lat2)) * Math.sin(lngDiff / 2)
					* Math.sin(lngDiff / 2);
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			double distance = earthRadius * c;

			System.err.println("lat1--------" + lat1 + "---lan1--" + lon1
					+ "-lat2--" + lat2 + "-lon2--" + lon2);

			System.err.println("distance--------" + i + "-----" + distance);

			if (i == 0)
				minDistance = distance;
			else if (distance < minDistance) {
				minDistance = distance;
				position = i;
			}

		}
		System.err
				.println("getNearByKMStartEnd-----------pos------" + position);
		return position;
	}

	public synchronized PoleNearByLocationDto locationNearbyKM(String event,
			double km, double lat1, double lon1,
			ArrayList<DBObject> location_list, long dayStartWorkTime,String devicetype) {
		PoleNearByLocationDto dto = new PoleNearByLocationDto();

		try{
			
		
		
		long startTime,endTime;
		
		if(devicetype.equalsIgnoreCase("K")){
			startTime=dayStartWorkTime+23400;
			endTime=dayStartWorkTime+37800;
					
		}else{
			startTime=dayStartWorkTime;
			endTime=dayStartWorkTime+37800;
		}
		// System.err.println("locationNearbyKM------------"+"km---"+km+"------"+lat1+","+lon1+"---");
		
		int MaxSpeed=0 ,MaxSpeedCopy= 0;
		int TotalSpeed = 0;
		StringBuilder speedbuild = new StringBuilder();
		Double Total_distance = 0.0;
		double earthRadius = 3958.75;
		double minDistance = 0;
		int position = 0;
		for (int i = 0; i < location_list.size(); i++) {

			BasicDBObject locobj = (BasicDBObject) location_list.get(i).get(
					"location");
			double lat2 = locobj.getDouble("lat");
			double lon2 = locobj.getDouble("lon");

			if (i > 0) {
				BasicDBObject prev_locobj = (BasicDBObject) location_list.get(
						i - 1).get("location");
				double caldist = distance(prev_locobj.getDouble("lat"),
						prev_locobj.getDouble("lon"), lat2, lon2, "K");
				if (!Double.isNaN(caldist))
					Total_distance = Total_distance + caldist;
			}

			double distance_calculated = distance(lat1, lon1, lat2, lon2, "K");
//			System.err.println("locationNearbyKM-----distance------"+i+"------"+
//			distance_calculated+"--"+lat1+","+lon1+"---"+lat2+","+lon2);
//			System.err.println("locationNearbyKM-----speed------"+i+"------"+
//					location_list.get(i).get("speed"));
			int SpeedcCheck = Integer.parseInt(location_list.get(i).get("speed")+"");

			if (i == 0) {
				minDistance = distance_calculated;
				MaxSpeed = SpeedcCheck;
				MaxSpeedCopy=SpeedcCheck;

			} else if (distance_calculated < minDistance) {
				minDistance = distance_calculated;
				position = i;
			}

			Long loactionTime = Long.parseLong(location_list.get(i).get(
					"timestamp")
					+ "");

			if (i == 0) {
				/*if (SpeedcCheck > MaxSpeed
						&& (loactionTime > dayStartWorkTime + 41400 &&
								loactionTime < dayStartWorkTime + 55800))
					MaxSpeed = SpeedcCheck;
				else*/ if (SpeedcCheck > MaxSpeed
						&& (loactionTime > startTime &&
								loactionTime < endTime))
					MaxSpeed = SpeedcCheck;

			} else if (i == 1) {
			/*	if (SpeedcCheck > MaxSpeed
						&& (loactionTime > dayStartWorkTime + 41400 
								&& loactionTime < dayStartWorkTime + 55800)
						)
					MaxSpeed = SpeedcCheck;
				else*/ if (SpeedcCheck > MaxSpeed
						&& (loactionTime >startTime
								&& loactionTime < endTime)
						)
					MaxSpeed = SpeedcCheck;

			} else if (i > 10) {
				/*if (SpeedcCheck > MaxSpeed
						&& (loactionTime > dayStartWorkTime + 41400 
								&& loactionTime < dayStartWorkTime + 55800)
								
						&& (int) location_list.get(i - 1).get("speed") > 8
						&& (int) location_list.get(i - 2).get("speed") > 8
						&& (int) location_list.get(i - 3).get("speed") > 8
						&& (int) location_list.get(i - 4).get("speed") > 8
						&& (int) location_list.get(i - 5).get("speed") > 8
						&& (int) location_list.get(i - 6).get("speed") > 8
						&& (int) location_list.get(i - 7).get("speed") > 8
						&& (int) location_list.get(i - 8).get("speed") > 8
						&& (int) location_list.get(i - 9).get("speed") > 8
						&& (int) location_list.get(i - 10).get("speed") > 8	
						)
					MaxSpeed = SpeedcCheck;
				else*/
				
				if (SpeedcCheck > MaxSpeed
						&& (loactionTime > startTime 
								&& loactionTime < endTime)
						&& (int) location_list.get(i - 1).get("speed") > 8
						&& (int) location_list.get(i - 2).get("speed") > 8
						&& (int) location_list.get(i - 3).get("speed") > 8
						&& (int) location_list.get(i - 4).get("speed") > 8
						&& (int) location_list.get(i - 5).get("speed") > 8
						&& (int) location_list.get(i - 6).get("speed") > 8
						&& (int) location_list.get(i - 7).get("speed") > 8
						&& (int) location_list.get(i - 8).get("speed") > 8
						&& (int) location_list.get(i - 9).get("speed") > 8
						&& (int) location_list.get(i - 10).get("speed") > 8	)
					MaxSpeed = SpeedcCheck;
				
//				if (SpeedcCheck<8&&SpeedcCheck > MaxSpeedCopy
//						&& (loactionTime > dayStartWorkTime + 41400 && loactionTime < dayStartWorkTime + 55800))
//					MaxSpeedCopy = SpeedcCheck;
//				else 
				if (SpeedcCheck<8&&SpeedcCheck > MaxSpeedCopy
						&& (loactionTime > startTime && loactionTime < endTime))
					MaxSpeedCopy = SpeedcCheck;

				
			}

			TotalSpeed = TotalSpeed + SpeedcCheck;
			speedbuild.append(SpeedcCheck + "").append(",");

			// if (event.equalsIgnoreCase("end"))
			// System.err.println(i+"-----------speed--"+SpeedcCheck+"--max-"+MaxSpeed+"---Location time-----"+Common.getDateCurrentTimeZone(loactionTime));

		}
		
		if (MaxSpeed==0) 
			MaxSpeed=MaxSpeedCopy;
		
		 System.err.println("-----------MaxSpeed--"+MaxSpeed+"--MaxSpeedCopy-"+MaxSpeedCopy);

		/*
		 * event+"----locationNearbyKM-----time------"+Common.getDateCurrentTimeZone
		 * (dayStartWorkTime+23400)+
		 * ","+Common.getDateCurrentTimeZone(dayStartWorkTime+37800)
		 * +"---lot2---"+Common.getDateCurrentTimeZone(dayStartWorkTime+41400)
		 * +","+Common.getDateCurrentTimeZone(dayStartWorkTime+55800)+
		 */// System.err.println(event+"----SpeedcCheck----------"+speedbuild.toString());

		// System.err.println("DIstttttttttttttttttt---------"+Total_distance);
		System.out.println(event
				+ "--------"
				+ km
				+ "----Min at position-"
				+ position
				+ "--dist--"
				+ minDistance
				+ "===time=="
				+ Common.getDateCurrentTimeZone(((BasicDBObject) location_list
						.get(position)).getLong("timestamp")));

		int meterConversion = 1609;
		BasicDBObject locobj2 = (BasicDBObject) location_list.get(position)
				.get("location");
		dto.setLang(locobj2.getDouble("lon"));
		dto.setLat(locobj2.getDouble("lat"));
		dto.setMinDistance((minDistance));
		dto.setSpeed((int) location_list.get(position).get("speed"));
		dto.setAvgSpeed(TotalSpeed / location_list.size());
		dto.setMaxSpeed(MaxSpeed);
		dto.setTotalspeed(TotalSpeed);
		if (event.equalsIgnoreCase("start")) {
			if (Total_distance > 1) {
				dto.setStartkmBeatExpected(km);
				if (Math.round(km) <= km) {
					dto.setStartkmBeatActual(km + minDistance);

				} else {
					dto.setStartkmBeatActual(km - minDistance);

				}

				dto.setTotal_distance(Total_distance);
				dto.setTimestamp(((BasicDBObject) location_list.get(position))
						.getLong("timestamp"));

			} else {
				dto.setStartkmBeatExpected(km);
				dto.setStartkmBeatActual(0);
				dto.setTotal_distance(Total_distance);
				dto.setTimestamp((long) 0);

			}

			// System.out.println(event+"-@@@@@@@@--dto.getStartkmBeatActual()-----"+dto.getStartkmBeatActual()+"---setStartkmBeatExpected-"
			// + dto.getStartkmBeatExpected() );

		} else {
			if (Total_distance > 1) {

				dto.setEndKmBeatExpected(km);
				if (Math.round(km) <= km)
					dto.setEndKmBeatActual(km + minDistance);
				else
					dto.setEndKmBeatActual(km - minDistance);

				dto.setTimestamp(((BasicDBObject) location_list.get(position))
						.getLong("timestamp"));

			} else {

				dto.setEndKmBeatExpected(km);
				dto.setEndKmBeatActual(0);
				dto.setTimestamp((long) 0);

			}

			// System.out.println(event+"--@@@@@@@@@-dto.getEndKmBeatActual()-----"+dto.getEndKmBeatActual()+"---getEndKmBeatExpected-"
			// + dto.getEndKmBeatExpected() );

		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return dto;
	}

	public Double getStartBatteryStatus(DB mongoconnection, String device,
			long startdate) {
		Double batterystatus = 0.0;
		long timestamp = 0;
		
		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_ALERT_MSG);

			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device", Long.parseLong(device));

			BasicDBObject timestamp_whereQuery = new BasicDBObject("timestamp",
					new BasicDBObject("$gte", startdate - 7200).append("$lt",
							startdate + 3600));
			BasicDBList And_Milage = new BasicDBList();
			And_Milage.add(timestamp_whereQuery);
			And_Milage.add(device_whereQuery);
			DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

			DBCursor cursor = table.find(Total_Milage_query);
			cursor.sort(new BasicDBObject("timestamp", 1)).limit(1);

			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			 System.out.print("getBatteryStatus Count of u-p---"
			+ cursor.size() + "  " + Total_Milage_query);
			long total = cursor.count();

			if (cursor.size() != 0) {

				 System.err.println("*-getBatteryStatus----COunt---"
				+ cursor.size());

				while (cursor.hasNext()) {

					DBObject dbObject = (DBObject) cursor.next();

					batterystatus = Double.parseDouble(dbObject
							.get("voltage_level") + "");
					timestamp=Long.parseLong(dbObject.get("timestamp")+"");
				}

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("");
		System.out.println("batterystatus-----"+batterystatus+"----timestamp---"+timestamp);

		return batterystatus;
	}

	public Double getEndBatteryStatus(DB mongoconnection, String device,
			long startdate) {
		Double batterystatus = 0.0;
		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_ALERT_MSG);

			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device", Long.parseLong(device));

			BasicDBObject timestamp_whereQuery = new BasicDBObject("timestamp",
					new BasicDBObject("$gte", startdate + 18000).append("$lt",
							startdate + 25200));
			BasicDBList And_Milage = new BasicDBList();
			And_Milage.add(timestamp_whereQuery);
			And_Milage.add(device_whereQuery);
			DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

			DBCursor cursor = table.find(Total_Milage_query);
			cursor.sort(new BasicDBObject("timestamp", -1)).limit(1);

			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			// System.out.print("getBatteryStatus Count of u-p---"
			// + cursor.size() + "  " + Total_Milage_query);
			long total = cursor.count();

			if (cursor.size() != 0) {

				// System.err.println("*-getBatteryStatus----COunt---"
				// + cursor.size());

				while (cursor.hasNext()) {

					DBObject dbObject = (DBObject) cursor.next();

					batterystatus = Double.parseDouble(dbObject
							.get("voltage_level") + "");

				}

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("");
		// System.out.println("batterystatus----------------------------------"+batterystatus);

		return batterystatus;
	}

	public Double getKeyManStartBatteryStatus(DB mongoconnection,
			String device, long startdate) {
		Double batterystatus = 0.0;
		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_ALERT_MSG);

			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device", Long.parseLong(device));

			BasicDBObject timestamp_whereQuery = new BasicDBObject("timestamp",
					new BasicDBObject("$gte", startdate).append("$lt",
							startdate + 43200));
			BasicDBList And_Milage = new BasicDBList();
			And_Milage.add(timestamp_whereQuery);
			And_Milage.add(device_whereQuery);
			DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

			DBCursor cursor = table.find(Total_Milage_query);
			cursor.sort(new BasicDBObject("timestamp", 1)).limit(1);

			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

//			System.out.print("getKeyManStartBatteryStatus Count of u-p---"
//					+ cursor.size() + "  " + Total_Milage_query);
			long total = cursor.count();

			if (cursor.size() != 0) {

				// System.err.println("*-getBatteryStatus----COunt---"
				// + cursor.size());

				while (cursor.hasNext()) {

					DBObject dbObject = (DBObject) cursor.next();

					batterystatus = Double.parseDouble(dbObject
							.get("voltage_level") + "");

//					System.err
//							.println("*-getKeyManStartBatteryStatus----timestamp---"
//									+ Common.getDateCurrentTimeZone(Long
//											.parseLong(dbObject
//													.get("timestamp")
//													.toString())));

				}

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("");
//		System.out.println("batterystatus----------------------------------"
//				+ batterystatus);

		return batterystatus;
	}

	public Double getKeyManEndBatteryStatus(DB mongoconnection, String device,
			long startdate) {
		Double batterystatus = 0.0;
		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_ALERT_MSG);

			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device", Long.parseLong(device));

			BasicDBObject timestamp_whereQuery = new BasicDBObject("timestamp",
					new BasicDBObject("$gte", startdate-14400).append("$lt",
							startdate+7200 ));// add 12 hr
			
			BasicDBList And_Milage = new BasicDBList();
			And_Milage.add(timestamp_whereQuery);
			And_Milage.add(device_whereQuery);
			DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

			DBCursor cursor = table.find(Total_Milage_query);
			cursor.sort(new BasicDBObject("timestamp", -1)).limit(1);

			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
//
//			System.out.print("getKeyManEndBatteryStatus Count of u-p---"
//					+ cursor.size() + "  " + Total_Milage_query);
			long total = cursor.count();

			if (cursor.size() != 0) {

				while (cursor.hasNext()) {

					DBObject dbObject = (DBObject) cursor.next();

					batterystatus = Double.parseDouble(dbObject
							.get("voltage_level") + "");

//					System.err
//							.println("*-getKeyManEndBatteryStatus----timestamp---"
//									+ Common.getDateCurrentTimeZone(Long
//											.parseLong(dbObject
//													.get("timestamp")
//													.toString())));

				}

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("");
		// System.out.println("batterystatus----------------------------------"+batterystatus);

		return batterystatus;
	}

	public MessageObject GenerateExceptionReportKeyManBeatPath(Connection con,
			DB mongoconnection, String parentId) {

		MessageObject msgo = new MessageObject();
		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionReportKeyManBeatPath ---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

		// ///////////////////////////////********************************/////////////////////////////
		// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("KeyMan Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"KeyMan Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						5));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);

		row.setHeight((short) 500);

		// ///////////////////////////////////All Section
		// End//////////////////////////////////////////////////////////

		for (int j = 0; j < mailSendInfo.size(); j++) {
		//	for (int j = 0; j <=2;j++) {

			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report of keymen"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyle.setAlignment(HorizontalAlignment.CENTER);
			font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);

			CellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap

			int petrolMancount = 0;
			

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for Keyman of section "
							+ mailSendInfo.get(j).getDept() + "  Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyle);
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 6));

			BasicDBList sectionDeviceList = new BasicDBList();

			for (int i = 0; i < deviceInfoList.size(); i++) {
				// for (int i = 4; i <=4; i++) {

				System.err.println(" **Device =---================"
						+ deviceInfoList.get(i).getName() + "\n");

				Double startBatteryStatus = getKeyManStartBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+23400);

				Double endBatteryStatus = getKeyManEndBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+23400);

				if (deviceInfoList.get(i).getName().startsWith("K/")) {
					BasicDBObject subdocument = new BasicDBObject();

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfKeyman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));

								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

								dto.setSectionName(rsgertrip
										.getString("SectionName"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));
								RailwayKeymanTripsBeatsList.add(dto);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					// long DayStartWorkTime=1547836200;
					// System.out.println("DayStartWorkTime----" +
					// DayStartWorkTime);

					Double distanceTolerance = 1.3;
					// Double startLatOfRouteKm = 0.0, startLangOfRouteKm = 0.0,
					// endLatOfRouteKm = 0.0, endLangOfRouteKm = 0.0;

					// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
					// RailwayPetrolmanTripsBeatsList) {
					for (int r = 0; r < RailwayKeymanTripsBeatsList.size(); r++) {
						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayKeymanTripsBeatsList
								.get(r);

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.parseLong("355488020181042"));

						/*
						 * BasicDBObject timestamp_whereQuery = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime).append("$lt",
						 * DayStartWorkTime+86400));
						 */

						/*Summer Time
						 * BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 23400).append("$lt",
										DayStartWorkTime + 37800));
						BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 41400).append("$lt",
										DayStartWorkTime + 55800));
										
*/
						//Winter Time
					/*	BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 18000).append("$lt",
										DayStartWorkTime + 36000));
						BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 43200).append("$lt",
										DayStartWorkTime + 54000));
*/
						//Winter Time all 8 hr calculated for trip complete
						BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 23400).append("$lt",
										DayStartWorkTime + 36000));
						BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 36000).append("$lt",
										DayStartWorkTime + 54000));

						/*
						 * BasicDBObject timestamp_whereQuery_morning = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+23400).append("$lt",
						 * DayStartWorkTime+37800)); BasicDBObject
						 * timestamp_whereQuery_afternoon = new BasicDBObject(
						 * "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+41400).append("$lt",
						 * DayStartWorkTime+55800));
						 */
						BasicDBList morn_Milage = new BasicDBList();
						morn_Milage.add(timestamp_whereQuery_morning);

						morn_Milage.add(device_whereQuery);
						DBObject morn_Milage_query = new BasicDBObject("$and",
								morn_Milage);

						BasicDBList after_Milage = new BasicDBList();
						after_Milage.add(timestamp_whereQuery_afternoon);
						after_Milage.add(device_whereQuery);
						DBObject after_Milage_query = new BasicDBObject("$and",
								after_Milage);

						BasicDBList Milage = new BasicDBList();
						Milage.add(morn_Milage_query);
						Milage.add(after_Milage_query);
						DBObject final_query = new BasicDBObject("$or", Milage);

						DBCursor cursor = table.find(final_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + final_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							System.err.println("*--listObjects---COunt---"
									+ cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();

						}

						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());
							;

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = locationNearbyKM(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"K");
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation = locationNearbyKM(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"K");
								if (tripEndLocation.getEndKmBeatActual() <= 0) {
									tripStartLocation.setStartkmBeatActual(0);

								}

								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								
								
								for (DBObject dbObject : listObjects) {
									int SpeedcCheck = (int) dbObject.get("speed");
									Long timeloc=Long.parseLong(dbObject.get("timestamp")+"");
								
							
									if(SpeedcCheck>8&&((timeloc>(DayStartWorkTime + 21600) && timeloc<(DayStartWorkTime+36000) ))){
										exDto.setMaxSpeed(SpeedcCheck);
										}
									
									}

			
								
								exceptiionalTrip.add(exDto);
							} else {

								exceptiionalTrip.add(null);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setLocationSize(0);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exDto.setMaxSpeed(0);
							exceptiionalTrip.add(exDto);
						}

					}
					System.out.println("\n\n");
					System.out
							.println("  Here--*************exceptiionalTrip****************************--------------------"
									+ exceptiionalTrip.size());
					System.out.println("\n\n");
					// //////////////////////////////////////////////

					// //////////////////////////////////
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null) {
						StringBuilder remark = new StringBuilder();
						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								deviceInfoList.get(i).getName() + "   ("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")");
						subdocument.put("device", deviceInfoList.get(i)
								.getDeviceID());
						subdocument.put("device_name", deviceInfoList.get(i)
								.getName());

						row.getCell(0).setCellStyle(HeadingStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));
						// start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((startBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("start_battery_status",
								df2.format(((startBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						// End Battery Staus
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("End Battery status :- "
								+ df2.format(((endBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("end_battery_status",
								df2.format(((endBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");

						remark_cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						cell = row.createCell(0);
						cell.setCellValue("Trip#");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(1);
						cell.setCellValue("Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue("Expected Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue("End Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(4);
						cell.setCellValue("Expected end Beat");
						cell.setCellStyle(tripColumnStyle);

						/*
						 * cell = row.createCell(5);
						 * cell.setCellValue("Avg speed");
						 * cell.setCellStyle(tripColumnStyle);
						 */

						cell = row.createCell(5);
						cell.setCellValue("Max speed");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(6);
						cell.setCellValue("Distance cover");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						for (ExceptionReortsTrip exceptionReortsTrip : exceptiionalTrip) {
							System.out
									.println(" **exceptiionalTrip=---================"
											+ new Gson()
													.toJson(exceptionReortsTrip));

							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null
									&& exceptionReortsTrip.getLocationSize() > 0) {

								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = 0;
									double expactedKmCover = 0;
									if (exceptionReortsTrip.getTripStart()
											.getStartkmBeatActual() != 0
											&& exceptionReortsTrip.getTripEnd()
													.getEndKmBeatActual() != 0
											&& exceptionReortsTrip
													.getTripStart()
													.getMinDistance() < 5
											&& exceptionReortsTrip.getTripEnd()
													.getMinDistance() < 5) {
										kmcover = (exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatActual() - exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatActual());
										if (kmcover < 0)
											kmcover = -1 * kmcover;
										expactedKmCover = exceptionReortsTrip
												.getTripStart()
												.getEndKmBeatExpected()
												- exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected();
										if (expactedKmCover < 0)
											expactedKmCover = -1
													* expactedKmCover;

										if (kmcover < expactedKmCover
												- distanceTolerance) {
											remark.append("\tTotal beats not completed in Trip "
													+ exceptionReortsTrip
															.getTripNo() + ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark_cell
													.setCellValue("Remark status :- All work done succesfully.");
											KeyManCoverSucefullyDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
										}

									} else {
										kmcover = 0;

										if (exceptionReortsTrip.getTripStart()
												.getTotal_distance() < 1) {
											remark.append("\tDevice is not moved "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else if (exceptionReortsTrip
												.getTripStart()
												.getMinDistance() > 5
												&& exceptionReortsTrip
														.getTripEnd()
														.getMinDistance() > 5) {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
											{
											KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
											KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
											}else{
												KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
	
											}
											
										}

									}

			
									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());
										subdocument
												.put("Tripno",
														exceptionReortsTrip
																.getTripNo());

										cell.setCellStyle(wrap_style);

										cell = row.createCell(1);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));
										subdocument
												.put("start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatActual()));
										cell.setCellStyle(wrap_style);

										cell = row.createCell(2);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatExpected());
										subdocument
												.put("expected_start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatExpected()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(3);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));
										subdocument.put("end_beat", df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(4);
										cell.setCellValue(exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatExpected());
										subdocument
												.put("expected_end_beat",
														df2.format(exceptionReortsTrip
																.getTripEnd()
																.getEndKmBeatExpected()));

										cell.setCellStyle(wrap_style);

										/*
										 * cell = row.createCell(5);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 * cell.setCellStyle(wrap_style);
										 */

										cell = row.createCell(5);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart().getMaxSpeed());
										subdocument.put("max_speed", df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getMaxSpeed()));

										if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8&&exceptionReortsTrip.getMaxSpeed()>8) {
											KeyManOverspeedDevices_Set.add(deviceInfoList.get(i)
															.getName());
										}

										cell.setCellStyle(wrap_style);

										cell = row.createCell(6);
										cell.setCellValue(df2.format(kmcover));
										subdocument.put("kmcover",
												df2.format(kmcover));
										cell.setCellStyle(wrap_style);

										// System.out.println("  Here--*****************************************--------------------");
										//

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// Exception of location not found
								if (exceptionReortsTrip != null
										&& exceptionReortsTrip
												.getLocationSize() == 0)
									try {
										// System.out.println(" **exceptionReortsTrip*******row******************"
										// +
										// "***-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size()+"-----"+exceptionReortsTrip.getLocationSize());

										KeyManOffDevice_Set.add(deviceInfoList
												.get(i).getName());

										remark.append("Device is Off");

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(1);
										cell.setCellStyle(loactionNotFoundColumnStyle);

										cell = row.createCell(1);
										cell.setCellValue("Device is Off");
										cell.setCellStyle(loactionNotFoundColumnStyle);
										sheet.addMergedRegion(new CellRangeAddress(
												sheet.getLastRowNum(), sheet
														.getLastRowNum(), 1, 3));

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

							}
						}

						if (remark.toString().length() > 0) {
							remark_cell.setCellValue("Remark status :- "
									+ remark.toString());
							subdocument.put("remark", remark.toString());
							remark_cell.setCellStyle(error_remarkColumnStyle);
						}

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayKeymanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null) {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(
										deviceInfoList.get(i).getName()
												+ "   ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ")");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :- RDPS data not found.");
								subdocument.put("remark",
										"RDPS data not found.");
								if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
								{
								KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
								KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
								}else{
									KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());

								}

								cell.setCellStyle(remarkColumnStyle);
								// cell.setCellStyle(error_remarkColumnStyle);

								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(

										deviceInfoList.get(i).getName()
												+ "  ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ") ");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :-Trips not Shedule for this device.");
								subdocument.put("remark",
										"Trips not Shedule for this device.");
								if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
								{
								KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
								KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
								}else{
									KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());

								}

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());
					if (startBatteryStatus < 3
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName()))
						KeyManLowBattery_Set.add(deviceInfoList.get(i)
								.getName());

					sectionDeviceList.add(subdocument);

				} else {
					// System.out.println("patrolman Man found--------------------");
					petrolMancount++;
				}

			}

			Maindocument.put("exceptional_device", sectionDeviceList);

			report_table.insert(Maindocument);

			sheet.setColumnWidth(0, 1300);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 2000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);

			if (deviceInfoList.size() > 0
					&& petrolMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept() + "_"
						+ "Exception_Trip_Report_KeyMan"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ "_" + parentId + ".xlsx";
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					String sendemail = "";
					for (String emaString : mailSendInfo.get(j).getEmailIds()) {
						sendemail = sendemail + "," + emaString.trim();
					}
					SendEmail mail = new SendEmail();

				 mail.sendDeviceExceptionMailToJaipur(sendemail, file,
					 mailSendInfo
					  .get(j).getDept(),"Exception Trip Report of keymen"
					  ,"Exception Trip Report of keymen",false
						,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					 
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// /////ALl Section////////////////////////

			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReport);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			cell = row.createCell(1);
			cell.setCellValue(KeyManLowBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight
					.add((short) ((KeyManLowBattery_Set.toString()
							.replace("[", "").replace("]", "").toString()
							.length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(KeyManOffDevice_Set.toString().replace("[", "")
					.replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOffDevice_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			short height_row = (short) ((KeyManLowBattery_Set.toString()
					.replace("[", "").replace("]", "").toString().length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);
			System.err.println("Row max height-----" + height_row);

			sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

			if (j == mailSendInfo.size() - 1) {
				String outFileNameAllSectionStatus = "All Section KeyMan status Report_"
						+ parentId
						+ "_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ ".xlsx";
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReport.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReport.close();
				
					 String sendemail = getEmailIdForAllsectionReport(con,parentId);
						SendEmail mail = new SendEmail();
						System.err.println("Send mail All Section----" + sendemail);
							mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
									  "All Section"," KeyMan Work Status Report AllSection",
									  " KeyMan Work Status Report AllSection",true
										,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					

					
				} catch (Exception e) {
					e.printStackTrace();
				}
			

			}

			// /////////////////////*Alll Section////////////////////
			
			
			///Insert Datewise Saction Data
			 
		
			 try {

					// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
					java.sql.CallableStatement ps = con
							.prepareCall("{call SaveDatewiseExceptionReport(?,?,?,?,?,?,?,?)}");

					
					ps.setInt(1, Integer.parseInt(parentId));
					ps.setString(2,DayStartWorkTime+"");
					ps.setString(3, KeyManLowBattery_Set.toString().replace("[", "")
							.replace("]", ""));

					ps.setString(4,KeyManOffDevice_Set.toString()
							.replace("[", "").replace("]", ""));

					ps.setString(5, KeyManExceptionalDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(6, KeyManOverspeedDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(7,mailSendInfo.get(j).getDept() );
					ps.setString(8, KeyManCoverSucefullyDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					int result = ps.executeUpdate();
					if (result == 0) {
						msgo.setError("true");
						msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
					} else {
						// //System.err.println("Error=="+result);
						msgo.setError("false");
						msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
				}

		}// //////////for section end

		return msgo;

	}

	public MessageObject UpdateDevicesSimNo(Connection con, String simno,
			String idno, String parentId) {

		String[] simno_array = simno.split(",");
		String[] idno_array = idno.split(",");

		MessageObject msgo = new MessageObject();
		for (int i = 0; i < idno_array.length; i++) {

			try {

				// ////System.err.UpdateDevicesIMEIAndSimNoprint(emailid);
				int result = 0;

				java.sql.CallableStatement ps = con
						.prepareCall("{call UpdateDeviceSimNo(?,?,?)}");
				ps.setString(1, simno_array[i].trim());
				ps.setString(2, "-" + idno_array[i].trim());
				ps.setInt(3, Integer.parseInt(parentId));
				System.err.println("UpdateDeviceSimNo==" + ps
						+ simno_array[i].trim() + "---" + idno_array[i].trim());

				result = ps.executeUpdate();

				if (result == 0) {
					msgo.setError("true");
					msgo.setMessage("User is not Update successfully");
				} else {
					// //System.err.println("Error=="+result);
					msgo.setError("false");
					msgo.setMessage("User is Update successfully");
				}

				/*
				 * psx.setString(1, name_array[i].trim()); psx.setInt(2,534981);
				 * psx.setString(3, idno_array[i].trim());
				 */

			} catch (Exception e) {
				msgo.setError("true");
				msgo.setMessage("Error :" + e.getMessage());
				e.printStackTrace();
			}
		}

		return msgo;

	}

	public MessageObject UpdateParentName(Connection con, String name,
			String parentEmail) {

		String[] name_array = name.split(",");
		String[] parentEmail_array = parentEmail.split(",");

		MessageObject msgo = new MessageObject();
		for (int i = 0; i < name_array.length; i++) {

			try {

				// ////System.err.print(emailid);
				int result = 0;

				java.sql.CallableStatement ps = con
						.prepareCall("{call UpdateParentName(?,?)}");
				ps.setString(1, name_array[i].trim());
				ps.setString(2, parentEmail_array[i].trim());
				System.err.println("UpdateParentName==" + ps
						+ name_array[i].trim() + "---"
						+ parentEmail_array[i].trim());

				result = ps.executeUpdate();

				if (result == 0) {
					msgo.setError("true");
					msgo.setMessage("User is not Update successfully");
				} else {
					// //System.err.println("Error=="+result);
					msgo.setError("false");
					msgo.setMessage("User is Update successfully");
				}

				/*
				 * psx.setString(1, name_array[i].trim()); psx.setInt(2,534981);
				 * psx.setString(3, idno_array[i].trim());
				 */

			} catch (Exception e) {
				msgo.setError("true");
				msgo.setMessage("Error :" + e.getMessage());
				e.printStackTrace();
			}
		}

		return msgo;

	}

	public MessageObject addVaransiTrainAddressFromCSV(DB mongoconnection,
			String filename, String railWay, String division,
			String station_From, String station_TO, String chainage,
			String trolley, String line, String mode,
			ArrayList<String> kiloMeter, ArrayList<String> distance,
			ArrayList<String> latitude, ArrayList<String> longitude,
			ArrayList<String> feature_Code, ArrayList<String> feature_Detail,
			String parentId, ArrayList<String> section_Detail) {
		MessageObject msgo = new MessageObject();

		BasicDBList parentDbList = new BasicDBList();

		parentDbList.add(Integer.parseInt(parentId));

		try {

			BasicDBList addres_list = new BasicDBList();

			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_RAILADDRESS);

			BasicDBObject Maindocument = new BasicDBObject();
			ObjectId objid = new ObjectId();
			Maindocument.put("address_id", "" + objid);
			/*
			 * Maindocument.put("filename",filename.trim().replace(",",""));
			 * Maindocument.put("ParentId", parentDbList);
			 * 
			 * 
			 * Maindocument.put("railway",railWay.trim().replace(",",""));
			 * Maindocument.put("division",division.trim().replace(",",""));
			 * Maindocument
			 * .put("station_from",station_From.trim().replace(",",""));
			 * Maindocument.put("station_to",station_TO.trim().replace(",",""));
			 * Maindocument.put("chainage",chainage.trim().replace(",",""));
			 * Maindocument.put("trolley",trolley.trim().replace(",",""));
			 * Maindocument.put("line",line.trim().replace(",",""));
			 * Maindocument.put("mode",mode.trim().replace(",",""));
			 * station_From=station_From.substring(station_From.indexOf("[") +
			 * 1); station_From=station_From.substring(0,
			 * station_From.indexOf("]"));
			 * 
			 * station_TO=station_TO.substring(station_TO.indexOf("[") + 1);
			 * station_TO=station_TO.substring(0, station_TO.indexOf("]"));
			 */
			Maindocument.put("filename", filename.trim().replace(",", ""));
			Maindocument.put("ParentId", parentDbList);
			Maindocument.put("railway", railWay.trim().replace(",", ""));
			Maindocument.put("division", division.trim().replace(",", ""));
			Maindocument.put("station_from",
					station_From.trim().replace(",", ""));
			Maindocument.put("station_to", station_TO.trim().replace(",", ""));
			Maindocument.put("chainage", chainage.trim().replace(",", ""));
			Maindocument.put("trolley", trolley.trim().replace(",", ""));
			Maindocument.put("timestamp", System.currentTimeMillis() / 1000);
			Maindocument.put("line", line.trim().replace(",", ""));
			Maindocument.put("mode", mode.trim().replace(",", ""));
			
			  station_From=station_From.substring(station_From.indexOf("[") +
			  1); 
			  station_From=station_From.substring(0,
			  station_From.indexOf("]"));
			  
			 station_TO=station_TO.substring(station_TO.indexOf("[") + 1);
			  station_TO=station_TO.substring(0, station_TO.indexOf("]"));
			 

			for (int i = 0; i < kiloMeter.size(); i++) {
			//	System.err.println("-------------"
					//	+ i
					//	+ "-----"
						//+ Double.parseDouble(distance.get(i).trim()
						//		.replace(",", "")));
				BasicDBObject addresDocument = new BasicDBObject();
				addresDocument.put("kilometer", kiloMeter.get(i).trim()
						.replace(",", ""));
				addresDocument.put("distance",
						distance.get(i).trim().replace(",", ""));
				addresDocument.put("latitude",
						latitude.get(i).trim().replace(",", ""));
				addresDocument.put("longitude", longitude.get(i).trim()
						.replace(",", ""));
				// addresDocument.put("feature_code",feature_Code.get(i).trim().replace(",",""));

				addresDocument.put("feature_detail", (feature_Detail.get(i)
						.trim()).replace(",", "").replace("$", ","));//+" "+section_Detail.get(i));
				
				if (feature_Detail.get(i).trim().replace(",", "").toLowerCase()
						.contains("station")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_12.png");
					addresDocument.put("feature_code", "12");

				} else if (feature_Detail.get(i).trim().replace(",", "").toLowerCase().toLowerCase()
						.contains("rub/lhs")
						|| feature_Code.get(i).trim().replace(",", "")
								.equals("107")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_107.png");
					addresDocument.put("feature_code", "107");

				} else if (feature_Detail.get(i).trim().replace(",", "").toLowerCase()
						.contains("bridge(minor)")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_113.png");
					addresDocument.put("feature_code", "113");

				} else if (feature_Detail.get(i).trim().replace(",", "").toLowerCase()
						.toLowerCase().contains("bridge")
						|| feature_Code.get(i).trim().replace(",", "")
								.equals("42")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_42.png");
					addresDocument.put("feature_code", "42");

				} else if (feature_Detail.get(i).trim().replace(",", "").toLowerCase()
						.contains("gang/keyman")
						|| feature_Code.get(i).trim().replace(",", "")
								.equals("101")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_101.png");
					addresDocument.put("feature_code", "101");

				} else if (((feature_Code.get(i).trim().replace(",", "")
						.equals("1")&&(feature_Detail.get(i).trim().replace(",", "")
						.toLowerCase().contains("km post"))&&distance.get(i).equals("0")
						
						||(feature_Code.get(i).trim().replace(",", "")
								.equals("0")&&distance.get(i).equals("0")))))
								
					 {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_150.png");
					addresDocument.put("feature_code", "150");

				}  /*else if ((feature_Code.get(i).trim().replace(",", "")
						.equals("1")||feature_Detail.get(i).trim().replace(",", "")
						.toLowerCase().contains("km post"))&& !feature_Code.get(i).trim().replace(",", "")
						.equals("150")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_150.png");
					addresDocument.put("feature_code", "150");
					addresDocument.put("distance",0);

				}*//*else if (feature_Detail.get(i).trim().replace(",", "")
						.toLowerCase().contains("new km")
						&& Double.parseDouble(distance.get(i).trim()
								.replace(",", "")) == 0) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_150.png");
					addresDocument.put("feature_code", "150");

				}*/ else if (feature_Detail.get(i).trim().replace(",", "").toLowerCase()
						.contains("ohe")|| feature_Code.get(i).trim().replace(",", "")
						.equals("2")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_2.png");
					addresDocument.put("feature_code", "2");

				} else if (feature_Detail.get(i).trim().toLowerCase().contains("level")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc__4.png");
					addresDocument.put("feature_code", "4");

				}
				else if (feature_Detail.get(i).trim().toLowerCase().contains("circular")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_102.png");
					addresDocument.put("feature_code", "102");

				} else if (feature_Detail.get(i).trim().toLowerCase().contains("curve")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_102.png");
					addresDocument.put("feature_code", "102");

				} else if (feature_Detail.get(i).trim().toLowerCase().contains("gang/hd")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_103.png");
					addresDocument.put("feature_code", "103");

				} else if (feature_Detail.get(i).trim().toLowerCase().contains("switch")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_104.png");
					addresDocument.put("feature_code", "104");

				} else if (feature_Detail.get(i).trim()
						.contains("MISCELLANEOUS")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_105.png");
					addresDocument.put("feature_code", "105");

				} else if (feature_Detail.get(i).trim().toLowerCase().contains("siding/loop")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_106.png");
					addresDocument.put("feature_code", "106");

				} else if (feature_Detail.get(i).trim().toLowerCase().contains("section")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_108.png");
					addresDocument.put("feature_code", "108");

				} else if (feature_Detail.get(i).trim().toLowerCase().contains("cabin")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_109.png");
					addresDocument.put("feature_code", "109");

				} else if (feature_Detail.get(i).trim().toLowerCase().contains("fob")) {
					addresDocument
							.put("feature_image",
									"~/Images/FeatureCodePhoto/ic_default_featurecode.png");
					addresDocument.put("feature_code", "110");

				} else if (feature_Detail.get(i).trim().toLowerCase().contains("joint")
						|| feature_Code.get(i).trim().equals("112")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_112.png");
					addresDocument.put("feature_code", "112");

				} else {
					addresDocument
							.put("feature_image",
									"~/Images/FeatureCodePhoto/ic_default_featurecode.png");
					addresDocument.put("feature_code", "999");

				}

				addresDocument.put("section",station_From+"-"+station_TO);
				
//				addresDocument.put("section","RGD-TIA");
//
				/*if (section_Detail.size()>0&&section_Detail.get(i).trim().length()>0) {
					addresDocument.put("section",
							section_Detail.get(i).trim());
				}else{
					addresDocument.put("section","ANA-DMO");
					addresDocument.put("section",	station_From.replace("SECTION", "").replace(",", "")
									.replace("(Ex)", "").replace(" ", ""));
				}
				*/
				addresDocument.put("block_section", station_From+"-"+station_TO);
				// addresDocument.put("remark",remark_array.get(i).trim());
				addres_list.add(addresDocument);

			}
			// System.err.println("---------addres_list----" +
			// addres_list.size());
			Maindocument.put("address_details", addres_list);

			table.insert(Maindocument);

			msgo.setError("false");
			msgo.setMessage("Address added successfully.---"
					+ addres_list.size());

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("address_details not added successfully.---");
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("address_details not added successfully.---");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("address_details not added successfully.---");
		}
		return msgo;
	}

	public MessageObject AlertForStartWorkWithLowBatteryPatrolman(
			Connection con,

			DB mongoconnection, String parentId) {

		MessageObject msgo = new MessageObject();

		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);

		System.err.println("AlertForStartWorkWithLowBattery ---"
				+ new Gson().toJson(mailSendInfo));

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long startdate = Long.valueOf(Common.getGMTTimeStampFromDate(day + "-"
				+ String.valueOf(month + 1) + "-" + year + " 10:00 pm"));
		long enddate = Long.valueOf(Common.getGMTTimeStampFromDate(day + 1
				+ "-" + String.valueOf(month + 1) + "-" + year + " 05:00 am"));

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(WorkbookUtil
				.createSafeSheetName("Battery Status Report"));

		XSSFCellStyle HeadingStyle = workbook.createCellStyle();
		HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// HeadingStyle.setFont(font);

		XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
		tripColumnStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex());
		tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// tripColumnStyle.setFont(font);

		XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
		remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
				.getIndex());
		remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle loactionNotFoundColumnStyle = workbook.createCellStyle();
		loactionNotFoundColumnStyle
				.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
		loactionNotFoundColumnStyle
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 5);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheet.createRow(0);
		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0);

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0);

		row = sheet.createRow(sheet.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"Low battery status Report (below 40%)  Date :-"
						+ Common.getDateFromLong(startdate));
		row.setRowStyle(HeadingStyle);

		row = sheet.createRow(sheet.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell = row.createCell(1);
		cell.setCellValue("Low Battery Devices");
		cell = row.createCell(2);
		cell.setCellValue("InActive Devices");
		for (int j = 0; j < mailSendInfo.size(); j++) {
			// for (int j = 8; j <mailSendInfo.size(); j++) {
			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			StringBuilder LowBatteryDevices = new StringBuilder();
			StringBuilder InactiveBatteryDevices = new StringBuilder();

			for (int i = 0; i < deviceInfoList.size(); i++) {

				if (deviceInfoList.get(i).getName().startsWith("P/")) {
					Double startBatteryStatus = -1.0;

					try {
						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_ALERT_MSG);

						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery
								.put("device", Long.parseLong(deviceInfoList
										.get(i).getDeviceID()));

						BasicDBObject timestamp_whereQuery = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										startdate).append("$lt", enddate));
						BasicDBList And_Milage = new BasicDBList();
						And_Milage.add(timestamp_whereQuery);
						And_Milage.add(device_whereQuery);
						DBObject Total_Milage_query = new BasicDBObject("$and",
								And_Milage);

						DBCursor cursor = table.find(Total_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1)).limit(1);

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						// System.out.print("getBatteryStatus Count of u-p---"
						// + cursor.size() + "  " + Total_Milage_query);
						long total = cursor.count();

						if (cursor.size() != 0) {

							// System.err.println("*-getBatteryStatus----COunt---"
							// + cursor.size());

							while (cursor.hasNext()) {

								DBObject dbObject = (DBObject) cursor.next();

								startBatteryStatus = Double
										.parseDouble(dbObject
												.get("voltage_level") + "");

								System.err
										.println("*-getBatteryStatus----COunt---"
												+ startBatteryStatus);

							}

							if (startBatteryStatus < 3) {
								LowBatteryDevices.append(deviceInfoList.get(i)
										.getName());
								LowBatteryDevices.append(" , ");
							}

						} else {
							System.err
									.println("*-getBatteryStatus----COunt---00");
							InactiveBatteryDevices.append(deviceInfoList.get(i)
									.getName());
							InactiveBatteryDevices.append(" , ");
						}

					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MongoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
			row = sheet.createRow(sheet.getLastRowNum() + 1);

			cell = row.createCell(0);

			cell.setCellValue(mailSendInfo.get(j).getDept());
			cell = row.createCell(1);
			cell.setCellValue(LowBatteryDevices.toString());
			cell = row.createCell(2);
			cell.setCellValue(InactiveBatteryDevices.toString());

		}
		/*
		 * for (int s=1; s<3; s++){ sheet.autoSizeColumn(s); }
		 */

		String outFileName = "Low battery status Report (below 40%) for PatrolMan"
				+ Common.getDateFromLong(startdate).replace(":", "-") + ".xlsx";
		try {
			String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
					+ outFileName;
			FileOutputStream fos = new FileOutputStream(new File(file));

			/*
			 * File file_l = new File(file);
			 * 
			 * 
			 * Set<PosixFilePermission> ownerWritable =
			 * PosixFilePermissions.fromString("rw-r--r--"); FileAttribute<?>
			 * permissions =
			 * PosixFilePermissions.asFileAttribute(ownerWritable);
			 * Files.createFile(file_l.toPath(), permissions);
			 * 
			 * 
			 * FileOutputStream fos = new FileOutputStream(file_l);
			 */
			workbook.write(fos);
			fos.flush();
			fos.close();
			workbook.close();
			// String sendemail="rupesh.p@mykiddytracker.com,";

			String sendemail = "";
			for (String emaString : mailSendInfo.get(j).getEmailIds()) {
				if (emaString.trim().length() > 0) {
					if (sendemail.length() == 0) {
						sendemail = emaString.trim();
					} else {
						sendemail = sendemail + "," + emaString.trim();

					}

				}
			}
			SendEmail mail = new SendEmail();

			mail.sendDeviceExceptionMailToJaipur(sendemail, file,
					"All Section ",
					"Low battery status Report (below 40%) for PatrolMan",
					"Low battery status Report (below 40%) for PatrolMan",
					false
					,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

		} catch (Exception e) {

			e.printStackTrace();
		}

		return msgo;

	}

	public MessageObject AlertForKeymanWorkStatusReport(Connection con,
			DB mongoconnection, String parentId) {

		MessageObject msgo = new MessageObject();

		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);

		// System.err.println("AlertForKeymanWorkStatusReport ---"+new
		// Gson().toJson(mailSendInfo));

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long startdate = Long.valueOf(Common.getGMTTimeStampFromDate(day + "-"
				+ String.valueOf(month + 1) + "-" + year + " 5:00 am"));
		long enddate = Long.valueOf(Common.getGMTTimeStampFromDate(day + "-"
				+ String.valueOf(month + 1) + "-" + year + " 4:00 pm"));

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("KeyMan Status Report"));
		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// HeadingStyle.setFont(font);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// tripColumnStyle.setFont(font);

		XSSFCellStyle remarkColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		remarkColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
		remarkColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle loactionNotFoundColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		loactionNotFoundColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
		loactionNotFoundColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 5);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_style = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_style.setWrapText(true); // Set wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);
		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"KeyMan Work Status Report Date :-"
						+ Common.getDateFromLong(startdate));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						5));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);

		row.setHeight((short) 500);
		// for (int j = 0; j < mailSendInfo.size(); j++) {
		for (int j = 14; j <= 14; j++) {
			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			ArrayList<Short> rowHeight = new ArrayList<>();
			ArrayList<String> LowBatteryDevices = new ArrayList();
			StringBuilder InactiveBatteryDevices = new StringBuilder();

			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", startdate);
			Maindocument.put("parentid", parentId);

			for (int i = 0; i < deviceInfoList.size(); i++) {

				if (deviceInfoList.get(i).getName().startsWith("K/")) {
					Double startBatteryStatus = -1.0;

					try {
						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_ALERT_MSG);

						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery
								.put("device", Long.parseLong(deviceInfoList
										.get(i).getDeviceID()));

						BasicDBObject timestamp_whereQuery = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										startdate).append("$lt", enddate));
						BasicDBList And_Milage = new BasicDBList();
						And_Milage.add(timestamp_whereQuery);
						And_Milage.add(device_whereQuery);
						DBObject Total_Milage_query = new BasicDBObject("$and",
								And_Milage);

						DBCursor cursor = table.find(Total_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1)).limit(1);

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						// System.out.print("getBatteryStatus Count of u-p---"
						// + cursor.size() + "  " + Total_Milage_query);
						long total = cursor.count();

						if (cursor.size() != 0) {

							// System.err.println("*-getBatteryStatus----COunt---"
							// + cursor.size());

							while (cursor.hasNext()) {

								DBObject dbObject = (DBObject) cursor.next();

								startBatteryStatus = Double
										.parseDouble(dbObject
												.get("voltage_level") + "");

								// System.err.println("*-getBatteryStatus----COunt---"+startBatteryStatus);

							}

							if (startBatteryStatus < 3) {
								LowBatteryDevices.add(deviceInfoList.get(i)
										.getName());
							}

						} else {

							// System.err.println("*-getBatteryStatus----COunt---00");
							InactiveBatteryDevices.append(deviceInfoList.get(i)
									.getName().trim());
							InactiveBatteryDevices.append(" , ");
						}

					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MongoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

			ExceptionKeymanDTO exceptionDto = GetExceptionKeyManListFromBeatNotCover(
					startdate, enddate, con, mongoconnection,
					mailSendInfo.get(j).getDeviceIds(),
					InactiveBatteryDevices.toString(), "K/");

			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_style);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_style); // Apply style to cell
			cell = row.createCell(1);
			StringBuilder lowBatteryDeviceBuilder = new StringBuilder();
			for (int k = 0; k < LowBatteryDevices.size(); k++) {
				if (!exceptionDto.getKeyManOffDevice().contains(
						LowBatteryDevices.get(k).trim()))
					lowBatteryDeviceBuilder.append(LowBatteryDevices.get(k)
							.trim());

			}

			cell.setCellValue(lowBatteryDeviceBuilder.toString());
			Maindocument.put("low_battery", lowBatteryDeviceBuilder.toString());

			cell.setCellStyle(wrap_style); // Apply style to cell
			rowHeight
					.add((short) ((lowBatteryDeviceBuilder.toString().length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(exceptionDto.getKeyManOffDevice());
			Maindocument.put("off_device", exceptionDto.getKeyManOffDevice());
			cell.setCellStyle(wrap_style); // Apply style to cell
			rowHeight
					.add((short) ((exceptionDto.getKeyManOffDevice().length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(exceptionDto.getTotalBeatnotCover());
			Maindocument.put("beat_notcover",
					exceptionDto.getTotalBeatnotCover());

			cell.setCellStyle(wrap_style); // Apply style to cell
			rowHeight.add((short) ((exceptionDto.getTotalBeatnotCover()
					.length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(exceptionDto.getTotalBeatCoverSucesfull());
			Maindocument.put("beat_sucessfull",
					exceptionDto.getTotalBeatCoverSucesfull());

			cell.setCellStyle(wrap_style); // Apply style to cell
			rowHeight.add((short) ((exceptionDto.getTotalBeatCoverSucesfull()
					.length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(exceptionDto.getOverSpeedDevices());
			Maindocument.put("overspeed", exceptionDto.getOverSpeedDevices());

			cell.setCellStyle(wrap_style); // Apply style to cell
			rowHeight
					.add((short) ((exceptionDto.getOverSpeedDevices().length() / 15) * 350));

			short height_row = (short) ((lowBatteryDeviceBuilder.length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);
			System.err.println("Row max height-----" + height_row);
			report_table.insert(Maindocument);

		}// saction for loop end
		sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
		sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
		sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
		sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
		sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
		sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

		String outFileName = "All Section KeyMan status Report_" + parentId
				+ "_" + Common.getDateFromLong(startdate).replace(":", "-")
				+ ".xlsx";
		try {
			String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
					+ outFileName;
			FileOutputStream fos = new FileOutputStream(new File(file));

			/*
			 * Set<PosixFilePermission> ownerWritable =
			 * PosixFilePermissions.fromString("rw-rw-rw-"); FileAttribute<?>
			 * permissions =
			 * PosixFilePermissions.asFileAttribute(ownerWritable);
			 * Files.createFile(file_l.toPath(), permissions);
			 * 
			 * 
			 * FileOutputStream fos = new FileOutputStream(file_l);
			 */
			workbookAlertForKeymanWorkStatusReport.write(fos);
			fos.flush();
			fos.close();
			workbookAlertForKeymanWorkStatusReport.close();
			// String sendemail="rupesh.p@mykiddytracker.com,";

			String sendemail = "";
			for (String emaString : mailSendInfo.get(j).getEmailIds()) {
				if (emaString.trim().length() > 0) {
					if (sendemail.length() == 0) {
						sendemail = emaString.trim();
					} else {
						sendemail = sendemail + "," + emaString.trim();

					}

				}
			}
			SendEmail mail = new SendEmail();

			System.err.println("Send mailllllllllllll----" + sendemail);

			/*
			 * mail.sendDeviceExceptionMailToJaipur(sendemail, file,
			 * "All Section"," KeyMan Work status Report_AllSection",
			 * " KeyMan Work status Report_AllSection",true);
			 */
		} catch (Exception e) {

			e.printStackTrace();
		}

		return msgo;

	}

	public MessageObject AlertForInactiveDevicePartolMan(Connection con,
			DB mongoconnection, String parentId) {

		MessageObject msgo = new MessageObject();

		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);

		System.err.println("AlertForInactiveDevicePartolMan ---"
				+ new Gson().toJson(mailSendInfo));

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long startdate = Long.valueOf(Common.getGMTTimeStampFromDate(day-1 + "-"
				+ String.valueOf(month + 1) + "-" + year + " 10:00 pm"));
		long enddate = Long.valueOf(Common.getGMTTimeStampFromDate(day+ "-"
				+ String.valueOf(month + 1) + "-" + year + " 7:00 am"));

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(WorkbookUtil
				.createSafeSheetName("Patrolman Status Report"));
		XSSFCellStyle HeadingStyle = workbook.createCellStyle();
		HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// HeadingStyle.setFont(font);

		XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
		tripColumnStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex());
		tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyle.setWrapText(true);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// tripColumnStyle.setFont(font);

		XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
		remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
				.getIndex());
		remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle loactionNotFoundColumnStyle = workbook.createCellStyle();
		loactionNotFoundColumnStyle
				.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
		loactionNotFoundColumnStyle
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 5);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_style = workbook.createCellStyle(); // Create new style
		wrap_style.setWrapText(true); // Set wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheet.createRow(0);

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0);

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0);

		row = sheet.createRow(sheet.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"PatrolMan Work Status Report Date :-"
						+ Common.getDateCurrentTimeZone(startdate)+" To "+Common.getDateCurrentTimeZone(enddate));
		sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet
				.getLastRowNum(), 0, 5));
		row.getCell(0).setCellStyle(HeadingStyle);

		row = sheet.createRow(sheet.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyle);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyle);
		cell = row.createCell(3);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyle);
		cell = row.createCell(2);
		cell.setCellValue("On device  ");
		cell.setCellStyle(tripColumnStyle);
		;
		/*cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyle);

		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyle);
*/
		row.setHeight((short) 500);
		for (int j = 0; j < mailSendInfo.size(); j++) {
			// for (int j = 5; j <=5; j++) {
			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			ArrayList<Short> rowHeight = new ArrayList<>();
			StringBuilder LowBatteryDevices = new StringBuilder();
			StringBuilder InactiveBatteryDevices = new StringBuilder();
			StringBuilder ActiveDeviceList = new StringBuilder();

			for (int i = 0; i < deviceInfoList.size(); i++) {

				if (deviceInfoList.get(i).getName().startsWith("P/")) {
					Double startBatteryStatus = -1.0;

					try {
						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_ALERT_MSG);

						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery
								.put("device", Long.parseLong(deviceInfoList
										.get(i).getDeviceID()));

						BasicDBObject timestamp_whereQuery = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										startdate).append("$lt", enddate));
						BasicDBList And_Milage = new BasicDBList();
						And_Milage.add(timestamp_whereQuery);
						And_Milage.add(device_whereQuery);
						DBObject Total_Milage_query = new BasicDBObject("$and",
								And_Milage);

						DBCursor cursor = table.find(Total_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1)).limit(1);

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						// System.out.print("getBatteryStatus Count of u-p---"
						// + cursor.size() + "  " + Total_Milage_query);
						long total = cursor.count();

						if (cursor.size() != 0) {

							// System.err.println("*-getBatteryStatus----COunt---"
							// + cursor.size());

							while (cursor.hasNext()) {

								DBObject dbObject = (DBObject) cursor.next();

								startBatteryStatus = Double
										.parseDouble(dbObject
												.get("voltage_level") + "");

								System.err
										.println("*-getBatteryStatus----COunt---"
												+ startBatteryStatus);

							}

							if (startBatteryStatus < 3) {
								LowBatteryDevices.append(deviceInfoList.get(i)
										.getName());
								LowBatteryDevices.append(" , ");
							}
							ActiveDeviceList.append(deviceInfoList.get(i)
									.getName());
							ActiveDeviceList.append(" , ");
							
						} else {
							System.err
									.println("*-getBatteryStatus----COunt---00");
							InactiveBatteryDevices.append(deviceInfoList.get(i)
									.getName());
							InactiveBatteryDevices.append(" , ");
						}

					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MongoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

			row = sheet.createRow(sheet.getLastRowNum() + 1);
			row.setRowStyle(wrap_style);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			cell.setCellStyle(wrap_style); // Apply style to cell
			cell = row.createCell(1);
			cell.setCellValue(LowBatteryDevices.toString());
			cell.setCellStyle(wrap_style); // Apply style to cell
			rowHeight
					.add((short) ((LowBatteryDevices.toString().length() / 15) * 250));

		/*	ExceptionKeymanDTO exceptionDto = GetPatrolManListSatus(
					startdate, enddate, con, mongoconnection,
					mailSendInfo.get(j).getDeviceIds(),
					InactiveBatteryDevices.toString(), "P/");
*/
			
			cell = row.createCell(2);
			cell.setCellValue(ActiveDeviceList.toString());
			cell.setCellStyle(wrap_style); // Apply style to cel
			
			cell = row.createCell(3);
			cell.setCellValue(InactiveBatteryDevices.toString());
			cell.setCellStyle(wrap_style); // Apply style to cell

			if (InactiveBatteryDevices.length()<ActiveDeviceList.length()) {
				
				rowHeight
				.add((short) ((ActiveDeviceList.length() / 15) * 250));
			

			}else {
				rowHeight
				.add((short) ((InactiveBatteryDevices.length() / 15) * 250));
			}
			
			
			short height_row = (short) ((LowBatteryDevices.length() / 15) * 250);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			if(height_row>0)
			row.setHeight(height_row);
			else
				row.setHeight((short) 2005);
			System.err.println("Row max height-----"+ mailSendInfo.get(j).getDept()+"---"+ row.getHeight());

		}
		sheet.setColumnWidth(0, 1500);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 3000);
		/*sheet.setColumnWidth(4, 3000);
		sheet.setColumnWidth(5, 3000);*/

		/*
		 * for (int s=1; s<3; s++){ sheet.autoSizeColumn(s); }
		 */

		String outFileName = "All Section Patrolman status Report_"
				+ Common.getDateFromLong(startdate).replace(":", "-") + ".xlsx";
		try {
			String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
					+ outFileName;
			FileOutputStream fos = new FileOutputStream(new File(file));

			/*
			 * Set<PosixFilePermission> ownerWritable =
			 * PosixFilePermissions.fromString("rw-r--r--"); FileAttribute<?>
			 * permissions =
			 * PosixFilePermissions.asFileAttribute(ownerWritable);
			 * Files.createFile(file_l.toPath(), permissions);
			 * 
			 * 
			 * FileOutputStream fos = new FileOutputStream(file_l);
			 */

			workbook.write(fos);
			fos.flush();
			fos.close();
			workbook.close();
			// String sendemail="rupesh.p@mykiddytracker.com,";

			String sendemail = "";
			for (String emaString : mailSendInfo.get(j).getEmailIds()) {
				if (emaString.trim().length() > 0) {
					if (sendemail.length() == 0) {
						sendemail = emaString.trim();
					} else {
						sendemail = sendemail + "," + emaString.trim();

					}

				}
			}
			System.err.println("Sendemail--------" + sendemail);
			SendEmail mail = new SendEmail();

			mail.sendDeviceExceptionMailToJaipur(sendemail, file,
					"All Section", " Patrolman Work status Report_",
					" Patrolman Work status Report_", true	
					,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

		} catch (Exception e) {

			e.printStackTrace();
		}

		return msgo;

	}

	public MessageObject AlertForInactiveDeviceKeyMan(Connection con,
			DB mongoconnection, String parentId) {

		MessageObject msgo = new MessageObject();

		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);

		System.err.println("AlertForInactiveDeviceKeyMan ---"
				+ new Gson().toJson(mailSendInfo));

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long startdate = Long.valueOf(Common.getGMTTimeStampFromDate(day + "-"
				+ String.valueOf(month + 1) + "-" + year + " 5:00 am"));
		long enddate = Long.valueOf(Common.getGMTTimeStampFromDate(day + "-"
				+ String.valueOf(month + 1) + "-" + year + " 8:00 pm"));

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(WorkbookUtil
				.createSafeSheetName("Patrolman Status Report"));
		XSSFCellStyle HeadingStyle = workbook.createCellStyle();
		HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// HeadingStyle.setFont(font);

		XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
		tripColumnStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex());
		tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyle.setWrapText(true);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// tripColumnStyle.setFont(font);

		XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
		remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
				.getIndex());
		remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle loactionNotFoundColumnStyle = workbook.createCellStyle();
		loactionNotFoundColumnStyle
				.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
		loactionNotFoundColumnStyle
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 5);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_style = workbook.createCellStyle(); // Create new style
		wrap_style.setWrapText(true); // Set wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheet.createRow(0);

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0);

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0);

		row = sheet.createRow(sheet.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"KeyMan Work Status Report Date :-"
						+ Common.getDateFromLong(startdate));
		sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet
				.getLastRowNum(), 0, 5));
		row.getCell(0).setCellStyle(HeadingStyle);

		row = sheet.createRow(sheet.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyle);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyle);
		cell = row.createCell(3);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyle);
		cell = row.createCell(2);
		cell.setCellValue("On device  ");
		cell.setCellStyle(tripColumnStyle);
		;
		/*cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyle);

		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyle);
*/
		row.setHeight((short) 500);
		for (int j = 0; j < mailSendInfo.size(); j++) {
			// for (int j = 5; j <=5; j++) {
			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			ArrayList<Short> rowHeight = new ArrayList<>();
			StringBuilder LowBatteryDevices = new StringBuilder();
			StringBuilder InactiveBatteryDevices = new StringBuilder();
			StringBuilder ActiveDeviceList = new StringBuilder();

			for (int i = 0; i < deviceInfoList.size(); i++) {

				if (deviceInfoList.get(i).getName().startsWith("K/")) {
					Double startBatteryStatus = -1.0;

					try {
						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_ALERT_MSG);

						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery
								.put("device", Long.parseLong(deviceInfoList
										.get(i).getDeviceID()));

						BasicDBObject timestamp_whereQuery = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										startdate).append("$lt", enddate));
						BasicDBList And_Milage = new BasicDBList();
						And_Milage.add(timestamp_whereQuery);
						And_Milage.add(device_whereQuery);
						DBObject Total_Milage_query = new BasicDBObject("$and",
								And_Milage);

						DBCursor cursor = table.find(Total_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1)).limit(1);

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						// System.out.print("getBatteryStatus Count of u-p---"
						// + cursor.size() + "  " + Total_Milage_query);
						long total = cursor.count();

						if (cursor.size() != 0) {

							// System.err.println("*-getBatteryStatus----COunt---"
							// + cursor.size());

							while (cursor.hasNext()) {

								DBObject dbObject = (DBObject) cursor.next();

								startBatteryStatus = Double
										.parseDouble(dbObject
												.get("voltage_level") + "");

								System.err
										.println("*-getBatteryStatus----COunt---"
												+ startBatteryStatus);

							}

							if (startBatteryStatus < 3) {
								LowBatteryDevices.append(deviceInfoList.get(i)
										.getName());
								LowBatteryDevices.append(" , ");
							}
							ActiveDeviceList.append(deviceInfoList.get(i)
									.getName());
							ActiveDeviceList.append(" , ");
							
						} else {
							System.err
									.println("*-getBatteryStatus----COunt---00");
							InactiveBatteryDevices.append(deviceInfoList.get(i)
									.getName());
							InactiveBatteryDevices.append(" , ");
						}

					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MongoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

			row = sheet.createRow(sheet.getLastRowNum() + 1);
			row.setRowStyle(wrap_style);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			cell.setCellStyle(wrap_style); // Apply style to cell
			cell = row.createCell(1);
			cell.setCellValue(LowBatteryDevices.toString());
			cell.setCellStyle(wrap_style); // Apply style to cell
			rowHeight
					.add((short) ((LowBatteryDevices.toString().length() / 15) * 250));

		/*	ExceptionKeymanDTO exceptionDto = GetPatrolManListSatus(
					startdate, enddate, con, mongoconnection,
					mailSendInfo.get(j).getDeviceIds(),
					InactiveBatteryDevices.toString(), "P/");
*/
			
			cell = row.createCell(2);
			cell.setCellValue(ActiveDeviceList.toString());
			cell.setCellStyle(wrap_style); // Apply style to cel
			
			cell = row.createCell(3);
			cell.setCellValue(InactiveBatteryDevices.toString());
			cell.setCellStyle(wrap_style); // Apply style to cell

			if (InactiveBatteryDevices.length()<ActiveDeviceList.length()) {
				rowHeight
				.add((short) ((InactiveBatteryDevices.length() / 15) * 250));

			}else {
				rowHeight
				.add((short) ((ActiveDeviceList.length() / 15) * 250));

			}
			
			
			short height_row = (short) ((LowBatteryDevices.length() / 15) * 250);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);
			System.err.println("Row max height-----" + height_row);

		}
		sheet.setColumnWidth(0, 1500);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 3000);
		/*sheet.setColumnWidth(4, 3000);
		sheet.setColumnWidth(5, 3000);*/

		/*
		 * for (int s=1; s<3; s++){ sheet.autoSizeColumn(s); }
		 */

		String outFileName = "All Section KeyMan status Report_"
				+ Common.getDateFromLong(startdate).replace(":", "-") + ".xlsx";
		try {
			String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
					+ outFileName;
			FileOutputStream fos = new FileOutputStream(new File(file));

			/*
			 * Set<PosixFilePermission> ownerWritable =
			 * PosixFilePermissions.fromString("rw-r--r--"); FileAttribute<?>
			 * permissions =
			 * PosixFilePermissions.asFileAttribute(ownerWritable);
			 * Files.createFile(file_l.toPath(), permissions);
			 * 
			 * 
			 * FileOutputStream fos = new FileOutputStream(file_l);
			 */

			workbook.write(fos);
			fos.flush();
			fos.close();
			workbook.close();
			// String sendemail="rupesh.p@mykiddytracker.com,";

			String sendemail = "";
			for (String emaString : mailSendInfo.get(j).getEmailIds()) {
				
				if (emaString.trim().length() > 0) {
					if (sendemail.length() == 0) {
						sendemail = emaString.trim();
					} else {
						sendemail = sendemail + "," + emaString.trim();

					}

				}
			}
			System.err.println("Sendemail--------" + sendemail);
			SendEmail mail = new SendEmail();

			mail.sendDeviceExceptionMailToJaipur(sendemail, file,
					"All Section", " KeyMan Work status Report_",
					" Patrolman Work status Report_", true
					,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());


		} catch (Exception e) {

			e.printStackTrace();
		}

		return msgo;

	}

	public ArrayList<RailMailSendInfoDto> getSentMailInfoList(Connection con,
			String parentId) {
		ArrayList<RailMailSendInfoDto> mailSendInfo = new ArrayList<>();
		try {
			System.err.println("New parentId=== " + parentId);

			java.sql.CallableStatement ps_call_main = con
					.prepareCall("{call GetIdFromHieracy(?)}");
			ps_call_main.setInt(1, Integer.parseInt(parentId));
			ResultSet rs = ps_call_main.executeQuery();
			
				int dept=0;

			while (rs.next()) {
//				System.err.println("New rs.getInt()===== " + rs.getInt("Id"));
				try {

					java.sql.CallableStatement ps_call = con
							.prepareCall("{call GetGpsDeviceToSendMail(?)}");
					ps_call.setInt(1, rs.getInt("Id"));
					ResultSet rs1 = ps_call.executeQuery();
					ArrayList<String> EmailIds = new ArrayList();
					ArrayList<String> DeviceIds;
					if (rs1 != null) {
						while (rs1.next()) {

//							 System.out.println("New next.===== "
//							 + rs1.getString("ChildStudent"));

							RailMailSendInfoDto dto = new RailMailSendInfoDto();
							EmailIds.add((rs1.getString("EmailId") + "").trim());
							dto.setDept(rs1.getString("DeptName").trim());
							dto.setDeptId(rs1.getInt("DeptId"));
							dto.setReportSendEmailId(rs1.getString("reportSendEmailId").trim());
							dto.setReportSendEmailPassword(rs1.getString("reportSendEmailPassword").trim());
							
							System.err.println("Dept--"+dept+"---"+dto.getDept()+"---"+dto.getDeptId());
							
							ArrayList<RailDeviceInfoDto> deviceInfoList = new ArrayList<>();

							if (rs1.getString("ChildStudent") != null) {

								String[] device_no = rs1.getString(
										"ChildStudent").split(",");

								if (device_no.length > 0)
									for (int i = 0; i < device_no.length; i++) {

										/*
										 * System.err .println(
										 * "GetDeviceIMEIT=device_no[i]==== " +
										 * device_no[i] + "-----" +
										 * rs1.getString("ParentId"));
										 */
										try {

											ps_call = con
													.prepareCall("{call GetDeviceIMEITosendMail(?,?)}");
											ps_call.setString(1, "-"
													+ device_no[i]);
											ps_call.setString(2,
													rs1.getString("ParentId"));

											ResultSet rs3 = ps_call
													.executeQuery();

											if (rs3 != null) {
												while (rs3.next()) {

													RailDeviceInfoDto dto_info = new RailDeviceInfoDto();
													dto_info.setStudentId(rs3
															.getInt(1));
													dto_info.setName(rs3
															.getString(2));
													dto_info.setDeviceID(rs3
															.getString(3));

													deviceInfoList
															.add(dto_info);

												}
											}
											// System.err.println("deviceInfoList===== "+new
											// Gson().toJson(deviceInfoList));

										} catch (Exception e) {
											e.printStackTrace();
										}

									}// for end

							}

							if (rs1.getString("DeptParents") != null) {
								String[] ParentIds = rs1.getString(
										"DeptParents").split(",");

								for (String ParentId : ParentIds) {

									ps_call = con
											.prepareCall("{call GetEmailOfDeptParent(?)}");
									ps_call.setString(1, ParentId);

									ResultSet rs4 = ps_call.executeQuery();

									if (rs4 != null)
										while (rs4.next())
											EmailIds.add((rs4
													.getString("EmailId") + "")
													.trim());

								}

							}

							dto.setEmailIds(EmailIds);
							dto.setDeviceIds(deviceInfoList);
							dto.setParentId(rs1.getInt("ParentId") + "");

							mailSendInfo.add(dto);
							dept++;

						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mailSendInfo;
	}

	public MessageObject UpdateSectionNameInStudent(Connection con,
			String sectionName, String studentIds) {

		String[] sectionName_array = sectionName.split(",");
		String[] studentIds_array = studentIds.split(",");

		MessageObject msgo = new MessageObject();
		for (int i = 0; i < studentIds_array.length; i++) {

			try {

				// ////System.err.print(emailid);
				int result = 0;

				java.sql.CallableStatement ps = con
				// .prepareCall("{call UpdateSectionNameRailwayKeymanBeatPath(?,?)}");
						.prepareCall("{call UpdateSectionNameStudent(?,?)}");

				ps.setString(1, sectionName_array[i].trim());
				ps.setString(2, "-" + studentIds_array[i].trim());

				result = ps.executeUpdate();

				if (result == 0) {
					msgo.setError("true");
					msgo.setMessage("User is not Update successfully");
				} else {
					// //System.err.println("Error=="+result);
					msgo.setError("false");
					msgo.setMessage("User is Update successfully");
				}

				/*
				 * psx.setString(1, name_array[i].trim()); psx.setInt(2,534981);
				 * psx.setString(3, idno_array[i].trim());
				 */

			} catch (Exception e) {
				msgo.setError("true");
				msgo.setMessage("Error :" + e.getMessage());
				e.printStackTrace();
			}
		}

		return msgo;
	}

	public MessageObject AlertForPatrolManTotalBeatNotCover(Connection con,
			DB mongoconnection, String parentId) {

		MessageObject msgo = new MessageObject();

		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);

		System.err.println("AlertForPatrolManTotalBeatNotCover ---"
				+ new Gson().toJson(mailSendInfo));

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long startdate = Long.valueOf(Common.getGMTTimeStampFromDate(day + "-"
				+ String.valueOf(month + 1) + "-" + year + " 00:00 am"));
		long enddate = Long.valueOf(Common.getGMTTimeStampFromDate(day + "-"
				+ String.valueOf(month + 1) + "-" + year + " 07:00 am"));

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(WorkbookUtil
				.createSafeSheetName("PatrolMan Not Work properly Report"));

		XSSFCellStyle HeadingStyle = workbook.createCellStyle();
		HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// HeadingStyle.setFont(font);

		XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
		tripColumnStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex());
		tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// tripColumnStyle.setFont(font);

		XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
		remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
				.getIndex());
		remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle loactionNotFoundColumnStyle = workbook.createCellStyle();
		loactionNotFoundColumnStyle
				.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
		loactionNotFoundColumnStyle
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 5);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheet.createRow(0);
		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0);

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0);

		row = sheet.createRow(sheet.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"PatrolMan Not Work properly Report List Date :-"
						+ Common.getDateFromLong(startdate));
		row.setRowStyle(HeadingStyle);

		row = sheet.createRow(sheet.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell = row.createCell(1);
		cell.setCellValue("PatrolMan Devices which not work properly");

		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();
		for (int j = 0; j < mailSendInfo.size(); j++) {
			// for (int j = 5; j <=5; j++) {
			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			Set<String> PatrolManExceptionalDevices_Set = new HashSet<String>();

			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayPetrolmanTripsBeatsList = new ArrayList<>();

			for (int i = 0; i < deviceInfoList.size(); i++) {
				// for (int i = 9; i <= 9; i++) {

				Double startBatteryStatus = getStartBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						startdate);
				Double endBatteryStatus = getEndBatteryStatus(mongoconnection,
						deviceInfoList.get(i).getDeviceID(), startdate);

				if (deviceInfoList.get(i).getName().startsWith("P/")) {

					RailwayPetrolmanTripsBeatsList
							.removeAll(RailwayPetrolmanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfPetrolman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));
								dto.setFkTripMasterId(rsgertrip
										.getInt("fk_TripMasterId"));
								dto.setKmFromTo(rsgertrip.getString("KmFromTo"));
								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));
								dto.setTotalKmCover(rsgertrip
										.getDouble("TotalKmCover"));
								dto.setInitialDayStartTime(rsgertrip
										.getString("InitailDayStratTime"));
								dto.setSheetNo(rsgertrip.getInt("SheetNo"));
								dto.setSectionName(rsgertrip
										.getString("SectionName"));
								dto.setTripName(rsgertrip.getString("TripName"));
								dto.setTripTimeShedule(rsgertrip
										.getString("TripTimeShedule"));
								dto.setTripStartTimeAdd(rsgertrip
										.getInt("TripStartTimeAdd"));
								dto.setTripSpendTimeIntervalAdd(rsgertrip
										.getInt("TripSpendTimeIntervalAdd"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));

								RailwayPetrolmanTripsBeatsList.add(dto);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					long DayStartWorkTime = Long.valueOf(Common
							.getGMTTimeStampFromDate(day + "-"
									+ String.valueOf(month + 1) + "-" + year
									+ " 00:00 am"));
					// long DayStartWorkTime=1547836200;
					// System.out.println("DayStartWorkTime----" +
					// DayStartWorkTime);

					Double distanceTolerance = 0.5;
					long timeTolerance = 1800;
					// Double startLatOfRouteKm = 0.0, startLangOfRouteKm = 0.0,
					// endLatOfRouteKm = 0.0, endLangOfRouteKm = 0.0;

					// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
					// RailwayPetrolmanTripsBeatsList) {
					for (int r = 0; r < RailwayPetrolmanTripsBeatsList.size(); r++) {

						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayPetrolmanTripsBeatsList
								.get(r);

						long triptStartTime = DayStartWorkTime
								+ railMailSendInfoDto.getTripStartTimeAdd()
								- timeTolerance;
						long triptEndTime = DayStartWorkTime
								+ railMailSendInfoDto.getTripStartTimeAdd()
								+ railMailSendInfoDto
										.getTripSpendTimeIntervalAdd()
								+ timeTolerance;

						// System.out.println("triptStartTime----"
						// + Common.getDateCurrentTimeZone(triptStartTime)
						// + "========triptEndTime----"
						// + Common.getDateCurrentTimeZone(triptEndTime));
						ArrayList<String> kmStartLatSet = new ArrayList<>(), kmStartLangSet = new ArrayList<>(), kmEndLatSet = new ArrayList<>(), kmEndLangSet = new ArrayList<>();

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.parseLong("355488020181042"));

						BasicDBObject timestamp_whereQuery = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										triptStartTime).append("$lt",
										triptEndTime));
						BasicDBList And_Milage = new BasicDBList();
						And_Milage.add(timestamp_whereQuery);
						And_Milage.add(device_whereQuery);
						DBObject Total_Milage_query = new BasicDBObject("$and",
								And_Milage);

						DBCursor cursor = table.find(Total_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + Total_Milage_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							// System.err.println("*--listObjects---COunt---" +
							// cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();

						}
						/*
						 * int
						 * pos=getNearByKMStartEnd(listObjects.get(0),kmStartLatSet
						 * ,kmStartLangSet);
						 * 
						 * System.err.println("*-----kmStartLatSet---"+new
						 * Gson().toJson(kmStartLatSet));
						 * System.err.println("*-----kmStartLangSet---"+new
						 * Gson().toJson(kmStartLangSet));
						 * System.err.println("*-----kmEndLatSet---"+new
						 * Gson().toJson(kmEndLatSet));
						 * System.err.println("*-----kmEndLangSet---"+new
						 * Gson().toJson(kmEndLangSet));
						 * 
						 * startLatOfRouteKm=Double.parseDouble(kmStartLatSet.get
						 * (pos));
						 * startLangOfRouteKm=Double.parseDouble(kmStartLangSet
						 * .get(pos));
						 * endLatOfRouteKm=Double.parseDouble(kmEndLatSet
						 * .get(pos));
						 * endLangOfRouteKm=Double.parseDouble(kmEndLangSet
						 * .get(pos));
						 */

						Double minDistanceCal = 0.0;
						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());
							;

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = locationNearbyKM(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"P");
								tripStartLocation
										.setTripStartExpectedTime(triptStartTime
												+ timeTolerance);
								tripStartLocation
										.setTripEndExpectedTime(triptEndTime
												- timeTolerance);
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation = locationNearbyKM(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"P");

								tripEndLocation
										.setTripStartExpectedTime(triptStartTime
												+ timeTolerance);
								tripEndLocation
										.setTripEndExpectedTime(triptEndTime
												- timeTolerance);
								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);

								exceptiionalTrip.add(exDto);
							} else {

								exceptiionalTrip.add(null);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exceptiionalTrip.add(exDto);

						}

					}

					// //////////////////////////////////
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null) {
						StringBuilder remark = new StringBuilder();

						for (ExceptionReortsTrip exceptionReortsTrip : exceptiionalTrip) {
							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null) {
								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = (exceptionReortsTrip
											.getTripStart()
											.getStartkmBeatActual() - exceptionReortsTrip
											.getTripEnd().getEndKmBeatActual());
									if (kmcover < 0)
										kmcover = -1 * kmcover;

									double expactedKmCover = exceptionReortsTrip
											.getTripStart()
											.getEndKmBeatExpected()
											- exceptionReortsTrip
													.getTripStart()
													.getStartkmBeatExpected();
									if (expactedKmCover < 0)
										expactedKmCover = -1 * expactedKmCover;

									if (exceptionReortsTrip.getTripStart()
											.getTripStartExpectedTime()
											- exceptionReortsTrip
													.getTripStart()
													.getTimestamp() > timeTolerance) {
										PatrolManExceptionalDevices_Set
												.add(deviceInfoList.get(i)
														.getName());
									}

									if (kmcover < expactedKmCover
											- distanceTolerance) {
										PatrolManExceptionalDevices_Set
												.add(deviceInfoList.get(i)
														.getName());
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// Exception of location not found

								PatrolManExceptionalDevices_Set
										.add(deviceInfoList.get(i).getName());

							}
						}

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayPetrolmanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						PatrolManExceptionalDevices_Set.add(deviceInfoList.get(
								i).getName());

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());

				} else {
					// System.out.println("Key Man found--------------------");
				}
			}

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			cell = row.createCell(0);

			cell.setCellValue(mailSendInfo.get(j).getDept());
			cell = row.createCell(1);
			cell.setCellValue(PatrolManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));

		}
		/*
		 * for (int s=1; s<3; s++){ sheet.autoSizeColumn(s); }
		 */

		String outFileName = "PatrolMan Exception Devices List -"
				+ Common.getDateFromLong(startdate).replace(":", "-") + ".xlsx";
		try {
			String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
					+ outFileName;
			FileOutputStream fos = new FileOutputStream(new File(file));

			/*
			 * Set<PosixFilePermission> ownerWritable =
			 * PosixFilePermissions.fromString("rw-r--r--"); FileAttribute<?>
			 * permissions =
			 * PosixFilePermissions.asFileAttribute(ownerWritable);
			 * Files.createFile(file_l.toPath(), permissions);
			 * 
			 * 
			 * FileOutputStream fos = new FileOutputStream(file_l);
			 */
			workbook.write(fos);
			fos.flush();
			fos.close();
			workbook.close();
			// String sendemail="rupesh.p@mykiddytracker.com,";

			String sendemail = "";
			for (String emaString : mailSendInfo.get(j).getEmailIds()) {
				if (emaString.trim().contains("enggctljpgps@gmail.com")
						|| emaString.contains("den"))
					if (sendemail.length() == 0) {
						sendemail = emaString.trim();
					} else {
						sendemail = sendemail + "," + emaString.trim();

					}

			}
			SendEmail mail = new SendEmail();

			mail.sendDeviceExceptionMailToJaipur(sendemail, file,
					"All Section ", "PatrolMan Not Work properly Report",
					"PatrolMan Not Work properly Report", false
					,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());


		} catch (Exception e) {

			e.printStackTrace();
		}

		return msgo;
	}

	public MessageObject AlertForKeyManTotalBeatNotCover(Connection con,
			DB mongoconnection, String parentId) {

		MessageObject msgo = new MessageObject();

		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);
		System.err.println("AlertForKeyManTotalBeatNotCover ---"
				+ new Gson().toJson(mailSendInfo));
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long startdate = Long.valueOf(Common.getGMTTimeStampFromDate(day + "-"
				+ String.valueOf(month + 1) + "-" + year + " 05:00 am"));
		long enddate = Long.valueOf(Common.getGMTTimeStampFromDate(day + "-"
				+ String.valueOf(month + 1) + "-" + year + " 04:00 pm"));
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(WorkbookUtil
				.createSafeSheetName("keymen Not Work properly Report"));

		XSSFCellStyle HeadingStyle = workbook.createCellStyle();
		HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// HeadingStyle.setFont(font);

		XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
		tripColumnStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex());
		tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// tripColumnStyle.setFont(font);

		XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
		remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
				.getIndex());
		remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle loactionNotFoundColumnStyle = workbook.createCellStyle();
		loactionNotFoundColumnStyle
				.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
		loactionNotFoundColumnStyle
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 5);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		Row row = sheet.createRow(0);
		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0);

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0);

		row = sheet.createRow(sheet.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"keymen Not Work properly Report List Date :-"
						+ Common.getDateFromLong(startdate));
		row.setRowStyle(HeadingStyle);

		row = sheet.createRow(sheet.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell = row.createCell(1);
		cell.setCellValue("keymen Devices which not work properly");

		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();
		for (int j = 0; j < mailSendInfo.size(); j++) {
			// for (int j = 8; j <=8; j++) {
			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();

			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			int petrolMancount = 0;
			for (int i = 0; i < deviceInfoList.size(); i++) {
				Double startBatteryStatus = getStartBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						startdate);
				Double endBatteryStatus = getEndBatteryStatus(mongoconnection,
						deviceInfoList.get(i).getDeviceID(), startdate);

				if (deviceInfoList.get(i).getName().startsWith("K/")) {

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfKeyman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));

								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

								dto.setSectionName(rsgertrip
										.getString("SectionName"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));
								RailwayKeymanTripsBeatsList.add(dto);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					long DayStartWorkTime = Long.valueOf(Common
							.getGMTTimeStampFromDate(day + "-"
									+ String.valueOf(month + 1) + "-" + year
									+ " 00:00 am"));

					Double distanceTolerance = 1.3;

					// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
					// RailwayPetrolmanTripsBeatsList) {
					for (int r = 0; r < RailwayKeymanTripsBeatsList.size(); r++) {
						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayKeymanTripsBeatsList
								.get(r);

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.parseLong("355488020181042"));

						BasicDBObject timestamp_whereQuery = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime).append("$lt",
										DayStartWorkTime + 86400));
						BasicDBList And_Milage = new BasicDBList();
						And_Milage.add(timestamp_whereQuery);
						And_Milage.add(device_whereQuery);
						DBObject Total_Milage_query = new BasicDBObject("$and",
								And_Milage);

						DBCursor cursor = table.find(Total_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", -1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + Total_Milage_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							System.err.println("*--listObjects---COunt---"
									+ cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();

						}

						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());
							;

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = locationNearbyKM(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"K");
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation = locationNearbyKM(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"K");

								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								exceptiionalTrip.add(exDto);
							} else {

								exceptiionalTrip.add(null);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setLocationSize(0);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exceptiionalTrip.add(exDto);
						}

					}

					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null) {
						StringBuilder remark = new StringBuilder();

						for (ExceptionReortsTrip exceptionReortsTrip : exceptiionalTrip) {
							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null
									&& exceptionReortsTrip.getLocationSize() > 0) {

								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = (exceptionReortsTrip
											.getTripStart()
											.getStartkmBeatActual() - exceptionReortsTrip
											.getTripEnd().getEndKmBeatActual());
									if (kmcover < 0)
										kmcover = -1 * kmcover;

									double expactedKmCover = exceptionReortsTrip
											.getTripStart()
											.getEndKmBeatExpected()
											- exceptionReortsTrip
													.getTripStart()
													.getStartkmBeatExpected();
									if (expactedKmCover < 0)
										expactedKmCover = -1 * expactedKmCover;

									if (kmcover < expactedKmCover
											- distanceTolerance)
										KeyManExceptionalDevices_Set
												.add(deviceInfoList.get(i)
														.getName());

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

							}
						}

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayKeymanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null)
							KeyManExceptionalDevices_Set.add(deviceInfoList
									.get(i).getName());
						else
							KeyManExceptionalDevices_Set.add(deviceInfoList
									.get(i).getName());

					}

				}
				// System.out.println("ttttt--send mail---"
				// + deviceInfoList.size() + "----");
				exceptiionalTrip.removeAll(exceptiionalTrip);

				// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());

			}

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			cell = row.createCell(0);

			cell.setCellValue(mailSendInfo.get(j).getDept());
			cell = row.createCell(1);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));

		}

		String outFileName = "keymen Exception Devices List -"
				+ Common.getDateFromLong(startdate).replace(":", "-") + ".xlsx";
		try {
			String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
					+ outFileName;
			FileOutputStream fos = new FileOutputStream(new File(file));

			/*
			 * Set<PosixFilePermission> ownerWritable =
			 * PosixFilePermissions.fromString("rw-r--r--"); FileAttribute<?>
			 * permissions =
			 * PosixFilePermissions.asFileAttribute(ownerWritable);
			 * Files.createFile(file_l.toPath(), permissions);
			 * 
			 * 
			 * FileOutputStream fos = new FileOutputStream(file_l);
			 */
			workbook.write(fos);
			fos.flush();
			fos.close();
			workbook.close();
			// String sendemail="rupesh.p@mykiddytracker.com,";

			/*
			 * String sendemail = ""; for (String emaString :
			 * mailSendInfo.get(j).getEmailIds()) { if
			 * (emaString.trim().contains
			 * ("enggctljpgps@gmail.com")||emaString.contains("den")) {
			 * sendemail = sendemail + "," + emaString.trim();
			 * 
			 * } } SendEmail mail = new SendEmail();
			 * System.err.println("AlertForKeyManTotalBeatNotCover"+sendemail);
			 * 
			 * mail.sendDeviceExceptionMailToJaipur(sendemail, file,
			 * "All Section "
			 * ,"keymen Not Work properly Report","keymen Not Work properly Report"
			 * ,false);
			 */

		} catch (Exception e) {

			e.printStackTrace();
		}

		return msgo;
	}

	public synchronized ExceptionKeymanDTO GetExceptionKeyManListFromBeatNotCover(
			long startdate, long enddate, Connection con, DB mongoconnection,
			ArrayList<RailDeviceInfoDto> deviceInfoList, String inActiveDevice,
			String man) {
		Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();

		Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
		Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
		Set<String> KeyManOffDevice_Set = new HashSet<String>();
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

		int petrolMancount = 0;
		for (int i = 0; i < deviceInfoList.size(); i++) {
			// for (int i = 4; i<=4; i++) {

			Double startBatteryStatus = getStartBatteryStatus(mongoconnection,
					deviceInfoList.get(i).getDeviceID(), startdate);
			Double endBatteryStatus = getEndBatteryStatus(mongoconnection,
					deviceInfoList.get(i).getDeviceID(), startdate);

			if (deviceInfoList.get(i).getName().startsWith(man)) {
				int maxSpeed = 0;

				RailwayKeymanTripsBeatsList
						.removeAll(RailwayKeymanTripsBeatsList);
				exceptiionalTrip.removeAll(exceptiionalTrip);
				try {

					java.sql.CallableStatement psgettrip = con
							.prepareCall("{call GetTripSheduleOfKeyman(?)}");

					psgettrip.setInt(1, deviceInfoList.get(i).getStudentId());

					ResultSet rsgertrip = psgettrip.executeQuery();
					if (rsgertrip != null) {
						while (rsgertrip.next()) {

							RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
							dto.setId(rsgertrip.getInt("Id"));

							dto.setStudentId(rsgertrip.getInt("StudentId"));
							dto.setDeviceId(rsgertrip.getString("DeviceID"));

							dto.setKmStart(rsgertrip.getDouble("KmStart"));
							dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

							dto.setSectionName(rsgertrip
									.getString("SectionName"));

							dto.setKmStartLat((rsgertrip
									.getDouble("kmStartLat")));
							dto.setKmStartLang(rsgertrip
									.getDouble("kmStartLang"));
							dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
							dto.setKmEndLang(rsgertrip.getDouble("kmEndLang"));
							RailwayKeymanTripsBeatsList.add(dto);

						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				long DayStartWorkTime = Long.valueOf(Common
						.getGMTTimeStampFromDate(day + "-"
								+ String.valueOf(month + 1) + "-" + year
								+ " 00:00 am"));

				Double distanceTolerance = 1.3;

				// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
				// RailwayPetrolmanTripsBeatsList) {
				for (int r = 0; r < RailwayKeymanTripsBeatsList.size(); r++) {
					RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayKeymanTripsBeatsList
							.get(r);

					DBCollection table = mongoconnection
							.getCollection(Common.TABLE_LOCATION);
					BasicDBObject device_whereQuery = new BasicDBObject();
					device_whereQuery.put("device",
							Long.parseLong(railMailSendInfoDto.getDeviceId()));
					// device_whereQuery.put("device",Long.parseLong("355488020181042"));

					BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
							"timestamp", new BasicDBObject("$gte",
									DayStartWorkTime + 18900).append("$lt",
									DayStartWorkTime + 36000));
					BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
							"timestamp", new BasicDBObject("$gte",
									DayStartWorkTime + 43200).append("$lt",
									DayStartWorkTime + 61200));

					BasicDBList morn_Milage = new BasicDBList();
					morn_Milage.add(timestamp_whereQuery_morning);

					morn_Milage.add(device_whereQuery);
					DBObject morn_Milage_query = new BasicDBObject("$and",
							morn_Milage);

					BasicDBList after_Milage = new BasicDBList();
					after_Milage.add(timestamp_whereQuery_afternoon);
					after_Milage.add(device_whereQuery);
					DBObject after_Milage_query = new BasicDBObject("$and",
							after_Milage);

					BasicDBList Milage = new BasicDBList();
					Milage.add(morn_Milage_query);
					Milage.add(after_Milage_query);
					DBObject final_query = new BasicDBObject("$or", Milage);

					DBCursor cursor = table.find(final_query);

					cursor.sort(new BasicDBObject("timestamp", 1));

					cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

					System.err.println("Cursorlocation  Count of u-p11----"
							+ cursor.size() + "  " + final_query);
					// long i = 0;
					long total = cursor.size();
					Double MaxSpeed = 0.0;
					Double TotalSpeed = 0.0;
					int Triplocationcount = 0;
					ArrayList<DBObject> listObjects = new ArrayList<>();
					if (cursor.size() > 0) {

						System.err.println("*--listObjects---COunt---"
								+ cursor.size());

						listObjects = (ArrayList<DBObject>) cursor.toArray();

					}

					// Get sart kM Location
					if (listObjects.size() > 0) {

						System.err.println("*-----Km --stat-"
								+ railMailSendInfoDto.getKmStartLat() + ","
								+ railMailSendInfoDto.getKmStartLang()
								+ "------" + railMailSendInfoDto.getKmEndLat()
								+ "," + railMailSendInfoDto.getKmEndLang());
						;

						if (railMailSendInfoDto.getKmStartLat() > 0
								&& railMailSendInfoDto.getKmStartLang() > 0
								&& railMailSendInfoDto.getKmEndLat() > 0
								&& railMailSendInfoDto.getKmEndLang() > 0) {

							PoleNearByLocationDto tripStartLocation = locationNearbyKM(
									"Start", railMailSendInfoDto.getKmStart(),
									railMailSendInfoDto.getKmStartLat(),
									railMailSendInfoDto.getKmStartLang(),
									listObjects, DayStartWorkTime,"K");
							tripStartLocation
									.setStartkmBeatExpected(railMailSendInfoDto
											.getKmStart());
							tripStartLocation
									.setEndKmBeatExpected(railMailSendInfoDto
											.getKmEnd());

							PoleNearByLocationDto tripEndLocation = locationNearbyKM(
									"End", railMailSendInfoDto.getKmEnd(),
									railMailSendInfoDto.getKmEndLat(),
									railMailSendInfoDto.getKmEndLang(),
									listObjects, DayStartWorkTime,"K");

							tripEndLocation
									.setStartkmBeatExpected(railMailSendInfoDto
											.getKmStart());
							tripEndLocation
									.setEndKmBeatExpected(railMailSendInfoDto
											.getKmEnd());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setTripStart(tripStartLocation);
							exDto.setTripEnd(tripEndLocation);
							exDto.setLocationSize(listObjects.size());
							exceptiionalTrip.add(exDto);
						} else {

							exceptiionalTrip.add(null);
						}
						listObjects.removeAll(listObjects);
					} else {

						KeyManOffDevice_Set
								.add(deviceInfoList.get(i).getName());

						ExceptionReortsTrip exDto = new ExceptionReortsTrip();
						exDto.setTripNo(r + 1);
						exDto.setLocationSize(0);
						exDto.setTripStart(null);
						exDto.setTripEnd(null);
						exceptiionalTrip.add(exDto);
					}

				}

				// System.out.println("exceptionlist-----"+new
				// Gson().toJson(exceptionReortsTrip));

				if (exceptiionalTrip.size() > 0
						&& exceptiionalTrip.get(0) != null) {
					StringBuilder remark = new StringBuilder();

					for (ExceptionReortsTrip exceptionReortsTrip : exceptiionalTrip) {
						if (exceptionReortsTrip != null
								&& exceptionReortsTrip.getTripStart() != null
								&& exceptionReortsTrip.getLocationSize() > 0) {

							try {
								// System.out.println(" **exceptionReortsTrip*******row*********************--------"+new
								// Gson().toJson(exceptionReortsTrip));

								/*
								 * double kmcover = (exceptionReortsTrip
								 * .getTripStart() .getStartkmBeatActual() -
								 * exceptionReortsTrip .getTripEnd()
								 * .getEndKmBeatActual()); if (kmcover < 0)
								 * kmcover = -1 * kmcover;
								 * 
								 * double expactedKmCover = exceptionReortsTrip
								 * .getTripStart() .getEndKmBeatExpected() -
								 * exceptionReortsTrip .getTripStart()
								 * .getStartkmBeatExpected(); if
								 * (expactedKmCover < 0) expactedKmCover = -1*
								 * expactedKmCover;
								 * 
								 * 
								 * 
								 * if (kmcover < expactedKmCover -
								 * distanceTolerance) { if
								 * (!inActiveDevice.contains
								 * (deviceInfoList.get(i).getName()))
								 * KeyManExceptionalDevices_Set
								 * .add(deviceInfoList.get(i).getName()); }
								 * 
								 * else { KeyManCoverSucefullyDevices_Set.add(
								 * deviceInfoList.get(i).getName());
								 * 
								 * }
								 */

								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = 0;
									double expactedKmCover = 0;
									if (exceptionReortsTrip.getTripStart()
											.getStartkmBeatActual() != 0
											&& exceptionReortsTrip.getTripEnd()
													.getEndKmBeatActual() != 0
											&& exceptionReortsTrip
													.getTripStart()
													.getMinDistance() < 5
											&& exceptionReortsTrip.getTripEnd()
													.getMinDistance() < 5) {
										kmcover = (exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatActual() - exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatActual());
										if (kmcover < 0)
											kmcover = -1 * kmcover;

										expactedKmCover = exceptionReortsTrip
												.getTripStart()
												.getEndKmBeatExpected()
												- exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected();
										if (expactedKmCover < 0)
											expactedKmCover = -1
													* expactedKmCover;
										System.err
												.println(" **kmcover*******row***********expactedKmCover******-------"
														+ deviceInfoList.get(i)
																.getName()
														+ "------------"
														+ kmcover
														+ " -----------"
														+ expactedKmCover);

										if (kmcover < expactedKmCover
												- distanceTolerance) {
											if (!KeyManOffDevice_Set
													.contains(deviceInfoList
															.get(i).getName()
															.trim()))
												KeyManExceptionalDevices_Set
														.add(deviceInfoList
																.get(i)
																.getName()
																.trim());
										} else {
											KeyManCoverSucefullyDevices_Set
													.add(deviceInfoList.get(i)
															.getName().trim());

										}

									} else {
										if (exceptionReortsTrip.getTripStart()
												.getTotal_distance() < 1) {
											if (!KeyManOffDevice_Set
													.contains(deviceInfoList
															.get(i).getName()
															.trim()))
												KeyManExceptionalDevices_Set
														.add(deviceInfoList
																.get(i)
																.getName()
																.trim());

										} else if (exceptionReortsTrip
												.getTripStart()
												.getMinDistance() > 5
												&& exceptionReortsTrip
														.getTripEnd()
														.getMinDistance() > 5) {
											if (!KeyManOffDevice_Set
													.contains(deviceInfoList
															.get(i).getName()
															.trim()))
												KeyManExceptionalDevices_Set
														.add(deviceInfoList
																.get(i)
																.getName());
										}

									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								System.out.println("Max speed--------"
										+ exceptionReortsTrip.getTripStart()
												.getMaxSpeed() + "----"
										+ deviceInfoList.get(i).getName());

								if (exceptionReortsTrip.getTripStart()
										.getMaxSpeed() > 8) {
									KeyManOverspeedDevices_Set
											.add(deviceInfoList.get(i)
													.getName());
								}

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {

							if (!KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName().trim()))
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

						}
					}

				} else {
					System.err
							.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
									+ RailwayKeymanTripsBeatsList.size()
									+ "----"
									+ deviceInfoList.get(i).getName()
									+ "(" + deviceInfoList.get(i).getDeviceID());

					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) == null)
						if (!KeyManOffDevice_Set.contains(deviceInfoList.get(i)
								.getName().trim()))
							KeyManExceptionalDevices_Set.add(deviceInfoList
									.get(i).getName());
						else if (!KeyManOffDevice_Set.contains(deviceInfoList
								.get(i).getName().trim()))
							KeyManExceptionalDevices_Set.add(deviceInfoList
									.get(i).getName());

				}

			}
			// System.out.println("ttttt--send mail---"
			// + deviceInfoList.size() + "----");
			exceptiionalTrip.removeAll(exceptiionalTrip);

			// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());

		}
		;
		System.out.println("inActiveDevice--------Size-----------"
				+ inActiveDevice);

		ExceptionKeymanDTO keymanDto = new ExceptionKeymanDTO();
		keymanDto.setTotalBeatCoverSucesfull(KeyManCoverSucefullyDevices_Set
				.toString().replace("[", "").replace("]", ""));
		keymanDto.setTotalBeatnotCover(KeyManExceptionalDevices_Set.toString()
				.replace("[", "").replace("]", ""));
		keymanDto.setOverSpeedDevices(KeyManOverspeedDevices_Set.toString()
				.replace("[", "").replace("]", ""));
		keymanDto.setKeyManOffDevice(KeyManOffDevice_Set.toString()
				.replace("[", "").replace("]", ""));
		return keymanDto;

	}

	public MessageObject UpdateDevicesIMEIAndSimNo(Connection con,
			String simno, String imeiNo, String idno, String parentId) {

		String[] simno_array = simno.split(",");
		String[] idno_array = idno.split(",");
		String[] imei_array = imeiNo.split(",");

		MessageObject msgo = new MessageObject();
		for (int i = 0; i < idno_array.length; i++) {

			try {

				// ////System.err.UpdateDevicesIMEIAndSimNoprint(emailid);
				int result = 0;

				java.sql.CallableStatement ps = con
						.prepareCall("{call UpdateDevicesIMEIAndSimNo(?,?,?,?)}");
				ps.setString(1, simno_array[i].trim());
				ps.setString(2, "-" + idno_array[i].trim());
				ps.setInt(3, Integer.parseInt(parentId));
				ps.setString(4, imei_array[i].trim());

				System.err.println("UpdateDevicesIMEIAndSimNo==" + ps
						+ simno_array[i].trim() + "---" + idno_array[i].trim());

				result = ps.executeUpdate();

				if (result == 0) {
					msgo.setError("true");
					msgo.setMessage("User is not Update successfully");
				} else {
					// //System.err.println("Error=="+result);
					msgo.setError("false");
					msgo.setMessage("User is Update successfully");
				}

				/*
				 * psx.setString(1, name_array[i].trim()); psx.setInt(2,534981);
				 * psx.setString(3, idno_array[i].trim());
				 */

			} catch (Exception e) {
				msgo.setError("true");
				msgo.setMessage("Error :" + e.getMessage());
				e.printStackTrace();
			}
		}

		return msgo;

	}

	public HistoryInfoDTO GetHistorydata(DB mongoconnection, String student_id,
			String startdate, String enddate) {
		HistoryInfoDTO hid = new HistoryInfoDTO();
		ArrayList<HistoryDTO> dtp = new ArrayList<HistoryDTO>();

		String DeviceIMIE_No = student_id;
	
		try {
			if (DeviceIMIE_No.length() > 0) {
				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_LOCATION);
				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device", Long.parseLong(DeviceIMIE_No));

				BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp", new BasicDBObject("$gte",
								Long.parseLong(startdate)).append("$lt",
								Long.parseLong(enddate)));

				BasicDBList And_Milage = new BasicDBList();
				And_Milage.add(timestamp_whereQuery);
				And_Milage.add(device_whereQuery);
				DBObject Total_Milage_query = new BasicDBObject("$and",
						And_Milage);

				DBCursor cursor = table.find(Total_Milage_query).sort(
						new BasicDBObject("timestamp", 1));// .limit(200);
				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				System.out.println("Cursor Count of u-p11----" + cursor.size()
						+ "  " + Total_Milage_query);

				if (cursor.size() != 0) {
					int i = 0;
					while (cursor.hasNext()) {
						DBObject dbObject = cursor.next();
					//	if (i < 500) {
							HistoryDTO obj = new HistoryDTO();
							DBObject dbObject_location = (DBObject) dbObject
									.get("location");

							obj.setLan(dbObject_location.get("lon").toString());
							obj.setLat(dbObject_location.get("lat").toString());

							BasicDBList status = (BasicDBList) dbObject
									.get("status");
							/*
							 * obj.setLan_direction(status.get(3).get(
							 * "lon_direction" ).toString());
							 * obj.setLat_direction(status.get(4).get(
							 * "lat_direction").toString());
							 */
							obj.setLan_direction((status.get(2) + "").replace(
									"{ \"lon_direction\" : \"", ""));

							obj.setLan_direction(obj.getLan_direction()
									.replace("\"}", ""));

							obj.setLat_direction((status.get(3) + "").replace(
									"{ \"lat_direction\" : \"", ""));
							obj.setLat_direction(obj.getLat_direction()
									.replace("\"}", ""));

							obj.setSpeed(dbObject.get("speed").toString());
							obj.setTimestamp(dbObject.get("timestamp")
									.toString());
							
							if (dbObject.containsField("blind")) {
								obj.setIsBlind(Integer.parseInt(dbObject.get("blind")+""));
								obj.setBlindLocationGetTimestamp(new ObjectId(dbObject.get("_id").toString()).getTime()/1000+"");

								
							}else{
								obj.setIsBlind(0);
								obj.setBlindLocationGetTimestamp("0");
							}
							

						/*	int BatteryStatus=getLatestBatteryStatus(mongoconnection, DeviceIMIE_No, Long.parseLong(obj.getTimestamp()));
							if(BatteryStatus==999)
								obj.setBatteryStatus("Not Found");
							else
							obj.setBatteryStatus(BatteryStatus+"");
							*/
							dtp.add(obj);
					//	}
						i++;
					}
					hid.setHistoryInfo(dtp);
					hid.setHistoryDataSize(cursor.size());
				} else {

				}

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return hid;
	}

	/*
	 * public static SmallestDistanceDto distancePole (LatLng L1,
	 * ArrayList<FeatureAddressDetailsDTO> featurelist ) { double earthRadius =
	 * 3958.75; double minDistance = 0; int position = 0; for (int
	 * i=0;i<featurelist.size();i++){ LatLng L2=new
	 * LatLng(featurelist.get(i).getLatitude(),
	 * featurelist.get(i).getLongitude()); double latDiff =
	 * Math.toRadians(L2.latitude-L1.latitude); double lngDiff =
	 * Math.toRadians(L2.longitude-L1.longitude); double a = Math.sin(latDiff
	 * /2) * Math.sin(latDiff /2) + Math.cos(Math.toRadians(L1.latitude)) *
	 * Math.cos(Math.toRadians(L2.latitude)) * Math.sin(lngDiff /2) *
	 * Math.sin(lngDiff /2); double c = 2 * Math.atan2(Math.sqrt(a),
	 * Math.sqrt(1-a)); double distance = earthRadius * c;
	 * 
	 * if (i==0) minDistance=distance; else if (distance<minDistance) {
	 * minDistance=distance; position=i; }
	 * 
	 * } int meterConversion = 1609; SmallestDistanceDto dto=new
	 * SmallestDistanceDto(); dto.setDistance(((int)(minDistance *
	 * meterConversion))+""); dto.setPolePosition(position); return dto ;
	 * 
	 * }
	 */

	public ArrayList<HistoryDTO> GenerateLocationIPReport(DB mongoconnection) {
		HistoryInfoDTO hid = new HistoryInfoDTO();
		ArrayList<HistoryDTO> dtp = new ArrayList<HistoryDTO>();

		try {

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("KeyMan Status Report"));
			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short) 20);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			CellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap

			ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();
			Row row = sheet.createRow(0);

			row = sheet.createRow(sheet.getLastRowNum() + 1);
			row.createCell(0);

			row = sheet.createRow(sheet.getLastRowNum() + 1);
			row.createCell(0);

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue("LoactionReport :-");
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 5));
			row.getCell(0).setCellStyle(HeadingStyle);

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			Cell cell = row.createCell(0);

			cell.setCellValue("device");
			cell.setCellStyle(tripColumnStyle);
			cell = row.createCell(1);
			cell.setCellValue("lat");
			cell.setCellStyle(tripColumnStyle);
			cell = row.createCell(2);
			cell.setCellValue("lon");
			cell.setCellStyle(tripColumnStyle);
			cell = row.createCell(3);
			cell.setCellValue("speed");
			cell.setCellStyle(tripColumnStyle);
			;
			cell = row.createCell(4);
			cell.setCellValue("timestamp");
			cell.setCellStyle(tripColumnStyle);

			cell = row.createCell(5);
			cell.setCellValue("IPaddres");
			cell.setCellStyle(tripColumnStyle);
			cell = row.createCell(6);
			cell.setCellValue("Pid");
			cell.setCellStyle(tripColumnStyle);

			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_LOCATION);
			BasicDBObject device_whereQuery = new BasicDBObject();
			/*
			 * device_whereQuery.put("device", Long.parseLong(DeviceIMIE_No));
			 * 
			 * BasicDBObject timestamp_whereQuery = new BasicDBObject(
			 * "timestamp", new BasicDBObject("$gte",
			 * Long.parseLong(startdate)).append( "$lt",
			 * Long.parseLong(enddate)));
			 * 
			 * BasicDBList And_Milage = new BasicDBList();
			 * And_Milage.add(timestamp_whereQuery);
			 * And_Milage.add(device_whereQuery); DBObject Total_Milage_query =
			 * new BasicDBObject("$and", And_Milage);
			 */
			DBCursor cursor = table.find(device_whereQuery).sort(
					new BasicDBObject("timestamp", -1));// .limit(200);
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			System.out.println("Cursor Count of u-p11----" + cursor.size()
					+ "  " + device_whereQuery);

			if (cursor.size() != 0) {
				int i = 0;
				while (cursor.hasNext()) {
					DBObject dbObject = cursor.next();

					HistoryDTO obj = new HistoryDTO();
					DBObject dbObject_location = (DBObject) dbObject
							.get("location");

					/*
					 * obj.setLan(dbObject_location.get("lon").toString());
					 * obj.setLat(dbObject_location.get("lat").toString());
					 * 
					 * BasicDBList status = (BasicDBList) dbObject
					 * .get("status");
					 * 
					 * obj.setLan_direction((status.get(2) + "").replace(
					 * "{ \"lon_direction\" : \"", ""));
					 * 
					 * obj.setLan_direction(obj.getLan_direction().replace(
					 * "\"}", ""));
					 * 
					 * obj.setLat_direction((status.get(3) + "").replace(
					 * "{ \"lat_direction\" : \"", ""));
					 * obj.setLat_direction(obj.getLat_direction().replace(
					 * "\"}", ""));
					 * 
					 * obj.setSpeed(dbObject.get("speed").toString());
					 * obj.setTimestamp(dbObject.get("timestamp").toString());
					 * dtp.add(obj);
					 */

					try {

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue(dbObject.get("device").toString());
						cell.setCellStyle(wrap_style);

						cell = row.createCell(1);
						cell.setCellValue(dbObject_location.get("lat")
								.toString());
						cell.setCellStyle(wrap_style);

						cell = row.createCell(2);
						cell.setCellValue(dbObject_location.get("lon")
								.toString());
						cell.setCellStyle(wrap_style);

						cell = row.createCell(3);
						cell.setCellValue(dbObject.get("speed").toString());

						cell.setCellStyle(wrap_style);

						cell = row.createCell(4);
						cell.setCellValue(Common.getDateCurrentTimeZone(Long
								.parseLong(dbObject.get("timestamp").toString())));

						cell.setCellStyle(wrap_style);

						cell = row.createCell(5);
						BasicDBList ip_list = (BasicDBList) dbObject
								.get("ipaddress");

						cell.setCellValue(getIPaddress_String(ip_list));

						cell.setCellStyle(wrap_style);

						cell = row.createCell(6);
						BasicDBList pid_list = (BasicDBList) dbObject
								.get("pid");

						cell.setCellValue(getIPaddress_String(pid_list));

						cell.setCellStyle(wrap_style);

						// System.out.println("  Here--*****************************************--------------------");
						//

					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MongoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			} else {

			}

			String outFileName = "All_LOcation_Report_"
					+ Common.getDateCurrentTimeZone(System.currentTimeMillis())
					+ ".xlsx";

			String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
					+ outFileName;
			try {
				File file_l = new File(file);

				FileOutputStream fos = new FileOutputStream(new File(file));

				/*
				 * Set<PosixFilePermission> ownerWritable =
				 * PosixFilePermissions.fromString("rw-r--r--");
				 * FileAttribute<?> permissions =
				 * PosixFilePermissions.asFileAttribute(ownerWritable);
				 * Files.createFile(file_l.toPath(), permissions);
				 * 
				 * 
				 * FileOutputStream fos = new FileOutputStream(file_l);
				 */
				workbook.write(fos);
				fos.flush();
				fos.close();
				workbook.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	private String getIPaddress_String(BasicDBList ip_list) {
		StringBuilder ip = new StringBuilder();
		// TODO Auto-generated method stub
		for (int i = 0; i < ip_list.size(); i++) {
			ip.append(Character.toString((char) (int) ip_list.get(i)));
		}
		System.err.println(ip.toString());

		return ip.toString();
	}

	public MessageObject GenerateExceptionReportPetrolmanForVaranasi(
			Connection con, DB mongoconnection, String parentId) {

		MessageObject msgo = new MessageObject();
		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		for (int j = 0; j < mailSendInfo.size(); j++) {
			// for (int j = 5; j < mailSendInfo.size();j++) {

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);
			long startdate = Long.valueOf(Common.getGMTTimeStampFromDate(day-1
					+ "-" + String.valueOf(month + 1) + "-" + year
					+ " 05:00 am"));

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report of Patrolman"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyle.setAlignment(HorizontalAlignment.CENTER);
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);

			CellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap

			int petrolMancount = 0;
			long DayStartWorkTime = Long.valueOf(Common
					.getGMTTimeStampFromDate(day-1 + "-"
							+ String.valueOf(month + 1) + "-" + year
							+ " 00:00 am"));

			Row row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for Patrolman of section "
							+ mailSendInfo.get(j).getDept() + "  Date: "
							+ Common.getDateFromLong(startdate));
			row.getCell(0).setCellStyle(HeadingStyle);
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 6));

			for (int i = 0; i < deviceInfoList.size(); i++) {
				// for (int i = 4; i <=4; i++) {

				System.err.println(" **Device =---================"
						+ deviceInfoList.get(i).getName() + "\n");

				Double startBatteryStatus = getKeyManStartBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						startdate);
				Double endBatteryStatus = getKeyManEndBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						startdate);

				if (deviceInfoList.get(i).getName().startsWith("P/")) {

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfKeyman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));

								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

								dto.setSectionName(rsgertrip
										.getString("SectionName"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));
								RailwayKeymanTripsBeatsList.add(dto);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					// long DayStartWorkTime=1547836200;
					// System.out.println("DayStartWorkTime----" +
					// DayStartWorkTime);

					Double distanceTolerance = 1.3;
					// Double startLatOfRouteKm = 0.0, startLangOfRouteKm = 0.0,
					// endLatOfRouteKm = 0.0, endLangOfRouteKm = 0.0;

					// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
					// RailwayPetrolmanTripsBeatsList) {
					for (int r = 0; r < RailwayKeymanTripsBeatsList.size(); r++) {
						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayKeymanTripsBeatsList
								.get(r);

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.parseLong("355488020181042"));

						/*
						 * BasicDBObject timestamp_whereQuery = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime).append("$lt",
						 * DayStartWorkTime+86400));
						 */

						BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 23400).append("$lt",
										DayStartWorkTime + 37800));
						BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 41400).append("$lt",
										DayStartWorkTime + 55800));

						/*
						 * BasicDBObject timestamp_whereQuery_morning = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+23400).append("$lt",
						 * DayStartWorkTime+37800)); BasicDBObject
						 * timestamp_whereQuery_afternoon = new BasicDBObject(
						 * "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+41400).append("$lt",
						 * DayStartWorkTime+55800));
						 */
						BasicDBList morn_Milage = new BasicDBList();
						morn_Milage.add(timestamp_whereQuery_morning);

						morn_Milage.add(device_whereQuery);
						DBObject morn_Milage_query = new BasicDBObject("$and",
								morn_Milage);

						BasicDBList after_Milage = new BasicDBList();
						after_Milage.add(timestamp_whereQuery_afternoon);
						after_Milage.add(device_whereQuery);
						DBObject after_Milage_query = new BasicDBObject("$and",
								after_Milage);

						BasicDBList Milage = new BasicDBList();
						Milage.add(morn_Milage_query);
						Milage.add(after_Milage_query);
						DBObject final_query = new BasicDBObject("$or", Milage);

						DBCursor cursor = table.find(final_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + final_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							System.err.println("*--listObjects---COunt---"
									+ cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();

						}

						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());
							;

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = locationNearbyKM(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"P");
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation = locationNearbyKM(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"P");
								if (tripEndLocation.getEndKmBeatActual() <= 0) {
									tripStartLocation.setStartkmBeatActual(0);

								}

								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								exceptiionalTrip.add(exDto);
							} else {

								exceptiionalTrip.add(null);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setLocationSize(0);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exceptiionalTrip.add(exDto);
						}

					}
					System.out.println("\n\n");
					System.out
							.println("  Here--*************exceptiionalTrip****************************--------------------"
									+ exceptiionalTrip.size());
					System.out.println("\n\n");
					// //////////////////////////////////////////////

					// //////////////////////////////////
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null) {
						StringBuilder remark = new StringBuilder();
						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								deviceInfoList.get(i).getName() + "   ("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")");
						row.getCell(0).setCellStyle(HeadingStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));
						// start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((startBatteryStatus / 6) * 100))
								+ " %");
						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						// End Battery Staus
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("End Battery status :- "
								+ df2.format(((endBatteryStatus / 6) * 100))
								+ " %");
						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");
						remark_cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						cell = row.createCell(0);
						cell.setCellValue("Trip#");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(1);
						cell.setCellValue("Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue("Expected Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue("End Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(4);
						cell.setCellValue("Expected end Beat");
						cell.setCellStyle(tripColumnStyle);

						/*
						 * cell = row.createCell(5);
						 * cell.setCellValue("Avg speed");
						 * cell.setCellStyle(tripColumnStyle);
						 */

						cell = row.createCell(5);
						cell.setCellValue("Max speed");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(6);
						cell.setCellValue("Distance cover");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						for (ExceptionReortsTrip exceptionReortsTrip : exceptiionalTrip) {
							System.out
									.println(" **exceptiionalTrip=---================"
											+ new Gson()
													.toJson(exceptionReortsTrip));

							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null
									&& exceptionReortsTrip.getLocationSize() > 0) {

								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = 0;
									double expactedKmCover = 0;
									if (exceptionReortsTrip.getTripStart()
											.getStartkmBeatActual() != 0
											&& exceptionReortsTrip.getTripEnd()
													.getEndKmBeatActual() != 0
											&& exceptionReortsTrip
													.getTripStart()
													.getMinDistance() < 5
											&& exceptionReortsTrip.getTripEnd()
													.getMinDistance() < 5) {
										kmcover = (exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatActual() - exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatActual());
										if (kmcover < 0)
											kmcover = -1 * kmcover;
										expactedKmCover = exceptionReortsTrip
												.getTripStart()
												.getEndKmBeatExpected()
												- exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected();
										if (expactedKmCover < 0)
											expactedKmCover = -1
													* expactedKmCover;

										if (kmcover < expactedKmCover
												- distanceTolerance)
											remark.append("\tTotal beats not completed in Trip "
													+ exceptionReortsTrip
															.getTripNo() + ".");

									} else {
										kmcover = 0;
										if (exceptionReortsTrip.getTripStart()
												.getTotal_distance() < 1) {
											remark.append("\tDevice is not moved "
													+ ".");
										} else if (exceptionReortsTrip
												.getTripStart()
												.getMinDistance() > 5
												&& exceptionReortsTrip
														.getTripEnd()
														.getMinDistance() > 5) {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
										} else {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
										}

									}

									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());
										cell.setCellStyle(wrap_style);

										cell = row.createCell(1);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));
										cell.setCellStyle(wrap_style);

										cell = row.createCell(2);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatExpected());
										cell.setCellStyle(wrap_style);

										cell = row.createCell(3);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));
										cell.setCellStyle(wrap_style);

										cell = row.createCell(4);
										cell.setCellValue(exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatExpected());
										cell.setCellStyle(wrap_style);

										/*
										 * cell = row.createCell(5);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 * cell.setCellStyle(wrap_style);
										 */

										cell = row.createCell(5);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart().getMaxSpeed());
										cell.setCellStyle(wrap_style);

										cell = row.createCell(6);
										cell.setCellValue(df2.format(kmcover));
										cell.setCellStyle(wrap_style);

										// System.out.println("  Here--*****************************************--------------------");
										//

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// Exception of location not found
								if (exceptionReortsTrip != null
										&& exceptionReortsTrip
												.getLocationSize() == 0)
									try {
										// System.out.println(" **exceptionReortsTrip*******row******************"
										// +
										// "***-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size()+"-----"+exceptionReortsTrip.getLocationSize());

										remark.append("Device is Off");

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(1);
										cell.setCellStyle(loactionNotFoundColumnStyle);

										cell = row.createCell(1);
										cell.setCellValue("Device is Off");
										cell.setCellStyle(loactionNotFoundColumnStyle);
										sheet.addMergedRegion(new CellRangeAddress(
												sheet.getLastRowNum(), sheet
														.getLastRowNum(), 1, 3));

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								else {

								}

							}
						}

						if (remark.toString().length() > 0) {
							remark_cell.setCellValue("Remark status :- "
									+ remark.toString());

							remark_cell.setCellStyle(error_remarkColumnStyle);
						}

						else
							remark_cell
									.setCellValue("Remark status :- All work done succesfully.");

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayKeymanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null) {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(
										deviceInfoList.get(i).getName()
												+ "   ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ")");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								Cell cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :- RDPS data not found.");
								cell.setCellStyle(remarkColumnStyle);
								// cell.setCellStyle(error_remarkColumnStyle);

								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(

										deviceInfoList.get(i).getName()
												+ "  ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ") ");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								Cell cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :-Trips not Shedule for this device.");
								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());

				} else {
					// System.out.println("patrolman Man found--------------------");
					petrolMancount++;
				}
			}

			sheet.setColumnWidth(0, 1300);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 2000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);

			if (deviceInfoList.size() > 0
					&& petrolMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept() + "_"
						+ "Exception_Trip_Report_V_Patrolman_"
						+ Common.getDateFromLong(startdate).replace(":", "-")
						+ ".xlsx";
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));

					/*
					 * Set<PosixFilePermission> ownerWritable =
					 * PosixFilePermissions.fromString("rw-r--r--");
					 * FileAttribute<?> permissions =
					 * PosixFilePermissions.asFileAttribute(ownerWritable);
					 * Files.createFile(file_l.toPath(), permissions);
					 * 
					 * 
					 * FileOutputStream fos = new FileOutputStream(file_l);
					 */
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					String sendemail = "";
					for (String emaString : mailSendInfo.get(j).getEmailIds()) {
						if (emaString.trim().length() > 0) {
							if (sendemail.length() == 0) {
								sendemail = emaString.trim();
							} else {
								sendemail = sendemail + "," + emaString.trim();

							}

						}
					}
					SendEmail mail = new SendEmail();

					mail.sendDeviceExceptionMailToJaipur(sendemail, file,
							mailSendInfo.get(j).getDept(),
							"Exception Trip Report of Patrolman Varanasi",
							"Exception Trip Report of Patrolman Varanasi",
							false
							,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());


				} catch (Exception e) {

					e.printStackTrace();
				}

			}

		}

		return msgo;

	}

	public MessageObject AlertForPatrolmanWorkStatusReportForVaranasi(
			Connection con, DB mongoconnection, String parentId) {

		MessageObject msgo = new MessageObject();

		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);

		System.err.println("AlertForPatrolmanWorkStatusReportForVaranasi ---"
				+ new Gson().toJson(mailSendInfo));

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long startdate = Long.valueOf(Common.getGMTTimeStampFromDate(day + "-"
				+ String.valueOf(month + 1) + "-" + year + " 5:00 am"));
		long enddate = Long.valueOf(Common.getGMTTimeStampFromDate(day + "-"
				+ String.valueOf(month + 1) + "-" + year + " 4:00 pm"));

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(WorkbookUtil
				.createSafeSheetName("KeyMan Status Report"));
		XSSFCellStyle HeadingStyle = workbook.createCellStyle();
		HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// HeadingStyle.setFont(font);

		XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
		tripColumnStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex());
		tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyle.setWrapText(true);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// tripColumnStyle.setFont(font);

		XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
		remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
				.getIndex());
		remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle loactionNotFoundColumnStyle = workbook.createCellStyle();
		loactionNotFoundColumnStyle
				.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
		loactionNotFoundColumnStyle
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font = workbook.createFont();
		font.setFontHeightInPoints((short) 5);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_style = workbook.createCellStyle(); // Create new style
		wrap_style.setWrapText(true); // Set wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheet.createRow(0);

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0);

		row = sheet.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0);

		row = sheet.createRow(sheet.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"PatrolMan Work Status Report Date :-"
						+ Common.getDateFromLong(startdate));
		sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet
				.getLastRowNum(), 0, 5));
		row.getCell(0).setCellStyle(HeadingStyle);

		row = sheet.createRow(sheet.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyle);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyle);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyle);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyle);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyle);

		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyle);

		row.setHeight((short) 500);
		for (int j = 0; j < mailSendInfo.size(); j++) {
			// for (int j = 5; j <=5; j++) {
			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			ArrayList<Short> rowHeight = new ArrayList<>();
			StringBuilder LowBatteryDevices = new StringBuilder();
			StringBuilder InactiveBatteryDevices = new StringBuilder();

			for (int i = 0; i < deviceInfoList.size(); i++) {

				if (deviceInfoList.get(i).getName().startsWith("P/")) {
					Double startBatteryStatus = -1.0;

					try {
						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_ALERT_MSG);

						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery
								.put("device", Long.parseLong(deviceInfoList
										.get(i).getDeviceID()));

						BasicDBObject timestamp_whereQuery = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										startdate).append("$lt", enddate));
						BasicDBList And_Milage = new BasicDBList();
						And_Milage.add(timestamp_whereQuery);
						And_Milage.add(device_whereQuery);
						DBObject Total_Milage_query = new BasicDBObject("$and",
								And_Milage);

						DBCursor cursor = table.find(Total_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1)).limit(1);

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						// System.out.print("getBatteryStatus Count of u-p---"
						// + cursor.size() + "  " + Total_Milage_query);
						long total = cursor.count();

						if (cursor.size() != 0) {

							// System.err.println("*-getBatteryStatus----COunt---"
							// + cursor.size());

							while (cursor.hasNext()) {

								DBObject dbObject = (DBObject) cursor.next();

								startBatteryStatus = Double
										.parseDouble(dbObject
												.get("voltage_level") + "");

								System.err
										.println("*-getBatteryStatus----COunt---"
												+ startBatteryStatus);

							}

							if (startBatteryStatus < 3) {
								LowBatteryDevices.append(deviceInfoList.get(i)
										.getName());
								LowBatteryDevices.append(" , ");
							}

						} else {
							System.err
									.println("*-getBatteryStatus----COunt---00");
							InactiveBatteryDevices.append(deviceInfoList.get(i)
									.getName());
							InactiveBatteryDevices.append(" , ");
						}

					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MongoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

			row = sheet.createRow(sheet.getLastRowNum() + 1);
			row.setRowStyle(wrap_style);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			cell.setCellStyle(wrap_style); // Apply style to cell
			cell = row.createCell(1);
			cell.setCellValue(LowBatteryDevices.toString());
			cell.setCellStyle(wrap_style); // Apply style to cell
			rowHeight
					.add((short) ((LowBatteryDevices.toString().length() / 15) * 350));

			ExceptionKeymanDTO exceptionDto = GetExceptionKeyManListFromBeatNotCover(
					startdate, enddate, con, mongoconnection,
					mailSendInfo.get(j).getDeviceIds(),
					InactiveBatteryDevices.toString(), "P/");

			cell = row.createCell(2);
			cell.setCellValue(exceptionDto.getKeyManOffDevice());
			cell.setCellStyle(wrap_style); // Apply style to cell
			rowHeight
					.add((short) ((exceptionDto.getKeyManOffDevice().length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(exceptionDto.getTotalBeatnotCover());
			cell.setCellStyle(wrap_style); // Apply style to cell
			rowHeight.add((short) ((exceptionDto.getTotalBeatnotCover()
					.length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(exceptionDto.getTotalBeatCoverSucesfull());
			cell.setCellStyle(wrap_style); // Apply style to cell
			rowHeight.add((short) ((exceptionDto.getTotalBeatCoverSucesfull()
					.length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(exceptionDto.getOverSpeedDevices());
			cell.setCellStyle(wrap_style); // Apply style to cell
			rowHeight
					.add((short) ((exceptionDto.getOverSpeedDevices().length() / 15) * 350));

			short height_row = (short) ((LowBatteryDevices.length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);
			System.err.println("Row max height-----" + height_row);

		}
		sheet.setColumnWidth(0, 1500);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 3000);
		sheet.setColumnWidth(4, 3000);
		sheet.setColumnWidth(5, 3000);

		/*
		 * for (int s=1; s<3; s++){ sheet.autoSizeColumn(s); }
		 */

		String outFileName = "All Section Patrolman status Report_"
				+ Common.getDateFromLong(startdate).replace(":", "-") + ".xlsx";
		try {
			String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
					+ outFileName;
			FileOutputStream fos = new FileOutputStream(new File(file));

			/*
			 * Set<PosixFilePermission> ownerWritable =
			 * PosixFilePermissions.fromString("rw-r--r--"); FileAttribute<?>
			 * permissions =
			 * PosixFilePermissions.asFileAttribute(ownerWritable);
			 * Files.createFile(file_l.toPath(), permissions);
			 * 
			 * 
			 * FileOutputStream fos = new FileOutputStream(file_l);
			 */

			workbook.write(fos);
			fos.flush();
			fos.close();
			workbook.close();
			// String sendemail="rupesh.p@mykiddytracker.com,";

			String sendemail = "";
			for (String emaString : mailSendInfo.get(j).getEmailIds()) {
				if (emaString.trim().length() > 0) {
					if (sendemail.length() == 0) {
						sendemail = emaString.trim();
					} else {
						sendemail = sendemail + "," + emaString.trim();

					}

				}
			}
			System.err.println("Sendemail--------" + sendemail);
			SendEmail mail = new SendEmail();

			mail.sendDeviceExceptionMailToJaipur(sendemail, file,
					"All Section", " Patrolman Work status Report_",
					" Patrolman Work status Report_", true
					,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());


		} catch (Exception e) {

			e.printStackTrace();
		}

		return msgo;

	}

	public ArrayList<ExceptionReportFileDTO> getExceptionReportFile(
			Connection con, String parentIdReq, String timestamp) {

		ArrayList<ExceptionReportFileDTO> data = new ArrayList<>();
		String parentId="0";
		File folder = new File(
				Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN);
		File[] listOfFiles = folder.listFiles();
		System.out.println("time--------- "
				+ Common.getDateFromLong(Long.parseLong(timestamp))
				+ "-----file---");
		ArrayList<String> sectionData = new ArrayList<>();

		
		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetHierarchyChlidFromParent(?)}");
			ps.setString(1, parentIdReq);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					sectionData.add(rs.getString("DeptName"));
					parentId=rs.getString("ParentId");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int fileCount = 0; fileCount < listOfFiles.length; fileCount++) {
			if (listOfFiles[fileCount].isFile()) {

				ExceptionReportFileDTO dto = new ExceptionReportFileDTO();
				
				if (listOfFiles[fileCount].getName().contains(parentId)
						&& listOfFiles[fileCount].getName().contains(
								Common.getDateFromLong(
										Long.parseLong(timestamp)).replace(":",
										"-")))
				
				{
					 if(parentId.equals(parentIdReq))
					 {
						 dto.setFileName(listOfFiles[fileCount].getName());
							dto.setTime(Common.getDateFromLong(Long
									.parseLong(timestamp)));
							data.add(dto);
					 }
					 else
					 {
						 for (int sectionCount = 0; sectionCount < sectionData.size(); sectionCount++) 
							{
								
								if (listOfFiles[fileCount].getName().contains(sectionData.get(sectionCount))) 
								{
									dto.setFileName(listOfFiles[fileCount].getName());
									dto.setTime(Common.getDateFromLong(Long
											.parseLong(timestamp)));
									data.add(dto);
								}
							}
							
					 }
					

				}
			}
		}
		System.out.println("Files--------- " + new Gson().toJson(data));

		return data;
	}

	public ArrayList<ExceptionDeviceDTO> GetExceptionDeviceList(
			Connection con, DB mongoconnection, String parentId, String startTimestamp,
			String endTimestamp) {


		ArrayList<ExceptionDeviceDTO>  msgo = new ArrayList<ExceptionDeviceDTO> ();
		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_AllSectioWorkStatusReport);
			// System.out.println("device==============----------"+" "+table.getFullName());

			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("parentid", parentId);


			BasicDBObject timestamp_whereQuery = new BasicDBObject(
					"timestamp", new BasicDBObject("$gte",
							Long.parseLong(startTimestamp)).append("$lt",Long.parseLong(endTimestamp)));
			BasicDBList And_Milage = new BasicDBList();
			And_Milage.add(timestamp_whereQuery);
			And_Milage.add(device_whereQuery);
			DBObject Total_Milage_query = new BasicDBObject("$and",
					And_Milage);
			
			DBCursor cursor = table.find(Total_Milage_query)
					.sort(new BasicDBObject("timestamp", 1));
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			 System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+Total_Milage_query);
			long total = cursor.count();

			if (cursor.size() != 0) {

				System.err.println("*-GetExceptionDeviceList----COunt---"+ cursor.size());
				int i = 0;
				while (cursor.hasNext()) {

					DBObject dbObject = (DBObject) cursor.next();

					BasicDBList devicelistlist = (BasicDBList) dbObject
							.get("exceptional_device");
					System.err.println("*-GetExceptionDeviceList----COunt---"
							+ cursor.size());

				
					System.err.println("*-devicelistlist----" + i + "---"
							+ devicelistlist.size());

					for (int j = 0; j < devicelistlist.size(); j++) {
						DBObject dbObjectDevice = (DBObject) devicelistlist.get(j);

						//if((dbObjectDevice.get("remark")==null)){
						ExceptionDeviceDTO dto=new ExceptionDeviceDTO();
						dto.setTimestamp(dbObject.get("timestamp")+"");
						dto.setParentid(dbObject.get("parentid")+"");
						dto.setSection(dbObject.get("section")+"");
						dto.setDevice(dbObjectDevice.get("device")+"");
						dto.setDevice_name(dbObjectDevice.get("device_name")+"");
						dto.setEnd_battery_status(dbObjectDevice.get("end_battery_status")+"");
						dto.setEnd_beat(dbObjectDevice.get("end_beat")+"");
						dto.setExpected_end_beat(dbObjectDevice.get("expected_end_beat")+"");
						dto.setExpected_start_beat(dbObjectDevice.get("expected_start_beat")+"");
						dto.setKmcover(dbObjectDevice.get("kmcover")+"");
						dto.setMax_speed(dbObjectDevice.get("max_speed")+"");
						dto.setStart_battery_status(dbObjectDevice.get("start_battery_status")+"");
						dto.setStart_beat(dbObjectDevice.get("start_beat")+"");
						dto.setTripno(dbObjectDevice.get("Tripno")+"");

						msgo.add(dto);
						//}
					}

					i++;

				}

			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgo;
	}
	
	public ArrayList<DateRangeExceptionReportDTO> getDeviceRangeExceptionReport(
			Connection con, String parentId, String reportType, int startTimestamp, int endTimestamp) throws SQLException {
		ArrayList<DateRangeExceptionReportDTO> msgo = new ArrayList<DateRangeExceptionReportDTO>();
		try {
			ResultSet rs = null;
			String queryString = "";
			switch(reportType) {
				case "Low Battery Exception" :
					queryString = "select ReportTimeStamp,ParentId,SectionName,LowBatteryDevice from DateWiseExceptionReport where ParentId= ? AND ReportTimeStamp BETWEEN ? AND ? order by ReportTimeStamp Asc ";
					break;
				case "Beat Not Covered Exception" :
					queryString = "select ReportTimeStamp,ParentId,SectionName,BeatNotCoverDevice from DateWiseExceptionReport where ParentId= ? AND ReportTimeStamp BETWEEN ? AND ? order by ReportTimeStamp Asc ";
					break;
				case "Off Device Exception" :
					queryString = "select ReportTimeStamp,ParentId,SectionName,OffDevice from DateWiseExceptionReport where ParentId= ? AND ReportTimeStamp BETWEEN ? AND ? order by ReportTimeStamp Asc";
					break;
				case "Overspeed Exception" :
					queryString = "select ReportTimeStamp,ParentId,SectionName,OverSpeedDevice from DateWiseExceptionReport where ParentId= ? AND ReportTimeStamp BETWEEN ? AND ? order by ReportTimeStamp Asc";
					break;
			}
			PreparedStatement ps = con
					.prepareStatement(queryString);
			ps.setString(1, parentId);
			ps.setInt(2, startTimestamp);
			ps.setInt(3, endTimestamp);
			rs = ps.executeQuery();
			if (rs != null) {
				while(rs.next()){
					DateRangeExceptionReportDTO dto = new DateRangeExceptionReportDTO();

					dto.setParentId(rs.getInt("ParentId")+"");
					dto.setSectionName(rs.getString("SectionName"));
					if(reportType.equals("Low Battery Exception")) {
						dto.setDevicesName(rs.getString("LowBatteryDevice"));
					}
					else if(reportType.equals("Beat Not Covered Exception")) {
						dto.setDevicesName(rs.getString("BeatNotCoverDevice"));
					}
					else if(reportType.equals("Off Device Exception")) {
						dto.setDevicesName(rs.getString("OffDevice"));
					}
					else if(reportType.equals( "Overspeed Exception")) {
						dto.setDevicesName(rs.getString("OverSpeedDevice"));
					}
					dto.setTimestamp(rs.getInt("ReportTimeStamp"));
					msgo.add(dto);
					
				}
				

			} 
		}
		catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgo;
	}

	public MessageObject GenerateExceptionReportKeyManBeatPathJabalpur(
			Connection con, DB mongoconnection, String parentId) {


		MessageObject msgo = new MessageObject();
		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionReportKeyManBeatPathJabalpur ---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day + "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

		// ///////////////////////////////********************************/////////////////////////////
		// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("KeyMan Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"KeyMan Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						5));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);

		row.setHeight((short) 500);

		// ///////////////////////////////////All Section
		// End//////////////////////////////////////////////////////////

		for (int j = 0; j < mailSendInfo.size(); j++) {
			// for (int j = 0; j <=0;j++) {

			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report of keymen"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyle.setAlignment(HorizontalAlignment.CENTER);
			font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);

			CellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap
			wrap_style.setAlignment((short) 0);

			int petrolMancount = 0;
			

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for Keyman of section "
							+ mailSendInfo.get(j).getDept() + "  Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyle);
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 6));

			BasicDBList sectionDeviceList = new BasicDBList();

			for (int i = 0; i < deviceInfoList.size(); i++) {
				// for (int i = 119; i <=119; i++) {

//				System.err.println(" **Device =---================"
//						+ deviceInfoList.get(i).getName() + "\n"+mailSendInfo.get(j).getDept()+"---"+deviceInfoList.get(i)
//						.getStudentId());

				Double startBatteryStatus = getKeyManStartBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+18000);

				Double endBatteryStatus = getKeyManEndBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+18000);

				if (deviceInfoList.get(i).getName().startsWith("K/")) {
					BasicDBObject subdocument = new BasicDBObject();

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfKeyman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));

								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

								dto.setSectionName(rsgertrip
										.getString("SectionName"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));
								RailwayKeymanTripsBeatsList.add(dto);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					// long DayStartWorkTime=1547836200;
					// System.out.println("DayStartWorkTime----" +
					// DayStartWorkTime);

					Double distanceTolerance = 1.3;
					// Double startLatOfRouteKm = 0.0, startLangOfRouteKm = 0.0,
					// endLatOfRouteKm = 0.0, endLangOfRouteKm = 0.0;

					// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
					// RailwayPetrolmanTripsBeatsList) {
					for (int r = 0; r < RailwayKeymanTripsBeatsList.size(); r++) {
						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayKeymanTripsBeatsList
								.get(r);

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.eparseLong("355488020181042"));

						/*
						 * BasicDBObject timestamp_whereQuery = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime).append("$lt",
						 * DayStartWorkTime+86400));
						 */

						BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 23400).append("$lt",
										DayStartWorkTime + 37800));
						BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 41400).append("$lt",
										DayStartWorkTime + 55800));

						/*
						 * BasicDBObject timestamp_whereQuery_morning = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+23400).append("$lt",
						 * DayStartWorkTime+37800)); BasicDBObject
						 * timestamp_whereQuery_afternoon = new BasicDBObject(
						 * "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+41400).append("$lt",
						 * DayStartWorkTime+55800));
						 */
						BasicDBList morn_Milage = new BasicDBList();
						morn_Milage.add(timestamp_whereQuery_morning);

						morn_Milage.add(device_whereQuery);
						DBObject morn_Milage_query = new BasicDBObject("$and",
								morn_Milage);

						BasicDBList after_Milage = new BasicDBList();
						after_Milage.add(timestamp_whereQuery_afternoon);
						after_Milage.add(device_whereQuery);
						DBObject after_Milage_query = new BasicDBObject("$and",
								after_Milage);

						BasicDBList Milage = new BasicDBList();
						Milage.add(morn_Milage_query);
						Milage.add(after_Milage_query);
						DBObject final_query = new BasicDBObject("$or", Milage);

						DBCursor cursor = table.find(final_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + final_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							System.err.println("*--listObjects---COunt---"
									+ cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();

						}

						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());
							;

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = locationNearbyKM(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"K");
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation = locationNearbyKM(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"K");
								if (tripEndLocation.getEndKmBeatActual() <= 0) {
									tripStartLocation.setStartkmBeatActual(0);

								}

								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								System.out
								.println(" listObjects.get(0).get(timestamp)---11--"+listObjects.get(0).get("timestamp"));
								exDto.setDeviceStartTime( listObjects.get(0).get("timestamp")+"");
								exDto.setDeviceEndTime(listObjects.get(listObjects.size()-1).get("timestamp")+"");

								exceptiionalTrip.add(exDto);
							} else {

								exceptiionalTrip.add(null);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setLocationSize(0);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exceptiionalTrip.add(exDto);
						}

					}
					System.out.println("\n\n");
					System.out
							.println("  Here--*************exceptiionalTrip****************************--------------------"
									+ exceptiionalTrip.size());
					System.out.println("\n\n");
					// //////////////////////////////////////////////

					// //////////////////////////////////
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null) {
						StringBuilder remark = new StringBuilder();
						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								deviceInfoList.get(i).getName() + "   ("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")");
						subdocument.put("device", deviceInfoList.get(i)
								.getDeviceID());
						subdocument.put("device_name", deviceInfoList.get(i)
								.getName());

						row.getCell(0).setCellStyle(HeadingStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 12));
						// start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((startBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("start_battery_status",
								df2.format(((startBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 12));

						// End Battery Staus
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("End Battery status :- "
								+ df2.format(((endBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("end_battery_status",
								df2.format(((endBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 12));

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");

						remark_cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 12));

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						cell = row.createCell(0);
						cell.setCellValue("Trip#");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(1);
						cell.setCellValue("Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue("Expected Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue("End Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(4);
						cell.setCellValue("Expected end Beat");
						cell.setCellStyle(tripColumnStyle);

						/*
						 * cell = row.createCell(5);
						 * cell.setCellValue("Avg speed");
						 * cell.setCellStyle(tripColumnStyle);
						 */

						cell = row.createCell(5);
						cell.setCellValue("Max speed");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(6);
						cell.setCellValue("Distance cover");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						cell = row.createCell(7);
						cell.setCellValue("Expected StartTime");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						cell = row.createCell(8);
						cell.setCellValue("Actual StartTime");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						cell = row.createCell(9);
						cell.setCellValue("Expected End Time");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);


						cell = row.createCell(10);
						cell.setCellValue("Actual End Time");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);
						
						cell = row.createCell(11);
						cell.setCellValue("Actual Trip Cover");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						
						   cell = row.createCell(12);
                         cell.setCellValue("Expected Trip Cover");
                     	cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);
						
						for (ExceptionReortsTrip exceptionReortsTrip : exceptiionalTrip) {
							System.out
									.println(" **exceptiionalTrip=---================"
											+ new Gson()
													.toJson(exceptionReortsTrip));

							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null
									&& exceptionReortsTrip.getLocationSize() > 0) {

								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = 0;
									double expactedKmCover = 0;
									if (exceptionReortsTrip.getTripStart()
											.getStartkmBeatActual() != 0
											&& exceptionReortsTrip.getTripEnd()
													.getEndKmBeatActual() != 0
											&& exceptionReortsTrip
													.getTripStart()
													.getMinDistance() < 5
											&& exceptionReortsTrip.getTripEnd()
													.getMinDistance() < 5) {
										kmcover = (exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatActual() - exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatActual());
										if (kmcover < 0)
											kmcover = -1 * kmcover;
										expactedKmCover = exceptionReortsTrip
												.getTripStart()
												.getEndKmBeatExpected()
												- exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected();
										if (expactedKmCover < 0)
											expactedKmCover = -1
													* expactedKmCover;

										if (kmcover < expactedKmCover
												- distanceTolerance) {
											remark.append("\tTotal beats not completed in Trip "
													+ exceptionReortsTrip
															.getTripNo() + ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark_cell
													.setCellValue("Remark status :- All work done succesfully.");
											KeyManCoverSucefullyDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
										}

									} else {
										kmcover = 0;

										if (exceptionReortsTrip.getTripStart()
												.getTotal_distance() < 1) {
											remark.append("\tDevice is not moved "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else if (exceptionReortsTrip
												.getTripStart()
												.getMinDistance() > 5
												&& exceptionReortsTrip
														.getTripEnd()
														.getMinDistance() > 5) {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
										}

									}

									System.out
											.println(" **expactedKmCover******---"
													+ expactedKmCover
													+ "---kmcover-" + kmcover);

									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());
										subdocument
												.put("Tripno",
														exceptionReortsTrip
																.getTripNo());

										cell.setCellStyle(wrap_style);

										cell = row.createCell(1);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));
										subdocument
												.put("start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatActual()));
										cell.setCellStyle(wrap_style);

										cell = row.createCell(2);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatExpected());
										subdocument
												.put("expected_start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatExpected()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(3);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));
										subdocument.put("end_beat", df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(4);
										cell.setCellValue(exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatExpected());
										subdocument
												.put("expected_end_beat",
														df2.format(exceptionReortsTrip
																.getTripEnd()
																.getEndKmBeatExpected()));

										cell.setCellStyle(wrap_style);

										/*
										 * cell = row.createCell(5);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 * cell.setCellStyle(wrap_style);
										 */

										cell = row.createCell(5);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart().getMaxSpeed());
										subdocument.put("max_speed", df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getMaxSpeed()));

										if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8) {
											KeyManOverspeedDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
										}

										cell.setCellStyle(wrap_style);

										if (exceptionReortsTrip.getDeviceStartTime()!=null) {
											
											
											cell = row.createCell(6);
											cell.setCellValue(df2.format(kmcover));
											subdocument.put("kmcover",
													df2.format(kmcover));
											cell.setCellStyle(wrap_style);

											cell = row.createCell(7);
											cell.setCellValue(Common.getDateCurrentTimeZone(DayStartWorkTime+25200));
											cell.setCellStyle(wrap_style);
											
										

											cell = row.createCell(8);
											cell.setCellValue(Common.getDateCurrentTimeZone(Long.parseLong(exceptionReortsTrip
													.getDeviceStartTime())));

											cell.setCellStyle(wrap_style);
											cell = row.createCell(9);
											cell.setCellValue(Common.getDateCurrentTimeZone(DayStartWorkTime+57600));

											cell.setCellStyle(wrap_style);
											cell = row.createCell(10);
											cell.setCellValue(Common.getDateCurrentTimeZone(Long.parseLong(exceptionReortsTrip
													.getDeviceEndTime())));
											cell.setCellStyle(wrap_style);
											
											if (remark_cell.getStringCellValue().toString().contains("All work done succesfully")) {
												cell = row.createCell(11);
												cell.setCellValue("1");

												cell.setCellStyle(wrap_style);
											 }else{
                                                cell = row.createCell(11);
                                                cell.setCellValue("0");

                                                cell.setCellStyle(wrap_style);
                                            }
										
											   cell = row.createCell(12);
	                                            cell.setCellValue("1");
	                                            cell.setCellStyle(wrap_style);
	                                      
											
										}
									
										// System.out.println("  Here--*****************************************--------------------");
										//

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// Exception of location not found
								if (exceptionReortsTrip != null
										&& exceptionReortsTrip
												.getLocationSize() == 0)
									try {
										// System.out.println(" **exceptionReortsTrip*******row******************"
										// +
										// "***-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size()+"-----"+exceptionReortsTrip.getLocationSize());

										KeyManOffDevice_Set.add(deviceInfoList
												.get(i).getName());

										remark.append("Device is Off");

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(1);
										cell.setCellStyle(loactionNotFoundColumnStyle);

										cell = row.createCell(1);
										cell.setCellValue("Device is Off");
										cell.setCellStyle(loactionNotFoundColumnStyle);
										sheet.addMergedRegion(new CellRangeAddress(
												sheet.getLastRowNum(), sheet
														.getLastRowNum(), 1, 3));

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								else {

								}

							}
						}

						if (remark.toString().length() > 0) {
							remark_cell.setCellValue("Remark status :- "
									+ remark.toString());
							subdocument.put("remark", remark.toString());
							remark_cell.setCellStyle(error_remarkColumnStyle);
						}

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayKeymanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null) {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(
										deviceInfoList.get(i).getName()
												+ "   ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ")");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 12));
								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 12));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 12));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :- RDPS data not found.");
								subdocument.put("remark",
										"RDPS data not found.");
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								// cell.setCellStyle(error_remarkColumnStyle);

								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(

										deviceInfoList.get(i).getName()
												+ "  ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ") ");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 12));

								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 12));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 12));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :-Trips not Shedule for this device.");
								subdocument.put("remark",
										"Trips not Shedule for this device.");
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 12));

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());
					if (startBatteryStatus < 3
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName()))
						KeyManLowBattery_Set.add(deviceInfoList.get(i)
								.getName());

					sectionDeviceList.add(subdocument);

				} else {
					// System.out.println("patrolman Man found--------------------");
					petrolMancount++;
				}

			}

			Maindocument.put("exceptional_device", sectionDeviceList);

			report_table.insert(Maindocument);

			sheet.setColumnWidth(0, 1300);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 2000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);

			if (deviceInfoList.size() > 0
					&& petrolMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept().trim().replace("PWI/", "") + "_"
						+ "Exception_Trip_Report_KeyMan"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ "_" + parentId + ".xlsx";
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					String sendemail = "";
					for (String emaString : mailSendInfo.get(j).getEmailIds()) {
						sendemail = sendemail + "," + emaString.trim();
					}
					SendEmail mail = new SendEmail();

					
					mail.sendDeviceExceptionMailToJaipur(sendemail, file,
					 mailSendInfo
					  .get(j).getDept(),"Exception Trip Report of keymen"
					  ,"Exception Trip Report of keymen",false
						,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					 
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// /////ALl Section////////////////////////

			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReport);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			cell = row.createCell(1);
			cell.setCellValue(KeyManLowBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight
					.add((short) ((KeyManLowBattery_Set.toString()
							.replace("[", "").replace("]", "").toString()
							.length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(KeyManOffDevice_Set.toString().replace("[", "")
					.replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOffDevice_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			short height_row = (short) ((KeyManLowBattery_Set.toString()
					.replace("[", "").replace("]", "").toString().length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);
			System.err.println("Row max height-----" + height_row);

			sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

			if (j == mailSendInfo.size() - 1) {
				String outFileNameAllSectionStatus = "All Section KeyMan status Report_"
						+ parentId
						+ "_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ ".xlsx";
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReport.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReport.close();
					
					 String sendemail = getEmailIdForAllsectionReport(con,parentId);
						SendEmail mail = new SendEmail();
						System.err.println("Send mail All Section----" + sendemail);
							mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
									  "All Section"," KeyMan Work Status Report AllSection",
									  " KeyMan Work Status Report AllSection",true
										,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

				
					
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// /////////////////////*Alll Section////////////////////
			
			
			///Insert Datewise Saction Data
			 
			 
			 try {

					// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
					java.sql.CallableStatement ps = con
							.prepareCall("{call SaveDatewiseExceptionReport(?,?,?,?,?,?,?,?)}");

					
					ps.setInt(1, Integer.parseInt(parentId));
					ps.setString(2,DayStartWorkTime+"");
					ps.setString(3, KeyManLowBattery_Set.toString().replace("[", "")
							.replace("]", ""));

					ps.setString(4,KeyManOffDevice_Set.toString()
							.replace("[", "").replace("]", ""));

					ps.setString(5, KeyManExceptionalDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(6, KeyManOverspeedDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(7,mailSendInfo.get(j).getDept() );
					ps.setString(8, KeyManCoverSucefullyDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					int result = ps.executeUpdate();
					if (result == 0) {
						msgo.setError("true");
						msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
					} else {
						// //System.err.println("Error=="+result);
						msgo.setError("false");
						msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
				}

		}// //////////for section end

		return msgo;

	}
	
	
	public synchronized ExceptionKeymanDTO GetPatrolManListSatus(
			long startdate, long enddate, Connection con, DB mongoconnection,
			ArrayList<RailDeviceInfoDto> deviceInfoList, String inActiveDevice,
			String man) {
		Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();

		Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
		Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
		Set<String> KeyManOffDevice_Set = new HashSet<String>();
		Set<String> KeyManOnDevice_Set = new HashSet<String>();
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

		int petrolMancount = 0;
		for (int i = 0; i < deviceInfoList.size(); i++) {
			// for (int i = 4; i<=4; i++) {

			Double startBatteryStatus = getStartBatteryStatus(mongoconnection,
					deviceInfoList.get(i).getDeviceID(), startdate);
			Double endBatteryStatus = getEndBatteryStatus(mongoconnection,
					deviceInfoList.get(i).getDeviceID(), startdate);

			if (deviceInfoList.get(i).getName().startsWith(man)) {
				int maxSpeed = 0;
				

				long DayStartWorkTime = Long.valueOf(Common
						.getGMTTimeStampFromDate(day + "-"
								+ String.valueOf(month + 1) + "-" + year
								+ " 00:00 am"));

				Double distanceTolerance = 1.3;

				// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
				// RailwayPetrolmanTripsBeatsList) {
				//for (int r = 0; r < RailwayKeymanTripsBeatsList.size(); r++) {
					
					DBCollection table = mongoconnection
							.getCollection(Common.TABLE_LOCATION);
					BasicDBObject device_whereQuery = new BasicDBObject();
					device_whereQuery.put("device",
							Long.parseLong(deviceInfoList.get(i).getDeviceID()));
					// device_whereQuery.put("device",Long.parseLong("355488020181042"));

					BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
							"timestamp", new BasicDBObject("$gte",
									DayStartWorkTime + 18900).append("$lt",
									DayStartWorkTime + 36000));
					BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
							"timestamp", new BasicDBObject("$gte",
									DayStartWorkTime + 43200).append("$lt",
									DayStartWorkTime + 61200));

					BasicDBList morn_Milage = new BasicDBList();
					morn_Milage.add(timestamp_whereQuery_morning);

					morn_Milage.add(device_whereQuery);
					DBObject morn_Milage_query = new BasicDBObject("$and",
							morn_Milage);

					BasicDBList after_Milage = new BasicDBList();
					after_Milage.add(timestamp_whereQuery_afternoon);
					after_Milage.add(device_whereQuery);
					DBObject after_Milage_query = new BasicDBObject("$and",
							after_Milage);

					BasicDBList Milage = new BasicDBList();
					Milage.add(morn_Milage_query);
					Milage.add(after_Milage_query);
					DBObject final_query = new BasicDBObject("$or", Milage);

					DBCursor cursor = table.find(final_query);

					cursor.sort(new BasicDBObject("timestamp", 1));

					cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

					System.err.println("Cursorlocation  Count of u-p11----"
							+ cursor.size() + "  " + final_query);
					// long i = 0;
					long total = cursor.size();
					Double MaxSpeed = 0.0;
					Double TotalSpeed = 0.0;
					int Triplocationcount = 0;
					ArrayList<DBObject> listObjects = new ArrayList<>();
					if (cursor.size() > 0) {

						System.err.println("*--listObjects---COunt---"
								+ cursor.size());
						listObjects = (ArrayList<DBObject>) cursor.toArray();
					}
					// Get sart kM Location
					if (listObjects.size() > 0) {
						KeyManOnDevice_Set.add(deviceInfoList.get(i).getName());
					} else {

						KeyManOffDevice_Set
								.add(deviceInfoList.get(i).getName());
					}
			}
		
		}
		;
		System.out.println("inActiveDevice--------Size-----------"
				+ inActiveDevice);

		ExceptionKeymanDTO keymanDto = new ExceptionKeymanDTO();
		keymanDto.setTotalBeatCoverSucesfull(KeyManCoverSucefullyDevices_Set
				.toString().replace("[", "").replace("]", ""));
		keymanDto.setTotalBeatnotCover(KeyManExceptionalDevices_Set.toString()
				.replace("[", "").replace("]", ""));
		keymanDto.setOverSpeedDevices(KeyManOverspeedDevices_Set.toString()
				.replace("[", "").replace("]", ""));
		keymanDto.setKeyManOffDevice(KeyManOffDevice_Set.toString()
				.replace("[", "").replace("]", ""));
		return keymanDto;

	}

	public MessageObject SendWrongLocationEmail(String deviceId, String lat,
			String lan, String speed, Connection con) {
		SendEmail mail = new SendEmail();

		MessageObject msgo = new MessageObject();
		String name="";

		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetDetailsFromIMEI(?)}");
			ps.setString(1, deviceId);

			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					name=rs.getString("FirstName")+rs.getString("LastName");
				}

			}
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		/*if(Long.parseLong(deviceId)>0)
		 mail.SendWrongLocationEmail("Got wrong location of Name : "+name+" and IMEI NO: "+deviceId+" with https://www.google.com/maps/place/"+lat+","+lan+""
		 		+ " and Speed: "+speed);
		 */
		 msgo.setError("false");
			msgo.setMessage("SendWrongLocationEmail  Successfully");
		 return msgo;
	
	}

	public MessageObject ExportKeyManBeatToRDPSData(Connection con,
			DB mongoconnection, String parentId) {
		MessageObject msgo = new MessageObject();

		try {
			ArrayList<RailwayPetrolmanTripsBeatsDTO>RailwayKeymanTripsBeatsList=new ArrayList<>();

			java.sql.CallableStatement psgettrip = con
					.prepareCall("{call GetTripofAllKeyman(?)}");

			psgettrip.setInt(1,Integer.parseInt(parentId));

			ResultSet rsgertrip = psgettrip.executeQuery();
			if (rsgertrip != null) {
				while (rsgertrip.next()) {

					RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
					dto.setId(rsgertrip.getInt("Id"));

					dto.setStudentId(rsgertrip.getInt("StudentId"));
					dto.setDeviceId(rsgertrip.getString("DeviceID"));

					dto.setKmStart(rsgertrip.getDouble("KmStart"));
					dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

					dto.setSectionName(rsgertrip
							.getString("SectionName"));

					dto.setKmStartLat((rsgertrip
							.getDouble("kmStartLat")));
					dto.setKmStartLang(rsgertrip
							.getDouble("kmStartLang"));
					dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
					dto.setKmEndLang(rsgertrip
							.getDouble("kmEndLang"));
					RailwayKeymanTripsBeatsList.add(dto);

				}
			}
			
				

					BasicDBList addres_list = new BasicDBList();

					DBCollection table = mongoconnection
							.getCollection(Common.TABLE_RAILADDRESS);

					BasicDBObject Maindocument = new BasicDBObject();
					ObjectId objid = new ObjectId();
					Maindocument.put("address_id", "" + objid);
					
					Maindocument.put("filename","");
					Maindocument.put("ParentId", parentId);
					Maindocument.put("railway",  "Northen Railway");
					Maindocument.put("division",  "Jaipur");
					Maindocument.put("station_from", "");
					Maindocument.put("station_to", "");
					Maindocument.put("chainage",  "");
					Maindocument.put("trolley", "");
					Maindocument.put("timestamp", System.currentTimeMillis() / 1000);
					Maindocument.put("line",  "");
					Maindocument.put("mode",  "");
				
					for (int i = 0; i < RailwayKeymanTripsBeatsList.size(); i++) {

						BasicDBObject addresDocument = new BasicDBObject();
						addresDocument.put("kilometer", RailwayKeymanTripsBeatsList.get(i).getKmStart());
						addresDocument.put("distance",
								 "00");
						addresDocument.put("latitude",
								RailwayKeymanTripsBeatsList.get(i).getKmStartLat().toString()
								.replace(",", ""));
						addresDocument.put("longitude", RailwayKeymanTripsBeatsList.get(i).getKmStartLang().toString()
								.replace(",", ""));
						// addresDocument.put("feature_code",feature_Code.get(i).trim().replace(",",""));

						addresDocument.put("feature_detail","KeyMan Beat ,"+RailwayKeymanTripsBeatsList.get(i).getKmStart());
						addresDocument.put("feature_image",
								"~/Images/FeatureCodePhoto/fc_120.png");
						addresDocument.put("feature_code", "120");
						
						// addresDocument.put("section",station_From+"-"+station_TO);
						addresDocument.put("section",
								RailwayKeymanTripsBeatsList.get(i).getSectionName().toString());
						addresDocument.put("block_section", "");
						// addresDocument.put("remark",remark_array.get(i).trim());
						addres_list.add(addresDocument);
					}
				
					// System.err.println("---------addres_list----" +
					// addres_list.size());
					Maindocument.put("address_details", addres_list);

					table.insert(Maindocument);

					msgo.setError("false");
					msgo.setMessage("Address added successfully.---"
							+ addres_list.size());

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("address_details not added successfully.---");
				} catch (MongoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("address_details not added successfully.---");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("address_details not added successfully.---");
				}
			
		return msgo;

	}

	public ArrayList<RailDeviceInfoDto> TodayDeviceStatus(DB mongoconnection,
			Connection con, String parentId) {

		ArrayList<RailDeviceInfoDto> UserList = new ArrayList<>();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long DayStartWorkTimes = Long.valueOf(Common
				.getGMTTimeStampFromDate(day + "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		try {
			

			
			

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetDeviceRailAddress(?)}");
			ps.setString(1, parentId);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					RailDeviceInfoDto dto = new RailDeviceInfoDto();
					dto.setStudentId(rs.getInt(1));
					dto.setName(rs.getString(2));
					dto.setDeviceID(rs.getString(3));
					UserList.add(dto);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("TodayDeviceStatus==============----------"
				+ " " + UserList.size());

		for (int i = 0; i < UserList.size(); i++)
			try {
				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_TODAYLOCATION);
				// //System.out.println("device==============----------"+" "+table.getFullName());

				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device",
						Long.parseLong(UserList.get(i).getDeviceID()));

				/*BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp", new BasicDBObject("$lt",
								Long.parseLong(startdate)).append("$gte", (Long
								.parseLong(startdate) - (Integer
								.parseInt(pastMin) * 60))));

				BasicDBList And_Milage = new BasicDBList();
				And_Milage.add(timestamp_whereQuery);
				And_Milage.add(device_whereQuery);
				DBObject query = new BasicDBObject("$and", And_Milage);*/

				DBCursor cursor = table.find(device_whereQuery)
						.sort(new BasicDBObject("timestamp", -1)).limit(1);
				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				// System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+query);
				// long total = cursor.count();

				if (cursor.size() != 0) {

					//System.err.println("*-GenerateGPSHolder10MinData----COunt---"+cursor.size());

					while (cursor.hasNext()) {

						DBObject dbObject = (DBObject) cursor.next();

						BasicDBObject Sourceobj = (BasicDBObject) dbObject
								.get("location");

						UserList.get(i).setLang(Sourceobj.get("lon") + "");
						UserList.get(i).setLat(Sourceobj.get("lat") + "");

						// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
						// ""+UserList.get(i).getLang()));
						UserList.get(i).setSpeed(dbObject.get("speed") + "");
						UserList.get(i).setTime(dbObject.get("timestamp") + "");
						
						if (Long.parseLong(UserList.get(i).getTime())<DayStartWorkTimes)
							UserList.get(i).setDeviceOnStatus(0);
						else 
							UserList.get(i).setDeviceOnStatus(1);

						try {
							java.sql.CallableStatement ps = con
									.prepareCall("{call GetFeatureCodeswithDist(?,?,?)}");
							ps.setString(2, Sourceobj.get("lon") + "");
							ps.setString(1, Sourceobj.get("lat") + "");
							ps.setString(3, parentId);

							// System.err.println("yy ------"+UserList.get(i).getDeviceID()+"---"+new Gson().toJson(ps));;

							ResultSet rs = ps.executeQuery();

							if (rs != null) {

								while (rs.next()) {

									FeatureAddressDetailsDTO addressDto = new FeatureAddressDetailsDTO();
									addressDto.setDistance(rs
											.getString("Distance") + "");
									addressDto.setFeatureCode(rs
											.getString("FeatureCode") + "");
									addressDto.setFeature_image(rs
											.getString("Images") + "");
									addressDto.setFeatureDetail(rs
											.getString("FeatureDetail") + "");
									addressDto.setKiloMeter(rs
											.getString("kiloMeter") + "");
									addressDto.setLatitude(rs
											.getString("Latitude") + "");
									addressDto.setLongitude(rs
											.getString("Longitude") + "");
									addressDto.setSection(rs
											.getString("Section") + "");
									addressDto.setBlockSection(rs
											.getString("BlockSection") + "");
									addressDto.setNearByDistance(rs
											.getString("NearByDistance") + "");
									UserList.get(i).setRailFeatureDto(
											addressDto);

								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				} else {
					UserList.get(i).setDeviceOnStatus(0);

					DBCollection toadytable = mongoconnection
							.getCollection(Common.TABLE_TODAYLOCATION);
					BasicDBObject device_toadytablewhereQuery = new BasicDBObject();
					device_toadytablewhereQuery.put("device",
							Long.parseLong(UserList.get(i).getDeviceID()));

					DBCursor cursor_today = toadytable
							.find(device_toadytablewhereQuery)
							.sort(new BasicDBObject("timestamp", -1)).limit(1);
					cursor_today
							.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

					// System.err.println("Else ---query "+i+"==="+device_toadytablewhereQuery);
					// //System.out.println("cursor_today Count of u-p11----"+
					// cursor.size() + "  " + device_whereQuery);

					if (cursor_today.size() != 0) {

						for (DBObject dbObject : cursor_today) {

							DBObject dbObject_location = (DBObject) dbObject
									.get("location");

							UserList.get(i).setLang(
									dbObject_location.get("lon") + "");
							UserList.get(i).setLat(
									dbObject_location.get("lat") + "");
							UserList.get(i)
									.setSpeed(dbObject.get("speed") + "");
							UserList.get(i).setTime(
									dbObject.get("timestamp") + "");

						}
					} else {
						UserList.get(i).setLang("0");
						UserList.get(i).setLat("0");
						UserList.get(i).setSpeed("0");
						UserList.get(i).setTime("0");
					}

				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return UserList;
	}
	

	public ArrayList<RailDeviceInfoDto> DeviceONOffStatus(DB mongoconnection,
			String startdate, Connection con, String parentId) {
		MessageObject msgo = new MessageObject();
		ArrayList<RailDeviceInfoDto> UserList = new ArrayList<>();

		try {

			java.sql.CallableStatement ps = con
					.prepareCall("{call GetDeviceRailAddress(?)}");
			ps.setString(1, parentId);
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					RailDeviceInfoDto dto = new RailDeviceInfoDto();
					dto.setStudentId(rs.getInt(1));
					dto.setName(rs.getString(2));
					dto.setDeviceID(rs.getString(3));

					UserList.add(dto);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < UserList.size(); i++)
			try {
				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_LOCATION);
				// //System.out.println("device==============----------"+" "+table.getFullName());

				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device",
						Long.parseLong(UserList.get(i).getDeviceID()));

				BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp", new BasicDBObject("$gte",
								Long.parseLong(startdate)).append("$lt",
								Long.parseLong(startdate) + 86400));

				BasicDBList And_Milage = new BasicDBList();
				And_Milage.add(timestamp_whereQuery);
				And_Milage.add(device_whereQuery);
				DBObject query = new BasicDBObject("$and", And_Milage);

				DBCursor cursor = table.find(query)
						.sort(new BasicDBObject("timestamp", -1)).limit(1);
				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				// //System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+query);
				long total = cursor.count();

				if (cursor.size() != 0) {

					// //System.err.println("*-GetGeofenceHistory----COunt---"+cursor.size());

					while (cursor.hasNext()) {

						DBObject dbObject = (DBObject) cursor.next();

						BasicDBObject Sourceobj = (BasicDBObject) dbObject
								.get("location");

						BasicDBList status = (BasicDBList) dbObject
								.get("status");
						UserList.get(i).setLan_direction(
								(status.get(2) + "").replace(
										"{ \"lon_direction\" : \"", ""));

						UserList.get(i).setLan_direction(
								UserList.get(i).getLan_direction()
										.replace("\"}", ""));

						UserList.get(i).setLat_direction(
								(status.get(3) + "").replace(
										"{ \"lat_direction\" : \"", ""));
						UserList.get(i).setLat_direction(
								UserList.get(i).getLat_direction()
										.replace("\"}", ""));

						if (UserList.get(i).getLat_direction()
								.equalsIgnoreCase("N")
								&& UserList.get(i).getLan_direction()
										.equalsIgnoreCase("E")) {

							UserList.get(i).setLang(Sourceobj.get("lon") + "");
							UserList.get(i).setLat(Sourceobj.get("lat") + "");
							// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
							// ""+UserList.get(i).getLang()));

						} else if (UserList.get(i).getLat_direction()
								.equalsIgnoreCase("N")
								&& UserList.get(i).getLan_direction()
										.equalsIgnoreCase("W")) {

							UserList.get(i).setLang(
									"-" + Sourceobj.get("lon") + "");
							UserList.get(i).setLat(Sourceobj.get("lat") + "");
							// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
							// ""+UserList.get(i).getLang()));

						} else if (UserList.get(i).getLat_direction()
								.equalsIgnoreCase("S")
								&& UserList.get(i).getLan_direction()
										.equalsIgnoreCase("E")) {

							UserList.get(i).setLang(Sourceobj.get("lon") + "");
							UserList.get(i).setLat(
									"-" + Sourceobj.get("lat") + "");
							// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
							// ""+UserList.get(i).getLang()));

						} else if (UserList.get(i).getLat_direction()
								.equalsIgnoreCase("S")
								&& UserList.get(i).getLan_direction()
										.equalsIgnoreCase("W")) {

							UserList.get(i).setLang(
									"-" + Sourceobj.get("lon") + "");
							UserList.get(i).setLat(
									"-" + Sourceobj.get("lat") + "");
							// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
							// ""+UserList.get(i).getLang()));

						}

						UserList.get(i).setSpeed(dbObject.get("speed") + "");
						UserList.get(i).setTime(dbObject.get("timestamp") + "");
						UserList.get(i).setDeviceOnStatus(1);

					}

				} else {
					UserList.get(i).setDeviceOnStatus(0);
					
					DBCollection toadytable = mongoconnection
							.getCollection(Common.TABLE_TODAYLOCATION);
					BasicDBObject device_toadytablewhereQuery = new BasicDBObject();
					device_toadytablewhereQuery.put("device",
							Long.parseLong(UserList.get(i).getDeviceID()));

					DBCursor cursor_today = toadytable
							.find(device_toadytablewhereQuery)
							.sort(new BasicDBObject("timestamp", -1)).limit(1);
					cursor_today
							.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

					// System.err.println("Else ---query "+i+"==="+device_toadytablewhereQuery);
					// //System.out.println("cursor_today Count of u-p11----"+
					// cursor.size() + "  " + device_whereQuery);

					if (cursor_today.size() != 0) {

						for (DBObject dbObject : cursor_today) {

							BasicDBObject Sourceobj = (BasicDBObject) dbObject
									.get("location");

							BasicDBList status = (BasicDBList) dbObject
									.get("status");
							UserList.get(i).setLan_direction(
									(status.get(2) + "").replace(
											"{ \"lon_direction\" : \"", ""));

							UserList.get(i).setLan_direction(
									UserList.get(i).getLan_direction()
											.replace("\"}", ""));

							UserList.get(i).setLat_direction(
									(status.get(3) + "").replace(
											"{ \"lat_direction\" : \"", ""));
							UserList.get(i).setLat_direction(
									UserList.get(i).getLat_direction()
											.replace("\"}", ""));

							if (UserList.get(i).getLat_direction()
									.equalsIgnoreCase("N")
									&& UserList.get(i).getLan_direction()
											.equalsIgnoreCase("E")) {

								UserList.get(i).setLang(Sourceobj.get("lon") + "");
								UserList.get(i).setLat(Sourceobj.get("lat") + "");
								// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
								// ""+UserList.get(i).getLang()));

							} else if (UserList.get(i).getLat_direction()
									.equalsIgnoreCase("N")
									&& UserList.get(i).getLan_direction()
											.equalsIgnoreCase("W")) {

								UserList.get(i).setLang(
										"-" + Sourceobj.get("lon") + "");
								UserList.get(i).setLat(Sourceobj.get("lat") + "");
								// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
								// ""+UserList.get(i).getLang()));

							} else if (UserList.get(i).getLat_direction()
									.equalsIgnoreCase("S")
									&& UserList.get(i).getLan_direction()
											.equalsIgnoreCase("E")) {

								UserList.get(i).setLang(Sourceobj.get("lon") + "");
								UserList.get(i).setLat(
										"-" + Sourceobj.get("lat") + "");
								// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
								// ""+UserList.get(i).getLang()));

							} else if (UserList.get(i).getLat_direction()
									.equalsIgnoreCase("S")
									&& UserList.get(i).getLan_direction()
											.equalsIgnoreCase("W")) {

								UserList.get(i).setLang(
										"-" + Sourceobj.get("lon") + "");
								UserList.get(i).setLat(
										"-" + Sourceobj.get("lat") + "");
								// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
								// ""+UserList.get(i).getLang()));

							}

							UserList.get(i).setSpeed(dbObject.get("speed") + "");
							UserList.get(i).setTime(dbObject.get("timestamp") + "");

						}
					} else {
						UserList.get(i).setLang("0");
						UserList.get(i).setLat("0");
						UserList.get(i).setSpeed("0");
						UserList.get(i).setTime("0");
					}

				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return UserList;

	}

	

	public MessageObject InsertPatrolmanBeat(String device, String section,
			String km, String parentId, Connection con, DB mongoconnection) {


		ArrayList<PersonDetails> p = new ArrayList<PersonDetails>();
		String Photo = "";
		
		String[] kiloMeter_array = km.split(",");
		String[] device_array = device.split(",");
		String[] section_array = section.split(",");

		String Size = kiloMeter_array.length + "--"
				
				+ "--" + section_array.length;

		// System.err.println(Size);

		MessageObject msgo = new MessageObject();

		BasicDBList parentDbList = new BasicDBList();
		parentDbList.add(parentId);
	

		try {

			BasicDBList addres_list = new BasicDBList();

			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_RAILADDRESS);

			BasicDBObject Maindocument = new BasicDBObject();
			ObjectId objid = new ObjectId();
			Maindocument.put("address_id", "" + objid);
			Maindocument.put("filename", "Patrolman Beat ");
			Maindocument.put("ParentId", parentDbList);
			Maindocument.put("railway", "Jaipur");
			Maindocument.put("division", "");
			Maindocument.put("station_from", "");
			Maindocument.put("station_to", "");
			Maindocument.put("chainage", "");
			Maindocument.put("trolley", "");
			Maindocument.put("line", "");
			Maindocument.put("mode", "");

			for (int i = 0; i < kiloMeter_array.length; i++) {
				//System.err.println("-------------" + i + "-----"
					//	+ Double.parseDouble(kiloMeter_array[i].trim()));
				BasicDBObject addresDocument = new BasicDBObject();
				addresDocument.put("kilometer",(kiloMeter_array[i].trim()));
				addresDocument.put("distance", "0");
				boolean isfound=false;
		
				try {

					java.sql.CallableStatement ps = con
							.prepareCall("{call GetLatLanFromRailAddress(?,?,?)}");
					ps.setInt(1, Integer.parseInt(parentId));
					ps.setString(2, kiloMeter_array[i]);
					ps.setString(3, section_array[i]);


					ResultSet rs = ps.executeQuery();
					if (rs != null) {
						while (rs.next()) {
							isfound=true;
							addresDocument.put("latitude", rs.getDouble("Latitude"));
							addresDocument.put("longitude", rs.getDouble("Longitude"));
						}

					}
					
					
					

				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				// addresDocument.put("feature_code",feature_Code_array[i].trim());

				addresDocument.put("feature_detail","PatrolMan Start/End Beat "+kiloMeter_array[i]+" km");
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_121.png");
				addresDocument.put("feature_code", "121");

				addresDocument.put("section", section_array[i]);

				addresDocument.put("block_section", "");

				// addresDocument.put("remark",remark_array[i].trim());
				if (isfound) 
					addres_list.add(addresDocument);
				else {
					System.err.println("Lat Lan not found----"+kiloMeter_array[i]+"---"+section_array[i]);

				}

			}
			System.err.println("---------addres_list----" + addres_list.size());
			Maindocument.put("address_details", addres_list);

			table.insert(Maindocument);

			msgo.setError("false");
			msgo.setMessage("Address added successfully.---" + Size);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("address_details not added successfully.---" + Size);
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("address_details not added successfully.---" + Size);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("address_details not added successfully.---" + Size);
		}
		
		
return null;
	
	}
	
	
	public ArrayList<MessageObject> BackupNremoveWrongLoc(DB mongoconnection, ArrayList<WrongLocationDataDTO> locationData) {
		ArrayList<MessageObject>  msgo = new ArrayList<MessageObject>();
		try {
			for(WrongLocationDataDTO locDevInfo: locationData) {
				long imeiNo;
				MessageObject msg = new MessageObject();
				DBCollection locationTable = mongoconnection
						.getCollection("location");
				BasicDBObject device_whereQuery = new BasicDBObject();
				imeiNo= Long.parseLong(locDevInfo.getImeiNo());
				device_whereQuery.put("device", Long.parseLong(locDevInfo.getImeiNo()));
				BasicDBObject timestamp_whereQuery = new BasicDBObject(
						"timestamp", new BasicDBObject("$gte",Long.parseLong(locDevInfo.getStartTimestamp())).append("$lte",Long.parseLong(locDevInfo.getEndTimestamp())));
				BasicDBList dbList = new BasicDBList();
				dbList.add(device_whereQuery);
				dbList.add(timestamp_whereQuery);
				DBObject total_query = new BasicDBObject("$and", dbList);
				
				DBCursor cursor1 = locationTable.find(total_query)
						.sort(new BasicDBObject("timestamp", 1));
				cursor1.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
				
				//System.out.print("Cursor Count of u-p---"+cursor1.size()+"  "+total_query);
				long total = cursor1.size();
				if (cursor1.size() != 0) {
					//System.err.println("BackupNremoveWrongLoc*-----COunt---"+ cursor1.size());
					int i = 0;
					while (cursor1.hasNext()) {
						DBObject dbObject = (DBObject) cursor1.next();
						DBCollection del_loc_recTable = mongoconnection
								.getCollection("del_loc_rec");
						del_loc_recTable.insert(dbObject);
						i++;
					}
					WriteResult cursor2 = locationTable.remove(total_query);
					msg.setMessage(total+" Records inserted into del_loc_rec table and removed from location table for "+imeiNo);
					msg.setError("false");
					msgo.add(msg);
				}
				else {
					msg.setMessage("No data found...for "+imeiNo);
					msg.setError("false");
				}
			}
		}catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgo;
	}


	public MessageObject GenerateExceptionReportPatrolManBeatPathJabalpur(
			Connection con, DB mongoconnection, String parentId) {


		MessageObject msgo = new MessageObject();
		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionReportPatrolManBeatPathJabalpur ---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day-1 + "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

		// ///////////////////////////////********************************/////////////////////////////
		// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("KeyMan Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"PatrolMan Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						5));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);

		row.setHeight((short) 500);

		// ///////////////////////////////////All Section
		// End//////////////////////////////////////////////////////////

		for (int j = 0; j < mailSendInfo.size(); j++) {
			// for (int j = 20; j <=20;j++) {

			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report of keymen"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyle.setAlignment(HorizontalAlignment.CENTER);
			font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);

			CellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap
			wrap_style.setAlignment((short) 0);

			int petrolMancount = 0;
			

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for PatrolMan of section "
							+ mailSendInfo.get(j).getDept() + "  Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyle);
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 6));

			BasicDBList sectionDeviceList = new BasicDBList();

			for (int i = 0; i < deviceInfoList.size(); i++) {
				// for (int i = 18; i <=18; i++) {

//				System.err.println(" **Device =---================"
//						+ deviceInfoList.get(i).getName() + "\n"+mailSendInfo.get(j).getDept());

				Double startBatteryStatus = getKeyManStartBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+64800);///yearteday 6pm

				Double endBatteryStatus = getKeyManEndBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+115200);//today 8am

				if (deviceInfoList.get(i).getName().startsWith("P/")) {
					BasicDBObject subdocument = new BasicDBObject();

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfKeyman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));

								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

								dto.setSectionName(rsgertrip
										.getString("SectionName"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));
								RailwayKeymanTripsBeatsList.add(dto);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					// long DayStartWorkTime=1547836200;
					// System.out.println("DayStartWorkTime----" +
					// DayStartWorkTime);

					Double distanceTolerance = 1.3;
					// Double startLatOfRouteKm = 0.0, startLangOfRouteKm = 0.0,
					// endLatOfRouteKm = 0.0, endLangOfRouteKm = 0.0;

					// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
					// RailwayPetrolmanTripsBeatsList) {
					for (int r = 0; r < RailwayKeymanTripsBeatsList.size(); r++) {
						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayKeymanTripsBeatsList
								.get(r);

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.parseLong("355488020181042"));

						/*
						 * BasicDBObject timestamp_whereQuery = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime).append("$lt",
						 * DayStartWorkTime+86400));
						 */
						

						BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 64800).append("$lt",
										DayStartWorkTime + 115200));
					/*	BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 41400).append("$lt",
										DayStartWorkTime + 55800));
*/
						/*
						 * BasicDBObject timestamp_whereQuery_morning = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+23400).append("$lt",
						 * DayStartWorkTime+37800)); BasicDBObject
						 * timestamp_whereQuery_afternoon = new BasicDBObject(
						 * "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+41400).append("$lt",
						 * DayStartWorkTime+55800));
						 */
						BasicDBList morn_Milage = new BasicDBList();
						morn_Milage.add(timestamp_whereQuery_morning);

						morn_Milage.add(device_whereQuery);
						DBObject morn_Milage_query = new BasicDBObject("$and",
								morn_Milage);

						//BasicDBList after_Milage = new BasicDBList();
						//after_Milage.add(timestamp_whereQuery_afternoon);
						//after_Milage.add(device_whereQuery);
						//DBObject after_Milage_query = new BasicDBObject("$and",
								//after_Milage);

						BasicDBList Milage = new BasicDBList();
						Milage.add(morn_Milage_query);
						//Milage.add(after_Milage_query);
						DBObject final_query = new BasicDBObject("$or", Milage);

						DBCursor cursor = table.find(final_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + final_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							System.err.println("*--listObjects---COunt---"
									+ cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();

						}

						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());
							;

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = locationNearbyKM(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"P");
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation = locationNearbyKM(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"P");
								if (tripEndLocation.getEndKmBeatActual() <= 0) {
									tripStartLocation.setStartkmBeatActual(0);

								}

								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								System.out
								.println(" listObjects.get(0).get(timestamp)---222--"+listObjects.get(0).get("timestamp"));
								exDto.setDeviceStartTime( listObjects.get(0).get("timestamp")+"");
								exDto.setDeviceEndTime(listObjects.get(listObjects.size()-1).get("timestamp")+"");

								exceptiionalTrip.add(exDto);
							} else {

								exceptiionalTrip.add(null);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setLocationSize(0);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exceptiionalTrip.add(exDto);
						}

					}
					System.out.println("\n\n");
					System.out
							.println("  Here--*************exceptiionalTrip****************************--------------------"
									+ exceptiionalTrip.size());
					System.out.println("\n\n");
					// //////////////////////////////////////////////

					// //////////////////////////////////
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null) {
						StringBuilder remark = new StringBuilder();
						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								deviceInfoList.get(i).getName() + "   ("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")");
						subdocument.put("device", deviceInfoList.get(i)
								.getDeviceID());
						subdocument.put("device_name", deviceInfoList.get(i)
								.getName());

						row.getCell(0).setCellStyle(HeadingStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 12));
						// start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((startBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("start_battery_status",
								df2.format(((startBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 12));

						// End Battery Staus
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("End Battery status :- "
								+ df2.format(((endBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("end_battery_status",
								df2.format(((endBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 12));

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");

						remark_cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 12));

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						cell = row.createCell(0);
						cell.setCellValue("Trip#");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(1);
						cell.setCellValue("Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue("Expected Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue("End Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(4);
						cell.setCellValue("Expected end Beat");
						cell.setCellStyle(tripColumnStyle);

						/*
						 * cell = row.createCell(5);
						 * cell.setCellValue("Avg speed");
						 * cell.setCellStyle(tripColumnStyle);
						 */

						cell = row.createCell(5);
						cell.setCellValue("Max speed");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(6);
						cell.setCellValue("Distance cover");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						cell = row.createCell(7);
						cell.setCellValue("Expected StartTime");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						cell = row.createCell(8);
						cell.setCellValue("Actual StartTime");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						cell = row.createCell(9);
						cell.setCellValue("Expected End Time");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);


						cell = row.createCell(10);
						cell.setCellValue("Actual End Time");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);
						
						cell = row.createCell(11);
						cell.setCellValue("Actual Trip Cover");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						
						   cell = row.createCell(12);
                         cell.setCellValue("Expected Trip Cover");
                     	cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);
						
						for (ExceptionReortsTrip exceptionReortsTrip : exceptiionalTrip) {
							System.out
									.println(" **exceptiionalTrip=---================"
											+ new Gson()
													.toJson(exceptionReortsTrip));

							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null
									&& exceptionReortsTrip.getLocationSize() > 0) {

								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = 0;
									double expactedKmCover = 0;
									if (exceptionReortsTrip.getTripStart()
											.getStartkmBeatActual() != 0
											&& exceptionReortsTrip.getTripEnd()
													.getEndKmBeatActual() != 0
											&& exceptionReortsTrip
													.getTripStart()
													.getMinDistance() < 5
											&& exceptionReortsTrip.getTripEnd()
													.getMinDistance() < 5) {
										kmcover = (exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatActual() - exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatActual());
										if (kmcover < 0)
											kmcover = -1 * kmcover;
										expactedKmCover = exceptionReortsTrip
												.getTripStart()
												.getEndKmBeatExpected()
												- exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected();
										if (expactedKmCover < 0)
											expactedKmCover = -1
													* expactedKmCover;

										if (kmcover < expactedKmCover
												- distanceTolerance) {
											remark.append("\tTotal beats not completed in Trip "
													+ exceptionReortsTrip
															.getTripNo() + ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark_cell
													.setCellValue("Remark status :- All work done succesfully.");
											KeyManCoverSucefullyDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
										}

									} else {
										kmcover = 0;

										if (exceptionReortsTrip.getTripStart()
												.getTotal_distance() < 1) {
											remark.append("\tDevice is not moved "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else if (exceptionReortsTrip
												.getTripStart()
												.getMinDistance() > 5
												&& exceptionReortsTrip
														.getTripEnd()
														.getMinDistance() > 5) {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
										}

									}

									System.out
											.println(" **expactedKmCover******---"
													+ expactedKmCover
													+ "---kmcover-" + kmcover);

									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());
										subdocument
												.put("Tripno",
														exceptionReortsTrip
																.getTripNo());

										cell.setCellStyle(wrap_style);

										cell = row.createCell(1);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));
										subdocument
												.put("start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatActual()));
										cell.setCellStyle(wrap_style);

										cell = row.createCell(2);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatExpected());
										subdocument
												.put("expected_start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatExpected()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(3);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));
										subdocument.put("end_beat", df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(4);
										cell.setCellValue(exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatExpected());
										subdocument
												.put("expected_end_beat",
														df2.format(exceptionReortsTrip
																.getTripEnd()
																.getEndKmBeatExpected()));

										cell.setCellStyle(wrap_style);

										/*
										 * cell = row.createCell(5);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 * cell.setCellStyle(wrap_style);
										 */

										cell = row.createCell(5);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart().getMaxSpeed());
										subdocument.put("max_speed", df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getMaxSpeed()));

										if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8) {
											KeyManOverspeedDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
										}

										cell.setCellStyle(wrap_style);

										if (exceptionReortsTrip.getDeviceStartTime()!=null) {
											
											
											cell = row.createCell(6);
											cell.setCellValue(df2.format(kmcover));
											subdocument.put("kmcover",
													df2.format(kmcover));
											cell.setCellStyle(wrap_style);

											cell = row.createCell(7);
											cell.setCellValue(Common.getDateCurrentTimeZone(DayStartWorkTime+25200));
											cell.setCellStyle(wrap_style);
											
										

											cell = row.createCell(8);
											cell.setCellValue(Common.getDateCurrentTimeZone(Long.parseLong(exceptionReortsTrip
													.getDeviceStartTime())));

											cell.setCellStyle(wrap_style);
											cell = row.createCell(9);
											cell.setCellValue(Common.getDateCurrentTimeZone(DayStartWorkTime+57600));

											cell.setCellStyle(wrap_style);
											cell = row.createCell(10);
											cell.setCellValue(Common.getDateCurrentTimeZone(Long.parseLong(exceptionReortsTrip
													.getDeviceEndTime())));
											cell.setCellStyle(wrap_style);
											
											if (remark_cell.getStringCellValue().toString().contains("All work done succesfully")) {
												cell = row.createCell(11);
												cell.setCellValue("1");

												cell.setCellStyle(wrap_style);
											 }else{
                                                cell = row.createCell(11);
                                                cell.setCellValue("0");

                                                cell.setCellStyle(wrap_style);
                                            }
										
											   cell = row.createCell(12);
	                                            cell.setCellValue("1");
	                                            cell.setCellStyle(wrap_style);
	                                      
											
										}
									
										// System.out.println("  Here--*****************************************--------------------");
										//

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// Exception of location not found
								if (exceptionReortsTrip != null
										&& exceptionReortsTrip
												.getLocationSize() == 0)
									try {
										// System.out.println(" **exceptionReortsTrip*******row******************"
										// +
										// "***-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size()+"-----"+exceptionReortsTrip.getLocationSize());

										KeyManOffDevice_Set.add(deviceInfoList
												.get(i).getName());

										remark.append("Device is Off");

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(1);
										cell.setCellStyle(loactionNotFoundColumnStyle);

										cell = row.createCell(1);
										cell.setCellValue("Device is Off");
										cell.setCellStyle(loactionNotFoundColumnStyle);
										sheet.addMergedRegion(new CellRangeAddress(
												sheet.getLastRowNum(), sheet
														.getLastRowNum(), 1, 3));

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								else {

								}

							}
						}

						if (remark.toString().length() > 0) {
							remark_cell.setCellValue("Remark status :- "
									+ remark.toString());
							subdocument.put("remark", remark.toString());
							remark_cell.setCellStyle(error_remarkColumnStyle);
						}

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayKeymanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null) {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(
										deviceInfoList.get(i).getName()
												+ "   ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ")");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 12));
								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 12));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 12));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :- RDPS data not found.");
								subdocument.put("remark",
										"RDPS data not found.");
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								// cell.setCellStyle(error_remarkColumnStyle);

								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(

										deviceInfoList.get(i).getName()
												+ "  ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ") ");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 12));

								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 12));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 12));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :-Trips not Shedule for this device.");
								subdocument.put("remark",
										"Trips not Shedule for this device.");
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 12));

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());
					if (startBatteryStatus < 3
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName()))
						KeyManLowBattery_Set.add(deviceInfoList.get(i)
								.getName());

					sectionDeviceList.add(subdocument);

				} else {
					// System.out.println("patrolman Man found--------------------");
					petrolMancount++;
				}

			}

			Maindocument.put("exceptional_device", sectionDeviceList);

			report_table.insert(Maindocument);

			sheet.setColumnWidth(0, 1300);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 2000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);

			if (deviceInfoList.size() > 0
					&& petrolMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept().trim().replace("PWI/", "") + "_"
						+ "Exception_Trip_Report_PatrolMan"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ "_" + parentId + ".xlsx";
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					String sendemail = "";
					for (String emaString : mailSendInfo.get(j).getEmailIds()) {
						sendemail = sendemail + "," + emaString.trim();
					}
					SendEmail mail = new SendEmail();

					
			mail.sendDeviceExceptionMailToJaipur(sendemail, file,
					 mailSendInfo
					  .get(j).getDept(),"Exception Trip Report of PatrolMan"
					  ,"Exception Trip Report of keymen",false
						,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					 
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// /////ALl Section////////////////////////

			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReport);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			cell = row.createCell(1);
			cell.setCellValue(KeyManLowBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight
					.add((short) ((KeyManLowBattery_Set.toString()
							.replace("[", "").replace("]", "").toString()
							.length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(KeyManOffDevice_Set.toString().replace("[", "")
					.replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOffDevice_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			short height_row = (short) ((KeyManLowBattery_Set.toString()
					.replace("[", "").replace("]", "").toString().length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);
			System.err.println("Row max height-----" + height_row);

			sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

			if (j == mailSendInfo.size() - 1) {
				String outFileNameAllSectionStatus = "All Section Patrolman status Report_"
						+ parentId
						+ "_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ ".xlsx";
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReport.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReport.close();

					 String sendemail = getEmailIdForAllsectionReport(con,parentId);
							SendEmail mail = new SendEmail();
							System.err.println("Send mail All Section----" + sendemail);
								mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
										  "All Section"," PatrolMan Work Status Report AllSection",
										  " PatrolMan Work Status Report AllSection",true
											,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

				
					
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// /////////////////////*Alll Section////////////////////
			
			
			///Insert Datewise Saction Data
			 
			 
			 try {

					// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
					java.sql.CallableStatement ps = con
							.prepareCall("{call SaveDatewiseExceptionReport(?,?,?,?,?,?,?,?)}");

					
					ps.setInt(1, Integer.parseInt(parentId));
					ps.setString(2,DayStartWorkTime+"");
					ps.setString(3, KeyManLowBattery_Set.toString().replace("[", "")
							.replace("]", ""));

					ps.setString(4,KeyManOffDevice_Set.toString()
							.replace("[", "").replace("]", ""));

					ps.setString(5, KeyManExceptionalDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(6, KeyManOverspeedDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(7,mailSendInfo.get(j).getDept() );
					ps.setString(8, KeyManCoverSucefullyDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					int result = ps.executeUpdate();
					if (result == 0) {
						msgo.setError("true");
						msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
					} else {
						// //System.err.println("Error=="+result);
						msgo.setError("false");
						msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
				}

		}// //////////for section end

		return msgo;

	}


	public MessageObject addRanchiTrainAddressFromCSV(DB mongoconnection,
			String filename, String railWay, String division,
			String station_From, String station_TO, String chainage,
			String trolley, String line, String mode,
			ArrayList<String> kiloMeter, ArrayList<String> distance,
			ArrayList<String> latitude, ArrayList<String> longitude,
			ArrayList<String> feature_Code, ArrayList<String> feature_Detail,
			String parentId, ArrayList<String> section_Detail) {
		MessageObject msgo = new MessageObject();
		
		
		
		
		

		BasicDBList parentDbList = new BasicDBList();

		parentDbList.add(Integer.parseInt(parentId));

		try {

			BasicDBList addres_list = new BasicDBList();

			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_RAILADDRESS);

			BasicDBObject Maindocument = new BasicDBObject();
			ObjectId objid = new ObjectId();
			Maindocument.put("address_id", "" + objid);
			
			Maindocument.put("filename", filename.trim().replace(",", ""));
			Maindocument.put("ParentId", parentDbList);
			Maindocument.put("railway", railWay.trim().replace(",", ""));
			Maindocument.put("division", division.trim().replace(",", ""));
			Maindocument.put("station_from",
					station_From.trim().replace(",", ""));
			Maindocument.put("station_to", station_TO.trim().replace(",", ""));
			Maindocument.put("chainage", chainage.trim().replace(",", ""));
			Maindocument.put("trolley", trolley.trim().replace(",", ""));
			Maindocument.put("timestamp", System.currentTimeMillis() / 1000);
			Maindocument.put("line", line.trim().replace(",", ""));
			Maindocument.put("mode", mode.trim().replace(",", ""));
			
			  station_From=station_From.substring(station_From.indexOf("[") +
			  1); 
			  station_From=station_From.substring(0,
			  station_From.indexOf("]"));
			  
			 station_TO=station_TO.substring(station_TO.indexOf("[") + 1);
			  station_TO=station_TO.substring(0, station_TO.indexOf("]"));
			 
			for (int i = 0; i < kiloMeter.size(); i++) {
			//	System.err.println("-------------"
					//	+ i
					//	+ "-----"
						//+ Double.parseDouble(distance.get(i).trim()
						//		.replace(",", "")));
				BasicDBObject addresDocument = new BasicDBObject();
				addresDocument.put("kilometer", kiloMeter.get(i).trim()
						.replace(",", ""));
				addresDocument.put("distance",
						distance.get(i).trim().replace(",", ""));
				addresDocument.put("latitude",
						latitude.get(i).trim().replace(",", ""));
				addresDocument.put("longitude", longitude.get(i).trim()
						.replace(",", ""));
				// addresDocument.put("feature_code",feature_Code.get(i).trim().replace(",",""));


				  //System.err.println("-------feature_Code------"+feature_Code.get(i)+"--"+feature_Detail.get(i));

				addresDocument.put("feature_detail", feature_Detail.get(i)
						.trim().replace(",", "").replace("$", ","));
				
				if (feature_Code.get(i).trim().equals("23")||
						feature_Detail.get(i).trim().toLowerCase().contains("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           ")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_12.png");
					addresDocument.put("feature_code", "12");

				} else if (feature_Code.get(i).trim().equals("4")||feature_Detail.get(i).trim().toLowerCase().contains("Level")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc__4.png");
					addresDocument.put("feature_code", "4");

				} else if (feature_Code.get(i).trim().equals("16")||feature_Code.get(i).trim().equals("15")||
						feature_Detail.get(i).trim().contains("Curve")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_102.png");
					addresDocument.put("feature_code", "102");

				} 
				 else if (feature_Code.get(i).trim().equals("2")||feature_Detail.get(i).trim().replace(",", "")
						 .toString().toLowerCase().contains("ohe")) {
					
							addresDocument.put("feature_image",
									"~/Images/FeatureCodePhoto/fc_2.png");
							addresDocument.put("feature_code", "2");

					
				} 
				 else if (feature_Code.get(i).trim().equals("26")||feature_Detail.get(i).trim().replace(",", "")
							.toLowerCase().contains("axle")) {
					 addresDocument
						.put("feature_image",
								"~/Images/FeatureCodePhoto/ic_default_featurecode.png");
						addresDocument.put("feature_code", "26");

				} else if (feature_Code.get(i).trim().equals("1")||feature_Detail.get(i).trim().
						replace(",", "")
						.toLowerCase().contains("km post")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_150.png");
					addresDocument.put("feature_code", "150");
					
			} else if (feature_Code.get(i).trim().equals("9")||feature_Detail.get(i).trim().replace(",", "")
					.toLowerCase().contains("bridge minor")) {
				addresDocument.put("feature_image",
						"~/Images/FeatureCodePhoto/fc_113.png");
				addresDocument.put("feature_code", "113");

			}else if (feature_Code.get(i).trim().equals("5")||feature_Detail.get(i).trim().replace(",", "")
					.toLowerCase().contains("sej")) {
				addresDocument.put("feature_image",
						"~/Images/FeatureCodePhoto/ic_default_featurecode.png");
				addresDocument.put("feature_code", "5");

			}else if (feature_Code.get(i).trim().equals("12")||feature_Detail.get(i).trim().replace(",", "")
					.toLowerCase().contains("fob")) {
				addresDocument
				.put("feature_image",
						"~/Images/FeatureCodePhoto/ic_default_featurecode.png");
		addresDocument.put("feature_code", "110");


			}else if (feature_Code.get(i).trim().equals("24")||
					feature_Detail.get(i).trim().toLowerCase().contains("signal")) {
				//.contains("MISCELLANEOUS")) 
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_105.png");
					addresDocument.put("feature_code", "105");
			}
			else if (feature_Code.get(i).trim().equals("10")||(feature_Code.get(i).trim().equals("7"))||
					(feature_Code.get(i).trim().equals("8"))||feature_Detail.get(i).trim().replace(",", "")
					.toLowerCase().contains("bridge")) {
				addresDocument.put("feature_image",
						"~/Images/FeatureCodePhoto/fc_42.png");
				addresDocument.put("feature_code", "42");

			}
				
			else if (feature_Detail.get(i).trim().replace(",", "").toLowerCase()
						.contains("rub")||feature_Detail.get(i).trim().replace(",", "").toLowerCase()
						.contains("lhs")
						|| feature_Code.get(i).trim().replace(",", "")
								.equals("107")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_107.png");
					addresDocument.put("feature_code", "107");
				}
				
				 else if (feature_Detail.get(i).trim().replace(",", "")
						.toLowerCase().contains("gang")
						|| feature_Code.get(i).trim().replace(",", "")
								.equals("101")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_101.png");
					addresDocument.put("feature_code", "101");

				} 
				else if (feature_Detail.get(i).trim().toLowerCase().contains("gang hd")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_103.png");
					addresDocument.put("feature_code", "103");

				} else if (feature_Detail.get(i).trim().toLowerCase().contains("switch")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_104.png");
					addresDocument.put("feature_code", "104");

				} else if (feature_Detail.get(i).trim()
						.contains("MISCELLANEOUS")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_105.png");
					addresDocument.put("feature_code", "105");

				} else if (feature_Detail.get(i).trim().toLowerCase().contains("siding/loop")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_106.png");
					addresDocument.put("feature_code", "106");

				} else if (feature_Detail.get(i).trim().toLowerCase().contains("section")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_108.png");
					addresDocument.put("feature_code", "108");

				} else if (feature_Detail.get(i).trim().toLowerCase().contains("cabin")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_109.png");
					addresDocument.put("feature_code", "109");

				}  else if (feature_Detail.get(i).trim().toLowerCase().contains("joint")
						|| feature_Code.get(i).trim().equals("112")) {
					addresDocument.put("feature_image",
							"~/Images/FeatureCodePhoto/fc_112.png");
					addresDocument.put("feature_code", "112");

				} else {
					addresDocument.put("feature_code", "999");

					addresDocument
							.put("feature_image",
									"~/Images/FeatureCodePhoto/ic_default_featurecode.png");
				}

				//addresDocument.put("section","CNI-MURI");
				if (section_Detail.size()>0&&section_Detail.get(i).trim().length()>0) {
					addresDocument.put("section",
							section_Detail.get(i).trim());
				}else{
					//addresDocument.put("section","ANA-DMO");
					//addresDocument.put("section",	station_From.replace("SECTION", "").replace(",", "")
						//			.replace("(Ex)", "").replace(" ", ""));
					addresDocument.put("section",station_From+"-"+station_TO);
				}
				
				addresDocument.put("block_section", station_From+"-"+station_TO);
				// addresDocument.put("remark",remark_array.get(i).trim());
				addres_list.add(addresDocument);

			}
			// System.err.println("---------addres_list----" +
			// addres_list.size());
			Maindocument.put("address_details", addres_list);

			table.insert(Maindocument);

			msgo.setError("false");
			msgo.setMessage("Address added successfully.---"
					+ addres_list.size());

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("address_details not added successfully.---");
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("address_details not added successfully.---");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msgo.setError("true");
			msgo.setMessage("address_details not added successfully.---");
		}
		return msgo;
	}

	public MessageObject setSectionNameToRailwaykeymanBeat(DB mongoconnection,
			Connection con, String parentId) {



		MessageObject msgo = new MessageObject();
		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);
		//System.err.println("GenerateExceptionReportKeyManBeatPathJabalpur ---"
			//	+ new Gson().toJson(mailSendInfo));

		for (int j = 0; j < mailSendInfo.size(); j++) {

			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();


			BasicDBList sectionDeviceList = new BasicDBList();

			for (int i = 0; i < deviceInfoList.size(); i++) {
				// for (int i = 119; i <=119; i++) {


				DBCollection table = mongoconnection
						.getCollection(Common.TABLE_LOCATION);
				BasicDBObject device_whereQuery = new BasicDBObject();
				device_whereQuery.put("device",
						Long.parseLong(deviceInfoList.get(i).getDeviceID()));
				// device_whereQuery.put("device",Long.parseLong("355488020181042"));

				
				BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
						"timestamp", new BasicDBObject("$gte",
								1571468408).append("$lt",1571481008
								));
			
				
				BasicDBList morn_Milage = new BasicDBList();
				morn_Milage.add(timestamp_whereQuery_morning);
				morn_Milage.add(device_whereQuery);
				DBObject morn_Milage_query = new BasicDBObject("$and",
						morn_Milage);
				
				
				DBCursor cursor = table.find(morn_Milage_query);
				cursor.sort(new BasicDBObject("_id", -1)).limit(1);
				
				

				cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

				// System.out.println("Cursor Count of u-p11----"+cursor.size()+"  "+device_whereQuery);
				long total = cursor.size();
				Double MaxSpeed = 0.0;
				Double TotalSpeed = 0.0;
				int Triplocationcount = 0;

				if (cursor.size() > 0) {


					DBObject dbObject1 = (DBObject) cursor.next();

					try {

						// ////System.err.UpdateDevicesIMEIAndSimNoprint(emailid);
						int result = 0;
						BasicDBObject locobj = (BasicDBObject) dbObject1.get("location");
						double lat1  = locobj.getDouble("lat");
						double lon1 = locobj.getDouble("lon");

						System.err.println("UpdateSectionNameInRailwayKeyManBeat=="+lat1+"--"+lon1
							);
						java.sql.CallableStatement ps = con
								.prepareCall("{call GetandUpadteSectionNameFromLocation(?,?,?,?)}");
						ps.setDouble(1, lat1);
						ps.setDouble(2, lon1);
						ps.setInt(3, deviceInfoList.get(i).getStudentId());
						ps.setInt(4, Integer.parseInt(parentId));

						

						result = ps.executeUpdate();

						if (result == 0) {
							msgo.setError("true");
							msgo.setMessage("section is not Update successfully");
							System.out.println("section is Update--not -"+i +"-------"+deviceInfoList.get(i).getName());

						} else {
							System.err.println("section is Update---"+i +"-------"+deviceInfoList.get(i).getName());
							msgo.setError("false");
							msgo.setMessage("section is Update successfully");
						}

						/*
						 * psx.setString(1, name_array[i].trim()); psx.setInt(2,534981);
						 * psx.setString(3, idno_array[i].trim());
						 */

					} catch (Exception e) {
						msgo.setError("true");
						msgo.setMessage("Error :" + e.getMessage());
						e.printStackTrace();
					}
					
					}else{
						System.out.println("Location not found---"+i +"-------");

					}
				}
			}
			
		return msgo;

	
	}

	
	public MessageObject GenerateExceptionReportKeyManBeatPathRanchi(
			Connection con, DB mongoconnection, String parentId) {


		MessageObject msgo = new MessageObject();
		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionReportKeyManBeatPathRanchi ---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day + "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

		// ///////////////////////////////********************************/////////////////////////////
		// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("KeyMan Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"KeyMan Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						5));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);

		row.setHeight((short) 500);

		// ///////////////////////////////////All Section
		// End//////////////////////////////////////////////////////////

		for (int j = 0; j < mailSendInfo.size(); j++) {
			// for (int j = 0; j <=0;j++) {

			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report of keymen"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyle.setAlignment(HorizontalAlignment.CENTER);
			font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);

			CellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap
			wrap_style.setAlignment((short) 0);

			int petrolMancount = 0;
			

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for Keyman of section "
							+ mailSendInfo.get(j).getDept() + "  Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyle);
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 6));

			BasicDBList sectionDeviceList = new BasicDBList();

			if (deviceInfoList.size()>0) {
				
			for (int i = 0; i < deviceInfoList.size(); i++) {
				// for (int i = 119; i <=119; i++) {

//				System.err.println(" **Device =---================"
//						+ deviceInfoList.get(i).getName() + "\n"+mailSendInfo.get(j).getDept()+"---"+deviceInfoList.get(i)
//						.getStudentId());

				Double startBatteryStatus = getKeyManStartBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+18000);

				Double endBatteryStatus = getKeyManEndBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+18000);

				if (deviceInfoList.get(i).getName().startsWith("K/")) {
					BasicDBObject subdocument = new BasicDBObject();

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfKeyman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));

								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

								dto.setSectionName(rsgertrip
										.getString("SectionName"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));
								RailwayKeymanTripsBeatsList.add(dto);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					// long DayStartWorkTime=1547836200;
					// System.out.println("DayStartWorkTime----" +
					// DayStartWorkTime);

					Double distanceTolerance = 1.3;
					// Double startLatOfRouteKm = 0.0, startLangOfRouteKm = 0.0,
					// endLatOfRouteKm = 0.0, endLangOfRouteKm = 0.0;

					// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
					// RailwayPetrolmanTripsBeatsList) {
					for (int r = 0; r < RailwayKeymanTripsBeatsList.size(); r++) {
						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayKeymanTripsBeatsList
								.get(r);

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.eparseLong("355488020181042"));

						/*
						 * BasicDBObject timestamp_whereQuery = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime).append("$lt",
						 * DayStartWorkTime+86400));
						 */

						BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 23400).append("$lt",
										DayStartWorkTime + 37800));
						BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 41400).append("$lt",
										DayStartWorkTime + 55800));

						/*
						 * BasicDBObject timestamp_whereQuery_morning = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+23400).append("$lt",
						 * DayStartWorkTime+37800)); BasicDBObject
						 * timestamp_whereQuery_afternoon = new BasicDBObject(
						 * "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+41400).append("$lt",
						 * DayStartWorkTime+55800));
						 */
						BasicDBList morn_Milage = new BasicDBList();
						morn_Milage.add(timestamp_whereQuery_morning);

						morn_Milage.add(device_whereQuery);
						DBObject morn_Milage_query = new BasicDBObject("$and",
								morn_Milage);

						BasicDBList after_Milage = new BasicDBList();
						after_Milage.add(timestamp_whereQuery_afternoon);
						after_Milage.add(device_whereQuery);
						DBObject after_Milage_query = new BasicDBObject("$and",
								after_Milage);

						BasicDBList Milage = new BasicDBList();
						Milage.add(morn_Milage_query);
						Milage.add(after_Milage_query);
						DBObject final_query = new BasicDBObject("$or", Milage);

						DBCursor cursor = table.find(final_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + final_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							System.err.println("*--listObjects---COunt---"
									+ cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();

						}

						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());
							;

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = locationNearbyKM(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"K");
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation = locationNearbyKM(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"K");
								if (tripEndLocation.getEndKmBeatActual() <= 0) {
									tripStartLocation.setStartkmBeatActual(0);

								}

								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								System.out
								.println(" listObjects.get(0).get(timestamp)--33---"+listObjects.get(0).get("timestamp"));
								exDto.setDeviceStartTime( listObjects.get(0).get("timestamp")+"");
								exDto.setDeviceEndTime(listObjects.get(listObjects.size()-1).get("timestamp")+"");

								exceptiionalTrip.add(exDto);
							} else {

								exceptiionalTrip.add(null);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setLocationSize(0);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exceptiionalTrip.add(exDto);
						}

					}
					System.out.println("\n\n");
					System.out
							.println("  Here--*************exceptiionalTrip****************************--------------------"
									+ exceptiionalTrip.size());
					System.out.println("\n\n");
					// //////////////////////////////////////////////

					// //////////////////////////////////
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null) {
						StringBuilder remark = new StringBuilder();
						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								deviceInfoList.get(i).getName() + "   ("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")");
						subdocument.put("device", deviceInfoList.get(i)
								.getDeviceID());
						subdocument.put("device_name", deviceInfoList.get(i)
								.getName());

						row.getCell(0).setCellStyle(HeadingStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));
						// start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((startBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("start_battery_status",
								df2.format(((startBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));

						// End Battery Staus
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("End Battery status :- "
								+ df2.format(((endBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("end_battery_status",
								df2.format(((endBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");

						remark_cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						cell = row.createCell(0);
						cell.setCellValue("Trip#");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(1);
						cell.setCellValue("Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue("Expected Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue("End Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(4);
						cell.setCellValue("Expected end Beat");
						cell.setCellStyle(tripColumnStyle);

						/*
						 * cell = row.createCell(5);
						 * cell.setCellValue("Avg speed");
						 * cell.setCellStyle(tripColumnStyle);
						 */

						cell = row.createCell(5);
						cell.setCellValue("Max speed");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(6);
						cell.setCellValue("Distance cover");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						/*cell = row.createCell(7);
						cell.setCellValue("Expected StartTime");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						cell = row.createCell(8);
						cell.setCellValue("Actual StartTime");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						cell = row.createCell(9);
						cell.setCellValue("Expected End Time");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);


						cell = row.createCell(10);
						cell.setCellValue("Actual End Time");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);
						
						cell = row.createCell(11);
						cell.setCellValue("Actual Trip Cover");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						
						   cell = row.createCell(12);
                         cell.setCellValue("Expected Trip Cover");
                     	cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);*/
						
						for (ExceptionReortsTrip exceptionReortsTrip : exceptiionalTrip) {
							System.out
									.println(" **exceptiionalTrip=---================"
											+ new Gson()
													.toJson(exceptionReortsTrip));

							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null
									&& exceptionReortsTrip.getLocationSize() > 0) {

								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = 0;
									double expactedKmCover = 0;
									if (exceptionReortsTrip.getTripStart()
											.getStartkmBeatActual() != 0
											&& exceptionReortsTrip.getTripEnd()
													.getEndKmBeatActual() != 0
											&& exceptionReortsTrip
													.getTripStart()
													.getMinDistance() < 5
											&& exceptionReortsTrip.getTripEnd()
													.getMinDistance() < 5) {
										kmcover = (exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatActual() - exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatActual());
										if (kmcover < 0)
											kmcover = -1 * kmcover;
										expactedKmCover = exceptionReortsTrip
												.getTripStart()
												.getEndKmBeatExpected()
												- exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected();
										if (expactedKmCover < 0)
											expactedKmCover = -1
													* expactedKmCover;

										if (kmcover < expactedKmCover
												- distanceTolerance) {
											remark.append("\tTotal beats not completed in Trip "
													+ exceptionReortsTrip
															.getTripNo() + ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark_cell
													.setCellValue("Remark status :- All work done succesfully.");
											KeyManCoverSucefullyDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
										}

									} else {
										kmcover = 0;

										if (exceptionReortsTrip.getTripStart()
												.getTotal_distance() < 1) {
											remark.append("\tDevice is not moved "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else if (exceptionReortsTrip
												.getTripStart()
												.getMinDistance() > 5
												&& exceptionReortsTrip
														.getTripEnd()
														.getMinDistance() > 5) {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
										}

									}

									System.out
											.println(" **expactedKmCover******---"
													+ expactedKmCover
													+ "---kmcover-" + kmcover);

									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());
										subdocument
												.put("Tripno",
														exceptionReortsTrip
																.getTripNo());

										cell.setCellStyle(wrap_style);

										cell = row.createCell(1);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));
										subdocument
												.put("start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatActual()));
										cell.setCellStyle(wrap_style);

										cell = row.createCell(2);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatExpected());
										subdocument
												.put("expected_start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatExpected()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(3);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));
										subdocument.put("end_beat", df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(4);
										cell.setCellValue(exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatExpected());
										subdocument
												.put("expected_end_beat",
														df2.format(exceptionReortsTrip
																.getTripEnd()
																.getEndKmBeatExpected()));

										cell.setCellStyle(wrap_style);

										/*
										 * cell = row.createCell(5);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 * cell.setCellStyle(wrap_style);
										 */

										cell = row.createCell(5);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart().getMaxSpeed());
										subdocument.put("max_speed", df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getMaxSpeed()));

										if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8) {
											KeyManOverspeedDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
										}

										cell.setCellStyle(wrap_style);

										if (exceptionReortsTrip.getDeviceStartTime()!=null) {
											
											
											cell = row.createCell(6);
											cell.setCellValue(df2.format(kmcover));
											subdocument.put("kmcover",
													df2.format(kmcover));
											cell.setCellStyle(wrap_style);

											/*cell = row.createCell(7);
											cell.setCellValue(Common.getDateCurrentTimeZone(DayStartWorkTime+25200));
											cell.setCellStyle(wrap_style);
											
										

											cell = row.createCell(8);
											cell.setCellValue(Common.getDateCurrentTimeZone(Long.parseLong(exceptionReortsTrip
													.getDeviceStartTime())));

											cell.setCellStyle(wrap_style);
											cell = row.createCell(9);
											cell.setCellValue(Common.getDateCurrentTimeZone(DayStartWorkTime+57600));

											cell.setCellStyle(wrap_style);
											cell = row.createCell(10);
											cell.setCellValue(Common.getDateCurrentTimeZone(Long.parseLong(exceptionReortsTrip
													.getDeviceEndTime())));
											cell.setCellStyle(wrap_style);
											
											if (remark_cell.getStringCellValue().toString().contains("All work done succesfully")) {
												cell = row.createCell(11);
												cell.setCellValue("1");

												cell.setCellStyle(wrap_style);
											 }else{
                                                cell = row.createCell(11);
                                                cell.setCellValue("0");

                                                cell.setCellStyle(wrap_style);
                                            }
										
											   cell = row.createCell(12);
	                                            cell.setCellValue("1");
	                                            cell.setCellStyle(wrap_style);
	                                      */
											
										}
									
										// System.out.println("  Here--*****************************************--------------------");
										//

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// Exception of location not found
								if (exceptionReortsTrip != null
										&& exceptionReortsTrip
												.getLocationSize() == 0)
									try {
										// System.out.println(" **exceptionReortsTrip*******row******************"
										// +
										// "***-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size()+"-----"+exceptionReortsTrip.getLocationSize());

										KeyManOffDevice_Set.add(deviceInfoList
												.get(i).getName());

										remark.append("Device is Off");

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(1);
										cell.setCellStyle(loactionNotFoundColumnStyle);

										cell = row.createCell(1);
										cell.setCellValue("Device is Off");
										cell.setCellStyle(loactionNotFoundColumnStyle);
										sheet.addMergedRegion(new CellRangeAddress(
												sheet.getLastRowNum(), sheet
														.getLastRowNum(), 1, 3));

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								else {

								}

							}
						}

						if (remark.toString().length() > 0) {
							remark_cell.setCellValue("Remark status :- "
									+ remark.toString());
							subdocument.put("remark", remark.toString());
							remark_cell.setCellStyle(error_remarkColumnStyle);
						}

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayKeymanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null) {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(
										deviceInfoList.get(i).getName()
												+ "   ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ")");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));
								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :- RDPS data not found.");
								subdocument.put("remark",
										"RDPS data not found.");
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								// cell.setCellStyle(error_remarkColumnStyle);

								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(

										deviceInfoList.get(i).getName()
												+ "  ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ") ");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :-Trips not Shedule for this device.");
								subdocument.put("remark",
										"Trips not Sgethedule for this device.");
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());
					if (startBatteryStatus < 3
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName()))
						KeyManLowBattery_Set.add(deviceInfoList.get(i)
								.getName());

					sectionDeviceList.add(subdocument);

				} else {
					// System.out.println("patrolman Man found--------------------");
					petrolMancount++;
				}

			}
		}

			Maindocument.put("exceptional_device", sectionDeviceList);

			report_table.insert(Maindocument);

			sheet.setColumnWidth(0, 1300);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 2000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);

			if (deviceInfoList.size() > 0
					&& petrolMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept().trim().replace("PWI/", "").replace("SSE/", "") + "_"
						+ "Exception_Trip_Report_KeyMan"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ "_" + parentId + ".xlsx";
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					String sendemail = "";
					for (String emaString : mailSendInfo.get(j).getEmailIds()) {
						sendemail = sendemail + "," + emaString.trim();
					}
					SendEmail mail = new SendEmail();

					
		mail.sendDeviceExceptionMailToJaipur(sendemail, file,
					 mailSendInfo
					  .get(j).getDept(),"Exception Trip Report of keymen"
					  ,"Exception Trip Report of keymen",false
						,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					 
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// /////ALl Section////////////////////////

			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReport);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			cell = row.createCell(1);
			cell.setCellValue(KeyManLowBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight
					.add((short) ((KeyManLowBattery_Set.toString()
							.replace("[", "").replace("]", "").toString()
							.length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(KeyManOffDevice_Set.toString().replace("[", "")
					.replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOffDevice_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			short height_row = (short) ((KeyManLowBattery_Set.toString()
					.replace("[", "").replace("]", "").toString().length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);
			System.err.println("Row max height-----" + height_row);

			sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

			if (j == mailSendInfo.size() - 1) {
				String outFileNameAllSectionStatus = "All Section KeyMan status Report_"
						+ parentId
						+ "_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ ".xlsx";
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReport.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReport.close();
					 String sendemail = getEmailIdForAllsectionReport(con,parentId);
						SendEmail mail = new SendEmail();
						System.err.println("Send mail All Section----" + sendemail);
							mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
									  "All Section"," KeyMan Work Status Report AllSection",
									  " KeyMan Work Status Report AllSection",true
										,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

				
				
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

			// /////////////////////*Alll Section////////////////////
			
			
			///Insert Datewise Saction Data
			 
	 
			 try {

					// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
					java.sql.CallableStatement ps = con
							.prepareCall("{call SaveDatewiseExceptionReport(?,?,?,?,?,?,?,?)}");

					
					ps.setInt(1, Integer.parseInt(parentId));
					ps.setString(2,DayStartWorkTime+"");
					ps.setString(3, KeyManLowBattery_Set.toString().replace("[", "")
							.replace("]", ""));

					ps.setString(4,KeyManOffDevice_Set.toString()
							.replace("[", "").replace("]", ""));

					ps.setString(5, KeyManExceptionalDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(6, KeyManOverspeedDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(7,mailSendInfo.get(j).getDept() );
					ps.setString(8, KeyManCoverSucefullyDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					int result = ps.executeUpdate();
					if (result == 0) {
						msgo.setError("true");
						msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
					} else {
						// //System.err.println("Error=="+result);
						msgo.setError("false");
						msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
				}

		}
	
		return msgo;

	}
	

	public MessageObject GenerateExceptionReportPatrolManBeatPathRanchi(
			Connection con, DB mongoconnection, String parentId) {


		MessageObject msgo = new MessageObject();
		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionReportPatrolManBeatPathRanchi ---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day + "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

		// ///////////////////////////////********************************/////////////////////////////
		// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("PatrolMan Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"Patrolman Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						5));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);

		row.setHeight((short) 500);

		// ///////////////////////////////////All Section
		// End//////////////////////////////////////////////////////////

		for (int j = 0; j < mailSendInfo.size(); j++) {
			// for (int j = 0; j <=0;j++) {

			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report of keymen"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyle.setAlignment(HorizontalAlignment.CENTER);
			font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);

			CellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap
			wrap_style.setAlignment((short) 0);

			int petrolMancount = 0;
			

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for Patrolman of section "
							+ mailSendInfo.get(j).getDept() + "  Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyle);
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 6));

			BasicDBList sectionDeviceList = new BasicDBList();

			if (deviceInfoList.size()>0) {
				
			for (int i = 0; i < deviceInfoList.size(); i++) {
				// for (int i = 119; i <=119; i++) {

//				System.err.println(" **Device =---================"
//						+ deviceInfoList.get(i).getName() + "\n"+mailSendInfo.get(j).getDept()+"---"+deviceInfoList.get(i)
//						.getStudentId());

				Double startBatteryStatus = getKeyManStartBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime-7200);

				Double endBatteryStatus = getKeyManEndBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+18000);

				if (deviceInfoList.get(i).getName().startsWith("P/")) {
					BasicDBObject subdocument = new BasicDBObject();

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfKeyman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));

								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

								dto.setSectionName(rsgertrip
										.getString("SectionName"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));
								RailwayKeymanTripsBeatsList.add(dto);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					// long DayStartWorkTime=1547836200;
					// System.out.println("DayStartWorkTime----" +
					// DayStartWorkTime);

					Double distanceTolerance = 1.3;
					// Double startLatOfRouteKm = 0.0, startLangOfRouteKm = 0.0,
					// endLatOfRouteKm = 0.0, endLangOfRouteKm = 0.0;

					// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
					// RailwayPetrolmanTripsBeatsList) {
					for (int r = 0; r < RailwayKeymanTripsBeatsList.size(); r++) {
						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayKeymanTripsBeatsList
								.get(r);

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.eparseLong("355488020181042"));

						/*
						 * BasicDBObject timestamp_whereQuery = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime).append("$lt",
						 * DayStartWorkTime+86400));
						 */

						BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime - 7200).append("$lt",
										DayStartWorkTime + 21600));
					/*	BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 41400).append("$lt",
										DayStartWorkTime + 55800));
*/
						/*
						 * BasicDBObject timestamp_whereQuery_morning = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+23400).append("$lt",
						 * DayStartWorkTime+37800)); BasicDBObject
						 * timestamp_whereQuery_afternoon = new BasicDBObject(
						 * "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+41400).append("$lt",
						 * DayStartWorkTime+55800));
						 */
						BasicDBList morn_Milage = new BasicDBList();
						morn_Milage.add(timestamp_whereQuery_morning);

						morn_Milage.add(device_whereQuery);
						DBObject morn_Milage_query = new BasicDBObject("$and",
								morn_Milage);

						BasicDBList after_Milage = new BasicDBList();
						after_Milage.add(timestamp_whereQuery_morning);
						after_Milage.add(device_whereQuery);
						DBObject after_Milage_query = new BasicDBObject("$and",
								after_Milage);

						BasicDBList Milage = new BasicDBList();
						Milage.add(morn_Milage_query);
						Milage.add(after_Milage_query);
						DBObject final_query = new BasicDBObject("$or", Milage);

						DBCursor cursor = table.find(final_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + final_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							System.err.println("*--listObjects---COunt---"
									+ cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();

						}

						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());
							;

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = locationNearbyKM(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"K");
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation = locationNearbyKM(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"K");
								if (tripEndLocation.getEndKmBeatActual() <= 0) {
									tripStartLocation.setStartkmBeatActual(0);

								}

								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								System.out
								.println(" listObjects.get(0).get(timestamp)---44--"+listObjects.get(0).get("timestamp"));
								exDto.setDeviceStartTime( listObjects.get(0).get("timestamp")+"");
								exDto.setDeviceEndTime(listObjects.get(listObjects.size()-1).get("timestamp")+"");

								exceptiionalTrip.add(exDto);
							} else {

								exceptiionalTrip.add(null);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setLocationSize(0);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exceptiionalTrip.add(exDto);
						}

					}
					System.out.println("\n\n");
					System.out
							.println("  Here--*************exceptiionalTrip****************************--------------------"
									+ exceptiionalTrip.size());
					System.out.println("\n\n");
					// //////////////////////////////////////////////

					// //////////////////////////////////
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null) {
						StringBuilder remark = new StringBuilder();
						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								deviceInfoList.get(i).getName() + "   ("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")");
						subdocument.put("device", deviceInfoList.get(i)
								.getDeviceID());
						subdocument.put("device_name", deviceInfoList.get(i)
								.getName());

						row.getCell(0).setCellStyle(HeadingStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));
						// start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((startBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("start_battery_status",
								df2.format(((startBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));

						// End Battery Staus
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("End Battery status :- "
								+ df2.format(((endBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("end_battery_status",
								df2.format(((endBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");

						remark_cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						cell = row.createCell(0);
						cell.setCellValue("Trip#");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(1);
						cell.setCellValue("Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue("Expected Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue("End Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(4);
						cell.setCellValue("Expected end Beat");
						cell.setCellStyle(tripColumnStyle);

						/*
						 * cell = row.createCell(5);
						 * cell.setCellValue("Avg speed");
						 * cell.setCellStyle(tripColumnStyle);
						 */

						cell = row.createCell(5);
						cell.setCellValue("Max speed");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(6);
						cell.setCellValue("Distance cover");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

					/*	cell = row.createCell(7);
						cell.setCellValue("Expected StartTime");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						cell = row.createCell(8);
						cell.setCellValue("Actual StartTime");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						cell = row.createCell(9);
						cell.setCellValue("Expected End Time");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);


						cell = row.createCell(10);
						cell.setCellValue("Actual End Time");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);
						
						cell = row.createCell(11);
						cell.setCellValue("Actual Trip Cover");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						
						   cell = row.createCell(12);
                         cell.setCellValue("Expected Trip Cover");
                     	cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);*/
						
						for (ExceptionReortsTrip exceptionReortsTrip : exceptiionalTrip) {
							System.out
									.println(" **exceptiionalTrip=---================"
											+ new Gson()
													.toJson(exceptionReortsTrip));

							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null
									&& exceptionReortsTrip.getLocationSize() > 0) {

								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = 0;
									double expactedKmCover = 0;
									if (exceptionReortsTrip.getTripStart()
											.getStartkmBeatActual() != 0
											&& exceptionReortsTrip.getTripEnd()
													.getEndKmBeatActual() != 0
											&& exceptionReortsTrip
													.getTripStart()
													.getMinDistance() < 5
											&& exceptionReortsTrip.getTripEnd()
													.getMinDistance() < 5) {
										kmcover = (exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatActual() - exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatActual());
										if (kmcover < 0)
											kmcover = -1 * kmcover;
										expactedKmCover = exceptionReortsTrip
												.getTripStart()
												.getEndKmBeatExpected()
												- exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected();
										if (expactedKmCover < 0)
											expactedKmCover = -1
													* expactedKmCover;

										if (kmcover < expactedKmCover
												- distanceTolerance) {
											remark.append("\tTotal beats not completed in Trip "
													+ exceptionReortsTrip
															.getTripNo() + ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark_cell
													.setCellValue("Remark status :- All work done succesfully.");
											
											KeyManCoverSucefullyDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
										}

									} else {
										kmcover = 0;

										if (exceptionReortsTrip.getTripStart()
												.getTotal_distance() < 1) {
											remark.append("\tDevice is not moved "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else if (exceptionReortsTrip
												.getTripStart()
												.getMinDistance() > 5
												&& exceptionReortsTrip
														.getTripEnd()
														.getMinDistance() > 5) {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
										}

									}

									System.out
											.println(" **expactedKmCover******---"
													+ expactedKmCover
													+ "---kmcover-" + kmcover);

									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());
										subdocument
												.put("Tripno",
														exceptionReortsTrip
																.getTripNo());

										cell.setCellStyle(wrap_style);

										cell = row.createCell(1);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));
										subdocument
												.put("start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatActual()));
										cell.setCellStyle(wrap_style);

										cell = row.createCell(2);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatExpected());
										subdocument
												.put("expected_start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatExpected()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(3);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));
										subdocument.put("end_beat", df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(4);
										cell.setCellValue(exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatExpected());
										subdocument
												.put("expected_end_beat",
														df2.format(exceptionReortsTrip
																.getTripEnd()
																.getEndKmBeatExpected()));

										cell.setCellStyle(wrap_style);

										/*
										 * cell = row.createCell(5);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 * cell.setCellStyle(wrap_style);
										 */

										cell = row.createCell(5);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart().getMaxSpeed());
										subdocument.put("max_speed", df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getMaxSpeed()));

										if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8) {
											KeyManOverspeedDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
										}

										cell.setCellStyle(wrap_style);

										if (exceptionReortsTrip.getDeviceStartTime()!=null) {
											
											
											cell = row.createCell(6);
											cell.setCellValue(df2.format(kmcover));
											subdocument.put("kmcover",
													df2.format(kmcover));
											cell.setCellStyle(wrap_style);

											/*cell = row.createCell(7);
											cell.setCellValue(Common.getDateCurrentTimeZone(DayStartWorkTime+25200));
											cell.setCellStyle(wrap_style);
											
										

											cell = row.createCell(8);
											cell.setCellValue(Common.getDateCurrentTimeZone(Long.parseLong(exceptionReortsTrip
													.getDeviceStartTime())));

											cell.setCellStyle(wrap_style);
											cell = row.createCell(9);
											cell.setCellValue(Common.getDateCurrentTimeZone(DayStartWorkTime+57600));

											cell.setCellStyle(wrap_style);
											cell = row.createCell(10);
											cell.setCellValue(Common.getDateCurrentTimeZone(Long.parseLong(exceptionReortsTrip
													.getDeviceEndTime())));
											cell.setCellStyle(wrap_style);
											
											if (remark_cell.getStringCellValue().toString().contains("All work done succesfully")) {
												cell = row.createCell(11);
												cell.setCellValue("1");

												cell.setCellStyle(wrap_style);
											 }else{
                                                cell = row.createCell(11);
                                                cell.setCellValue("0");

                                                cell.setCellStyle(wrap_style);
                                            }
										
											   cell = row.createCell(12);
	                                            cell.setCellValue("1");
	                                            cell.setCellStyle(wrap_style);*/
	                                      
											
										}
									
										// System.out.println("  Here--*****************************************--------------------");
										//

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// Exception of location not found
								if (exceptionReortsTrip != null
										&& exceptionReortsTrip
												.getLocationSize() == 0)
									try {
										// System.out.println(" **exceptionReortsTrip*******row******************"
										// +
										// "***-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size()+"-----"+exceptionReortsTrip.getLocationSize());

										KeyManOffDevice_Set.add(deviceInfoList
												.get(i).getName());

										remark.append("Device is Off");

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(1);
										cell.setCellStyle(loactionNotFoundColumnStyle);

										cell = row.createCell(1);
										cell.setCellValue("Device is Off");
										cell.setCellStyle(loactionNotFoundColumnStyle);
										sheet.addMergedRegion(new CellRangeAddress(
												sheet.getLastRowNum(), sheet
														.getLastRowNum(), 1, 3));

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								else {

								}

							}
						}

						if (remark.toString().length() > 0) {
							remark_cell.setCellValue("Remark status :- "
									+ remark.toString());
							subdocument.put("remark", remark.toString());
							remark_cell.setCellStyle(error_remarkColumnStyle);
						}

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayKeymanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null) {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(
										deviceInfoList.get(i).getName()
												+ "   ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ")");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));
								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :- RDPS data not found.");
								subdocument.put("remark",
										"RDPS data not found.");
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								// cell.setCellStyle(error_remarkColumnStyle);

								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(

										deviceInfoList.get(i).getName()
												+ "  ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ") ");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :-Trips not Shedule for this device.");
								subdocument.put("remark",
										"Trips not Sgethedule for this device.");
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());
					if (startBatteryStatus < 3
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName()))
						KeyManLowBattery_Set.add(deviceInfoList.get(i)
								.getName());

					sectionDeviceList.add(subdocument);

				} else {
					// System.out.println("patrolman Man found--------------------");
					petrolMancount++;
				}

			}
		}

			Maindocument.put("exceptional_device", sectionDeviceList);

			report_table.insert(Maindocument);

			sheet.setColumnWidth(0, 1300);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 2000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);

			
			
			
			
			if (deviceInfoList.size() > 0
					&& petrolMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept().trim().replace("PWI/", "").replace("SSE/", "") + "_"
						+ "Exception_Trip_Report_PatrolMan"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ "_" + parentId + ".xlsx";
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					String sendemail = "";
					for (String emaString : mailSendInfo.get(j).getEmailIds()) {
						sendemail = sendemail + "," + emaString.trim();
					}
					SendEmail mail = new SendEmail();

				
		mail.sendDeviceExceptionMailToJaipur(sendemail, file,
					 mailSendInfo
					  .get(j).getDept(),"Exception Trip Report of PatrolMan"
					  ,"Exception Trip Report of keymen",false
						,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					 
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// /////ALl Section////////////////////////

			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReport);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			cell = row.createCell(1);
			cell.setCellValue(KeyManLowBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight
					.add((short) ((KeyManLowBattery_Set.toString()
							.replace("[", "").replace("]", "").toString()
							.length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(KeyManOffDevice_Set.toString().replace("[", "")
					.replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOffDevice_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			short height_row = (short) ((KeyManLowBattery_Set.toString()
					.replace("[", "").replace("]", "").toString().length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);
			System.err.println("Row max height-----" + height_row);

			sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

			if (j == mailSendInfo.size() - 1) {
				String outFileNameAllSectionStatus = "All Section Patrolman status Report_"
						+ parentId
						+ "_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ ".xlsx";
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReport.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReport.close();

					 String sendemail = getEmailIdForAllsectionReport(con,parentId);
							SendEmail mail = new SendEmail();
							System.err.println("Send mail All Section----" + sendemail);
								mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
										  "All Section"," PatrolMan Work Status Report AllSection",
										  " PatrolMan Work Status Report AllSection",true
											,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

						
					 

					
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		

			// /////////////////////*Alll Section////////////////////
			
			
			///Insert Datewise Saction Data
			 
		 
		/*	try {

					// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
					java.sql.CallableStatement ps = con
							.prepareCall("{call SaveDatewiseExceptionReport(?,?,?,?,?,?,?,?)}");

					
					ps.setInt(1, Integer.parseInt(parentId));
					ps.setString(2,DayStartWorkTime+"");
					ps.setString(3, KeyManLowBattery_Set.toString().replace("[", "")
							.replace("]", ""));

					ps.setString(4,KeyManOffDevice_Set.toString()
							.replace("[", "").replace("]", ""));

					ps.setString(5, KeyManExceptionalDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(6, KeyManOverspeedDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(7,mailSendInfo.get(j).getDept() );
					ps.setString(8, KeyManCoverSucefullyDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					int result = ps.executeUpdate();
					if (result == 0) {
						msgo.setError("true");
						msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
					} else {
						// //System.err.println("Error=="+result);
						msgo.setError("false");
						msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
				}*/
		}

		// //////////for section end

		return msgo;

	}
	
	
	
	public String getEmailIdForAllsectionReport(Connection con, String parentId) {
		String sendemail="";

		try {
			CallableStatement ps_call = con
					.prepareCall("{call GetEmailIdForAllsectionReport(?)}");
			ps_call.setString(1, parentId);

			ResultSet rs4 = ps_call.executeQuery();

			if (rs4 != null)
				while (rs4.next()){
					
					if (sendemail.length() == 0) {
						sendemail = rs4
								.getString("EmailId")
								.trim();
					} else {
						sendemail = sendemail + "," + rs4
								.getString("EmailId")
								.trim();

					}
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.err.println("getEmailIdForAllsectionReport--"+sendemail);

		return sendemail;
		}	
	

	public synchronized PoleNearByLocationDto locationNearbyKMPatrolMan(String event,
			double km, double lat1, double lon1,
			ArrayList<DBObject> location_list, long dayStartWorkTime,String devicetype,
			long tripStartTime,long tripEndTime, long TimeInterval, long timeTolerance) {
		
		long startTime,endTime;
		
		
			startTime=tripStartTime;
			endTime=tripEndTime;
		
		// System.err.println("locationNearbyKM------------"+"km---"+km+"------"+lat1+","+lon1+"---");
		
		int MaxSpeed=0 ,MaxSpeedCopy= 0;
		int TotalSpeed = 0;
		StringBuilder speedbuild = new StringBuilder();
		Double Total_distance = 0.0;
		double earthRadius = 3958.75;
		double minDistance = 0;
		int position = 0;
		
//		System.err.println("PoleNearByLocationDto startTime-"+startTime+" timeTolerance-"+timeTolerance+" TimeInterval-"+TimeInterval);
//		System.out.println("PoleNearByLocationDto loactionTime >"+ Common.getDateCurrentTimeZone(startTime+timeTolerance)
//							+"  loactionTime < "+Common.getDateCurrentTimeZone(startTime+TimeInterval+timeTolerance));
		for (int i = 0; i < location_list.size(); i++) {

			BasicDBObject locobj = (BasicDBObject) location_list.get(i).get(
					"location");
			double lat2 = locobj.getDouble("lat");
			double lon2 = locobj.getDouble("lon");

			if (i > 0) {
				BasicDBObject prev_locobj = (BasicDBObject) location_list.get(
						i - 1).get("location");
				double caldist = distance(prev_locobj.getDouble("lat"),
						prev_locobj.getDouble("lon"), lat2, lon2, "K");
				if (!Double.isNaN(caldist))
					Total_distance = Total_distance + caldist;
			}

			double distance_calculated = distance(lat1, lon1, lat2, lon2, "K");
//			 System.err.println(Common.getDateCurrentTimeZone(((BasicDBObject) location_list
//						.get(i)).getLong("timestamp"))+"locationNearbyKM-----distance------"+i+"------"+distance_calculated+"--"+lat1+","+lon1+"---"+lat2+","+lon2);
//
			int SpeedcCheck = (int) location_list.get(i).get("speed");

			if (i == 0) {
				minDistance = distance_calculated;
				MaxSpeed = SpeedcCheck;
				MaxSpeedCopy=SpeedcCheck;

			} else if (distance_calculated < minDistance) {
				minDistance = distance_calculated;
				position = i;
			}

			Long loactionTime = Long.parseLong(location_list.get(i).get(
					"timestamp")
					+ "");

			  if (i > 5) {
			
				
				if (SpeedcCheck > MaxSpeed
						&& (loactionTime > startTime+timeTolerance
								&& loactionTime < startTime+TimeInterval+timeTolerance)
						&& (int) location_list.get(i - 1).get("speed") > 8
						&& (int) location_list.get(i - 2).get("speed") > 8
						&& (int) location_list.get(i - 3).get("speed") > 8
						&& (int) location_list.get(i - 4).get("speed") > 8
						&& (int) location_list.get(i - 5).get("speed") > 8
//						&& (int) location_list.get(i - 6).get("speed") > 8
//						&& (int) location_list.get(i - 7).get("speed") > 8
//						&& (int) location_list.get(i - 8).get("speed") > 8
//						&& (int) location_list.get(i - 9).get("speed") > 8
//						&& (int) location_list.get(i - 10).get("speed") > 8
						)
					MaxSpeed = SpeedcCheck;
				
//				if (SpeedcCheck<8&&SpeedcCheck > MaxSpeedCopy
//						&& (loactionTime > dayStartWorkTime + 41400 && loactionTime < dayStartWorkTime + 55800))
//					MaxSpeedCopy = SpeedcCheck;
//				else 
				if (SpeedcCheck<8&&SpeedcCheck > MaxSpeedCopy
						&& (loactionTime > startTime && loactionTime < startTime+TimeInterval))
					MaxSpeedCopy = SpeedcCheck;

				
			}

			TotalSpeed = TotalSpeed + SpeedcCheck;
			speedbuild.append(SpeedcCheck + "").append(",");

			// if (event.equalsIgnoreCase("end"))
			// System.err.println(i+"-----------speed--"+SpeedcCheck+"--max-"+MaxSpeed+"---Location time-----"+Common.getDateCurrentTimeZone(loactionTime));

		}
		
		if (MaxSpeed==0) 
			MaxSpeed=MaxSpeedCopy;
		
//		 System.err.println("-----------MaxSpeed--"+MaxSpeed+"--MaxSpeedCopy-"+MaxSpeedCopy);

		/*
		 * event+"----locationNearbyKM-----time------"+Common.getDateCurrentTimeZone
		 * (dayStartWorkTime+23400)+
		 * ","+Common.getDateCurrentTimeZone(dayStartWorkTime+37800)
		 * +"---lot2---"+Common.getDateCurrentTimeZone(dayStartWorkTime+41400)
		 * +","+Common.getDateCurrentTimeZone(dayStartWorkTime+55800)+
		 */// System.err.println(event+"----SpeedcCheck----------"+speedbuild.toString());

		// System.err.println("DIstttttttttttttttttt---------"+Total_distance);
//		System.out.println(event
//				+ "--------"
//				+ km
//				+ "----Min at position-"
//				+ position
//				+ "--dist--"
//				+ minDistance
//				+ "===time=="
//				+ Common.getDateCurrentTimeZone(((BasicDBObject) location_list
//						.get(position)).getLong("timestamp")));

		int meterConversion = 1609;
		BasicDBObject locobj2 = (BasicDBObject) location_list.get(position)
				.get("location");
		PoleNearByLocationDto dto = new PoleNearByLocationDto();
		dto.setLang(locobj2.getDouble("lon"));
		dto.setLat(locobj2.getDouble("lat"));
		dto.setMinDistance((minDistance));
		dto.setSpeed((int) location_list.get(position).get("speed"));
		dto.setAvgSpeed(TotalSpeed / location_list.size());
		dto.setMaxSpeed(MaxSpeed);
		dto.setTotalspeed(TotalSpeed);
		if (event.equalsIgnoreCase("start")) {
			if (Total_distance > 1) {
				dto.setStartkmBeatExpected(km);
				if (Math.round(km) <= km) {
					dto.setStartkmBeatActual(km + minDistance);

				} else {
					dto.setStartkmBeatActual(km - minDistance);

				}

				dto.setTotal_distance(Total_distance);
				long time=((BasicDBObject) location_list.get(position))
						.getLong("timestamp");
//				if (time>startTime&&time<endTime) {
					dto.setTimestamp(time);

//				}else {
//					dto.setTimestamp((long) 0);
//				}

			} else {
				dto.setStartkmBeatExpected(km);
				dto.setStartkmBeatActual(0);
				dto.setTotal_distance(Total_distance);
				dto.setTimestamp((long) 0);

			}

			// System.out.println(event+"-@@@@@@@@--dto.getStartkmBeatActual()-----"+dto.getStartkmBeatActual()+"---setStartkmBeatExpected-"
			// + dto.getStartkmBeatExpected() );

		} else {
			if (Total_distance > 1) {

				dto.setEndKmBeatExpected(km);
				if (Math.round(km) <= km)
					dto.setEndKmBeatActual(km + minDistance);
				else
					dto.setEndKmBeatActual(km - minDistance);

			
				long time=((BasicDBObject) location_list.get(position))
						.getLong("timestamp");
				
//				if (time>startTime&&time<endTime) {
					dto.setTimestamp(time);

//				}else {
//					dto.setTimestamp((long) 0);
//				}

			} else {

				dto.setEndKmBeatExpected(km);
				dto.setEndKmBeatActual(0);
				dto.setTimestamp((long) 0);

			}

			// System.out.println(event+"--@@@@@@@@@-dto.getEndKmBeatActual()-----"+dto.getEndKmBeatActual()+"---getEndKmBeatExpected-"
			// + dto.getEndKmBeatExpected() );

		}

		return dto;
	}


	public synchronized PoleNearByLocationDto locationNearbyKM(String event,
			double km, double lat1, double lon1,
			ArrayList<DBObject> location_list, long dayStartWorkTime,String devicetype, long startTime1, long endTime1, long startTime2, long endTime2) {
		PoleNearByLocationDto dto = new PoleNearByLocationDto();

		try{
			
		
		
		
		 System.err.println("locationNearbyKM------------"+"km---"+km+"------"+lat1+","+lon1+"---");
		
		int MaxSpeed=0 ,MaxSpeedCopy= 0;
		int TotalSpeed = 0;
		StringBuilder speedbuild = new StringBuilder();
		Double Total_distance = 0.0;
		double earthRadius = 3958.75;
		double minDistance = 0;
		int position = 0;
		for (int i = 0; i < location_list.size(); i++) {

			BasicDBObject locobj = (BasicDBObject) location_list.get(i).get(
					"location");
			double lat2 = locobj.getDouble("lat");
			double lon2 = locobj.getDouble("lon");
			int SpeedcCheck = Integer.parseInt(location_list.get(i).get("speed")+"");

			if (i > 0) {
				BasicDBObject prev_locobj = (BasicDBObject) location_list.get(
						i - 1).get("location");
				double caldist = distance(prev_locobj.getDouble("lat"),
						prev_locobj.getDouble("lon"), lat2, lon2, "K");
				if (!Double.isNaN(caldist)&&SpeedcCheck>0)
					Total_distance = Total_distance + caldist;
			}

			double distance_calculated = distance(lat1, lon1, lat2, lon2, "K");
//			System.err.println("locationNearbyKM-----distance------"+i+"------"+
//			distance_calculated+"--"+lat1+","+lon1+"---"+lat2+","+lon2);
//			System.err.println("locationNearbyKM-----speed------"+i+"------"+
//					location_list.get(i).get("speed"));

			if (i == 0) {
				minDistance = distance_calculated;
				MaxSpeed = 1;
				MaxSpeedCopy=1;

			} else if (distance_calculated < minDistance) {
				minDistance = distance_calculated;
				position = i;
			}

			Long loactionTime = Long.parseLong(location_list.get(i).get(
					"timestamp")
					+ "");

			/*if (i == 0) {
				/* if (SpeedcCheck > MaxSpeed
						&& ((loactionTime > startTime1 &&loactionTime < endTime1)
								|| (loactionTime > startTime2 && loactionTime < endTime2)))
					MaxSpeed = SpeedcCheck;

			} else if (i == 1) {
		
				if (SpeedcCheck > MaxSpeed
							&& ((loactionTime > startTime1 &&loactionTime < endTime1)
						|| (loactionTime > startTime2 && loactionTime < endTime2)))
						
					MaxSpeed = SpeedcCheck;

			} else */
			if (i > 8) {
			
//				System.err.println(MaxSpeed+"---Maxspeed Check------"+i+"--"+SpeedcCheck+"--"+(loactionTime > startTime1 &&loactionTime < endTime1)+
//						"---"+(loactionTime > startTime2 && loactionTime < endTime2)+"---"+Common.getDateCurrentTimeZone(loactionTime));

//				System.out.println("---"+Common.getDateCurrentTimeZone(loactionTime)+"---"+Common.getDateCurrentTimeZone(startTime1)
//						+"--"+Common.getDateCurrentTimeZone(endTime1)+"--"+Common.getDateCurrentTimeZone(
//								startTime2)+"--"+Common.getDateCurrentTimeZone(endTime2));
				if (SpeedcCheck > MaxSpeed
						&& ((loactionTime > startTime1 &&loactionTime < endTime1)
								|| (loactionTime > startTime2 && loactionTime < endTime2))
						&& (int) location_list.get(i - 1).get("speed") > 8
						&& (int) location_list.get(i - 2).get("speed") > 8
						&& (int) location_list.get(i - 3).get("speed") > 8
						&& (int) location_list.get(i - 4).get("speed") > 8
						&& (int) location_list.get(i - 5).get("speed") > 8
						&& (int) location_list.get(i - 6).get("speed") > 8
						&& (int) location_list.get(i - 7).get("speed") > 8
						&& (int) location_list.get(i - 8).get("speed") > 8
//						&& (int) location_list.get(i - 9).get("speed") > 8
//						&& (int) location_list.get(i - 10).get("speed") > 8	
				
						)
					{
					System.err.println("Maxspeed Check True");
					MaxSpeed = SpeedcCheck;

					for (int k=0;k<10;k++)
						if ((int) location_list.get(i - k).get("speed") >MaxSpeed) 
							MaxSpeed=(int) location_list.get(i - k).get("speed");
                        

					}
				
				if (SpeedcCheck<8&&SpeedcCheck > MaxSpeedCopy
						&& ((loactionTime > startTime1 &&loactionTime < endTime1)
								|| (loactionTime > startTime2 && loactionTime < endTime2)))
					MaxSpeedCopy = SpeedcCheck;

				
			}

			TotalSpeed = TotalSpeed + SpeedcCheck;
			speedbuild.append(SpeedcCheck + "").append(",");

			// if (event.equalsIgnoreCase("end"))
			// System.err.println(i+"-----------speed--"+SpeedcCheck+"--max-"+MaxSpeed+"---Location time-----"+Common.getDateCurrentTimeZone(loactionTime));

		}
		
		if (MaxSpeed==0) 
			MaxSpeed=MaxSpeedCopy;
		
//		 System.err.println("-----------MaxSpeed--"+MaxSpeed+"--MaxSpeedCopy-"+MaxSpeedCopy);

		/*
		 * event+"----locationNearbyKM-----time------"+Common.getDateCurrentTimeZone
		 * (dayStartWorkTime+23400)+
		 * ","+Common.getDateCurrentTimeZone(dayStartWorkTime+37800)
		 * +"---lot2---"+Common.getDateCurrentTimeZone(dayStartWorkTime+41400)
		 * +","+Common.getDateCurrentTimeZone(dayStartWorkTime+55800)+
		 */// System.err.println(event+"----SpeedcCheck----------"+speedbuild.toString());

		// System.err.println("DIstttttttttttttttttt---------"+Total_distance);
		/*System.out.println(event
				+ "--------"
				+ km
				+ "----Min at position-"
				+ position
				+ "--dist--"
				+ minDistance
				+ "===time=="
				+ Common.getDateCurrentTimeZone(((BasicDBObject) location_list
						.get(position)).getLong("timestamp")));*/

		int meterConversion = 1609;
		BasicDBObject locobj2 = (BasicDBObject) location_list.get(position)
				.get("location");
		dto.setLang(locobj2.getDouble("lon"));
		dto.setLat(locobj2.getDouble("lat"));
		dto.setMinDistance((minDistance));
		dto.setSpeed((int) location_list.get(position).get("speed"));
		dto.setAvgSpeed(TotalSpeed / location_list.size());
		dto.setMaxSpeed(MaxSpeed);
		dto.setTotalspeed(TotalSpeed);
		if (event.equalsIgnoreCase("start")) {
			if (Total_distance > 1) {
				dto.setStartkmBeatExpected(km);
				if (Math.round(km) <= km) {
					dto.setStartkmBeatActual(km + minDistance);

				} else {
					dto.setStartkmBeatActual(km - minDistance);

				}

				dto.setTotal_distance(Total_distance);
				dto.setTimestamp(((BasicDBObject) location_list.get(position))
						.getLong("timestamp"));

			} else {
				dto.setStartkmBeatExpected(km);
				dto.setStartkmBeatActual(0);
				dto.setTotal_distance(Total_distance);
				dto.setTimestamp((long) 0);

			}

			// System.out.println(event+"-@@@@@@@@--dto.getStartkmBeatActual()-----"+dto.getStartkmBeatActual()+"---setStartkmBeatExpected-"
			// + dto.getStartkmBeatExpected() );

		} else {
			if (Total_distance > 1) {

				dto.setEndKmBeatExpected(km);
				if (Math.round(km) <= km)
					dto.setEndKmBeatActual(km + minDistance);
				else
					dto.setEndKmBeatActual(km - minDistance);

				dto.setTimestamp(((BasicDBObject) location_list.get(position))
						.getLong("timestamp"));

			} else {

				dto.setEndKmBeatExpected(km);
				dto.setEndKmBeatActual(0);
				dto.setTimestamp((long) 0);

			}

			// System.out.println(event+"--@@@@@@@@@-dto.getEndKmBeatActual()-----"+dto.getEndKmBeatActual()+"---getEndKmBeatExpected-"
			// + dto.getEndKmBeatExpected() );

		}
		System.err.println("locationNearbyKM--loaction time--"+Common.getDateCurrentTimeZone(dto.getTimestamp()));

		System.out.println("locationNearbyKM--dto--"+new Gson().toJson(dto));

		}catch(Exception e){
			e.printStackTrace();
		}
		return dto;
	}

public MessageObject insertGeoFenceLocationInTodayLocation(Connection con,
			DB mongoconnection) {
//		
//		String device, String startLat,
//		String startLan, String fenceDist
//		String[] device_array = device.split(",");
//		String[] startLat_array = startLat.split(",");
//		String[] startLan_array = startLan.split(",");
//		String[] fenceDist_array = fenceDist.split(",");
		MessageObject msgo=new MessageObject();

		try {
			java.sql.CallableStatement ps = con
					.prepareCall("{call GetDeviceReferanceFenceData()}");
			ResultSet rs = ps.executeQuery();

			if (rs != null) {
				while (rs.next()) {

					try {

						//for (int id = 0; id < device_array.length; id++) {

							System.out.println("------device----------" + "-------"
									+ rs.getLong("DeviceID"));
							DBCollection table = mongoconnection
									.getCollection(Common.TABLE_DEVICE);

							BasicDBObject newDocument = new BasicDBObject();
							newDocument.append(
									"$set",
									new BasicDBObject("fenceData",new BasicDBObject()
											.append("fenceLat",
													(rs.getDouble("Ref_Lat")))
											.append("fenceLan",
													(rs.getDouble("Ref_Long")))
//											.append("fenceDist", rs.getInt("FenceKM"))
													.append("fenceDist", 100)
											.append("fenceDuration", 3600)));

							BasicDBObject searchQuery = new BasicDBObject()
									.append("device",
											rs.getLong("DeviceID"));

							//table.update(searchQuery, newDocument);
							table.update(
								    searchQuery, 
								    newDocument, true, false);
							System.out.println("insertGeoFenceLocationInTodayLocation------" + searchQuery
									+ newDocument);

						
						//}

						msgo.setError("false");
						msgo.setMessage("Task added successfully.");

					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						msgo.setError("true");
						msgo.setMessage("Task not added successfully");
					} catch (MongoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						msgo.setError("true");
						msgo.setMessage("Task not added successfully");

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						msgo.setError("true");
						msgo.setMessage("Task not added successfully");
					}
				
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		
			
			
	
		
		return msgo;
	}
	

	public int getLatestBatteryStatus(DB mongoconnection, String device,
			long startdate) {
		int batterystatus = 999;
		long timestamp = 0;
		
		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_ALERT_MSG);

			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device", Long.parseLong(device));

			BasicDBObject timestamp_whereQuery = new BasicDBObject("timestamp",
					new BasicDBObject("$gte", startdate - 3600).append("$lt",
							startdate));
			BasicDBList And_Milage = new BasicDBList();
			And_Milage.add(timestamp_whereQuery);
			And_Milage.add(device_whereQuery);
			DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

			DBCursor cursor = table.find(Total_Milage_query);
			
			cursor.sort(new BasicDBObject("timestamp", -1)).limit(1);
			

			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

//			 System.out.print("getBatteryStatus Count of u-p---"
//			+ cursor.size() + "  " + Total_Milage_query);
			long total = cursor.count();

			if (cursor.size() != 0) {
				while (cursor.hasNext()) {

					DBObject dbObject = (DBObject) cursor.next();

					batterystatus = Integer.parseInt(dbObject
							.get("voltage_level") + "");
					timestamp=Long.parseLong(dbObject.get("timestamp")+"");
				}

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("");
		System.out.println("batterystatus-----"+batterystatus+"----timestamp---"+timestamp);

		return batterystatus;
	}

	public  ArrayList<DeviceBatteryInfo>  GetBatteryStatusInfo(DB mongoconnection,
			String device, long startdate, long enddate) {
		 ArrayList<DeviceBatteryInfo> batteryList=new ArrayList<>();
		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_ALERT_MSG);

			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device", Long.parseLong(device));

			BasicDBObject timestamp_whereQuery = new BasicDBObject("timestamp",
					new BasicDBObject("$gte", startdate).append("$lt",
							enddate));
			BasicDBList And_Milage = new BasicDBList();
			And_Milage.add(timestamp_whereQuery);
			And_Milage.add(device_whereQuery);
			DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

			DBCursor cursor = table.find(Total_Milage_query);
			
			cursor.sort(new BasicDBObject("timestamp", 1));//.limit(1);
			

			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			 System.out.print("getBatteryStatus Count of u-p---"
			+ cursor.size() + "  " + Total_Milage_query);
			long total = cursor.count();

			if (cursor.size() != 0) {
				while (cursor.hasNext()) {

					DBObject dbObject = (DBObject) cursor.next();
					DeviceBatteryInfo dto=new DeviceBatteryInfo();
					
					dto.setBatteryLevel(Double.parseDouble((df2.format((Double.parseDouble(dbObject
							.get("voltage_level") + "") / 6) * 100))));
					dto.setTimestamp(Long.parseLong(dbObject.get("timestamp")+""));
					dto.setNetwork(Double.parseDouble((df2.format((Double.parseDouble(dbObject
							.get("gsm_signal_strength") + "") / 4) * 100))));
					dto.setDeviceId(device);
					batteryList.add(dto);
				}

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return batteryList;
	}
	
	
	public String getRdpsKmNearByLocation(Connection con,Double lat,Double lang,String parentId, String sectionName, Double Km ) {

			String km="NA";
		try {
			java.sql.CallableStatement ps = con
					.prepareCall("{call GetNerbyRDPSwithDist(?,?,?,?,?)}");
			ps.setDouble(2, lang );
			ps.setDouble(1, lat );
			ps.setInt(3, Integer.parseInt(parentId));
			ps.setString(4, sectionName);	
			ps.setDouble(5, Km);
			System.out.println("getRdpsKmNearByLocation Km---"+Km);
			 System.err.println("getRdpsKmNearByLocation input 11------"+lat+"---"+lang+"--"+parentId+"--"+sectionName+"--");;

			ResultSet rs = ps.executeQuery();

			if (rs != null) {

				while (rs.next()) {
					FeatureAddressDetailsDTO addressDto = new FeatureAddressDetailsDTO();

					addressDto.setDistance(rs
							.getString("Distance") + "");
					addressDto.setFeatureCode(rs
							.getString("FeatureCode") + "");
					addressDto.setFeature_image(rs
							.getString("Images") + "");
					addressDto.setFeatureDetail(rs
							.getString("FeatureDetail") + "");
					addressDto.setKiloMeter(rs
							.getString("kiloMeter") + "");
					addressDto.setLatitude(rs
							.getString("Latitude") + "");
					addressDto.setLongitude(rs
							.getString("Longitude") + "");
					addressDto.setSection(rs
							.getString("Section") + "");
					addressDto.setBlockSection(rs
							.getString("BlockSection") + "");
					addressDto.setNearByDistance(rs
							.getString("NearByDistance") + "");
					 System.err.println("getRdpsKmNearByLocation ------"+addressDto.getNearByDistance());

					
//					if (Double.parseDouble(addressDto.getNearByDistance())<150.00) 
						km=df2.format(Double.parseDouble(addressDto.getKiloMeter()))+"";

					
					 System.err.println("getRdpsKmNearByLocation ------ "+new Gson().toJson(addressDto));;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return km;
	}

	public MessageObject GenerateExceptionReportPetrolmanBeatPathWithWholeTimestampConsider(
			Connection con, DB mongoconnection, String parentId, int dayCount, Boolean isSendMail, int seasonId, int timeTolerance, Double distanceTolerance)
	{

		MessageObject msgo = new MessageObject();

		//for (int pastday = 74;pastday >45; pastday--) {
		ArrayList<RailMailSendInfoDto> mailSendInfo = getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionReportpatrolmanBeatPath ---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();
		
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-dayCount;
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		

		long startdate = Long.valueOf(Common.getGMTTimeStampFromDate(day
				+ "-" + String.valueOf(month + 1) + "-" + year
				+ " 00:00 am"));
		long enddate = Long.valueOf(Common.getGMTTimeStampFromDate(day
				+ "-" + String.valueOf(month + 1) + "-" + year
				+ " 00:00 am"));


		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

		// ///////////////////////////////********************************/////////////////////////////
		// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("Patrolman Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"PatrolMan Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						6));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(6);
		cell.setCellValue("Start with Battery 30%-60%");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 500);

		// ///////////////////////////////////All Section
		// End//////////////////////////////////////////////////////////


		// //***************/////////////////////////////
				// All Section Count AlertForKeymanWorkStatusReport

				XSSFWorkbook workbookAlertForKeymanWorkStatusReportCount = new XSSFWorkbook();
				XSSFSheet sheetAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createSheet(WorkbookUtil
								.createSafeSheetName("KeyMan Status Report with count"));
				XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle();
				HeadingStyleAlertForKeymanWorkStatusReportCount
						.setFillForegroundColor(IndexedColors.AQUA.getIndex());
				HeadingStyleAlertForKeymanWorkStatusReportCount
						.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				 font = workbookAlertForKeymanWorkStatusReport.createFont();
				font.setFontHeightInPoints((short) 20);
				font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

				 tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle();
				tripColumnStyleAlertForKeymanWorkStatusReport
						.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
								.getIndex());
				tripColumnStyleAlertForKeymanWorkStatusReport
						.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
				font = workbookAlertForKeymanWorkStatusReport.createFont();
				font.setFontHeightInPoints((short) 15);
				font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
				XSSFCellStyle wrap_styleAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle(); // Create new style
				wrap_styleAlertForKeymanWorkStatusReportCount.setWrapText(true); // Set
																	

				row = sheetAlertForKeymanWorkStatusReportCount.createRow(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);
				row.createCell(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);
				row.createCell(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);

				row.createCell(0);
				row.getCell(0).setCellValue(
						"PatrolMan Work Status Count Report Date :-"
								+ Common.getDateFromLong(DayStartWorkTime));
				sheetAlertForKeymanWorkStatusReportCount
						.addMergedRegion(new CellRangeAddress(
								sheetAlertForKeymanWorkStatusReportCount.getLastRowNum(),
								sheetAlertForKeymanWorkStatusReportCount.getLastRowNum(), 0,
								4));
				row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReportCount);

				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);

				 cell = row.createCell(0);

				cell.setCellValue("Section Name");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				/*cell = row.createCell(1);
				cell.setCellValue("Start with Low Battery Devices");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);*/
				cell = row.createCell(1);
				cell.setCellValue("Off Devices");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				cell = row.createCell(2);
				cell.setCellValue("Beat not cover");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				;
				cell = row.createCell(3);
				cell.setCellValue("Beat completed Sucessfully");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				cell = row.createCell(4);
				cell.setCellValue("Total Device");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				row.setHeight((short) 800);
			/*	cell = row.createCell(6);
				cell.setCellValue("Section Status");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				row.setHeight((short) 500);*/

				// ///////////////////////////////////All Section COunt// End//////////////////////////////////////////////////////////

				int divDeviceOffCount=0;
				int divDeviceBeatNotCoverCount=0;
				int divDeviceBeatCoverCount=0;
		
		
		for (int j = 0; j < mailSendInfo.size(); j++) {
//	 for (int j =0; j <=0; j++) {
		//for (int j = mailSendInfo.size()-1; j <=mailSendInfo.size()-1;j++) {

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayPetrolmanTripsBeatsList = new ArrayList<>();
			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			Set<String> KeyMan60PercentBattery_Set = new HashSet<String>();

			ArrayList<Short> rowHeight = new ArrayList<>();
			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Patrolman Exception Trip Report"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 20);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);
			int keyMancount = 0;

			for (int i = 0; i < deviceInfoList.size(); i++) {
			//	 for (int i = 0; i <= 0; i++) {

//			System.err.println("Device----"+deviceInfoList.get(i).getName());

				if (deviceInfoList.get(i).getName().startsWith("P/")) {
//				if (deviceInfoList.get(i).getName().startsWith("P/")&&deviceInfoList.get(i).getName().contains("-215")) {

//					System.err.println("Device-sssss---"+deviceInfoList.get(i).getName());

					Double startBatteryStatus = getStartBatteryStatus(
							mongoconnection, deviceInfoList.get(i).getDeviceID(),
							startdate);
					Double endBatteryStatus = getEndBatteryStatus(mongoconnection,
							deviceInfoList.get(i).getDeviceID(), startdate);
					

					RailwayPetrolmanTripsBeatsList
							.removeAll(RailwayPetrolmanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfPetrolmanWithSeasonId(?,?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());
						psgettrip.setInt(2, seasonId);
						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));
								dto.setFkTripMasterId(rsgertrip
										.getInt("fk_TripMasterId"));
								dto.setKmFromTo(rsgertrip.getString("KmFromTo"));
								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));
								dto.setTotalKmCover(rsgertrip
										.getDouble("TotalKmCover"));
								dto.setInitialDayStartTime(rsgertrip
										.getString("InitailDayStratTime"));
								dto.setSheetNo(rsgertrip.getInt("SheetNo"));
								dto.setSectionName(rsgertrip
										.getString("SectionName"));
								dto.setTripName(rsgertrip.getString("TripName"));
								dto.setTripTimeShedule(rsgertrip
										.getString("TripTimeShedule"));
								dto.setTripStartTimeAdd(rsgertrip
										.getInt("TripStartTimeAdd"));
								dto.setTripSpendTimeIntervalAdd(rsgertrip
										.getInt("TripSpendTimeIntervalAdd"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));

								RailwayPetrolmanTripsBeatsList.add(dto);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}


					
					
					ArrayList totalLocationList=GetHistorydata(mongoconnection, deviceInfoList.get(i).getDeviceID()+"", DayStartWorkTime+"");

					if (totalLocationList.size()==0) {
						KeyManOffDevice_Set.add(deviceInfoList
								.get(i).getName());						
					}

					System.err.println("totalLocationList----size--"+totalLocationList.size());
					for (int r = 0; r < RailwayPetrolmanTripsBeatsList.size(); r++) {

						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayPetrolmanTripsBeatsList
								.get(r);
						
						/*long timeTolerance = railMailSendInfoDto
								.getTripSpendTimeIntervalAdd()/3;
						*/
					
						long triptStartTime = DayStartWorkTime
								+ railMailSendInfoDto.getTripStartTimeAdd()
								- timeTolerance;
						long triptEndTime = DayStartWorkTime
								+ railMailSendInfoDto.getTripStartTimeAdd()
								+ railMailSendInfoDto
										.getTripSpendTimeIntervalAdd()
								+ timeTolerance;

						// System.out.println("triptStartTime----"
						// + Common.getDateCurrentTimeZone(triptStartTime)
						// + "========triptEndTime----"
						// + Common.getDateCurrentTimeZone(triptEndTime));
						ArrayList<String> kmStartLatSet = new ArrayList<>(), kmStartLangSet = new ArrayList<>(), kmEndLatSet = new ArrayList<>(), kmEndLangSet = new ArrayList<>();

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.parseLong("355488020181042"));

						BasicDBObject timestamp_whereQuery = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										triptStartTime).append("$lt",
										triptEndTime));
						
						System.out.println("triptStartTime--"+triptStartTime+"--triptEndTime---"+triptEndTime);
						
						BasicDBList And_Milage = new BasicDBList();
						And_Milage.add(timestamp_whereQuery);
						And_Milage.add(device_whereQuery);
						DBObject Total_Milage_query = new BasicDBObject("$and",
								And_Milage);

						DBCursor cursor = table.find(Total_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + Total_Milage_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							// System.err.println("*--listObjects---COunt---" +
							// cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();

						}
						

						Double minDistanceCal = 0.0;
						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = locationNearbyKM(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"P",triptStartTime,triptEndTime);
								
								tripStartLocation.setTripStartExpectedTime(triptStartTime+ timeTolerance);
								tripStartLocation.setTripEndExpectedTime(triptEndTime- timeTolerance);
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation = locationNearbyKM(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"P",triptStartTime,triptEndTime);

								tripEndLocation
										.setTripStartExpectedTime(triptStartTime
												+ timeTolerance);
								tripEndLocation
										.setTripEndExpectedTime(triptEndTime
												- timeTolerance);
								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());

								exceptiionalTrip.add(exDto);
							} else {

								exceptiionalTrip.add(null);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exDto.setLocationSize(0);

							exceptiionalTrip.add(exDto);

						}

					}

					// //////////////////////////////////
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null
							) {
						StringBuilder remark = new StringBuilder();
						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								"Exception  Report for  "
										+ deviceInfoList.get(i).getName() + "("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")  Date :-"
										+ Common.getDateFromLong(startdate));
						row.setRowStyle(HeadingStyle);

						// Start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((startBatteryStatus / 6) * 100))
								+ " %");
						row.setRowStyle(remarkColumnStyle);
						// End Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("End Battery status :- "
								+ df2.format(((endBatteryStatus / 6) * 100))
								+ " %");
						row.setRowStyle(remarkColumnStyle);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");
						row.setRowStyle(remarkColumnStyle);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.setRowStyle(tripColumnStyle);

						cell = row.createCell(0);

						cell.setCellValue("Trip#");
						cell = row.createCell(1);
						cell.setCellValue("Start Time	");
						cell = row.createCell(2);
						cell.setCellValue("Expected Start Time");
						cell = row.createCell(3);
						cell.setCellValue("Start Beat");
						cell = row.createCell(4);
						cell.setCellValue("Expected Start Beat");
						cell = row.createCell(5);
						cell.setCellValue("End Time	");
						cell = row.createCell(6);
						cell.setCellValue("Expected End Time");
						cell = row.createCell(7);
						cell.setCellValue("End Beat");
						cell = row.createCell(8);
						cell.setCellValue("Expected end Beat");

						/*
						 * cell = row.createCell(9);
						 * cell.setCellValue("Stoppage min"); cell =
						 * row.createCell(10);
						 * cell.setCellValue("Average speed");
						 */
						cell = row.createCell(9);
						cell.setCellValue("Max Speed");
						cell = row.createCell(10);
						cell.setCellValue("Total Distance cover");

						for (ExceptionReortsTrip exceptionReortsTrip :  
							exceptiionalTrip) {
							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null) {
								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = (exceptionReortsTrip
											.getTripStart()
											.getStartkmBeatActual() - exceptionReortsTrip
											.getTripEnd().getEndKmBeatActual());
									if (kmcover < 0)
										kmcover = -1 * kmcover;

									double expactedKmCover = exceptionReortsTrip
											.getTripStart()
											.getEndKmBeatExpected()
											- exceptionReortsTrip
													.getTripStart()
													.getStartkmBeatExpected();
									if (expactedKmCover < 0)
										expactedKmCover = -1 * expactedKmCover;
/*
									if (exceptionReortsTrip.getTripStart()
											.getTripStartExpectedTime()
											- exceptionReortsTrip
													.getTripStart()
													.getTimestamp() > timeTolerance)
										remark.append("\t "
												+ exceptionReortsTrip
														.getTripNo()
												+ " delayed.");
*/
									if (kmcover < expactedKmCover
											- distanceTolerance)
										remark.append("\tTotal beats not completed in Trip "
												+ exceptionReortsTrip
														.getTripNo() + ".");

									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());

										cell = row.createCell(1);
										if (exceptionReortsTrip.getTripStart()
												.getTimestamp() > 0)
											cell.setCellValue(Common
													.getDateCurrentTimeZone(exceptionReortsTrip
															.getTripStart()
															.getTimestamp()));
										else
											cell.setCellValue("Not Found");

										cell = row.createCell(2);
										cell.setCellValue(Common
												.getDateCurrentTimeZone(exceptionReortsTrip
														.getTripStart()
														.getTripStartExpectedTime()));

										cell = row.createCell(3);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));
										cell = row.createCell(4);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected()));

										cell = row.createCell(5);
										if (exceptionReortsTrip.getTripEnd()
												.getTimestamp() > 0)
											cell.setCellValue(Common
													.getDateCurrentTimeZone(exceptionReortsTrip
															.getTripEnd()
															.getTimestamp()));
										else
											cell.setCellValue("Not Found");

										cell = row.createCell(6);
										cell.setCellValue(Common
												.getDateCurrentTimeZone(exceptionReortsTrip
														.getTripEnd()
														.getTripEndExpectedTime()));

										cell = row.createCell(7);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));
										cell = row.createCell(8);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatExpected()));

										/*
										 * cell = row.createCell(9);
										 * cell.setCellValue(""); cell =
										 * row.createCell(10);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 */
										cell = row.createCell(9);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart().getMaxSpeed());
										cell = row.createCell(10);
										cell.setCellValue(df2.format(kmcover));
										
									

										if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8&&exceptionReortsTrip.getMaxSpeed()>8) {
											KeyManOverspeedDevices_Set.add(deviceInfoList.get(i)
															.getName());
										}


										// System.out.println("  Here--*****************************************--------------------");
										//
										
										
										
										
										

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// Exception of location not found
					
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());
									// Exception of location not found
									if (exceptionReortsTrip != null
											&& exceptionReortsTrip
													.getLocationSize() == 0)
										try {
											// System.out.println(" **exceptionReortsTrip*******row******************"
											// +
											// "***-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size()+"-----"+exceptionReortsTrip.getLocationSize());

											if (totalLocationList.size()==0) {
												KeyManOffDevice_Set.add(deviceInfoList
														.get(i).getName());
												remark.append("Device is Off");
												row = sheet
														.createRow(sheet.getLastRowNum() + 1);
												cell = row.createCell(0);
												cell.setCellValue(exceptionReortsTrip
														.getTripNo());
												cell.setCellStyle(loactionNotFoundColumnStyle);

												cell = row.createCell(1);
												cell.setCellValue("Device is Off : "
														+ exceptionReortsTrip.getTripNo()
														+ ".");
												cell.setCellStyle(loactionNotFoundColumnStyle);

											}else{
												remark.append("\tLocation not found for Trip no : "
														+ exceptionReortsTrip.getTripNo()
														+ ".");
												row = sheet
														.createRow(sheet.getLastRowNum() + 1);
												cell = row.createCell(0);
												cell.setCellValue(exceptionReortsTrip
														.getTripNo());
												cell.setCellStyle(loactionNotFoundColumnStyle);

												cell = row.createCell(1);
												cell.setCellValue("Location not found for Trip no: "
														+ exceptionReortsTrip.getTripNo()
														+ ".");
												cell.setCellStyle(loactionNotFoundColumnStyle);
											}
											
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

						if (remark.toString().length() > 0)
							{
							remark_cell.setCellValue("Remark status :- "
									+ remark.toString());
									
							
							if (!KeyManOffDevice_Set.contains(deviceInfoList.get(i)
											.getName())) {
								KeyManExceptionalDevices_Set
								.add(deviceInfoList.get(i)
										.getName());
								
							}
							
						
							}
								
						else
							{
							remark_cell
							.setCellValue("Remark status :- All work done succesfully.");

							if (!KeyManOffDevice_Set.contains(deviceInfoList.get(i)
									.getName()))
							KeyManCoverSucefullyDevices_Set
							.add(deviceInfoList.get(i)
									.getName());
							}

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayPetrolmanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						/*
						 * if(railMailSendInfoDto.getKmStartLat()>0&&
						 * railMailSendInfoDto.getKmStartLang()>0
						 * &&railMailSendInfoDto
						 * .getKmEndLat()>0&&railMailSendInfoDto
						 * .getKmEndLang()>0)
						 */
						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null) {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0)
										.setCellValue(
												"Exception  Report for  "
														+ deviceInfoList.get(i)
																.getName()
														+ "("
														+ deviceInfoList.get(i)
																.getDeviceID()
														+ ")  Date :-"
														+ Common.getDateFromLong(startdate));
								row.setRowStyle(HeadingStyle);

								// Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Battery status :- Not found");
								row.setRowStyle(remarkColumnStyle);

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row.setRowStyle(remarkColumnStyle);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :- RDPS data not found.");
								
								if (!KeyManOffDevice_Set.contains(deviceInfoList.get(i)
										.getName()))
								KeyManExceptionalDevices_Set
								.add(deviceInfoList.get(i)
										.getName());

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0)
										.setCellValue(
												"Exception  Report for  "
														+ deviceInfoList.get(i)
																.getName()
														+ "("
														+ deviceInfoList.get(i)
																.getDeviceID()
														+ ")  Date :-"
														+ Common.getDateFromLong(startdate));
								row.setRowStyle(HeadingStyle);

								// Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Battery status :- Not found");
								row.setRowStyle(remarkColumnStyle);

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row.setRowStyle(remarkColumnStyle);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :-Trips not Shedule for this device.");

								//if ( exceptiionalTrip.get(0).getLocationSize() > 0) {
								if (!KeyManOffDevice_Set.contains(deviceInfoList.get(i)
										.getName()))
									KeyManExceptionalDevices_Set
									.add(deviceInfoList.get(i)
											.getName());
								/*}else {
									KeyManOffDevice_Set
									.add(deviceInfoList.get(i)
											.getName());
								}
								*/
								
								
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());
					
					if (startBatteryStatus < 3
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName()))
						KeyManLowBattery_Set.add(deviceInfoList.get(i)
								.getName());
					
					if ((startBatteryStatus==3||startBatteryStatus==4)
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName()))
						KeyMan60PercentBattery_Set.add(deviceInfoList.get(i)
								.getName());
					
				} else {
					// System.out.println("patrol Man found--------------------");
				keyMancount++;

				}
				
				
			}

			for (int s = 1; s < 15; s++) {
				sheet.autoSizeColumn(s);
			}

			if (deviceInfoList.size() > 0&&keyMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept().replace("/", "_") + "_"
						+ "Exception_Trip_Report_PartolMen"
						+ Common.getDateFromLong(startdate).replace(":", "-")	+ "_"+parentId
						+ ".xlsx";
				
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					
					if(isSendMail){
						String sendemail = "";
						for (String emaString : mailSendInfo.get(j).getEmailIds()) {
							sendemail = sendemail + "," + emaString.trim();
						}
						SendEmail mail = new SendEmail();
						mail.sendDeviceExceptionMailToJaipur(sendemail, file,
								mailSendInfo.get(j).getDept(),
								"Exception Trip Report of PatrolMan",
								"Exception Trip Report of PatrolMan", false
								,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());


					}
				
			} catch (Exception e) {

					e.printStackTrace();
				}

			}

		// /////ALl Section////////////////////////
			if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
					KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1){
				
			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReport);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			cell = row.createCell(1);
			cell.setCellValue(KeyManLowBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight
					.add((short) ((KeyManLowBattery_Set.toString()
							.replace("[", "").replace("]", "").toString()
							.length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(KeyManOffDevice_Set.toString().replace("[", "")
					.replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOffDevice_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));
			
			
			
			cell = row.createCell(6);
			cell.setCellValue(KeyMan60PercentBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); 
			
			short height_row = (short) ((KeyManLowBattery_Set.toString()
					.replace("[", "").replace("]", "").toString().length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);
			System.err.println("Row max height-----" + height_row);

			sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

			if (j == mailSendInfo.size() - 1) {
				String outFileNameAllSectionStatus = "All Section PartolMen status Report_"
						+ Common.getDateFromLong(startdate).replace(":", "-")	+ "_"+parentId
						+ ".xlsx";
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReport.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReport.close();
		
					if (isSendMail) {
						 String sendemail = getEmailIdForAllsectionReport(con,parentId);
							SendEmail mail = new SendEmail();
							System.err.println("Send mail All Section----" + sendemail);
								mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
										  "All Section"," PartolMen Work Status Report AllSection",
										  " PartolMen Work Status Report AllSection",true
											,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

						
					}
					
							
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
		}

			// /////////////////////*Alll Section////////////////////
			
			
			
			

			
			// /////ALl Section COunt////////////////////////
			 divDeviceOffCount=divDeviceOffCount+KeyManOffDevice_Set.size();
			 divDeviceBeatNotCoverCount=divDeviceBeatNotCoverCount+KeyManExceptionalDevices_Set.size();
			 divDeviceBeatCoverCount=divDeviceBeatCoverCount+KeyManCoverSucefullyDevices_Set.size();

			
	
				 if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
							KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1)
				 {	

			row = sheetAlertForKeymanWorkStatusReportCount
					.createRow(sheetAlertForKeymanWorkStatusReportCount
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	

			cell = row.createCell(1);
			cell.setCellValue(KeyManOffDevice_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	
			cell = row.createCell(2);
			cell.setCellValue(KeyManExceptionalDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			

			cell = row.createCell(3);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

			cell = row.createCell(4);
			cell.setCellValue((KeyManCoverSucefullyDevices_Set.size()+KeyManOffDevice_Set.size()+
					KeyManExceptionalDevices_Set.size())+"");
			
			
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			
	
			
			row.setHeight((short) 600);

			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(0, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(4, 3000);

			if (j == mailSendInfo.size() - 1) {
				
				//for total row
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount
								.getLastRowNum() + 1);
				row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				cell = row.createCell(0);
				cell.setCellValue("Total Device");

				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

				cell = row.createCell(1);
				cell.setCellValue(divDeviceOffCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				;

				cell = row.createCell(2);
				cell.setCellValue(divDeviceBeatNotCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				
				cell = row.createCell(3);
				cell.setCellValue(divDeviceBeatCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				

				cell = row.createCell(4);
				cell.setCellValue((divDeviceOffCount+divDeviceBeatCoverCount+
						divDeviceBeatNotCoverCount)+"");
				
				
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);


				
				row.setHeight((short) 600);

				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(0, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(1, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(2, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(3, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(4, 3000);
				
				String outFileNameAllSectionStatus = "All_Section_Patrolman_Status_Device_Count_Report_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_"+parentId+ ".xlsx";
				
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReportCount.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReportCount.close();

					String sendemail = getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					/*if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," Keyman Work Status device count Report AllSection",
								  " Keyman Work Status device count Report AllSection",true	
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					}*/
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

			// /////////////////////*Alll Section COunt end ////////////////////
			
			
			///Insert Datewise Saction Data
		 
	
	
	if (isSendMail) 
	try {

					// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
					java.sql.CallableStatement ps = con
							.prepareCall("{call SaveDatewiseExceptionReport(?,?,?,?,?,?,?,?)}");

					
					
					ps.setInt(1, Integer.parseInt(parentId));
					ps.setString(2,DayStartWorkTime+"");
					ps.setString(3, KeyManLowBattery_Set.toString().replace("[", "")
							.replace("]", ""));

					ps.setString(4,KeyManOffDevice_Set.toString()
							.replace("[", "").replace("]", ""));

					ps.setString(5, KeyManExceptionalDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(6, KeyManOverspeedDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(7,mailSendInfo.get(j).getDept() );
					ps.setString(8, KeyManCoverSucefullyDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					int result = ps.executeUpdate();
					if (result == 0) {
						msgo.setError("true");
						msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
					} else {
						// //System.err.println("Error=="+result);
						msgo.setError("false");
						msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
				}
			}
		//}

		return msgo;

	}
	
	public synchronized PoleNearByLocationDto locationNearbyKM(String event,
			double km, double lat1, double lon1,
			ArrayList<DBObject> location_list, long dayStartWorkTime,String devicetype,
			long tripStartTime,long tripEndTime) {
		
		long startTime,endTime,MaxSpeedTime = 0;
		
		if(devicetype.equalsIgnoreCase("K")){
			startTime=dayStartWorkTime+23400;
			endTime=dayStartWorkTime+37800;
					
		}else{
			startTime=tripStartTime;
			endTime=tripEndTime;
		}
		// System.err.println("locationNearbyKM------------"+"km---"+km+"------"+lat1+","+lon1+"---");
		
		int MaxSpeed=0 ,MaxSpeedCopy= 0;
		int TotalSpeed = 0;
		StringBuilder speedbuild = new StringBuilder();
		Double Total_distance = 0.0;
		double earthRadius = 3958.75;
		double minDistance = 0;
		int position = 0;
		for (int i = 0; i < location_list.size(); i++) {

			BasicDBObject locobj = (BasicDBObject) location_list.get(i).get(
					"location");
			double lat2 = locobj.getDouble("lat");
			double lon2 = locobj.getDouble("lon");

			if (i > 0) {
				BasicDBObject prev_locobj = (BasicDBObject) location_list.get(
						i - 1).get("location");
				double caldist = distance(prev_locobj.getDouble("lat"),
						prev_locobj.getDouble("lon"), lat2, lon2, "K");
				if (!Double.isNaN(caldist))
					Total_distance = Total_distance + caldist;
			}

			double distance_calculated = distance(lat1, lon1, lat2, lon2, "K");
			 //System.err.println("locationNearbyKM-----distance------"+i+"------"+distance_calculated+"--"+lat1+","+lon1+"---"+lat2+","+lon2);

			int SpeedcCheck = (int) location_list.get(i).get("speed");
		
			Long loactionTime = Long.parseLong(location_list.get(i).get(
					"timestamp")
					+ "");
			if (i == 0) {
				minDistance = distance_calculated;
				MaxSpeed = SpeedcCheck;
				MaxSpeedCopy=SpeedcCheck;
				MaxSpeedTime=loactionTime;
			} else if (distance_calculated < minDistance) {
				minDistance = distance_calculated;
				position = i;
			}

		

			if (i == 0) {
				/*if (SpeedcCheck > MaxSpeed
						&& (loactionTime > dayStartWorkTime + 41400 &&
								loactionTime < dayStartWorkTime + 55800))
					MaxSpeed = SpeedcCheck;
				else*/ if (SpeedcCheck > MaxSpeed
						&& (loactionTime > startTime &&
								loactionTime < endTime))
					{
					MaxSpeed = SpeedcCheck;
					MaxSpeedTime=loactionTime;
					}

			} else if (i == 1) {
			/*	if (SpeedcCheck > MaxSpeed
						&& (loactionTime > dayStartWorkTime + 41400 
								&& loactionTime < dayStartWorkTime + 55800)
						)
					MaxSpeed = SpeedcCheck;
				else*/ if (SpeedcCheck > MaxSpeed
						&& (loactionTime >startTime
								&& loactionTime < endTime)
						)
					{
					MaxSpeed = SpeedcCheck;
					MaxSpeedTime=loactionTime;
					}

			} else if (i > 10) {
				/*if (SpeedcCheck > MaxSpeed
						&& (loactionTime > dayStartWorkTime + 41400 
								&& loactionTime < dayStartWorkTime + 55800)
								
						&& (int) location_list.get(i - 1).get("speed") > 8
						&& (int) location_list.get(i - 2).get("speed") > 8
						&& (int) location_list.get(i - 3).get("speed") > 8
						&& (int) location_list.get(i - 4).get("speed") > 8
						&& (int) location_list.get(i - 5).get("speed") > 8
						&& (int) location_list.get(i - 6).get("speed") > 8
						&& (int) location_list.get(i - 7).get("speed") > 8
						&& (int) location_list.get(i - 8).get("speed") > 8
						&& (int) location_list.get(i - 9).get("speed") > 8
						&& (int) location_list.get(i - 10).get("speed") > 8	
						)
					MaxSpeed = SpeedcCheck;
				else*/
				
				if (SpeedcCheck > MaxSpeed
						&& (loactionTime > startTime 
								&& loactionTime < endTime)
						&& (int) location_list.get(i - 1).get("speed") > 8
						&& (int) location_list.get(i - 2).get("speed") > 8
						&& (int) location_list.get(i - 3).get("speed") > 8
						&& (int) location_list.get(i - 4).get("speed") > 8
						&& (int) location_list.get(i - 5).get("speed") > 8
						&& (int) location_list.get(i - 6).get("speed") > 8
						&& (int) location_list.get(i - 7).get("speed") > 8
						&& (int) location_list.get(i - 8).get("speed") > 8
						&& (int) location_list.get(i - 9).get("speed") > 8
						&& (int) location_list.get(i - 10).get("speed") > 8	)
					{
					MaxSpeed = SpeedcCheck;
					MaxSpeedTime=loactionTime;
					}
				
//				if (SpeedcCheck<8&&SpeedcCheck > MaxSpeedCopy
//						&& (loactionTime > dayStartWorkTime + 41400 && loactionTime < dayStartWorkTime + 55800))
//					MaxSpeedCopy = SpeedcCheck;
//				else 
				if (SpeedcCheck<8&&SpeedcCheck > MaxSpeedCopy
						&& (loactionTime > startTime && loactionTime < endTime))
					MaxSpeedCopy = SpeedcCheck;

				
			}

			TotalSpeed = TotalSpeed + SpeedcCheck;
			speedbuild.append(SpeedcCheck + "").append(",");

			// if (event.equalsIgnoreCase("end"))
			// System.err.println(i+"-----------speed--"+SpeedcCheck+"--max-"+MaxSpeed+"---Location time-----"+Common.getDateCurrentTimeZone(loactionTime));

		}
		
		if (MaxSpeed==0) 
			MaxSpeed=MaxSpeedCopy;
		
		 System.err.println("-----------MaxSpeed--"+MaxSpeed+"--MaxSpeedCopy-"+MaxSpeedCopy);

		/*
		 * event+"----locationNearbyKM-----time------"+Common.getDateCurrentTimeZone
		 * (dayStartWorkTime+23400)+
		 * ","+Common.getDateCurrentTimeZone(dayStartWorkTime+37800)
		 * +"---lot2---"+Common.getDateCurrentTimeZone(dayStartWorkTime+41400)
		 * +","+Common.getDateCurrentTimeZone(dayStartWorkTime+55800)+
		 */// System.err.println(event+"----SpeedcCheck----------"+speedbuild.toString());

		// System.err.println("DIstttttttttttttttttt---------"+Total_distance);
		System.out.println(event
				+ "--------"
				+ km
				+ "----Min at position-"
				+ position
				+ "--dist--"
				+ minDistance
				+ "===time=="
				+ Common.getDateCurrentTimeZone(((BasicDBObject) location_list
						.get(position)).getLong("timestamp")));

		int meterConversion = 1609;
		BasicDBObject locobj2 = (BasicDBObject) location_list.get(position)
				.get("location");
		PoleNearByLocationDto dto = new PoleNearByLocationDto();
		dto.setLang(locobj2.getDouble("lon"));
		dto.setLat(locobj2.getDouble("lat"));
		dto.setMinDistance((minDistance));
		dto.setSpeed((int) location_list.get(position).get("speed"));
		dto.setAvgSpeed(TotalSpeed / location_list.size());
		dto.setMaxSpeed(MaxSpeed);
		dto.setMaxSpeedTime(MaxSpeedTime);
		dto.setTotalspeed(TotalSpeed);
		if (event.equalsIgnoreCase("start")) {
			if (Total_distance > 1) {
				dto.setStartkmBeatExpected(km);
				if (Math.round(km) <= km) {
					dto.setStartkmBeatActual(km + minDistance);

				} else {
					dto.setStartkmBeatActual(km - minDistance);

				}

				dto.setTotal_distance(Total_distance);
				long time=((BasicDBObject) location_list.get(position))
						.getLong("timestamp");
				if (time>startTime&&time<endTime) {
					dto.setTimestamp(time);

				}else {
					dto.setTimestamp((long) 0);
				}

			} else {
				dto.setStartkmBeatExpected(km);
				dto.setStartkmBeatActual(0);
				dto.setTotal_distance(Total_distance);
				dto.setTimestamp((long) 0);

			}

			// System.out.println(event+"-@@@@@@@@--dto.getStartkmBeatActual()-----"+dto.getStartkmBeatActual()+"---setStartkmBeatExpected-"
			// + dto.getStartkmBeatExpected() );

		} else {
			if (Total_distance > 1) {

				dto.setEndKmBeatExpected(km);
				if (Math.round(km) <= km)
					dto.setEndKmBeatActual(km + minDistance);
				else
					dto.setEndKmBeatActual(km - minDistance);

			
				long time=((BasicDBObject) location_list.get(position))
						.getLong("timestamp");
				
				if (time>startTime&&time<endTime) {
					dto.setTimestamp(time);

				}else {
					dto.setTimestamp((long) 0);
				}

			} else {

				dto.setEndKmBeatExpected(km);
				dto.setEndKmBeatActual(0);
				dto.setTimestamp((long) 0);

			}

			// System.out.println(event+"--@@@@@@@@@-dto.getEndKmBeatActual()-----"+dto.getEndKmBeatActual()+"---getEndKmBeatExpected-"
			// + dto.getEndKmBeatExpected() );

		}

		return dto;
	}

	
	public ArrayList<RailwayKeymanDTO> GetDeviceBeat(Connection con,
			int studentId) {
		ArrayList<RailwayKeymanDTO> raildtp = new ArrayList<RailwayKeymanDTO>();

		
		
		System.err.println("GetDeviceBeat==="+studentId);
		String Photo = "";
		try {
			java.sql.CallableStatement ps= con.prepareCall("{call GetBeatOfDevice(?)}");
			ps.setInt(1,studentId );
			ResultSet rs=ps.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					
					RailwayKeymanDTO beatDto=new RailwayKeymanDTO();
					
					beatDto.setKmStart(rs.getDouble("KmStart"));
					beatDto.setKmEnd(rs.getDouble("KmEnd"));
					beatDto.setSectionName(rs.getString("SectionName").trim());
					Long startTime=rs.getLong("StartTime")+86400;
					Long endTime=rs.getLong("EndTime")+86400;
					if (startTime>=86400)
						startTime=startTime-86400;
					if (endTime>=86400)
						endTime=endTime-86400	;
				
					 
					  int minutesStart = (int) Math.floor(startTime % 3600 / 60);
					    int hoursStart = (int) Math.floor(startTime / 3600);
					    
					    int minutesEnd = (int) Math.floor(endTime % 3600 / 60);
					    int  hoursEnd = (int) Math.floor(endTime / 3600);
					   
						beatDto.setTripStartTime((hoursStart)+":"+(minutesStart));
						beatDto.setTripEndTime((hoursEnd)+":"+(minutesEnd));
						
						System.err.println("setTripStartTime----"+startTime+"--------"+beatDto.getTripStartTime());
						System.err.println("setTripEndTime----"+endTime+"----------"+beatDto.getTripEndTime());

						raildtp.add(beatDto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return raildtp;
	}

}
