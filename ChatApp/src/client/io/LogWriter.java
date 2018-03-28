package client.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import client.model.MessageItem;
import client.utility.DateTimeFileNameFormatter;

public class LogWriter {

	/**
	 * Writes out a file using the specified Username and all of the Clients viewable messages.
	 * @param username
	 * 		The Username of the Client
	 * @param messages
	 * 		The viewable messages of the Client
	 * @throws IOException
	 */
	public static void writeToLogFile(String username, ArrayList<MessageItem> messages) throws IOException {
		File log = new File(
				username + "_" + DateTimeFileNameFormatter.formatLocalDateTimeToFile(LocalDateTime.now()) + ".txt");
		FileWriter writer = new FileWriter(log);

		for (MessageItem messageItem : messages) {
			writer.append(messageItem.toCSV());
		}

		writer.close();
	}
}
