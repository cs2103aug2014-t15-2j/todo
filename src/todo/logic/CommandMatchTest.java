package todo.logic;

import static org.junit.Assert.*;

import org.junit.Test;

import todo.util.CommandType;

//@author A0098155W
/**
* Unit test for CommandMatch class.
*/
public class CommandMatchTest {

	@Test
	public void test() {
		CommandMatch commandMatch = new CommandMatch();
		
		//Boundary test case, null
		assertEquals(CommandType.INVALID, commandMatch.determineCommandType(null));
		
		//Boundary test case, non-null string all small letters
		assertEquals(CommandType.CREATE, commandMatch.determineCommandType("add"));
		
		//Boundary test case, non-null string mix of capital and small letters
		assertEquals(CommandType.READ, commandMatch.determineCommandType("lISt"));
		
		//Boundary test case, positive integer string
		assertEquals(CommandType.INVALID, commandMatch.determineCommandType("65424"));
		
		//Boundary test case, negative integer string
		assertEquals(CommandType.INVALID, commandMatch.determineCommandType("-99"));
		
		
		// Tests for all possible return enums
		assertEquals(CommandType.CREATE, commandMatch.determineCommandType("add"));
		assertEquals(CommandType.READ, commandMatch.determineCommandType("view"));
		assertEquals(CommandType.UPDATE, commandMatch.determineCommandType("modify"));
		assertEquals(CommandType.DELETE, commandMatch.determineCommandType("del"));
		assertEquals(CommandType.DONE, commandMatch.determineCommandType("done"));
		assertEquals(CommandType.UNDONE, commandMatch.determineCommandType("undone"));
		assertEquals(CommandType.CLEAR, commandMatch.determineCommandType("clear"));
		assertEquals(CommandType.INVALID, commandMatch.determineCommandType("1234"));
		assertEquals(CommandType.UNDO, commandMatch.determineCommandType("undo"));
		assertEquals(CommandType.REDO, commandMatch.determineCommandType("redo"));
		assertEquals(CommandType.EXIT, commandMatch.determineCommandType("exit"));
	}

}
