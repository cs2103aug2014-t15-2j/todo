package todo.ui;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.logic.Logic;
import todo.model.*;
//@author A0111082Y
public class GUIcontrol {
	private Logic logic;
	private ItemList allTasks;
	private ArrayList<Item> completedTasks;
	private ArrayList<Item> unCompletedTasks;
	String systemMessage = "";
	private static final String MESSAGE_SYSTEM = "SystemMessage:";

	public GUIcontrol() throws DOMException, ParserConfigurationException,
			SAXException, IOException, ParseException {
		logic = Logic.getInstanceLogic();
		allTasks = Logic.getItemList();
		allTasks.checkStatus();
		ArrayList<Item> completedTasks = new ArrayList<Item>();
		ArrayList<Item> unCompletedTasks = new ArrayList<Item>();
		
		allTasks.showUncompletedList();
	}

	public ItemList getItemList() {
		return allTasks;
	}

	public ArrayList<Item> getCompletedTasks() {
		completedTasks = allTasks.showCompletedList();
		return completedTasks;
	}

	public ArrayList<Item> getUnCompletedTasks() {
		unCompletedTasks = allTasks.showUncompletedList();
		return unCompletedTasks;

	}
	public ArrayList<Item> sendToGUI(String userInput) throws Exception {
		ArrayList<Item> itemsForGUI = new ArrayList<Item>();
		itemsForGUI = logic.getItemsforGUI(logic.executeCommand(userInput));
		return itemsForGUI;
	}
	public String getSystemMessageControl () {
		systemMessage= logic.getSystemMessage();
		return this.systemMessage;
	}

	public String sendToLogic(String userInput) throws Exception {
		String systemMessage = MESSAGE_SYSTEM;
		try {
			logic.executeCommand(userInput);
		} catch (ParserConfigurationException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		systemMessage = logic.getSystemMessage();
		return systemMessage;
	}
}