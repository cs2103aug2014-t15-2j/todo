package todo.nlp;

public class NLPConfig {
	public static final String[] filterOut = {"eve"};
	public static final String[] preStart = {"from", "on", "at"};
	public static final String[] preDue = {"by", "before", "due", "in"};
	public static final String[] timeKeyword = {"EXPLICIT_TIME", "minute", "hour"};
}
