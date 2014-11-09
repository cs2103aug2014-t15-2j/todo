package todo.model;

import todo.util.StringUtil;

//@author A0105570N
/**
 * A Message instance contains a text string
 * There are various methods that can do modification
 * to the string
 *
 */
public class Message {
	public static String LOCATION_SYMBOL = "@";
	public static String LOCATION_SYMBOL_WITH_ESCAPE = "\\\\@";
	public static String TAG_SYMBOL = "#";
	public static String TAG_SYMBOL_WITH_ESCAPE = "\\\\#";
	
	private String text;
	
	public Message(String message){
		this.text = message;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String newText){
		this.text = newText;
	}
	
	// correct the date format if any wrong date format found
	public void correctDateFormat(){
		text = StringUtil.correctDateFormat(text);
	}
	
	// return the text without any quoted content
	public String withoutQuotation(){
		return StringUtil.removeQuoted(text);
	}
	
	// return the word before given string in the text
	public String getWordBeforeSubstring(String substring){
		return StringUtil.getWordBeforeSubstring(text,substring);
	}
	
	// return two words before the given string in the text
	public String getTwoWordsBeforeSubstring(String substring){
		return StringUtil.getTwoWordsBeforeSubstring(text,substring);
	}
	
	// delete all escape characters in the text
	public void deleteEscapeCharaster(){
		text = text.replaceAll(TAG_SYMBOL_WITH_ESCAPE, LOCATION_SYMBOL);
		text = text.replaceAll(LOCATION_SYMBOL_WITH_ESCAPE, TAG_SYMBOL);
	}
	
	// if the whole text is quoted, then delete the quotation marks
	public void deleteFullQuote(){
		text = StringUtil.trimString(StringUtil.removeFullQuote(text));
	}
	
	public boolean isEmpty(){
		return text.equals(StringUtil.EMPTY_STRING);
	}
	
	public void trim(){
		text = StringUtil.trimString(text);
	}
	
	// delete given string from the text
	public void deleteSubstring(String str){
		text = text.replace(str, StringUtil.EMPTY_STRING);
		trim();
	}
	
	// change the first letter of the text to upper case
	public void formatText(){
		if(text.length()>0){
			text = Character.toUpperCase(text.charAt(0)) + text.substring(1); 
		}
	}
}
