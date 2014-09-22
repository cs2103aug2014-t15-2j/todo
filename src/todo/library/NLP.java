package todo.library;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class NLP {
	public static void addParser(String msg){
		Parser parser = new Parser();
		List<DateGroup> groups = parser.parse(msg);
		ArrayList<String> tagList = new ArrayList<String>();
		
		
		Date date1 = null;
		Date date2 = null;
		if (groups.size() == 2){
			date1 = groups.get(0).getDates().get(0);
			msg = msg.replace(groups.get(0).getText(), "");
			date2 = groups.get(1).getDates().get(0);
			msg = msg.replace(groups.get(1).getText(), "");
		}else if(groups.size() == 1){
			date1 = groups.get(0).getDates().get(0);
			msg = msg.replaceAll(groups.get(0).getText(), "");
		}
		
		msg = msg.trim().replaceAll(" +", " ");
		String[] strArray = msg.split(" ");
		
		for(int i = strArray.length-1; i > 0 ; i--){
			if (strArray[i].charAt(0) == '#'){
				tagList.add(0, strArray[i].substring(1));
				msg = msg.replace(strArray[i], "");
			}
		}
		msg = msg.trim().replaceAll(" +", " ");
		
		
		System.out.println("description: " + msg);
		DateFormat mDateFormate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		if (date1 != null)
			System.out.println("date1: " + mDateFormate.format(date1));
		if (date2 != null)
			System.out.println("date2: " + mDateFormate.format(date2));
		if (tagList.size() != 0)
			System.out.println("tags: " + tagList.toString());
	}
	
}
