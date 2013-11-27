package fu.inf.artgraph.crawler;

/**
 * Speichert Aufgaben, die der Crawler abzuarbeiten hat.
 */
public class CrawlerTask {
	
	String url;
	int year;
	int month;
	SPElements[] spElements;
	
	public CrawlerTask(String url, int year, int month, SPElements[] spElements) {
		this.url = url;
		this.year = year;
		this.month = month;
		this.spElements = spElements;
	}

	public String getUrl() {
		return url;
	}

	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public SPElements[] getSPElements() {
		return spElements;
	}
	
}
