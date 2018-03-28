package client.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import client.ChatAppClientMain;
import client.io.LogWriter;
import client.model.MessageItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ChatAppClientController {

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
	private ArrayList<MessageItem> items;

	public ChatAppClientController() {
	}

	public void initialize() {
		this.MessagesView.setItems(this.messages);
		Thread messageView = new Thread(messagesViewUpdater());
		Thread incomingMessage = new Thread(createIncomingMessageRunnable());
		messageView.start();
		incomingMessage.start();
		this.items = new ArrayList<MessageItem>();
	}

	@FXML
	void SendMessage(ActionEvent event) {
		String message = this.MessageBox.getText();
		this.MessageBox.clear();
		message = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ","
				+ ChatAppClientMain.client.getUserName() + "," + message;

		ChatAppClientMain.client.getOutgoing().println(message);
	}

	@FXML
	void DisconnectFromServer(ActionEvent event) {
		try {
			LogWriter.writeToLogFile(ChatAppClientMain.client.getUserName(), this.items);
			ChatAppClientMain.client.getSocket().close();
			ChatAppClientMain.client.getIncoming().close();
			ChatAppClientMain.client.getOutgoing().close();
			System.exit(0);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println("Client failed to unbind from server");
		}
	}

	private Runnable messagesViewUpdater() {
		return () -> {
			while (!ChatAppClientMain.client.getSocket().isClosed()) {
				if (!ChatAppClientMain.client.getMessages().isEmpty()) {
					String message = ChatAppClientMain.client.getMessages().pop();
					MessageItem current = new MessageItem(message);
					this.items.add(current);
					try {
						this.messages.add(current.toString());
					} catch (IllegalStateException e) {

					}
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
			while (!ChatAppClientMain.client.getSocket().isClosed()) {
				try {
					message = reader.readLine();
				} catch (IOException e) {
					System.out.println("Failed to get message stream from server");
				} catch (NullPointerException e) {
					break;
				}
				if (message != null && !message.equals("")) {
					ChatAppClientMain.client.getMessages().push(message);
				}
			}
		};
	}
}
