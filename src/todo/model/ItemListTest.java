package todo.model;

import static org.junit.Assert.*;
import org.junit.Test;

//@author A0115992X
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
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetItem(){
		Item itemA = new Item(item1);
		Item itemB = new Item(item2);
		
		ItemList myList = new ItemList();
		
		myList.add(itemA);
		myList.add(itemB);
		
		// This is a boundary case for the "valid index" partition
		
		assertEquals(myList.getItem(0), itemA);
		assertEquals(myList.getItem(1), itemB);
		
		// This is a boundary case for the "negative index" partition
		myList.getItem(-1);
		myList.getItem(-2);
	}
	
	@Test
	public void testAdd(){
		Item itemA = new Item(item1);
		Item itemB = new Item(item2);
		
		ItemList myList = new ItemList();
		
		assertEquals(myList.size(), 0);
		
		myList.add(itemA);
		assertEquals(myList.size(), 1);
		
		myList.add(itemB);
		assertEquals(myList.size(), 2);
		
		assertEquals(myList.getItem(0), itemA);
		
	}
	
	@Test
	public void testDelete(){
		Item itemA = new Item(item1);
		Item itemB = new Item(item2);
		
		ItemList myList = new ItemList();
		
		myList.add(itemA);
		myList.add(itemB);
		assertEquals(myList.size(),2);
		
		myList.delete(2);
		assertEquals(myList.size(),1);
		
		myList.delete(1);
		assertEquals(myList.size(),0);
		
	}

}
