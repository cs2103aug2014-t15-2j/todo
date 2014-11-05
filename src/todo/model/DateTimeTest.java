package todo.model;

import static org.junit.Assert.*;

import java.time.format.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import todo.model.DateTime;

import org.junit.Test;

public class DateTimeTest {

	@Test
	public final void test() {

		LocalDateTime dateTime = LocalDateTime.now();
		// default format
		System.out.println("Default format of LocalDateTime=" + dateTime);
		// specific format
		System.out.println(dateTime.format(DateTimeFormatter
				.ofPattern("d::MMM::uuuu HH::mm::ss")));
		System.out.println(dateTime.format(DateTimeFormatter.BASIC_ISO_DATE));

		Instant timestamp = Instant.now();
		// default format
		System.out.println("Default format of Instant= " + timestamp);

		// Parse examples
		LocalDateTime dt = LocalDateTime.parse("27::Apr::2014 21::39",
				DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm"));
		System.out.println("Default format after parsing = " + dt);
		Date aDate = new Date();
		DateTime abc = new DateTime(aDate);
		//Create a LocalDateTime from date
		LocalDateTime xyz = abc.convertDateToLocalDateTime (aDate);
		
		//Generate default string version of localDateTime
		System.out.println("the localdateTime is "+xyz);
		
		//Get the string of the localdateTime and trims it to remove extra characters
		String xyzString = xyz.toString().replace('T', ' ');
		System.out.println("new string = "+xyzString);
		xyzString=xyzString.substring(0,16);
		System.out.println("new string2 = "+xyzString);
		
		//Create a LocalDateTime from string
		LocalDateTime mno = LocalDateTime.parse(xyzString,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		System.out.println("the localdateTime after conversion is "+mno);
		//
		String yyString = xyzString.substring(0,10);
		System.out.println("yyString = "+yyString);
		LocalDate yy = LocalDate.parse(yyString,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		System.out.println(yy);
	}

}
