package todo.storage;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import todo.model.Item;
import todo.model.ItemList;
import todo.model.DateTime;

public class XMLReader {
<<<<<<< HEAD
	// Logger
	private final static Logger LOGGER = Logger.getLogger(XMLReader.class .getName());
=======
	private DateTimeFormatter dateWithTime = DateTimeFormatter.ofPattern(Storage.DATE_WITH_TIME);
	private DateFormat dateWithoutTime = new SimpleDateFormat(Storage.DATE_WITHOUT_TIME);
>>>>>>> origin/master

	/**
	 * Default constructor
	 */
	public XMLReader() {

	}

	/**
	 * This method reads the XML file 'todo.xml' and returns an ItemList
	 * 
	 * @return ItemList
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 * @throws DOMException
	 */
	public ItemList readFromXML() throws ParserConfigurationException, SAXException, IOException, DOMException, ParseException {
		File newFile = new File(Storage.FILE_DESTINATION);
		if (!newFile.exists()) {
			return new ItemList();
		}
		DocumentBuilder docBuilder =  DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = docBuilder.parse(newFile);
		ItemList newItemList = buildItemList(doc);
		
		// There only one ITEM_QUANTITY to set for Item
		Node itemQty = doc.getElementsByTagName(Storage.ITEM_QUANTITY).item(Storage.ZERO);
		Item.setItemQty(Integer.parseInt(itemQty.getTextContent()));

		// There only one LAST_ITEM_ID to set for Item
		Node lastItemId = doc.getElementsByTagName(Storage.LAST_ITEM_ID).item(Storage.ZERO);
		Item.setLastItemID(Integer.parseInt(lastItemId.getTextContent()));

		return newItemList;
	}

	/**
	 * This method traverses XML nodes, reads their values and stores into an
	 * Itemlist that is to be returned
	 * 
	 * @param doc
	 * @return ItemList
	 * @throws DOMException
	 * @throws ParseException
	 */
	private ItemList buildItemList(Document doc) throws DOMException, ParseException {
		ItemList newItemList = new ItemList();

		NodeList items = doc.getElementsByTagName(Storage.ITEM);

		for (int i = 0; i < items.getLength(); i++) {
			Item newItem = new Item();

			// ------------------Setting Item Object ID -------------------------
			Node itemId = doc.getElementsByTagName(Storage.ITEM_ID).item(i);
			// -1 here because setItemId always + 1 before itemId is set
			newItem.setItemID(Integer.parseInt(itemId.getTextContent()) - Storage.ONE);

			// ------------------Setting Item Object Importance------------------
			Node importance = doc.getElementsByTagName(Storage.IMPORTANCE).item(i);
			if (importance.getTextContent().equals(Storage.TRUE)) {
				newItem.setImportance(true);
			}

			// ------------------Setting Item Object Description-----------------
			Node description = doc.getElementsByTagName(Storage.DESCRIPTION).item(i);
			newItem.setDescription(description.getTextContent());

			// ------------------Retrieving of startDateTime---------------------
			Node startDateTime = doc.getElementsByTagName(Storage.START_DATE_TIME).item(i);
			Element startDateTimeElement = (Element) startDateTime;
			Node sHasTime = startDateTimeElement.getElementsByTagName(Storage.HAS_TIME).item(Storage.ZERO);
			Node sDateTime = startDateTimeElement.getElementsByTagName(Storage.DATE_TIME).item(Storage.ZERO);
<<<<<<< HEAD
			if(sDateTime.getTextContent() != Storage.EMPTY){
				if(sHasTime.getTextContent().equals(Storage.TRUE)){
					String dateWithTime =sDateTime.getTextContent().replace('T', ' '); 
					//System.out.println("Date with time = "+dateWithTime);
					dateWithTime = dateWithTime.substring(0,16);
					//System.out.println("Date with time 2 = "+dateWithTime);
					LocalDateTime dateWithTimeObject = LocalDateTime.parse(dateWithTime,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
					newItem.setStartDateTime(new DateTime(dateWithTimeObject, true));
				}else{
					String dateWithoutTime =sDateTime.getTextContent().replace('T', ' '); 
					dateWithoutTime = dateWithoutTime.substring(0,16);
					//System.out.println("Date without time = "+dateWithoutTime);
					LocalDateTime dateWithoutTimeObject = LocalDateTime.parse(dateWithoutTime,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
					newItem.setDueDateTime(new DateTime(dateWithoutTimeObject, true));

=======
			if (sDateTime.getTextContent() != Storage.EMPTY) {
				if (sHasTime.getTextContent().equals(Storage.TRUE)) {
					LocalDateTime sDate = LocalDateTime.parse(sDateTime.getTextContent(), dateWithTime);
					newItem.setStartDateTime(new DateTime(sDate, true));
				} else {
					Date sDate = dateWithoutTime.parse(sDateTime.getTextContent());
					newItem.setStartDateTime(new DateTime(sDate, false));
>>>>>>> origin/master
				}
			}
			// ------------------Retrieving of startDateTime---------------------

			// ------------------Retrieving of DueDateTime-----------------------
			Node dueDateTime = doc.getElementsByTagName(Storage.DUE_DATE_TIME).item(i);
			Element dueDateTimeElement = (Element) dueDateTime;
			Node dHasTime = dueDateTimeElement.getElementsByTagName(Storage.HAS_TIME).item(Storage.ZERO);
<<<<<<< HEAD
			Node dDateTime = dueDateTimeElement.getElementsByTagName(Storage.DATE_TIME).item(Storage.ZERO);;
			
			if(dDateTime.getTextContent() != Storage.EMPTY){
				if(dHasTime.getTextContent().equals(Storage.TRUE)){
					String dateWithTime =sDateTime.getTextContent().replace('T', ' '); 
					//System.out.println("Date with time = "+dateWithTime);
					if(dateWithTime!=""){
					dateWithTime = dateWithTime.substring(0,16);
					//System.out.println("Date with time 2 = "+dateWithTime);
					LocalDateTime dateWithTimeObject = LocalDateTime.parse(dateWithTime,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
					newItem.setDueDateTime(new DateTime(dateWithTimeObject, true));
					}
					
				}else{
					String dateWithoutTime =sDateTime.getTextContent().replace('T', ' '); 
					if(dateWithoutTime!=""){
					dateWithoutTime = dateWithoutTime.substring(0,16);
					System.out.println("Date without time = "+dateWithoutTime);
					LocalDateTime dateWithoutTimeObject = LocalDateTime.parse(dateWithoutTime,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
					newItem.setDueDateTime(new DateTime(dateWithoutTimeObject, true));
				}
=======
			Node dDateTime = dueDateTimeElement.getElementsByTagName(Storage.DATE_TIME).item(Storage.ZERO);

			if (dDateTime.getTextContent() != Storage.EMPTY) {
				if (dHasTime.getTextContent().equals(Storage.TRUE)) {
					LocalDateTime dDate = LocalDateTime.parse(dDateTime.getTextContent(), dateWithTime);
					newItem.setDueDateTime(new DateTime(dDate, true));
				} else {
					Date dDate = dateWithoutTime.parse(dDateTime.getTextContent());
					newItem.setDueDateTime(new DateTime(dDate, false));
>>>>>>> origin/master
				}
			}
			// ------------------Retrieving of DueDateTime-----------------------

			Node location = doc.getElementsByTagName(Storage.LOCATION).item(i);
			newItem.setLocation(location.getTextContent());

			Node tags = doc.getElementsByTagName(Storage.TAGS).item(i);
			Element tagsElement = (Element) tags;
			NodeList allTags = tagsElement.getElementsByTagName(Storage.TAG);

			ArrayList<String> newTags = new ArrayList<String>();
			for (int j = 0; j < allTags.getLength(); j++) {
				newTags.add(allTags.item(j).getTextContent());
			}
			newItem.setTags(newTags);

			Node isActive = doc.getElementsByTagName(Storage.IS_COMPLETED).item(i);
			if (isActive.getTextContent().equals(Storage.TRUE)) {
				newItem.setStatusDone();
			} else {
				newItem.setStatusUndone();
			}

			newItemList.add(newItem);
		}

		return newItemList;
	}

}
