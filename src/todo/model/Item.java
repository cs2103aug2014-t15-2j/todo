package todo.model;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * @author SAMSUNG
 *
 */
/**
 * @author SAMSUNG
 *
 */
/**
 * @author SAMSUNG
 *
 */
/**
 * @author SAMSUNG
 *
 */
public class Item {
	
	private static final boolean unImportant = false;
	private static int itemQty = 0;
	private int itemId = 0;
	private static int lastItemId = 0;
	private boolean importance = unImportant;
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
	 * This method is a constructor that creates an item object with the following attributes
	 * @param description
	 * @param importance
	 * @param tags
	 */
	public Item(String description, boolean importantance, ArrayList<String> tags){
		setDescription(description);
		setImportance(importantance);
		setTags(tags);
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	}
	
	/**
	 * This method is a constructor that creates an item object with the following attributes
	 * @param description
	 * @param startDateTime
	 * @param dueDateTime
	 * @param importance
	 * @param tags
	 */
	public Item(String description, DateTime startDateTime, DateTime dueDateTime, boolean importantance, ArrayList<String> tags){
		setDescription(description);
		setStartDateTime(startDateTime);
		setDueDateTime(dueDateTime);
		setImportance(importance);
		setTags(tags);
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	}
	
	/**
	 * This method is a constructor that creates an item object with the following attributes
	 * @param description
	 * @param location
	 * @param importance
	 * @param tags
	 */
	public Item(String description, String location, boolean importance, ArrayList<String> tags){
		setDescription(description);
		setLocation(location);
		setImportance(importance);
		setTags(tags);
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	}
	/**
	 * This method is a constructor that creates an item object with the following attributes
	 * @param description
	 * @param startDateTime
	 * @param dueDateTime
	 * @param location
	 * @param tags
	 */
	public Item(String description, DateTime startDateTime, DateTime dueDateTime, String location, ArrayList<String> tags){
		setDescription(description);
		setStartDateTime(startDateTime);
		setDueDateTime(dueDateTime);
		setLocation(location);
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
	 * @param importance
	 * @param tags
	 */
	public Item(String description, DateTime startDateTime, DateTime dueDateTime, String location, boolean importance, ArrayList<String> tags){
		setDescription(description);
		setStartDateTime(startDateTime);
		setDueDateTime(dueDateTime);
		setLocation(location);
		setImportance(importance);
		setTags(tags);
		setItemID(getLastItemId());
		itemQty++;
		lastItemId++;
	}
	/**
	 * This method is a constructor that takes in an item object and clones it.
	 * @param itemId
	 * @param description
	 * @param startDateTime
	 * @param dueDateTime
	 * @param location
	 * @param importance
	 * @param tags
	 * @param status
	 */
	private Item(int itemId , String description, DateTime startDateTime, DateTime dueDateTime, String location, boolean importance, ArrayList<String> tags,boolean status) {
		setDescription(description);
		setStartDateTime(startDateTime);
		setDueDateTime(dueDateTime);
		setLocation(location);
		setImportance(importance);
		setTags(tags);
		setItemID(itemId);
		if(status) {
			setStatusDone();
		}
		if(!status) {
			setStatusUndone();
		}
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
	
	public boolean getImportance() {
		return this.importance;
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
	
	public void setImportance(boolean newImportance) {
		this.importance = newImportance;
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
		
		result = result.concat(" |Status: ");
		if(getStatus()){
			result = result.concat("Completed");
		}
		if(!getStatus()){
			result = result.concat("Uncompleted");
		}
		if(getImportance()){
			result = result.concat("Important");
		}
		return result;
		
	}
	
	//Clone Item - Takes in an item and returns an exact copy of the item without changing the itemQty and item number
	public Item cloneItem() throws ParseException{
		int itemId = this.getItemId();
		String description = this.getDescription();
		
		DateTime startDateTime = null;
		if(this.getStartDateTime() != null){
			startDateTime = this.getStartDateTime().cloneDateTime();
		}

		DateTime dueDateTime = null;
		if(this.getDueDateTime() != null){
			dueDateTime = this.getDueDateTime().cloneDateTime();
		}

		String location = this.getLocation();
		boolean importance = this.getImportance();
		ArrayList<String> tags = cloneTags(this.getTags());
		boolean status = this.getStatus();
		
		Item clonned = new Item (itemId, description, startDateTime, dueDateTime, location, importance, tags, status);
		return clonned;
	} 
	
	 
	private ArrayList<String> cloneTags(ArrayList<String> tags){
		ArrayList<String> clonnedTagList = new ArrayList<String>();
		for (int j = 0; j < tags.size(); j++) {
			String cloneTag = new String(tags.get(j));
			clonnedTagList.add(cloneTag);	
		}
		return clonnedTagList;
	}
	
}
