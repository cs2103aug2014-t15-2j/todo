package todo;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.ArrayList;

import todo.util.LogUtil;
import todo.logic.Logic;
import todo.util.StringUtil;
import todo.model.*;
import todo.ui.*;
//@author A0111082Y

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
		GUIcontrol guiControl = new GUIcontrol();
		
		do{
			String userInput = requestForCommand();
			LogUtil.Log(TAG, userInput);
			mCommandType = logic.getCommandType(StringUtil.getFirstWord(userInput));
			LogUtil.Log(TAG, mCommandType.toString());
			ArrayList<Item> returnValue = new ArrayList<Item>();
			returnValue = (guiControl.sendToGUI(userInput));
			System.out.println("Status:");
			System.out.println(logic.getSystemMessage());
			
			System.out.println(printArrayList(returnValue));
		}while(mCommandType != CommandType.EXIT);
		scanner.close();
	}
		
	private static String requestForCommand(){
		System.out.print("command: ");
		return scanner.nextLine().trim();
	}
	private static String printArrayList(ArrayList<Item> printTarget) {
		String result = "";
		if (printTarget.size() == 0){
			return "The list is empty";
		}else{
			for (int i = 0; i < printTarget.size(); i++){
				result += ((i+1) + ". " + printTarget.get(i).toString()+"\n");
			}
		}
		return result;
	}
}
