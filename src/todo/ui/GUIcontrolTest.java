package todo.ui;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

public class GUIcontrolTest {

	@Test
	public final void testGUIcontrol() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException {
		GUIcontrol controller = new GUIcontrol();
		
	}

	@Test
	public final void testGetItemList() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException {
		GUIcontrol controller = new GUIcontrol();
		System.out.println(controller.getItemList().toString());
	}

	@Test
	public final void testGetCompletedTasks() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException {
		GUIcontrol controller = new GUIcontrol();
		System.out.println(controller.getCompletedTasks().toString());
	}
	

	@Test
	public final void testGetUnCompletedTasks() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException {
		GUIcontrol controller = new GUIcontrol();
		System.out.println(controller.getUnCompletedTasks().toString());
	
	}

}
