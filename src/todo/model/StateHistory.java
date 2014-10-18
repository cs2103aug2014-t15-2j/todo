package todo.model;

import java.util.Stack;

public class StateHistory {
	private Stack<ItemList> history;
	private Stack<ItemList> future;

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
			for (int i = 0; i < il.size(); i++) {
				newItemList.add(il.getItem(i));
			}

			history.push(newItemList);
		} catch (Exception e) {
			// Something is wrong, saveHistory has failed
			return false;
		}

		return true;
	}

	public boolean saveStateToFuture(ItemList il) {
		try {
			ItemList newItemList = new ItemList();
			for (int i = 0; i < il.size(); i++) {
				newItemList.add(il.getItem(i));
			}

			future.push(newItemList);
		} catch (Exception e) {
			// Something is wrong, saveHistory has failed
			return false;
		}
		return true;
	}

	public boolean canUndo() {
		if (!history.empty()) {
			return true;
		}

		return false;
	}

	public boolean canRedo() {
		if (!future.empty()) {
			return true;
		}

		return false;
	}

	public ItemList undo() {
		return history.pop();
	}

	public ItemList redo() {
		return future.pop();
	}
}
