package outputs;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import application.GeoCalculations;
import application.LLA;
import application.Sensor;
import javafx.geometry.Pos;
import net.sf.marineapi.nmea.parser.SentenceFactory;
import net.sf.marineapi.nmea.sentence.GGASentence;
import net.sf.marineapi.nmea.sentence.HDTSentence;
import net.sf.marineapi.nmea.sentence.TalkerId;
import net.sf.marineapi.nmea.util.GpsFixQuality;
import net.sf.marineapi.nmea.util.Position;
import net.sf.marineapi.nmea.util.Time;
import net.sf.marineapi.nmea.util.Units;


public class Outputs {
	
	
	public static byte[] EMMstring(Sensor s){
		SimpleDateFormat sdfDate = new SimpleDateFormat("hh:mm:ss");
		LLA sensorLLA = GeoCalculations.geoPosFromParallax(s);
		Position pos = new Position(Math.toDegrees(sensorLLA.getLatitude()),Math.toDegrees(sensorLLA.getLongitude()));
		System.out.println(pos.toString());
		
		Date now = new Date();
		String strDate = sdfDate.format(now);		
		String string = "^000 1 "+ strDate + " " + decDegToDegMinForEMM(pos) + " "+ "2" +" " +"10"+" "+"01"+" "+sensorLLA.getAltitude() +  " 0.000000 "; // checksum.... need to make good EMM converter
			int sum = 0;
			for (char c : string.toCharArray()){
				sum+= (int)c;
			}
		String hex = Integer.toHexString(sum);

		String checksum = hex.substring(Math.max(hex.length() - 2, 0));

		
		
		
		string +=  checksum +"\r\n" ;
		byte[] b = string.getBytes();
		return b;
		
	}
	
	public static byte[] NMEAstring(Sensor s){
		Time t = new Time(); 
		LLA sensorLLA = s.getLLA();
		System.out.println(sensorLLA.toString() + "NMEA string");
		Position pos = new Position(Math.toDegrees(sensorLLA.getLatitude()),Math.toDegrees(sensorLLA.getLongitude()));
		
	
		
		SentenceFactory sf = SentenceFactory.getInstance();
		HDTSentence hdt = (HDTSentence) sf.createParser(TalkerId.IN, "HDT");
		hdt.setHeading(s.getOwnShip().getShipHeadingDegrees360());
		String hdtS = hdt.toSentence();
		GGASentence gga = (GGASentence) sf.createParser(TalkerId.GP, "GGA");
		gga.setAltitude(sensorLLA.getAltitude());
		gga.setPosition(pos);
		gga.setAltitudeUnits(Units.METER);
		gga.setGeoidalHeight(0);
		gga.setSatelliteCount(9);
		gga.setHorizontalDOP(1.5);
		gga.setTime(t);
		gga.setFixQuality(GpsFixQuality.NORMAL);
		String string = hdtS+ "\r\n" + gga.toSentence()+"\r\n";
		byte[] b = string.getBytes();
		return b;
	}
	
	public static byte[] MPSstring(Sensor s){
		String string = "Not implemented";
		byte[] b = string.getBytes();
		return b;
	}
	
	public static byte[] SIISstring(Sensor s){
		String string = "Not implemented";
		byte[] b = string.getBytes();
		return b;
	}
	
	public static String decDegToDegMinForEMM(Position p){
		double lat = p.getLatitude();
		double lon = p.getLongitude();
		
		double latDeg = Math.floor(lat);
		double lonDeg = Math.floor(lon);
		
		double latMin = (lat - latDeg)*60; 
		double lonMin = (lon - lonDeg)*60;

	
		
		DecimalFormat numberFormat = new DecimalFormat("00.00000");
		NumberFormat longi = new DecimalFormat("000");
		NumberFormat lati = new DecimalFormat("00");
		
		return lati.format(latDeg) + ":" + numberFormat.format(latMin) + p.getLatitudeHemisphere().toChar() + " " + longi.format(lonDeg) + ":" + numberFormat.format(lonMin) + p.getLongitudeHemisphere().toChar();
	}
	
	static String stringToHex(String string) {
		  StringBuilder buf = new StringBuilder(200);
		  for (char ch: string.toCharArray()) {
		    if (buf.length() > 0)
		      buf.append(' ');
		    buf.append(String.format("%04x", (int) ch));
		  }
		  return buf.toString();
		}
	 static long toAscii(String s){
        StringBuilder sb = new StringBuilder();
        long asciiInt;
        for (int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            asciiInt = (int)c; 
            System.out.println(c +"="+asciiInt);
            sb.append(asciiInt);
        }
        return Long.parseLong(sb.toString());
}
	
	

}
