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

/**
 * This class contains all the utility methods used by NLP
 * @author siwei
 *
 */
public class NLPUtil {
	private static String TAG = "NLPUtil";
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
			DateGroup group = groups.get(0);
			String groupText = group.getText();
			LogUtil.Log(TAG, "Detect data/time: "+groupText);
			dateTimeList = NLPUtil.getDateTime(group);
			String wordBeforeDate = msg.getWordBeforeSubstring(groupText);
			if (Arrays.asList(NLPConfig.preDue).contains(wordBeforeDate)){
				// exchange start time and due time
				dateTimeList.add(dateTimeList.remove(0));
			}
			msg.setText(NLPUtil.deletePreposition(msg.getText(), wordBeforeDate, groupText));
		}else{
			LogUtil.Log(TAG, "No date time detected");
		}
		return dateTimeList;
	}
	
	/**
	 * Extract location
	 * @param msg
	 * @return location string
	 */
	protected static String extractLocation(Message msg){
		String location = "";
		String locationString = StringUtil.getBracketLocation(msg.getText());
		// look for long location
		if (locationString.length() > 2){
			location = locationString.substring(2, locationString.length()-1);
			msg.deleteSubstring(locationString);
			msg.trim();
			return location;
		}
		msg.trim();
		// look for one-word location
		String[] strArray = msg.getText().split(" ");
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
	
	protected static String deletePreposition(String msg, String wordBeforeDate, String groupText){
		// delete preposition before date
		if (Arrays.asList(NLPConfig.preStart).contains(wordBeforeDate)
				|| Arrays.asList(NLPConfig.preDue).contains(wordBeforeDate)){
			return msg.replace(wordBeforeDate + " " + groupText, "");
		}else{ // no preposition
			return msg.replace(groupText, "");
		}
	}
	
	/**
	 * Read indexes from a string
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
		
		// Matching delimiter in the string. "," -> "/"
		if (str.contains(",")){
			delimiter = ",";
			str = StringUtil.trimString(str);
		}else if (str.contains("/")){
			delimiter = "/";
			str = StringUtil.trimString(str);
		}else{
			delimiter = " ";
		}

		String[] arr = str.split(delimiter);
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
					for (int i = Integer.valueOf(subArr[0]); i < (Integer.valueOf(subArr[1])+1); i++){
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
	

	protected static List<DateTime> getDateTime(DateGroup group){
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
			syntaxTree = group.getSyntaxTree().toStringTree();
			String[] syntaxTreeArray = syntaxTree.split("DATE_TIME ");
			if(StringUtil.stringContainSub(syntaxTreeArray[1], NLPConfig.timeKeyword)){
				// if start date has time
				startDateHasTime = true;
			}
			if(StringUtil.stringContainSub(syntaxTreeArray[2], NLPConfig.timeKeyword)){
				// if due date has time
				dueDateHasTime = true;
			}
		}else if(group.getDates().size() == 1){
			// only start time or due time
			syntaxTree = group.getSyntaxTree().toStringTree();
			if(StringUtil.stringContainSub(syntaxTree, NLPConfig.timeKeyword)){
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
	 * Add all the DateGroups found from a given string into a list
	 * @param msgToDetectDate the given string
	 * @return the list of DateGroups
	 */
	protected static List<DateGroup> getDateGroups(String msgToDetectDate){
		List<DateGroup> groups = parser.parse(msgToDetectDate);
		String tempTextToDetect = "";
		
		// escape wrong date time parse by Natty
		// skip when the date text is an integer
		// or a word in the filterOut list
		while (groups.size() > 0 && (StringUtil.isInteger(groups.get(0).getText())
				|| Arrays.asList(NLPConfig.filterOut).contains(groups.get(0).getText()))){
			String currentTextToDetect = groups.get(0).getText();
			LogUtil.Log(TAG, "Text to detect data/time: "+currentTextToDetect);
			if (currentTextToDetect.equals(tempTextToDetect)){
				groups.clear();
				break;
			}
			// prevent infinite loop
			tempTextToDetect = currentTextToDetect;
			msgToDetectDate = msgToDetectDate.replace(groups.get(0).getText(), "");
			if(msgToDetectDate.equals("")){
				groups.clear();
				break;
			}
			groups = parser.parse(msgToDetectDate);
		}
		
		//escape when the first integer of the date/time is a part of the description
		//if the first char is an integer, and there is no space before it
		if(groups.size() > 0 && StringUtil.isInteger(groups.get(0).getText().charAt(0)+"") 
				&& msgToDetectDate.indexOf(groups.get(0).getText()) > 0
				&& msgToDetectDate.charAt(msgToDetectDate.indexOf(groups.get(0).getText())-1) != ' '){
			msgToDetectDate = groups.get(0).getText().substring(1);
			//delete all the subsequent integers
			while (StringUtil.isInteger(msgToDetectDate.charAt(0)+"")){
				msgToDetectDate = msgToDetectDate.substring(1);
			}
			groups = parser.parse(msgToDetectDate);
		}
		
		return groups;
	}

}
