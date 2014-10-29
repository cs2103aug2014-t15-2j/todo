import static org.junit.Assert.*;

import java.util.Date;
import java.lang.String;

import org.junit.Test;

import todo.nlp.NLP;
import todo.model.DateTime;
import todo.model.Item;


public class NLPtest {

	String addbasicdescription = "something";
	String test2 = "do homework by 9pm 17/10/2014";
	
	Date now = new Date();
	Date currentDate1 = new Date(17/10/2014);
	DateTime basicday1 = new DateTime(currentDate1);
	
	Item addbasic = new Item (addbasicdescription);
	Item addbasictime = new Item ("do homework",basicday1);
	@Test
	public void testaddbasic() throws Exception {
		assertEquals(addbasic.toString(),NLP.getInstance().addParser(addbasicdescription).toString());
		
	}
	
	@Test
	public void testaddwithtime() throws Exception {
		assertEquals(addbasictime,NLP.getInstance().addParser(test2));
	}

}
