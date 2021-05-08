package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.formula.functions.Function;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.util.Beta;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.BSON;
import org.bson.Document;

import Utility.DuplicateMap;
import Utility.SendEmail;

import com.google.gson.Gson;
import com.microsoft.sqlserver.jdbc.SQLServerPreparedStatement;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;

import dto.DailyReportSummaryDTO;
import dto.DailyReportSummaryWithStoppageDTO;
import dto.DeviceBatteryStatusDTO;
import dto.ExceptionReortsTrip;
import dto.FeatureAddressDetailsDTO;
import dto.HistoryDTO;
import dto.LowBatteryStatusReportDto;
import dto.MessageObject;
import dto.NearbyFeatureCodeWithDistanceDTO;
import dto.PaymentNotificationDTO;
import dto.PoleNearByLocationDto;
import dto.PoleNearByLocationDtoStartEnd;
import dto.RailDeviceInfoDto;
import dto.RailMailSendInfoDto;
import dto.RailwayPetrolmanTripsBeatsDTO;
import dto.ReportAnalysisDTO;
import dto.ReportDataSummary;
import dto.ReportSummeryDTO;
import dto.TripInfoDto;
import dto.ZoneWiseReportPreDataDTO;
import static com.mongodb.client.model.Aggregates.*;

public class ReportDAO {
	private Calendar calendar = Calendar.getInstance();
	private int year = calendar.get(Calendar.YEAR);
	private int month = calendar.get(Calendar.MONTH);
	private int day = calendar.get(Calendar.DAY_OF_MONTH);
	private static DecimalFormat df2 = new DecimalFormat(".##");
	
	public MessageObject GenerateExceptionReportKeyManBeatPathWholeDay(

			Connection con, DB mongoconnection, String parentId,int day, Boolean isSendMail, long startTime1, long startTime2, long endTime1, long endTime2) 
	{

		MessageObject msgo = new MessageObject();
		LoginDAO loginObj=new LoginDAO();
		ArrayList<RailMailSendInfoDto> mailSendInfo = loginObj.getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionReportKeyManBeatPathWholeDay ---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-day;
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

		// ///////////////////////////////********************************/////////////////////////////
		// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("KeyMan Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"KeyMan Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						6));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 800);
	/*	cell = row.createCell(6);
		cell.setCellValue("Section Status");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 500);*/

		// ///////////////////////////////////All Section
		// End//////////////////////////////////////////////////////////
		
	
		// //***************/////////////////////////////
				// All Section Count AlertForKeymanWorkStatusReport

				XSSFWorkbook workbookAlertForKeymanWorkStatusReportCount = new XSSFWorkbook();
				XSSFSheet sheetAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createSheet(WorkbookUtil
								.createSafeSheetName("KeyMan Status Report with count"));
				XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle();
				HeadingStyleAlertForKeymanWorkStatusReportCount
						.setFillForegroundColor(IndexedColors.AQUA.getIndex());
				HeadingStyleAlertForKeymanWorkStatusReportCount
						.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				 font = workbookAlertForKeymanWorkStatusReport.createFont();
				font.setFontHeightInPoints((short) 20);
				font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

				 tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle();
				tripColumnStyleAlertForKeymanWorkStatusReport
						.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
								.getIndex());
				tripColumnStyleAlertForKeymanWorkStatusReport
						.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
				font = workbookAlertForKeymanWorkStatusReport.createFont();
				font.setFontHeightInPoints((short) 15);
				font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
				XSSFCellStyle wrap_styleAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle(); // Create new style
				wrap_styleAlertForKeymanWorkStatusReportCount.setWrapText(true); // Set
																	

				row = sheetAlertForKeymanWorkStatusReportCount.createRow(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);
				row.createCell(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);
				row.createCell(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);

				row.createCell(0);
				row.getCell(0).setCellValue(
						"KeyMan Work Status Count Report Date :-"
								+ Common.getDateFromLong(DayStartWorkTime));
				sheetAlertForKeymanWorkStatusReportCount
						.addMergedRegion(new CellRangeAddress(
								sheetAlertForKeymanWorkStatusReportCount.getLastRowNum(),
								sheetAlertForKeymanWorkStatusReportCount.getLastRowNum(), 0,
								4));
				row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReportCount);

				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);

				 cell = row.createCell(0);

				cell.setCellValue("Section Name");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				/*cell = row.createCell(1);
				cell.setCellValue("Start with Low Battery Devices");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);*/
				cell = row.createCell(1);
				cell.setCellValue("Off Devices");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				cell = row.createCell(2);
				cell.setCellValue("Beat not cover");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				;
				cell = row.createCell(3);
				cell.setCellValue("Beat completed Sucessfully");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				cell = row.createCell(4);
				cell.setCellValue("Total Device");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				row.setHeight((short) 800);
			/*	cell = row.createCell(6);
				cell.setCellValue("Section Status");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				row.setHeight((short) 500);*/

				// ///////////////////////////////////All Section COunt// End//////////////////////////////////////////////////////////

				int divDeviceOffCount=0;
				int divDeviceBeatNotCoverCount=0;
				int divDeviceBeatCoverCount=0;

		for (int j = 0; j < mailSendInfo.size(); j++) {
//		for (int j = 13; j<=13 ;j++) {
//
			
			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report of keymen"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyle.setAlignment(HorizontalAlignment.CENTER);
			font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);

			CellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap

			int petrolMancount = 0;
			

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for Keyman of section "
							+ mailSendInfo.get(j).getDept() + "  Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyle);
			
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 5));

			BasicDBList sectionDeviceList = new BasicDBList();

			for (int i = 0; i < deviceInfoList.size(); i++) {
				// for (int i = 4; i <=4; i++) {

//				System.err.println(" **Device =---================"
//						+ deviceInfoList.get(i).getName() + "\n");

				Double startBatteryStatus = loginObj.getKeyManStartBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+startTime1);

				Double endBatteryStatus = loginObj.getKeyManEndBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+endTime2);

				if (deviceInfoList.get(i).getName().startsWith("K/")) {
//				if (deviceInfoList.get(i).getName().startsWith("K/")
//						&&deviceInfoList.get(i).getName().contains("-505")) {

					BasicDBObject subdocument = new BasicDBObject();
					

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfKeyman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));

								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

								dto.setSectionName(rsgertrip
										.getString("SectionName"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));
								RailwayKeymanTripsBeatsList.add(dto);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					// long DayStartWorkTime=1547836200;
					// System.out.println("DayStartWorkTime----" +
					// DayStartWorkTime);

					Double distanceTolerance = 1.3;
					// Double startLatOfRouteKm = 0.0, startLangOfRouteKm = 0.0,
					// endLatOfRouteKm = 0.0, endLangOfRouteKm = 0.0;

					// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
					// RailwayPetrolmanTripsBeatsList) {
					for (int r = 0; r < RailwayKeymanTripsBeatsList.size(); r++) {
						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayKeymanTripsBeatsList
								.get(r);

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.parseLong("355488020181042"));

						/*
						 * BasicDBObject timestamp_whereQuery = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime).append("$lt",
						 * DayStartWorkTime+86400));
						 */

						/*Summer Time
						 * BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 23400).append("$lt",
										DayStartWorkTime + 37800));
						BasicDBOLoginDAO loginObj=new LoginDAO();
		ArrayList<RailMailSendInfoDto> mailSendInfo = loginObj.getSentMailInfoList(con,
				parentId);bject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 41400).append("$lt",
										DayStartWorkTime + 55800));
										
*/
						//Winter Time
					/*	BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 18000).append("$lt",
										DayStartWorkTime + 36000));
						BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 43200).append("$lt",
										DayStartWorkTime + 54000));
*/
						//Winter Time all 8 hr calculated for trip complete
						BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + startTime1).append("$lt",
										DayStartWorkTime + endTime2));
//						BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
//								"timestamp", new BasicDBObject("$gte",
//										DayStartWorkTime + startTime2).append("$lt",
//										DayStartWorkTime + endTime2));

						/*
						 * BasicDBObject timestamp_whereQuery_morning = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+23400).append("$lt",
						 * DayStartWorkTime+37800)); BasicDBObject
						 * timestamp_whereQuery_afternoon = new BasicDBObject(
						 * "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+41400).append("$lt",
						 * DayStartWorkTime+55800));
						 */
						BasicDBList morn_Milage = new BasicDBList();
						morn_Milage.add(timestamp_whereQuery_morning);

						morn_Milage.add(device_whereQuery);
						DBObject morn_Milage_query = new BasicDBObject("$and",
								morn_Milage);

//						BasicDBList after_Milage = new BasicDBList();
////						after_Milage.add(timestamp_whereQuery_afternoon);
//						after_Milage.add(device_whereQuery);
//						DBObject after_Milage_query = new BasicDBObject("$and",
//								after_Milage);

//						BasicDBList Milage = new BasicDBList();
//						Milage.add(morn_Milage_query);
//						Milage.add(after_Milage_query);
//						DBObject final_query = new BasicDBObject("$or", Milage);

						DBCursor cursor = table.find(morn_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + morn_Milage_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							System.err.println("*--listObjects---COunt---"
									+ cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();

						}

						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());
							;

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = loginObj.locationNearbyKM(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"K");
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation =loginObj. locationNearbyKM(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"K");
								if (tripEndLocation.getEndKmBeatActual() <= 0) {
									tripStartLocation.setStartkmBeatActual(0);

								}

								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								
								
								for (DBObject dbObject : listObjects) {
									int SpeedcCheck = (int) dbObject.get("speed");
									Long timeloc=Long.parseLong(dbObject.get("timestamp")+"");
								
							
									if(SpeedcCheck>8&&((timeloc>(DayStartWorkTime + 21600) && timeloc<(DayStartWorkTime+36000) ))){
										exDto.setMaxSpeed(SpeedcCheck);
										}
									
									}

			
								
								exceptiionalTrip.add(exDto);
							} else {

								exceptiionalTrip.add(null);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setLocationSize(0);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exDto.setMaxSpeed(0);
							exceptiionalTrip.add(exDto);
						}
						
					}
//					System.out.println("\n\n");
//					System.out
//							.println("  Here--*************exceptiionalTrip****************************--------------------"
//									+ exceptiionalTrip.size());
//					System.out.println("\n\n");
					// //////////////////////////////////////////////

					// //////////////////////////////////DayStartWorkTime
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null) {
						StringBuilder remark = new StringBuilder();
						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								deviceInfoList.get(i).getName() + "   ("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")");
						subdocument.put("device", deviceInfoList.get(i)
								.getDeviceID());
						subdocument.put("device_name", deviceInfoList.get(i)
								.getName());

						row.getCell(0).setCellStyle(HeadingStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));
						// start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((startBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("start_battery_status",
								df2.format(((startBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						// End Battery Staus
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						if (endBatteryStatus>0) {
							cell.setCellValue("End Battery status :- "
									+ df2.format(((endBatteryStatus / 6) * 100))
									+ " %");
						}else{
							cell.setCellValue("End Battery status :- 0% / May be device off ");
						}
						
						subdocument.put("end_battery_status",
								df2.format(((endBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");

						remark_cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						cell = row.createCell(0);
						cell.setCellValue("Trip#");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(1);
						cell.setCellValue("Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue("Expected Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue("End Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(4);
						cell.setCellValue("Expected end Beat");
						cell.setCellStyle(tripColumnStyle);

						/*
						 * cell = row.createCell(5);
						 * cell.setCellValue("Avg speed");
						 * cell.setCellStyle(tripColumnStyle);
						 */

						cell = row.createCell(5);
						cell.setCellValue("Max speed");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(6);
						cell.setCellValue("Distance cover");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						for (ExceptionReortsTrip exceptionReortsTrip : exceptiionalTrip) {
////							System.out
//									.println(" **exceptiionalTrip=---================"
//											+ new Gson()
//													.toJson(exceptionReortsTrip));

							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null
									&& exceptionReortsTrip.getLocationSize() > 0) {

								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = 0;
									double expactedKmCover = 0;
									if (exceptionReortsTrip.getTripStart()
											.getStartkmBeatActual() != 0
											&& exceptionReortsTrip.getTripEnd()
													.getEndKmBeatActual() != 0
											&& exceptionReortsTrip
													.getTripStart()
													.getMinDistance() < 5
											&& exceptionReortsTrip.getTripEnd()
													.getMinDistance() < 5) {
										kmcover = (exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatActual() - exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatActual());
										if (kmcover < 0)
											kmcover = -1 * kmcover;
										expactedKmCover = exceptionReortsTrip
												.getTripStart()
												.getEndKmBeatExpected()
												- exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected();
										if (expactedKmCover < 0)
											expactedKmCover = -1
													* expactedKmCover;

										if (kmcover < expactedKmCover
												- distanceTolerance) {
											remark.append("\tTotal beats not completed in Trip "
													+ exceptionReortsTrip
															.getTripNo() + ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark_cell
													.setCellValue("Remark status :- All work done succesfully.");
											KeyManCoverSucefullyDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
										}

									} else {
										kmcover = 0;

										if (exceptionReortsTrip.getTripStart()
												.getTotal_distance() < 1) {
											remark.append("\tDevice is not moved "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else if (exceptionReortsTrip
												.getTripStart()
												.getMinDistance() > 5
												&& exceptionReortsTrip
														.getTripEnd()
														.getMinDistance() > 5) {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											KeyManExceptionalDevices_Set.add(deviceInfoList
													.get(i).getName());
										}

									}

			
									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());
										subdocument
												.put("Tripno",
														exceptionReortsTrip
																.getTripNo());

										cell.setCellStyle(wrap_style);

										cell = row.createCell(1);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));
										subdocument
												.put("start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatActual()));
										cell.setCellStyle(wrap_style);

										cell = row.createCell(2);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatExpected());
										subdocument
												.put("expected_start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatExpected()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(3);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));
										subdocument.put("end_beat", df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(4);
										cell.setCellValue(exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatExpected());
										subdocument
												.put("expected_end_beat",
														df2.format(exceptionReortsTrip
																.getTripEnd()
																.getEndKmBeatExpected()));

										cell.setCellStyle(wrap_style);

										/*
										 * cell = row.createCell(5);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 * cell.setCellStyle(wrap_style);
										 */

										cell = row.createCell(5);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart().getMaxSpeed());
										subdocument.put("max_speed", df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getMaxSpeed()));

										if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8&&exceptionReortsTrip.getMaxSpeed()>8) {
											KeyManOverspeedDevices_Set.add(deviceInfoList.get(i)
															.getName());
										}

										cell.setCellStyle(wrap_style);

										cell = row.createCell(6);
										cell.setCellValue(df2.format(kmcover));
										subdocument.put("kmcover",
												df2.format(kmcover));
										cell.setCellStyle(wrap_style);

										// System.out.println("  Here--*****************************************--------------------");
										//

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// Exception of location not found
								if (exceptionReortsTrip != null
										&& exceptionReortsTrip
												.getLocationSize() == 0)
									try {
										// System.out.println(" **exceptionReortsTrip*******row******************"
										// +
										// "***-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size()+"-----"+exceptionReortsTrip.getLocationSize());

										if (!deviceInfoList
												.get(i).getName().contains("/LR/")) {
											KeyManOffDevice_Set.add(deviceInfoList
													.get(i).getName());

										}
									
										remark.append("Device is Off");

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(1);
										cell.setCellStyle(loactionNotFoundColumnStyle);

										cell = row.createCell(1);
										cell.setCellValue("Device is Off");
										cell.setCellStyle(loactionNotFoundColumnStyle);
										sheet.addMergedRegion(new CellRangeAddress(
												sheet.getLastRowNum(), sheet
														.getLastRowNum(), 1, 3));

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

							}
						}

						if (remark.toString().length() > 0) {
							remark_cell.setCellValue("Remark status :- "
									+ remark.toString());
							subdocument.put("remark", remark.toString());
							remark_cell.setCellStyle(error_remarkColumnStyle);
						}

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayKeymanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null) {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(
										deviceInfoList.get(i).getName()
												+ "   ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ")");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :- RDPS data not found.");
								subdocument.put("remark",
										"RDPS data not found.");
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								// cell.setCellStyle(error_remarkColumnStyle);

								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(

										deviceInfoList.get(i).getName()
												+ "  ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ") ");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :-Trips not Shedule for this device.");
								subdocument.put("remark",
										"Trips not Shedule for this device.");


								if (!deviceInfoList
										.get(i).getName().contains("/LR/"))
							KeyManExceptionalDevices_Set.add(deviceInfoList
									.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());
					if (startBatteryStatus < 3
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName())&&!deviceInfoList
											.get(i).getName().contains("/LR/"))
						KeyManLowBattery_Set.add(deviceInfoList.get(i)
								.getName());

					sectionDeviceList.add(subdocument);
					
					
					if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName())&&
							KeyManExceptionalDevices_Set.contains(deviceInfoList.get(i).getName())) 
						KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());

				} else {
					// System.out.println("patrolman Man found--------------------");
					petrolMancount++;
				}

			}

			Maindocument.put("exceptional_device", sectionDeviceList);

			report_table.insert(Maindocument);

			sheet.setColumnWidth(0, 1300);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 2000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);

			if (deviceInfoList.size() > 0
					&& petrolMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept() + "_"
						+ "Exception_Trip_Report_KeyMan"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ "_" + parentId + ".xlsx";
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					String sendemail = "";
					for (String emaString : mailSendInfo.get(j).getEmailIds()) {
						sendemail = sendemail + "," + emaString.trim();
					}
					SendEmail mail = new SendEmail();

					
					if(isSendMail)
					 {
						 mail.sendDeviceExceptionMailToJaipur(sendemail, file,
								 mailSendInfo
								  .get(j).getDept(),"Exception Trip Report of keymen"
								  ,"Exception Trip Report of keymen",false
									,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					 }
			
					 
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// /////ALl Section////////////////////////
			
				
			System.err.println("All section--countee--"+KeyManLowBattery_Set.size()+KeyManCoverSucefullyDevices_Set.size()+
					KeyManExceptionalDevices_Set.size()+KeyManOffDevice_Set.size());
			
			
			if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
					KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1)
			
			{
		
			
			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReport);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			cell = row.createCell(1);
			cell.setCellValue(KeyManLowBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight
					.add((short) ((KeyManLowBattery_Set.toString()
							.replace("[", "").replace("]", "").toString()
							.length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(KeyManOffDevice_Set.toString().replace("[", "")
					.replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOffDevice_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			
			
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport);// Apply // style// to// cell
			rowHeight.add((short) ((KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));
			
			/*cell = row.createCell(6);
			if(KeyManExceptionalDevices_Set.size()>0)
				cell.setCellValue("Not completed successfully ");

			else
				cell.setCellValue("Completed successfully");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); */

			short height_row = (short) ((KeyManLowBattery_Set.toString()
					.replace("[", "").replace("]", "").toString().length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);

			sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

			if (j == mailSendInfo.size() - 1) {
				String outFileNameAllSectionStatus = "All_Section_KeyMan_Status_Report_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_"+parentId+ ".xlsx";
				
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReport.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReport.close();

					String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," Keyman Work Status Report AllSection",
								  " Keyman Work Status Report AllSection",true	
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					}
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

			// /////////////////////*Alll Section////////////////////
			
			
			
			
			// /////ALl Section COunt////////////////////////
			 divDeviceOffCount=divDeviceOffCount+KeyManOffDevice_Set.size();
			 divDeviceBeatNotCoverCount=divDeviceBeatNotCoverCount+KeyManExceptionalDevices_Set.size();
			 divDeviceBeatCoverCount=divDeviceBeatCoverCount+KeyManCoverSucefullyDevices_Set.size();

			
	
				 if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
							KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1)
				 {	

			row = sheetAlertForKeymanWorkStatusReportCount
					.createRow(sheetAlertForKeymanWorkStatusReportCount
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	

			cell = row.createCell(1);
			cell.setCellValue(KeyManOffDevice_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	
			cell = row.createCell(2);
			cell.setCellValue(KeyManExceptionalDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			

			cell = row.createCell(3);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

			cell = row.createCell(4);
			cell.setCellValue((KeyManCoverSucefullyDevices_Set.size()+KeyManOffDevice_Set.size()+
					KeyManExceptionalDevices_Set.size())+"");
			
			
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			
	
			
			row.setHeight((short) 600);

			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(0, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(4, 3000);

			if (j == mailSendInfo.size() - 1) {
				
				//for total row
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount
								.getLastRowNum() + 1);
				row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				cell = row.createCell(0);
				cell.setCellValue("Total Device");

				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

				cell = row.createCell(1);
				cell.setCellValue(divDeviceOffCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				;

				cell = row.createCell(2);
				cell.setCellValue(divDeviceBeatNotCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				
				cell = row.createCell(3);
				cell.setCellValue(divDeviceBeatCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				

				cell = row.createCell(4);
				cell.setCellValue((divDeviceOffCount+divDeviceBeatCoverCount+
						divDeviceBeatNotCoverCount)+"");
				
				
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);


				
				row.setHeight((short) 600);

				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(0, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(1, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(2, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(3, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(4, 3000);
				
				String outFileNameAllSectionStatus = "All_Section_KeyMan_Status_Device_Count_Report_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_"+parentId+ ".xlsx";
				
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReportCount.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReportCount.close();

					String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					/*if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," Keyman Work Status device count Report AllSection",
								  " Keyman Work Status device count Report AllSection",true	
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					}*/
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

			// /////////////////////*Alll Section COunt end ////////////////////
			
			
			///Insert Datewise Saction Data
			 
			if(isSendMail)
			 try {

					// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
					java.sql.CallableStatement ps = con
							.prepareCall("{call SaveDatewiseExceptionReport(?,?,?,?,?,?,?,?)}");

					
					ps.setInt(1, Integer.parseInt(parentId));
					ps.setString(2,DayStartWorkTime+"");
					ps.setString(3, KeyManLowBattery_Set.toString().replace("[", "")
							.replace("]", ""));

					ps.setString(4,KeyManOffDevice_Set.toString()
							.replace("[", "").replace("]", ""));

					ps.setString(5, KeyManExceptionalDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(6, KeyManOverspeedDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(7,mailSendInfo.get(j).getDept() );
					ps.setString(8, KeyManCoverSucefullyDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					int result = ps.executeUpdate();
					if (result == 0) {
						msgo.setError("true");
						msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
					} else {
						// //System.err.println("Error=="+result);
						msgo.setError("false");
						msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
				}

		}// //////////for section end

		return msgo;

	}

	
	
	
	
	public MessageObject GenerateExceptionReportKeyManBeatPathWholeDayNewLogic(

			Connection con, DB mongoconnection, String parentId,int day,
			Boolean isSendMail, long startTime1, long startTime2, long endTime1,
			long endTime2, Boolean exceptionSummary, Double distanceToleranceInKm) 
	{

		MessageObject msgo = new MessageObject();
		LoginDAO loginObj=new LoginDAO();
		ArrayList<RailMailSendInfoDto> mailSendInfo = loginObj.getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionReportKeyManBeatPathWholeDayNewLogic ---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-day;
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

		// ///////////////////////////////********************************/////////////////////////////
		// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("KeyMan Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"KeyMan Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						6));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 800);


		// ///////////////////////////////////All Section
		// End//////////////////////////////////////////////////////////
		
	
		
		
		// //***************/////////////////////////////
				// All Section Count AlertForKeymanWorkStatusReport

				XSSFWorkbook workbookAlertForKeymanWorkStatusReportCount = new XSSFWorkbook();
				XSSFSheet sheetAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createSheet(WorkbookUtil
								.createSafeSheetName("KeyMan Status Report with count"));
				XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle();
				HeadingStyleAlertForKeymanWorkStatusReportCount
						.setFillForegroundColor(IndexedColors.AQUA.getIndex());
				HeadingStyleAlertForKeymanWorkStatusReportCount
						.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				 font = workbookAlertForKeymanWorkStatusReport.createFont();
				font.setFontHeightInPoints((short) 20);
				font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

				 tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle();
				tripColumnStyleAlertForKeymanWorkStatusReport
						.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
								.getIndex());
				tripColumnStyleAlertForKeymanWorkStatusReport
						.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
				font = workbookAlertForKeymanWorkStatusReport.createFont();
				font.setFontHeightInPoints((short) 15);
				font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
				XSSFCellStyle wrap_styleAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle(); // Create new style
				wrap_styleAlertForKeymanWorkStatusReportCount.setWrapText(true); // Set
																	

				row = sheetAlertForKeymanWorkStatusReportCount.createRow(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);
				row.createCell(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);
				row.createCell(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);

				row.createCell(0);
				row.getCell(0).setCellValue(
						"KeyMan Work Status Count Report Date :-"
								+ Common.getDateFromLong(DayStartWorkTime));
				sheetAlertForKeymanWorkStatusReportCount
						.addMergedRegion(new CellRangeAddress(
								sheetAlertForKeymanWorkStatusReportCount.getLastRowNum(),
								sheetAlertForKeymanWorkStatusReportCount.getLastRowNum(), 0,
								4));
				row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReportCount);

				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);

				 cell = row.createCell(0);

				cell.setCellValue("Section Name");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				/*cell = row.createCell(1);
				cell.setCellValue("Start with Low Battery Devices");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);*/
				cell = row.createCell(1);
				cell.setCellValue("Off Devices");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				cell = row.createCell(2);
				cell.setCellValue("Beat not cover");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				;
				cell = row.createCell(3);
				cell.setCellValue("Beat completed Sucessfully");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				cell = row.createCell(4);
				cell.setCellValue("Total Device");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				cell = row.createCell(5);
				cell.setCellValue("Detail Remark");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				row.setHeight((short) 800);
			/*	cell = row.createCell(6);
				cell.setCellValue("Section Status");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				row.setHeight((short) 500);*/

				// ///////////////////////////////////All Section COunt// End//////////////////////////////////////////////////////////


				
				////////////////////////////////////////// exception Summary////////////////////////
				
				
				XSSFWorkbook workbookSummary = new XSSFWorkbook();
				XSSFSheet sheetSummary = workbookSummary.createSheet(WorkbookUtil
						.createSafeSheetName("Exception Trip Report Summary of keymen"));

				XSSFCellStyle HeadingStyleSummary = workbookSummary.createCellStyle();
				HeadingStyleSummary.setFillForegroundColor(IndexedColors.AQUA.getIndex());
				HeadingStyleSummary.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				HeadingStyleSummary.setAlignment(HorizontalAlignment.CENTER);
				XSSFFont fontSummary = workbookSummary.createFont();
				font.setBold(true);
				font.setColor(IndexedColors.WHITE.getIndex());
				HeadingStyleSummary.setFont(font);

				XSSFCellStyle tripColumnStyleSummary = workbookSummary.createCellStyle();
				tripColumnStyleSummary
						.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
								.getIndex());
				tripColumnStyleSummary.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				tripColumnStyleSummary.setWrapText(true);
				font = workbookSummary.createFont();
				font.setFontHeightInPoints((short) 15);
				font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
				// tripColumnStyle.setFont(font);

				XSSFCellStyle remarkColumnStyleSummary = workbookSummary.createCellStyle();
				remarkColumnStyleSummary.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
						.getIndex());
				remarkColumnStyleSummary.setFillPattern(FillPatternType.SOLID_FOREGROUND);

				XSSFCellStyle error_remarkColumnStyleSummary = workbookSummary.createCellStyle();
				error_remarkColumnStyleSummary
						.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
								.getIndex());
				error_remarkColumnStyleSummary
						.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				XSSFFont redfontSummary = workbookSummary.createFont();
				redfontSummary.setColor(IndexedColors.RED.getIndex());
				error_remarkColumnStyleSummary.setFont(redfontSummary);

				// loactionNotFoundColumnStyle.setFont(font);

				CellStyle wrap_styleSummary = workbookSummary.createCellStyle(); // Create new
																	// style
				wrap_styleSummary.setWrapText(true); // Set wordwrap

				int petrolMancount = 0;

				 row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);

				row.createCell(0);
				row.getCell(0).setCellValue(
						"Exception Report for Keyman"
								 + " Date: "
								+ Common.getDateFromLong(DayStartWorkTime));
				row.getCell(0).setCellStyle(HeadingStyleSummary);
				
				sheetSummary.addMergedRegion(new CellRangeAddress(sheetSummary.getLastRowNum(),
						sheetSummary.getLastRowNum(), 0, 7));

				BasicDBList sectionDeviceList = new BasicDBList();
				
				row = sheetSummary.createRow(0);
				row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);



				cell = row.createCell(0);
				cell.setCellValue("Sr no");
				cell.setCellStyle(tripColumnStyleSummary);
			/*	
				 cell = row.createCell(1);
				cell.setCellValue("Name");
				cell.setCellStyle(tripColumnStyle);*/

				cell = row.createCell(1);
				cell.setCellValue("Device name");
				cell.setCellStyle(tripColumnStyleSummary);

				cell = row.createCell(2);
				cell.setCellValue("Late start >15min");
				cell.setCellStyle(tripColumnStyleSummary);
				
				cell = row.createCell(3);
				cell.setCellValue("Early off >15 min");
				cell.setCellStyle(tripColumnStyleSummary);

				cell = row.createCell(4);
				cell.setCellValue("Overspeed>8kmph");
				cell.setCellStyle(tripColumnStyleSummary);
				
				cell = row.createCell(5);
				cell.setCellValue("Stoppage >60min. No of stoppage at location with railways geo fence");
				cell.setCellStyle(tripColumnStyleSummary);
				
				cell = row.createCell(6);
				cell.setCellValue("Trip not completed");
				cell.setCellStyle(tripColumnStyleSummary);
				
				cell = row.createCell(7);
				cell.setCellValue("Travelled km, 500 m short from alloted km");
				cell.setCellStyle(tripColumnStyleSummary);
				row.setHeight((short) 1200);
				
				
				
				////////////////////////////////////report Summary//////////////////////////////
				int divDeviceOffCount=0;
				int divDeviceBeatNotCoverCount=0;
				int divDeviceBeatCoverCount=0;

		for (int j = 0; j < mailSendInfo.size(); j++) {
//		for (int j = 1; j<=1 ;j++) {
//
			
			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report of keymen"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyle.setAlignment(HorizontalAlignment.CENTER);
			font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);

			CellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap			

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for Keyman of section "
							+ mailSendInfo.get(j).getDept() + "  Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyle);
			
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 5));

			for (int i = 0; i < deviceInfoList.size(); i++) {
//				 for (int i = 5; i <=5; i++) {

				System.err.println(" **Device =---================"+ deviceInfoList.get(i).getName() + "\n");

				Double startBatteryStatus = loginObj.getKeyManStartBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+startTime1);

				Double endBatteryStatus = loginObj.getKeyManEndBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+endTime2);

				if (deviceInfoList.get(i).getName().startsWith("K/")) {
//				if (deviceInfoList.get(i).getName().startsWith("K/")&&deviceInfoList.get(i).getName().contains("-013")){
					BasicDBObject subdocument = new BasicDBObject();

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfKeyman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));

								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

								dto.setSectionName(rsgertrip
										.getString("SectionName"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));
								RailwayKeymanTripsBeatsList.add(dto);

//								System.err.println("startApprox--"+rsgertrip
//										.getDouble("startApprox")+"-endApprox--"+rsgertrip
//										.getDouble("endApprox"));
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					// long DayStartWorkTime=1547836200;
					// System.out.println("DayStartWorkTime----" +
					// DayStartWorkTime);
					ArrayList<DBObject> listObjects=new ArrayList<>();
					// Double startLatOfRouteKm = 0.0, startLangOfRouteKm = 0.0,
					// endLatOfRouteKm = 0.0, endLangOfRouteKm = 0.0;

					// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
					// RailwayPetrolmanTripsBeatsList) {
					for (int r = 0; r < RailwayKeymanTripsBeatsList.size(); r++) {
						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayKeymanTripsBeatsList
								.get(r);

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.parseLong("355488020181042"));

						
						//Winter Time all 8 hr calculated for trip complete
						BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + startTime1).append("$lt",
										DayStartWorkTime + endTime2));
//					
						BasicDBList morn_Milage = new BasicDBList();
						morn_Milage.add(timestamp_whereQuery_morning);

						morn_Milage.add(device_whereQuery);
						DBObject morn_Milage_query = new BasicDBObject("$and",
								morn_Milage);

//						
						DBCursor cursor = table.find(morn_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + morn_Milage_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
					 listObjects .clear();
						if (cursor.size() > 0) {

							System.err.println("*--listObjects---COunt---"
									+ cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();

						}

						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());
							;

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = loginObj.locationNearbyKM(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"K",DayStartWorkTime+startTime1+1000,
										DayStartWorkTime+endTime1,DayStartWorkTime+startTime2,DayStartWorkTime+endTime2);
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation =loginObj. locationNearbyKM(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"K",DayStartWorkTime+startTime1+1000,
										DayStartWorkTime+endTime1,DayStartWorkTime+startTime2,DayStartWorkTime+endTime2);
								if (tripEndLocation.getEndKmBeatActual() <= 0) {
									tripStartLocation.setStartkmBeatActual(0);

								}

								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								exDto.setLocationListObjects(listObjects);
								
								for (DBObject dbObject : listObjects) {
									int SpeedcCheck = (int) dbObject.get("speed");
									Long timeloc=Long.parseLong(dbObject.get("timestamp")+"");
								
							
									if(SpeedcCheck>8&&((timeloc>(DayStartWorkTime + startTime1) && timeloc<(DayStartWorkTime+endTime1) )||
											(timeloc>(DayStartWorkTime + startTime2) && timeloc<(DayStartWorkTime+endTime2) ))){
										exDto.setMaxSpeed(SpeedcCheck);
										exDto.setMaxSpeedTime(timeloc);
										}
									
									}

								System.out.println("setLocationListObjects--"+exDto.getLocationListObjects());

								exDto.setSectionName(railMailSendInfoDto.getSectionName());
								exceptiionalTrip.add(exDto);
							} else {
								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								PoleNearByLocationDto tripEndLocation=new PoleNearByLocationDto();
								tripEndLocation.setStartkmBeatExpected(railMailSendInfoDto.getKmStart());
								tripEndLocation.setEndKmBeatExpected(railMailSendInfoDto.getKmEnd());
								exDto.setTripStart(tripEndLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								exDto.setLocationListObjects(listObjects);

								System.out.println("setLocationListObjects--"+exDto.getLocationListObjects());
								exceptiionalTrip.add(exDto);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setLocationSize(0);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exDto.setMaxSpeed(0);
							exDto.setSectionName(railMailSendInfoDto.getSectionName());
							exDto.setLocationListObjects(listObjects);
							System.out.println("setLocationListObjects--"+exDto.getLocationListObjects());

							exceptiionalTrip.add(exDto);
						}
						
					}
//					System.out.println("\n\n");
//					System.out
//							.println("  Here--*************exceptiionalTrip****************************--------------------"
//									+ exceptiionalTrip.size());
//					System.out.println("\n\n");
					// //////////////////////////////////////////////

					// //////////////////////////////////DayStartWorkTime
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null) {
						StringBuilder remark = new StringBuilder();
						String DetailRemark = "";

						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								deviceInfoList.get(i).getName() + "   ("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")");
						subdocument.put("device", deviceInfoList.get(i)
								.getDeviceID());
						subdocument.put("device_name", deviceInfoList.get(i)
								.getName());

						row.getCell(0).setCellStyle(HeadingStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));
						// start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((startBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("start_battery_status",
								df2.format(((startBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));

						// End Battery Staus
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						if (endBatteryStatus>0) {
							cell.setCellValue("End Battery status :- "
									+ df2.format(((endBatteryStatus / 6) * 100))
									+ " %");
						}else{
							cell.setCellValue("End Battery status :- 0% / May be device off ");
						}
						
						subdocument.put("end_battery_status",
								df2.format(((endBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");

						remark_cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						cell = row.createCell(0);
						cell.setCellValue("Trip#");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(1);
						cell.setCellValue("Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue("Expected Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue("End Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(4);
						cell.setCellValue("Expected end Beat");
						cell.setCellStyle(tripColumnStyle);

						/*
						 * cell = row.createCell(5);
						 * cell.setCellValue("Avg speed");
						 * cell.setCellStyle(tripColumnStyle);
						 */

						cell = row.createCell(5);
						cell.setCellValue("Max speed");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(6);
						cell.setCellValue("Distance cover");
						cell.setCellStyle(tripColumnStyle);
						cell = row.createCell(7);
						
						cell.setCellValue("Detail Remark");
						cell.setCellStyle(tripColumnStyle);

						row.setHeight((short) 800);
						for (ExceptionReortsTrip exceptionReortsTrip : exceptiionalTrip) {
////							System.out
//									.println(" **exceptiionalTrip=---================"
//											+ new Gson()
//													.toJson(exceptionReortsTrip));

							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null
									&& exceptionReortsTrip.getLocationSize() > 0
									&&((exceptionReortsTrip.getTripStart().getLat()!=null
									|| exceptionReortsTrip.getTripStart().getLang()!=null)
									&&( exceptionReortsTrip.getTripEnd().getLat()!=null
									|| exceptionReortsTrip.getTripEnd().getLang()!=null))) {

								String startActualKm=loginObj.getRdpsKmNearByLocation(con, exceptionReortsTrip.getTripStart().getLat(),
										exceptionReortsTrip.getTripStart().getLang(), parentId,exceptionReortsTrip.getSectionName(),exceptionReortsTrip.getTripStart().getStartkmBeatExpected());
								
								String endActualKm=loginObj.getRdpsKmNearByLocation(con, exceptionReortsTrip.getTripEnd().getLat(),
										exceptionReortsTrip.getTripEnd().getLang(), parentId,exceptionReortsTrip.getSectionName(),exceptionReortsTrip.getTripEnd().getEndKmBeatExpected());
								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = 0;
									double expactedKmCover = 0;
									/*if (exceptionReortsTrip.getTripStart()
											.getStartkmBeatActual() != 0
											&& exceptionReortsTrip.getTripEnd()
													.getEndKmBeatActual() != 0) {*/
										
										
									/*	kmcover = (exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatActual() - exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatActual());
										if (kmcover < 0)
											kmcover = -1 * kmcover;
										*/
										
								if (!endActualKm.equals("NA")&&!startActualKm.equals("NA"))
								{
							
										kmcover=Double.parseDouble(endActualKm)-Double.parseDouble(startActualKm);
										if (kmcover<0) 								
										kmcover=kmcover*-1;
										
										expactedKmCover = exceptionReortsTrip
												.getTripStart()
												.getEndKmBeatExpected()
												- exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected();
										
										
										if (expactedKmCover < 0)
											expactedKmCover = -1
													* expactedKmCover;
										
										System.err.println("Math Absss--start---"+Math.abs(exceptionReortsTrip.getTripStart().getStartkmBeatExpected()-Double.parseDouble(startActualKm)));
										System.err.println("Math Absss---end--"+Math.abs(exceptionReortsTrip.getTripEnd().getEndKmBeatExpected()-Double.parseDouble(endActualKm)));

										
										if(kmcover!=0&&exceptionReortsTrip
												.getTripStart().getMaxSpeed()<=8&&kmcover >= expactedKmCover
												- distanceToleranceInKm) {
											
											remark_cell
											.setCellValue("Remark status :- All work done succesfully.");
									KeyManCoverSucefullyDevices_Set
											.add(deviceInfoList.get(i)
													.getName());
									
									
										}else if(kmcover!=0&&exceptionReortsTrip
												.getTripStart().getMaxSpeed()>8&&kmcover >= expactedKmCover
												- distanceToleranceInKm) {
											
 											remark.append("\tWork done succesfully But found in Overspeed "
 													+ "at "+Common.getDateCurrentTimeZone(exceptionReortsTrip.getMaxSpeedTime()));

									KeyManCoverSucefullyDevices_Set
											.add(deviceInfoList.get(i)
													.getName());
									
									DetailRemark="Device found in Over speed  at "+Common.getDateCurrentTimeZone(exceptionReortsTrip.getMaxSpeedTime());
									
										}else if (kmcover!=0&&kmcover < expactedKmCover
												- distanceToleranceInKm/*&&
												(Math.abs(exceptionReortsTrip.getTripStart().getStartkmBeatExpected()-Double.parseDouble(startActualKm))<5
												||Math.abs(exceptionReortsTrip.getTripEnd().getEndKmBeatExpected()-Double.parseDouble(endActualKm))<5)*/) {
											


											System.err.println("Math Absss-----"+Math.abs(exceptionReortsTrip.getTripStart().getStartkmBeatExpected()-Double.parseDouble(startActualKm)));
											remark.append("\tTotal beats not completed in Trip "
													+ exceptionReortsTrip
															.getTripNo() + ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
											
											DetailRemark="Total beats not completed in Trip";

										}
										else if (kmcover==0&&exceptionReortsTrip.getTripStart()
												.getTotal_distance() < 1){
											remark.append("\tDevice is not moved "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
											
											DetailRemark="Device was not walk on Track";

										}else if (kmcover==0&&exceptionReortsTrip.getTripStart()
												.getTotal_distance() > 1) {
											
											System.err.println("Dist COver----"+exceptionReortsTrip.getTripStart()
												.getTotal_distance() );
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
											
											DetailRemark="Device was move between "+startActualKm +"-"+endActualKm+" but expected between "+
													exceptionReortsTrip
													.getTripStart()
													.getStartkmBeatExpected()+"-"+exceptionReortsTrip
													.getTripEnd()
													.getEndKmBeatExpected();


										}else{
											remark.append("\tDevice is not moved "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
										}

									/*}else{
										
										System.out.println("Else  in --------------------------------\n");
									}
*/
									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());
										subdocument
												.put("Tripno",
														exceptionReortsTrip
																.getTripNo());

										cell.setCellStyle(wrap_style);

										cell = row.createCell(1);
										/*cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));*/

									
										cell.setCellValue(startActualKm)
										;
										subdocument
												.put("start_beat",startActualKm);
										cell.setCellStyle(wrap_style);

										cell = row.createCell(2);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatExpected());
										subdocument
												.put("expected_start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatExpected()));

										cell.setCellStyle(wrap_style);

										
										cell = row.createCell(3);
										
												
									/*	cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));*/
												cell.setCellValue(endActualKm);
										subdocument.put("end_beat",endActualKm);

										cell.setCellStyle(wrap_style);

										cell = row.createCell(4);
										cell.setCellValue(exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatExpected());
										subdocument
												.put("expected_end_beat",
														df2.format(exceptionReortsTrip
																.getTripEnd()
																.getEndKmBeatExpected()));

										cell.setCellStyle(wrap_style);

										/*
										 * cell = row.createCell(5);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 * cell.setCellStyle(wrap_style);
										 */

										cell = row.createCell(5);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart().getMaxSpeed());
										subdocument.put("max_speed", df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getMaxSpeed()));

										if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8&&exceptionReortsTrip.getMaxSpeed()>8) {
											KeyManOverspeedDevices_Set.add(deviceInfoList.get(i)
															.getName());
										}

										cell.setCellStyle(wrap_style);

										cell = row.createCell(6);
									
										
										cell.setCellValue(df2.format(kmcover));
										subdocument.put("kmcover",
												df2.format(kmcover));
										cell.setCellStyle(wrap_style);

										// System.out.println("  Here--*****************************************--------------------");
										//
									
										cell = row.createCell(7);
									
										
										cell.setCellValue(DetailRemark);
										cell.setCellStyle(wrap_style);									
									

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									
									
									///////////////Summary report Row generate-////////////////////////
									

									try {

										 Row rowSummary = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);

										 Cell cellSummary = rowSummary.createCell(0);
										cellSummary.setCellValue(sheetSummary.getLastRowNum()-2);
//										subdocument
//												.put("Tripno",
//														exceptionReortsTrip
//																.getTripNo());

									
										cellSummary.setCellStyle(wrap_styleSummary);
										
										cellSummary = rowSummary.createCell(1);
										cellSummary.setCellValue(deviceInfoList.get(i).getName());
										cellSummary.setCellStyle(wrap_styleSummary);

									
										/*cellSummary.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));*/

									
//										;
//										subdocument
//												.put("start_beat",startActualKm);

										cellSummary = rowSummary.createCell(2);
										if (exceptionReortsTrip.getLocationListObjects().size()>0&&Long.parseLong(exceptionReortsTrip.getLocationListObjects().get(0).get("timestamp")+"")-(startTime1+DayStartWorkTime)>900)
											cellSummary.setCellValue(Common.getDateCurrentTimeZone(exceptionReortsTrip
												.getTripStart().getTimestamp()));
										else cellSummary.setCellValue("-");

										cellSummary.setCellStyle(wrap_styleSummary);

//										subdocument
//												.put("expected_start_beat",
//														df2.format(exceptionReortsTrip
//																.getTripStart()
//																.getStartkmBeatExpected()));


			
										cellSummary = rowSummary.createCell(3);
										if (exceptionReortsTrip.getLocationListObjects().size()>0&&(endTime2+DayStartWorkTime)-Long.parseLong(exceptionReortsTrip.getLocationListObjects().get(exceptionReortsTrip.getLocationSize()-1).get("timestamp")+"")>900) 
											cellSummary.setCellValue(Common.getDateCurrentTimeZone(exceptionReortsTrip
												.getTripEnd().getTimestamp()));
										else cellSummary.setCellValue("-");
										cellSummary.setCellStyle(wrap_styleSummary);

											
										
										//subdocument.put("end_beat",endActualKm);


										cellSummary = rowSummary.createCell(4);
										if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8&&exceptionReortsTrip.getMaxSpeed()>8)
											cellSummary.setCellValue(exceptionReortsTrip.getTripStart().getMaxSpeed());
										else cellSummary.setCellValue("-");
										cellSummary.setCellStyle(wrap_styleSummary);

									
										
//										subdocument
//												.put("expected_end_beat",
//														df2.format(exceptionReortsTrip
//																.getTripEnd()
//																.getEndKmBeatExpected()));


								

										cellSummary = rowSummary.createCell(5);
										/*if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8&&exceptionReortsTrip.getMaxSpeed()>8)
											cellSummary.setCellValue(exceptionReortsTrip.getTripStart().getMaxSpeed());
										else*/ cellSummary.setCellValue("-");
										cellSummary.setCellStyle(wrap_styleSummary);;
										
//										subdocument.put("max_speed", df2
//												.format(exceptionReortsTrip
//														.getTripStart()
//														.getMaxSpeed()));

										

										cellSummary = rowSummary.createCell(6);
										if (kmcover==0||exceptionReortsTrip.getTripStart()
												.getTotal_distance() < 1||kmcover < expactedKmCover
												- distanceToleranceInKm)
											cellSummary.setCellValue("kmcover : "+df2.format(kmcover)+" Km");
										else cellSummary.setCellValue("-");
										cellSummary.setCellStyle(wrap_styleSummary);
										
//										subdocument.put("kmcover",
//												df2.format(kmcover));
										// System.out.println("  Here--*****************************************--------------------");
										//
									
										cellSummary = rowSummary.createCell(7);
										if (kmcover==0||exceptionReortsTrip.getTripStart()
												.getTotal_distance() < 1||kmcover < expactedKmCover
												- 500)
											cellSummary.setCellValue("Expected "+expactedKmCover+"Km but device cover only : "+df2.format(kmcover)+" Km");
										else cellSummary.setCellValue("-");									
																			
										cellSummary.setCellStyle(wrap_styleSummary);


									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									
									
									
									
									/////Summary report row generate end///////////////////////

									
								}//NA check end
								else{

									
									//Near by beat RDPS NOt FOund
									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());
										
										
										remark_cell
										.setCellValue("Remark status :-Near by beat RDPS Not Found for trip -"+exceptionReortsTrip
												.getTripNo());
										remark_cell.setCellStyle(error_remarkColumnStyle);
										row.setHeight((short) 800);

										subdocument
												.put("Tripno",
														exceptionReortsTrip
																.getTripNo());

										cell.setCellStyle(wrap_style);

										cell = row.createCell(1);
										/*cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));*/

									
										cell.setCellValue("Near By Beat RDPS Not Found");
										cell.setCellStyle(wrap_style);

										cell = row.createCell(2);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatExpected());
									

										cell.setCellStyle(wrap_style);

										
										cell = row.createCell(3);
										
										cell.setCellValue("Near By Beat RDPS Not Found");

										cell.setCellStyle(wrap_style);

										cell = row.createCell(4);
										cell.setCellValue(exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatExpected());
									
										cell.setCellStyle(wrap_style);

										/*
										 * cell = row.createCell(5);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 * cell.setCellStyle(wrap_style);
										 */

										cell = row.createCell(5);
										cell.setCellValue(0);
										
									
										cell.setCellStyle(wrap_style);

										cell = row.createCell(6);
									
										
										cell.setCellValue(0);
										cell.setCellStyle(wrap_style);
										

										cell = row.createCell(7);
									
										DetailRemark="RDPS Data not found near by "+
												exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatExpected()+"-"+exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatExpected()+"Km";
										
										cell.setCellValue(DetailRemark);
										cell.setCellStyle(wrap_style);

										if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
											{
											KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
											KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
										}else{
											KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());

										}
												

										// System.out.println("  Here--*****************************************--------------------");
										//

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									//Near By Beat RDPS Not Found NOt found End/////////////////////////////////
									
									
							
									
								}
								
								
								
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
								
								
							}else if((exceptionReortsTrip.getTripStart()!=null&&(exceptionReortsTrip.getTripStart().getLat()==null
									|| exceptionReortsTrip.getTripStart().getLang()==null)
									&&(exceptionReortsTrip.getTripEnd()!=null&&( exceptionReortsTrip.getTripEnd().getLat()==null
									|| exceptionReortsTrip.getTripEnd().getLang()==null)))){
								
								//RDPS NOt FOund
								try {

									row = sheet.createRow(sheet
											.getLastRowNum() + 1);
									cell = row.createCell(0);
									cell.setCellValue(exceptionReortsTrip
											.getTripNo());
									
									
									remark_cell
									.setCellValue("Remark status :- RDPS Not Found for trip -"+exceptionReortsTrip
											.getTripNo());
									remark_cell.setCellStyle(error_remarkColumnStyle);
									row.setHeight((short) 800);

									subdocument
											.put("Tripno",
													exceptionReortsTrip
															.getTripNo());

									cell.setCellStyle(wrap_style);

									cell = row.createCell(1);
									/*cell.setCellValue(df2
											.format(exceptionReortsTrip
													.getTripStart()
													.getStartkmBeatActual()));*/

								
									cell.setCellValue("RDPS Data Not Found");
									cell.setCellStyle(wrap_style);

									cell = row.createCell(2);
									cell.setCellValue(exceptionReortsTrip
											.getTripStart()
											.getStartkmBeatExpected());
								

									cell.setCellStyle(wrap_style);

									
									cell = row.createCell(3);
									
									cell.setCellValue("RDPS Data Not Found");

									cell.setCellStyle(wrap_style);

									cell = row.createCell(4);
									cell.setCellValue(exceptionReortsTrip
											.getTripEnd()
											.getEndKmBeatExpected());
								
									cell.setCellStyle(wrap_style);

									/*
									 * cell = row.createCell(5);
									 * cell.setCellValue(exceptionReortsTrip
									 * .getTripStart() .getAvgSpeed());
									 * cell.setCellStyle(wrap_style);
									 */

									cell = row.createCell(5);
									cell.setCellValue(0);
									
								
									cell.setCellStyle(wrap_style);

									cell = row.createCell(6);
								
									
									cell.setCellValue(0);
									cell.setCellStyle(wrap_style);
									
									DetailRemark="RDPS Data not found near by "+
											exceptionReortsTrip
											.getTripStart()
											.getStartkmBeatExpected()+"-"+exceptionReortsTrip
											.getTripEnd()
											.getEndKmBeatExpected()+"Km";
									
									cell.setCellValue(DetailRemark);

									if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
										{
										KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
										KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
									}else{
										KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());

									}
											

									// System.out.println("  Here--*****************************************--------------------");
									//

								} catch (NumberFormatException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (MongoException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								//RDPS NOt found End/////////////////////////////////
								
								
						}else {
								// Exception of location not found
								if (exceptionReortsTrip != null
										&& exceptionReortsTrip
												.getLocationSize() == 0)
									try {
										// System.out.println(" **exceptionReortsTrip*******row******************"
										// +
										// "***-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size()+"-----"+exceptionReortsTrip.getLocationSize());

										if (!deviceInfoList
												.get(i).getName().contains("/LR/")) {
											KeyManOffDevice_Set.add(deviceInfoList
													.get(i).getName());

										}
									
										remark.append("Device is Off");

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(1);
										cell.setCellStyle(loactionNotFoundColumnStyle);

										cell = row.createCell(1);
										cell.setCellValue("Device is Off");
										cell.setCellStyle(loactionNotFoundColumnStyle);
										sheet.addMergedRegion(new CellRangeAddress(
												sheet.getLastRowNum(), sheet
														.getLastRowNum(), 1, 3));

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

							}
						}

						if (remark.toString().length() > 0) {
							remark_cell.setCellValue("Remark status :- "
									+ remark.toString());
							subdocument.put("remark", remark.toString());
							remark_cell.setCellStyle(error_remarkColumnStyle);
						}

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayKeymanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null) {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(
										deviceInfoList.get(i).getName()
												+ "   ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ")");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));
								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :- RDPS data not found.");
								subdocument.put("remark",
										"RDPS data not found.");
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								// cell.setCellStyle(error_remarkColumnStyle);

								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(

										deviceInfoList.get(i).getName()
												+ "  ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ") ");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :-Trips not Shedule for this device please contact to admin to allocate work schedule to this device.");
								subdocument.put("remark",
										"Trips not Shedule for this device.");


								if (!deviceInfoList
										.get(i).getName().contains("/LR/"))
							KeyManExceptionalDevices_Set.add(deviceInfoList
									.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 7));

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());
					if (startBatteryStatus < 3
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName())&&!deviceInfoList
											.get(i).getName().contains("/LR/"))
						KeyManLowBattery_Set.add(deviceInfoList.get(i)
								.getName());

					sectionDeviceList.add(subdocument);
					
					
					if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName())&&
							KeyManExceptionalDevices_Set.contains(deviceInfoList.get(i).getName())) 
						KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());

				} else {
					// System.out.println("patrolman Man found--------------------");
					petrolMancount++;
				}

			}

			Maindocument.put("exceptional_device", sectionDeviceList);

			report_table.insert(Maindocument);

			sheet.setColumnWidth(0, 1300);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 2000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);
			sheet.setColumnWidth(7, 3000);

			if (deviceInfoList.size() > 0
					&& petrolMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept() + "_"
						+ "Exception_Trip_Report_KeyMan"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ "_" + parentId + ".xlsx";
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					String sendemail = "";
					for (String emaString : mailSendInfo.get(j).getEmailIds()) {
						sendemail = sendemail + "," + emaString.trim();
					}
					SendEmail mail = new SendEmail();

					
					if(isSendMail)
					 {
						 mail.sendDeviceExceptionMailToJaipur(sendemail, file,
								 mailSendInfo
								  .get(j).getDept(),"Exception Trip Report of keymen"
								  ,"Exception Trip Report of keymen",false
									,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					 }
			
					 
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// /////ALl Section////////////////////////
			
				
			System.err.println("All section--countee--"+KeyManLowBattery_Set.size()+KeyManCoverSucefullyDevices_Set.size()+
					KeyManExceptionalDevices_Set.size()+KeyManOffDevice_Set.size());
			
			
			if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
					KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1)
			
			{
		
			
			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReport);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			cell = row.createCell(1);
			cell.setCellValue(KeyManLowBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight
					.add((short) ((KeyManLowBattery_Set.toString()
							.replace("[", "").replace("]", "").toString()
							.length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(KeyManOffDevice_Set.toString().replace("[", "")
					.replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOffDevice_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			
			
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport);// Apply // style// to// cell
			rowHeight.add((short) ((KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));
			
			/*cell = row.createCell(6);
			if(KeyManExceptionalDevices_Set.size()>0)
				cell.setCellValue("Not completed successfully ");

			else
				cell.setCellValue("Completed successfully");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); */

			short height_row = (short) ((KeyManLowBattery_Set.toString()
					.replace("[", "").replace("]", "").toString().length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);

			sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

			if (j == mailSendInfo.size() - 1) {
				String outFileNameAllSectionStatus = "All_Section_KeyMan_status_Report_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_"+parentId+ ".xlsx";
				
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReport.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReport.close();

					String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," Keyman Work Status Report AllSection",
								  " Keyman Work Status Report AllSection",true	
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					}
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

			// /////////////////////*Alll Section////////////////////
			
			
			
			
			// /////ALl Section COunt////////////////////////
			 divDeviceOffCount=divDeviceOffCount+KeyManOffDevice_Set.size();
			 divDeviceBeatNotCoverCount=divDeviceBeatNotCoverCount+KeyManExceptionalDevices_Set.size();
			 divDeviceBeatCoverCount=divDeviceBeatCoverCount+KeyManCoverSucefullyDevices_Set.size();

			
	
				 if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
							KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1)
				 {	

			row = sheetAlertForKeymanWorkStatusReportCount
					.createRow(sheetAlertForKeymanWorkStatusReportCount
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	

			cell = row.createCell(1);
			cell.setCellValue(KeyManOffDevice_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	
			cell = row.createCell(2);
			cell.setCellValue(KeyManExceptionalDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			

			cell = row.createCell(3);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

			cell = row.createCell(4);
			cell.setCellValue((KeyManCoverSucefullyDevices_Set.size()+KeyManOffDevice_Set.size()+
					KeyManExceptionalDevices_Set.size())+"");
			
			
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			
	
			
			row.setHeight((short) 600);

			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(0, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(4, 3000);

			if (j == mailSendInfo.size() - 1) {
				
				//for total row
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount
								.getLastRowNum() + 1);
				row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				cell = row.createCell(0);
				cell.setCellValue("Total Device");

				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

				cell = row.createCell(1);
				cell.setCellValue(divDeviceOffCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				;

				cell = row.createCell(2);
				cell.setCellValue(divDeviceBeatNotCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				
				cell = row.createCell(3);
				cell.setCellValue(divDeviceBeatCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				

				cell = row.createCell(4);
				cell.setCellValue((divDeviceOffCount+divDeviceBeatCoverCount+
						divDeviceBeatNotCoverCount)+"");
				
				
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);


				
				row.setHeight((short) 600);

				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(0, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(1, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(2, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(3, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(4, 3000);
				
				String outFileNameAllSectionStatus = "All_Section_KeyMan_Status_Device_Count_Report_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_"+parentId+ ".xlsx";
				
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReportCount.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReportCount.close();

					String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					/*if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," Keyman Work Status device count Report AllSection",
								  " Keyman Work Status device count Report AllSection",true	
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					}*/
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

			// /////////////////////*Alll Section COunt end ////////////////////
			

					// /////Report  Summary ////////////////////////
			
				
					if (j == mailSendInfo.size() - 1&&exceptionSummary) {
						
						sheetSummary.setColumnWidth(0, 800);
						sheetSummary.setColumnWidth(1, 4000);
						sheetSummary.setColumnWidth(2, 3000);
						sheetSummary.setColumnWidth(3, 3000);
						sheetSummary.setColumnWidth(4, 1500);
						sheetSummary.setColumnWidth(5, 3000);
						sheetSummary.setColumnWidth(6, 3000);
						sheetSummary.setColumnWidth(7, 10000);

						
						String outFileNameAllSectionStatus = "Exception_Keyman_Device_Summary_"								
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
									+ "_"+parentId+ ".xlsx";
						
						
						try {
							String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
									+ outFileNameAllSectionStatus;
							FileOutputStream fosalert = new FileOutputStream(new File(
									fileAllSection));
						workbookSummary.write(fosalert);
							fosalert.flush();
							fosalert.close();
							workbookSummary.close();

							String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
							SendEmail mail = new SendEmail();
							System.err.println("Send mail All Section----" + sendemail);
							/*if(isSendMail)
							{
								mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
										  "All Section"," Keyman Work Status device count Report AllSection",
										  " Keyman Work Status device count Report AllSection",true	
										  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

							}*/
								
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					// /////////////////////*Report Summary end////////////////////
			///Insert Datewise Saction Data
			 
			if(isSendMail)
			 try {

					// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
					java.sql.CallableStatement ps = con
							.prepareCall("{call SaveDatewiseExceptionReport(?,?,?,?,?,?,?,?)}");

					
					ps.setInt(1, Integer.parseInt(parentId));
					ps.setString(2,DayStartWorkTime+"");
					ps.setString(3, KeyManLowBattery_Set.toString().replace("[", "")
							.replace("]", ""));

					ps.setString(4,KeyManOffDevice_Set.toString()
							.replace("[", "").replace("]", ""));

					ps.setString(5, KeyManExceptionalDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(6, KeyManOverspeedDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(7,mailSendInfo.get(j).getDept() );
					ps.setString(8, KeyManCoverSucefullyDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					int result = ps.executeUpdate();
					if (result == 0) {
						msgo.setError("true");
						msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
					} else {
						// //System.err.println("Error=="+result);
						msgo.setError("false");
						msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
				}

		}// //////////for section end

		return msgo;

	}

	
	
	public MessageObject GenerateExceptionReportPatrolManBeatPathAsKeyman(

			Connection con, DB mongoconnection, String parentId, int day, Boolean isSendMail, int dutyStartTime, int dutyEndTime) {
		LoginDAO loginObj=new LoginDAO();

		MessageObject msgo = new MessageObject();
		ArrayList<RailMailSendInfoDto> mailSendInfo = loginObj.getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionReportPatrolManBeatPathAsKeyman ---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-day;
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

		// //****//////////////////////// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("PatrolMan Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"Patrolman Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						5));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);

		row.setHeight((short) 800);

		// //////////////////////////All Section End///////////////////////////////////////

		
		//*******////////// All Section Count AlertForKeymanWorkStatusReport*/////////////////////////

		XSSFWorkbook workbookPatrolmanWorkStatusReportCount = new XSSFWorkbook();
		XSSFSheet sheetPatrolmanWorkStatusReportCount = workbookPatrolmanWorkStatusReportCount
				.createSheet(WorkbookUtil
						.createSafeSheetName("PatrolMan Status Report with count"));
		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReportCount = workbookPatrolmanWorkStatusReportCount
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReportCount
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReportCount
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		 font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		 tripColumnStyleAlertForKeymanWorkStatusReport = workbookPatrolmanWorkStatusReportCount
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		XSSFCellStyle wrap_styleAlertForKeymanWorkStatusReportCount = workbookPatrolmanWorkStatusReportCount
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReportCount.setWrapText(true); // Set
															

		row = sheetPatrolmanWorkStatusReportCount.createRow(0);
		row = sheetPatrolmanWorkStatusReportCount
				.createRow(sheetPatrolmanWorkStatusReportCount.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetPatrolmanWorkStatusReportCount
				.createRow(sheetPatrolmanWorkStatusReportCount.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetPatrolmanWorkStatusReportCount
				.createRow(sheetPatrolmanWorkStatusReportCount.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"PatrolMan Work Status Count Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetPatrolmanWorkStatusReportCount
				.addMergedRegion(new CellRangeAddress(
						sheetPatrolmanWorkStatusReportCount.getLastRowNum(),
						sheetPatrolmanWorkStatusReportCount.getLastRowNum(), 0,
						4));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReportCount);

		row = sheetPatrolmanWorkStatusReportCount
				.createRow(sheetPatrolmanWorkStatusReportCount.getLastRowNum() + 1);

		 cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		/*cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);*/
		cell = row.createCell(1);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(3);
		cell.setCellValue("Beat competed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(4);
		cell.setCellValue("Total Device");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 800);
	/*	cell = row.createCell(6);
		cell.setCellValue("Section Status");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 500);*/

		// ///////////////////////////////////All Section COunt// End//////////////////////////////////////////////////////////

		int divDeviceOffCount=0;
		int divDeviceBeatNotCoverCount=0;
		int divDeviceBeatCoverCount=0;

		
		
		for (int j = 0; j < mailSendInfo.size(); j++) {
		//	for (int j = 0; j <=5;j++) {

			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report of keymen"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyle.setAlignment(HorizontalAlignment.CENTER);
			font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);

			XSSFCellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap
			wrap_style.setAlignment(HorizontalAlignment.CENTER);

			int petrolMancount = 0;
			

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for Patrolman of section "
							+ mailSendInfo.get(j).getDept() + "  Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyle);
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 6));

			BasicDBList sectionDeviceList = new BasicDBList();

			for (int i = 0; i < deviceInfoList.size(); i++) {
				// for (int i = 4; i <=4; i++) {

				System.err.println(" **Device =---================"
						+ deviceInfoList.get(i).getName() + "\n");

				DeviceBatteryStatusDTO BatteryStatus =getBatterystatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+dutyStartTime,DayStartWorkTime+dutyEndTime);
				
				

				if (deviceInfoList.get(i).getName().startsWith("P/")) {
					BasicDBObject subdocument = new BasicDBObject();

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfKeyman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));

								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

								dto.setSectionName(rsgertrip
										.getString("SectionName"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));
								RailwayKeymanTripsBeatsList.add(dto);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					// long DayStartWorkTime=1547836200;
					// System.out.println("DayStartWorkTime----" +
					// DayStartWorkTime);

					Double distanceTolerance = 1.3;
					// Double startLatOfRouteKm = 0.0, startLangOfRouteKm = 0.0,
					// endLatOfRouteKm = 0.0, endLangOfRouteKm = 0.0;

					// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
					// RailwayPetrolmanTripsBeatsList) {
					for (int r = 0; r < RailwayKeymanTripsBeatsList.size(); r++) {
						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayKeymanTripsBeatsList
								.get(r);

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.parseLong("355488020181042"));

						/*
						 * BasicDBObject timestamp_whereQuery = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime).append("$lt",
						 * DayStartWorkTime+86400));
						 */

						/*Summer Time
						 * BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 23400).append("$lt",
										DayStartWorkTime + 37800));
						BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 41400).append("$lt",
										DayStartWorkTime + 55800));
										
*/
						//Winter Time
					/*	BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 18000).append("$lt",
										DayStartWorkTime + 36000));
						BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 43200).append("$lt",
										DayStartWorkTime + 54000));
*/
						//Winter Time all 8 hr calculated for trip complete
						BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime +dutyStartTime).append("$lt",
										DayStartWorkTime + dutyEndTime));
						/*
						 * BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
						 * "timestamp", new BasicDBObject("$gte", DayStartWorkTime - 7200).append("$lt",
						 * DayStartWorkTime + 32400));
						 */
						/*
						 * BasicDBObject timestamp_whereQuery_morning = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+23400).append("$lt",
						 * DayStartWorkTime+37800)); BasicDBObject
						 * timestamp_whereQuery_afternoon = new BasicDBObject(
						 * "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+41400).append("$lt",
						 * DayStartWorkTime+55800));
						 */
						BasicDBList morn_Milage = new BasicDBList();
						morn_Milage.add(timestamp_whereQuery_morning);

						morn_Milage.add(device_whereQuery);
						DBObject morn_Milage_query = new BasicDBObject("$and",
								morn_Milage);

						/*
						 * BasicDBList after_Milage = new BasicDBList();
						 * after_Milage.add(timestamp_whereQuery_afternoon);
						 * after_Milage.add(device_whereQuery); DBObject after_Milage_query = new
						 * BasicDBObject("$and", after_Milage);
						 */

						/*
						 * BasicDBList Milage = new BasicDBList(); Milage.add(morn_Milage_query);
						 * Milage.add(after_Milage_query); DBObject final_query = new
						 * BasicDBObject("$or", Milage);
						 */
						DBCursor cursor = table.find(morn_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + morn_Milage_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							System.err.println("*--listObjects---COunt---"
									+ cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();

						}

						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());
							;

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = loginObj.locationNearbyKM(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"P");
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation = loginObj.locationNearbyKM(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"P");
								if (tripEndLocation.getEndKmBeatActual() <= 0) {
									tripStartLocation.setStartkmBeatActual(0);

								}

								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								
								
								for (DBObject dbObject : listObjects) {
									int SpeedcCheck = (int) dbObject.get("speed");
									Long timeloc=Long.parseLong(dbObject.get("timestamp")+"");
								
							
									if(SpeedcCheck>8&&((timeloc>(DayStartWorkTime + 21600) && timeloc<(DayStartWorkTime+36000) ))){
										exDto.setMaxSpeed(SpeedcCheck);
										}
									
									}

			
								
								exceptiionalTrip.add(exDto);
							} else {

								exceptiionalTrip.add(null);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setLocationSize(0);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exDto.setMaxSpeed(0);
							exceptiionalTrip.add(exDto);
						}

					}
					System.out.println("\n\n");
					System.out
							.println("  Here--*************exceptiionalTrip****************************--------------------"
									+ exceptiionalTrip.size());
					System.out.println("\n\n");
					// //////////////////////////////////////////////

					// //////////////////////////////////
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null) {
						StringBuilder remark = new StringBuilder();
						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								deviceInfoList.get(i).getName() + "   ("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")");
						subdocument.put("device", deviceInfoList.get(i)
								.getDeviceID());
						subdocument.put("device_name", deviceInfoList.get(i)
								.getName());

						row.getCell(0).setCellStyle(HeadingStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));
						// start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((BatteryStatus.getStartBatteryStatus() / 6) * 100))
								+ " %");
						subdocument.put("start_battery_status",
								df2.format(((BatteryStatus.getStartBatteryStatus() / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						// End Battery Staus
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("End Battery status :- "
								+ df2.format(((BatteryStatus.getEndBatteryStatus() / 6) * 100))
								+ " %");
						subdocument.put("end_battery_status",
								df2.format(((BatteryStatus.getEndBatteryStatus() / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");

						remark_cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						cell = row.createCell(0);
						cell.setCellValue("Trip#");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(1);
						cell.setCellValue("Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue("Expected Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue("End Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(4);
						cell.setCellValue("Expected end Beat");
						cell.setCellStyle(tripColumnStyle);

						/*
						 * cell = row.createCell(5);
						 * cell.setCellValue("Avg speed");
						 * cell.setCellStyle(tripColumnStyle);
						 */

						cell = row.createCell(5);
						cell.setCellValue("Max speed");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(6);
						cell.setCellValue("Distance cover");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						for (ExceptionReortsTrip exceptionReortsTrip : exceptiionalTrip) {
							System.out
									.println(" **exceptiionalTrip=---================"
											+ new Gson()
													.toJson(exceptionReortsTrip));

							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null
									&& exceptionReortsTrip.getLocationSize() > 0) {

								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = 0;
									double expactedKmCover = 0;
									if (exceptionReortsTrip.getTripStart()
											.getStartkmBeatActual() != 0
											&& exceptionReortsTrip.getTripEnd()
													.getEndKmBeatActual() != 0
											&& exceptionReortsTrip
													.getTripStart()
													.getMinDistance() < 5
											&& exceptionReortsTrip.getTripEnd()
													.getMinDistance() < 5) {
										kmcover = (exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatActual() - exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatActual());
										if (kmcover < 0)
											kmcover = -1 * kmcover;
										expactedKmCover = exceptionReortsTrip
												.getTripStart()
												.getEndKmBeatExpected()
												- exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected();
										if (expactedKmCover < 0)
											expactedKmCover = -1
													* expactedKmCover;

										if (kmcover < expactedKmCover
												- distanceTolerance) {
											remark.append("\tTotal beats not completed in Trip "
													+ exceptionReortsTrip
															.getTripNo() + ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark_cell
													.setCellValue("Remark status :- All work done succesfully.");
											KeyManCoverSucefullyDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
										}

									} else {
										kmcover = 0;

										if (exceptionReortsTrip.getTripStart()
												.getTotal_distance() < 1) {
											remark.append("\tDevice is not moved "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else if (exceptionReortsTrip
												.getTripStart()
												.getMinDistance() > 5
												&& exceptionReortsTrip
														.getTripEnd()
														.getMinDistance() > 5) {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											KeyManExceptionalDevices_Set.add(deviceInfoList
													.get(i).getName());
										}

									}

			
									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());
										subdocument
												.put("Tripno",
														exceptionReortsTrip
																.getTripNo());

										cell.setCellStyle(wrap_style);

										cell = row.createCell(1);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));
										subdocument
												.put("start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatActual()));
										cell.setCellStyle(wrap_style);

										cell = row.createCell(2);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatExpected());
										subdocument
												.put("expected_start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatExpected()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(3);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));
										subdocument.put("end_beat", df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(4);
										cell.setCellValue(exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatExpected());
										subdocument
												.put("expected_end_beat",
														df2.format(exceptionReortsTrip
																.getTripEnd()
																.getEndKmBeatExpected()));

										cell.setCellStyle(wrap_style);

										/*
										 * cell = row.createCell(5);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 * cell.setCellStyle(wrap_style);
										 */

										cell = row.createCell(5);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart().getMaxSpeed());
										subdocument.put("max_speed", df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getMaxSpeed()));

										if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8&&exceptionReortsTrip.getMaxSpeed()>8) {
											KeyManOverspeedDevices_Set.add(deviceInfoList.get(i)
															.getName());
										}

										cell.setCellStyle(wrap_style);

										cell = row.createCell(6);
										cell.setCellValue(df2.format(kmcover));
										subdocument.put("kmcover",
												df2.format(kmcover));
										cell.setCellStyle(wrap_style);

										// System.out.println("  Here--*****************************************--------------------");
										//

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// Exception of location not found
								if (exceptionReortsTrip != null
										&& exceptionReortsTrip
												.getLocationSize() == 0)
									try {
										// System.out.println(" **exceptionReortsTrip*******row******************"
										// +
										// "***-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size()+"-----"+exceptionReortsTrip.getLocationSize());

										
										if (!deviceInfoList
												.get(i).getName().contains("/LR/")) {
											KeyManOffDevice_Set.add(deviceInfoList
													.get(i).getName());

										}

										remark.append("Device is Off");

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(1);
										cell.setCellStyle(loactionNotFoundColumnStyle);

										cell = row.createCell(1);
										cell.setCellValue("Device is Off");
										cell.setCellStyle(loactionNotFoundColumnStyle);
										sheet.addMergedRegion(new CellRangeAddress(
												sheet.getLastRowNum(), sheet
														.getLastRowNum(), 1, 3));

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

							}
						}

						if (remark.toString().length() > 0) {
							remark_cell.setCellValue("Remark status :- "
									+ remark.toString());
							subdocument.put("remark", remark.toString());
							remark_cell.setCellStyle(error_remarkColumnStyle);
						}

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayKeymanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null) {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(
										deviceInfoList.get(i).getName()
												+ "   ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ")");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((BatteryStatus.getStartBatteryStatus() / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((BatteryStatus.getStartBatteryStatus() / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((BatteryStatus.getEndBatteryStatus() / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((BatteryStatus.getEndBatteryStatus() / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :- RDPS data not found.");
								subdocument.put("remark",
										"RDPS data not found.");
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								// cell.setCellStyle(error_remarkColumnStyle);

								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(

										deviceInfoList.get(i).getName()
												+ "  ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ") ");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((BatteryStatus.getStartBatteryStatus() / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((BatteryStatus.getStartBatteryStatus() / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((BatteryStatus.getEndBatteryStatus() / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((BatteryStatus.getEndBatteryStatus() / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :-Trips not Shedule for this device.");
								subdocument.put("remark",
										"Trips not Shedule for this device.");
								
								if (!deviceInfoList
											.get(i).getName().contains("/LR/"))
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());
					if (BatteryStatus.getStartBatteryStatus() < 3
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName())&&!deviceInfoList
											.get(i).getName().contains("/LR/"))
						KeyManLowBattery_Set.add(deviceInfoList.get(i)
								.getName());
					

					sectionDeviceList.add(subdocument);

				} else {
					// System.out.println("patrolman Man found--------------------");
					petrolMancount++;
				}

			}

			Maindocument.put("exceptional_device", sectionDeviceList);

			report_table.insert(Maindocument);

			sheet.setColumnWidth(0, 1300);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 2000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);

			if (deviceInfoList.size() > 0
					&& petrolMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept() + "_"
						+ "Exception_Trip_Report_Patrolman"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ "_" + parentId + ".xlsx";
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					String sendemail = "";
					for (String emaString : mailSendInfo.get(j).getEmailIds()) {
						sendemail = sendemail + "," + emaString.trim();
					}
					SendEmail mail = new SendEmail();

				
					if(isSendMail)
				{
						mail.sendDeviceExceptionMailToJaipur(sendemail, file,
								 mailSendInfo
								  .get(j).getDept(),"Exception Trip Report of Patrolman"
								  ,"Exception Trip Report of Patrolman",false
									,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

				}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// /////ALl Section////////////////////////

			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReport);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			cell = row.createCell(1);
			cell.setCellValue(KeyManLowBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight
					.add((short) ((KeyManLowBattery_Set.toString()
							.replace("[", "").replace("]", "").toString()
							.length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(KeyManOffDevice_Set.toString().replace("[", "")
					.replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOffDevice_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			short height_row = (short) ((KeyManLowBattery_Set.toString()
					.replace("[", "").replace("]", "").toString().length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);
			System.err.println("Row max height-----" + height_row);

			sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

			if (j == mailSendInfo.size() - 1) {
				String outFileNameAllSectionStatus = "All_Section_Patrolman_Status_Report_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")+ "_"+parentId
						+ ".xlsx";
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReport.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReport.close();
					
					
					String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," Patrolman Work status Report_AllSection",
								  " Patrolman Work status Report_AllSection",true
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					}
						
		
					
					 
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			

			}

			// /////////////////////*Alll Section////////////////////
			
			
			
			// /////ALl Section COunt////////////////////////
			 divDeviceOffCount=divDeviceOffCount+KeyManOffDevice_Set.size();
			 divDeviceBeatNotCoverCount=divDeviceBeatNotCoverCount+KeyManExceptionalDevices_Set.size();
			 divDeviceBeatCoverCount=divDeviceBeatCoverCount+KeyManCoverSucefullyDevices_Set.size();

			


			row = sheetPatrolmanWorkStatusReportCount
					.createRow(sheetPatrolmanWorkStatusReportCount
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	

			cell = row.createCell(1);
			cell.setCellValue(KeyManOffDevice_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	
			cell = row.createCell(2);
			cell.setCellValue(KeyManExceptionalDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			

			cell = row.createCell(3);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

			cell = row.createCell(4);
			cell.setCellValue((KeyManCoverSucefullyDevices_Set.size()+KeyManOffDevice_Set.size()+
					KeyManExceptionalDevices_Set.size())+"");
			
			
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			
	
			
			row.setHeight((short) 600);

			sheetPatrolmanWorkStatusReportCount.setColumnWidth(0, 3000);
			sheetPatrolmanWorkStatusReportCount.setColumnWidth(1, 3000);
			sheetPatrolmanWorkStatusReportCount.setColumnWidth(2, 3000);
			sheetPatrolmanWorkStatusReportCount.setColumnWidth(3, 3000);
			sheetPatrolmanWorkStatusReportCount.setColumnWidth(4, 3000);

			if (j == mailSendInfo.size() - 1) {
				
				//for total row
				row = sheetPatrolmanWorkStatusReportCount
						.createRow(sheetPatrolmanWorkStatusReportCount
								.getLastRowNum() + 1);
				row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				cell = row.createCell(0);
				cell.setCellValue("Total Device");

				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

				cell = row.createCell(1);
				cell.setCellValue(divDeviceOffCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				;

				cell = row.createCell(2);
				cell.setCellValue(divDeviceBeatNotCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				
				cell = row.createCell(3);
				cell.setCellValue(divDeviceBeatCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				

				cell = row.createCell(4);
				cell.setCellValue((divDeviceOffCount+divDeviceBeatCoverCount+
						divDeviceBeatNotCoverCount)+"");
				
				
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);


				
				row.setHeight((short) 600);

				sheetPatrolmanWorkStatusReportCount.setColumnWidth(0, 3000);
				sheetPatrolmanWorkStatusReportCount.setColumnWidth(1, 3000);
				sheetPatrolmanWorkStatusReportCount.setColumnWidth(2, 3000);
				sheetPatrolmanWorkStatusReportCount.setColumnWidth(3, 3000);
				sheetPatrolmanWorkStatusReportCount.setColumnWidth(4, 3000);
				
				String outFileNameAllSectionStatus = "All_Section_PatrolMan_Status_Device_Count_Report_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_"+parentId+ ".xlsx";
				
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookPatrolmanWorkStatusReportCount.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookPatrolmanWorkStatusReportCount.close();

					String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					/*if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," PatrolMan Work Status device count Report AllSection",
								  " PatrolMan Work Status device count Report AllSection",true	
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					}*/
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			

			}

			// /////////////////////*Alll Section COunt end ////////////////////
			
			
			///Insert Datewise Saction Data
			 
			if(isSendMail)
				 try {
		
							// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
							java.sql.CallableStatement ps = con
									.prepareCall("{call SaveDatewiseExceptionReport(?,?,?,?,?,?,?,?)}");
		
							
							ps.setInt(1, Integer.parseInt(parentId));
							ps.setString(2,DayStartWorkTime+"");
							ps.setString(3, KeyManLowBattery_Set.toString().replace("[", "")
									.replace("]", ""));
		
							ps.setString(4,KeyManOffDevice_Set.toString()
									.replace("[", "").replace("]", ""));
		
							ps.setString(5, KeyManExceptionalDevices_Set.toString()
									.replace("[", "").replace("]", ""));
							ps.setString(6, KeyManOverspeedDevices_Set.toString()
									.replace("[", "").replace("]", ""));
							ps.setString(7,mailSendInfo.get(j).getDept() );
							ps.setString(8, KeyManCoverSucefullyDevices_Set.toString()
									.replace("[", "").replace("]", ""));
							int result = ps.executeUpdate();
							if (result == 0) {
								msgo.setError("true");
								msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
							} else {
								// //System.err.println("Error=="+result);
								msgo.setError("false");
								msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
							}
						} catch (Exception e) {
							e.printStackTrace();
							msgo.setError("true");
							msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
						}

		}// //////////for section end

		return msgo;

	}

	public MessageObject GenerateExceptionReportPatrolManBeatPathAsKeymanNewLogic(

			Connection con, DB mongoconnection, String parentId,int day, Boolean isSendMail, long startTime1, long startTime2, long endTime1, long endTime2) 
	{

		MessageObject msgo = new MessageObject();
		LoginDAO loginObj=new LoginDAO();
		ArrayList<RailMailSendInfoDto> mailSendInfo = loginObj.getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionReportPatrolManBeatPathAsKeymanNewLogic ---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-day;
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

		// ///////////////////////////////********************************/////////////////////////////
		// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("Patrolman Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"PatrolMan Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						6));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 800);
	/*	cell = row.createCell(6);
		cell.setCellValue("Section Status");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 500);*/

		// ///////////////////////////////////All Section
		// End//////////////////////////////////////////////////////////
		
	
		// //***************/////////////////////////////
				// All Section Count AlertForKeymanWorkStatusReport

				XSSFWorkbook workbookAlertForKeymanWorkStatusReportCount = new XSSFWorkbook();
				XSSFSheet sheetAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createSheet(WorkbookUtil
								.createSafeSheetName("PatrolMan Status Report with count"));
				XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle();
				HeadingStyleAlertForKeymanWorkStatusReportCount
						.setFillForegroundColor(IndexedColors.AQUA.getIndex());
				HeadingStyleAlertForKeymanWorkStatusReportCount
						.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				 font = workbookAlertForKeymanWorkStatusReport.createFont();
				font.setFontHeightInPoints((short) 20);
				font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

				 tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle();
				tripColumnStyleAlertForKeymanWorkStatusReport
						.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
								.getIndex());
				tripColumnStyleAlertForKeymanWorkStatusReport
						.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
				font = workbookAlertForKeymanWorkStatusReport.createFont();
				font.setFontHeightInPoints((short) 15);
				font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
				XSSFCellStyle wrap_styleAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle(); // Create new style
				wrap_styleAlertForKeymanWorkStatusReportCount.setWrapText(true); // Set
																	

				row = sheetAlertForKeymanWorkStatusReportCount.createRow(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);
				row.createCell(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);
				row.createCell(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);

				row.createCell(0);
				row.getCell(0).setCellValue(
						"Patrolman Work Status Count Report Date :-"
								+ Common.getDateFromLong(DayStartWorkTime));
				sheetAlertForKeymanWorkStatusReportCount
						.addMergedRegion(new CellRangeAddress(
								sheetAlertForKeymanWorkStatusReportCount.getLastRowNum(),
								sheetAlertForKeymanWorkStatusReportCount.getLastRowNum(), 0,
								4));
				row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReportCount);

				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);

				 cell = row.createCell(0);

				cell.setCellValue("Section Name");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				/*cell = row.createCell(1);
				cell.setCellValue("Start with Low Battery Devices");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);*/
				cell = row.createCell(1);
				cell.setCellValue("Off Devices");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				cell = row.createCell(2);
				cell.setCellValue("Beat not cover");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				;
				cell = row.createCell(3);
				cell.setCellValue("Beat completed Sucessfully");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				cell = row.createCell(4);
				cell.setCellValue("Total Device");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				row.setHeight((short) 800);
			/*	cell = row.createCell(6);
				cell.setCellValue("Section Status");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				row.setHeight((short) 500);*/

				// ///////////////////////////////////All Section COunt// End//////////////////////////////////////////////////////////

				int divDeviceOffCount=0;
				int divDeviceBeatNotCoverCount=0;
				int divDeviceBeatCoverCount=0;

		for (int j = 0; j < mailSendInfo.size(); j++) {
//		for (int j = 29; j<=29 ;j++) {
//
			
			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report of Patrolman"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyle.setAlignment(HorizontalAlignment.CENTER);
			font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);

			CellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap

			int petrolMancount = 0;
			

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for Patrolman of section "
							+ mailSendInfo.get(j).getDept() + "  Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyle);
			
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 5));

			BasicDBList sectionDeviceList = new BasicDBList();

			for (int i = 0; i < deviceInfoList.size(); i++) {
//				 for (int i = 5; i <=5; i++) {

				System.err.println(" **Device =---================"+ deviceInfoList.get(i).getName() + "\n");

				Double startBatteryStatus = loginObj.getKeyManStartBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+startTime1);

				Double endBatteryStatus = loginObj.getKeyManEndBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+endTime2);

				if (deviceInfoList.get(i).getName().startsWith("P/")) {
//				if (deviceInfoList.get(i).getName().startsWith("K/")&&deviceInfoList.get(i).getName().contains("-355")){
					BasicDBObject subdocument = new BasicDBObject();

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfKeyman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));

								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

								dto.setSectionName(rsgertrip
										.getString("SectionName"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));
								RailwayKeymanTripsBeatsList.add(dto);

//								System.err.println("startApprox--"+rsgertrip
//										.getDouble("startApprox")+"-endApprox--"+rsgertrip
//										.getDouble("endApprox"));
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					// long DayStartWorkTime=1547836200;
					// System.out.println("DayStartWorkTime----" +
					// DayStartWorkTime);

					Double distanceTolerance = 1.3;
					// Double startLatOfRouteKm = 0.0, startLangOfRouteKm = 0.0,
					// endLatOfRouteKm = 0.0, endLangOfRouteKm = 0.0;

					// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
					// RailwayPetrolmanTripsBeatsList) {
					for (int r = 0; r < RailwayKeymanTripsBeatsList.size(); r++) {
						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayKeymanTripsBeatsList
								.get(r);

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.parseLong("355488020181042"));

						//Winter Time all 8 hr calculated for trip complete
						BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + startTime1).append("$lt",
										DayStartWorkTime + endTime2));

				
						 
						BasicDBList morn_Milage = new BasicDBList();
						morn_Milage.add(timestamp_whereQuery_morning);

						morn_Milage.add(device_whereQuery);
						DBObject morn_Milage_query = new BasicDBObject("$and",
								morn_Milage);


						DBCursor cursor = table.find(morn_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + morn_Milage_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							System.err.println("*--listObjects---COunt---"
									+ cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();

						}

						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());
							;

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = loginObj.locationNearbyKM(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"K",DayStartWorkTime+startTime1+1000,
										DayStartWorkTime+endTime1,DayStartWorkTime+startTime2,DayStartWorkTime+endTime2);
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation =loginObj. locationNearbyKM(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"K",DayStartWorkTime+startTime1+1000,
										DayStartWorkTime+endTime1,DayStartWorkTime+startTime2,DayStartWorkTime+endTime2);
								if (tripEndLocation.getEndKmBeatActual() <= 0) {
									tripStartLocation.setStartkmBeatActual(0);

								}

								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								
								
								for (DBObject dbObject : listObjects) {
									int SpeedcCheck = (int) dbObject.get("speed");
									Long timeloc=Long.parseLong(dbObject.get("timestamp")+"");
								
							
									if(SpeedcCheck>8&&((timeloc>(DayStartWorkTime + startTime1) && timeloc<(DayStartWorkTime+endTime1) )||
											(timeloc>(DayStartWorkTime + startTime2) && timeloc<(DayStartWorkTime+endTime2) ))){
										exDto.setMaxSpeed(SpeedcCheck);
										exDto.setMaxSpeedTime(timeloc);

										}
									
									}


								exDto.setSectionName(railMailSendInfoDto.getSectionName());
								exceptiionalTrip.add(exDto);
							} else {
								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								PoleNearByLocationDto tripEndLocation=new PoleNearByLocationDto();
								tripEndLocation.setStartkmBeatExpected(railMailSendInfoDto.getKmStart());
								tripEndLocation.setEndKmBeatExpected(railMailSendInfoDto.getKmEnd());
								exDto.setTripStart(tripEndLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
				
								exceptiionalTrip.add(exDto);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setLocationSize(0);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exDto.setMaxSpeed(0);
							exDto.setSectionName(railMailSendInfoDto.getSectionName());

							exceptiionalTrip.add(exDto);
						}
						
					}


					// //////////////////////////////////DayStartWorkTime
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null) {
						StringBuilder remark = new StringBuilder();
						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								deviceInfoList.get(i).getName() + "   ("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")");
						subdocument.put("device", deviceInfoList.get(i)
								.getDeviceID());
						subdocument.put("device_name", deviceInfoList.get(i)
								.getName());

						row.getCell(0).setCellStyle(HeadingStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));
						// start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((startBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("start_battery_status",
								df2.format(((startBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						// End Battery Staus
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						if (endBatteryStatus>0) {
							cell.setCellValue("End Battery status :- "
									+ df2.format(((endBatteryStatus / 6) * 100))
									+ " %");
						}else{
							cell.setCellValue("End Battery status :- 0% / May be device off ");
						}
						
						subdocument.put("end_battery_status",
								df2.format(((endBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");

						remark_cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						cell = row.createCell(0);
						cell.setCellValue("Trip#");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(1);
						cell.setCellValue("Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue("Expected Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue("End Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(4);
						cell.setCellValue("Expected end Beat");
						cell.setCellStyle(tripColumnStyle);

						/*
						 * cell = row.createCell(5);
						 * cell.setCellValue("Avg speed");
						 * cell.setCellStyle(tripColumnStyle);
						 */

						cell = row.createCell(5);
						cell.setCellValue("Max speed");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(6);
						cell.setCellValue("Distance cover");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						for (ExceptionReortsTrip exceptionReortsTrip : exceptiionalTrip) {
////							System.out
//									.println(" **exceptiionalTrip=---================"
//											+ new Gson()
//													.toJson(exceptionReortsTrip));

							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null
									&& exceptionReortsTrip.getLocationSize() > 0
									&&((exceptionReortsTrip.getTripStart().getLat()!=null
									|| exceptionReortsTrip.getTripStart().getLang()!=null)
									&&( exceptionReortsTrip.getTripEnd().getLat()!=null
									|| exceptionReortsTrip.getTripEnd().getLang()!=null))) {

								String startActualKm=loginObj.getRdpsKmNearByLocation(con, exceptionReortsTrip.getTripStart().getLat(),
										exceptionReortsTrip.getTripStart().getLang(), parentId,exceptionReortsTrip.getSectionName(),exceptionReortsTrip.getTripStart().getStartkmBeatExpected());
								
								String endActualKm=loginObj.getRdpsKmNearByLocation(con, exceptionReortsTrip.getTripEnd().getLat(),
										exceptionReortsTrip.getTripEnd().getLang(), parentId,exceptionReortsTrip.getSectionName(),exceptionReortsTrip.getTripEnd().getEndKmBeatExpected());
								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = 0;
									double expactedKmCover = 0;
									if (exceptionReortsTrip.getTripStart()
											.getStartkmBeatActual() != 0
											&& exceptionReortsTrip.getTripEnd()
													.getEndKmBeatActual() != 0) {
										
										
									/*	kmcover = (exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatActual() - exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatActual());
										if (kmcover < 0)
											kmcover = -1 * kmcover;
										*/
										if (!endActualKm.equals("NA")&&!startActualKm.equals("NA"))
										{
										
										kmcover=Double.parseDouble(endActualKm)-Double.parseDouble(startActualKm);
										if (kmcover<0) 								
										kmcover=kmcover*-1;
										
										expactedKmCover = exceptionReortsTrip
												.getTripStart()
												.getEndKmBeatExpected()
												- exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected();
										
										
										if (expactedKmCover < 0)
											expactedKmCover = -1
													* expactedKmCover;
										
										
										if(kmcover!=0&&exceptionReortsTrip
												.getTripStart().getMaxSpeed()<=8&&kmcover >= expactedKmCover
												- distanceTolerance) {
											
											remark_cell
											.setCellValue("Remark status :- All work done succesfully.");
									KeyManCoverSucefullyDevices_Set
											.add(deviceInfoList.get(i)
													.getName());
									
									
										}else if(kmcover!=0&&exceptionReortsTrip
												.getTripStart().getMaxSpeed()>8&&kmcover >= expactedKmCover
												- distanceTolerance) {
											
 											remark.append("\tWork done succesfully But found in Overspeed "
 													+ "at "+Common.getDateCurrentTimeZone(exceptionReortsTrip.getMaxSpeedTime()));
									KeyManCoverSucefullyDevices_Set
											.add(deviceInfoList.get(i)
													.getName());
									
									
										}else if (kmcover!=0&&kmcover < expactedKmCover
												- distanceTolerance) {
											remark.append("\tTotal beats not completed in Trip "
													+ exceptionReortsTrip
															.getTripNo() + ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										}
										else if (kmcover==0&&exceptionReortsTrip.getTripStart()
												.getTotal_distance() < 1){
											remark.append("\tDevice is not moved "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
											
										}else if (kmcover==0&&exceptionReortsTrip.getTripStart()
												.getTotal_distance() > 1) {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										}else{
											remark.append("\tDevice is not moved "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
										}

									}

									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());
										subdocument
												.put("Tripno",
														exceptionReortsTrip
																.getTripNo());

										cell.setCellStyle(wrap_style);

										cell = row.createCell(1);
										/*cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));*/

									
										cell.setCellValue(startActualKm)
										;
										subdocument
												.put("start_beat",startActualKm);
										cell.setCellStyle(wrap_style);

										cell = row.createCell(2);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatExpected());
										subdocument
												.put("expected_start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatExpected()));

										cell.setCellStyle(wrap_style);

										
										cell = row.createCell(3);
										
												
									/*	cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));*/
												cell.setCellValue(endActualKm);
										subdocument.put("end_beat",endActualKm);

										cell.setCellStyle(wrap_style);

										cell = row.createCell(4);
										cell.setCellValue(exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatExpected());
										subdocument
												.put("expected_end_beat",
														df2.format(exceptionReortsTrip
																.getTripEnd()
																.getEndKmBeatExpected()));

										cell.setCellStyle(wrap_style);

										/*
										 * cell = row.createCell(5);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 * cell.setCellStyle(wrap_style);
										 */

										cell = row.createCell(5);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart().getMaxSpeed());
										subdocument.put("max_speed", df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getMaxSpeed()));

										if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8&&exceptionReortsTrip.getMaxSpeed()>8) {
											KeyManOverspeedDevices_Set.add(deviceInfoList.get(i)
															.getName());
										}

										cell.setCellStyle(wrap_style);

										cell = row.createCell(6);
									
										
										cell.setCellValue(df2.format(kmcover));
										subdocument.put("kmcover",
												df2.format(kmcover));
										cell.setCellStyle(wrap_style);

										// System.out.println("  Here--*****************************************--------------------");
										//

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								

									}//NA check end
									else{

										
										//Near by beat RDPS NOt FOund
										try {

											row = sheet.createRow(sheet
													.getLastRowNum() + 1);
											cell = row.createCell(0);
											cell.setCellValue(exceptionReortsTrip
													.getTripNo());
											
											
											remark_cell
											.setCellValue("Remark status :-Near by beat RDPS Not Found for trip -"+exceptionReortsTrip
													.getTripNo());
											remark_cell.setCellStyle(error_remarkColumnStyle);
											row.setHeight((short) 800);

											subdocument
													.put("Tripno",
															exceptionReortsTrip
																	.getTripNo());

											cell.setCellStyle(wrap_style);

											cell = row.createCell(1);
											/*cell.setCellValue(df2
													.format(exceptionReortsTrip
															.getTripStart()
															.getStartkmBeatActual()));*/

										
											cell.setCellValue("Near By Beat RDPS Not Found");
											cell.setCellStyle(wrap_style);

											cell = row.createCell(2);
											cell.setCellValue(exceptionReortsTrip
													.getTripStart()
													.getStartkmBeatExpected());
										

											cell.setCellStyle(wrap_style);

											
											cell = row.createCell(3);
											
											cell.setCellValue("Near By Beat RDPS Not Found");

											cell.setCellStyle(wrap_style);

											cell = row.createCell(4);
											cell.setCellValue(exceptionReortsTrip
													.getTripEnd()
													.getEndKmBeatExpected());
										
											cell.setCellStyle(wrap_style);

											/*
											 * cell = row.createCell(5);
											 * cell.setCellValue(exceptionReortsTrip
											 * .getTripStart() .getAvgSpeed());
											 * cell.setCellStyle(wrap_style);
											 */

											cell = row.createCell(5);
											cell.setCellValue(0);
											
										
											cell.setCellStyle(wrap_style);

											cell = row.createCell(6);
										
											
											cell.setCellValue(0);
											cell.setCellStyle(wrap_style);
											
											

											if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
												{
												KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
												KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
											}else{
												KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());

											}
													

											// System.out.println("  Here--*****************************************--------------------");
											//

										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (MongoException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										//Near By Beat RDPS Not Found NOt found End///////////////////////////////
									}
									

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
								
								
								
							}else if((exceptionReortsTrip.getTripStart()!=null&&(exceptionReortsTrip.getTripStart().getLat()==null
									|| exceptionReortsTrip.getTripStart().getLang()==null)
									&&(exceptionReortsTrip.getTripEnd()!=null&&( exceptionReortsTrip.getTripEnd().getLat()==null
									|| exceptionReortsTrip.getTripEnd().getLang()==null)))){
								
								//RDPS NOt FOund
								try {

									row = sheet.createRow(sheet
											.getLastRowNum() + 1);
									cell = row.createCell(0);
									cell.setCellValue(exceptionReortsTrip
											.getTripNo());
									
									
									remark_cell
									.setCellValue("Remark status :- RDPS Not Found for trip -"+exceptionReortsTrip
											.getTripNo());
									remark_cell.setCellStyle(error_remarkColumnStyle);
									row.setHeight((short) 800);

									subdocument
											.put("Tripno",
													exceptionReortsTrip
															.getTripNo());

									cell.setCellStyle(wrap_style);

									cell = row.createCell(1);
									/*cell.setCellValue(df2
											.format(exceptionReortsTrip
													.getTripStart()
													.getStartkmBeatActual()));*/

								
									cell.setCellValue("RDPS Not Found");
									cell.setCellStyle(wrap_style);

									cell = row.createCell(2);
									cell.setCellValue(exceptionReortsTrip
											.getTripStart()
											.getStartkmBeatExpected());
								

									cell.setCellStyle(wrap_style);

									
									cell = row.createCell(3);
									
									cell.setCellValue("RDPS Not Found");

									cell.setCellStyle(wrap_style);

									cell = row.createCell(4);
									cell.setCellValue(exceptionReortsTrip
											.getTripEnd()
											.getEndKmBeatExpected());
								
									cell.setCellStyle(wrap_style);

									/*
									 * cell = row.createCell(5);
									 * cell.setCellValue(exceptionReortsTrip
									 * .getTripStart() .getAvgSpeed());
									 * cell.setCellStyle(wrap_style);
									 */

									cell = row.createCell(5);
									cell.setCellValue(0);
									
								
									cell.setCellStyle(wrap_style);

									cell = row.createCell(6);
								
									
									cell.setCellValue(0);
									cell.setCellStyle(wrap_style);
									
									

									if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
										{
										KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
										KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
									}
											

									// System.out.println("  Here--*****************************************--------------------");
									//

								} catch (NumberFormatException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (MongoException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								//RDPS NOt found End/////////////////////////////////
								
								
						}else {
								// Exception of location not found
								if (exceptionReortsTrip != null
										&& exceptionReortsTrip
												.getLocationSize() == 0)
									try {
										// System.out.println(" **exceptionReortsTrip*******row******************"
										// +
										// "***-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size()+"-----"+exceptionReortsTrip.getLocationSize());

										if (!deviceInfoList
												.get(i).getName().contains("/LR/")) {
											KeyManOffDevice_Set.add(deviceInfoList
													.get(i).getName());

										}
									
										remark.append("Device is Off");

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(1);
										cell.setCellStyle(loactionNotFoundColumnStyle);

										cell = row.createCell(1);
										cell.setCellValue("Device is Off");
										cell.setCellStyle(loactionNotFoundColumnStyle);
										sheet.addMergedRegion(new CellRangeAddress(
												sheet.getLastRowNum(), sheet
														.getLastRowNum(), 1, 3));

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

							}
						}

						if (remark.toString().length() > 0) {
							remark_cell.setCellValue("Remark status :- "
									+ remark.toString());
							subdocument.put("remark", remark.toString());
							remark_cell.setCellStyle(error_remarkColumnStyle);
						}

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayKeymanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null) {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(
										deviceInfoList.get(i).getName()
												+ "   ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ")");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :- RDPS data not found.");
								subdocument.put("remark",
										"RDPS data not found.");
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								// cell.setCellStyle(error_remarkColumnStyle);

								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(

										deviceInfoList.get(i).getName()
												+ "  ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ") ");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :-Trips not Shedule for this device.");
								subdocument.put("remark",
										"Trips not Shedule for this device.");


								if (!deviceInfoList
										.get(i).getName().contains("/LR/"))
							KeyManExceptionalDevices_Set.add(deviceInfoList
									.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());
					if (startBatteryStatus < 3
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName())&&!deviceInfoList
											.get(i).getName().contains("/LR/"))
						KeyManLowBattery_Set.add(deviceInfoList.get(i)
								.getName());

					sectionDeviceList.add(subdocument);
					
					
					if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName())&&
							KeyManExceptionalDevices_Set.contains(deviceInfoList.get(i).getName())) 
						KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());

				} else {
					// System.out.println("patrolman Man found--------------------");
					petrolMancount++;
				}

			}

			Maindocument.put("exceptional_device", sectionDeviceList);

			report_table.insert(Maindocument);

			sheet.setColumnWidth(0, 1300);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 2000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);

			if (deviceInfoList.size() > 0
					&& petrolMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept() + "_"
						+ "Exception_Trip_Report_Patrolman"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ "_" + parentId + ".xlsx";
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					String sendemail = "";
					for (String emaString : mailSendInfo.get(j).getEmailIds()) {
						sendemail = sendemail + "," + emaString.trim();
					}
					SendEmail mail = new SendEmail();

					
					if(isSendMail)
					 {
						 mail.sendDeviceExceptionMailToJaipur(sendemail, file,
								 mailSendInfo
								  .get(j).getDept(),"Exception Trip Report of Patrolman"
								  ,"Exception Trip Report of Patrolman",false
									,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					 }
			
					 
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// /////ALl Section////////////////////////
			
				
			System.err.println("All section--countee--"+KeyManLowBattery_Set.size()+KeyManCoverSucefullyDevices_Set.size()+
					KeyManExceptionalDevices_Set.size()+KeyManOffDevice_Set.size());
			
			
			if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
					KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1)
			
			{
		
			
			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReport);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			cell = row.createCell(1);
			cell.setCellValue(KeyManLowBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight
					.add((short) ((KeyManLowBattery_Set.toString()
							.replace("[", "").replace("]", "").toString()
							.length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(KeyManOffDevice_Set.toString().replace("[", "")
					.replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOffDevice_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			
			
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport);// Apply // style// to// cell
			rowHeight.add((short) ((KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));
			
			/*cell = row.createCell(6);
			if(KeyManExceptionalDevices_Set.size()>0)
				cell.setCellValue("Not completed successfully ");

			else
				cell.setCellValue("Completed successfully");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); */

			short height_row = (short) ((KeyManLowBattery_Set.toString()
					.replace("[", "").replace("]", "").toString().length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);

			sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

			if (j == mailSendInfo.size() - 1) {
				String outFileNameAllSectionStatus = "All_Section_PatrolMan_Status_Report_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_"+parentId+ ".xlsx";
				
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReport.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReport.close();

					String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," Patrolaman Work Status Report AllSection",
								  " Patrolaman Work Status Report AllSection",true	
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					}
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

			// /////////////////////*Alll Section////////////////////
			
			
			
			
			// /////ALl Section COunt////////////////////////
			 divDeviceOffCount=divDeviceOffCount+KeyManOffDevice_Set.size();
			 divDeviceBeatNotCoverCount=divDeviceBeatNotCoverCount+KeyManExceptionalDevices_Set.size();
			 divDeviceBeatCoverCount=divDeviceBeatCoverCount+KeyManCoverSucefullyDevices_Set.size();

			
	
				 if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
							KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1)
				 {	

			row = sheetAlertForKeymanWorkStatusReportCount
					.createRow(sheetAlertForKeymanWorkStatusReportCount
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	

			cell = row.createCell(1);
			cell.setCellValue(KeyManOffDevice_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	
			cell = row.createCell(2);
			cell.setCellValue(KeyManExceptionalDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			

			cell = row.createCell(3);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

			cell = row.createCell(4);
			cell.setCellValue((KeyManCoverSucefullyDevices_Set.size()+KeyManOffDevice_Set.size()+
					KeyManExceptionalDevices_Set.size())+"");
			
			
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			
	
			
			row.setHeight((short) 600);

			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(0, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(4, 3000);

			if (j == mailSendInfo.size() - 1) {
				
				//for total row
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount
								.getLastRowNum() + 1);
				row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				cell = row.createCell(0);
				cell.setCellValue("Total Device");

				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

				cell = row.createCell(1);
				cell.setCellValue(divDeviceOffCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				;

				cell = row.createCell(2);
				cell.setCellValue(divDeviceBeatNotCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				
				cell = row.createCell(3);
				cell.setCellValue(divDeviceBeatCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				

				cell = row.createCell(4);
				cell.setCellValue((divDeviceOffCount+divDeviceBeatCoverCount+
						divDeviceBeatNotCoverCount)+"");
				
				
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);


				
				row.setHeight((short) 600);

				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(0, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(1, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(2, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(3, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(4, 3000);
				
				String outFileNameAllSectionStatus = "All_Section_PatrolMan_Status_Device_Count_Report_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_"+parentId+ ".xlsx";
				
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReportCount.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReportCount.close();

					String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					/*if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," Keyman Work Status device count Report AllSection",
								  " Keyman Work Status device count Report AllSection",true	
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					}*/
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

			// /////////////////////*Alll Section COunt end ////////////////////
			
			
			///Insert Datewise Saction Data
			 
			if(isSendMail)
			 try {

					// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
					java.sql.CallableStatement ps = con
							.prepareCall("{call SaveDatewiseExceptionReport(?,?,?,?,?,?,?,?)}");

					
					ps.setInt(1, Integer.parseInt(parentId));
					ps.setString(2,DayStartWorkTime+"");
					ps.setString(3, KeyManLowBattery_Set.toString().replace("[", "")
							.replace("]", ""));

					ps.setString(4,KeyManOffDevice_Set.toString()
							.replace("[", "").replace("]", ""));

					ps.setString(5, KeyManExceptionalDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(6, KeyManOverspeedDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(7,mailSendInfo.get(j).getDept() );
					ps.setString(8, KeyManCoverSucefullyDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					int result = ps.executeUpdate();
					if (result == 0) {
						msgo.setError("true");
						msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
					} else {
						// //System.err.println("Error=="+result);
						msgo.setError("false");
						msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
				}

		}// //////////for section end

		return msgo;

	}

	
	
	public MessageObject GenerateZoneWiseReport(Connection con,
			DB mongoconnection, String zoneId, int day2, Boolean isSendMail) {
		LoginDAO loginObj=new LoginDAO();

		MessageObject msgo = new MessageObject();
	
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-day2;
		ArrayList<ZoneWiseReportPreDataDTO> zoneDataList=new ArrayList<>();

		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		try {
			System.err.println("New parentId=== " + zoneId);

			java.sql.CallableStatement ps_call_main = con
					.prepareCall("{call GetZoneWiseDivisionDeviceDetails(?,?)}");
			ps_call_main.setInt(1, Integer.parseInt(zoneId));
			ps_call_main.setString(2,DayStartWorkTime+"");

			ResultSet rs = ps_call_main.executeQuery();
			while (rs.next()) {
			
		

							ZoneWiseReportPreDataDTO dto = new ZoneWiseReportPreDataDTO();
							dto.setDivisionName(rs.getString("DivisionName"));
							dto.setBeatCoverSucesfully(rs.getString("BeatCoverSucesfully"));
							dto.setBeatNotCoverDevice(rs.getString("BeatNotCoverDevice"));
							dto.setDivisionParentId(rs.getString("divisionParentId"));
							dto.setIMEI_NO(rs.getString("IMEI_NO"));
							dto.setKeymanCount(rs.getString("KeymanCount"));
							dto.setOffDevices(rs.getString("OffDevices"));
							dto.setOtherDeviceCount(rs.getString("OtherDeviceCount"));
							dto.setPatrolManCount(rs.getString("PatrolManCount"));
							dto.setStudentIds(rs.getString("StudentIds"));
							dto.setTotalDeviceCount(rs.getString("TotalDeviceCount"));
							dto.setZoneName(rs.getString("zoneName"));
							dto.setZoneMailId(rs.getString("zoneMailId"));

							zoneDataList.add(dto);
						
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

	

	XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook
				.createSheet(WorkbookUtil
						.createSafeSheetName("Zone Wise Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbook
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 20);
		HeadingStyleAlertForKeymanWorkStatusReport.setAlignment(HorizontalAlignment.CENTER);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbook
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		
		tripColumnStyleAlertForKeymanWorkStatusReport.setAlignment(HorizontalAlignment.CENTER);

		font = workbook.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbook
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheet.createRow(0);
		row = sheet
				.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0);
		row = sheet
				.createRow(sheet.getLastRowNum() + 1);
		row.createCell(0);
		row = sheet
				.createRow(sheet.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"Zonewise Device Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheet
				.addMergedRegion(new CellRangeAddress(
						sheet.getLastRowNum(),
						sheet.getLastRowNum(), 0,
						5));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheet
				.createRow(sheet.getLastRowNum() + 1);
		
		

		row = sheet.createRow(sheet.getLastRowNum() + 1);




		Cell cell = row.createCell(0);

		cell.setCellValue("Division Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Total Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("No of Gps Devices is in use (On)");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("No of Gps Devices is not in use (off)");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(5);
		cell.setCellValue("No of Devices where Staff not adhere his duty");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(4);
		cell.setCellValue("No of Devices where Staff Complete his duty");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);

		row.setHeight((short) 1000);

		// ///////////////////////////////////All Section
		// End//////////////////////////////////////////////////////////
		

	
		CellStyle wrap_style = workbook.createCellStyle(); // Create new
															// style
		wrap_style.setWrapText(true); // Set wordwrap
		int petrolMancount = 0;
		

		for (int j = 0; j < zoneDataList.size(); j++) {
		//	for (int j = 0; j <=2;j++) {
			ZoneWiseReportPreDataDTO dto=zoneDataList.get(j);
	
			String[] beatCoverSucesfully = dto.getBeatCoverSucesfully().split(",");
			String[] beatNotCoverDevice = dto.getBeatNotCoverDevice().split(",");
			String[] IMEI_NO = dto.getIMEI_NO().split(",");
			String[] offDevices = dto.getOffDevices().split(",");
			String[] studentIds = dto.getStudentIds().split(",");
			
			int OnDeviceCount=getOndeviceCount(mongoconnection,IMEI_NO,DayStartWorkTime,day2);
		
			
			ArrayList<Short> rowHeight = new ArrayList<>();




			row = sheet.createRow(sheet.getLastRowNum() + 1);

			
	

						//row.getCell(0).setCellStyle(HeadingStyle);
					/*	sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));
						*/

						cell = row.createCell(0);
						cell.setCellValue(dto.getDivisionName());
						cell.setCellStyle(wrap_style);

						cell = row.createCell(1);
						cell.setCellValue(dto.getTotalDeviceCount());
						//cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue(OnDeviceCount);
						//cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue(Integer.parseInt(dto.getTotalDeviceCount())-OnDeviceCount);
						//cell.setCellStyle(tripColumnStyle);

					
			
						cell = row.createCell(4);
						cell.setCellValue(beatCoverSucesfully.length);
						//cell.setCellStyle(tripColumnStyle);
						cell = row.createCell(5);
						cell.setCellValue(beatNotCoverDevice.length);
						//cell.setCellStyle(tripColumnStyle);

						
						row.setHeight((short) 600);

				
					
			
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 2000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);


			}

		String outFileName = zoneDataList.get(0).getZoneName() + "_"
				+ "ZoneWise Report"
				+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
				+ "_" + zoneId + ".xlsx";
		try {
			String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
					+ outFileName;
			FileOutputStream fos = new FileOutputStream(new File(file));
			workbook.write(fos);
			fos.flush();
			fos.close();
			workbook.close();

			
			SendEmail mail = new SendEmail();

		
			if(isSendMail)
		{
				mail.sendDeviceExceptionMailToJaipur(zoneDataList.get(0).getZoneMailId(), file,
						zoneDataList.get(0).getZoneName() 
						 ,"ZoneWise Division Report"
						  ,"ZoneWise Division Report",false,"report.primesystrack@gmail.com","Prime@apple2020");
		}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgo;

	}

	private int getOndeviceCount(DB mongoconnection, String[] iMEI_NO, long dayStartWorkTime, int day2) {
		// TODO Auto-generated method stub
		int onDeviceCount=0;
		DBCollection toadytable;
		if (day2>0) {
			 toadytable = mongoconnection
					.getCollection(Common.TABLE_LOCATION);
			
		}else {
			toadytable = mongoconnection
					.getCollection(Common.TABLE_TODAYLOCATION);	
		}
		
		for(String device:iMEI_NO){
			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device",
					Long.parseLong(device.trim()));
		
			BasicDBObject timestamp_whereQuery = new BasicDBObject(
					"timestamp", new BasicDBObject("$lt",
							dayStartWorkTime+86400).append("$gte",dayStartWorkTime));
		
			BasicDBList And_Milage = new BasicDBList();
			And_Milage.add(timestamp_whereQuery);
			And_Milage.add(device_whereQuery);
			DBObject query = new BasicDBObject("$and", And_Milage);
		
			DBCursor cursor = toadytable.find(query)
					.sort(new BasicDBObject("timestamp", 1)).limit(1);
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
			
			System.err.println(cursor.size()+"-----"+query);

				/*while (cursor.hasNext()) {
					onDeviceCount++;
			
				}
		*/
			if (cursor.size()>0) {
				onDeviceCount++;

			}
			
		}
		
		

		return onDeviceCount;
	}

//	
//	BasicDBObject device_whereQuery = new BasicDBObject();
//	device_whereQuery.put("device",
//			Long.parseLong(device.trim()));
//
//	BasicDBObject timestamp_whereQuery = new BasicDBObject(
//			"timestamp", new BasicDBObject("$lt",
//					dayStartWorkTime+86400).append("$gte",dayStartWorkTime));
//
//	BasicDBList And_Milage = new BasicDBList();
//	And_Milage.add(timestamp_whereQuery);
//	And_Milage.add(device_whereQuery);
//	DBObject query = new BasicDBObject("$and", And_Milage);
//
//	DBCursor cursor = table.find(query)
//			.sort(new BasicDBObject("timestamp", -1)).limit(1);
//	cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
//
//	 System.out.print("Cursor Count of u-p---"+cursor.size()+"  "+query);
//	// long total = cursor.count();
//
//	if (cursor.size() != 0) {
//
//		//System.err.println("*-GenerateGPSHolder10MinData----COunt---"+cursor.size());
//
//		while (cursor.hasNext()) {
//
//			onDeviceCount++;
//	
//		}
//	}
//
//	
//}
	
	
	
	
	
	public synchronized PoleNearByLocationDtoStartEnd getNearByLocationNearbyKM(String event,
			double km, double lat1, double lon1,
			ArrayList<DBObject> location_list, long dayStartWorkTime,String devicetype, Connection con, String parentId) {
		
		long startTime,endTime;
		
		if(devicetype.equalsIgnoreCase("K")){
			startTime=dayStartWorkTime+23400;
			endTime=dayStartWorkTime+37800;
					
		}else{
			startTime=dayStartWorkTime;
			endTime=dayStartWorkTime+37800;
		}
		// System.err.println("locationNearbyKM------------"+"km---"+km+"------"+lat1+","+lon1+"---");
		

		
		int MaxSpeed=0 ,MaxSpeedCopy= 0;
		int TotalSpeed = 0;
		StringBuilder speedbuild = new StringBuilder();
		Double Total_distance = 0.0;
		double earthRadius = 3958.75;
		double minDistance = 0;
		int position = 0;
		ArrayList<NearbyFeatureCodeWithDistanceDTO> loactionListNearBy=new ArrayList<>();
		
		
		NearbyFeatureCodeWithDistanceDTO nearStartDto = null;
		NearbyFeatureCodeWithDistanceDTO nearEndDto=null;
		
		for (int i = 0; i < location_list.size(); i++) {

			BasicDBObject locobj = (BasicDBObject) location_list.get(i).get(
					"location");
			double lat2 = locobj.getDouble("lat");
			double lon2 = locobj.getDouble("lon");
			
			

			Long loactionTime = Long.parseLong(location_list.get(i).get(
					"timestamp")
					+ "");
			
			
			try {
				java.sql.CallableStatement ps = con
						.prepareCall("{call GetNearestFeaturecodeWithDistance(?,?,?)}");
				ps.setDouble(1, lat2);
				ps.setDouble(2, lon2);
				ps.setInt(3, Integer.parseInt(parentId));

				ResultSet rs = ps.executeQuery();
				
				
				

				
				
				if (rs != null) {
					while (rs.next()) {

						NearbyFeatureCodeWithDistanceDTO dto=new NearbyFeatureCodeWithDistanceDTO(); 
						dto.setStartLan(lat2);
						dto.setStartLat(lon2);
						dto.setTime(loactionTime);
						
						// list.setPhoto(""+rs.getString("photo"));
						dto.setFeatureDetailLat(rs.getDouble("Latitude"));
						dto.setFeatureDetailLan(rs.getDouble("Longitude"));
						dto.setKm(rs.getInt("KiloMeter"));
						dto.setDisMeter(rs.getInt("Distance"));
						dto.setKmDistance(rs.getDouble("KmDistance"));
						dto.setDistanceCalulated(rs.getDouble("DistanceCal"));
						dto.setFeaturecode(rs.getInt("FeatureCode"));
						dto.setFeatureDetail(rs.getString("FeatureDetail"));
						dto.setSpeed(Integer.parseInt(location_list.get(i).get(
								"speed")+""));
						dto.setSection(rs.getString("Section"));
						
						if(nearStartDto==null){
						
							nearStartDto=dto;
							nearEndDto=dto;
						}else{
							if(dto.getKmDistance()<nearStartDto.getKmDistance())
								nearStartDto=dto;
							if(dto.getKmDistance()>nearEndDto.getKmDistance())
								nearEndDto=dto;

						}
						
						//loactionListNearBy.add(dto);

					}
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
			
			

			if (i > 0) {
				BasicDBObject prev_locobj = (BasicDBObject) location_list.get(
						i - 1).get("location");
				double caldist = distance(prev_locobj.getDouble("lat"),
						prev_locobj.getDouble("lon"), lat2, lon2, "K");
				if (!Double.isNaN(caldist))
					Total_distance = Total_distance + caldist;
			}

			double distance_calculated = distance(lat1, lon1, lat2, lon2, "K");
			 System.err.println("locationNearbyKM-----distance------"+i+"------"+distance_calculated+"--"+lat1+","+lon1+"---"+lat2+","+lon2);

			int SpeedcCheck = (int) location_list.get(i).get("speed");

			if (i == 0) {
				minDistance = distance_calculated;
				MaxSpeed = SpeedcCheck;
				MaxSpeedCopy=SpeedcCheck;

			} else if (distance_calculated < minDistance) {
				minDistance = distance_calculated;
				position = i;
			}


			if (i == 0) {
				/*if (SpeedcCheck > MaxSpeed
						&& (loactionTime > dayStartWorkTime + 41400 &&
								loactionTime < dayStartWorkTime + 55800))
					MaxSpeed = SpeedcCheck;
				else*/ if (SpeedcCheck > MaxSpeed
						&& (loactionTime > startTime &&
								loactionTime < endTime))
					MaxSpeed = SpeedcCheck;

			} else if (i == 1) {
			/*	if (SpeedcCheck > MaxSpeed
						&& (loactionTime > dayStartWorkTime + 41400 
								&& loactionTime < dayStartWorkTime + 55800)
						)
					MaxSpeed = SpeedcCheck;
				else*/ if (SpeedcCheck > MaxSpeed
						&& (loactionTime >startTime
								&& loactionTime < endTime)
						)
					MaxSpeed = SpeedcCheck;

			} else if (i > 10) {
				/*if (SpeedcCheck > MaxSpeed
						&& (loactionTime > dayStartWorkTime + 41400 
								&& loactionTime < dayStartWorkTime + 55800)
								
						&& (int) location_list.get(i - 1).get("speed") > 8
						&& (int) location_list.get(i - 2).get("speed") > 8
						&& (int) location_list.get(i - 3).get("speed") > 8
						&& (int) location_list.get(i - 4).get("speed") > 8
						&& (int) location_list.get(i - 5).get("speed") > 8
						&& (int) location_list.get(i - 6).get("speed") > 8
						&& (int) location_list.get(i - 7).get("speed") > 8
						&& (int) location_list.get(i - 8).get("speed") > 8
						&& (int) location_list.get(i - 9).get("speed") > 8
						&& (int) location_list.get(i - 10).get("speed") > 8	
						)
					MaxSpeed = SpeedcCheck;
				else*/
				
				if (SpeedcCheck > MaxSpeed
						&& (loactionTime > startTime 
								&& loactionTime < endTime)
						&& (int) location_list.get(i - 1).get("speed") > 8
						&& (int) location_list.get(i - 2).get("speed") > 8
						&& (int) location_list.get(i - 3).get("speed") > 8
						&& (int) location_list.get(i - 4).get("speed") > 8
						&& (int) location_list.get(i - 5).get("speed") > 8
						&& (int) location_list.get(i - 6).get("speed") > 8
						&& (int) location_list.get(i - 7).get("speed") > 8
						&& (int) location_list.get(i - 8).get("speed") > 8
						&& (int) location_list.get(i - 9).get("speed") > 8
						&& (int) location_list.get(i - 10).get("speed") > 8	)
					MaxSpeed = SpeedcCheck;
				
//				if (SpeedcCheck<8&&SpeedcCheck > MaxSpeedCopy
//						&& (loactionTime > dayStartWorkTime + 41400 && loactionTime < dayStartWorkTime + 55800))
//					MaxSpeedCopy = SpeedcCheck;
//				else 
				if (SpeedcCheck<8&&SpeedcCheck > MaxSpeedCopy
						&& (loactionTime > startTime && loactionTime < endTime))
					MaxSpeedCopy = SpeedcCheck;

				
			}

			TotalSpeed = TotalSpeed + SpeedcCheck;
			speedbuild.append(SpeedcCheck + "").append(",");

			// if (event.equalsIgnoreCase("end"))
			// System.err.println(i+"-----------speed--"+SpeedcCheck+"--max-"+MaxSpeed+"---Location time-----"+Common.getDateCurrentTimeZone(loactionTime));

		}
		
		if (MaxSpeed==0) 
			MaxSpeed=MaxSpeedCopy;
		
		 System.err.println("-----------MaxSpeed--"+MaxSpeed+"--MaxSpeedCopy-"+MaxSpeedCopy);

		/*
		 * event+"----locationNearbyKM-----time------"+Common.getDateCurrentTimeZone
		 * (dayStartWorkTime+23400)+
		 * ","+Common.getDateCurrentTimeZone(dayStartWorkTime+37800)
		 * +"---lot2---"+Common.getDateCurrentTimeZone(dayStartWorkTime+41400)
		 * +","+Common.getDateCurrentTimeZone(dayStartWorkTime+55800)+
		 */// System.err.println(event+"----SpeedcCheck----------"+speedbuild.toString());

		// System.err.println("DIstttttttttttttttttt---------"+Total_distance);
		System.out.println(event
				+ "--------"
				+ km
				+ "----Min at position-"
				+ position
				+ "--dist--"
				+ minDistance
				+ "===time=="
				+ Common.getDateCurrentTimeZone(((BasicDBObject) location_list
						.get(position)).getLong("timestamp")));

		int meterConversion = 1609;
		
       // Collections.sort(loactionListNearBy);
		PoleNearByLocationDtoStartEnd dtoStartEnd=new PoleNearByLocationDtoStartEnd();
		//For start
		
	 {		PoleNearByLocationDto dto = new PoleNearByLocationDto();

			//NearbyFeatureCodeWithDistanceDTO nearStartDto=loactionListNearBy.get(0);

			if (Total_distance > 1) {
				dto.setLang(nearStartDto.getFeatureDetailLan());
				dto.setLat(nearStartDto.getFeatureDetailLat());
				dto.setMinDistance(nearStartDto.getDistanceCalulated());
				dto.setSpeed(nearStartDto.getSpeed());
				dto.setAvgSpeed(TotalSpeed / location_list.size());
				dto.setMaxSpeed(MaxSpeed);
				dto.setTotalspeed(TotalSpeed);
				
				dto.setStartkmBeatExpected(km);
				dto.setStartkmBeatActual(nearStartDto.getKmDistance());
				dto.setTotal_distance(Total_distance);
				dto.setTimestamp(nearStartDto.getTime());

			} else {
				dto.setLang(nearStartDto.getFeatureDetailLan());
				dto.setLat(nearStartDto.getFeatureDetailLat());
				dto.setMinDistance(nearStartDto.getDistanceCalulated());
				dto.setSpeed(nearStartDto.getSpeed());
				dto.setAvgSpeed(TotalSpeed / location_list.size());
				dto.setMaxSpeed(MaxSpeed);
				dto.setTotalspeed(TotalSpeed);
				dto.setStartkmBeatExpected(km);
				dto.setStartkmBeatActual(0);
				dto.setTotal_distance(Total_distance);
				dto.setTimestamp((long) 0);

			}

			// System.out.println(event+"-@@@@@@@@--dto.getStartkmBeatActual()-----"+dto.getStartkmBeatActual()+"---setStartkmBeatExpected-"
			// + dto.getStartkmBeatExpected() );
			dtoStartEnd.setStartPole(dto);

		} 

		
		//For end
		{
			PoleNearByLocationDto dto = new PoleNearByLocationDto();

			//NearbyFeatureCodeWithDistanceDTO nearEndDto=loactionListNearBy.get(loactionListNearBy.size()-1);
			if (Total_distance > 1) {
				dto.setLang(nearEndDto.getFeatureDetailLan());
				dto.setLat(nearEndDto.getFeatureDetailLat());
				dto.setMinDistance(nearEndDto.getDistanceCalulated());
				dto.setSpeed(nearEndDto.getSpeed());
				dto.setAvgSpeed(TotalSpeed / location_list.size());
				dto.setMaxSpeed(MaxSpeed);
				dto.setTotalspeed(TotalSpeed);
				
				dto.setStartkmBeatExpected(km);
				dto.setStartkmBeatActual(nearEndDto.getKmDistance());
				dto.setTotal_distance(Total_distance);
				dto.setTimestamp(nearEndDto.getTime());

			} else {
				dto.setLang(nearEndDto.getFeatureDetailLan());
				dto.setLat(nearEndDto.getFeatureDetailLat());
				dto.setMinDistance(nearEndDto.getDistanceCalulated());
				dto.setSpeed(nearEndDto.getSpeed());
				dto.setAvgSpeed(TotalSpeed / location_list.size());
				dto.setMaxSpeed(MaxSpeed);
				dto.setTotalspeed(TotalSpeed);
				dto.setEndKmBeatExpected(km);
				dto.setEndKmBeatActual(0);
				dto.setTimestamp((long) 0);

			}

			// System.out.println(event+"--@@@@@@@@@-dto.getEndKmBeatActual()-----"+dto.getEndKmBeatActual()+"---getEndKmBeatExpected-"
			// + dto.getEndKmBeatExpected() );
			
			dtoStartEnd.setEndPole(dto);


		}
		System.err.println("loactionListNearBy size----"+loactionListNearBy.size());

		return dtoStartEnd;
	}

	private double distance(double lat1, double lon1, double lat2, double lon2,
			String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit.equals("K")) {
			dist = dist * 1.609344;
		} else if (unit.equals("N")) {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
	
	
	
	
	
	
	
	
	
	///Logic + - upper and lower round off
	public MessageObject GenerateExceptionReportPatrolManBeatPathAsKeymanUppperLowerRoundoff(

			Connection con, DB mongoconnection, String parentId, int day, Boolean isSendMail) {
		LoginDAO loginObj=new LoginDAO();

		MessageObject msgo = new MessageObject();
		ArrayList<RailMailSendInfoDto> mailSendInfo = loginObj.getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionReportPatrolManBeatPathAsKeyman ---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-day;
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

		// ///////////////////////////////********************************/////////////////////////////
		// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("PatrolMan Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"Patrolman Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						5));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);

		row.setHeight((short) 500);

		// ///////////////////////////////////All Section
		// End//////////////////////////////////////////////////////////

		//for (int j = 0; j < mailSendInfo.size(); j++) {
			for (int j = 0; j <=5;j++) {

			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report of keymen"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyle.setAlignment(HorizontalAlignment.CENTER);
			font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);

			XSSFCellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap
			wrap_style.setAlignment(HorizontalAlignment.CENTER);

			int petrolMancount = 0;
			

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for Patrolman of section "
							+ mailSendInfo.get(j).getDept() + "  Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyle);
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 6));

			BasicDBList sectionDeviceList = new BasicDBList();

			for (int i = 0; i < deviceInfoList.size(); i++) {
				// for (int i = 4; i <=4; i++) {

				System.err.println(" **Device =---================"
						+ deviceInfoList.get(i).getName() + "\n");

				Double startBatteryStatus = loginObj.getKeyManStartBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+18000);

				Double endBatteryStatus = loginObj.getKeyManEndBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+18000);

				if (deviceInfoList.get(i).getName().startsWith("P/")) {
					BasicDBObject subdocument = new BasicDBObject();

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfKeyman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));

								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

								dto.setSectionName(rsgertrip
										.getString("SectionName"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));
								RailwayKeymanTripsBeatsList.add(dto);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					// long DayStartWorkTime=1547836200;
					// System.out.println("DayStartWorkTime----" +
					// DayStartWorkTime);

					Double distanceTolerance = 1.3;
					// Double startLatOfRouteKm = 0.0, startLangOfRouteKm = 0.0,
					// endLatOfRouteKm = 0.0, endLangOfRouteKm = 0.0;

					// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
					// RailwayPetrolmanTripsBeatsList) {
					for (int r = 0; r < RailwayKeymanTripsBeatsList.size(); r++) {
						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayKeymanTripsBeatsList
								.get(r);

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.parseLong("355488020181042"));

						/*
						 * BasicDBObject timestamp_whereQuery = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime).append("$lt",
						 * DayStartWorkTime+86400));
						 */

						/*Summer Time
						 * BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 23400).append("$lt",
										DayStartWorkTime + 37800));
						BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 41400).append("$lt",
										DayStartWorkTime + 55800));
										
*/
						//Winter Time
					/*	BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 18000).append("$lt",
										DayStartWorkTime + 36000));
						BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 43200).append("$lt",
										DayStartWorkTime + 54000));
*/
						//Winter Time all 8 hr calculated for trip complete
						BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime - 7200).append("$lt",
										DayStartWorkTime + 32400));
						BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime - 7200).append("$lt",
												DayStartWorkTime + 32400));
						/*
						 * BasicDBObject timestamp_whereQuery_morning = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+23400).append("$lt",
						 * DayStartWorkTime+37800)); BasicDBObject
						 * timestamp_whereQuery_afternoon = new BasicDBObject(
						 * "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+41400).append("$lt",
						 * DayStartWorkTime+55800));
						 */
						BasicDBList morn_Milage = new BasicDBList();
						morn_Milage.add(timestamp_whereQuery_morning);

						morn_Milage.add(device_whereQuery);
						DBObject morn_Milage_query = new BasicDBObject("$and",
								morn_Milage);

						BasicDBList after_Milage = new BasicDBList();
						after_Milage.add(timestamp_whereQuery_afternoon);
						after_Milage.add(device_whereQuery);
						DBObject after_Milage_query = new BasicDBObject("$and",
								after_Milage);

						BasicDBList Milage = new BasicDBList();
						Milage.add(morn_Milage_query);
						Milage.add(after_Milage_query);
						DBObject final_query = new BasicDBObject("$or", Milage);

						DBCursor cursor = table.find(final_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + final_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							System.err.println("*--listObjects---COunt---"
									+ cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();

						}

						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());
							;

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = locationNearbyKM(
										"Start",
										railMailSendInfoDto.getKmStart(),railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"P");
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation = locationNearbyKM(
										"End",railMailSendInfoDto.getKmStart(), railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"P");
								if (tripEndLocation.getEndKmBeatActual() <= 0) {
									tripStartLocation.setStartkmBeatActual(0);

								}

								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								
								
								for (DBObject dbObject : listObjects) {
									int SpeedcCheck = (int) dbObject.get("speed");
									Long timeloc=Long.parseLong(dbObject.get("timestamp")+"");
								
							
									if(SpeedcCheck>8&&((timeloc>(DayStartWorkTime + 21600) && timeloc<(DayStartWorkTime+36000) ))){
										exDto.setMaxSpeed(SpeedcCheck);
										}
									
									}

			
								
								exceptiionalTrip.add(exDto);
							} else {

								exceptiionalTrip.add(null);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setLocationSize(0);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exDto.setMaxSpeed(0);
							exceptiionalTrip.add(exDto);
						}

					}
					System.out.println("\n\n");
					System.out
							.println("  Here--*************exceptiionalTrip****************************--------------------"
									+ exceptiionalTrip.size());
					System.out.println("\n\n");
					// //////////////////////////////////////////////

					// //////////////////////////////////
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null) {
						StringBuilder remark = new StringBuilder();
						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								deviceInfoList.get(i).getName() + "   ("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")");
						subdocument.put("device", deviceInfoList.get(i)
								.getDeviceID());
						subdocument.put("device_name", deviceInfoList.get(i)
								.getName());

						row.getCell(0).setCellStyle(HeadingStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));
						// start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((startBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("start_battery_status",
								df2.format(((startBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						// End Battery Staus
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("End Battery status :- "
								+ df2.format(((endBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("end_battery_status",
								df2.format(((endBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");

						remark_cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						cell = row.createCell(0);
						cell.setCellValue("Trip#");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(1);
						cell.setCellValue("Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue("Expected Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue("End Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(4);
						cell.setCellValue("Expected end Beat");
						cell.setCellStyle(tripColumnStyle);

						/*
						 * cell = row.createCell(5);
						 * cell.setCellValue("Avg speed");
						 * cell.setCellStyle(tripColumnStyle);
						 */

						cell = row.createCell(5);
						cell.setCellValue("Max speed");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(6);
						cell.setCellValue("Distance cover");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						for (ExceptionReortsTrip exceptionReortsTrip : exceptiionalTrip) {
							System.out
									.println(" **exceptiionalTrip=---================"
											+ new Gson()
													.toJson(exceptionReortsTrip));

							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null
									&& exceptionReortsTrip.getLocationSize() > 0) {

								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = 0;
									double expactedKmCover = 0;
									if (exceptionReortsTrip.getTripStart()
											.getStartkmBeatActual() != 0
											&& exceptionReortsTrip.getTripEnd()
													.getEndKmBeatActual() != 0
											&& exceptionReortsTrip
													.getTripStart()
													.getMinDistance() < 5
											&& exceptionReortsTrip.getTripEnd()
													.getMinDistance() < 5) {
										kmcover = (exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatActual() - exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatActual());
										if (kmcover < 0)
											kmcover = -1 * kmcover;
										expactedKmCover = exceptionReortsTrip
												.getTripStart()
												.getEndKmBeatExpected()
												- exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected();
										if (expactedKmCover < 0)
											expactedKmCover = -1
													* expactedKmCover;

										if (kmcover < expactedKmCover
												- distanceTolerance) {
											remark.append("\tTotal beats not completed in Trip "
													+ exceptionReortsTrip
															.getTripNo() + ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark_cell
													.setCellValue("Remark status :- All work done succesfully.");
											KeyManCoverSucefullyDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
										}

									} else {
										kmcover = 0;

										if (exceptionReortsTrip.getTripStart()
												.getTotal_distance() < 1) {
											remark.append("\tDevice is not moved "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else if (exceptionReortsTrip
												.getTripStart()
												.getMinDistance() > 5
												&& exceptionReortsTrip
														.getTripEnd()
														.getMinDistance() > 5) {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											KeyManExceptionalDevices_Set.add(deviceInfoList
													.get(i).getName());
										}

									}

			
									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());
										subdocument
												.put("Tripno",
														exceptionReortsTrip
																.getTripNo());

										cell.setCellStyle(wrap_style);

										cell = row.createCell(1);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));
										subdocument
												.put("start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatActual()));
										cell.setCellStyle(wrap_style);

										cell = row.createCell(2);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatExpected());
										subdocument
												.put("expected_start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatExpected()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(3);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));
										subdocument.put("end_beat", df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(4);
										cell.setCellValue(exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatExpected());
										subdocument
												.put("expected_end_beat",
														df2.format(exceptionReortsTrip
																.getTripEnd()
																.getEndKmBeatExpected()));

										cell.setCellStyle(wrap_style);

										/*
										 * cell = row.createCell(5);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 * cell.setCellStyle(wrap_style);
										 */

										cell = row.createCell(5);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart().getMaxSpeed());
										subdocument.put("max_speed", df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getMaxSpeed()));

										if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8&&exceptionReortsTrip.getMaxSpeed()>8) {
											KeyManOverspeedDevices_Set.add(deviceInfoList.get(i)
															.getName());
										}

										cell.setCellStyle(wrap_style);

										cell = row.createCell(6);
										cell.setCellValue(df2.format(kmcover));
										subdocument.put("kmcover",
												df2.format(kmcover));
										cell.setCellStyle(wrap_style);

										// System.out.println("  Here--*****************************************--------------------");
										//

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// Exception of location not found
								if (exceptionReortsTrip != null
										&& exceptionReortsTrip
												.getLocationSize() == 0)
									try {
										// System.out.println(" **exceptionReortsTrip*******row******************"
										// +
										// "***-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size()+"-----"+exceptionReortsTrip.getLocationSize());

										KeyManOffDevice_Set.add(deviceInfoList
												.get(i).getName());

										remark.append("Device is Off");

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(1);
										cell.setCellStyle(loactionNotFoundColumnStyle);

										cell = row.createCell(1);
										cell.setCellValue("Device is Off");
										cell.setCellStyle(loactionNotFoundColumnStyle);
										sheet.addMergedRegion(new CellRangeAddress(
												sheet.getLastRowNum(), sheet
														.getLastRowNum(), 1, 3));

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

							}
						}

						if (remark.toString().length() > 0) {
							remark_cell.setCellValue("Remark status :- "
									+ remark.toString());
							subdocument.put("remark", remark.toString());
							remark_cell.setCellStyle(error_remarkColumnStyle);
						}

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayKeymanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null) {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(
										deviceInfoList.get(i).getName()
												+ "   ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ")");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :- RDPS data not found.");
								subdocument.put("remark",
										"RDPS data not found.");
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								// cell.setCellStyle(error_remarkColumnStyle);

								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(

										deviceInfoList.get(i).getName()
												+ "  ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ") ");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :-Trips not Shedule for this device.");
								subdocument.put("remark",
										"Trips not Shedule for this device.");
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());
					if (startBatteryStatus < 3
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName()))
						KeyManLowBattery_Set.add(deviceInfoList.get(i)
								.getName());

					sectionDeviceList.add(subdocument);

				} else {
					// System.out.println("patrolman Man found--------------------");
					petrolMancount++;
				}

			}

			Maindocument.put("exceptional_device", sectionDeviceList);

			report_table.insert(Maindocument);

			sheet.setColumnWidth(0, 1300);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 2000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);

			if (deviceInfoList.size() > 0
					&& petrolMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept() + "_"
						+ "Exception_Trip_Report_Patrolman"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ "_" + parentId + ".xlsx";
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					String sendemail = "";
					for (String emaString : mailSendInfo.get(j).getEmailIds()) {
						sendemail = sendemail + "," + emaString.trim();
					}
					SendEmail mail = new SendEmail();

				
					if(isSendMail)
				{
						mail.sendDeviceExceptionMailToJaipur(sendemail, file,
								 mailSendInfo
								  .get(j).getDept(),"Exception Trip Report of Patrolman"
								  ,"Exception Trip Report of Patrolman",false
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());
				}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// /////ALl Section////////////////////////

			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReport);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			cell = row.createCell(1);
			cell.setCellValue(KeyManLowBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight
					.add((short) ((KeyManLowBattery_Set.toString()
							.replace("[", "").replace("]", "").toString()
							.length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(KeyManOffDevice_Set.toString().replace("[", "")
					.replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOffDevice_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			short height_row = (short) ((KeyManLowBattery_Set.toString()
					.replace("[", "").replace("]", "").toString().length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);
			System.err.println("Row max height-----" + height_row);

			sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

			if (j == mailSendInfo.size() - 1) {
				String outFileNameAllSectionStatus = "All_Section_Patrolman_Status_Report_"
						+ parentId
						+ "_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ ".xlsx";
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReport.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReport.close();
					
					
					String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," Patrolman Work status Report_AllSection",
								  " Patrolman Work status Report_AllSection",true
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());
					}
						
		
					
					 
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			

			}

			// /////////////////////*Alll Section////////////////////
			
			
			///Insert Datewise Saction Data
			 
			if(isSendMail)
				 try {
		
							// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
							java.sql.CallableStatement ps = con
									.prepareCall("{call SaveDatewiseExceptionReport(?,?,?,?,?,?,?,?)}");
		
							
							ps.setInt(1, Integer.parseInt(parentId));
							ps.setString(2,DayStartWorkTime+"");
							ps.setString(3, KeyManLowBattery_Set.toString().replace("[", "")
									.replace("]", ""));
		
							ps.setString(4,KeyManOffDevice_Set.toString()
									.replace("[", "").replace("]", ""));
		
							ps.setString(5, KeyManExceptionalDevices_Set.toString()
									.replace("[", "").replace("]", ""));
							ps.setString(6, KeyManOverspeedDevices_Set.toString()
									.replace("[", "").replace("]", ""));
							ps.setString(7,mailSendInfo.get(j).getDept() );
							ps.setString(8, KeyManCoverSucefullyDevices_Set.toString()
									.replace("[", "").replace("]", ""));
							int result = ps.executeUpdate();
							if (result == 0) {
								msgo.setError("true");
								msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
							} else {
								// //System.err.println("Error=="+result);
								msgo.setError("false");
								msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
							}
						} catch (Exception e) {
							e.printStackTrace();
							msgo.setError("true");
							msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
						}

		}// //////////for section end

		return msgo;

	}
	
	

	public synchronized PoleNearByLocationDto locationNearbyKM(String event,
			double Kmstart,double kmEnd, double lat1, double lon1,
			ArrayList<DBObject> location_list, long dayStartWorkTime,String devicetype) {
		
		long startTime,endTime;
		
		if(devicetype.equalsIgnoreCase("K")){
			startTime=dayStartWorkTime+23400;
			endTime=dayStartWorkTime+37800;
					
		}else{
			startTime=dayStartWorkTime;
			endTime=dayStartWorkTime+37800;
		}
		// System.err.println("locationNearbyKM------------"+"km---"+km+"------"+lat1+","+lon1+"---");
		
		int MaxSpeed=0 ,MaxSpeedCopy= 0;
		int TotalSpeed = 0;
		StringBuilder speedbuild = new StringBuilder();
		Double Total_distance = 0.0;
		double earthRadius = 3958.75;
		double minDistance = 0;
		int position = 0;
		for (int i = 0; i < location_list.size(); i++) {

			BasicDBObject locobj = (BasicDBObject) location_list.get(i).get(
					"location");
			double lat2 = locobj.getDouble("lat");
			double lon2 = locobj.getDouble("lon");

			if (i > 0) {
				BasicDBObject prev_locobj = (BasicDBObject) location_list.get(
						i - 1).get("location");
				double caldist = distance(prev_locobj.getDouble("lat"),
						prev_locobj.getDouble("lon"), lat2, lon2, "K");
				if (!Double.isNaN(caldist))
					Total_distance = Total_distance + caldist;
			}

			double distance_calculated = distance(lat1, lon1, lat2, lon2, "K");
			 //System.err.println("locationNearbyKM-----distance------"+i+"------"+distance_calculated+"--"+lat1+","+lon1+"---"+lat2+","+lon2);

			int SpeedcCheck = (int) location_list.get(i).get("speed");

			if (i == 0) {
				minDistance = distance_calculated;
				MaxSpeed = SpeedcCheck;
				MaxSpeedCopy=SpeedcCheck;

			} else if (distance_calculated < minDistance) {
				minDistance = distance_calculated;
				position = i;
			}

			Long loactionTime = Long.parseLong(location_list.get(i).get(
					"timestamp")
					+ "");

			if (i == 0) {
				/*if (SpeedcCheck > MaxSpeed
						&& (loactionTime > dayStartWorkTime + 41400 &&
								loactionTime < dayStartWorkTime + 55800))
					MaxSpeed = SpeedcCheck;
				else*/ if (SpeedcCheck > MaxSpeed
						&& (loactionTime > startTime &&
								loactionTime < endTime))
					MaxSpeed = SpeedcCheck;

			} else if (i == 1) {
			/*	if (SpeedcCheck > MaxSpeed
						&& (loactionTime > dayStartWorkTime + 41400 
								&& loactionTime < dayStartWorkTime + 55800)
						)
					MaxSpeed = SpeedcCheck;
				else*/ if (SpeedcCheck > MaxSpeed
						&& (loactionTime >startTime
								&& loactionTime < endTime)
						)
					MaxSpeed = SpeedcCheck;

			} else if (i > 10) {
				/*if (SpeedcCheck > MaxSpeed
						&& (loactionTime > dayStartWorkTime + 41400 
								&& loactionTime < dayStartWorkTime + 55800)
								
						&& (int) location_list.get(i - 1).get("speed") > 8
						&& (int) location_list.get(i - 2).get("speed") > 8
						&& (int) location_list.get(i - 3).get("speed") > 8
						&& (int) location_list.get(i - 4).get("speed") > 8
						&& (int) location_list.get(i - 5).get("speed") > 8
						&& (int) location_list.get(i - 6).get("speed") > 8
						&& (int) location_list.get(i - 7).get("speed") > 8
						&& (int) location_list.get(i - 8).get("speed") > 8
						&& (int) location_list.get(i - 9).get("speed") > 8
						&& (int) location_list.get(i - 10).get("speed") > 8	
						)
					MaxSpeed = SpeedcCheck;
				else*/
				
				if (SpeedcCheck > MaxSpeed
						&& (loactionTime > startTime 
								&& loactionTime < endTime)
						&& (int) location_list.get(i - 1).get("speed") > 8
						&& (int) location_list.get(i - 2).get("speed") > 8
						&& (int) location_list.get(i - 3).get("speed") > 8
						&& (int) location_list.get(i - 4).get("speed") > 8
						&& (int) location_list.get(i - 5).get("speed") > 8
						&& (int) location_list.get(i - 6).get("speed") > 8
						&& (int) location_list.get(i - 7).get("speed") > 8
						&& (int) location_list.get(i - 8).get("speed") > 8
						&& (int) location_list.get(i - 9).get("speed") > 8
						&& (int) location_list.get(i - 10).get("speed") > 8	)
					MaxSpeed = SpeedcCheck;
				
//				if (SpeedcCheck<8&&SpeedcCheck > MaxSpeedCopy
//						&& (loactionTime > dayStartWorkTime + 41400 && loactionTime < dayStartWorkTime + 55800))
//					MaxSpeedCopy = SpeedcCheck;
//				else 
				if (SpeedcCheck<8&&SpeedcCheck > MaxSpeedCopy
						&& (loactionTime > startTime && loactionTime < endTime))
					MaxSpeedCopy = SpeedcCheck;

				
			}

			TotalSpeed = TotalSpeed + SpeedcCheck;
			speedbuild.append(SpeedcCheck + "").append(",");

			// if (event.equalsIgnoreCase("end"))
			// System.err.println(i+"-----------speed--"+SpeedcCheck+"--max-"+MaxSpeed+"---Location time-----"+Common.getDateCurrentTimeZone(loactionTime));

		}
		
		if (MaxSpeed==0) 
			MaxSpeed=MaxSpeedCopy;
		
		 System.err.println("-----------MaxSpeed--"+MaxSpeed+"--MaxSpeedCopy-"+MaxSpeedCopy);

		/*
		 * event+"----locationNearbyKM-----time------"+Common.getDateCurrentTimeZone
		 * (dayStartWorkTime+23400)+
		 * ","+Common.getDateCurrentTimeZone(dayStartWorkTime+37800)
		 * +"---lot2---"+Common.getDateCurrentTimeZone(dayStartWorkTime+41400)
		 * +","+Common.getDateCurrentTimeZone(dayStartWorkTime+55800)+
		 */// System.err.println(event+"----SpeedcCheck----------"+speedbuild.toString());

		// System.err.println("DIstttttttttttttttttt---------"+Total_distance);
		System.out.println(event
				+ "--------"
				+ Kmstart
				+ "----Min at position-"
				+ position
				+ "--dist--"
				+ minDistance
				+ "===time=="
				+ Common.getDateCurrentTimeZone(((BasicDBObject) location_list
						.get(position)).getLong("timestamp")));

		int meterConversion = 1609;
		BasicDBObject locobj2 = (BasicDBObject) location_list.get(position)
				.get("location");
		PoleNearByLocationDto dto = new PoleNearByLocationDto();
		dto.setLang(locobj2.getDouble("lon"));
		dto.setLat(locobj2.getDouble("lat"));
		dto.setMinDistance((minDistance));
		dto.setSpeed((int) location_list.get(position).get("speed"));
		dto.setAvgSpeed(TotalSpeed / location_list.size());
		dto.setMaxSpeed(MaxSpeed);
		dto.setTotalspeed(TotalSpeed);
		
		
		if (event.equalsIgnoreCase("start")) {
			if (Total_distance > 1) {
				dto.setStartkmBeatExpected(Kmstart);
				if (Math.round(Kmstart) <= Kmstart) {
					dto.setStartkmBeatActual(Kmstart + minDistance);

				} else {
					dto.setStartkmBeatActual(Kmstart - minDistance);

				}

				dto.setTotal_distance(Total_distance);
				dto.setTimestamp(((BasicDBObject) location_list.get(position))
						.getLong("timestamp"));

			} else {
				dto.setStartkmBeatExpected(Kmstart);
				dto.setStartkmBeatActual(0);
				dto.setTotal_distance(Total_distance);
				dto.setTimestamp((long) 0);

			}

			// System.out.println(event+"-@@@@@@@@--dto.getStartkmBeatActual()-----"+dto.getStartkmBeatActual()+"---setStartkmBeatExpected-"
			// + dto.getStartkmBeatExpected() );

		} else {
			if (Total_distance > 1) {

				dto.setEndKmBeatExpected(kmEnd);
				if (Math.round(kmEnd) <= kmEnd)
					dto.setEndKmBeatActual(kmEnd + minDistance);
				else
					dto.setEndKmBeatActual(kmEnd - minDistance);

				dto.setTimestamp(((BasicDBObject) location_list.get(position))
						.getLong("timestamp"));

			} else {

				dto.setEndKmBeatExpected(kmEnd);
				dto.setEndKmBeatActual(0);
				dto.setTimestamp((long) 0);

			}

			// System.out.println(event+"--@@@@@@@@@-dto.getEndKmBeatActual()-----"+dto.getEndKmBeatActual()+"---getEndKmBeatExpected-"
			// + dto.getEndKmBeatExpected() );

		}

		return dto;
	}
	
	

	private DeviceBatteryStatusDTO getBatterystatus(DB mongoconnection, String deviceID, long dutyStartTime, long dutyEndTime) {

		DeviceBatteryStatusDTO batterystatus =new DeviceBatteryStatusDTO();
		try {
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_ALERT_MSG);

			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device", Long.parseLong(deviceID));

			BasicDBObject timestamp_whereQuery = new BasicDBObject("timestamp",
					new BasicDBObject("$gte", dutyStartTime-3600).append("$lt",
							dutyEndTime + 3600));
			BasicDBList And_Milage = new BasicDBList();
			And_Milage.add(timestamp_whereQuery);
			And_Milage.add(device_whereQuery);
			DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

			//for start battery status
			DBCursor Startcursor = table.find(Total_Milage_query);
			Startcursor.sort(new BasicDBObject("timestamp", 1)).limit(1);
			Startcursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			System.out.print("getKeyManStartBatteryStatus Count of u-p---"
					+ Startcursor.size() + "  " + Total_Milage_query);
			long total = Startcursor.count();

			if (Startcursor.size() != 0) {

				// System.err.println("*-getBatteryStatus----COunt---"
				// + cursor.size());

				while (Startcursor.hasNext()) {

					DBObject dbObject = (DBObject) Startcursor.next();
					batterystatus.setStartBatteryStatus(Double.parseDouble(dbObject.get("voltage_level") + ""));
					batterystatus.setStartBatteryStatusTime(Long.parseLong(dbObject.get("timestamp").toString()));

					System.err
					.println("*-getKeyManStartBatteryStatus--Startcursor--voltage_level---"
							+ Integer.parseInt(dbObject.get("voltage_level") + ""));

					System.err
							.println("*-getKeyManStartBatteryStatus--Startcursor--timestamp---"
									+ Common.getDateCurrentTimeZone(Long
											.parseLong(dbObject
													.get("timestamp")
													.toString())));

				}

			}
			
			
			//for end Battery status
			DBCursor Endcursor = table.find(Total_Milage_query);
			Endcursor.sort(new BasicDBObject("timestamp", -1)).limit(1);
			Endcursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			System.out.print("getKeyManStartBatteryStatus Count of u-p---"
					+ Endcursor.size() + "  " + Total_Milage_query);

			if (Endcursor.size() != 0) {

				// System.err.println("*-getBatteryStatus----COunt---"
				// + cursor.size());

				while (Endcursor.hasNext()) {

					DBObject dbObject = (DBObject) Endcursor.next();
					batterystatus.setEndBatteryStatus(Double.parseDouble(dbObject.get("voltage_level") + ""));
					batterystatus.setEndBatteryStatusTime(Long.parseLong(dbObject.get("timestamp").toString()));

					System.err
					.println("*-getKeyManStartBatteryStatus--Endcursor--voltage_level---"
							+ Integer.parseInt(dbObject.get("voltage_level") + ""));

					System.err
							.println("*-getKeyManStartBatteryStatus--Endcursor--timestamp---"
									+ Common.getDateCurrentTimeZone(Long
											.parseLong(dbObject
													.get("timestamp")
													.toString())));

				}

			}
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (batterystatus.getStartBatteryStatus()==null) {

			batterystatus.setStartBatteryStatus(0.0);
		}
		if (batterystatus.getEndBatteryStatus()==null) {

			batterystatus.setEndBatteryStatus(0.0);
		}
		
		// System.out.println("");
		System.out.println("batterystatus----------------------------------"
				+ batterystatus);

		return batterystatus;
	
	
	}





	public MessageObject GenerateExceptionReportKeyManBeatPathWholeDayByBeatLatLang(
			Connection con, DB mongoconnection, String parentId, int day2,
			Boolean isSendMail, long startTime1, long startTime2,
			long endTime1, long endTime2) {

		MessageObject msgo = new MessageObject();
		LoginDAO loginObj=new LoginDAO();
		ArrayList<RailMailSendInfoDto> mailSendInfo = loginObj.getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionReportKeyManBeatPathWholeDay ---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-day;
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

		// ///////////////////////////////********************************/////////////////////////////
		// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("KeyMan Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"KeyMan Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						7));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 800);
	/*	cell = row.createCell(6);
		cell.setCellValue("Section Status");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 500);*/

		// ///////////////////////////////////All Section
		// End//////////////////////////////////////////////////////////
		
	
		// //***************/////////////////////////////
				// All Section Count AlertForKeymanWorkStatusReport

				XSSFWorkbook workbookAlertForKeymanWorkStatusReportCount = new XSSFWorkbook();
				XSSFSheet sheetAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createSheet(WorkbookUtil
								.createSafeSheetName("KeyMan Status Report with count"));
				XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle();
				HeadingStyleAlertForKeymanWorkStatusReportCount
						.setFillForegroundColor(IndexedColors.AQUA.getIndex());
				HeadingStyleAlertForKeymanWorkStatusReportCount
						.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				 font = workbookAlertForKeymanWorkStatusReport.createFont();
				font.setFontHeightInPoints((short) 20);
				font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

				 tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle();
				tripColumnStyleAlertForKeymanWorkStatusReport
						.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
								.getIndex());
				tripColumnStyleAlertForKeymanWorkStatusReport
						.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
				font = workbookAlertForKeymanWorkStatusReport.createFont();
				font.setFontHeightInPoints((short) 15);
				font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
				XSSFCellStyle wrap_styleAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle(); // Create new style
				wrap_styleAlertForKeymanWorkStatusReportCount.setWrapText(true); // Set
																	

				row = sheetAlertForKeymanWorkStatusReportCount.createRow(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);
				row.createCell(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);
				row.createCell(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);

				row.createCell(0);
				row.getCell(0).setCellValue(
						"KeyMan Work Status Count Report Date :-"
								+ Common.getDateFromLong(DayStartWorkTime));
				sheetAlertForKeymanWorkStatusReportCount
						.addMergedRegion(new CellRangeAddress(
								sheetAlertForKeymanWorkStatusReportCount.getLastRowNum(),
								sheetAlertForKeymanWorkStatusReportCount.getLastRowNum(), 0,
								4));
				row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReportCount);

				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);

				 cell = row.createCell(0);

				cell.setCellValue("Section Name");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				/*cell = row.createCell(1);
				cell.setCellValue("Start with Low Battery Devices");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);*/
				cell = row.createCell(1);
				cell.setCellValue("Off Devices");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				cell = row.createCell(2);
				cell.setCellValue("Beat not cover");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				;
				cell = row.createCell(3);
				cell.setCellValue("Beat completed Sucessfully");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				cell = row.createCell(4);
				cell.setCellValue("Total Device");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				row.setHeight((short) 800);
			/*	cell = row.createCell(6);
				cell.setCellValue("Section Status");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				row.setHeight((short) 500);*/

				// ///////////////////////////////////All Section COunt// End//////////////////////////////////////////////////////////

				int divDeviceOffCount=0;
				int divDeviceBeatNotCoverCount=0;
				int divDeviceBeatCoverCount=0;

		for (int j = 0; j < mailSendInfo.size(); j++) {
//		for (int j = 29; j<=29 ;j++) {

			
			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report of keymen"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyle.setAlignment(HorizontalAlignment.CENTER);
			font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);

			CellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap

			int petrolMancount = 0;
			

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for Keyman of section "
							+ mailSendInfo.get(j).getDept() + "  Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyle);
			
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 5));

			BasicDBList sectionDeviceList = new BasicDBList();

			for (int i = 0; i < deviceInfoList.size(); i++) {
				// for (int i = 4; i <=4; i++) {

//				System.err.println(" **Device =---================"
//						+ deviceInfoList.get(i).getName() + "\n");

				Double startBatteryStatus = loginObj.getKeyManStartBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+startTime1);

				Double endBatteryStatus = loginObj.getKeyManEndBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+endTime2);

				if (deviceInfoList.get(i).getName().startsWith("K/")) {
					BasicDBObject subdocument = new BasicDBObject();

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfKeyman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));

								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

								dto.setSectionName(rsgertrip
										.getString("SectionName"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));
								RailwayKeymanTripsBeatsList.add(dto);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					// long DayStartWorkTime=1547836200;
					// System.out.println("DayStartWorkTime----" +
					// DayStartWorkTime);

					Double distanceTolerance = 1.3;
					// Double startLatOfRouteKm = 0.0, startLangOfRouteKm = 0.0,
					// endLatOfRouteKm = 0.0, endLangOfRouteKm = 0.0;

					// for (RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto :
					// RailwayPetrolmanTripsBeatsList) {
					for (int r = 0; r < RailwayKeymanTripsBeatsList.size(); r++) {
						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayKeymanTripsBeatsList
								.get(r);

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.parseLong("355488020181042"));

						/*
						 * BasicDBObject timestamp_whereQuery = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime).append("$lt",
						 * DayStartWorkTime+86400));
						 */

						/*Summer Time
						 * BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 23400).append("$lt",
										DayStartWorkTime + 37800));
						BasicDBOLoginDAO loginObj=new LoginDAO();
		ArrayList<RailMailSendInfoDto> mailSendInfo = loginObj.getSentMailInfoList(con,
				parentId);bject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 41400).append("$lt",
										DayStartWorkTime + 55800));
										
*/
						//Winter Time
					/*	BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 18000).append("$lt",
										DayStartWorkTime + 36000));
						BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + 43200).append("$lt",
										DayStartWorkTime + 54000));
*/
						//Winter Time all 8 hr calculated for trip complete
						BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + startTime1).append("$lt",
										DayStartWorkTime + endTime2));
//						BasicDBObject timestamp_whereQuery_afternoon = new BasicDBObject(
//								"timestamp", new BasicDBObject("$gte",
//										DayStartWorkTime + startTime2).append("$lt",
//										DayStartWorkTime + endTime2));

						/*
						 * BasicDBObject timestamp_whereQuery_morning = new
						 * BasicDBObject( "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+23400).append("$lt",
						 * DayStartWorkTime+37800)); BasicDBObject
						 * timestamp_whereQuery_afternoon = new BasicDBObject(
						 * "timestamp", new BasicDBObject("$gte",
						 * DayStartWorkTime+41400).append("$lt",
						 * DayStartWorkTime+55800));
						 */
						BasicDBList morn_Milage = new BasicDBList();
						morn_Milage.add(timestamp_whereQuery_morning);

						morn_Milage.add(device_whereQuery);
						DBObject morn_Milage_query = new BasicDBObject("$and",
								morn_Milage);

//						BasicDBList after_Milage = new BasicDBList();
////						after_Milage.add(timestamp_whereQuery_afternoon);
//						after_Milage.add(device_whereQuery);
//						DBObject after_Milage_query = new BasicDBObject("$and",
//								after_Milage);

//						BasicDBList Milage = new BasicDBList();
//						Milage.add(morn_Milage_query);
//						Milage.add(after_Milage_query);
//						DBObject final_query = new BasicDBObject("$or", Milage);

						DBCursor cursor = table.find(morn_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + morn_Milage_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							System.err.println("*--listObjects---COunt---"
									+ cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();

						}

						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());
							;

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = loginObj.locationNearbyKM(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"K");
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation =loginObj. locationNearbyKM(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"K");
								if (tripEndLocation.getEndKmBeatActual() <= 0) {
									tripStartLocation.setStartkmBeatActual(0);

								}

								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								
								
								for (DBObject dbObject : listObjects) {
									int SpeedcCheck = (int) dbObject.get("speed");
									Long timeloc=Long.parseLong(dbObject.get("timestamp")+"");
								
							
									if(SpeedcCheck>8&&((timeloc>(DayStartWorkTime + 21600) && timeloc<(DayStartWorkTime+36000) ))){
										exDto.setMaxSpeed(SpeedcCheck);
										}
									
									}

			
								
								exceptiionalTrip.add(exDto);
							} else {

								exceptiionalTrip.add(null);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setLocationSize(0);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exDto.setMaxSpeed(0);
							exceptiionalTrip.add(exDto);
						}
						
					}
//					System.out.println("\n\n");
//					System.out
//							.println("  Here--*************exceptiionalTrip****************************--------------------"
//									+ exceptiionalTrip.size());
//					System.out.println("\n\n");
					// //////////////////////////////////////////////

					// //////////////////////////////////DayStartWorkTime
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null) {
						StringBuilder remark = new StringBuilder();
						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								deviceInfoList.get(i).getName() + "   ("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")");
						subdocument.put("device", deviceInfoList.get(i)
								.getDeviceID());
						subdocument.put("device_name", deviceInfoList.get(i)
								.getName());

						row.getCell(0).setCellStyle(HeadingStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));
						// start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((startBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("start_battery_status",
								df2.format(((startBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						// End Battery Staus
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						if (endBatteryStatus>0) {
							cell.setCellValue("End Battery status :- "
									+ df2.format(((endBatteryStatus / 6) * 100))
									+ " %");
						}else{
							cell.setCellValue("End Battery status :- 0% / May be device off ");
						}
						
						subdocument.put("end_battery_status",
								df2.format(((endBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");

						remark_cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 6));

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						cell = row.createCell(0);
						cell.setCellValue("Trip#");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(1);
						cell.setCellValue("Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue("Expected Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue("End Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(4);
						cell.setCellValue("Expected end Beat");
						cell.setCellStyle(tripColumnStyle);

						/*
						 * cell = row.createCell(5);
						 * cell.setCellValue("Avg speed");
						 * cell.setCellStyle(tripColumnStyle);
						 */

						cell = row.createCell(5);
						cell.setCellValue("Max speed");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(6);
						cell.setCellValue("Distance cover");
						cell.setCellStyle(tripColumnStyle);
						row.setHeight((short) 600);

						for (ExceptionReortsTrip exceptionReortsTrip : exceptiionalTrip) {
////							System.out
//									.println(" **exceptiionalTrip=---================"
//											+ new Gson()
//													.toJson(exceptionReortsTrip));

							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null
									&& exceptionReortsTrip.getLocationSize() > 0) {

								try {
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									double kmcover = 0;
									double expactedKmCover = 0;
									if (exceptionReortsTrip.getTripStart()
											.getStartkmBeatActual() != 0
											&& exceptionReortsTrip.getTripEnd()
													.getEndKmBeatActual() != 0
											&& exceptionReortsTrip
													.getTripStart()
													.getMinDistance() < 5
											&& exceptionReortsTrip.getTripEnd()
													.getMinDistance() < 5) {
										kmcover = (exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatActual() - exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatActual());
										if (kmcover < 0)
											kmcover = -1 * kmcover;
										expactedKmCover = exceptionReortsTrip
												.getTripStart()
												.getEndKmBeatExpected()
												- exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected();
										if (expactedKmCover < 0)
											expactedKmCover = -1
													* expactedKmCover;

										if (kmcover < expactedKmCover
												- distanceTolerance) {
											remark.append("\tTotal beats not completed in Trip "
													+ exceptionReortsTrip
															.getTripNo() + ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark_cell
													.setCellValue("Remark status :- All work done succesfully.");
											KeyManCoverSucefullyDevices_Set
													.add(deviceInfoList.get(i)
															.getName());
										}

									} else {
										kmcover = 0;

										if (exceptionReortsTrip.getTripStart()
												.getTotal_distance() < 1) {
											remark.append("\tDevice is not moved "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else if (exceptionReortsTrip
												.getTripStart()
												.getMinDistance() > 5
												&& exceptionReortsTrip
														.getTripEnd()
														.getMinDistance() > 5) {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											KeyManExceptionalDevices_Set
													.add(deviceInfoList.get(i)
															.getName());

										} else {
											remark.append("\tDevice moved on other than expected beat. "
													+ ".");
											KeyManExceptionalDevices_Set.add(deviceInfoList
													.get(i).getName());
										}

									}

			
									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());
										subdocument
												.put("Tripno",
														exceptionReortsTrip
																.getTripNo());

										cell.setCellStyle(wrap_style);

										cell = row.createCell(1);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatActual()));
										subdocument
												.put("start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatActual()));
										cell.setCellStyle(wrap_style);

										cell = row.createCell(2);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart()
												.getStartkmBeatExpected());
										subdocument
												.put("expected_start_beat",
														df2.format(exceptionReortsTrip
																.getTripStart()
																.getStartkmBeatExpected()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(3);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));
										subdocument.put("end_beat", df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatActual()));

										cell.setCellStyle(wrap_style);

										cell = row.createCell(4);
										cell.setCellValue(exceptionReortsTrip
												.getTripEnd()
												.getEndKmBeatExpected());
										subdocument
												.put("expected_end_beat",
														df2.format(exceptionReortsTrip
																.getTripEnd()
																.getEndKmBeatExpected()));

										cell.setCellStyle(wrap_style);

										/*
										 * cell = row.createCell(5);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 * cell.setCellStyle(wrap_style);
										 */

										cell = row.createCell(5);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart().getMaxSpeed());
										subdocument.put("max_speed", df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getMaxSpeed()));

										if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8&&exceptionReortsTrip.getMaxSpeed()>8) {
											KeyManOverspeedDevices_Set.add(deviceInfoList.get(i)
															.getName());
										}

										cell.setCellStyle(wrap_style);

										cell = row.createCell(6);
										cell.setCellValue(df2.format(kmcover));
										subdocument.put("kmcover",
												df2.format(kmcover));
										cell.setCellStyle(wrap_style);

										// System.out.println("  Here--*****************************************--------------------");
										//

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								// Exception of location not found
								if (exceptionReortsTrip != null
										&& exceptionReortsTrip
												.getLocationSize() == 0)
									try {
										// System.out.println(" **exceptionReortsTrip*******row******************"
										// +
										// "***-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size()+"-----"+exceptionReortsTrip.getLocationSize());

										if (!deviceInfoList
												.get(i).getName().contains("/LR/")) {
											KeyManOffDevice_Set.add(deviceInfoList
													.get(i).getName());

										}
									
										remark.append("Device is Off");

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(1);
										cell.setCellStyle(loactionNotFoundColumnStyle);

										cell = row.createCell(1);
										cell.setCellValue("Device is Off");
										cell.setCellStyle(loactionNotFoundColumnStyle);
										sheet.addMergedRegion(new CellRangeAddress(
												sheet.getLastRowNum(), sheet
														.getLastRowNum(), 1, 3));

									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

							}
						}

						if (remark.toString().length() > 0) {
							remark_cell.setCellValue("Remark status :- "
									+ remark.toString());
							subdocument.put("remark", remark.toString());
							remark_cell.setCellStyle(error_remarkColumnStyle);
						}

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayKeymanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null) {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(
										deviceInfoList.get(i).getName()
												+ "   ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ")");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :- RDPS data not found.");
								subdocument.put("remark",
										"RDPS data not found.");
								KeyManExceptionalDevices_Set.add(deviceInfoList
										.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								// cell.setCellStyle(error_remarkColumnStyle);

								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0).setCellValue(

										deviceInfoList.get(i).getName()
												+ "  ("
												+ deviceInfoList.get(i)
														.getDeviceID() + ") ");
								row.getCell(0).setCellStyle(HeadingStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// start Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Start Battery status :- "
										+ df2.format(((startBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("start_battery_status",
												df2.format(((startBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								// End Battery Staus
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("End Battery status :- "
										+ df2.format(((endBatteryStatus / 6) * 100))
										+ " %");
								subdocument
										.put("end_battery_status",
												df2.format(((endBatteryStatus / 6) * 100))
														+ " %");

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :-Trips not Shedule for this device.");
								subdocument.put("remark",
										"Trips not Shedule for this device.");


								if (!deviceInfoList
										.get(i).getName().contains("/LR/"))
							KeyManExceptionalDevices_Set.add(deviceInfoList
									.get(i).getName());

								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(
										sheet.getLastRowNum(), sheet
												.getLastRowNum(), 0, 6));

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());
					if (startBatteryStatus < 3
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName())&&!deviceInfoList
											.get(i).getName().contains("/LR/"))
						KeyManLowBattery_Set.add(deviceInfoList.get(i)
								.getName());

					sectionDeviceList.add(subdocument);
					
					
					if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName())&&
							KeyManExceptionalDevices_Set.contains(deviceInfoList.get(i).getName())) 
						KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());

				} else {
					// System.out.println("patrolman Man found--------------------");
					petrolMancount++;
				}

			}

			Maindocument.put("exceptional_device", sectionDeviceList);

			report_table.insert(Maindocument);

			sheet.setColumnWidth(0, 1300);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 2000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);

			if (deviceInfoList.size() > 0
					&& petrolMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept() + "_"
						+ "Exception_Trip_Report_KeyMan"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ "_" + parentId + ".xlsx";
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					String sendemail = "";
					for (String emaString : mailSendInfo.get(j).getEmailIds()) {
						sendemail = sendemail + "," + emaString.trim();
					}
					SendEmail mail = new SendEmail();

					
					if(isSendMail)
					 {
						 mail.sendDeviceExceptionMailToJaipur(sendemail, file,
								 mailSendInfo
								  .get(j).getDept(),"Exception Trip Report of keymen"
								  ,"Exception Trip Report of keymen",false
									,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					 }
			
					 
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// /////ALl Section////////////////////////
			
				
			System.err.println("All section--countee--"+KeyManLowBattery_Set.size()+KeyManCoverSucefullyDevices_Set.size()+
					KeyManExceptionalDevices_Set.size()+KeyManOffDevice_Set.size());
			
			
			if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
					KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1)
			
			{
		
			
			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReport);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			cell = row.createCell(1);
			cell.setCellValue(KeyManLowBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight
					.add((short) ((KeyManLowBattery_Set.toString()
							.replace("[", "").replace("]", "").toString()
							.length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(KeyManOffDevice_Set.toString().replace("[", "")
					.replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOffDevice_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			
			
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport);// Apply // style// to// cell
			rowHeight.add((short) ((KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));
			
			/*cell = row.createCell(6);
			if(KeyManExceptionalDevices_Set.size()>0)
				cell.setCellValue("Not completed successfully ");

			else
				cell.setCellValue("Completed successfully");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); */

			short height_row = (short) ((KeyManLowBattery_Set.toString()
					.replace("[", "").replace("]", "").toString().length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);

			sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

			if (j == mailSendInfo.size() - 1) {
				String outFileNameAllSectionStatus = "All_Section_KeyMan_Status_Report_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_"+parentId+ ".xlsx";
				
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReport.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReport.close();

					String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," Keyman Work Status Report AllSection",
								  " Keyman Work Status Report AllSection",true	
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					}
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

			// /////////////////////*Alll Section////////////////////
			
			
			
			
			// /////ALl Section COunt////////////////////////
			 divDeviceOffCount=divDeviceOffCount+KeyManOffDevice_Set.size();
			 divDeviceBeatNotCoverCount=divDeviceBeatNotCoverCount+KeyManExceptionalDevices_Set.size();
			 divDeviceBeatCoverCount=divDeviceBeatCoverCount+KeyManCoverSucefullyDevices_Set.size();

			
	
				 if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
							KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1)
				 {	

			row = sheetAlertForKeymanWorkStatusReportCount
					.createRow(sheetAlertForKeymanWorkStatusReportCount
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	

			cell = row.createCell(1);
			cell.setCellValue(KeyManOffDevice_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	
			cell = row.createCell(2);
			cell.setCellValue(KeyManExceptionalDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			

			cell = row.createCell(3);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

			cell = row.createCell(4);
			cell.setCellValue((KeyManCoverSucefullyDevices_Set.size()+KeyManOffDevice_Set.size()+
					KeyManExceptionalDevices_Set.size())+"");
			
			
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			
	
			
			row.setHeight((short) 600);

			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(0, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(4, 3000);

			if (j == mailSendInfo.size() - 1) {
				
				//for total row
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount
								.getLastRowNum() + 1);
				row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				cell = row.createCell(0);
				cell.setCellValue("Total Device");

				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

				cell = row.createCell(1);
				cell.setCellValue(divDeviceOffCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				;

				cell = row.createCell(2);
				cell.setCellValue(divDeviceBeatNotCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				
				cell = row.createCell(3);
				cell.setCellValue(divDeviceBeatCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				

				cell = row.createCell(4);
				cell.setCellValue((divDeviceOffCount+divDeviceBeatCoverCount+
						divDeviceBeatNotCoverCount)+"");
				
				
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);


				
				row.setHeight((short) 600);

				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(0, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(1, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(2, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(3, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(4, 3000);
				
				String outFileNameAllSectionStatus = "All_Section_KeyMan_Status_Device_Count_Report_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_"+parentId+ ".xlsx";
				
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReportCount.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReportCount.close();

					String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					/*if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," Keyman Work Status device count Report AllSection",
								  " Keyman Work Status device count Report AllSection",true	
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					}*/
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

			// /////////////////////*Alll Section COunt end ////////////////////
			
			
			///Insert Datewise Saction Data
			 
			if(isSendMail)
			 try {

					// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
					java.sql.CallableStatement ps = con
							.prepareCall("{call SaveDatewiseExceptionReport(?,?,?,?,?,?,?,?)}");

					
					ps.setInt(1, Integer.parseInt(parentId));
					ps.setString(2,DayStartWorkTime+"");
					ps.setString(3, KeyManLowBattery_Set.toString().replace("[", "")
							.replace("]", ""));

					ps.setString(4,KeyManOffDevice_Set.toString()
							.replace("[", "").replace("]", ""));

					ps.setString(5, KeyManExceptionalDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(6, KeyManOverspeedDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(7,mailSendInfo.get(j).getDept() );
					ps.setString(8, KeyManCoverSucefullyDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					int result = ps.executeUpdate();
					if (result == 0) {
						msgo.setError("true");
						msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
					} else {
						// //System.err.println("Error=="+result);
						msgo.setError("false");
						msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
				}

		}// //////////for section end

		return msgo;

	}





	public MessageObject GenerateExceptionReportUSFD(Connection con, DB mongoconnection, String parentId, int day2,
			Boolean isSendMail, int roleId, String devicetype) {
		MessageObject msgo=new MessageObject();

		ArrayList<RailMailSendInfoDto> mailSendInfo=new ArrayList<>();
		if (roleId==7) {
			mailSendInfo.addAll(getDevicesForUSFD(con,parentId));
		}else {
			LoginDAO loginObj=new LoginDAO();
			mailSendInfo.addAll(loginObj.getSentMailInfoList(con,
					parentId));
		}
		
		System.err.println("GenerateExceptionRepor---"+devicetype
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-day2;
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);
		
		for (int j = 0; j < mailSendInfo.size(); j++) {
			//for (int j = 0; j <=2;j++) {
			 int totalTrip=0;
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());


			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report of "+devicetype));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyle.setAlignment(HorizontalAlignment.CENTER);
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 6);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);

			CellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap

			int petrolMancount = 0;
			

			XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for "+devicetype+" at " + "  Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyle);
			
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 7));

			BasicDBList sectionDeviceList = new BasicDBList();
			
			row = sheet.createRow(0);
			row = sheet.createRow(sheet.getLastRowNum() + 1);
			row.createCell(0);

			for (int i = 0; i < deviceInfoList.size(); i++) {
				// for (int i = 4; i <=4; i++) {
		
								//put logic for usfd trip
				if (deviceInfoList.get(i).getName().startsWith(devicetype)) {
					
					
					row = sheet.createRow(sheet.getLastRowNum() + 1);
					row = sheet.createRow(sheet.getLastRowNum() + 1);

					row.createCell(0);
					row.getCell(0).setCellValue(
							deviceInfoList.get(i).getName() + "   ("
									+ deviceInfoList.get(i).getDeviceID()
									+ ")");
			
					row.getCell(0).setCellStyle(HeadingStyle);
					sheet.addMergedRegion(new CellRangeAddress(sheet
							.getLastRowNum(), sheet.getLastRowNum(), 0, 7));
	
					row = sheet.createRow(sheet.getLastRowNum() + 1);

					XSSFCell cell = row.createCell(0);
					cell.setCellValue("Trip");
					cell.setCellStyle(tripColumnStyle);
				/*	
					 cell = row.createCell(1);
					cell.setCellValue("Name");
					cell.setCellStyle(tripColumnStyle);*/

					cell = row.createCell(1);
					cell.setCellValue("Start KM");
					cell.setCellStyle(tripColumnStyle);

					cell = row.createCell(2);
					cell.setCellValue("Start Time");
					cell.setCellStyle(tripColumnStyle);
					
					cell = row.createCell(3);
					cell.setCellValue("End KM");
					cell.setCellStyle(tripColumnStyle);

					cell = row.createCell(4);
					cell.setCellValue("End Time");
					cell.setCellStyle(tripColumnStyle);
					
					cell = row.createCell(5);
					cell.setCellValue("Max Speed");
					cell.setCellStyle(tripColumnStyle);
					
					cell = row.createCell(6);
					cell.setCellValue("Avg Speed");
					cell.setCellStyle(tripColumnStyle);
					row.setHeight((short) 600);
					
					cell = row.createCell(7);
					cell.setCellValue("Distance Cover");
					cell.setCellStyle(tripColumnStyle);
					row.setHeight((short) 600);
					
					System.err.println(" **Device =---================"
							+ deviceInfoList.get(i).getName() + "\n");
					
					BasicDBObject subdocument = new BasicDBObject();

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					
					Double distanceTolerance = 1.3;
			
						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(deviceInfoList.get(i).getDeviceID()));
					
						//Winter Time all 8 hr calculated for trip complete
						BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime).append("$lt",DayStartWorkTime+86400));
					
					
						BasicDBList after_Milage = new BasicDBList();
						after_Milage.add(timestamp_whereQuery_morning);
						after_Milage.add(device_whereQuery);
						DBObject after_Milage_query = new BasicDBObject("$and",
								after_Milage);
						DBCursor cursor = table.find(after_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + after_Milage_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							System.err.println("*--listObjects---COunt---"
									+ cursor.size());

							listObjects = (ArrayList<DBObject>) cursor.toArray();

						}
									
						ArrayList<TripInfoDto> Triplist = new ArrayList<TripInfoDto>();
						DBCollection tableTrip = mongoconnection
								.getCollection(Common.TABLE_TRIPREPORT);
						// System.out.println("device==============----------"+device+" "+table.getFullName());

						BasicDBObject device_whereQueryTrip = new BasicDBObject();
						device_whereQuery.put("device", deviceInfoList.get(i).getDeviceID()+"");

						BasicDBObject timestamp_whereQuery = new BasicDBObject(
								"source_info.timestamp",  new BasicDBObject("$gte",
										DayStartWorkTime).append("$lt",DayStartWorkTime+86400));

						BasicDBList And_Milage = new BasicDBList();
						And_Milage.add(timestamp_whereQuery);
						And_Milage.add(device_whereQuery);
						DBObject Total_Milage_query = new BasicDBObject("$and", And_Milage);

						DBCursor cursorTrip = tableTrip.find(Total_Milage_query).sort(
								new BasicDBObject("source_info.timestamp", 1));
						;
						cursorTrip.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("*-----cursorTrip-Report--"+cursorTrip.size());

						System.err.println("*-----cursorTrip-Report --"+Total_Milage_query);
						int Trip=0;

						if (cursorTrip.size() != 0) {

							while (cursorTrip.hasNext()) {
								Trip++;
								totalTrip++;
								TripInfoDto trip = new TripInfoDto();
								DBObject dbObject = (DBObject) cursorTrip.next();

								BasicDBObject Sourceobj = (BasicDBObject) dbObject
										.get("source_info");
								BasicDBObject Destobj = (BasicDBObject) dbObject
										.get("dest_info");

								/*trip.setDevice(deviceInfoList.get(i).getDeviceID());
								trip.setSrclat(Sourceobj.getString("lat"));
								trip.setSrclon(Sourceobj.getString("lon"));
								trip.setSrcspeed(Sourceobj.getString("speed"));
								trip.setSrc_adress(Sourceobj.getString("src_address"));

								trip.setSrctimestamp(Sourceobj.getString("timestamp"));


								trip.setDestlat(Destobj.getString("lat"));
								trip.setDestlon(Destobj.getString("lon"));
								trip.setDestspeed(Destobj.getString("speed"));
								trip.setDest_address(Destobj.getString("dest_address"));

								trip.setDesttimestamp(Destobj.getString("timestamp"));

								trip.setTotalkm(dbObject.get("totalkm").toString());
								trip.setMaxspeed(dbObject.get("maxspeed").toString());
								trip.setAvgspeed(String.format("%.2f",
										dbObject.get("avgspeed")));

								trip.setDevicename(dbObject.get("name").toString());
								// trip.setDevicename("vts");

								Triplist.add(trip);*/
								
								System.err.println("Sourceobj==== "+Sourceobj);
								System.err.println("Destobj==== "+Destobj);

							row = sheet.createRow(sheet
										.getLastRowNum() + 1);
							
								 cell = row.createCell(0);
								cell.setCellValue(Trip);
								cell.setCellStyle(wrap_style);
								
								/*cell = row.createCell(1);
								cell.setCellValue(dbObject.get("name").toString());
								cell.setCellStyle(wrap_style);*/

							/*	ArrayList<FeatureAddressDetailsDTO> featureListRdps = getNearbyKm(Sourceobj.getString("lat"),
										Sourceobj.getString("lon"),
										Destobj.getString("lat"),
										Destobj.getString("lon"),con,"534858");*/
								ArrayList<FeatureAddressDetailsDTO> featureListRdps = getNearbyKm(Sourceobj.getString("lat"),
										Sourceobj.getString("lon"),
										Destobj.getString("lat"),
										Destobj.getString("lon"),con,parentId);
								System.err.println("featureListRdps==== "+featureListRdps.size());

								
								cell = row.createCell(1);
								
								if (featureListRdps.size()==2&&Double.parseDouble(featureListRdps.get(0).getNearByDistance())<=500)
									cell.setCellValue(featureListRdps.get(0).getKiloMeter()+" ("+featureListRdps.get(0).getSection()+")");
									else
										cell.setCellValue(Sourceobj.getString("src_address"));
									cell.setCellStyle(wrap_style);
								
									//start time	
								cell = row.createCell(2);
									cell.setCellValue(Common.getDateCurrentTimeZone(Long.parseLong(Sourceobj.get("timestamp")+"")));

									cell.setCellStyle(wrap_style);
								
									cell = row.createCell(3);

								if (featureListRdps.size()==2&&Double.parseDouble(featureListRdps.get(1).getNearByDistance())<=500)
									cell.setCellValue(featureListRdps.get(1).getKiloMeter()+" ("+featureListRdps.get(1).getSection()+")");
									else
										cell.setCellValue(Destobj.getString("dest_address"));
								
								cell.setCellStyle(wrap_style);
								
								//end time
								cell = row.createCell(4);
								cell.setCellValue(Common.getDateCurrentTimeZone(Long.parseLong(Destobj.get("timestamp")+"")));

								cell.setCellStyle(wrap_style);
								
								cell = row.createCell(5);
								cell.setCellValue(dbObject.get("maxspeed")+"");
					
								cell.setCellStyle(wrap_style);
								
								cell = row.createCell(6);
								cell.setCellValue(String.format("%.2f",
										dbObject.get("avgspeed"))+"");

								cell.setCellStyle(wrap_style);

								cell = row.createCell(7);
								cell.setCellValue(String.format("%.2f",
										dbObject.get("totalkm"))+" Km");

								cell.setCellStyle(wrap_style);
							}
							//set excel 
						}else{
							row = sheet.createRow(sheet.getLastRowNum() + 1);

							row.createCell(0);
							row.getCell(0).setCellValue("Trip not found for this device.");
					
							sheet.addMergedRegion(new CellRangeAddress(sheet
									.getLastRowNum(), sheet.getLastRowNum(), 0, 5));

						}
				}
			}
					sheet.setColumnWidth(0, 1300);
					sheet.setColumnWidth(1, 4000);
					sheet.setColumnWidth(2, 4000);
					sheet.setColumnWidth(3, 2000);
					sheet.setColumnWidth(4, 3000);
					
					if (deviceInfoList.size() > 0
							&& petrolMancount != deviceInfoList.size()&&totalTrip!=0) {
						String outFileName = mailSendInfo.get(j).getDept() + "_"
								+ "Exception_Trip_Report_"+devicetype+"_"
								+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
								+ "_" + parentId + ".xlsx";
						
						try {
							String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
									+ outFileName.replace("/","_");
							FileOutputStream fos = new FileOutputStream(new File(file));
							workbook.write(fos);
							fos.flush();
							fos.close();
							workbook.close();
							// String sendemail="rupesh.p@mykiddytracker.com,";
		
							String sendemail = "";
							for (String emaString : mailSendInfo.get(j).getEmailIds()) {
								sendemail = sendemail + "," + emaString.trim();
							}
							SendEmail mail = new SendEmail();
		
							
							if(isSendMail)
							 {
								 mail.sendDeviceExceptionMailToJaipur(sendemail, file,
										 mailSendInfo
										  .get(j).getDept(),"Exception Trip Report of "+devicetype
										  ,"Exception Trip Report of "+devicetype,false, sendemail, sendemail);
							 }
					
							 
						} catch (Exception e) {
							e.printStackTrace();
						}finally {
						
		
						}
			}
		}
				
		return msgo;

	}
	
	private ArrayList<RailMailSendInfoDto> getDevicesForUSFD(Connection con, String parentId) {
		ArrayList<RailMailSendInfoDto> info=new ArrayList<>();

		try{

			java.sql.CallableStatement ps = con.prepareCall("{call getDevicesForUSFD(?)}");
			ps.setInt(1, Integer.parseInt(parentId));
			
			// //System.err.println("yy ------"+new Gson().toJson(ps));;

			ResultSet rs = ps.executeQuery();
			ArrayList<RailDeviceInfoDto> deviceList=new ArrayList<>();
			RailMailSendInfoDto obj = new RailMailSendInfoDto();
			ArrayList<String> emailIDs=new ArrayList<>();
			String email;
			
			if (rs != null) {
				while (rs.next()) {

					obj.setDept("0");
					obj.setDeptId(0);
					RailDeviceInfoDto dto_info = new RailDeviceInfoDto();
					dto_info.setStudentId(rs
							.getInt("StudentId"));
					dto_info.setName(rs
							.getString("Name"));
					dto_info.setDeviceID(rs
							.getString("DeviceId"));
					deviceList.add(dto_info);
					email=rs.getString("Email");

				}
				obj.setEmailIds(emailIDs);
				obj.setDeviceIds(deviceList);

			}
			
			info.add(obj);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return info;
	}

	private ArrayList<FeatureAddressDetailsDTO> getNearbyKm(String lat1, String lan1, String lat2, String lan2, Connection con, String parentId) {
		
		ArrayList<FeatureAddressDetailsDTO> FeatureSignlsList = new ArrayList<>();
		
			try {
				java.sql.CallableStatement psStart = con
						.prepareCall("{call GetFeatureCodeswithDist(?,?,?)}");
				psStart.setString(1, lat1);
				psStart.setString(2,lan1);
				psStart.setString(3, parentId);

				System.err.println("start -getNearbyKm-----'"+lat1+"','"+lan1+"','"+parentId+"'");;
				System.err.println("start -getNearbyKm-----'"+psStart);;

				ResultSet rsStart = psStart.executeQuery();

					while (rsStart.next()) {

						FeatureAddressDetailsDTO addressDto = new FeatureAddressDetailsDTO();
						addressDto.setDistance(rsStart
								.getString("Distance") + "");
						addressDto.setFeatureCode(rsStart
								.getString("FeatureCode") + "");
						addressDto.setFeature_image(rsStart
								.getString("Images") + "");
						addressDto.setFeatureDetail(rsStart
								.getString("FeatureDetail") + "");
						addressDto.setKiloMeter(rsStart
								.getString("kiloMeter") + "");
						addressDto.setLatitude(rsStart
								.getString("Latitude") + "");
						addressDto.setLongitude(rsStart
								.getString("Longitude") + "");
						addressDto.setSection(rsStart
								.getString("Section") + "");
						addressDto.setBlockSection(rsStart
								.getString("BlockSection") + "");
						addressDto.setNearByDistance(rsStart
								.getString("NearByDistance") + "");
						FeatureSignlsList.add(addressDto);

					}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			
			try {
				java.sql.CallableStatement psEnd = con
						.prepareCall("{call GetFeatureCodeswithDist(?,?,?)}");
				psEnd.setString(1, lat2);
				psEnd.setString(2,lan2);
				psEnd.setString(3, parentId);

				System.err.println("End -getNearbyKm-----'"+lat2+"','"+lan2+"','"+parentId+"'");;

				ResultSet rsEnd = psEnd.executeQuery();

				while (rsEnd.next()) {
						System.err.println("End -getNearbyKm--Rsultset---'"+lat2+"','"+lan2+"','"+parentId+"'");;

						FeatureAddressDetailsDTO addressDto = new FeatureAddressDetailsDTO();
						addressDto.setDistance(rsEnd
								.getString("Distance") + "");
						addressDto.setFeatureCode(rsEnd
								.getString("FeatureCode") + "");
						addressDto.setFeature_image(rsEnd
								.getString("Images") + "");
						addressDto.setFeatureDetail(rsEnd
								.getString("FeatureDetail") + "");
						addressDto.setKiloMeter(rsEnd
								.getString("kiloMeter") + "");
						addressDto.setLatitude(rsEnd
								.getString("Latitude") + "");
						addressDto.setLongitude(rsEnd
								.getString("Longitude") + "");
						addressDto.setSection(rsEnd
								.getString("Section") + "");
						addressDto.setBlockSection(rsEnd
								.getString("BlockSection") + "");
						addressDto.setNearByDistance(rsEnd
								.getString("NearByDistance") + "");
						FeatureSignlsList.add(addressDto);

					}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			
	/*	try{
		
		java.sql.CallableStatement ps = con.prepareCall("{call GetFeaturecodeSignals(?,?,?,?,?)}");
		ps.setString(1, lat1);
		ps.setString(2, lan1);
		ps.setString(3,lat2);
		ps.setString(4,lan2);
		ps.setInt(5, 150);
		System.err.println("getNearbyKm-----  '"+lat1+"','"+lan1+"','"+lat2+"','"+lan2+"'");;

		ResultSet rs = ps.executeQuery();

		if (rs != null) {
			while (rs.next()) {

				FeatureAddressDetailsDTO addressDto = new FeatureAddressDetailsDTO();
				addressDto.setDistance(rs.getString("Distance"));
				addressDto.setFeatureCode(rs
						.getString("FeatureCode"));
				addressDto.setFeature_image(rs.getString("Images"));

				addressDto.setFeatureDetail(rs
						.getString("FeatureDetail"));
				addressDto.setKiloMeter(rs.getString("kiloMeter"));
				addressDto.setLatitude(rs.getString("Latitude"));
				addressDto.setLongitude(rs.getString("Longitude"));
				addressDto.setSection(rs.getString("Section"));
				addressDto.setBlockSection(rs
						.getString("BlockSection"));

				FeatureSignlsList.add(addressDto);

			}
				
		}
		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/
		return FeatureSignlsList;
	}





	
	public MessageObject GenerateExceptionReportKotaKeyMan(Connection con,
			DB mongoconnection, String parentId, int day2, Boolean isSendMail, long startTime1, long startTime2, long endTime1, long endTime2, Boolean exceptionSummary, Double distancetoleranceInKm, Connection conSunMssql) 
	{
		MessageObject msgo=new MessageObject();
		LoginDAO loginObj=new LoginDAO();
		ArrayList<RailMailSendInfoDto> mailSendInfo=loginObj.getSentMailInfoList(con,parentId);	
		System.err.println("GenerateExceptionReportKotaKeyMan---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-day2;
	
			long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
			////////////////////////////////////////// exception Summary////////////////////////
			
			
			XSSFWorkbook workbookSummary = new XSSFWorkbook();
			XSSFSheet sheetSummary = workbookSummary.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report Summary of keymen"));

			XSSFCellStyle HeadingStyleSummary = workbookSummary.createCellStyle();
			HeadingStyleSummary.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyleSummary.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyleSummary.setAlignment(HorizontalAlignment.CENTER);
			XSSFFont fontSummary = workbookSummary.createFont();
			XSSFFont font = workbookSummary.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyleSummary.setFont(font);

			XSSFCellStyle tripColumnStyleSummary = workbookSummary.createCellStyle();
			tripColumnStyleSummary
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyleSummary.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyleSummary.setWrapText(true);
			font = workbookSummary.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyleSummary = workbookSummary.createCellStyle();
			remarkColumnStyleSummary.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyleSummary.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyleSummary = workbookSummary.createCellStyle();
			error_remarkColumnStyleSummary
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyleSummary
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfontSummary = workbookSummary.createFont();
			redfontSummary.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyleSummary.setFont(redfontSummary);

			// loactionNotFoundColumnStyle.setFont(font);

			CellStyle wrap_styleSummary = workbookSummary.createCellStyle(); // Create new
																// style
			wrap_styleSummary.setWrapText(true); // Set wordwrap

			int petrolMancount = 0;

			 XSSFRow row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for Keyman"
							 + " Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyleSummary);
			
			sheetSummary.addMergedRegion(new CellRangeAddress(sheetSummary.getLastRowNum(),
					sheetSummary.getLastRowNum(), 0, 7));

			BasicDBList sectionDeviceList = new BasicDBList();
			
			row = sheetSummary.createRow(0);
			row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);



			XSSFCell cell = row.createCell(0);
			cell.setCellValue("Sr no");
			cell.setCellStyle(tripColumnStyleSummary);
		/*	
			 cell = row.createCell(1);
			cell.setCellValue("Name");
			cell.setCellStyle(tripColumnStyle);*/

			cell = row.createCell(1);
			cell.setCellValue("Device name");
			cell.setCellStyle(tripColumnStyleSummary);

			cell = row.createCell(2);
			cell.setCellValue("Late start >15min");
			cell.setCellStyle(tripColumnStyleSummary);
			
			cell = row.createCell(3);
			cell.setCellValue("Early off >15 min");
			cell.setCellStyle(tripColumnStyleSummary);

			cell = row.createCell(4);
			cell.setCellValue("Overspeed>8kmph");
			cell.setCellStyle(tripColumnStyleSummary);
			
			cell = row.createCell(5);
			cell.setCellValue("Stoppage >60min. No of stoppage at location with railways geo fence");
			cell.setCellStyle(tripColumnStyleSummary);
			
			cell = row.createCell(6);
			cell.setCellValue("Trip not completed");
			cell.setCellStyle(tripColumnStyleSummary);
			
			cell = row.createCell(7);
			cell.setCellValue("Travelled km, 500 m short from alloted km");
			cell.setCellStyle(tripColumnStyleSummary);
			row.setHeight((short) 1200);
			
		
			
			////////////////////////////////////report Summary//////////////////////////////

		
		for (int j = 0; j < mailSendInfo.size(); j++) {
//			for (int j =1; j <=1;j++) {
			 int totalTrip=0;
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j).getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();


			for (int i = 0; i < deviceInfoList.size(); i++) {
				// for (int i = 4; i <=4; i++) {
		
								//put logic for usfd trip
				if (deviceInfoList.get(i).getName().startsWith("K/")) {
//				if (deviceInfoList.get(i).getName().contains("049")) {
			
				
					ArrayList<ReportAnalysisDTO> LocationWithDistList=new ArrayList<>();
					RailwayPetrolmanTripsBeatsDTO dtoBeatExpected = new RailwayPetrolmanTripsBeatsDTO();

					System.err.println("---Device---"+deviceInfoList.get(i).getName()+"----"+deviceInfoList.get(i).getDeviceID());
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfKeyman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								dtoBeatExpected.setId(rsgertrip.getInt("Id"));

								dtoBeatExpected.setStudentId(rsgertrip.getInt("StudentId"));
								dtoBeatExpected.setDeviceId(rsgertrip.getString("DeviceID"));

								dtoBeatExpected.setKmStart(rsgertrip.getDouble("KmStart"));
								dtoBeatExpected.setKmEnd(rsgertrip.getDouble("KmEnd"));

								dtoBeatExpected.setSectionName(rsgertrip
										.getString("SectionName"));

								dtoBeatExpected.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dtoBeatExpected.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dtoBeatExpected.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dtoBeatExpected.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));
								RailwayKeymanTripsBeatsList.add(dtoBeatExpected);

//								System.err.println("startApprox--"+rsgertrip
//										.getDouble("startApprox")+"-endApprox--"+rsgertrip
//										.getDouble("endApprox"));
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

//					System.err.println("RailwayKeymanTripsBeatsList--size---"+RailwayKeymanTripsBeatsList.size());
//					System.err.println(" **Device =---================"
//							+ deviceInfoList.get(i).getName() + "\n");
					
					BasicDBObject subdocument = new BasicDBObject();

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					
					
					DBCollection table = mongoconnection
							.getCollection(Common.TABLE_LOCATION);
					BasicDBObject device_whereQuery = new BasicDBObject();
					device_whereQuery.put("device", Long
							.parseLong(deviceInfoList.get(i).getDeviceID()));
					// device_whereQuery.put("device",Long.parseLong("355488020181042"));

					
					//Winter Time all 8 hr calculated for trip complete
					BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
							"timestamp", new BasicDBObject("$gte",
									DayStartWorkTime + startTime1).append("$lt",
									DayStartWorkTime + endTime2));
//				
					BasicDBObject speed_whereQuery_morning = new BasicDBObject(
							"speed", new BasicDBObject("$gt",0));
					BasicDBList morn_Milage = new BasicDBList();
					morn_Milage.add(timestamp_whereQuery_morning);
//					morn_Milage.add(speed_whereQuery_morning);
					morn_Milage.add(device_whereQuery);
					
					DBObject morn_Milage_query = new BasicDBObject("$and",
							morn_Milage);

//					
					DBCursor cursor = table.find(morn_Milage_query);
					cursor.sort(new BasicDBObject("timestamp", 1));

					cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

					System.err.println("Cursorlocation  Count of u-p11----"
							+ cursor.size() + "  " + morn_Milage_query);
					
					int k=0;
					DBObject prevDbObjectLoc=null;
					double distPrev_current=0;
					DecimalFormat df = new DecimalFormat("00.000000"); 
					StringBuilder LatString = new StringBuilder(),
							LanString=new StringBuilder(),TimestampString=new StringBuilder()
					,speedString=new StringBuilder();
					if (cursor.size() != 0) {

						for (DBObject dbObject : cursor) {
							
							
//							insertTodayLoaction(conSunMssql,dbObject);

							ReportAnalysisDTO obj = new ReportAnalysisDTO();
							DBObject dbObject_location = (DBObject) dbObject
									.get("location");
							LatString.append(df.format(dbObject_location.get("lat"))).append(",");
							LanString.append(df.format(dbObject_location.get("lon"))).append(",");
							TimestampString.append(dbObject.get("timestamp")).append(",");
							speedString.append(dbObject.get("speed")).append(",");
						}
					} else {

					}
//					row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
//					

					DailyReportSummaryDTO dailySummaryObj=new DailyReportSummaryDTO();

					try {

					java.sql.CallableStatement psgettrip = conSunMssql
							.prepareCall("{call GetDailySummaryOfDevice(?,?,?,?,?,?,?)}");
					psgettrip.setString(1, LatString.toString());
					psgettrip.setString(2, LanString.toString());
					psgettrip.setString(3, TimestampString.toString());
			
					psgettrip.setInt(4,Integer.parseInt(parentId));
					psgettrip.setLong(5,DayStartWorkTime);
					psgettrip.setInt(6,dtoBeatExpected.getStudentId());
					psgettrip.setString(7, speedString.toString());

					System.out.println("data---  '"+LatString+"','"+LanString+"','"+TimestampString+"',"+parentId+","+DayStartWorkTime+","+dtoBeatExpected.getStudentId()+",'"+speedString+"'");
//					System.out.println("LanString---"+LanString);
//					System.out.println("TimestampString---"+TimestampString);
//					System.out.println("Speed---"+speedString);

					ResultSet rsgertrip = psgettrip.executeQuery();
//					System.err.println("rsgertrip-count--"+new Gson().toJson(rsgertrip));
					if (rsgertrip != null) {
						while (rsgertrip.next()) {
							System.err.println("rsgertrip---"+rsgertrip.getString("EventType")+"---"+rsgertrip.getDouble("KiloMeter"));

							ReportDataSummary dto = new ReportDataSummary();
							if(rsgertrip.getString("EventType").equalsIgnoreCase("StartKm"))
							{
								dto.setDeviceId(deviceInfoList.get(i).getDeviceID());
								dto.setFeatureDetail(rsgertrip.getString("FeatureDetail") + "");
								dto.setKilometer(rsgertrip.getDouble("KiloMeter"));
								dto.setDistBetwLocAndRdps(rsgertrip.getDouble("DistBetwLocAndRdps"));
							
								dto.setCompareLan(rsgertrip	.getDouble("CompareLan") );
								dto.setCompareLat(rsgertrip.getDouble("CompareLat") );
								dto.setEventType(rsgertrip.getString("EventType"));
								dto.setDiffFromExpected(rsgertrip.getDouble("diffFromExpected") );
								dto.setOverSpeedConsecutiveCount(rsgertrip.getInt("OverspeedCount"));

								dailySummaryObj.setStartKm(dto);
							}else if(rsgertrip.getString("EventType").equalsIgnoreCase("EndKm"))
							{
								dto.setDeviceId(deviceInfoList.get(i).getDeviceID());
								dto.setFeatureDetail(rsgertrip.getString("FeatureDetail") + "");
								dto.setKilometer(rsgertrip.getDouble("KiloMeter"));
								dto.setDistBetwLocAndRdps(rsgertrip.getDouble("DistBetwLocAndRdps"));
								dto.setDiffFromExpected(rsgertrip.getDouble("diffFromExpected") );

								dto.setCompareLan(rsgertrip	.getDouble("CompareLan") );
								dto.setCompareLat(rsgertrip.getDouble("CompareLat") );
								dto.setEventType(rsgertrip.getString("EventType"));
								dto.setOverSpeedConsecutiveCount(rsgertrip.getInt("OverspeedCount"));

								dailySummaryObj.setEndKm(dto);
							}else if(rsgertrip.getString("EventType").equalsIgnoreCase("StartTime"))
							{
								dto.setDeviceId(deviceInfoList.get(i).getDeviceID());
								dto.setFeatureDetail(rsgertrip.getString("FeatureDetail") + "");
								dto.setDiffFromExpected(rsgertrip.getDouble("diffFromExpected") );

								dto.setStartOrEndTimestamp(rsgertrip.getLong("KiloMeter"));
								dto.setDistBetwLocAndRdps(rsgertrip.getDouble("DistBetwLocAndRdps"));
								dto.setEventType(rsgertrip.getString("EventType"));
								dailySummaryObj.setStartTime(dto);
							}else if(rsgertrip.getString("EventType").equalsIgnoreCase("EndTime"))
							{
								dto.setDeviceId(deviceInfoList.get(i).getDeviceID());
								dto.setFeatureDetail(rsgertrip.getString("FeatureDetail") + "");
								dto.setStartOrEndTimestamp(rsgertrip.getLong("KiloMeter"));
								dto.setDistBetwLocAndRdps(rsgertrip.getDouble("DistBetwLocAndRdps"));
			
								dto.setDiffFromExpected(rsgertrip.getDouble("diffFromExpected") );

								dto.setEventType(rsgertrip.getString("EventType"));
								
								dailySummaryObj.setEndTime(dto);
							}
						
						}
					}
					System.out.println("Result Daily Report---"+new Gson().toJson(dailySummaryObj));

				} catch (Exception e) {
					e.printStackTrace();
				}
            System.err.println("dailySummaryObj---"+new Gson().toJson(dailySummaryObj));
					if (dtoBeatExpected!=null&&dailySummaryObj!=null&&
							dailySummaryObj.getEndKm()!=null&&
							dailySummaryObj.getStartKm()!=null&&
							dailySummaryObj.getStartTime()!=null&&
							dailySummaryObj.getEndTime()!=null) {
						
						double kmExpected=Math.abs(dtoBeatExpected.getKmEnd()-dtoBeatExpected.getKmStart());
						double kmActualCover=Math.abs(dailySummaryObj.getEndKm().getKilometer()-dailySummaryObj.getStartKm().getKilometer());
						if (dailySummaryObj.getStartTime().getDiffFromExpected()>900
							||dailySummaryObj.getEndTime().getDiffFromExpected()>900
							|| dailySummaryObj.getStartKm().getOverSpeedConsecutiveCount()>5
							||(kmExpected-kmActualCover)*1000>500)
						{
							
							try {

								XSSFRow rowSummary = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);

								 Cell cellSummary = rowSummary.createCell(0);
								cellSummary.setCellValue(sheetSummary.getLastRowNum()-2);
								cellSummary.setCellStyle(wrap_styleSummary);
								
								cellSummary = rowSummary.createCell(1);
								cellSummary.setCellValue(deviceInfoList.get(i).getName());
								cellSummary.setCellStyle(wrap_styleSummary);

							
								

								cellSummary = rowSummary.createCell(2);
								if (dailySummaryObj.getStartTime().getDiffFromExpected()>900)
									cellSummary.setCellValue(Common.getDateCurrentTimeZone(dailySummaryObj.getStartTime().getStartOrEndTimestamp()));
								else cellSummary.setCellValue("-");



								cellSummary = rowSummary.createCell(3);
								if (dailySummaryObj.getEndTime().getDiffFromExpected()>900)
									cellSummary.setCellValue(Common.getDateCurrentTimeZone(dailySummaryObj.getEndTime().getStartOrEndTimestamp()));
								else cellSummary.setCellValue("-");
								cellSummary.setCellStyle(wrap_styleSummary);

									
								


								cellSummary = rowSummary.createCell(4);
								if (dailySummaryObj.getStartKm().getOverSpeedConsecutiveCount()>5)
									cellSummary.setCellValue("Found in OverSpeed");
								else cellSummary.setCellValue("-");
								cellSummary.setCellStyle(wrap_styleSummary);

						

								cellSummary = rowSummary.createCell(5);
								/*if (exceptionReortsTrip.getTripStart()
										.getMaxSpeed() > 8&&exceptionReortsTrip.getMaxSpeed()>8)
									cellSummary.setCellValue(exceptionReortsTrip.getTripStart().getMaxSpeed());
								else*/ cellSummary.setCellValue("-");
								cellSummary.setCellStyle(wrap_styleSummary);;
						
								
								cellSummary = rowSummary.createCell(6);
								if ((kmExpected-kmActualCover)*1000>500)
									cellSummary.setCellValue("kmcover : "+df2.format(kmActualCover)+" Km");
								else cellSummary.setCellValue("-");
								cellSummary.setCellStyle(wrap_styleSummary);
								
//								subdocument.put("kmcover",
//										df2.format(kmcover));
								// System.out.println("  Here--*****************************************--------------------");
								//
							
								cellSummary = rowSummary.createCell(7);
								if ((kmExpected-kmActualCover)*1000>500)
									cellSummary.setCellValue("Expected "+kmExpected+"Km but device cover only : "+df2.format(kmActualCover)+" Km");
								else cellSummary.setCellValue("-");									
																	
								cellSummary.setCellStyle(wrap_styleSummary);


							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							}
						
						}
					
		            
					}
//				
				}
				

			}
			
					
				
				
//			if (deviceInfoList.size() > 0
//						&& petrolMancount != deviceInfoList.size()) {
				sheetSummary.setColumnWidth(0, 800);
				sheetSummary.setColumnWidth(1, 4000);
				sheetSummary.setColumnWidth(2, 3000);
				sheetSummary.setColumnWidth(3, 3000);
				sheetSummary.setColumnWidth(4, 3000);
				sheetSummary.setColumnWidth(5, 3000);
				sheetSummary.setColumnWidth(6, 3000);
				sheetSummary.setColumnWidth(7, 10000);

					String outFileName ="Exception_Summary_Report_KeyMan"
							+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_" + parentId + ".xlsx";

					try {
						String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
								+ outFileName;
						FileOutputStream fos = new FileOutputStream(new File(file));

						workbookSummary.write(fos);
						fos.flush();
						fos.close();
						workbookSummary.close();
						// String sendemail="rupesh.p@mykiddytracker.com,";
	
						/*String sendemail = "";
						for (String emaString : mailSendInfo.get(j).getEmailIds()) {
							sendemail = sendemail + "," + emaString.trim();
						}
						SendEmail mail = new SendEmail();
	
					if(isSendMail)
						 {
							 mail.sendDeviceExceptionMailToJaipur(sendemail, file,
									 mailSendInfo
									  .get(j).getDept(),"Exception Trip Report of keymen"
									  ,"Exception Trip Report of ",false,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());
						 }
				*/
						 
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
					
	
//					}
							
			
		}
		return msgo;
			
	}





	private void insertTodayLoaction(Connection conSunMssql, DBObject dbObject) {
		try {

			// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
			java.sql.CallableStatement ps = conSunMssql
					.prepareCall("{call InsertTodayLocation(?,?,?,?,?,?)}");

			
			
			ps.setDouble(1, (double)( (DBObject) dbObject
					.get("location")).get("lat"));
			ps.setDouble(2,(double) ( (DBObject) dbObject
					.get("location")).get("lon"));
			ps.setInt(3,  (int) dbObject.get("speed"));

			ps.setString(4, dbObject.get("device")+"");

			ps.setInt(5, (int) dbObject.get("satellite_no"));
			ps.setString(6,  dbObject.get("timestamp")+"");
			int result = ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	
		
	}





	private FeatureAddressDetailsDTO getRdpsKmNearByLocation(Connection con,
			Double deviceLocationLat, Double deviceLocationLan, String parentId) {

		FeatureAddressDetailsDTO addressDto=null;

		
	try {
		java.sql.CallableStatement ps = con
				.prepareCall("{call GetNerbyRDPSwithDistWithoutSectionName(?,?,?)}");
		ps.setDouble(2, deviceLocationLan );
		ps.setDouble(1, deviceLocationLat );
		ps.setInt(3, Integer.parseInt(parentId));		
	
		 System.out.println("getRdpsKmNearByLocation input ------"+deviceLocationLat+","+deviceLocationLan+"--"+parentId+"--"+"--");;

		ResultSet rs = ps.executeQuery();

		if (rs != null) {

			while (rs.next()) {
				addressDto = new FeatureAddressDetailsDTO();
				addressDto.setDistance(rs
						.getString("Distance") + "");
				addressDto.setFeatureCode(rs
						.getString("FeatureCode") + "");
				addressDto.setFeature_image(rs
						.getString("Images") + "");
				addressDto.setFeatureDetail(rs
						.getString("FeatureDetail") + "");
				addressDto.setKiloMeter(rs
						.getString("kiloMeter") + "");
				addressDto.setLatitude(rs
						.getString("Latitude") + "");
				addressDto.setLongitude(rs
						.getString("Longitude") + "");
				addressDto.setSection(rs
						.getString("Section") + "");
				addressDto.setBlockSection(rs
						.getString("BlockSection") + "");
				addressDto.setNearByDistance(rs
						.getString("NearByDistance") + "");
				addressDto.setCompareLan(rs
						.getDouble("CompareLan") );
				addressDto.setCompareLat(rs
						.getDouble("CompareLat") );
				addressDto.setLocationKmCalculated(rs
						.getDouble("LocationKM"));
//				 System.err.println("getRdpsKmNearByLocation ------"+addressDto.getNearByDistance());

				
//				if (Double.parseDouble(addressDto.getNearByDistance())<150.00) 
//					km=df2.format(Double.parseDouble(addressDto.getKiloMeter()))+"";

				
				 System.err.println("getRdpsKmNearByLocation ------ "+new Gson().toJson(addressDto));;

			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}

	return addressDto;
}
		
	





	public MessageObject GenerateExceptionReportPetrolmanBeatPathNewLogic(
			Connection con, DB mongoconnection, String parentId, int dayCount, Boolean isSendMail, int seasonId)
	{

		MessageObject msgo = new MessageObject();

		//for (int pastday = 74;pastday >45; pastday--) {
		LoginDAO loginObj=new LoginDAO();
		ArrayList<RailMailSendInfoDto> mailSendInfo = loginObj.getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionReportpatrolmanBeatPath ---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();
		
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-dayCount;
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		

		long startdate = Long.valueOf(Common.getGMTTimeStampFromDate(day
				+ "-" + String.valueOf(month + 1) + "-" + year
				+ " 00:00 am"));
		long enddate = Long.valueOf(Common.getGMTTimeStampFromDate(day
				+ "-" + String.valueOf(month + 1) + "-" + year
				+ " 00:00 am"));


		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

		// ///////////////////////////////********************************/////////////////////////////
		// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("Patrolman Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"PatrolMan Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						6));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(6);
		cell.setCellValue("Start with Battery 30%-60%");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 800);

		// /////////////////////////All Section End//////////////////////////////////////////////////
		
		
		//*******////////// All Section Count AlertForKeymanWorkStatusReport*/////////////////////////

		XSSFWorkbook workbookPatrolmanWorkStatusReportCount = new XSSFWorkbook();
		XSSFSheet sheetPatrolmanWorkStatusReportCount = workbookPatrolmanWorkStatusReportCount
				.createSheet(WorkbookUtil
						.createSafeSheetName("PatrolMan Status Report with count"));
		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReportCount = workbookPatrolmanWorkStatusReportCount
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReportCount
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReportCount
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		 font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		 tripColumnStyleAlertForKeymanWorkStatusReport = workbookPatrolmanWorkStatusReportCount
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		XSSFCellStyle wrap_styleAlertForKeymanWorkStatusReportCount = workbookPatrolmanWorkStatusReportCount
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReportCount.setWrapText(true); // Set
															

		row = sheetPatrolmanWorkStatusReportCount.createRow(0);
		row = sheetPatrolmanWorkStatusReportCount
				.createRow(sheetPatrolmanWorkStatusReportCount.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetPatrolmanWorkStatusReportCount
				.createRow(sheetPatrolmanWorkStatusReportCount.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetPatrolmanWorkStatusReportCount
				.createRow(sheetPatrolmanWorkStatusReportCount.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"PatrolMan Work Status Count Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetPatrolmanWorkStatusReportCount
				.addMergedRegion(new CellRangeAddress(
						sheetPatrolmanWorkStatusReportCount.getLastRowNum(),
						sheetPatrolmanWorkStatusReportCount.getLastRowNum(), 0,
						4));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReportCount);

		row = sheetPatrolmanWorkStatusReportCount
				.createRow(sheetPatrolmanWorkStatusReportCount.getLastRowNum() + 1);

		 cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		/*cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);*/
		cell = row.createCell(1);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(3);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(4);
		cell.setCellValue("Total Device");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 800);
	/*	cell = row.createCell(6);
		cell.setCellValue("Section Status");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 500);*/

		// ///////////////////////////////////All Section COunt// End//////////////////////////////////////////////////////////

		int divDeviceOffCount=0;
		int divDeviceBeatNotCoverCount=0;
		int divDeviceBeatCoverCount=0;

		
		
		
		
		for (int j = 0; j < mailSendInfo.size(); j++) {
//	 for (int j =7; j <=7; j++) {
		//for (int j = mailSendInfo.size()-1; j <=mailSendInfo.size()-1;j++) {

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayPetrolmanTripsBeatsList = new ArrayList<>();
			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			Set<String> KeyMan60PercentBattery_Set = new HashSet<String>();

			ArrayList<Short> rowHeight = new ArrayList<>();
			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Patrolman Exception Trip Report"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 20);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			
			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);
			int keyMancount = 0;

			for (int i = 0; i < deviceInfoList.size(); i++) {
//				 for (int i = 0; i <= 0; i++) {

			

				if (deviceInfoList.get(i).getName().startsWith("P/")) {
//				if (deviceInfoList.get(i).getName().startsWith("P/")
//								&&deviceInfoList.get(i).getName().contains("-426")) {		

					 int deviceLoactionCount=0;
					
					Double startBatteryStatus = loginObj.getStartBatteryStatus(
							mongoconnection, deviceInfoList.get(i).getDeviceID(),
							startdate);
					Double endBatteryStatus = loginObj.getEndBatteryStatus(mongoconnection,
							deviceInfoList.get(i).getDeviceID(), startdate);
					

					RailwayPetrolmanTripsBeatsList
							.removeAll(RailwayPetrolmanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfPetrolmanWithSeasonId(?,?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());
						psgettrip.setInt(2, seasonId);
						ResultSet rsgertrip = psgettrip.executeQuery();
						System.err.println("GetTripSheduleOfPetrolmanWithSeasonId=----"+rsgertrip);
						
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));
								dto.setFkTripMasterId(rsgertrip
										.getInt("fk_TripMasterId"));
								dto.setKmFromTo(rsgertrip.getString("KmFromTo"));
								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));
								dto.setTotalKmCover(rsgertrip
										.getDouble("TotalKmCover"));
								dto.setInitialDayStartTime(rsgertrip
										.getString("InitailDayStratTime"));
								dto.setSheetNo(rsgertrip.getInt("SheetNo"));
								dto.setSectionName(rsgertrip
										.getString("SectionName"));
								dto.setTripName(rsgertrip.getString("TripName"));
								dto.setTripTimeShedule(rsgertrip
										.getString("TripTimeShedule"));
								dto.setTripStartTimeAdd(rsgertrip
										.getInt("TripStartTimeAdd"));
								dto.setTripSpendTimeIntervalAdd(rsgertrip
										.getInt("TripSpendTimeIntervalAdd"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));

								RailwayPetrolmanTripsBeatsList.add(dto);

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}


					
					
					/*ArrayList totalLocationList=GetHistorydata(mongoconnection, deviceInfoList.get(i).getDeviceID()+"", DayStartWorkTime+"");

					if (totalLocationList.size()==0)
					{
						KeyManOffDevice_Set.add(deviceInfoList
								.get(i).getName());						
					}*/
					Double distanceTolerance = 0.9;
					

//					System.err.println("totalLocationList----size--"+totalLocationList.size());
					for (int r = 0; r < RailwayPetrolmanTripsBeatsList.size(); r++) {

						RailwayPetrolmanTripsBeatsDTO railMailSendInfoDto = RailwayPetrolmanTripsBeatsList
								.get(r);
						
						/*long timeTolerance = railMailSendInfoDto
								.getTripSpendTimeIntervalAdd()/3;
						*/
//						long timeTolerance=900;

						long timeTolerance=2400;

						long triptStartTime = DayStartWorkTime
								+ railMailSendInfoDto.getTripStartTimeAdd()
								- timeTolerance;
						long triptEndTime = DayStartWorkTime
								+ railMailSendInfoDto.getTripStartTimeAdd()
								+ railMailSendInfoDto
										.getTripSpendTimeIntervalAdd()
								+ timeTolerance;

						// System.out.println("triptStartTime----"
						// + Common.getDateCurrentTimeZone(triptStartTime)
						// + "========triptEndTime----"
						// + Common.getDateCurrentTimeZone(triptEndTime));
						ArrayList<String> kmStartLatSet = new ArrayList<>(), kmStartLangSet = new ArrayList<>(), kmEndLatSet = new ArrayList<>(), kmEndLangSet = new ArrayList<>();

						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(railMailSendInfoDto.getDeviceId()));
						// device_whereQuery.put("device",Long.parseLong("355488020181042"));

						BasicDBObject timestamp_whereQuery = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										triptStartTime).append("$lt",
										triptEndTime));
						
						System.out.println("triptStartTime--"+triptStartTime+"--triptEndTime---"+triptEndTime);
						
						BasicDBList And_Milage = new BasicDBList();
						And_Milage.add(timestamp_whereQuery);
						And_Milage.add(device_whereQuery);
						DBObject Total_Milage_query = new BasicDBObject("$and",
								And_Milage);

						DBCursor cursor = table.find(Total_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + Total_Milage_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
						ArrayList<DBObject> listObjects = new ArrayList<>();
						if (cursor.size() > 0) {

							// System.err.println("*--listObjects---COunt---" +
							// cursor.size());

							listObjects = (ArrayList<DBObject>) cursor
									.toArray();
							deviceLoactionCount=listObjects.size();

						}
						/*
						 * int
						 * pos=getNearByKMStartEnd(listObjects.get(0),kmStartLatSet
						 * ,kmStartLangSet);
						 * 
						 * System.err.println("*-----kmStartLatSet---"+new
						 * Gson().toJson(kmStartLatSet));
						 * System.err.println("*-----kmStartLangSet---"+new
						 * Gson().toJson(kmStartLangSet));
						 * System.err.println("*-----kmEndLatSet---"+new
						 * Gson().toJson(kmEndLatSet));
						 * System.err.println("*-----kmEndLangSet---"+new
						 * Gson().toJson(kmEndLangSet));
						 * 
						 * startLatOfRouteKm=Double.parseDouble(kmStartLatSet.get
						 * (pos));
						 * startLangOfRouteKm=Double.parseDouble(kmStartLangSet
						 * .get(pos));
						 * endLatOfRouteKm=Double.parseDouble(kmEndLatSet
						 * .get(pos));
						 * endLangOfRouteKm=Double.parseDouble(kmEndLangSet
						 * .get(pos));
						 */

						Double minDistanceCal = 0.0;
						// Get sart kM Location
						if (listObjects.size() > 0) {

							System.err.println("*-----Km --stat-"
									+ railMailSendInfoDto.getKmStartLat() + ","
									+ railMailSendInfoDto.getKmStartLang()
									+ "------"
									+ railMailSendInfoDto.getKmEndLat() + ","
									+ railMailSendInfoDto.getKmEndLang());

							if (railMailSendInfoDto.getKmStartLat() > 0
									&& railMailSendInfoDto.getKmStartLang() > 0
									&& railMailSendInfoDto.getKmEndLat() > 0
									&& railMailSendInfoDto.getKmEndLang() > 0) {

								PoleNearByLocationDto tripStartLocation = loginObj.locationNearbyKMPatrolMan(
										"Start",
										railMailSendInfoDto.getKmStart(),
										railMailSendInfoDto.getKmStartLat(),
										railMailSendInfoDto.getKmStartLang(),
										listObjects, DayStartWorkTime,"P",triptStartTime,(triptStartTime+timeTolerance*2),railMailSendInfoDto.getTripSpendTimeIntervalAdd(),timeTolerance);
								
								tripStartLocation.setTripStartExpectedTime(triptStartTime+ timeTolerance);
								tripStartLocation.setTripEndExpectedTime(triptEndTime- timeTolerance);
								tripStartLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripStartLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								PoleNearByLocationDto tripEndLocation = loginObj.locationNearbyKMPatrolMan(
										"End", railMailSendInfoDto.getKmEnd(),
										railMailSendInfoDto.getKmEndLat(),
										railMailSendInfoDto.getKmEndLang(),
										listObjects, DayStartWorkTime,"P",(triptEndTime-timeTolerance*2),triptEndTime,railMailSendInfoDto.getTripSpendTimeIntervalAdd(),timeTolerance);

								tripEndLocation
										.setTripStartExpectedTime(triptStartTime
												+ timeTolerance);
								tripEndLocation
										.setTripEndExpectedTime(triptEndTime
												- timeTolerance);
								tripEndLocation
										.setStartkmBeatExpected(railMailSendInfoDto
												.getKmStart());
								tripEndLocation
										.setEndKmBeatExpected(railMailSendInfoDto
												.getKmEnd());

								ExceptionReortsTrip exDto = new ExceptionReortsTrip();
								exDto.setTripNo(r + 1);
								exDto.setTripStart(tripStartLocation);
								exDto.setTripEnd(tripEndLocation);
								exDto.setLocationSize(listObjects.size());
								for (DBObject dbObjectSpeed : listObjects) {
									int SpeedcCheck = (int) dbObjectSpeed.get("speed");
									Long timeloc=Long.parseLong(dbObjectSpeed.get("timestamp")+"");
								
							
									if(SpeedcCheck>8&&(timeloc>triptStartTime+timeTolerance && timeloc<triptEndTime-timeTolerance)){
										exDto.setMaxSpeed(SpeedcCheck);
										exDto.setMaxSpeedTime(timeloc);
										}
									
									}

			
								exceptiionalTrip.add(exDto);
							} else {

								exceptiionalTrip.add(null);
							}
							listObjects.removeAll(listObjects);
						} else {

							System.out
									.println(" **exceptionReortsTrip*******Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

							ExceptionReortsTrip exDto = new ExceptionReortsTrip();
							exDto.setTripNo(r + 1);
							exDto.setTripStart(null);
							exDto.setTripEnd(null);
							exDto.setLocationSize(0);

							exceptiionalTrip.add(exDto);

						}

					}

					// //////////////////////////////////
					if (exceptiionalTrip.size() > 0
							&& exceptiionalTrip.get(0) != null
							) {
						StringBuilder remark = new StringBuilder();
						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								"Exception Report for "
										+ deviceInfoList.get(i).getName() + "("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")  Date :-"
										+ Common.getDateFromLong(startdate));
						row.getCell(0).setCellStyle(HeadingStyle);

						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 11));


						// Start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((startBatteryStatus / 6) * 100))
								+ " %");
						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 11));
						// End Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("End Battery status :- "
								+ df2.format(((endBatteryStatus / 6) * 100))
								+ " %");
						row.getCell(0).setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 11));
						
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");
						row.getCell(0).setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 11));

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						cell = row.createCell(0);

						cell.setCellValue("Trip#");
						cell.setCellStyle(tripColumnStyle);
						cell = row.createCell(1);
						cell.setCellValue("Start Time	");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue("Expected Start Time");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue("Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(4);
						cell.setCellValue("Expected Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(5);
						cell.setCellValue("End Time	");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(6);
						cell.setCellValue("Expected End Time");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(7);
						cell.setCellValue("End Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(8);
						cell.setCellValue("Expected end Beat");
						cell.setCellStyle(tripColumnStyle);


						/*
						 * cell = row.createCell(9);
						 * cell.setCellValue("Stoppage min"); cell =
						 * row.createCell(10);
						 * cell.setCellValue("Average speed");
						 */
						cell = row.createCell(9);
						cell.setCellValue("Max Speed");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(10);
						cell.setCellValue("Total Distance cover");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(11);
						cell.setCellValue("Trip Detail Remark");
						cell.setCellStyle(tripColumnStyle);
						
						for (ExceptionReortsTrip exceptionReortsTrip :  
							exceptiionalTrip) {
							if (exceptionReortsTrip != null
									&& exceptionReortsTrip.getTripStart() != null) {
								try {
									

									double kmcover = 0;
									double expactedKmCover = 0;
									String startActualKm=loginObj.getRdpsKmNearByLocation(con, exceptionReortsTrip.getTripStart().getLat(),
											exceptionReortsTrip.getTripStart().getLang(), parentId,exceptionReortsTrip.getSectionName(),exceptionReortsTrip.getTripStart().getStartkmBeatExpected());
									
									String endActualKm=loginObj.getRdpsKmNearByLocation(con, exceptionReortsTrip.getTripEnd().getLat(),
											exceptionReortsTrip.getTripEnd().getLang(), parentId,exceptionReortsTrip.getSectionName(),exceptionReortsTrip.getTripEnd().getEndKmBeatExpected());
										// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());

									String DetailRemark="";
									if (!endActualKm.equals("NA")&&!startActualKm.equals("NA"))
									{
									
									kmcover=Double.parseDouble(endActualKm)-Double.parseDouble(startActualKm);
									if (kmcover<0) 								
									kmcover=kmcover*-1;
									
									expactedKmCover = exceptionReortsTrip
											.getTripStart()
											.getEndKmBeatExpected()
											- exceptionReortsTrip
													.getTripStart()
													.getStartkmBeatExpected();
									
									
									if (expactedKmCover < 0)
										expactedKmCover = -1
												* expactedKmCover;
								
									/*if (exceptionReortsTrip.getTripStart()
											.getTripStartExpectedTime()
											- exceptionReortsTrip
													.getTripStart()
													.getTimestamp() > timeTolerance)
										remark.append("\t "
												+ exceptionReortsTrip
														.getTripNo()
												+ " delayed.");*/

									if (kmcover < expactedKmCover
											- distanceTolerance && exceptionReortsTrip.getTripStart().getTotal_distance()>1)
										{
										remark.append("\tTotal beats not completed in Trip "
										
												+ exceptionReortsTrip
														.getTripNo() + ".");
										DetailRemark="Total beat route distance not cover.";

										}
									else if( exceptionReortsTrip.getTripStart().getTotal_distance()<1)
										{
										remark.append("\tDevice was not moved in Trip "
												+ exceptionReortsTrip
														.getTripNo() + ".");
										DetailRemark="Device was not moved in Trip ";
										}
									
									
									try {

										row = sheet.createRow(sheet
												.getLastRowNum() + 1);
										cell = row.createCell(0);
										cell.setCellValue(exceptionReortsTrip
												.getTripNo());

										cell = row.createCell(1);
										if (exceptionReortsTrip.getTripStart()
												.getTimestamp() > 0)
											cell.setCellValue(Common
													.getDateCurrentTimeZone(exceptionReortsTrip
															.getTripStart()
															.getTimestamp()));
										else if(exceptionReortsTrip.getTripStart().getTotal_distance()<1)
											{
											cell.setCellValue("Device was not moved");
											DetailRemark="Device was not moved in Trip ";

											}
										else
										{
										
										cell.setCellValue("Not found");
										if (exceptionReortsTrip.getTripStart()
											.getTimestamp()== 0)
//										remark.append("\tDevice not found near by beat at expected time may be cover beat early or late in Trip "+ exceptionReortsTrip.getTripNo());
											DetailRemark="Device not found near by beat at expected time may be beat cover early or late in Trip";

										
										}
										cell = row.createCell(2);
										cell.setCellValue(Common
												.getDateCurrentTimeZone(exceptionReortsTrip
														.getTripStart()
														.getTripStartExpectedTime()));

										cell = row.createCell(3);
										cell.setCellValue(df2
												.format(Double.parseDouble(startActualKm)));
										cell = row.createCell(4);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripStart()
														.getStartkmBeatExpected()));

										cell = row.createCell(5);
										if (exceptionReortsTrip.getTripEnd()
												.getTimestamp() > 0)
											{
											cell.setCellValue(Common
													.getDateCurrentTimeZone(exceptionReortsTrip
															.getTripEnd()
															.getTimestamp()));
											if (exceptionReortsTrip
													.getTripEnd()
													.getTimestamp()<exceptionReortsTrip
													.getTripStart()
													.getTimestamp()) {
												
//												remark.append("\tDevice moved in opposite direction ie."
//														+ "end beat to start beat in Trip No "+ exceptionReortsTrip.getTripNo());
												DetailRemark="Device not found near by beat at expected time may be beat cover early or late in Trip";

												
											}
											
											
											}
										else if(exceptionReortsTrip.getTripStart().getTotal_distance()<1)
											{
											cell.setCellValue("Device was not moved");
											DetailRemark="Device was not moved";

											}
										else
											{
											
											cell.setCellValue("Not found");
											if (exceptionReortsTrip.getTripEnd()
												.getTimestamp()== 0&&exceptionReortsTrip.getTripStart()
												.getTimestamp()> 0)
//											remark.append("\tDevice not found near by beat at expected time may be beat cover beat early or late in Trip "+ exceptionReortsTrip.getTripNo());
												DetailRemark="Device not found near by beat at expected time may be beat cover early or late in Trip";
											
											
											}


										cell = row.createCell(6);
										cell.setCellValue(Common
												.getDateCurrentTimeZone(exceptionReortsTrip
														.getTripEnd()
														.getTripEndExpectedTime()));

										cell = row.createCell(7);
										cell.setCellValue(df2
												.format(Double.parseDouble(endActualKm)));
										cell = row.createCell(8);
										cell.setCellValue(df2
												.format(exceptionReortsTrip
														.getTripEnd()
														.getEndKmBeatExpected()));

										/*
										 * cell = row.createCell(9);
										 * cell.setCellValue(""); cell =
										 * row.createCell(10);
										 * cell.setCellValue(exceptionReortsTrip
										 * .getTripStart() .getAvgSpeed());
										 */
										cell = row.createCell(9);
										cell.setCellValue(exceptionReortsTrip
												.getTripStart().getMaxSpeed());
										cell = row.createCell(10);
										cell.setCellValue(df2.format(kmcover));
										
									
										cell = row.createCell(11);
										cell.setCellValue(DetailRemark);
										
										if (DetailRemark.length()>0) {


											if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
											{
											KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
											KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
											
											}else{
												KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());

											}
											remark.append("\tTotal beats not completed in Trip "
													
												+ exceptionReortsTrip
														.getTripNo() + ".");
											
												row.getCell(3).setCellValue("-");
												row.getCell(7).setCellValue("-");
												row.getCell(10).setCellValue("-");

												
										}
										
										if (exceptionReortsTrip.getTripStart()
												.getMaxSpeed() > 8&&exceptionReortsTrip.getMaxSpeed()>8) {
											KeyManOverspeedDevices_Set.add(deviceInfoList.get(i)
															.getName());
										}


										// System.out.println("  Here--*****************************************--------------------");
										//
										
										
										
										
										

									} catch (NumberFormatException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (MongoException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									
									}//NA if end
									else
									{
										System.err.println("++++++++++++++++++++++Near by rdps not found++++++++++++++++++++++++++++++");
										
										
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
								
								
							
							} else {
								// Exception of location not found
					
									// System.out.println(" **exceptionReortsTrip*******row*********************-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size());
									// Exception of location not found
									if (exceptionReortsTrip != null
											&& exceptionReortsTrip
													.getLocationSize() == 0)
										try {
											// System.out.println(" **exceptionReortsTrip*******row******************"
											// +
											// "***-----sheet.getLastRowNum()---------------"+sheet.getLastRowNum()+"----"+exceptiionalTrip.size()+"-----"+exceptionReortsTrip.getLocationSize());

											if (deviceLoactionCount==0) {
												KeyManOffDevice_Set.add(deviceInfoList
														.get(i).getName());
												remark.append("\tDevice is Off");
												row = sheet
														.createRow(sheet.getLastRowNum() + 1);
												cell = row.createCell(0);
												cell.setCellValue(exceptionReortsTrip
														.getTripNo());
												cell.setCellStyle(loactionNotFoundColumnStyle);

												cell = row.createCell(1);
												cell.setCellValue("Device is Off : "
														+ exceptionReortsTrip.getTripNo()
														+ ".");
												cell.setCellStyle(loactionNotFoundColumnStyle);

											}else{
												remark.append("\nLocation not found for Trip no : "
														+ exceptionReortsTrip.getTripNo()
														+ ".");
												row = sheet
														.createRow(sheet.getLastRowNum() + 1);
												cell = row.createCell(0);
												cell.setCellValue(exceptionReortsTrip
														.getTripNo());
												cell.setCellStyle(loactionNotFoundColumnStyle);

												cell = row.createCell(1);
												cell.setCellValue("Location not found in shedule time for Trip no: "
														+ exceptionReortsTrip.getTripNo()
														+ ".");
												cell.setCellStyle(loactionNotFoundColumnStyle);
//												DetailRemark="Location not found in shedule time for Trip.";

												if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
												{
												KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
												KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
												}else{
													KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());

												}
											}
											
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						
							if (remark.toString().length() > 0)
							{
							remark_cell.setCellValue("Remark status :- "
									+ remark.toString());
							remark_cell.setCellStyle(error_remarkColumnStyle);

							if (!KeyManOffDevice_Set.contains(deviceInfoList.get(i)
											.getName())) {
								if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
								{
								KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
								KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
								}else{
									KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());

								}
								
							}
							
//							remark_cell.getRow().setHeight((short) ((Common.findWord(remark.toString(), "\n").size())*400));
						
							}
								
						else
							{
							if (exceptionReortsTrip.getTripStart()
									.getMaxSpeed() > 8&&exceptionReortsTrip.getMaxSpeed()>8) {
								KeyManOverspeedDevices_Set.add(deviceInfoList.get(i)
												.getName());
								remark_cell.setCellValue("\nRemark status :- Work done succesfully But found in Overspeed");

								remark_cell.setCellStyle(error_remarkColumnStyle);

							}else
							{
								remark_cell.setCellValue("Remark status :- All work done succesfully.");

							}
							
							if (!KeyManOffDevice_Set.contains(deviceInfoList.get(i)
									.getName()))
							KeyManCoverSucefullyDevices_Set
							.add(deviceInfoList.get(i)
									.getName());
							}
						}

						

					} else {
						System.err
								.println(" **exceptionReortsTrip*******Rdps data  not found*********d************--"
										+ RailwayPetrolmanTripsBeatsList.size()
										+ "----"
										+ deviceInfoList.get(i).getName()
										+ "("
										+ deviceInfoList.get(i).getDeviceID());

						/*
						 * if(railMailSendInfoDto.getKmStartLat()>0&&
						 * railMailSendInfoDto.getKmStartLang()>0
						 * &&railMailSendInfoDto
						 * .getKmEndLat()>0&&railMailSendInfoDto
						 * .getKmEndLang()>0)
						 */
						if (exceptiionalTrip.size() > 0
								&& exceptiionalTrip.get(0) == null) {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0)
										.setCellValue(
												"Exception  Report for  "
														+ deviceInfoList.get(i)
																.getName()
														+ "("
														+ deviceInfoList.get(i)
																.getDeviceID()
														+ ")  Date :-"
														+ Common.getDateFromLong(startdate));
								row.setRowStyle(HeadingStyle);

								// Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Battery status :- Not found");
								row.setRowStyle(remarkColumnStyle);

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row.setRowStyle(remarkColumnStyle);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :- RDPS data not found.");
								
								if (!KeyManOffDevice_Set.contains(deviceInfoList.get(i)
										.getName()))
								{
									
									if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
									{
									KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
									KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
									}else{
										KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());

									}
								}

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {

							try {
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								row.createCell(0);
								row.getCell(0)
										.setCellValue(
												"Exception  Report for  "
														+ deviceInfoList.get(i)
																.getName()
														+ "("
														+ deviceInfoList.get(i)
																.getDeviceID()
														+ ")  Date :-"
														+ Common.getDateFromLong(startdate));
								row.setRowStyle(HeadingStyle);

								// Battery Status
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								cell = row.createCell(0);
								cell.setCellValue("Battery status :- Not found");
								row.setRowStyle(remarkColumnStyle);

								row = sheet
										.createRow(sheet.getLastRowNum() + 1);
								row.setRowStyle(remarkColumnStyle);
								cell = row.createCell(0);
								cell.setCellValue("Remark status :-Trips not Shedule for this device.");

								if (deviceLoactionCount > 0) {
								if (!KeyManOffDevice_Set.contains(deviceInfoList.get(i)
										.getName()))
									KeyManExceptionalDevices_Set
									.add(deviceInfoList.get(i)
											.getName());
								}else {
									KeyManOffDevice_Set
									.add(deviceInfoList.get(i)
											.getName());
								}
								
								
								
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MongoException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					}
					// System.out.println("ttttt--send mail---"
					// + deviceInfoList.size() + "----");
					exceptiionalTrip.removeAll(exceptiionalTrip);

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());
					
					if (startBatteryStatus < 3
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName()))
						KeyManLowBattery_Set.add(deviceInfoList.get(i)
								.getName());
					
					if ((startBatteryStatus==3||startBatteryStatus==4)
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName()))
						KeyMan60PercentBattery_Set.add(deviceInfoList.get(i)
								.getName());
					
				} else {
					// System.out.println("patrol Man found--------------------");
				keyMancount++;

				}
				
				
			}

			for (int s = 1; s < 15; s++) {
				sheet.autoSizeColumn(s);
			}

			if (deviceInfoList.size() > 0&&keyMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept().replace("/", "_") + "_"
						+ "Exception_Trip_Report_PartolMen"
						+ Common.getDateFromLong(startdate).replace(":", "-")	+ "_"+parentId
						+ ".xlsx";
				
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					
					if(isSendMail){
						String sendemail = "";
						for (String emaString : mailSendInfo.get(j).getEmailIds()) {
							sendemail = sendemail + "," + emaString.trim();
						}
						SendEmail mail = new SendEmail();
						mail.sendDeviceExceptionMailToJaipur(sendemail, file,
								mailSendInfo.get(j).getDept(),
								"Exception Trip Report of PatrolMan",
								"Exception Trip Report of PatrolMan", false
								,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());


					}
				
			} catch (Exception e) {

					e.printStackTrace();
				}

			}

		// /////ALl Section////////////////////////

			if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
					KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1){
				
			
			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReport);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			cell = row.createCell(1);
			cell.setCellValue(KeyManLowBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight
					.add((short) ((KeyManLowBattery_Set.toString()
							.replace("[", "").replace("]", "").toString()
							.length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(KeyManOffDevice_Set.toString().replace("[", "")
					.replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOffDevice_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));
			
			
			
			cell = row.createCell(6);
			cell.setCellValue(KeyMan60PercentBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); 
			
			short height_row = (short) ((KeyManLowBattery_Set.toString()
					.replace("[", "").replace("]", "").toString().length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);
			System.err.println("Row max height-----" + height_row);

			sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

			if (j == mailSendInfo.size() - 1) {
				String outFileNameAllSectionStatus = "All Section PartolMen status Report_"
						+ Common.getDateFromLong(startdate).replace(":", "-")	+ "_"+parentId
						+ ".xlsx";
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReport.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReport.close();
		
					if (isSendMail) {
						 String sendemail =loginObj. getEmailIdForAllsectionReport(con,parentId);
							SendEmail mail = new SendEmail();
							System.err.println("Send mail All Section----" + sendemail);
								mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
										  "All Section"," PartolMen Work Status Report AllSection",
										  " PartolMen Work Status Report AllSection",true
											,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

						
					}
					
							
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}

			// /////////////////////*Alll Section////////////////////
			
			
			
			// /////ALl Section COunt////////////////////////
			
			
			 divDeviceOffCount=divDeviceOffCount+KeyManOffDevice_Set.size();
			 divDeviceBeatNotCoverCount=divDeviceBeatNotCoverCount+KeyManExceptionalDevices_Set.size();
			 divDeviceBeatCoverCount=divDeviceBeatCoverCount+KeyManCoverSucefullyDevices_Set.size();

			

			 if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
						KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1){
				 
			
			row = sheetPatrolmanWorkStatusReportCount
					.createRow(sheetPatrolmanWorkStatusReportCount
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	

			cell = row.createCell(1);
			cell.setCellValue(KeyManOffDevice_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	
			cell = row.createCell(2);
			cell.setCellValue(KeyManExceptionalDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			

			cell = row.createCell(3);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

			cell = row.createCell(4);
			cell.setCellValue((KeyManCoverSucefullyDevices_Set.size()+KeyManOffDevice_Set.size()+
					KeyManExceptionalDevices_Set.size())+"");
			
			
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			
	
			
			row.setHeight((short) 600);

			sheetPatrolmanWorkStatusReportCount.setColumnWidth(0, 3000);
			sheetPatrolmanWorkStatusReportCount.setColumnWidth(1, 3000);
			sheetPatrolmanWorkStatusReportCount.setColumnWidth(2, 3000);
			sheetPatrolmanWorkStatusReportCount.setColumnWidth(3, 3000);
			sheetPatrolmanWorkStatusReportCount.setColumnWidth(4, 3000);

			if (j == mailSendInfo.size() - 1) {
				
				//for total row
				row = sheetPatrolmanWorkStatusReportCount
						.createRow(sheetPatrolmanWorkStatusReportCount
								.getLastRowNum() + 1);
				row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				cell = row.createCell(0);
				cell.setCellValue("Total Device");

				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

				cell = row.createCell(1);
				cell.setCellValue(divDeviceOffCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				;

				cell = row.createCell(2);
				cell.setCellValue(divDeviceBeatNotCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				
				cell = row.createCell(3);
				cell.setCellValue(divDeviceBeatCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				

				cell = row.createCell(4);
				cell.setCellValue((divDeviceOffCount+divDeviceBeatCoverCount+
						divDeviceBeatNotCoverCount)+"");
				
				
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);


				
				row.setHeight((short) 600);

				sheetPatrolmanWorkStatusReportCount.setColumnWidth(0, 3000);
				sheetPatrolmanWorkStatusReportCount.setColumnWidth(1, 3000);
				sheetPatrolmanWorkStatusReportCount.setColumnWidth(2, 3000);
				sheetPatrolmanWorkStatusReportCount.setColumnWidth(3, 3000);
				sheetPatrolmanWorkStatusReportCount.setColumnWidth(4, 3000);
				
				String outFileNameAllSectionStatus = "All Section PatrolMan status device count Report_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_"+parentId+ ".xlsx";
				
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookPatrolmanWorkStatusReportCount.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookPatrolmanWorkStatusReportCount.close();

					String sendemail =loginObj. getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					/*if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," PatrolMan Work Status device count Report AllSection",
								  " PatrolMan Work Status device count Report AllSection",true	
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					}*/
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			

				}
			 }

			// /////////////////////*Alll Section COunt end ////////////////////
			
			
			///Insert Datewise Saction Data
	if (isSendMail) 
	try {

					// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
					java.sql.CallableStatement ps = con
							.prepareCall("{call SaveDatewiseExceptionReport(?,?,?,?,?,?,?,?)}");

					
					
					ps.setInt(1, Integer.parseInt(parentId));
					ps.setString(2,DayStartWorkTime+"");
					ps.setString(3, KeyManLowBattery_Set.toString().replace("[", "")
							.replace("]", ""));

					ps.setString(4,KeyManOffDevice_Set.toString()
							.replace("[", "").replace("]", ""));

					ps.setString(5, KeyManExceptionalDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(6, KeyManOverspeedDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(7,mailSendInfo.get(j).getDept() );
					ps.setString(8, KeyManCoverSucefullyDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					int result = ps.executeUpdate();
					if (result == 0) {
						msgo.setError("true");
						msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
					} else {
						// //System.err.println("Error=="+result);
						msgo.setError("false");
						msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
				}
			}
		//}

		return msgo;

	}





	public MessageObject GenerateExceptionForGateMitra(Connection con,
			DB mongoconnection, String parentId, int day, Boolean isSendMail,
			long startTime1, long startTime2, long endTime1, long endTime2,
			String nameStartWith, Double distancetoleranceInKm,
			Connection conSunMssql) {


		MessageObject msgo = new MessageObject();
		LoginDAO loginObj=new LoginDAO();
		ArrayList<RailMailSendInfoDto> mailSendInfo = loginObj.getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionForGateMitra ---"
				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-day;
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		

		// ///////////////////////////////********************************/////////////////////////////
		// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("GateMitra Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"GateMitra Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						6));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 800);


		// ///////////////////////////////////All Section
		// End//////////////////////////////////////////////////////////
		
	
			
				int divDeviceOffCount=0;
				int divDeviceBeatNotCoverCount=0;
				int divDeviceBeatCoverCount=0;

		for (int j = 0; j < mailSendInfo.size(); j++) {
//		for (int j = 1; j<=1 ;j++) {
//
			
			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentId", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report of GateMitra"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyle.setAlignment(HorizontalAlignment.CENTER);
			font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);

			CellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap			

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for GateMitra of section "
							+ mailSendInfo.get(j).getDept() + "  Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyle);
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 3));
			
		
			
			
			
			for (int i = 0; i < deviceInfoList.size(); i++) {
//				 for (int i = 3; i <=3; i++) {

				System.err.println(" **Device =---================"+ deviceInfoList.get(i).getName() + "\n");

				Double startBatteryStatus = loginObj.getKeyManStartBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+startTime1);

				Double endBatteryStatus = loginObj.getKeyManEndBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+endTime2);

				if (deviceInfoList.get(i).getName().startsWith(nameStartWith)) {
//				if (deviceInfoList.get(i).getName().startsWith("K/")&&deviceInfoList.get(i).getName().contains("-013")){
					ArrayList<ReportAnalysisDTO> listObjects=new ArrayList<>();

					BasicDBObject subdocument = new BasicDBObject();

					RailwayKeymanTripsBeatsList
							.removeAll(RailwayKeymanTripsBeatsList);
					exceptiionalTrip.removeAll(exceptiionalTrip);
					
					
					
					
					row = sheet.createRow(sheet.getLastRowNum() + 1);
					row = sheet.createRow(sheet.getLastRowNum() + 1);
					row.createCell(0);
					row.getCell(0).setCellValue(
							"Exception Report for "
									+ deviceInfoList.get(i).getName() + "("
									+ deviceInfoList.get(i).getDeviceID()
									+ ")");
					row.getCell(0).setCellStyle(HeadingStyle);

					sheet.addMergedRegion(new CellRangeAddress(sheet
							.getLastRowNum(), sheet.getLastRowNum(), 0, 4));

					row = sheet
							.createRow(sheet.getLastRowNum() + 1);

					 cell = row.createCell(0);

					cell.setCellValue("Status");
					cell.setCellStyle(tripColumnStyle);
					cell = row.createCell(1);
					cell.setCellValue("Time");
					cell.setCellStyle(tripColumnStyle);
					cell = row.createCell(2);
					cell.setCellValue("Allocated Point/LC");
					cell.setCellStyle(tripColumnStyle);
					cell = row.createCell(3);
					cell.setCellValue("Location");
					cell.setCellStyle(tripColumnStyle);
					cell = row.createCell(4);
					cell.setCellValue("Distance");
					cell.setCellStyle(tripColumnStyle);
					
					try {

						java.sql.CallableStatement psgettrip = con
								.prepareCall("{call GetTripSheduleOfKeyman(?)}");

						psgettrip.setInt(1, deviceInfoList.get(i)
								.getStudentId());

						ResultSet rsgertrip = psgettrip.executeQuery();
						if (rsgertrip != null) {
							while (rsgertrip.next()) {

								RailwayPetrolmanTripsBeatsDTO dto = new RailwayPetrolmanTripsBeatsDTO();
								dto.setId(rsgertrip.getInt("Id"));

								dto.setStudentId(rsgertrip.getInt("StudentId"));
								dto.setDeviceId(rsgertrip.getString("DeviceID"));

								dto.setKmStart(rsgertrip.getDouble("KmStart"));
								dto.setKmEnd(rsgertrip.getDouble("KmEnd"));

								dto.setSectionName(rsgertrip
										.getString("SectionName"));

								dto.setKmStartLat((rsgertrip
										.getDouble("kmStartLat")));
								dto.setKmStartLang(rsgertrip
										.getDouble("kmStartLang"));
								dto.setKmEndLat(rsgertrip.getDouble("kmEndLat"));
								dto.setKmEndLang(rsgertrip
										.getDouble("kmEndLang"));
								

		
				
						DBCollection table = mongoconnection
								.getCollection(Common.TABLE_LOCATION);
						BasicDBObject device_whereQuery = new BasicDBObject();
						device_whereQuery.put("device", Long
								.parseLong(dto.getDeviceId()));

												BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
								"timestamp", new BasicDBObject("$gte",
										DayStartWorkTime + startTime1).append("$lt",
										DayStartWorkTime + endTime2));
//					
						BasicDBList morn_Milage = new BasicDBList();
						morn_Milage.add(timestamp_whereQuery_morning);

						morn_Milage.add(device_whereQuery);
						DBObject morn_Milage_query = new BasicDBObject("$and",
								morn_Milage);

//						
						DBCursor cursor = table.find(morn_Milage_query);
						cursor.sort(new BasicDBObject("timestamp", 1));

						cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

						System.err.println("Cursorlocation  Count of u-p11----"
								+ cursor.size() + "  " + morn_Milage_query);
						// long i = 0;
						long total = cursor.size();
						Double MaxSpeed = 0.0;
						Double TotalSpeed = 0.0;
						int Triplocationcount = 0;
					 listObjects .clear();
						if (cursor.size() > 0) {
							
							while (cursor.hasNext()) {
								DBObject dbObject = cursor.next();
								DBObject dbObject_location = (DBObject) dbObject
											.get("location");
							ReportAnalysisDTO obj = new ReportAnalysisDTO();
							obj.setDeviceIMEI(dto.getDeviceId());
							obj.setDeviceLocationLat((Double) dbObject_location.get("lat"));
							obj.setDeviceLocationLan((Double) dbObject_location.get("lon"));
							obj.setDeviceLocationTimestamp(Long.parseLong(dbObject.get("timestamp")+""));
							obj.setDistDiffbetRDPSAndLocation(loginObj.distance(obj.getDeviceLocationLat(), obj.getDeviceLocationLan()
									, dto.getKmStartLat(), dto.getKmStartLang(), "K")*1000);
							
							
							System.out.println("---distcalulated------"+obj.getDistDiffbetRDPSAndLocation());
							listObjects.add(obj);
							}

						} else 
							System.out
									.println(" *********Deviceloctaion not found*********************-----sheet.getLastRowNum()---------------"
											+ sheet.getLastRowNum());

						String status="Na";

						DBCollection report_gatemitra_table = mongoconnection
								.getCollection(Common.TABLE_GATEMITRAREPORT);

					
						if (listObjects.size()>0) 
						{
							Long prevTimestamp = (long) 0;
							for (int l= 0;l < listObjects.size();l++) 
							{
								
								ReportAnalysisDTO dbObject =listObjects.get(l);
								BasicDBObject mainDoc =null;

								if (status.equalsIgnoreCase("Na")&&dbObject.getDistDiffbetRDPSAndLocation()<(distancetoleranceInKm*1000)) 
								{ 
									mainDoc=new BasicDBObject();
									mainDoc.put("timestamp", dbObject.getDeviceLocationTimestamp());
									mainDoc.put("lat",dbObject.getDeviceLocationLat());
									mainDoc.put("lan",dbObject.getDeviceLocationLan());
									mainDoc.put("device", dbObject.getDeviceIMEI());
									mainDoc.put("parentId", parentId);
									mainDoc.put("section", mailSendInfo.get(j).getDept());
									mainDoc.put("inOutStatus", "In");
									mainDoc.put("time",Common.getDateCurrentTimeZone(dbObject.getDeviceLocationTimestamp()));
									mainDoc.put("distance", dbObject.getDistDiffbetRDPSAndLocation());

									status="In";
									prevTimestamp= dbObject.getDeviceLocationTimestamp();
									
								}else if (status.equalsIgnoreCase("In")&&dbObject.getDistDiffbetRDPSAndLocation()>(distancetoleranceInKm*1000))
										//&&(dbObject.getDeviceLocationTimestamp()-prevTimestamp)>900)
								{		

									mainDoc=new BasicDBObject();

									mainDoc.put("timestamp", dbObject.getDeviceLocationTimestamp());
									mainDoc.put("lat",dbObject.getDeviceLocationLat());
									mainDoc.put("lan",dbObject.getDeviceLocationLan());
									mainDoc.put("device", dbObject.getDeviceIMEI());
									mainDoc.put("parentId", parentId);
									mainDoc.put("section", mailSendInfo.get(j).getDept());
									mainDoc.put("inOutStatus", "Out");
									mainDoc.put("time",Common.getDateCurrentTimeZone(dbObject.getDeviceLocationTimestamp()));
									mainDoc.put("distance", dbObject.getDistDiffbetRDPSAndLocation());

									status="Out";
								}else if (status.equalsIgnoreCase("Out")&&dbObject.getDistDiffbetRDPSAndLocation()<(distancetoleranceInKm*1000))
								{
									mainDoc=new BasicDBObject();

									mainDoc.put("timestamp", dbObject.getDeviceLocationTimestamp());
									mainDoc.put("lat",dbObject.getDeviceLocationLat());
									mainDoc.put("lan",dbObject.getDeviceLocationLan());
									mainDoc.put("device", dbObject.getDeviceIMEI());
									mainDoc.put("parentId", parentId);
									mainDoc.put("section", mailSendInfo.get(j).getDept());
									mainDoc.put("inOutStatus", "In");
									mainDoc.put("time",Common.getDateCurrentTimeZone(dbObject.getDeviceLocationTimestamp()));
									mainDoc.put("distance", dbObject.getDistDiffbetRDPSAndLocation());

									status="In";
									prevTimestamp= dbObject.getDeviceLocationTimestamp();

								}
								
								
								if (mainDoc!=null&&!status.equalsIgnoreCase("Na"))					
								{
									report_gatemitra_table.insert(mainDoc);	
							
									row = sheet
											.createRow(sheet.getLastRowNum() + 1);
									cell = row.createCell(0);
									cell.setCellValue(status);
									cell = row.createCell(1);
									cell.setCellValue(Common.getDateCurrentTimeZone(dbObject.getDeviceLocationTimestamp()));
									cell = row.createCell(2);
									cell.setCellValue("LC No : "+dto.getKmStart());
									cell = row.createCell(3);
									CreationHelper createHelper = workbook.getCreationHelper();

									 //URL Link
								      cell.setCellValue("https://www.google.com/maps/place/"+dbObject.getDeviceLocationLat()+","+dbObject.getDeviceLocationLan());
								      XSSFHyperlink link = (XSSFHyperlink)createHelper.createHyperlink(Hyperlink.LINK_URL);
								      link.setAddress("https://www.google.com/maps/place/"+dbObject.getDeviceLocationLat()+","+dbObject.getDeviceLocationLan());
								      cell.setHyperlink((XSSFHyperlink) link);
								      cell = row.createCell(4);
									  cell.setCellValue(dbObject.getDistDiffbetRDPSAndLocation()+" m");
									  
								}else if(status.equalsIgnoreCase("Na")&&l==listObjects.size()-1){
									

									mainDoc=new BasicDBObject();

								
									mainDoc.put("device", dto.getDeviceId());
									mainDoc.put("parentId", parentId);
									mainDoc.put("section", mailSendInfo.get(j).getDept());
									mainDoc.put("inOutStatus", "Device Off");
									if (mainDoc!=null)					
										{
										report_gatemitra_table.insert(mainDoc);
										row = sheet
												.createRow(sheet.getLastRowNum() + 1);

										 cell = row.createCell(0);

										cell.setCellValue("Remark : Device is moved on Other than expected Point.");
										cell.setCellStyle(remarkColumnStyle);
										sheet.addMergedRegion(new CellRangeAddress(sheet
												.getLastRowNum(), sheet.getLastRowNum(), 0, 4));
										}
									
									
								}
							}
							
						}else{
							BasicDBObject mainDoc=new BasicDBObject();

						
							mainDoc.put("device", dto.getDeviceId());
							mainDoc.put("parentId", parentId);
							mainDoc.put("section", mailSendInfo.get(j).getDept());
							mainDoc.put("inOutStatus", "Device Off");
							if (mainDoc!=null)					
								{
								report_gatemitra_table.insert(mainDoc);
								row = sheet
										.createRow(sheet.getLastRowNum() + 1);

								 cell = row.createCell(0);

								cell.setCellValue("Remark : Device is Off.");
								cell.setCellStyle(remarkColumnStyle);
								sheet.addMergedRegion(new CellRangeAddress(sheet
										.getLastRowNum(), sheet.getLastRowNum(), 0, 4));
								
								}
							}
						
							
						
						
							}
						}
						
						sheet.setColumnWidth(0, 3000);
						sheet.setColumnWidth(1, 3000);
						sheet.setColumnWidth(2, 5000);

						sheet.setColumnWidth(3, 15000);

				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				}
					
			}
		
			///save sheet in to excel
			if (deviceInfoList.size() > 0) {
				String outFileName = mailSendInfo.get(j).getDept().replace("/", "_") + "_"
						+ "Exception Trip Report GateMitra "
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")	+ "_"+parentId
						+ ".xlsx";
				
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_PETROLMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					
					if(isSendMail){
						String sendemail = "";
						for (String emaString : mailSendInfo.get(j).getEmailIds()) {
							sendemail = sendemail + "," + emaString.trim();
						}
						SendEmail mail = new SendEmail();
						mail.sendDeviceExceptionMailToJaipur(sendemail, file,
								mailSendInfo.get(j).getDept(),
								"Exception Trip Report of GateMitra",
								"Exception Trip Report of GateMitra", false
								,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());


					}
				
			} catch (Exception e) {

					e.printStackTrace();
				}

			}
		}

		return msgo;

	
	}




/*

	public MessageObject DumpLocationDataToSql(Connection con,
			DB mongoconnection, int parentId, int day2, long startTime,
			long endTime, Connection conSunMssql) {
		
		MessageObject	msgo=new MessageObject();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-day2;
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		
		long startTimeDb = 0,endTimeDb=0;
		StringBuilder DeviceList = new StringBuilder();
		ArrayList<Long>DeviceListLong=new ArrayList<>();
		try {

			java.sql.CallableStatement psgettrip = con
					.prepareCall("{call GetDeviceListForLocationDump(?)}");

			psgettrip.setInt(1, parentId);

			ResultSet rs = psgettrip.executeQuery();
			if (rs != null) {
				while (rs.next()){
					if (startTimeDb==0)
					{
						startTimeDb=rs.getInt("StartTime");
						endTimeDb=rs.getInt("EndTime");
					}
					
					DeviceListLong.add(rs.getLong("DeviceID"));
					DeviceList.append(rs.getLong("DeviceID")).append(",");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("DeviceListLong----"+startTimeDb+"---"+endTimeDb+"----"+new Gson().toJson(DeviceListLong));
		

			  
			DBCollection table = mongoconnection
					.getCollection(Common.TABLE_LOCATION);
			BasicDBObject device_whereQuery = new BasicDBObject();
			device_whereQuery.put("device", new BasicDBObject("$in",DeviceListLong ));


			BasicDBObject timestamp_whereQuery_morning = new BasicDBObject(
					"timestamp", new BasicDBObject("$gte",
							DayStartWorkTime + startTime).append("$lt",
							DayStartWorkTime + endTime));
//		

			BasicDBList morn_Milage = new BasicDBList();
			morn_Milage.add(timestamp_whereQuery_morning);
			morn_Milage.add(device_whereQuery);
			
			DBObject morn_Milage_query = new BasicDBObject("$and",
					morn_Milage);

//			
			DBCursor cursor = table.find(timestamp_whereQuery_morning);
			cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);

			System.err.println("Cursorlocation  Count of u-p11----"
					+ cursor.size() + "  " + timestamp_whereQuery_morning);
			
		
			DecimalFormat df = new DecimalFormat("00.000000"); 
			StringBuilder LatString = new StringBuilder(),LanString= new StringBuilder(),
					TimestampString= new StringBuilder(),SpeedString= new StringBuilder();
			if (cursor.size() != 0) {

				for (DBObject dbObject : cursor) {
					
					
//					insertTodayLoaction(conSunMssql,dbObject);

					ReportAnalysisDTO obj = new ReportAnalysisDTO();
					DBObject dbObject_location = (DBObject) dbObject
							.get("location");
					LatString.append(df.format(dbObject_location.get("lat"))).append(",");
					LanString.append(df.format(dbObject_location.get("lon"))).append(",");
					TimestampString.append(dbObject.get("timestamp")).append(",");
					SpeedString.append(dbObject.get("speed")).append(",");
					
				}
			}

			
			//insert  Locatio in sql table
			try {

				java.sql.CallableStatement psLoc = conSunMssql
						.prepareCall("{call SaveDumpedLocationInMSSQL(?,?,?,?,?,?)}");
				psLoc.setString(1, LatString.toString());
				psLoc.setString(2, LanString.toString());
				psLoc.setString(3, TimestampString.toString());
				psLoc.setString(4,SpeedString.toString());
				psLoc.setString(5,DeviceList.toString());
				psLoc.setInt(6,parentId);
				
				System.out.println("LatString---"+LatString);
				System.out.println("LanString---"+LanString);
				System.out.println("TimestampString---"+TimestampString);
				System.out.println("DeviceList---"+DeviceList);

				int result = psLoc.executeUpdate();
				if (result == 0) {
					msgo.setError("true");
					msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
				} else {
					// //System.err.println("Error=="+result);
					msgo.setError("false");
					msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
				}
			
			}catch(Exception e){
				e.printStackTrace();
			}
		
		return msgo;
		
		
	}	*/
	
	
	
	public MessageObject DumpLocationDataToSqlThroughCSV(Connection con,
			DB mongoconnection, int day2, long startTime,
			long endTime, Connection conSunMssql,boolean isDumpMOngoDbToCsv,boolean isCsvToSql,boolean isMappedRdps) {
		
		MessageObject	msgo=new MessageObject();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-day2;
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));

		
		long startTimeDay=DayStartWorkTime+startTime;
		long endTimeDay=DayStartWorkTime+endTime;
		
		//Getlast updtaed time from sql meta data 
		if (startTime==0&&endTime==0) {
			try {
				java.sql.CallableStatement psStart = con
						.prepareCall("{call GetMongoLocationSyncTime(?)}");
				psStart.registerOutParameter(1, Types.INTEGER);
				
				psStart.executeUpdate();
				
				startTimeDay=psStart.getInt(1);
				endTimeDay=System.currentTimeMillis()/1000;;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		//---------------------Make CSv dump from Mongo Db Start-------------------------------
		

      
        //Production
//        String fileName = "/home/prime/MongoToSqlDumpData/export.csv";
	        

        String db = "tracking";
        String Host = "114.143.99.170";
//        String Host = "localhost";
        String Port = "19209";
        System.err.println("STart--"+startTimeDay+"----Endtime-"+endTimeDay);

        String command = "/usr/bin/mongoexport -h " + Host + " --port " + Port + " -d " + db + " -c " + Common.TABLE_LOCATION + " --type=csv --query {timestamp:{$gte:"+startTimeDay+",$lte:"+endTimeDay+"}}"+" -f  device,location.lat,location.lon,speed,timestamp,satellite_no -o " + Common.dbDumpfileName + "";

	    	
	
	  	        if(isDumpMOngoDbToCsv)	      
	        try {
	            System.out.println(command);
	            Process process = Runtime.getRuntime().exec(command);
	            int waitFor = process.waitFor();
	            System.out.println("waitFor:: " + waitFor);
	            BufferedReader success = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

	            String s = "";
	            while ((s = success.readLine()) != null) {
	                System.out.println(s);
	            }

	            while ((s = error.readLine()) != null) {
	                System.out.println("Std ERROR : " + s);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    	//---------------------Make CSv dump from Mongo Db End-------------------------------
	        
	        
	    	//---------------------CSv dump to SQL Db Start-------------------------------
	        if(isCsvToSql)	     
	        try {

				java.sql.CallableStatement psLoc = conSunMssql
						.prepareCall("{call SaveCSVDumpedInMSSQL()}");

				int result = psLoc.executeUpdate();
				if (result == 0) {
					System.err.println("SaveCSVDumpedInMSSQL error ");

					msgo.setError("true");
					msgo.setMessage("DumpLocationDataToSqlThroughCSV not Inserted");
				} else {
					System.err.println("SaveCSVDumpedInMSSQL done");
					msgo.setError("false");
					msgo.setMessage("DumpLocationDataToSqlThroughCSV Inserted Successfully");
				}
			
			}catch(Exception e){
				e.printStackTrace();
			}
	        
	        
	    	//---------------------CSv dump to SQL Db end-------------------------------
	        
	        
	        //---------------------Mapped RDPS to every Loaction start-------------------------------
	        if(isMappedRdps)	 
	        try {

				java.sql.CallableStatement psLoc = conSunMssql
						.prepareCall("{call SaveLocationWithMappedNearestRdps()}");

				int result = psLoc.executeUpdate();
				if (result == 0) {
					System.err.println("SaveLocationWithMappedNearestRdps error ");

					msgo.setError("true");
					msgo.setMessage("DumpLocationDataToSqlThroughCSV a not Inserted");
				} else {
					System.err.println("SaveLocationWithMappedNearestRdps done ");
					msgo.setError("false");
					msgo.setMessage("DumpLocationDataToSqlThroughCSV  a Inserted Successfully");
				}
			
			}catch(Exception e){
				e.printStackTrace();
			}
	        
	        
	        
	        
	    	//---------------------Mapped RDPS to every Loaction  end-------------------------------
	        
	        //--------update last updtaed time from sql meta data 
	   
			if (startTime==0&&endTime==0) {
				try {
					java.sql.CallableStatement psStart = con
							.prepareCall("{call UpdateMongoLocationSyncTime(?)}");
					psStart.setLong(1,endTimeDay);
					
					psStart.executeUpdate();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			

		return msgo;
	}
	
	
	public MessageObject GenerateExceptionReportKotaKeyManFromRdpsMapping(Connection con,
			DB mongoconnection, String parentId, int day2, Boolean isSendMail, long startTime1, long startTime2, long endTime1, long endTime2, Boolean exceptionSummary, Double distancetoleranceInKm, Connection conSunMssql) 
	{
		MessageObject msgo=new MessageObject();
		LoginDAO loginObj=new LoginDAO();
//		ArrayList<RailMailSendInfoDto> mailSendInfo=loginObj.getSentMailInfoList(con,parentId);	
//		System.err.println("GenerateExceptionReportKotaKeyManFromRdpsMapping---"
//				+ new Gson().toJson(mailSendInfo));
		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-day2;
	
			long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
			////////////////////////////////////////// exception Summary////////////////////////
			
			
			XSSFWorkbook workbookSummary = new XSSFWorkbook();
			XSSFSheet sheetSummary = workbookSummary.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report Summary of keymen"));

			XSSFCellStyle HeadingStyleSummary = workbookSummary.createCellStyle();
			HeadingStyleSummary.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyleSummary.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyleSummary.setAlignment(HorizontalAlignment.CENTER);
			XSSFFont fontSummary = workbookSummary.createFont();
			XSSFFont font = workbookSummary.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyleSummary.setFont(font);

			XSSFCellStyle tripColumnStyleSummary = workbookSummary.createCellStyle();
			tripColumnStyleSummary
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyleSummary.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyleSummary.setWrapText(true);
			font = workbookSummary.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyleSummary = workbookSummary.createCellStyle();
			remarkColumnStyleSummary.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyleSummary.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyleSummary = workbookSummary.createCellStyle();
			error_remarkColumnStyleSummary
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyleSummary
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfontSummary = workbookSummary.createFont();
			redfontSummary.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyleSummary.setFont(redfontSummary);

			// loactionNotFoundColumnStyle.setFont(font);

			CellStyle wrap_styleSummary = workbookSummary.createCellStyle(); // Create new
																// style
			wrap_styleSummary.setWrapText(true); // Set wordwrap

			int petrolMancount = 0;

			 XSSFRow row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for Keyman"
							 + " Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyleSummary);
			
			sheetSummary.addMergedRegion(new CellRangeAddress(sheetSummary.getLastRowNum(),
					sheetSummary.getLastRowNum(), 0, 7));

			BasicDBList sectionDeviceList = new BasicDBList();
			
			row = sheetSummary.createRow(0);
			row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);



			XSSFCell cell = row.createCell(0);
			cell.setCellValue("Sr no");
			cell.setCellStyle(tripColumnStyleSummary);
		/*	
			 cell = row.createCell(1);
			cell.setCellValue("Name");
			cell.setCellStyle(tripColumnStyle);*/

			cell = row.createCell(1);
			cell.setCellValue("Device name");
			cell.setCellStyle(tripColumnStyleSummary);

			cell = row.createCell(2);
			cell.setCellValue("Late On Track >15min");
			cell.setCellStyle(tripColumnStyleSummary);
			
			cell = row.createCell(3);
			cell.setCellValue("Early Off Track >15 min");
			cell.setCellStyle(tripColumnStyleSummary);

			cell = row.createCell(4);
			cell.setCellValue("Overspeed>8 kmph");
			cell.setCellStyle(tripColumnStyleSummary);
			
			cell = row.createCell(5);
			cell.setCellValue("Stoppage >60 min. No of stoppage at location with railways geo fence");
			cell.setCellStyle(tripColumnStyleSummary);
			
			cell = row.createCell(6);
			cell.setCellValue("Trip not completed");
			cell.setCellStyle(tripColumnStyleSummary);
			
			cell = row.createCell(7);
			cell.setCellValue("Travelled km, 500 m short from alloted km");
			cell.setCellStyle(tripColumnStyleSummary);
			row.setHeight((short) 1200);
			
		
			
			////////////////////////////////////report Summary//////////////////////////////

		
//		for (int j = 0; j < mailSendInfo.size(); j++) {
//			for (int j =1; j <=1;j++) {
			 int totalTrip=0;
			ArrayList<Short> rowHeight = new ArrayList<>();

//			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j).getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();


//			for (int i = 0; i < deviceInfoList.size(); i++) {
				// for (int i = 4; i <=4; i++) {
		
								//put logic for usfd trip
//				if (deviceInfoList.get(i).getName().startsWith("K/")) {
//				if (deviceInfoList.get(i).getName().contains("049")) {
			
				
					ArrayList<ReportAnalysisDTO> LocationWithDistList=new ArrayList<>();
					RailwayPetrolmanTripsBeatsDTO dailySummaryObj = new RailwayPetrolmanTripsBeatsDTO();

//					System.err.println("---Device---"+deviceInfoList.get(i).getName()+"----"+deviceInfoList.get(i).getDeviceID());
					

//					System.err.println("RailwayKeymanTripsBeatsList--size---"+RailwayKeymanTripsBeatsList.size());
//					System.err.println(" **Device =---================"
//							+ deviceInfoList.get(i).getName() + "\n");
					

//					
				
	
//					row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
//					

					ArrayList<DailyReportSummaryWithStoppageDTO> deviceReportList=new ArrayList<>();

					try {

					java.sql.CallableStatement psgettrip = conSunMssql
							.prepareCall("{call GetDailySummaryOfDeviceFromReportSummary(?,?)}");
				
					psgettrip.setInt(1,Integer.parseInt(parentId));
					psgettrip.setLong(2,DayStartWorkTime);
				

					ResultSet rsgertrip = psgettrip.executeQuery();
					System.err.println("DayStartWorkTime---"+DayStartWorkTime);
					if (rsgertrip != null) {
						while (rsgertrip.next()) {

							DailyReportSummaryWithStoppageDTO dto = new DailyReportSummaryWithStoppageDTO();
							dto.setDeviceEndKm(rsgertrip.getDouble("DeviceEndKm"));
							dto.setDeviceEndTime(rsgertrip.getLong("DeviceEndTime"));
							dto.setDeviceId(rsgertrip.getString("DeviceId"));
							dto.setDeviceOvespeedCount(rsgertrip.getInt("DeviceOvespeedCount"));
							dto.setDeviceStartKm(rsgertrip.getDouble("DeviceStartKm"));
							dto.setDeviceStartTime(rsgertrip.getLong("DeviceStartTime"));
							dto.setDeviceStoppageCount(rsgertrip.getInt("DeviceStoppageCount"));
							dto.setExpectedEndKm(rsgertrip.getDouble("ExpectedEndKm"));
							dto.setExpectedEndTime(rsgertrip.getLong("ExpectedEndTime"));
							dto.setExpectedStartKm(rsgertrip.getDouble("ExpectedStartKm"));
							dto.setExpectedStartTime(rsgertrip.getLong("ExpectedStartTime"));
							dto.setName(rsgertrip.getString("Name"));
							dto.setReportOfDay(DayStartWorkTime);
							dto.setMaxSpeed(rsgertrip.getInt("MaxSpeed"));
							dto.setAvgSpeed(rsgertrip.getInt("AvgSpeed"));
							dto.setLocationCount(rsgertrip.getInt("LocationCount"));
							dto.setEndTimeDiff(rsgertrip.getInt("EndTimeDiff"));
							dto.setStartTimeDiff(rsgertrip.getInt("StartTimeDiff"));
							dto.setActualKmCover(rsgertrip.getDouble("ActualKmCover"));
							dto.setExpectedKmCover(rsgertrip.getDouble("ExpectedKmCover"));

							
							deviceReportList.add(dto);
					
						
						}
					}
					System.out.println("Result Daily Report---"+new Gson().toJson(dailySummaryObj));

				} catch (Exception e) {
					e.printStackTrace();
				}
					
		
					
				for (DailyReportSummaryWithStoppageDTO Obj : deviceReportList) {
						
					
           
						System.err.println("dailySummaryObj---"+new Gson().toJson(Obj));
            
           
				if (Obj.getLocationCount()==0)
				{
            
					XSSFRow rowSummary = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
	
					Cell cellSummary = rowSummary.createCell(0);
					cellSummary.setCellValue(sheetSummary.getLastRowNum()-2);
					cellSummary.setCellStyle(wrap_styleSummary);
					
					cellSummary = rowSummary.createCell(1);
					cellSummary.setCellValue(Obj.getName());
					cellSummary.setCellStyle(wrap_styleSummary);
					
					cellSummary = rowSummary.createCell(2);
					cellSummary.setCellValue("Device was off for a day.");
					cellSummary.setCellStyle(error_remarkColumnStyleSummary);
					sheetSummary.addMergedRegion(new CellRangeAddress(sheetSummary.getLastRowNum(),
							sheetSummary.getLastRowNum(), 2, 7));
					
            }else if(Obj.getLocationCount()>0&&Obj.getExpectedStartKm()==-1||Obj.getExpectedEndKm()==-1){
            	XSSFRow rowSummary = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            	
				Cell cellSummary = rowSummary.createCell(0);
				cellSummary.setCellValue(sheetSummary.getLastRowNum()-2);
				cellSummary.setCellStyle(wrap_styleSummary);
				
				cellSummary = rowSummary.createCell(1);
				cellSummary.setCellValue(Obj.getName());
				cellSummary.setCellStyle(wrap_styleSummary);
				
				cellSummary = rowSummary.createCell(2);
				cellSummary.setCellValue("Yet Trip not sheduled for this device.please share route detils to admin.");
				cellSummary.setCellStyle(error_remarkColumnStyleSummary);
				sheetSummary.addMergedRegion(new CellRangeAddress(sheetSummary.getLastRowNum(),
						sheetSummary.getLastRowNum(), 2, 7));
            }
            else{

//				double kmExpected=Math.abs(Obj.getExpectedEndKm()-Obj.getExpectedStartKm());
//				double kmActualCover=Math.abs(Obj.getDeviceEndKm()-Obj.getDeviceStartKm());
//				long startTimeDiff=Obj.getDeviceStartTime()-Obj.getExpectedStartTime();
//				long endTimeDiff=Obj.getExpectedEndTime()-Obj.getDeviceEndTime();
				
				if(Obj.getStartTimeDiff()>900 ||Obj.getEndTimeDiff()>900||Obj.getDeviceOvespeedCount()>5
						||(Obj.getExpectedKmCover()-Obj.getActualKmCover())*1000>500)
				{
					try {

						XSSFRow rowSummary = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);

						 Cell cellSummary = rowSummary.createCell(0);
						cellSummary.setCellValue(sheetSummary.getLastRowNum()-2);
						cellSummary.setCellStyle(wrap_styleSummary);
						
						cellSummary = rowSummary.createCell(1);
						cellSummary.setCellValue(Obj.getName());
						cellSummary.setCellStyle(wrap_styleSummary);

					
						

						cellSummary = rowSummary.createCell(2);
						if (Obj.getStartTimeDiff()>900)
							cellSummary.setCellValue(Common.getDateCurrentTimeZone(Obj.getDeviceStartTime()));
						else cellSummary.setCellValue("-");



						cellSummary = rowSummary.createCell(3);
						if (Obj.getEndTimeDiff()>900)
							cellSummary.setCellValue(Common.getDateCurrentTimeZone(Obj.getDeviceEndTime()));
						else cellSummary.setCellValue("-");
						cellSummary.setCellStyle(wrap_styleSummary);

							
						


						cellSummary = rowSummary.createCell(4);
						if (Obj.getDeviceOvespeedCount()>5)
							cellSummary.setCellValue("Found in OverSpeed");
						else cellSummary.setCellValue("-");
						cellSummary.setCellStyle(wrap_styleSummary);

				

						cellSummary = rowSummary.createCell(5);
						if (Obj.getDeviceStoppageCount()>60)
							cellSummary.setCellValue("Stoppage Found");
						else cellSummary.setCellValue("-");
						cellSummary.setCellStyle(wrap_styleSummary);;
				
						
						cellSummary = rowSummary.createCell(6);
						if ((Obj.getExpectedKmCover()-Obj.getActualKmCover())*1000>500)
							cellSummary.setCellValue("kmcover : "+df2.format(Obj.getActualKmCover())+" Km");
						else cellSummary.setCellValue("-");
						cellSummary.setCellStyle(wrap_styleSummary);
						
//						subdocument.put("kmcover",
//								df2.format(kmcover));
						// System.out.println("  Here--*****************************************--------------------");
						//
					
						cellSummary = rowSummary.createCell(7);
						if ((Obj.getExpectedKmCover()-Obj.getActualKmCover())*1000>500)
							cellSummary.setCellValue("Expected "+Obj.getExpectedKmCover()
									+" Km but device cover only : "+df2.format(Obj.getActualKmCover())+" Km");
						else cellSummary.setCellValue("-");									
															 
						cellSummary.setCellStyle(wrap_styleSummary);


					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MongoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
				}
            
			}
		}
					

					
				
				
				sheetSummary.setColumnWidth(0, 800);
				sheetSummary.setColumnWidth(1, 4000);
				sheetSummary.setColumnWidth(2, 3000);
				sheetSummary.setColumnWidth(3, 4000);
				sheetSummary.setColumnWidth(4, 4000);
				sheetSummary.setColumnWidth(5, 3000);
				sheetSummary.setColumnWidth(6, 5000);
				sheetSummary.setColumnWidth(7, 15000);

					String outFileName ="Exception_Summary_Report_KeyMan"
							+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_" + parentId + ".xlsx";

					try {
						String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
								+ outFileName;
						FileOutputStream fos = new FileOutputStream(new File(file));

						workbookSummary.write(fos);
						fos.flush();
						fos.close();
						workbookSummary.close();
						// String sendemail="rupesh.p@mykiddytracker.com,";
	
						/*String sendemail = "";
						for (String emaString : mailSendInfo.get(j).getEmailIds()) {
							sendemail = sendemail + "," + emaString.trim();
						}
						SendEmail mail = new SendEmail();
	
					if(isSendMail)
						 {
							 mail.sendDeviceExceptionMailToJaipur(sendemail, file,
									 mailSendInfo
									  .get(j).getDept(),"Exception Trip Report of keymen"
									  ,"Exception Trip Report of ",false,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());
						 }
				*/
						 
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
					
	
//					}
							
			
		}
		return msgo;
			
	}






	public MessageObject GenerateSaveDailySummaryOfDeviceByParentIdWithCursorMappedRdps(
			Connection con, DB mongoconnection, String parentId, int day2,
			Connection conSunMssql) 
	{
		
		MessageObject msgo=new MessageObject();
		  try {
			  year = calendar.get(Calendar.YEAR);
				month = calendar.get(Calendar.MONTH);
				day = calendar.get(Calendar.DAY_OF_MONTH)-day2;
			
					long DayStartWorkTime = Long.valueOf(Common
						.getGMTTimeStampFromDate(day+ "-"
								+ String.valueOf(month + 1) + "-" + year
								+ " 00:00 am"));

				java.sql.CallableStatement psLoc = conSunMssql
						.prepareCall("{call SaveDailySummaryOfDeviceByParentIdWithCursorMappedRdps(?,?)}");
				psLoc.setInt(1, Integer.parseInt(parentId));
				psLoc.setLong(2, DayStartWorkTime);

				int result = psLoc.executeUpdate();
				if (result == 0) {
					msgo.setError("true");
					msgo.setMessage("Excepation report not Inserted.");
				} else {
					// //System.err.println("Error=="+result);
					msgo.setError("false");
					msgo.setMessage("Excepation report generate Inserted Successfully");
				}
			
			}catch(Exception e){
				e.printStackTrace();
			}
		return msgo;
	}



	public ArrayList<DailyReportSummaryWithStoppageDTO> GetReportSummeryInfo(Connection con,
			int parentId , long DayStartWorkTime, Connection conSunMssql, int roleId) {
		ArrayList<DailyReportSummaryWithStoppageDTO> deviceReportList=new ArrayList<>();

		try {

		java.sql.CallableStatement psgettrip = conSunMssql
				.prepareCall("{call GetDailySummaryOfDeviceFromReportSummary(?,?,?)}");
	
		System.err.println(parentId+"--"+DayStartWorkTime+"--"+roleId);
		psgettrip.setInt(1,parentId);
		psgettrip.setLong(2,DayStartWorkTime);
		psgettrip.setInt(3,roleId);

		ResultSet rsgertrip = psgettrip.executeQuery();
		System.err.println("DayStartWorkTime---"+DayStartWorkTime);
		if (rsgertrip != null) {
			while (rsgertrip.next()) {

				DailyReportSummaryWithStoppageDTO dto = new DailyReportSummaryWithStoppageDTO();
				dto.setDeviceEndKm(rsgertrip.getDouble("DeviceEndKm"));
				dto.setDeviceEndTime(rsgertrip.getLong("DeviceEndTime"));
				dto.setDeviceId(rsgertrip.getString("DeviceId"));
				dto.setDeviceOvespeedCount(rsgertrip.getInt("DeviceOvespeedCount"));
				dto.setDeviceStartKm(rsgertrip.getDouble("DeviceStartKm"));
				dto.setDeviceStartTime(rsgertrip.getLong("DeviceStartTime"));
				dto.setDeviceStoppageCount(rsgertrip.getInt("DeviceStoppageCount"));
				dto.setExpectedEndKm(rsgertrip.getDouble("ExpectedEndKm"));
				dto.setExpectedEndTime(rsgertrip.getLong("ExpectedEndTime"));
				dto.setExpectedStartKm(rsgertrip.getDouble("ExpectedStartKm"));
				dto.setExpectedStartTime(rsgertrip.getLong("ExpectedStartTime"));
				dto.setName(rsgertrip.getString("Name"));
				dto.setReportOfDay(DayStartWorkTime);
				dto.setMaxSpeed(rsgertrip.getInt("MaxSpeed"));
				dto.setAvgSpeed(rsgertrip.getInt("AvgSpeed"));
				dto.setLocationCount(rsgertrip.getInt("LocationCount"));
				dto.setEndTimeDiff(rsgertrip.getInt("EndTimeDiff"));
				dto.setStartTimeDiff(rsgertrip.getInt("StartTimeDiff"));
				dto.setActualKmCover(rsgertrip.getDouble("ActualKmCover"));
				dto.setExpectedKmCover(rsgertrip.getDouble("ExpectedKmCover"));
				deviceReportList.add(dto);
		
			
			}
		}
		System.out.println("Result Daily Report---"+new Gson().toJson(deviceReportList));

	} catch (Exception e) {
		e.printStackTrace();
	}
		return deviceReportList;
	}




	
	public ArrayList<DeviceOnOffInfoDto> DeviceONOffStatusAPI(Connection con,DB mongoconnection, long startdate, int parentId, Connection conSunMssql) {
		MessageObject msgo = new MessageObject();
		ArrayList<DeviceOnOffInfoDto> DeviceList = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		
	
		// If startdate is todays date long	currentDay 
		if (startdate >= DayStartWorkTime)
		{
			System.err.println("if startdate--"+startdate);

			try {

				java.sql.CallableStatement ps = con.prepareCall("{call GetDeviceRailAddress(?)}");
				ps.setInt(1, parentId);
				ResultSet rs = ps.executeQuery();

				if (rs != null) {
					while (rs.next()) {

						DeviceOnOffInfoDto dto = new DeviceOnOffInfoDto();
						dto.setStudentId(rs.getInt(1));
						dto.setName(rs.getString(2));
						dto.setDeviceID(rs.getString(3));
						DeviceList.add(dto);

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("TodayDeviceStatus==============----------"
					+ " " + DeviceList.size());

			for (int i = 0; i < DeviceList.size(); i++)
				try {
					DBCollection table = mongoconnection
							.getCollection(Common.TABLE_TODAYLOCATION);
					// //System.out.println("device==============----------"+" "+table.getFullName());

					BasicDBObject device_whereQuery = new BasicDBObject();
					device_whereQuery.put("device",
							Long.parseLong(DeviceList.get(i).getDeviceID()));
					DBCursor cursor = table.find(device_whereQuery);
					cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT);
					
					if (cursor.size() != 0) {

						//System.err.println("*-GenerateGPSHolder10MinData----COunt---"+cursor.size());

						while (cursor.hasNext()) {

							DBObject dbObject = (DBObject) cursor.next();

							BasicDBObject Sourceobj = (BasicDBObject) dbObject
									.get("location");

							DeviceList.get(i).setLang(Sourceobj.get("lon") + "");
							DeviceList.get(i).setLat(Sourceobj.get("lat") + "");

							// UserList.get(i).setAddress(Common.GetAddress(""+UserList.get(i).getLat(),
							// ""+UserList.get(i).getLang()));
							DeviceList.get(i).setSpeed(dbObject.get("speed") + "");
							DeviceList.get(i).setTime(dbObject.get("timestamp") + "");
							
							if (Long.parseLong(DeviceList.get(i).getTime()) < DayStartWorkTime)
								DeviceList.get(i).setDeviceOnStatus(0);
							else 
								DeviceList.get(i).setDeviceOnStatus(1);

						}

					} else {
						DeviceList.get(i).setDeviceOnStatus(0);
						DeviceList.get(i).setLang("0");
						DeviceList.get(i).setLat("0");
						DeviceList.get(i).setSpeed("0");
						DeviceList.get(i).setTime("0");
						
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MongoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			
			}
		else {
			///When Startdate is not today date that means past days
			System.err.println("else startdate--"+parentId+"----"+startdate);
			try 
			{
				
				java.sql.CallableStatement ps = conSunMssql
						.prepareCall("{call GetDeviceOnOffHistoryFromReportSummary(?,?)}");
				ps.setInt(1, parentId);
				ps.setLong(2, startdate);

				ResultSet rsgertrip = ps
						.executeQuery();

				System.err.println(rsgertrip.next()+"");

					while (rsgertrip.next()) {
						System.err.println("else startdate--"+rsgertrip.getString("Name"));

						DeviceOnOffInfoDto dto = new DeviceOnOffInfoDto();
			
						dto.setName(rsgertrip.getString("Name"));
						dto.setDeviceID(rsgertrip.getString("DeviceId"));
						if (rsgertrip.getInt("LocationCount")>0) 
							dto.setDeviceOnStatus(1);
						else
							dto.setDeviceOnStatus(0);	
						dto.setTime(rsgertrip.getLong("DeviceOffTime")+"");

						
						DeviceList.add(dto);

					}
				
				// System.err.println("deviceInfoList===== "+new
				// Gson().toJson(deviceInfoList));

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return DeviceList;
	}

	public MessageObject GenerateExceptionReportKeyManBeatFromReportSummary(

			Connection con, DB mongoconnection, String parentId,int day,
			Boolean isSendMail, long startTime1, long startTime2, long endTime1,
			long endTime2, Boolean exceptionSummary, Double distanceToleranceInKm,Connection conSunMssql ) 
	{

		MessageObject msgo = new MessageObject();
		LoginDAO loginObj=new LoginDAO();
		ArrayList<RailMailSendInfoDto> mailSendInfo = loginObj.getSentMailInfoList(con,
				parentId);
		System.err.println("GenerateExceptionReportKeyManBeatFromReportSummary ---"
				+ new Gson().toJson(mailSendInfo));
//		ArrayList<ExceptionReortsTrip> exceptiionalTrip = new ArrayList<>();

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH)-day;
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year
						+ " 00:00 am"));
		
		DBCollection report_table = mongoconnection
				.getCollection(Common.TABLE_AllSectioWorkStatusReport);

		// ///////////////////////////////********************************/////////////////////////////
		// All Section AlertForKeymanWorkStatusReport

		XSSFWorkbook workbookAlertForKeymanWorkStatusReport = new XSSFWorkbook();
		XSSFSheet sheetAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createSheet(WorkbookUtil
						.createSafeSheetName("KeyMan Status Report"));

		XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		HeadingStyleAlertForKeymanWorkStatusReport.setWrapText(true); // S
		XSSFFont font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle();
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleAlertForKeymanWorkStatusReport
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
		font = workbookAlertForKeymanWorkStatusReport.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReport
				.createCellStyle(); // Create new style
		wrap_styleAlertForKeymanWorkStatusReport.setWrapText(true); // Set
																	// wordwrap

		ArrayList<LowBatteryStatusReportDto> lowBatteryStatusSectionWise = new ArrayList<>();

		Row row = sheetAlertForKeymanWorkStatusReport.createRow(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"KeyMan Work Status Report Date :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetAlertForKeymanWorkStatusReport
				.addMergedRegion(new CellRangeAddress(
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(),
						sheetAlertForKeymanWorkStatusReport.getLastRowNum(), 0,
						6));
		row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReport);

		row = sheetAlertForKeymanWorkStatusReport
				.createRow(sheetAlertForKeymanWorkStatusReport.getLastRowNum() + 1);

		Cell cell = row.createCell(0);

		cell.setCellValue("Section Name");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(1);
		cell.setCellValue("Start with Low Battery Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(2);
		cell.setCellValue("Off Devices");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(3);
		cell.setCellValue("Beat not cover");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		;
		cell = row.createCell(4);
		cell.setCellValue("Beat completed Sucessfully");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		cell = row.createCell(5);
		cell.setCellValue("Over speed devicess");
		cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
		row.setHeight((short) 800);


		// ///////////////////////////////////All Section
		// End//////////////////////////////////////////////////////////
		
	
		
		
		// //***************/////////////////////////////
				// All Section Count AlertForKeymanWorkStatusReport

				XSSFWorkbook workbookAlertForKeymanWorkStatusReportCount = new XSSFWorkbook();
				XSSFSheet sheetAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createSheet(WorkbookUtil
								.createSafeSheetName("KeyMan Status Report with count"));
				XSSFCellStyle HeadingStyleAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle();
				HeadingStyleAlertForKeymanWorkStatusReportCount
						.setFillForegroundColor(IndexedColors.AQUA.getIndex());
				HeadingStyleAlertForKeymanWorkStatusReportCount
						.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				 font = workbookAlertForKeymanWorkStatusReport.createFont();
				font.setFontHeightInPoints((short) 20);
				font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
				HeadingStyleAlertForKeymanWorkStatusReportCount.setWrapText(true); 
				 tripColumnStyleAlertForKeymanWorkStatusReport = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle();
				tripColumnStyleAlertForKeymanWorkStatusReport
						.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
								.getIndex());
				tripColumnStyleAlertForKeymanWorkStatusReport
						.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				tripColumnStyleAlertForKeymanWorkStatusReport.setWrapText(true);
				font = workbookAlertForKeymanWorkStatusReport.createFont();
				font.setFontHeightInPoints((short) 15);
				font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
				XSSFCellStyle wrap_styleAlertForKeymanWorkStatusReportCount = workbookAlertForKeymanWorkStatusReportCount
						.createCellStyle(); // Create new style
				wrap_styleAlertForKeymanWorkStatusReportCount.setWrapText(true); // Set
																	

				row = sheetAlertForKeymanWorkStatusReportCount.createRow(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);
				row.createCell(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);
				row.createCell(0);
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);

				row.createCell(0);
				row.getCell(0).setCellValue(
						"KeyMan Work Status Count Report Date :-"
								+ Common.getDateFromLong(DayStartWorkTime));
				sheetAlertForKeymanWorkStatusReportCount
						.addMergedRegion(new CellRangeAddress(
								sheetAlertForKeymanWorkStatusReportCount.getLastRowNum(),
								sheetAlertForKeymanWorkStatusReportCount.getLastRowNum(), 0,
								4));
				row.getCell(0).setCellStyle(HeadingStyleAlertForKeymanWorkStatusReportCount);

				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount.getLastRowNum() + 1);

				 cell = row.createCell(0);

				cell.setCellValue("Section Name");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				/*cell = row.createCell(1);
				cell.setCellValue("Start with Low Battery Devices");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);*/
				cell = row.createCell(1);
				cell.setCellValue("Off Devices");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				cell = row.createCell(2);
				cell.setCellValue("Beat not cover");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				;
				cell = row.createCell(3);
				cell.setCellValue("Beat completed Sucessfully");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				cell = row.createCell(4);
				cell.setCellValue("Total Device");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				cell = row.createCell(5);
				cell.setCellValue("Detail Remark");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				row.setHeight((short) 800);
			/*	cell = row.createCell(6);
				cell.setCellValue("Section Status");
				cell.setCellStyle(tripColumnStyleAlertForKeymanWorkStatusReport);
				row.setHeight((short) 500);*/

				// ///////////////////////////////////All Section COunt// End//////////////////////////////////////////////////////////


				
				////////////////////////////////////////// exception Summary////////////////////////
				
				
				XSSFWorkbook workbookSummary = new XSSFWorkbook();
				XSSFSheet sheetSummary = workbookSummary.createSheet(WorkbookUtil
						.createSafeSheetName("Exception Trip Report Summary of keymen"));

				XSSFCellStyle HeadingStyleSummary = workbookSummary.createCellStyle();
				HeadingStyleSummary.setFillForegroundColor(IndexedColors.AQUA.getIndex());
				HeadingStyleSummary.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				HeadingStyleSummary.setAlignment(HorizontalAlignment.CENTER);
				XSSFFont fontSummary = workbookSummary.createFont();
				HeadingStyleSummary.setWrapText(true); 

				font.setBold(true);
				font.setColor(IndexedColors.WHITE.getIndex());
				HeadingStyleSummary.setFont(font);

				XSSFCellStyle tripColumnStyleSummary = workbookSummary.createCellStyle();
				tripColumnStyleSummary
						.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
								.getIndex());
				tripColumnStyleSummary.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				tripColumnStyleSummary.setWrapText(true);
				font = workbookSummary.createFont();
				font.setFontHeightInPoints((short) 15);
				font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
				// tripColumnStyle.setFont(font);

				XSSFCellStyle remarkColumnStyleSummary = workbookSummary.createCellStyle();
				remarkColumnStyleSummary.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
						.getIndex());
				remarkColumnStyleSummary.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				remarkColumnStyleSummary.setWrapText(true); 

				XSSFCellStyle error_remarkColumnStyleSummary = workbookSummary.createCellStyle();
				error_remarkColumnStyleSummary
						.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
								.getIndex());
				error_remarkColumnStyleSummary
						.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				XSSFFont redfontSummary = workbookSummary.createFont();
				redfontSummary.setColor(IndexedColors.RED.getIndex());
				error_remarkColumnStyleSummary.setFont(redfontSummary);
				error_remarkColumnStyleSummary.setWrapText(true); 

				// loactionNotFoundColumnStyle.setFont(font);

				CellStyle wrap_styleSummary = workbookSummary.createCellStyle(); // Create new
																	// style
				wrap_styleSummary.setWrapText(true); // Set wordwrap

				int petrolMancount = 0;

				 row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);

				row.createCell(0);
				row.getCell(0).setCellValue(
						"Exception Report for Keyman"
								 + " Date: "
								+ Common.getDateFromLong(DayStartWorkTime));
				row.getCell(0).setCellStyle(HeadingStyleSummary);
				
				sheetSummary.addMergedRegion(new CellRangeAddress(sheetSummary.getLastRowNum(),
						sheetSummary.getLastRowNum(), 0, 7));

				BasicDBList sectionDeviceList = new BasicDBList();
				
				row = sheetSummary.createRow(0);
				row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);



				cell = row.createCell(0);
				cell.setCellValue("Sr no");
				cell.setCellStyle(tripColumnStyleSummary);
			/*	
				 cell = row.createCell(1);
				cell.setCellValue("Name");
				cell.setCellStyle(tripColumnStyle);*/

				cell = row.createCell(1);
				cell.setCellValue("Device name");
				cell.setCellStyle(tripColumnStyleSummary);

				cell = row.createCell(2);
				cell.setCellValue("Late start >15min");
				cell.setCellStyle(tripColumnStyleSummary);
				
				cell = row.createCell(3);
				cell.setCellValue("Early off >15 min");
				cell.setCellStyle(tripColumnStyleSummary);

				cell = row.createCell(4);
				cell.setCellValue("Overspeed>8kmph");
				cell.setCellStyle(tripColumnStyleSummary);
				
				cell = row.createCell(5);
				cell.setCellValue("Stoppage >60min. No of stoppage at location with railways geo fence");
				cell.setCellStyle(tripColumnStyleSummary);
				
				cell = row.createCell(6);
				cell.setCellValue("Trip not completed");
				cell.setCellStyle(tripColumnStyleSummary);
				
				cell = row.createCell(7);
				cell.setCellValue("Travelled km, 500 m short from alloted km");
				cell.setCellStyle(tripColumnStyleSummary);
				row.setHeight((short) 1200);
				
				
				
				////////////////////////////////////report Summary//////////////////////////////
				int divDeviceOffCount=0;
				int divDeviceBeatNotCoverCount=0;
				int divDeviceBeatCoverCount=0;
				
				
				
				
//			ListMultimap<String, DailyReportSummaryWithStoppageDTO> deviceReportListMap = ArrayListMultimap.create();
			
			
//			Map<String, DailyReportSummaryWithStoppageDTO> deviceReportListMap = new HashMap<>();
				
//				ListMultimap<String, DailyReportSummaryWithStoppageDTO> deviceReportListMap = 
//						ArrayListMultimap.create();
				 DuplicateMap<String,DailyReportSummaryWithStoppageDTO> deviceReportListMap=new DuplicateMap<>();
				ArrayList<DailyReportSummaryWithStoppageDTO> deviceReportList=new ArrayList<>();
				try {

				java.sql.CallableStatement psgettrip = conSunMssql
						.prepareCall("{call GetDailySummaryOfDeviceFromReportSummary(?,?)}");
			
				psgettrip.setInt(1,Integer.parseInt(parentId));
				psgettrip.setLong(2,DayStartWorkTime);
			

				ResultSet rsgertrip = psgettrip.executeQuery();
				System.err.println("DayStartWorkTime---"+DayStartWorkTime);
				if (rsgertrip != null) {
					while (rsgertrip.next()) {

						DailyReportSummaryWithStoppageDTO dto = new DailyReportSummaryWithStoppageDTO();
						dto.setDeviceEndKm(rsgertrip.getDouble("DeviceEndKm"));
						dto.setDeviceEndTime(rsgertrip.getLong("DeviceEndTime"));
						dto.setDeviceId(rsgertrip.getString("DeviceId"));
						dto.setDeviceOvespeedCount(rsgertrip.getInt("DeviceOvespeedCount"));
						dto.setDeviceStartKm(rsgertrip.getDouble("DeviceStartKm"));
						dto.setDeviceStartTime(rsgertrip.getLong("DeviceStartTime"));
						dto.setDeviceStoppageCount(rsgertrip.getInt("DeviceStoppageCount"));
						dto.setExpectedEndKm(rsgertrip.getDouble("ExpectedEndKm"));
						dto.setExpectedEndTime(rsgertrip.getLong("ExpectedEndTime"));
						dto.setExpectedStartKm(rsgertrip.getDouble("ExpectedStartKm"));
						dto.setExpectedStartTime(rsgertrip.getLong("ExpectedStartTime"));
						dto.setName(rsgertrip.getString("Name"));
						dto.setReportOfDay(DayStartWorkTime);
						dto.setMaxSpeed(rsgertrip.getInt("MaxSpeed"));
						dto.setAvgSpeed(rsgertrip.getInt("AvgSpeed"));
						dto.setLocationCount(rsgertrip.getInt("LocationCount"));
						dto.setEndTimeDiff(rsgertrip.getInt("EndTimeDiff"));
						dto.setStartTimeDiff(rsgertrip.getInt("StartTimeDiff"));
						dto.setActualKmCover(rsgertrip.getDouble("ActualKmCover"));
						dto.setExpectedKmCover(rsgertrip.getDouble("ExpectedKmCover"));

						deviceReportListMap.put(rsgertrip.getString("DeviceId"), dto);
						//deviceReportListMap.put(dto.getDeviceId()+"-"+dto.getExpectedStartKm()+"-"+dto.getExpectedEndKm(), dto);

						deviceReportList.add(dto);
				
					
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
				

		System.out.println("deviceReportListMap-----"+deviceReportListMap.size());
		
		for (int j = 0; j < mailSendInfo.size(); j++) {
//		for (int j = 10; j<=10 ;j++) {
//
			
			Set<String> KeyManCoverSucefullyDevices_Set = new HashSet<String>();
			Set<String> KeyManExceptionalDevices_Set = new HashSet<String>();
			Set<String> KeyManOverspeedDevices_Set = new HashSet<String>();
			Set<String> KeyManOffDevice_Set = new HashSet<String>();
			Set<String> KeyManLowBattery_Set = new HashSet<String>();
			ArrayList<Short> rowHeight = new ArrayList<>();

			ArrayList<RailDeviceInfoDto> deviceInfoList = mailSendInfo.get(j)
					.getDeviceIds();
			// Get Trips
			ArrayList<RailwayPetrolmanTripsBeatsDTO> RailwayKeymanTripsBeatsList = new ArrayList<>();

			BasicDBObject Maindocument = new BasicDBObject();
			Maindocument.put("timestamp", DayStartWorkTime);
			Maindocument.put("parentid", parentId);
			Maindocument.put("section", mailSendInfo.get(j).getDept());

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(WorkbookUtil
					.createSafeSheetName("Exception Trip Report of keymen"));

			XSSFCellStyle HeadingStyle = workbook.createCellStyle();
			HeadingStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
			HeadingStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			HeadingStyle.setAlignment(HorizontalAlignment.CENTER);
			font = workbook.createFont();
			font.setBold(true);
			font.setColor(IndexedColors.WHITE.getIndex());
			HeadingStyle.setFont(font);

			XSSFCellStyle tripColumnStyle = workbook.createCellStyle();
			tripColumnStyle
					.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
							.getIndex());
			tripColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			tripColumnStyle.setWrapText(true);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 15);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// tripColumnStyle.setFont(font);

			XSSFCellStyle remarkColumnStyle = workbook.createCellStyle();
			remarkColumnStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
					.getIndex());
			remarkColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			XSSFCellStyle error_remarkColumnStyle = workbook.createCellStyle();
			error_remarkColumnStyle
					.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
							.getIndex());
			error_remarkColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont redfont = workbook.createFont();
			redfont.setColor(IndexedColors.RED.getIndex());
			error_remarkColumnStyle.setFont(redfont);

			XSSFCellStyle loactionNotFoundColumnStyle = workbook
					.createCellStyle();
			loactionNotFoundColumnStyle
					.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
			loactionNotFoundColumnStyle
					.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			font = workbook.createFont();
			font.setFontHeightInPoints((short) 5);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			// loactionNotFoundColumnStyle.setFont(font);

			CellStyle wrap_style = workbook.createCellStyle(); // Create new
																// style
			wrap_style.setWrapText(true); // Set wordwrap			

			row = sheet.createRow(sheet.getLastRowNum() + 1);

			row.createCell(0);
			row.getCell(0).setCellValue(
					"Exception Report for Keyman of section "
							+ mailSendInfo.get(j).getDept() + "  Date: "
							+ Common.getDateFromLong(DayStartWorkTime));
			row.getCell(0).setCellStyle(HeadingStyle);
			
			sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
					sheet.getLastRowNum(), 0, 5));

			for (int i = 0; i < deviceInfoList.size(); i++) {
//				 for (int i = 5; i <=5; i++) {

//				System.err.println(" **Device =---================"+ deviceInfoList.get(i).getName() + "\n");

				Double startBatteryStatus = loginObj.getKeyManStartBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+startTime1);

				Double endBatteryStatus = loginObj.getKeyManEndBatteryStatus(
						mongoconnection, deviceInfoList.get(i).getDeviceID(),
						DayStartWorkTime+endTime2);

			if (deviceInfoList.get(i).getName().startsWith("K/")) {
//				if (deviceInfoList.get(i).getName().startsWith("K/")&&deviceInfoList.get(i).getName().contains("-293")){
				
				BasicDBObject subdocument = new BasicDBObject();
				StringBuilder remark = new StringBuilder();
				String DetailRemark = "";
			if (deviceReportListMap.get(deviceInfoList.get(i).getDeviceID())!=null)
				{	
					
						

						row = sheet.createRow(0);
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						row.createCell(0);

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						row.createCell(0);
						row.getCell(0).setCellValue(
								deviceInfoList.get(i).getName() + "   ("
										+ deviceInfoList.get(i).getDeviceID()
										+ ")");
						subdocument.put("device", deviceInfoList.get(i)
								.getDeviceID());
						subdocument.put("device_name", deviceInfoList.get(i)
								.getName());

						row.getCell(0).setCellStyle(HeadingStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));
						// start Battery Status
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						cell.setCellValue("Start Battery status :- "
								+ df2.format(((startBatteryStatus / 6) * 100))
								+ " %");
						subdocument.put("start_battery_status",
								df2.format(((startBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));

						// End Battery Staus
						row = sheet.createRow(sheet.getLastRowNum() + 1);
						cell = row.createCell(0);
						if (endBatteryStatus>0) {
							cell.setCellValue("End Battery status :- "
									+ df2.format(((endBatteryStatus / 6) * 100))
									+ " %");
						}else{
							cell.setCellValue("End Battery status :- 0% / May be device off ");
						}
						
						subdocument.put("end_battery_status",
								df2.format(((endBatteryStatus / 6) * 100))
										+ " %");

						cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));

						row = sheet.createRow(sheet.getLastRowNum() + 1);
						Cell remark_cell = row.createCell(0);
						remark_cell.setCellValue("Remark status :- ");

						remark_cell.setCellStyle(remarkColumnStyle);
						sheet.addMergedRegion(new CellRangeAddress(sheet
								.getLastRowNum(), sheet.getLastRowNum(), 0, 7));

						row = sheet.createRow(sheet.getLastRowNum() + 1);

						cell = row.createCell(0);
						cell.setCellValue("Trip#");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(1);
						cell.setCellValue("Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(2);
						cell.setCellValue("Expected Start Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(3);
						cell.setCellValue("End Beat");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(4);
						cell.setCellValue("Expected end Beat");
						cell.setCellStyle(tripColumnStyle);

						/*
						 * cell = row.createCell(5);
						 * cell.setCellValue("Avg speed");
						 * cell.setCellStyle(tripColumnStyle);
						 */

						cell = row.createCell(5);
						cell.setCellValue("Max speed");
						cell.setCellStyle(tripColumnStyle);

						cell = row.createCell(6);
						cell.setCellValue("Distance cover");
						cell.setCellStyle(tripColumnStyle);
						cell = row.createCell(7);
						
						cell.setCellValue("Detail Remark");
						cell.setCellStyle(tripColumnStyle);

						row.setHeight((short) 800);
						
System.out.println("Get trip from mapfor device---"+deviceInfoList.get(i).getDeviceID()+" --"+deviceReportListMap.get(deviceInfoList.get(i).getDeviceID()));
//System.err.println("Size deviceReportListMap.getdevice--"+deviceReportListMap.get(deviceInfoList.get(i).getDeviceID()).size());

						int trip=0;
						
						
							for (DailyReportSummaryWithStoppageDTO Obj : deviceReportListMap.get(deviceInfoList.get(i).getDeviceID()))
						
							{	trip++;
							System.err.println("dailySummaryObj---"+new Gson().toJson(Obj));
					            		           
									if (Obj.getLocationCount()==0)
									{
										//Device Off for a day
					            
										try {
											
											if (!deviceInfoList
													.get(i).getName().contains("/LR/")) {
												KeyManOffDevice_Set.add(deviceInfoList
														.get(i).getName());

											}
										
											remark.append("Device is Off");

											row = sheet.createRow(sheet
													.getLastRowNum() + 1);
											cell = row.createCell(0);
											cell.setCellValue(1);
											cell.setCellStyle(loactionNotFoundColumnStyle);

											cell = row.createCell(1);
											cell.setCellValue("Device is Off");
											cell.setCellStyle(loactionNotFoundColumnStyle);
											sheet.addMergedRegion(new CellRangeAddress(
													sheet.getLastRowNum(), sheet
															.getLastRowNum(), 1, 3));

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
					            
									}else if(Obj.getLocationCount()>0&&Obj.getExpectedStartKm()==-1||Obj.getExpectedEndKm()==-1)
									{
					            
										//Trip Not shedule yet
										
										try {
											row = sheet
													.createRow(sheet.getLastRowNum() + 1);
											row = sheet
													.createRow(sheet.getLastRowNum() + 1);
											row = sheet
													.createRow(sheet.getLastRowNum() + 1);

											row.createCell(0);
											row.getCell(0).setCellValue(

													deviceInfoList.get(i).getName()
															+ "  ("
															+ deviceInfoList.get(i)
																	.getDeviceID() + ") ");
											row.getCell(0).setCellStyle(HeadingStyle);
											sheet.addMergedRegion(new CellRangeAddress(
													sheet.getLastRowNum(), sheet
															.getLastRowNum(), 0, 7));

											// start Battery Status
											row = sheet
													.createRow(sheet.getLastRowNum() + 1);
											cell = row.createCell(0);
											cell.setCellValue("Start Battery status :- "
													+ df2.format(((startBatteryStatus / 6) * 100))
													+ " %");
											subdocument
													.put("start_battery_status",
															df2.format(((startBatteryStatus / 6) * 100))
																	+ " %");

											cell.setCellStyle(remarkColumnStyle);
											sheet.addMergedRegion(new CellRangeAddress(
													sheet.getLastRowNum(), sheet
															.getLastRowNum(), 0, 7));

											// End Battery Staus
											row = sheet
													.createRow(sheet.getLastRowNum() + 1);
											cell = row.createCell(0);
											cell.setCellValue("End Battery status :- "
													+ df2.format(((endBatteryStatus / 6) * 100))
													+ " %");
											subdocument
													.put("end_battery_status",
															df2.format(((endBatteryStatus / 6) * 100))
																	+ " %");

											cell.setCellStyle(remarkColumnStyle);
											sheet.addMergedRegion(new CellRangeAddress(
													sheet.getLastRowNum(), sheet
															.getLastRowNum(), 0, 7));

											row = sheet
													.createRow(sheet.getLastRowNum() + 1);
											cell = row.createCell(0);
											cell.setCellValue("Remark status :- Trips not Shedule for this device.");
											subdocument.put("remark",
													"Trips not Shedule for this device please contact to admin to allocate work schedule to this device..");


											if (!deviceInfoList
													.get(i).getName().contains("/LR/"))
										KeyManExceptionalDevices_Set.add(deviceInfoList
												.get(i).getName());

											cell.setCellStyle(error_remarkColumnStyle);
											sheet.addMergedRegion(new CellRangeAddress(
													sheet.getLastRowNum(), sheet
															.getLastRowNum(), 0, 7));
											row = sheet
													.createRow(sheet.getLastRowNum() + 1);
											cell = row.createCell(0);
											cell.setCellValue("Trips not Shedule for this device please contact to admin to allocate work schedule to this device.");

											sheet.addMergedRegion(new CellRangeAddress(
													sheet.getLastRowNum(), sheet
															.getLastRowNum(), 0, 7));
										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (MongoException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

					          
									}
									else if(Obj.getLocationCount()>0&&Obj.getActualKmCover()<0.5&&Obj.getExpectedKmCover()>0.5)
									{
										//Device noT moved
										
										remark.append("\tDevice is not moved in trip no - "+trip
												+ ".");
										
										
										try {

											row = sheet.createRow(sheet
													.getLastRowNum() + 1);
											cell = row.createCell(0);
											cell.setCellValue(trip);
										

											cell.setCellStyle(wrap_style);

											cell = row.createCell(1);								
											cell.setCellValue("-");
											
											cell.setCellStyle(wrap_style);

											cell = row.createCell(2);
											cell.setCellValue(Obj.getExpectedStartKm());
											cell.setCellStyle(wrap_style);

											
											cell = row.createCell(3);
											cell.setCellValue("-");
											cell.setCellStyle(wrap_style);

											cell = row.createCell(4);
											cell.setCellValue(Obj.getExpectedEndKm());
											cell.setCellStyle(wrap_style);

											cell = row.createCell(5);
											cell.setCellValue(Obj.getMaxSpeed());
											
											cell.setCellStyle(wrap_style);
											
											cell = row.createCell(6);
											cell.setCellValue(Obj.getActualKmCover());
											cell.setCellStyle(wrap_style);

											// System.out.println("  Here--*****************************************--------------------");
											//
										
											cell = row.createCell(7);
											cell.setCellValue("Device is not moved.");
											cell.setCellStyle(wrap_style);									
										

										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (MongoException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										KeyManExceptionalDevices_Set
												.add(deviceInfoList.get(i)
														.getName());
										
									}else if(Obj.getLocationCount()>0&&Obj.getDeviceStartTime()==-1&&
											Obj.getDeviceEndTime()==-1)
									{
										
										//Near by beat RDPS NOt FOund
										try {

											row = sheet.createRow(sheet
													.getLastRowNum() + 1);
											cell = row.createCell(0);
											cell.setCellValue(trip);
											
											
											remark_cell
											.setCellValue("Remark status :-Near by beat RDPS Not Found for trip -"+trip);
											remark_cell.setCellStyle(error_remarkColumnStyle);
											row.setHeight((short) 800);

											

											cell.setCellStyle(wrap_style);

											cell = row.createCell(1);
										
											cell.setCellValue("Near By Beat RDPS Not Found");
											cell.setCellStyle(wrap_style);

											cell = row.createCell(2);
											cell.setCellValue(Obj.getExpectedStartKm());
										

											cell.setCellStyle(wrap_style);

											
											cell = row.createCell(3);
											
											cell.setCellValue("Near By Beat RDPS Not Found");

											cell.setCellStyle(wrap_style);

											cell = row.createCell(4);
											cell.setCellValue(Obj.getExpectedEndKm());
										
											cell.setCellStyle(wrap_style);

											cell = row.createCell(5);
											cell.setCellValue(0);
											
										
											cell.setCellStyle(wrap_style);

											cell = row.createCell(6);
										
											
											cell.setCellValue(0);
											cell.setCellStyle(wrap_style);
											

											cell = row.createCell(7);
										
											DetailRemark="RDPS Data not found near by "+
													Obj.getExpectedStartKm()
													+"-"+Obj.getExpectedEndKm()+"Km";
											
											cell.setCellValue(DetailRemark);
											cell.setCellStyle(wrap_style);

											if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName()))
												{
												KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());
												KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());
											}else{
												KeyManExceptionalDevices_Set.add(deviceInfoList.get(i).getName());

											}
													

											// System.out.println("  Here--*****************************************--------------------");
											//

										} catch (NumberFormatException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (MongoException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										//Near By Beat RDPS Not Found NOt found End/////////////////////////////////
										
								}else
								{
									
									if(Obj.getActualKmCover()!=0&&Obj.getDeviceOvespeedCount()<5
										&&Obj.getActualKmCover() >= Obj.getExpectedKmCover()
											- distanceToleranceInKm&&
											Math.abs(Obj.getDeviceStartKm()-Obj.getExpectedStartKm())<Obj.getExpectedKmCover()
											&&Math.abs(Obj.getExpectedEndKm()-Obj.getDeviceEndKm())<Obj.getExpectedKmCover()
											) 
									{
										
										remark_cell
										.setCellValue("Remark status :- All work done succesfully.");
								KeyManCoverSucefullyDevices_Set
										.add(deviceInfoList.get(i)
												.getName());
								
								
									}else if(Obj.getActualKmCover()!=0&&Obj.getDeviceOvespeedCount()>5&&Obj.getActualKmCover() >= Obj.getExpectedKmCover()
											- distanceToleranceInKm) 
									{
										
											remark.append("\tWork done succesfully But found in Overspeed " );

								KeyManCoverSucefullyDevices_Set
										.add(deviceInfoList.get(i)
												.getName());
								
								DetailRemark="Device found in Over speed  ";
								
									}else if (Obj.getActualKmCover()!=0&&Obj.getActualKmCover() < Obj.getExpectedKmCover()
											- distanceToleranceInKm) 
									{
										
										remark.append("\tTotal beats not completed in Trip "
												+ trip + ".");
										KeyManExceptionalDevices_Set
												.add(deviceInfoList.get(i)
														.getName());
										
										DetailRemark="Total beats not completed in Trip";

									}
									else if (Obj.getActualKmCover()==0&&Obj.getActualKmCover()< .5){
										remark.append("\tDevice is not moved "
												+ ".");
										KeyManExceptionalDevices_Set
												.add(deviceInfoList.get(i)
														.getName());
										
										DetailRemark="Device was walk on Track";

									}else if (Math.abs(Obj.getDeviceStartKm()-Obj.getExpectedStartKm())>Obj.getExpectedKmCover()
											||Math.abs(Obj.getExpectedEndKm()-Obj.getDeviceEndKm())>Obj.getExpectedKmCover()) 
									{
										
									
										remark.append("\tDevice moved on other than expected beat. "
												+ ".");
										KeyManExceptionalDevices_Set
												.add(deviceInfoList.get(i)
														.getName());
										
										DetailRemark="Device was move between "+Obj.getDeviceStartKm() +"-"+Obj.getDeviceEndKm()+" but expected between "+
												Obj.getExpectedStartKm()+"-"+	Obj.getExpectedEndKm();

								
									}
								
								
								

								

								try {

									row = sheet.createRow(sheet
											.getLastRowNum() + 1);
									cell = row.createCell(0);
									cell.setCellValue(trip);
									cell.setCellStyle(wrap_style);

									cell = row.createCell(1);								
									cell.setCellValue(Obj.getDeviceStartKm());
									
									cell.setCellStyle(wrap_style);

									cell = row.createCell(2);
									cell.setCellValue(Obj.getExpectedStartKm());
									cell.setCellStyle(wrap_style);

									
									cell = row.createCell(3);
									cell.setCellValue(Obj.getDeviceEndKm());
									cell.setCellStyle(wrap_style);

									cell = row.createCell(4);
									cell.setCellValue(Obj.getExpectedEndKm());
									cell.setCellStyle(wrap_style);

									cell = row.createCell(5);
									cell.setCellValue(Obj.getMaxSpeed());
									

									if (Obj.getDeviceOvespeedCount()>5) 
										KeyManOverspeedDevices_Set.add(deviceInfoList.get(i)
														.getName());

									cell.setCellStyle(wrap_style);
									
									cell = row.createCell(6);
									cell.setCellValue(Obj.getActualKmCover());
									cell.setCellStyle(wrap_style);

									// System.out.println("  Here--*****************************************--------------------");
									//
								
									cell = row.createCell(7);
									cell.setCellValue(DetailRemark);
									cell.setCellStyle(wrap_style);									
								

								} catch (NumberFormatException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (MongoException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
								
								///////////////Summary report Row generate-////////////////////////
							
			           
							if (Obj.getLocationCount()==0)
							{
			            
								XSSFRow rowSummary = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
				
								Cell cellSummary = rowSummary.createCell(0);
								cellSummary.setCellValue(sheetSummary.getLastRowNum()-2);
								cellSummary.setCellStyle(wrap_styleSummary);
								
								cellSummary = rowSummary.createCell(1);
								cellSummary.setCellValue(Obj.getName());
								cellSummary.setCellStyle(wrap_styleSummary);
								
								cellSummary = rowSummary.createCell(2);
								cellSummary.setCellValue("Device was off for a day.");
								cellSummary.setCellStyle(error_remarkColumnStyleSummary);
								sheetSummary.addMergedRegion(new CellRangeAddress(sheetSummary.getLastRowNum(),
										sheetSummary.getLastRowNum(), 2, 7));
								
			            }else if(Obj.getLocationCount()>0&&Obj.getExpectedStartKm()==-1||Obj.getExpectedEndKm()==-1){
			            	XSSFRow rowSummary = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
			            	
							Cell cellSummary = rowSummary.createCell(0);
							cellSummary.setCellValue(sheetSummary.getLastRowNum()-2);
							cellSummary.setCellStyle(wrap_styleSummary);
							
							cellSummary = rowSummary.createCell(1);
							cellSummary.setCellValue(Obj.getName());
							cellSummary.setCellStyle(wrap_styleSummary);
							
							cellSummary = rowSummary.createCell(2);
							cellSummary.setCellValue("Yet Trip not sheduled for this device.please share route detils to admin.");
							cellSummary.setCellStyle(error_remarkColumnStyleSummary);
							sheetSummary.addMergedRegion(new CellRangeAddress(sheetSummary.getLastRowNum(),
									sheetSummary.getLastRowNum(), 2, 7));
			            }
			            else{

//							double kmExpected=Math.abs(Obj.getExpectedEndKm()-Obj.getExpectedStartKm());
//							double kmActualCover=Math.abs(Obj.getDeviceEndKm()-Obj.getDeviceStartKm());
//							long startTimeDiff=Obj.getDeviceStartTime()-Obj.getExpectedStartTime();
//							long endTimeDiff=Obj.getExpectedEndTime()-Obj.getDeviceEndTime();
							
							if(Obj.getStartTimeDiff()>900 ||Obj.getEndTimeDiff()>900||Obj.getDeviceOvespeedCount()>5
									||(Obj.getExpectedKmCover()-Obj.getActualKmCover())*1000>500)
							{
								try {

									XSSFRow rowSummary = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);

									 Cell cellSummary = rowSummary.createCell(0);
									cellSummary.setCellValue(sheetSummary.getLastRowNum()-2);
									cellSummary.setCellStyle(wrap_styleSummary);
									
									cellSummary = rowSummary.createCell(1);
									cellSummary.setCellValue(Obj.getName());
									cellSummary.setCellStyle(wrap_styleSummary);

								
									

									cellSummary = rowSummary.createCell(2);
									if (Obj.getStartTimeDiff()>900)
										cellSummary.setCellValue(Common.getDateCurrentTimeZone(Obj.getDeviceStartTime()));
									else cellSummary.setCellValue("-");



									cellSummary = rowSummary.createCell(3);
									if (Obj.getEndTimeDiff()>900)
										cellSummary.setCellValue(Common.getDateCurrentTimeZone(Obj.getDeviceEndTime()));
									else cellSummary.setCellValue("-");
									cellSummary.setCellStyle(wrap_styleSummary);

										
									


									cellSummary = rowSummary.createCell(4);
									if (Obj.getDeviceOvespeedCount()>5)
										cellSummary.setCellValue("Found in OverSpeed");
									else cellSummary.setCellValue("-");
									cellSummary.setCellStyle(wrap_styleSummary);

							

									cellSummary = rowSummary.createCell(5);
									if (Obj.getDeviceStoppageCount()>60)
										cellSummary.setCellValue("Stoppage Found");
									else cellSummary.setCellValue("-");
									cellSummary.setCellStyle(wrap_styleSummary);;
							
									
									cellSummary = rowSummary.createCell(6);
									if ((Obj.getExpectedKmCover()-Obj.getActualKmCover())*1000>500)
										cellSummary.setCellValue("kmcover : "+df2.format(Obj.getActualKmCover())+" Km");
									else cellSummary.setCellValue("-");
									cellSummary.setCellStyle(wrap_styleSummary);
									
//									subdocument.put("kmcover",
//											df2.format(kmcover));
									// System.out.println("  Here--*****************************************--------------------");
									//
								
									cellSummary = rowSummary.createCell(7);
									if ((Obj.getExpectedKmCover()-Obj.getActualKmCover())*1000>500)
										cellSummary.setCellValue("Expected "+Obj.getExpectedKmCover()
												+" Km but device cover only : "+df2.format(Obj.getActualKmCover())+" Km");
									else cellSummary.setCellValue("-");									
																		 
									cellSummary.setCellStyle(wrap_styleSummary);


								} catch (NumberFormatException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (MongoException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							
							}
			            
						}
					
								
								/////Summary report row generate end///////////////////////

		
						}//for end
							
							if (remark.toString().length() > 0) {
								remark_cell.setCellValue("Remark status :- "
										+ remark.toString());
								subdocument.put("remark", remark.toString());
								remark_cell.setCellStyle(error_remarkColumnStyle);
							}
					}
					else
					{
						//Trip Not shedule yet
						
						try {
							row = sheet
									.createRow(sheet.getLastRowNum() + 1);
							row = sheet
									.createRow(sheet.getLastRowNum() + 1);
							row = sheet
									.createRow(sheet.getLastRowNum() + 1);

							row.createCell(0);
							row.getCell(0).setCellValue(

									deviceInfoList.get(i).getName()
											+ "  ("
											+ deviceInfoList.get(i)
													.getDeviceID() + ") ");
							row.getCell(0).setCellStyle(HeadingStyle);
							sheet.addMergedRegion(new CellRangeAddress(
									sheet.getLastRowNum(), sheet
											.getLastRowNum(), 0, 7));

							// start Battery Status
							row = sheet
									.createRow(sheet.getLastRowNum() + 1);
							cell = row.createCell(0);
							cell.setCellValue("Start Battery status :- "
									+ df2.format(((startBatteryStatus / 6) * 100))
									+ " %");
							subdocument
									.put("start_battery_status",
											df2.format(((startBatteryStatus / 6) * 100))
													+ " %");

							cell.setCellStyle(remarkColumnStyle);
							sheet.addMergedRegion(new CellRangeAddress(
									sheet.getLastRowNum(), sheet
											.getLastRowNum(), 0, 7));

							// End Battery Staus
							row = sheet
									.createRow(sheet.getLastRowNum() + 1);
							cell = row.createCell(0);
							cell.setCellValue("End Battery status :- "
									+ df2.format(((endBatteryStatus / 6) * 100))
									+ " %");
							subdocument
									.put("end_battery_status",
											df2.format(((endBatteryStatus / 6) * 100))
													+ " %");

							cell.setCellStyle(remarkColumnStyle);
							sheet.addMergedRegion(new CellRangeAddress(
									sheet.getLastRowNum(), sheet
											.getLastRowNum(), 0, 7));

							row = sheet
									.createRow(sheet.getLastRowNum() + 1);
							cell = row.createCell(0);
							cell.setCellValue("Remark status :- Trips not Shedule for this device.");
							subdocument.put("remark",
									"Trips not Shedule for this device.");


							if (!deviceInfoList
									.get(i).getName().contains("/LR/"))
								KeyManExceptionalDevices_Set.add(deviceInfoList
								.get(i).getName());

							cell.setCellStyle(error_remarkColumnStyle);
							sheet.addMergedRegion(new CellRangeAddress(
									sheet.getLastRowNum(), sheet
											.getLastRowNum(), 0, 7));
							
							row = sheet
									.createRow(sheet.getLastRowNum() + 1);
							cell = row.createCell(0);
							cell.setCellValue("Trips not Shedule for this device please contact to admin to allocate work schedule to this device.");

							sheet.addMergedRegion(new CellRangeAddress(
									sheet.getLastRowNum(), sheet
											.getLastRowNum(), 0, 7));

						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MongoException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
							
									

						
						

				

					// System.out.println("exceptiionalTrip--------Size-----------"+exceptiionalTrip.size());
					if (startBatteryStatus < 3
							&& !KeyManOffDevice_Set.contains(deviceInfoList
									.get(i).getName())&&!deviceInfoList
											.get(i).getName().contains("/LR/"))
						KeyManLowBattery_Set.add(deviceInfoList.get(i)
								.getName());

					sectionDeviceList.add(subdocument);
					
					
					if (KeyManCoverSucefullyDevices_Set.contains(deviceInfoList.get(i).getName())&&
							KeyManExceptionalDevices_Set.contains(deviceInfoList.get(i).getName())) 
						KeyManCoverSucefullyDevices_Set.remove(deviceInfoList.get(i).getName());

				} else {
					// System.out.println("patrolman Man found--------------------");
					petrolMancount++;
				}

			}

			Maindocument.put("exceptional_device", sectionDeviceList);

			report_table.insert(Maindocument);

			sheet.setColumnWidth(0, 1300);
			sheet.setColumnWidth(1, 2000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 2000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);
			sheet.setColumnWidth(7, 3000);

			if (deviceInfoList.size() > 0
					&& petrolMancount != deviceInfoList.size()) {
				String outFileName = mailSendInfo.get(j).getDept() + "_"
						+ "Exception_Trip_Report_KeyMan"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
						+ "_" + parentId + ".xlsx";
				try {
					String file = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileName;
					FileOutputStream fos = new FileOutputStream(new File(file));
					workbook.write(fos);
					fos.flush();
					fos.close();
					workbook.close();
					// String sendemail="rupesh.p@mykiddytracker.com,";

					String sendemail = "";
					for (String emaString : mailSendInfo.get(j).getEmailIds()) {
						sendemail = sendemail + "," + emaString.trim();
					}
					SendEmail mail = new SendEmail();

					
					if(isSendMail)
					 {
						 mail.sendDeviceExceptionMailToJaipur(sendemail, file,
								 mailSendInfo
								  .get(j).getDept(),"Exception Trip Report of keymen"
								  ,"Exception Trip Report of keymen",false
									,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					 }
			
					 
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// /////ALl Section////////////////////////
			
				
			System.err.println("All section--countee--"+KeyManLowBattery_Set.size()+KeyManCoverSucefullyDevices_Set.size()+
					KeyManExceptionalDevices_Set.size()+KeyManOffDevice_Set.size());
			
			
			if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
					KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1)
			
			{
		
			
			row = sheetAlertForKeymanWorkStatusReport
					.createRow(sheetAlertForKeymanWorkStatusReport
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReport);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());
			Maindocument.put("Section", mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			cell = row.createCell(1);
			cell.setCellValue(KeyManLowBattery_Set.toString().replace("[", "")
					.replace("]", ""));

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight
					.add((short) ((KeyManLowBattery_Set.toString()
							.replace("[", "").replace("]", "").toString()
							.length() / 15) * 350));

			cell = row.createCell(2);
			cell.setCellValue(KeyManOffDevice_Set.toString().replace("[", "")
					.replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManOffDevice_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(3);
			cell.setCellValue(KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManExceptionalDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(4);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); // Apply
																			// style
																			// to
																			// cell
			rowHeight.add((short) ((KeyManCoverSucefullyDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));

			cell = row.createCell(5);
			cell.setCellValue(KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", ""));
			
			
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport);// Apply // style// to// cell
			rowHeight.add((short) ((KeyManOverspeedDevices_Set.toString()
					.replace("[", "").replace("]", "").length() / 15) * 350));
			
			/*cell = row.createCell(6);
			if(KeyManExceptionalDevices_Set.size()>0)
				cell.setCellValue("Not completed successfully ");

			else
				cell.setCellValue("Completed successfully");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReport); */

			short height_row = (short) ((KeyManLowBattery_Set.toString()
					.replace("[", "").replace("]", "").toString().length() / 15) * 350);
			for (int i = 0; i < rowHeight.size(); i++) {
				if (height_row < rowHeight.get(i)) {
					height_row = rowHeight.get(i);
					System.err.println("Row height-----" + rowHeight.get(i)
							+ "----");
				}

			}
			row.setHeight(height_row);

			sheetAlertForKeymanWorkStatusReport.setColumnWidth(0, 1500);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(4, 3000);
			sheetAlertForKeymanWorkStatusReport.setColumnWidth(5, 3000);

			if (j == mailSendInfo.size() - 1) {
				String outFileNameAllSectionStatus = "All_Section_KeyMan_status_Report_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_"+parentId+ ".xlsx";
				
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReport.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReport.close();

					String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," Keyman Work Status Report AllSection",
								  " Keyman Work Status Report AllSection",true	
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					}
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

			// /////////////////////*Alll Section////////////////////
			
			
			
			
			// /////ALl Section COunt////////////////////////
			 divDeviceOffCount=divDeviceOffCount+KeyManOffDevice_Set.size();
			 divDeviceBeatNotCoverCount=divDeviceBeatNotCoverCount+KeyManExceptionalDevices_Set.size();
			 divDeviceBeatCoverCount=divDeviceBeatCoverCount+KeyManCoverSucefullyDevices_Set.size();

			
	
				 if(KeyManLowBattery_Set.size()>0||KeyManCoverSucefullyDevices_Set.size()>0||
							KeyManExceptionalDevices_Set.size()>0||KeyManOffDevice_Set.size()>0||j == mailSendInfo.size() - 1)
				 {	

			row = sheetAlertForKeymanWorkStatusReportCount
					.createRow(sheetAlertForKeymanWorkStatusReportCount
							.getLastRowNum() + 1);
			row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			cell = row.createCell(0);
			cell.setCellValue(mailSendInfo.get(j).getDept());

			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	

			cell = row.createCell(1);
			cell.setCellValue(KeyManOffDevice_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
	
			cell = row.createCell(2);
			cell.setCellValue(KeyManExceptionalDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			

			cell = row.createCell(3);
			cell.setCellValue(KeyManCoverSucefullyDevices_Set.size()+"");
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

			cell = row.createCell(4);
			cell.setCellValue((KeyManCoverSucefullyDevices_Set.size()+KeyManOffDevice_Set.size()+
					KeyManExceptionalDevices_Set.size())+"");
			
			
			cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
			
	
			
			row.setHeight((short) 600);

			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(0, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(1, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(2, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(3, 3000);
			sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(4, 3000);

			if (j == mailSendInfo.size() - 1) {
				
				//for total row
				row = sheetAlertForKeymanWorkStatusReportCount
						.createRow(sheetAlertForKeymanWorkStatusReportCount
								.getLastRowNum() + 1);
				row.setRowStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				cell = row.createCell(0);
				cell.setCellValue("Total Device");

				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
		

				cell = row.createCell(1);
				cell.setCellValue(divDeviceOffCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				;

				cell = row.createCell(2);
				cell.setCellValue(divDeviceBeatNotCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);
				
				cell = row.createCell(3);
				cell.setCellValue(divDeviceBeatCoverCount+"");
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount); 
				

				cell = row.createCell(4);
				cell.setCellValue((divDeviceOffCount+divDeviceBeatCoverCount+
						divDeviceBeatNotCoverCount)+"");
				
				
				cell.setCellStyle(wrap_styleAlertForKeymanWorkStatusReportCount);


				
				row.setHeight((short) 600);

				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(0, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(1, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(2, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(3, 3000);
				sheetAlertForKeymanWorkStatusReportCount.setColumnWidth(4, 3000);
				
				String outFileNameAllSectionStatus = "All_Section_KeyMan_Status_Device_Count_Report_"
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
							+ "_"+parentId+ ".xlsx";
				
				try {
					String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
							+ outFileNameAllSectionStatus;
					FileOutputStream fosalert = new FileOutputStream(new File(
							fileAllSection));
					workbookAlertForKeymanWorkStatusReportCount.write(fosalert);
					fosalert.flush();
					fosalert.close();
					workbookAlertForKeymanWorkStatusReportCount.close();

					String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
					SendEmail mail = new SendEmail();
					System.err.println("Send mail All Section----" + sendemail);
					/*if(isSendMail)
					{
						mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
								  "All Section"," Keyman Work Status device count Report AllSection",
								  " Keyman Work Status device count Report AllSection",true	
								  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

					}*/
						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

			// /////////////////////*Alll Section COunt end ////////////////////
			

					// /////Report  Summary ////////////////////////
			
				
					if (j == mailSendInfo.size() - 1&&exceptionSummary) {
						
						sheetSummary.setColumnWidth(0, 800);
						sheetSummary.setColumnWidth(1, 4000);
						sheetSummary.setColumnWidth(2, 3000);
						sheetSummary.setColumnWidth(3, 4000);
						sheetSummary.setColumnWidth(4, 4000);
						sheetSummary.setColumnWidth(5, 3000);
						sheetSummary.setColumnWidth(6, 5000);
						sheetSummary.setColumnWidth(7, 15000);

						
						String outFileNameAllSectionStatus = "Exception_Keyman_Device_Summary_"								
						+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
									+ "_"+parentId+ ".xlsx";
						
						
						try {
							String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
									+ outFileNameAllSectionStatus;
							FileOutputStream fosalert = new FileOutputStream(new File(
									fileAllSection));
						workbookSummary.write(fosalert);
							fosalert.flush();
							fosalert.close();
							workbookSummary.close();

							String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
							SendEmail mail = new SendEmail();
							System.err.println("Send mail All Section----" + sendemail);
							/*if(isSendMail)
							{
								mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
										  "All Section"," Keyman Work Status device count Report AllSection",
										  " Keyman Work Status device count Report AllSection",true	
										  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

							}*/
								
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					// /////////////////////*Report Summary end////////////////////
			///Insert Datewise Saction Data
			 
			if(isSendMail)
			 try {

					// System.err.println(KmStartEnd[0].trim()+"-"+KmStartEnd[1].trim()+"-"+KmStartEnd[0].trim()+"-"+TotalKmCover[j]);
					java.sql.CallableStatement ps = con
							.prepareCall("{call SaveDatewiseExceptionReport(?,?,?,?,?,?,?,?)}");

					
					ps.setInt(1, Integer.parseInt(parentId));
					ps.setString(2,DayStartWorkTime+"");
					ps.setString(3, KeyManLowBattery_Set.toString().replace("[", "")
							.replace("]", ""));

					ps.setString(4,KeyManOffDevice_Set.toString()
							.replace("[", "").replace("]", ""));

					ps.setString(5, KeyManExceptionalDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(6, KeyManOverspeedDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					ps.setString(7,mailSendInfo.get(j).getDept() );
					ps.setString(8, KeyManCoverSucefullyDevices_Set.toString()
							.replace("[", "").replace("]", ""));
					int result = ps.executeUpdate();
					if (result == 0) {
						msgo.setError("true");
						msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
					} else {
						// //System.err.println("Error=="+result);
						msgo.setError("false");
						msgo.setMessage("SaveDatewiseExceptionReport Inserted Successfully");
					}
				} catch (Exception e) {
					e.printStackTrace();
					msgo.setError("true");
					msgo.setMessage("SaveDatewiseExceptionReport not Inserted");
				}

		}// //////////for section end

		return msgo;

	}





	
	public MessageObject GetNotificationForPaymentSubscriptionAPI(Connection con) {
		MessageObject msgo = new MessageObject();
		LoginDAO loginObj=new LoginDAO();
		
		ArrayList<PaymentNotificationDTO> DeviceLisiWillBeExpire = new ArrayList<>();
		try {

			java.sql.CallableStatement ps= con.prepareCall("{call GetPaymentNofityAdmin()}");
			ResultSet rs=ps.executeQuery();
			if (rs!=null) {
				while (rs.next()) {
															
					PaymentNotificationDTO Dto=new PaymentNotificationDTO();
					Dto.setEmailId(rs.getString("EmailID").trim());
					Dto.setName(rs.getString("Name").trim());
					Dto.setStudentName(rs.getString("StudentName").trim());
					Dto.setMobileNo(rs.getString("MobileNo").trim());
					Dto.setParentId(rs.getInt("UserID"));
					Dto.setRollId(rs.getInt("Role_id"));
					Dto.setParentName(rs.getString("UserName").trim());
					Dto.setPassword(rs.getString("password"));
					Dto.setStudentId(rs.getInt("StudentID"));
					Dto.setDiffDate(rs.getString("DiffDate"));
					Dto.setExpiryDate(rs.getString("ExpiryDate"));
					Dto.setPaymentRenewDate(rs.getString("PayRenDate"));
					DeviceLisiWillBeExpire.add(Dto);		
				}
			}
		
		System.err.println("GeneratePaymentSubscriptionNotificationReport ---"
				+ DeviceLisiWillBeExpire.size());

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		
		
		long DayStartWorkTime = Long.valueOf(Common
				.getGMTTimeStampFromDate(day+ "-"
						+ String.valueOf(month + 1) + "-" + year + " 00:00 am"));
	
		XSSFWorkbook workbookSummary = new XSSFWorkbook();
		XSSFSheet sheetSummary = workbookSummary
				.createSheet(WorkbookUtil
						.createSafeSheetName("Payment Subscription Notification Report"));

		XSSFCellStyle HeadingStyleSummary = workbookSummary
				.createCellStyle();
		HeadingStyleSummary
				.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleSummary
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbookSummary.createFont();
		font.setFontHeightInPoints((short) 20);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

		XSSFCellStyle tripColumnStyleSummary = workbookSummary
				.createCellStyle();
		tripColumnStyleSummary
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		tripColumnStyleSummary
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleSummary.setWrapText(true);
		font = workbookSummary.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		CellStyle wrap_styleSummary = workbookSummary
				.createCellStyle(); // Create new style
		wrap_styleSummary.setWrapText(true); // Set
																	
																	
		XSSFRow row = sheetSummary.createRow(0);
		row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
		/*row.createCell(0);
		row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
		row.createCell(0);
		row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);	*/
		row.createCell(0);
		row.getCell(0).setCellValue("Payment Subscription Notification Report :-"
						+ Common.getDateFromLong(DayStartWorkTime));
		sheetSummary.addMergedRegion(new CellRangeAddress(
						sheetSummary.getLastRowNum(),
						sheetSummary.getLastRowNum(), 0,5));
		row.getCell(0).setCellStyle(HeadingStyleSummary);

		row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
		

		XSSFCell cell = row.createCell(0);
		cell.setCellValue("User Name");
		cell.setCellStyle(tripColumnStyleSummary);
	
		cell = row.createCell(1);
		cell.setCellValue("Password");
		cell.setCellStyle(tripColumnStyleSummary);

		cell = row.createCell(2);
		cell.setCellValue("Student Name");
		cell.setCellStyle(tripColumnStyleSummary);
		
		cell = row.createCell(3);
		cell.setCellValue("Parent Name");
		cell.setCellStyle(tripColumnStyleSummary);

		cell = row.createCell(4);
		cell.setCellValue("Mobile No");
		cell.setCellStyle(tripColumnStyleSummary);
		
		cell = row.createCell(5);
		cell.setCellValue("Diff Date");
		cell.setCellStyle(tripColumnStyleSummary);
		
		for (int j = 0; j < DeviceLisiWillBeExpire.size(); j++){
			PaymentNotificationDTO curruntObj=DeviceLisiWillBeExpire.get(j);
				
					row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);

				 cell = row.createCell(0);
				cell.setCellValue(curruntObj.getName());
			
				cell = row.createCell(1);
				cell.setCellValue(curruntObj.getPassword());

				cell = row.createCell(2);
				cell.setCellValue(curruntObj.getStudentName());
				
				cell = row.createCell(3);
				cell.setCellValue(curruntObj.getParentName());

				cell = row.createCell(4);
				cell.setCellValue(curruntObj.getMobileNo());
				
				cell = row.createCell(5);
				cell.setCellValue(curruntObj.getDiffDate());
						
		}
		sheetSummary.setColumnWidth(0, 4000);
		sheetSummary.setColumnWidth(1, 4000);
		sheetSummary.setColumnWidth(2, 4000);
		sheetSummary.setColumnWidth(3, 4000);
		sheetSummary.setColumnWidth(4, 4000);
		sheetSummary.setColumnWidth(5, 4000);
		sheetSummary.setColumnWidth(6, 4000);
		sheetSummary.setColumnWidth(7, 4000);

			String outFileName ="Payment Subscription Notification Report"
					+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-") + ".xlsx";

			try {
				String file = Common.ServerDevice_PaymentSub_Trip_FileUplod_Path_PAYSUBSCROPTION+ outFileName;
				FileOutputStream fos = new FileOutputStream(new File(file));

				workbookSummary.write(fos);
				fos.flush();
				fos.close();
				workbookSummary.close();
				String sendemail="rupesh@primesystech.com,";

				SendEmail mail = new SendEmail();
				
					 mail.sendPaymentSubscriptionMail(sendemail, file
							,"Payment Subscription Notification Report");				
			
			} catch (Exception e) {
				e.printStackTrace();
			}		
		
	}catch(Exception e){
		e.printStackTrace();
	}
		return msgo;
	}


	//Get Jabalpur Patrolman report GetPatrolmanReportSummeryInfo 
	public ArrayList<DailyReportSummaryWithStoppageDTO> GetDailySummaryOfDeviceFromReportSummaryOfPatrolMan(Connection con,
			int parentId , long DayStartWorkTime, Connection conSunMssql, int roleId) {
		ArrayList<DailyReportSummaryWithStoppageDTO> deviceReportList=new ArrayList<>();
		 DuplicateMap<String,DailyReportSummaryWithStoppageDTO> deviceReportListMap=new DuplicateMap<>();
		 ArrayList<String> deviceList=new ArrayList();
		 ArrayList<DailyReportSummaryWithStoppageDTO> deviceListReportSummary=new ArrayList();
		try {

			System.err.println("parentid--"+parentId+"---"+DayStartWorkTime+"--rolid--"+roleId);
			
		java.sql.CallableStatement psgettrip = conSunMssql
				.prepareCall("{call GetDailySummaryOfDeviceFromReportSummaryOfPatrolManWithSection(?,?,?)}");
	
		psgettrip.setInt(1,parentId);
		psgettrip.setLong(2,DayStartWorkTime);
		psgettrip.setInt(3,roleId);

		ResultSet rsgertrip = psgettrip.executeQuery();
		System.err.println("DayStartWorkTime---"+DayStartWorkTime);
		if (rsgertrip != null) {
			while (rsgertrip.next()) {

				DailyReportSummaryWithStoppageDTO dto = new DailyReportSummaryWithStoppageDTO();
				dto.setDeviceEndKm(rsgertrip.getDouble("DeviceEndKm"));
				dto.setDeviceEndTime(rsgertrip.getLong("DeviceEndTime"));
				dto.setDeviceId(rsgertrip.getString("DeviceId"));
				dto.setDeviceOvespeedCount(rsgertrip.getInt("DeviceOvespeedCount"));
				dto.setDeviceStartKm(rsgertrip.getDouble("DeviceStartKm"));
				dto.setDeviceStartTime(rsgertrip.getLong("DeviceStartTime"));
				dto.setDeviceStoppageCount(rsgertrip.getInt("DeviceStoppageCount"));
				dto.setExpectedEndKm(rsgertrip.getDouble("ExpectedEndKm"));
				dto.setExpectedEndTime(rsgertrip.getLong("ExpectedEndTime"));
				dto.setExpectedStartKm(rsgertrip.getDouble("ExpectedStartKm"));
				dto.setExpectedStartTime(rsgertrip.getLong("ExpectedStartTime"));
				dto.setName(rsgertrip.getString("Name"));
				dto.setDeviceOnTime(rsgertrip.getLong("DeviceOnTime"));
				dto.setDeviceOffTime(rsgertrip.getLong("DeviceOffTime"));
				dto.setReportOfDay(DayStartWorkTime);
				dto.setMaxSpeed(rsgertrip.getInt("MaxSpeed"));
				dto.setAvgSpeed(rsgertrip.getInt("AvgSpeed"));
				dto.setLocationCount(rsgertrip.getInt("LocationCount"));
				dto.setEndTimeDiff(rsgertrip.getInt("EndTimeDiff"));
				dto.setStartTimeDiff(rsgertrip.getInt("StartTimeDiff"));
				if (rsgertrip.getDouble("ActualKmCover")>20)
					dto.setActualKmCover(0);
				else 				
				dto.setActualKmCover(rsgertrip.getDouble("ActualKmCover"));
				dto.setExpectedKmCover(rsgertrip.getDouble("ExpectedKmCover"));
				deviceReportListMap.put(rsgertrip.getString("DeviceId"), dto);
				  if(!deviceList.contains(rsgertrip.getString("DeviceId")))
				deviceList.add(rsgertrip.getString("DeviceId"));
				  
			
			}
		}
//		System.out.println("GetDailySummaryOfDeviceFromReportSummaryOfPatrolMan--"+deviceReportListMap.size());
//		System.err.println("----ListMap=="+new Gson().toJson(deviceReportListMap));
//		for (int i = 0; i < deviceReportListMap.size(); i++) {
//			System.err.println("--device---"+deviceReportListMap.get(k));
//		}
		

		
		////////////////////////////////////////// exception Summary////////////////////////
		
		
		XSSFWorkbook workbookSummary = new XSSFWorkbook();
		XSSFSheet sheetSummary = workbookSummary.createSheet(WorkbookUtil
				.createSafeSheetName("Exception Trip Report Summary of keymen"));

		XSSFCellStyle HeadingStyleSummary = workbookSummary.createCellStyle();
		HeadingStyleSummary.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		HeadingStyleSummary.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		HeadingStyleSummary.setAlignment(HorizontalAlignment.CENTER);
		XSSFFont fontSummary = workbookSummary.createFont();
		HeadingStyleSummary.setWrapText(true); 

		XSSFFont font = workbookSummary.createFont();
		font.setBold(true);
		font.setColor(IndexedColors.WHITE.getIndex());
		HeadingStyleSummary.setFont(font);

		XSSFCellStyle tripColumnStyleSummary = workbookSummary.createCellStyle();
		tripColumnStyleSummary
				.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
						.getIndex());
		tripColumnStyleSummary.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		tripColumnStyleSummary.setWrapText(true);
		font = workbookSummary.createFont();
		font.setFontHeightInPoints((short) 15);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		// tripColumnStyle.setFont(font);

		XSSFCellStyle remarkColumnStyleSummary = workbookSummary.createCellStyle();
		remarkColumnStyleSummary.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
				.getIndex());
		remarkColumnStyleSummary.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		remarkColumnStyleSummary.setWrapText(true); 

		XSSFCellStyle error_remarkColumnStyleSummary = workbookSummary.createCellStyle();
		error_remarkColumnStyleSummary
				.setFillForegroundColor(IndexedColors.LIGHT_YELLOW
						.getIndex());
		error_remarkColumnStyleSummary
				.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont redfontSummary = workbookSummary.createFont();
		redfontSummary.setColor(IndexedColors.RED.getIndex());
		error_remarkColumnStyleSummary.setFont(redfontSummary);
		error_remarkColumnStyleSummary.setWrapText(true); 

		// loactionNotFoundColumnStyle.setFont(font);

		CellStyle wrap_styleSummary = workbookSummary.createCellStyle(); // Create new
															// style
		wrap_styleSummary.setWrapText(true); // Set wordwrap

		int petrolMancount = 0;

		 XSSFRow row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);

		row.createCell(0);
		row.getCell(0).setCellValue(
				"Exception Report for PatrolMan"
						 + " Date: "
						+ Common.getDateFromLong(DayStartWorkTime));
		row.getCell(0).setCellStyle(HeadingStyleSummary);
		
		sheetSummary.addMergedRegion(new CellRangeAddress(sheetSummary.getLastRowNum(),
				sheetSummary.getLastRowNum(), 0, 13));

		BasicDBList sectionDeviceList = new BasicDBList();
		
		row = sheetSummary.createRow(0);
		row = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);



		XSSFCell cell = row.createCell(0);
		cell.setCellValue("Sr no");
		cell.setCellStyle(tripColumnStyleSummary);


		cell = row.createCell(1);
		cell.setCellValue("Device name");
		cell.setCellStyle(tripColumnStyleSummary);

		cell = row.createCell(2);
		cell.setCellValue("Allocated Beat");
		cell.setCellStyle(tripColumnStyleSummary);
		
		cell = row.createCell(3);
		cell.setCellValue("Allocated Start Time");
		cell.setCellStyle(tripColumnStyleSummary);
		
		cell = row.createCell(4);
		cell.setCellValue("Actual Start time");
		cell.setCellStyle(tripColumnStyleSummary);
		cell = row.createCell(5);
		cell.setCellValue("Allocated End Time");
		cell.setCellStyle(tripColumnStyleSummary);
		
		cell = row.createCell(6);
		cell.setCellValue("Actual End time");
		cell.setCellStyle(tripColumnStyleSummary);
		
		
		
		cell = row.createCell(7);
		cell.setCellValue("Allocated Km");
		cell.setCellStyle(tripColumnStyleSummary);
		
		cell = row.createCell(8);
		cell.setCellValue("Actual Km");
		cell.setCellStyle(tripColumnStyleSummary);
		
		cell = row.createCell(9);
		cell.setCellValue("Allocated Trip");
		cell.setCellStyle(tripColumnStyleSummary);
		
		cell = row.createCell(10);
		cell.setCellValue("Actual Trip");
		cell.setCellStyle(tripColumnStyleSummary);
		
		cell = row.createCell(11);
		cell.setCellValue("Max Speed");
		cell.setCellStyle(tripColumnStyleSummary);
		
		cell = row.createCell(12);
		cell.setCellValue("Avg Speed");
		cell.setCellStyle(tripColumnStyleSummary);
		
		cell = row.createCell(13);
		cell.setCellValue("Remark/Exception");
		cell.setCellStyle(tripColumnStyleSummary);
		row.setHeight((short) 1200);
		
		
		
		////////////////////////////////////report Summary//////////////////////////////
		
		for (int i = 0; i < deviceList.size(); i++) {
		
			ArrayList<DailyReportSummaryWithStoppageDTO> SingleDeviceData= deviceReportListMap.get(deviceList.get(i));
			DailyReportSummaryWithStoppageDTO reportDTO=new DailyReportSummaryWithStoppageDTO();
			reportDTO.setDeviceStartTime(0);
			int compeletedTrip=0;
			int totalLocationCount=0;
			Double ActualKmCover=0.0;
			int deviceOverSpeedCount=0;
			StringBuilder beatNotCoverTrip=new StringBuilder();
			StringBuilder nearByRDPSNotFoundTrip=new StringBuilder();
			StringBuilder loactionNotFoundInTrip=new StringBuilder();
			StringBuilder moveThanOthetBeat=new StringBuilder();

			StringBuilder remark=new StringBuilder();
			XSSFRow rowSummary = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
			 Cell cellSummary = rowSummary.createCell(0);
			cellSummary.setCellValue(i+1);
			cellSummary.setCellStyle(wrap_styleSummary);
			
			cellSummary = rowSummary.createCell(1);
			cellSummary.setCellValue(SingleDeviceData.get(0).getName());
			reportDTO.setName(SingleDeviceData.get(0).getName());
			cellSummary = rowSummary.createCell(2);
			cellSummary.setCellValue(SingleDeviceData.get(0).getExpectedStartKm()+"-"+SingleDeviceData.get(0).getExpectedEndKm());
			reportDTO.setAllocatedBeat(SingleDeviceData.get(0).getExpectedStartKm()+"-"+SingleDeviceData.get(0).getExpectedEndKm());
			
			for (int j = 0; j < SingleDeviceData.size(); j++) {
				if (j==0) {
					cellSummary = rowSummary.createCell(3);
					cellSummary.setCellValue(Common.getTimeCurrentTimeZone_HH_mm(SingleDeviceData.get(j).getExpectedStartTime()));
					reportDTO.setExpectedStartTime(SingleDeviceData.get(j).getExpectedStartTime());
					
					
					cellSummary = rowSummary.createCell(7);
					cellSummary.setCellValue(SingleDeviceData.get(j).getExpectedKmCover()*SingleDeviceData.size());
					reportDTO.setExpectedKmCover(SingleDeviceData.get(j).getExpectedKmCover()*SingleDeviceData.size());
//					ActualKmCover=SingleDeviceData.get(j).getActualKmCover();
					
				}
				
				
				
//				System.err.println("-----"+reportDTO.getDeviceStartTime());
				if (SingleDeviceData.get(j).getDeviceStartTime()>0)
				{	cellSummary = rowSummary.createCell(4);
					cellSummary.setCellValue(Common.getTimeCurrentTimeZone_HH_mm(SingleDeviceData.get(j).getDeviceStartTime()));
					reportDTO.setDeviceStartTime(SingleDeviceData.get(j).getDeviceOnTime());
					reportDTO.setStartTimeDiff((int) (SingleDeviceData.get(j).getDeviceStartTime()-SingleDeviceData.get(j).getExpectedStartTime()));

				}
				else if (reportDTO.getDeviceStartTime()==0) {
				
					cellSummary = rowSummary.createCell(4);
					cellSummary.setCellValue("-");
				}
					
			
			
				
				
				
				if (j==SingleDeviceData.size()-1) {
					cellSummary = rowSummary.createCell(5);
					cellSummary.setCellValue(Common.getTimeCurrentTimeZone_HH_mm(SingleDeviceData.get(j).getExpectedEndTime()));
					reportDTO.setExpectedEndTime(SingleDeviceData.get(j).getExpectedEndTime());			
				}

				cellSummary = rowSummary.createCell(6);
				if (SingleDeviceData.get(j).getDeviceEndTime()>0)
					{
					cellSummary.setCellValue(Common.getTimeCurrentTimeZone_HH_mm(SingleDeviceData.get(j).getDeviceEndTime()));
					reportDTO.setDeviceEndTime(SingleDeviceData.get(j).getDeviceOffTime());
					
					reportDTO.setEndTimeDiff((int) (SingleDeviceData.get(j).getExpectedEndTime()-SingleDeviceData.get(j).getDeviceEndTime()));

					
					}
					else
						cellSummary.setCellValue("-");
							
				if (SingleDeviceData.get(j).getDeviceEndKm()>=0 && SingleDeviceData.get(j).getDeviceStartKm()>=0&&SingleDeviceData.get(j).getActualKmCover()<50)
				
					ActualKmCover=ActualKmCover+SingleDeviceData.get(j).getActualKmCover();
				else
					ActualKmCover=ActualKmCover+0;
				
					if (SingleDeviceData.get(j).getDeviceOvespeedCount()>deviceOverSpeedCount)
						deviceOverSpeedCount=SingleDeviceData.get(j).getDeviceOvespeedCount();
			
				cellSummary = rowSummary.createCell(8);
				cellSummary.setCellValue(ActualKmCover);
				reportDTO.setActualKmCover(ActualKmCover);
				
				totalLocationCount=totalLocationCount+SingleDeviceData.get(j).getLocationCount();

				if ((SingleDeviceData.get(j).getExpectedKmCover()-SingleDeviceData.get(j).getActualKmCover())<0.5)
					{
					compeletedTrip++;
					if (Math.abs(SingleDeviceData.get(j).getDeviceStartKm()-SingleDeviceData.get(j).getExpectedStartKm())>(SingleDeviceData.get(j).getExpectedKmCover()+10)
							||Math.abs(SingleDeviceData.get(j).getDeviceEndKm()-SingleDeviceData.get(j).getExpectedEndKm())>(SingleDeviceData.get(j).getExpectedKmCover()+10)){
						{
							if (moveThanOthetBeat.toString().length()>0) 
								moveThanOthetBeat.append(","+(j+1));
							else moveThanOthetBeat.append((j+1));
						}
							
					}
						
					}else if (SingleDeviceData.get(j).getLocationCount()>0&&(SingleDeviceData.get(j).getDeviceStartKm()==SingleDeviceData.get(j).getDeviceEndKm())) {
						//remark.append("\nNear by RDPS not found for trip :"+(j+1));
						if (moveThanOthetBeat.toString().length()>0) 
							moveThanOthetBeat.append(","+(j+1));
						else moveThanOthetBeat.append((j+1));
						
					}
				
					else if (SingleDeviceData.get(j).getLocationCount()>0&&(SingleDeviceData.get(j).getDeviceStartKm()==-1||SingleDeviceData.get(j).getDeviceEndKm()==-1)) {
						//remark.append("\nNear by RDPS not found for trip :"+(j+1));
						if (nearByRDPSNotFoundTrip.toString().length()>0) 
							nearByRDPSNotFoundTrip.append(","+(j+1));
						else nearByRDPSNotFoundTrip.append((j+1));
						
					}else if (SingleDeviceData.get(j).getLocationCount()>0&&(SingleDeviceData.get(j).getDeviceStartKm()!=-1&&SingleDeviceData.get(j).getDeviceEndKm()!=-1)) {
					/*	if(remark.toString().contains("Beat not Complete in Trip"))
							remark.append(","+(j+1));
						else
							remark.append("\nBeat not Complete in Trip:"+(j+1));*/
						if (beatNotCoverTrip.toString().length()>0) 
							beatNotCoverTrip.append(","+(j+1));
						else beatNotCoverTrip.append((j+1));
							
					}/*else if(totalLocationCount==0){
						remark.append("\nDevice was Off.");
					}*/else{
//						remark.append("\nLocation not found in Trip:"+(j+1));
						
						if (loactionNotFoundInTrip.toString().length()>0) 
							loactionNotFoundInTrip.append(","+(j+1));
						else loactionNotFoundInTrip.append((j+1));
				
						}
					
			
				
			}
			
			cellSummary = rowSummary.createCell(9);
			cellSummary.setCellValue(SingleDeviceData.size());
			reportDTO.setAllocatedTrip(SingleDeviceData.size());

			cellSummary = rowSummary.createCell(10);
			cellSummary.setCellValue(compeletedTrip);
			reportDTO.setActualTrip(compeletedTrip);
			
			cellSummary = rowSummary.createCell(11);
			cellSummary.setCellValue(SingleDeviceData.get(0).getMaxSpeed());
			reportDTO.setMaxSpeed(SingleDeviceData.get(0).getMaxSpeed());
			
			cellSummary = rowSummary.createCell(12);
			cellSummary.setCellValue(SingleDeviceData.get(0).getAvgSpeed());
			reportDTO.setAvgSpeed(SingleDeviceData.get(0).getAvgSpeed());
			reportDTO.setDeviceDetailTripList(SingleDeviceData);
			reportDTO.setLocationCount(totalLocationCount);
			
			
			cell = rowSummary.createCell(13);
			
			
				
				if (beatNotCoverTrip.length()>0)
				remark.append("Beat not complted in Trip : "+beatNotCoverTrip);
		
				if(loactionNotFoundInTrip.length()>0)
				remark.append("\nLocation not found in Trip: : "+loactionNotFoundInTrip);
				if(nearByRDPSNotFoundTrip.length()>0)
				remark.append("\nNear by RDPS not found for trip : "+nearByRDPSNotFoundTrip);
				if(moveThanOthetBeat.length()>0)
					remark.append("\nDevice moved on other than expected beat for trip : "+moveThanOthetBeat+"\nPlease check history.");
			
		
				 if (totalLocationCount==0) 
				 {
					 cell.setCellValue("Device was Off.");
					cell.setCellStyle(remarkColumnStyleSummary);
				 }else if(remark.length()>0)
					{
					cell.setCellValue(remark.toString());
					cell.setCellStyle(remarkColumnStyleSummary);
					}else
					cell.setCellValue("All work done succesfully");
			
			
			reportDTO.setRemark(cell.getStringCellValue());
			reportDTO.setDeviceOvespeedCount(deviceOverSpeedCount);
			deviceListReportSummary.add(reportDTO);
			
		}
		
		sheetSummary.setColumnWidth(0, 1200);
		sheetSummary.setColumnWidth(1, 3000);
		sheetSummary.setColumnWidth(2, 3000);
		sheetSummary.setColumnWidth(3, 3000);
		sheetSummary.setColumnWidth(4, 3000);
		sheetSummary.setColumnWidth(5, 3000);
		sheetSummary.setColumnWidth(6, 1500);
		sheetSummary.setColumnWidth(7, 1500);
		sheetSummary.setColumnWidth(8, 1500);
		sheetSummary.setColumnWidth(9, 1500);
		sheetSummary.setColumnWidth(10, 1500);
		sheetSummary.setColumnWidth(11, 1500);
		sheetSummary.setColumnWidth(12, 1500);
		sheetSummary.setColumnWidth(13, 15000);

		
		String outFileNameAllSectionStatus = "Exception_PatrolMan_Device_Summary_"								
		+ Common.getDateFromLong(DayStartWorkTime).replace(":", "-")
					+ "_"+parentId+ ".xlsx";
		
		
		try {
			String fileAllSection = Common.ServerDevice_Exception_Trip_FileUplod_Path_KEYMAN
					+ outFileNameAllSectionStatus;
			FileOutputStream fosalert = new FileOutputStream(new File(
					fileAllSection));
		workbookSummary.write(fosalert);
			fosalert.flush();
			fosalert.close();
			workbookSummary.close();

//			String sendemail = loginObj.getEmailIdForAllsectionReport(con,parentId);
//			SendEmail mail = new SendEmail();
//			System.err.println("Send mail All Section----" + sendemail);
			/*if(isSendMail)
			{
				mail.sendDeviceExceptionMailToJaipur(sendemail, fileAllSection,
						  "All Section"," Keyman Work Status device count Report AllSection",
						  " Keyman Work Status device count Report AllSection",true	
						  ,mailSendInfo.get(j).getReportSendEmailId(),mailSendInfo.get(j).getReportSendEmailPassword());

			}*/
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
		System.err.println("Get PatroilmanSummary ----"+deviceListReportSummary.size());
		return deviceListReportSummary;
	}


}
