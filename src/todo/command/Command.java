package todo.command;

import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

public interface Command {
	public String execute() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException;
}
