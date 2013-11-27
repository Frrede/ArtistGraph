package fu.inf.artgraph.gui;

import javax.swing.SwingWorker;

import fu.inf.artgraph.crawler.CrawledData;
import fu.inf.artgraph.crawler.Crawler;
import fu.inf.artgraph.crawler.CrawlerCallback;
import fu.inf.artgraph.crawler.CrawlerMessage;
import fu.inf.artgraph.crawler.SPContainer;

public class CrawlWorker extends SwingWorker<CrawledData[], Void> implements CrawlerCallback {
	
	private SPContainer spContainer;
	private Crawler crawler;
	private ProcessTableModel ptm;
	private FinalCallback fc;
	
	public CrawlWorker(SPContainer spContainer, ProcessTableModel ptm, FinalCallback fc) {
		this.spContainer = spContainer;
		this.crawler = new Crawler(spContainer, this);
		this.ptm = ptm;
		this.fc = fc;
		ptm.updateStatus(spContainer, "Crawling");
	}
	
	@Override
	protected CrawledData[] doInBackground() throws Exception {
		
		return crawler.getData();
		
	}
	
	@Override
    protected void done() {
		
		try {
			ptm.updateStatus(this.spContainer, "Init crawling");
			new TagWorker(this.get(), ptm, this.spContainer, fc).execute();
		}
		catch(Exception e) {
			ptm.updateStatus(this.spContainer, "ERROR!");
		}
		
	}
	
	@Override
	public void sendStatus(CrawlerMessage cm) {
		ptm.updateCrawlingStatus(this.spContainer, cm);
	}

}
