package fu.inf.artgraph.gui;

import javax.swing.SwingWorker;

import fu.inf.artgraph.tagger.NameData;
import fu.inf.artgraph.tagger.Tagger;
import fu.inf.artgraph.tagger.TaggerCallback;
import fu.inf.artgraph.tagger.TaggerGer;
import fu.inf.artgraph.tagger.TaggerMessage;
import fu.inf.artgraph.crawler.CrawledData;
import fu.inf.artgraph.crawler.SPContainer;

public class TagWorker extends SwingWorker<NameData[], Void> implements TaggerCallback {
	
	private CrawledData[] crawledData;
	private Tagger tagger;
	private ProcessTableModel ptm;
	private SPContainer spContainer;
	private FinalCallback fc;
	
	public TagWorker(CrawledData[] crawledData, ProcessTableModel ptm, SPContainer spContainer, FinalCallback fc) throws Exception {
		this.crawledData = crawledData;
		this.tagger = new TaggerGer(crawledData, this);
		this.ptm = ptm;
		this.spContainer = spContainer;
		this.fc = fc;
		ptm.updateStatus(spContainer, "Tagging");
	}
	
	@Override
	protected NameData[] doInBackground() throws Exception {
		
		return tagger.getNameData();
		
	}
	
	@Override
    protected void done() {
		
		NameData[] nd = null;
		try {
			nd = this.get();
		}
		catch(Exception e) {
			ptm.updateStatus(spContainer, "Data Error!");
		}
		
		if(nd != null && nd.length > 0) {
			ptm.updateStatus(spContainer, "Ready!");
			fc.addResult(nd[0].getMediaName(), nd, crawledData);
		}
		else {
			ptm.updateStatus(spContainer, "No Result Error!");
		}
	}
	
	@Override
	public void sendStatus(TaggerMessage tm) {
		ptm.updateTaggingStatus(spContainer, tm);
	}
	
}
