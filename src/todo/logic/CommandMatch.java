package todo.logic;

import todo.model.CommandType;

//@author A0098155W
/**
 * This class is used by Logic to determine what command type is entered
 * by user.
 */
public class CommandMatch {

	public CommandMatch() {

	}

	/**
	 * Takes in a String and determines the command type it represents.
	 * 
	 * @param String containing command type
	 * @return predefined CommandType enum
	 */
	public CommandType determineCommandType(String commandString) {
		if (commandString == null) {
			return CommandType.INVALID;
		}

		switch (commandString.toLowerCase()) {
		// ---------Possible cases of 'create'----------
		case "create":
			return CommandType.CREATE;
		case "c":
			return CommandType.CREATE;
		case "add":
			return CommandType.CREATE;
		case "a":
			return CommandType.CREATE;
		case "insert":
			return CommandType.CREATE;
		case "+":
			return CommandType.CREATE;

		// ----------Possible cases of 'read'-----------
		case "read":
			return CommandType.READ;
		case "r":
			return CommandType.READ;
		case "ls":
			return CommandType.READ;
		case "list":
			return CommandType.READ;
		case "view":
			return CommandType.READ;
		case "display":
			return CommandType.READ;
		case "show":
			return CommandType.READ;
		case "all":
			return CommandType.READ;

		// ---------Possible cases of 'update'----------
		case "update":
			return CommandType.UPDATE;
		case "u":
			return CommandType.UPDATE;
		case "edit":
			return CommandType.UPDATE;
		case "modify":
			return CommandType.UPDATE;
		case "change":
			return CommandType.UPDATE;

		// ---------Possible cases of 'delete'----------
		case "delete":
			return CommandType.DELETE;
		case "d":
			return CommandType.DELETE;
		case "del":
			return CommandType.DELETE;
		case "remove":
			return CommandType.DELETE;
		case "cancel":
			return CommandType.DELETE;
		case "-":
			return CommandType.DELETE;

		// ---------Possible cases of 'done'------------
		case "done":
			return CommandType.DONE;
		case "finish":
			return CommandType.DONE;
		case "finished":
			return CommandType.DONE;
		case "complete":
			return CommandType.DONE;
		case ">":
			return CommandType.DONE;

		// --------Possible cases of 'undone'------------
		case "undone":
			return CommandType.UNDONE;
		case "<":
			return CommandType.UNDONE;

		// ---------Possible cases of 'clear'-----------
		case "clear":
			return CommandType.CLEAR;
		case "empty":
			return CommandType.CLEAR;

		// ----------Possible cases of 'undo'-----------
		case "undo":
			return CommandType.UNDO;
		case "revert":
			return CommandType.UNDO;

		// ----------Possible cases of 'redo'-----------
		case "redo":
			return CommandType.REDO;

		// ----------Possible cases of 'exit'-----------
		case "exit":
			return CommandType.EXIT;
		case "quit":
			return CommandType.EXIT;
		case "close":
			return CommandType.EXIT;

		// ----------Otherwise, 'invalid'-----------
		default:
			return CommandType.INVALID;
		}
	}

}
