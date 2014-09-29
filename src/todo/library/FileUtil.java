package todo.library;

import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.model.ItemList;
import todo.storage.Storage;

public class FileUtil {
	public static ItemList readDataFromFile() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException{
		return Storage.readFromXML();
	}
	
	public static void saveDataToFile(ItemList mItemList) throws ParserConfigurationException, TransformerException{
		Storage.storeIntoXML(mItemList);
	}
}
