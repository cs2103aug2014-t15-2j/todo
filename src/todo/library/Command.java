package todo.library;

public class Command {
	public static enum CommandType {
		// four basic CRUD operations
		CREATE, READ, UPDATE, DELETE, CLEAR, INVALID, EXIT
	};

	/**
	 * This method takes in user input and determine the command type
	 * 
	 * @param entire line of user input
	 * @return predefined CommandType enum
	 */
	public static CommandType determineCommandType(String commandString) {

		switch (commandString.toLowerCase()) {
		// ----------Possible cases of 'create'-----------
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
			
		// ----------Possible cases of 'update'-----------
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
			
		// ----------Possible cases of 'delete'-----------
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
		// ---------Possible cases of 'clear'-----------
		case "clear" :
			return CommandType.CLEAR;
		case "empty" :
			return CommandType.CLEAR;
			
		// ----------Possible cases of 'exit'-----------
		case "exit":
			return CommandType.EXIT;
		case "quit":
			return CommandType.EXIT;
		case "close":
			return CommandType.EXIT;
				
		// ----------otherwise, 'invalid'-----------
		default:
			return CommandType.INVALID;
		}
	}

}
