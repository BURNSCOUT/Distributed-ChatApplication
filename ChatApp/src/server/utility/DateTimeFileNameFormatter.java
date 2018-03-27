/**
 * 
 */
package server.utility;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author jason
 *
 */
public class DateTimeFileNameFormatter {
	
	/**
	 * Formats DateTime String from 'yyyy-MM-dd HH:mm:ss.f' to 'yyyy-MM-dd_HH-mm-ss.f'
	 * @param dateTime timestamp the sql timestamp in 'yyyy-mm-dd hh:mm:ss' format
	 * @return A string that can be set as a file name
	 */
	public static String formatTimestampToFileName(Timestamp dateTime) {
		String dateTimeString = dateTime.toString();
		
		return formatTimestampToFile(dateTimeString);
	}
	
	/**
	 * Formats DateTime String from 'yyyy-MM-dd HH:mm:ss.f' to 'yyyy-MM-dd_HH-mm-ss.f'
	 * @param dateTime a String in 'yyyy-mm-dd hh:mm:ss' format
	 * @return A string that can be set as a file name
	 */
	public static String formatTimestampToFile(String dateTime) {
		String timeStampNoWhiteSpace = dateTime.replace(" ", "_");
		String timeStampFormatted = timeStampNoWhiteSpace.replace(":", "-");
		
		return timeStampFormatted;
	}
	
	
	/**
	 * Formats DateTime String from 'yyyy-dd-mmTHH:mm:ss' to 'yyyy-dd-m_THH-mm-ss'
	 * @param dateTime LocalDateTime the date and time in 'yyyy-mm-dd hh:mm:ss' format
	 * @return A string that can be set as a file name
	 */
	public static String formatLocalDateTimeToFile(LocalDateTime dateTime) {
		String dateTimeString = dateTime.toString();
		
		return formatLocalDateTimeToFile(dateTimeString);
	}
	
	/**
	 * Formats DateTime String from 'yyyy-dd-mmTHH:mm:ss' to 'yyyy-dd-m_THH-mm-ss'
	 * @param dateTime a String in 'yyyy-dd-mmTHH:mm:ss' format
	 * @return A string that can be set as a file name
	 */
	public static String formatLocalDateTimeToFile(String dateTime) {
		String dateTimeNoT = dateTime.replace("T", "_");
		String dateTimeFormatted = dateTimeNoT.replace(":", "-");
		
		return dateTimeFormatted;
	}
}
