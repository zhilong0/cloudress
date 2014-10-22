package com.tt.common.utils;

public abstract class MathUtils {

	public final static double AVERAGE_RADIUS_OF_EARTH = 6371.001;

	public static int calculateDistance(double lat1, double lng1, double lat2, double lng2) {
		double latDistance = Math.toRadians(lat1 - lat2);
		double lngDistance = Math.toRadians(lng1 - lng2);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
				* Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH * c * 1000));
	}
}
