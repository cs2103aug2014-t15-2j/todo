package todo;

import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import todo.ui.MyGUI;


public class guiLogic extends JFrame {
//implements KeyListener,
//WindowStateListener, WindowListener {
	private JTextField inputField;
	private JPanel backgroudPane;
	private JTextArea commandFeedback;

	//Launch the main program
	public static void main(String[] args) {
		new MyGUI();
	}
}