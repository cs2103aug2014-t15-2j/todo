package todo.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
	public static final String DATE_WITH_TIME = "MM/dd/yyyy HH:mm:ss";
	public static final String DATE_WITHOUT_TIME = "MM/dd/yyyy";

	private boolean hasTime;
	private Date date;

	public DateTime(Date date) {
		this.date = date;
		this.hasTime = true;
	}

	public DateTime(Date date, boolean hasTime) {
		this.date = date;
		this.hasTime = hasTime;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDate(Date date, boolean hasTime) {
		this.date = date;
		this.hasTime = hasTime;
	}

	public Date getDate() {
		return this.date;
	}

	public boolean hasTime() {
		return this.hasTime;
	}

	public String toString() {
		DateFormat mDateFormate;
		if (hasTime) {
			mDateFormate = new SimpleDateFormat(DATE_WITH_TIME);
		} else {
			mDateFormate = new SimpleDateFormat(DATE_WITHOUT_TIME);
		}
		return mDateFormate.format(this.date);
	}

	/**
	 * This method clones the object itself
	 * @return an exact cloned copy of itself
	 * @throws ParseException
	 */
	public DateTime cloneDateTime() throws ParseException {
		DateFormat dateWithTime = new SimpleDateFormat(DATE_WITH_TIME);
		DateFormat dateWithoutTime = new SimpleDateFormat(DATE_WITHOUT_TIME);

		DateTime clonedDateTime = null;
		Date clonedDate = null;

		if (this.hasTime()) {
			clonedDate = dateWithTime.parse(this.toString());
			clonedDateTime = new DateTime(clonedDate, true);
		} else {
			clonedDate = dateWithoutTime.parse(this.toString());
			clonedDateTime = new DateTime(clonedDate, false);
		}

		return clonedDateTime;
	}
}
