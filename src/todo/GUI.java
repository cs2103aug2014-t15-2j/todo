package todo;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import todo.library.StringUtil;
import todo.library.Command.CommandType;

public class GUI extends JPanel implements ActionListener{
	
	protected JTextField textField;
    protected JTextArea textArea;
    private final static String newline = "\n";
	
	public GUI() {
		super(new GridBagLayout());
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
 
	}
	
	public void actionPerformed(ActionEvent evt) {
        String userInput = textField.getText();
        
        textField.setText("");
 
        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());

        CommandType mCommandType = todo.getCommandType(StringUtil.getFirstWord(userInput));
		
		if (mCommandType != CommandType.EXIT){
			try {
				todo.executeCommand(mCommandType, userInput);
			} catch (ParserConfigurationException | TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		textArea.setText(todo.getListString());
    }

}