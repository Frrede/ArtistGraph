package fu.inf.artgraph.crawler;

/**
 * Container um den Selector aus der Javascript-Konfig abzubilden.
 */
public class SPSelector {
	
	private String selector;
	private Modifier modifier;
	
	public SPSelector(String selector, Modifier modifier) {
		this.selector = selector;
		this.modifier = modifier;
	}
	
	public String getSelector() {
		return selector;
	}

	public Modifier getModifier() {
		return modifier;
	}
	
}
