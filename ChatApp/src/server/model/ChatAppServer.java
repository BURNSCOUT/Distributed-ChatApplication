package server.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ChatAppServer {

	private static ServerSocket server;
	private static HashMap<String, Socket> clients;
	private final static int PORT = 4225;

	public static void run() {
		Runnable hookRun = () -> {
			try {
				server.close();
			} catch (Exception e) {
				System.out.println("Server closing failed");
			}
		};
		Runtime.getRuntime().addShutdownHook(new Thread(hookRun));
		server = null;
		clients = new HashMap<String, Socket>();
		
		try {
			server = new ServerSocket(PORT);
		} catch (Exception e){
			System.out.println(e);
		}
		
		int count = 1;
		
		while(true) {
			Socket client = null;
			try {
				client = server.accept();
			} catch (IOException e) {
				System.out.println("Failed to accept client socket");
				e.printStackTrace();
			}
			
			if(client != null) {
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
			PrintStream stream = null;
			BufferedReader reader = null;
			try {
				input = new InputStreamReader(client.getInputStream());
				stream = new PrintStream(client.getOutputStream());
				reader = new BufferedReader(input);
				
				while(!client.isClosed()) {
					String msg = reader.readLine();
					stream.println("\tServer recieved your message: " + msg);
				}

			} catch (IOException e) {
				System.out.println("Error occured while communicating with client");
				e.printStackTrace();
			}
			
			try {
				client.close();
				input.close();
				reader.close();
			} catch (IOException e) {
				System.out.println("Error occured while closing the " + user + " client socket");
			}
		};
	}
}
