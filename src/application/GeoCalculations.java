package application;

import application.LLA;

public class GeoCalculations {

	
	public static final double EARTH_RADIUS_IN_METER= 6378137;
	
	
	public static double geoDistanceInMetersBetweenLocation(double lat1, double lat2, double lon1, double lon2){
		double dlat = Math.abs(lat1-lat2);
		double dlon = Math.abs(lon1-lon2);
		double a = Math.pow((Math.sin(dlat/2)), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow((Math.sin(dlon/2)),2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return EARTH_RADIUS_IN_METER * c;
	}
	public static double geoDistanceInMetersBetweenLocation(LLA pos1, LLA pos2){
		double dlat = Math.abs(pos1.getLatitude()-pos2.getLatitude());
		double dlon = Math.abs(pos1.getLongitude()-pos2.getLongitude());
		double a = Math.pow((Math.sin(dlat/2)), 2) + Math.cos(pos1.getLatitude()) * Math.cos(pos2.getLatitude()) * Math.pow((Math.sin(dlon/2)),2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return EARTH_RADIUS_IN_METER * c;
	}
	
	
	public static double geoAngleBetweenLocations(double lat1, double lat2, double lon1, double lon2) {
		double dlat = lat2-lat1;
		double dlon = lon2-lon1;
		double angle = (Math.atan2(dlat,dlon) * 180) / Math.PI;
		return Math.toRadians(angle);
	}
	public static double geoAngleBetweenLocations(LLA pos1, LLA pos2) {
		double dlat = pos2.getLatitude()-pos1.getLatitude();
		double dlon = pos2.getLongitude()-pos1.getLongitude();
		double angle = (Math.atan2(dlat,dlon) * 180) / Math.PI;
		return Math.toRadians(angle);
	}
	
	public static LLA geoPosFromParallax(LLA pos1, double x,double y,double z, double bearing){
		double brng = Math.atan(y/x) - bearing;
		double d = Math.sqrt(y*y+x*x);
		double lat2 = Math.asin(Math.sin(pos1.getLatitude())*Math.cos(d/EARTH_RADIUS_IN_METER) + Math.cos(pos1.getLatitude())*Math.sin(d/EARTH_RADIUS_IN_METER)*Math.cos(brng));
		double lon2 = pos1.getLongitude() + Math.atan2(Math.sin(brng)*Math.sin(d/EARTH_RADIUS_IN_METER)*Math.cos(pos1.getLatitude()), Math.cos(d/EARTH_RADIUS_IN_METER)-Math.sin(pos1.getLatitude())*Math.sin(lat2));
		return new LLA(lat2, lon2, pos1.getAltitude()+z); 
	}
	public static LLA geoPosFromParallax(Sensor s){
		double x = s.getxParallax();
		double y = s.getyParallax();
		double z = s.getzParallax();
		double bearing = s.getOwnShip().getShipHeading();
		LLA pos1 = s.getOwnShip().getShipLLA();
		double brng = Math.atan(y/x) - bearing;
		double d = Math.sqrt(y*y+x*x);
		double lat2 = Math.asin(Math.sin(pos1.getLatitude())*Math.cos(d/EARTH_RADIUS_IN_METER) + Math.cos(pos1.getLatitude())*Math.sin(d/EARTH_RADIUS_IN_METER)*Math.cos(brng));
		double lon2 = pos1.getLongitude() + Math.atan2(Math.sin(brng)*Math.sin(d/EARTH_RADIUS_IN_METER)*Math.cos(pos1.getLatitude()), Math.cos(d/EARTH_RADIUS_IN_METER)-Math.sin(pos1.getLatitude())*Math.sin(lat2));
		return new LLA(lat2, lon2, pos1.getAltitude()+z); 
	}
	
	public static LLA geoPosFromDistance(LLA pos1, double d, double bearing){		
		double lat2 = Math.asin(Math.sin(pos1.getLatitude())*Math.cos(d/EARTH_RADIUS_IN_METER) + Math.cos(pos1.getLatitude())*Math.sin(d/EARTH_RADIUS_IN_METER)*Math.cos(bearing));
		double lon2 = pos1.getLongitude() + Math.atan2(Math.sin(bearing)*Math.sin(d/EARTH_RADIUS_IN_METER)*Math.cos(pos1.getLatitude()), Math.cos(d/EARTH_RADIUS_IN_METER)-Math.sin(pos1.getLatitude())*Math.sin(lat2));
		return new LLA(lat2, lon2, pos1.getAltitude()); 
	}
	
	
	
}
