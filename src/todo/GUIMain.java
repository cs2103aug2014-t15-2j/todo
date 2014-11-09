package todo;

import java.io.IOException;
import java.text.ParseException;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.ui.GUI;

//@author A0105570N-unused
public class GUIMain {

	public static void main(String arg[]) throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException, TransformerException{
		createAndShowGUI();
	}
	
	private static void createAndShowGUI() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException {
        //Create and set up the window.
        JFrame frame = new JFrame("JustDidIt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add contents to the window.
        GUI mGUI = new GUI();
        frame.add(mGUI);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
