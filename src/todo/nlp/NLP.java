package todo.nlp;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.logic.Logic;
import todo.model.DateTime;
import todo.model.Item;
import todo.util.LogUtil;
import todo.util.StringUtil;

import com.joestelmach.natty.DateGroup;

public class NLP {
	
	private static NLP NLPSingpleton;
	private static String TAG = "NLP";

	private NLP(){
		
	}
	
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
		
		LogUtil.Log(TAG, "Start NLP add parser");
		
		// add empty start and due date time
		dateTimeList.add(null);
		dateTimeList.add(null);
		
		// step1 remove quoted content & correct date format
		// & get date groups
		msg = StringUtil.correctDateFormat(msg);
		groups = NLPUtil.getDateGroups(StringUtil.removeQuoted(msg));

		
		// step2 find possible date time
		if (groups.size() != 0){
			DateGroup group = groups.get(0);
			String groupText = group.getText();
			LogUtil.Log(TAG, "Detect data/time: "+groupText);
			dateTimeList = NLPUtil.getDateTime(group);
			String wordBeforeDate = StringUtil.getWordBeforeSubstring(msg,groupText);
			if (Arrays.asList(NLPConfig.preDue).contains(wordBeforeDate)){
				// exchange start time and due time
				dateTimeList.add(dateTimeList.remove(0));
			}
			
			msg = NLPUtil.deletePreposition(msg, wordBeforeDate, groupText);
		}else{
			LogUtil.Log(TAG, "No date time detected");
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
		
		if (msg.equals("")){
			LogUtil.Log(TAG, "Item description is empty, please consider using quotation marks");
			return null;
		}
		
		assert msg != "";
		return new Item(msg, dateTimeList.get(0), dateTimeList.get(1), location, 1, tagList);
	}
	
	/**
	 * Update Parser
	 * @param item
	 * @param msg
	 * @return
	 */
	public boolean updateParser(Item item, String msg){
		
		LogUtil.Log(TAG, "Start NLP update parser");
		if (StringUtil.isFullQuote(msg)){
			item.setDescription(StringUtil.removeFullQuote(msg));
		}else if (msg.charAt(0) == '@'){
			item.setLocation(msg.substring(1));
		}else if (StringUtil.getFirstWord(msg).equals(NLPConfig.addTagCommand)){
			// add tags
			String[] strArray = msg.split(" ");
			ArrayList<String>  tagList = item.getTags();
			for(int i = 0; i < strArray.length ; i++){
				if (strArray[i].length() > 1 && strArray[i].charAt(0) == '#'){
					tagList.add(strArray[i].substring(1));
					msg = msg.replace(strArray[i], "");
				}
			}
			item.setTags(tagList);
		}else if (StringUtil.getFirstWord(msg).equals(NLPConfig.deleteTagCommand)){
			// delete tags
			String[] strArray = msg.split(" ");
			ArrayList<String>  tagList = item.getTags();
			for(int i = 0; i < strArray.length ; i++){
				if (strArray[i].length() > 1 && strArray[i].charAt(0) == '#'){
					String toRemove = strArray[i].substring(1);
					for ( int j = 0;  j < tagList.size(); j++){
			            String tempName = tagList.get(j);
			            if(tempName.equals(toRemove)){
			            	tagList.remove(j);
			            }
			        }
					msg = msg.replace(strArray[i], "");
				}
			}
			item.setTags(tagList);
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
		LogUtil.Log(TAG, "Start NLP index parser");
		return NLPUtil.readIndexList(indices);
	}
	
	/**
	 * General Parser
	 * @param input
	 * @return if the input is in library, return standard command, otherwise, return original string
	 */
	public String generalParser(String input){
		LogUtil.Log(TAG, "Start NLP general parser");
		return NLPLibrary.getInstance().parse(input);
	}

}
