package todo.model;

import java.util.Date;

public class testTemp {
	
	public static void main(String [] args){
		
		ItemList myList = new ItemList();
		myList.add(new Item("basketball training", new DateTime(new Date(1411876800000L))));
		myList.add(new Item("Going for lab session"));
		myList.add(new Item("attend meeting", new DateTime(new Date(1398916800000L))));
		myList.add(new Item("finish homework"));
		myList.add(new Item("Doing X", new DateTime(new Date(1404273600000L))));
		

				
		myList.displayList();
		
	//	myList.delete(2);
		myList.sortByTimeDecreasing();
		myList.displayList();

		
	}

}
