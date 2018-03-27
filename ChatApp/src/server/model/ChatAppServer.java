package server.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.HashMap;

public class ChatAppServer {

	private static ServerSocket server;
	private static HashMap<String, Socket> clients;
	private final static int PORT = 4225;
	private volatile static ArrayDeque<String> messages;
	private static Object lock = new Object();
	private static Object lock2 = new Object();

	public static void run() {
		Runnable hookRun = () -> {
			try {
				server.close();
			} catch (Exception e) {
				System.out.println("Server closing failed");
			}
		};
		Runtime.getRuntime().addShutdownHook(new Thread(hookRun));
		messages = new ArrayDeque<String>();
		Thread sendingThread = new Thread(createSendingRunnable());
		sendingThread.start();
		server = null;
		clients = new HashMap<String, Socket>();

		try {
			server = new ServerSocket(PORT);
		} catch (Exception e) {
			System.out.println(e);
		}

		while (true) {
			Socket client = null;
			try {
				client = server.accept();
			} catch (IOException e) {
				System.out.println("Failed to accept client socket");
				e.printStackTrace();
			}

			if (client != null) {
				Thread current = new Thread(createRunnable(client));
				current.start();
			}
		}
	}

	private static Runnable createRunnable(Socket client) {
		return () -> {
			InputStreamReader input = null;
			BufferedReader reader = null;
			try {
				input = new InputStreamReader(client.getInputStream());
				reader = new BufferedReader(input);
				
				boolean pass = true;
				String user = null;
				PrintStream stream = new PrintStream(client.getOutputStream());
				while(pass) {
					user = reader.readLine();
					synchronized(lock2) {
						if(user != null && !user.equals("") && clients.get(user) == null) {
							clients.put(user, client);
							stream.println("true");
							messages.push(LocalDateTime.now() + ",Server," + user + " has joined the chat room.");
							pass = false;
						} else  {
							stream.println("false");
						}
					}
				}

				while (!client.isClosed()) {
					String msg = reader.readLine();
					if(msg != null) {
						System.out.println("Server recieved msg: " + msg);
						synchronized(lock) {
							messages.push(msg);
						}
					} else {
						clients.remove(user);
						client.close();
						input.close();
						reader.close();
						System.out.println("I have disconnected");
					}
				}

			} catch (IOException e) {
				System.out.println("Error occured while communicating with client");
				e.printStackTrace();
			}
		};
	}

	private static Runnable createSendingRunnable() {
		return () -> {
			PrintStream stream = null;
			while (true) {
				if (!messages.isEmpty()) {
					for (Socket current : clients.values()) {
						try {
							stream = new PrintStream(current.getOutputStream());
							System.out.println(messages.peek());
							stream.println(messages.peek());

						} catch (IOException e) {
							System.out.println("Server failed to send message to a client");
						}
					}
					messages.pop();
				}
			}
		};

	}
}
