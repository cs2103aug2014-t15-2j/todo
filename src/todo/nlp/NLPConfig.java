package todo.nlp;

public class NLPConfig {
	public static final String[] filterOut = {"eve","sun"};
	public static final String[] preStart = {"from", "on", "at"};
	public static final String[] preDue = {"by", "before", "due", "in"};
	public static final String[] timeKeyword = {"EXPLICIT_TIME", "minute", "hour"};
	public static final String keywordAll = "all";
	
	public static final String addTagCommand = "add";
	public static final String deleteTagCommand = "delete";
	
	public static final String[] updateDeleteStart = {"no start"};
	public static final String[] updateDeleteDue = {"no due"};
	public static final String[] updateDeleteDate = {"no date"};
	public static final String[] updateDeleteLocation = {"no location"};
	public static final String[] updateCleanTag = {"no tag", "clean tag", "clean tags"};
}
