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

	private DateFormat dateWithTime = new SimpleDateFormat(Storage.DATE_WITH_TIME);
	private DateFormat dateWithoutTime = new SimpleDateFormat(Storage.DATE_WITHOUT_TIME);

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
		File newFile = new File(Storage.FILE_DESTINATION);
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
		Node itemQty = doc.getElementsByTagName(Storage.ITEM_QUANTITY).item(Storage.ZERO);
		Item.setItemQty(Integer.parseInt(itemQty.getTextContent()));
	
		// There only one LAST_ITEM_ID to set for Item
		Node lastItemId = doc.getElementsByTagName(Storage.LAST_ITEM_ID).item(Storage.ZERO);
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
		
		NodeList items = doc.getElementsByTagName(Storage.ITEM);
		
		for(int i = 0; i < items.getLength(); i++){
			Item newItem = new Item();
			
			Node itemId = doc.getElementsByTagName(Storage.ITEM_ID).item(i);
			// -1 here because setItemId always + 1 before itemId is set
			newItem.setItemID(Integer.parseInt(itemId.getTextContent()) - 1);
			
			Node priority = doc.getElementsByTagName(Storage.PRIORITY).item(i);
			newItem.setPriority(Integer.parseInt(priority.getTextContent()));
			
			Node description = doc.getElementsByTagName(Storage.DESCRIPTION).item(i);
			newItem.setDescription(description.getTextContent());

			//------------------Retrieving of startDateTime---------------------
			Node startDateTime = doc.getElementsByTagName(Storage.START_DATE_TIME).item(i);
			Element startDateTimeElement = (Element) startDateTime;
			Node sHasTime = startDateTimeElement.getElementsByTagName(Storage.HAS_TIME).item(Storage.ZERO);
			Node sDateTime = startDateTimeElement.getElementsByTagName(Storage.DATE_TIME).item(Storage.ZERO);
			
			if(sDateTime.getTextContent() != Storage.EMPTY){
				if(sHasTime.getTextContent().equals(Storage.TRUE)){
					Date sDate = dateWithTime.parse(sDateTime.getTextContent());
					newItem.setStartDateTime(new DateTime(sDate, true));
				}else{
					Date sDate = dateWithoutTime.parse(sDateTime.getTextContent());
					newItem.setStartDateTime(new DateTime(sDate, false));
				}
			}
			//------------------Retrieving of startDateTime---------------------
			
			//------------------Retrieving of DueDateTime-----------------------
			Node dueDateTime = doc.getElementsByTagName(Storage.DUE_DATE_TIME).item(i);
			Element dueDateTimeElement = (Element) dueDateTime;
			Node dHasTime = dueDateTimeElement.getElementsByTagName(Storage.HAS_TIME).item(Storage.ZERO);
			Node dDateTime = dueDateTimeElement.getElementsByTagName(Storage.DATE_TIME).item(Storage.ZERO);;
			
			if(dDateTime.getTextContent() != Storage.EMPTY){
				if(dHasTime.getTextContent().equals(Storage.TRUE)){
					Date dDate = dateWithTime.parse(dDateTime.getTextContent());
					newItem.setDueDateTime(new DateTime(dDate, true));
				}else{
					Date dDate = dateWithoutTime.parse(dDateTime.getTextContent());
					newItem.setDueDateTime(new DateTime(dDate, false));
				}
			}
			//------------------Retrieving of DueDateTime-----------------------
			
			Node location = doc.getElementsByTagName(Storage.LOCATION).item(i);
			newItem.setLocation(location.getTextContent());

			Node tags = doc.getElementsByTagName(Storage.TAGS).item(i);
			Element tagsElement = (Element) tags;
			NodeList allTags = tagsElement.getElementsByTagName(Storage.TAG);
			
			ArrayList<String> newTags = new ArrayList<String>();
			for(int j = 0; j < allTags.getLength(); j++){
				newTags.add(allTags.item(j).getTextContent());
			}
			newItem.setTags(newTags);
			
			Node isActive = doc.getElementsByTagName(Storage.IS_COMPLETED).item(i);
			if(isActive.getTextContent().equals(Storage.TRUE)){
				newItem.setStatusDone();
			}else{
				newItem.setStatusUndone();
			}
		
			newItemList.add(newItem);
		}
		
		return newItemList;
	}

}
