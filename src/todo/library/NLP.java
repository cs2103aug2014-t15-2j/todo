package todo.library;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import todo.model.DateTime;
import todo.model.Item;
import todo.model.ItemList;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class NLP {
	public static final String[] filterOut = {"eve"};
	public static final String[] preStart = {"from", "on", "at"};
	public static final String[] preDue = {"by", "before", "due", "in"};
	public static final String[] timeKeyword = {"EXPLICIT_TIME", "minute", "hour"};
	
	public static Item addParser(String msg){		
		Parser parser = new Parser();
		List<DateGroup> groups;
		ArrayList<String> tagList = new ArrayList<String>();
		String[] strArray;
		
		Date startDate = null;
		Date dueDate = null;
		String location = "";
		boolean startDateHasTime = false;
		boolean dueDateHasTime = false;
		DateTime startDateTime = null;
		DateTime dueDateTime = null;
		
		String msgToDetectDate = removeQuoted(msg);
		groups = parser.parse(msgToDetectDate);
		
		
		// escape wrong date time parse by Natty
		// skip when the date text is an integer
		// or a word in the filterOut list
		while (groups.size() > 0 && (isInteger(groups.get(0).getText())
				|| Arrays.asList(filterOut).contains(groups.get(0).getText()))){
			 msgToDetectDate = msgToDetectDate.replace(groups.get(0).getText(), "");
			if(msgToDetectDate.equals("")){
				groups.clear();
				break;
			}
			groups = parser.parse(msgToDetectDate);
		}
		
		//escape when the first integer of the date/time is a part of the description
		//if the first char is an integer, and there is no space before it
		if(groups.size() > 0 && isInteger(groups.get(0).getText().charAt(0)+"") 
				&& msgToDetectDate.charAt(msgToDetectDate.indexOf(groups.get(0).getText())-1) != ' '){
			msgToDetectDate = groups.get(0).getText().substring(1);
			//delete all the subsequent integers
			while (isInteger(msgToDetectDate.charAt(0)+"")){
				msgToDetectDate = msgToDetectDate.substring(1);
			}
			groups = parser.parse(msgToDetectDate);
		}
		
		// find possible date time
		if (groups.size() > 0){
			String syntaxTree;
			DateGroup group = groups.get(0);
			if (group.getDates().size() == 2){
				// has both start time and due time
				startDate = group.getDates().get(0);
				dueDate = group.getDates().get(1);
				syntaxTree = group.getSyntaxTree().toStringTree();
				String[] syntaxTreeArray = syntaxTree.split("DATE_TIME ");
				if(stringContainSub(syntaxTreeArray[1], timeKeyword)){
					// if start date has time
					startDateHasTime = true;
				}
				if(stringContainSub(syntaxTreeArray[2], timeKeyword)){
					// if due date has time
					dueDateHasTime = true;
				}
			}else if(group.getDates().size() == 1){
				// only start time or due time
				syntaxTree = group.getSyntaxTree().toStringTree();
				if(stringContainSub(syntaxTree, timeKeyword)){
					// if has time
					startDateHasTime = true;
				}
				startDate = group.getDates().get(0);
			}
			
			// delete preposition before date
			String wordBeforeDate = getWordBeforeSubstring(msg,group.getText());
			if (Arrays.asList(preStart).contains(wordBeforeDate)
					|| Arrays.asList(preDue).contains(wordBeforeDate)){
				// if it is due date, then set to date2
				if (Arrays.asList(preDue).contains(wordBeforeDate)){
					dueDate = startDate;
					startDate = null;
					dueDateHasTime = startDateHasTime;
				}
				msg = msg.replace(wordBeforeDate + " " + group.getText(), "");
			}else{ // no preposition
				msg = msg.replace(group.getText(), "");
			}
		}
		
		// find long location
		String locationString = getBracketLocation(msg);
		if (locationString.length() > 0){
			location = locationString.substring(2, locationString.length()-1);
			msg = msg.replace(locationString, "");
			msg = trimString(msg);
		}

		// find out one word location and all the tags
		msg = trimString(msg);
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
		msg = trimString(removeFullQuote(msg));
		
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
		
		// build DateTime for startDate & dueDate
		if (startDate != null){
			startDateTime = new DateTime(startDate, startDateHasTime);
		}
		if (dueDate != null){
			dueDateTime = new DateTime(dueDate, dueDateHasTime);
		}
		return new Item(msg, startDateTime, dueDateTime, location, 1, tagList);
	}
	
	private static String trimString(String str){
		return str.trim().replaceAll(" +", " ");
	}
	
	/**
	 * Get the word before the given substring
	 * @param str original string
	 * @param sub substring
	 * @return the word string before the substring
	 */
	private static String getWordBeforeSubstring(String str, String sub){
		int idx = str.lastIndexOf(sub)-2;
		String result = "";
		while (idx > 0 && str.charAt(idx) != ' '){
			result = str.charAt(idx) + result;
			idx--;
		}
		return result;
	}
	
	/**
	 * Check if a string can be converted into an integer
	 * @param s string
	 * @return boolean
	 */
	private static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
	/**
	 * Delete all the content quoted in a string
	 * @param str original string
	 * @return cleaned string
	 */
	private static String removeQuoted(String str){
		//System.out.println("current str: "+str);
		if(str.contains("\"") && str.replaceAll("[^\"]", "").length() % 2 == 0) {
			//there is quotation, and the number of them is even
			String toDelete = "\"";
			int idx = str.indexOf('"');
			while(idx+1<str.length() && str.charAt(idx+1) != ('\"')){
				toDelete += str.charAt(idx+1);
				idx++;
			}
			if(idx+1<str.length() && str.charAt(idx+1) == '\"'){
				toDelete += "\"";
				str = str.replace(toDelete, "");
			}
			return removeQuoted(str).trim().replaceAll(" +", " ");
		}else{
			return str.trim().replaceAll(" +", " ");
		}
	}
	
	/**
	 * if there are only two quotation marks in the string
	 * and one is at the beginning, and the other is at the end
	 * then remove the two quotation marks
	 * @param str the original string
	 * @return string
	 */
	private static String removeFullQuote(String str){
		if (str.length() > 1){
			if (str.charAt(0) == '\"' && str.charAt(str.length()-1) == '\"'){
				int count = 0;
				for(int i = 0; i < str.length(); i++){
					if(str.charAt(i) == '\"'){
						count++;
					}
				}
				if (count == 2){
					return str.substring(1, str.length()-1);
				}
			}
		}
		return str;
	}
	
	/**
	 * get the location within a pair of brackets after @
	 * @param str original string
	 * @return "@(content)"
	 */
	private static String getBracketLocation(String str){
		String toDelete = "";
		if(str.contains("@(")) {
			toDelete = "@(";
			int idx = str.indexOf("@(");
			
			// if there is an escape character
			if (str.charAt(idx-1) == '\\'){
				return getBracketLocation(str.substring(idx+1, str.length()));
			}
			idx++;
			while(idx+1<str.length() && str.charAt(idx+1) != (')')){
				toDelete += str.charAt(idx+1);
				idx++;
			}
			if(idx+1<str.length() && str.charAt(idx+1) == ')'){
				toDelete += ")";
			}else{
				toDelete = "";
			}
		}
		return toDelete;
	}
	
	/**
	 * This method check a string contains any string in a string array
	 * @param str the string to be checked
	 * @param list a string array
	 * @return boolean
	 */
	private static boolean stringContainSub(String str, String[] list){
		for (int i = 0; i < list.length; i++){
			if (str.contains(list[i])){
				return true;
			}
		}
		return false;
	}

}
