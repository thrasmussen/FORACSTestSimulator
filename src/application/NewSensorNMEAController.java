
package application;



import com.fazecast.jSerialComm.SerialPort;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class NewSensorNMEAController {
	
	

    @FXML
    private ComboBox<Integer> cmbBoudrate;

    @FXML
    private ComboBox<String> cmbComPort;

    @FXML
    private ComboBox<Integer> cmbDatabits;

    @FXML
    private ComboBox<Integer> cmbStopbit;

    @FXML
    private ComboBox<Integer> cmbParity;

    
    
	public void initialize()  {
		System.out.println("Running");

		ObservableList<String> options = 
			    FXCollections.observableArrayList();
		
			
		
		
		for(SerialPort port : SerialPort.getCommPorts()){
			options.add(port.getSystemPortName());
		}
		cmbComPort.setItems(options);
		
		
		
		ObservableList<Integer> baudList = 
			    FXCollections.observableArrayList(
			        4800,
			        9600,
			        19200
			    );
		cmbBoudrate.setItems(baudList);
		
		ObservableList<Integer> databitList = 
			    FXCollections.observableArrayList(
			        8
			      
			    );
		cmbDatabits.setItems(databitList);
		

	}
	
	@FXML
    void setComPort(ActionEvent event) {
    	System.out.println(SerialPort.getCommPort(cmbComPort.getValue()));
    	NewSensorController.sPort = SerialPort.getCommPort(cmbComPort.getValue());
    	
    	//Setting default values
		NewSensorController.sPort.setParity(SerialPort.NO_PARITY);
		NewSensorController.sPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
		NewSensorController.sPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
		
		cmbDatabits.setValue(NewSensorController.sPort.getNumDataBits());
		cmbStopbit.setValue(NewSensorController.sPort.getNumStopBits());
		cmbParity.setValue(NewSensorController.sPort.getParity());
    }
	
	@FXML
    void actionGPSToggle(ActionEvent event) {

    }

    @FXML
    void actionHeadingToggle(ActionEvent event) {

    }

    @FXML
    void actionBearingToggle(ActionEvent event) {

    }


    @FXML
    void actionBaudrate(ActionEvent event) {
    	NewSensorController.sPort.setBaudRate(cmbBoudrate.getValue());
    }

    @FXML
    void actionDatabist(ActionEvent event) {
    	NewSensorController.sPort.setNumDataBits(cmbDatabits.getValue());
    }

    @FXML
    void actionParity(ActionEvent event) {
    	NewSensorController.sPort.setParity(cmbParity.getValue());
    }

    @FXML
    void actionStopbit(ActionEvent event) {
    	NewSensorController.sPort.setNumStopBits(cmbStopbit.getValue());
    }

}
