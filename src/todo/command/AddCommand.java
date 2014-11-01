package todo.command;

import java.util.ArrayList;

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
	
	public AddCommand(){
		
	}

	@Override
	public String execute() {
		if (description.equals("")){
			return DESCRIPTION_EMPTY;
		}
		Item newItem = new Item(description, start, due, location, tagList);
		Logic.getItemList().add(newItem);
		return ADD_SUCCESSFUL;
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
