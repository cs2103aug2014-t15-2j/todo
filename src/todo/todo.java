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

import javax.swing.JFrame;

public class todo {
	
	private static Scanner scanner;
	
	private static ItemList mItemList;
	private static boolean fastUpdate;
	
	public static void main(String arg[]) throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException, TransformerException{
		
		
		CommandType mCommandType;
		scanner = new Scanner(System.in);
		
		// read date from data file
		mItemList = FileUtil.readDataFromFile();

		createAndShowGUI();
		
		String userInput = requeatForCommand();
		mCommandType = getCommandType(StringUtil.getFirstWord(userInput));
		
		while (mCommandType != CommandType.EXIT){
			executeCommand(mCommandType, userInput);
			userInput = requeatForCommand();
			mCommandType = getCommandType(StringUtil.getFirstWord(userInput));
		}
		scanner.close();
	}
	
	static String requeatForCommand(){
		System.out.print("command: ");
		return scanner.nextLine().trim();
	}
	
	public static CommandType getCommandType(String commandTypeString){
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
	public static void executeCommand(CommandType commandType, String userInput) throws ParserConfigurationException, TransformerException {
		switch (commandType) {
			case CREATE:
				add(userInput);
				break;
			case READ:
				read();
				break;
			case UPDATE:
				update(userInput);
				break;
			case DELETE:
				delete(userInput);
				break;
			case INVALID:
				System.out.println("Invalid command");
				break;
			default:
				break;
		}
	}
	
	public static void add(String userInput) throws ParserConfigurationException, TransformerException{
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
	
	public static void update(String userInput) throws ParserConfigurationException, TransformerException{
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

		NLP.updateParser(mItemList.getItem(updateIndex-1), updateInfo);
		save();
	}
	
	public static void delete(String userInput) throws ParserConfigurationException, TransformerException{
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
	
	
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TextDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add contents to the window.
        GUI mGUI = new GUI();
        frame.add(mGUI);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        mGUI.textArea.setText(todo.getListString());
    }
	
	// For GUI testing purpose
	public static String getListString(){
		return mItemList.toString();
	}
}
