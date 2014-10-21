package todo.model;

import java.util.ArrayList;

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
		
		Item item1 = new Item("item1", location, 0, tagString1);
		Item item2 = new Item("item2", location, 0, tagString2);

		ItemList myList = new ItemList();
		myList.add(item1);
		myList.add(item2);
		
		System.out.println(myList.filterByTags("work % fun"));
		
	}

}
