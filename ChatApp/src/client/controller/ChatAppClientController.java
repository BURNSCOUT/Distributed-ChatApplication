package client.controller;

import java.io.IOException;

import client.model.ChatAppClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ChatAppClientController {

	private ChatAppClient client;

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

	public void initialize() {
		this.client = new ChatAppClient();
		Thread messageView = new Thread(messagesViewUpdater());
		messageView.start();
	}

	@FXML
	void SendMessage(ActionEvent event) {
		String message = this.MessageBox.getText();
		this.MessageBox.clear();

		this.client.getOutgoing().println(message);
		// I think this is done not sure though
	}

	@FXML
	void DisconnectFromServer(ActionEvent event) {
		try {
			this.client.getSocket().close();
			this.client.getIncoming().close();
			this.client.getOutgoing().close();
		} catch (IOException e) {
			System.out.println("Client failed to unbind from server");
		}

		// Hide the current window
		// re-show user name input in case they want to reconnect
	}

	private Runnable messagesViewUpdater() {
		return () -> {
			while (true) {
				if (!this.client.getMessages().isEmpty()) {
					String message = this.client.getMessages().pop();
					// somehow put message in messagesview
				}
			}
		};
	}
	
	private String parseMessage(String serverMessage) {
		String[] splitstr = serverMessage.split(",");
		String formattedMessage = String.format("[%s] %s : %s", splitstr[0], splitstr[1], splitstr[2]);
		
		return formattedMessage;
	}

}
