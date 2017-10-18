package com.wapice.hackathon.streetreboot.fmiparser.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import com.wapice.hackathon.streetreboot.fmiparser.model.MeasurementPoint;
import com.wapice.hackathon.streetreboot.fmiparser.model.MeasurementPointUtils;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TestFMIParser {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, DOMException, ParseException, InterruptedException {
		//testParseFromXML();
		testParseFromAPI();
		System.out.println("Done");
	}

	private static void testParseFromXML() throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(
				new File("C:\\Projects\\Hackathon\\StreetReboot\\workspace\\FMIParser\\src\\main\\resources\\xml_example\\fmi_history_data.xml"));
		
		NodeList nodeList = doc.getElementsByTagName("wml2:MeasurementTimeseries");
		for (int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) node;
				System.out.println(elem.getAttribute("gml:id"));
				
				NodeList childrenNode = elem.getChildNodes();
				for (int childIndex=0; childIndex<childrenNode.getLength(); childIndex++) {
					Node childNode = childrenNode.item(childIndex);
					
					if (childNode.getNodeType() == Node.ELEMENT_NODE) {
						Element childElem = (Element) childNode;				
						Element measurementElem = (Element) childElem.getElementsByTagName("wml2:MeasurementTVP").item(0);
						Element timeElem = (Element) measurementElem.getElementsByTagName("wml2:time").item(0);
						System.out.println(timeElem.getTextContent());
						Element valueElem = (Element) measurementElem.getElementsByTagName("wml2:value").item(0);
						System.out.println(valueElem.getTextContent());
					}
				}
			}
		}
	}
	
	private static void testParseFromAPI() throws ParserConfigurationException, IOException, SAXException, DOMException, ParseException, InterruptedException {
		Map<Long, MeasurementPoint> measurementMapByTimestamp = new HashMap<Long, MeasurementPoint>();
		String basedURL="http://data.fmi.fi/fmi-apikey/8d4ff471-09be-4257-9ebe-6ab800f4b76c/wfs?request=getFeature&storedquery_id=fmi::observations::weather::timevaluepair&place=kumpula,helsinki&timestep=120&";
		final String FILE_HEADER = "timestamp,zoneId,date,airTemperature,windSpeed,gustSpeed,windDirection,relativeHumidity,dewPointTemperature,precipation,precipationDensity,snowDepth,airPressure,visibility,cloudAmount,presentWeather";
		final String NEW_LINE_SEPARATOR = "\n";
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		c.setTime(sdf.parse("2016-11-01 00:00:00"));// all done
		
			for(int i=0; i<30;i++) {
				Date from = c.getTime();
				String fromDate = sdf.format(from);
				fromDate = String.join("T", fromDate.split(" "));
				fromDate+="Z";
				//System.out.println(fromDate);			
				c.add(Calendar.DATE, 5);
				Date to = c.getTime();
				String toDate = sdf.format(to);
				toDate = String.join("T", toDate.split(" "));
				toDate+="Z";
				//System.out.println(toDate);	
				String calledURL = basedURL + "starttime=" + fromDate + "&endtime=" + toDate;
				readData(measurementMapByTimestamp, calledURL);
			}
		/*Set<Long>removedIds = removeNull(measurementMapByTimestamp);
		for(Long id: removedIds) {
			measurementMapByTimestamp.remove(id);
		}*/
		Map<Long, MeasurementPoint> map = new TreeMap<>(measurementMapByTimestamp);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("kumpula.csv");

			//Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());
			
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			//Write a new student object list to the CSV file

			for(Map.Entry<Long, MeasurementPoint> entry: map.entrySet()) {
				System.out.println("Timestamp: " + entry.getKey() + ", value: " + entry.getValue().toString());
				fileWriter.append(entry.getKey().toString());
				fileWriter.append(",");
				fileWriter.append(entry.getValue().toString());
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			
			
			System.out.println("CSV file was created successfully !!!");
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
	}
	
	private static Set<Long> removeNull(Map<Long, MeasurementPoint> measurementMapByTimestamp) {
		Set<Long> list = new HashSet<Long>();
		for(Map.Entry<Long, MeasurementPoint> entry: measurementMapByTimestamp.entrySet()) {
			if(entry.getValue().hasNull()) {
				list.add(entry.getKey());
			}
		}
		return list;
	}
	
	private static void readData(Map<Long, MeasurementPoint> measurementMapByTimestamp, String urlString) throws IOException, SAXException, ParserConfigurationException, DOMException, ParseException {
		System.out.println(urlString);
		URL url = new URL(urlString);
		String location = "Helsinki-Kumpula";
		InputStream stream = url.openStream();
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(stream);
		
		NodeList nodeList = doc.getElementsByTagName("wml2:MeasurementTimeseries");
		System.out.println(nodeList.getLength());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) node;
				
				NodeList childrenNode = elem.getChildNodes();
				for (int childIndex=0; childIndex<childrenNode.getLength(); childIndex++) {
					Node childNode = childrenNode.item(childIndex);
					
					if (childNode.getNodeType() == Node.ELEMENT_NODE) {
						Element childElem = (Element) childNode;				
						Element measurementElem = (Element) childElem.getElementsByTagName("wml2:MeasurementTVP").item(0);
						Element timeElem = (Element) measurementElem.getElementsByTagName("wml2:time").item(0);
						Element valueElem = (Element) measurementElem.getElementsByTagName("wml2:value").item(0);
						Double value = Double.parseDouble(valueElem.getTextContent());
						//System.out.println(valueElem.getTextContent());
						Date d = formatter.parse(timeElem.getTextContent().replaceAll("T", " ").replaceAll("Z", ""));
						Long timestamp = d.getTime();
						if(measurementMapByTimestamp.get(timestamp)!=null) {
							MeasurementPoint mp = measurementMapByTimestamp.get(timestamp);
							if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_AIR_PRESSURE)){
								mp.setAirPressure(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_AIR_TEMPERATURE)) {
								mp.setAirTemperature(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_CLOUD_AMOUNT)) {
								mp.setCloudAmount(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_DEW_POINT_TEMPERATURE)) {
								mp.setDewPointTemperature(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_PRECIPATION)) {
								mp.setPrecipation(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_PRECIPATION_DENSITY)) {
								mp.setPrecipationDensity(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_PRESENT_WEATHER)) {
								mp.setPresentWeather(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_RELATIVE_HUMIDITY)) {
								mp.setRelativeHumidity(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_SNOW_DEPTH)) {
								mp.setSnowDepth(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_VISIBILITY)) {
								mp.setVisibility(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_WIND_DIRECTION)) {
								mp.setWindDirection(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_WIND_GUST)) {
								mp.setGustSpeed(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_WIND_SPEED)) {
								mp.setWindSpeed(value);
							}
						} else {
							MeasurementPoint mp = new MeasurementPoint();
							mp.setTimestamp(d);
							mp.setStationId(location);
							if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_AIR_PRESSURE)){
								mp.setAirPressure(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_AIR_TEMPERATURE)) {
								mp.setAirTemperature(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_CLOUD_AMOUNT)) {
								mp.setCloudAmount(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_DEW_POINT_TEMPERATURE)) {
								mp.setDewPointTemperature(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_PRECIPATION)) {
								mp.setPrecipation(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_PRECIPATION_DENSITY)) {
								mp.setPrecipationDensity(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_PRESENT_WEATHER)) {
								mp.setPresentWeather(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_RELATIVE_HUMIDITY)) {
								mp.setRelativeHumidity(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_SNOW_DEPTH)) {
								mp.setSnowDepth(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_VISIBILITY)) {
								mp.setVisibility(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_WIND_DIRECTION)) {
								mp.setWindDirection(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_WIND_GUST)) {
								mp.setGustSpeed(value);
							} else if(MeasurementPointUtils.getPointIdFromGmlId(elem.getAttribute("gml:id")).equals(MeasurementPoint.GML_ID_WIND_SPEED)) {
								mp.setWindSpeed(value);
							}
							measurementMapByTimestamp.put(timestamp, mp);
						}
					}
				}
			}
		}
	}
}
