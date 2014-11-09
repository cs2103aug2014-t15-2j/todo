package todo.nlp;
//@author A0105570N
/**
 * This class configures all the keywords used by NLP
 */
public class NLPConfig {
	// Date/time keywords
	// these key words are wrongly interpreted by Natty
	// so filter them out
	public static final String[] filterOut = {"eve","sun", "mon", "wed", "sat", "fri"};
	public static final String[] nattyHoliday = {
		"April Fool's Day",
		"Black Friday",
		"Christmas Day",
		"Christmas Eve",
		"Columbus Day (US-OPM)",
		"Earth Day",
		"Easter Sunday",
		"Father's Day",
		"Flag Day",
		"Good Friday",
		"Groundhog's Day",
		"Halloween",
		"Independence Day",
		"Kwanzaa",
		"Labor Day",
		"Martin Luther King Jr.'s Day",
		"Memorial Day",
		"Mother's Day",
		"New Year's Day",
		"New Year's Eve",
		"Patriot Day",
		"President's Day",
		"St. Patrick's Day",
		"Tax Day",
		"Thanksgiving Day",
		"US General Election",
		"Valentine's Day",
		"Veteran's Day"};
	public static final String[] preStart = {"from", "on", "at"};
	public static final String[] preDue = {"by", "by:", "before", "due", "in", "due on", "due in", "due by", "due at", "due from"};
	public static final String[] timeKeyword = {"EXPLICIT_TIME", "minute", "hour"};
	public static final String keywordAll = "all";
	
	// update clean attribute command
	public static final String[] updateDeleteStart = {"no start"};
	public static final String[] updateDeleteDue = {"no due"};
	public static final String[] updateDeleteDate = {"no date"};
	public static final String[] updateDeleteLocation = {"no location"};
	public static final String[] updateCleanTag = {"no tag", "clean tag", "clean tags"};
}
