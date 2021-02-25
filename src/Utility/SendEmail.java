package Utility;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.codec.binary.Base64;

import dao.Common;
import dto.TripInfoDto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;

import Utility.MyAuthenticator;

public class SendEmail {

	Calendar cal = Calendar.getInstance();
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	String now =dateFormat.format(cal.getTime());
	
	
	public boolean sendEmailActivation(String toemailID,String otp)
	{
		boolean valid=false;
		 final String from = "patil.rupesh4892@gmail.com";
		 String host="192.168.56.1";
		 
		 Properties props =new Properties();
		 props.put("mail.smtp.host", "smtp.gmail.com");  
		  props.put("mail.smtp.socketFactory.port", "465");  
		  props.put("mail.smtp.socketFactory.class",  
		            "javax.net.ssl.SSLSocketFactory");  
		  props.put("mail.smtp.auth", "true");  
		  props.put("mail.smtp.port", "465");  

	      
		  Session session = Session.getDefaultInstance(props,  
				   new javax.mail.Authenticator() {  
				   protected PasswordAuthentication getPasswordAuthentication() {  
				   return new PasswordAuthentication(from,"");//change accordingly  
				   }  
				  });  

	      try{
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO,
	                                  new InternetAddress(toemailID));

	         // Set Subject: header field
	         message.setSubject("kalakhoj ContactUs ");

	         // Now set the actual message
	         message.setText("Your OTP is .....");

	         // Send message
	         Transport.send(message);
	         System.out.println("Sent message successfully....");
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
		return false;
	}
	
	public boolean sendMonthlyEmail(String totalKm,String mailid, String devicename, String startdate, String enddate) {
		
		

		boolean valid=false;
		final String password ="password123";
		final String  from = "contact@primesystrack.com";
		

		Properties props =new Properties();
		props.put("mail.smtp.host", "smtp.zoho.com");  
		/*	props.put("mail.smtp.host", host);  */
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class",  
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  
	//	System.out.println(toemailID+" "+Pass);

		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(from,password);//change accordingly  
			}  
		});  

		try{


			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(mailid));
			// Set Subject: header field
			message.setSubject("Primesys Track (Monthly Mileage Report)");
			// This mail has 2 part, the BODY and the embedded image
			MimeMultipart multipart = new MimeMultipart("related");
			// first part (the html)
			BodyPart messageBodyPart = new MimeBodyPart();
			String template="<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\">"
					+ "<title>Welcome To Primesys Track</title></head><body><div style=\"margin: 10px auto;"
					+ " padding: 15px; border: 1px solid #4eb499; font-family: &#39;serif &#39;,"
					+ " Arial !important; -webkit-font-smoothing: antialiased; font-style: normal; "
					+ "font-weight: 300; font-size: 19px; color: #fff;background: #0097A7;background:"
					+ "-moz-linear-gradient(-45deg, #336799 0%, #336799 100%);background: "
					+ "-webkit-gradient(linear, left top, right bottom, color-stop(0%, #336799),"
					+ " color-stop(100%, #336799));background: -webkit-linear-gradient(-45deg"
					+ ", #4eb499 0%, #4eb499 100%);background: -o-linear-gradient(-45deg, "
					+ "#336799 0%, #336799 100%);background: -ms-linear-gradient(-45deg,"
					+ " #336799 0%, #336799 100%);background: linear-gradient(-45deg,"
					+ " #66cdcc 0%, #336799 100%);filter: progid:DXImageTransform."
					+ "Microsoft.gradient(startColorstr=#336799, endColorstr=#66cdcc,"
					+ " GradientType=1);\"><a href=\"http://www.primesystech.com/\" style=\"float:left;\" "
					+ "target=\"_blank\"></a><br><br><br><br><p style=\"font-size:15px;\">Hi "+mailid+"</p> "
							+ "<p></p> <p style=\"font-size:15px\">"
							+ "Thanks for using to PrimesysTrack  Your monthly mileage report  for  "+ devicename +" is ' "+String.format("%.2f",Double.parseDouble(totalKm))+" ' Km from  " +Common.getDateCurrentTimeZone(Long.parseLong(startdate))+" to "+Common.getDateCurrentTimeZone(Long.parseLong(enddate))
							
							+".</p><p style=\"font-size:15px\">Regards,</p> <p style=\"font-size:20px;\">Primesys Track Team</p>"+
					""+"<p style=\"font-size:15px;\">Enquiry &amp; Support: contact@primesystech.com</p><p style=\"font-size:15px;\">© Primesys Technologies</p><div></div></div></body></html>\"";
			message.setText(template);

			messageBodyPart.setContent(template, "text/html");
			// add it
			multipart.addBodyPart(messageBodyPart);


			message.setContent(multipart);
			// Send message
			Transport.send(message);
			valid=true;


			System.out.println("Sent message successfully....");
		}catch (MessagingException mex) {
			System.out.println("Exception----"+mex.getMessage()+"------");
			valid=false;
			mex.printStackTrace();
		}
		return valid;

		
	}





	public boolean sendSignupEmail(String name, String username, String email,
			String userpassword) {
		

		boolean valid=false;
		final String password ="password123";
		final String  from = "contact@mykiddytracker.com";
		

		Properties props =new Properties();
		props.put("mail.smtp.host", "smtp.zoho.com");  
		/*	props.put("mail.smtp.host", host);  */
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class",  
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  
	//	System.out.println(toemailID+" "+Pass);

		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(from,password);//change accordingly  
			}  
		});  

		try{


			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));
			
			message.setRecipient(Message.RecipientType.BCC, new InternetAddress("pt@primesystech.com"));
			//message.setRecipient(Message.RecipientType.BCC, new InternetAddress("rupesh.p@mykiddytracker.com"));

			
			
			
			// Set Subject: header field
			message.setSubject(name+"!"+" Thanks for Registering to MyKiddyTracker Demo.");
			// This mail has 2 part, the BODY and the embedded image
			MimeMultipart multipart = new MimeMultipart("related");
			// first part (the html)
			BodyPart messageBodyPart = new MimeBodyPart();
			String template="<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\">"
					+ "<title>Welcome To MyKiddyTracker</title></head>"
					+ ""
					+ "<body>"
					+ "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" bgcolor=\"dadada\" style=\"margin: 10px auto; padding: 15px; border: 1px solid #6b396c; font-family: 'serif', Arial !important; -webkit-font-smoothing: antialiased; font-style: normal; font-weight: 300; font-size: 19px; color: #fff;background: #cc6698;background: -moz-linear-gradient(-45deg, #cc6698 0%, #6b396c 100%);background: -webkit-gradient(linear, left top, right bottom, color-stop(0%, #cc6698), color-stop(100%, #6b396c));background: -webkit-linear-gradient(-45deg, #cc6698 0%, #6b396c 100%);background: -o-linear-gradient(-45deg, #cc6698 0%, #6b396c 100%);background: -ms-linear-gradient(-45deg, #cc6698 0%, #6b396c 100%);background: linear-gradient(-45deg, #cc6698 0%, #6b396c 100%);filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#cc6698, endColorstr=#6b396c, GradientType=1);\">"+
					""+
					"    <tr>"+
					"        <td width=\"100%\" align=\"left\">"+
					"            <a href=\"http://mykiddytracker.com\" style=\"float:left;\" target=\"_blank\">"+
					"                <img src=\"http://mykiddytracker.com/img/berry/logo.png\" alt=\"\" class=\"logo\">"+
					"            </a>"+
					"        </td>"+
					"    </tr>"+
					"    <tr>"+
					"        <td width=\"100%\" align=\"left\" style=\"padding:13.5px 27px;border-bottom:1px solid #c7c7c7;\">"+
					"            <p>Hi "+name+" </p>"+
					"            <p>Thanks for registering to MyKiddyTracker App Demo. We have created one Demo Account for you, which will allow you to communicate with your friends and Family Members. Feel free to explore all the options available to you.</p>"+
					"            <p>Use below details to login to the MyKiddyTracker App,</p>"+
					"            <p>Username : "+username+"</p>"+
					"            <p>Password : "+userpassword+"</p>"+
					"            <p>Use Below Link to Download the MyKiddyTracker App.</p>"+
					"        </td>"+
					"    </tr>"+
					""+
					"    <tr>"+
					"        <td width=\"100%\" align=\"left\" style=\"padding:7px 27px;border-bottom:1px solid #c7c7c7;\">"+
					"            <a href=\"https://play.google.com/store/apps/details?id=com.primesys.mitra&hl=en\" target=\"_blank\" style=\"margin-right: 10px;color: #6b396c;border-color: #ffffff;border: 2px solid #ffffff;background: none;background-color: #ffffff;outline: 0;box-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);-webkit-border-radius: 3em; -moz-border-radius: 3em;-ms-border-radius: 3em;-o-border-radius: 3em;border-radius: 3em;-webkit-box-shadow: none;-moz-box-shadow: none;box-shadow: none;padding: 15px 25px;-webkit-animation-duration: 1.3s;animation-duration: 1.3s;-webkit-animation-fill-mode: both;animation-fill-mode: both;display: inline-block;font-weight: normal;text-align: center;vertical-align: middle;white-space: nowrap;font-size: 14px;line-height: 1.42857143;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;text-decoration: none;cursor: pointer;\">"+
					"                Click To Download APK File"+
					"            </a>"+
					"        </td>"+
					"    </tr>"+
					""+
					"    <tr>"+
					"        <td width=\"100%\" align=\"left\" style=\"padding:7px 27px;\">"+
					"            <p>Regards,</p>"+
					"            <p style=\"font-size:20px;\">MyKiddyTracker Team.</p>"+
					"            <p style=\"font-size:12px;\">© <a href=\"www.primesystech.com\">Primesys Technologies</a></p>"+
					"        </td>"+
					"    </tr>"+
					""+
					"    <tr>"+
					"        <td width=\"100%\" height=\"39\"></td>"+
					"    </tr>"+
					"</table>"
					+ "</body>"
							+ ""
							+ ""
							+ "</html>\"";
			message.setText(template);

			messageBodyPart.setContent(template, "text/html");
			// add it
			multipart.addBodyPart(messageBodyPart);


			message.setContent(multipart);
			// Send message
			Transport.send(message);


			System.out.println("Sent message successfully....");
		}catch (MessagingException mex) {
			System.out.println("Exception----"+mex.getMessage()+"------");

			mex.printStackTrace();
		}
		return valid;

	}





	public Boolean SendForgetEmail(String name, String email, String pass, String username) {
		// TODO Auto-generated method stub

		boolean valid=false;
		final String password ="password123";
		final String  from = "contact@mykiddytracker.com";
		

		Properties props =new Properties();
		props.put("mail.smtp.host", "smtp.zoho.com");  
		/*	props.put("mail.smtp.host", host);  */
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class",  
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  
	//	System.out.println(toemailID+" "+Pass);

		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(from,password);//change accordingly  
			}  
		});  

		try{	Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		// Set To: header field of the header.
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));
	//	message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("rupesh.p@mykiddytracker.com"));

		
		// Set Subject: header field
		message.setSubject(name+"!"+" Password recovery for Mykiddytrcaker.");
		// This mail has 2 part, the BODY and the embedded image
		MimeMultipart multipart = new MimeMultipart("related");
		// first part (the html)
		BodyPart messageBodyPart = new MimeBodyPart();
		String template="<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\">"
				+ "<title>Welcome To MyKiddyTracker</title></head>"
				+ ""
				+ "<body link=\"white\" >"
				+"<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" bgcolor=\"dadada\" style=\"margin: 10px auto; padding: 15px; border: 1px solid #6b396c; font-family: 'serif', Arial !important; -webkit-font-smoothing: antialiased; font-style: normal; font-weight: 300; font-size: 19px; color: #fff;background: #cc6698;background: -moz-linear-gradient(-45deg, #cc6698 0%, #6b396c 100%);background: -webkit-gradient(linear, left top, right bottom, color-stop(0%, #cc6698), color-stop(100%, #6b396c));background: -webkit-linear-gradient(-45deg, #cc6698 0%, #6b396c 100%);background: -o-linear-gradient(-45deg, #cc6698 0%, #6b396c 100%);background: -ms-linear-gradient(-45deg, #cc6698 0%, #6b396c 100%);background: linear-gradient(-45deg, #cc6698 0%, #6b396c 100%);filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#cc6698, endColorstr=#6b396c, GradientType=1);\">"+
				""+
				"    <tr>"+
				"        <td width=\"100%\" align=\"left\">"+
				"            <a href=\"http://mykiddytracker.com\" style=\"float:left;\" target=\"_blank\">"+
				"                <img src=\"http://mykiddytracker.com/img/berry/logo.png\" alt=\"\" class=\"logo\">"+
				"            </a>"+
				"        </td>"+
				"    </tr>"+
				""+
				""+
				"    <tr>"+
				"        <td width=\"100%\" align=\"left\" style=\"padding:13.5px 27px;border-bottom:1px solid #c7c7c7;\">"+
				"            <p>Hi "+name+",</p>"+
				"            <p>Thanks for choosing the MyKiddyTracker App to Track you Loved Ones. We hope to provide you the Best Tracking experience you have ever seen.</p>"+
				"            <p>Use below details to login to the MyKiddyTracker App,</p>"+
				"            <p>Username : "+username+"</p>"+
					"            <p>Password : "+pass+"</p>"+
				"            <p>Use Below Link to Download the MyKiddyTracker App.</p>"+
				"        </td>"+
				"    </tr>"+
				""+
				"    <tr>"+
				"        <td width=\"100%\" align=\"left\" style=\"padding:7px 27px;border-bottom:1px solid #c7c7c7;\">"+
				"            <a href=\"https://play.google.com/store/apps/details?id=com.primesys.mitra&hl=en\" target=\"_blank\" style=\"margin-right: 10px;color: #6b396c;border-color: #ffffff;border: 2px solid #ffffff;background: none;background-color: #ffffff;outline: 0;box-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);-webkit-border-radius: 3em; -moz-border-radius: 3em;-ms-border-radius: 3em;-o-border-radius: 3em;border-radius: 3em;-webkit-box-shadow: none;-moz-box-shadow: none;box-shadow: none;padding: 15px 25px;-webkit-animation-duration: 1.3s;animation-duration: 1.3s;-webkit-animation-fill-mode: both;animation-fill-mode: both;display: inline-block;font-weight: normal;text-align: center;vertical-align: middle;white-space: nowrap;font-size: 14px;line-height: 1.42857143;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;text-decoration: none;cursor: pointer;\">"+
				"                Click To Download the MyKiddyTracker App"+
				"            </a>"+
				"        </td>"+
				"    </tr>"+
				""+
				"    <tr>"+
				"        <td width=\"100%\" align=\"left\" style=\"padding:7px 27px;\">"+
				"            <p>Regards,</p>"+
				"            <p style=\"font-size:20px;\">MyKiddyTracker Team.</p>"+
				"            <p style=\"font-size:12px;\">© <a href=\"www.primesystech.com\">Primesys Technologies</a></p>"+
				"        </td>"+
				"    </tr>"+
				"    <tr>"+
				"        <td width=\"100%\" height=\"39\"></td>"+
				"    </tr>"+
				"</table>"+
			 "</body>"
						+ ""
						+ ""
						+ "</html>\"";
		message.setText(template);

		messageBodyPart.setContent(template, "text/html");
		// add it
		multipart.addBodyPart(messageBodyPart);


		message.setContent(multipart);
		// Send message
		Transport.send(message);


		System.out.println("Sent message successfully....");
		
		
		}catch (MessagingException mex) {
			System.out.println("Exception----"+mex.getMessage()+"------");

			mex.printStackTrace();
		}
		return valid;
	}





	public Boolean SendTripReportEmail(ArrayList<TripInfoDto> triplist,
			String email,String offset_otDevice, String empname, String flag) {
		// TODO Auto-generated method stub

		boolean valid=false;
		Double TotalKm=0.0;Double Totalspeed=0.0;
		final String password ="password123";
		final String  from = "contact@primesystrack.com";
		/*final String password ="rupesh9604";
		final String  from = "rupesh.p@mykiddytracker.com";
		*/

		Properties props =new Properties();
		props.put("mail.smtp.host", "smtp.zoho.com");  
		/*	props.put("mail.smtp.host", host);  */
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class",  
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  
	//	System.out.println(toemailID+" "+Pass);

		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(from,password);//change accordingly  
			}  
		});  

		try{	Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		// Set To: header field of the header.
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));

		
		// Set Subject: header field
		message.setSubject("Trip Report for Primesys Track.");
		// This mail has 2 part, the BODY and the embedded image
		MimeMultipart multipart = new MimeMultipart("related");
		// first part (the html)
		BodyPart messageBodyPart = new MimeBodyPart();
		
		StringBuilder strBody = new StringBuilder(); 
		strBody.append("<!DOCTYPE html>")
	     .append("<html>")
	     .append("<head>")
	     .append("<style>")
	     .append("table {")
	     .append("   border: 1px solid black;")
	     .append("    width: 100%;")
	     .append("}")
	     .append("")
	     .append("th, td {")
	     .append("   border: 1px solid black;")
	     .append("")
	     .append("text-align: left;")
	     .append("    padding: 8px;")
	     .append("}")
	     .append("")
	     .append("tr:nth-child(even){background-color: #f2f2f2}")
	     .append("</style>")
	     .append("</head>")
	     .append("<body>")

	     .append("");
	     
	     
	         
	     if(triplist.size()>0)
	     {
	    	 strBody.append("<div style=\"overflow-x:auto;\">")
	     .append("<table style=\"width:100%\">")
	   /*  .append("  <caption align=\"center\">"+"Trip Report generated for "+triplist.get(0).getDevicename()+" which having IMEI NO: "+
	    	     triplist.get(0).getDevice()+"at  "+now +"."+"</caption>")*/
	  

	     .append("<tr bgcolor=\"#38fff8\">")
	     .append("<th colspan=\"10\" align=\"centre\">"+"<Font size=\"5\"/>"+"Trip Report "+"</th>")

	     
	     
	     .append("  </tr>");
	     
	 
	      strBody.append("<tr bgcolor=\"#38fff8\">");
	  /*   strBody.append("    <th colspan=\"10\"  align=\"centre\">"+"<Font size=\"5\"/>"+"Vehicle Name: "+triplist.get(0).getDevicename()+", "
	     		+ "IMEI No: "+  triplist.get(0).getDevice()+",     Date: "+now +"</th>")
*/
	    

	   if (flag.equalsIgnoreCase("15")) {
		    strBody.append("<th colspan=\"10\"  align=\"centre\">"+"<Font size=\"5\"/>"+" Date :- "+Common.GetDateWithOffset(Long.parseLong(triplist.get(0).getDesttimestamp()),offset_otDevice)+", Generated on :- "+now +" for "+triplist.get(0).getDevicename()+",("+empname+")"+"</th>");
		}else {
		    strBody.append("<th colspan=\"10\"  align=\"centre\">"+"<Font size=\"5\"/>"+" Date :- "+Common.GetDateWithOffset(Long.parseLong(triplist.get(0).getDesttimestamp()),offset_otDevice)+", Generated on :- "+now+" for "+triplist.get(0).getDevicename()+"</th>");
		}
	    	      
	     
	   strBody.append("  </tr>")

	     .append("<tr bgcolor=\"#38fff8\">")
	     .append("    <th>Trip#</th>")
	    /* .append("    <th>Date</th>")
	     .append("    <th>Name</th>")
	     .append("    <th>IMEI <br>No</th>")*/
	     .append("    <th>Start Time</th>")
	     .append("    <th>Start Address</th>")
	     .append("    <th>End Time</th>")
	     .append("    <th>End Address</th>");
	    /* if (flag.equalsIgnoreCase("15")) 
	     	strBody.append("Driver Name");
*/
	     strBody.append("    <th>Stoppage<br>min</th>")
	     .append("    <th>Average <br>speed</th>")
	     .append("    <th>Max <br>Speed</th>")
		     .append("    <th >Total <br>Distance</th>")

	   /*  .append("    <th>MIn<br>spedd</th>")*/
	     .append("  </tr>")
	     .append(" ");
	  
		 
		for(int i=0;i<triplist.size();i++)
		{
			TripInfoDto trip=triplist.get(i);
			System.err.println("*-----triplist---"+i);
			TotalKm=TotalKm+Double.parseDouble(trip.getTotalkm());
			Totalspeed=Totalspeed+Double.parseDouble(trip.getAvgspeed());
		strBody.append("<tr>")
		
	     .append("    <td>"+(i+1)+"</td>")
	   /*  .append("    <td>"+now+"</td>")
	     .append("    <td>"+trip.getDevicename()+"</td>")
	     .append("    <td>"+trip.getDevice()+"</td>")*/
	      .append("    <td>"+Common.GetDateTimeWithOffset(Long.parseLong(triplist.get(i).getSrctimestamp()),offset_otDevice)+"</td>");
	      if(trip.getSrc_adress()!=null&&!trip.getSrc_adress().equals(""))
	    	  strBody.append("    <td>"+trip.getSrc_adress()+"</td>");
	    	  else 
		    	  strBody.append("    <td>"+Common.GetAddress(trip.getSrclat(), trip.getSrclon())+"</td>");

	     strBody.append("    <td>"+Common.GetDateTimeWithOffset(Long.parseLong(triplist.get(i).getDesttimestamp()),offset_otDevice)+"</td>");
	     
	      if(trip.getDest_address()!=null&&!trip.getDest_address().equals(""))
	    	  strBody.append("    <td>"+trip.getDest_address()+"</td>");
	    	  else 
		    	  strBody.append("    <td>"+Common.GetAddress(trip.getDestlat(), trip.getDestlon())+"</td>");

		
		


		if(i==0)
			strBody.append("<td>"+"-"+"</td>");
			else 
				strBody.append("<td>"+((Long.parseLong(triplist.get(i).getSrctimestamp())-Long.parseLong(triplist.get(i-1).getDesttimestamp()))/60)+" Min"+"</td>");
				
		strBody.append("    <td>"+trip.getAvgspeed()+"</td>")
/*
	     .append("    <td>"+trip.getDestspeed()+"</td>")*/
	     .append("    <td>"+trip.getMaxspeed()+"</td>");
		
		
	     if (flag.equalsIgnoreCase("15")) {
	    	 strBody.append("<td>"+String.format("%.2f",Double.parseDouble(trip.getTotalkm())*0.621371)+" miles"+"</td>");

		}else {
			strBody .append("<td>"+String.format("%.2f",Double.parseDouble(trip.getTotalkm()))+" Km"+"</td>");

		}

	     strBody.append("  </tr>");
	     
		}
		
		
		strBody .append("  </tr>")
	      .append("<tr bgcolor=\"#38fff8\">");
	      
	        if(flag.equals("15"))
			     strBody.append("    <th style=\"text-align: right;font-weight: bold\" colspan=\"8\">"+"<Font size=\"5\"/>"+"Total miles " +"</th>");
		   else {
			   strBody .append("    <th style=\"text-align: right;font-weight: bold\" colspan=\"8\">"+"<Font size=\"5\"/>"+"Total Km " +"</th>");

		}
	     
		     if (flag.equals("15")) {
					strBody.append( "   <th> <Font size=\"5\"/>"+String.format("%.2f", TotalKm*0.621371)+" miles"+"</th>");


			}else {
				strBody.append( "   <th> <Font size=\"5\"/>"+String.format("%.2f", TotalKm)+" Km"+"</th>");

			}

	    

		     strBody .append("  </tr>");
	    

	    strBody.append("</table>")
	    
	    .append("</div>");
	     }
	     else{
	    	 strBody.append("<p>Report is not available. Please contact at contact@primesystrack.com</p>");
	    	 }
			     
		strBody.append("</body>");                                                                                                    
		strBody.append("</html>  "); 
		
		message.setText(strBody.toString());

		messageBodyPart.setContent(strBody.toString(), "text/html");
		// add it
		multipart.addBodyPart(messageBodyPart);


		message.setContent(multipart);
		// Send message
		Transport.send(message);

		valid=true;
		System.out.println("Sent message successfully....");
		
		
		}catch (MessagingException mex) {
			System.out.println("Exception----"+mex.getMessage()+"------");

			mex.printStackTrace();
		}
		return valid;
	}





	public boolean sendGuestUserEmail(String name, String email, String contact,
			String msg, String type, String city) {
		

		// TODO Auto-generated method stub

		boolean valid=false;
		Double TotalKm=0.0;Double Totalspeed=0.0;
		final String password ="password123";
		final String  from = "contact@primesystrack.com";
		

		Properties props =new Properties();
		props.put("mail.smtp.host", "smtp.zoho.com");  
		/*	props.put("mail.smtp.host", host);  */
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class",  
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  
	//	System.out.println(toemailID+" "+Pass);

		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(from,password);//change accordingly  
			}  
		});  

		try{	Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		// Set To: header field of the header.
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("contact@primesystrack.com"));
		
		// Set Subject: header field
		message.setSubject("New Guest user comes in our team.");
		// This mail has 2 part, the BODY and the embedded image
		MimeMultipart multipart = new MimeMultipart("related");
		// first part (the html)
		BodyPart messageBodyPart = new MimeBodyPart();
		

			StringBuilder strBody = new StringBuilder(); 
			strBody.append("<!DOCTYPE html>")
			     .append("<html>")
			     .append("<head>")
			     .append("<title>Primesys Track </title>")
			     .append("</head>")
			     .append("<body>")
			     .append("")
			     .append("<div>        <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" bgcolor=\"dadada\" style=\"margin: 10.0px auto;padding: 15.0px;border: 1.0px solid rgb(107,57,108);font-family: serif , Arial;font-style: normal;font-weight: 300;font-size: 19.0px;color: rgb(255,255,255);background: #0097A7;\">          <tbody><tr>             <td width=\"100%\" align=\"left\">                 <a href=\"http://mykiddytracker.com\" style=\"float: left;\" target=\"_blank\">                     <img id=\"5287195000000943001_imgsrc_url_0\" alt=\"\" src=\"http://mykidtracker.in:81/apk/primesystrackhome.png\">                 </a>             </td>         </tr>                                   <tr>             <td width=\"100%\" align=\"left\" style=\"padding: 13.5px 27.0px;border-bottom: 1.0px solid rgb(199,199,199);\">                 <p>Hi PrimesysTrack,</p>                <p>New Guest User comes in our PrimesysTrack  Team.Please go through below details to followup that interesting person  ,</p>  ")
			     .append("")
			     .append("<p>Name :"+ name+"</p>  ")
			     .append("<p>Mob No :"+ contact+"</p>  ")
			     .append("<p>Message : "+ msg+"</p>  ")
			     .append("<p>Vist on : "+ type+"</p>  ")
			     .append("")
			     .append("")
			     .append("</td>    ")
			     .append("</tr>      ")
			     .append("<tr>       ")
			     .append("       </tr>                  <tr>             <td width=\"100%\" align=\"left\" style=\"padding: 7.0px 27.0px;\">                 <p>Regards,</p>                 <p style=\"font-size: 20.0px;\">PrimeysTrack Team.</p>                 <p style=\"font-size: 12.0px;\">© <a href=\"http://www.primesystech.com\" target=\"_blank\">Primesys Technologies</a></p>             </td>         </tr>                  <tr>             <td width=\"100%\"></td>         </tr>     </tbody></table>    </div>")
			     .append("</body>")
			     .append("</html>");
	

		message.setText(strBody.toString());

		messageBodyPart.setContent(strBody.toString(), "text/html");
		// add it
		multipart.addBodyPart(messageBodyPart);


		message.setContent(multipart);
		// Send message
		Transport.send(message);

		valid=true;
		System.out.println("Sent message successfully....");
		
		
		}catch (MessagingException mex) {
			System.out.println("Exception----"+mex.getMessage()+"------");

			mex.printStackTrace();
		}
		return valid;
			
	}

	public boolean sendGuestWelcomeEmail(String name, String email, String contact,
			String msg, String type, String city) {
		

		// TODO Auto-generated method stub

		boolean valid=false;
		Double TotalKm=0.0;Double Totalspeed=0.0;
		final String password ="password123";
		final String  from = "contact@Primesystrack.com";
		

		Properties props =new Properties();
		props.put("mail.smtp.host", "smtp.zoho.com");  
		/*	props.put("mail.smtp.host", host);  */
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class",  
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  
	//	System.out.println(toemailID+" "+Pass);

		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(from,password);//change accordingly  
			}  
		});  

		try{	Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		// Set To: header field of the header.
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));

		
		// Set Subject: header field
		message.setSubject("New Guest user comes in our team.");
		// This mail has 2 part, the BODY and the embedded image
		MimeMultipart multipart = new MimeMultipart("related");
		// first part (the html)
		BodyPart messageBodyPart = new MimeBodyPart();
		

			StringBuilder strBody = new StringBuilder(); 
			strBody.append("<!DOCTYPE html>")
			     .append("<html>")
			     .append("<head>")
			     .append("<title>Primesys Track </title>")
			     .append("</head>")
			     .append("<body>")
			     .append("")
			     .append("<div>        <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" bgcolor=\"dadada\" style=\"margin: 10.0px auto;padding: 15.0px;border: 1.0px solid rgb(107,57,108);font-family: serif , Arial;font-style: normal;font-weight: 300;font-size: 19.0px;color: rgb(255,255,255);background: #0097A7;\">          <tbody><tr>             <td width=\"100%\" align=\"left\">                 <a href=\"http://mykiddytracker.com\" style=\"float: left;\" target=\"_blank\">                     <img id=\"5287195000000943001_imgsrc_url_0\" alt=\"\" src=\"http://mykidtracker.in:81/apk/primesystrackhome.png\">                 </a>             </td>         </tr>                                   <tr>             <td width=\"100%\" align=\"left\" style=\"padding: 13.5px 27.0px;border-bottom: 1.0px solid rgb(199,199,199);\">                 <p>Hi PrimesysTrack,</p>                <p>New Guest User comes in our PrimesysTrack  Team.Please go through below details to followup that interesting person  ,</p>  ")
			     .append("")
			     .append("<p>Name :"+ name+"</p>  ")
			     .append("<p>Mob No :"+ contact+"</p>  ")
			     .append("<p>Message : "+ msg+"</p>  ")
			     .append("<p>Vist on : "+ type+"</p>  ")
			     .append("")
			     .append("")
			     .append("</td>    ")
			     .append("</tr>      ")
			     .append("<tr>       ")
			     .append("       </tr>                  <tr>             <td width=\"100%\" align=\"left\" style=\"padding: 7.0px 27.0px;\">                 <p>Regards,</p>                 <p style=\"font-size: 20.0px;\">PrimeysTrack Team.</p>                 <p style=\"font-size: 12.0px;\">© <a href=\"http://www.primesystech.com\" target=\"_blank\">Primesys Technologies</a></p>             </td>         </tr>                  <tr>             <td width=\"100%\"></td>         </tr>     </tbody></table>    </div>")
			     .append("</body>")
			     .append("</html>");
	

		message.setText(strBody.toString());

		messageBodyPart.setContent(strBody.toString(), "text/html");
		// add it
		multipart.addBodyPart(messageBodyPart);


		message.setContent(multipart);
		// Send message
		Transport.send(message);

		valid=true;
		System.out.println("Sent message successfully....");
		
		
		}catch (MessagingException mex) {
			System.out.println("Exception----"+mex.getMessage()+"------");

			mex.printStackTrace();
		}
		return valid;
			
	}





	public Boolean SendNotifyAboutExpiryDateEmail(String email,
			String name, String datediff) {

		// TODO Auto-generated method stub

		boolean valid=false;
		final String password ="password123";
		final String  from = "contact@mykiddytracker.com";
		

		Properties props =new Properties();
		props.put("mail.smtp.host", "smtp.zoho.com");  
		/*	props.put("mail.smtp.host", host);  */
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class",  
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  
	//	System.out.println(toemailID+" "+Pass);

		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(from,password);//change accordingly  
			}  
		});  

		try{	Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		// Set To: header field of the header.
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));
	//	message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("rupesh.p@mykiddytracker.com"));

		
		// Set Subject: header field
		message.setSubject("Hi "+name+"!"+" Subcription alert form Mykiddytrcaker.");
		// This mail has 2 part, the BODY and the embedded image
		MimeMultipart multipart = new MimeMultipart("related");
		// first part (the html)
		BodyPart messageBodyPart = new MimeBodyPart();
		String template="<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\">"
				+ "<title>Welcome To MyKiddyTracker</title></head>"
				+ ""
				+ "<body link=\"white\" >"
				+"<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" bgcolor=\"dadada\" style=\"margin: 10px auto; padding: 15px; border: 1px solid #6b396c; font-family: 'serif', Arial !important; -webkit-font-smoothing: antialiased; font-style: normal; font-weight: 300; font-size: 19px; color: #fff;background: #cc6698;background: -moz-linear-gradient(-45deg, #cc6698 0%, #6b396c 100%);background: -webkit-gradient(linear, left top, right bottom, color-stop(0%, #cc6698), color-stop(100%, #6b396c));background: -webkit-linear-gradient(-45deg, #cc6698 0%, #6b396c 100%);background: -o-linear-gradient(-45deg, #cc6698 0%, #6b396c 100%);background: -ms-linear-gradient(-45deg, #cc6698 0%, #6b396c 100%);background: linear-gradient(-45deg, #cc6698 0%, #6b396c 100%);filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#cc6698, endColorstr=#6b396c, GradientType=1);\">"+
				""+
				"    <tr>"+
				"        <td width=\"100%\" align=\"left\">"+
				"            <a href=\"http://mykiddytracker.com\" style=\"float:left;\" target=\"_blank\">"+
				"                <img src=\"http://mykiddytracker.com/img/berry/logo.png\" alt=\"\" class=\"logo\">"+
				"            </a>"+
				"        </td>"+
				"    </tr>"+
				""+
				""+
				"    <tr>"+
				"        <td width=\"100%\" align=\"left\" style=\"padding:13.5px 27px;border-bottom:1px solid #c7c7c7;\">"+
				"            <p>Hi "+name+",</p>"+
				"            <p>Thanks for choosing  MyKiddyTracker App to Track your Loved Ones. We hope to provide you the Best Tracking experience you ever have.</p>"+
				"            <p>your subcription will expire in "+datediff +" days,Please recharge your device.</p>"+
         
				"            <p>Use Below Link to Download the MyKiddyTracker App.</p>"+
				"        </td>"+
				"    </tr>"+
				""+
				"    <tr>"+
				"        <td width=\"100%\" align=\"left\" style=\"padding:7px 27px;border-bottom:1px solid #c7c7c7;\">"+
				"            <a href=\"https://play.google.com/store/apps/details?id=com.primesys.mitra&hl=en\" target=\"_blank\" style=\"margin-right: 10px;color: #6b396c;border-color: #ffffff;border: 2px solid #ffffff;background: none;background-color: #ffffff;outline: 0;box-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);-webkit-border-radius: 3em; -moz-border-radius: 3em;-ms-border-radius: 3em;-o-border-radius: 3em;border-radius: 3em;-webkit-box-shadow: none;-moz-box-shadow: none;box-shadow: none;padding: 15px 25px;-webkit-animation-duration: 1.3s;animation-duration: 1.3s;-webkit-animation-fill-mode: both;animation-fill-mode: both;display: inline-block;font-weight: normal;text-align: center;vertical-align: middle;white-space: nowrap;font-size: 14px;line-height: 1.42857143;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;text-decoration: none;cursor: pointer;\">"+
				"                Click To Download the MyKiddyTracker App"+
				"            </a>"+
				"        </td>"+
				"    </tr>"+
				""+
				"    <tr>"+
				"        <td width=\"100%\" align=\"left\" style=\"padding:7px 27px;\">"+
				"            <p>Regards,</p>"+
				"            <p style=\"font-size:20px;\">MyKiddyTracker Team.</p>"+
				"            <p style=\"font-size:12px;\">© <a href=\"www.primesystech.com\">Primesys Technologies</a></p>"+
				"        </td>"+
				"    </tr>"+
				"    <tr>"+
				"        <td width=\"100%\" height=\"39\"></td>"+
				"    </tr>"+
				"</table>"+
			 "</body>"
						+ ""
						+ ""
						+ "</html>\"";
		message.setText(template);

		messageBodyPart.setContent(template, "text/html");
		// add it
		multipart.addBodyPart(messageBodyPart);


		message.setContent(multipart);
		// Send message
		Transport.send(message);


		System.out.println("Sent message successfully....");
		
		
		}catch (MessagingException mex) {
			System.out.println("Exception----"+mex.getMessage()+"------");

			mex.printStackTrace();
		}
		return valid;
	
	}





	public synchronized String SendWebTripReportEmail(ArrayList<TripInfoDto> triplist,final String offset_otDevice, String flag, String empname) {

		// TODO Auto-generated method stub

		Double TotalKm=0.0;Double Totalspeed=0.0;
		String msg="";
		
		try{	
		
			
			StringBuilder strBody = new StringBuilder(); 
			strBody.append("<!DOCTYPE html>")
		     .append("<html>")
		     .append("<head>")
		     .append("<style>")
		     .append("table {")
		     .append("   border: 1px solid black;")
		     .append("    width: 100%;")
		     .append("}")
		     .append("")
		     .append("th, td {")
		     .append("   border: 1px solid black;")
		     .append("")
		     .append("text-align: left;")
		     .append("    padding: 8px;")
		     .append("}")
		     .append("")
		     .append("tr:nth-child(even){background-color: #f2f2f2}")
		     .append("</style>")
		     .append("</head>")
		     .append("<body>")

		     .append("");
		     
		     
		         
		     if(triplist.size()>0)
		     {
		    	 strBody.append("<div style=\"overflow-x:auto;\">")
		     .append("<table style=\"width:100%\">")
		   /*  .append("  <caption align=\"center\">"+"Trip Report generated for "+triplist.get(0).getDevicename()+" which having IMEI NO: "+
		    	     triplist.get(0).getDevice()+"at  "+now +"."+"</caption>")*/
		  

		     .append("<tr bgcolor=\"#38fff8\">")
		    // .append("<th colspan=\"9\" align=\"centre\">"+"<Font size=\"5\"/>"+"Trip Report "+"</th>")

		     .append("<th style=\"text-align: center;font-size: 25px;font-weight: bold\" colspan=\"10\" align=\"centre\">"+"<Font size=\"5\"/>"+"Trip Report "+"</th>")
		     
		     .append("  </tr>");
		     
		 
		      strBody.append("<tr bgcolor=\"#38fff8\">");

			   if (flag.equalsIgnoreCase("15")&&!empname.equalsIgnoreCase("N/A")) {
				    //strBody.append("<th colspan=\"10\"  align=\"Center\">"+"<Font size=\"5\"/>"+" Date :- "+Common.GetDateWithOffset(Long.parseLong(triplist.get(0).getDesttimestamp()),offset_otDevice)+", Generated on :- "+now +" for "+triplist.get(0).getDevicename()+",("+empname+")"+"</th>");
				    strBody.append("<td colspan=\"10\"  align=\"Center\" bgcolor=\"#38fff8\" >"+"<Font size=\"5\"/>"+"<b><center>  Date :- "+Common.GetDateWithOffset(Long.parseLong(triplist.get(0).getDesttimestamp()),offset_otDevice)+", Generated on :- "+now+" for "+triplist.get(0).getDevicename()+",("+empname+")"+" </center> </b> </td>");

				}else {
				    strBody.append("<td colspan=\"10\"  align=\"Center\" bgcolor=\"#38fff8\" >"+"<Font size=\"5\"/>"+"<b><center>  Date :- "+Common.GetDateWithOffset(Long.parseLong(triplist.get(0).getDesttimestamp()),offset_otDevice)+", Generated on :- "+now+" for "+triplist.get(0).getDevicename()+" </center> </b> </td>");
				}

		     
		     
		      strBody .append("  </tr>")

		     .append("<tr bgcolor=\"#38fff8\">")
		     .append("    <th>Trip#</th>")
		    /* .append("    <th>Date</th>")
		     .append("    <th>Name</th>")
		     .append("    <th>IMEI <br>No</th>")*/
		     .append("    <th>Start Time</th>")
		     .append("    <th style=\"width: 230px\" >Start Address</th>")
		     .append("    <th>End Time</th>")
		     .append("    <th style=\"width: 230px\" >End Address</th>");
		     
		    /* if (flag.equals("15"))
		     		strBody.append("    <th>Driver<br>Name</th>");*/

		     strBody.append("    <th>Stoppage<br>min</th>")
		     .append("    <th>Average <br>speed</th>")
		     .append("    <th>Max <br>Speed</th>")
		     .append("    <th >Total <br>Distance</th>")

		   /*  .append("    <th>MIn<br>spedd</th>")*/
		     .append("  </tr>")
		     .append(" ");
		  
			 
			for(int i=0;i<triplist.size();i++)
			{
				TripInfoDto trip=triplist.get(i);
				System.err.println("*-----triplist---"+i);
				TotalKm=TotalKm+Double.parseDouble(trip.getTotalkm());
				Totalspeed=Totalspeed+Double.parseDouble(trip.getAvgspeed());
			strBody.append("<tr>")
			
		     .append("    <td>"+(i+1)+"</td>")
		   /*  .append("    <td>"+now+"</td>")
		     .append("    <td>"+trip.getDevicename()+"</td>")
		     .append("    <td>"+trip.getDevice()+"</td>")*/
		     .append("    <td>"+Common.GetDateTimeWithOffset(Long.parseLong(triplist.get(i).getSrctimestamp()),offset_otDevice)+"</td>");
	      if(trip.getSrc_adress()!=null&&!trip.getSrc_adress().equals(""))
	    	  strBody.append("    <td>"+trip.getSrc_adress()+"</td>");
	    	  else 
		    	  strBody.append("    <td>"+Common.GetAddress(trip.getSrclat(), trip.getSrclon())+"</td>");

	     strBody.append("    <td>"+Common.GetDateTimeWithOffset(Long.parseLong(triplist.get(i).getDesttimestamp()),offset_otDevice)+"</td>");
	     
	      if(trip.getDest_address()!=null&&!trip.getDest_address().equals(""))
	    	  strBody.append("    <td>"+trip.getDest_address()+"</td>");
	    	  else 
		    	  strBody.append("    <td>"+Common.GetAddress(trip.getDestlat(), trip.getDestlon())+"</td>");

	/*	if (flag.equalsIgnoreCase("15")) {
			strBody.append("    <td>"+empname+"</td>");

		}*/
		
			if(i==0)
				strBody.append("<td>"+"-"+"</td>");
				else 
					strBody.append("<td>"+((Long.parseLong(triplist.get(i).getSrctimestamp())-Long.parseLong(triplist.get(i-1).getDesttimestamp()))/60)+" Min"+"</td>");
					
			strBody.append("    <td>"+trip.getAvgspeed()+"</td>")
	/*
		     .append("    <td>"+trip.getDestspeed()+"</td>")*/
		     .append("    <td>"+trip.getMaxspeed()+"</td>");
			
			
		     if (flag.equalsIgnoreCase("15")) {
		    	 strBody.append("<td>"+String.format("%.2f",Double.parseDouble(trip.getTotalkm())*0.621371)+" miles"+"</td>");

			}else {
				strBody .append("<td>"+String.format("%.2f",Double.parseDouble(trip.getTotalkm()))+" Km"+"</td>");

			}

		     strBody.append("  </tr>");
		     
			}
			
			
			strBody .append("  </tr>")
		      .append("<tr bgcolor=\"#38fff8\">");
		      
		        if(flag.equals("15"))
				     strBody.append("    <th style=\"text-align: right;font-weight: bold\" colspan=\"8\">"+"<Font size=\"5\"/>"+"Total miles " +"</th>");
			   else {
				   strBody .append("    <th style=\"text-align: right;font-weight: bold\" colspan=\"8\">"+"<Font size=\"5\"/>"+"Total Km " +"</th>");

			}
		     
			     if (flag.equals("15")) {
						strBody.append( "   <th> <Font size=\"5\"/>"+String.format("%.2f", TotalKm*0.621371)+"miles"+"</th>");


				}else {
					strBody.append( "   <th> <Font size=\"5\"/>"+String.format("%.2f", TotalKm)+" Km"+"</th>");

				}

		    

			 strBody .append("  </tr>");
	

		    strBody.append("</table>")
		    
		    .append("</div>");
		     }
		     else{
		    	 strBody.append("<p>Report is not available. Please contact at contact@primesystrack.com</p>");
		    	 }
				     
			strBody.append("</body>");                                                                                                    
			strBody.append("</html>  "); 
			
		
		msg=strBody.toString();
		
		
		}catch (Exception mex) {
			System.out.println("Exception----"+mex.getMessage()+"------");

			mex.printStackTrace();
		}
	
		return msg;
	
	}





	public synchronized Boolean SendTripReportEmailAttachment(
			ArrayList<TripInfoDto> triplist, String email, String offset_otDevice, String flag, String empname) {
		
		

		// TODO Auto-generated method stub

		boolean valid=false;
		Double TotalKm=0.0;Double Totalspeed=0.0;
		final String password ="password123";
		final String  from = "contact@primesystrack.com";
		/*final String password ="rupesh9604";
		final String  from = "rupesh.p@mykiddytracker.com";
		*/

		Properties props =new Properties();
		props.put("mail.smtp.host", "smtp.zoho.com");  
		/*	props.put("mail.smtp.host", host);  */
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class",  
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  
	//	System.out.println(toemailID+" "+Pass);

		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(from,password);//change accordingly  
			}  
		});  

		try{	Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		// Set To: header field of the header.
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));

		
		// Set Subject: header field
		message.setSubject("Trip Report for Primesys Track.");
		// This mail has 2 part, the BODY and the embedded image
		MimeMultipart multipart = new MimeMultipart("related");
		// first part (the html)
		BodyPart messageBodyPart = new MimeBodyPart();
		
		StringBuilder strBody = new StringBuilder(); 
		strBody.append("<!DOCTYPE html>")
	     .append("<html>")
	     .append("<head>")
	     .append("<style>")
	     .append("table {")
	     .append("   border: 1px solid black;")
	     .append("    width: 100%;")
	     .append("}")
	     .append("")
	     .append("th, td {")
	     .append("   border: 1px solid black;")
	     .append("")
	     .append("text-align: left;")
	     .append("    padding: 8px;")
	     .append("}")
	     .append("")
	     .append("tr:nth-child(even){background-color: #f2f2f2}")
	     .append("</style>")
	     .append("</head>")
	     .append("<body>")

	     .append("");
	     
	     
	         
	     if(triplist.size()>0)
	     {
	    	 strBody.append("<div style=\"overflow-x:auto;\">")
	     .append("<table style=\"width:100%\">")
	   /*  .append("  <caption align=\"center\">"+"Trip Report generated for "+triplist.get(0).getDevicename()+" which having IMEI NO: "+
	    	     triplist.get(0).getDevice()+"at  "+now +"."+"</caption>")*/
	  

	     .append("<tr bgcolor=\"#38fff8\">")
	     .append("<th colspan=\"10\" align=\"centre\">"+"<Font size=\"5\"/>"+"Trip Report "+"</th>")

	     
	     
	     .append("  </tr>");
	     
	 
	      strBody.append("<tr bgcolor=\"#38fff8\">");
	    

		   if (flag.equalsIgnoreCase("15")) {
			    strBody.append("<th colspan=\"10\"  align=\"centre\">"+"<Font size=\"5\"/>"+" Date :- "+Common.GetDateWithOffset(Long.parseLong(triplist.get(0).getDesttimestamp()),offset_otDevice)+", Generated on :- "+now +" for "+triplist.get(0).getDevicename()+",("+empname+")"+"</th>");
			}else {
			    strBody.append("<th colspan=\"10\"  align=\"centre\">"+"<Font size=\"5\"/>"+" Date :- "+Common.GetDateWithOffset(Long.parseLong(triplist.get(0).getDesttimestamp()),offset_otDevice)+", Generated on :- "+now+" for "+triplist.get(0).getDevicename()+"</th>");
			}
		    	  
	    	      
	     
	   strBody.append("  </tr>")

	     .append("<tr bgcolor=\"#38fff8\">")
	     .append("    <th>Trip#</th>")
	    /* .append("    <th>Date</th>")
	     .append("    <th>Name</th>")
	     .append("    <th>IMEI <br>No</th>")*/
	     .append("    <th>Start Time</th>")
	     .append("    <th>Start Address</th>")
	     .append("    <th>End Time</th>")
	     .append("    <th>End Address</th>");
	     /*if(flag.equals("15"))
		     strBody.append("    <th>Driver <br> Name</th>");*/

	     
	     strBody.append("    <th>Stoppage<br>min</th>")
	     .append("    <th>Average <br>speed</th>")
	     .append("    <th>Max <br>Speed</th>")
		     .append("    <th >Total <br>Distance</th>")

	   /*  .append("    <th>MIn<br>spedd</th>")*/
	     .append("  </tr>")
	     .append(" ");
	  
		 
		for(int i=0;i<triplist.size();i++)
		{
			TripInfoDto trip=triplist.get(i);
			System.err.println("*-----triplist---"+i+"======== timesrc--- "+trip.getSrctimestamp());
			TotalKm=TotalKm+Double.parseDouble(trip.getTotalkm());
			Totalspeed=Totalspeed+Double.parseDouble(trip.getAvgspeed());
		strBody.append("<tr>")
		
	     .append("    <td>"+(i+1)+"</td>")
	   /*  .append("    <td>"+now+"</td>")
	     .append("    <td>"+trip.getDevicename()+"</td>")
	     .append("    <td>"+trip.getDevice()+"</td>")*/
	     .append("    <td>"+Common.GetDateTimeWithOffset(Long.parseLong(triplist.get(i).getSrctimestamp()),offset_otDevice)+"</td>");
	      if(trip.getSrc_adress()!=null&&!trip.getSrc_adress().equals(""))
	    	  strBody.append("    <td>"+trip.getSrc_adress()+"</td>");
	    	  else 
		    	  strBody.append("    <td>"+Common.GetAddress(trip.getSrclat(), trip.getSrclon())+"</td>");

	     strBody.append("    <td>"+Common.GetDateTimeWithOffset(Long.parseLong(triplist.get(i).getDesttimestamp()),offset_otDevice)+"</td>");
	     
	      if(trip.getDest_address()!=null&&!trip.getDest_address().equals(""))
	    	  strBody.append("    <td>"+trip.getDest_address()+"</td>");
	    	  else 
		    	  strBody.append("    <td>"+Common.GetAddress(trip.getDestlat(), trip.getDestlon())+"</td>");
/*
		if (flag.equalsIgnoreCase("15")) {
			strBody.append("    <td>"+empname+"</td>");

		}
		*/

		if(i==0)
			strBody.append("<td>"+"-"+"</td>");
			else 
				strBody.append("<td>"+((Long.parseLong(triplist.get(i).getSrctimestamp())-Long.parseLong(triplist.get(i-1).getDesttimestamp()))/60)+" Min"+"</td>");
				
		strBody.append("    <td>"+ new DecimalFormat("#.##").format(Double.parseDouble(trip.getAvgspeed()))+"</td>")
/*
	     .append("    <td>"+trip.getDestspeed()+"</td>")*/
	     .append("    <td>"+trip.getMaxspeed()+"</td>");

	     if (flag.equalsIgnoreCase("15")) {
	    	 strBody.append("<td>"+String.format("%.2f",Double.parseDouble(trip.getTotalkm())*0.621371)+" miles"+"</td>");

		}else {
			strBody .append("<td>"+String.format("%.2f",Double.parseDouble(trip.getTotalkm()))+" Km"+"</td>");

		}

	     strBody.append("  </tr>");
	     
		}
		
		
		strBody .append("  </tr>")
	      .append("<tr bgcolor=\"#38fff8\">");
	      
	       if(flag.equals("15"))
			     strBody.append("    <th style=\"text-align: right;font-weight: bold\" colspan=\"8\">"+"<Font size=\"5\"/>"+"Total miles " +"</th>");
		   else {
			   strBody .append("    <th style=\"text-align: right;font-weight: bold\" colspan=\"8\">"+"<Font size=\"5\"/>"+"Total Km " +"</th>");

		}
	     
		     if (flag.equals("15")) {
					strBody.append( "   <th> <Font size=\"5\"/>"+String.format("%.2f", TotalKm*0.621371)+" miles"+"</th>");


			}else {
				strBody.append( "   <th> <Font size=\"5\"/>"+String.format("%.2f", TotalKm)+" Km"+"</th>");

			}

	    

		 strBody .append("  </tr>");

	    

	    strBody.append("</table>")
	    
	    .append("</div>");
	     }
	     else{
	    	 strBody.append("<p>Report is not available. Please contact at contact@primesystrack.com</p>");
	    	 }
			     
		strBody.append("</body>");                                                                                                    
		strBody.append("</html>  "); 
		
		message.setText(strBody.toString());

		messageBodyPart.setContent(strBody.toString(), "text/html");
		// add it
		multipart.addBodyPart(messageBodyPart);


		message.setContent(multipart);
		// Send message
		Transport.send(message);

		valid=true;
		System.out.println("Sent message successfully....");
		
		
		}catch (MessagingException mex) {
			System.out.println("Exception----"+mex.getMessage()+"------");

			mex.printStackTrace();
		}
		return valid;
	
		
		
/*

		// TODO Auto-generated method stub

		boolean valid=false;
		Double TotalKm=0.0;Double Totalspeed=0.0;
		String msg="";
		
		try{	
		
			
			StringBuilder strBody = new StringBuilder(); 
			strBody.append("<!DOCTYPE html>")
		     .append("<html>")
		     .append("<head>")
		     .append("<style>")
		     .append("table {")
		     .append("   border: 1px solid black;")
		     .append("    width: 100%;")
		     .append("}")
		     .append("")
		     .append("th, td {")
		     .append("   border: 1px solid black;")
		     .append("")
		     .append("text-align: left;")
		     .append("    padding: 8px;")
		     .append("}")
		     .append("")
		     .append("tr:nth-child(even){background-color: #f2f2f2}")
		     .append("</style>")
		     .append("</head>")
		     .append("<body>")

		     .append("");
		     
		     
		         
		     if(triplist.size()>0)
		     {
		    	 strBody.append("<div style=\"overflow-x:auto;\">")
		     .append("<table style=\"width:100%\">")
		     .append("  <caption align=\"center\">"+"Trip Report generated for "+triplist.get(0).getDevicename()+" which having IMEI NO: "+
		    	     triplist.get(0).getDevice()+"at  "+now +"."+"</caption>")
		  

		     .append("<tr bgcolor=\"#38fff8\">")
		    // .append("<th colspan=\"9\" align=\"centre\">"+"<Font size=\"5\"/>"+"Trip Report "+"</th>")

		     .append("<th style=\"text-align: center;font-size: 25px;font-weight: bold\" colspan=\"9\" align=\"centre\">"+"<Font size=\"5\"/>"+"Trip Report "+"</th>")
		     
		     .append("  </tr>");
		     
		 
		      strBody.append("<tr bgcolor=\"#38fff8\">")
		     .append("    <th  style=\"text-align: center;font-size: 25px;font-weight: bold\" colspan=\"9\"  align=\"centre\">"+"<Font size=\"5\"/>"+"Vehicle Name: "+triplist.get(0).getDevicename()+", IMEI No: "+
		    	     triplist.get(0).getDevice()+",     Date: "+now +"</th>")

		     
		     
		     .append("  </tr>")

		     .append("<tr bgcolor=\"#38fff8\">")
		     .append("    <th>Trip#</th>")
		     .append("    <th>Date</th>")
		     .append("    <th>Name</th>")
		     .append("    <th>IMEI <br>No</th>")
		     .append("    <th>Start <br>Time</th>")
		     .append("    <th style=\"width: 230px\" >Start <br>Address</th>")
		     .append("    <th>End<br>Time</th>")
		     .append("    <th style=\"width: 230px\" >End<br> Address</th>")
		     .append("    <th>Stoppage<br>min</th>")
		     .append("    <th>Average <br>speed</th>")
		     .append("    <th>Max <br>Speed</th>")
		     .append("    <th >Total KM</th>")

		     .append("    <th>MIn<br>spedd</th>")
		     .append("  </tr>")
		     .append(" ");
		  
			 
			for(int i=0;i<triplist.size();i++)
			{
				TripInfoDto trip=triplist.get(i);
				System.err.println("*-----triplist---"+i);
				TotalKm=TotalKm+Double.parseDouble(trip.getTotalkm());
				Totalspeed=Totalspeed+Double.parseDouble(trip.getAvgspeed());
			strBody.append("<tr>")
			
		     .append("    <td>"+(i+1)+"</td>")
		     .append("    <td>"+now+"</td>")
		     .append("    <td>"+trip.getDevicename()+"</td>")
		     .append("    <td>"+trip.getDevice()+"</td>")
		     .append("    <td>"+Common.getDateCurrentTimeZone(Long.parseLong(trip.getSrctimestamp()))+"</td>");
		     strBody.append("    <td>"+Common.GetAddress(trip.getSrclat(), trip.getSrclon())+"</td>")
		     .append("    <td>"+Common.getDateCurrentTimeZone(Long.parseLong(trip.getDesttimestamp()))+"</td>")
		     .append("    <td>"+Common.GetAddress(trip.getDestlat(), trip.getDestlon())+"</td>");

			if(i==0)
				strBody.append("<td>"+"-"+"</td>");
				else 
					strBody.append("<td>"+((Long.parseLong(triplist.get(i).getSrctimestamp())-Long.parseLong(triplist.get(i-1).getDesttimestamp()))/60)+" Min"+"</td>");
					
			strBody.append("    <td>"+trip.getAvgspeed()+"</td>")
	
		     .append("    <td>"+trip.getDestspeed()+"</td>")
		     .append("    <td>"+trip.getMaxspeed()+"</td>")
		     	     .append("    <td>"+String.format("%.2f",Double.parseDouble(trip.getTotalkm()))+" Km"+"</td>")

		     .append("  </tr>");
		     
			}
			
			
			strBody .append("  </tr>")
		      .append("<tr bgcolor=\"#38fff8\">")
		     .append("    <th style=\"text-align: right;font-weight: bold\" colspan=\"8\">"+"<Font size=\"5\"/>"+"Total Km " +"</th>")
		    .append( "   <th> <Font size=\"5\"/>"+String.format("%.2f", TotalKm)+" Km"+"</th>")
		    

		     .append("  </tr>");
		    

		    strBody.append("</table>")
		    
		    .append("</div>");
		     }
		     else{
		    	 strBody.append("<p>Report is not available. Please contact at contact@primesystrack.com</p>");
		    	 }
				     
			strBody.append("</body>");                                                                                                    
			strBody.append("</html>  "); 
			
		
		msg=strBody.toString();
		
		
		}catch (Exception mex) {
			System.out.println("Exception----"+mex.getMessage()+"------");

			mex.printStackTrace();
		}
	
		return msg;
	
	*/
	}

	public Boolean sendDeviceOnOffMailToDepartmentEmp(String sendemail, String file, String dept) {
		
		

		boolean valid=false;
		final String password ="Punemkt@18";
		final String  from = "report@primesystrack.com";
		/*final String password ="Primesys@123";
		final String  from = "delesh@primesystech.com";
		*/

		Properties props =new Properties();
		props.put("mail.smtp.host", "smtp.zoho.com");  
		/*	props.put("mail.smtp.host", host);  */
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class",  
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  
	//	System.out.println(toemailID+" "+Pass);

		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(from,password);//change accordingly  
			}  
		});  

		try{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			// Set To: header field of the header.
			  message.setRecipient(Message.RecipientType.TO, new InternetAddress("enggctljp@gmail.com"));
			// message.setRecipient(Message.RecipientType.TO, new InternetAddress("patil.rupesh4892@gmail.com"));
			   
			  message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(sendemail));
			//  message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("rupesh@primesystech.com"));

			  
			  
			// Set Subject: header field
			message.setSubject(dept+" DeviceOnOff Status");
			
			// This mail has 2 part, the BODY and the embedded image
			MimeMultipart multipart = new MimeMultipart("related");
		
			StringBuilder strBody = new StringBuilder(); 
			strBody.append("Please find the attachment of device on and off status report for "+dept); 
			
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			message.setText(strBody.toString());
			messageBodyPart.setContent(strBody.toString(), "text/html");
			// add it
			multipart.addBodyPart(messageBodyPart);
			// Part two is attachment
	         messageBodyPart = new MimeBodyPart();
	         DataSource source = new FileDataSource(file);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(file);
	         multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);
			// Send message
			Transport.send(message);


			System.out.println("Sent Order message successfully....");
			valid=true;
		}catch (MessagingException mex) {
			System.out.println("Exception----"+mex.getMessage());
			valid=false;
		}
		return valid;

		
	}



public Boolean sendDeviceTripMailToDepartmentEmp(String sendemail, String file, String dept) {
		
		

		boolean valid=false;
		final String password ="Prime@apple2020";
		final String  from = "report.primesystrack@gmail.com";
		
		/*final String password ="Punemkt@18";
		final String  from = "report@primesystrack.com";
		final String password ="Primesys@123";
		final String  from = "delesh@primesystech.com";
		*/
		
		Properties props =new Properties();
		//props.put("mail.smtp.host", "smtp.zoho.com");  
		 props.put("mail.smtp.host", "smtp.gmail.com");  

		///*	props.put("mail.smtp.host", host);  */
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class",  
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  
	//	System.out.println(toemailID+" "+Pass);

		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(from,password);//change accordingly  
			}  
		});  

		try{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			// Set To: header field of the header.
			  message.setRecipient(Message.RecipientType.TO, new InternetAddress("enggctljp@gmail.com"));
			// message.setRecipient(Message.RecipientType.TO, new InternetAddress("patil.rupesh4892@gmail.com"));
			   
			  message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(sendemail));
			//  message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("rupesh@primesystech.com"));

			// Set Subject: header field
			message.setSubject(dept+" Device Trip Report");
			
			// This mail has 2 part, the BODY and the embedded image
			MimeMultipart multipart = new MimeMultipart("related");
		
			StringBuilder strBody = new StringBuilder(); 
			strBody.append("Please find the attachment of device trip report for "+dept); 
			
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			message.setText(strBody.toString());
			messageBodyPart.setContent(strBody.toString(), "text/html");
			// add it
			multipart.addBodyPart(messageBodyPart);
			// Part two is attachment
	         messageBodyPart = new MimeBodyPart();
	         DataSource source = new FileDataSource(file);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(file);
	         multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);
			// Send message
			Transport.send(message);


			System.out.println("Sent Order message successfully....");
			valid=true;
		}catch (MessagingException mex) {
			System.out.println("Exception----"+mex.getMessage());
			valid=false;
		}
		return valid;

		
	}


public Boolean sendDeviceExceptionMailToDepartmentEmp(String sendemail, String file, String dept) {
	
	

	boolean valid=false;
	final String password ="Prime@apple2020";
	final String  from = "report.primesystrack@gmail.com";
	
	/*final String password ="Punemkt@18";
	final String  from = "report@primesystrack.com";
	final String password ="Primesys@123";
	final String  from = "delesh@primesystech.com";
	*/
	

	Properties props =new Properties();
	 props.put("mail.smtp.host", "smtp.gmail.com");  

	/*	props.put("mail.smtp.host", "smtp.zoho.com");  
*/	/*	props.put("mail.smtp.host", host);  */
	props.put("mail.smtp.socketFactory.port", "465");  
	props.put("mail.smtp.socketFactory.class",  
			"javax.net.ssl.SSLSocketFactory");
	props.put("mail.smtp.auth", "true");  
	props.put("mail.smtp.port", "465");  
//	System.out.println(toemailID+" "+Pass);

	Session session = Session.getDefaultInstance(props,  
			new javax.mail.Authenticator() {  
		protected PasswordAuthentication getPasswordAuthentication() {  
			return new PasswordAuthentication(from,password);//change accordingly  
		}  
	});  

	try{
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		// Set To: header field of the header.
		 message.setRecipient(Message.RecipientType.TO, new InternetAddress("enggctljp@gmail.com"));
		//message.setRecipient(Message.RecipientType.TO, new InternetAddress("patil.rupesh4892@gmail.com"));
		   
		  message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(sendemail));
		//  message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("rupesh@primesystech.com"));

		// Set Subject: header field
		message.setSubject(dept+" Device Exception Trip Report");
		
		// This mail has 2 part, the BODY and the embedded image
		MimeMultipart multipart = new MimeMultipart("related");
	
		StringBuilder strBody = new StringBuilder(); 
		strBody.append("Please find the attachment of device exception trip report for "+dept); 
		
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		message.setText(strBody.toString());
		messageBodyPart.setContent(strBody.toString(), "text/html");
		// add it
		multipart.addBodyPart(messageBodyPart);
		// Part two is attachment
         messageBodyPart = new MimeBodyPart();
         DataSource source = new FileDataSource(file);
         messageBodyPart.setDataHandler(new DataHandler(source));
         messageBodyPart.setFileName(file);
         multipart.addBodyPart(messageBodyPart);

		message.setContent(multipart);
		// Send message
		Transport.send(message);


		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Sent Order message successfully....");
		valid=true;
	}catch (MessagingException mex) {
		System.out.println("Exception----"+mex.getMessage());
		valid=false;
	}
	return valid;

	
}

public Boolean sendDeviceExceptionMailToJaipur(String sendemail, String file,
		String dept,String subject,String msg, boolean isAllSectionReport,
		 String from,  String  password ) {
	
	
	System.out.println("from---"+from+"----password---"+password);

	boolean valid=false;
	/*final String password ="Prime@apple2020";
	final String  from = "report.primesystrack@gmail.com";*/
	
//	 password ="password123";
//	  from = "contact@primesystrack.com";
//	
/*	 password ="Primesys@12345";
   from = "report@primesystrack.com";*/
	
	/*final String password ="Punemkt@18";
	final String  from = "report@primesystrack.com";
	final String password ="Primesys@123";
	final String  from = "delesh@primesystech.com";
	*/
	

	Properties props =new Properties();
//	 props.put("mail.smtp.host", "smtp.gmail.com");
	if (from.contains("gmail.com")) 
		props.put("mail.smtp.host", "smtp.gmail.com");
	else
		props.put("mail.smtp.host", "smtp.zoho.com"); 

	/*	props.put("mail.smtp.host", host);  */
	props.put("mail.smtp.socketFactory.port", "465");  
	props.put("mail.smtp.socketFactory.class",  
			"javax.net.ssl.SSLSocketFactory");
	props.put("mail.smtp.auth", "true");  
	props.put("mail.smtp.port", "465");  
	props.put("mail.smtp.starttls.enable", "true");  

//	System.out.println(toemailID+" "+Pass);

	/*Session session = Session.getDefaultInstance(props,  
			new javax.mail.Authenticator() {  
		protected PasswordAuthentication getPasswordAuthentication() {  

			return new PasswordAuthentication(from,password);//change accordingly  
		}  
	});  
	session.setDebug(false);*/
	MyAuthenticator authenticator = new MyAuthenticator(from, password);
//	PasswordAuthentication passwordAuthenticator=authenticator.getPasswordAuthentication();
	Session session = Session.getInstance(props, authenticator);
	session.setDebug(false);
	System.err.println("sendDeviceExceptionMailToJaipur Sendemail--------"+sendemail);

	try{
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		if(isAllSectionReport){
			
			
			message.setRecipient(Message.RecipientType.TO, new InternetAddress("report.primesystrack@gmail.com"));
			InternetAddress[] toAddresses = InternetAddress.parse(sendemail);
			 message.addRecipients(Message.RecipientType.CC,toAddresses);

//			  message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("rupesh@primesystech.com"));
//				message.setRecipient(Message.RecipientType.TO, new InternetAddress("patil.rupesh4892@gmail.com"));


		}else{
			// Set To: header field of the header.
			message.setRecipient(Message.RecipientType.TO, new InternetAddress("report.primesystrack@gmail.com"));

			// message.setRecipient(Message.RecipientType.TO, new InternetAddress("enggctlbsb@gmail.com"));
//			message.setRecipient(Message.RecipientType.TO, new InternetAddress("patil.rupesh4892@gmail.com"));
			   
			 // message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(sendemail));
			 
				InternetAddress[] toAddresses = InternetAddress.parse(sendemail);
				 message.addRecipients(Message.RecipientType.CC,toAddresses);
			//  message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("rupesh@primesystech.com"));

		}
		
		// Set Subject: header field
		message.setSubject(dept+" "+subject);
		
		// This mail has 2 part, the BODY and the embedded image
		MimeMultipart multipart = new MimeMultipart("related");
	
		StringBuilder strBody = new StringBuilder(); 
		strBody.append("Please find the attachment of "+msg+" for "+dept); 
		
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		message.setText(strBody.toString());
		messageBodyPart.setContent(strBody.toString(), "text/html");
		// add it
		multipart.addBodyPart(messageBodyPart);
		// Part two is attachment
         messageBodyPart = new MimeBodyPart();
         DataSource source = new FileDataSource(file);
         messageBodyPart.setDataHandler(new DataHandler(source));
         messageBodyPart.setFileName(file);
         multipart.addBodyPart(messageBodyPart);

		message.setContent(multipart);
		// Send message
		Transport.send(message);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Sent Mail message successfully....");
		valid=true;
	}catch (MessagingException mex) {
		System.out.println("Exception----"+mex.getMessage());
		mex.printStackTrace();
		valid=false;
	}catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
	return valid;

	
}


//code added by Bhavana
	public Boolean SendFeedbackEmail(String email,String satisfy,String usage,String aspect,String compare,String like,String suggestion) {

		

		boolean valid=true;
		String Color = "Blue";
		String fontColor = "Blue";
		final String password ="password123";
		final String from = "contact@mykiddytracker.com";

		Properties props =new Properties();
		props.put("mail.smtp.host", "smtp.zoho.com");  
		/*	props.put("mail.smtp.host", host);  */
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class",  
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  
	//	System.out.println(toemailID+" "+Pass);

		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(from,password);//change accordingly  
			}  
		});  

		try {
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		// Set To: header field of the header.
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("contact@primesystech.com"));
		
		// Set Subject: header field
		message.setSubject("Feedback submission");
		// This mail has 2 part, the BODY and the embedded image
		MimeMultipart multipart = new MimeMultipart("related");
		// first part (the html)
		BodyPart messageBodyPart = new MimeBodyPart();
		if(satisfy.equals("Unsatisfied") || compare.equals("Somewhat Worse"))
			Color = "Red";
		else
			Color = "Green";
		String template="<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\">"
				+ "<title>Feedback Response</title></head>"
				+ ""
				+ "<body link=\"white\" >"
				+"Dear Admin,"
				+"<ol>"
				+ "<li>Feedback Submitted by user: "+email+ "</li><br/>"
				+ "<li>User satisfaction with product/service: <span style=\"font-weight:600;color:"+Color+";\">"+satisfy+ "</li><br/>"
				+ "<li>Product/Service usage by user: <span style=\"font-weight:600;color:"+fontColor+";\">"+usage+ "</li><br/>"
				+ "<li>Aspect of the product/service user most satisfied with: <span style=\"font-weight:600;color:"+fontColor+";\">"+aspect+ "</li><br/>"
				+ "<li>Product/Service offered by our company compared with other companies: <span style=\"font-weight:600;color:"+Color+";\">"+compare+ "</li><br/>"
				+ "<li>User like about our product/service: <span style=\"font-weight:600;color:"+fontColor+";\">"+like+ "</li><br/>"
				+ "<li>Suggestions for our product quality improvement by user: <span style=\"font-weight:600;color:"+fontColor+";\">"+suggestion+ "</li><br/>"
				+"</body>"
				+ "</html>\"";
		message.setText(template);

		messageBodyPart.setContent(template, "text/html");
		// add it
		multipart.addBodyPart(messageBodyPart);


		message.setContent(multipart);
		// Send message
		Transport.send(message);


		System.out.println("Sent message successfully....");
		
		
		}catch (MessagingException mex) {
			System.out.println("Exception----"+mex.getMessage()+"------");
			valid=false;
			mex.printStackTrace();
		}
		return valid;
	}
	



	public boolean SendWrongLocationEmail(String msg) {
		// TODO Auto-generated method stub

				boolean valid=false;
				Double TotalKm=0.0;Double Totalspeed=0.0;
				final String password ="primesys@123";
				final String  from = "wronglocation33@gmail.com";
				

				Properties props =new Properties();
				 props.put("mail.smtp.host", "smtp.gmail.com");  

				/*	props.put("mail.smtp.host", "smtp.zoho.com");  
			*/	/*	props.put("mail.smtp.host", host);  */
				props.put("mail.smtp.socketFactory.port", "465");  
				props.put("mail.smtp.socketFactory.class",  
						"javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.auth", "true");  
				props.put("mail.smtp.port", "465");  
				props.put("mail.smtp.starttls.enable", "true");  
			//	System.out.println(toemailID+" "+Pass);

				Session session = Session.getDefaultInstance(props,  
						new javax.mail.Authenticator() {  
					protected PasswordAuthentication getPasswordAuthentication() {  
						return new PasswordAuthentication(from,password);//change accordingly  
					}  
				});  

				try{	Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				// Set To: header field of the header.
				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("rupesh.p@mykiddytracker.com"));
				
				InternetAddress[] toAddresses = InternetAddress.parse("chaitanya.m@primesystech.com,pradip@primesystech.com,pritam@primesystech.com");
				message.addRecipients(Message.RecipientType.CC,toAddresses);

				//message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("naveen@primesystech.com"));

				
				// Set Subject: header field
				message.setSubject("Got Wrong Location.");
				// This mail has 2 part, the BODY and the embedded image
				MimeMultipart multipart = new MimeMultipart("related");
				// first part (the html)
				BodyPart messageBodyPart = new MimeBodyPart();
				

					StringBuilder strBody = new StringBuilder(); 
					strBody.append("<!DOCTYPE html>")
					     .append("<html>")
					     .append("<head>")
					     .append("<title>Primesys Track Got Wrong Location</title>")
					     .append("</head>")
					     .append("<body>")
					     .append("")
					     .append("<div>        <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" bgcolor=\"dadada\" style=\"margin: 10.0px auto;padding: 15.0px;border: 1.0px solid rgb(107,57,108);font-family: serif , Arial;font-style: normal;font-weight: 300;font-size: 19.0px;color: rgb(255,255,255);background: #0097A7;\">          <tbody><tr>             <td width=\"100%\" align=\"left\">                 <a href=\"http://mykiddytracker.com\" style=\"float: left;\" target=\"_blank\">                     <img id=\"5287195000000943001_imgsrc_url_0\" alt=\"\" src=\"http://mykidtracker.in:81/apk/primesystrackhome.png\">                 </a>             </td>         </tr>                                   <tr>             <td width=\"100%\" align=\"left\" style=\"padding: 13.5px 27.0px;border-bottom: 1.0px solid rgb(199,199,199);\">                 <p>Hi PrimesysTrack,</p>             "
					     		+ "   <p>New Wrong Location found in our PrimesysTrack.Please go through below details to follow up issue </p>  ")
					     .append("")
					     .append("<p>Details :"+ msg+"</p>  ")

					     .append("")
					     .append("")
					     .append("</td>    ")
					     .append("</tr>      ")
					     .append("<tr>       ")
					     .append("       </tr>                  <tr>             <td width=\"100%\" align=\"left\" style=\"padding: 7.0px 27.0px;\">                 <p>Regards,</p>                 <p style=\"font-size: 20.0px;\">PrimeysTrack Team.</p>                 <p style=\"font-size: 12.0px;\">© <a href=\"http://www.primesystech.com\" target=\"_blank\">Primesys Technologies</a></p>             </td>         </tr>                  <tr>             <td width=\"100%\"></td>         </tr>     </tbody></table>    </div>")
					     .append("</body>")
					     .append("</html>");
			

				message.setText(strBody.toString());

				messageBodyPart.setContent(strBody.toString(), "text/html");
				// add it
				multipart.addBodyPart(messageBodyPart);


				message.setContent(multipart);
				// Send message
				Transport.send(message);

				valid=true;
				System.out.println("Sent message successfully....");
				
				
				}catch (MessagingException mex) {
					System.out.println("Exception----"+mex.getMessage()+"------");

					mex.printStackTrace();
				}
				return valid;
					
	}





	
	public Boolean sendIssueMailToClient(String toMail, String subject,String message, String ccMail) {
		boolean valid=false;
		final String password ="Prime@apple2020";
		final String  from = "report.primesystrack@gmail.com";
		
	   	Properties props =new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");  

		/*	props.put("mail.smtp.host", "smtp.zoho.com");  
	*/	/*	props.put("mail.smtp.host", host);  */
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class",  
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  
		props.put("mail.smtp.starttls.enable", "true");  
	//	System.out.println(toemailID+" "+Pass);

		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(from,password);//change accordingly  
			}  
		});  

		try{

			Message message1 = new MimeMessage(session);
			message1.setFrom(new InternetAddress(from));
			// Set To: header field of the header.
			message1.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toMail));
			
//			message1.setRecipient(Message.RecipientType.BCC, new InternetAddress("pt@primesystech.com"));
			if (ccMail.length()>0)
			message1.setRecipient(Message.RecipientType.CC, new InternetAddress(ccMail));
		
			// Set Subject: header field
			message1.setSubject(subject+"!"+" Device Issue Status");
			// This mail has 2 part, the BODY and the embedded image
			MimeMultipart multipart = new MimeMultipart("related");
			// first part (the html)
			BodyPart messageBodyPart = new MimeBodyPart();
			
			message1.setText(message);

			messageBodyPart.setContent(message, "text/html");
			// add it
			multipart.addBodyPart(messageBodyPart);

			message1.setContent(multipart);
			// Send message
			Transport.send(message1);

			valid=true;

			System.out.println("Sent message successfully....");
		}catch (MessagingException mex) {
			System.out.println("Exception----"+mex.getMessage()+"------");

			mex.printStackTrace();
			valid=false;
		}
		return valid;		

	}

	
	public boolean sendPaymentSubscriptionMail(String sendemail, String file, String subject) {
		boolean valid=false;
		final String password ="password123";
		final String  from = "contact@primesystrack.com";
	

		Properties props =new Properties();
		props.put("mail.smtp.host", "smtp.zoho.com");  
		/*	props.put("mail.smtp.host", host);  */
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class",  
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  

		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(from,password);//change accordingly  
			}  
		});  

		try{

			Message message1 = new MimeMessage(session);
			message1.setFrom(new InternetAddress(from));
			// Set To: header field of the header.
			message1.setRecipients(Message.RecipientType.TO,InternetAddress.parse(sendemail));
			
			//message1.setRecipient(Message.RecipientType.BCC, new InternetAddress("pt@primesystech.com"));
			
		
			// Set Subject: header field
			message1.setSubject(subject+"!"+" Your Subscription is expired soon");
			// This mail has 2 part, the BODY and the embedded image
			MimeMultipart multipart = new MimeMultipart("related");
			// first part (the html)	
			StringBuilder strBody = new StringBuilder(); 
			strBody.append("Please find the attachment for subcription expire soon."); 
			
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			message1.setText(strBody.toString());
			messageBodyPart.setContent(strBody.toString(), "text/html");
			// add it
			multipart.addBodyPart(messageBodyPart);
			// Part two is attachment
	         messageBodyPart = new MimeBodyPart();
	         DataSource source = new FileDataSource(file);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(file);
	         multipart.addBodyPart(messageBodyPart);

	         message1.setContent(multipart);
			// Send message
			Transport.send(message1);

			valid=true;

			System.out.println("Sent message successfully....");
		}catch (MessagingException mex) {
			System.out.println("Exception----"+mex.getMessage()+"------");

			mex.printStackTrace();
			valid=false;
		}
		return valid;		

	}



}
