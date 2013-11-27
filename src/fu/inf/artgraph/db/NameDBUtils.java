package fu.inf.artgraph.db;

import java.util.ArrayList;

import fu.inf.artgraph.tagger.NameData;

/**
 * Beinhaltet Funktionen um die Datenbank mit Künstlernamen zu administieren.
 */
public class NameDBUtils {

	private static SQLConnector sc;
	
	private static void check() throws Exception {
		if(sc == null) {
			sc = SQLFactory.getSQLConnector("name");
		}
	}
	
	/**
	 * 
	 * @param manually boolean Ob der Name von Hand hinzugefügt wurde.
	 * @param prefName String Der komplette Name.
	 * @param name String Der Hauptname.
	 * @param moreNames String Die weiteren Namen.
	 * @return boolean
	 */
	public static boolean addArtist(boolean manually, String prefName, String name, String ... moreNames) {
		
		try {
			check();
			
			int aid = createArtist(prefName, manually);
			createName(name, aid);
			if(moreNames != null) {
				for(int i = 0; i < moreNames.length; i++) {
					String mn = moreNames[i];
					createMoreName(mn, aid, i);
				}
			}
			return true;
		}
		catch(Exception e) {
			return false;
		}	
		
	}
	
	/**
	 * 
	 * @param manually boolean Ob der Name von Hand hinzugefügt wurde.
	 * @param compName Der vollständige Name nach dem Schema "Nachname1 Nachname2, Vorname1 Vorname2"
	 * @return boolean
	 */
	public static boolean addArtistByEntireName(boolean manually, String compName) {
		
		ArrayList<String> nameList = new ArrayList<String>();
		
		String[] spl = compName.split(",");
		String[] names = spl[0].split("\\s");
		
		String name = names[0];
		if(spl.length == 1 && names.length > 1) {
			return false;
		}
		if(spl.length == 1 && names.length == 1) {
			return addArtist(manually, name, name);
		}
		
		String fullFirstName = "";
		String fullLastName = name;
		String[] moreNames = spl[1].split("\\s");
		
		for(int i = 1; i < names.length; i++) {
			String n = names[i];
			if(n.length() > 0) {
				nameList.add(n);
				fullLastName += " " + n;
			}
		}
		
		for(int i = 0; i < moreNames.length; i++) {
			String mn = moreNames[i];
			if(mn.length() > 0) {
				nameList.add(mn);
				fullFirstName += mn;
				if(i > 0)  {
					fullFirstName += " ";
				}
			}
		}
		
		String fullName = fullFirstName + fullLastName;
		
		String[] nlArray = nameList.toArray(new String[nameList.size()]);
		return addArtist(manually, fullName, name, nlArray);
	}
	
	private static int createArtist(String name, boolean manually) throws Exception {
		
		String query;
		if(!manually) {
			query = "INSERT INTO artist(prefName) VALUES (\"" + name + "\")";
		}
		else {
			query = "INSERT INTO artist(prefName, autoCreated) VALUES (\"" + name + "\", 1)";
		}

		String[][] nf = sc.insertAndGetNewFields(query);
		int id = Integer.parseInt(nf[0][0]);
		return id;

	}
	
	private static void createNameString(String name) throws Exception {
		
		String query = "INSERT IGNORE INTO stringNames(name) VALUES (\"" + name + "\")";
		
		sc.insertOrUpdate(query);
	}
	
	private static void createName(String name, int aid) throws Exception {
		
		createNameString(name);
		
		String query = "INSERT INTO name(aid, snId) VALUES (" + aid + ", (SELECT snId FROM stringNames WHERE name = \"" + name + "\"))";

		sc.insertOrUpdate(query);
	}
	
	private static void createMoreName(String name, int aid, int position) throws Exception {
		
		createNameString(name);
		
		String query = "INSERT INTO morenames(aid, snId, position) VALUES (" + aid + ", (SELECT snId FROM stringNames WHERE name = \"" + name + "\"), " + position + ");";

		sc.insertOrUpdate(query);
	}
	
}
