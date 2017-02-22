package application;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.GFXEventHandler;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.control.skin.IntegerFieldSkin;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import com.fazecast.jSerialComm.*;

public class MainController implements EventHandler<ActionEvent>, MapComponentInitializedListener{
	
	private static Ship SUT;
	private ExecutorService executor = Executors.newCachedThreadPool();
	private static Target t1 = new Target("Generic Target 1", "radar", "yep", new LLA(0, 0, 0));
	private static Target t2 = new Target("Generic Target 2", "radar", "yep", new LLA(2, 2, 0));;
	private static Scenario scenario = new Scenario();
	
	
	public static Scenario getScenario() {
		return scenario;
	}
	@FXML Slider slideVelocityControll;
	@FXML Slider slideRudderControll;
	@FXML TextField txtRudder;
	@FXML TextField txtHeading;
	@FXML TextField txtVelocity;
	@FXML TreeView<String> treeView;
	@FXML Slider slideAltitudeControll;
	@FXML TextField txtAltitude;
	@FXML Slider slideHeadingControll;
    @FXML VBox centerVbox;
    @FXML TextField txtLatitude;
    @FXML TextField txtLongitude;
    @FXML ListView<String> logList;

    
    
    @FXML Label label;
    
    GoogleMap map;
    GoogleMapView mapView;
    Marker ownShipMarker;
    
    Sensor s1 ;
	Sensor s2 ;
	
	
	static TreeItem<String> treeRoot = new TreeItem<String>("root");
	static TreeItem<String> treeOwnShip = new TreeItem<String>("OwnShip");
	static TreeItem<String> treeTargets = new TreeItem<String>("Targets");

	
	
	public void initialize()  {
		
		System.out.println("**** Printing ports ****");
		for (SerialPort port : SerialPort.getCommPorts()) {
			System.out.println(port.toString());
			System.out.println(port.getSystemPortName());
		}
		System.out.println("**** Printing ports end ****");
		
		///////////////////////////// MATH TEST /////////////////////////////////////////
		
		LLA test = new LLA(Math.toRadians(5), Math.toRadians(5), 0);
		test = GeoCalculations.geoPosFromDistance(test, 1000, 0);
		System.out.println("test: " + test.toString());
		
		///////////////////////////// Scenario Init /////////////////////////////////////////
		
		scenario.getRange().addTarget(t1);
		scenario.getRange().addTarget(t2);
		
		//////////////////////////// SHIP INIT //////////////////////////////////////////


		SUT = new Ship("HMS Neversail", "", "", 1,1);
		LLA start = new LLA(Math.toRadians(58.991106), Math.toRadians(5.714579), 0);
		SUT.setShipLLA(start);
		scenario.setShipUnderTest(SUT);
		
		
		//////////////////////////// MAP INIT //////////////////////////////////////////
		mapView = new GoogleMapView();
		map = new GoogleMap();
		mapView.addMapInializedListener(this);
		
		
		
		centerVbox.getChildren().add(mapView);
		
		//////////////////////////// LISTENERS AND BINDINGS //////////////////////////////////////////
		
		//Rudder
		Bindings.bindBidirectional(scenario.getShipUnderTest().shipRudderProperty(),slideRudderControll.valueProperty());
		Bindings.bindBidirectional(txtRudder.textProperty(), scenario.getShipUnderTest().shipRudderProperty(), new NumberStringConverter());
		//Speed
		Bindings.bindBidirectional(scenario.getShipUnderTest().shipSpeedProperty(), slideVelocityControll.valueProperty());
		Bindings.bindBidirectional(txtVelocity.textProperty(), scenario.getShipUnderTest().shipSpeedProperty(), new NumberStringConverter());		
		//Heading
		Bindings.bindBidirectional(txtHeading.textProperty(), slideHeadingControll.valueProperty(), new NumberStringConverter());
		
		//txtLatitude.textProperty().bind(Bindings.format("%.2f", scenario.getShipUnderTest().getShipLLA().latitudeProperty()));
		System.out.println(scenario.getShipUnderTest().getShipLLA().getLatitude());
		Bindings.bindBidirectional(txtLatitude.textProperty(), scenario.getShipUnderTest().getShipLLA().latitudeProperty(), new NumberStringConverter());
		Bindings.bindBidirectional(txtLongitude.textProperty(), scenario.getShipUnderTest().getShipLLA().longitudeProperty(), new NumberStringConverter());
		//slideHeadingControll.valueProperty().bind(scenario.getShipUnderTest().shipHeadingDegreesProperty());
//		Bindings.bindBidirectional(scenario.getShipUnderTest().shipHeadingProperty(), slideHeadingControll.valueProperty());
//		Bindings.bindBidirectional(txtHeading.textProperty(), scenario.getShipUnderTest().shipHeadingProperty(), new NumberStringConverter());	
		//Altitude

		
	
	//	slideRudderControll.valueProperty().bindBidirectional(txtRudder.textProperty(), new NumberStringConverter());
		
		//txtRudder.textProperty().bind(Bindings.format("%.2f", slideRudderControll.valueProperty()));
//		txtHeading.textProperty().bind(Bindings.format("%.2f", slideHeadingControll.valueProperty()));
//		txtAltitude.textProperty().bind(Bindings.format("%.2f", slideAltitudeControll.valueProperty()));
//		txtVelocity.textProperty().bind(Bindings.format("%.2f", slideVelocityControll.valueProperty()));


		
		
		
//		SUT.shipSpeedProperty().addListener((v, oldValue, newValue)-> {
//			scenario.getShipUnderTest().setShipSpeed(newValue.doubleValue());
//			//SUT.setShipSpeed(newValue.doubleValue());
//		});
		
//		SUT.getShipLLA().latitudeProperty().addListener((v, oldValue, newValue)-> {
//			System.out.println("update");
//			MarkerOptions mOptions = new MarkerOptions();
//	        Marker marker = new Marker(mOptions);
//			LatLong pos = new LatLong(SUT.getShipLLA().getLatitude(), SUT.getShipLLA().getLatitude());
//			
//			 marker.setPosition(pos);
//		     map.addMarker(marker);
//		     
//		});
		
//		scenario.getShipUnderTest().getShipLLA().latitudeProperty().addListener((v, oldValue, newValue)->{
//			LLA currPos = SUT.getShipLLA();
//			ownShipMarker.setPosition(new LatLong(currPos.getLatitudeDeg(),currPos.getLongitudeDeg()));
//			
//			
//			
//		});
//		
//		scenario.getShipUnderTest().getShipLLA().longitudeProperty().addListener((v, oldValue, newValue)->{
//			LLA currPos = SUT.getShipLLA();
//			ownShipMarker.setPosition(new LatLong(currPos.getLatitudeDeg(),currPos.getLongitudeDeg()));
//			
//			
//		});
		
		scenario.getShipUnderTest().shipHeadingProperty().addListener((v, oldValue, newValue)->{
			System.out.println("Heading update");
		
			slideHeadingControll.setValue(scenario.getShipUnderTest().getShipHeading() * (180 / Math.PI));
		    
		     
		});
		
		scenario.updateCountProperty().addListener((v, oldValue, newValue)->{
//			System.out.println("update " + newValue);
//			if((int)newValue%10 == 0){
//				System.out.println("mod100");
//			}
//			LLA currPos = SUT.getShipLLA();
//			ownShipMarker.setPosition(new LatLong(currPos.getLatitudeDeg(),currPos.getLongitudeDeg()));
//			map.setCenter(new LatLong(currPos.getLatitudeDeg(),currPos.getLongitudeDeg()));
		});
		
		
//		slideHeadingControll.valueProperty().addListener((observable, oldValue, newValue) -> {
//			scenario.getShipUnderTest().setShipHeading(newValue.doubleValue() * (Math.PI * 180));
//
//		});
		
//		slideVelocityControll.valueProperty().addListener((observable, oldValue, newValue) -> {
//			SUT.setShipSpeed(newValue.doubleValue());
//		});		
		
		
		//Test sensors
		
		s1 = new Sensor("Radar1", "Radar", -10, -10, 0, SUT, null, t1);
		s2 = new Sensor("Radar2", "Radar", 20, 20, 0, SUT, null, t1);
		scenario.getShipUnderTest().addSensor(s1);
		scenario.getShipUnderTest().addSensor(s2);
		
//TreeView
		
		
		
		treeOwnShip.setExpanded(true);
		treeTargets.setExpanded(true);
		//treeOwnShip.getChildren().add(test);
		
		System.out.println(scenario.getShipUnderTest().getShipSensors().size());
		for(Sensor sensor : scenario.getShipUnderTest().getShipSensors()){
			TreeItem<String> sensorItem = new TreeItem<String>(sensor.getName());
			treeOwnShip.getChildren().add(sensorItem);
		}
		for(Target target : scenario.getRange().getTargets()){
			TreeItem<String> targetItem = new TreeItem<String>(target.getName());
			treeTargets.getChildren().add(targetItem);
		}
		treeRoot.isExpanded();

		treeRoot.getChildren().add(treeOwnShip);
		treeRoot.getChildren().add(treeTargets);
		treeView.setRoot(treeRoot);
		treeView.setShowRoot(false);
		
		
		
		
		
	}



	

	public static Ship getSUT() {
		return SUT;
		
	}

	public static void setSUT(Ship sUT) {
		SUT = sUT;
	}

	public void btnNewShip(ActionEvent event){
		System.out.println("New Ship");
		Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("application/NewShip.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Ship Configuration");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void btnNewSensor(ActionEvent event){
		System.out.println("New Sensor");
		Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("application/NewSensor.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Sensor Configuration");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void btnNewTarget(ActionEvent event){
		System.out.println("New Target");
	}
	
	public void btnOpenShip(ActionEvent event) {
		System.out.println("Open Ship");		
	}
	
	public void btnOpenSensor(ActionEvent event) {
		System.out.println("Open Sensor");		
	}
	
	public void btnOpenTarget(ActionEvent event) {
		System.out.println("Open test Target");		
	}
	
	public void btnSaveObject(ActionEvent event) {
		System.out.println("Save Object");		
	}
	
	public void btnStartSimulation(ActionEvent event){
		SUT.setRunning(true);
		System.out.println("Start Simulation");
		scenario.getShipUnderTest().setShipLastUpdated();
		new Thread(scenario).start();

		//SUT.setRunning(true);
		System.out.println("Thread Ship");
		
		
		//executor.execute(SUT);

		
		
//		Sensor[] sensors = SUT.getShipSensors();
//		for (int i = 0; i < sensors.length; i++) {
//			executor.execute(sensors[i]);			
//		}
		
	}
	
	public void btnPauseSimulation(ActionEvent event){
		LLA currPos = SUT.getShipLLA();
		ownShipMarker.setPosition(new LatLong(currPos.getLatitudeDeg(),currPos.getLongitudeDeg()));
		map.setCenter(new LatLong(currPos.getLatitudeDeg(),currPos.getLongitudeDeg()));
		System.out.println("Pause Simulation");

		
	}
	
	public void btnStopSimulation(ActionEvent event){
		System.out.println("Stop Simulation");
		SUT.setRunning(false);
		
	}
	
	public void setRudder(ActionEvent event){
		double val = Double.parseDouble(txtRudder.getText());
		SUT.setShipRudder(val);
		System.out.println(SUT.getShipRudder());
		
	}


	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void mapInitialized() {
		LatLong center = new LatLong(0, 0); 
        mapView.addMapReadyListener(() -> { 
            // This call will fail unless the map is completely ready. 
            
        }); 
         
        MapOptions options = new MapOptions(); 
        options.center(new LatLong(scenario.getShipUnderTest().getShipLLA().getLatitudeDeg(), scenario.getShipUnderTest().getShipLLA().getLongitudeDeg())) 
                .zoom(12) 
                .overviewMapControl(true) 
                .panControl(false) 
                .rotateControl(false) 
                .scaleControl(false) 
                .streetViewControl(false) 
                .zoomControl(true) ;
               // .mapType(MapTypeIdEnum.TERRAIN); 
 
        map = mapView.createMap(options); 
         
 
        MarkerOptions mOptions = new MarkerOptions();
       mOptions.icon("http://findicons.com/files/icons/1273/jolly_roger_vol_2/128/ship.png");
        
        ownShipMarker = new Marker(mOptions);
        ownShipMarker.setPosition(new LatLong(scenario.getShipUnderTest().getShipLLA().getLatitude(), scenario.getShipUnderTest().getShipLLA().getLongitude()));
        ownShipMarker.setTitle("OwnShip");
        
        mapView.setPrefHeight(1000);

        map.addMarker(ownShipMarker);
        
        

		
	}
	
	
	public static void newSensorToTree(Sensor s){
		TreeItem<String> sensorItem = new TreeItem<String>(s.getName());
		treeOwnShip.getChildren().add(sensorItem);
	}

}
