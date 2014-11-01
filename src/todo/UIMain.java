package todo;

import java.io.PrintStream;
import java.util.Scanner;

import todo.util.CommandType;
import todo.util.LogUtil;
import todo.logic.Logic;
import todo.util.StringUtil;

public class UIMain {
	
	private static String TAG = "UIMain";
	private static Scanner scanner;
	private static Logic logic;

	public static void main(String arg[]) throws Exception{
		
		processor(new Scanner(System.in), System.out);
	}
	
	public static void processor(Scanner input, PrintStream output) throws Exception {
		  // Remainder of code

		CommandType mCommandType;
		scanner = new Scanner(System.in);
		logic = Logic.getInstanceLogic();
		
		do{
			String userInput = requestForCommand();
			LogUtil.Log(TAG, userInput);
			mCommandType = logic.getCommandType(StringUtil.getFirstWord(userInput));
			LogUtil.Log(TAG, mCommandType.toString());
			System.out.println(logic.executeCommand(userInput));
		}while(mCommandType != CommandType.EXIT);
		scanner.close();
	}
		
	private static String requestForCommand(){
		System.out.print("command: ");
		return scanner.nextLine().trim();
	}
}
