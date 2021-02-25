package webservices;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.gson.Gson;

import dto.AddNewEmpDTO;
import dto.DeviceDataDTO;
import dto.DriverEmpTaskSheduledDTO;
import dto.DriverEmployeeTaskDetailsDTO;
import dto.GetEmployeeTaskManagementDto;
import dto.MessageObject;
import dto.TaskMgntAddressDTO;
import model.APIController;
//code added by Bhavana
@Path("TaskManagementService")
public class TaskManagementService {
	@POST
	@Path("AddNewEmployee")
	@Produces("application/json")
	public String AddNewEmployee(@FormParam("empName") String empName,@FormParam("mobileNo") String mobileNo,@FormParam("emailId") String email,
			@FormParam("address") String address, @FormParam("gender") String gender,@FormParam("webUserId") int webUserId,
			@FormParam("studentId") int employeeId,@FormParam("flag") String flag, @FormParam("parentId") String parentId) {
		String response =null;
		Random random =new Random();
		System.out.println(webUserId+"******************");
		AddNewEmpDTO newEmpData = new AddNewEmpDTO();
		newEmpData.setEmpName(empName.trim());
		newEmpData.setMobileNo(mobileNo);
		newEmpData.setEmail(email.trim());
		newEmpData.setAddress(address.trim());
		newEmpData.setGender(gender);
		newEmpData.setUserId(Integer.parseInt(parentId));
		newEmpData.setCreatedBy(webUserId);
		newEmpData.setEmployeeId(employeeId);
		String id = String.format("%04d", random.nextInt(10000));//to set password
		newEmpData.setPassword(empName.trim().substring(0, 4)+id); //password: employee name 1st 4 chars + 4 random digits
		newEmpData.setUpdateDt(new Date(System.currentTimeMillis()));
		newEmpData.setFlag(Integer.parseInt(flag));
		try 
		{
			MessageObject msgData = null;
			APIController handler= new APIController();
			msgData = handler.SaveNewEmp(newEmpData);
			Gson gson = new Gson();
			response = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		System.out.println("Add EMp-response--"+response);

		return response;
	}
	//end code added by Bhavana	
	
	@POST
	@Path("GetEmployeeDetails")
	@Produces("application/json")
	public String GetEmployeeDetails(@FormParam("UserId") int userId) {
		String result = null;
		try {
			System.out.println("GetEmployeeDetails------------"+userId);

			ArrayList<GetEmployeeTaskManagementDto> empData = new ArrayList<GetEmployeeTaskManagementDto>();
			APIController handler= new APIController();
			empData = handler.GetEmployeeDetails(userId);
			Gson gson = new Gson();
			result = gson.toJson(empData);
		}catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return result;
	}
	
	

	//Save new Address in TaskMgntAddressMaster for allocate task to emp
		@POST
		@Path("saveAddressOfEmpTask")
		@Produces("application/json")
		public String saveAddressOfEmpTask(@FormParam("address_long") String address_long,@FormParam("address_short") String address_short,
				@FormParam("lat") Double lat,
				@FormParam("lan") Double lan,@FormParam("parentId") int parentId, @FormParam("isDefault") int isDefault, @FormParam("address_id") int address_id) {
			String response =null;
			
TaskMgntAddressDTO newEmpData = new TaskMgntAddressDTO();
			newEmpData.setAddress_long(address_long.trim());
			newEmpData.setAddress_short(address_short);
			newEmpData.setIsDefault(isDefault);
			newEmpData.setLan(lan);
			newEmpData.setLat(lat);
			newEmpData.setAddress_id(address_id);
			newEmpData.setParentId(parentId);
	
			try 
			{
				MessageObject msgData = null;
				APIController handler= new APIController();
				msgData = handler.saveAddressOfEmpTask(newEmpData);
				Gson gson = new Gson();
				response = gson.toJson(msgData);
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			System.out.println("response----"+response);

			return response;	
		
	}
		@POST
		@Path("GetAddressOfEmpTask")
		@Produces("application/json")
		public String GetAddressOfEmpTask(@FormParam("parentId") int parentId) {
			String result = null;
			try {
				ArrayList<TaskMgntAddressDTO> empData = new ArrayList<TaskMgntAddressDTO>();
				APIController handler= new APIController();
				empData = handler.GetAddressOfEmpTask(parentId);
				Gson gson = new Gson();
				result = gson.toJson(empData);
			}catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			return result;
		}
		
		@POST
		@Path("deleteAddressOfEmpTask")
		@Produces("application/json")
		public String DeleteAddressOfEmpTask(@FormParam("address_id") int addressId) {
			String result = null;
			try {
				MessageObject msgData = null;
				APIController handler= new APIController();
				msgData = handler.DeleteAddressOfEmpTask(addressId);
				Gson gson = new Gson();
				result = gson.toJson(msgData);
			}catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			return result;
		}
		
		//Save new Address in TaskMgntAddressMaster for allocate task to emp
		@POST
		@Path("saveDriverEmployeeTask")
		@Produces("application/json")
		public String saveDriverEmployeeTask(@FormParam("taskId") int taskId,
				@FormParam("driverId") int driverId,
				@FormParam("studentId_Carid") int studentId_Carid,@FormParam("endAddressId") int endAddressId,
				@FormParam("StartAddressId") int startAddressId,@FormParam("parentId") int parentId, 
				@FormParam("startTime") long startTime, @FormParam("endTime") long endTime ) {
			String response =null;
			DriverEmployeeTaskDetailsDTO newEmpData = new DriverEmployeeTaskDetailsDTO();
			newEmpData.setTaskId(taskId);
			newEmpData.setDriverId(driverId);
			newEmpData.setEndAddressId(endAddressId);
			newEmpData.setEndTime(endTime);
			newEmpData.setStartAddressId(startAddressId);
			newEmpData.setStartTime(startTime);
			newEmpData.setStudentId_Carid(studentId_Carid);
			newEmpData.setParentId(parentId);
		
	
			try 
			{
				MessageObject msgData = null;
				APIController handler= new APIController();
				msgData = handler.saveDriverEmployeeTask(newEmpData);
				Gson gson = new Gson();
				response = gson.toJson(msgData);
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			System.out.println("saveDriverEmployeeTask---response----"+response);

			return response;	
		
	}
		
		@POST
		@Path("GetEmployeeTask")
		@Produces("application/json")
		public String GetEmployeeTask(@FormParam("driver_id") int driver_id,
				@FormParam("start_time") long startTime,@FormParam("end_time") long endTime) {
			String result = null;
			try {
				ArrayList<DriverEmployeeTaskDetailsDTO> empData = new ArrayList<DriverEmployeeTaskDetailsDTO>();
				APIController handler= new APIController();
				empData = handler.GetEmployeeTask(driver_id,startTime,endTime);
				Gson gson = new Gson();
				result = gson.toJson(empData);
			}catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			return result;
		}
		
		@POST
		@Path("deleteEmployee")
		@Produces("application/json")
		public String deleteEmployee(@FormParam("empId") int empId) {
			String result = null;
			try {
				MessageObject msgData = null;
				APIController handler= new APIController();
				msgData = handler.deleteEmployee(empId);
				Gson gson = new Gson();
				result = gson.toJson(msgData);
			}catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			System.out.println("Delete EMp-response--"+result);

			return result;
		}
		
		
		@POST
		@Path("copyEmployeeTask")
		@Produces("application/json")
		public String copyEmployeeTask(@FormParam("task_ids") String taskId,@FormParam("parentId") int parentId) {
			String result = null;
			try {
				MessageObject msgData = null;
				APIController handler= new APIController();
				msgData = handler.copyEmployeeTask(taskId,parentId) ;
				Gson gson = new Gson();
				result = gson.toJson(msgData);
			}catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			return result;
		}
		
		
		
		
}
