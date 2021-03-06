package todo.storage;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.model.DateTime;
import todo.model.Item;
import todo.model.ItemList;

//@author A0098155W
/**
 * Unit test for Storage class.
 */
public class StorageTest {

	@Test
	public void testCorrectnessOfStoredData()
			throws ParserConfigurationException, TransformerException,
			DOMException, SAXException, IOException, ParseException {

		// the start Creation of dummy ItemList---------------
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Storage.DATE_FORMAT);
		String description1 = "meet yy";
		LocalDateTime startDateTime1 = LocalDateTime.parse("2014-11-10 21:00", formatter);
		DateTime sdateTime1 = new DateTime(startDateTime1, true);
		LocalDateTime dueDateTime1 = LocalDateTime.parse("2014-11-10 21:30", formatter);
		DateTime ddateTime1 = new DateTime(dueDateTime1, true);
		String location1 = "clementi";
		boolean isImportant1 = true;
		ArrayList<String> tagList1 = new ArrayList<String>();
		Item item1 = new Item(description1, sdateTime1, ddateTime1, location1, isImportant1, 
								tagList1);
		item1.setStatusDone();
		
		String description2 = "meeting profs";
		LocalDateTime startDateTime2 = LocalDateTime.parse("2014-11-04 21:00", formatter);
		DateTime sdateTime2 = new DateTime(startDateTime2, false);
		LocalDateTime dueDateTime2 = LocalDateTime.parse("2014-11-05 21:30", formatter);
		DateTime ddateTime2 = new DateTime(dueDateTime2, true);
		String location2 = "utown";
		boolean isImportant2 = true;
		ArrayList<String> tagList2 = new ArrayList<String>();
		tagList2.add("Important");
		tagList2.add("Bring Laptop");
		Item item2 = new Item(description2, sdateTime2, ddateTime2, location2, isImportant2,
								tagList2);

		String description3 = "go to lecture";
		LocalDateTime startDateTime3 = LocalDateTime.parse("2014-11-07 19:00", formatter);
		DateTime sdateTime3 = new DateTime(startDateTime3, true);
		LocalDateTime dueDateTime3 = LocalDateTime.parse("2014-11-08 20:30", formatter);
		DateTime ddateTime3 = new DateTime(dueDateTime3, false);
		String location3 = "lt19";
		boolean isImportant3 = false;
		ArrayList<String> tagList3 = new ArrayList<String>();
		tagList3.add("bring textbook");
		tagList3.add("last lecture");
		Item item3 = new Item(description3, sdateTime3, ddateTime3, location3, isImportant3,
								tagList3);

		ItemList il = new ItemList();
		il.add(item1);
		il.add(item2);
		il.add(item3);
		// the end Creation of dummy ItemList---------------
		
		Storage storage = new Storage();
		// save to file
		storage.saveDataToFile(il);
		// read from file
		ItemList savedil = storage.readDataFromFile();
		
		Item savedItem1 = savedil.getItem(0);
		Item savedItem2 = savedil.getItem(1);
		Item savedItem3 = savedil.getItem(2);

		//check savedItem1's attributes
		assertEquals("meet yy", savedItem1.getDescription());
		assertEquals("clementi", savedItem1.getLocation());
		assertEquals(true, savedItem1.getStatus());
		assertEquals(true, savedItem1.getImportance());
		assertEquals("2014-11-10 21:00", String.valueOf(savedItem1.getStartDateTime()));
		assertEquals(true, savedItem1.getStartDateTime().hasTime());
		assertEquals("2014-11-10 21:30", String.valueOf(savedItem1.getDueDateTime()));
		assertEquals(true, savedItem1.getDueDateTime().hasTime());

		//check savedItem2's attributes
		assertEquals("meeting profs", savedItem2.getDescription());
		assertEquals("utown", savedItem2.getLocation());
		assertEquals(false, savedItem2.getStatus());
		assertEquals(true, savedItem2.getImportance());
		assertEquals("2014-11-04", String.valueOf(savedItem2.getStartDateTime()));
		assertEquals(false, savedItem2.getStartDateTime().hasTime());
		assertEquals("2014-11-05 21:30", String.valueOf(savedItem2.getDueDateTime()));
		assertEquals(true, savedItem2.getDueDateTime().hasTime());
		assertEquals("Important", savedItem2.getTags().get(0));
		assertEquals("Bring Laptop", savedItem2.getTags().get(1));

		//check savedItem3's attributes
		assertEquals("go to lecture", savedItem3.getDescription());
		assertEquals("lt19", savedItem3.getLocation());
		assertEquals(false, savedItem3.getStatus());
		assertEquals(false, savedItem3.getImportance());
		assertEquals("2014-11-07 19:00", String.valueOf(savedItem3.getStartDateTime()));
		assertEquals(true, savedItem3.getStartDateTime().hasTime());
		assertEquals("2014-11-08", String.valueOf(savedItem3.getDueDateTime()));
		assertEquals(false, savedItem3.getDueDateTime().hasTime());
		assertEquals("bring textbook", savedItem3.getTags().get(0));
		assertEquals("last lecture", savedItem3.getTags().get(1));
	}

}
