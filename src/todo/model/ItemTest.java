package todo.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ItemTest {
	String item1 = "firstitem";
	String datetime1 = "friday fifth october";
	String item2 = "2nditem";
	String datetime2 = "saturday sixth october";
	String item3 = "3rditem";
	String datetime3 = "sunday fifth october";
	
	
	@Test
	public void testQtyMultiple() {
		Item abc = new Item(item1,datetime1);
		assertEquals(1,Item.getItemQty());
		Item def = new Item(item2,datetime2);
		assertEquals(2,Item.getItemQty());
		Item ghi = new Item(item3,datetime3);
		assertEquals(3,Item.getItemQty());
		
	}
	@Test
	public void testDataInput() {
		Item abc = new Item(item1,datetime1);
		assertEquals(abc.getDescription(),item1);
		assertEquals(abc.getStartDateTime(),datetime1);
		assertEquals(abc.getItemId(),1);
		assertEquals(abc.getLastItemId(),0);
		assertEquals(4,Item.getItemQty());
	}
	
}
