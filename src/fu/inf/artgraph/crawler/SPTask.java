package fu.inf.artgraph.crawler;

/**
 * Bildet Tasks aus dem Konfigurationsformat ab.
 */
public class SPTask {
	
	private SPSelector taskYear;
	private SPSelector taskMonth;
	
	private SPSelector[] taskParse;
	
	private SPSelector[] taskFollow;
	private SPElements[][] spElements;
	
	public SPTask(SPSelector taskYear, SPSelector taskMonth, SPSelector[] taskParse,
			SPSelector[] taskFollow, SPElements[][] spElements) {
		
		this.taskYear = taskYear;
		this.taskMonth = taskMonth;
		this.taskParse = taskParse;
		this.taskFollow = taskFollow;
		this.spElements = spElements;
	}

	public SPSelector getTaskYear() {
		return taskYear;
	}

	public SPSelector getTaskMonth() {
		return taskMonth;
	}

	public SPSelector[] getTaskParse() {
		return taskParse;
	}

	public SPSelector[] getTaskFollow() {
		return taskFollow;
	}

	public SPElements[][] getSPElements() {
		return spElements;
	}
	
}
