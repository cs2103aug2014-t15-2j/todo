package todo.nlp;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.command.AddCommand;
import todo.command.UpdateCommand;
import todo.model.DateTime;
import todo.model.Item;
import todo.model.Message;
import todo.util.LogUtil;
import todo.util.StringUtil;

import com.joestelmach.natty.DateGroup;

public class NLP {
	private static NLP NLPSingleton = null;
	private static String TAG = "NLP";

	private NLP(){
	}
	
	/**
	 * Singleton method for getting a NLP instance
	 * @return NLP instance
	 */
	public static NLP getInstance(){
		if(NLPSingleton == null){
			NLPSingleton = new NLP();
		}
		return NLPSingleton;
	}
	
	/**
	 *  Add Parser
	 *  extract date/time, location, tags, and description information from user input
	 * @param msg: the content part of an add command, contains description, 
	 * 				date/time (optional), location (optional), tags (optional)
	 * @return a new item created
	 * @throws Exception 
	 */
	public AddCommand addParser(String msg){		
		ArrayList<String> tagList;
		List<DateTime> dateTimeList;
		String location;
		
		LogUtil.Log(TAG, "Start NLP add parser");
		// Create a message object to go through info extraction process
		Message message = new Message(msg);
		
		// step1 correct date format
		message.correctDateFormat();
		// step2 find possible date time
		dateTimeList = NLPUtil.extractDateTime(message);
		// step3 find location
		location = NLPUtil.extractLocation(message);
		// step4 find tags
		tagList = NLPUtil.extractTags(message);
		// step5 delete escape characters
		message.deleteEscapeCharaster();
		// step6 delete whole sentence quotation marks
		message.deleteFullQuote();
		
		message.trim();

		AddCommand addCommand = new AddCommand();
		addCommand.setDesctiption(message.getText());
		addCommand.setStart(dateTimeList.get(0));
		addCommand.setDue(dateTimeList.get(1));
		addCommand.setLocation(location);
		addCommand.setTagList(tagList);
		
		return addCommand;
	}
	
	/**
	 * Update Parser
	 * @param item
	 * @param msg
	 * @return
	 */
	public UpdateCommand updateParser(Item item, String msg){
		LogUtil.Log(TAG, "Start NLP update parser");
		UpdateCommand updateCommand = new UpdateCommand();
		updateCommand.setItem(item);
		
		
		/*
		if (StringUtil.isFullQuote(msg)){
			// update description
			item.setDescription(StringUtil.removeFullQuote(msg));
		}else if (msg.charAt(0) == '@'){
			// update location
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
			// update date/time
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
		*/
		return updateCommand;
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
