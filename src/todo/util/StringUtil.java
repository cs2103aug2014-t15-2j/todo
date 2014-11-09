package todo.util;

//@author A0105570N
public class StringUtil {

	public static String EMPTY_STRING = "";
	
	/**
	 * This method check a string contains any string in a string array
	 * @param str the string to be checked
	 * @param list a string array
	 * @return boolean
	 */
	public static boolean stringContainListSubstring(String str, String[] list){
		for (int i = 0; i < list.length; i++){
			if (str.contains(list[i])){
				return true;
			}
		}
		return false;
	}
	
	public static String stringDeleteListSubstring(String str, String[] list){
		for (int i = 0; i < list.length; i++){
			String currentSubstring = list[i];
			if (str.contains(currentSubstring)){
				str = str.replaceAll(currentSubstring, "");
			}
		}
		return str;
	}
	
	/**
	 * Delete all the content quoted in a string
	 * E.g. this is "a quoted" string -> this is string
	 * @param str original string
	 * @return cleaned string
	 */
	public static String removeQuoted(String str){
		if(str.contains("\"") && str.replaceAll("[^\"]", "").length() % 2 == 0) {
			// there is quotation, and the number of them is even
			String toDelete = "\"";
			int idx = str.indexOf('"');
			while(idx+1<str.length() && str.charAt(idx+1) != ('\"')){
				toDelete += str.charAt(idx+1);
				idx++;
			}
			if(idx+1<str.length() && str.charAt(idx+1) == '\"'){
				toDelete += "\"";
				str = str.replace(toDelete, "");
			}
			return trimString(removeQuoted(str));
		}else{
			return trimString(str);
		}
	}
	
	/**
	 * Check if a string can be converted into an integer
	 * @param s string
	 * @return boolean
	 */
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
	/**
	 * Get the word before the given substring
	 * @param str original string
	 * @param sub substring
	 * @return the word string before the substring
	 */
	public static String getWordBeforeSubstring(String str, String sub){
		int idx = str.lastIndexOf(sub)-2;
		String result = "";
		// stop when encounter a space
		while (idx >= 0 && str.charAt(idx) != ' '){
			result = str.charAt(idx) + result;
			idx--;
		}
		return result;
	}
	
	/**
	 * Get two words before the given substring
	 * @param str original string
	 * @param sub substring
	 * @return two words before the given substring
	 */
	public static String getTwoWordsBeforeSubstring(String str, String sub){
		int idx = str.lastIndexOf(sub)-2;
		int countSpace = 0;
		String result = "";
		// stop when encounter the second space
		while (idx >= 0 && !(countSpace == 1 && str.charAt(idx) == ' ')){
			if (str.charAt(idx) == ' '){
				countSpace++;
			}
			result = str.charAt(idx) + result;
			idx--;
		}
		return result;
	}
	
	/**
	 * Check if a string is fully quoted
	 * The start and end with quotation marks
	 * And there are only two quotation marks in the string
	 * @param str
	 * @return
	 */
	public static boolean isFullQuote(String str){
		if (str.length() > 1){
			if (str.charAt(0) == '\"' && str.charAt(str.length()-1) == '\"'){
				// start and end with quotation marks
				int count = 0;
				for(int i = 0; i < str.length(); i++){
					// count number of "
					if(str.charAt(i) == '\"'){
						count++;
					}
				}
				if (count == 2){
					// and only two quotation marks in the string
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * if there are only two quotation marks in the string
	 * and one is at the beginning, and the other is at the end
	 * then remove the two quotation marks
	 * @param str the original string
	 * @return string
	 */
	public static String removeFullQuote(String str){
		if (isFullQuote(str)){
			return str.substring(1, str.length()-1);
		}else{
			return str;
		}
	}
	
	/**
	 * get the location within a pair of brackets after @
	 * @param str original string
	 * @return "@(content)"
	 */
	public static String getBracketLocation(String str){
		String toDelete = "";
		if(str.contains("@(")) {
			// there is a long location in the string
			toDelete = "@(";
			int idx = str.indexOf("@(");
			
			// if there is an escape character
			if (idx-1 >=0 && str.charAt(idx-1) == '\\'){
				return getBracketLocation(str.substring(idx+1, str.length()));
			}
			idx++;
			while(idx+1<str.length() && str.charAt(idx+1) != ')'){
				toDelete += str.charAt(idx+1);
				idx++;
			}
			if(idx+1<str.length() && str.charAt(idx+1) == ')'){
				toDelete += ")";
			}else{
				toDelete = "";
			}
		}
		return toDelete;
	}
	
	public static String trimString(String str){
		return str.trim().replaceAll(" +", " ");
	}

	/**
	 * get the first word from a string
	 * if there is only one word in the string, return the word
	 * @param str: the given string : String
	 * @return the frist word : String
	 */
	public static String getFirstWord(String str) {
		String firstWord = str.trim().split("\\s+")[0];
		return firstWord;
	}
	
	/**
	 * Change dd/mm format to mm/dd
	 * @param String with posible dd/mm or dd/mm/yy date
	 * @return String with posible mm/dd or mm/dd/yy date
	 */
	public static String correctDateFormat(String str){
		String[] arr = str.split(" ");
		for(int i = 0; i < arr.length; i++){
			String[] arr2 = arr[i].split("/");
			if (arr2.length == 3
			&& isInteger(arr2[0])
			&& isInteger(arr2[1])
			&& isInteger(arr2[2])){
				if(Integer.valueOf(arr2[0]) > 12){
					str = str.replace(arr[i], arr2[1]+"/"+arr2[0]+"/"+arr2[2]);
				}
			}else if (arr2.length == 2
			&& isInteger(arr2[0])
			&& isInteger(arr2[1])){
				if(Integer.valueOf(arr2[0]) > 12){
					str = str.replace(arr[i], arr2[1]+"/"+arr2[0]);
				}
			}
		}
		return str;
	}

}
