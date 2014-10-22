package todo.storage;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.model.DateTime;
import todo.model.Item;
import todo.model.ItemList;

public class StorageTest {

	@Test
	public void testCorrectnessOfStoredData() throws ParserConfigurationException,
			TransformerException, DOMException, SAXException, IOException,
			ParseException {
		try {
			Storage storage = new Storage();
			// the start Creation of dummy ItemList---------------
			Item item1 = new Item("meet yy", new DateTime(new Date()),
					new DateTime(new Date()), "clementi", 0,
					new ArrayList<String>());

			ArrayList<String> al2 = new ArrayList<String>();
			al2.add("Important");
			al2.add("Bring Laptop");
			Item item2 = new Item("meet sw", new DateTime(new Date()),
					new DateTime(new Date()), "utown", 2, al2);

			ArrayList<String> al3 = new ArrayList<String>();
			al3.add("bring textbook");
			al3.add("last lecture");
			Item item3 = new Item("go to lecture", new DateTime(new Date()),
					new DateTime(new Date()), "lt19", 3, al3);

			ItemList il = new ItemList();
			il.add(item1);
			il.add(item2);
			il.add(item3);
			il.add(new Item());
			// the end Creation of dummy ItemList---------------

			storage.saveDataToFile(il);

			ItemList savedil = storage.readDataFromFile();
			Item savedItem1 = savedil.getItem(0);
			Item savedItem2 = savedil.getItem(1);
			Item savedItem3 = savedil.getItem(2);
			Item savedItem4 = savedil.getItem(3);

			assertEquals("meet yy", savedItem1.getDescription());
			assertEquals("clementi", savedItem1.getLocation());
			
			assertEquals("Important", savedItem2.getTags().get(0));
			assertEquals("Bring Laptop", savedItem2.getTags().get(1));
			
			assertEquals("bring textbook", savedItem3.getTags().get(0));
			assertEquals("last lecture", savedItem3.getTags().get(1));
			assertEquals("go to lecture", savedItem3.getDescription());
			assertEquals("lt19", savedItem3.getLocation());
			
			//Item4 does not have description
			assertEquals("", savedItem4.getDescription());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
