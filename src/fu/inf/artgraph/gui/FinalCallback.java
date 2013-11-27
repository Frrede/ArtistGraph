package fu.inf.artgraph.gui;

import fu.inf.artgraph.crawler.CrawledData;
import fu.inf.artgraph.tagger.NameData;

/**
 * Interface für Ergebnis-Callback von Worker.
 */
public interface FinalCallback {
	
	public void addResult(String name, NameData[] nd, CrawledData[] cd);
	
}
