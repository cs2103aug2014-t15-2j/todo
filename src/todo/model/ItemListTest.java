package todo.model;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class ItemListTest {
	String item1 = "item-1";
	String item2 = "item-2";
	String item3 = "item-3";
	String item4 = "item-4";
	
	@Test
	public void testSize() {
		
		ItemList myList = new ItemList();
		
		assertEquals(myList.size(), 0);
		
		Item itemA = new Item(item1);
		myList.add(itemA);		
		
		assertEquals(myList.size(), 1);
		
		Item itemB = new Item(item2);
		myList.add(itemB);
		assertEquals(myList.size(), 2);
		
	}
	
	@Test
	public void testGetItem(){
		Item itemA = new Item(item1);
		Item itemB = new Item(item2);
		
		ItemList myList = new ItemList();
		
		myList.add(itemA);
		myList.add(itemB);
		
		assertEquals(myList.getItem(1), itemA);
		assertEquals(myList.getItem(2), itemB);
		
	}

}
