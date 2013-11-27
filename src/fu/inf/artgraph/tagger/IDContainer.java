package fu.inf.artgraph.tagger;

/**
 * Kontainer zum vorübergehenden Speichern der Namens IDs.
 */
public class IDContainer {
	
	private String aId;
	private int count;
	private int max;
	private String name; // Vollständiger Name des Künstlers.
	private boolean shortname; // Ob Nachname den Künstler eindeutig identifiziert.
	private boolean disabled; // Gibt an, ob ein Künstler nicht beachtet werden soll.
	
	public IDContainer(String aId, int count, int max, String name, boolean shortname, boolean disabled) {
		this.aId = aId;
		this.count = count;
		this.max = max;
		this.name = name;
		this.shortname = shortname;
		this.disabled = disabled;
	}
	
	public String getAId() {
		return aId;
	}

	public int getCount() {
		return count;
	}
	
	public int getMax() {
		return max;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasShortname() {
		return shortname;
	}
	
	public boolean isDisabled() {
		return disabled;
	}
}
