package todo.storage;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import todo.model.Item;
import todo.model.ItemList;
import todo.model.DateTime;

public class XMLWriter {
	
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
	public static final String IS_COMPLETED = "isCompleted";

	// Related to date and time
	public static final String DATE_WITH_TIME = "MM/dd/yyyy HH:mm:ss";
	public static final String DATE_WITHOUT_TIME = "MM/dd/yyyy";

	// Other miscellaneous constant string and integer
	public static final String EMPTY = "";

	private static DateFormat dateWithTime = new SimpleDateFormat(DATE_WITH_TIME);
	private static DateFormat dateWithoutTime = new SimpleDateFormat(DATE_WITHOUT_TIME);

	/**
	 * Default constructor
	 */
	public XMLWriter() {

	}
	
	/**
	 * This method creates a XML file 'todo.xml' and stores data according to the ItemList that gets passed in
	 * 
	 * @param ItemList
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public void storeIntoXML(ItemList il) throws ParserConfigurationException, TransformerException{
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
			
			//----------------------Storing of startDateTime-----------------------
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
			//----------------------Storing of startDateTime-----------------------
			
			//-----------------------Storing of dueDateTime------------------------
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
			//-----------------------Storing of dueDateTime------------------------
	
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
			
			Element isActive = doc.createElement(IS_COMPLETED);
			isActive.appendChild(doc.createTextNode(String.valueOf(currentItem.getStatus())));
			item.appendChild(isActive);
		}
		
		serialise(doc);
	}
	
	/**
	 * This method writes into the XMl, i.e. 'todo.xml' with the Document being passed in
	 * 
	 * @param doc
	 * @throws TransformerException
	 */
	private void serialise(Document doc) throws TransformerException{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		// Output to file
		StreamResult result = new StreamResult(new File(FILE_DESTINATION));
		// Output to console
		//StreamResult result = new StreamResult(System.out);
		
		transformer.setOutputProperty(OutputKeys.INDENT, YES);
		transformer.setOutputProperty(XML_INDENT, INDENT_AMOUNT);
		transformer.transform(source, result);
	}

}