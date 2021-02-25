package dao;

import java.sql.Connection;
import java.util.ArrayList;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoException;

import dto.MessageObject;

public class RDPSUploadDAO {

	public MessageObject addSamstipurSlowModeTrainAddressFromCSV(
			DB mongoconnection, String filename, String railWay,
			String division, String station_From, String station_TO,
			String chainage, String trolley, String line, String mode,
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
			
			 /* station_From=station_From.substring(station_From.indexOf("[") +
			  1); 
			  station_From=station_From.substring(0,
			  station_From.indexOf("]"));
			  
			 station_TO=station_TO.substring(station_TO.indexOf("[") + 1);
			  station_TO=station_TO.substring(0, station_TO.indexOf("]"));
			 */

				for (int i = 0; i < kiloMeter.size(); i++) {
				/*	if (!feature_Code.get(i).trim().equalsIgnoreCase("48")&&
							!feature_Detail.get(i).trim().contains("Auto Record")) {*/

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
							.trim()).replace(",", "").replace("$", ",")+" "+section_Detail.get(i));
					
					if (feature_Code.get(i).trim().replace(",", "")
							.equals("3")||feature_Detail.get(i).trim().replace(",", "").toLowerCase()
							.contains("station")) {
						addresDocument.put("feature_image",
								"~/Images/FeatureCodePhoto/fc_12.png");
						addresDocument.put("feature_code", "12");
						
					} else if (feature_Code.get(i).trim().replace(",", "")
							.equals("22")||feature_Detail.get(i).trim().replace(",", "").toLowerCase().toLowerCase()
							.contains("rub/lhs")) {
						addresDocument.put("feature_image",
								"~/Images/FeatureCodePhoto/fc_107.png");
						addresDocument.put("feature_code", "107");
						
					} else if (feature_Code.get(i).trim().replace(",", "")
							.equals("24")||feature_Code.get(i).trim().replace(",", "")
							.equals("25")||feature_Detail.get(i).trim().replace(",", "").toLowerCase()
							.contains("bridge(minor)")) {
						addresDocument.put("feature_image",
								"~/Images/FeatureCodePhoto/fc_113.png");
						addresDocument.put("feature_code", "113");
						
					} else if (feature_Code.get(i).trim().replace(",", "")
							.equals("20")||feature_Code.get(i).trim().replace(",", "")
							.equals("21")||feature_Detail.get(i).trim().replace(",", "").toLowerCase()
							.toLowerCase().contains("bridge")||
							feature_Detail.get(i).trim().replace(",", "").toLowerCase()
							.toLowerCase().contains("rob")
							) {
						addresDocument.put("feature_image",
								"~/Images/FeatureCodePhoto/fc_42.png");
						addresDocument.put("feature_code", "42");
						
					} else if (feature_Code.get(i).trim().replace(",", "")
							.equals("36")||feature_Detail.get(i).trim().replace(",", "").toLowerCase()
							.contains("gang/keyman")
							) {
						addresDocument.put("feature_image",
								"~/Images/FeatureCodePhoto/fc_101.png");
						addresDocument.put("feature_code", "101");
						
					} else if ((feature_Code.get(i).trim().replace(",", "")
							.equals("1")||feature_Detail.get(i).trim().replace(",", "")
							.toLowerCase().contains("km post")&& Double.parseDouble(distance.get(i).trim()
									.replace(",", "")) == 0)) {
						addresDocument.put("feature_image",
								"~/Images/FeatureCodePhoto/fc_150.png");
						addresDocument.put("feature_code", "150");
						
					} else if (feature_Code.get(i).trim().replace(",", "")
							.equals("1")||feature_Detail.get(i).trim().replace(",", "")
							.toLowerCase().contains("new km")
							&& Double.parseDouble(distance.get(i).trim()
									.replace(",", "")) == 0) {
						addresDocument.put("feature_image",
								"~/Images/FeatureCodePhoto/fc_150.png");
						addresDocument.put("feature_code", "150");
						
					} else if (feature_Code.get(i).trim().replace(",", "")
							.equals("2")||feature_Detail.get(i).trim().replace(",", "").toLowerCase()
							.contains("ohe")) {
						addresDocument.put("feature_image",
								"~/Images/FeatureCodePhoto/fc_2.png");
						addresDocument.put("feature_code", "2");
						
					} else if (feature_Code.get(i).trim().replace(",", "")
							.equals("26")||feature_Detail.get(i).trim().toLowerCase().contains("level")
							||feature_Detail.get(i).trim().toLowerCase().contains("lc")) {
						addresDocument.put("feature_image",
								"~/Images/FeatureCodePhoto/fc__4.png");
						addresDocument.put("feature_code", "4");
						
					}
					
					
					else if (feature_Code.get(i).trim().replace(",", "")
							.equals("12")||feature_Code.get(i).trim().replace(",", "")
							.equals("13")||feature_Detail.get(i).trim().toLowerCase().contains("circular")) {
						addresDocument.put("feature_image",
								"~/Images/FeatureCodePhoto/fc_102.png");
						addresDocument.put("feature_code", "102");
						
					} else if (feature_Code.get(i).trim().replace(",", "")
							.equals("10")||feature_Code.get(i).trim().replace(",", "")
							.equals("11")||feature_Detail.get(i).trim().toLowerCase().contains("curve")) {
						addresDocument.put("feature_image",
								"~/Images/FeatureCodePhoto/fc_102.png");
						addresDocument.put("feature_code", "102");
						
					} else if (feature_Code.get(i).trim().replace(",", "")
							.equals("36")||feature_Detail.get(i).trim().toLowerCase().contains("gang/hd")) {
						addresDocument.put("feature_image",
								"~/Images/FeatureCodePhoto/fc_103.png");
						addresDocument.put("feature_code", "103");
						
					} else if (feature_Code.get(i).trim().replace(",", "")
							.equals("47")||feature_Detail.get(i).trim().toLowerCase().contains("switch")) {
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
						
					} else if (feature_Code.get(i).trim().replace(",", "")
							.equals("6")||feature_Detail.get(i).trim().toLowerCase().contains("cabin")) {
						addresDocument.put("feature_image",
								"~/Images/FeatureCodePhoto/fc_109.png");
						addresDocument.put("feature_code", "109");
						
					} else if (feature_Code.get(i).trim().replace(",", "")
							.equals("25")||feature_Detail.get(i).trim().toLowerCase().contains("fob")) {
						addresDocument
						.put("feature_image",
								"~/Images/FeatureCodePhoto/ic_default_featurecode.png");
						addresDocument.put("feature_code", "110");
						
					} else if (feature_Code.get(i).trim().replace(",", "")
							.equals("24")||feature_Detail.get(i).trim().toLowerCase().contains("joint")
							) {
								addresDocument.put("feature_image",
										"~/Images/FeatureCodePhoto/fc_112.png");
								addresDocument.put("feature_code", "112");
								
							}else if (feature_Code.get(i).trim().replace(",", "")
									.equals("120")||feature_Detail.get(i).trim().toLowerCase().contains("beat")
									) {
										addresDocument.put("feature_image",
												"~/Images/FeatureCodePhoto/fc_120.png");
										addresDocument.put("feature_code", "120");
										
									} else {
								addresDocument
								.put("feature_image",
										"~/Images/FeatureCodePhoto/ic_default_featurecode.png");
								addresDocument.put("feature_code", "999");

							}
							
							addresDocument.put("section",section_Detail.get(i));
							/*if (section_Detail.size()>0&&section_Detail.get(i).trim().length()>0) {
					addresDocument.put("section",
							section_Detail.get(i).trim());
				}else{
					addresDocument.put("section","ANA-DMO");
					addresDocument.put("section",	station_From.replace("SECTION", "").replace(",", "")
									.replace("(Ex)", "").replace(" ", ""));
				}
							 */
							addresDocument.put("block_section", "");
							// addresDocument.put("remark",remark_array.get(i).trim());
							addres_list.add(addresDocument);
							
				//}iff end
			}
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

	public MessageObject SaveHierarchyWithCreateLogin(
				Connection con, DB mongoconnection, String[] nameArray,
				String[] mobileNoArray, String[] emailIdArray,
				String[] parenrtIdArray, String[] studentsArray) {
			
			MessageObject msg=new MessageObject();
			int rows=nameArray.length;
			if(rows==nameArray.length&&rows==mobileNoArray.length&&rows==emailIdArray.length&&rows==
					parenrtIdArray.length&&rows==studentsArray.length)
			{
				

					for(int i=0;i<nameArray.length;i++)
					 try {
		
							// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
							java.sql.CallableStatement ps = con
									.prepareCall("{call SaveTrackCustomhierarchyUserNew_Add_in_hierachy(?,?,?,?,?)}");
		
							
							ps.setString(1, nameArray[i]);
							ps.setString(2,mobileNoArray[i]);
							ps.setString(3, emailIdArray[i]);
		
							ps.setString(4,parenrtIdArray[i]);
		
							ps.setString(5, studentsArray[i]);
						
							int result = ps.executeUpdate();
							if (result == 0) {
								msg.setError("true");
								msg.setMessage("SaveHiearachyUserWithCreateLogin not Created");
							} else {
								// //System.err.println("Error=="+result);
								msg.setError("false");
								msg.setMessage("SaveHiearachyUserWithCreateLogin Created Successfully");
							}
						} catch (Exception e) {
							e.printStackTrace();
							msg.setError("true");
							msg.setMessage("SaveHiearachyUserWithCreateLogin not Created.Plesetry agin");
						}
			}else{
				msg.setError("true");
				msg.setMessage("SaveHiearachyUserWithCreateLogin not Created.Plesetry check input numbers of each Array");
			}
			return msg;
	
	}

}
