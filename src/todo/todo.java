package todo;


import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.library.Command;
import todo.library.Command.CommandType;
import todo.library.FileUtil;
import todo.library.NLP;
import todo.library.StringUtil;
import todo.model.Item;
import todo.model.ItemList;

public class todo {
	
	private static Scanner scanner;
	private static String userInput;
	
	private static ItemList mItemList;
	private static boolean fastUpdate;
	
	public static void main(String arg[]) throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException, TransformerException{
		
		CommandType mCommandType;
		scanner = new Scanner(System.in);
		
		// read date from data file
		mItemList = FileUtil.readDataFromFile();

		userInput = requeatForCommand();
		mCommandType = getCommandType(StringUtil.getFirstWord(userInput));
		
		while (mCommandType != CommandType.EXIT){
			executeCommand(mCommandType);
			userInput = requeatForCommand();
			mCommandType = getCommandType(StringUtil.getFirstWord(userInput));
		}
		scanner.close();
	}
	
	private static String requeatForCommand(){
		System.out.print("command: ");
		return scanner.nextLine().trim();
	}
	
	private static CommandType getCommandType(String commandTypeString){
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
	 */
	public static void executeCommand(CommandType commandType) throws ParserConfigurationException, TransformerException {
		switch (commandType) {
			case CREATE:
				add();
				break;
			case READ:
				read();
				break;
			case UPDATE:
				update();
				break;
			case DELETE:
				delete();
				break;
			case INVALID:
				System.out.println("Invalid command");
				break;
			default:
				break;
		}
	}
	
	public static void add() throws ParserConfigurationException, TransformerException{
		String content;
		String [] arr = userInput.split(" ", 2);
		if (arr.length > 1){
			content = arr[1];
			Item newItem = NLP.addParser(content);
			mItemList.add(newItem);
		}else{
			System.out.println("add a new event or task");
			System.out.println("e.g. add project meeting next monday #project");
		}
		save();
	}
	
	public static void read(){
		mItemList.displayList();
	}
	
	public static void update(){
		String updateInfo = "";
		String [] arr;
		int updateIndex = -1;
		int arrLen;
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
			System.out.println("update an event or task");
		}
		//NLP
		System.out.println(updateIndex);
		System.out.println(updateInfo);
	}
	
	public static void delete() throws ParserConfigurationException, TransformerException{
		int index;
		String [] arr = userInput.split(" ", 2);
		if (arr.length > 1){
			index = Integer.valueOf(arr[1]);
			mItemList.delete(index);
		}else{
			System.out.println("delete a new event or task");
			System.out.println("e.g. "); //TODO
		}
		save();
	}
	
	private static void save() throws ParserConfigurationException, TransformerException{
		FileUtil.saveDataToFile(mItemList);
	}
	
}
