package fu.inf.artgraph.gui;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import fu.inf.artgraph.tagger.TaggerMessage;

public class TagCellRenderer extends JProgressBar implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		boolean hasFocus, int row, int column) {
		
		int progress = 0;
		if (value instanceof TaggerMessage) {
			
			TaggerMessage tm = (TaggerMessage) value;
			
			int all = tm.getAll();
			int ready = tm.getReady();
			
			int subAll = tm.getSubAll();
			int subReady = tm.getSubReady();
			
			progress = Math.round((float) ready / (float) all * 100f);
			
			this.setStringPainted(true);
			this.setString(ready + " / " + all + " (" + subReady + " / " + subAll + ")"); 
		}
		this.setValue(progress);
		return this;
	}

}
