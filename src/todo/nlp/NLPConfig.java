package todo.nlp;

/**
 * This class configs all the keywords used by NLP
 * @author siwei
 *
 */
public class NLPConfig {
	// Date/time keywords
	public static final String[] filterOut = {"eve","sun", "mon", "wed", "sat", "fri"};
	public static final String[] preStart = {"from", "on", "at"};
	public static final String[] preDue = {"by", "before", "due", "in"};
	public static final String[] timeKeyword = {"EXPLICIT_TIME", "minute", "hour"};
	public static final String keywordAll = "all";
	
	// update clean attribute command
	public static final String[] updateDeleteStart = {"no start"};
	public static final String[] updateDeleteDue = {"no due"};
	public static final String[] updateDeleteDate = {"no date"};
	public static final String[] updateDeleteLocation = {"no location"};
	public static final String[] updateCleanTag = {"no tag", "clean tag", "clean tags"};
}
