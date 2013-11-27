package fu.inf.artgraph.tagger;

import fu.inf.artgraph.crawler.CrawledData;

/**
 * Abstrakte Klasse Tagger
 */
public abstract class Tagger {
	
	protected CrawledData[] crawledData;
	
	public Tagger(CrawledData[] crawledData) {
		this.crawledData = crawledData;
	}
	
	public abstract NameData[] getNameData(); 	
	
}
