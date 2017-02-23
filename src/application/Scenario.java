package application;

import java.io.IOException;
import java.security.SignatureException;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;

public class Scenario extends Task<Integer>{

	private Ship shipUnderTest;
	private Range range = new Range();
	private LongProperty updateCount = new SimpleLongProperty(0);
	
	
	

	public Ship getShipUnderTest() {
		return shipUnderTest;
	}
	public LongProperty updateCountProperty(){
		return this.updateCount;
	}



	public void setShipUnderTest(Ship shipUnderTest) {
		this.shipUnderTest = shipUnderTest;
	}




	@Override
	protected Integer call() throws Exception {
		
		for(Sensor s: shipUnderTest.getShipSensors()){
			System.out.println("Starting thread " + s );
			new Thread(s).start();
		}
		while (shipUnderTest.isRunning()) {
			
			shipUnderTest.updatePosition();
			
//			System.out.println(shipUnderTest.getShipName() + ": LLA:" + shipUnderTest.getShipLLA().getLatitudeDeg() + ", " + shipUnderTest.getShipLLA().getLongitudeDeg() + ", HDT: "+ Math.toDegrees(shipUnderTest.getShipHeading()));
			
//			for(Sensor sensor : shipUnderTest.getShipSensors()){
//				sensor.sendData();
//			}
			System.out.println(shipUnderTest.getShipLLA().toString());
			updateCount.set(updateCount.get() + 1);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		}
		
		return null;
	}




	public long getUpdateCount() {
		return updateCount.longValue();
	}




	public Range getRange() {
		return range;
	}



}
