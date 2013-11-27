import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fu.inf.artgraph.db.SQLConnector;
import fu.inf.artgraph.db.SQLFactory;


public class ulanInDB {

	static SQLConnector sc;
	
	public static void main(String[] args) {
		
		boolean yes = true;
		
		try {
			sc = SQLFactory.getSQLConnector("name");
		}
		catch(Exception e) {
			yes = false;
		}
		
		if(yes) {
			File mainFolder = new File("C:/Users/Frede/Desktop/ULAN");
			getFiles(mainFolder);
		}
		
	}
	
	public static void getFiles(File f) {
        File files[];
        if(f.isFile()) {
        	
        	String path = f.getAbsolutePath();
        	try {
        		getNames(path);
        		System.out.println(path);
        	}
        	catch(Exception e) {
        		System.out.println("ERROR!!! " + path);
        	}
        }
        else {
            files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                getFiles(files[i]);
            }
        }
    }

	
	public static void getNames(String path) throws Exception {
		
		String file = readFile(path);
		Document doc = Jsoup.parse(file);
		
		Elements eles = doc.select("table > tbody > tr > td > table > tbody > tr > td > span.page > nobr > a > b");
		
		for(Element ele : eles) {
			
			String nameText = ele.text();
			
			if(!nameText.contains("Top of the ULAN list / hierarchy") && !nameText.contains("Persons, Artists")) {
				
				try {
					inDB(nameText);
				}
				catch(Exception e) {
					System.out.println(nameText + " konnte nicht eingetragen werden.");
				}
				
			}
			
		}
		
		//System.out.println("asd");
		
	}
	
	private static boolean inDB(String nameText) throws Exception {
		
		String[] spl1 = nameText.split(",\\s", 2);
		
		for(String s : spl1) {
			if(s.matches("^[0-9]+$")) {
				System.out.println("Nur Nummer!? -> " + nameText);
				return false;
			}
		}
		
		String name = spl1[0].replaceAll("[.,;:!?]", "");
		
		String compName = "";
		if(spl1.length > 1) {
			compName = spl1[1].replaceAll("[,;:!?]", "");
			compName += " ";
		}
		compName += name;
		int aid = createArtist(compName);
		
		String[] names = name.split("\\s");
		
		createName(names[0], aid);
		
		if(names.length > 1) {
			
			for(int i = 1; i < names.length; i++) {
				
				String n = names[i].replaceAll("[.,;:!?]", "");
				
				createMoreName(n, aid, i-1);
				
			}
		}
		
		if(spl1.length > 1) {
			
			String[] mNames = spl1[1].split("\\s");
			
			for(int i = 0; i < mNames.length; i++) {
				
				String n = mNames[i].replaceAll("[.,;:!?]", "");
				
				createMoreName(n, aid, i + (names.length - 1));
				
			}
			
		}
		
		return true;
	}
	
	public static int createArtist(String name) throws Exception {
		
		String query = "INSERT INTO artist(prefName) VALUES (\"" + name + "\")";

		String[][] nf = sc.insertAndGetNewFields(query);
		int id = Integer.parseInt(nf[0][0]);
		return id;

	}
	
	public static void createNameString(String name) throws Exception {
		
		String query = "INSERT IGNORE INTO stringNames(name) VALUES (\"" + name + "\")";
		
		sc.insertOrUpdate(query);
	}
	
	public static void createName(String name, int aid) throws Exception {
		
		createNameString(name);
		
		String query = "INSERT INTO name(aid, snId) VALUES (" + aid + ", (SELECT snId FROM stringNames WHERE name = \"" + name + "\"))";

		sc.insertOrUpdate(query);
	}
	
	public static void createMoreName(String name, int aid, int position) throws Exception {
		
		createNameString(name);
		
		String query = "INSERT INTO morenames(aid, snId, position) VALUES (" + aid + ", (SELECT snId FROM stringNames WHERE name = \"" + name + "\"), " + position + ");";

		sc.insertOrUpdate(query);
	}
	
	static String readFile(String path) throws IOException {
		Charset encoding = Charset.forName("UTF-8");
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	
}
