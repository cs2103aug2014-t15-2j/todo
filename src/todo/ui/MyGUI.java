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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

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
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.model.Item;

//@author A0115992X

@SuppressWarnings("serial")
public class MyGUI extends JFrame implements ActionListener {
	
	// Layout setting strings
	public static final String NAME_WINDOW = "JustDidIt";
	public static final String FONT = "Verdana";
	
	// Command string
	public static final String COMMAND_SHOW = "show";
	
	// Layout setting parameters
	public static final int PARAM_INSETS = 4;
	public static final int PARAM_GRIDX = 0;
	public static final int PARAM_GRIDY = 0;
	public static final int PARAM_WEIGHTX = 1;
	public static final int PARAM_WEIGHTY = 1;
	public static final int PARAM_DIMENSION_SCROLL = 400;
	public static final int PARAM_DIMENSION_ITEMPANE = 50;
	public static final int PARAM_BORDER = 0;
	public static final int PARAM_FONT_SIZE_INDEX = 13;
	
	// Common parameters
	public static final int PARAM_INIT = 0;
	public static final int PARAM_INCRE = 1;
	

	public GUIcontrol guiControl = null;
	protected JTextField textField;
	public JLabel messageLabel;
	public JScrollPane scrollPane;
	public JPanel mainPane;
	public String userInput;
	public ArrayList<Item> dynamicList = new ArrayList<Item>();	

	public static void main(String[] args) {
		new MyGUI();
	}

	// This constructor defines the overall GUI layout
	public MyGUI() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}         

				JFrame frame = new JFrame(NAME_WINDOW);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      

				GridBagLayout gridbag = new GridBagLayout(); 
				frame.setLayout(gridbag);                
				frame.getContentPane().setBackground(Color.WHITE);

				GridBagConstraints gbc = new GridBagConstraints();
				gbc.insets = new Insets(PARAM_INSETS, PARAM_INSETS, PARAM_INSETS, PARAM_INSETS);
				gbc.gridx = PARAM_GRIDX;
				gbc.weightx = PARAM_WEIGHTX;
				gbc.gridy = PARAM_GRIDY;
				gbc.fill = GridBagConstraints.HORIZONTAL;

				// Link MyGUI to GUIcontrol that can access Logic
				try {
					guiControl = new GUIcontrol();
				} catch (DOMException | ParserConfigurationException
						| SAXException | IOException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					dynamicList = guiControl.sendToGUI(COMMAND_SHOW);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				gbc.gridy++;             
				scrollPane = new JScrollPane(createMainItemPane()) {
					@Override
					public Dimension getPreferredSize() {
						return new Dimension(PARAM_DIMENSION_SCROLL, PARAM_DIMENSION_SCROLL);
					}           	
					@Override
					public Dimension getMinimumSize() {
						return new Dimension(PARAM_DIMENSION_SCROLL, PARAM_DIMENSION_SCROLL);
					}
				};
				scrollPane.setBackground(Color.WHITE);
				frame.add(scrollPane, gbc);

				gbc.gridy++;             
				frame.add(createMessagePane(), gbc);

				gbc.gridy++;
				frame.add(createTextFieldPane(), gbc);    
				
				// Focus the textField when open the GUI
				frame.addWindowListener(new WindowAdapter() {
					public void windowOpened(WindowEvent e) {
						textField.requestFocus();
					}
				});

				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	// This method gathers multiple item panels to a main panel
	public JPanel createMainItemPane() {
		mainPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(PARAM_INSETS, PARAM_INSETS, PARAM_INSETS, PARAM_INSETS);
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridx = PARAM_GRIDX;
		gbc.weightx = PARAM_WEIGHTX;
		gbc.gridy = PARAM_GRIDY;
		gbc.fill = GridBagConstraints.HORIZONTAL; 
		gbc.gridwidth = GridBagConstraints.REMAINDER;

		for(int i = PARAM_INIT; i <dynamicList.size(); i++) {
			mainPane.add(createItemPane(i), gbc);            
			gbc.gridy++; 
		}
		gbc.weighty = PARAM_WEIGHTY ;
		gbc.fill = GridBagConstraints.BOTH;
		JPanel emptyPanel = new JPanel();

		mainPane.add(emptyPanel, gbc);

		return mainPane;
	}

	// This method defines individual item panel
	public JPanel createItemPane(int index) {
		
		assert(index < dynamicList.size());
		
		if(index < dynamicList.size()) {
			JPanel pane = new JPanel(){

				@Override
				public Dimension getPreferredSize() {
					return new Dimension(PARAM_DIMENSION_ITEMPANE, PARAM_DIMENSION_ITEMPANE);
				}

				@Override
				public Dimension getMinimumSize() {
					return new Dimension(PARAM_DIMENSION_ITEMPANE, PARAM_DIMENSION_ITEMPANE);
				}

			};
			pane.setLayout(new BorderLayout());

			// Displaying item index
			int displayIndex = index + PARAM_INCRE;
			JPanel indexPane = new JPanel();
			indexPane.setLayout(new BorderLayout());
			JLabel indexLabel = new JLabel("   " + displayIndex + " ");
			indexLabel.setFont(new Font(FONT, Font.BOLD, PARAM_FONT_SIZE_INDEX));
			indexPane.add(indexLabel, BorderLayout.WEST);

			// Displaying item completion status by check boxes
			JCheckBox checkBox = new JCheckBox();
			if(dynamicList.get(index).getStatus()) {
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

			// Defines description TextArea
			String displayDescription = "";
			displayDescription = dynamicList.get(index).getDescription();
			String displayLocation = "";
			if(dynamicList.get(index).getLocation().equals(null)){
			}else{
				if(dynamicList.get(index).getLocation().equals("")){
					displayLocation = "Location:  -" ;
				}else{
					displayLocation = "Location: " + dynamicList.get(index).getLocation();
				}
			}
			String displayTags = "";
			if(dynamicList.get(index).getTags().equals(null)){
			}else{
				if(dynamicList.get(index).getTags().size() != 0){
					displayTags = dynamicList.get(index).getTags().toString();
				}
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
			
			// Defines DateTime TextArea
			JTextArea dateTimeTextArea = new JTextArea(18, 18);
			dateTimeTextArea.setFont(new Font(FONT, Font.PLAIN, 12));

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

			pane.setBackground(Color.WHITE);
			return pane;
		}else{
			JPanel pane = new JPanel();
			return pane;
		}
	}

	// This method defines the system message panel
	public JPanel createMessagePane() {
		JPanel pane = new JPanel(new BorderLayout());
		messageLabel = new JLabel();
		pane.add(messageLabel);
		pane.setBackground(Color.WHITE);
		pane.setBorder(new EmptyBorder(PARAM_BORDER, PARAM_BORDER, 
				PARAM_BORDER, PARAM_BORDER) );

		return pane;

	}
	
	// This method defines the text field panel
	public JPanel createTextFieldPane() {
		JPanel pane = new JPanel();
		textField = new JTextField(50);
		textField.setFont(new Font(FONT, Font.BOLD, 14));
		textField.setBackground(Color.WHITE);
		textField.addActionListener(this);
		pane.setBackground(Color.WHITE);
		pane.add(textField);

		return pane;
	}

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

		// Update the GUI after each operation
		mainPane.removeAll();
		mainPane.revalidate();
		mainPane.repaint();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(PARAM_INSETS, PARAM_INSETS, PARAM_INSETS, PARAM_INSETS);
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridx = PARAM_GRIDX;
		gbc.weightx = PARAM_WEIGHTX;
		gbc.gridy = PARAM_GRIDY;
		gbc.fill = GridBagConstraints.HORIZONTAL;           

		for(int i = PARAM_INIT; i < dynamicList.size(); i++){
			mainPane.add(createItemPane(i), gbc);            
			gbc.gridy++; 
		}
		gbc.weighty = PARAM_WEIGHTY;
		gbc.fill = GridBagConstraints.BOTH;
		mainPane.add(new JPanel(), gbc);

		messageLabel.setText(guiControl.getSystemMessageControl());

		mainPane.revalidate();
	}
}
