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
	private boolean isActive = true;
	
	//Constructors
	//Default Constructor
	public Item(){
		setItemID(lastItemId);	
		itemQty++;
	}
	
	public Item(String description, DateTime startDateTime){
		setDescription(description);
		setStartDateTime(startDateTime);
		setItemID(getLastItemId());
		itemQty++;
	}
	
	public Item(String description, DateTime startDateTime, DateTime dueDateTime, String location, int priority, ArrayList<String> tags){
		setDescription(description);
		setStartDateTime(startDateTime);
		setDueDateTime(dueDateTime);
		setLocation(location);
		setPriority(priority);
		setTags(tags);
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
	
	public boolean getIsActive(){
		return this.isActive;
	}
	
	//Modifiers
	public static void setItemQty(int itemQty){
		Item.itemQty = itemQty;
	}
	
	public static void setLastItemID(int lastItemID){
		Item.lastItemId = lastItemID;
	}
	
	public void setItemID (int lastItemId){
		itemId = lastItemId+1;
		lastItemId++;
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
	
	public void setDone(){
		this.isActive = false;
	}
	
	public void setUndone(){
		this.isActive = true;
	}
	
	//Display
	public void displayTagList() {
		for(int i = 0; i < tags.size(); i++) {
			System.out.println(tags.get(i));
		}
	}
}
