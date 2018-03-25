package server.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.HashMap;

public class ChatAppServer {

	private static ServerSocket server;
	private static HashMap<String, Socket> clients;
	private final static int PORT = 4225;
	private volatile static ArrayDeque<String> messages;
	private static Object lock = new Object();

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

		int count = 1;

		while (true) {
			Socket client = null;
			try {
				client = server.accept();
			} catch (IOException e) {
				System.out.println("Failed to accept client socket");
				e.printStackTrace();
			}

			if (client != null) {
				clients.put("User" + count, client);
				Thread current = new Thread(createRunnable(client, "User" + count));
				current.start();
				count++;
			}
		}
	}

	private static Runnable createRunnable(Socket client, String user) {
		return () -> {
			InputStreamReader input = null;
			BufferedReader reader = null;
			try {
				input = new InputStreamReader(client.getInputStream());
				reader = new BufferedReader(input);

				while (!client.isClosed()) {
					String msg = reader.readLine();
					if(msg != null) {
						System.out.println("Server recieved msg: " + msg);
						synchronized(lock) {
							messages.push(user + ": " + msg);
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
