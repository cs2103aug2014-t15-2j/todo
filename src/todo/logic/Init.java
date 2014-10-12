package todo.logic;

import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.storage.Storage;

public class Init {
	public static void init() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException{
		Data.storage = new Storage();
		
		// read date from data file
		Data.mItemList = Data.storage.readDataFromFile();
	}
}
