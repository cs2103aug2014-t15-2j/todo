package todo.model;

import java.text.ParseException;
import java.util.Stack;

public class StateHistory {

	private Stack<ItemList> history;
	private Stack<ItemList> future;

	public StateHistory() {
		history = new Stack<ItemList>();
		future = new Stack<ItemList>();
	}

	/**
	 * Save a ItemList's state onto history stack.
	 * 
	 * @param targetState
	 * @throws ParseException
	 */
	public void saveStateToHistory(ItemList targetState) throws ParseException {
		ItemList clonedState = new ItemList();
		copyItemList(targetState, clonedState);

		history.push(clonedState);
	}

	/**
	 * Save a ItemList's state onto future stack.
	 * 
	 * @param targetState
	 * @throws ParseException
	 */
	public void saveStateToFuture(ItemList targetState) throws ParseException {
		ItemList clonedState = new ItemList();
		copyItemList(targetState, clonedState);

		future.push(clonedState);
	}

	/**
	 * Clones the entire targetState into a clonedState
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

	/**
	 * Pops all states from future stack, i.e. user is not able to redo after
	 * this method.
	 */
	public void popAllFromFuture() {
		while (!future.empty()) {
			future.pop();
		}
	}
}
