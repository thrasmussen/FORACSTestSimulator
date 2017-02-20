package outputs;

import java.text.SimpleDateFormat;
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
		SimpleDateFormat sdfDate = new SimpleDateFormat("hhmmss");
		Date now = new Date();
		String strDate = sdfDate.format(now);		
		String string = "^000 1 "+ strDate + " " + "59:03.8300000N" + " " + "05:35.971430W" + " "+ "2" +" " +"10"+" "+"1.5"+" "+s.getLLA().getAltitude() + "\r\n"; // checksum.... need to make good EMM converter
		byte[] b = string.getBytes();
		return b;
		
	}
	
	public static byte[] NMEAstring(Sensor s){
		Time t = new Time(); 
		LLA sensorLLA = GeoCalculations.geoPosFromParallax(s);
		Position pos = new Position(Math.toDegrees(sensorLLA.getLatitude()),Math.toDegrees(sensorLLA.getLongitude()));
		System.out.println("POS : " +pos.toString());

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
	
	

}
