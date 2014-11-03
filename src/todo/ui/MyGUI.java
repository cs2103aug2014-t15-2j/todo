package todo.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import todo.logic.Logic;

public class MyGUI {
	
	private Logic logic;
	protected JTextField textField;
	public JLabel label;
	public JScrollPane scrollPane;
	
	// Main method that creates the GUI
	public static void main(String[] args) {
		new MyGUI();
	}
	
	// This methods defines the overall frame
	public MyGUI() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
                
                logic = GUIcontrol.getLogic();

                JFrame frame = new JFrame("JustDidIt");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                GridBagLayout gridbag = new GridBagLayout();
                
                frame.setLayout(gridbag);
                
                frame.getContentPane().setBackground(Color.WHITE);
               

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(4, 4, 4, 4);
                gbc.gridx = 0;
                gbc.weightx = 1;
                gbc.gridy = 0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                
                frame.add(createTimePane(), gbc);
                
                gbc.gridy++;
               /* 
                JPanel itemPanel = createOverallItemPane();
                scrollPane = new JScrollPane(itemPanel);
                (gridbag).setConstraints(scrollPane, gbc); */
                
                frame.add(createScrollableItemPane(), gbc);
                         
                gbc.gridy++;
                frame.add(createMessagePane(), gbc);
                
                textField = new JTextField(50);
        		textField.setFont(new Font("Verdana", Font.BOLD, 14));
        		
        		gbc.gridy++;
        		frame.add(createTextFieldPane(), gbc);

        		//frame.setSize(600,550);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
	
	// This method add Scroll bar panel for item panels
	public JScrollPane createScrollableItemPane(){
		JPanel pane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        for(int i=0; i<9; i++){
        	pane.add(createItemPane(), gbc);
        	gbc.gridy++;
        }
        
        pane.add(createItemPane(), gbc); 

        scrollPane = new JScrollPane(pane){
        	@Override
        	public Dimension getPreferredSize(){
				return new Dimension(400, 400);
			}
        	
        	 @Override
             public Dimension getMinimumSize() {
             	return new Dimension(400, 400);
             }
        };
		
		return scrollPane;
	}
	
	// This method defines individual item panel
	public JPanel createItemPane() {
        JPanel pane = new JPanel(){

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(50, 50);
            }

        };
        pane.setLayout(new BorderLayout());
        
        // Defines index Label  (The old one) 
        
        /*JLabel indexLabel = new JLabel("   1   ");
        indexLabel.setFont(new Font("Verdana", Font.BOLD, 13));
        Border indexBorder = BorderFactory.createLineBorder(Color.ORANGE, 5);
        indexLabel.setBorder(indexBorder);
        pane.add(indexLabel, BorderLayout.WEST); */
        
        JPanel indexPane = new JPanel();
        indexPane.setLayout(new BorderLayout());
        JLabel indexLabel = new JLabel("   1 ");
        indexLabel.setFont(new Font("Verdana", Font.BOLD, 13));
        indexPane.add(indexLabel, BorderLayout.WEST);
        
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(true);
        indexPane.add(checkBox, BorderLayout.CENTER);
        checkBox.setEnabled(false);
        
        Border indexBorder = BorderFactory.createLineBorder(Color.ORANGE, 5);
        indexPane.setBorder(indexBorder);
        
        indexPane.setBackground(Color.WHITE);
        checkBox.setBackground(Color.WHITE);
        
        pane.add(indexPane, BorderLayout.WEST);
              
        // Defines DateTime TextArea
        JTextArea dateTimeTextArea = new JTextArea(18, 18);
        dateTimeTextArea.setFont(new Font("Verdana", Font.PLAIN, 12));
        dateTimeTextArea.setText(" Start: 11/03/2014 08:00 am " + "\n" + "\n" + " Due:  11/07/2014 23:59 pm");
        
        Border dateTimeBorder = BorderFactory.createLineBorder(Color.ORANGE, 1);
        dateTimeTextArea.setBorder(dateTimeBorder);
        pane.add(dateTimeTextArea, BorderLayout.EAST);
        
        // Defines description TextArea
        JTextArea descriTextArea = new JTextArea(20, 20);
        descriTextArea.setFont(new Font("Verdana", Font.PLAIN, 12));
        descriTextArea.setText(" Attend project meeting of CS2103 after school. "
                               + "\n" + " Location: UTown " + "\n" + " #School    #Work");
        
        Border descriBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
        descriTextArea.setBorder(descriBorder);
        pane.add(descriTextArea, BorderLayout.CENTER);
        
        pane.setBackground(Color.WHITE);
        return pane;
    }
	
	// This method defines text field panel
	public JPanel createTextFieldPane() {
		JPanel pane = new JPanel();
	    pane.add(textField);
	
	    return pane;
	}
	
	// This method defines the current time panel
	public JPanel createTimePane() {
		JPanel pane = new JPanel(new BorderLayout());
		label = new JLabel("Current Time: " + LocalDateTime.now().toString());
		pane.add(label);
		pane.setBackground(Color.WHITE);
		
		return pane;
	}
	
	// This method defines the message panel
	public JPanel createMessagePane() {
		JPanel pane = new JPanel(new BorderLayout());
		label = new JLabel("Returned Message ");
		pane.add(label);
	    pane.setBackground(Color.WHITE);
		
		return pane;
		
	}
	
	public void actionPerformed(ActionEvent e){
		
		String userInput = textField.getText();
		textField.setText("");
		
	}
}
