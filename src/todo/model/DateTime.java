package todo.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.*;

//@author A0111082Y
public class DateTime {
	public static final String DATE_WITH_TIME = "MM/dd/yyyy HH:mm:ss";
	public static final String DATE_WITHOUT_TIME = "MM/dd/yyyy";

	private boolean hasTime;
	private LocalDateTime date;

	public DateTime(Date date) {
		this.date = convertDateToLocalDateTime (date);
		this.hasTime = true;
	}

	public DateTime(Date date, boolean hasTime) {
		this.date = convertDateToLocalDateTime (date);
		this.hasTime = hasTime;
	}
	
	public DateTime(LocalDateTime date, boolean hasTime) {
		this.date = date;
		this.hasTime = hasTime;
	}

	public void setDate(Date date) {
		this.date = convertDateToLocalDateTime (date);
	}

	public void setDate(Date date, boolean hasTime) {
		this.date = convertDateToLocalDateTime (date);
		this.hasTime = hasTime;
	}

	public LocalDateTime getDate() {
		return this.date;
	}

	public boolean hasTime() {
		return this.hasTime;
	}
	//This methods converts a dateTime object to a string format
	public String toString() {
		
		String output = this.date.toString().replace("T", " ");
		
		if (hasTime) {
			output = output.substring(0, 16);
		}else{
			output = output.substring(0, 10);
		}
		return output;
	}
	//Format the string for writing into xml file
	
	public String writeString(){
		return this.date.toString().replace("T", " ").substring(0, 16);
	}
	/**
	 * This method clones the object itself
	 * @return an exact cloned copy of itself
	 * @throws ParseException
	 */
	
	// This method provides deep clonning function or the datetime object.
	public DateTime cloneDateTime() throws ParseException {
		
		DateTime clonnedDateTime = null;
		if (this.hasTime()){
			Instant instant = this.date.toInstant(ZoneOffset.of("+08"));
			Date clonnedDate = Date.from(instant);
			clonnedDateTime = new DateTime(clonnedDate,true);
		}
		else {
			Instant instant = this.date.toInstant(ZoneOffset.of("+08"));
			Date clonnedDate = Date.from(instant);
			clonnedDateTime = new DateTime(clonnedDate,false);
			
		}

		return clonnedDateTime;
	}
	
	public LocalDateTime convertDateToLocalDateTime (Date targetDate){
		Instant instant = Instant.ofEpochMilli(targetDate.getTime());
		LocalDateTime formattedDate = LocalDateTime.ofInstant(instant, ZoneOffset.of("+08"));
		
		return formattedDate;
		
	}

	// This method checks if the due date is before the start date
		public static boolean isInValidDate(LocalDateTime startDate,
				LocalDateTime dueDate) {
			boolean invalid;
			if ((startDate != null) && (dueDate != null)) {
				if (dueDate.isBefore(startDate)) {
					invalid = true;
				} else {
					invalid = false;
				}
			} else {
				invalid = true;
			}
			
			return invalid;

	}
}
