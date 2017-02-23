package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.marineapi.nmea.util.Position;



public class Ship implements Runnable {
	
	private String shipName;
	private String shipType;
	private String shipHullnumber;
	private double shipLength;
	private double shipWidth;
	private DoubleProperty shipHeading = new SimpleDoubleProperty();
	private DoubleProperty shipHeadingDegrees = new SimpleDoubleProperty();
	private DoubleProperty shipPitch = new SimpleDoubleProperty();
	private DoubleProperty shipRoll = new SimpleDoubleProperty();
	private DoubleProperty shipRudder = new SimpleDoubleProperty();
	private DoubleProperty shipSpeed = new SimpleDoubleProperty();
	private ArrayList<Sensor> shipSensors = new ArrayList<Sensor>();
	private LLA shipLLA;
	private double shipLastUpdate = 0;
	private boolean running;
	private Lock lock = new Lock() {	


		
		
		@Override
		public void unlock() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean tryLock() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public Condition newCondition() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void lockInterruptibly() throws InterruptedException {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void lock() {
			// TODO Auto-generated method stub
			
		}
	};
	private ArrayList<LLA> shipHistory = new ArrayList<LLA>();
	
	public Ship (String shipName, String shipType, String shipHullnumber, double shipLenght, double shipwidth ){
		this.shipName = shipName;
		this.shipType = shipType;
		this.shipHullnumber = shipHullnumber;
		this.shipLength = shipLenght;
		this.shipWidth = shipwidth;
		this.shipHeading.set(0);
		this.shipPitch.set(0); 
		this.shipRoll.set(0);  
		this.shipLLA = new LLA(0, 0, 0);
		this.running = true;
		this.shipRudder.set(0);
		
		shipHeadingDegrees.bind(shipHeading.multiply(180/Math.PI));
		
	}
	
	public void setShipLastUpdated(){
		shipLastUpdate = System.nanoTime();
	}
	
	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}

	public String getShipHullnumber() {
		return shipHullnumber;
	}

	public void setShipHullnumber(String shipHullnumber) {
		this.shipHullnumber = shipHullnumber;
	}

	public double getShipLength() {
		return shipLength;
	}

	public void setShipLength(double shipLength) {
		this.shipLength = shipLength;
	}

	public double getShipWidth() {
		return shipWidth;
	}

	public void setShipWidth(double shipWidth) {
		this.shipWidth = shipWidth;
	}

	public double getShipHeading() {
		return Math.atan2(Math.sin(shipHeading.get()), Math.cos(shipHeading.get()));
	}
	public double getShipHeadingDegrees360() {
		if (Math.toDegrees(Math.atan2(Math.sin(shipHeading.get()), Math.cos(shipHeading.get()))) < 0)
		return Math.toDegrees(Math.atan2(Math.sin(shipHeading.get()), Math.cos(shipHeading.get()))) + 360 ;
		else return Math.toDegrees(Math.atan2(Math.sin(shipHeading.get()), Math.cos(shipHeading.get())));
	}

	public void setShipHeading(double shipHeading) {
		this.shipHeading.set(Math.atan2(Math.sin(shipHeading), Math.cos(shipHeading)));	
	}
	
	public DoubleProperty shipHeadingProperty(){
		return this.shipHeading;
	}
	
	public DoubleProperty shipHeadingDegreesProperty(){
		return this.shipHeadingDegrees;
	}

	public double getShipPitch() {
		return shipPitch.get();
	}

	public void setShipPitch(double shipPitch) {
		this.shipPitch.set(shipPitch);
	}
	
	public DoubleProperty shipPitchProperty(){
		return this.shipPitch;
	}

	public double getShipRoll() {
		return shipRoll.get();
	}

	public void setShipRoll(double shipRoll) {
		this.shipRoll.set(shipRoll);
	}
	public DoubleProperty shipRollProperty(){
		return this.shipRoll;
	}

	public double getShipRudder() {
		return shipRudder.get();
	}

	public void setShipRudder(double shipRudder) {
		this.shipRudder.set(shipRudder);
	}
	
	public DoubleProperty shipRudderProperty(){
		return this.shipRudder;
	}

	public double getShipSpeed() {
		return shipSpeed.get();
	}

	public void setShipSpeed(double shipSpeed) {
		this.shipSpeed.set(shipSpeed);
	}
	
	public DoubleProperty shipSpeedProperty(){
		return this.shipSpeed;
	}

	public ArrayList<Sensor> getShipSensors() {
		return shipSensors;
	}

	public void setShipSensors(ArrayList<Sensor> shipSensors) {
		this.shipSensors = shipSensors;
	}

	public LLA getShipLLA() {
		return shipLLA;
	}

	public void setShipLLA(LLA shipLLA) {
		this.shipLLA = shipLLA;
	}

	public boolean isRunning() {
		return running;
	}


	public void setRunning(boolean running) {
		this.running = running;
	}


	@Override
	public void run() {

		while (running) {
			lock.lock();
			shipHistory.add(shipLLA);


			
			this.updatePosition();
			lock.unlock();
			System.out.println(getShipName() + ": LLA:" + getShipLLA().getLatitude() + ", " + getShipLLA().getLongitude() + ", HDT: "+ Math.toDegrees(getShipHeading()));
			
			try {
				udpSenderTest();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			 
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		}
		
	}
	
	public void updatePosition () {
		
		
		double time = System.nanoTime();
	    double delta_time =  ((time - shipLastUpdate) / 1000000000);
	    shipLastUpdate = time;
	    
	    
		shipHeading.set(shipHeading.doubleValue() + shipRudder.doubleValue() * 0.0001); 
		//shipLLA = new LLA(newLat, newLon, shipLLA.getAltitude());	
		shipLLA = GeoCalculations.geoPosFromDistance(shipLLA, shipSpeed.doubleValue() * delta_time, shipHeading.doubleValue());
		
	}
	
	public Sensor addSensor(Sensor s){
		s.setOwnShip(this);
		shipSensors.add(s);
		return s;
		
	}
	public Sensor addSensor(String name, String type, double xParallax, double yParallax, double zParallax, Target target){
		Sensor s = new Sensor(name, type, xParallax, yParallax, zParallax, this, null, target);
		return s;
	}
	/**
	 * 
	 * @throws IOException
	 */
	private void udpSenderTest() throws IOException{
		String string = "HDT:" + this.getShipHeading() ;
		byte[] byteArray = string.getBytes() ;
		DatagramSocket s = new DatagramSocket();
		s.connect(InetAddress.getByName("213.213.213.213"), 8888);
		DatagramPacket p = new DatagramPacket(byteArray, byteArray.length);
		s.send(p);
		s.close();
	}
	
	

	

}

