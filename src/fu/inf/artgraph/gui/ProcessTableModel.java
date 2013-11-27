package fu.inf.artgraph.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import fu.inf.artgraph.crawler.CrawlerMessage;
import fu.inf.artgraph.crawler.SPContainer;
import fu.inf.artgraph.tagger.TaggerMessage;

public class ProcessTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private List<ProcessRowData> rows;
	private Map<SPContainer, ProcessRowData> savedSPC;

    public ProcessTableModel() {
        this.rows = new ArrayList<ProcessRowData>(25);
        this.savedSPC = new HashMap<SPContainer, ProcessRowData>(25);
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        String name = "?";
        switch (column) {
            case 0:
                name = "Name";
                break;
            case 1:
                name = "State";
                break;
            case 2:
                name = "Crawling Progress";
                break;
            case 3:
                name = "Tagging Progress";
                break;
        }
        return name;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
    	ProcessRowData rowData = rows.get(rowIndex);
        Object value = null;
        switch (columnIndex) {
            case 0:
                value = rowData.getPagename();
                break;
            case 1:
                value = rowData.getStatus();
                break;
            case 2:
                value = rowData.getCrawlerMessage();
                break;
            case 3:
                value = rowData.getTaggerMessage();
                break;
        }
        return value;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    	ProcessRowData rowData = rows.get(rowIndex);
        switch (columnIndex) {
            case 1:
                if (aValue instanceof String) {
                    rowData.setStatus((String) aValue);
                }
                break;
            case 2:
                if (aValue instanceof CrawlerMessage) {
                    rowData.setCrawlerMessage((CrawlerMessage) aValue);
                }
                break;
            case 3:
            	if (aValue instanceof TaggerMessage) {
                    rowData.setTaggerMessage((TaggerMessage) aValue);
                }
            	break;
        }
    }
    
    public void addConfig(SPContainer spContainer) {
    	String pn = spContainer.getName();
    	String st = "Ready to start";
    	CrawlerMessage cm = new CrawlerMessage(0, 0, 0);
    	TaggerMessage tm = new TaggerMessage(0, 0, 0, 0);
    	ProcessRowData crd = new ProcessRowData(pn, st, cm, tm);
    	savedSPC.put(spContainer, crd);
        rows.add(crd);
        this.fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
    }
    
    public void removeConfig(SPContainer spContainer, int index) {
    	savedSPC.remove(spContainer);
    	rows.remove(index);
    }
    
    public void updateStatus(SPContainer spContainer, String status) {
    	ProcessRowData crd = savedSPC.get(spContainer);
    	if(crd != null) {
    		int row = rows.indexOf(crd);
            setValueAt(status, row, 1);
            fireTableCellUpdated(row, 1);
    	}
    }
    
    public void updateCrawlingStatus(SPContainer spContainer, CrawlerMessage cm) {
    	ProcessRowData crd = savedSPC.get(spContainer);
    	if(crd != null) {
    		int row = rows.indexOf(crd);
            setValueAt(cm, row, 2);
            fireTableCellUpdated(row, 2);
    	}
    }
    
    public void updateTaggingStatus(SPContainer spContainer, TaggerMessage tm) {
    	ProcessRowData crd = savedSPC.get(spContainer);
    	if(crd != null) {
    		int row = rows.indexOf(crd);
            setValueAt(tm, row, 3);
            fireTableCellUpdated(row, 3);
    	}
    }
}