/**
 * 
 */
package server.io;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import client.model.MessageItem;
import client.utility.DateTimeFileNameFormatter;

/**
 * @author jason
 *
 */
public class XMLMessageIO {
	private static final String ROOT_ELEMENT_NAME = "History";
	private static final String CHAT_HISTORY_ITEM_ELEMENT_NAME = "MessageItem";
	private static final String CHAT_HISTORY_ID_ELEMENT_NAME = "id";
	private static final String DATE_TIME_ELEMENT_NAME = "timestamp";
	private static final String USERNAME_ELEMENT_NAME = "username";
	private static final String MESSAGE_ELEMENT_NAME = "message";
	
	/**
	 * Saves History in a file name formatted 'userName_yyyy-mm-dd_HH-mm-ss.f.txt' in a resource folder in project root directory in xml format 
	 * <History>
	 * <MessageItem id="index">
	 * <username>UserName</username>
	 * <timestamp>yyyy-mm-dd HH:mm:ss.f</timestamp>
	 * <message>Message</message>
	 * </MessageItem>
	 * </History>
	 * 
	 * @param clientUserName The user who's history is being saved
	 * @param connectionTimestamp The SQL Timestamp from java api java.sql.Timestamp 
	 * @param history A collection of MessageItem objects
	 * @throws ParserConfigurationException Indicates a serious configuration error
	 * @throws TransformerException This class specifies an exceptional condition that occurred during the transformation process
	 */
	public static void saveHistory(String clientUserName, LocalDateTime connectionTimestamp, ArrayList<MessageItem> history) throws ParserConfigurationException, TransformerException {
		
		String fileName = getFileName(clientUserName, connectionTimestamp);
		
		Document document = createXMLDocument(history);
		
		writeToFile(document, fileName);
				
	}
	
	private static Document createXMLDocument(ArrayList<MessageItem> history) throws ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		
		Element rootElement = document.createElement(ROOT_ELEMENT_NAME);
		document.appendChild(rootElement);	
		
		for (int i = 0; i < history.size(); i++) {
			
			MessageItem messageItem = history.get(i);
			rootElement.appendChild(getMessageItem(document, i, messageItem));
			
		}
		
		return document;
	}
		
	private static Node getMessageItem(Document document, int index, MessageItem messageItem) {
		Element messageItemElement = document.createElement(CHAT_HISTORY_ITEM_ELEMENT_NAME);
		
		messageItemElement.setAttribute(CHAT_HISTORY_ID_ELEMENT_NAME, String.valueOf(index));
		
		messageItemElement.appendChild(getMessageItemElements(document, messageItemElement, USERNAME_ELEMENT_NAME, messageItem.getUserName()));
		messageItemElement.appendChild(getMessageItemElements(document, messageItemElement, DATE_TIME_ELEMENT_NAME, messageItem.getDateTime().toString()));
		messageItemElement.appendChild(getMessageItemElements(document, messageItemElement, MESSAGE_ELEMENT_NAME, messageItem.getMessage()));
		
		return messageItemElement;
	}
	
    private static Node getMessageItemElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
    
    private static void writeToFile(Document document, String fileName) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		DOMSource source = new DOMSource(document);
		
		StreamResult console = new StreamResult(System.out);
		StreamResult file = new StreamResult(new File(fileName));	
		
		transformer.transform(source, console);
		transformer.transform(source, file);
	}
    
    /**
     * Loads a collection of MessageItem objects from 
     * a file name formatted 'userName_yyyy-mm-dd_HH-mm-ss.f.txt' 
     * in a resource folder in project root directory in xml format 
	 * <History>
	 * <MessageItem id="index">
	 * <username>UserName</username>
	 * <timestamp>yyyy-mm-dd HH:mm:ss.f</timestamp>
	 * <message>Message</message>
	 * </MessageItem>
	 * </History>
     * 
     * @param clientUserName The user who's history is being saved
	 * @param connectionLocalDateTime The SQL Timestamp from java api java.sql.Timestamp 
     * @return A collection of MessageItem objects
     * @throws ParserConfigurationException Indicates a serious configuration error.
     * @throws SAXException Encapsulate a general SAX error or warning.
     * @throws IOException Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
     */
    public static ArrayList<MessageItem> loadHistory(String clientUserName, LocalDateTime connectionLocalDateTime) throws ParserConfigurationException, SAXException, IOException {
		
		String fileName = getFileName(clientUserName, connectionLocalDateTime);
		
		File xmlFile = new File(fileName);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(xmlFile);
		
		NodeList nodeList = document.getElementsByTagName(CHAT_HISTORY_ITEM_ELEMENT_NAME);
		
		ArrayList<MessageItem> history = new ArrayList<>();
		
		for(int i = 0; i < nodeList.getLength(); i++) {
			history.add(getMessageItem(nodeList.item(i)));
		}
		
		return history;
	}
	
	private static MessageItem getMessageItem(Node node) {
		MessageItem messageItem = new MessageItem();
		
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;
			
			messageItem.setUserName(getTagValue(USERNAME_ELEMENT_NAME, element));
			messageItem.setDateTime(LocalDateTime.parse(getTagValue(DATE_TIME_ELEMENT_NAME, element)));
			messageItem.setMessage(getTagValue(MESSAGE_ELEMENT_NAME, element));
		}
		
		return messageItem;
	}
	
	private static String getTagValue(String tag, Element element) {
		NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
	}
    
    private static String getFileName(String clientUserName, LocalDateTime connectionLocalDateTime) {
    	String formattedTimestamp = DateTimeFileNameFormatter.formatLocalDateTimeToFile(connectionLocalDateTime);
		return "resources" + File.separator + clientUserName + "_" + formattedTimestamp + ".txt";
    }
}
