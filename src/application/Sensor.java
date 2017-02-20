package application;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fazecast.jSerialComm.SerialPort;

import outputs.Outputs;

public class Sensor implements Runnable{
	
	private String name;
	private String type; 
	private String dataType;
	
	private double xParallax;
	private double yParallax;
	private double zParallax;
	private Ship ownShip;
	
	private Sector[] blindSectors;
	
	private Target target;
	
	private SerialPort comPort;

	
	

	public Sensor(){
		
	}

	public Sensor(String name, String type, double xParallax, double yParallax, double zParallax, Ship ownShip,
			Sector[] blindSectors, Target target) {
		super();
		this.name = name;
		this.type = type;
		this.xParallax = xParallax;
		this.yParallax = yParallax;
		this.zParallax = zParallax;
		this.ownShip = ownShip;
		this.blindSectors = blindSectors;
		this.target = target;
		this.dataType = "";
	}

	public SerialPort getComPort() {
		return comPort;
	}

	public void setComPort(SerialPort comPort) {
		this.comPort = comPort;
	}
	
	public String getOutputType() {
		return dataType;
	}

	public void setOutputType(String outputType) {
		this.dataType = outputType;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (ownShip.isRunning()) {
			
			try {
				Thread.sleep(1000);
				double angle = GeoCalculations.geoAngleBetweenLocations(this.getLLA(), this.target.getLLA());
				double distance = GeoCalculations.geoDistanceInMetersBetweenLocation(this.getLLA(), this.target.getLLA());			
				System.out.println("Sensor:" + name +" ,Target:" + this.target.getName() + " ,Distance=" + distance + " ,Bearing=" + Math.toDegrees(angle) + ", ownpos = " +getLLA().toString());
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
		
		
	}
	public String sendData(){
		
		byte[] b;

		switch (dataType) {
			case "SIIS":
				System.out.println("Sensor: "+name+" is sending SIIS data");
			return "";
			case "NMEA":
				System.out.println("Sensor: "+name+" is sending NMEA data");
				b = outputs.Outputs.NMEAstring(this);
				comPort.openPort();
				comPort.writeBytes(b, b.length);
				comPort.closePort();
				
			return b.toString();
				
			
			case "EMM":
				System.out.println("Sensor: "+name+" is sending EMM data");	
				b = outputs.Outputs.EMMstring(this);
				comPort.openPort();
				comPort.writeBytes(b, b.length);
				comPort.closePort();
				
			return b.toString();
			
			case "MPS":
				System.out.println("Sensor: "+name+" is sending MPS data");
			return "";
			
			default:
				System.out.println("Sensor: "+name+" is sending default data");
			return "";
			
		}
		
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getxParallax() {
		return xParallax;
	}

	public void setxParallax(double xParallax) {
		this.xParallax = xParallax;
	}

	public double getyParallax() {
		return yParallax;
	}

	public void setyParallax(double yParallax) {
		this.yParallax = yParallax;
	}

	public double getzParallax() {
		return zParallax;
	}

	public void setzParallax(double zParallax) {
		this.zParallax = zParallax;
	}

	public Ship getOwnShip() {
		return ownShip;
	}

	public void setOwnShip(Ship ownShip) {
		this.ownShip = ownShip;
	}

	public Sector[] getBlindSectors() {
		return blindSectors;
	}

	public void setBlindSectors(Sector[] blindSectors) {
		this.blindSectors = blindSectors;
	}

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}
	
	public LLA getLLA(){
		return GeoCalculations.geoPosFromParallax(ownShip.getShipLLA(), xParallax, yParallax, zParallax, ownShip.getShipHeading());
	}
	private void sendSIIS(){
		//UDP or serial
	}	
	private void sendNMEA(){
		//serial
		
		
		
		
		
		
	}
	private void sendEMM(){
		//serial
		
	}
	private void sendMPS(){
		//UDP or serial
		
	}
	
	public void printSettings(){
		String s = "";
		s += "Name: " +name + "\n" ;
		s += "Type: " +type + "\n" ;
		s += "X Parallax: " +xParallax + "\n" ;
		s += "y Parallax: " +yParallax + "\n" ;
		s += "Z Parallax: " +zParallax + "\n" ;
		s += "Ownship: " +ownShip + "\n" ;
		s += "Blind sectors: " +blindSectors + "\n" ;
		s += "Target: " +target + "\n" ;
		s += "Output type: " +dataType + "\n" ;
		s += "ComPort: " +comPort + "\n" ;
		
		
		System.out.println(s);
		
	}
	
	
	
	
	
	

}
