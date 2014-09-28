package todo;


import java.util.Scanner;

import todo.library.Command;
import todo.library.Command.CommandType;
import todo.library.NLP;
import todo.model.Item;
import todo.model.ItemList;

public class todo {
	
	private static Scanner scanner;
	private static String userInput;
	
	private static ItemList mItemList;
	
	public static void main(String arg[]){
		
		String commandTypeString;
		CommandType mCommandType;
		scanner = new Scanner(System.in);
		
		// read date from data file
		readDataFile();

		userInput = requeatForCommand();
		commandTypeString = getFirstWord(userInput);
		mCommandType = Command.determineCommandType(commandTypeString);
		
		while (mCommandType != Command.CommandType.EXIT){
			executeCommand(mCommandType);
			userInput = requeatForCommand();
			commandTypeString = getFirstWord(userInput);
			mCommandType = Command.determineCommandType(commandTypeString);
		}
		scanner.close();
	}
	
	private static String requeatForCommand(){
		System.out.print("command: ");
		return scanner.nextLine();
	}
	
	/**
	 * This method executes whichever commandType it receives accordingly
	 * @param commandType
	 */
	public static void executeCommand(CommandType commandType) {
		switch (commandType) {
			case CREATE:
				String content;
				String [] arr = userInput.split(" ", 2);
				if (arr.length > 1){
					content = arr[1];
					Item newItem = NLP.addParser(content);
					mItemList.add(newItem);
				}else{
					System.out.println("[add] add a new event or task");
					System.out.println("e.g. add project meeting next monday #project");
				}
				break;
			case READ:
				mItemList.displayList();
				break;
			case UPDATE:
				System.out.println("Update command");
				break;
			case DELETE:
				System.out.println("Delete command");
				break;
			case INVALID:
				System.out.println("Invalid command");
				break;
			default:
				break;
		}
	}
	
	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}
	
	private static void readDataFile(){
		mItemList = new ItemList();
	}
	
}
