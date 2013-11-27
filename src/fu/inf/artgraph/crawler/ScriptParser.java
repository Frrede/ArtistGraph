package fu.inf.artgraph.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Verarbeitet eine Javascript-Datei im passenden Konfigurationsformat.
 */
public class ScriptParser {
	
	ScriptEngineManager manager;
    ScriptEngine engine;
    Invocable inv;
	
	/* Beinhaltet eine Javascript-Funktion, die benötigt wird um Javascript-Objekte zu
	 * Java-Objekten zu konvertieren. Diese wird dem Javascript vor dem Parsten angehängt.
	 */
	private static String JS_ADD = "" +
	"function convertToJava(o) {" +
	    "var rval;" +
	    "if (Array.isArray(o)) {" +
	        "rval = new java.util.ArrayList();" +
	        "for (var key in o) {" +
	            "rval.add(convertToJava(o[key]));" +
	        "}" +
		"}"  +
	    "else if (typeof o === 'object') {" +
	        "rval = new java.util.HashMap();" +
	        "for (var key in o) {" +
	            "rval.put(key, convertToJava(o[key]));" +
	        "}" +
	    "}" +
	    "else if (typeof o === 'function') {" +
	        "rval = o;" +
	    "}" +
	    "else if (!isNaN(parseInt(o))) {" +
	    	"rval = parseInt(o);" +
	    "}" +
	    "else {" +
	        "rval = o;" +
	    "}" +
	    "return rval;" +
	"}";
	
	/**
	 * Erstellt einen ScriptParser und initialisiert die Javascript-Engine.
	 */
	public ScriptParser() {
		manager = new ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript");
        inv = (Invocable) engine;
	}
	
	/**
	 * @param jsFile String Die Javascript-Datei als String
	 * @return SPContainer Beinhaltet alle Daten aus dem Script
	 */
	public SPContainer parseJSConfig(String jsFile) throws ScriptException, NoSuchMethodException {
		
        String script = JS_ADD + "var config = ";
        script += jsFile;
		script += ";";
		
		engine.eval(script);
        
		HashMap config = (HashMap) engine.eval("convertToJava(config);");
		
		// Name
		String name = (String) config.get("name");
		
		HashMap source = (HashMap) config.get("source");
		
		// URL
		String url = (String) source.get("url");
		
		// Range
		HashMap loops = (HashMap) source.get("loops");
		
		TagRange[] tr = null;
		if(loops != null) {
			tr = new TagRange[loops.size()];
			int count = 0;
			for(Object elem : loops.keySet()) {

				String key = (String) elem;
				HashMap ele = (HashMap) loops.get(key);
				double min = (double) ele.get("from");
				double max = (double) ele.get("to");
				
				tr[count] = new TagRange(key.charAt(0), (int)min, (int)max);
				
				count++;
			}
		}
		
		SPElements[] spd = getElements((ArrayList) config.get("elements"));
		
		return new SPContainer(name, url, tr, spd);
	}
	
	/**
	 * Parst alles unterhalb vom obersten "elements". Weitere "document" werden rekursiv
	 * aufgerufen.
	 * 
	 * @param elements HashMap Erwartet das oberste "elements" aus dem Script.
	 * @return SPDocument Beinhaltet alles unterhalb von "elements".
	 */
	private SPElements[] getElements(ArrayList elementsArr) {
		
		int length = elementsArr.size();
		SPElements[] retEle = new SPElements[length];
		
		for(int i = 0; i < length; i++) {
			
			HashMap elements = (HashMap) elementsArr.get(i);
			
			// SelectorAll
			String selAll = (String) elements.get("selectorAll");
			
			// Validate (optional)
			HashMap val = (HashMap) elements.get("validate");
			String vsel = null;
			Pattern vpat = null;
			if(val != null && val.size() > 0) {
				vsel = (String) val.get("subSelector");
				vpat = Pattern.compile((String) val.get("regex"));
			}
			
			// Globales Year/Month (optional)
			HashMap globalDate = (HashMap) elements.get("globalDate");
			
			SPSelector spGlobYear = null;
			SPSelector spGlobMonth = null;
			if(globalDate != null && globalDate.size() > 0) {
				HashMap year = (HashMap) globalDate.get("year");
				spGlobYear = getSPSelector(year, "selector");
				
				HashMap month = (HashMap) globalDate.get("month");
				if(month != null) {
					spGlobMonth = getSPSelector(month, "selector");
				}
			}
			
			// Task
			ArrayList tasks = (ArrayList) elements.get("tasks");
			int tLength = tasks.size();
			SPTask[] spTasks = new SPTask[tLength];
			
			for(int j = 0; j < tLength; j++) {
				
				HashMap task = (HashMap) tasks.get(j);
				
				// Year/Month für jedes Element (optional)
				HashMap eleDate = (HashMap) task.get("date");
				
				SPSelector spEleYear = null;
				SPSelector spEleMonth = null;
				if(eleDate != null && eleDate.size() > 0) {
					HashMap year = (HashMap) eleDate.get("year");
					spEleYear = getSPSelector(year, "subSelector");
					
					HashMap month = (HashMap) eleDate.get("month");
					if(month != null) {
						spEleMonth = getSPSelector(month, "subSelector");
					}
				}
				
				// Parse (optional)
				ArrayList parse = (ArrayList) task.get("parse");
				SPSelector[] spParse = null;
				if(parse != null && parse.size() > 0) {
					int pLength = parse.size();
					spParse = new SPSelector[pLength];
					for(int k = 0; k < pLength; k++) {
						spParse[k] = getSPSelector((HashMap) parse.get(k), "subSelectorAll");
					}
				}
				
				// Follow (optional)
				ArrayList follow = (ArrayList) task.get("follow");
				SPSelector[] spUrl = null;
				SPElements[][] subElements = null;
				
				if(follow != null && follow.size() > 0) {
					int fLength = follow.size();
					spUrl = new SPSelector[fLength];
					subElements = new SPElements[fLength][];
					for(int k = 0; k < fLength; k++) {
						HashMap fol = (HashMap) follow.get(k);
						spUrl[k] = getSPSelector((HashMap) fol.get("url"), "subSelectorAll");
						subElements[k] = getElements((ArrayList) fol.get("elements"));
					}
				}
				
				// Task erstellen
				spTasks[j] = new SPTask(spEleYear, spEleMonth, spParse, spUrl, subElements);
			}
			
			// Elements erstellen
			retEle[i] = new SPElements(selAll, vsel, vpat, spGlobYear, spGlobMonth, spTasks);
			
		}
		
		return retEle;
	}
	
	/** 
	 * @param parent HashMap Das Objekt aus dem der SPSelector gelesen werden soll.
	 * @return SPSelector
	 */
	private SPSelector getSPSelector(HashMap parent, String selectorName) {
		
		// Selektor
		String psel = (String) parent.get(selectorName);
		
		// Modify-Funktion (optional)
		Object oMod = parent.get("modify");
		Modifier pmod = null;
		if(oMod != null) {
			pmod = new Modifier(parent.get("modify"), inv);
		}
		return new SPSelector(psel, pmod);
	}
	
}
