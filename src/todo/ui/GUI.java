package todo.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.library.StringUtil;
import todo.library.Command.CommandType;
import todo.logic.Logic;

public class GUI extends JPanel implements ActionListener{
	
	private Logic logic;
	protected JTextField textField;
    public JTextArea textArea;
	
	public GUI() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException {
		super(new GridBagLayout());
		
		logic = Logic.getInstanceLogic();
		
        textField = new JTextField(50);
        textField.addActionListener(this);
 
        textArea = new JTextArea(30, 50);
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

        CommandType mCommandType = logic.getCommandType(StringUtil.getFirstWord(userInput));
		
		if (mCommandType != CommandType.EXIT){
			try {
				logic.executeCommand(mCommandType, userInput);
			} catch (ParserConfigurationException | TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		textArea.setText(logic.getListString());
    }

}
