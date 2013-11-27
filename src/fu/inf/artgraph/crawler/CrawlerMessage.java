package fu.inf.artgraph.crawler;

/**
 * Format für Meldungen vom Crawler.
 */
public class CrawlerMessage {
	
	private int urlCount = 0;
	private int readyCount = 0;
	private int errorCount = 0;
	
	public CrawlerMessage(int urlCount, int readyCount, int errorCount) {
		this.urlCount = urlCount;
		this.readyCount = readyCount;
		this.errorCount = errorCount;
	}

	public int getURLCount() {
		return urlCount;
	}

	public int getReadyCount() {
		return readyCount;
	}

	public int getErrorCount() {
		return errorCount;
	}
	
}
