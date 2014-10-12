package todo;

import java.io.IOException;
import java.text.ParseException;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.logic.Data;
import todo.logic.Init;
import todo.ui.GUI;

public class GUIMain {

	public static void main(String arg[]) throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException, TransformerException{
		
		Init.init();
		createAndShowGUI();
	}
	
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TextDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add contents to the window.
        GUI mGUI = new GUI();
        frame.add(mGUI);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        mGUI.textArea.setText(getListString());
    }
	
	// For GUI testing purpose
	public static String getListString(){
		return Data.mItemList.toString();
	}
}
