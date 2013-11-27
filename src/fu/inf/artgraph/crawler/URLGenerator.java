package fu.inf.artgraph.crawler;

import java.util.Iterator;

/**
 * Generiert URLs aus einem String und einer beliebigen Anzahl von Tags und Ranges.
 * In der URL sollten sich die gleiche Anzahl an Platzhaltern befinden wie die Anzahl der Tags
 * und Ranges.
 * 
 * Bsp.: http://www.test.com/year/$y$/month/$m$/
 */
public class URLGenerator implements Iterator<String> {
	
	private String url;
	private TagRange[] tagRange;
	
	private boolean hasNext = true;
	private int size;
	private int[] loops;
	
	/**
	 * Erstellt einen URLGenerator und initialisiert weitere Variablen.
	 * 
	 * @param url String URL mit Platzhaltern der Form "$n$". "n" muss Tag aus TagRange
	 * entsprechen.
	 * @param tagRange TagRange Tags und zugehörige Ranges.
	 */
	public URLGenerator(String url, TagRange[] tagRange) {
		this.url = url;
		this.tagRange = tagRange;
		
		size = tagRange.length;
		loops = new int[size];
		for(int i = 0; i < size; i++) {
			loops[i] = tagRange[i].getMin();
		}
	}
	
	/**
	 * Gibt an, ob eine nächste URL vorliegt.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean hasNext() {
		return hasNext;
	}

	/**
	 * Gibt eine URL mit der nächsten Kombination aus den Ranges zurück.
	 * 
	 * @return String Die Nächste URL oder null wenn keine nächste
	 */
	@Override
	public String next() {
		
		if(!hasNext) return null;
		
		String nurl = "";
		boolean withBreak = false;
		
		// Von rechts nach links wird erhöht
		for(int i = size-1; i >= 0; i--) {
			
			// Es wird nur erhöht, wenn die Zahl noch kleiner ist als der Endwert
			if(loops[i] < tagRange[i].getMax()) {
				
				nurl = buildURL();
				
				// Alle weiter rechts stehenden Zahlen werden zurückgesetzt
				for(int r = i+1; r < size; r++) {
					loops[r] = tagRange[r].getMin();
				}
				
				loops[i]++;
				withBreak = true;
				break;
			}				
		}
		
		if(!withBreak) {
			hasNext = false;
			nurl = buildURL();
		}
		
		return nurl;
	}
	
	private String buildURL() {
		String nurl = url;
		for(int i = 0; i < size; i++) {
			nurl = nurl.replace("$" + tagRange[i].getTag() + "$",  ""+loops[i]);
		}
		return nurl;
	}
	
	/**
	 * Nicht unterstützt, da keine URL gelöscht werden können soll.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
