package todo.logic;

import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import todo.library.Command;
import todo.library.StringUtil;
import todo.library.Command.CommandType;
import todo.model.Item;
import todo.nlp.NLP;

public class Operation {
	
	
	public static CommandType getCommandType(String commandTypeString){
		CommandType result;
		// if the first word is an integer, then command type is update
		// otherwise, parse the command
		if(StringUtil.isInteger(commandTypeString)){
			result = CommandType.UPDATE;
			Data.fastUpdate = true;
		}else{
			result = Command.determineCommandType(commandTypeString);
			Data.fastUpdate = false;
		}
		return result;
	}
	
	/**
	 * This method executes whichever commandType it receives accordingly
	 * @param commandType
	 * @throws TransformerException 
	 * @throws ParserConfigurationException 
	 */
	public static String executeCommand(CommandType commandType, String userInput) throws ParserConfigurationException, TransformerException {
		String result = "";
		
		switch (commandType) {
			case CREATE:
				result = add(userInput);
				break;
			case READ:
				result = read();
				break;
			case UPDATE:
				result = update(userInput);
				break;
			case DELETE:
				result = simpleOperation(CommandType.DELETE, userInput);
				break;
			case DONE:
				result = simpleOperation(CommandType.DONE,userInput);
				break;
			case UNDONE:
				result = simpleOperation(CommandType.UNDONE,userInput);
				break;
			case CLEAR:
				result = clear();
				break;
			case INVALID:
				result = "Invalid command.";
				break;
			default:
				// shouldn't reach here.
				break;
		}
		return result;
	}
	
	
	public static String add(String userInput) throws ParserConfigurationException, TransformerException{
		String content;
		String [] arr = userInput.split(" ", 2);
		String result = "";
		
		if (arr.length > 1){
			content = arr[1];
			Item newItem = NLP.addParser(content);
			result = Data.mItemList.add(newItem);
		}else{
			result += "add a new event or task.\n";
			result += "e.g. add project meeting next monday #project";
		}
		
		save();
		return result;
	}
	
	public static String read(){
		String result = "";
		result = Data.mItemList.displayList();
		
		return result;
	}
	
	public static String clear() {
		String result = "";
		result = Data.mItemList.clear();
		
		return result;
	}
	public static String update(String userInput) throws ParserConfigurationException, TransformerException{
		String updateInfo = "";
		String [] arr;
		int updateIndex = -1;
		int arrLen;
		String result = "";
		
		if (Data.fastUpdate){
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

		if(NLP.updateParser(Data.mItemList.getItem(updateIndex-1), updateInfo)){
			save();
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
	 */
	public static String simpleOperation(CommandType type, String userInput) throws ParserConfigurationException, TransformerException{
		String [] arr = userInput.split(" ", 2);
		String result = "";
		
		if (arr.length > 1){
			if (StringUtil.isInteger(arr[1])){
				int index = Integer.valueOf(arr[1]);
				switch (type){
					case DELETE:
						result = Data.mItemList.delete(index);
						break;
					case DONE:
						result = Data.mItemList.done(index);
						break;
					case UNDONE:
						result = Data.mItemList.undone(index);
						break;
					default:
						result = "Invalid command type.";
				}
			}else{ 
				ArrayList<Integer> indexList = NLP.batchIndexParser(arr[1]);
				if(!indexList.isEmpty()){
					while(!indexList.isEmpty()){
						Integer thisIndex = indexList.remove(indexList.size() - 1);
						switch (type){
							case DELETE:
								result = Data.mItemList.delete(thisIndex);
								break;
							case DONE:
								result = Data.mItemList.done(thisIndex);
								break;
							case UNDONE:
								result = Data.mItemList.undone(thisIndex);
								break;
							default:
								result = "Invalid command type.";
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
		save();
		return result;
	}
	
	private static void save() throws ParserConfigurationException, TransformerException{
		Data.storage.saveDataToFile(Data.mItemList);
	}
	
}
