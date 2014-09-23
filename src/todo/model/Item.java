package todo.model;

import java.util.ArrayList;

public class Item {
	private static int itemQty =0;
	private int itemId = 0;
	private static int lastItemId =0;
	private int priority =0;
	private String description;
	private String startDateTime = null; //CHANGE TYPE TO STRING FOR NOW. FOR TESTING PURPOSE. REVERT TO DATETIME CLASS LATER
	private String dueDateTime = null;   //CHANGE TYPE TO STRING FOR NOW. FOR TESTING PURPOSE. REVERT TO DATETIME CLASS LATER
	private String location = null;
	private ArrayList<String> tags = new ArrayList<String>();
	
	
	//Constructors
	//Default Constructor
	public Item(){
		
		itemQty++;
		setItemID(lastItemId);	
	}
	
	public Item(String description, String startDateTime){
		
		setDescription(description);
		setStartDateTime(startDateTime);
		setItemID(getLastItemId());
		itemQty++;
	}
	//Accessors
	public static int getItemQty() {
		
		return itemQty;
	}
	
	public int getItemId() {
		return itemId;
	}
	
	public int getLastItemId() {
		
		return lastItemId;
	}
	
	public String getDescription(){
		
		return this.description;
	}
	
	public String getStartDateTime(){ //CHANGE TYPE TO STRING FOR NOW. FOR TESTING PURPOSE. REVERT TO DATETIME CLASS LATER
		
		return this.startDateTime;
	}
	
	public String getDueDateTime() { ////CHANGE TYPE TO STRING FOR NOW. FOR TESTING PURPOSE. REVERT TO DATETIME CLASS LATER
		
		return this.dueDateTime;
	}
	
	public String getLocation() {
		
		return this.location;
	}
	
	public int getPriority() {
		
		return this.priority;
	}
	
	public ArrayList<String> getTags() {
		
		return this.tags;
	}
	//Modifiers
	
	//
	public  void setItemID (int lastItemId){
		
		itemId = lastItemId+1;
		lastItemId++;
	}
	public void setDescription(String description){
		
		this.description = description;
	}
	
	public void setStartDateTime(String startDateTime){ //CHANGE TYPE TO STRING FOR NOW. FOR TESTING PURPOSE. REVERT TO DATETIME CLASS LATER
		
		this.startDateTime = startDateTime;
	}
	
	public void setPriority(int newPriority) {
		this.priority = newPriority;
	}
	
	public void setTags(ArrayList<String> tagList){
		this.tags = tagList;
	}
	
	//Display
	public void displayTagList() {
		for(int i = 0; i < tags.size(); i++) {
			System.out.println(tags.get(i));
		}
	}
}
