package todo.library;

public class Command {
	enum CommandType {
		// four basic CRUD operations
		CREATE, READ, UPDATE, DELETE, INVALID
	};

	/**
	 * This method takes in user input and determine the command type
	 * 
	 * @param entire line of user input
	 * @return predefined CommandType enum
	 */
	public static CommandType determineCommandType(String userInput) {
		// check if string is null
		if (userInput == null) {
			throw new Error("userInput string cannot be null!");
		}

		String commandTypeString = getFirstWord(userInput);

		switch (commandTypeString.toLowerCase()) {
		// ----------Possible cases of 'create'-----------
		case "create":
			return CommandType.CREATE;
		case "c":
			return CommandType.CREATE;
		case "add":
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
			
		// ----------Possible cases of 'update'-----------
		case "update":
			return CommandType.UPDATE;
		case "u":
			return CommandType.UPDATE;
		case "edit":
			return CommandType.UPDATE;
		case "modify":
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
			
		// ----------otherwise, 'invalid'-----------
		default:
			return CommandType.INVALID;
		}
	}

	/**
	 * This method executes whichever commandType it receives accordingly
	 * @param commandType
	 */
	public static void executeCommand(CommandType commandType) {
		switch (commandType) {
		case CREATE:
			break;
		case READ:
			break;
		case UPDATE:
			break;
		case DELETE:
			break;
		default:
			break;
		}
	}

	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}
}
