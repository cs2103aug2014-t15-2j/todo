package todo.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
public class ItemTest {
	String item1 = "item-1";
	String item2 = "item-2";
	String item3 = "item-3";
	String item4 = "item-4";
	String location1 = "SchoolOfComputing";
	String locatin2 = "Utown";
	int priority1 = 1;
	Date currentDate1 = new Date();
	DateTime testDateTime1 = new DateTime(currentDate1);
	Date currentDate2 = new Date();
	DateTime testDateTime2 = new DateTime(currentDate2);
	Date currentDate3 = new Date();
	DateTime testDateTime3 = new DateTime(currentDate3);
	Date currentDate4 = new Date();
	DateTime testDateTime4 = new DateTime(currentDate4);
	
	@Test
	public void testItemConstructor() {
		System.out.println(Item.getItemQty());
		Item abc = new Item (item1,testDateTime1);
		Item def = new Item (item1,testDateTime1);
		System.out.println(Item.getItemQty());
		assertEquals(abc.getDescription(),def.getDescription());
		assertEquals(abc.getStartDateTime(),def.getStartDateTime());
		assertEquals(Item.getItemQty(),2);
		
	}
	
	@Test
	public void testStaticAttributes() {
		System.out.println(Item.getItemQty());
		Item ghi = new Item(item2,testDateTime2);
		System.out.println(Item.getItemQty());
		assertEquals(3,Item.getItemQty());
		assertEquals(3,Item.getLastItemId());
	}
	
	
	@Test
	public void testTagsInput() {
		ArrayList<String> tagList = new ArrayList<String>();
		tagList.add("homework");
		tagList.add("school");
		Item jkl = new Item (item4,testDateTime4);
		jkl.setTags(tagList);
		assertEquals(tagList,jkl.getTags());
		assertEquals(4,Item.getItemQty());
		assertEquals(4,Item.getLastItemId());
	}
	@Test
	public void testAllConstructors() {
		ArrayList<String> tagList = new ArrayList<String>();
		tagList.add("homework");
		tagList.add("school");
		
		Item abc1 = new Item(item1);
		abc1.setStatusDone();
		System.out.println(abc1.toString());
		Item abc2 = new Item(item1,testDateTime1);
		System.out.println(abc2.toString());
		Item abc3 = new Item(item1,priority1,tagList);
		System.out.println(abc3.toString());
		Item abc4 = new Item(item1,tagList,priority1,location1);
		System.out.println(abc4.toString());
		Item abc5 = new Item(item1, testDateTime1, testDateTime2, location1, priority1, tagList);
		System.out.println(abc5.toString());
		
		assertEquals(9,Item.getItemQty());
		assertEquals(9,Item.getLastItemId());
	}
	
}
