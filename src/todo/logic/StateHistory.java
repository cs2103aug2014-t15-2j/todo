package todo.logic;

import java.text.ParseException;
import java.util.Stack;

import todo.model.ItemList;

//@author A0098155W
/**
 * This class maintains previous and future states for user to undo and redo.
 */
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
		/* targetState should be already initialized in Logic before it is
			passed in. If it is null, there must be bug somewhere. */
		assert (targetState != null) : "Parameter should not be null";

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
		/* targetState should be already initialized in Logic before it is
			passed in. If it is null, there must be bug somewhere. */
		assert (targetState != null) : "Parameter should not be null";

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
	
	/**
	 * Pops all states from future stack, i.e. user is not able to redo after
	 * this method.
	 */
	public void popAllFromFuture() {
		while (!future.empty()) {
			future.pop();
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
}
