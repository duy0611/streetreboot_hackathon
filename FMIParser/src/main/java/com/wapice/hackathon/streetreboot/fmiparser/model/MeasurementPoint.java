package com.wapice.hackathon.streetreboot.fmiparser.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;

public class MeasurementPoint {
	
	public static String GML_ID_AIR_TEMPERATURE = "t2m";
	public static String GML_ID_WIND_SPEED = "ws_10min";
	public static String GML_ID_WIND_GUST = "wg_10min";
	public static String GML_ID_WIND_DIRECTION = "wd_10min";
	public static String GML_ID_RELATIVE_HUMIDITY = "rh";
	public static String GML_ID_DEW_POINT_TEMPERATURE = "td";
	public static String GML_ID_PRECIPATION = "r_1h";
	public static String GML_ID_PRECIPATION_DENSITY = "ri_10min";
	public static String GML_ID_SNOW_DEPTH = "snow_aws";
	public static String GML_ID_AIR_PRESSURE = "p_sea";
	public static String GML_ID_VISIBILITY = "vis";
	public static String GML_ID_CLOUD_AMOUNT = "n_man";
	public static String GML_ID_PRESENT_WEATHER = "wawa";
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private String stationId;
	
	private Date timestamp;
	
	/**
	 	obs-obs-1-1-t2m
		obs-obs-1-1-ws_10min
		obs-obs-1-1-wg_10min
		obs-obs-1-1-wd_10min
		obs-obs-1-1-rh
		obs-obs-1-1-td
		obs-obs-1-1-r_1h
		obs-obs-1-1-ri_10min
		obs-obs-1-1-snow_aws
		obs-obs-1-1-p_sea
		obs-obs-1-1-vis
		obs-obs-1-1-n_man
		obs-obs-1-1-wawa
	 */
	
	private Double airTemperature; //degC - avg - 1m
	private Double windSpeed; //m/s - avg - 10m
	private Double gustSpeed; //m/s - max - 10m
	private Double windDirection; //deg - avg - 10m	
	private Double relativeHumidity; //% - avg - 1m
	private Double dewPointTemperature; //degC - avg - 1m
	private Double precipation; //mm - acc - 1h
	private Double precipationDensity; //mm/h - avg -10m
	private Double snowDepth; //cm - instant - 1m
	private Double airPressure; //hPa - avg - 1m
	private Double visibility; //m - avg - 1m
	private Double cloudAmount; //1/8 - instant - 1m
	private Double presentWeather; //no unit - rank - 1m
	
	
	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Double getAirTemperature() {
		return airTemperature;
	}
	public void setAirTemperature(Double airTemperature) {
		this.airTemperature = airTemperature;
	}
	public Double getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(Double windSpeed) {
		this.windSpeed = windSpeed;
	}
	public Double getGustSpeed() {
		return gustSpeed;
	}
	public void setGustSpeed(Double gustSpeed) {
		this.gustSpeed = gustSpeed;
	}
	public Double getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(Double windDirection) {
		this.windDirection = windDirection;
	}
	public Double getPrecipation() {
		return precipation;
	}
	public void setPrecipation(Double precipation) {
		this.precipation = precipation;
	}
	public Double getRelativeHumidity() {
		return relativeHumidity;
	}
	public void setRelativeHumidity(Double relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}
	public Double getDewPointTemperature() {
		return dewPointTemperature;
	}
	public void setDewPointTemperature(Double dewPointTemperature) {
		this.dewPointTemperature = dewPointTemperature;
	}
	public Double getPrecipationDensity() {
		return precipationDensity;
	}
	public void setPrecipationDensity(Double precipationDensity) {
		this.precipationDensity = precipationDensity;
	}
	public Double getSnowDepth() {
		return snowDepth;
	}
	public void setSnowDepth(Double snowDepth) {
		this.snowDepth = snowDepth;
	}
	public Double getAirPressure() {
		return airPressure;
	}
	public void setAirPressure(Double airPressure) {
		this.airPressure = airPressure;
	}
	public Double getVisibility() {
		return visibility;
	}
	public void setVisibility(Double visibility) {
		this.visibility = visibility;
	}
	public Double getCloudAmount() {
		return cloudAmount;
	}
	public void setCloudAmount(Double cloudAmount) {
		this.cloudAmount = cloudAmount;
	}
	public Double getPresentWeather() {
		return presentWeather;
	}
	public void setPresentWeather(Double presentWeather) {
		this.presentWeather = presentWeather;
	}
	
	public Boolean hasNull() {
		return this.airPressure==null||this.airTemperature==null||this.cloudAmount==null||
				this.dewPointTemperature==null||this.gustSpeed==null||this.precipation==null||
				this.precipationDensity==null||this.presentWeather==null||this.relativeHumidity==null||
				this.snowDepth==null||this.visibility==null||this.windDirection==null||this.windSpeed==null;
	}
	
	public String toString() {
		return this.stationId+","+
				df.format(this.timestamp)+","+
				this.airTemperature.toString() + "," +
				this.windSpeed.toString() + "," +
				this.gustSpeed.toString()+","+
				this.windDirection.toString() +","+
				this.relativeHumidity.toString()+","+
				this.dewPointTemperature.toString()+","+
				this.precipation.toString()+","+
				this.precipationDensity.toString()+","+
				this.snowDepth.toString()+","+
				this.airPressure.toString()+","+
				this.visibility.toString()+","+
				this.cloudAmount.toString()+","+
				this.presentWeather.toString()+",";
	}
	
}
