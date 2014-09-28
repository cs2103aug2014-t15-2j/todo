package todo.storage;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.model.DateTime;
import todo.model.Item;
import todo.model.ItemList;

public class StorageTest {
	public static void main(String[] args) throws ParserConfigurationException, TransformerException, SAXException, IOException, DOMException, ParseException{
		
		//the start Creation of dummy ItemList---------------
		Item item1 = new Item("meet yy", new DateTime(new Date()), new DateTime(new Date()), "clementi", 0, new ArrayList<String>());
		
		ArrayList<String> al2 = new ArrayList<String>();
		al2.add("Important");
		al2.add("Bring Laptop");
		Item item2 = new Item("meet sw", new DateTime(new Date()), new DateTime(new Date()), "utown", 2, al2);
		
		ArrayList<String> al3 = new ArrayList<String>();
		al3.add("bring textbook");
		al3.add("last lecture");
		Item item3 = new Item("go to lecture", new DateTime(new Date()), new DateTime(new Date()), "lt19", 3, al3);
		
		ItemList il = new ItemList();
		il.add(item1);
		il.add(item2);
		il.add(item3);
		il.add(new Item());
		//the end Creation of dummy ItemList---------------
		
		Storage.storeIntoXML(il);
		
	/*
		ItemList il = Storage.readFromXML();
		il.displayList();
		*/
	}
}

