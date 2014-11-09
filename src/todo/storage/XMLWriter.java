package todo.storage;

import java.io.File;
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

public class XmlWriter {

	public XmlWriter() {

	}

	/**
	 * This method creates a XML file 'todo.xml' and stores data according to
	 * the ItemList that gets passed in.
	 *
	 * @param ItemList
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public void write(ItemList iL) throws ParserConfigurationException, TransformerException {
		Document doc = initializeDocument();
		Element root = createRoot(doc);

		appendItemQuantity(doc, root);
		appendLastItemId(doc, root);

		for (int i = 0; i < iL.size(); i++) {
			Item curItem = iL.getItem(i);
			Element item = appendItem(doc, root);

			appendItemId(doc, curItem, item);
			appendImportance(doc, curItem, item);
			appendDescription(doc, curItem, item);
			appendStartDateTime(doc, curItem, item);
			appendDueDateTime(doc, curItem, item);
			appendLocation(doc, curItem, item);
			appendTagList(doc, curItem, item);
			appendStatus(doc, curItem, item);
		}

		serialise(doc);
	}

	private Document initializeDocument() throws ParserConfigurationException {
		DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();
		DocumentBuilder dB = dBF.newDocumentBuilder();
		Document doc = dB.newDocument();
		
		return doc;
	}

	private Element createRoot(Document doc) {
		Element root = doc.createElement(Storage.XML_ITEM_LIST);
		doc.appendChild(root);

		return root;
	}

	private void appendItemQuantity(Document doc, Element root) {
		Element itemQty = doc.createElement(Storage.XML_ITEM_QUANTITY);
		itemQty.appendChild(doc.createTextNode(String.valueOf(Item.getItemQty())));
		root.appendChild(itemQty);
	}

	private void appendLastItemId(Document doc, Element root) {
		Element lastItemId = doc.createElement(Storage.XML_LAST_ITEM_ID);
		String lastItemIdText = String.valueOf(Item.getLastItemId());
		lastItemId.appendChild(doc.createTextNode(lastItemIdText));
		root.appendChild(lastItemId);
	}

	private Element appendItem(Document doc, Element root) {
		Element item = doc.createElement(Storage.XML_ITEM);
		root.appendChild(item);

		return item;
	}

	private void appendItemId(Document doc, Item curItem, Element item) {
		Element itemId = doc.createElement(Storage.XML_ITEM_ID);
		String itemIdText = String.valueOf(curItem.getItemId());
		itemId.appendChild(doc.createTextNode(itemIdText));
		item.appendChild(itemId);
	}

	private void appendImportance(Document doc, Item curItem, Element item) {
		Element importance = doc.createElement(Storage.XML_IMPORTANCE);
		String importanceText = String.valueOf(curItem.getImportance());
		importance.appendChild(doc.createTextNode(importanceText));
		item.appendChild(importance);
	}

	private void appendDescription(Document doc, Item curItem, Element item) {
		Element description = doc.createElement(Storage.XML_DESCRIPTION);
		String descriptionText = curItem.getDescription();

		if (descriptionText == null) {
			description.appendChild(doc.createTextNode(Storage.EMPTY));
		} else {
			description.appendChild(doc.createTextNode(descriptionText));
		}

		item.appendChild(description);
	}

	private void appendStartDateTime(Document doc, Item curItem, Element item) {
		Element startDateTime = doc.createElement(Storage.XML_START_DATE_TIME);
		item.appendChild(startDateTime);

		DateTime sdt = curItem.getStartDateTime();
		Element sdtHasTime = doc.createElement(Storage.XML_HAS_TIME);
		Element sdtDateTime = doc.createElement(Storage.XML_DATE_TIME);

		if (sdt == null) {
			sdtHasTime.appendChild(doc.createTextNode(Storage.EMPTY));
			sdtDateTime.appendChild(doc.createTextNode(Storage.EMPTY));
		} else {
			String hasTimeText = String.valueOf(sdt.hasTime());
			sdtHasTime.appendChild(doc.createTextNode(hasTimeText));
			sdtDateTime.appendChild(doc.createTextNode(sdt.writeString()));
		}

		startDateTime.appendChild(sdtHasTime);
		startDateTime.appendChild(sdtDateTime);
	}

	private void appendDueDateTime(Document doc, Item curItem, Element item) {
		Element dueDateTime = doc.createElement(Storage.XML_DUE_DATE_TIME);
		item.appendChild(dueDateTime);

		DateTime ddt = curItem.getDueDateTime();
		Element ddtHasTime = doc.createElement(Storage.XML_HAS_TIME);
		Element ddtDateTime = doc.createElement(Storage.XML_DATE_TIME);

		if (ddt == null) {
			ddtHasTime.appendChild(doc.createTextNode(Storage.EMPTY));
			ddtDateTime.appendChild(doc.createTextNode(Storage.EMPTY));
		} else {
			String hasTimeText = String.valueOf(ddt.hasTime());
			ddtHasTime.appendChild(doc.createTextNode(hasTimeText));
			ddtDateTime.appendChild(doc.createTextNode(ddt.writeString()));
		}

		dueDateTime.appendChild(ddtHasTime);
		dueDateTime.appendChild(ddtDateTime);
	}

	private void appendLocation(Document doc, Item curItem, Element item) {
		Element location = doc.createElement(Storage.XML_LOCATION);
		String locationText = curItem.getLocation();

		if (locationText == null) {
			location.appendChild(doc.createTextNode(Storage.EMPTY));
		} else {
			location.appendChild(doc.createTextNode(locationText));
		}

		item.appendChild(location);
	}

	private void appendTagList(Document doc, Item curItem, Element item) {
		Element tags = doc.createElement(Storage.XML_TAG_LIST);
		item.appendChild(tags);
		ArrayList<String> tagsToBeStored = curItem.getTags();

		for (int j = 0; j < tagsToBeStored.size(); j++) {
			Element tag = doc.createElement(Storage.XML_TAG);
			tag.appendChild(doc.createTextNode(tagsToBeStored.get(j)));
			tags.appendChild(tag);
		}
	}

	private void appendStatus(Document doc, Item curItem, Element item) {
		Element isCompleted = doc.createElement(Storage.XML_IS_COMPLETED);
		String isCompletedText = String.valueOf(curItem.getStatus());
		isCompleted.appendChild(doc.createTextNode(isCompletedText));
		item.appendChild(isCompleted);
	}

	/**
	 * This method writes into the XML, i.e. 'todo.xml' with the Document being
	 * passed in.
	 *
	 * @param doc
	 * @throws TransformerException
	 */
	private void serialise(Document doc) throws TransformerException {
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer trans = transFactory.newTransformer();
		DOMSource domSource = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(Storage.FILE_DESTINATION));

		// Set todo.xml to be human-readable for advanced user to manipulate
		trans.setOutputProperty(OutputKeys.INDENT, Storage.FILE_YES);
		trans.setOutputProperty(Storage.FILE_INDENT, Storage.FILE_INDENT_AMOUNT);
		trans.transform(domSource, result);
	}
}