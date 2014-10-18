package todo.storage;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	// Logger
	private final static Logger LOGGER = Logger.getLogger(XMLReader.class .getName());
	
	// Related to FileIO
	public static final String FILE_DESTINATION = "todo.xml";

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
	public static final String TRUE = "true";
	public static final int ZERO = 0;

	private static DateFormat dateWithTime = new SimpleDateFormat(DATE_WITH_TIME);
	private static DateFormat dateWithoutTime = new SimpleDateFormat(DATE_WITHOUT_TIME);

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
	public ItemList readFromXML() throws ParserConfigurationException, SAXException, IOException, DOMException, ParseException{
		File newFile = new File(FILE_DESTINATION);
		if(!newFile.exists()){
			return new ItemList();
		}
		DocumentBuilder docBuilder = null;
		Document doc = null;
		ItemList newItemList = null;
		try{
			//FileHandler fh = new FileHandler("MyLogFile.log");  
			//LOGGER.addHandler(fh);
	       // SimpleFormatter formatter = new SimpleFormatter();  
	        //fh.setFormatter(formatter);  
	        docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        doc = docBuilder.parse(newFile);
	        doc.getDocumentElement().normalize();
	        newItemList = traverseNodesToRead(doc);
		}catch(Exception e){
			// SEVERE/WARNING/INFO/CONFIG/FINE/FINER/FINEST
			LOGGER.setLevel(Level.INFO);
			LOGGER.info("Error related to external file");
		}
		
		// There only one ITEM_QUANTITY to set for Item
		Node itemQty = doc.getElementsByTagName(ITEM_QUANTITY).item(ZERO);
		Item.setItemQty(Integer.parseInt(itemQty.getTextContent()));
	
		// There only one LAST_ITEM_ID to set for Item
		Node lastItemId = doc.getElementsByTagName(LAST_ITEM_ID).item(ZERO);
		Item.setLastItemID(Integer.parseInt(lastItemId.getTextContent()));
		
		return newItemList;
	}
	
	/**
	 * This method traverses XML nodes, reads their values and stores into an Itemlist that is to be return 
	 * 
	 * @param doc
	 * @return ItemList
	 * @throws DOMException
	 * @throws ParseException
	 */
	private ItemList traverseNodesToRead(Document doc) throws DOMException, ParseException{
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

			//------------------Retrieving of startDateTime---------------------
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
			//------------------Retrieving of startDateTime---------------------
			
			//------------------Retrieving of DueDateTime-----------------------
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
			//------------------Retrieving of DueDateTime-----------------------
			
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
			
			Node isActive = doc.getElementsByTagName(IS_COMPLETED).item(i);
			if(isActive.getTextContent().equals(TRUE)){
				newItem.setStatusDone();
			}else{
				newItem.setStatusUndone();
			}
		
			newItemList.add(newItem);
		}
		
		return newItemList;
	}

}
