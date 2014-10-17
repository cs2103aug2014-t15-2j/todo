import static org.junit.Assert.*;

import org.junit.Test;

import todo.nlp.NLP;
import todo.model.Item;
import todo.model.ItemList;

public class NLPtest {

	String test1 = "add something";
	Item abc = new Item (test1);
	@Test
	public void testadd() {
		assertEquals(abc,NLP.getInstance().addParser(test1));
	}

}
