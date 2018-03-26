/**
 * 
 */
package server.utility;

import java.sql.Timestamp;

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
	public static String FormatDateTimeToFileName(Timestamp dateTime) {
		String dateTimeString = dateTime.toString();
		
		return FormatDateTimeToFile(dateTimeString);
	}
	
	/**
	 * Formats DateTime String from 'yyyy-MM-dd HH:mm:ss.f' to 'yyyy-MM-dd_HH-mm-ss.f'
	 * @param dateTime a String in 'yyyy-mm-dd hh:mm:ss' format
	 * @return A string that can be set as a file name
	 */
	public static String FormatDateTimeToFile(String dateTime) {
		String timeStampNoWhiteSpace = dateTime.replace(" ", "_");
		String timeStampFormatted = timeStampNoWhiteSpace.replace(":", "-");
		
		return timeStampFormatted;
	}
}
