package todo.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ItemList {
	    // Data attributes
		private ArrayList <Item> itemList = new ArrayList <Item> ();
		
		// Constructor
		
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
		public void delete(int index){
			try{
				String removedItemDescription = itemList.get(index - 1).getDescription();
				itemList.remove(index - 1);
				System.out.println("\"" + removedItemDescription + "\"" + " is deleted.");
				Item.setItemQtyAfterDeletion();
			}catch(IndexOutOfBoundsException e){
				if(itemList.size() == 0){
					System.out.println("Fail to delete, the list is empty.");
				}else if(index > itemList.size()){
					System.out.println("Fail to delete, invalid index used.");
				}
			}
				
		}
		
		// Display the whole itemList
		public void displayList(){
			int i = 1;
			for (Item item : itemList){
				System.out.println(i + ". " + item.toString());
				i++;
			}
			if(itemList.size() == 0){
				System.out.println("The list is empty");
			}
		}
		
		// Clear the whole itemList
		public void clear(){
			itemList.clear();
		}
		
		// Sort the itemList according to alphabetical order of description
		public void sortByFirstAlphabet(){
		Collections.sort(itemList, new Comparator<Item>(){
			public int compare(Item item1, Item item2){
				return item1.getDescription().compareToIgnoreCase(item2.getDescription());
			}
		});
		}
		
		// Sort the itemList from early to later times
		public void sortByTimeIncreasing(){
ArrayList<Item> listWithoutStartDateTime = new ArrayList<Item>();
			
			for(int i=0; i < itemList.size(); i++){
				if (itemList.get(i).getStartDateTime() == null){
					listWithoutStartDateTime.add(itemList.get(i));
					itemList.remove(i);
					i--;
				}
			}
			
			Collections.sort(itemList, new Comparator<Item>(){
				public int compare(Item item1, Item item2){
					return item1.getStartDateTime().getDate().compareTo(item2.getStartDateTime().getDate());
				}
			});
			for(Item item : listWithoutStartDateTime){
				itemList.add(item);
			}
		}
		
		//Sort the itemList from later to early times
		public void sortByTimeDecreasing(){
			ArrayList<Item> listWithoutStartDateTime = new ArrayList<Item>();
			
			for(int i=0; i < itemList.size(); i++){
				if (itemList.get(i).getStartDateTime() == null){
					listWithoutStartDateTime.add(itemList.get(i));
					itemList.remove(i);
					i--;
				}
			}
			
			Collections.sort(itemList, new Comparator<Item>(){
				public int compare(Item item2, Item item1){
					return item1.getStartDateTime().getDate().compareTo(item2.getStartDateTime().getDate());
				}
			});
			for(Item item : listWithoutStartDateTime){
				itemList.add(item);
			}
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
