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
import todo.util.StringUtil;

public class NLPUtil {

	public static Parser parser = new Parser();
	
	
	static String deletePreposition(String msg, String wordBeforeDate, String groupText){
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
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws DOMException 
	 */
	static ArrayList<Integer> readIndexList(String str) throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException{
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
	

	static List<DateTime> getDateTime(DateGroup group){
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
	static List<DateGroup> getDateGroups(String msgToDetectDate){
		List<DateGroup> groups = parser.parse(msgToDetectDate);
		
		// escape wrong date time parse by Natty
		// skip when the date text is an integer
		// or a word in the filterOut list
		while (groups.size() > 0 && (StringUtil.isInteger(groups.get(0).getText())
				|| Arrays.asList(NLPConfig.filterOut).contains(groups.get(0).getText()))){
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
