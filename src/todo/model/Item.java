package todo.model;

public class Item {
	private String description;
	private DateTime startDateTime;
	
	public Item(){
		
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public void setStartDateTime(DateTime startDateTime){
		this.startDateTime = startDateTime;
	}
	
	
	public String getDescription(){
		return this.description;
	}
	
	public DateTime getStartDateTime(){
		return this.startDateTime;
	}
}
