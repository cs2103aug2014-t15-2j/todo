package todo.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.util.CommandType;
import todo.logic.Logic;
import todo.util.StringUtil;

public class GUI extends JPanel implements ActionListener{
	
	private Logic logic;
	protected JTextField textField;
    public JTextArea textArea;
	
	public GUI() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException {
		super(new GridBagLayout());
		
		logic = Logic.getInstanceLogic();

        textField = new JTextField(50);
		textField.setFont(new Font("Verdana", Font.BOLD, 14));
        textField.addActionListener(this);
 
        textArea = new JTextArea(30, 50);
		textArea.setFont(new Font("Verdana", Font.BOLD, 16));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
 
        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
 
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);
        
        textArea.setText(logic.getListString());
	}
	
	public void actionPerformed(ActionEvent evt) {
        String userInput = textField.getText();
        
        textField.setText("");
 
        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());

		try {
			logic.executeCommand(userInput);
		} catch (ParserConfigurationException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		textArea.setText(logic.getListString());
    }

}
