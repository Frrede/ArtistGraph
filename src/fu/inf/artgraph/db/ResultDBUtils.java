package fu.inf.artgraph.db;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import fu.inf.artgraph.tagger.NameData;

/**
 * Beinhaltet Funktionen um die Ergebnisdatenbank zu füllen.
 */
public class ResultDBUtils {

	private static SQLConnector sc;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM");
	
	private static void check() throws Exception {
		if(sc == null) {
			sc = SQLFactory.getSQLConnector("result");
		}
	}
	
	public static boolean addNameDataToDB(ArrayList<NameData> nameData) {
		
		try {
			check();
			
			if(nameData == null) return false;
			
			for(NameData nd : nameData) {
				
				String mname = nd.getMediaName();
				String aname = nd.getArtName();
				String date = dateFormat.format(nd.getDate().getTime()) + "-00";
				int type = nd.getType();
				
				if(type != 2) {
					createNameData(mname, aname, date, type);
				}
								
			}
			
			return true;
		}
		catch(Exception e) {
			return false;
		}	
		
	}
	
	/**
	 * Fügt NameData zu DB.
	 * 
	 * @param mediaName String Name des Mediums.
	 * @param artistName String Name des Künstlers.
	 * @param date String Datum
	 * @param type int Typ der NameData
	 * @throws SQLException
	 */
	private static void createNameData(String mediaName, String artistName, String date, int type) throws SQLException {
		
		createMediaName(mediaName);
		createArtistName(artistName);
		
		String query = "INSERT IGNORE INTO namedata(mid, aid, date, type) VALUES (";
		query += "(SELECT mid FROM medianame WHERE mname = \"" + mediaName + "\"), ";
		query += "(SELECT aid FROM artistname WHERE aname = \"" + artistName + "\"), ";
		
		query += "\"" + date + "\", " + type + ");";
		
		sc.insertOrUpdate(query);
		
	}
	
	/**
	 * Erstellt Medien Name.
	 * 
	 * @param mediaName String Name des Mediums.
	 * @throws SQLException 
	 */
	private static void createMediaName(String mediaName) throws SQLException {
		
		String query = "INSERT IGNORE INTO medianame(mname) VALUES (\"" + mediaName + "\"); ";
		sc.insertOrUpdate(query);
		
	}
	
	/**
	 * Erstellt Künstler Name.
	 * 
	 * @param artistName String Name des Künstlers.
	 * @throws SQLException 
	 */
	private static void createArtistName(String artistName) throws SQLException {
		
		String query = "INSERT IGNORE INTO artistname(aname) VALUES (\"" + artistName + "\"); ";
		sc.insertOrUpdate(query);
		
	}
	
}
