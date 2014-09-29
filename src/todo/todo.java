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
import todo.library.NLP;
import todo.library.StringUtil;
import todo.model.Item;
import todo.model.ItemList;
import todo.storage.Storage;

public class todo {
	
	private static Scanner scanner;
	private static String userInput;
	
	private static ItemList mItemList;
	private static Integer currentIndex;
	
	public static void main(String arg[]) throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException, TransformerException{
		
		String commandTypeString;
		CommandType mCommandType;
		scanner = new Scanner(System.in);
		
		// read date from data file
		readDataFromFile();

		userInput = requeatForCommand();
		commandTypeString = StringUtil.getFirstWord(userInput);
		
		// if the first word id an integer, then update
		// otherwise, parse the command
		if(StringUtil.isInteger(commandTypeString)){
			mCommandType = CommandType.UPDATE;
			currentIndex = Integer.valueOf(commandTypeString);
		}else{
			mCommandType = Command.determineCommandType(commandTypeString);
			currentIndex = null;
		}
		
		while (mCommandType != Command.CommandType.EXIT){
			executeCommand(mCommandType);
			userInput = requeatForCommand();
			commandTypeString = StringUtil.getFirstWord(userInput);
			mCommandType = Command.determineCommandType(commandTypeString);
		}
		scanner.close();
	}
	
	private static String requeatForCommand(){
		System.out.print("command: ");
		return scanner.nextLine().trim();
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
				System.out.println("Update command");
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
		saveDateToFile();
	}
	
	public static void read(){
		mItemList.displayList();
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
		saveDateToFile();
	}
	
	private static void readDataFromFile() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException{
		mItemList = Storage.readFromXML();
	}
	
	private static void saveDateToFile() throws ParserConfigurationException, TransformerException{
		Storage.storeIntoXML(mItemList);
	}
	
}
