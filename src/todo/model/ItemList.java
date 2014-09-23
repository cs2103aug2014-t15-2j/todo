package todo.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ItemList {
	// Data attributes
		private ArrayList <Item> itemList = new ArrayList <Item> ();
		
		// Return the size of itemList
		public int size(){
			return itemList.size();
		}
		
		// Return a specific item from the itemList
		public Item getItem(int index){
			return itemList.get(index);
		}
		
		// Add item
		public void add(Item item){
			itemList.add(item);
		}
		
		// Delete item
		public void delete(int item){
			itemList.remove(item);
		}
		
		// Display the whole itemList
		public void displayList(){
			for (Item i : itemList){
				System.out.println(i.getDescription() + " " + i.getStartDateTime());
			}
		}
		
		// Clear the whole itemList
		public void clear(){
			itemList.clear();
		}
		
		// Sort the itemList according to alphabetical order of description
		public void sort(){
		Collections.sort(itemList, new Comparator<Item>(){
			public int compare(Item item1, Item item2){
				return item1.getDescription().compareToIgnoreCase(item2.getDescription());
			}
		});
		}
		
		// Search certain key word in itemList
		public void search(String searchKey){

			for(Item i : itemList){
				if (i.getDescription().contains(searchKey)){
					System.out.println(i.getDescription() + " " + i.getStartDateTime());
				}
			}
		}
		
}
