package todo.model;

import java.util.ArrayList;
import java.util.Date;

public class testFilter {
	public static void main(String arg[]){
		ArrayList<String> tagString1 = new ArrayList<String>();
		tagString1.add("work");
		tagString1.add("Business");
		tagString1.add("fun");
		
		ArrayList<String> tagString2 = new ArrayList<String>();
		tagString2.add("work");
		tagString2.add("fun");
		
		String location = new String("school");
		
		Item item1 = new Item("item1", location, true, tagString1);
		Item item2 = new Item("item2", location, false, tagString2);
		Item item3 = new Item("item3", new DateTime(new Date((long)1413950400000.0)));
		Item item4 = new Item("item4", new DateTime(new Date((long)1409544000000.0)));
		Item item5 = new Item("item5", new DateTime(new Date((long)1409544000000.0)));

		ItemList myList = new ItemList();
		myList.add(item1);
		myList.add(item2);
		myList.add(item3);
		myList.add(item4);
		myList.add(item5);
		
		//myList.getItem(-5);
		myList.filterByDateTime("10/22/2014");
		//System.out.println(myList.filterByDateTime(new Date((long)1409544000000.0)));
		//System.out.println(myList.filterByTags("work % fun"));
		
	}

}
