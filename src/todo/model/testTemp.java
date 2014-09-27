package todo.model;

import java.util.Date;

public class testTemp {
	
	public static void main(String [] args){
		
		ItemList myList = new ItemList();
		myList.add(new Item("basketball training", new DateTime(new Date(1411876800000L))));
		myList.displayList();
	}

}
