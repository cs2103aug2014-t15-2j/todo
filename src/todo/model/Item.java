package todo.model;

import java.util.ArrayList;

public class Item {
	private static int itemQty = 0;
	private int itemId = 0;
	private static int lastItemId = 0;
	private int priority = 0;
	private String description;
	private DateTime startDateTime = null;
	private DateTime dueDateTime = null;
	private String location = null;
	private ArrayList<String> tags = new ArrayList<String>();
	private boolean isCompleted = false;
	
	//Constructors
	
	//Default Constructor
	public Item(){
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	}
	
	//Constructor with only description 
	public Item(String description) {
		setDescription(description);
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	}
	
	//Constructor with description and start time.
	public Item(String description, DateTime startDateTime){
		setDescription(description);
		setStartDateTime(startDateTime);
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	}
	
	//Constructor with priority and tags
	public Item(String description, int priority, ArrayList<String> tags){
		setDescription(description);
		setPriority(priority);
		setTags(tags);
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	}
	//Constructor with start,due,priority,tags
	public Item(String description, DateTime startDateTime, DateTime dueDateTime, int priority, ArrayList<String> tags){
		setDescription(description);
		setStartDateTime(startDateTime);
		setDueDateTime(dueDateTime);
		setPriority(priority);
		setTags(tags);
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	}
	
	//Constructor with tags, priority, location
	public Item(String description, String location, int priority, ArrayList<String> tags){
		setDescription(description);
		setLocation(location);
		setPriority(priority);
		setTags(tags);
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	}
	
	//Constructor with all attributes
	public Item(String description, DateTime startDateTime, DateTime dueDateTime, String location, int priority, ArrayList<String> tags){
		setDescription(description);
		setStartDateTime(startDateTime);
		setDueDateTime(dueDateTime);
		setLocation(location);
		setPriority(priority);
		setTags(tags);
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	}
	
	//Accessors
	public static int getItemQty() {
		return itemQty;
	}
	
	public int getItemId() {
		return itemId;
	}
	
	public static int getLastItemId() {
		return lastItemId;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public DateTime getStartDateTime(){
		return this.startDateTime;
	}
	
	public DateTime getDueDateTime() {
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
	
	public boolean getStatus(){
		return this.isCompleted;
	}
	
	//Modifiers
	public static void setItemQty(int itemQty){
		Item.itemQty = itemQty;
	}
	
	public static void setItemQtyAfterDeletion(){
		Item.itemQty = itemQty--;
	}
	
	public static void setLastItemID(int lastItemID){
		Item.lastItemId = lastItemID;
	}
	
	public void setItemID (int lastItemId){
		itemId = lastItemId+1;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public void setStartDateTime(DateTime startDateTime){
		this.startDateTime = startDateTime;
	}
	
	public void setDueDateTime(DateTime dueDateTime){
		this.dueDateTime = dueDateTime;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	
	public void setPriority(int newPriority) {
		this.priority = newPriority;
	}
	
	public void setTags(ArrayList<String> tagList){
		this.tags = tagList;
	}
	
	public void setStatusDone(){
		this.isCompleted = true;
	}
	
	public void setStatusUndone(){
		this.isCompleted = false;
	}
	
	//Display item
	public String toString() {
		String result = "";
		result = result.concat("Description: ");
		result = result.concat(description);
		
		if(startDateTime != null) {
			result = result.concat(" Start: ");
			result = result.concat(getStartDateTime().toString());
		}
		
		if(dueDateTime != null) {
			result = result.concat(" Due: ");
			result = result.concat(getDueDateTime().toString());
		}
		
		if (location != null){
			result = result.concat(" Location: ");
			result = result.concat(getLocation());
		}
		
		if (tags != null) {
			if(!tags.isEmpty()){
			result = result.concat(" Tags: ");
			result = result.concat(getTags().toString());
			}
		}
		
		result = result.concat(" Priority: ");
		result = result.concat(Integer.toString(getPriority()));
		
		result = result.concat(" Status: ");
		if(getStatus()){
			result = result.concat("Completed");
		}
		if(!getStatus()){
			result = result.concat("Uncompleted");
		}
		return result;
		
	}
}
