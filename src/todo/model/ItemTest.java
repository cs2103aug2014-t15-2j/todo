package todo.model;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.time.*;

//@author A0111082Y

// NOTE SOME TEST CASES WRITTEN BEFORE EXTENSIVE MODIFICAL TO THE MODEL PACKAGE

import org.junit.Test;
public class ItemTest {
	String item1 = "item-1";
	String item2 = "item-2";
	String item3 = "item-3";
	String item4 = "item-4";
	String location1 = "SchoolOfComputing";
	String locatin2 = "Utown";
	boolean important = true;
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
		System.out.println("Currently testing: testItemConstructors: ");
		Item abc = new Item (item1,testDateTime1);
		Item def = new Item (item1,testDateTime1);
		
		assertEquals(abc.getDescription(),def.getDescription());
		assertEquals(abc.getStartDateTime(),def.getStartDateTime());
		System.out.println(currentDate4.getTime());
		Instant instant = Instant.ofEpochMilli(currentDate4.getTime());
		System.out.println("Instant from Date:\n" + instant);
		Date date = Date.from(instant);
		System.out.println("Date from Instant:\n" + date + " long: " + date.getTime());
		LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.of("+8"));
		System.out.println("LocalDateTime from Instant:\n" + ldt);
	}
		
	
	public void testTagsInput() {
		ArrayList<String> tagList = new ArrayList<String>();
		tagList.add("homework");
		tagList.add("school");
		Item jkl = new Item (item4,testDateTime4);
		jkl.setTags(tagList);
		assertEquals(tagList,jkl.getTags());
		assertEquals(4,Item.getItemQty());
		assertEquals(4,Item.getLastItemId());
		System.out.println();
	}
	
	@Test
	public void testAllConstructors() {
		System.out.println("Currently testing: testAllConstructors: ");
		ArrayList<String> tagList = new ArrayList<String>();
		tagList.add("homework");
		tagList.add("school");
		
		Item abc1 = new Item(item1);
		abc1.setStatusDone();
		System.out.println(abc1.toString());
		Item abc2 = new Item(item1,testDateTime1);
		System.out.println(abc2.toString());
		Item abc3 = new Item(item1,important,tagList);
		System.out.println(abc3.toString());
		Item abc4 = new Item(item1,location1, important, tagList);
		System.out.println(abc4.toString());
		Item abc5 = new Item(item1, testDateTime1, testDateTime2, location1, important, tagList);
		System.out.println(abc5.toString());
		System.out.println();
		
	}
	@Test
	public void testItemList() {
		System.out.println("Currently testing: testItemList: ");
		ItemList myList = new ItemList();
		
		ArrayList<String> tagList = new ArrayList<String>();
		tagList.add("homework");
		tagList.add("school");
		
		Item abc1 = new Item(item1);
		abc1.setStatusDone();
		
		Item abc2 = new Item(item1,testDateTime1);
	
		Item abc3 = new Item(item1,important,tagList);
		
		Item abc4 = new Item(item1,location1,important, tagList);
		
		Item abc5 = new Item(item1, testDateTime1, testDateTime2, location1, important, tagList);

		myList.add(abc1);
		myList.add(abc2);
		myList.add(abc3);
		myList.add(abc4);
		myList.add(abc5);
		myList.toString();
		System.out.println();
	}
	@Test
	public void testItemListSort() {
		System.out.println("Currently testing: testItemListSort: ");
		ItemList myList = new ItemList();
		
		ArrayList<String> tagList = new ArrayList<String>();
		tagList.add("homework");
		tagList.add("school");
		
		Item abc1 = new Item(item1);
		abc1.setStatusDone();
		
		Item abc2 = new Item(item1,testDateTime1);
	
		Item abc3 = new Item(item1,important,tagList);
		
		Item abc4 = new Item(item1,location1, important, tagList);
		
		Item abc5 = new Item(item1, testDateTime1, testDateTime2, location1, important, tagList);

		myList.add(abc1);
		myList.add(abc2);
		myList.add(abc3);
		myList.add(abc4);
		myList.add(abc5);
		myList.toString();
		
		//myList.sortByTimeDecreasing();
		System.out.println(myList.toString());
		System.out.println();
		
	}
	
	@Test
	public void CompletedListDisplayTest() {
		System.out.println("Currently testing: testItemListSort: ");
		ItemList myList = new ItemList();
		
		ArrayList<String> tagList = new ArrayList<String>();
		tagList.add("homework");
		tagList.add("school");
		
		Item abc1 = new Item("item1");
		abc1.setStatusDone();
		
		Item abc2 = new Item("item2",testDateTime1);
	
		Item abc3 = new Item("item3",important,tagList);
		
		Item abc4 = new Item("item4",location1, important, tagList);
		abc4.setStatusDone();
		
		Item abc5 = new Item("item5", testDateTime1, testDateTime2, location1, important, tagList);

		myList.add(abc1);
		myList.add(abc2);
		myList.add(abc3);
		myList.add(abc4);
		myList.add(abc5);
		System.out.println(myList.toString());
		System.out.println("Completed: ");
		System.out.println(myList.showCompletedListString());
		System.out.println("UnCompleted: ");
		System.out.println(myList.showUncompletedListString());
		
		
		
	}
	@Test
	public void cloneTest() throws ParseException {
		System.out.println("Currently testing: cloneTest: ");
		ArrayList<String> tagList = new ArrayList<String>();
		tagList.add("homework");
		tagList.add("school");
		Item abc5 = new Item("item5", testDateTime1, testDateTime2, location1, important, tagList);
		
	assertEquals(abc5.toString(),abc5.cloneItem().toString());
	}
	
	@Test
	public void searchIndexTest() {
		System.out.println("Currently testing: testItemListSort: ");
		ItemList myList = new ItemList();
		
		ArrayList<String> tagList = new ArrayList<String>();
		tagList.add("homework");
		tagList.add("school");
		
		Item abc1 = new Item("item1");
		abc1.setStatusDone();
		
		Item abc2 = new Item("item2",testDateTime1);
	
		Item abc3 = new Item("item3",important,tagList);
		
		Item abc4 = new Item("item4",location1, important, tagList);
		abc4.setStatusDone();
		
		Item abc5 = new Item("item5", testDateTime1, testDateTime2, location1, important, tagList);
		System.out.println("BEFORE SORT");
		
		myList.add(abc3);
		myList.add(abc4);
		myList.add(abc5);
		myList.add(abc1);
		myList.add(abc2);
		myList.toString();
		System.out.println(myList.toString());
		System.out.println("After SORT");
		System.out.println(myList.toString());
	
		System.out.println ("key abc3 position : " +ItemList.searchIndex(myList, 16));
		
		
		
	}
	@Test 
	public void testDateComparing() {
		System.out.println("Currently testing: testDateTimeComparing: ");
		ItemList myList = new ItemList();
		ArrayList<String> tagList = new ArrayList<String>();
		tagList.add("homework");
		tagList.add("school");
		Item abc1 = new Item("item1");
		abc1.setStatusDone();
		Item abc2 = new Item("item2",testDateTime1);
		Item abc3 = new Item("item3",important,tagList);
		Item abc4 = new Item("item4",location1, important, tagList);
		abc4.setStatusDone();
		Item abc5 = new Item("item5", testDateTime1, testDateTime2, location1, important, tagList);
		if (DateTime.isInValidDate(abc5.getStartDateTime().getDate(),abc5.getDueDateTime().getDate().minusHours(10))){
			System.out.println("Invalid");
		}
		else {
			System.out.println("Valid");
		}
	}
}
