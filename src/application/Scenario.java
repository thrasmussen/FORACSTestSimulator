package application;

import java.io.IOException;

import javafx.concurrent.Task;

public class Scenario extends Task<Integer>{

	private Ship shipUnderTest;
	private Range range = new Range();
	
	
	

	public Ship getShipUnderTest() {
		return shipUnderTest;
	}




	public void setShipUnderTest(Ship shipUnderTest) {
		this.shipUnderTest = shipUnderTest;
	}




	@Override
	protected Integer call() throws Exception {
		
		
		while (shipUnderTest.isRunning()) {
			
			shipUnderTest.updatePosition();
			
			System.out.println(shipUnderTest.getShipName() + ": LLA:" + shipUnderTest.getShipLLA().getLatitudeDeg() + ", " + shipUnderTest.getShipLLA().getLongitudeDeg() + ", HDT: "+ Math.toDegrees(shipUnderTest.getShipHeading()));
			
			for(Sensor sensor : shipUnderTest.getShipSensors()){
				sensor.sendData();
			}

			 
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		}
		
		return null;
	}




	public Range getRange() {
		return range;
	}



}
