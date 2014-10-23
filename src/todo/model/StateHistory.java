package todo.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

public class StateHistory {

	// Related to date and time
	public static final String DATE_WITH_TIME = "MM/dd/yyyy HH:mm:ss";
	public static final String DATE_WITHOUT_TIME = "MM/dd/yyyy";

	private Stack<ItemList> history;
	private Stack<ItemList> future;
	private DateFormat dateWithTime = new SimpleDateFormat(
			DATE_WITH_TIME);
	private DateFormat dateWithoutTime = new SimpleDateFormat(
			DATE_WITHOUT_TIME);

	/**
	 * Default constructor of StateHistory
	 */
	public StateHistory() {
		history = new Stack<ItemList>();
		future = new Stack<ItemList>();
	}

	public boolean saveStateToHistory(ItemList il) {
		try {
			ItemList newItemList = new ItemList();
			copyItemList(il, newItemList);

			history.push(newItemList);
		} catch (Exception e) {
			e.printStackTrace();
			// Something is wrong, save history has failed
			return false;
		}

		return true;
	}
	
	public boolean saveStateToFuture(ItemList il) {
		try {
			ItemList newItemList = new ItemList();
			copyItemList(il, newItemList);

			future.push(newItemList);
		} catch (Exception e) {
			// Something is wrong, save future has failed
			return false;
		}
		return true;
	}

	/**
	 * This method copies the entire il into a newItemList that is going 
	 * to be push into either history or future stack
	 * @param il
	 * @param newItemList
	 * @throws ParseException
	 */
	private void copyItemList(ItemList il, ItemList newItemList)
			throws ParseException {
		for (int i = 0; i < il.size(); i++) {
			Item c = il.getItem(i);// for convenience

			// priority
			int priority = 1;// hard-coded 1 currently

			// description
			String description = new String(c.getDescription());

			if(c.getLocation().equals("")){
				System.out.println("c.getLocation() is empty" );
			}
			// location
			String location = new String(c.getLocation());
				
			if(location.equals("")){
				System.out.println("location is emtpy");
			}
			
			// tagList
			ArrayList<String> tags = new ArrayList<String>();
			for (int j = 0; j < c.getTags().size(); j++) {
				String tag = new String(c.getTags().get(j));
				tags.add(tag);
			}

			// startDateTime
			DateTime startDateTime = null;
			Date startDate = null;
			if (c.getStartDateTime() != null) {
				if (c.getStartDateTime().hasTime()) {
					startDate = dateWithTime.parse(c.getStartDateTime()
							.toString());
					startDateTime = new DateTime(startDate, true);
				} else {
					startDate = dateWithoutTime.parse(c.getStartDateTime()
							.toString());
					startDateTime = new DateTime(startDate, false);
				}
			}
			// dueDateTime
			DateTime dueDateTime = null;
			Date dueDate = null;
			if (c.getDueDateTime() != null) {
				if (c.getDueDateTime().hasTime()) {
					dueDate = dateWithTime.parse(c.getDueDateTime()
							.toString());
					dueDateTime = new DateTime(dueDate, true);
				} else {
					dueDate = dateWithoutTime.parse(c.getDueDateTime()
							.toString());
					dueDateTime = new DateTime(dueDate, false);
				}
			}

			Item newItem = new Item(description, startDateTime,
					dueDateTime, location, priority, tags);

			// status
			if (c.getStatus()) {
				newItem.setStatusDone();
			} else {
				newItem.setStatusUndone();
			}

			// because setItemID always + 1
			newItem.setItemID(c.getItemId() - 1);

			newItemList.add(newItem);
		}
	}

	public boolean canUndo() {
		return history.empty() ? false : true;
	}

	public boolean canRedo() {
		return future.empty() ? false : true;
	}

	public ItemList undo() {
		return history.pop();
	}

	public ItemList redo() {
		return future.pop();
	}

	public boolean popAllFromFuture() {
		try {
			while (!future.empty()) {
				future.pop();
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
