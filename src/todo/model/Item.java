package todo.model;

import java.util.ArrayList;

public class Item {
	private static int itemQty = 0;
	private int itemId = 0;
	private static int lastItemId = 0;
	private int priority = 1;
	private String description;
	private DateTime startDateTime = null;
	private DateTime dueDateTime = null;
	private String location = "";
	private ArrayList<String> tags = new ArrayList<String>();
	private boolean isCompleted = false;
	
	//Constructors
	
	//Default Constructor
	public Item(){
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	} 
	
	/**
	 * This method is a constructor that creates an item object with only description
	 * @param description
	 */
	public Item(String description) {
		setDescription(description);
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	}
	
	//Constructor with description and start time.
	
	/**
	 * This method is a constructor that creates an item object with only description and startDateTime
	 * @param description
	 * @param startDateTime
	 */
	public Item(String description, DateTime startDateTime){
		setDescription(description);
		setStartDateTime(startDateTime);
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	}
	
	/**
	 *This method is a constructor that creates an item object with only description, priority and tags
	 * @param description
	 * @param priority
	 * @param tags
	 */
	public Item(String description, int priority, ArrayList<String> tags){
		setDescription(description);
		setPriority(priority);
		setTags(tags);
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	}
	
	/**
	 * This method is a constructor that creates an item object with start,due,priority,tags
	 * @param description
	 * @param startDateTime
	 * @param dueDateTime
	 * @param priority
	 * @param tags
	 */
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
	
	/**
	 * This method is a constructor that creates an item object with description location,priority,tags
	 * @param description
	 * @param location
	 * @param priority
	 * @param tags
	 */
	public Item(String description, String location, int priority, ArrayList<String> tags){
		setDescription(description);
		setLocation(location);
		setPriority(priority);
		setTags(tags);
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	}
	
	/**
	 * This method is a constructor that creates an item object with all attributes
	 * @param description
	 * @param startDateTime
	 * @param dueDateTime
	 * @param location
	 * @param priority
	 * @param tags
	 */
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
		assert itemQty >=0;
	}
	
	public static void setLastItemID(int lastItemID){
		Item.lastItemId = lastItemID;
		assert lastItemID >0;
	}
	
	public void setItemID (int lastItemId){
		itemId = lastItemId+1;
		assert itemId>0;
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
		assert priority >= 0;
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
		result = result.concat("");
		result = result.concat(description);
		
		if(startDateTime != null) {
			result = result.concat(" |Start: ");
			result = result.concat(getStartDateTime().toString());
		}
		
		if(dueDateTime != null) {
			result = result.concat(" |Due: ");
			result = result.concat(getDueDateTime().toString());
		}
		
		if (!location.equals("")){
			result = result.concat(" |Location: ");
			result = result.concat(getLocation());
		}
		
		if (tags != null) {
			if(!tags.isEmpty()){
			result = result.concat(" |Tags: ");
			result = result.concat(getTags().toString());
			}
		}
		
		result = result.concat(" |Priority: ");
		result = result.concat(Integer.toString(getPriority()));
		
		result = result.concat(" |Status: ");
		if(getStatus()){
			result = result.concat("Completed");
		}
		if(!getStatus()){
			result = result.concat("Uncompleted");
		}
		return result;
		
	}
}
