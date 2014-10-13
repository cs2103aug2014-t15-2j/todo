package todo.logic;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.library.Command;
import todo.library.StringUtil;
import todo.library.Command.CommandType;
import todo.model.Item;
import todo.model.ItemList;
import todo.nlp.NLP;
import todo.storage.Storage;

public class Logic {
	
	private static Logic logicSingleton;
	private Storage storage;
	private ItemList mItemList;
	private boolean fastUpdate;
	
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
		mItemList = storage.readDataFromFile();
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
			result = Command.determineCommandType(commandTypeString);
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
	public String executeCommand(CommandType commandType, String userInput) throws ParserConfigurationException, TransformerException, DOMException, SAXException, IOException, ParseException {
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
				result = simpleOperation(CommandType.DONE, userInput);
				break;
			case UNDONE:
				result = simpleOperation(CommandType.UNDONE, userInput);
				break;
			case CLEAR:
				result = clear();
				break;
			case INVALID:
				result = "Command not recognised.";
				break;
			default:
				// shouldn't reach here.
				break;
		}
		return result;
	}
	
	
	public String add(String userInput) throws ParserConfigurationException, TransformerException{
		String content;
		String [] arr = userInput.split(" ", 2);
		String result = "";
		
		if (arr.length > 1){
			content = arr[1];
			Item newItem = NLP.addParser(content);
			result = mItemList.add(newItem);
		}else{
			result += "add a new event or task.\n";
			result += "e.g. add project meeting next monday #project";
		}
		
		save();
		return result;
	}
	
	public String read(){
		String result = "";
		result = mItemList.displayList();
		
		return result;
	}
	
	public String clear() {
		String result = "";
		result = mItemList.clear();
		
		return result;
	}
	public String update(String userInput) throws ParserConfigurationException, TransformerException{
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

		if(NLP.updateParser(mItemList.getItem(updateIndex-1), updateInfo)){
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
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws DOMException 
	 */
	public String simpleOperation(CommandType type, String userInput) throws ParserConfigurationException, TransformerException, DOMException, SAXException, IOException, ParseException{
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
				ArrayList<Integer> indexList = NLP.batchIndexParser(arr[1]);
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
		save();
		return result;
	}
	
	private void save() throws ParserConfigurationException, TransformerException{
		storage.saveDataToFile(mItemList);
	}
	
	// For GUI testing purpose
	public String getListString(){
			return mItemList.toString();
	}
	
}
