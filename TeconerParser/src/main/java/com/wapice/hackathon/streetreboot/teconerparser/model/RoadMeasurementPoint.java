package com.wapice.hackathon.streetreboot.teconerparser.model;

import java.util.Date;

public class RoadMeasurementPoint {

	private Date timestamp;
	
	private Double airTemperature;
	private Double surfaceTemperature;
	private Double waterThickness;
	
	private Double latitude;
	private Double longitude;
	
	private Double friction;

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

	public Double getSurfaceTemperature() {
		return surfaceTemperature;
	}

	public void setSurfaceTemperature(Double surfaceTemperature) {
		this.surfaceTemperature = surfaceTemperature;
	}

	public Double getWaterThickness() {
		return waterThickness;
	}

	public void setWaterThickness(Double waterThickness) {
		this.waterThickness = waterThickness;
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

	public Double getFriction() {
		return friction;
	}

	public void setFriction(Double friction) {
		this.friction = friction;
	}
	
	@Override
	public String toString() {
		return "RoadMeasurementPoint["
				+ "timestamp= " + timestamp
				+ ", airTemperature= " + airTemperature
				+ ", surfaceTemperature= " + surfaceTemperature
				+ ", waterThickness= " + waterThickness
				+ ", friction= " + friction
				+ ", latitude= " + latitude
				+ ", longitude= " + longitude
				+ "]";
	}
}
