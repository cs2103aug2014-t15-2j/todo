package todo.command;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.logic.Logic;
import todo.model.DateTime;
import todo.model.Item;
import todo.util.StringUtil;

//@author A0105570N
public class AddCommand implements Command {
	public static String ADD_SUCCESSFUL = "New item added.";
	public static String DESCRIPTION_EMPTY = "Item description is empty, please consider using quotation marks.";
	public static String INVALID_START_DUE = "Due Date is before Start Date";

	private String description;
	private DateTime start;
	private DateTime due;
	private String location;
	private ArrayList<String> tagList;
	private String statusMessage;


	public AddCommand() {

	}

	@Override
	public String execute() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException {
		Logic logic = Logic.getInstanceLogic();
		
		// empty message check
		if (description.equals(StringUtil.EMPTY_STRING)) {
			statusMessage = DESCRIPTION_EMPTY;
			return statusMessage;
		}

		// start date/time & due date/time validation
		if (this.start != null && this.due != null) {
			if (DateTime.isInValidDate(this.start.getDate(), this.due.getDate())) {
				statusMessage = INVALID_START_DUE;
				return statusMessage;
			}
		}

		// create a new item
		Item newItem = new Item(description, start, due, location, tagList);
		Logic.getItemList().add(newItem);
		statusMessage = ADD_SUCCESSFUL;
		logic.setSystemMessage(statusMessage);
		return statusMessage;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStart(DateTime start) {
		this.start = start;
	}

	public void setDue(DateTime due) {
		this.due = due;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setTagList(ArrayList<String> tagList) {
		this.tagList = tagList;
	}

}
