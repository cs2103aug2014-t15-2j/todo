package todo.model;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

public class StateHistoryTest {

	
	@Test
	public void testCorrectnessOfSavedState() throws ParseException {
		//the start Creation of dummy ItemList---------------
		ArrayList<String> tl1 = new ArrayList<String>();
		Item item1 = new Item("meet yy", new DateTime(new Date()), new DateTime(new Date()), "clementi", true, tl1);
				
		ArrayList<String> tl2 = new ArrayList<String>();
		tl2.add("important");
		tl2.add("Bring Laptop");
		Item item2 = new Item("meet sw", new DateTime(new Date()), new DateTime(new Date()), "utown", false, tl2);
				
		ArrayList<String> tl3 = new ArrayList<String>();
		tl3.add("bring textbook");
		tl3.add("last lecture");
		Item item3 = new Item("go to lecture", new DateTime(new Date()), new DateTime(new Date()), "lt19", false, tl3);
				
		ItemList newItemList = new ItemList();
		newItemList.add(item1);
		newItemList.add(item2);
		newItemList.add(item3);
		//the end Creation of dummy ItemList---------------

		StateHistory newStateHistory = new StateHistory();
		
		//save once to history stack
		newStateHistory.saveStateToHistory(newItemList);
		//once i save, i should be able to undo, but not redo
		assertEquals(true, newStateHistory.canUndo());
		assertEquals(false, newStateHistory.canRedo());
		
		//save several same ItemList to future stack, then pop all
		newStateHistory.saveStateToFuture(newItemList);
		newStateHistory.saveStateToFuture(newItemList);
		newStateHistory.saveStateToFuture(newItemList);
		newStateHistory.popAllFromFuture();
		//since ItemList in future were all popped, redo shouldn't be allowed
		assertEquals(false, newStateHistory.canRedo());
		
		//get the last pushed ItemList
		ItemList poppedItemList = newStateHistory.undo();
		//compare if the values are correct
		Item savedItem1 = poppedItemList.getItem(0);
		Item savedItem2 = poppedItemList.getItem(1);
		Item savedItem3 = poppedItemList.getItem(2);
		assertEquals(savedItem1.getDescription(), "meet yy");
		assertEquals(savedItem1.getLocation(), "clementi");
		
		assertEquals(savedItem2.getTags().get(0), "important");
		assertEquals(savedItem2.getTags().get(1), "Bring Laptop");
		assertEquals(savedItem2.getDescription(), "meet sw");
		assertEquals(savedItem2.getLocation(), "utown");
		
		assertEquals(savedItem3.getTags().get(0), "bring textbook");
		assertEquals(savedItem3.getTags().get(1), "last lecture");
		assertEquals(savedItem3.getDescription(), "go to lecture");
		assertEquals(savedItem3.getLocation(), "lt19");
		
		
		 /* This is a boundary case for the ��null�� partition */
		///assertEquals(newStateHistory.saveStateToFuture(null), false);
		/* This is a boundary case for the ��non-null�� partition */
		//assertEquals(newStateHistory.saveStateToFuture(newItemList), true);
	}

}
