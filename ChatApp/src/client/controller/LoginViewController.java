package client.controller;

import java.io.BufferedReader;
import java.io.IOException;

import client.ChatAppClientMain;
import client.model.ChatAppClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginViewController {
    @FXML
    private Label errorLabel;

    @FXML
    private TextField usernameInput;

    @FXML
    private Button submitButton;

    private ChatAppClient client;
    
    @FXML
    void LoginOnClick(ActionEvent event) {
    	String user = this.usernameInput.getText();
    	ChatAppClientMain.client.getOutgoing().println(user);
    	if(this.checkLogin(user)) {
    		this.openMainWindow();
    	} else {
    		this.errorLabel.setVisible(true);
    	}
    }
    
    private boolean checkLogin(String user) {
    	BufferedReader reader = null;
    	try {
    		reader = new BufferedReader(ChatAppClientMain.client.getIncoming());
    	} catch (NullPointerException e) {
    		System.out.println("Server is offline");
    	}
    	
    	String message = null;
    	try {
    		message = reader.readLine();
    	} catch (IOException e) {
    		System.out.println("Failed to check username");
    	}
    	
    	if(message.equals("true")) {
    		ChatAppClientMain.client.setUsername(user);
    		return true;
    	}
    	return false;
    }
    
    private void openMainWindow() {
    	this.errorLabel.getScene().getWindow().hide();
    	System.out.println("Fired");
    	try {
    		Stage stage = new Stage();
    		Parent root = FXMLLoader.load(getClass().getResource("../view/ClientView.fxml"));
    		Scene scene = new Scene(root, 613, 300);
    		stage.setScene(scene);
    		stage.show();
    		System.out.println("Made it");
    	} catch (Exception e) {
			// TODO: handle exception
    		System.out.println(e.getMessage());
		}
    }
}
