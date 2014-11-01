package todo.command;

import java.util.ArrayList;

import todo.model.DateTime;
import todo.model.Item;

public class UpdateCommand implements Command{
	private static String UPDATE_SUCCESSFUL = "Updated.";
	private static String UPDATE_FAILED = "Update failed.";
	
	private Item item;
	private String description;
	private DateTime start;
	private DateTime due;
	private String location;
	private ArrayList<String> tagList;
	
	private boolean updateStart;
	private boolean updateDue;
	private boolean updateLocation;
	private boolean cleanTag;
	private boolean updated;
	
	public UpdateCommand(){
	}
	
	@Override
	public String execute() {
		
		if(updateStart || start != null){
			item.setStartDateTime(start);
			updated = true;
		}
		if(updateDue || due != null){
			item.setDueDateTime(due);
			updated = true;
		}
		if(updateLocation || location != ""){
			item.setLocation(location);
			updated = true;
		}
		if(description != null){
			item.setDescription(description);
			updated = true;
		}
		if(tagList != null){
			for(String tag: tagList){
				if(item.getTags().contains(tag)){
					item.deleteTaf(tag);
				}else{
					item.addTag(tag);
				}
			}
			updated = true;
		}
		if(cleanTag){
			item.setTags(new ArrayList<String>());
			updated = true;
		}
		
		if (updated){
			return UPDATE_SUCCESSFUL;
		}else{
			return UPDATE_FAILED;
		}
		
	}

	public void setItem(Item item){
		this.item = item;
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
	
	public void setUpdateStart(){
		this.updateStart = true;
	}
	
	public void setUpdateDue(){
		this.updateDue = true;
	}
	
	public void setUpdateLocation(){
		this.updateLocation = true;
	}
	
	public void setCleanTag(){
		this.cleanTag = true;
	}
}
