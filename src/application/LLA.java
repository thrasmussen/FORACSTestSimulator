package application;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import net.sf.marineapi.nmea.util.Datum;

public class LLA {
	
	private DoubleProperty latitude = new SimpleDoubleProperty(); //degrees
	private DoubleProperty longitude = new SimpleDoubleProperty(); //degrees
	private DoubleProperty altitude = new SimpleDoubleProperty(); //meters
	private Datum datum = Datum.WGS84;
	
	public LLA (double latitude,double longitude,double altitude){
		this.latitude.set(latitude); 
		this.longitude.set(longitude);
		this.altitude.set(altitude); 
	}
	
	public DoubleProperty latitudeProperty(){
		return latitude;
	}
	
	public double getLatitude() {
		return latitude.get();
	}
	
	public double getLatitudeDeg() {
		return Math.toDegrees(latitude.get());
	}
	
	public void setLatitude(double latitude) {
		this.latitude.set(latitude);
	}
	public void setLatitudeDegrees(double latitude) {
		this.latitude.set(Math.toRadians(latitude));
	}
	
	public DoubleProperty longitudeProperty(){
		return longitude;
	}	
	
	public double getLongitude() {
		return longitude.get();
	}
	public double getLongitudeDeg() {
		return Math.toDegrees(longitude.get());
	}
	
	
	public void setLongitude(double longitude) {
		this.longitude.set(longitude);
	}
	
	public void setLongitudeDegrees(double longitude) {
		this.longitude.set(Math.toRadians(longitude));
	}
	
	public DoubleProperty altitudeProperty(){
		return altitude;
	}
	public double getAltitude() {
		return altitude.get();
	}
	public void setAltitude(double altitude) {
		this.altitude.set(altitude);
	}
	
	public double[] getLLA() {
	    double[] LLA = {latitude.get(),longitude.get(),altitude.get()};
	    return LLA;
	}
	
	public String toString() {
		return "lat:"+Math.toDegrees(latitude.doubleValue())+", lon:"+Math.toDegrees(longitude.doubleValue())+", alt:"+altitude.doubleValue();
		
	};
	public String toStringRad() {
		return "lat:"+latitude.doubleValue()+", lon:"+longitude.doubleValue()+", alt:"+altitude.doubleValue();
		
	};


}
