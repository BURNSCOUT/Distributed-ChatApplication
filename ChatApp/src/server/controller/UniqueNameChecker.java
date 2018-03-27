/**
 * 
 */
package server.controller;

import java.io.IOException;
import java.util.ArrayList;

import server.io.UsernameIO;

/**
 * @author jason
 *
 */
public class UniqueNameChecker {
	
private ArrayList<String> takenUserNames;
	
	/**
	 * Initializer gets a collection of userNames from file in resources folder using UsernameIO.LoadUserNames().
	 */
	public UniqueNameChecker() {
		try {
			this.takenUserNames = UsernameIO.loadUserNames();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			this.takenUserNames = new ArrayList<>();
			e.printStackTrace();
		}
	}
	
	public UniqueNameChecker(ArrayList<String> takenUsernames) {
		this.takenUserNames = takenUsernames;
	}
	
	
	/**
	 * Checks specified userName against all current userNames in server for uniqueness. 
	 * 
	 * @param userName The userName to check against servers userNames
	 * @return true if userName is unique, false otherwise.
	 */
	public boolean isUniqueName(String userName) {
				
		for (String takenuserName : takenUserNames) {
			if (!this.checkUniqueCharacters(takenuserName, userName)) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean checkUniqueCharacters(String takenUsername, String newUsername) {
		
		Character takenUserCharacter = '&';
		Character newUserCharacter = '&';
		
		if(takenUsername.length() > 0) {
			takenUserCharacter = takenUsername.charAt(0);
		}
		
		if(newUsername.length() > 0) {
			newUserCharacter = newUsername.charAt(0);
		}
		
		if(!takenUserCharacter.equals(newUserCharacter)) {
			return true;
		}
		
		if(takenUserCharacter.equals('&') && newUserCharacter.equals('&')) {
			return false;
		}
		
		takenUsername = takenUsername.substring(1);
		newUsername = newUsername.substring(1);
		
		return checkUniqueCharacters(takenUsername, newUsername);
		
	}
}
