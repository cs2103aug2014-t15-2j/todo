package todo.nlp;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.model.Item;
import todo.util.StringUtil;

public class NLPTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void addParserTest() {
		// correct date format -> remove quoted content -> get first word before substring
		// -> get bracket location -> remove full quotation
		// step1 remove quoted content & correct date format & get date groups
		// step2 find possible date time
		// step3 find long location
		// step4 find out one word location and all the tags
		// step5 delete all the escape characters
		// step6 if the whole sentence is quoted, then delete the quotation marks
		NLP nlp = NLP.getInstance();
		String input = "Finish homework by next monday";
		Item add = nlp.addParser(input);
		assertEquals(add.getDescription(), "Finish homework");
	}
	
	@Test
	public void indexParserTest() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException {
		// is integer -> trim string
		NLP nlp = NLP.getInstance();
		String input = "1,3,5,3-6,2";
		ArrayList<Integer> indexes = nlp.batchIndexParser(input);
		int[] result = convertIntegers(indexes);
		int[] correct = new int[]{1,2,3,4,5,6};
		assertArrayEquals(result, correct);
	}
	
	@Test
	public void StringUtilTest(){
		// remove quoted content
		String toRemoveQuoted = "Watch movie \"The Day After Tomorrow\"";
		assertEquals(StringUtil.removeQuoted(toRemoveQuoted), "Watch movie");
		
		// is integer
		assertTrue(StringUtil.isInteger("3"));
		assertFalse(StringUtil.isInteger("a"));
		
		// get the word before the substring
		String toFindString = "Finish the project by next monday";
		String sub = "next monday";
		assertEquals(StringUtil.getWordBeforeSubstring(toFindString, sub), "by");
		
		// is full quote
		String quote1 = "\"This is string is fully quoted\"";
		String quote2 = "This \"string\" is not fully quoted";
		String quote3 = "There is no quotation mark in this string";
		assertTrue(StringUtil.isFullQuote(quote1));
		assertFalse(StringUtil.isFullQuote(quote2));
		assertFalse(StringUtil.isFullQuote(quote3));
		
		// remove full quote
		String strToBeQuoted = "A test string";
		String strQuoted = "\""+strToBeQuoted+"\"";
		assertEquals(StringUtil.removeFullQuote(strQuoted), strToBeQuoted);
		
		// find bracket location
		String strWithLocation = "Meet Mark @(Central Park)";
		assertEquals(StringUtil.getBracketLocation(strWithLocation), "@(Central Park)");
		
		// get first word
		String strWithCommand = "add do exercise";
		assertEquals(StringUtil.getFirstWord(strWithCommand), "add");
		
		// correct date format, from dd/mm to mm/dd
		assertEquals(StringUtil.correctDateFormat("30/12"), "12/30");
		assertEquals(StringUtil.correctDateFormat("5/12"), "5/12");
		assertEquals(StringUtil.correctDateFormat("25/7/14"), "7/25/14");
	}
	
	/**
	 *  For index Parser Test
	 * @param integers
	 * @return
	 */
	public static int[] convertIntegers(List<Integer> integers)
	{
	    int[] ret = new int[integers.size()];
	    for (int i=0; i < ret.length; i++)
	    {
	        ret[i] = integers.get(i).intValue();
	    }
	    return ret;
	}
}
