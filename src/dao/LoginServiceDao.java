package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import Utility.SendEmail;

import com.google.gson.Gson;

import dto.ApplicationMetaData;
import dto.AssociatedParentDTO;
import dto.DeviceDataDTO;
import dto.DevicePaymentInfoDetailsDTO;
import dto.HistoryDTO;
import dto.LoginUserDto;
import dto.MessageObject;

public class LoginServiceDao {

	public LoginUserDto getLoginDetails(Connection con, String username,
			String password) {
		LoginUserDto	mObj=new LoginUserDto();
	

		try{				
			CallableStatement ps= con.prepareCall("{call CheckValidUser(?,?,?,?)}");
	
				ps.setString(1,username);
				ps.setString(2,password);
				ps.setString(3,"5jj");
				ps.setString(4,username);			
			  	boolean gotResults = ps.execute();
				ResultSet rs = null;
			 if(!gotResults){
				   System.out.println("No results returned");
					mObj.setError(true);
					mObj.setMessage("Please check your credentials");
				   
				} else {
				   rs = ps.getResultSet();
				   
					   if(rs!=null && rs.next()){
							mObj.setError(false);
							mObj.setMessage("Authenticate successfully");
							
							
							mObj.setRoleId(rs.getInt("Role_ID"));
							mObj.setUsrId(rs.getInt("UserID"));
							mObj.setFenceAllow(rs.getInt("FenceAllow"));
							mObj.setFlag(rs.getInt("Flag"));
							mObj.setSocketPort(rs.getInt("SocketPort"));
							mObj.setAccReportAllow(rs.getInt("ACCReportAllow"));
							mObj.setAccSqliteEnable(rs.getInt("ACCSqliteEnable"));
							mObj.setAccSMSDeleteNo(rs.getInt("ACCSMSDeleteNo"));
							mObj.setAccSmsDeleteCheckCount(rs.getInt("ACCSmsDeleteCheckCount"));
							mObj.setMarkerTimeDIff(rs.getInt("MarkerTimeDIff"));
							mObj.setPolylineDistLimit(rs.getInt("PolylineDistLimit"));
							mObj.setWrongwayTolerance(rs.getInt("WrongwayTolerance"));
							mObj.setDeviceStatusReqTime(rs.getInt("DeviceStatusReqTime"));
							mObj.setLocationPushInterval(rs.getInt("LocationPushInterval"));
							mObj.setSosAllowNo(rs.getInt("SOSAllowNo"));
							mObj.setVtsFuncAllow(rs.getInt("VtsFuncAllow"));
							mObj.setVtsSmsAllow(rs.getInt("VtsSmsAllow"));
							mObj.setSosBtnCount(rs.getInt("SOSBtnCount"));
							mObj.setSosBtnTimeSpan(rs.getInt("SOSBtnTimeSpan"));
							mObj.setTrackReqTimeout(rs.getInt("TrackReqTimeout"));
							mObj.setPlatformRenewalStatus(rs.getInt("PlatformRenewalStatus"));
							mObj.setWebUserID(rs.getInt("WebUserID"));
							mObj.setStudentCount(rs.getInt("Student_Count"));
							mObj.setSchoolId(rs.getInt("school_id"));

							mObj.setRegistrationTypeWeb(rs.getString("RegistrationType"));
							mObj.setSocketUrl(rs.getString("SocketUrl"));
							mObj.setUserName(rs.getString("userName"));
							mObj.setClassName(rs.getString("ClassName"));
							mObj.setUserType(rs.getString("userType"));
							mObj.setEmailID(rs.getString("EmailID"));
							mObj.setPhoto(rs.getString("Photo"));
							mObj.setDistUnit(rs.getString("dist_unit"));
							mObj.setMobileNo(rs.getString("MobileNo"));

	
			
					}else {
						mObj.setError(true);
						mObj.setMessage("Please check your credentials");
					}
			
			}
			
		}catch(Exception e){
			e.printStackTrace();
			mObj.setError(true);
			mObj.setMessage("Please check your credentials");
		}
		return mObj;
	}
	

	public ArrayList<DeviceDataDTO> getTrackInfo(Connection con, String parentId) {
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
					AllDeviceDataList.add(dto);
					
				
					
					
				}
			}
		}catch(Exception e){

			e.printStackTrace();
		}
		
		return AllDeviceDataList;
	}


	public ArrayList<DevicePaymentInfoDetailsDTO> getDevicePaymentInfoForUsers(
			Connection con, String userId) {
ArrayList<DevicePaymentInfoDetailsDTO> AllDeviceDataList=new ArrayList<>();
		
		try{
			java.sql.CallableStatement ps= con.prepareCall("{call GetDevicePaymentInfoForUsers(?)}");
			ps.setInt(1,Integer.parseInt(userId ));
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
	
	//code added by Bhavana
		public MessageObject SendFeedback(String email,String satisfy,String usage,String aspect,String compare,String like,String suggestion)
		{
			MessageObject msgobj = new MessageObject();

			try {
				SendEmail se = new SendEmail();
				Boolean sent = se.SendFeedbackEmail(email,satisfy,usage,aspect,compare,like,suggestion);
				if(sent) {
					msgobj.setMessage("Feedback submitted");
					msgobj.setError("false");
				}
				else {
					msgobj.setMessage("Feedback not submitted");
					msgobj.setError("true");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			return msgobj;

		}//end code added by Bhavana

}
