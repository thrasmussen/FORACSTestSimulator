package application;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.fazecast.jSerialComm.*;
public class NewSensorController implements EventHandler<ActionEvent> {
	
	
	public static SerialPort sPort;

 

	@FXML
    private TextField txtSensorParallaxX;

    @FXML
    private TextField txtSensorParallaxY;
    
    @FXML
    private TextField txtSensorParallaxZ;
    
    @FXML
    private TextField txtSensorName;

    @FXML
    private TextField txtSensorType;
    
    @FXML
    private AnchorPane choosOutputArea;
    
    @FXML
    private MenuButton chooseOutputSelector;
    
    @FXML
    private ComboBox<String> chooseTarget;
    
    private Sensor s;

    @FXML
    void btnNewSensorOk(ActionEvent event) {
    	System.out.println("Close");
    	//Sensor s = MainController.getSUT().addSensor(new Sensor(txtSensorName.getText(), txtSensorType.getText(), Double.parseDouble(txtSensorParallaxX.getText()), Double.parseDouble(txtSensorParallaxY.getText()), Double.parseDouble(txtSensorParallaxZ.getText()),null, null, null));
    	System.out.println(Double.parseDouble(txtSensorParallaxX.getText()));
    	System.out.println(Double.parseDouble(txtSensorParallaxY.getText()));
    	System.out.println(Double.parseDouble(txtSensorParallaxZ.getText()));
    	
    	
    	s.setName(txtSensorName.getText());
    	s.setType(txtSensorType.getText());
    	s.setxParallax(Double.parseDouble(txtSensorParallaxX.getText()));
    	s.setyParallax(Double.parseDouble(txtSensorParallaxY.getText()));
    	s.setzParallax(Double.parseDouble(txtSensorParallaxZ.getText()));
    	s.setOwnShip(MainController.getSUT());
    	
    	
    	
    	s.setComPort(sPort);
    	MainController.newSensorToTree(s);
    	MainController.getSUT().addSensor(s);
    	s.printSettings();
		Stage stage = (Stage) txtSensorName.getScene().getWindow();
		    // do what you have to do
		    stage.close();
    }
    
    
	public void initialize()  {
		s = new Sensor();
		
		ObservableList<String> options = 
			    FXCollections.observableArrayList();
		for(Target target : MainController.getScenario().getRange().getTargets()){
			options.add(target.toString());
		}
		chooseTarget.setItems(options);
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
	
	}
	
	@FXML
    void chooseNMEA(ActionEvent event) {
		s.setOutputType("NMEA");
		System.out.println("NMEA");
		chooseOutputSelector.setText("NMEA");
		choosOutputArea.getChildren().clear();

		Parent root;
		try {
			root = FXMLLoader.load(getClass().getClassLoader().getResource("application/NewSensorNMEA.fxml"));
			choosOutputArea.getChildren().add(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    }

    @FXML
    void chooseSIIS(ActionEvent event) {
    	s.setOutputType("SIIS");
    	System.out.println("SIIS");
    	chooseOutputSelector.setText("SIIS");
		choosOutputArea.getChildren().clear();
    	
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getClassLoader().getResource("application/NewSensorSIIS.fxml"));
			choosOutputArea.getChildren().add(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void chooseEMM(ActionEvent event) {
    	System.out.println("EMM");
    	chooseOutputSelector.setText("EMM");
    	s.setOutputType("EMM");
    	
		choosOutputArea.getChildren().clear();
    	
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getClassLoader().getResource("application/NewSensorEMM.fxml"));
			choosOutputArea.getChildren().add(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	

    	
    	
    	
    	
    
    }

    @FXML
    void chooseMPS(ActionEvent event) {
    	s.setOutputType("MPS");
    	System.out.println("MPS");
    	chooseOutputSelector.setText("MPS");
    	choosOutputArea.getChildren().clear();
    }
   
    @FXML
    void actionTargetChoosen(ActionEvent event) {

    }

	

}
