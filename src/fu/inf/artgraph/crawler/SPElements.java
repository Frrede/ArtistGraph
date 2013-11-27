package fu.inf.artgraph.crawler;

import java.util.regex.Pattern;

/**
 * Container um "document" aus der Javascript-Konfig abzubilden.
 * Enhält angaben dazu, was mit der zu parsenden Webseite gemacht werden soll.
 */
public class SPElements {
	
	private String selectorAll;
	
	private String validateSubSelector;
	private Pattern validateRegex;
	
	private SPSelector globalYear;
	private SPSelector globalMonth;
	
	private SPTask[] spTask;

	public SPElements(String selectorAll, String validateSubSelector, Pattern validateRegex,
			SPSelector globalYear, SPSelector globalMonth, SPTask[] spTask) {
		
		this.selectorAll = selectorAll;
		this.validateSubSelector = validateSubSelector;
		this.validateRegex = validateRegex;
		this.globalYear = globalYear;
		this.globalMonth = globalMonth;
		this.spTask = spTask;
	}

	public String getSelectorAll() {
		return selectorAll;
	}

	public String getValidateSubSelector() {
		return validateSubSelector;
	}

	public Pattern getValidateRegex() {
		return validateRegex;
	}

	public SPSelector getGlobalYear() {
		return globalYear;
	}

	public SPSelector getGlobalMonth() {
		return globalMonth;
	}

	public SPTask[] getSPTask() {
		return spTask;
	}
	
}
