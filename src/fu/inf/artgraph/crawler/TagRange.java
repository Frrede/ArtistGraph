package fu.inf.artgraph.crawler;

import fu.inf.artgraph.utils.Range;

/**
 * Verknüpft eine Range mit einer Kennzeichnung.
 */
public class TagRange extends Range {
	
	private char tag;
	
	public TagRange(char tag, int min, int max) {
		super(min, max);
		this.tag = tag;
	}
	
	public TagRange(char tag, int max) {
		super(max);
		this.tag = tag;
	}
	
	public char getTag() {
		return tag;
	}
	
}
