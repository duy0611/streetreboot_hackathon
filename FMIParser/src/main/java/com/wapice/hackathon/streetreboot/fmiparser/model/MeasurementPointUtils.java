package com.wapice.hackathon.streetreboot.fmiparser.model;

public class MeasurementPointUtils {

	public static String getPointIdFromGmlId(String gmlId) {
		//obs-obs-1-1-t2m
		return gmlId.substring(gmlId.lastIndexOf("-") + 1, gmlId.length());
	}
}
