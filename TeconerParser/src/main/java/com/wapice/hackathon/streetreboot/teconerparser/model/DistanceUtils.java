package com.wapice.hackathon.streetreboot.teconerparser.model;

public class DistanceUtils {

	/**
	 * Return the distance in meters
	 * 
	 * @param lat1
	 * @param long1
	 * @param lat2
	 * @param long2
	 * @return
	 */
	public static double calcualteDistance(Double lat1, Double long1, Double lat2, Double long2) {
		try {
			double earthRadius = 6371000; //meters
		    double dLat = Math.toRadians(lat2-lat1);
		    double dLng = Math.toRadians(long2-long1);
		    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
		               Math.sin(dLng/2) * Math.sin(dLng/2);
		    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		    float dist = (float) (earthRadius * c);
	
		    return dist;
		}
		catch(Exception e) {
			System.err.println("Error when calculate distance: " + e.getMessage());
		}
		return Double.MAX_VALUE;
	}
}
