package todo.storage;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
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

/**
 * This class reads from external file with destination - Storage.FILE_DESTINATION.
 * 
 * @author Lui
 *
 */
public class XmlReader {
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Storage.DATE_FORMAT);

	public XmlReader() {

	}

	/**
	 * This method reads the xml file 'todo.xml' and returns an ItemList
	 *
	 * @return ItemList
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 * @throws DOMException
	 */
	public ItemList read() throws ParserConfigurationException, SAXException, IOException, DOMException, ParseException {
		File file = new File(Storage.FILE_DESTINATION);
		if (!file.exists()) {
			return new ItemList();
		}

		Document doc = initDocument(file);
		ItemList newItemList = buildItemList(doc);
		setItemStaticAttributes(doc);

		return newItemList;
	}

	private Document initDocument(File newFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();
		DocumentBuilder dB = dBF.newDocumentBuilder();
		Document doc = dB.parse(newFile);
		doc.getDocumentElement().normalize();
		
		return doc;
	}

	/**
	 * This method sets two Item's static attributes.
	 * 
	 * @param doc
	 */
	private void setItemStaticAttributes(Document doc) {
		Node itemQty = doc.getElementsByTagName(Storage.XML_ITEM_QUANTITY).item(Storage.ZERO);
		Item.setItemQty(Integer.parseInt(itemQty.getTextContent()));

		Node lastItemId = doc.getElementsByTagName(Storage.XML_LAST_ITEM_ID).item(Storage.ZERO);
		Item.setLastItemID(Integer.parseInt(lastItemId.getTextContent()));
	}

	/**
	 * This method traverses xml nodes, reads their values and stores into an
	 * Itemlist that is to be returned
	 *
	 * @param doc
	 * @return ItemList
	 * @throws DOMException
	 * @throws ParseException
	 */
	private ItemList buildItemList(Document doc) throws DOMException, ParseException {
		ItemList newItemList = new ItemList();
		NodeList items = doc.getElementsByTagName(Storage.XML_ITEM);

		for (int i = 0; i < items.getLength(); i++) {
			Item newItem = new Item();
			setItemId(doc, i, newItem);
			setImportance(doc, i, newItem);
			setDescription(doc, i, newItem);
			setStartDateTime(doc, i, newItem);
			setDueDateTime(doc, i, newItem);
			setLocation(doc, i, newItem);
			setTagList(doc, i, newItem);
			setStatus(doc, i, newItem);

			newItemList.add(newItem);
		}

		return newItemList;
	}

	private void setItemId(Document doc, int i, Item newItem) {
		Node itemId = doc.getElementsByTagName(Storage.XML_ITEM_ID).item(i);

		// -1 because setItemId always + 1 before itemId is set
		newItem.setItemID(Integer.parseInt(itemId.getTextContent()) - Storage.ONE);
	}
	
	private void setImportance(Document doc, int i, Item newItem) {
		Node importance = doc.getElementsByTagName(Storage.XML_IMPORTANCE).item(i);
		
		if (importance.getTextContent().equals(Storage.TRUE)) {
			newItem.setImportance(true);
		}
	}
	
	private void setDescription(Document doc, int i, Item newItem) {
		Node description = doc.getElementsByTagName(Storage.XML_DESCRIPTION).item(i);
		newItem.setDescription(description.getTextContent());
	}
	
	private void setStartDateTime(Document doc, int i, Item newItem) {
		Node sdt = doc.getElementsByTagName(Storage.XML_START_DATE_TIME).item(i);
		Element sdtElement = (Element) sdt;
		Node sdtHasTime = sdtElement.getElementsByTagName(Storage.XML_HAS_TIME).item(Storage.ZERO);
		Node sdtDateTime = sdtElement.getElementsByTagName(Storage.XML_DATE_TIME).item(Storage.ZERO);

		if (!sdtDateTime.getTextContent().equals(Storage.EMPTY)) {
			LocalDateTime sDate = LocalDateTime.parse(sdtDateTime.getTextContent(), formatter);
			if (sdtHasTime.getTextContent().equals(Storage.TRUE)) {
				newItem.setStartDateTime(new DateTime(sDate, true));
			} else {
				newItem.setStartDateTime(new DateTime(sDate, false));
			}
		}
	}
	
	private void setDueDateTime(Document doc, int i, Item newItem) {
		Node ddt = doc.getElementsByTagName(Storage.XML_DUE_DATE_TIME).item(i);
		Element ddtElement = (Element) ddt;
		Node ddtHastTime = ddtElement.getElementsByTagName(Storage.XML_HAS_TIME).item(Storage.ZERO);
		Node ddtDateTime = ddtElement.getElementsByTagName(Storage.XML_DATE_TIME).item(Storage.ZERO);
		
		if (!ddtDateTime.getTextContent().equals(Storage.EMPTY)) {
			LocalDateTime dDate = LocalDateTime.parse(ddtDateTime.getTextContent(), formatter);
			
			if (ddtHastTime.getTextContent().equals(Storage.TRUE)) {
				newItem.setDueDateTime(new DateTime(dDate, true));
			} else {
				newItem.setDueDateTime(new DateTime(dDate, false));
			}
		}
	}
	
	private void setLocation(Document doc, int i, Item newItem) {
		Node location = doc.getElementsByTagName(Storage.XML_LOCATION).item(i);
		newItem.setLocation(location.getTextContent());
	}
	
	private void setTagList(Document doc, int i, Item newItem) {
		Node tags = doc.getElementsByTagName(Storage.XML_TAG_LIST).item(i);
		Element tagsElement = (Element) tags;
		NodeList allTags = tagsElement.getElementsByTagName(Storage.XML_TAG);
		ArrayList<String> newTags = new ArrayList<String>();
		
		for (int j = 0; j < allTags.getLength(); j++) {
			newTags.add(allTags.item(j).getTextContent());
		}
		
		newItem.setTags(newTags);
	}
	
	private void setStatus(Document doc, int i, Item newItem) {
		Node isCompleted = doc.getElementsByTagName(Storage.XML_IS_COMPLETED).item(i);
		
		if (isCompleted.getTextContent().equals(Storage.TRUE)) {
			newItem.setStatusDone();
		} else {
			newItem.setStatusUndone();
		}
	}
}
