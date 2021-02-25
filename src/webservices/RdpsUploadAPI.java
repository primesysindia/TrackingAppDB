package webservices;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import model.APIController;
import Utility.CoordinateConversion;

import com.google.gson.Gson;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import dto.MessageObject;
import dto.WrongLocationDataDTO;

@Path("RdpsUploadAPI")

public class RdpsUploadAPI {
	
	
CoordinateConversion mCoordinateConversion=new CoordinateConversion();
	@POST
	@Path("addRanchiTrainAddressFromCSV")
	@Produces("application/json")
	public String addRanchiTrainAddressFromCSV(@FormParam("filePath") String filePath,
			@FormParam("ParentId") String ParentId)
	{
		
		  
		
			String feeds  = null;
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();
	    System.out.println("folder---- " + folder.getName());

		for (int fileCount = 0; fileCount< listOfFiles.length;fileCount++) {
			
			
		  int j;
		if (listOfFiles[fileCount].isFile()) {
		    System.out.println("File " + listOfFiles[fileCount].getName());
		    
						//System.out.println("addRanchiTrainAddressFromCSV"); 
						
						
						try { 
						// Create object of filereader 
						// class with csv file as parameter. 
						FileReader filereader = new FileReader(filePath+listOfFiles[fileCount].getName()); 
						
						// create csvParser object with 
						// custom seperator semi-colon 
						CSVParser parser = new CSVParserBuilder().withSeparator(',').build(); 
						
						// create csvReader object with 
						// parameter filereader and parser 
						CSVReader csvReader = new CSVReaderBuilder(filereader) 
						                          .withCSVParser(parser) 
						                          .build(); 
						
						// Read all data at once 
						List<String[]> allData = csvReader.readAll(); 
						int i=0;
						
						if (allData.get(0)[0].toString().length()>0) 
							  i=1;
						
						// int i=1;
						//System.err.print("--allData get 00--"+allData.get(0)[0].toString()+"---"+allData.get(0)[0].toString().length()); 
						
						String filename="",RailWay="",Division="",station_From="",Station_TO="",Chainage ="",Trolley="",
								Line="",Mode="";
								
								ArrayList<String>	KiloMeter = new ArrayList<>(),Distance = new ArrayList<>(),Latitude = new ArrayList<>(),Longitude = new ArrayList<>(),Feature_Code = new ArrayList<>(),Feature_Detail=new ArrayList<>();
								 ArrayList<String>	Section_Detail=new ArrayList<>();
						// print Data 
						for (String[] row : allData) { 
								StringBuilder sbrow=new StringBuilder();
						
						    for (String cell : row) { 
						      //  System.out.print(cell + "$"); 
						        	sbrow.append(cell+",");
						
						    } 
						
						  int z;
						if(i==1)
							   filename=sbrow.toString();
						
						   else if(i==2)
							   Trolley=sbrow.toString();
						    else if(i==3)
						    	z=0;
						   else if(i==4)
							   RailWay=sbrow.toString();
						   else if(i==5)
							   Division=sbrow.toString();
							 
						   else if(i==6)
							   station_From=sbrow.toString();
						   else if(i==7)
							   Station_TO=sbrow.toString();

						   else if(i==8)
							   Line=sbrow.toString();
						   else if(i==9)
					    	z=0;
						   else if(i==10)
							   Mode=sbrow.toString();
						   else if(i==11)
						    	z=0;
						 else if(i==12)
						    	z=0;

						   else {
						       // System.err.println(sbrow.toString()); 
						
							   String[] rdpsData=sbrow.toString().split(",");
						     //   System.err.println(rdpsData.length); 
							   if(rdpsData.length>4&&rdpsData[0].trim().length()>0&&rdpsData[2].trim().length()>0&&rdpsData[3].trim().length()>0)
						        	   {
								   
								   if (rdpsData[0].length()>0)
						        	   KiloMeter.add(rdpsData[0]);
						        	   
						        	   if (rdpsData[1].length()>0)
						        	   Distance.add(rdpsData[1]);
						        	
						
						        	   if (rdpsData[2].length()>0)
						        	   {
						        		//  System.err.println("rdpsData[2]-lat----"+rdpsData[2]+"---"+i); 
						        		  // 22° 57'  27.630"N

						        		   // 25�20'42.8730"N
						            	   String[] lat_hr=rdpsData[2].split("�");
						            	   String[] lat_min=lat_hr[1].split("'");
							        		//  System.err.println("rdpsData[2]-lat_hr----"+lat_hr+"---"+i); 
							        		//  System.err.println("rdpsData[2]-lat_min----"+lat_min+"---"+i); 

						            	   double decimal = ((Integer.parseInt(lat_min[0].trim()) *60)+Double.parseDouble( lat_min[1].substring(0, lat_min[1].indexOf("N")-1).trim()))/ 3600;
						            
						        		   Latitude.add((Integer.parseInt(lat_hr[0].trim())+decimal)+"");
						
						        	   }
						        	   
						        	   if (rdpsData[3].length()>0)
						        	   {
						        		  // System.err.println("rdpsData[3]-lon----"+rdpsData[3]); 

						        		   // 25�20'42.8730"N
						        		   // 086° 04'  59.844"E
						            	   String[] lat_hr=rdpsData[3].split("�");
						            	   String[] lat_min=lat_hr[1].split("'");
						            	   double decimal = ((Integer.parseInt(lat_min[0].trim()) *60)+(Double.parseDouble( lat_min[1].substring(0, lat_min[1].indexOf("E")-1).trim())))/ (3600);
						      
						        		   Longitude.add((Integer.parseInt(lat_hr[0].trim())+decimal)+"");
						        	   }
						        	   
						        	   if (rdpsData[4].length()>0)
						        	   Feature_Code.add(rdpsData[4].trim());
						        	   else if (rdpsData[0].length()>0) 
						        		   Feature_Code.add("999");
						
						        	   if (rdpsData[5].length()>0)
						        	   Feature_Detail.add(rdpsData[5].trim());
						        	   Section_Detail.add(folder.getName());

						        	   
						        }
							
						}
						
						   // System.out.println(); 
						    i++;
						}
						
						System.out.println(KiloMeter.size()+"-"+Distance.size()+"-"+Latitude.size()+"-"+Longitude.size()+"-"+Feature_Code.size()+"-"+Feature_Detail.size()+"-"+Section_Detail.size()); 

						j=0;
						MessageObject msgData = new MessageObject ();
					
						APIController handler= new APIController();
						msgData=handler.addRanchiTrainAddressFromCSV(filename,RailWay,Division,station_From,Station_TO,Chainage,Trolley,
								Line,Mode,KiloMeter,Distance,Latitude,Longitude,Feature_Code,Feature_Detail,ParentId,Section_Detail);
						Gson gson = new Gson();
						feeds = gson.toJson(msgData);
						
						} 
						catch (Exception e) { 
						e.printStackTrace(); 
						System.out.println(e.getMessage());
						
						}
						
		    
		  } else if (listOfFiles[fileCount].isDirectory()) {
		    System.out.println("Directory " + listOfFiles[fileCount].getName());
		    
		    

			File folder1 = new File(filePath+listOfFiles[fileCount].getName()+"/");
			File[] listOfFiles1 = folder1.listFiles();

			for (int fileCount1 = 0; fileCount1< listOfFiles1.length;fileCount1++) {
			  if (listOfFiles1[fileCount1].isFile()) {
			    System.out.println("File " + listOfFiles1[fileCount1].getName());
			    
							System.out.println("addRanchiTrainAddressFromCSV-----file---"+folder1+listOfFiles1[fileCount1].getName()); 
							
							try { 
								// Create object of filereader 
								// class with csv file as parameter. 
								FileReader filereader = new FileReader(filePath+listOfFiles[fileCount].getName()); 
								
								// create csvParser object with 
								// custom seperator semi-colon 
								CSVParser parser = new CSVParserBuilder().withSeparator(',').build(); 
								
								// create csvReader object with 
								// parameter filereader and parser 
								CSVReader csvReader = new CSVReaderBuilder(filereader) 
								                          .withCSVParser(parser) 
								                          .build(); 
								
								// Read all data at once 
								List<String[]> allData = csvReader.readAll(); 
								int i=0;
								
								if (allData.get(0)[0].toString().length()>0) 
									  i=1;
								
								// int i=1;
								//System.err.print("--allData get 00--"+allData.get(0)[0].toString()+"---"+allData.get(0)[0].toString().length()); 
								
								String filename="",RailWay="",Division="",station_From="",Station_TO="",Chainage ="",Trolley="",
										Line="",Mode="";
										
										ArrayList<String>	KiloMeter = new ArrayList<>(),Distance = new ArrayList<>(),Latitude = new ArrayList<>(),Longitude = new ArrayList<>(),Feature_Code = new ArrayList<>(),Feature_Detail=new ArrayList<>();
										 ArrayList<String>	Section_Detail=new ArrayList<>();
								// print Data 
								for (String[] row : allData) { 
										StringBuilder sbrow=new StringBuilder();
								
								    for (String cell : row) { 
								      //  System.out.print(cell + "$"); 
								        	sbrow.append(cell+",");
								
								    } 
								
								  int z;
								if(i==1)
									   filename=sbrow.toString();
								
								   else if(i==2)
									   Trolley=sbrow.toString();
								    else if(i==3)
								    	z=0;
								   else if(i==4)
									   RailWay=sbrow.toString();
								   else if(i==5)
									   Division=sbrow.toString();
									 
								   else if(i==6)
									   station_From=sbrow.toString();
								   else if(i==7)
									   Station_TO=sbrow.toString();

								   else if(i==8)
									   Line=sbrow.toString();
								   else if(i==9)
							    	 z=0;
								   else if(i==10)
									   Mode=sbrow.toString();
								   else if(i==11)
								    	z=0;
								 else if(i==12)
								    	z=0;

								   else {
								       // System.err.println(sbrow.toString()); 
								
									   String[] rdpsData=sbrow.toString().split(",");
								     //   System.err.println(rdpsData.length); 
									   if(rdpsData.length>4&&rdpsData[0].trim().length()>0&&rdpsData[2].trim().length()>0&&rdpsData[3].trim().length()>0)
								        	   {
										   
										   if (rdpsData[0].length()>0)
								        	   KiloMeter.add(rdpsData[0]);
								        	   
								        	   if (rdpsData[1].length()>0)
								        	   Distance.add(rdpsData[1]);
								        	
								
								        	   if (rdpsData[2].length()>0)
								        	   {
								        		//   System.err.println("rdpsData[2]-lat----"+rdpsData[2]); 
								        		   // 25�20'42.8730"N
								            	   String[] lat_hr=rdpsData[2].split("�");
								            	   String[] lat_min=lat_hr[1].split("'");
								       
								            	   double decimal = ((Integer.parseInt(lat_min[0].trim()) *60)+Double.parseDouble( lat_min[1].substring(0, lat_min[1].indexOf("N")-1).trim()))/ 3600;
								            
								        		   Latitude.add((Integer.parseInt(lat_hr[0].trim())+decimal)+"");
								
								        	   }
								        	   
								        	   if (rdpsData[3].length()>0)
								        	   {
								        		   // 25�20'42.8730"N
								            	   String[] lat_hr=rdpsData[3].split("�");
								            	   String[] lat_min=lat_hr[1].split("'");
								            	   double decimal = ((Integer.parseInt(lat_min[0].trim()) *60)+(Double.parseDouble( lat_min[1].substring(0, lat_min[1].indexOf("E")-1).trim())))/ (3600);
								      
								        		   Longitude.add((Integer.parseInt(lat_hr[0].trim())+decimal)+"");
								        	   }
								        	   
								        	   if (rdpsData[4].length()>0)
								        	   Feature_Code.add(rdpsData[4].trim());
								        	   else if (rdpsData[0].length()>0) 
								        		   Feature_Code.add("999");
								
								        	   if (rdpsData[5].length()>0)
								        	   Feature_Detail.add(rdpsData[5].trim());
								        	
								        	   Section_Detail.add(folder.getName());
								        	   
								        }
									
								}
								
								   // System.out.println(); 
								    i++;
								}
								
								System.out.println(KiloMeter.size()+"-"+Distance.size()+"-"+Latitude.size()+"-"+Longitude.size()+"-"+Feature_Code.size()+"-"+Feature_Detail.size()+"-"+Section_Detail.size()); 
								
								j=0;
								MessageObject msgData = new MessageObject ();
								
								APIController handler= new APIController();
								msgData=handler.addRanchiTrainAddressFromCSV(filename,RailWay,Division,station_From,Station_TO,Chainage,Trolley,
										Line,Mode,KiloMeter,Distance,Latitude,Longitude,Feature_Code,Feature_Detail,ParentId,Section_Detail);
								Gson gson = new Gson();
								feeds = gson.toJson(msgData);
								
								} 
								catch (Exception e) { 
								e.printStackTrace(); 
								System.out.println(e.getMessage());
								
								}
		    
		    
		  }
		}
		  
		  
		  }
		 System.out.println("Reposn --"+feeds);
		}
		return feeds;
		
		
	
	}
	@POST
	@Path("addVaransiTrainAddressFromCSV")
	@Produces("application/json")
	public String addVaransiTrainAddressFromCSV(@FormParam("filePath") String filePath,
			@FormParam("ParentId") String ParentId)
	{
		
		  
		
			String feeds  = null;
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();

		for (int fileCount = 0; fileCount< listOfFiles.length;fileCount++) {
		  int j;
		if (listOfFiles[fileCount].isFile()) {
		    System.out.println("File " + listOfFiles[fileCount].getName());
		    
						//System.out.println("addVaransiTrainAddressFromCSV"); 
						
						
						try { 
						// Create object of filereader 
						// class with csv file as parameter. 
						FileReader filereader = new FileReader(filePath+listOfFiles[fileCount].getName()); 
						
						// create csvParser object with 
						// custom seperator semi-colon 
						CSVParser parser = new CSVParserBuilder().withSeparator(',').build(); 
						
						// create csvReader object with 
						// parameter filereader and parser 
						CSVReader csvReader = new CSVReaderBuilder(filereader) 
						                          .withCSVParser(parser) 
						                          .build(); 
						
						// Read all data at once 
						List<String[]> allData = csvReader.readAll(); 
						int i=0;
						
						if (allData.get(0)[0].toString().length()>0) 
							  i=1;
						
						// int i=1;
						//System.err.print("--allData get 00--"+allData.get(0)[0].toString()+"---"+allData.get(0)[0].toString().length()); 
						
						String filename="",RailWay="",Division="",station_From="",Station_TO="",Chainage ="",Trolley="",
								Line="",Mode="";
								
								ArrayList<String>	KiloMeter = new ArrayList<>(),Distance = new ArrayList<>(),Latitude = new ArrayList<>(),Longitude = new ArrayList<>(),Feature_Code = new ArrayList<>(),Feature_Detail=new ArrayList<>();
								 ArrayList<String>	Section_Detail=new ArrayList<>();
						// print Data 
						for (String[] row : allData) { 
								StringBuilder sbrow=new StringBuilder();
						
						    for (String cell : row) { 
						      //  System.out.print(cell + "$"); 
						        	sbrow.append(cell+",");
						
						    } 
						
						  if(i==1)
							   filename=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						
						   else if(i==2)
							   RailWay=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==3)
							   Division=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==4)
							   station_From=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==5)
							   Station_TO=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==6)
							   Chainage=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==7)
							   Trolley=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==8)
							   Line=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==9)
							   Mode=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==10)
						      System.out.println("Headerrrrrrrrrrr\t"+filename); 
						  /*  if(i==1)
						    	RailWay=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							
							   else if(i==2)
								   Division=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==3)
								   station_From=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1).replace("SECTION", "");
							   else if(i==4)
								   Station_TO=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==5)
								   Station_TO=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==6)
								   Chainage=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==7)
								   Trolley=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==8)
								   Line=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==9)
								   Mode=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==10)
							      System.out.print("Headerrrrrrrrrrr\t"); */
						   else {
						       // System.err.println(sbrow.toString()); 
						
							   try{
							   String[] rdpsData=sbrow.toString().split(",");
						     //   System.err.println(rdpsData.length); 
							   if(rdpsData.length>4&&rdpsData[0].trim().length()>0&&rdpsData[2].trim().length()>0&&rdpsData[3].trim().length()>0)
						        	   {
								   
								   if (rdpsData[0].length()>0)
						        	   KiloMeter.add(rdpsData[0]);
						        	   
						        	   if (rdpsData[1].length()>0)
						        	   Distance.add(rdpsData[1]);
						        	
						
						        	   if (rdpsData[2].length()>0)
						        	   {
//						        		   System.err.println("rdpsData[2]-lat----"+rdpsData[2]); 
						        		   // 25�20'42.8730"N
						            	   String[] lat_hr=rdpsData[2].split("�");
//						            	   String[] lat_hr=rdpsData[2].split("°");
						            	   String[] lat_min=lat_hr[1].split("'");
////						        		   System.err.println("rdpsData[2]-lat_hr----"+lat_hr.length); 
//						        		   System.err.println("rdpsData[2]-lat_min----"+lat_min.length); 
//						            	   System.err.println(lat_min[1].trim());
//						            	   System.err.println(lat_min[1].trim().substring(0, (lat_min[1].trim().indexOf("N"))-1));
//						            	   
						            	   double decimal = ((Integer.parseInt(lat_min[0].trim()) *60)+Double.parseDouble( lat_min[1].trim().substring(0, (lat_min[1].replace("\"", "").trim().indexOf("N"))-1).trim()))/ 3600;
						            	 //  answer = degrees + decimal
						            	   //System.err.println("Decimal lat---"+degreesToDecimal(rdpsData[2]+" "+rdpsData[3])[0]);
						            	 //  System.err.println("1--Decimal lat---"+(Integer.parseInt(lat_hr[0].trim())));
						            	  // System.err.println("2==Decimal lat---"+decimal);
						
						            	 //  System.err.println("-----------3==Decimal lat---"+i+"------------"+(Integer.parseInt(lat_hr[0].trim())+decimal));
						        		   Latitude.add((Integer.parseInt(lat_hr[0].trim())+decimal)+"");
						
						        	   }
						        	   
						        	   if (rdpsData[3].length()>0)
						        	   {
						        		   // 25�20'42.8730"N
						            	   String[] lat_hr=rdpsData[3].split("�");
//						            	   String[] lat_hr=rdpsData[2].split("°");

						            	   String[] lat_min=lat_hr[1].split("'");
						            	  
						            	   
						            	   double decimal = ((Integer.parseInt(lat_min[0].trim()) *60)+(Double.parseDouble( lat_min[1].replace("\"", "").trim().substring(0, lat_min[1].trim().indexOf("E")-1).trim())))/ (3600);
						            	 //  answer = degrees + decimal
						            	   //System.err.println("Decimal lat---"+degreesToDecimal(rdpsData[2]+" "+rdpsData[3])[0]);
						            	 //  System.err.println("1--Decimal lat---"+(Integer.parseInt(lat_hr[0].trim())));
						            	  // System.err.println("2==Decimal lat---"+decimal);
						
						            	 //  System.err.println("3==Decimal lat---"+(Integer.parseInt(lat_hr[0].trim())+decimal));
						        		   Longitude.add((Integer.parseInt(lat_hr[0].trim())+decimal)+"");
						        	   }
						        	   
						        	   if (rdpsData[4].length()>0)
						        	   Feature_Code.add(rdpsData[4].trim());
						        	   else if (rdpsData[0].length()>0) 
						        		   Feature_Code.add("999");
						
						        	   if (rdpsData[5].length()>0)
						        	   {
//						        		   Feature_Detail.add(rdpsData[5]+" "+rdpsData[6]);
						        		   Feature_Detail.add(rdpsData[5]);
						        		   
//						        		  Section_Detail.add(rdpsData[6]+".");
						        		  

						        	   }
						        	  /* if (rdpsData.length>5&&rdpsData[6].length()>0)
						        		   Section_Detail.add(rdpsData[6].trim());*/
						        	   
						        }
							
						
						  }catch (Exception e) {
//							 TODO: handle exception
//							 
//			        		   System.err.println("rdpsData[2]-lat----"+rdpsData[2]); 

							  System.out.println("Error At Line ---"+i+"---file--"+listOfFiles[fileCount].getName());
							  e.printStackTrace();
						  }

				 }
						
						   // System.out.println(); 
						    i++;
						}
						
						System.out.println(KiloMeter.size()+"-"+Distance.size()+"-"+Latitude.size()+"-"+Longitude.size()+"-"+Feature_Code.size()+"-"+Feature_Detail.size()+"-"+Section_Detail.size()); 
						
						j=0;
						MessageObject msgData = new MessageObject ();
						
						APIController handler= new APIController();
						msgData=handler.addVaransiTrainAddressFromCSV(filename,RailWay,Division,station_From,Station_TO,Chainage,Trolley,
								Line,Mode,KiloMeter,Distance,Latitude,Longitude,Feature_Code,Feature_Detail,ParentId,Section_Detail);
						Gson gson = new Gson();
						feeds = gson.toJson(msgData);
						
						} 
						catch (Exception e) { 
						e.printStackTrace(); 
						System.out.println(e.getMessage());
						
						}
						
		    
		  } else if (listOfFiles[fileCount].isDirectory()) {
		    System.out.println("Directory " + listOfFiles[fileCount].getName());
		    
		    

			File folder1 = new File(filePath+listOfFiles[fileCount].getName()+"/");
			File[] listOfFiles1 = folder1.listFiles();

			for (int fileCount1 = 0; fileCount1< listOfFiles1.length;fileCount1++) {
			  if (listOfFiles1[fileCount1].isFile()) {
			    System.out.println("File " + listOfFiles1[fileCount1].getName());
			    
							System.out.println("addVaransiTrainAddressFromCSV-----file---"+folder1+listOfFiles1[fileCount1].getName()); 
							
							
							try { 
							// Create object of filereader 
							// class with csv file as parameter. 
							FileReader filereader = new FileReader(folder1+"/"+listOfFiles1[fileCount1].getName()); 
							
							// create csvParser object with 
							// custom seperator semi-colon 
							CSVParser parser = new CSVParserBuilder().withSeparator(',').build(); 
							
							// create csvReader object with 
							// parameter filereader and parser 
							CSVReader csvReader = new CSVReaderBuilder(filereader) 
							                          .withCSVParser(parser) 
							                          .build(); 
							
							// Read all data at once 
							List<String[]> allData = csvReader.readAll(); 
							int i=0;
							
							if (allData.get(0)[0].toString().length()>0) 
								  i=1;
							
							// int i=1;
							System.err.print("--allData get 00--"+allData.get(0)[0].toString()+"---"+allData.get(0)[0].toString().length()); 
							
							String filename="",RailWay="",Division="",station_From="",Station_TO="",Chainage ="",Trolley="",
									Line="",Mode="";
									
									ArrayList<String>	KiloMeter = new ArrayList<>(),Distance = new ArrayList<>(),Latitude = new ArrayList<>(),
											Longitude = new ArrayList<>(),Feature_Code = new ArrayList<>(),
											Feature_Detail=new ArrayList<>(),Section_Detail=new ArrayList<>();
							// print Data 
							for (String[] row : allData) { 
								try{
									StringBuilder sbrow=new StringBuilder();
							
							    for (String cell : row) { 
							      //  System.out.print(cell + "$"); 
							        	sbrow.append(cell+",");
							
							    } 
							
							   if(i==1)
								   filename=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							
							   else if(i==2)
								   RailWay=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==3)
								   Division=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==4)
								   station_From=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==5)
								   Station_TO=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==6)
								   Chainage=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==7)
								   Trolley=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==8)
								   Line=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==9)
								   Mode=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==10)
							      System.out.print("Headerrrrrrrrrrr\t"); 
							   else {
							       // System.err.println(sbrow.toString()); 
							
								   String[] rdpsData=sbrow.toString().split(",");
							     //   System.err.println(rdpsData.length); 
								   if(rdpsData.length>4&&rdpsData[0].trim().length()>0&&rdpsData[2].trim().length()>0&&rdpsData[3].trim().length()>0)
							        	   {
									   
									   if (rdpsData[0].length()>0)
							        	   KiloMeter.add(rdpsData[0]);
							        	   
							        	   if (rdpsData[1].length()>0)
							        	   Distance.add(rdpsData[1]);
							        	
							
							        	   if (rdpsData[2].length()>0)
							        	   {
							        		   // 25�20'42.8730"N
							            	   String[] lat_hr=rdpsData[2].split("�");
							            	   String[] lat_min=lat_hr[1].split("'");
							            	  
							            	   
							            	   double decimal = ((Integer.parseInt(lat_min[0]) *60)+Double.parseDouble( lat_min[1].substring(0, lat_min[1].indexOf("N")-1)))/ 3600;
							            	 //  answer = degrees + decimal
							            	   //System.err.println("Decimal lat---"+degreesToDecimal(rdpsData[2]+" "+rdpsData[3])[0]);
							            	 //  System.err.println("1--Decimal lat---"+(Integer.parseInt(lat_hr[0].trim())));
							            	  // System.err.println("2==Decimal lat---"+decimal);
							
							            	 //  System.err.println("-----------3==Decimal lat---"+i+"------------"+(Integer.parseInt(lat_hr[0].trim())+decimal));
							        		   Latitude.add((Integer.parseInt(lat_hr[0].trim())+decimal)+"");
							
							        	   }
							        	   
							        	   if (rdpsData[3].length()>0)
							        	   {
							        		   // 25�20'42.8730"N
							            	   String[] lat_hr=rdpsData[3].split("�");
							            	   String[] lat_min=lat_hr[1].split("'");
							            	  
							            	   
							            	   double decimal = ((Integer.parseInt(lat_min[0]) *60)+(Double.parseDouble( lat_min[1].substring(0, lat_min[1].indexOf("E")-1))))/ (3600);
							            	 //  answer = degrees + decimal
							            	   //System.err.println("Decimal lat---"+degreesToDecimal(rdpsData[2]+" "+rdpsData[3])[0]);
							            	 //  System.err.println("1--Decimal lat---"+(Integer.parseInt(lat_hr[0].trim())));
							            	  // System.err.println("2==Decimal lat---"+decimal);
							
							            	 //  System.err.println("3==Decimal lat---"+(Integer.parseInt(lat_hr[0].trim())+decimal));
							        		   Longitude.add((Integer.parseInt(lat_hr[0].trim())+decimal)+"");
							        	   }
							        	   
							        	   if (rdpsData[4].length()>0)
							        	   Feature_Code.add(rdpsData[4]);
							        	   else if (rdpsData[0].length()>0) 
							        		   Feature_Code.add("999");
										
							            	  
							
							        	   if (rdpsData[5].length()>0)
							        	   {
							        		   Feature_Detail.add(rdpsData[5]);
							        		   Section_Detail.add(rdpsData[6]+".");

							        	   }
							        	  // if (rdpsData[6].length()>0)
							        		//   Section_Detail.add(rdpsData[6]+"".trim());
							        	  
							        	   
							        }
								
							}
							
							 //   System.out.println(); 
							    i++;
								}catch (Exception e) {
									// TODO: handle exception
									System.out.println("Error At Line ---"+i);
									//e.printStackTrace();

								}
							}
							
							System.out.println("sizeeee"+KiloMeter.size()+"-"+Distance.size()+"-"+Latitude.size()+"-"+Longitude.size()+"-"+Feature_Code.size()+"-"+Feature_Detail.size()+"-"+Section_Detail.size()); 
							
							j=0;
							MessageObject msgData = new MessageObject ();
							
							APIController handler= new APIController();
							msgData=handler.addVaransiTrainAddressFromCSV(filename,RailWay,Division,station_From,Station_TO,Chainage,Trolley,
									Line,Mode,KiloMeter,Distance,Latitude,Longitude,Feature_Code,Feature_Detail,ParentId,Section_Detail);
							Gson gson = new Gson();
							feeds = gson.toJson(msgData);
							
							} 
							catch (Exception e) { 

							e.printStackTrace(); 
							
							}
							
		    
		    
		  }
		}
		  }
		 System.out.println("Reposn --"+feeds);
		}
		return feeds;
		
		
	
	}	

	@POST
	@Path("addTrainAddress")
	@Produces("application/json")
	public String addTrainAddress(@FormParam("filename") String filename,@FormParam("RailWay") String RailWay,
			@FormParam("Division") String Division,@FormParam("Station_From") String station_From,
			@FormParam("Station_To") String Station_TO,
			@FormParam("Chainage") String Chainage
			,@FormParam("Trolley") String Trolley,@FormParam("Line") String Line,@FormParam("Mode") String Mode
			,@FormParam("KiloMeter") String KiloMeter,@FormParam("Distance") String Distance,
			@FormParam("Latitude") String Latitude,@FormParam("Longitude") String Longitude,
			@FormParam("Feature_Code") String Feature_Code,@FormParam("Feature_Detail") String Feature_Detail,
			@FormParam("Section") String Section,
			@FormParam("ParentId") String ParentId)
	{
		

		String feeds  = null;
		try 
		{
			int j = 0;
			MessageObject msgData = new MessageObject ();

			APIController handler= new APIController();
			msgData=handler.addTrainAddress(filename,RailWay,Division,station_From,Station_TO,Chainage,Trolley,
					Line,Mode,KiloMeter,Distance,Latitude,Longitude,Feature_Code,Feature_Detail,ParentId,Section);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		System.out.println(feeds);

		return feeds;
	}
	
	@POST
	@Path("addRanchiTrainAddress")
	@Produces("application/json")
	public String addRanchiTrainAddress(@FormParam("RailWay") String RailWay,
			@FormParam("Division") String Division,@FormParam("Section") String Section,
			@FormParam("Latitude") String Latitude,@FormParam("Longitude") String Longitude,
			@FormParam("Feature_Code") String Feature_Code,@FormParam("Feature_Detail") String Feature_Detail,
			@FormParam("ParentId") String ParentId)
	{
		String feeds  = null;
		try 
		{
			int j = 0;
			MessageObject msgData = new MessageObject ();

			APIController handler= new APIController();
			msgData=handler.addRanchiTrainAddress(RailWay,Division,Section
					,Latitude,Longitude,Feature_Code,Feature_Detail,ParentId);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
			
		
			
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		System.out.println(feeds);

		return feeds;
		
	}
	
	@POST
	@Path("addSamstipurSlowModeTrainAddressFromCSV")
	@Produces("application/json")
	public String addSamstipurSlowModeTrainAddressFromCSV(@FormParam("filePath") String filePath,
			@FormParam("ParentId") String ParentId)
	{
		
		  
		
			String feeds  = null;
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();

		for (int fileCount = 0; fileCount< listOfFiles.length;fileCount++) {
		  int j;
		if (listOfFiles[fileCount].isFile()) {
		    System.out.println("File " + listOfFiles[fileCount].getName());
		    
						//System.out.println("addVaransiTrainAddressFromCSV"); 
						
						
						try { 
						// Create object of filereader 
						// class with csv file as parameter. 
						FileReader filereader = new FileReader(filePath+listOfFiles[fileCount].getName()); 
						
						// create csvParser object with 
						// custom seperator semi-colon 
						CSVParser parser = new CSVParserBuilder().withSeparator(',').build(); 
						
						// create csvReader object with 
						// parameter filereader and parser 
						CSVReader csvReader = new CSVReaderBuilder(filereader) 
						                          .withCSVParser(parser) 
						                          .build(); 
						
						// Read all data at once 
						List<String[]> allData = csvReader.readAll(); 
					/*	int i=0;
						
						if (allData.get(0)[0].toString().length()>0) 
							 i=1;
						*/
						// int i=1;
						//System.err.print("--allData get 00--"+allData.get(0)[0].toString()+"---"+allData.get(0)[0].toString().length()); 
						
						/*String filename=listOfFiles[fileCount].getName()
								,RailWay="EAST CENTRAL [ECR] ",
								Division="Samstipur",station_From="",Station_TO="", 
								Chainage ="Increasing",Trolley="",
								Line="",Mode="Slow";*/
						
						String filename=listOfFiles[fileCount].getName()
								,RailWay="Western Railway ",
								Division=" data",station_From="",Station_TO="", 
								Chainage ="Increasing",Trolley="",
								Line="",Mode="Fast";
								
								ArrayList<String>	KiloMeter = new ArrayList<>(),Distance = new ArrayList<>(),Latitude = new ArrayList<>(),Longitude = new ArrayList<>(),Feature_Code = new ArrayList<>(),Feature_Detail=new ArrayList<>();
								 ArrayList<String>	Section_Detail=new ArrayList<>();
						// print Data 
						for (int i=1;i<allData.size();i++) { 
								StringBuilder sbrow=new StringBuilder();
						
						    for (String cell : allData.get(i)) { 
						      //  System.out.print(cell + "$"); 
						        	sbrow.append(cell+",");
						
						    } 
						
				
							   try{
							   String[] rdpsData=sbrow.toString().split(",");
						     //   System.err.println(rdpsData.length); 
							   if(rdpsData.length>4&&rdpsData[0].trim().length()>0&&rdpsData[2].trim().length()>0&&rdpsData[3].trim().length()>0)
						        	   {
//					        		  System.err.println("--"+i+"---"+rdpsData[0]+"-"+rdpsData[1]+"-"+
//					        				  rdpsData[2]+"-"+rdpsData[3]+"-"+rdpsData[4]+"-"+rdpsData[5]); 
	   
								   if (rdpsData[0].length()>0)
						        	   KiloMeter.add(rdpsData[0]);
						        	   
						        	   if (rdpsData[1].length()>0)
						        	   Distance.add(rdpsData[1]);
						        	
						
						        	   if (rdpsData[2].length()>0)
						        	   {
//						        		  System.err.println("rdpsData[21]-lat----"+rdpsData[2]); 
						        		   // 25�20'42.8730"N
						            	   String[] lat_hr=rdpsData[2].split("�");
						            	   String[] lat_min=lat_hr[1].split("'");
//						        		  System.err.println("rdpsData[21]-lat_hr----"+lat_hr.length); 
//						        		   System.err.println("rdpsData[21]-lat_min----"+lat_min.length); 
//
						            	   
						            	   double decimal = ((Integer.parseInt(lat_min[0].trim()) *60)+Double.parseDouble( lat_min[1].substring(0, lat_min[1].indexOf("N")-1).trim()))/ 3600;
						            	 //  answer = degrees + decimal
//						            	   System.err.println("Decimal lat---"+degreesToDecimal(rdpsData[2]+" "+rdpsData[3])[0]);
//						            	   System.err.println("1--Decimal lat1---"+(Integer.parseInt(lat_hr[0].trim())));
//						            	   System.err.println("2==Decimal lat1---"+decimal);
						
//						            	   System.err.println("-----------==Decimal la1---"+i+"------------"+(Integer.parseInt(lat_hr[0].trim())+decimal));
						        		   Latitude.add((Integer.parseInt(lat_hr[0].trim())+decimal)+"");
						
						        	   }else{
						        		   Latitude.add(0+"");
						        		   System.out.println("Lat else- ");
 
						        	   }
						        	   
						        	   if (rdpsData[3].length()>0)
						        	   {
						        		   // 25�20'42.8730"N
						            	   String[] lat_hr=rdpsData[3].split("�");
						            	   String[] lat_min=lat_hr[1].split("'");
						            	  
						            	   
						            	   double decimal = ((Integer.parseInt(lat_min[0].trim()) *60)+(Double.parseDouble( lat_min[1].substring(0, lat_min[1].indexOf("E")-1).trim())))/ (3600);
						            	 //  answer = degrees + decimal
						            	   //System.err.println("Decimal lat---"+degreesToDecimal(rdpsData[2]+" "+rdpsData[3])[0]);
//						            	   System.err.println("1--Decimal la1t---"+(Integer.parseInt(lat_hr[0].trim())));
//						            	   System.err.println("2==Decimal lat1---"+decimal);
						
//						            	  System.err.println("3==Decimal lat1---"+(Integer.parseInt(lat_hr[0].trim())+decimal));
						        		   Longitude.add((Integer.parseInt(lat_hr[0].trim())+decimal)+"");
						        	   }else{
						        		   Latitude.add(0+"");
						        		   System.out.println("Lan else- ");

						        	   }
						        	   
						        	   if (rdpsData[4].length()>0)
						        	   Feature_Code.add(rdpsData[4].trim());
						        	   else if (rdpsData[0].length()>0) 
						        		   Feature_Code.add("999");
						
						        	   if (rdpsData[5].length()>0)
						        	   {
						        		   Feature_Detail.add(rdpsData[5]);
						        		   Section_Detail.add((rdpsData[6]+"").trim());

						        	   }else{
						        		   Feature_Detail.add("0");
						        		   Section_Detail.add("0");
						        	   }
						        	  /* if (rdpsData.length>5&&rdpsData[6].length()>0)
						        		   Section_Detail.add(rdpsData[6].trim());*/
						        	   
						        }
							
						
						  }catch (Exception e) {
							// TODO: handle exception
							 
	
							  System.out.println("Error At Line ---"+i);
							  e.printStackTrace();
						//  }

				 }
						
						   // System.out.println(); 
						   // i++;
						}
						
						System.out.println(KiloMeter.size()+"-"+Distance.size()+"-"+Latitude.size()+"-"+Longitude.size()+"-"+Feature_Code.size()+"-"+Feature_Detail.size()+"-"+Section_Detail.size()); 
						
						j=0;
						MessageObject msgData = new MessageObject ();
						
						APIController handler= new APIController();
						msgData=handler.addSamstipurSlowModeTrainAddressFromCSV(filename,RailWay,Division,station_From,Station_TO,Chainage,Trolley,
								Line,Mode,KiloMeter,Distance,Latitude,Longitude,Feature_Code,Feature_Detail,ParentId,Section_Detail);
						Gson gson = new Gson();
						feeds = gson.toJson(msgData);
						
						} 
						catch (Exception e) { 
						e.printStackTrace(); 
						System.out.println(e.getMessage());
						
						}
						
		    
		  }
		 System.out.println("Reposn --"+feeds);
		}
		return feeds;
		
		
	
	}	
	

	@POST
	@Path("addSamstipurSlowModeTrainAddressFromCSV_UTM_File")
	@Produces("application/json")
	public String addSamstipurSlowModeTrainAddressFromCSV_UTM_File(@FormParam("filePath") String filePath,
			@FormParam("ParentId") String ParentId)
	{
		
		  
		
			String feeds  = null;
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();

		for (int fileCount = 0; fileCount< listOfFiles.length;fileCount++) {
		  int j;
		if (listOfFiles[fileCount].isFile()) {
		    System.out.println("File " + listOfFiles[fileCount].getName());
		    
						//System.out.println("addVaransiTrainAddressFromCSV"); 
						
						
						try { 
						// Create object of filereader 
						// class with csv file as parameter. 
						FileReader filereader = new FileReader(filePath+listOfFiles[fileCount].getName()); 
						
						// create csvParser object with 
						// custom seperator semi-colon 
						CSVParser parser = new CSVParserBuilder().withSeparator(',').build(); 
						
						// create csvReader object with 
						// parameter filereader and parser 
						CSVReader csvReader = new CSVReaderBuilder(filereader) 
						                          .withCSVParser(parser) 
						                          .build(); 
						
						// Read all data at once 
						List<String[]> allData = csvReader.readAll(); 
					/*	int i=0;
						
						if (allData.get(0)[0].toString().length()>0) 
							 i=1;
						*/
						// int i=1;
						//System.err.print("--allData get 00--"+allData.get(0)[0].toString()+"---"+allData.get(0)[0].toString().length()); 
						
						/*String filename=listOfFiles[fileCount].getName()
								,RailWay="EAST CENTRAL [ECR] ",
								Division="Samstipur",station_From="",Station_TO="", 
								Chainage ="Increasing",Trolley="",
								Line="",Mode="Slow";*/
						
						String filename=listOfFiles[fileCount].getName()
								,RailWay="Western Railway ",
								Division=" data",station_From="",Station_TO="", 
								Chainage ="Increasing",Trolley="",
								Line="",Mode="Fast";
								
								ArrayList<String>	KiloMeter = new ArrayList<>(),Distance = new ArrayList<>(),Latitude = new ArrayList<>(),Longitude = new ArrayList<>(),Feature_Code = new ArrayList<>(),Feature_Detail=new ArrayList<>();
								 ArrayList<String>	Section_Detail=new ArrayList<>();
						// print Data 
						for (int i=1;i<allData.size();i++) { 
								StringBuilder sbrow=new StringBuilder();
						
						    for (String cell : allData.get(i)) { 
						      //  System.out.print(cell + "$"); 
						        	sbrow.append(cell+",");
						
						    } 
						
							   String[] rdpsData=sbrow.toString().split(",");

							   try{
						     //   System.err.println(rdpsData.length); 
							   if(rdpsData.length>4&&rdpsData[0].trim().length()>0&&rdpsData[2].trim().length()>0&&rdpsData[3].trim().length()>0)
						        	   {
//					        		  System.err.println("--"+i+"---"+rdpsData[0]+"-"+rdpsData[1]+"-"+
//					        				  rdpsData[2]+"-"+rdpsData[3]+"-"+rdpsData[4]+"-"+rdpsData[5]); 
//	   
								   if (rdpsData[0].length()>0)
						        	   KiloMeter.add(rdpsData[0]);
						        	   
						        	   if (rdpsData[1].length()>0)
						        	   Distance.add(rdpsData[1]);
						        	
						
						        	   if (rdpsData[2].length()>0)
						        	   {
//						        		  System.err.println("rdpsData[21]-lat----"+rdpsData[2]); 
						        		  
						        		 CoordinateConversion conObj=new CoordinateConversion();
						        		 double[] LatLanArray=conObj.utm2LatLon(rdpsData[2]);
//						        		  System.err.println("rdpsData[1]-lat-Conversion---"+new Gson().toJson(LatLanArray)); 

						        		   
						        		   Latitude.add(LatLanArray[0]+"");
						        		   Longitude.add(LatLanArray[1]+"");
						
						        	   }else{
						        		   Latitude.add(0+"");
						        		   Longitude.add(0+"");

						        		   System.out.println("Lat else- ");
 
						        	   }
						        	   
						        	  
						        	   
						        	   if (rdpsData[3].length()>0)
						        	   Feature_Code.add(rdpsData[3].trim());
//						        	   else if (rdpsData[0].length()>0) 
//						        		   Feature_Code.add("999");
						
						        	   if (rdpsData[4].length()>0)
						        	   {
						        		   Feature_Detail.add(rdpsData[4]+rdpsData[5]);
						        		   Section_Detail.add((rdpsData[5]+"").trim());

						        	   }else{
						        		   Feature_Detail.add("0");
						        		   Section_Detail.add("0");
						        	   }
						        	  /* if (rdpsData.length>5&&rdpsData[6].length()>0)
						        		   Section_Detail.add(rdpsData[6].trim());*/
						        	   
						        }
							
						
						  }catch (Exception e) {
							// TODO: handle exception
							 
							  System.err.println("--"+i+"---"+rdpsData[0]+"-"+rdpsData[1]+"-"+
			        				  rdpsData[2]+"-"+rdpsData[3]+"-"+rdpsData[4]+"-"+rdpsData[5]); 

							  System.out.println("Error At Line ---"+i);
							  e.printStackTrace();
						//  }

				 }
						
						   // System.out.println(); 
						   // i++;
						}
						
						System.out.println(KiloMeter.size()+"-"+Distance.size()+"-"+Latitude.size()+"-"+Longitude.size()+"-"+Feature_Code.size()+"-"+Feature_Detail.size()+"-"+Section_Detail.size()); 
						
						j=0;
						MessageObject msgData = new MessageObject ();
						
						APIController handler= new APIController();
						msgData=handler.addSamstipurSlowModeTrainAddressFromCSV(filename,RailWay,Division,station_From,Station_TO,Chainage,Trolley,
								Line,Mode,KiloMeter,Distance,Latitude,Longitude,Feature_Code,Feature_Detail,ParentId,Section_Detail);
						Gson gson = new Gson();
						feeds = gson.toJson(msgData);
						
						} 
						catch (Exception e) { 
						e.printStackTrace(); 
						System.out.println(e.getMessage());
						
						}
						
		    
		  }
		 System.out.println("Reposn --"+feeds);
		}
		return feeds;
		
		
	
	}	
	@POST
	@Path("SaveHierarchyWithCreateLogin")
	@Produces("application/json")
	public String SaveHierarchyWithCreateLogin(@FormParam("Name") String name,
			@FormParam("MobileNo") String mobileNo,
			@FormParam("EmailId") String emailId,
			@FormParam("ParentId") String parenrtId,
			@FormParam("Students") String students)
	{
		System.out.println("SaveHierarchyWithCreateLogin-----"+name);
		ArrayList<WrongLocationDataDTO> locationData = new ArrayList<WrongLocationDataDTO>();
		String feeds  = null;
		try 
		{
			String[] nameArray = name.split(",");
			String[] mobileNoArray = mobileNo.split(",");
			String[] emailIdArray = emailId.split(",");
			String[] parenrtIdArray= parenrtId.split(",");
			String[] StudentsArray = students.split("_");

			MessageObject msgData= new MessageObject();
			APIController handler= new APIController();
			msgData = handler.SaveHierarchyWithCreateLogin(nameArray,mobileNoArray,emailIdArray,
					parenrtIdArray,StudentsArray);
			Gson gson = new Gson();
			feeds = gson.toJson(msgData);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		//System.out.println("BackupNremoveWrongLoc-----"+feeds);

		return feeds;
	}
	
	@POST
	@Path("add_UTM_RDPSData")
	@Produces("application/json")
	public String add_UTM_RDPSData(@FormParam("filePath") String filePath,
			@FormParam("ParentId") String ParentId)
	{
		
		  
		
			String feeds  = null;
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();

		for (int fileCount = 0; fileCount< listOfFiles.length;fileCount++) {
		  int j;
		if (listOfFiles[fileCount].isFile()) {
		    System.out.println("File " + listOfFiles[fileCount].getName());
		    
						//System.out.println("addVaransiTrainAddressFromCSV"); 
						
						try { 
						// Create object of filereader 
						// class with csv file as parameter. 
						FileReader filereader = new FileReader(filePath+listOfFiles[fileCount].getName()); 
						
						// create csvParser object with 
						// custom seperator semi-colon 
						CSVParser parser = new CSVParserBuilder().withSeparator(',').build(); 
						
						// create csvReader object with 
						// parameter filereader and parser 
						CSVReader csvReader = new CSVReaderBuilder(filereader) 
						                          .withCSVParser(parser) 
						                          .build(); 
						
						// Read all data at once 
						List<String[]> allData = csvReader.readAll(); 
						int i=0;
						
						if (allData.get(0)[0].toString().length()>0) 
							  i=1;
						
						// int i=1;
						//System.err.print("--allData get 00--"+allData.get(0)[0].toString()+"---"+allData.get(0)[0].toString().length()); 
						
						String filename="",RailWay="",Division="",station_From="",Station_TO="",Chainage ="",Trolley="",
								Line="",Mode="";
								
								ArrayList<String>	KiloMeter = new ArrayList<>(),Distance = new ArrayList<>(),Latitude = new ArrayList<>(),Longitude = new ArrayList<>(),Feature_Code = new ArrayList<>(),Feature_Detail=new ArrayList<>();
								 ArrayList<String>	Section_Detail=new ArrayList<>();
								 
								 Double lastLat=0.0,lastLan=0.0;
						// print Data 
						for (String[] row : allData) { 
								StringBuilder sbrow=new StringBuilder();
						
						    for (String cell : row) { 
						      //  System.out.print(cell + "$"); 
						        	sbrow.append(cell+",");
						
						    } 
						
						  if(i==1)
							   filename=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						
						 /*  else if(i==2)
							   RailWay=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==3)
							   Division=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==4)
							   station_From=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==5)
							   Station_TO=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==6)
							   Chainage=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==7)
							   Trolley=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==8)
							   Line=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==9)
							   Mode=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
						   else if(i==10)*/
						     // System.out.println("Headerrrrrrrrrrr\t"+filename); 
						  /*  if(i==1)
						    	RailWay=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							
							   else if(i==2)
								   Division=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==3)
								   station_From=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1).replace("SECTION", "");
							   else if(i==4)
								   Station_TO=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==5)
								   Station_TO=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==6)
								   Chainage=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==7)
								   Trolley=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==8)
								   Line=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==9)
								   Mode=sbrow.toString().substring(sbrow.toString().lastIndexOf(":") + 1);
							   else if(i==10)
							      System.out.print("Headerrrrrrrrrrr\t"); */
						   else {
						       // System.err.println(sbrow.toString()); 
							   double[]latLanArray =  new double[2]; ;
							   try{
							   String[] rdpsData=sbrow.toString().split(",");
						     //   System.err.println(rdpsData.length); 
							   if(rdpsData.length>3&&rdpsData[0].trim().length()>0&&rdpsData[2].trim().length()>0&&rdpsData[3].trim().length()>0)
						        	   {
								   
								   if (rdpsData[0].length()>0)
						        	   KiloMeter.add(rdpsData[0]);
						        	   
						        	 
						
						        	   if (rdpsData[2].length()>0&&rdpsData[3].length()>0)
						        	   {
						        		  System.out.println("lat ---"+rdpsData[2].toString()+"-lan----"+rdpsData[3].toString()); 
							         latLanArray= mCoordinateConversion.utm2LatLon(43+" "+"N"+" "+rdpsData[2]+" "+rdpsData[3]);
						        		   
						        		   Latitude.add(latLanArray[0]+"");
						        		   Longitude.add(latLanArray[1]+"");

						
						        	   }
						        	   
						    
						        	   
						        	 /*  if (rdpsData[4].length()>0)
						        	   Feature_Code.add(rdpsData[4].trim());
						        	   else if (rdpsData[0].length()>0) */
						        		   Feature_Code.add("2");
						
						        	   if (rdpsData[1].length()>0)
						        	   {
						        		   Feature_Detail.add("Ohe No "+rdpsData[1]);

						        	   }
						        	   if(i>2){
											Distance.add((mCoordinateConversion.distance(latLanArray[0], latLanArray[1], lastLat, lastLan, "K")*1000)+"");

						        	   }else {
											Distance.add("0");

									}
						        	   
						        	  /* if (rdpsData.length>5&&rdpsData[6].length()>0)
						        		   Section_Detail.add(rdpsData[6].trim());*/
						        	   
						        }
							
							   lastLat= latLanArray[0];
								  lastLan=latLanArray[1];
								  
						  }catch (Exception e) {
							// TODO: handle exception
							 
	
							  System.out.println("Error At Line ---"+i+"---file--"+listOfFiles[fileCount].getName());
							  e.printStackTrace();
						  }

				 }
						
//						  if(i>1)
//							System.err.println(KiloMeter.get(i)+"-"+Distance.get(i)+"-"+Latitude.get(i)+"-"+Longitude.get(i)+"-"+Feature_Code.get(i)+"-"+Feature_Detail.get(i)); 

						   // System.out.println(); 
						    i++;

						}
						
						System.out.println(KiloMeter.size()+"-"+Distance.size()+"-"+Latitude.size()+"-"+Longitude.size()+"-"+Feature_Code.size()+"-"+Feature_Detail.size()+"-"+Section_Detail.size()); 
						System.out.println("Distance----"+new Gson().toJson(Distance)); 

						j=0;
						/*MessageObject msgData = new MessageObject ();
						
						APIController handler= new APIController();
						msgData=handler.addVaransiTrainAddressFromCSV(filename,RailWay,Division,station_From,Station_TO,Chainage,Trolley,
								Line,Mode,KiloMeter,Distance,Latitude,Longitude,Feature_Code,Feature_Detail,ParentId,Section_Detail);
						Gson gson = new Gson();
						feeds = gson.toJson(msgData);
						*/
						} 
						catch (Exception e) { 
						e.printStackTrace(); 
						System.out.println(e.getMessage());
						
						}
						
		    
		  }
		 System.out.println("Reposn --"+feeds);
		}
		return feeds;
		
		
	
	}	

	
	
	
	
}
