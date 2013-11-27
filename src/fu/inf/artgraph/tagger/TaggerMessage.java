package fu.inf.artgraph.tagger;

/**
 * Format für Meldungen vom Tagger.
 */
public class TaggerMessage {
	
	private int all;
	private int ready;
	private int subAll;
	private int subReady;
	
	public TaggerMessage(int all, int ready, int subAll, int subReady) {
		this.all = all;
		this.ready = ready;
		this.subAll = subAll;
		this.subReady = subReady;
	}

	public int getAll() {
		return all;
	}

	public int getReady() {
		return ready;
	}
	
	public int getSubAll() {
		return subAll;
	}

	public int getSubReady() {
		return subReady;
	}
	
}
