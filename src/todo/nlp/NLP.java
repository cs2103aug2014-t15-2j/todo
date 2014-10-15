package todo.nlp;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.library.StringUtil;
import todo.logic.Logic;
import todo.model.DateTime;
import todo.model.Item;

import com.joestelmach.natty.DateGroup;

public class NLP {
	
	private static NLP NLPSingpleton;
	private static String TAG = "NLP";

	public static NLP getInstance(){
		if(NLPSingpleton == null){
			NLPSingpleton = new NLP();
		}
		return NLPSingpleton;
	}
	
	/**
	 *  Add Parser
	 * @param msg
	 * @return
	 */
	public Item addParser(String msg){		
		
		List<DateGroup> groups;
		ArrayList<String> tagList = new ArrayList<String>();
		String[] strArray;
		List<DateTime> dateTimeList = new ArrayList<DateTime>();
		String location = "";
		
		// add empty start and due date time
		dateTimeList.add(null);
		dateTimeList.add(null);
		
		// step1 remove quoted content & get date groups
		groups = NLPUtil.getDateGroups(StringUtil.removeQuoted(msg));
		
		// step2 find possible date time
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
		
		// step3 find long location
		String locationString = StringUtil.getBracketLocation(msg);
		if (locationString.length() > 0){
			location = locationString.substring(2, locationString.length()-1);
			msg = msg.replace(locationString, "");
			msg = StringUtil.trimString(msg);
		}

		// step4 find out one word location and all the tags
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
		
		// step5 delete all the escape characters
		msg = msg.replaceAll("\\\\#", "#");
		msg = msg.replaceAll("\\\\@", "@");
		
		// step6 if the whole sentence is quoted, then delete the quotation marks
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
	
	/**
	 * Update Parser
	 * @param item
	 * @param msg
	 * @return
	 */
	public boolean updateParser(Item item, String msg){
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
	
	/**
	 * Index Parser
	 * @param indices
	 * @return
	 * @throws DOMException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParseException
	 */
	public ArrayList<Integer> batchIndexParser(String indices) throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException{
		return NLPUtil.readIndexList(indices);
	}
	
	/**
	 * General Parser
	 * @param input
	 * @return
	 */
	public String generalParser(String input){
		return input;
	}

}
