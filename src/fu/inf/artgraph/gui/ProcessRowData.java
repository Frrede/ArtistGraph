package fu.inf.artgraph.gui;

import fu.inf.artgraph.crawler.CrawlerMessage;
import fu.inf.artgraph.tagger.TaggerMessage;

public class ProcessRowData {
	
	private String pagename;
	private String status;
	private CrawlerMessage crawlerMessage;
	private TaggerMessage taggerMessage;
	
	public ProcessRowData(String pagename, String status,	CrawlerMessage cMessage, TaggerMessage tMessage) {
		this.pagename = pagename;
		this.status = status;
		this.crawlerMessage = cMessage;
		this.taggerMessage = tMessage;
	}

	public String getPagename() {
		return pagename;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public CrawlerMessage getCrawlerMessage() {
		return crawlerMessage;
	}

	public void setCrawlerMessage(CrawlerMessage crawlerMessage) {
		this.crawlerMessage = crawlerMessage;
	}
	
	public TaggerMessage getTaggerMessage() {
		return taggerMessage;
	}

	public void setTaggerMessage(TaggerMessage taggerMessage) {
		this.taggerMessage = taggerMessage;
	}
	
}
