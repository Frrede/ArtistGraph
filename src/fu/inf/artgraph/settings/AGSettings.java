package fu.inf.artgraph.settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Liest Einstellungen aus einer .ini-Datei aus und stellt diese zur Verfügung.
 */
public class AGSettings {
	
	private static AGSettings instance = null;
	
	// Referenznamen Datenbank
	private String ndb_adress;
	private String ndb_port;
	private String ndb_user;
	private String ndb_pwd;
	private String ndb_name;
	
	// Ergebnis Datenbank
	private String rdb_adress;
	private String rdb_port;
	private String rdb_user;
	private String rdb_pwd;
	private String rdb_name;
	
	// Standard-Pfad zu Konfig-Dateien
	private String default_path;
	
	/**
	 * Erstellt eine Instanz von Settings.
	 */
	private AGSettings() throws FileNotFoundException, IOException {
		Properties p = new Properties();
		p.load(new FileInputStream("settings.ini"));
		
		// Referenznamen Datenbank
		ndb_adress = p.getProperty("N_DBAdress");
		ndb_port = p.getProperty("N_DBPort");
		ndb_user = p.getProperty("N_DBUser");
		ndb_pwd = p.getProperty("N_DBPwd");
		ndb_name = p.getProperty("N_DBName");
		
		// Referenznamen Datenbank
		rdb_adress = p.getProperty("R_DBAdress");
		rdb_port = p.getProperty("R_DBPort");
		rdb_user = p.getProperty("R_DBUser");
		rdb_pwd = p.getProperty("R_DBPwd");
		rdb_name = p.getProperty("R_DBName");
		
		// Pfad zu Konfig-Dateien
		default_path = p.getProperty("DefaultPath");
	}
	
	/**
	 * Liefert bestehende Instanz zurück oder lässt neue erstellen.
	 * 
	 * @return AGSettings
	 */
	public static AGSettings getInstance() throws FileNotFoundException, IOException {
		if(instance == null) {
			instance = new AGSettings();
		}
		return instance;
	}
	
	public String getNDBAdress() {
		return ndb_adress;
	}

	public String getNDBPort() {
		return ndb_port;
	}

	public String getNDBUser() {
		return ndb_user;
	}

	public String getNDBPwd() {
		return ndb_pwd;
	}

	public String getNDBName() {
		return ndb_name;
	}
	
	public String getRDBAdress() {
		return rdb_adress;
	}

	public String getRDBPort() {
		return rdb_port;
	}

	public String getRDBUser() {
		return rdb_user;
	}

	public String getRDBPwd() {
		return rdb_pwd;
	}

	public String getRDBName() {
		return rdb_name;
	}
	
	public String getDefaultPath() {
		return default_path;
	}
	
}
