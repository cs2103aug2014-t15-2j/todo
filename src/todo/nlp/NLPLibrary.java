package todo.nlp;

import java.util.HashMap;

/**
 * This class is used by NLP general parser.
 * Match a command that cannot be determined with a valid command
 * @author siwei
 *
 */
public class NLPLibrary {
	
	private static HashMap<String, String> library = new HashMap<String, String>();
	private static NLPLibrary NLPLibrarySingpleton;
	
	private NLPLibrary(){
	}
	
	public static NLPLibrary getInstance(){
		if(NLPLibrarySingpleton == null){
			NLPLibrarySingpleton = new NLPLibrary();
			initLibrary();
		}
		return NLPLibrarySingpleton;
	}
	
	private static void initLibrary(){
		library.put("just did everything", "done all");
		library.put("kill the whole shit", "delete all");
		library.put("wtf did i do", "undo");
		library.put("go back", "undo");
	}
	
	public String parse(String str){
		String value = library.get(str);
		if (value != null) {
		    return library.get(str);
		}
		// if cannot find any match in library, then return the original string
		return str;
	}
}
