package com.wapice.hackathon.streetreboot.teconerparser.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class SubArea {

	private String id;
	private String name;
	
	private Double latitude;
	private Double longitude;
	
	public static List<SubArea> SUBAREA_LIST = new ArrayList<>();
	
	static {
		File file = new File("C:\\Projects\\Hackathon\\StreetReboot\\workspace\\TeconerParser\\src\\main\\resources\\subarea_helsinki.csv");
		InputStream stream;
		try {
			stream = new FileInputStream(file);
			
			final CSVParser parser = new CSVParserBuilder()
					 .withSeparator(',')
					 .withIgnoreQuotations(true)
					 .build();

			final CSVReader reader = new CSVReaderBuilder(new InputStreamReader(stream))
					 .withSkipLines(1)
					 .withCSVParser(parser)
					 .build();
			
			reader.forEach(new Consumer<String[]>() {

				@Override
				public void accept(String[] value) {
					SubArea area = new SubArea(
							value[0], value[1], 
							Double.parseDouble(value[2]), 
							Double.parseDouble(value[3]));
					
					SUBAREA_LIST.add(area);
				}
				
			});
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public SubArea(String id, String name, Double latitude, Double longitude) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	
}
