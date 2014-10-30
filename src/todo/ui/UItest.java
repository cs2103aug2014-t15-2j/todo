package todo.ui;

import static org.junit.Assert.*;

import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;


public class UItest {
	@Rule
	public StandardOutputStreamLog log = new StandardOutputStreamLog();

	@Rule
	public TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
