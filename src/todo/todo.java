package todo;


import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import nlp.NLP;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.library.Command;
import todo.library.Command.CommandType;
import todo.library.FileUtil;
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
		
		do{
			String userInput = requeatForCommand();
			mCommandType = getCommandType(StringUtil.getFirstWord(userInput));
			System.out.println(executeCommand(mCommandType, userInput));
		}while(mCommandType != CommandType.EXIT);
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
				result = delete(userInput);
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
			result = mItemList.add(newItem);
		}else{
			result += "add a new event or task.\n";
			result += "e.g. add project meeting next monday #project";
		}
		
		save();
		return result;
	}
	
	public static String read(){
		String result = "";
		result = mItemList.displayList();
		
		return result;
	}
	
	public static String update(String userInput) throws ParserConfigurationException, TransformerException{
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
	
	public static String delete(String userInput) throws ParserConfigurationException, TransformerException{
		int index;
		String [] arr = userInput.split(" ", 2);
		String result = "";
		
		// why arr.length > 1? cater for batch operation? right now it handles only one delete operation
		if (arr.length > 1 && StringUtil.isInteger(arr[1])){
			index = Integer.valueOf(arr[1]);
			result = mItemList.delete(index);
		}else{
			result += "delete a existing event or task.\n";
			result += "e.g. delete 3";
		}
		
		save();
		return result;
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
