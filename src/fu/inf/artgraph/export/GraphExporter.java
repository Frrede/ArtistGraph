package fu.inf.artgraph.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import fu.inf.artgraph.db.SQLConnector;
import fu.inf.artgraph.db.SQLFactory;

/**
 * Exportiert die Ergebnisdaten aus der Datenbank in das GML-Format.
 */
public class GraphExporter {
	
	private static SQLConnector sc;
	
	private static void check() throws Exception {
		if(sc == null) {
			sc = SQLFactory.getSQLConnector("result");
		}
	}
	
	/**
	 * Speichert die Daten im GML-Format in einer Datei.
	 * 
	 * @param file File Zieldatei
	 * @param fromYear int Nur Daten ab diesem Jahr
	 * @param fromMonth int Nur Daten ab diesem Monat
	 * @param toYear int Nur Daten bis zu diesem Jahr
	 * @param toMonth int Nur Daten bis zu diesem Monat
	 * @param withDates boolean Ob das Datum auf einer Kante vermerkt werden soll.
	 * @throws Exception
	 */
	public static void saveAsGML(File file, int fromYear, int fromMonth, int toYear, int toMonth, boolean withDates) throws Exception {

		check();
		
		int id = 0; // Counter für alle Konten-IDs
		
		HashMap<String, Integer> idMapping = new HashMap<String, Integer>();
		
		// Länge von GROUP_CONCAT erhöhen.
		if(withDates) {
			String sQuery = "SET SESSION group_concat_max_len = 1000000;";
			sc.insertOrUpdate(sQuery);
		}
		
		String query = "SELECT aname, mname, COUNT(date) ";
		if(withDates) {
			query += ", GROUP_CONCAT(date ORDER BY date DESC SEPARATOR ',') ";
		}
		query += "FROM namedata n LEFT JOIN artistname a ON n.aid = a.aid ";
		query += "LEFT JOIN medianame m ON m.mid = n.mid ";
				
		if(fromYear > -1 && fromMonth > -1 && toYear > -1 && toMonth > -1) {
			String from = fromYear + "-" + fromMonth + "-00";
			query += "WHERE date >= \"" + from + "\" ";
			
			String to = toYear + "-" + toMonth + "-00";
			query += "AND date <= \"" + to + "\" ";
		}
		
		query += "GROUP BY aname, mname;";
		
		String out[][] = sc.getStringArrayByQuery(query);
		
		if (!file.exists()) {
			file.createNewFile();
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
		
		bw.write("graph [");
		bw.newLine();
		
		bw.write("    directed 0");
		bw.newLine();
		
		// Knoten erstellen
		for(int i = 0; i < out.length; i++) {
			
			String[] o = out[i];
			
			String aname = o[0];
			String mname = o[1];
			
			if(idMapping.get(aname) == null) {
				
				bw.write("    node [");
				bw.newLine();
				
				bw.write("        id " + id);
				bw.newLine();
				
				bw.write("        label \"" + aname + "\"");
				bw.newLine();
				
				bw.write("        type 0");
				bw.newLine();
				
				bw.write("    ]");
				bw.newLine();
				
				idMapping.put(aname, id);
				id++;
			}
			
			if(idMapping.get(mname) == null) {
				
				bw.write("    node [");
				bw.newLine();
				
				bw.write("        id " + id);
				bw.newLine();
				
				bw.write("        label \"" + mname + "\"");
				bw.newLine();
				
				bw.write("        type 1");
				bw.newLine();
				
				bw.write("    ]");
				bw.newLine();
				
				idMapping.put(mname, id);
				id++;
			}
			
		}
		
		// Kanten erstellen
		for(int i = 0; i < out.length; i++) {
			
			String[] o = out[i];
			
			String aname = o[0];
			String mname = o[1];
			int count = -1;
			try {
				count = Integer.parseInt(o[2]);
			}
			catch(Exception e) {
				
			}
			String dates = null;
			if(withDates) {
				dates = o[3];
				dates = dates.replace("-00", "-01"); // Monat auf Januar setzen
			}
			
			bw.write("    edge [");
			bw.newLine();
			
			bw.write("        source " + idMapping.get(aname));
			bw.newLine();
			
			bw.write("        target " + idMapping.get(mname));
			bw.newLine();
			
			if(count > -1) {
				bw.write("        count " + count);
				bw.newLine();
			}
			
			if(dates != null) {
				bw.write("        dates \"" + dates + "\"");
				bw.newLine();
			}
			
			bw.write("    ]");
			bw.newLine();
			
		}
		
		bw.write("]");
		bw.close();
	}

	/* DIE FUNKTIONEN AB HIER SIND NICHT IN DER GUI VERANKERT */
	
	/**
	 * Speichert Daten für jeden Monat einzeln als GML.
	 * 
	 * @param path String Zielordner
	 * @param fromYear int Nur Daten ab diesem Jahr
	 * @param fromMonth int Nur Daten ab diesem Monat
	 * @param toYear int Nur Daten bis zu diesem Jahr
	 * @param toMonth int Nur Daten bis zu diesem Monat
	 * @throws Exception
	 */
	public static void saveAllAsGML(String path, int fromYear, int fromMonth, int toYear, int toMonth) throws Exception {
		
		int month = fromMonth;
		int year = fromYear;
		
		while(year <= toYear) {
			
			File f = new File(path + "/" + year + "-" + month + ".gml");
			saveAsGML(f, year, month, year, month, false);
			
			if(month < 12) {
				month++;
			}
			else {
				year++;
				month = 1;
			}
			
			if(year > toYear || year == toYear && month > toMonth) {
				break;
			}
		}
		
	}
	
	/**
	 * Speichert alle Daten als Tabelle
	 * 
	 * @param file File Zieldatei
	 * @throws Exception 
	 */
	public static void saveDataAsTable(File file) throws Exception {
		
		check();
		
		String query = "SELECT aname, mname, COUNT(date) FROM namedata n LEFT JOIN artistname a ON n.aid = a.aid LEFT JOIN medianame m ON m.mid = n.mid GROUP BY aname, mname;";
		
		String out[][] = sc.getStringArrayByQuery(query);
		
		if (!file.exists()) {
			file.createNewFile();
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
		
		HashMap<String, Integer> indAName = new HashMap<String, Integer>();
		HashMap<String, Integer> indMName = new HashMap<String, Integer>();
		
		ArrayList<String> aNames = new ArrayList<String>();
		ArrayList<String> mNames = new ArrayList<String>();
		
		for(int i = 0; i < out.length; i++) {
			
			String[] o = out[i];
			
			String aName = o[0];
			String mName = o[1];
			
			if(!indAName.containsKey(aName)) {
				aNames.add(aName);
				indAName.put(aName, aNames.size()-1);
			}
			
			if(!indMName.containsKey(mName)) {
				mNames.add(mName);
				indMName.put(mName, mNames.size()-1);
			}
			
		}
		
		int[][] result = new int[aNames.size()][mNames.size()];
		
		for(int i = 0; i < out.length; i++) {
			
			String[] o = out[i];
			
			String aName = o[0];
			String mName = o[1];
			Integer res = Integer.parseInt(o[2]);
			
			Integer aInd = indAName.get(aName);
			Integer mInd = indMName.get(mName);
			
			result[aInd][mInd] = res;
			
		}
		
		// Spaltennamen
		for(int i = 0; i < mNames.size(); i++) {
			String l = mNames.get(i);
			if(i < mNames.size() - 1) {
				l += ";";
			}
			bw.write(l);
		}
		bw.newLine();
		
		// Zeilennamen + Daten
		for(int i = 0; i < result.length; i++) {
			
			bw.write(aNames.get(i));
			int[] row = result[i];
			for(int j = 0; j < row.length; j++) {
				bw.write(";" + row[j]);
			}
			bw.newLine();
		}
		
		bw.flush();
		bw.close();
	}
	
	
	/**
	 * Speichert das Datum aller Nennungen zu allen Künstlern in Tabelle
	 * 
	 * @param file File Zieldatei
	 * @throws Exception 
	 */
	public static void saveNameDatesAsTable(File file) throws Exception {
		
		check();
		
		String sQuery = "SET SESSION group_concat_max_len = 1000000;";
		sc.insertOrUpdate(sQuery);
		
		String query = "SELECT aname, GROUP_CONCAT(DISTINCT date ORDER BY date DESC SEPARATOR ',') FROM namedata n LEFT JOIN artistname a ON n.aid = a.aid LEFT JOIN medianame m ON m.mid = n.mid GROUP BY aname HAVING count(date) > 1;";
		
		String out[][] = sc.getStringArrayByQuery(query);
		
		if (!file.exists()) {
			file.createNewFile();
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
		
		// Zeilennamen + Daten
		for(int i = 0; i < out.length; i++) {
			String[] o = out[i];
			bw.write(o[0] + "; " + o[1].replace("-00", "-01")); // Monat auf Januar setzen
			bw.newLine();
		}
		
		bw.flush();
		bw.close();
	}
	
	/**
	 * Speichert das Datum aller Nennungen zu allen Künstlern in Tabelle
	 * 
	 * @param file File Zieldatei
	 * @throws Exception 
	 */
	public static void saveNameDatesAsTable(File file, int startYear, int startMonth, int endYear, int endMonth) throws Exception {
		
		check();
		
		String sQuery = "SET SESSION group_concat_max_len = 1000000;";
		sc.insertOrUpdate(sQuery);
		
		String query = "SELECT aname, GROUP_CONCAT(DISTINCT date ORDER BY date DESC SEPARATOR ',') FROM namedata n LEFT JOIN artistname a ON n.aid = a.aid LEFT JOIN medianame m ON m.mid = n.mid GROUP BY aname HAVING count(date) > 1;";
		
		String out[][] = sc.getStringArrayByQuery(query);
		
		if (!file.exists()) {
			file.createNewFile();
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
		
		// Zeilennamen + Daten
		for(int i = 0; i < out.length; i++) {
			String[] o = out[i];
			bw.write(o[0] + "; " + o[1].replace("-00", "-01")); // Monat auf Januar setzen
			bw.newLine();
		}
		
		bw.flush();
		bw.close();
	}
	
}
