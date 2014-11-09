package todo.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MessageTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String text = "This is a test message";
		String newText = "This is the new message text";
		Message msg = new Message(text);
		
		assertFalse(msg.isEmpty());
		assertEquals(msg.getText(), text);
		
		msg.setText(newText);
		assertEquals(msg.getText(), newText);
		
		// text remove full quotation
		msg.setText("\""+newText+"\"");
		msg.deleteFullQuote();
		assertEquals(msg.getText(), newText);
		
		// test delete substring
		String stringContainSubstring = "this is a string";
		msg.setText(stringContainSubstring);
		msg.deleteSubstring("a");
		assertEquals(msg.getText(), "this is string");
		
		// test format text
		String stringWithoutCapitalization = "a new string";
		msg.setText(stringWithoutCapitalization);
		msg.formatText();
		assertEquals(msg.getText(), "A new string");
		
		// test correct date format
		String dateString = "13/6/2014";
		msg.setText(dateString);
		msg.correctDateFormat();
		assertEquals(msg.getText(), "6/13/2014");
		
		// test without quotation
		String stringWithQuotation = "This \"string\" has quotation";
		msg.setText(stringWithQuotation);
		assertEquals(msg.withoutQuotation(), "This has quotation");
		
		// test get word before string
		String aNormalString = "Finish homeword due by monday";
		msg.setText(aNormalString);
		assertEquals(msg.getWordBeforeSubstring("monday"), "by");
		assertEquals(msg.getTwoWordsBeforeSubstring("monday"), "due by");
	}

}
