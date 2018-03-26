/**
 * 
 */
package server.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import server.model.MessageItem;
import server.utility.DateTimeFileNameFormatter;

/**
 * @author jason
 *
 */
public class MessageIO {
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = System.lineSeparator();
	private static final int USERNAME_INDEX = 0;
	private static final int TIMESTAMP_INDEX = 1;
	private static final int MESSAGE_INDEX = 2;
	
	/**
	 * Saves History in a file name formatted 'userName_yyyy-mm-dd_HH-mm-ss.f.txt' in a resource folder in project root directory in .CSV format
	 * 
	 * @param clientUserName The user who's history is being saved
	 * @param connectionTimestamp The SQL Timestamp from java api java.sql.Timestamp 
	 * @param history A collection of MessageItem objects
	 * @throws IOException Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
	 */
	public static void saveFile(String clientUserName, Timestamp connectionTimestamp, ArrayList<MessageItem> history) throws IOException {
		String path = getFileName(clientUserName, connectionTimestamp);
		
		FileWriter fileWriter = new FileWriter(path);
		
		for (MessageItem messageItem : history) {
			fileWriter.append(messageItem.getUserName());
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(messageItem.getTimeStamp().toString());
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(messageItem.getMessage());
			fileWriter.append(NEW_LINE_SEPARATOR);
		}
		
		fileWriter.flush();
		fileWriter.close();
	}
	
	/**
	 * Loads a collection of MessageItem objects from 
     * a file name formatted 'userName_yyyy-mm-dd_HH-mm-ss.f.txt' 
     * in a resource folder in project root directory
	 * 
	 * @param clientUserName The user who's history is being saved
	 * @param connectionTimestamp The SQL Timestamp from java api java.sql.Timestamp 
     * @return A collection of MessageItem objects
	 * @throws FileNotFoundException Signals that an attempt to open the file denoted by a specified pathname has failed.
	 * @throws IOException Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
	 */
	public static ArrayList<MessageItem> loadFile(String clientUserName, Timestamp connectionTimestamp) throws FileNotFoundException, IOException {
		String path = getFileName(clientUserName, connectionTimestamp);
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
		String line = "";
		ArrayList<MessageItem> messageItems = new ArrayList<MessageItem>();
		
		while ((line = bufferedReader.readLine()) != null) {
			String[] tokens = line.split(COMMA_DELIMITER);
			if (tokens.length > 0) {
				
				messageItems.add(getMessageItem(tokens));
			}
		}
		
		bufferedReader.close();
		
		return messageItems;
	}
	
	private static MessageItem getMessageItem (String[] tokens) {
		String userName = tokens[USERNAME_INDEX];
		Timestamp timestamp = Timestamp.valueOf(tokens[TIMESTAMP_INDEX]);
		String message = tokens[MESSAGE_INDEX];
		
		return new MessageItem(userName, timestamp, message);
	}
	
	private static String getFileName(String clientUserName, Timestamp connectionTimestamp) {
    	String formattedTimestamp = DateTimeFileNameFormatter.FormatDateTimeToFileName(connectionTimestamp);
		return "resources" + File.separator + clientUserName + "_" + formattedTimestamp + ".txt";
    }
}