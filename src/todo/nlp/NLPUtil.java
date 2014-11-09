package todo.nlp;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import todo.logic.Logic;
import todo.model.DateTime;
import todo.model.Message;
import todo.util.LogUtil;
import todo.util.StringUtil;
//@author A0105570N
/**
 * This class contains all the utility methods used by NLP
 */
public class NLPUtil {
	private static String TAG = "NLPUtil";
	private static String DELIMITER_COMMA = ",";
	private static String DELIMITER_SLASH = "/";
	private static String DELIMITER_SPACE = " ";
	
	public static Parser parser = new Parser();
	
	// Extractors
	
	/**
	 * Extract date time from a given string
	 * @param msg Message object that may contain date/time text
	 * @return a list of date/time
	 */
	protected static List<DateTime> extractDateTime(Message msg){
		List<DateTime> dateTimeList = new ArrayList<DateTime>();
		List<DateGroup> groups = NLPUtil.getDateGroups(msg.withoutQuotation());
		
		// initialize start and due date time
		dateTimeList.add(null);
		dateTimeList.add(null);
		
		if (groups.size() != 0){
			// there is date/time in the command
			DateGroup group = groups.get(0);
			String groupText = group.getText();
			LogUtil.Log(TAG, "Detect data/time: "+groupText);
			dateTimeList = NLPUtil.getDateTime(group);
			String wordBeforeDate = msg.getWordBeforeSubstring(groupText);
			String twoWordBeforeDate = msg.getTwoWordsBeforeSubstring(groupText);
			// if the word(s) before the date/time text is a due type preposition
			if (Arrays.asList(NLPConfig.preDue).contains(twoWordBeforeDate)
					|| Arrays.asList(NLPConfig.preDue).contains(wordBeforeDate)){
				// exchange start time and due time
				dateTimeList.add(dateTimeList.remove(0));
			}
			NLPUtil.deleteDateTimeText(msg, groupText);
		}else{
			LogUtil.Log(TAG, "No date time detected");
		}
		return dateTimeList;
	}
	
	/**
	 * Extract location
	 * There are two type of location:
	 * 1. Long location: @(long location)
	 * 2. Normal location: @location
	 * @param msg
	 * @return location string
	 */
	protected static String extractLocation(Message msg){
		String location = "";
		String locationString = StringUtil.getBracketLocation(msg.getText());
		// look for long location (location with more than one word)
		if (locationString.length() > 2){
			location = locationString.substring(2, locationString.length()-1);
			msg.deleteSubstring(locationString);
			msg.trim();
			return location;
		}
		msg.trim();
		// look for one-word location
		String[] strArray = msg.getText().split(" ");
		// find all words that start with "@" from the array
		for(int i = strArray.length-1; i >= 0 ; i--){
			if (strArray[i].length() > 1 && strArray[i].charAt(0) == '@'){
				location = strArray[i].substring(1);
				msg.deleteSubstring(strArray[i]);
			}
		}
		return location;
	}
	
	/**
	 * Extract tags
	 * Tags are all the words after #
	 * @param msg
	 * @return array list of tags
	 */
	protected static ArrayList<String> extractTags(Message msg){
		ArrayList<String> tagList = new ArrayList<String>();
		msg.trim();
		String[] strArray = msg.getText().split(" ");
		// look for all the word after a "#" in the string array
		for(int i = strArray.length-1; i >= 0 ; i--){
			if (strArray[i].length() > 1 && strArray[i].charAt(0) == '#'){
				String newTag = strArray[i].substring(1);
				// prevent duplicate tags
				if (!tagList.contains(newTag)){
					tagList.add(0, newTag);
				}
				msg.deleteSubstring(strArray[i]);
			}
		}
		return tagList;
	}
	
	/**
	 * Delete the date/time text and possible prepositions from Message
	 * @param msg: The message that contains the whole input
	 * @param groupText: Date/time text
	 */
	protected static void deleteDateTimeText(Message msg, String groupText){
		String wordBeforeDate = msg.getWordBeforeSubstring(groupText);
		String twoWordsBeforeDate = msg.getTwoWordsBeforeSubstring(groupText);
		// delete preposition before date
		if (Arrays.asList(NLPConfig.preStart).contains(twoWordsBeforeDate)
				|| Arrays.asList(NLPConfig.preDue).contains(twoWordsBeforeDate)){
			// the two word before the date/time is a preposition
			msg.setText(msg.getText().replace(twoWordsBeforeDate + " " + groupText, ""));
		}else if (Arrays.asList(NLPConfig.preStart).contains(wordBeforeDate)
				|| Arrays.asList(NLPConfig.preDue).contains(wordBeforeDate)){
			// the word before date/time is a preposition
			msg.setText(msg.getText().replace(wordBeforeDate + " " + groupText, ""));
		}else if( wordBeforeDate.length() >0 && wordBeforeDate.charAt(wordBeforeDate.length()-1) == ':'){
			// the word before date/time is a colon
			msg.setText(msg.getText().replace(": " + groupText, ""));
		}else {
			// no preposition
			msg.setText(msg.getText().replace(groupText, ""));
		}
	}
	
	/**
	 * Read indexes from a string
	 * E.g. "1,7,3-5,7-8" -> [1,3,4,5,7,8]
	 * @param str
	 * @return array list of integers
	 */
	protected static ArrayList<Integer> readIndexList(String str) throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException{
		ArrayList<Integer> result = new ArrayList<Integer>();
		String delimiter;
		str = str.trim();
		if (str.equalsIgnoreCase(NLPConfig.keywordAll)){
			str = "1-"+Logic.getInstanceLogic().getItemListSize();
		}
		
		// Matching delimiter in the string with precedence： "," > "/" > " "
		if (str.contains(",")){
			delimiter = DELIMITER_COMMA;
		}else if (str.contains(DELIMITER_SLASH)){
			delimiter = DELIMITER_SLASH;
		}else{
			delimiter = DELIMITER_SPACE;
		}
		
		str = StringUtil.trimString(str);
		String[] arr = str.split(delimiter);
		// look for all indices
		for (String s: arr){
			s = s.trim();
			if (StringUtil.isInteger(s)){
				// Discrete indexes
				int newValue = Integer.valueOf(s);
				if(!result.contains(newValue)){
					result.add(newValue);
				}
			}else if (s.contains("-")){
				// Continuous indexes
				String[] subArr = s.split("-");
				if (subArr.length == 2 && StringUtil.isInteger(subArr[0]) && StringUtil.isInteger(subArr[1]) 
					&& Integer.valueOf(subArr[0]) < Integer.valueOf(subArr[1])){
					// the strings before and after "-" are integers
					// and the first integer is greater than the second integer
					for (int i = Integer.valueOf(subArr[0]); i < (Integer.valueOf(subArr[1])+1); i++){
						// generate integers in the range
						if(!result.contains(i)){
							result.add(i);
						}
					}
				}
			}
		}
		Collections.sort(result);
		return result;
	}
	
	/**
	 * Get list of date/time
	 * Process the raw date/time data got from Natty
	 * @param group DateGruop
	 * @return a list of DateTime
	 */
	protected static List<DateTime> getDateTime(DateGroup group){
		String DATE_TIME_SEPARATOR = "DATE_TIME ";
		List<DateTime> dateTimeList = new ArrayList<DateTime>();
		Date startDate = null;
		Date dueDate = null;
		boolean startDateHasTime = false;
		boolean dueDateHasTime = false;
		DateTime startDateTime = null;
		DateTime dueDateTime = null;
		String syntaxTree;
		
		if (group.getDates().size() == 2){
			// has both start time and due time
			startDate = group.getDates().get(0);
			dueDate = group.getDates().get(1);
			// syntaxTree is a string that contains the hierarchies of date/time
			syntaxTree = group.getSyntaxTree().toStringTree();
			String[] syntaxTreeArray = syntaxTree.split(DATE_TIME_SEPARATOR);
			if(StringUtil.stringContainListSubstring(syntaxTreeArray[1], NLPConfig.timeKeyword)){
				// if start date has time
				startDateHasTime = true;
			}
			if(StringUtil.stringContainListSubstring(syntaxTreeArray[2], NLPConfig.timeKeyword)){
				// if due date has time
				dueDateHasTime = true;
			}
		}else if(group.getDates().size() == 1){
			// only start time or due time
			syntaxTree = group.getSyntaxTree().toStringTree();
			if(StringUtil.stringContainListSubstring(syntaxTree, NLPConfig.timeKeyword)){
				// if has time
				startDateHasTime = true;
			}
			startDate = group.getDates().get(0);
		}
		
		// build DateTime for startDate & dueDate
		if (startDate != null){
			startDateTime = new DateTime(startDate, startDateHasTime);
		}
		if (dueDate != null){
			dueDateTime = new DateTime(dueDate, dueDateHasTime);
		}
		dateTimeList.add(startDateTime);
		dateTimeList.add(dueDateTime);
		return dateTimeList;
	}
	
	
	/**
	 * Look for DateGroup by using Natty
	 * Add all the DateGroups found from a given string into a list
	 * @param msgToDetectDate the given string
	 * @return the list of DateGroups
	 */
	protected static List<DateGroup> getDateGroups(String msgToDetectDate){
		// escape all the Natty holiday cases
		msgToDetectDate = StringUtil.stringDeleteListSubstring(msgToDetectDate, NLPConfig.nattyHoliday);
		
		// call Natty to find date/time
		List<DateGroup> groups = parser.parse(msgToDetectDate);
		String tempTextToDetect = ""; // used to store the previous step's text
		
		// escape wrong date time parse by Natty
		// skip when the date text is an integer
		// or a word in the filterOut list
		while (groups.size() > 0 && (StringUtil.isInteger(groups.get(0).getText())
				|| Arrays.asList(NLPConfig.filterOut).contains(groups.get(0).getText()))){
			
			String currentTextToDetect = groups.get(0).getText();
			LogUtil.Log(TAG, "Text to detect data/time: "+currentTextToDetect);
			if (currentTextToDetect.equals(tempTextToDetect)){
				// if the new date/time text found is the same as the previous step
				groups.clear();
				break;
			}
			// record the current text to prevent infinite loop
			tempTextToDetect = currentTextToDetect;
			msgToDetectDate = msgToDetectDate.replace(groups.get(0).getText(), "");
			if(msgToDetectDate.equals("")){
				// nothing found in the text
				groups.clear();
				break;
			}
			// call Natty to look for DateGrup
			groups = parser.parse(msgToDetectDate);
		}
		
		// escape when the first integer of the date/time is a part of the description
		// if the first char is an integer, and there is no space before it
		// the is to prevent Natty get number text which is part of the description
		if (groups.size() > 0){
			String groupText = groups.get(0).getText();
			LogUtil.Log(TAG, "Group text: "+groupText);
			if( StringUtil.isInteger(groupText.charAt(0)+"") 
					&& msgToDetectDate.indexOf(groupText) > 0
					&& msgToDetectDate.charAt(msgToDetectDate.indexOf(groupText)-1) != ' '){
				msgToDetectDate = groups.get(0).getText().substring(1);
				//delete all the subsequent integers
				while (StringUtil.isInteger(msgToDetectDate.charAt(0)+"")){
					msgToDetectDate = msgToDetectDate.substring(1);
				}
				groups = parser.parse(msgToDetectDate);
			}
		}
		
		return groups;
	}

}
