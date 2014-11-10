package todo.logic;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
//@author A0111082Y
public class LogicTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		Logic logic = Logic.getInstanceLogic();
		logic.executeCommand("add project meeting on 1 Nov #meeting");
		String addedString = "project meeting |Start: 2014-11-01 |Tags: [meeting] |Status: Uncompleted";
		String newList = logic.getListString();
		assertEquals(newList.substring(newList.length()-addedString.length()-2).trim(), addedString);
	}

}
