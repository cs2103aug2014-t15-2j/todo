package todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.joestelmach.natty.*;

import todo.library.NLP;
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
				String content = scanner.nextLine().trim();
				/*
				Item item = NaturalLanguageProcessor.processAdd(content);
				System.out.println("Description: " + item.getDescription());
				System.out.println("StartDateTime: " + item.getStartDateTime());
				*/
				/*
				Parser parser = new Parser();
				List<DateGroup> groups = parser.parse(content);
				
				Date dates = groups.get(0).getDates().get(0);
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				System.out.println("date: " + df.format(dates));
				*/
				NLP.addParser(content);
			}else{
				System.out.println("Invalid command");
				commandTypeString=scanner.nextLine();
			}
			System.out.print("command: ");
			commandTypeString=scanner.next();
		}

		scanner.close();
	}
	
}
