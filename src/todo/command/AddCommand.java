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

public class AddCommand implements Command{
	private static String ADD_SUCCESSFUL = "New item added.";
	private static String DESCRIPTION_EMPTY = "Item description is empty, please consider using quotation marks.";
	
	private String description;
	private DateTime start;
	private DateTime due;
	private String location;
	private ArrayList<String> tagList;
	private Logic logic;
	private String statusMessage = " ";
	
	public AddCommand() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException{
		logic = Logic.getInstanceLogic();
		
	}

	@Override
	public String execute() {
		String statusMessage = " ";
		if (description.equals("")){
			statusMessage= DESCRIPTION_EMPTY;
			logic.setSystemMessage(statusMessage);
			return statusMessage;
			
		}
		if(this.start != null && this.due != null){
			if(DateTime.isInValidDate(this.start.getDate(),this.due.getDate())){
			statusMessage= "Due Date is before Start Date";
			logic.setSystemMessage(statusMessage);
			return statusMessage;
			}
		}
		
		Item newItem = new Item(description, start, due, location, tagList);
		Logic.getItemList().add(newItem);
		statusMessage = ADD_SUCCESSFUL;
		
		logic.setSystemMessage(statusMessage);
		return statusMessage;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public void setStart(DateTime start){
		this.start = start;
	}

	public void setDue(DateTime due){
		this.due = due;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	
	public void setTagList(ArrayList<String> tagList){
		this.tagList = tagList;
	}
	
}
