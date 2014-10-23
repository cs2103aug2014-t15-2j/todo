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
import todo.model.Item;
import todo.model.ItemList;
import todo.model.StateHistory;
import todo.nlp.NLP;
import todo.storage.Storage;
import todo.util.StringUtil;

public class Logic {
	
	private static String TAG = "Logic";
	private static Logic logicSingleton;
	private Storage storage;
	private Command command;
	private ItemList mItemList;
	private StateHistory stateHistory;
	private boolean fastUpdate;
	
	private static final String ERROR_UNRECOGNISED_COMMAND = "Command not recognised.";
	
	private static final String MESSAGE_ADD_TIP ="add a new event or task.\n";
	private static final String MESSAGE_ADD_EXAMPLE ="eg add project meeting tomorrow @utown #cs2103 .";
	
	/**
	 * Private constructor for singleton Logic
	 * 
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws DOMException 
	 */
	private Logic() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException{
		storage = new Storage();
		command = new Command();
		mItemList = storage.readDataFromFile();
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
	public static Logic getInstanceLogic() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException{
		if(logicSingleton == null){
			logicSingleton = new Logic();
		}
		
		return logicSingleton;
	}
	
	public int getItemListSize(){
		return this.mItemList.size();
	}
	
	public CommandType getCommandType(String commandTypeString){
		CommandType result;
		// if the first word is an integer, then command type is update
		// otherwise, parse the command
		if(StringUtil.isInteger(commandTypeString)){
			result = CommandType.UPDATE;
			fastUpdate = true;
		}else{
			result = command.determineCommandType(commandTypeString);
			fastUpdate = false;
		}
		return result;
	}
	
	/**
	 * This method executes whichever commandType it receives accordingly
	 * @param commandType
	 * @throws TransformerException 
	 * @throws ParserConfigurationException 
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws DOMException 
	 */
	public String executeCommand(String userInput) throws ParserConfigurationException, TransformerException, DOMException, SAXException, IOException, ParseException {
		CommandType commandType = getCommandType(StringUtil.getFirstWord(userInput));
		String result = "";
		
		switch (commandType) {
			case CREATE:
				result = add(userInput);
				break;
			case READ:
				result = read(userInput);
				break;
			case UPDATE:
				result = update(userInput);
				break;
			case DELETE:
				result = simpleOperation(CommandType.DELETE, userInput);
				break;
			case DONE:
				result = simpleOperation(CommandType.DONE, userInput);
				break;
			case UNDONE:
				result = simpleOperation(CommandType.UNDONE, userInput);
				break;
			case CLEAR:
				result = clear();
				break;
			case UNDO:
				result = undo();
				break;
			case REDO:
				result = redo();
				break;
			case INVALID:
				LogUtil.Log(TAG, "invalid command, invoke NLP general parser");
				String standardInput = NLP.getInstance().generalParser(userInput);
				if (userInput != standardInput){
					executeCommand(standardInput);
				}else{
					result = ERROR_UNRECOGNISED_COMMAND;
				}
				break;
			default:
				// shouldn't reach here.
				break;    
		}
		return result;
	}
	
	private String undo() {
		String result = "";
		
		if(stateHistory.canUndo() && stateHistory.saveStateToFuture(mItemList)){
			mItemList = stateHistory.undo();
			
			result = "you have successfully undo the previous action.";
		}else{
			result = "no action can be undo.";
		}
		
		return result;
	}
	
	private String redo() {
		String result = "";
		
		if(stateHistory.canRedo() && stateHistory.saveStateToHistory(mItemList)){
			mItemList = stateHistory.redo();
			
			result = "you have successfully redo the previous action.";
		}else{
			result = "no action can be redo.";
		}
		
		return result;
	}


	private String add(String userInput) throws ParserConfigurationException, TransformerException{
		saveState();
		String content;
		String [] arr = userInput.split(" ", 2);
		String result = "";
		
		if (arr.length > 1){
			content = arr[1];
			Item newItem = NLP.getInstance().addParser(content);
			if (newItem != null){
				result = mItemList.add(newItem);
			}
		}else{
			result += MESSAGE_ADD_TIP;
			result += MESSAGE_ADD_EXAMPLE;
		}
		
		saveFile();
		return result;
	}
	
	private String read(String userInput){
		String result = "";
		//Filter by tags
	    if((userInput.contains("#"))){
	    	int hashTagPosition = userInput.indexOf("#");
	    	String tagString = "";
	    	tagString = userInput.substring(hashTagPosition+1, userInput.length());
	    	if(tagString.isEmpty()){
	    		return "Invalid: Missing Tag Names.";
	    	}else{
	    		result = mItemList.filterByTags(tagString);
	    		return result;
	    	}
	    //Filter by completed/uncompleted	
	    }else if((userInput.contains("completed") ||userInput.contains("done")) && !(userInput.contains("undone") ||userInput.contains("uncompleted")) ){
	    	result = mItemList.showCompletedListString();
	    	return result;
	    }else if((userInput.contains("undone")||userInput.contains("uncompleted"))){
	    	result = mItemList.showUncompletedListString();
	    	return result;
	    }else {
	    		result = mItemList.displayList();
			    return result;
	    }
	}
	
	private String clear() throws ParserConfigurationException, TransformerException {
		saveState();
		String result = "";
		result = mItemList.clear();
		
		saveFile();		
		return result;
	}
	
	private String update(String userInput) throws ParserConfigurationException, TransformerException{
		saveState();
		String updateInfo = "";
		String [] arr;
		int updateIndex = -1;
		int arrLen;
		String result = "";
		
		if (fastUpdate){
			// start with item index
			arr = userInput.split(" ",2);
			arrLen = 2;
			
		}else{
			// start with update command
			arr = userInput.split(" ",3);
			arrLen = 3;
		}

		if (arr.length == arrLen && StringUtil.isInteger(arr[arrLen-2])){
			updateInfo = arr[arrLen-1];
			updateIndex = Integer.valueOf(arr[arrLen-2]);
		}else{
			result += "update an event or task";
		}

		if(!updateInfo.isEmpty() 
				&& mItemList.validIndex(updateIndex-1) 
				&& NLP.getInstance().updateParser(mItemList.getItem(updateIndex-1), updateInfo)){
			saveFile();
			result = "update's successful.";
		}else{
			result = "update's failed.";
		}

		return result;
	}
	
	/**
	 * simple operations
	 * include delete, done, undone
	 * takes in only index numbers
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
	private String simpleOperation(CommandType type, String userInput) throws ParserConfigurationException, TransformerException, DOMException, SAXException, IOException, ParseException{
		saveState();
		String [] arr = userInput.split(" ", 2);
		String result = "";
		
		if (arr.length > 1){
			if (StringUtil.isInteger(arr[1])){
				int index = Integer.valueOf(arr[1]);
				switch (type){
					case DELETE:
						result += mItemList.delete(index);
						break;
					case DONE:
						result += mItemList.done(index);
						break;
					case UNDONE:
						result += mItemList.undone(index);
						break;
					default:
						result += "Invalid command type.";
				}
			}else{ 
				ArrayList<Integer> indexList = NLP.getInstance().batchIndexParser(arr[1]);
				if(!indexList.isEmpty()){
					while(!indexList.isEmpty()){
						Integer thisIndex = indexList.remove(indexList.size() - 1);
						switch (type){
							case DELETE:
								result += mItemList.delete(thisIndex)+"\n";
								break;
							case DONE:
								result += mItemList.done(thisIndex)+"\n";
								break;
							case UNDONE:
								result += mItemList.undone(thisIndex)+"\n";
								break;
							default:
								result += "Invalid command type.";
						}
					}
				}else{
					result += "Invalid parameter";
				}
			}
		}else{
			switch (type){
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
		return result;
	}
	
	private void saveState(){
		stateHistory.saveStateToHistory(mItemList);
		stateHistory.popAllFromFuture();
	}
	
	private void saveFile() throws ParserConfigurationException, TransformerException{
		storage.saveDataToFile(mItemList);
	}
	
	// For GUI testing purpose
	public String getListString(){
			return mItemList.displayList();
	}
	
}
