package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;

import dto.MessageObject;
import dto.StationaryDataDTO;
import dto.StationaryPurchaseItemInfo;
import dto.StationarySpinnerDTO;
import SQLdto.CategoryDetailsDTO;

public class StationarySpinnerdataDAO {

	public ArrayList<StationarySpinnerDTO> GetStationarySpinnerdata(Connection con, String flag, String id, String schoolid) {
		// TODO Auto-generated method stub
		ArrayList<StationarySpinnerDTO> list=new ArrayList<StationarySpinnerDTO>();
		java.sql.CallableStatement cal=null;
		try{

			
			
		/*	System.err.println("Userid: -------hhh------>"+user_id);
			PreparedStatement pcal=connection.prepareStatement("select cat.CategoryName as categoryname,usr.ExpiryDate,usr.Category_Desc as categorydesc,cat.Photo as categoryImage,usr.Created_by as createdby,usr.Quiz_Id as quizid,usr.School_Id as schoolid from dbo.Category cat inner join UserQuizMap usr on usr.Category = cat.id where usr.UserId = (select UserID from UserLoginMaster where Link_ID=?) ");
			pcal.setString(1, user_id);
			ResultSet rsResultSet=pcal.executeQuery();
			*/
			
			
			if (flag.equalsIgnoreCase("Category")) {
				cal=con.prepareCall("{ call GetStaCategory(?,?) }");
				cal.setInt(1, Integer.parseInt(id));
				cal.setInt(2, Integer.parseInt(schoolid));

			}
			else if (flag.equalsIgnoreCase("SubCategory")) {
				
				cal=con.prepareCall("{ call GetStaSubCategory(?,?) }");
				cal.setInt(1, Integer.parseInt(id));
				cal.setInt(2, Integer.parseInt(schoolid));

			}else if (flag.equalsIgnoreCase("ClassId")) {
				
				cal=con.prepareCall("{ call GetClassID(?) }");
				cal.setInt(1, Integer.parseInt(id));
				
			}else if (flag.equalsIgnoreCase("Mesurement")) {
			
				cal=con.prepareCall("{ call GetMeasurement(?,?) }");
				cal.setInt(1, Integer.parseInt(id));
				cal.setInt(2, Integer.parseInt(schoolid));

			}else if (flag.equalsIgnoreCase("Dimension")) {
				
				cal=con.prepareCall("{ call GetDimension(?,?) }");
				cal.setInt(1, Integer.parseInt(id));
				cal.setInt(2, Integer.parseInt(schoolid));

			}else if (flag.equalsIgnoreCase("Color")) {
				
				cal=con.prepareCall("{ call GetStationary(?,?) }");
				cal.setInt(1, Integer.parseInt(id));
				cal.setInt(2, Integer.parseInt(schoolid));

			}else if (flag.equalsIgnoreCase("Subject")) {
				
				cal=con.prepareCall("{ call GetSubjectPur(?,?) }");
				cal.setInt(1, Integer.parseInt(schoolid));

			}
			
			ResultSet rsResultSet =cal.executeQuery();
		
		if(rsResultSet!=null)	{	
				while (rsResultSet.next()) {
					StationarySpinnerDTO dto=new StationarySpinnerDTO();

					if (flag.equalsIgnoreCase("Category")) {
						dto.setEntityId(""+rsResultSet.getString("Category_ID"));
						dto.setEntityname(""+rsResultSet.getString("Category_Name"));
					}
					else if (flag.equalsIgnoreCase("SubCategory")) {
						
						dto.setEntityId(""+rsResultSet.getString("Sub_Category_id"));
						dto.setEntityname(""+rsResultSet.getString("Sub_category_name"));
						
					}else if (flag.equalsIgnoreCase("ClassId")) {
						
						dto.setEntityId(""+rsResultSet.getString("ClassID"));
						dto.setEntityname(""+rsResultSet.getString("ClassName"));
						
					}else if (flag.equalsIgnoreCase("mesurement")) {
					
						dto.setEntityId(""+rsResultSet.getString("Measure_unit_id"));
						dto.setEntityname(""+rsResultSet.getString("Measure_Unit_Name"));
						
					}else if (flag.equalsIgnoreCase("Dimension")) {
						
						dto.setEntityId(""+rsResultSet.getString("Dimension_id"));
						dto.setEntityname(""+rsResultSet.getString("Dimension_Name"));
						
					}else if (flag.equalsIgnoreCase("Color")) {
						
						dto.setEntityId(""+rsResultSet.getString("Color_Id"));
						dto.setEntityname(""+rsResultSet.getString("Color_Name"));
					}
					else if (flag.equalsIgnoreCase("Subject")) {
						
						dto.setEntityId(""+rsResultSet.getString("Item_Type_id"));
						dto.setEntityname(""+rsResultSet.getString("Subject"));
					}

				
					list.add(dto);

				}
				System.err.println("GetCategory list: --------"+list.toString());


			}




		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public StationaryPurchaseItemInfo GetItemforSubject(
			Connection con, String cat_id, String subcat_id, String class_id,
			String subject_id) {
		StationaryPurchaseItemInfo msgObj=new StationaryPurchaseItemInfo();
		try
		{	ResultSet rs=null;
		//Made changes in select for email;
			java.sql.CallableStatement stmt= con.prepareCall("{call GetItemforSubject(?,?,?,?)}");
		

			stmt.setString(1,cat_id);
			stmt.setString(2,subcat_id);
			stmt.setString(3,class_id);
			stmt.setString(4,subject_id);
			
			 rs = stmt.executeQuery();

				if(rs.next())
				{
					System.out.println("ITem--------"+rs.getString("Available_Quantity")+" --"+msgObj.getSellingPrice());

					 msgObj.setAvialQty(rs.getString("Available_Quantity"));
				 
					 msgObj.setItemTypeId(rs.getString("Item_Type_id"));

						msgObj.setSellingPrice(rs.getString("Selling_Price"));
						msgObj.setError("flase");
				
						
				 
				}
				else
				{
					msgObj.setError("true");
				}
				
		
		}catch(Exception e)	
		{	e.printStackTrace();}
		finally
		{
			try {
				con.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("ITEEM MAster--------"+msgObj.getAvialQty()+" --"+msgObj.getSellingPrice());
		return msgObj;	
	}

	public StationaryPurchaseItemInfo GetItemforCustomer(
			Connection con, String cat_id, String subcat_id, String mesure_id,
			String dimen_id, String color_id) {
		StationaryPurchaseItemInfo msgObj=new StationaryPurchaseItemInfo();
		try
		{	ResultSet rs=null;
		//Made changes in select for email;
			java.sql.CallableStatement stmt= con.prepareCall("{call GetItemforCustomer(?,?,?,?,?)}");
		

			stmt.setString(1,cat_id);
			stmt.setString(2,subcat_id);
			stmt.setString(3,mesure_id);
			stmt.setString(4,dimen_id);
			stmt.setString(5,color_id);

			 rs = stmt.executeQuery();
				if(rs.next())
				{
					System.out.println("ITem--------"+rs.getString("Available_Quantity")+" --"+msgObj.getSellingPrice());

					 msgObj.setAvialQty(rs.getString("Available_Quantity"));
					 msgObj.setItemTypeId(rs.getString("Item_Type_id"));

						
						msgObj.setSellingPrice(rs.getString("Selling_Price"));
						msgObj.setError("flase");
				
						
				 
				}
				else
				{
					msgObj.setError("true");
				}
				
		
		}catch(Exception e)	
		{	e.printStackTrace();}
		finally
		{
			try {
				con.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return msgObj;
	}

	public MessageObject SaveStationaryPayment(Connection con, String userid,
			String totalamount, String paymentmode, String paymentstatus,
			String orderid, String itemtypeid, String quantity,
			String itemamount) {
		MessageObject msgObj = new MessageObject();
		java.sql.CallableStatement stmt;
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String now =dateFormat.format(cal.getTime());
		try {
			stmt = con.prepareCall("{call SaveStationaryPaymentAPP(?,?,?,?,?,?,?,?,?,?)}");
			stmt.setString(1,userid);
			stmt.setString(2,totalamount);
			stmt.setString(3,paymentmode);
			stmt.setString(4,paymentstatus);
			stmt.setString(5,orderid);
			stmt.setString(6,userid);
			stmt.setString(7,now);
			stmt.setString(8,itemtypeid);
			stmt.setString(9,quantity);
			stmt.setString(10,itemamount);
			
			int result = stmt.executeUpdate();
			
			if (result==0) {
				msgObj.setError("true");
				msgObj.setMessage("Payment Failed");
			}else{
			//	System.err.println("Error=="+result);
				msgObj.setError("false"); 
				msgObj.setMessage("Payment generated Successfully");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return msgObj;
	}

	public ArrayList<StationaryDataDTO> GetStationatydata(Connection con,
			String userid) {
		ArrayList<StationaryDataDTO> msgObj=new ArrayList<StationaryDataDTO>();
		try
		{	ResultSet rs=null;
		//Made changes in select for email;
			java.sql.CallableStatement stmt= con.prepareCall("{call GetUserStaPaymentAPP(?)}");
		

			stmt.setString(1,userid);
			
			 rs = stmt.executeQuery();
				if(rs!=null)
				{
					while (rs.next()) {
						
						StationaryDataDTO data=new StationaryDataDTO();
						
						data.setUserName(rs.getString("UserName"));
						data.setTotal_Transaction_amount(rs.getString("Total_Transaction_amount"));

							
						data.setItemAmount(rs.getString("ItemAmount"));
						data.setPayment_status(rs.getString("status"));

						data.setBankTransActionID(rs.getString("BankTransActionID"));
						data.setTransaction_date(rs.getString("Transaction_date"));

							
						data.setCreated_at(rs.getString("Created_at"));
						data.setPayment_mode(rs.getString("Payment_mode"));
						data.setPayment_Transaction_Id(rs.getString("Payment_Transaction_Id"));
						
						System.err.println(rs.getString("Payment_Transaction_Id"));
						msgObj.add(data);

					}
					
						

				}
				
				
		
		}catch(Exception e)	
		{	e.printStackTrace();}
		finally
		{
			try {
				con.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return msgObj;
	}

	

}
