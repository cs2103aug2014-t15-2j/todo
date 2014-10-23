package todo.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ItemList {
	    // Data attributes
		private ArrayList <Item> itemList = new ArrayList <Item> ();
		private ArrayList <Item> completedList = new ArrayList <Item> ();
		private ArrayList <Item> uncompletedList = new ArrayList <Item> ();
		
		// Constructor
		
		// Return the size of itemList
		public int size(){
			return itemList.size();
		}
		
		// Return a specific item from the itemList
		public Item getItem(int index){
				if(index < 0){
					IndexOutOfBoundsException exObj1 = 
							new IndexOutOfBoundsException("Invalid index for getItem():"
									+ "negative index.");
					throw exObj1;
				}
				
				if(index >= itemList.size()){
					IndexOutOfBoundsException exObj2 = 
							new IndexOutOfBoundsException("Invalid index for getItem(): "
									+ "exceeds list range.");
					throw exObj2;
				}
				
			return itemList.get(index);
		}
		
		// check if a index valid
		public boolean validIndex(int index){
			assert index>= 0;
			return index < (size()+2) && index > 0;
		}
		
		// Add item
		public String add(Item item){
			assert item.getDescription() != null;
			String itemDescription = item.getDescription();
			String result  = "\"" + itemDescription + "\"" + "is added.";
			itemList.add(item);
			uncompletedList.add(item);
			
			return result;
		}
		
		// Delete item
		public String delete(int index){
			try{
				String removedItemDescription = itemList.get(index - 1).getDescription();
				String result = "\"" + removedItemDescription + "\"" + " is deleted.";
				itemList.remove(index - 1);
				Item.setItemQtyAfterDeletion();
				
				return result;
			}catch(IndexOutOfBoundsException e){
				String returnErrorMessage = null;
				if(itemList.size() == 0){
					returnErrorMessage =  "Fail to delete, the list is empty. ";
				}else if(index > itemList.size()){
					returnErrorMessage =  "Fail to delete, invalid index used. ";
				}else if(index <= 0){
					returnErrorMessage = "Fail to delete, invalid index used. ";
				}
				
				return returnErrorMessage;
			}
				
		}
		
		// Done item
		public String done(int index){
			try{
				String doneItemDescription = itemList.get(index - 1).getDescription();
				String result = "\"" + doneItemDescription + "\"" + " is done.";
				itemList.get(index - 1).setStatusDone();;
				completedList.add(itemList.get(index -1));
				return result;
			}catch(IndexOutOfBoundsException e){
				String returnErrorMessage = null;
				if(itemList.size() == 0){
					returnErrorMessage =  "Fail to set done, the list is empty.";
				}else if(index > itemList.size()){
					returnErrorMessage =  "Fail to set done, invalid index used.";
				}
				
				return returnErrorMessage;
			}	
		}
		
		// Undone item
		public String undone(int index){
			try{
				String undoneItemDescription = itemList.get(index - 1).getDescription();
				String result = "\"" + undoneItemDescription + "\"" + " is undone.";
				itemList.get(index - 1).setStatusUndone();;
				return result;
			}catch(IndexOutOfBoundsException e){
				String returnErrorMessage = null;
				if(itemList.size() == 0){
					returnErrorMessage =  "Fail to set undone, the list is empty.";
				}else if(index > itemList.size()){
					returnErrorMessage =  "Fail to set undone, invalid index used.";
				}
				
				return returnErrorMessage;
			}	
		}
		
		// Display the whole itemList
		public String displayList(){
			int i = 1;
			String result = "";
			for (Item item : itemList){
				String appendString = i + ". " + item.toString() + "\n";
				result += appendString;
				i++;
			}
			if(itemList.size() == 0){
				result = "The list is empty.";
			}
			
			return result;
		}
		
		// Display the whole itemList
		public String toString(){
			String result = "";
			if (this.size() == 0){
				return "Empty";
			}else{
				for (int i = 0; i < this.size(); i++){
					result += ((i+1) + ". " + this.getItem(i).toString()+"\n");
				}
			}
			return result;
		}
		
		// Clear the whole itemList
		public String clear(){
			itemList.clear();
			String result = "All tasks are cleared.";
			
			return result;
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
		public String search(String searchKey){
			String result = "";
			for(Item i : itemList){
				if (i.getDescription().contains(searchKey)){
					String appendString = i.getDescription() + " " + i.getStartDateTime();
					result += appendString;
				}
			}
			
			return result;
		}		
		
		public String filterByTags(String tagString){
			String[] splitedTags = tagString.split("\\W+");
			String filteredList = "";
			int matchNumber = splitedTags.length;
			int currentMatchNumber;
			
			for(Item i : itemList){
				currentMatchNumber = 0;
				String tagCompared = "";
				for(int j = 0; j < i.getTags().size(); j++){
					for(int k = 0; k <splitedTags.length; k++){
						tagCompared = splitedTags[k];
						if(i.getTags().get(j).equals(tagCompared)){
							currentMatchNumber++;
							break;
						}
					}
				}
				if(currentMatchNumber == matchNumber){
					String appendString = i.toString();
					filteredList += appendString;
					filteredList += "\n";
				}
			}
			return filteredList;			
		}
		
		public String filterByDateTime(Date dateTimeFiltered){

			String filteredList = "";
			for(int i = 0; i < itemList.size(); i++ ){
				DateTime dateTime = itemList.get(i).getStartDateTime();
				if(dateTime != null){
					Date itemDate = dateTime.getDate();
					if(itemDate.equals(dateTimeFiltered)){
						String appendString = itemList.get(i).toString();
					    filteredList += appendString;
					    filteredList += "\n";
				    }
				}
			}
			return filteredList;
		}
		
		public ArrayList<Item> showCompletedList() {
			return completedList;
		}
		
		public ArrayList<Item> showUncompletedList() {
			return uncompletedList;
		}
		
		public String showCompletedListString(){
			String result = "";
			if (completedList.size() == 0){
				return "Empty";
			}else{
				for (int i = 0; i < completedList.size(); i++){
					result += ((i+1) + ". " + completedList.get(i).toString()+"\n");
				}
			}
			return result;
		}
		
		public String showUncompletedListString(){
			String result = "";
			if (uncompletedList.size() == 0){
				return "Empty";
			}else{
				for (int i = 0; i < uncompletedList.size(); i++){
					result += ((i+1) + ". " + uncompletedList.get(i).toString()+"\n");
				}
			}
			return result;
		}
		
		
		//To run at the initial launch of JustDidIt
		public void checkStatus() {
			for (int i =0; i < itemList.size(); i++ ) {
				if(itemList.get(i).getStatus() == true) {
					completedList.add(itemList.get(i));
				}
				
				if(itemList.get(i).getStatus() == false) {
					uncompletedList.add(itemList.get(i));
				}
				
			}
		}
			
}		

