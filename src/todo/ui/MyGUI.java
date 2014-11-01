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
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MyGUI {
	
	protected JTextField textField;
	public JLabel label;
	
	// Main method that creates the GUI
	public static void main(String[] args){
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
                frame.setLayout(new GridBagLayout());
                

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(4, 4, 4, 4);
                gbc.gridx = 0;
                gbc.weightx = 1;
                gbc.gridy = 0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                
                frame.add(createTimePane(), gbc);
                gbc.gridy++;
                
                frame.add(createItemPane(Color.ORANGE), gbc);
                gbc.gridy++;
                frame.add(createItemPane(Color.LIGHT_GRAY), gbc);
                gbc.gridy++;
                frame.add(createItemPane(Color.ORANGE), gbc);
                gbc.gridy++;
                frame.add(createItemPane(Color.LIGHT_GRAY), gbc);
                gbc.gridy++;
                frame.add(createItemPane(Color.ORANGE), gbc);
                gbc.gridy++;
                frame.add(createItemPane(Color.LIGHT_GRAY), gbc);
                gbc.gridy++;
                frame.add(createItemPane(Color.ORANGE), gbc);
                
                textField = new JTextField(50);
        		textField.setFont(new Font("Verdana", Font.BOLD, 14));
        		
        		gbc.gridy++;
        		frame.add(createTextFieldPane(), gbc);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
	
	// This method defines individual item panel
	public JPanel createItemPane(Color color) {
        JPanel pane = new JPanel(){

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(50, 50);
            }
            
            @Override
            public Dimension getMinimumSize() {
            	return new Dimension(50, 50);
            }

        };
        pane.setBackground(color);
        return pane;
    }
	
	// This method defines text field panel
	public JPanel createTextFieldPane(){
		JPanel pane = new JPanel(new BorderLayout());
	    pane.add(textField);
	
	    return pane;
	}
	
	// This method defines the current time panel
	public JPanel createTimePane(){
		JPanel pane = new JPanel(new BorderLayout());
		label = new JLabel("Current Time: 00:00 ");
		pane.add(label);
		
		return pane;
	}
	
}
