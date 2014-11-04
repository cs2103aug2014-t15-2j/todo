package todo.model;

import static org.junit.Assert.*;

import java.time.format.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Test;

public class DateTimeTest {

	@Test
	public final void test() {
        
       LocalDateTime dateTime = LocalDateTime.now();
       System.out.println(dateTime);
       //default format
       System.out.println("Default format of LocalDateTime="+dateTime);
       //specific format
       System.out.println(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")));
       //System.out.println(dateTime.format(DateTimeFormatter.BASIC_ISO_DATE));
        
       Instant timestamp = Instant.now();
       //default format
       //System.out.println("Default format of Instant="+timestamp);
       
      String a = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")).replace('T', ' ');
      System.out.println(a);

       LocalDateTime dt = LocalDateTime.parse(a,
               DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
       System.out.println("Default format after parsing = "+dt);;
       
	}

}
