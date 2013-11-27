package fu.inf.artgraph.gui;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import fu.inf.artgraph.crawler.CrawlerMessage;

public class CrawlCellRenderer extends JProgressBar implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		boolean hasFocus, int row, int column) {
		
		int progress = 0;
		if (value instanceof CrawlerMessage) {
			
			CrawlerMessage cm = (CrawlerMessage) value;
			
			int urlCount = cm.getURLCount();
			int readyCount = cm.getReadyCount();
			int errorCount = cm.getErrorCount();
			
			progress = Math.round((float) readyCount / (float) urlCount * 100f);
			
			this.setStringPainted(true);
			this.setString(readyCount + " (" + errorCount + ") / " + urlCount); 
		}
		this.setValue(progress);
		return this;
	}

}
