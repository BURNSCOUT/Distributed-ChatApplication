package client;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.net.Socket;

import client.model.ChatAppClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class ChatAppClientMain extends Application {
	public static ChatAppClient client;

	public static void main(String[] args) {
		client = new ChatAppClient();
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("view/ClientLoginView.fxml"));
			Scene scene = new Scene(root, 300, 200);
			//BorderPane root = FXMLLoader.load(getClass().getResource("view/ClientLoginView.fxml"));
			//Scene scene = new Scene(root, 613, 300);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
