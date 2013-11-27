package fu.inf.artgraph.utils;

/**
 * Speichert zwei Integer, die als Range interpretiert werden.
 */
public class Range {
	
	private int min, max;
	
	/**
	 * Sollte min größer sein als max werden beide vertauscht.
	 * 
	 * @param min int Beginn der Range
	 * @param max int Ende der Range
	 */
	public Range(int min, int max) {
		
		if(min > max) {
			int nm = max;
			max = min;
			min = nm;
		}
		
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Wird kein min angegeben wird min auf 0 gesetzt.
	 * 
	 * @param max int Ende der Range
	 */
	public Range(int max) {
		this.min = 0;
		this.max = max;
	}
	
	public int getMin() {
		return min;
	}
	
	public int getMax() {
		return max;
	}
	
}
