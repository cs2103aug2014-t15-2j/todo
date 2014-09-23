package todo.model;

public class Item {
	private static int itemQty =0;
	private int itemId = 0;
	private int lastItemId =0;
	private int priority =0;
	private String description;
	private String startDateTime; //CHANGE TYPE TO STRING FOR NOW. FOR TESTING PURPOSE. REVERT TO DATETIME CLASS LATER
	private String dueDateTime;   //CHANGE TYPE TO STRING FOR NOW. FOR TESTING PURPOSE. REVERT TO DATETIME CLASS LATER
	private String location; 
	
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
	
	//Modifiers
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
	
}
