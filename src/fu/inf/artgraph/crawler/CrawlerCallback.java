package fu.inf.artgraph.crawler;

/**
 * Interface für Meldungen vom Crawler.
 */
public interface CrawlerCallback {
	
	public void sendStatus(CrawlerMessage cm);
	
}
