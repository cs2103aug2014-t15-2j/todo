package todo.storage;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import todo.model.Item;
import todo.model.ItemList;
import todo.model.DateTime;

public class Storage {
	// Related to FileIO
	public static final String FILE_DESTINATION = "todo.xml";
	public static final String YES = "yes";
	public static final String XML_INDENT = "{http://xml.apache.org/xslt}indent-amount";
	public static final String INDENT_AMOUNT = "4";
	
	// Related to XML tags
	public static final String ITEM_LIST = "ItemList";
	public static final String ITEM_QUANTITY = "itemQty";
	public static final String LAST_ITEM_ID = "lastItemId";
	public static final String ITEM = "Item";
	public static final String ITEM_ID = "itemId";
	public static final String PRIORITY = "priority";
	public static final String DESCRIPTION = "description";
	public static final String START_DATE_TIME = "startDateTime";
	public static final String HAS_TIME = "hasTime";
	public static final String DATE_TIME = "DateTime";
	public static final String DUE_DATE_TIME = "dueDateTime";
	public static final String LOCATION = "location";
	public static final String TAGS = "tags";
	public static final String TAG = "tag";
	public static final String IS_ACTIVE = "isActive";
	
	// Related to date and time
	public static final String DATE_WITH_TIME = "MM/dd/yyyy HH:mm:ss";
	public static final String DATE_WITHOUT_TIME = "MM/dd/yyyy";
	
	// Other miscellaneous constant string and integer
	public static final String EMPTY = "";
	public static final String TRUE = "true";
	public static final int ZERO = 0;
	
	private static DateFormat dateWithTime =  new SimpleDateFormat(DATE_WITH_TIME);
	private static DateFormat dateWithoutTime =  new SimpleDateFormat(DATE_WITHOUT_TIME);
	
	/**
	 * This method reads the XML file 'todo.xml' and returns an ItemList
	 * @return ItemList
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException 
	 * @throws DOMException 
	 */
	public static ItemList readFromXML() throws ParserConfigurationException, SAXException, IOException, DOMException, ParseException{
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = docBuilder.parse(new File(FILE_DESTINATION));
		doc.getDocumentElement().normalize();
		ItemList newItemList = new ItemList();
		
		NodeList items = doc.getElementsByTagName(ITEM);
	
		for(int i = 0; i < items.getLength(); i++){
			Item newItem = new Item();
			
			Node itemId = doc.getElementsByTagName(ITEM_ID).item(i);
			// -1 here because setItemId always + 1 before itemId is set
			newItem.setItemID(Integer.parseInt(itemId.getTextContent()) - 1);
			
			Node priority = doc.getElementsByTagName(PRIORITY).item(i);
			newItem.setPriority(Integer.parseInt(priority.getTextContent()));
			
			Node description = doc.getElementsByTagName(DESCRIPTION).item(i);
			newItem.setDescription(description.getTextContent());

			//--------------------Retrieving of startDateTime
			Node startDateTime = doc.getElementsByTagName(START_DATE_TIME).item(i);
			Element startDateTimeElement = (Element) startDateTime;
			Node sHasTime = startDateTimeElement.getElementsByTagName(HAS_TIME).item(ZERO);
			Node sDateTime = startDateTimeElement.getElementsByTagName(DATE_TIME).item(ZERO);
			
			if(sDateTime.getTextContent() != EMPTY){
				if(sHasTime.getTextContent().equals(TRUE)){
					Date sDate = dateWithTime.parse(sDateTime.getTextContent());
					newItem.setStartDateTime(new DateTime(sDate, true));
				}else{
					Date sDate = dateWithoutTime.parse(sDateTime.getTextContent());
					newItem.setStartDateTime(new DateTime(sDate, false));
				}
			}
			//--------------------Retrieving of startDateTime
			
			//--------------------Retrieving of DueDateTime
			Node dueDateTime = doc.getElementsByTagName(DUE_DATE_TIME).item(i);
			Element dueDateTimeElement = (Element) dueDateTime;
			Node dHasTime = dueDateTimeElement.getElementsByTagName(HAS_TIME).item(ZERO);
			Node dDateTime = dueDateTimeElement.getElementsByTagName(DATE_TIME).item(ZERO);;
			
			if(dDateTime.getTextContent() != EMPTY){
				if(dHasTime.getTextContent().equals(TRUE)){
					Date dDate = dateWithTime.parse(dDateTime.getTextContent());
					newItem.setDueDateTime(new DateTime(dDate, true));
				}else{
					Date dDate = dateWithoutTime.parse(dDateTime.getTextContent());
					newItem.setDueDateTime(new DateTime(dDate, false));
				}
			}
			//--------------------Retrieving of DueDateTime	
			
			Node location = doc.getElementsByTagName(LOCATION).item(i);
			newItem.setLocation(location.getTextContent());

			Node tags = doc.getElementsByTagName(TAGS).item(i);
			Element tagsElement = (Element) tags;
			NodeList allTags = tagsElement.getElementsByTagName(TAG);
			
			ArrayList<String> newTags = new ArrayList<String>();
			for(int j = 0; j < allTags.getLength(); j++){
				newTags.add(allTags.item(j).getTextContent());
			}
			newItem.setTags(newTags);
			
			Node isActive = doc.getElementsByTagName(IS_ACTIVE).item(i);
			if(isActive.getTextContent().equals(TRUE)){
				newItem.setStatusUndone();
			}else{
				newItem.setStatusDone();
			}
		
			newItemList.add(newItem);
		}
		
		// there is only one static itemQty tag
		Node itemQty = doc.getElementsByTagName(ITEM_QUANTITY).item(ZERO);
		Item.setItemQty(Integer.parseInt(itemQty.getTextContent()));
	
		// there is only one static lastItemId tag
		Node lastItemId = doc.getElementsByTagName(LAST_ITEM_ID).item(ZERO);
		Item.setLastItemID(Integer.parseInt(lastItemId.getTextContent()));

		return newItemList;
	}
	
	/**
	 * This method creates a XML file 'todo.xml' and stores data according to the ItemList that gets passed in
	 * @param ItemList
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public static void storeIntoXML(ItemList il) throws ParserConfigurationException, TransformerException{
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		
		Element rootElement = doc.createElement(ITEM_LIST);
		doc.appendChild(rootElement);
		
		Element itemQty = doc.createElement(ITEM_QUANTITY);
		itemQty.appendChild(doc.createTextNode(String.valueOf(Item.getItemQty())));
		rootElement.appendChild(itemQty);
		
		Element lastItemId = doc.createElement(LAST_ITEM_ID);
		lastItemId.appendChild(doc.createTextNode(String.valueOf(Item.getLastItemId())));
		rootElement.appendChild(lastItemId);
		
		for(int i = 0; i < il.size(); i++){
			Item currentItem = il.getItem(i);
			
			Element item = doc.createElement(ITEM);
			rootElement.appendChild(item);
			
			Element itemId = doc.createElement(ITEM_ID);
			itemId.appendChild(doc.createTextNode(String.valueOf(currentItem.getItemId())));
			item.appendChild(itemId);
			
			Element priority = doc.createElement(PRIORITY);
			priority.appendChild(doc.createTextNode(String.valueOf(currentItem.getPriority())));
			item.appendChild(priority);
			
			Element description = doc.createElement(DESCRIPTION);
			if(currentItem.getDescription() == null){
				description.appendChild(doc.createTextNode(EMPTY));
			}else{
				description.appendChild(doc.createTextNode(currentItem.getDescription()));
			}
			item.appendChild(description);
			
			//------------------------------Storing of startDateTime--------------------------------
			Element startDateTime = doc.createElement(START_DATE_TIME);
			item.appendChild(startDateTime);
			
			DateTime sdt = currentItem.getStartDateTime();	
			Element sdtHasTime = doc.createElement(HAS_TIME);
			Element sdtDateTime = doc.createElement(DATE_TIME);
			if(sdt == null){
				sdtHasTime.appendChild(doc.createTextNode(EMPTY));
				sdtDateTime.appendChild(doc.createTextNode(EMPTY));
			}else{
				sdtHasTime.appendChild(doc.createTextNode(String.valueOf(sdt.hasTime())));
				
				if(sdt.hasTime()){
					sdtDateTime.appendChild(doc.createTextNode(dateWithTime.format(sdt.getDate())));
				}else{
					sdtDateTime.appendChild(doc.createTextNode(dateWithoutTime.format(sdt.getDate())));
				}
			}
			startDateTime.appendChild(sdtHasTime);
			startDateTime.appendChild(sdtDateTime);
			//------------------------------Storing of startDateTime--------------------------------
			
			//------------------------------Storing of dueDateTime----------------------------------
			Element dueDateTime = doc.createElement(DUE_DATE_TIME);
			item.appendChild(dueDateTime);
			
			DateTime ddt = currentItem.getDueDateTime();
			Element ddtHasTime = doc.createElement(HAS_TIME);
			Element ddtDateTime = doc.createElement(DATE_TIME);
			if(ddt == null){
				ddtHasTime.appendChild(doc.createTextNode(EMPTY));
				ddtDateTime.appendChild(doc.createTextNode(EMPTY));
			}else{
				ddtHasTime.appendChild(doc.createTextNode(String.valueOf(ddt.hasTime())));
				
				if(ddt.hasTime()){
					ddtDateTime.appendChild(doc.createTextNode(dateWithTime.format(ddt.getDate())));
				}else{
					ddtDateTime.appendChild(doc.createTextNode(dateWithoutTime.format(ddt.getDate())));
				}
			}
			dueDateTime.appendChild(ddtHasTime);
			dueDateTime.appendChild(ddtDateTime);
			//------------------------------Storing of dueDateTime----------------------------------
	
			Element location = doc.createElement(LOCATION);
			if(currentItem.getLocation() == null){
				location.appendChild(doc.createTextNode(EMPTY));
			}else{
				location.appendChild(doc.createTextNode(currentItem.getLocation()));
			}
			item.appendChild(location);
			
			Element tags = doc.createElement(TAGS);
			item.appendChild(tags);
			
			ArrayList<String> tagsToBeStored = currentItem.getTags();
			for(int j = 0; j < tagsToBeStored.size(); j++){
				Element tag = doc.createElement(TAG);
				tag.appendChild(doc.createTextNode(tagsToBeStored.get(j)));
				tags.appendChild(tag);
			}
			
			Element isActive = doc.createElement(IS_ACTIVE);
			isActive.appendChild(doc.createTextNode(String.valueOf(currentItem.getStatus())));
			item.appendChild(isActive);
		}
		
		Storage.serialise(doc);
		System.out.println("File saved!");
	}
	
	/**
	 * This method writes into the XMl, i.e. 'todo.xml' with the Document passed in
	 * @param doc
	 * @throws TransformerException
	 */
	public static void serialise(Document doc) throws TransformerException{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		// Output to file
		StreamResult result = new StreamResult(new File(FILE_DESTINATION));
		// Currently, output to console for testing only
		//StreamResult result = new StreamResult(System.out);
		
		transformer.setOutputProperty(OutputKeys.INDENT, YES);
		transformer.setOutputProperty(XML_INDENT, INDENT_AMOUNT);
		transformer.transform(source, result);
	}
}
