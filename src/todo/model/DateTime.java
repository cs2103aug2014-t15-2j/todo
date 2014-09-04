package todo.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTime {
	private boolean hasTime;
	private Calendar calendar;
	
	public DateTime(){
	}
	
	public DateTime(Calendar calendar){
		this.calendar = calendar;
	}
	
	public DateTime(int year, int month, int dayOfMonth){
		this.calendar = new GregorianCalendar(year, month, dayOfMonth);
		this.hasTime = false;
	}
	
	public DateTime(int year, int month, int dayOfMonth, int hourOfDay, int minute){
		this.calendar = new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute);
		this.hasTime = true;
	}
	
	public void setDate(){
		
	}
	
	public String toString(){
		String result;
		SimpleDateFormat sdf;
		if (hasTime){
			sdf = new SimpleDateFormat("yyyy MMM dd HH:mm");
			
		}else{
			sdf = new SimpleDateFormat("yyyy MMM dd");
		}
		result = sdf.format(calendar.getTime());
		return result;
	}
}
