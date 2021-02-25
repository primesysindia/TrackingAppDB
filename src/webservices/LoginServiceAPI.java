package webservices;

import java.util.ArrayList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import model.APIController;

import com.google.gson.Gson;
import com.mysql.jdbc.log.Log;

import dto.DeviceDataDTO;
import dto.DevicePaymentInfoDetailsDTO;
import dto.LoginUserDto;
import dto.MessageObject;

@Path("LoginServiceAPI")
public class LoginServiceAPI {

	
	@POST
	@Path("getLoginDetails")
	@Produces("application/json")
	public String getLoginDetails(@FormParam("username") String username,@FormParam("password") String password)
	{
		
		//System.out.println("getLoginDetails-----"+username+"--"+password);
		
		String feeds  = null;
		try 
		{
			LoginUserDto msgData = new LoginUserDto();
			APIController handler= new APIController();
			msgData = handler.getLoginDetails(username,password);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		//System.out.println("getLoginDetails-----"+feeds);

		return feeds;
	}


	@POST
	@Path("getTrackInfo")
	@Produces("application/json")
	public String getTrackInfo(@FormParam("parentId") String parentId)
	{
		
		System.out.println("getTrackInfo-----"+parentId);

		String feeds  = null;
		try 
		{
			ArrayList<DeviceDataDTO> msgData = new ArrayList<DeviceDataDTO>();
			APIController handler= new APIController();
			msgData = handler.getTrackInfo(parentId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	//	System.out.println("getLoginDetails-----"+feeds);

		return feeds;
	}


	@POST
	@Path("getDevicePaymentInfoForUsers")
	@Produces("application/json")
	public String getDevicePaymentInfoForUsers(@FormParam("userId") String userId)
	{
		
		System.out.println("getDevicePaymentInfoForUsers-----"+userId);

		String feeds  = null;
		try 
		{
			ArrayList<DevicePaymentInfoDetailsDTO> msgData = new ArrayList<DevicePaymentInfoDetailsDTO>();
			APIController handler= new APIController();
			msgData = handler.getDevicePaymentInfoForUsers(userId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
	
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		//System.out.println("getLoginDetails-----"+feeds);

		return feeds;
	}

	/* Code added by Bhavana */
	@POST
	@Path("SendFeedback")
	@Produces("application/json")
	public String SendFeedback(@FormParam("emailId") String email,@FormParam("satisfy") String satisfy,@FormParam("usage") String usage,
			@FormParam("aspect") String aspect,@FormParam("compare") String compare,@FormParam("like") String like,@FormParam("suggestion") String suggestion)		
	{		
		String feeds  = null;
		try 
		{
			MessageObject msgData = null;
			APIController handler= new APIController();
			msgData = handler.SendFeedback(email,satisfy,usage,aspect,compare,like,suggestion);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);

		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return feeds;
	}	
	/* end code added by Bhavana */
	
}
