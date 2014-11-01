package todo.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MyGUI {
	
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

        		//frame.setSize(500,500);
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
        	pane.add(createItemPane(Color.LIGHT_GRAY), gbc);
        	gbc.gridy++;
        }
        
        pane.add(createItemPane(Color.LIGHT_GRAY), gbc); 

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
	public JPanel createItemPane(Color color) {
        JPanel pane = new JPanel(){

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(40, 40);
            }

        };
        pane.setBackground(color);
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
		label = new JLabel("Current Time: 00:00 ");
		pane.add(label);
		
		return pane;
	}
	
	// This method defines the message panel
	public JPanel createMessagePane() {
		JPanel pane = new JPanel(new BorderLayout());
		label = new JLabel("Returned Message ");
		pane.add(label);
		
		return pane;
		
	}
	
}
