/**
 * 
 */
package server.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author jason
 *
 */
public class UsernameIO {
	private static final String NEW_LINE_SEPARATOR = System.lineSeparator();

	public static void saveUserName(String userName) throws IOException {
		String path = "resources" + File.separator + "usernames.txt";

		FileWriter fileWriter = new FileWriter(path);

		fileWriter.append(userName);
		fileWriter.append(NEW_LINE_SEPARATOR);
		fileWriter.flush();
		fileWriter.close();
	}

	public static ArrayList<String> loadUserNames() throws IOException {
		String path = "resources" + File.separator + "usernames.txt";

		BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
		String userName = "";
		ArrayList<String> userNames = new ArrayList<>();

		while ((userName = bufferedReader.readLine()) != null) {
			userNames.add(userName);
		}

		bufferedReader.close();

		return userNames;
	}
}
