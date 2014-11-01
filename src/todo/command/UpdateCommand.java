package todo.command;

import java.util.ArrayList;

import todo.model.DateTime;
import todo.model.Item;

public class UpdateCommand implements Command{
	private static String UPDATE_SUCCESSFUL = "Updated.";
	
	private Item item;
	private String description;
	private DateTime start;
	private DateTime due;
	private String location;
	private ArrayList<String> tagList;
	
	@Override
	public String execute() {
		return UPDATE_SUCCESSFUL;
		
	}

	public void setItem(Item item){
		this.item = item;
	}
	
	public void setDesctiption(String description){
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
