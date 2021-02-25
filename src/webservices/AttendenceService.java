package webservices;

import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONArray;

import model.APIController;

import com.google.gson.Gson;

import dto.FrdlistDTO;
import dto.LocationDTO;
import dto.MessageObject;
import dto.PersonDetails;
import dto.StationaryDataDTO;
import dto.StationaryPurchaseItemInfo;
import dto.StationarySpinnerDTO;
import dto.StudentAttenInfo;
import dto.classDTO;

@Path("AttendenceService")
public class AttendenceService {

	@POST
	@Path("Getclassofteacher")
	@Produces("application/json")
	public String Getclassofteacher(@FormParam("TeacherId") String teacherid)
	{
		String feeds  = null;
		try 
		{

			ArrayList<classDTO> msgData = null;
			APIController handler= new APIController();
			msgData = handler.Getclassofteacher(teacherid);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			APIController.SaveApiReport("StudentAttendence", "Get ClassID For TeacherAPP",  "Getclassofteacher",teacherid,
					"Get ClassID For TeacherAPP ",  feeds,  "Java","null", "null");
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}


		@POST
		@Path("GetstudentOfTeacherApp")
		@Produces("application/json")
		public String GetstudentOfTeacherApp(@FormParam("ClassId") String classid,
				@FormParam("SchoolId") String schoolid)
		{
			String feeds  = null;
			try 
			{

				ArrayList<StudentAttenInfo> msgData = null;
				APIController handler= new APIController();
				msgData = handler.GetstudentOfTeacherApp(classid,schoolid);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				APIController.SaveApiReport("StudentAttendence", "GetstudentOfTeacherApp",  "GetStationatydata",classid,
						"Get Students list Of Teacher class  data ",  feeds,  "Java","null", "null");
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}

			return feeds;
		}

	/*

		@POST
		@Path("GetItemforCustomer")
		@Produces("application/json")
		public String GetItemforCustomer(@FormParam("CatId") String cat_id,
				@FormParam("SubCatId") String subcat_id,@FormParam("MesureId") String mesure_id,
				@FormParam("DimensionId") String dimen_id,@FormParam("ColorId") String color_id)
		{
			String feeds  = null;
			try 
			{

				StationaryPurchaseItemInfo msgData = null;
				APIController handler= new APIController();
				msgData = handler.GetItemforCustomer(cat_id, subcat_id,mesure_id,dimen_id,color_id);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				APIController.SaveApiReport("StudentAttendence", "GetStationatydata",  "GetStationatydata",userid,
						"Get Stationaty history data ",  feeds,  "Java","null", "null");
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}

			return feeds;
		}
		

		@POST
		@Path("SaveStationaryPayment")
		@Produces("application/json")
		public String SaveStationaryPayment(@FormParam("UserID") String userid,
				@FormParam("Total_Transaction_amount") String totalamount,@FormParam("Payment_mode") String paymentmode,
				@FormParam("Payment_status") String paymentstatus,@FormParam("Payment_Transaction_Id") String orderid,
				@FormParam("ItemTypeID") String itemtypeid,
				@FormParam("Quantity") String quantity,@FormParam("ItemAmount") String itemamount)		{

			String feeds  = null;
			try 
			{

				MessageObject msgData = null;
				APIController handler= new APIController();
				msgData = handler.SaveStationaryPayment(userid, totalamount,paymentmode,paymentstatus,orderid,itemtypeid,quantity,itemamount);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				APIController.SaveApiReport("StudentAttendence", "GetStationatydata",  "GetStationatydata",userid,
						"Get Stationaty history data ",  feeds,  "Java","null", "null");
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}

			return feeds;
		}
		@POST
		@Path("GetStationatydata")
		@Produces("application/json")
		public String GetStationatydata(@FormParam("UserId") String userid)		{

			String feeds  = null;
			try 
			{

				ArrayList<StationaryDataDTO> msgData = null;
				APIController handler= new APIController();
				msgData = handler.GetStationatydata(userid);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				APIController.SaveApiReport("StudentAttendence", "GetStationatydata",  "GetStationatydata",userid,
						"Get Stationaty history data ",  feeds,  "Java","null", "null");
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}

			return feeds;
		}*/
		
		@POST
		@Path("SaveAttendencetoServer")
		@Produces("application/json")
		public String SaveAttendencetoServer(@FormParam("Attendencelist") JSONArray Attendencelist,
				@FormParam("TeacherId") String teacherid,@FormParam("SchoolId") String schoolid,
				@FormParam("ClassId") String classid)		{

			String feeds  = null;
			try 
			{
				

				MessageObject msgData = null;
				APIController handler= new APIController();
				msgData = handler.SaveAttendencetoServer(Attendencelist,teacherid,schoolid,classid);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				APIController.SaveApiReport("StudentAttendence", "SaveAttendencetoServer",  "SaveAttendencetoServer",teacherid,
						"Save Attendence to Server",  feeds,  "Java","null", "null");
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}

			return feeds;
		}
		
		
		
		
}
