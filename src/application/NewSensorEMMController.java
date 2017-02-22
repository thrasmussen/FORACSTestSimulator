package application;

import com.fazecast.jSerialComm.SerialPort;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class NewSensorEMMController {

    @FXML
    private ComboBox<String> cmbComPort;

    @FXML
    void setComPort(ActionEvent event) {
    	NewSensorController.sPort = SerialPort.getCommPort(cmbComPort.getValue());
    	NewSensorController.sPort.setBaudRate(9600);
    	NewSensorController.sPort.setParity(SerialPort.NO_PARITY);
    	NewSensorController.sPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
    	NewSensorController.sPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
    	
    }
    
    public void initialize()  {
		ObservableList<String> options = 
			    FXCollections.observableArrayList();
		
			
		
		
		for(SerialPort port : SerialPort.getCommPorts()){
			options.add(port.getSystemPortName());
		}
		cmbComPort.setItems(options);
    }
}
