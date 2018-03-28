package client.model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayDeque;

public class ChatAppClient {

	private final String HOST = "160.10.217.143";
	private final int PORT = 4225;
	private volatile ArrayDeque<String> messages;
	private Socket client;
	private InputStreamReader incoming;
	private PrintStream outgoing;
	private String userName;

	public ChatAppClient() {
		this.messages = new ArrayDeque<String>();
		try {
			this.client = new Socket(HOST, PORT);
			this.outgoing = new PrintStream(this.client.getOutputStream());
			this.incoming = new InputStreamReader(this.client.getInputStream());
		} catch (IOException e) {
			System.out.println("Failed to connect to server");
		}
	}

	public InputStreamReader getIncoming() {
		return this.incoming;
	}

	public PrintStream getOutgoing() {
		return this.outgoing;
	}

	public ArrayDeque<String> getMessages() {
		return this.messages;
	}

	public Socket getSocket() {
		return this.client;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUsername(String username) {
		this.userName = username;
	}
}
