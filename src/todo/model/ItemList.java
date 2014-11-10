package todo.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;


import todo.storage.Storage;

//@author A0115992X


public class ItemList {
	// Error Messages
	public static final String ERROR_INDEX_NEGATIVE = "Invalid index used - Negative Index";
	public static final String ERROR_INDEX_EXCEEDED = "Invalid index used - Index out of range";
	public static final String ERROR_LIST_EMPTY = "The list is empty. ";
	public static final String ERROR_GENERAL = "invalid command";
	public static final String ERROR_NOT_FOUND = "Item not found in target list";
	public static final String ERROR_ALREADY_COMPLETED = "Task is already marked as completed!";
	public static final String ERROR_ALREADY_UNCOMPLETED = "Task is already marked as uncompleted!";

	// System Messages
	private static final String MESSAGE_ADDED = "%1$s" + " " + "is added.";
	private static final String MESSAGE_DELETED = "\"" + "%1$s" + "\""
			+ " is deleted.";
	private static final String MESSAGE_COMPLETED = "\"" + "%1$s" + "\""
			+ " is marked as completed.";
	private static final String MESSAGE_UNCOMPLETED = "\"" + "%1$s" + "\""
			+ " is marked as uncompleted.";
	private static final String MESSAGE_CLEARED = "All tasks are cleared.";

	public static final String DELETE_SUCCESSFUL = "Delete operation successful";
	public static final String DONE_SUCCESSFUL = "Done operation successful";
	public static final String UNDONE_SUCCESSFUL = "Undone operation successful";

	private static final String EMPTYSTRING = "";

	// Attributes
	private ArrayList<Item> itemList = new ArrayList<Item>();
	private ArrayList<Item> completedList = new ArrayList<Item>();
	private ArrayList<Item> uncompletedList = new ArrayList<Item>();
	private DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern(Storage.DATE_FORMAT);

	// Return the size of itemList
	public int size() {
		return itemList.size();
	}

	// Return a specific item from the itemList, the min. index is 0
	public Item getItem(int index) {
		if (index < 0) {
			IndexOutOfBoundsException exObj1 = new IndexOutOfBoundsException(
					ERROR_INDEX_NEGATIVE);
			throw exObj1;
		}

		if (index >= itemList.size()) {
			IndexOutOfBoundsException exObj2 = new IndexOutOfBoundsException(
					ERROR_INDEX_EXCEEDED);
			throw exObj2;
		}

		return itemList.get(index);
	}

	// Return the list of all items
	public ArrayList<Item> getAllItems() {
		return this.itemList;
	}

	// Return the list of completed items
	public ArrayList<Item> getCompletedItems() {
		return this.completedList;
	}

	// Return the list of uncompleted items
	public ArrayList<Item> getUnCompletedItems() {
		return this.uncompletedList;
	}

	// check if a index valid
	public boolean validIndex(int index) {
		assert index >= 0;
		return index < (size() + 2) && index >= 0;
	}

	// Add item
	public String add(Item item) {
		assert item.getDescription() != null;
		String itemDescription = item.getDescription();
		String result = String.format(MESSAGE_ADDED, itemDescription);

		itemList.add(item);
		uncompletedList.add(item);

		return result;
	}

	// Delete item, the min.index starts from 1
	public String delete(int index) {
		try {
			String removedItemDescription = itemList.get(index - 1)
					.getDescription();
			String result = String.format(MESSAGE_DELETED,
					removedItemDescription);
			itemList.remove(index - 1);
			Item.setItemQtyAfterDeletion();

			return DELETE_SUCCESSFUL;
		} catch (IndexOutOfBoundsException e) {
			String returnErrorMessage = ERROR_GENERAL;
			if (itemList.size() == 0) {
				returnErrorMessage = ERROR_LIST_EMPTY;
			} else if (index > itemList.size()) {
				returnErrorMessage = ERROR_INDEX_EXCEEDED;
			} else if (index <= 0) {
				returnErrorMessage = ERROR_INDEX_NEGATIVE;
			}

			return returnErrorMessage;
		}

	}
//@author A0111082Y
	// Done item
	public String done(int index) {
		String result = "";

		try {
			if (!itemList.get(index - 1).getStatus()) {
				String doneItemDescription = itemList.get(index - 1)
						.getDescription();
				result = String.format(MESSAGE_COMPLETED, doneItemDescription);
				itemList.get(index - 1).setStatusDone();
				;
				// Add item to list of completed item:
				completedList.add(itemList.get(index - 1));
				// Find itemId of the item that was mark as done
				int target = itemList.get(index - 1).getItemId();
				// Search for the index of the item in the uncompleted list
				int deleteIndex = searchIndex(uncompletedList, target);
				// Remove the item from the uncompleted list
				if (deleteIndex == -1) {
					String errorMessage = ERROR_NOT_FOUND;
					return errorMessage;
				} else {
					uncompletedList.remove(deleteIndex);
				}
			} else {
				return ERROR_ALREADY_COMPLETED;
			}

			return DONE_SUCCESSFUL;
		} catch (IndexOutOfBoundsException e) {
			String returnErrorMessage = ERROR_GENERAL;
			if (itemList.size() == 0) {
				returnErrorMessage = ERROR_LIST_EMPTY;
			} else if (index > itemList.size()) {
				returnErrorMessage = ERROR_INDEX_EXCEEDED;
			}

			return returnErrorMessage;
		}
	}
//@author A0111082Y
	// Undone item
	public String undone(int index) {
		String result = "";
		try {
			if (itemList.get(index - 1).getStatus()) {
				String undoneItemDescription = itemList.get(index - 1)
						.getDescription();
				itemList.get(index - 1).setStatusUndone();
				;
				result = String.format(MESSAGE_UNCOMPLETED,
						undoneItemDescription);
				// Add item to list of uncompleted item:
				uncompletedList.add(itemList.get(index - 1));
				// Find itemId of the item that was mark as done
				int target = itemList.get(index - 1).getItemId();
				// Search for the index of the item in the completed list
				int deleteIndex = searchIndex(completedList, target);
				// Remove the item from the completed list
				if (deleteIndex == -1) {
					String errorMessage = ERROR_NOT_FOUND;
					return errorMessage;
				} else {
					completedList.remove(deleteIndex);
				}
			} else {
				return ERROR_ALREADY_UNCOMPLETED;
			}

			return UNDONE_SUCCESSFUL;
		} catch (IndexOutOfBoundsException e) {
			String returnErrorMessage = ERROR_GENERAL;
			if (itemList.size() == 0) {
				returnErrorMessage = ERROR_LIST_EMPTY;
			} else if (index > itemList.size()) {
				returnErrorMessage = ERROR_INDEX_EXCEEDED;
			}

			return returnErrorMessage;
		}
	}
	//@author A0111082Y
	// Display the whole itemList in string format
	public String toString() {
		String result = "";
		if (this.size() == 0) {
			return ERROR_LIST_EMPTY;
		} else {
			for (int i = 0; i < this.size(); i++) {
				result += ((i + 1) + ". " + this.getItem(i).toString() + "\n");
			}
		}
		return result;
	}
	//@author A0111082Y
	// Clear the whole itemList
	public String clear() {
		itemList.clear();
		completedList.clear();
		uncompletedList.clear();
		String result = MESSAGE_CLEARED;

		return result;
	}

	//@author A0115992X
	// Sort the itemList by itemId in ascending order
	public void sortByItemId() {
		Collections.sort(itemList, new Comparator<Item>() {
			public int compare(Item item1, Item item2) {
				return item1.getItemId() - (item2.getItemId());
			}
		});

	}

	//@author A0115992X
	// Sort the itemList according to alphabetical order of description
	public void sortByFirstAlphabet() {
		Collections.sort(itemList, new Comparator<Item>() {
			public int compare(Item item1, Item item2) {
				return item1.getDescription().compareToIgnoreCase(
						item2.getDescription());

			}
		});
	}

	//@author A0115992X
	// Sort the itemList from early to later by comparing start time
	public void sortByTimeIncreasing() {
		ArrayList<Item> listWithoutStartDateTime = new ArrayList<Item>();
		// Extract items without start-time and stores them in a list
		// temporarily
		for (int i = 0; i < itemList.size(); i++) {
			if (itemList.get(i).getStartDateTime() == null) {
				listWithoutStartDateTime.add(itemList.get(i));
				itemList.remove(i);
				i--;
			}
		}
		// Look through remaining list and sort by start-time, from earliest to
		// latest
		Collections.sort(itemList, new Comparator<Item>() {
			public int compare(Item item1, Item item2) {
				return item1.getStartDateTime().getDate()
						.compareTo(item2.getStartDateTime().getDate());
			}
		});
		for (Item item : listWithoutStartDateTime) {
			itemList.add(item);
		}
	}

	//@author A0115992X
	// Sort the itemList from latest first with items without startdatetime at
	// the back
	public void sortByTimeDecreasing() {
		ArrayList<Item> listWithoutStartDateTime = new ArrayList<Item>();
		// Extract items without start-time and stores them in a list
		// temporarily
		for (int i = 0; i < itemList.size(); i++) {
			if (itemList.get(i).getStartDateTime() == null) {
				listWithoutStartDateTime.add(itemList.get(i));
				itemList.remove(i);
				i--;
			}
		}
		// Look through remaining list and sort by start-time, from earliest to
		// latest
		Collections.sort(itemList, new Comparator<Item>() {
			public int compare(Item item1, Item item2) {
				return item2.getStartDateTime().getDate()
						.compareTo(item1.getStartDateTime().getDate());
			}
		});
		for (Item item : listWithoutStartDateTime) {
			itemList.add(item);
		}

	}
	//@author A0111082Y
	// Search certain key word in itemList
	public String search(String searchKey) {
		searchKey = searchKey.toLowerCase();
		String result = EMPTYSTRING;
		for (Item i : itemList) {
			if (i.getDescription().contains(searchKey)) {
				String appendString = i.getDescription() + " "
						+ i.getStartDateTime();
				result += appendString;
			}
		}

		return result;
	}
	//@author A0115992X
	public ArrayList<Item> filterByTags(String tagString) {
		String[] splitedTags = tagString.split("\\W+");
		ArrayList<Item> itemWithTargetTags = new ArrayList<Item>();
		int matchNumber = splitedTags.length;
		int currentMatchNumber;

		for (Item i : itemList) {
			currentMatchNumber = 0;
			String tagCompared = "";
			for (int j = 0; j < i.getTags().size(); j++) {
				for (int k = 0; k < splitedTags.length; k++) {
					tagCompared = splitedTags[k];
					if (i.getTags().get(j).equals(tagCompared)) {
						currentMatchNumber++;
						break;
					}
				}
			}
			if (currentMatchNumber == matchNumber) {
				itemWithTargetTags.add(i);
			}
		}
		return itemWithTargetTags;
	}
	
//@author A0111082Y
	public ArrayList<Item> filterByLocation(String locationString) {
		String[] splitedTags = locationString.split("\\W+");
		
		ArrayList<Item> itemWithTargetLocation = new ArrayList<Item>();
		int matchNumber = splitedTags.length;
		int currentMatchNumber;

		for (Item i : itemList) {
			currentMatchNumber = 0;
			String locationCompared = "";
			for (int j = 0; j < i.getTags().size(); j++) {
				for (int k = 0; k < splitedTags.length; k++) {
					locationCompared = splitedTags[k];
					if (i.getLocation().equals(locationCompared)) {
						currentMatchNumber++;
						break;
					}
				}
			}
			if (currentMatchNumber == matchNumber) {
				itemWithTargetLocation.add(i);
			}
		}
		return itemWithTargetLocation;
	}
	
//@author A0111082Y
	
	/* Returns items with either start day or end date with the input date
	 * Input string must follow the format  "yyyy-MM-dd HH:mm"
	 */
	public ArrayList<Item> filterByDateTime(String dateTimeString) {
		ArrayList<Item> itemWithTargetDate = new ArrayList<Item>();
		LocalDateTime searchDate = LocalDateTime.parse(dateTimeString,
				formatter);
		for (int i = 0; i < itemList.size(); i++) {
			DateTime sDateTime = itemList.get(i).getStartDateTime();
			DateTime dDateTime = itemList.get(i).getDueDateTime();
			/*Compares the target date to both start date and due date attributes. adds it to the
			* list if it is equals to the same date.	
			*/
			
			//Case 1 - items with start date but no due date
			if ((sDateTime != null) && (dDateTime == null )) {
				LocalDateTime sItemDate = sDateTime.getDate();
				if (sItemDate.toLocalDate().isEqual(searchDate.toLocalDate())){
					itemWithTargetDate.add(itemList.get(i));
				}
			}
			//Case 2 - items with due date but no start date
			if ((sDateTime == null) &&(dDateTime != null )) {
				LocalDateTime dItemDate = dDateTime.getDate();
				if (dItemDate.toLocalDate().isEqual(searchDate.toLocalDate())){
					itemWithTargetDate.add(itemList.get(i));
				}
			}
			//Case 3 - items with both due date and start date
			if ((sDateTime != null) &&(dDateTime != null )) {
				LocalDateTime dItemDate = dDateTime.getDate();
				LocalDateTime sItemDate = sDateTime.getDate();
				
				if ((dItemDate.toLocalDate().isEqual(searchDate.toLocalDate()))||(sItemDate.toLocalDate().isEqual(searchDate.toLocalDate()))){
					itemWithTargetDate.add(itemList.get(i));
				}
			}
			
		}

		return itemWithTargetDate;

	}
//@author A0111082Y
	public ArrayList<Item> showCompletedList() {

		return completedList;
	}
	//@author A0111082Y
	public ArrayList<Item> showUncompletedList() {

		return uncompletedList;
	}

	public String showCompletedListString() {
		String result = "";

		if (completedList.size() == 0) {
			return ERROR_LIST_EMPTY;
		} else {
			for (int i = 0; i < completedList.size(); i++) {
				result += ((i + 1) + ". " + completedList.get(i).toString() + "\n");
			}
		}
		return result;
	}

	public String showUncompletedListString() {
		String result = "";

		if (uncompletedList.size() == 0) {
			return ERROR_LIST_EMPTY;
		} else {
			for (int i = 0; i < uncompletedList.size(); i++) {
				result += ((i + 1) + ". " + uncompletedList.get(i).toString() + "\n");
			}
		}
		return result;
	}

	// With the itemId, searches the arrayList and returns the index where the
	// item is stored
	public static int searchIndex(ArrayList<Item> searchList, int key) {
		int notfound = -1;
		int start = 0;
		int end = searchList.size();
		while (start <= end) {
			int mid = (start + end) / 2;

			if (key == searchList.get(mid).getItemId()) {

				return mid;

			}
			if (key < searchList.get(mid).getItemId()) {
				end = mid - 1;

			} else {
				start = mid + 1;
			}
		}

		return notfound;
	}

	// With the itemId, searches the ItemList and returns the index where the
	// item is stored
	public static int searchIndex(ItemList searchList, int key) {
		int notfound = -1;
		int start = 0;
		int end = searchList.size();
		while (start <= end) {
			int mid = (start + end) / 2;

			if (key == searchList.getItem(mid).getItemId()) {

				return mid;

			}
			if (key < searchList.getItem(mid).getItemId()) {
				end = mid - 1;

			} else {
				start = mid + 1;
			}
		}

		return notfound;
	}

	// Check for item status and add it to the completed/uncompleted list
	public void checkStatus() {
		completedList.clear();
		uncompletedList.clear();
		for (int i = 0; i < itemList.size(); i++) {

			if (itemList.get(i).getStatus() == true) {
				completedList.add(itemList.get(i));
			}

			if (itemList.get(i).getStatus() == false) {
				uncompletedList.add(itemList.get(i));
			}

		}
	}

}
//@author A0111082Y