package fu.inf.artgraph.tagger;

/**
 * Interface für Meldungen vom Tagger.
 */
public interface TaggerCallback {

	public void sendStatus(TaggerMessage tm);
	
}


