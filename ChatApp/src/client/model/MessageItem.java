/**
 * 
 */
package client.model;

import java.time.LocalDateTime;

/**
 * @author jason
 *
 */
public class MessageItem {
	private String userName;
	private LocalDateTime dateTime;
	private String message;
	
	/**
	 * Initializes in instance of MessageItem with empty properties
	 */
	public MessageItem() {
		this.userName = "";
		this.dateTime = LocalDateTime.now();
		this.message = "";
	}
	
	/**
	 * Initializes and instance of MessageItem with given properties
	 * @param userName user associated with message
	 * @param dateTime time message was sent in java Class LocalDateTime format
	 * @param message the message
	 */
	public MessageItem(String userName, LocalDateTime dateTime, String message) {
		this.userName = userName;
		this.dateTime = dateTime;
		this.message = message;
	}
	
	public MessageItem(String message) {
		String[] split = message.split(",");
		StringBuilder sb = new StringBuilder();
		
		sb.append(split[2]);
		for(int i = 3; i < split.length; i++) {
			sb.append("," + split[i]);
		}
		
		this.userName = split[1];
		this.dateTime = LocalDateTime.parse(split[0]);
		this.message = sb.toString();
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
	 * @param dateTime time sent in LocalDateTime format
	 */
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
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
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	/**
	 * Get the message that was sent
	 * 
	 * @return the message that was sent in java Class Timestamp format
	 */
	public String getMessage() {
		return message;
	}
	
	public String toCSV() {
		return this.dateTime.toString() + "," + this.userName + "," + this.message + System.lineSeparator();
	}
	
	@Override
	public String toString() {
		return "[" + this.dateTime.getHour() + ":" + this.dateTime.getMinute() + "] " + this.userName + " : " + this.message;
	}
}
