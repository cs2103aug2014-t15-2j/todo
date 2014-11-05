package todo.model;

import todo.util.StringUtil;

public class Message {
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
	
	public void correctDateFormat(){
		StringUtil.correctDateFormat(text);
	}
	
	public String withoutQuotation(){
		return StringUtil.removeQuoted(text);
	}
	
	public String getWordBeforeSubstring(String substring){
		return StringUtil.getWordBeforeSubstring(text,substring);
	}
	
	public String getTwoWordsBeforeSubstring(String substring){
		return StringUtil.getTwoWordsBeforeSubstring(text,substring);
	}
	
	public void deleteEscapeCharaster(){
		text = text.replaceAll("\\\\#", "#");
		text = text.replaceAll("\\\\@", "@");
	}
	
	public void deleteFullQuote(){
		text = StringUtil.trimString(StringUtil.removeFullQuote(text));
	}
	
	public boolean isEmpty(){
		return text.equals("");
	}
	
	public void trim(){
		text = StringUtil.trimString(text);
	}
	
	public void deleteSubstring(String str){
		text = text.replace(str, "");
	}
	
	// change the first letter of the text to upper case
	public void formatText(){
		if(text.length()>0){
			text = Character.toUpperCase(text.charAt(0)) + text.substring(1); 
		}
	}
}
