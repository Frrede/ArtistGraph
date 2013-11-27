package fu.inf.artgraph.crawler;

import javax.script.Invocable;
import sun.org.mozilla.javascript.internal.NativeObject;

/**
 * Beinhaltet eine aus dem Script geparste Funktion und kümmert sich umd das Ausführen.
 */
public class Modifier {
	
	private Invocable inv;
	private NativeObject no;
	
	/**
	 * Erstellt einen Modifier, über den sich später ein Sting mit der Script-Funktion
	 * modifizieren lässt.
	 * 
	 * @param function Object Vom Typ einer Javascript-Funktion vom Typ
	 * sun.org.mozilla.javascript.internal.InterpretedFunction
	 * @param fncName String Name der Javascript-Funktion
	 */
	public Modifier(Object function, Invocable inv) {
		no = new NativeObject();
		no.defineProperty("modify", function, NativeObject.READONLY);
		this.inv = inv;
	}
	
	/**
	 * 
	 * @param result String Das Resultat beim Parsen 
	 * @return String Das modifzierte Resultat, falls etwas nicht klappt das ursprüngliche.
	 */
	public String getResult(String result) {
		try {
			return (String) inv.invokeMethod(no, "modify", result);
		}
		catch(Exception e) {
			return result;
		}
	}
	
}
