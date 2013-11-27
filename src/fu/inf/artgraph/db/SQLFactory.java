package fu.inf.artgraph.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import fu.inf.artgraph.settings.AGSettings;

/**
 * Erzeugt zwei Varianten einer Datenbank-Verbindung.
 * Für die Datenbank mit den Vergleichsnamen und für die Ergebnisse.
 */
public class SQLFactory {
	
	private static AGSettings ags = null;
	private static SQLConnector nameConnector = null;
	private static SQLConnector resultConnector = null;
	
	public static SQLConnector getSQLConnector(String type) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
		
		if(ags == null) {
			ags = AGSettings.getInstance();
		}
		
		// Vergleichsnamen
		if(type.equals("name")) {
			
			if(nameConnector == null) {
				
				String adress = ags.getNDBAdress();
				String port = ags.getNDBPort();
				String user = ags.getNDBUser();
				String pwd = ags.getNDBPwd();
				String dbname = ags.getNDBName();
				
				nameConnector = new SQLConnector(adress, port, user, pwd, dbname);
				
			}
			return nameConnector;
			
		}
		
		// Ergebnisse
		else if(type.equals("result")) {
			
			if(resultConnector == null) {
				
				String adress = ags.getRDBAdress();
				String port = ags.getRDBPort();
				String user = ags.getRDBUser();
				String pwd = ags.getRDBPwd();
				String dbname = ags.getRDBName();
				
				resultConnector = new SQLConnector(adress, port, user, pwd, dbname);
				
			}
			return resultConnector;
			
		}
		
		// Sonst null
		else {
			return null;
		}
		
	}
	
}
