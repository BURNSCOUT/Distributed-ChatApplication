package client.controller;

import java.io.BufferedReader;
import java.io.IOException;

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
    void initialize() {
    	this.client = new ChatAppClient();
    }
    
    @FXML
    void LoginOnClick(ActionEvent event) {
    	String user = this.usernameInput.getText();
    	this.client.getOutgoing().println(user);
    	if(this.checkLogin(user)) {
    		this.openMainWindow();
    	} else {
    		this.errorLabel.setVisible(true);
    	}
    }
    
    private boolean checkLogin(String user) {
    	BufferedReader reader = null;
    	try {
    		reader = new BufferedReader(this.client.getIncoming());
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
    		this.client.setUsername(user);
    		return true;
    	}
    	return false;
    }
    
    private void openMainWindow() {
    	this.errorLabel.getScene().getWindow().hide();	
    	try {
    		Stage stage = new Stage();
    		FXMLLoader loader = FXMLLoader.load(getClass().getResource("../view/ClientLoginView.fxml"));
    		loader.setController(new ChatAppClientController(this.client));
    		Parent root = loader.load();
    		Scene scene = new Scene(root, 613, 300);
    		stage.setScene(scene);
    		stage.show();
    	} catch (Exception e) {
			// TODO: handle exception
		}
    }
}
