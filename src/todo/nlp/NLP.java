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

/**
 * There are four parsers in an instance of NLP:
 * Add Parser, Update Parser, Index Parser, and General parser
 * 
 * @author siwei
 *
 */
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
	 *  ADD PARSER
	 *  extract date/time, location, tags, and description information from user input
	 * @param msg: the content part of an add command, contains description, 
	 * 				date/time (optional), location (optional), tags (optional)
	 * @return add command
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws DOMException 
	 * @throws Exception 
	 */
	public AddCommand addParser(String msg) throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException{		
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
		// step7 trim and format message as description
		message.trim();
		message.formatText();

		AddCommand addCommand = new AddCommand();
		addCommand.setDescription(message.getText());
		addCommand.setStart(dateTimeList.get(0));
		addCommand.setDue(dateTimeList.get(1));
		addCommand.setLocation(location);
		addCommand.setTagList(tagList);
		
		return addCommand;
	}
	
	/**
	 * UPDATE PARSER
	 * @param item : the item to be updated
	 * @param msg : user input, contain following update info
	 * 				fully quoted as description
	 * 				start with @ to update location
	 * 				start with # to update tags
	 * 				contain date/time to update date/time
	 * 				clean command to clean corresponding field
	 * @return update command
	 */
	public UpdateCommand updateParser(Item item, String msg){
		List<DateTime> dateTimeList;
		ArrayList<String> tagList;
		String location;
		
		LogUtil.Log(TAG, "Start NLP update parser");
		UpdateCommand updateCommand = new UpdateCommand();
		updateCommand.setItem(item);
		
		Message message = new Message(msg);
		
		// set update start/due date/time
		message.correctDateFormat();
		dateTimeList = NLPUtil.extractDateTime(message);
		updateCommand.setStart(dateTimeList.get(0));
		updateCommand.setDue(dateTimeList.get(1));
		
		// set update location
		location = NLPUtil.extractLocation(message);
		updateCommand.setLocation(location);
		
		// set update tags
		tagList = NLPUtil.extractTags(message);
		updateCommand.setTagList(tagList);
		
		// set update description
		if (StringUtil.isFullQuote(msg)){
			updateCommand.setDescription(StringUtil.removeFullQuote(msg));
		}
		
		// clean command
		if (Arrays.asList(NLPConfig.updateDeleteStart).contains(msg)){
			// clean start date/time
			updateCommand.setUpdateStart();
		}
		if (Arrays.asList(NLPConfig.updateDeleteDue).contains(msg)){
			// clean due date/time
			updateCommand.setUpdateDue();
		}
		if (Arrays.asList(NLPConfig.updateDeleteDate).contains(msg)){
			// clean start & due date/time
			updateCommand.setUpdateStart();
			updateCommand.setUpdateDue();
		}
		if (Arrays.asList(NLPConfig.updateDeleteLocation).contains(msg)){
			// clean location
			updateCommand.setUpdateLocation();
		}
		if (Arrays.asList(NLPConfig.updateCleanTag).contains(msg)){
			// clean tags
			updateCommand.setCleanTag();
		}
		
		return updateCommand;
	}
	
	/**
	 * INDEX PARSER
	 * index parser convert a string of indices into a list of integer
	 * the final list is sorted and with no duplicates
	 * @param indices the string of indices in natural language
	 * @return a array list of integer
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
	 * GENERAL PARSER
	 * @param input
	 * @return if the input is in library, return standard command, otherwise, return original string
	 */
	public String generalParser(String input){
		LogUtil.Log(TAG, "Start NLP general parser");
		return NLPLibrary.getInstance().parse(input);
	}

}
