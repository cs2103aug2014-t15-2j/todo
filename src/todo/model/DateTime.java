package todo.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
	private boolean hasTime;
	private Date date;
	
	public DateTime(Date date){
		this.date = date;
		this.hasTime = true;
	}
	
	public DateTime(Date date, boolean hasTime){
		this.date = date;
		this.hasTime = hasTime;
	}
	
	public void setDate(Date date){
		this.date = date;
	}
	
	public void setDate(Date date, boolean hasTime){
		this.date = date;
		this.hasTime = hasTime;
	}
	
	public Date getDate(){
		return this.date;
	}
	
	public boolean hasTime(){
		return this.hasTime;
	}
	

	public String toString(){
		DateFormat mDateFormate;
		if (hasTime){
			mDateFormate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		}else{
			mDateFormate = new SimpleDateFormat("MM/dd/yyyy");
		}
		return mDateFormate.format(this.date);
	}
	
	public DateTime cloneDateTime(DateTime target ) {
		Date clonnedDate= target.getDate();
		DateTime clonnedDateTime = new DateTime(clonnedDate,false);
		if(target.hasTime()){
			clonnedDateTime.setDate(clonnedDate, true);
		}

		return clonnedDateTime;
	}
}
