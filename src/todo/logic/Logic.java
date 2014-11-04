package todo.logic;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.util.CommandType;
import todo.util.LogUtil;
import todo.command.AddCommand;
import todo.model.*;
import todo.nlp.NLP;
import todo.storage.Storage;
import todo.util.StringUtil;

public class Logic {

	private static String TAG = "Logic";
	private static Logic logicSingleton;
	private Storage storage;
	private CommandMatch command;
	private static ItemList mItemList;
	private StateHistory stateHistory;
	private boolean fastUpdate;
	private String finalMessage = "Status: ";
	private ArrayList<Item> itemsForGUI = new ArrayList<Item>();

	private static final String ERROR_UNRECOGNISED_COMMAND = "Command not recognised.";
	private static final String ERROR_MISSING_TAGS = "Invalid: Missing Tag Names.";

	private static final String MESSAGE_ADD_TIP = "Add command : add a new event or task.\n";
	private static final String MESSAGE_ADD_EXAMPLE = "eg add project meeting tomorrow @utown #cs2103 \n";
	private static final String MESSAGE_UNDO_SUCCESS = "you have successfully undo the previous action.";
	private static final String MESSAGE_CANNOT_UNDO = "no action can be undo.";
	private static final String MESSAGE_REDO_SUCCESS = "you have successfully redo the previous action.";
	private static final String MESSAGE_CANNOT_REDO = "no action can be redo.";
	private static final String MESSAGE_SHOW_UNCOMPLETED = "Showing all uncompleted tasks";
	private static final String MESSAGE_SHOW_COMPLETED = "Showing all completed tasks";
	private static final String MESSAGE_SHOW_FILTERED = "Showing task(s) labeled with" + " " + "%1$s";

	/**
	 * Private constructor for singleton Logic
	 * 
	 * @throws ParseException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws DOMException
	 */
	private Logic() throws DOMException, ParserConfigurationException,
			SAXException, IOException, ParseException {
		storage = new Storage();
		command = new CommandMatch();
		mItemList = storage.readDataFromFile();
		//mItemList.checkStatus();
		stateHistory = new StateHistory();
	}

	/**
	 * Method to get the Logic singleton
	 * 
	 * @return
	 * @throws DOMException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static Logic getInstanceLogic() throws DOMException,
			ParserConfigurationException, SAXException, IOException,
			ParseException {
		if (logicSingleton == null) {
			logicSingleton = new Logic();
		}

		return logicSingleton;
	}

	public int getItemListSize() {
		return Logic.mItemList.size();
	}

	public CommandType getCommandType(String commandTypeString) {
		CommandType result;
		// if the first word is an integer, then command type is update
		// otherwise, parse the command
		if (StringUtil.isInteger(commandTypeString)) {
			result = CommandType.UPDATE;
			fastUpdate = true;
		} else {
			result = command.determineCommandType(commandTypeString);
			fastUpdate = false;
		}
		return result;
	}

	/**
	 * This method executes whichever commandType it receives accordingly
	 * 
	 * @param commandType
	 * @throws Exception
	 */
	public ArrayList<Item> executeCommand(String userInput) throws Exception {
		CommandType commandType = getCommandType(StringUtil
				.getFirstWord(userInput));
		userInput = StringUtil.trimString(userInput);

		switch (commandType) {
		case CREATE:
			itemsForGUI = add(userInput);
			break;
		case READ:
			itemsForGUI = read(userInput);
			break;
		case UPDATE:
			itemsForGUI = update(userInput);
			break;
		case DELETE:
			itemsForGUI = simpleOperation(CommandType.DELETE, userInput);
			break;
		case DONE:
			itemsForGUI = simpleOperation(CommandType.DONE, userInput);
			break;
		case UNDONE:
			itemsForGUI = simpleOperation(CommandType.UNDONE, userInput);
			break;
		case CLEAR:
			itemsForGUI = clear();
			break;
		case UNDO:
			itemsForGUI = undo();
			break;
		case REDO:
			itemsForGUI = redo();
			break;
		
		case INVALID:
			LogUtil.Log(TAG, "invalid command, invoke NLP general parser");
			String standardInput = NLP.getInstance().generalParser(userInput);
			if (userInput != standardInput) {
				executeCommand(standardInput);
			} else {
				this.setSystemMessage(ERROR_UNRECOGNISED_COMMAND);
				
			}
			break;
		default:
			// shouldn't reach here.
			break;
			
		}
		return itemsForGUI;
	}

	private ArrayList<Item> undo() {
		if (stateHistory.canUndo() && stateHistory.saveStateToFuture(mItemList)) {
			mItemList = stateHistory.undo();
			
			this.setSystemMessage(MESSAGE_UNDO_SUCCESS);
		} else {
			this.setSystemMessage(MESSAGE_CANNOT_UNDO);
		}

		return mItemList.getAllItems();
	}

	private ArrayList<Item> redo() {
		if (stateHistory.canRedo() && stateHistory.saveStateToHistory(mItemList)) {
			mItemList = stateHistory.redo();

			this.setSystemMessage(MESSAGE_REDO_SUCCESS);
		} else {
			this.setSystemMessage(MESSAGE_CANNOT_REDO); 
		}

		return mItemList.getAllItems();
	}

	private ArrayList<Item> add(String userInput) throws ParserConfigurationException,
			TransformerException, DOMException, SAXException, IOException, ParseException {
		saveState();
		String content;
		String[] arr = userInput.split(" ", 2);
		String result = "";

		if (arr.length > 1) {
			content = arr[1];
			result = NLP.getInstance().addParser(content).execute();
			setSystemMessage(result);
		} else {
			result += MESSAGE_ADD_TIP;
			result += MESSAGE_ADD_EXAMPLE;
			setSystemMessage(result);
		}

		if(!getSystemMessage().equals(AddCommand.ADD_SUCCESSFUL)){
			undo();
			result = MESSAGE_ADD_TIP;
			result += MESSAGE_ADD_EXAMPLE;
			setSystemMessage(result);
		}
		saveFile();
		return mItemList.getAllItems();
	}

	private ArrayList<Item> read(String userInput) {  // --< FINAL VERSION SHOULD BE RETURNING ARRAYLIST OF ITEMS INSTEAD OF STRING
		String systemMessage = "";
		ArrayList<Item> filteredItems = new ArrayList<Item>();
		// Filter by tags
		if ((userInput.contains("#"))) {
			int hashTagPosition = userInput.indexOf("#");
			String tagString = "";
			tagString = userInput.substring(hashTagPosition + 1,
					userInput.length());
			if (tagString.isEmpty()) {
				systemMessage = ERROR_MISSING_TAGS;
				setSystemMessage(systemMessage);
			} else {
				systemMessage =String.format(MESSAGE_SHOW_FILTERED, tagString) ;
				setSystemMessage(systemMessage);
				filteredItems = mItemList.filterByTags(tagString);
			}
			// Filter by completed/uncompleted
		} else if ((userInput.contains("completed") || userInput
				.contains("done"))
				&& !(userInput.contains("undone") || userInput
						.contains("uncompleted"))) {
			filteredItems = mItemList.showCompletedList();
			systemMessage = MESSAGE_SHOW_COMPLETED;
			setSystemMessage(systemMessage);
		} else if ((userInput.contains("undone") || userInput
				.contains("uncompleted"))) {
			filteredItems = mItemList.showUncompletedList();
			systemMessage = MESSAGE_SHOW_UNCOMPLETED;
			setSystemMessage(systemMessage);

			// Filter by dateTime using standard format yyyy/MM/dd
		} else if (userInput.contains("on")) {
			int hasOnPosition = userInput.indexOf("n");
			String dateString = "";
			dateString = userInput.substring(hasOnPosition + 2,
					userInput.length());
			if (dateString.isEmpty()) {
				setSystemMessage("Invalid: Missing date for filter.");
			} else {
				systemMessage =String.format(MESSAGE_SHOW_FILTERED, dateString) ;
				setSystemMessage(systemMessage);
				filteredItems = mItemList.filterByDateTime(dateString);
			}
		}
		// FilteredList not required, returns complete list
		else {
			filteredItems = mItemList.getAllItems();
			systemMessage = "Showing all Tasks";
			setSystemMessage(systemMessage);
		}
		return filteredItems;
	}

	private ArrayList<Item> clear() throws ParserConfigurationException,
			TransformerException {
		saveState();
		String result = "";
		result = mItemList.clear();
		this.setSystemMessage(result);
		saveFile();
		return mItemList.getAllItems();
	}

	private ArrayList<Item> update(String userInput)
			throws ParserConfigurationException, TransformerException {
		saveState();
		String updateInfo = "";
		String[] arr;
		int updateIndex = -1;
		int arrLen;
		String result = "";

		if (fastUpdate) {
			// start with item index
			arr = userInput.split(" ", 2);
			arrLen = 2;
		} else {
			// start with update command
			arr = userInput.split(" ", 3);
			arrLen = 3;
		}

		if (arr.length == arrLen && StringUtil.isInteger(arr[arrLen - 2])) {
			updateInfo = arr[arrLen - 1];
			updateIndex = Integer.valueOf(arr[arrLen - 2]);
		} else {
			result += "update an event or task";
		}

		if (!updateInfo.isEmpty() && mItemList.validIndex(updateIndex - 1)) {
			result = NLP.getInstance().updateParser(mItemList.getItem(updateIndex - 1), updateInfo).execute();
			saveFile();
			LogUtil.Log(TAG, "update index " + (updateIndex - 1));
		} else {
			result = "update's failed.";
		}
		this.setSystemMessage(result);
		return mItemList.getAllItems();
	}

	/**
	 * simple operations include delete, done, undone takes in only index
	 * numbers
	 * 
	 * @param type
	 * @param userInput
	 * @return string
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws ParseException
	 * @throws IOException
	 * @throws SAXException
	 * @throws DOMException
	 */
	private ArrayList<Item> simpleOperation(CommandType type, String userInput)
			throws ParserConfigurationException, TransformerException,
			DOMException, SAXException, IOException, ParseException {
		saveState();
		String[] arr = userInput.split(" ", 2);
		String result = "";

		if (arr.length > 1) {
			ArrayList<Integer> indexList = NLP.getInstance().batchIndexParser(
					arr[1]);
			if (!indexList.isEmpty()) {
				while (!indexList.isEmpty()) {
					Integer thisIndex = indexList.remove(indexList.size() - 1);
					switch (type) {
					case DELETE:
						result += mItemList.delete(thisIndex) + "\n";
						break;
					case DONE:
						result += mItemList.done(thisIndex) + "\n";
						break;
					case UNDONE:
						result += mItemList.undone(thisIndex) + "\n";
						break;
					default:
						result += "Invalid command type.";
					}
				}
			} else {
				result += "Invalid parameter";
			}
		} else {
			switch (type) {
			case DELETE:
				result += "delete a existing event or task.\ne.g. delete 3";
				break;
			case DONE:
				result += "set an item as done.\ne.g. done 3";
				break;
			case UNDONE:
				result += "set an item as undone.\ne.g. undone 3";
				break;
			default:
				result = "Invalid command type.";
			}
		}

		saveFile();
		this.setSystemMessage(result);
		return mItemList.getAllItems();
	}

	public String getSystemMessage() {
		return this.finalMessage;
	}

	public void setSystemMessage(String message) {
		this.finalMessage = "";
		this.finalMessage = message;

	}

	private void saveState() {
		stateHistory.saveStateToHistory(mItemList);
		stateHistory.popAllFromFuture();
	}

	private void saveFile() throws ParserConfigurationException,
			TransformerException {
		storage.saveDataToFile(mItemList);
	}

	public static ItemList getItemList() {
		return mItemList;
	}
	public ArrayList<Item> getItemsforGUI () {
		return this.itemsForGUI;
	}

	// For GUI testing purpose
	public String getListString() {
		return mItemList.toString();
	}

}
