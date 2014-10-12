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
	
	private XMLWriter writer;
	private XMLReader reader;
	
	public Storage(){
		writer = new XMLWriter();
		reader = new XMLReader();
	}
	
	public ItemList readDataFromFile() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException{
		return reader.readFromXML();
	}
	
	public void saveDataToFile(ItemList mItemList) throws ParserConfigurationException, TransformerException{
		writer.storeIntoXML(mItemList);
	}
}
