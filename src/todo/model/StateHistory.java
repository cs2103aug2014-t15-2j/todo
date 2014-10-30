package todo.model;

import java.text.ParseException;
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

	/**
	 * Save a ItemList's state onto history stack
	 * 
	 * @param targetState
	 * @return 'true' if the state was successfully pushed onto the history
	 *         state, otherwise 'false'
	 */
	public boolean saveStateToHistory(ItemList targetState) {
		try {
			ItemList clonedState = new ItemList();
			copyItemList(targetState, clonedState);

			history.push(clonedState);
		} catch (Exception e) {
			// Something is wrong, save history has failed
			return false;
		}

		return true;
	}

	/**
	 * Save a ItemList's state onto future stack
	 * 
	 * @param targetState
	 * @return 'true' if the state was successfully pushed onto the future
	 *         stack, otherwise 'false'
	 */
	public boolean saveStateToFuture(ItemList targetState) {
		try {
			ItemList clonedState = new ItemList();
			copyItemList(targetState, clonedState);

			future.push(clonedState);
		} catch (Exception e) {
			// Something is wrong, save future has failed
			return false;
		}

		return true;
	}

	/**
	 * This method clones the entire targetState into a clonedState
	 * 
	 * @param targetState
	 * @param clonedState
	 * @throws ParseException
	 */
	private void copyItemList(ItemList targetState, ItemList clonedState)
			throws ParseException {
		for (int i = 0; i < targetState.size(); i++) {
			clonedState.add(targetState.getItem(i).cloneItem());
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

	public void popAllFromFuture() {
		while (!future.empty()) {
			future.pop();
		}
	}
}
