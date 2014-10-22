package todo.storage;

import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.model.ItemList;

/**
 * Facade class
 * 
 * @author Lui
 *
 */
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
	public static final String IS_COMPLETED = "isCompleted";

	// Related to date and time
	public static final String DATE_WITH_TIME = "MM/dd/yyyy HH:mm:ss";
	public static final String DATE_WITHOUT_TIME = "MM/dd/yyyy";

	// Other miscellaneous constant string and integer
	public static final String EMPTY = "";
	public static final String TRUE = "true";
	public static final int ZERO = 0;

	
	private XMLWriter writer;
	private XMLReader reader;

	public Storage() {
		writer = new XMLWriter();
		reader = new XMLReader();
	}

	public ItemList readDataFromFile() throws DOMException,
			ParserConfigurationException, SAXException, IOException,
			ParseException {
		return reader.readFromXML();
	}

	public void saveDataToFile(ItemList mItemList)
			throws ParserConfigurationException, TransformerException {
		writer.storeIntoXML(mItemList);
	}
}
