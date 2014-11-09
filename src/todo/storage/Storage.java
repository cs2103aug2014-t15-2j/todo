package todo.storage;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.model.ItemList;

//@author A0098155W
/**
 * This is the facade class of Storage component that interacts which outside
 * components.
 */
public class Storage {

	public static final String FILE_DESTINATION = "todo.xml";
	public static final String FILE_YES = "yes";
	public static final String FILE_INDENT = "{http://xml.apache.org/xslt}indent-amount";
	public static final String FILE_INDENT_AMOUNT = "4";

	public static final String XML_ITEM_LIST = "ItemList";
	public static final String XML_ITEM_QUANTITY = "itemQty";
	public static final String XML_LAST_ITEM_ID = "lastItemId";
	public static final String XML_ITEM = "Item";
	public static final String XML_ITEM_ID = "itemId";
	public static final String XML_IMPORTANCE = "importance";
	public static final String XML_DESCRIPTION = "description";
	public static final String XML_START_DATE_TIME = "startDateTime";
	public static final String XML_DUE_DATE_TIME = "dueDateTime";
	public static final String XML_HAS_TIME = "hasTime";
	public static final String XML_DATE_TIME = "DateTime";
	public static final String XML_LOCATION = "location";
	public static final String XML_TAG_LIST = "tagList";
	public static final String XML_TAG = "tag";
	public static final String XML_IS_COMPLETED = "isCompleted";

	public static final String WARNING_READ_ERROR = "Error when reading external file";
	public static final String WARNING_WRITE_ERROR = "Error when writing external file";

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
	public static final String EMPTY = "";
	public static final String SPACE = " ";
	public static final String TRUE = "true";
	public static final String CAPITAL_T = "T";
	public static final int ZERO = 0;
	public static final int ONE = 1;

	private XmlReader xmlReader;
	private XmlWriter xmlWriter;

	private static final Logger logger = Logger.getLogger(Storage.class .getName());

	public Storage() {
		xmlWriter = new XmlWriter();
		xmlReader = new XmlReader();
	}

	public ItemList readDataFromFile() {
		ItemList newItemList = null;

		try {
			newItemList = xmlReader.read();
		} catch (ParserConfigurationException | SAXException | IOException |
				DOMException | ParseException ex) {
			logger.setLevel(Level.WARNING);
			logger.warning(WARNING_READ_ERROR);
			logger.warning(ex.toString());
		}
		
		return newItemList;
	}

	public void saveDataToFile(ItemList mItemList) {
		try {
			xmlWriter.write(mItemList);
		} catch (ParserConfigurationException | TransformerException ex) {
			logger.setLevel(Level.WARNING);
			logger.warning(WARNING_WRITE_ERROR);
			logger.warning(ex.toString());
		}
	}
}