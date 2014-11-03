package todo.ui;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import todo.logic.Logic;
import todo.model.*;

public class GUIcontrol {
	private Logic logic;
	private ItemList allTasks;
	private ArrayList<Item> completedTasks;
	private ArrayList<Item> unCompletedTasks;
	


public GUIcontrol() throws DOMException, ParserConfigurationException, SAXException, IOException, ParseException {
	logic = Logic.getInstanceLogic();
	allTasks = Logic.getItemList();
	allTasks.checkStatus();
	ArrayList<Item> completedTasks = new ArrayList<Item>();
	ArrayList<Item> unCompletedTasks = new ArrayList<Item>(); allTasks.showUncompletedList();
}

public Logic getLogic(){
	return logic;
}

public ItemList getItemList() {
	return allTasks;
}

public ArrayList<Item> getCompletedTasks() {
	completedTasks = allTasks.showCompletedList();
	return completedTasks;
}

public ArrayList<Item> getUnCompletedTasks(){
	unCompletedTasks = allTasks.showUncompletedList();
	return unCompletedTasks;
	
}

}