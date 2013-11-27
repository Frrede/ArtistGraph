package fu.inf.artgraph.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import fu.inf.artgraph.tagger.NameData;

public class ResultTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM");
	
	ArrayList<NameData> nameData;
	
	public ResultTableModel() {
		nameData = new ArrayList<NameData>();
	}
	
    @Override
    public int getRowCount() {
        return nameData.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        String name = "?";
        switch (column) {
            case 0:
                name = "Name";
                break;
            case 1:
                name = "Date";
                break;
            case 2:
                name = "Type";
                break;
        }
        return name;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
    	NameData nd = nameData.get(rowIndex);
        Object value = null;
        switch (columnIndex) {
            case 0:
                value = nd.getArtName();
                break;
            case 1:
                value = dateFormat.format(nd.getDate().getTime());
                break;
            case 2:
            	int tid = nd.getType();
                if(tid == 1) {
                	value = "Tagged and in DB";
                }
                else if(tid == 2) {
                	value = "Tagged and artist title";
                }
                else if(tid == 3) {
                	value = "Found in DB";
                }
                else {
                	value = "NA";
                }
                break;
        }
        return value;
    }

    public int getTypeAt(int rowIndex) {
    	NameData nd = nameData.get(rowIndex);
    	return nd.getType();
    }
    
    public void addResult(ArrayList<NameData> nameData) {
    	this.nameData = nameData;
    	this.fireTableDataChanged();
    }
    
}
