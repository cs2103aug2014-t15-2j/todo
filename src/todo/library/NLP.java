package todo.library;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class NLP {
	public static final String[] preStart = {"from", "on", "at", "in"};
	public static final String[] preDue = {"by", "before", "due"};
	
	public static void addParser(String msg){
		// TEMP tutorial
		if (msg.equals("")){
			System.out.println("[add] add a new event or task");
			System.out.println("e.g. add project meeting next monday #project");
			return;
		}
		
		Parser parser = new Parser();
		List<DateGroup> groups;
		ArrayList<String> tagList = new ArrayList<String>();
		String[] strArray;
		
		Date date1 = null;
		Date date2 = null;
		
		String msgToDetectDate = removeQuoted(msg);
		groups = parser.parse(msgToDetectDate);
		
		// escape wrong date time parse by Natty, skip all integers
		while (groups.size() > 0 && isInteger(groups.get(0).getText())){
			 msgToDetectDate = msgToDetectDate.substring(msgToDetectDate.indexOf(groups.get(0).getText())+1, msgToDetectDate.length());
			if(msgToDetectDate.equals("")){
				groups.clear();
				break;
			}
			groups = parser.parse(msgToDetectDate);
		}
		
		// find possible date time
		if (groups.size() > 0){
			DateGroup group = groups.get(0);
			if (group.getDates().size() == 2){
				// has both start time and due time
				date1 = group.getDates().get(0);
				date2 = group.getDates().get(1);
			}else if(group.getDates().size() == 1){
				// only start time or due time
				date1 = group.getDates().get(0);
			}
			
			// delete preposition before date
			String wordBeforeDate = getWordBeforeSubstring(msg,group.getText());
			if (Arrays.asList(preStart).contains(wordBeforeDate)
					|| Arrays.asList(preDue).contains(wordBeforeDate)){
				// if it is due date, then set to date2
				if (Arrays.asList(preDue).contains(wordBeforeDate)){
					date2 = date1;
					date1 = null;
				}
				msg = msg.replace(wordBeforeDate + " " + group.getText(), "");
			}else{ // no preposition
				msg = msg.replace(group.getText(), "");
			}
		}
		
		msg = trimString(msg);
		strArray = msg.split(" ");
		
		// find out all the tags
		for(int i = strArray.length-1; i >= 0 ; i--){
			if (strArray[i].charAt(0) == '#'){
				tagList.add(0, strArray[i].substring(1));
				msg = msg.replace(strArray[i], "");
			}
		}
		
		//if the whole sentence is quoted, then delete the quotation marks
		msg = trimString(removeFullQuote(msg));

		
		//print out for testing
		System.out.println("description: " + msg);
		DateFormat mDateFormate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		if (date1 != null)
			System.out.println("start time: " + mDateFormate.format(date1));
		if (date2 != null)
			System.out.println("due time: " + mDateFormate.format(date2));
		if (tagList.size() != 0)
			System.out.println("tags: " + tagList.toString());
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
		if(str.contains("\"")) {
			String toDelete = "\"";
			int idx = str.indexOf('"');
			while(idx+1<str.length() && str.charAt(idx+1) != ('\"')){
				toDelete += str.charAt(idx+1);
				idx++;
			}
			if(str.charAt(idx+1) == '\"'){
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
		if (str.charAt(0) == '\"' && str.charAt(str.length()-1) == '\"'){
			int count = 0;
			for(int i = 0; i < str.length(); i++){
				if(str.charAt(i) == '\"'){
					count++;
				}
			}
			if (count == 2){
				return str.substring(1, str.length()-2);
			}
		}
		return str;
	}
}
