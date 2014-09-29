package todo.library;

public class StringUtil {

	/**
	 * This method check a string contains any string in a string array
	 * @param str the string to be checked
	 * @param list a string array
	 * @return boolean
	 */
	public static boolean stringContainSub(String str, String[] list){
		for (int i = 0; i < list.length; i++){
			if (str.contains(list[i])){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Delete all the content quoted in a string
	 * @param str original string
	 * @return cleaned string
	 */
	public static String removeQuoted(String str){
		//System.out.println("current str: "+str);
		if(str.contains("\"") && str.replaceAll("[^\"]", "").length() % 2 == 0) {
			//there is quotation, and the number of them is even
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
			return removeQuoted(str).trim().replaceAll(" +", " ");
		}else{
			return str.trim().replaceAll(" +", " ");
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
		while (idx > 0 && str.charAt(idx) != ' '){
			result = str.charAt(idx) + result;
			idx--;
		}
		return result;
	}
	
	public static boolean isFullQuote(String str){
		if (str.length() > 1){
			if (str.charAt(0) == '\"' && str.charAt(str.length()-1) == '\"'){
				int count = 0;
				for(int i = 0; i < str.length(); i++){
					if(str.charAt(i) == '\"'){
						count++;
					}
				}
				if (count == 2){
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
			toDelete = "@(";
			int idx = str.indexOf("@(");
			
			// if there is an escape character
			if (str.charAt(idx-1) == '\\'){
				return getBracketLocation(str.substring(idx+1, str.length()));
			}
			idx++;
			while(idx+1<str.length() && str.charAt(idx+1) != (')')){
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

}
