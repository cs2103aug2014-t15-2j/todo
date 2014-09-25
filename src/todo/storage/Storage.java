package todo.storage;

import java.io.File;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import todo.model.Item;
import todo.model.ItemList;
import todo.model.DateTime;


public class Storage {
	public static final String OUTPUT_XML= "todo.xml";
	public static final String YES = "yes";
	public static final String XML_INDENT = "{http://xml.apache.org/xslt}indent-amount";
	public static final String INDENT_AMOUNT ="4";
	
	/**
	 * This method creates a XML file according to the ItemList that gets passed in
	 * @param il
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public static void storeIntoXML(ItemList il) throws ParserConfigurationException, TransformerException{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		
		// root element
		Element rootElement = doc.createElement("ItemList");
		doc.appendChild(rootElement);
		
		// itemQty element
		Element itemQty = doc.createElement("itemQty");
		itemQty.appendChild(doc.createTextNode(String.valueOf(Item.getItemQty())));
		rootElement.appendChild(itemQty);
		
		// lastItemId element
		Element lastItemId = doc.createElement("lastItemId");
		lastItemId.appendChild(doc.createTextNode(String.valueOf(Item.getLastItemId())));
		rootElement.appendChild(lastItemId);
	
		
		for(int i = 0; i < il.size(); i++){
			Element item = doc.createElement("Item");
			rootElement.appendChild(item);
			
			Item currentItem = il.getItem(i);
			
			Element itemId = doc.createElement("itemId");
			itemId.appendChild(doc.createTextNode(String.valueOf(currentItem.getItemId())));
			item.appendChild(itemId);
			
			Element priority = doc.createElement("priority");
			priority.appendChild(doc.createTextNode(String.valueOf(currentItem.getPriority())));
			item.appendChild(priority);
			
			Element description = doc.createElement("description");
			description.appendChild(doc.createTextNode(currentItem.getDescription()));
			item.appendChild(description);
			
			//------------------------------Storing of startDateTime--------------------------------
			Element startDateTime = doc.createElement("startDateTime");
			item.appendChild(startDateTime);
			
			DateTime sdt = currentItem.getStartDateTime();
			Element sdtHasTime = doc.createElement("hasTime");
			sdtHasTime.appendChild(doc.createTextNode(String.valueOf(sdt.hasTime())));
			startDateTime.appendChild(sdtHasTime);
			
			Date sdtDate = sdt.getDate();
			Element sdtDateTime = doc.createElement("DateTime");
			sdtDateTime.appendChild(doc.createTextNode(String.valueOf(sdtDate)));
			startDateTime.appendChild(sdtDateTime);
			//------------------------------Storing of startDateTime--------------------------------
			
			//------------------------------Storing of dueDateTime----------------------------------
			Element dueDateTime = doc.createElement("dueDateTime");
			item.appendChild(dueDateTime);
			
			DateTime ddt = currentItem.getDueDateTime();
			Element ddtHasTime = doc.createElement("hasTime");
			ddtHasTime.appendChild(doc.createTextNode(String.valueOf(ddt.hasTime())));
			dueDateTime.appendChild(ddtHasTime);

			Date ddtDate = sdt.getDate();
			Element ddtDateTime = doc.createElement("DateTime");
			ddtDateTime.appendChild(doc.createTextNode(String.valueOf(ddtDate)));
			dueDateTime.appendChild(ddtDateTime);
			//------------------------------Storing of dueDateTime----------------------------------
	
			Element location = doc.createElement("location");
			location.appendChild(doc.createTextNode(currentItem.getLocation()));
			item.appendChild(location);
			
			Element tags = doc.createElement("tags");
			item.appendChild(tags);
			
			ArrayList<String> tagsToBeStored = currentItem.getTags();
			for(int j = 0; j < tagsToBeStored.size(); j++){
				Element tag = doc.createElement("tag");
				tag.appendChild(doc.createTextNode(tagsToBeStored.get(j)));
				tags.appendChild(tag);
			}
			
			Element isActive = doc.createElement("isActive");
			isActive.appendChild(doc.createTextNode(String.valueOf(currentItem.getIsActive())));
			item.appendChild(isActive);
		}
		
		Storage.serialise(doc);
		System.out.println("File saved!");
	}
	
	/**
	 * This method writes into the XMl, i.e. 'todo.xml'
	 * @param doc
	 * @throws TransformerException
	 */
	public static void serialise(Document doc) throws TransformerException{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		// Output to file
		// StreamResult result = new StreamResult(new File(OUTPUT_XML));
		// Currently, output to console for testing only
		StreamResult result = new StreamResult(System.out);
		
		transformer.setOutputProperty(OutputKeys.INDENT, YES);
		transformer.setOutputProperty(XML_INDENT, INDENT_AMOUNT);
		transformer.transform(source, result);
	}
}
