package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import dto.AddNewEmpDTO;
import dto.DeviceDataDTO;
import dto.DriverEmployeeTaskDetailsDTO;
import dto.GetEmployeeTaskManagementDto;
import dto.MessageObject;
import dto.TaskMgntAddressDTO;

public class TaskManagementDAO {
	//code added by Bhavana
	public MessageObject saveEmpDetails(Connection con,AddNewEmpDTO empDetail) {
		MessageObject msgobj = new MessageObject();
		try {
			System.out.println(empDetail.getCreatedBy());
			int result = 0;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call SaveTrackUserNewForUs(?,?,?,?,?,?,?,?,?,?,?,?)}");
			stmt.setInt(1, empDetail.getUserId());
			stmt.setString(2,empDetail.getEmpName());
			stmt.setString(3,empDetail.getGender());
			stmt.setString(4, empDetail.getMobileNo());
			stmt.setString(5, empDetail.getEmail());
			stmt.setString(6, empDetail.getAddress());
			stmt.setInt(7, empDetail.getCreatedBy());
			stmt.setDate(8, empDetail.getUpdateDt());
			stmt.setString(9, empDetail.getPassword());
			stmt.setInt(10, empDetail.roleId);
			stmt.setInt(11, empDetail.getFlag());
			stmt.setInt(12, empDetail.getEmployeeId());
			//result = stmt.executeUpdate();
			ResultSet rs=stmt.executeQuery();

			if (rs!=null) {
				while (rs.next()) {
					result=rs.getInt(1);
			if (result == 0) {
				msgobj.setError("true");
				msgobj.setMessage("employee not added");
				msgobj.setId("0");

			} else if (result == 1) {
				// //System.err.println("Error=="+result);
				msgobj.setId("1");

				msgobj.setError("false");
				msgobj.setMessage("employee added successfully");
			}else if (result == 2) {
				// //System.err.println("Error=="+result);
				msgobj.setError("false");
				msgobj.setId("2");

				msgobj.setMessage("employee updated successfully");
			}else if (result == 3) {
				// //System.err.println("Error=="+result);
				msgobj.setError("true");
				msgobj.setId("3");
				msgobj.setMessage("employee username alredy exist.");
			}
				}
			}
		} catch (Exception e) {
			msgobj.setError("true");
			msgobj.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}
		return msgobj;
	}
	
	public ArrayList<GetEmployeeTaskManagementDto> GetEmployeeDetails(Connection con,int parentId){
		ArrayList<GetEmployeeTaskManagementDto> empData = new ArrayList<GetEmployeeTaskManagementDto>();
		try{
			java.sql.CallableStatement ps= con.prepareCall("{call GetTrackUserDetailsForUS(?)}");
			ps.setString(1,parentId+"");
			ResultSet rs=ps.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					GetEmployeeTaskManagementDto dto=new GetEmployeeTaskManagementDto();
					dto.setParentId(rs.getInt("ParentID"));
					dto.setEmployeeId(rs.getInt("EmployeeID"));
					dto.setRoleId(rs.getInt("Role_ID"));
					dto.setMobileNo(rs.getString("MobileNo"));
					dto.setFlag(rs.getInt("Flag"));
					dto.setName(rs.getString("Name"));
					dto.setGender(rs.getString("Gender"));
					dto.setEmailId(rs.getString("EmailID"));
					dto.setAddress(rs.getString("Address"));
					dto.setUserName(rs.getString("UserName"));
					empData.add(dto);
				}
			}
		}catch(Exception e){

			e.printStackTrace();
		}
		return empData;
	}
	
	//end code added by Bhavana
	
	
	//Save new Address in TaskMgntAddressMaster for allocate task to emp
	public MessageObject saveAddressOfEmpTask(Connection con,TaskMgntAddressDTO newEmpData) {
		MessageObject msgobj = new MessageObject();
		try {

			int result = 0;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call saveAddressOfEmpTask(?,?,?,?,?,?,?)}");
			stmt.setString(1, newEmpData.getAddress_long());
			stmt.setString(2,newEmpData.getAddress_short());
			stmt.setDouble(4,newEmpData.getLan());
			stmt.setDouble(3, newEmpData.getLat());
			stmt.setInt(5, newEmpData.getParentId());
			stmt.setInt(6, newEmpData.getIsDefault());
			stmt.setInt(7, newEmpData.getAddress_id());

			result = stmt.executeUpdate();

			if (result == 0) {
				msgobj.setError("true");
				msgobj.setMessage("Address is not Upadted ");
			} else {
				// //System.err.println("Error=="+result);
				msgobj.setError("false");
				msgobj.setMessage(" Address Upadted successfully");
			}
		} catch (Exception e) {
			msgobj.setError("true");
			msgobj.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}
		return msgobj;
	}

	public ArrayList<TaskMgntAddressDTO> GetAddressOfEmpTask(Connection con,
			int parentId) {
		ArrayList<TaskMgntAddressDTO> empData = new ArrayList<TaskMgntAddressDTO>();
		try{
			java.sql.CallableStatement ps= con.prepareCall("{call GetAddressOfEmpTask(?)}");
			ps.setString(1,parentId+"");
			ResultSet rs=ps.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					TaskMgntAddressDTO dto=new TaskMgntAddressDTO();
					dto.setParentId(rs.getInt("ParentID"));
					dto.setAddress_id(rs.getInt("id"));
					dto.setAddress_long(rs.getString("address_long"));
					dto.setAddress_short(rs.getString("address_short"));
					dto.setIsDefault(rs.getInt("isDefault"));
					dto.setLan(rs.getDouble("lan"));
					dto.setLat(rs.getDouble("lat"));
					empData.add(dto);
				}
			}
		}catch(Exception e){

			e.printStackTrace();
		}
		return empData;
	}
	

	

	public MessageObject DeleteAddressOfEmpTask(Connection con, int addressId) {


		MessageObject msgobj = new MessageObject();
		try {

			int result = 0;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call DeleteAddressOfEmpTask(?)}");
			stmt.setInt(1, addressId);

			result = stmt.executeUpdate();

			if (result == 0) {
				msgobj.setError("true");
				msgobj.setMessage("Address is not Deleted ");
			} else {
				// //System.err.println("Error=="+result);
				msgobj.setError("false");
				msgobj.setMessage(" Address Deleted successfully");
			}
		} catch (Exception e) {
			msgobj.setError("true");
			msgobj.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}
		return msgobj;
	}

	public MessageObject saveDriverEmployeeTask(Connection con,
			DriverEmployeeTaskDetailsDTO data) {
		MessageObject msgobj = new MessageObject();
		try {

			int result = 0;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call saveDriverEmployeeTask(?,?,?,?,?,?,?,?)}");
			stmt.setInt(1, data.getDriverId());
			stmt.setInt(2,data.getEndAddressId());
			stmt.setLong(3,data.getEndTime());
			stmt.setInt(4, data.getStartAddressId());
			stmt.setInt(5, data.getStudentId_Carid());
			stmt.setInt(6, data.getTaskId());
			stmt.setLong(7, data.getStartTime());
			stmt.setInt(8, data.getParentId());

			//result = stmt.executeUpdate();
			ResultSet rs=stmt.executeQuery();

			if (rs!=null) {
				while (rs.next()) {
					result=rs.getInt(1);
			if (result == 0) {
				msgobj.setError("true");
				msgobj.setMessage("employee task not added");
				msgobj.setId("0");

			} else if (result == 1) {
				// //System.err.println("Error=="+result);
				msgobj.setId("1");

				msgobj.setError("false");
				msgobj.setMessage("employee task added successfully");
			}else if (result == 2) {
				// //System.err.println("Error=="+result);
				msgobj.setError("false");
				msgobj.setId("2");

				msgobj.setMessage("employee task updated successfully");
			}else if (result == 3) {
				// //System.err.println("Error=="+result);
				msgobj.setError("true");
				msgobj.setId("3");
				msgobj.setMessage("employee task  alredy allocated to this time.");
			}
				
				}
			}
		} catch (Exception e) {
			msgobj.setError("true");
			msgobj.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}

		return msgobj;
	}

	public ArrayList<DriverEmployeeTaskDetailsDTO> GetEmployeeTask(
			Connection con, int driver_id, long startTime, long endTime) {
		ArrayList<DriverEmployeeTaskDetailsDTO> empData = new ArrayList<DriverEmployeeTaskDetailsDTO>();
		try{
			System.err.println("GetEmployeeTask-----"+driver_id+"--"+startTime+"---"+endTime);

			java.sql.CallableStatement ps= con.prepareCall("{call GetEmployeeTask(?,?,?)}");
			ps.setInt(1,driver_id);
			ps.setLong(2, startTime);
			ps.setLong(3, endTime);
			ResultSet rs=ps.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
					DriverEmployeeTaskDetailsDTO dto=new DriverEmployeeTaskDetailsDTO();
					dto.setParentId(rs.getInt("ParentId"));
					dto.setDriverId(rs.getInt("DriverId"));
					dto.setEndAddress(rs.getString("endAddress_short"));
					dto.setEndAddressId(rs.getInt("EndAddressId"));
					dto.setEndTime(rs.getLong("EndTime"));
					dto.setStartAddress(rs.getString("startAddress_short"));
					dto.setStartAddressId(rs.getInt("StartAddressId"));
					dto.setStartTime(rs.getLong("StartTime"));
					dto.setStudentId_Carid(rs.getInt("StudentId_Carid"));
					dto.setTaskId(rs.getInt("Id"));
					dto.setDeviceName(rs.getString("device_name"));
					dto.setDriverMobNo(rs.getString("driver_mob"));
					dto.setDriverName(rs.getString("driver_name"));

					empData.add(dto);
				}
			}
		}catch(Exception e){

			e.printStackTrace();
		}
		System.err.println("GetEmployeeTask-----"+empData.size());
		return empData;
	}

	public MessageObject deleteEmployeeTask(Connection con, int taskId) {

		MessageObject msgobj = new MessageObject();
		try {

			int result = 0;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call DeleteEmployeeTask(?)}");
			stmt.setInt(1, taskId);

			result = stmt.executeUpdate();

			if (result == 0) {
				msgobj.setError("true");
				msgobj.setMessage("Task is not Deleted ");
			} else {
				// //System.err.println("Error=="+result);
				msgobj.setError("false");
				msgobj.setMessage(" Task Deleted successfully");
			}
		} catch (Exception e) {
			msgobj.setError("true");
			msgobj.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}
		return msgobj;
	}

	public MessageObject copyEmployeeTask(Connection con, String taskId,
			int parentId) {
		System.err.println("copyEmployeeTask-----"+taskId);
		MessageObject msgobj = new MessageObject();
		String [] taskIds=taskId.split(",");
		for(int i=0;i<taskIds.length;i++)
		try {

			int result = 0;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call CopyDriverEmployeeTask(?,?)}");
			stmt.setInt(1, Integer.parseInt(taskIds[i]));
			stmt.setInt(2,parentId);
			

			result = stmt.executeUpdate();

			if (result == 0) {
				msgobj.setError("true");
				msgobj.setMessage("Task is not Copied successfully.");
			} else {
				// //System.err.println("Error=="+result);
				msgobj.setError("false");
				msgobj.setMessage(" Task Copied successfully.");
			}
		} catch (Exception e) {
			msgobj.setError("true");
			msgobj.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}
		return msgobj;
	}
	
	public MessageObject deleteEmployee(Connection con, int taskId) {

		MessageObject msgobj = new MessageObject();
		try {

			int result = 0;
			java.sql.CallableStatement stmt = con
					.prepareCall("{call DeleteEmployee(?)}");
			stmt.setInt(1, taskId);

			result = stmt.executeUpdate();

			if (result == 0) {
				msgobj.setError("true");
				msgobj.setMessage("Employee is not Deleted ");
			} else {
				// //System.err.println("Error=="+result);
				msgobj.setError("false");
				msgobj.setMessage(" Employee Deleted successfully");
			}
		} catch (Exception e) {
			msgobj.setError("true");
			msgobj.setMessage("Error :" + e.getMessage());
			e.printStackTrace();
		}
		return msgobj;
	}
	
}
