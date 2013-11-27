package fu.inf.artgraph.crawler;

/**
 * Container um die Javascript-Konfig abzubilden.
 */
public class SPContainer {
	
	private String name;
	private String url;
	private TagRange[] tagRange;
	private SPElements[] spElements;
	
	public SPContainer(String name, String url, TagRange[] tagRange, SPElements[] spElements) {
		this.name = name;
		this.url = url;
		this.tagRange = tagRange;
		this.spElements = spElements;
	}

	public String getName() {
		return name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public TagRange[] getTagRange() {
		return tagRange;
	}
	
	public SPElements[] getSPElements() {
		return spElements;
	}
	
}
