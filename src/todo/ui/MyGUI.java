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
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.model.Item;

//import todo.ui.GUIcontrol;

public class MyGUI extends JFrame implements ActionListener {
	
	public GUIcontrol guiControl = null;
	protected JTextField textField;
	public JLabel messageLabel;
	public JScrollPane scrollPane;
	public JPanel mainPane;
	public String userInput;
	public ArrayList<Item> dynamicList = new ArrayList<Item>();
	
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
                
               
               /* 
                JPanel itemPanel = createOverallItemPane();
                scrollPane = new JScrollPane(itemPanel);
                (gridbag).setConstraints(scrollPane, gbc); */
              
				try {
					guiControl = new GUIcontrol();
				} catch (DOMException | ParserConfigurationException
						| SAXException | IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					dynamicList = guiControl.getItemList().getAllItems();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                gbc.gridy++;	
                scrollPane =  new JScrollPane(createMainItemPane()){
                	@Override
                	public Dimension getPreferredSize(){
        				return new Dimension(400, 400);
        			}
                	
                	 @Override
                     public Dimension getMinimumSize() {
                     	return new Dimension(400, 400);
                     }
                };
                scrollPane.setBackground(Color.WHITE);
                
                frame.add(scrollPane, gbc);
               // scrollPane.addActionListener();
                
                         
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
	public JPanel createMainItemPane(){
		  mainPane = new JPanel(new GridBagLayout());
		  GridBagConstraints gbc = new GridBagConstraints();
          gbc.insets = new Insets(4, 4, 4, 4);
          gbc.anchor = GridBagConstraints.NORTH;
          gbc.gridx = 0;
          gbc.weightx = 1.0;
          //gbc.weighty = 1; 
          gbc.gridy = 0;
          gbc.fill = GridBagConstraints.HORIZONTAL; 
          gbc.gridwidth = GridBagConstraints.REMAINDER;
          
          for(int i = 0; i <dynamicList.size(); i++){
              mainPane.add(createItemPane(i), gbc);            
          	  gbc.gridy++; 
          	  //gbc.anchor = GridBagConstraints.FIRST_LINE_START;
          }
          gbc.weighty = 1;
          gbc.fill = GridBagConstraints.BOTH;
          JPanel emptyPanel = new JPanel();
          
          mainPane.add(emptyPanel, gbc);
          

        
      // For testing:  Border mainBorder = BorderFactory.createLineBorder(Color.BLUE, 2);
      //  mainPane.setBorder(mainBorder);
        
        //mainPane.setBackground(Color.WHITE);
       /* scrollPane = new JScrollPane(mainPane){
        	@Override
        	public Dimension getPreferredSize(){
				return new Dimension(400, 400);
			}
        	
        	 @Override
             public Dimension getMinimumSize() {
             	return new Dimension(400, 400);
             }
        };  */
		
		return mainPane;
	}
	
	// This method defines individual item panel
	public JPanel createItemPane(int index) {
		if(index<dynamicList.size()){
        @SuppressWarnings("serial")
		JPanel pane = new JPanel(){

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(50, 50);
            }
            
            @Override
            public Dimension getMinimumSize(){
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
        
        // Displaying item index
        int displayIndex = index + 1;
        JPanel indexPane = new JPanel();
        indexPane.setLayout(new BorderLayout());
        JLabel indexLabel = new JLabel("   " + displayIndex + " ");
        indexLabel.setFont(new Font("Verdana", Font.BOLD, 13));
        indexPane.add(indexLabel, BorderLayout.WEST);
        
        // Displaying item status
        JCheckBox checkBox = new JCheckBox();
        
        if(dynamicList.get(index).getStatus()){
        	checkBox.setSelected(true);
        }else{
        	checkBox.setSelected(false);
        }
        
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

        if(index<dynamicList.size()){
        	String startDateTime;
        	String dueDateTime;
        	if(dynamicList.get(index).getStartDateTime() == null){
        		startDateTime = " -";
        	}else{
        		startDateTime = dynamicList.get(index).getStartDateTime().toString();
        	}

        	if(dynamicList.get(index).getDueDateTime() == null){
        		dueDateTime = " -";
        	}else{
        		dueDateTime = dynamicList.get(index).getDueDateTime().toString();
        	}
        	
        	
        dateTimeTextArea.setText(" Start: " + startDateTime
        	    + "\n" + "\n" 
        		+ " Due : " + dueDateTime);
        dateTimeTextArea.setEditable(false);
        }
        Border dateTimeBorder = BorderFactory.createLineBorder(Color.ORANGE, 1);
        dateTimeTextArea.setBorder(dateTimeBorder);
        pane.add(dateTimeTextArea, BorderLayout.EAST);
        
        // Defines description TextArea
        
        String displayDescription = "";
        displayDescription = dynamicList.get(index).getDescription();
        String displayLocation = "";
        if(dynamicList.get(index).getLocation().equals(null)){
        }else{
        	displayLocation = "Location: " + dynamicList.get(index).getLocation();
        }
        String displayTags = "";
        if(dynamicList.get(index).getTags().equals(null)){
        }else{
        	displayTags = dynamicList.get(index).getTags().toString();
        }
        
        JTextArea descriTextArea = new JTextArea(20, 20);
        descriTextArea.setFont(new Font("Verdana", Font.PLAIN, 12));
        descriTextArea.setText(" " + displayDescription
        		+ "\n" + " " + displayLocation
        		+ "\n" + " " + displayTags);
        descriTextArea.setEditable(false);
        
        Border descriBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
        descriTextArea.setBorder(descriBorder);
        pane.add(descriTextArea, BorderLayout.CENTER);
		
        pane.setBackground(Color.WHITE);
        return pane;
		}else{
			JPanel pane = new JPanel();
        return pane;
		}
    }
	
	// This method defines text field panel
	public JPanel createTextFieldPane() {
		JPanel pane = new JPanel();
        textField.addActionListener(this);
	    pane.add(textField);
	
	    return pane;
	}
	
	// This method defines the current time panel
	public JPanel createTimePane() {
		JPanel pane = new JPanel(new BorderLayout());
		JLabel label = new JLabel("Current Time: " + LocalDateTime.now().toString());
		pane.add(label);
		pane.setBackground(Color.WHITE);
		
		return pane;
	}
	
	// This method defines the message panel
	public JPanel createMessagePane() {
		JPanel pane = new JPanel(new BorderLayout());
		messageLabel = new JLabel();
		pane.add(messageLabel);
	    pane.setBackground(Color.WHITE);
		
		return pane;
		
	}

	@SuppressWarnings("serial")
	public void actionPerformed(ActionEvent evt) {
		
		userInput = textField.getText();
		
		textField.setText("");
		
		try {
			//guiControl.sendToLogic(userInput);
			dynamicList = guiControl.sendToGUI(userInput);
			
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	//	SwingUtilities.updateComponentTreeUI(MyGUI.this);

           mainPane.removeAll();
           mainPane.revalidate();
           mainPane.repaint();
           
           GridBagConstraints gbc = new GridBagConstraints();
           gbc.insets = new Insets(4, 4, 4, 4);
           gbc.anchor = GridBagConstraints.NORTH;
           gbc.gridx = 0;
           gbc.weightx = 1.0;
           gbc.gridy = 0;
           //gbc.weighty = 1.0;  
           gbc.fill = GridBagConstraints.HORIZONTAL;           
           
           for(int i = 0; i < guiControl.getItemList().size(); i++){
               mainPane.add(createItemPane(i), gbc);            
           	   gbc.gridy++; 
           }
           gbc.weighty = 1;
           gbc.fill = GridBagConstraints.BOTH;
           mainPane.add(new JPanel(), gbc);
           
           messageLabel.setText(guiControl.getSystemMessageControl());
           
           mainPane.revalidate();

	//	this.setVisible(false);
		
	//	scrollPane.getViewport().invalidate();

		
	//	scrollPane.getViewport().repaint();
		
	//	this.setVisible(false);
	//	new MyGUI().setVisible(true);

	}
}
