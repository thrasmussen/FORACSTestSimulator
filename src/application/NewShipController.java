package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewShipController {
	@FXML TextField txtShipName;
	@FXML TextField txtShipType;
	@FXML TextField txtShipHullnumber;
	@FXML TextField txtShipLength;
	@FXML TextField txtShipWidth;
	
	
	public void btnNewShipOk(ActionEvent event){
		System.out.println(txtShipName.getText() + " " + txtShipType.getText() + " " + txtShipHullnumber.getText());
		
		
		Ship ship = new Ship(txtShipName.getText(), txtShipType.getText(), txtShipHullnumber.getText(), Double.parseDouble(txtShipLength.getText()), Double.parseDouble(txtShipWidth.getText()));
		MainController.setSUT(ship);
		System.out.println("New ship created: " + MainController.getSUT().getShipName());
		
		
		 Stage stage = (Stage) txtShipName.getScene().getWindow();
		    // do what you have to do
		    stage.close();
	}
	
}
