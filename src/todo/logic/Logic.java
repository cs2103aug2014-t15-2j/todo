package todo.logic;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.util.LogUtil;
import todo.command.AddCommand;
import todo.command.UpdateCommand;
import todo.model.*;
import todo.nlp.NLP;
import todo.storage.Storage;
import todo.util.StringUtil;

//@author A0111082Y

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
	private static final String ERROR_MISSING_LOCATION = "Invalid: Missing location name";
	private static final String ERROR_INVALID_COMMAND = "Invalid command";
	private static final String ERROR_INVALID_PARAM = "Invalid parameter";
	private static final String ERROR_BATCH_PARTIAL_SUCCESS = "Some operations did not complete sucessfully";
	private static final String ERROR_INVALID_DATE_PARAMETER = "Invalid: Missing date for filter.";

	private static final String MESSAGE_ADD_TIP = "Add command : add a new event or task.\neg add project meeting tomorrow @utown #cs2103 \n";
	private static final String MESSAGE_DELETE_TIP = "Delete a existing event or task.\ne.g. delete 3";
	private static final String MESSAGE_DONE_TIP = "Set an item as done.\ne.g. done 3";
	private static final String MESSAGE_UNDONE_TIP = "Set an item as undone.\ne.g. undone 3";
	private static final String MESSAGE_UNDO_SUCCESS = "You have successfully undone the previous action.";
	private static final String MESSAGE_CANNOT_UNDO = "No action can be undone.";
	private static final String MESSAGE_REDO_SUCCESS = "You have successfully redo the previous action.";
	private static final String MESSAGE_CANNOT_REDO = "No action to redo.";
	private static final String MESSAGE_SHOW_UNCOMPLETED = "Showing all uncompleted tasks";
	private static final String MESSAGE_SHOW_COMPLETED = "Showing all completed tasks";
	private static final String MESSAGE_SHOW_FILTERED = "Showing task(s) labeled with" + " " + "%1$s";
	private static final String MESSAGE_SHOW_ALL = "Showing all tasks";
	private static final String MESSAGE_ITEM_NOT_FOUND = "Could not find required item, showing all items";
	
	private static final String LOGIC_HASHTAG = "#";
	private static final String LOGIC_AT = "@";
	private static final String LOGIC_EMPTY_STRING  = "";
	private static final String LOGIC_COMPLETED = "completed";
	private static final String LOGIC_UNCOMPLETED = "uncompleted";
	private static final String LOGIC_DONE = "done";
	private static final String LOGIC_UNDONE = "undone";
	private static final String LOGIC_ON = "on";
	private static final String LOGIC_N = "n";
	private static final String LOGIC_UPDATE_INDEX= "update index ";
	private static final String LOGIC_TODAY= "today";
	private static final String LOGIC_ADDTIME = " 11:00";
	
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
		//Calls functions to initialise all required values by reading from external file
		storage = new Storage();
		command = new CommandMatch();
		mItemList = storage.readDataFromFile();
		mItemList.checkStatus();
		stateHistory = new StateHistory();
	}

	/**
	 * Method to get the Logic singleton
	 * 
	 * @return logic instance
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
	/**
	 * This method determines the command type by matching the command to be executed to user input
	 * 
	 */
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
		case EXIT:
			System.exit(0);
		
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

	private ArrayList<Item> undo() throws ParseException {
		if (stateHistory.canUndo()) {
			stateHistory.saveStateToFuture(mItemList);
			mItemList = stateHistory.undo();
			
			this.setSystemMessage(MESSAGE_UNDO_SUCCESS);
		} else {
			this.setSystemMessage(MESSAGE_CANNOT_UNDO);
		}

		return mItemList.getAllItems();
	}

	private ArrayList<Item> redo() throws ParseException {
		if (stateHistory.canRedo()) {
			stateHistory.saveStateToHistory(mItemList);
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
		String result = LOGIC_EMPTY_STRING ;

		if (arr.length > 1) {
			content = arr[1];
			result = NLP.getInstance().addParser(content).execute();
			setSystemMessage(result);
		} 
		// When add command does now come along with item description
		else {
			result += MESSAGE_ADD_TIP;
			setSystemMessage(result);
		}
		
		// When add operation is not successful due to other errors 
		if(!getSystemMessage().equals(AddCommand.ADD_SUCCESSFUL)&&(!getSystemMessage().equals(AddCommand.INVALID_START_DUE))){
			stateHistory.undo();
			result = MESSAGE_ADD_TIP;
			setSystemMessage(result);
		}
		saveFile();
		return mItemList.getAllItems();
	}

	private ArrayList<Item> read(String userInput) {  
		String systemMessage = LOGIC_EMPTY_STRING ;
		ArrayList<Item> filteredItems = new ArrayList<Item>();
		
		// Search for items in the item list with the matching hash tags
		if ((userInput.contains(LOGIC_HASHTAG))) {
			filteredItems = getFilteredHashTags(userInput);
			if (filteredItems.size()==0) {
				filteredItems = replaceEmptyFilteredList();
			}
		}	
		
		// Search for items in the item list with the matching location
		else if (userInput.contains(LOGIC_AT)){
			 filteredItems = getFilteredLocation (userInput);
			 if (filteredItems.size()==0) {
					filteredItems = replaceEmptyFilteredList();
				}
		}
		
		// Search for items in the item list with the status completed
		else if (checkCompletedInput(userInput)) {
			filteredItems = getFilteredCompleted();
			if (filteredItems.size()==0) {
				filteredItems = replaceEmptyFilteredList();
			}
		} 
		
		// Search for items in the item list with the status uncompleted
		else if (checkUncompletedInput(userInput)) {
			 filteredItems = getFilteredUnompleted();
			 if (filteredItems.size()==0) {
					filteredItems = replaceEmptyFilteredList();
				}
		}	
		
		// Filter by dateTime using standard format yyyy-MM-dd
		else if (userInput.contains(LOGIC_ON)) {
			filteredItems = getFilteredDate(userInput);
		}
		
		// FilteredList not required, returns the full list of items
		else {
			filteredItems = getFullList();
		}
		return filteredItems;
	}

	private ArrayList<Item> clear() throws ParserConfigurationException,
			TransformerException, ParseException {
		saveState();
		String result = "";
		result = mItemList.clear();
		this.setSystemMessage(result);
		saveFile();
		return mItemList.getAllItems();
	}

	private ArrayList<Item> update(String userInput)
			throws ParserConfigurationException, TransformerException, ParseException {
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
			result = UpdateCommand.UPDATE_FAILED;
		}

		if (!updateInfo.isEmpty() && mItemList.validIndex(updateIndex - 1)) {
			result = NLP.getInstance().updateParser(mItemList.getItem(updateIndex - 1), updateInfo).execute();
			saveFile();
			LogUtil.Log(TAG, LOGIC_UPDATE_INDEX + (updateIndex - 1));
		} else {
			result = UpdateCommand.UPDATE_FAILED;
		}
		
		if(result.equals(UpdateCommand.UPDATE_FAILED)){
			stateHistory.undo();
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
		int num = 0;
		int numOfItems = 0;

		if (arr.length > 1) {
			ArrayList<Integer> indexList = NLP.getInstance().batchIndexParser(arr[1]);
			numOfItems = indexList.size();
			if (!indexList.isEmpty()) {
				while (!indexList.isEmpty()) {
					Integer thisIndex = indexList.remove(indexList.size() - 1);
					switch (type) {
					case DELETE:
						result = mItemList.delete(thisIndex);
						break;
					case DONE:
						result = mItemList.done(thisIndex);
						break;
					case UNDONE:
						result = mItemList.undone(thisIndex);
						break;
					default:
						result = ERROR_INVALID_COMMAND;
					}
					if(result.equals(ItemList.DELETE_SUCCESSFUL) ||
							result.equals(ItemList.DONE_SUCCESSFUL) ||
							 result.equals(ItemList.UNDONE_SUCCESSFUL)){
						num++;
					}
				}
			} else {
				result = ERROR_INVALID_PARAM;
			}
		} else {
			switch (type) {
			case DELETE:
				result = MESSAGE_DELETE_TIP;
				break;
			case DONE:
				result = MESSAGE_DONE_TIP;
				break;
			case UNDONE:
				result = MESSAGE_UNDONE_TIP;
				break;
			default:
				result = ERROR_INVALID_COMMAND;
			}
		}

		if(num == 0){
			stateHistory.undo();
		}
		
		if(num != 0 && num != numOfItems){
			this.setSystemMessage(ERROR_BATCH_PARTIAL_SUCCESS);
		}else{
			this.setSystemMessage(result);
		}
		
		saveFile();

		return mItemList.getAllItems();
	}

	public String getSystemMessage() {
		return this.finalMessage;
	}

	public void setSystemMessage(String message) {
		this.finalMessage = LOGIC_EMPTY_STRING;
		this.finalMessage = message;

	}

	private void saveState() throws ParseException {
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
	public ArrayList<Item> getItemsforGUI (ArrayList<Item> listForGUI) {
		return listForGUI;
	}
	
	private ArrayList<Item> getFilteredHashTags (String userInput){
		String systemMessage = LOGIC_EMPTY_STRING ;
		ArrayList<Item> filterByHashTags = new ArrayList<Item> ();
		int hashTagPosition = userInput.indexOf(LOGIC_HASHTAG);
		String tagString = LOGIC_EMPTY_STRING;
		tagString = userInput.substring(hashTagPosition + 1,
				userInput.length());
		if (tagString.isEmpty()) {
			systemMessage = ERROR_MISSING_TAGS;
			setSystemMessage(systemMessage);
		} else {
			systemMessage =String.format(MESSAGE_SHOW_FILTERED, tagString) ;
			setSystemMessage(systemMessage);
			filterByHashTags = mItemList.filterByTags(tagString);
		}
		return filterByHashTags;
	}
	
	private ArrayList<Item> getFilteredLocation (String userInput){
		String systemMessage = LOGIC_EMPTY_STRING ;
		ArrayList<Item> filterByLocation = new ArrayList<Item> ();
		int	locationPosition = userInput.indexOf(LOGIC_AT);
		String locationString = LOGIC_EMPTY_STRING;
		locationString = userInput.substring(locationPosition + 1,
				userInput.length());
		if (locationString.isEmpty()) {
			systemMessage = ERROR_MISSING_LOCATION;
			setSystemMessage(systemMessage);
		} else {
			systemMessage =String.format(MESSAGE_SHOW_FILTERED, locationString) ;
			setSystemMessage(systemMessage);
			filterByLocation = mItemList.filterByLocation(locationString);
		}
		return filterByLocation;
	}
	
	private ArrayList<Item> getFilteredCompleted() {
		String systemMessage = LOGIC_EMPTY_STRING ;
		ArrayList<Item> filterByCompleted = new ArrayList<Item> ();
		filterByCompleted = mItemList.showCompletedList();
		systemMessage = MESSAGE_SHOW_COMPLETED;
		setSystemMessage(systemMessage);
		return filterByCompleted;
	}
	
	private ArrayList<Item> getFilteredUnompleted() {
		String systemMessage = LOGIC_EMPTY_STRING ;
		ArrayList<Item> filterByUncompleted = new ArrayList<Item> ();
		filterByUncompleted = mItemList.showUncompletedList();
		systemMessage = MESSAGE_SHOW_UNCOMPLETED;
		setSystemMessage(systemMessage);
		return filterByUncompleted;
	}
	
	private ArrayList<Item> getFilteredDate(String userInput) {
		String systemMessage = LOGIC_EMPTY_STRING ;
		ArrayList<Item> filterByDate = new ArrayList<Item> ();
		int hasOnPosition = userInput.indexOf(LOGIC_N);
		String dateString = LOGIC_EMPTY_STRING;
		//Extract date from userInput
		dateString = userInput.substring(hasOnPosition + 2,
				userInput.length());
		
		// Case: Users wants to find task that are due today
		
		if(dateString.contains(LOGIC_TODAY)){
			dateString = LocalDateTime.now().toLocalDate().toString().replace("T", " ");
			dateString = dateString.concat(LOGIC_ADDTIME);
			//System.out.println(dateString);
		}
		else {
		dateString = dateString.concat(LOGIC_ADDTIME);
		}
		//System.out.println(dateString.length());
		if (dateString.length()==16) {
			systemMessage =String.format(MESSAGE_SHOW_FILTERED, dateString.substring(0, 10)) ;
			setSystemMessage(systemMessage);
			filterByDate = mItemList.filterByDateTime(dateString);
		} else {
			setSystemMessage(ERROR_INVALID_DATE_PARAMETER);
		}
		return filterByDate;
	}
	
	/* Replace an empty filtered list with the full list of items when the above function could not find 
	 * what the user is searching for
	 */
	private ArrayList<Item> replaceEmptyFilteredList() {
		String systemMessage = LOGIC_EMPTY_STRING ;
		ArrayList<Item> replaced = new ArrayList<Item> ();
		replaced = mItemList.getAllItems();
		systemMessage = MESSAGE_ITEM_NOT_FOUND;
		setSystemMessage(systemMessage);
		return replaced;
	}
	
	private ArrayList<Item> getFullList() {
		String systemMessage = LOGIC_EMPTY_STRING ;
		ArrayList<Item> fullList = new ArrayList<Item> ();
		fullList = mItemList.getAllItems();
		systemMessage = MESSAGE_SHOW_ALL;
		setSystemMessage(systemMessage);
		return fullList;
	}
	
	private boolean checkCompletedInput(String userInput){
		userInput = userInput.toLowerCase();
		if((userInput.contains(LOGIC_COMPLETED) || userInput.contains(LOGIC_DONE))
		&& !(userInput.contains(LOGIC_UNDONE) || userInput.contains(LOGIC_UNCOMPLETED))) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean checkUncompletedInput(String userInput){
		userInput = userInput.toLowerCase();
		if ((userInput.contains(LOGIC_UNDONE) || userInput
				.contains(LOGIC_UNCOMPLETED))) {
			return true;
		}
		else {
		return false;
		}
	}

	// For GUI testing purpose
	public String getListString() {
		return mItemList.toString();
	}

}
