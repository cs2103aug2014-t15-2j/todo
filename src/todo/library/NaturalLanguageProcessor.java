package todo.library;

import java.util.Calendar;

import todo.model.DateTime;
import todo.model.Item;

public class NaturalLanguageProcessor {
	public static Item processAdd(String msg){
		Item item = new Item();
		Calendar c = Calendar.getInstance();
		
		if (msg.contains("tomorrow")){
			msg = msg.replace(" tomorrow", "");
			c.add(Calendar.DATE, 1);
			DateTime startDateTime = new DateTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			item.setStartDateTime(startDateTime);
			item.setDescription(msg);
		}
		
		if (msg.contains("Monday")){
			item = processDayOfWeek(item, msg, "Monday");
		}
		if (msg.contains("Tuesday")){
			item = processDayOfWeek(item, msg, "Tuesday");
		}
		if (msg.contains("Wednesday")){
			item = processDayOfWeek(item, msg, "Wednesday");
		}
		if (msg.contains("Thursday")){
			item = processDayOfWeek(item, msg, "Thursday");
		}
		if (msg.contains("Friday")){
			item = processDayOfWeek(item, msg, "Friday");
		}
		if (msg.contains("Saturday")){
			item = processDayOfWeek(item, msg, "Saturday");
		}
		if (msg.contains("Sunday")){
			item = processDayOfWeek(item, msg, "Sunday");
		}
		return item;
	}
	
	private static Item processDayOfWeek(Item item, String msg, String day){
		Calendar c = Calendar.getInstance();
		msg = msg.replace(" "+day, "");
		int dayOfWeek = dayConverter(day);
		int currDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if (currDayOfWeek > dayOfWeek){
			c.add(Calendar.DATE, 7 - currDayOfWeek + dayOfWeek);
		}else{
			c.add(Calendar.DATE, dayOfWeek - currDayOfWeek);
		}
		DateTime startDateTime = new DateTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		item.setStartDateTime(startDateTime);
		item.setDescription(msg);
		return item;
	}
	
	private static int dayConverter(String day){
		int result = 0;
		if (day.equals("Sunday")){
			result = 1;
		}else if (day.equals("Monday")){
			result = 2;
		}else if (day.equals("Tuesday")){
			result = 3;
		}else if (day.equals("Wednesday")){
			result = 4;
		}else if (day.equals("Thursday")){
			result = 5;
		}else if (day.equals("Friday")){
			result = 6;
		}else if (day.equals("Saturday")){
			result = 7;
		}
		
		return result;
	}
}
