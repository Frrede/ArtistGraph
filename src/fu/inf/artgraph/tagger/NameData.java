package fu.inf.artgraph.tagger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Speichert Daten zu gefundenem Namen sowie eine Kennung wie dieser gefunden wurde.
 * 
 * 0: NA
 * 1: Getaggt und in DB gefunden
 * 2: Getaggt und Bezeichnung vor Name
 * 3: Direkt im Text gefunden (mit Vorname)
 */
public class NameData {
	
	private String mediaName;
	private Calendar date;
	private String artName;
	private int type; // Kennung
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
	
	public NameData(String mediaName, Calendar date, String artName, int type) {
		this.mediaName = mediaName;
		this.date = date;
		this.artName = artName;
		this.type = type;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mediaName == null) ? 0 : mediaName.hashCode());
        result = prime * result + ((date == null) ? 0 : date.get(Calendar.MONTH));
        result = prime * result + ((date == null) ? 0 : date.get(Calendar.YEAR));
        result = prime * result + ((artName == null) ? 0 : artName.hashCode());
        return result;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
        	return true;
        }  
        if (obj == null) {
        	return false;
        }
        if (getClass() != obj.getClass()) {
        	return false;
        }
        
        NameData nd = (NameData) obj;
        
        boolean isM = mediaName.equals(nd.getMediaName());
        boolean isDM = date.get(Calendar.MONTH) == nd.getDate().get(Calendar.MONTH);
        boolean isDY = date.get(Calendar.YEAR) == nd.getDate().get(Calendar.YEAR);
        boolean isA = artName.equals(nd.getArtName());
        if(isM && isDM && isDY && isA) {
        	return true;
        }
        
        return false;
    }
	
    @Override
    public String toString() {
    	String ret = mediaName + " (";
    	ret += dateFormat.format(date.getTime()) + ")";
    	ret += " - " + artName;
    	return ret;
    }
    
	public NameData(String mediaName, Calendar date, String artName) {
		this(mediaName, date, artName, 0);
	}
	
	public String getMediaName() {
		return mediaName;
	}

	public Calendar getDate() {
		return date;
	}

	public String getArtName() {
		return artName;
	}
	
	public int getType() {
		return type;
	}
	
}
