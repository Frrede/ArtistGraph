package fu.inf.artgraph.crawler;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Zum Speichern der gecrawlten Daten.
 */
public class CrawledData {
	
	private String name;
	private Calendar date;
	private String[] textBlocks;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
	
	/**
	 * @param name String Name der geparsten Seite.
	 * @param year int Jahreszahl der Ausgabe.
	 * @param month int Monat der Ausgabe von 1-12 (optional).
	 * @param textBlocks String[] Array von verschiedenen Textblöcken.
	 */
	public CrawledData(String name, int year, int month, String[] textBlocks) {
		this.name = name;
		this.textBlocks = textBlocks;
		
		date = Calendar.getInstance();
		date.clear();
		date.set(Calendar.YEAR, year);
		
		if(month > 0 && month <= 12) {
			date.set(Calendar.MONTH, month-1);
		}
	}
	
	public CrawledData(String name, int year, String[] textBlocks) {
		this(name, year, -1, textBlocks);
	}

	public String getName() {
		return name;
	}

	public Calendar getDate() {
		return date;
	}
	
	public String[] getTextBlocks() {
		return textBlocks;
	}
	
	public String toString() {
		String ret = name + ": (" + dateFormat.format(date.getTime()) + ")\n";
		ret += "------------\n";
		for(int i = 0; i < textBlocks.length; i++) {
			ret += textBlocks[i] + "\n";
		}
		return ret;
	}
}
