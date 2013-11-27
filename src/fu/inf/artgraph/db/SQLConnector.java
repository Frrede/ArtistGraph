package fu.inf.artgraph.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Klasse für die Verbindung mit der MySQL-Datenbank
 */
public class SQLConnector {
	
    private Connection conn = null;
	
	// Server-Adresse
	private String adress;
	// Port
	private String port;
	// Benutzer
	private String user;
	// Passwort
	private String pwd;
	// Datenbank-Name
	private String dbname;
	
	public SQLConnector(String adress, String port, String user, String pwd, String dbname)
			throws SQLException, FileNotFoundException, IOException, ClassNotFoundException {
		try {
			this.adress = adress;
			this.port = port;
			this.user = user;
			this.pwd = pwd;
			this.dbname = dbname;
			
			getConnection();
		}
		catch (SQLException e) {
			throw e;
		}
	}
	
	private void getConnection()
		throws SQLException, FileNotFoundException, IOException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://" + adress + ":" + port + "/" + dbname
				+ "?user=" + user + "&password=" + pwd + "&characterEncoding=utf-8&useUnicode=true");
	}
	
	/**
	 * Für "SELECT"
	 */
	public String[][] getStringArrayByQuery(String query) throws SQLException {
		checkConnection();
		if(conn != null) {
			Statement stmt;
			try {
				stmt = conn.createStatement();
				ResultSet resSt = stmt.executeQuery(query);
				ArrayList<String[]> al = new ArrayList<String[]>();
				int rowCount = resSt.getMetaData().getColumnCount();
				while(resSt.next()) {
					String[] s = new String[rowCount];
					for(int i = 0; i < rowCount; i++) {
						s[i] = resSt.getString(i+1);
					}
					al.add(s);
				}
				String[][] ret = new String[al.size()][];
				return al.toArray(ret);
			}
			catch(SQLException e) {
				throw e;
			}
		}
		else {
			throw new SQLException();
		}
	}
	
	/**
	 * Für "INSERT"
	 * Liefert die neu erstellten Felder
	 */
	public String[][] insertAndGetNewFields(String query) throws SQLException {
		checkConnection();
		if(conn != null) {
			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
				ResultSet resSt = stmt.getGeneratedKeys();
				ArrayList<String[]> al = new ArrayList<String[]>();
				int rowCount = resSt.getMetaData().getColumnCount();
				while(resSt.next()) {
					String[] s = new String[rowCount];
					for(int i = 0; i < rowCount; i++) {
						s[i] = resSt.getString(i+1);
					}
					al.add(s);
				}
				String[][] ret = new String[al.size()][];
				return al.toArray(ret);
			}
			catch(SQLException e) {
				throw e;
			}
		}
		else {
			throw new SQLException();
		}
	}

	/**
	 * Für "INSERT" oder "UPDATE"
	 */
	public void insertOrUpdate(String query) throws SQLException {
		checkConnection();
		if(conn != null) {
			Statement stmt;
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate(query);
			}
			catch(SQLException e) {
				throw e;
			}
		}
		else {
			throw new SQLException();
		}
	}
	
	/**
	 * Verbindung überprüfen und bei Bedarf neu aufbauen
	 */
	private void checkConnection() {
		if(conn != null) {
			try {
				if(!conn.isValid(0)) {
					getConnection();
				}
			}
			catch(Exception e) {
				
			}
		}
	}
}
