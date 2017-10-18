package com.wapice.hackathon.streetreboot.teconerparser.main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.wapice.hackathon.streetreboot.teconerparser.model.DistanceUtils;
import com.wapice.hackathon.streetreboot.teconerparser.model.RoadMeasurementPoint;
import com.wapice.hackathon.streetreboot.teconerparser.model.SubArea;
import com.wapice.hackathon.streetreboot.teconerparser.model.SubAreaMeasurementPoint;

public class TestTeconerParser {

	public static void main(String[] args) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		List<SubAreaMeasurementPoint> finalList = new ArrayList<>();
		
		String baseUrl = "https://keliapu.net/data/2017-03/";
		List<String> csvUrls = getListCsvUrls(baseUrl);
		
		System.out.println("There are " + csvUrls.size() + " files totally...");
		
		int count = 0;
		for (String csv : csvUrls) {
			doScanCsv(csv, finalList);
			
			count++;
			System.out.println("Read " + count + " out of " + csvUrls.size() + " files");
		}
		
		FileWriter writer = new FileWriter("teconer_output_201703.csv", false);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String BREAK_LINE = "\n";
		
		//headers
		writer
			.append("SubAreaId,TimeStamp,airTemperature,surfaceTemperature,waterThickness,friction")
			.append(BREAK_LINE);
		
		//values
		for (SubAreaMeasurementPoint p : finalList) {
			writer
				.append(p.getSubAreaId()).append(",")
				.append(sdf.format(p.getTimestamp())).append(",")
				.append(p.getAirTemperature().toString()).append(",")
				.append(p.getSurfaceTemperature().toString()).append(",")
				.append(p.getWaterThickness().toString()).append(",")
				.append(p.getFriction().toString())
				.append(BREAK_LINE)
				;
		}
		
		writer.close();
	}
	
	private static List<String> getListCsvUrls(String baseUrl) throws IOException {
		URL url = new URL(baseUrl);
		InputStream stream = url.openStream();
		
		List<String> resultList = new ArrayList<>();
		try {
			String[] strArr = IOUtils.toString(stream).split("\n");
			for (String str : strArr) {
				if(str.contains("href=") && str.contains("csv")) {
					resultList.add(baseUrl + str.substring(str.indexOf("href=") + 6, str.indexOf(".csv") + 4));
				}
			}
			
		} finally {
			IOUtils.closeQuietly(stream);
		}
		return resultList;
	}
	
	private static void doScanCsv(String csvUrl, List<SubAreaMeasurementPoint> finalList) throws IOException {
		URL url = new URL(csvUrl);
		InputStream stream = url.openStream();

		final CSVParser parser = new CSVParserBuilder()
									 .withSeparator(',')
									 .withIgnoreQuotations(true)
									 .build();
		
		 final CSVReader reader = new CSVReaderBuilder(new InputStreamReader(stream))
									 .withSkipLines(1)
									 .withCSVParser(parser)
									 .build();
		 
		 //read csv file to RoadMeasurementPoint
		 List<RoadMeasurementPoint> resultList = new ArrayList<>();
		 
		 reader.forEach(new Consumer<String[]>() {

			@Override
			public void accept(String[] value) {
				try {
					/**
					 * Timestamp: 0+1
					 * Friction: 5
					 * Air temp: 7
					 * Surface temp: 9
					 * Water: 13
					 * Latitude: 16
					 * Longitude: 17
					 */
					RoadMeasurementPoint point = new RoadMeasurementPoint();
					
					//2017.01.09 10:25:33
					SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
					point.setTimestamp(format.parse(value[0] + " " + value[1]));
					
					point.setLatitude(Double.parseDouble(value[16]));
					point.setLongitude(Double.parseDouble(value[17]));
					
					point.setAirTemperature(Double.parseDouble(value[7]));
					point.setSurfaceTemperature(Double.parseDouble(value[9]));
					point.setWaterThickness(Double.parseDouble(value[13]));
					
					point.setFriction(Double.parseDouble(value[5]));
					
					resultList.add(point);
				}
				catch(Exception e) {
					//invalid data, ignore
					System.err.println("Invalid data: " + e.getMessage());
				}
			}
			 
		 });
		 
		 //group the value according to sub_aera - timestep
		 Map<String, Map<Integer, List<RoadMeasurementPoint>>> subAreaMap = new HashMap<>();
		 
		 resultList.stream().forEach(new Consumer<RoadMeasurementPoint>() {

			@Override
			public void accept(RoadMeasurementPoint t) {
				String subAreaId = findClosestSubArea(t);
				if(subAreaId == null || subAreaId.isEmpty()) {
					return; //skip this value
				}
				
				int timeStep = ZonedDateTime.ofInstant(t.getTimestamp().toInstant(), ZoneId.systemDefault())
						.getHour() / 2;
				
				if( subAreaMap.get(subAreaId) == null) {
					subAreaMap.put(subAreaId, new HashMap<>());
				}
				
				if(subAreaMap.get(subAreaId).get(timeStep) == null) {
					subAreaMap.get(subAreaId).put(timeStep, new ArrayList<>());
				}
				
				subAreaMap.get(subAreaId).get(timeStep).add(t);
			}
			 
		 });
		 
		 //calculate average
		 List<SubAreaMeasurementPoint> subAreaPointList = new ArrayList<>();
		 for (String subAreaId : subAreaMap.keySet()) {
			 Map<Integer, List<RoadMeasurementPoint>> timeStepMap = subAreaMap.get(subAreaId);
			 for (Integer timeStep : timeStepMap.keySet()) {
				 List<RoadMeasurementPoint> points = timeStepMap.get(timeStep);
				 
				 SubAreaMeasurementPoint avgPoint = new SubAreaMeasurementPoint();
				 avgPoint.setSubAreaId(subAreaId);
				 
				 avgPoint.setAirTemperature(points.stream().collect(Collectors.averagingDouble(t -> t.getAirTemperature())));
				 avgPoint.setFriction(points.stream().collect(Collectors.averagingDouble(t -> t.getFriction())));
				 avgPoint.setSurfaceTemperature(points.stream().collect(Collectors.averagingDouble(t -> t.getSurfaceTemperature())));
				 avgPoint.setWaterThickness(points.stream().collect(Collectors.averagingDouble(t -> t.getWaterThickness())));
				 
				 Calendar calendar = Calendar.getInstance();
			     calendar.setTime(points.get(0).getTimestamp());
			     
			     calendar.set(Calendar.MILLISECOND, 0);
		         calendar.set(Calendar.SECOND, 0);
		         calendar.set(Calendar.MINUTE, 0);
		         calendar.set(Calendar.HOUR, timeStep * 2);
		         
				 avgPoint.setTimestamp(calendar.getTime());
				 
				 subAreaPointList.add(avgPoint);
			 }
		 }
		 
		 //print out the result
		 //for (SubAreaMeasurementPoint p : subAreaPointList) {
		 //	 System.out.println(p.toString());
		 //}
		 
		 finalList.addAll(subAreaPointList);
		 
		 System.out.println("Done scanning: " + csvUrl);
	}
	
	private static String findClosestSubArea(RoadMeasurementPoint value) {
		double minDistance = Double.MAX_VALUE;
		String subAreaId = "";
		for (SubArea area : SubArea.SUBAREA_LIST) {
			double distance = DistanceUtils.calcualteDistance(
					area.getLatitude(), area.getLongitude(), 
					value.getLatitude(), value.getLongitude());
			
			if(distance <= minDistance) {
				minDistance = distance;
				subAreaId = area.getId();
			}
		}
		
		if(minDistance > 5000) {
			return null;
		}
		
		return subAreaId;
	}
}
