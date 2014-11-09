package todo.logic;

import static org.junit.Assert.*;

import org.junit.Test;

import todo.util.CommandType;

public class CommandMatchTest {

	@Test
	public void test() {
		CommandMatch commandMatch = new CommandMatch();
		
		//Boundary test case, null
		assertEquals(CommandType.CREATE, commandMatch.determineCommandType("Add"));
		
		//Boundary test case, non-null
		assertEquals(CommandType.INVALID, commandMatch.determineCommandType(null));
	}

}
