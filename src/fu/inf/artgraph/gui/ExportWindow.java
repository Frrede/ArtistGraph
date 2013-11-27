package fu.inf.artgraph.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import fu.inf.artgraph.export.GraphExporter;

/**
 * Fenster zum Export nach GML.
 */
public class ExportWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	final JFileChooser fileSaver = new JFileChooser("Save file");
	
	private JCheckBox withDate = new JCheckBox("With date range");
	
	private JLabel from = new JLabel("From");
	private Box fromCont = new Box(BoxLayout.X_AXIS);
	private JTextField fromYear = new JTextField("YYYY");
	private JTextField fromMonth = new JTextField("MM");
	
	private JLabel to = new JLabel("To");
	private Box toCont = new Box(BoxLayout.X_AXIS);
	private JTextField toYear = new JTextField("YYYY");
	private JTextField toMonth = new JTextField("MM");
	
	private JCheckBox check = new JCheckBox("Save dates at edge");
	
	private JButton export = new JButton("Export");
	
	public ExportWindow() {
		
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setTitle("Export as GML");
		
		fileSaver.setDialogType(JFileChooser.SAVE_DIALOG);
		
		this.add(withDate);
		
		this.add(from);
		fromCont.add(fromYear);
		fromCont.add(fromMonth);
		this.add(fromCont);
		
		this.add(to);
		toCont.add(toYear);
		toCont.add(toMonth);
		this.add(toCont);
		
		this.add(check);
		
		this.add(Box.createRigidArea(new Dimension(0, 5)));
		this.add(export);
		
		this.pack();
		
		export.addActionListener(this);
		
		fromYear.setEnabled(false);
		fromMonth.setEnabled(false);
		toYear.setEnabled(false);
		toMonth.setEnabled(false);
		
		withDate.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object source = e.getSource();
		
		if(source == export) {
			
			int returnVal = fileSaver.showSaveDialog(this);
	        if (returnVal == 0) {
	        	
				File file = fileSaver.getSelectedFile();
				
				try {

					int fYear = -1;
					int fMonth = -1;
					int tYear = -1;
					int tMonth = -1;
					
					if(withDate.isSelected()) {
						
						try {
							fYear = Integer.parseInt(fromYear.getText());
							fMonth = Integer.parseInt(fromMonth.getText());
							tYear = Integer.parseInt(toYear.getText());
							tMonth = Integer.parseInt(toMonth.getText());
						}
						catch(Exception ex) {
							JOptionPane.showMessageDialog(this,
				    			    "Wrong date format!",
				    			    "Error!",
				    			    JOptionPane.ERROR_MESSAGE);
						}
					}
					
					if(file != null) {
						GraphExporter.saveAsGML(file, fYear, fMonth, tYear, tMonth, check.isSelected());
					}
					
					this.dispose();
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(this,
		    			    "Couldn't export graph!",
		    			    "Error!",
		    			    JOptionPane.ERROR_MESSAGE);
				}
	        	
	        }
	        
		}
		
		else if(source == withDate) {
			
			if(withDate.isSelected()) {
				fromYear.setEnabled(true);
				fromMonth.setEnabled(true);
				toYear.setEnabled(true);
				toMonth.setEnabled(true);
			}
			else {
				fromYear.setEnabled(false);
				fromMonth.setEnabled(false);
				toYear.setEnabled(false);
				toMonth.setEnabled(false);
			}
			
		}
	}
	
	
	
}
