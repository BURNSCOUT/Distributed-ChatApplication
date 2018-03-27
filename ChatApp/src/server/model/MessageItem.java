/**
 * 
 */
package server.model;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author jason
 *
 */
public class MessageItem {
	private String userName;
	private Timestamp timeStamp;
	private String message;
	
	/**
	 * Initializes in instance of MessageItem with empty properties
	 */
	public MessageItem() {
		this.userName = "";
		this.timeStamp = Timestamp.from(Instant.now());
		this.message = "";
	}
	
	/**
	 * Initializes and instance of MessageItem with given properties
	 * @param userName user associated with message
	 * @param timeStamp time message was sent in java Class Timestamp format
	 * @param message the message
	 */
	public MessageItem(String userName, Timestamp timeStamp, String message) {
		this.userName = userName;
		this.timeStamp = timeStamp;
		this.message = message;
	}
	
	/**
	 * Set users name
	 * 
	 * @param userName the name of the user
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Set the time the message was sent
	 * 
	 * @param timeStamp time sent in java Class Timestamp format
	 */
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	/**
	 * Set the message that was sent
	 * 
	 * @param message the message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Get the users name that sent the message
	 * 
	 * @return the users name
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Get the time the message was sent
	 * 
	 * @return the time the message was sent
	 */
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	
	/**
	 * Get the message that was sent
	 * 
	 * @return the message that was sent in java Class Timestamp format
	 */
	public String getMessage() {
		return message;
	}
	
	@Override
	public String toString() {
		return "[" + this.timeStamp.toString() + "] " + this.userName + " : " + this.message;
	}
}
