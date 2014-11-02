package todo.model;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.time.*;


public class ItemList {
		//Error Messages
		private static final String ERROR_INDEX_NEGATIVE = "Invalid index used - Negative Index";
		private static final String ERROR_INDEX_EXCEEDED = "Invalid index used - Index out of range";
		private static final String ERROR_LIST_EMPTY = "The list is empty. ";
		//System Messages
		private static final String MESSAGE_ADDED ="%1$s"+" "+"is added.";
		private static final String MESSAGE_DELETED = "\"" + "%1$s" + "\"" + " is deleted.";
		private static final String MESSAGE_COMPLETED = "\"" + "%1$s" + "\"" + " is marked as completed.";
		private static final String MESSAGE_UNCOMPLETED = "\"" + "%1$s" + "\"" + " is marked as uncompleted.";
		private static final String MESSAGE_CLEARED ="All tasks are cleared.";
	
		//Attributes
		private ArrayList <Item> itemList = new ArrayList <Item> ();
		private ArrayList <Item> completedList = new ArrayList <Item> ();
		private ArrayList <Item> uncompletedList = new ArrayList <Item> ();
		
		// Return the size of itemList
		public int size(){
			return itemList.size();
		}
		
		// Return a specific item from the itemList, the min. index is 0
		public Item getItem(int index){
				if(index < 0){
					IndexOutOfBoundsException exObj1 = 
							new IndexOutOfBoundsException(ERROR_INDEX_NEGATIVE);
					throw exObj1;
				}
				
				if(index >= itemList.size()){
					IndexOutOfBoundsException exObj2 = 
							new IndexOutOfBoundsException(ERROR_INDEX_EXCEEDED);
					throw exObj2;
				}
				
			return itemList.get(index);
		}
		
		// check if a index valid
		public boolean validIndex(int index){
			assert index>= 0;
			return index < (size()+2) && index >= 0;
		}
		
		// Add item
		public String add(Item item){
			assert item.getDescription() != null;
			String itemDescription = item.getDescription();
			String result  = String.format(MESSAGE_ADDED, itemDescription);
					
			itemList.add(item);
			uncompletedList.add(item);
			
			return result;
		}
		
		// Delete item, the min.index starts from 1
		public String delete(int index){
			try{
				String removedItemDescription = itemList.get(index - 1).getDescription();
				String result = String.format(MESSAGE_DELETED, removedItemDescription);
				itemList.remove(index - 1);
				Item.setItemQtyAfterDeletion();
				
				return result;
			}catch(IndexOutOfBoundsException e){
				String returnErrorMessage = null;
				if(itemList.size() == 0){
					returnErrorMessage =  ERROR_LIST_EMPTY;
				}else if(index > itemList.size()){
					returnErrorMessage = ERROR_INDEX_EXCEEDED;
				}else if(index <= 0){
					returnErrorMessage = ERROR_INDEX_NEGATIVE;
				}
				
				return returnErrorMessage;
			}
				
		}
		
		// Done item
		public String done(int index){
			try{
				String doneItemDescription = itemList.get(index - 1).getDescription();
				String result = String.format(MESSAGE_COMPLETED, doneItemDescription);
				itemList.get(index - 1).setStatusDone();;
				return result;
			}catch(IndexOutOfBoundsException e){
				String returnErrorMessage = null;
				if(itemList.size() == 0){
					returnErrorMessage =  ERROR_LIST_EMPTY;
				}else if(index > itemList.size()){
					returnErrorMessage =  ERROR_INDEX_EXCEEDED;
				}
				
				return returnErrorMessage;
			}	
		}
		
		// Undone item
		public String undone(int index){
			try{
				String undoneItemDescription = itemList.get(index - 1).getDescription();
				itemList.get(index - 1).setStatusUndone();;
				String result = String.format(MESSAGE_UNCOMPLETED, undoneItemDescription);
				return result;
			}catch(IndexOutOfBoundsException e){
				String returnErrorMessage = null;
				if(itemList.size() == 0){
					returnErrorMessage =  ERROR_LIST_EMPTY;
				}else if(index > itemList.size()){
					returnErrorMessage =  ERROR_INDEX_EXCEEDED;
				}
				
				return returnErrorMessage;
			}	
		}
		
		// Display the whole itemList
		public String toString(){
			String result = "";
			if (this.size() == 0){
				return ERROR_LIST_EMPTY;
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
			completedList.clear();
			uncompletedList.clear();
			String result = MESSAGE_CLEARED;
			
			return result;
		}
		//Sort the itemList by itemId in ascending order 
		public void sortByItemId() {
			Collections.sort(itemList, new Comparator<Item>(){
				public int compare(Item item1, Item item2){
					return item1.getItemId()- (item2.getItemId());
				}
			});	
			
		}
		
		// Sort the itemList according to alphabetical order of description
		public void sortByFirstAlphabet(){
			Collections.sort(itemList);

		}
		
		// Sort the itemList from early to later by comparing start time with earliest first with items without startdatetime at the back
		public void sortByTimeIncreasing(){
			ArrayList<Item> listWithoutStartDateTime = new ArrayList<Item>();
			//Extract items without start-time and stores them in a list temporarily
			for(int i=0; i < itemList.size(); i++){
				if (itemList.get(i).getStartDateTime() == null){
					listWithoutStartDateTime.add(itemList.get(i));
					itemList.remove(i);
					i--;
				}
			}
			//Look through remaining list and sort by start-time, from earliest to latest
			Collections.sort(itemList, new Comparator<Item>(){
				public int compare(Item item1, Item item2){
					return item1.getStartDateTime().getDate().compareTo(item2.getStartDateTime().getDate());
				}
			});
			for(Item item : listWithoutStartDateTime){
				itemList.add(item);
			}
		}
		
		//Sort the itemList from latest first with items without startdatetime at the back
		public void sortByTimeDecreasing(){
			ArrayList<Item> listWithoutStartDateTime = new ArrayList<Item>();
			//Extract items without start-time and stores them in a list temporarily
			for(int i=0; i < itemList.size(); i++){
				if (itemList.get(i).getStartDateTime() == null){
					listWithoutStartDateTime.add(itemList.get(i));
					itemList.remove(i);
					i--;
				}
			}
			//Look through remaining list and sort by start-time, from earliest to latest
			Collections.sort(itemList, new Comparator<Item>(){
				public int compare(Item item1, Item item2){
					return item2.getStartDateTime().getDate().compareTo(item1.getStartDateTime().getDate());
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
		
		// Assumption: the dateTime refers to startDateTime
		
		public String filterByDateTime(String dateTimeString){
			
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			Date dateTimeFiltered = null;
			try{
				dateTimeFiltered= df.parse(dateTimeString);
			} catch(ParseException e){
				e.printStackTrace();
			}

			String filteredList = "";
			for(int i = 0; i < itemList.size(); i++ ){
				DateTime dateTime = itemList.get(i).getStartDateTime();
				if(dateTime != null){
					LocalDateTime itemDate = dateTime.getDate();
					if(itemDate.equals(dateTimeFiltered)){
						String appendString = itemList.get(i).toString();
					    filteredList += appendString;
					    filteredList += "\n";
					    filteredList += "\n";
				    }
				}
			}
			return filteredList;
		}
		
		public ArrayList<Item> showCompletedList() {
			checkStatus();
			return completedList;
		}
		
		public ArrayList<Item> showUncompletedList() {
			checkStatus();
			return uncompletedList;
		}
		
		public String showCompletedListString(){
			String result = "";
			checkStatus();
			if (completedList.size() == 0){
				return ERROR_LIST_EMPTY;
			}else{
				for (int i = 0; i < completedList.size(); i++){
					result += ((i+1) + ". " + completedList.get(i).toString()+"\n");
				}
			}
			return result;
		}
		
		public String showUncompletedListString(){
			String result = "";
			checkStatus();
			if (uncompletedList.size() == 0){
				return ERROR_LIST_EMPTY;
			}else{
				for (int i = 0; i < uncompletedList.size(); i++){
					result += ((i+1) + ". " + uncompletedList.get(i).toString()+"\n");
				}
			}
			return result;
		}
		  
		   
		
		public static int searchIndex (ItemList searchList , int key){
			int start = 0 ;
			int end = searchList.size();
			while(start<=end) {
				int mid =(start+end)/2;
				System.out.println("enter search");
				System.out.println("Current to compare : "+searchList.getItem(mid).getItemId());
				if(key== searchList.getItem(mid).getItemId()){
					System.out.println("enter search2");
				return mid;
				
				}if(key<searchList.getItem(mid).getItemId()){
					end = mid -1;
					System.out.println("enter search3");
				}else {
					start = mid +1;
					System.out.println("enter search4");
				}
				}
			
				return -1; 
		}
		
		//Check for item status and add it to the completed/uncompleted list
		private void checkStatus() {
			completedList.clear();
			uncompletedList.clear();
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

