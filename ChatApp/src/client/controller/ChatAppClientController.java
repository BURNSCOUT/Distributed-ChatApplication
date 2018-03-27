package client.controller;

import java.io.BufferedReader;
import java.io.IOException;

import client.ChatAppClientMain;
import client.model.ChatAppClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
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

	private ObservableList<String> messages = FXCollections.observableArrayList();
	
	public ChatAppClientController(ChatAppClient client) {
	}
	
	public void initialize() {
		this.MessagesView.setItems(this.messages);
		Thread messageView = new Thread(messagesViewUpdater());
		Thread incomingMessage = new Thread(createIncomingMessageRunnable());
		messageView.start();
		incomingMessage.start();
		
	}

	@FXML
	void SendMessage(ActionEvent event) {
		String message = this.MessageBox.getText();
		this.MessageBox.clear();
		message = ChatAppClientMain.client.getUserName() + ": " + message;

		ChatAppClientMain.client.getOutgoing().println(message);
		// I think this is done not sure though
	}

	@FXML
	void DisconnectFromServer(ActionEvent event) {
		try {
			ChatAppClientMain.client.getSocket().close();
			ChatAppClientMain.client.getIncoming().close();
			ChatAppClientMain.client.getOutgoing().close();
		} catch (IOException e) {
			System.out.println("Client failed to unbind from server");
		}

		// Hide the current window
		// re-show user name input in case they want to reconnect
	}

	private Runnable messagesViewUpdater() {
		return () -> {
			while (!ChatAppClientMain.client.getSocket().isClosed()) {
				if (!ChatAppClientMain.client.getMessages().isEmpty()) {
					String message = ChatAppClientMain.client.getMessages().pop();
					// somehow put message in messagesview
					this.messages.add(message);
				}
			}
		};
	}
	
	private Runnable createIncomingMessageRunnable() {
		return () -> {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(ChatAppClientMain.client.getIncoming());
			} catch (NullPointerException e) {
				System.out.println("Server is offline");
			}
			String message = null;
			while(!ChatAppClientMain.client.getSocket().isClosed()) {
				try {
					message = reader.readLine();
				} catch (IOException e) {
					System.out.println("Failed to get message stream from server");
				} catch (NullPointerException e) {
					break;
				}
				if(message != null && !message.equals("")) {
					ChatAppClientMain.client.getMessages().push(message);
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
