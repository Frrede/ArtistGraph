package fu.inf.artgraph.gui;

import javax.swing.RowFilter;

public class ResultRowFilter extends RowFilter<ResultTableModel, Integer> {
	
	private int filterType = -1;
	
	@Override
	public boolean include(Entry<? extends ResultTableModel, ? extends Integer> entry) {
		
		if(filterType == -1) return true;
		
		ResultTableModel rtm = entry.getModel();
		int row = entry.getIdentifier();
		
		int type = rtm.getTypeAt(row);
		
		if(type == filterType) return true;
		
		return false;
	}

	public void setType(int type) {
		this.filterType = type;
	}
	
	
}
