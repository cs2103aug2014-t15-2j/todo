package todo;


import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.util.CommandType;
import todo.logic.Logic;
import todo.util.StringUtil;

public class UIMain {
	
	private static Scanner scanner;
	private static Logic logic;

	public static void main(String arg[]) throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException, TransformerException{
		
		CommandType mCommandType;
		scanner = new Scanner(System.in);
		logic = Logic.getInstanceLogic();
		
		do{
			String userInput = requestForCommand();
			mCommandType = logic.getCommandType(StringUtil.getFirstWord(userInput));
			System.out.println(logic.executeCommand(mCommandType, userInput));
		}while(mCommandType != CommandType.EXIT);
		scanner.close();
	}
	
	private static String requestForCommand(){
		System.out.print("command: ");
		return scanner.nextLine().trim();
	}
}
