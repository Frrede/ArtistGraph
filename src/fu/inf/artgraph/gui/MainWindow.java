package fu.inf.artgraph.gui;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableRowSorter;

import fu.inf.artgraph.crawler.CrawledData;
import fu.inf.artgraph.crawler.SPContainer;
import fu.inf.artgraph.crawler.ScriptParser;
import fu.inf.artgraph.db.NameDBUtils;
import fu.inf.artgraph.db.ResultDBUtils;
import fu.inf.artgraph.tagger.NameData;

/**
 * Das Hauptfenster der Anwendung.
 */
public class MainWindow extends JFrame implements ActionListener, FinalCallback {
	private static final long serialVersionUID = 1L;
	
	private ScriptParser scriptParser = new ScriptParser();
	private ArrayList<SPContainer> spcList = new ArrayList<SPContainer>(); 
	private ArrayList<ArrayList<NameData>> results = new ArrayList<ArrayList<NameData>>();
	private ArrayList<CrawledData[]> allCrawledData = new ArrayList<CrawledData[]>();
	private HashMap<String, Integer> restartCount = new HashMap<String, Integer>();
	
	// Obere Elemente
	private JPanel topContainer = new JPanel();
	private Box topOptCont = new Box(BoxLayout.Y_AXIS);
	
	final JFileChooser configChooser = new JFileChooser("Choose Config");
	
	private ProcessTableModel ptm = new ProcessTableModel();
	private JTable topTable = new JTable();
	
	private Box topButtonCont = new Box(BoxLayout.X_AXIS);
	private JButton topButtonAdd = new JButton("Add");
	private JButton topButtonRemove = new JButton("Remove");
	private DefaultListModel<String> tflModel = new DefaultListModel<String>();
	private JList<String> topFileList = new JList<String>(tflModel);
	private Box tbcCont = new Box(BoxLayout.X_AXIS);
	private JButton topButtonStart = new JButton("Start");
	private JButton topButtonRestart = new JButton("Restart selected");
	
	// Untere Elemente
	private JPanel bottomContainer = new JPanel();
	private Box bottomLOptCont = new Box(BoxLayout.Y_AXIS);
	private ResultTableModel rtm = new ResultTableModel();
	private JTable bottomTable = new JTable();
	private TableRowSorter<ResultTableModel> rowSorter = new TableRowSorter<ResultTableModel>(rtm);
	private ResultRowFilter rrf = new ResultRowFilter();
	private Box bottomROptCont = new Box(BoxLayout.Y_AXIS);
	
	private DefaultListModel<String> bnlModel = new DefaultListModel<String>();
	private JList<String> bottomNameList = new JList<String>(bnlModel);
	private int nameListIndex = -1;
	
	private JButton bottomRemoveResult = new JButton("Remove selected");
	
	private JRadioButton rbAll = new JRadioButton("Show all");
	private JRadioButton rbTagDB = new JRadioButton("Show tagged and in DB");
	private JRadioButton rbTagTitle = new JRadioButton("Show tagged and with title");
	private JRadioButton rbInDB = new JRadioButton("Show found in DB");
	
	private JButton bottomRemove = new JButton("Remove Item");
	private JButton bottomAddName = new JButton("Add Name to DB");
	private JButton bottomGoogle = new JButton("Search google");
	private JButton bottomULAN = new JButton("Search ULAN");
	private JButton buttonExport = new JButton("Export as GML");
	private JButton buttonToDB = new JButton("Add Results to DB");
	
	/**
	 * Konstruktor des Hauptfensters.
	 */
	public MainWindow() {
		
		this.setElements();
		
		this.bindElements();
		
	}
	
	/**
	 * Elemente erstellen und positionieren.
	 */
	private void setElements() {
		
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		Border border = BorderFactory.createEtchedBorder();
		
		// Oberen Elemente initaliseren
		topContainer.setBorder(BorderFactory.createTitledBorder(border, "Processing", TitledBorder.LEFT, TitledBorder.TOP));
		topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
		topContainer.add(topOptCont);
		topContainer.add(Box.createRigidArea(new Dimension(10, 0)));
		topContainer.add(new JScrollPane(topTable));
		
		topTable.setModel(ptm);
		topTable.getTableHeader().setReorderingAllowed(false);
		topTable.getColumn("Crawling Progress").setCellRenderer(new CrawlCellRenderer());
		topTable.getColumn("Tagging Progress").setCellRenderer(new TagCellRenderer());
		
		configChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		
		topOptCont.add(topButtonCont);
		topOptCont.add(Box.createRigidArea(new Dimension(0, 5)));
		topOptCont.add(new JScrollPane(topFileList));
		topOptCont.add(Box.createRigidArea(new Dimension(0, 5)));
		topOptCont.add(tbcCont);
		tbcCont.add(topButtonStart);
		tbcCont.add(Box.createRigidArea(new Dimension(5, 0)));
		tbcCont.add(topButtonRestart);
		
		topButtonCont.add(topButtonAdd);
		topButtonCont.add(Box.createRigidArea(new Dimension(5, 0)));
		topButtonCont.add(topButtonRemove);
		
		// Untere Elemente initaliseren
		bottomContainer.setBorder(BorderFactory.createTitledBorder(border, "Result", TitledBorder.LEFT, TitledBorder.TOP));
		bottomContainer.setLayout(new BoxLayout(bottomContainer, BoxLayout.X_AXIS));
		bottomContainer.add(bottomLOptCont);
		bottomContainer.add(Box.createRigidArea(new Dimension(5, 0)));
		bottomContainer.add(new JScrollPane(bottomTable));
		bottomContainer.add(Box.createRigidArea(new Dimension(5, 0)));
		bottomContainer.add(bottomROptCont);
		
		bottomTable.setModel(rtm);
		bottomTable.getTableHeader().setReorderingAllowed(false);
		bottomTable.setRowSorter(rowSorter);
		rowSorter.setRowFilter(rrf);
		
		bottomLOptCont.add(new JScrollPane(bottomNameList));
		bottomLOptCont.add(Box.createRigidArea(new Dimension(0, 5)));
		bottomLOptCont.add(bottomRemoveResult);
		bottomLOptCont.add(Box.createRigidArea(new Dimension(0, 5)));
		ButtonGroup group = new ButtonGroup();
		group.add(rbAll);
		group.add(rbTagDB);
		group.add(rbTagTitle);
		group.add(rbInDB);
		rbAll.setSelected(true);
		
		bottomLOptCont.add(rbAll);
		bottomLOptCont.add(rbTagDB);
		bottomLOptCont.add(rbTagTitle);
		bottomLOptCont.add(rbInDB);
		
		bottomROptCont.add(bottomRemove);
		bottomROptCont.add(Box.createRigidArea(new Dimension(0, 5)));
		bottomROptCont.add(new JSeparator());
		bottomROptCont.add(bottomGoogle);
		bottomROptCont.add(Box.createRigidArea(new Dimension(0, 5)));
		bottomROptCont.add(bottomULAN);
		bottomROptCont.add(Box.createRigidArea(new Dimension(0, 5)));
		bottomROptCont.add(bottomAddName);
		bottomROptCont.add(Box.createRigidArea(new Dimension(0, 5)));
		bottomROptCont.add(new JSeparator());
		bottomROptCont.add(buttonExport);
		bottomROptCont.add(Box.createRigidArea(new Dimension(0, 5)));
		bottomROptCont.add(buttonToDB);
		bottomROptCont.add(Box.createRigidArea(new Dimension(0, 5)));
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(800, 600));
		this.pack();
		
		this.add(topContainer);
		this.add(bottomContainer);
	}
	
	/**
	 * Events an Elemente binden.
	 */
	private void bindElements() {
		
		topButtonAdd.addActionListener(this);
		topButtonRemove.addActionListener(this);
		topButtonStart.addActionListener(this);
		topButtonRestart.addActionListener(this);
		
		bottomRemoveResult.addActionListener(this);
		bottomRemove.addActionListener(this);
		bottomAddName.addActionListener(this);
		bottomGoogle.addActionListener(this);
		bottomULAN.addActionListener(this);
		buttonExport.addActionListener(this);
		buttonToDB.addActionListener(this);
		
		rbAll.addActionListener(this);
		rbTagDB.addActionListener(this);
		rbTagTitle.addActionListener(this);
		rbInDB.addActionListener(this);
		
		bottomNameList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                int ind = bottomNameList.getSelectedIndex();
                if(ind > -1) {
                	ArrayList<NameData> nd = results.get(ind);
                	rtm.addResult(nd);
                	nameListIndex = ind;
                }
            }
        });
		
	}
	
	public void actionPerformed(ActionEvent e) {
		
		Object source = e.getSource();
		
		// Oberen Buttons
		if(source == topButtonAdd){
        	openConfig();            
        }
		else if(source == topButtonRemove) {
			int ind = topFileList.getSelectedIndex();
            if(ind > -1) {
            	tflModel.remove(ind);
            	ptm.removeConfig(spcList.remove(ind), ind);
            	ptm.fireTableDataChanged();
            }
		}
        else if(source == topButtonStart) {
        	topButtonAdd.setEnabled(false);
        	topButtonRemove.setEnabled(false);
        	topButtonStart.setEnabled(false);
        	topFileList.setEnabled(false);
        	
        	startCrawling();
        }
        else if(source == topButtonRestart) {
        	int ind = topTable.getSelectedRow();
        	if(ind > -1) {
        		String state = (String) ptm.getValueAt(ind, 1);
            	if(state.equals("Ready!")) {
            		restartTagging(ind);
            	}
            	else {
            		JOptionPane.showMessageDialog(null, "Restart possible after tagging!", "Error", JOptionPane.ERROR_MESSAGE);
            	}
        	}
        	else {
        		JOptionPane.showMessageDialog(null, "Select the item to restart!", "Error", JOptionPane.ERROR_MESSAGE);
        	}
        }
		
		// Unteren Buttons
        else if(source == bottomRemoveResult) {
        	int ind = bottomNameList.getSelectedIndex();
        	if(ind > -1) {
        		bnlModel.removeElementAt(ind);
        		results.remove(ind);
        		allCrawledData.remove(ind);
        	}
        }
        else if(source == bottomRemove) {
        	int ind = bottomTable.getSelectedRow();
        	if(ind > -1) {
        		int realInd = bottomTable.convertRowIndexToModel(ind);
        		ArrayList<NameData> nd = results.get(nameListIndex);
        		nd.remove(realInd);
        		rtm.addResult(nd);
        	}
        	
        }
        else if(source == bottomGoogle) {
        	int ind = bottomTable.getSelectedRow();
        	String selName = "";
        	if(ind > -1) {
        		int realInd = bottomTable.convertRowIndexToModel(ind);
        		ArrayList<NameData> nd = results.get(nameListIndex);
        		selName = nd.get(realInd).getArtName();
        		selName = selName.replace(" ", "+");
        		try {
					URL url = new URL("https://www.google.de/#q=" + selName);
					openWebpage(url);
				}
        		catch (Exception ex) {
					
				}
        	}
        }
        else if(source == bottomULAN) {
        	int ind = bottomTable.getSelectedRow();
			String selName = "";
        	if(ind > -1) {
        		int realInd = bottomTable.convertRowIndexToModel(ind);
        		ArrayList<NameData> nd = results.get(nameListIndex);
        		selName = nd.get(realInd).getArtName();
        		selName = selName.replace(" ", "+");
        		try {
					URL url = new URL("http://www.getty.edu/vow/ULANServlet?english=Y&find=" + selName + "&role=artist&page=1&nation=");
					openWebpage(url);
				}
        		catch (Exception ex) {
					
				}
        	}
		}
        else if(source == bottomAddName) {
        	
        	int ind = bottomTable.getSelectedRow();
        	String selName = "";
        	if(ind > -1) {
        		int realInd = bottomTable.convertRowIndexToModel(ind);
        		ArrayList<NameData> nd = results.get(nameListIndex);
        		selName = nd.get(realInd).getArtName();
        	}
        	
        	String s = (String)JOptionPane.showInputDialog(this,
        			"Add Name like \"Lastname1 Lastname2, Prename1 Prename2\"",
        			"Add Name",
        			JOptionPane.PLAIN_MESSAGE,
        			null,
        			null,
        			selName);
        	
        	if(s != null) {
        		boolean b = NameDBUtils.addArtistByEntireName(true, s);
        		if(b) {
        			JOptionPane.showMessageDialog(null, "Inserted!");
        		}
        		else {
        			JOptionPane.showMessageDialog(null, "Insert Error!", "Error", JOptionPane.ERROR_MESSAGE);
        		}
        	}
        	
        }
        else if(source == buttonExport) {
        	new ExportWindow().setVisible(true);
        }
        else if(source == buttonToDB) {
        	if(nameListIndex > -1) {
        		ArrayList<NameData> nd = results.get(nameListIndex);
            	boolean b = ResultDBUtils.addNameDataToDB(nd);
            	if(b) {
            		JOptionPane.showMessageDialog(null, "Inserted!");
            	}
            	else {
            		JOptionPane.showMessageDialog(null, "Insert Error!", "Error", JOptionPane.ERROR_MESSAGE);
            	}
        	}
        }
		
		// Radio Buttons unten
        else if(source == rbAll) {
        	rrf.setType(-1);
        	rtm.fireTableDataChanged();
        }
		else if(source == rbTagDB) {
			rrf.setType(1);
			rtm.fireTableDataChanged();
		}
		else if(source == rbTagTitle) {
			rrf.setType(2);
			rtm.fireTableDataChanged();
		}
		else if(source == rbInDB) {
			rrf.setType(3);
			rtm.fireTableDataChanged();
		}
    } 
	
	/**
	 * Laden und Parsen der Konfig-Datei.
	 */
	private void openConfig() {
		int returnVal = configChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            
        	SPContainer spc = null;
        	String fileName = "";
        	
        	try {
        		File file = configChooser.getSelectedFile();
        		String path = file.getPath();
        		fileName = file.getName();
                String config = readFile(path);
                
                spc = scriptParser.parseJSConfig(config);
                
        	}
        	catch(Exception exc) {
        		JOptionPane.showMessageDialog(this,
    			    "Couldn't read config file!",
    			    "Error!",
    			    JOptionPane.ERROR_MESSAGE);
        	}
        	
        	if(spc != null) {
        		spcList.add(spc);
        		tflModel.addElement(spc.getName() + " - " + fileName);
            	ptm.addConfig(spc);
        	}
        }
	}
	
	/**
	 * Crawlen starten.
	 */
	private void startCrawling() {
		
		for(SPContainer spc : spcList) {
			new CrawlWorker(spc, ptm, this).execute();
		}
		
	}
	
	/**
	 * Startet das Taggen für ein ausgewähltes Medium neu.
	 * 
	 * @param index int Index des Mediums.
	 */
	private void restartTagging(int index) {
		
		CrawledData[] cd = allCrawledData.get(index);
		SPContainer spc = spcList.get(index);
		try {
			new TagWorker(cd, ptm, spc, this).execute();
		}
		catch(Exception e) {
			
		}
		
	}
	
	/**
	 * Fügt Elemente zur Ergebnisliste.
	 */
	public void addResult(String name, NameData[] nd, CrawledData[] cd) {
		
		Integer count = restartCount.get(name);
		
		String nname;
		if(count != null) {
			++count;
			nname = name + " (" + count + ")";
			restartCount.put(name, count);
		}
		else {
			nname = name;
			restartCount.put(name, 0);
		}
		
		bnlModel.addElement(nname);
		results.add(new ArrayList<NameData>(Arrays.asList(nd)));
		allCrawledData.add(cd);
	}
	
	/**
	 * 
	 * @param path String Pfad zur Config-Datei.
	 * @return String Inhalt der Config-Datei.
	 * @throws IOException
	 */
	private static String readFile(String path) throws IOException {
		Charset encoding = Charset.defaultCharset();
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	
	/**
	 * Öffnet eine URI im Default-Browser.
	 * 
	 * @param uri URI
	 */
	public static void openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

	/**
	 * Öffnet eine URL im Default-Browser.
	 * 
	 * @param url URL 
	 */
	public static void openWebpage(URL url) {
	    try {
	        openWebpage(url.toURI());
	    } catch (URISyntaxException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Java Main Methode
	 */
	public static void main(String args[]) {
        new MainWindow().setVisible(true);
    }
	
}
