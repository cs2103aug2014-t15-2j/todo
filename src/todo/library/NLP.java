package todo.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import todo.model.DateTime;
import todo.model.Item;

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
		
		String msgToDetectDate = StringUtil.removeQuoted(msg);
		groups = parser.parse(msgToDetectDate);
		
		
		// escape wrong date time parse by Natty
		// skip when the date text is an integer
		// or a word in the filterOut list
		while (groups.size() > 0 && (StringUtil.isInteger(groups.get(0).getText())
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
		if(groups.size() > 0 && StringUtil.isInteger(groups.get(0).getText().charAt(0)+"") 
				&& msgToDetectDate.charAt(msgToDetectDate.indexOf(groups.get(0).getText())-1) != ' '){
			msgToDetectDate = groups.get(0).getText().substring(1);
			//delete all the subsequent integers
			while (StringUtil.isInteger(msgToDetectDate.charAt(0)+"")){
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
				if(StringUtil.stringContainSub(syntaxTreeArray[1], timeKeyword)){
					// if start date has time
					startDateHasTime = true;
				}
				if(StringUtil.stringContainSub(syntaxTreeArray[2], timeKeyword)){
					// if due date has time
					dueDateHasTime = true;
				}
			}else if(group.getDates().size() == 1){
				// only start time or due time
				syntaxTree = group.getSyntaxTree().toStringTree();
				if(StringUtil.stringContainSub(syntaxTree, timeKeyword)){
					// if has time
					startDateHasTime = true;
				}
				startDate = group.getDates().get(0);
			}
			
			// delete preposition before date
			String wordBeforeDate = StringUtil.getWordBeforeSubstring(msg,group.getText());
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
		
		// build DateTime for startDate & dueDate
		if (startDate != null){
			startDateTime = new DateTime(startDate, startDateHasTime);
		}
		if (dueDate != null){
			dueDateTime = new DateTime(dueDate, dueDateHasTime);
		}
		return new Item(msg, startDateTime, dueDateTime, location, 1, tagList);
	}
	

}
