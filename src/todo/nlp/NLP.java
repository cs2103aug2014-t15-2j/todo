package todo.nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import todo.library.StringUtil;
import todo.model.DateTime;
import todo.model.Item;

import com.joestelmach.natty.DateGroup;

public class NLP {

	public static Item addParser(String msg){		
		
		List<DateGroup> groups;
		ArrayList<String> tagList = new ArrayList<String>();
		String[] strArray;
		List<DateTime> dateTimeList = new ArrayList<DateTime>();
		String location = "";
		
		// add empty start and due date time
		dateTimeList.add(null);
		dateTimeList.add(null);
		
		groups = NLPUtil.getDateGroups(StringUtil.removeQuoted(msg));
		// find possible date time
		if (groups.size() != 0){
			DateGroup group = groups.get(0);
			String groupText = group.getText();
			dateTimeList = NLPUtil.getDateTime(group);
			String wordBeforeDate = StringUtil.getWordBeforeSubstring(msg,groupText);
			if (Arrays.asList(NLPConfig.preDue).contains(wordBeforeDate)){
				// exchange start time and due time
				dateTimeList.add(dateTimeList.remove(0));
			}
			
			msg = NLPUtil.deletePreposition(msg, wordBeforeDate, groupText);
		}
		
		// find long location
		String locationString = StringUtil.getBracketLocation(msg);
		if (locationString.length() > 0){
			location = locationString.substring(2, locationString.length()-1);
			msg = msg.replace(locationString, "");
			msg = StringUtil.trimString(msg);
		}

		// find out one word location and all the tags
		msg = StringUtil.trimString(msg);
		strArray = msg.split(" ");
		for(int i = strArray.length-1; i >= 0 ; i--){
			if (strArray[i].length() > 1 && strArray[i].charAt(0) == '#'){
				tagList.add(0, strArray[i].substring(1));
				msg = msg.replace(strArray[i], "");
			}
			if (strArray[i].length() > 1 && strArray[i].charAt(0) == '@'){
				location = strArray[i].substring(1);
				msg = msg.replace(strArray[i], "");
			}
		}
		
		// delete all the escape characters
		msg = msg.replaceAll("\\\\#", "#");
		msg = msg.replaceAll("\\\\@", "@");
		
		//if the whole sentence is quoted, then delete the quotation marks
		msg = StringUtil.trimString(StringUtil.removeFullQuote(msg));
		
		//print out for testing
		/*
		System.out.println("description: " + msg);
		DateFormat mDateFormate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		if (startDate != null)
			System.out.println("start time: " + mDateFormate.format(startDate) + "  " + startDateHasTime);
		if (dueDate != null)
			System.out.println("due time: " + mDateFormate.format(dueDate) + "  " + dueDateHasTime);
		if (!location.equals(""))
			System.out.println("location: " + location);
		if (tagList.size() != 0)
			System.out.println("tags: " + tagList.toString());
		*/
		
		
		return new Item(msg, dateTimeList.get(0), dateTimeList.get(1), location, 1, tagList);
	}
	
	public static boolean updateParser(Item item, String msg){
		if (StringUtil.isFullQuote(msg)){
			item.setDescription(StringUtil.removeFullQuote(msg));
		}else if (msg.charAt(0) == '@'){
			item.setLocation(msg.substring(1));
		}else{
			List<DateGroup> groups = NLPUtil.getDateGroups(msg);
			List<DateTime> dateTimeList = new ArrayList<DateTime>();
			if (groups.size() != 0){
				DateGroup group = groups.get(0);
				String groupText = group.getText();
				dateTimeList = NLPUtil.getDateTime(group);
				String wordBeforeDate = StringUtil.getWordBeforeSubstring(msg,groupText);
				if (Arrays.asList(NLPConfig.preDue).contains(wordBeforeDate)){
					// exchange start time and due time
					dateTimeList.add(dateTimeList.remove(0));
				}
				item.setStartDateTime(dateTimeList.get(0));
				item.setDueDateTime(dateTimeList.get(1));
			}
		}
		return true;
	}
	
	public static ArrayList<Integer> batchIndexParser(String indices){
		return NLPUtil.readIndexList(indices);
	}

}
