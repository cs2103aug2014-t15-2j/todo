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

public class XMLWriter {

	/**
	 * Default constructor
	 */
	public XMLWriter() {

	}

	/**
	 * This method creates a XML file 'todo.xml' and stores data according to
	 * the ItemList that gets passed in
	 * 
	 * @param ItemList
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public void writeIntoXML(ItemList il) throws ParserConfigurationException, TransformerException {
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		Element rootElement = doc.createElement(Storage.ITEM_LIST);
		doc.appendChild(rootElement);

		Element itemQty = doc.createElement(Storage.ITEM_QUANTITY);
		itemQty.appendChild(doc.createTextNode(String.valueOf(Item.getItemQty())));
		rootElement.appendChild(itemQty);

		Element lastItemId = doc.createElement(Storage.LAST_ITEM_ID);
		lastItemId.appendChild(doc.createTextNode(String.valueOf(Item.getLastItemId())));
		rootElement.appendChild(lastItemId);

		for (int i = 0; i < il.size(); i++) {
			Item currentItem = il.getItem(i);

			Element item = doc.createElement(Storage.ITEM);
			rootElement.appendChild(item);

			Element itemId = doc.createElement(Storage.ITEM_ID);
			itemId.appendChild(doc.createTextNode(String.valueOf(currentItem.getItemId())));
			item.appendChild(itemId);

			Element importance = doc.createElement(Storage.IMPORTANCE);
			importance.appendChild(doc.createTextNode(String.valueOf(currentItem.getImportance())));

			item.appendChild(importance);

			Element description = doc.createElement(Storage.DESCRIPTION);
			if (currentItem.getDescription() == null) {
				description.appendChild(doc.createTextNode(Storage.EMPTY));
			} else {
				description.appendChild(doc.createTextNode(currentItem.getDescription()));
			}
			item.appendChild(description);

			// ----------------------Storing of startDateTime-----------------------
			Element startDateTime = doc.createElement(Storage.START_DATE_TIME);
			item.appendChild(startDateTime);

			DateTime sdt = currentItem.getStartDateTime();
			Element sdtHasTime = doc.createElement(Storage.HAS_TIME);
			Element sdtDateTime = doc.createElement(Storage.DATE_TIME);
			if (sdt == null) {
				sdtHasTime.appendChild(doc.createTextNode(Storage.EMPTY));
				sdtDateTime.appendChild(doc.createTextNode(Storage.EMPTY));
			} else {
				sdtHasTime.appendChild(doc.createTextNode(String.valueOf(sdt.hasTime())));

				if (sdt.hasTime()) {
					String dateWithTime = sdt.getDate().toString();
					dateWithTime = dateWithTime.replace(Storage.CAPITAL_T, Storage.SPACE);
					sdtDateTime.appendChild(doc.createTextNode(dateWithTime));
				} else {
					String dateWithoutTime = sdt.getDate().toString();
					int indexOfT = dateWithoutTime.indexOf(Storage.CAPITAL_T);
					dateWithoutTime = dateWithoutTime.substring(Storage.ZERO, indexOfT);
					sdtDateTime.appendChild(doc.createTextNode(dateWithoutTime));
				}
			}
			startDateTime.appendChild(sdtHasTime);
			startDateTime.appendChild(sdtDateTime);
			// ----------------------Storing of startDateTime-----------------------

			// -----------------------Storing of dueDateTime------------------------
			Element dueDateTime = doc.createElement(Storage.DUE_DATE_TIME);
			item.appendChild(dueDateTime);

			DateTime ddt = currentItem.getDueDateTime();
			Element ddtHasTime = doc.createElement(Storage.HAS_TIME);
			Element ddtDateTime = doc.createElement(Storage.DATE_TIME);
			if (ddt == null) {
				ddtHasTime.appendChild(doc.createTextNode(Storage.EMPTY));
				ddtDateTime.appendChild(doc.createTextNode(Storage.EMPTY));
			} else {
				ddtHasTime.appendChild(doc.createTextNode(String.valueOf(ddt.hasTime())));

				if (ddt.hasTime()) {
					String dateWithTime = ddt.getDate().toString();
					dateWithTime = dateWithTime.replace(Storage.CAPITAL_T, Storage.SPACE);
					ddtDateTime.appendChild(doc.createTextNode(dateWithTime));
				} else {
					String dateWithoutTime = (ddt.getDate().toString());
					int indexOfT = dateWithoutTime.indexOf(Storage.CAPITAL_T);
					dateWithoutTime = dateWithoutTime.substring(Storage.ZERO, indexOfT);
					ddtDateTime.appendChild(doc.createTextNode(dateWithoutTime));
				}
			}
			dueDateTime.appendChild(ddtHasTime);
			dueDateTime.appendChild(ddtDateTime);
			// -----------------------Storing of dueDateTime------------------------

			Element location = doc.createElement(Storage.LOCATION);
			if (currentItem.getLocation() == null) {
				location.appendChild(doc.createTextNode(Storage.EMPTY));
			} else {
				location.appendChild(doc.createTextNode(currentItem.getLocation()));
			}
			item.appendChild(location);

			Element tags = doc.createElement(Storage.TAGS);
			item.appendChild(tags);

			ArrayList<String> tagsToBeStored = currentItem.getTags();
			for (int j = 0; j < tagsToBeStored.size(); j++) {
				Element tag = doc.createElement(Storage.TAG);
				tag.appendChild(doc.createTextNode(tagsToBeStored.get(j)));
				tags.appendChild(tag);
			}

			Element isActive = doc.createElement(Storage.IS_COMPLETED);
			isActive.appendChild(doc.createTextNode(String.valueOf(currentItem.getStatus())));
			item.appendChild(isActive);
		}

		serialise(doc);
	}

	/**
	 * This method writes into the XMl, i.e. 'todo.xml' with the Document being
	 * passed in
	 * 
	 * @param doc
	 * @throws TransformerException
	 */
	private void serialise(Document doc) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(Storage.FILE_DESTINATION));

		// Set todo.xml style to be human-readable
		transformer.setOutputProperty(OutputKeys.INDENT, Storage.YES);
		transformer.setOutputProperty(Storage.XML_INDENT, Storage.INDENT_AMOUNT);
		transformer.transform(source, result);
	}

}
