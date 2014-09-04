package todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import todo.library.NaturalLanguageProcessor;
import todo.model.Item;

public class todo {
	public static void main(String arg[]){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		System.out.println(month);
		
		Scanner scanner;
		String commandTypeString;
		scanner = new Scanner(System.in);
		System.out.print("command: ");
		commandTypeString=scanner.next();
		while (!commandTypeString.equals("exit")){
			if (commandTypeString.equals("add")){
				String content = scanner.nextLine().substring(1);
				Item item = NaturalLanguageProcessor.processAdd(content);
				System.out.println("Description: " + item.getDescription());
				System.out.println("StartDateTime: " + item.getStartDateTime());
			}
			System.out.print("command: ");
			commandTypeString=scanner.next();
		}

	}
	
}
