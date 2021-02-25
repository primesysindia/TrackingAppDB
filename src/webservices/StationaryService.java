package webservices;

import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import model.APIController;

import com.google.gson.Gson;

import dto.FrdlistDTO;
import dto.LocationDTO;
import dto.MessageObject;
import dto.PersonDetails;
import dto.StationaryDataDTO;
import dto.StationaryPurchaseItemInfo;
import dto.StationarySpinnerDTO;

@Path("StationaryService")
public class StationaryService {

	//login service
	@POST
	@Path("GetSpinnerdata")
	@Produces("application/json")
	public String GetStationarySpinnerdata(@FormParam("Flag") String flag,@FormParam("SchoolId") String Schoolid,@FormParam("SelectedId") String Id)
	{
		String feeds  = null;
		try 
		{

			ArrayList<StationarySpinnerDTO> msgData = null;
			APIController handler= new APIController();
			msgData = handler.GetStationarySpinnerdata(flag, Id,Schoolid);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

			APIController.SaveApiReport("Stationary", "Get data to fill up spinner",  "GetSpinnerdata",Id,
					"Get data to fill up spinner ",  feeds,  "Java","null", "null");
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}


		@POST
		@Path("GetItemforSubject")
		@Produces("application/json")
		public String GetItemforSubject(@FormParam("CatId") String cat_id,
				@FormParam("SubCatId") String subcat_id,@FormParam("ClassId") String class_id,
				@FormParam("SubjectId") String subject_id)
		{
			String feeds  = null;
			try 
			{

				StationaryPurchaseItemInfo msgData = null;
				APIController handler= new APIController();
				msgData = handler.GetItemforSubject(cat_id, subcat_id,class_id,subject_id);
				Gson gson = new Gson();
				feeds = gson.toJson(msgData);
				
				APIController.SaveApiReport("Stationary", "GetItemforSubject",  "GetItemforSubject",class_id,
						"Get Item for  selected Subject ",  feeds,  "Java","null", "null");
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}

			return feeds;
		}

	

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

				APIController.SaveApiReport("Stationary", "Get Item for Customer",  "GetItemforCustomer",cat_id,
						"Get Item which meetes all spinner condition in available qty ",  feeds,  "Java","null", "null");
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

				APIController.SaveApiReport("Stationary", "SaveStationaryPayment",  "SaveStationaryPayment",userid,
						"Save Stationary Payment",  feeds,  "Java","null", "null");
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

				APIController.SaveApiReport("Stationary", "GetStationatydata",  "GetStationatydata",userid,
						"Get Stationaty history data ",  feeds,  "Java","null", "null");
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}

			return feeds;
		}
		
		
}
