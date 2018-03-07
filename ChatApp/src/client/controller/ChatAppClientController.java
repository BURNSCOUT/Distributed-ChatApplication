package client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ChatAppClientController {

	// TODO Create run method that handles the flow of the program
	@FXML
    private Button disconnectButton;

    @FXML
    private Button sendMessageButton;

    @FXML
    private ListView<String> CurrentUsersList;
    
    @FXML
    private ListView<String> MessagesView;

    @FXML
    private Label ConnectionStatus;

    @FXML
    private TextArea MessageBox;

    @FXML
    void SendMessage(ActionEvent event) {

    }

    @FXML
    void DisconnectFromServer(ActionEvent event) {

    }

}
