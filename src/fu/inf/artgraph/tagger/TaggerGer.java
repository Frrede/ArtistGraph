package fu.inf.artgraph.tagger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import fu.inf.artgraph.crawler.CrawledData;
import fu.inf.artgraph.db.SQLConnector;
import fu.inf.artgraph.db.SQLFactory;

/**
 * Identifiziert die Künstlernamen aus den gecrawlten Daten mit deutschen Texten.
 */
public class TaggerGer extends Tagger {
	
	private AbstractSequenceClassifier<CoreLabel> classifier;
	private SQLConnector sc;
	
	private Set<NameData> foundNames = new HashSet<NameData>(); // HashSet verhindert doppelte Einträge.
	private Set<String> foundNameIDs = new HashSet<String>(); 
	private HashMap<Integer, IDContainer[]> cachedOneNameIDs = new HashMap<Integer, IDContainer[]>();
	
	private ArrayList<String> tokens = new ArrayList<String>();
	private Set<Integer> perPos = new HashSet<Integer>();
	private CrawledData cd;
	
	private TaggerCallback taggerCallback;
	
	// Zum splitten von Inhalt von XML-Tag oder Leerzeichen.
	Pattern rSplit = Pattern.compile("(<(?<tag>[A-Z-]+)[^>]*?>[^<]*</\\k<tag>>)|([^\\p{Space}]+)");
	// Alle Arten von Leerzeichen.
	Pattern rWS = Pattern.compile("\\s");
	// Alle Arten von relevanten Satzzeichen.
	Pattern rPuncts = Pattern.compile("[.,;:!?]");
	// Alle Arten von relevanten Satzzeichen die von Leerzeichen umschlossen sind.
	Pattern rPuncts2 = Pattern.compile("\\s[.,;:!?]\\s");
	// Satzzeichen oder Leerzeichen am Anfang oder Ende des Wortes.
	Pattern rPuncts3 = Pattern.compile("[.,;:!?]|^\\s+|\\s+$");
	// Anführungszeichen (inklusive Bindestrich).
	Pattern rQuotes = Pattern.compile("^['´`(\\-\\s]{1,2}|['´`)\\-\\s]{1,2}$|\"");
	// XML-Tags von Stanford NER.
	Pattern rTags = Pattern.compile("<[A-Z]-[A-Z]{3,4}>.+</[A-Z]-[A-Z]{3,4}>");
	// Schließender XML-Tag von Stanford NER.
	Pattern rTagRests = Pattern.compile("\\s?</?[A-Z]-[A-Z]{3,4}>\\s?");
	// Text mit zwei großgeschriebenen Namen ohne Leerzeichen.
	Pattern rTwoNames = Pattern.compile("^([A-Z][a-z]+)([A-Z][a-z]+)$");
	
	/*
	 * Bezeichnungen, die einen Künstler identifizieren können.
	 */
	private String rTitles = "(.*)künstler(s|in|n)?|(.*)gra(ph|f)iker(s|in|n)?|(.*)maler(s|in|n)?|" +
			"(.*)bildhauer(s|in|n)?|(.*)kupferstecher(s|in|n)?|(.*)graveur(s|in|n)?|" +
			"(.*)keramiker(s|in|n)?|(.*)lithogra(ph|f|n)(s|in)?|(.*)holzschnitzer(s|in|n)?|" +
			"(.*)zeichner(s|in|n)?|(.*)(ph|f)otograp(ph|f)(s|in|n)?";
	
	private Pattern pTitles = Pattern.compile(rTitles, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	// Wörter, die zwischen Bezeichner und Name stehen können.
	private String rFill = "wie";
	private Pattern pFill = Pattern.compile(rFill);
	
	/*
	 * Wörter, die auf keinen Fall Name sind.
	 */
	private String rNoNames = "der|die|das|ein|eine";
	private Pattern pNoNames = Pattern.compile(rNoNames, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/**
	 * @param crawledData CrawledData[] Die gecrawlten Texte vom Crawler.
	 * @throws Exception 
	 */
	public TaggerGer(CrawledData[] crawledData, TaggerCallback taggerCallback) throws Exception {
		super(crawledData);
		classifier = NERFactory.getClassifier();
		sc = SQLFactory.getSQLConnector("name");
		this.taggerCallback = taggerCallback;
	}

	@Override
	public NameData[] getNameData() {
		
		int length = crawledData.length;
		updateStatus(length, 0, 0, 0);		
		
		if(length == 0) return null;
		
		for(int i = 0; i < length; i++) {
			
			cd = crawledData[i];
			
			// Für jedes crawledData wird neu angefangen nach Künstlern zu suchen.
			foundNameIDs.clear();
			cachedOneNameIDs.clear();
			
			String[] textBlocks = cd.getTextBlocks();
			for(int t = 0; t < textBlocks.length; t++) {
				
				String text = textBlocks[t];
				
				if(text.length() == 0) continue;
				
				tokens.clear();
				tokens.add("");
				tokens.add("");
				perPos.clear();
				
				String taggedText = classifier.classifyWithInlineXML(text);
				
				// Alle Satzzeichen, auf denen das folgende Wort großgeschrieben sein könnte abrücken.
				taggedText = rPuncts.matcher(taggedText).replaceAll(" $0 ");
				
				Matcher m = rSplit.matcher(taggedText);
				while(m.find()) {
					
					// Anführungszeichen am Anfang oder Ende entfernen.
					String token = rQuotes.matcher(m.group()).replaceAll("");
					
					if(rTags.matcher(token).matches()) {
						if(token.contains("<I-PER>")) {
							token = token.replace("<I-PER>", "");
							token = token.replace("</I-PER>", "");
							token = rPuncts2.matcher(token).replaceAll("");
							token = rTwoNames.matcher(token).replaceAll("$1 $2");
							perPos.add(tokens.size());
						}
						else {
							token = "";
						}
					}
					
					tokens.add(token);
				}
				
				// Namen mit DB vergleichen.
				findNamesInDB();
				
				// Vor Namen nach Künstler-Bezeichnung suchen.
				findNamesByTitle();
				
				// Restlichen getaggten Namen entfernen.
				removeRemainingTokens();
				
				// Restlichen Wörter nach Namen durchsuchen.
				searchRemainingWords();
				
				updateStatus(length, i+1, textBlocks.length, t+1);
			}
			
		}
		
		return foundNames.toArray(new NameData[foundNames.size()]);
	}
	
	/**
	 * Sucht in Datenbank nach Künstlernamen
	 * 
	 * @param tokens String[] Array aller tokens.
	 * @param pos Integer[] 
	 * @param cd CrawledData
	 */
	private void findNamesInDB() {

		Integer[] aPos = perPos.toArray(new Integer[perPos.size()]);
		
		for(int p : aPos) {
			
			String foundName = tokens.get(p);
			String[] splNames = rWS.split(foundName);
			int len = splNames.length;
			
			String name = splNames[len-1];
			String[] moreNames = null;
			
			if(len > 1) {
				moreNames = new String[len-1];
				for(int m = 0; m < len-1; m++) {
					moreNames[m] = splNames[m];
				}
			}
			
			// Von DB vorsortiert nach "count"
			IDContainer[] idca = getNameIDFromDB(name, moreNames);
			
			if(idca == null) continue;
			
			if(moreNames != null) {
				
				IDContainer best = null;
				int mnSize = moreNames.length;
				
				// Keine Namen kleingeschrieben und länger als ein Zeichen?
				boolean special = true;
				for(String mn : moreNames) {
					if(mn.length() > 1 && !Character.isUpperCase(mn.codePointAt(0))) {
						special = false;
						break;
					}
				}
				special = special && idca.length == 1; // Nur ein Ergebnis?
				
				for(IDContainer idc : idca) {
					
					if(idc.isDisabled()) {
						continue;
					}
					
					int count = idc.getCount();
					int max = idc.getMax();
					
					/*
					 * Entweder müssen alle Namen gefunden werden, wobei nach einem Wort zu
					 * viel gesucht werden darf.
					 * (z.B. Peter Hans Müller wenn der Künstler Peter Müller heißt.)
					 * 
					 * Oder wenn keine kleingeschriebenen Namen vorkommen (z.B. van, von, der,..), die
					 * Namen nicht nur aus einem Zeichen bestehen (keine Abkürzungen) und es
					 * nur ein Ergebnis gibt reicht es, wenn bis zu zwei Namen zu wenig gesucht
					 * wurden. (z.B. Peter Müller anstelle von Peter Herbert Hans Müller.)
					 * 
					 */
					if((count == max && mnSize - max <= 1) || (special && max - count <= 2)) {
						best = idc;
						break;
					}
				}
				
				if(best != null) {
					
					if(!foundNameIDs.contains(best.getAId())) {
						String mname = cd.getName();
						Calendar date = cd.getDate();
						foundNames.add(new NameData(mname, date, best.getName(), 1));
						
						foundNameIDs.add(best.getAId());
					}
					
					tokens.set(p, "");
					perPos.remove(p);
					
				
				}
			}
			// Es wird nur nach Nachname gesucht.
			else {
				cachedOneNameIDs.put(p, idca);
			}
		}
		
		// Schauen was mit den einzelnen Namen passiert.
		
		Set<Integer> ks = cachedOneNameIDs.keySet();
		Integer[] ksi = ks.toArray(new Integer[ks.size()]);
		
		for(int k : ksi) {
			IDContainer[] idca = cachedOneNameIDs.remove(k);
			
			IDContainer best = null;
				
			boolean isFound = false;
			for(int i = 0; i < idca.length; i++) {
				
				IDContainer idc = idca[i];
				
				if(idc.isDisabled()) {
					continue;
				}
				
				if(best == null && idc.hasShortname()) {
					best = idc;
					isFound = true;
				}
				
				// Wenn id früher schon gefunden dann nicht weitermachen.
				if(foundNameIDs.contains(idc.getAId())) {
					tokens.set(k, "");
					perPos.remove(k);
					isFound = true;
					best = null;
					break;
				}
			}
			
			// Wenn das letzte count = max = 0 hat dann das nehmen.
			IDContainer idcLast = idca[idca.length-1];
			if(!isFound && !idcLast.isDisabled() && idcLast.getCount() == 0 && idcLast.getMax() == 0) {
				best = idcLast;
			} 
			
			if(best != null) {
				
				String mname = cd.getName();
				Calendar date = cd.getDate();
				foundNames.add(new NameData(mname, date, best.getName(), 1));
				
				tokens.set(k, "");
				perPos.remove(k);
				foundNameIDs.add(best.getAId());
				
			}
			
		}
		
	}
	
	/**
	 * Liefert die IDs der Künstler, die Anzahl der gefundenen weiteren Namen (count) und die
	 * maximale Anzahl an weiteren Namen (max). Die Ergebnisse sind nach count sortiert mit dem
	 * höchsten zuerst.
	 * 
	 * @param name String Nachname
	 * @param moreNames String[] Alle weiteren Namen
	 * @return IDContainer[]
	 */
	private IDContainer[] getNameIDFromDB(String name, String[] moreNames) {
		
		name = rPuncts3.matcher(name).replaceAll("");
		
		// Für Genitiv
		String namePart;
		int nLen = name.length() - 1;
		if(name.charAt(nLen) == 's') {
			String name2 = name.substring(0, nLen);
			namePart = "(s1.name = \"" + name + "\" OR s1.name = \"" + name2 + "\")";
		}
		else {
			namePart = "s1.name = \"" + name + "\"";
		}
		
		String query = "SELECT " +
							"IF(m.snId <> \"\", count(n.aid), 0) AS count, " +
							"(SELECT LENGTH(a.prefName) - LENGTH(REPLACE(a.prefName, ' ', ''))) AS max, " +
							"n.aid, " +
							"a.prefName, " +
							"n.shortname, " +
							"a.disabled " +
						"FROM name n LEFT JOIN morenames m ON n.aid = m.aid " +
							"LEFT JOIN artist a ON n.aid = a.aid " +
							"LEFT JOIN stringnames s1 ON n.snid = s1.snid " +
							"LEFT JOIN stringnames s2 ON s2.snid = m.snid " +
						"WHERE " + namePart;
		
		if(moreNames != null) {
			
			query += " AND ( ";
			
			for(int i = 0; i < moreNames.length; i++) {
				if(i > 0) {
					query += " OR ";
				}
				String mn = moreNames[i];
				mn = rPuncts3.matcher(mn).replaceAll("");
				query += "s2.name = \"" + mn + "\"";
			}
			
			query += ")";
		}
		
		query += " GROUP BY n.aid ORDER BY count DESC";
		
		String[][] strResult = null;
		try {
			strResult = sc.getStringArrayByQuery(query);
		}
		catch(Exception e) {
			
		}
		
		if(strResult == null || strResult.length == 0) {
			return null;
		}
		
		int resLen = strResult.length;
		IDContainer[] idc = new IDContainer[resLen];
		for(int i = 0; i < strResult.length; i++) {
			String[] stra = strResult[i];
			
			String snId = stra[2];
			int count = Integer.parseInt(stra[0]);
			int max = Integer.parseInt(stra[1]);
			String prefName = stra[3];
			boolean shortname = Integer.parseInt(stra[4]) > 0 ? true : false;
			boolean disabled = Integer.parseInt(stra[5]) > 0 ? true : false;
			
			idc[i] = new IDContainer(snId, count, max, prefName, shortname, disabled);
		}
		
		return idc;
	}
	
	/**
	 * Findet getaggte Namen über die Bezeichnung davor.
	 * 
	 * @param tokens String[] Array aller tokens.
	 * @param pos Integer[]
	 * @param cd CrawledData
	 */
	private void findNamesByTitle() {
		
		Integer[] aPos = perPos.toArray(new Integer[perPos.size()]);
		
		for(int p : aPos) {
			
			String before;
			int befPos;
			
			if(pFill.matcher(tokens.get(p-1)).matches()) {
				before = tokens.get(p-2);
				befPos = 2;
			}
			else {
				before = tokens.get(p-1);
				befPos = 1;
			}
			
			// Künstler-Bezeichnung gefunden
			if(pTitles.matcher(before).matches()) {
				
				String name = tokens.get(p);
				String mname = cd.getName();
				Calendar date = cd.getDate();
				foundNames.add(new NameData(mname, date, name, 2));
				
				tokens.set(p, "");
				tokens.set(p - befPos, "");
				perPos.remove(p);
				
			}
		}
		
	}
	
	/**
	 * Entfernt die restlichen Tokens.
	 * 
	 * @param tokens String[] Array aller tokens.
	 * @param pos Integer[]
	 */
	private void removeRemainingTokens() {
		
		Integer[] aPos = perPos.toArray(new Integer[perPos.size()]);
		for(int p : aPos) {
			tokens.set(p, "");
			perPos.remove(p);
		}
		
	}
	
	/**
	 * Sucht unter den restlichen großgeschriebenen Wörtern nach Namen in DB.
	 * Es müssen mindestens zwei Namen sein.
	 * 
	 * @param tokens String[] Array aller tokens.
	 * @param pos Integer[]
	 * @param cd CrawledData
	 */
	private void searchRemainingWords() {
		
		ArrayList<String> names = new ArrayList<String>();
		boolean complete = false;
		
		for(int i = 0; i < tokens.size(); i++) {
			
			if(complete) {
				
				// Namen suchen wenn mindestens zwei.
				if(names.size() >= 2) {
					
					boolean found = false;
					
					/* 
					 * Es wird zuerst die maximale Anzahl an Wörtern genommen. Das letzte ist 
					 * immer der Nachname. Danach wird das ganze ohne das letzte Wort
					 * wiederholt, da dieses eventuell kein Name ist. Das wird solange gemacht,
					 * bis keine zwei Namen mehr übrig sind.
					 */
					while(names.size() >= 2) {
						
						int pos = names.size() - 1;
						String name = names.get(pos);
						String[] moreNames = names.subList(0, pos).toArray(new String[pos-1]);
						
						IDContainer[] idca = getNameIDFromDB(name, moreNames);
						
						if(idca == null) {
							names.remove(pos);
							continue;
						}
						
						for(IDContainer idc : idca) {
							
							if(idc.isDisabled()) {
								continue;
							}
							
							String snId = idc.getAId();
							int count = idc.getCount();
							int max = idc.getMax();
							
							/*
							 * Gibt es einen Fund, der alle gesuchten Namen beinhaltet, der aus
							 * mindestens drei Namen besteht und keine Namen fehlen wird dieser
							 * sofort genommen.
							 */
							if(max - count <= 1 && max >= 2 && !foundNameIDs.contains(snId)) {
								
								String aname = idc.getName();
								String mname = cd.getName();
								Calendar date = cd.getDate();
								foundNames.add(new NameData(mname, date, aname, 3));
								
								found = true;
								break;
							}
							
						}
						
						if(found) {
							break;
						}
						
						names.remove(pos);
					}
					
				}
				
				complete = false;
				names.clear();
			}
			
			String token = tokens.get(i);
			
			// Name muss mindestens zwei Buchstaben haben.
			if(token.length() < 2) {
				complete = true;
				continue;
			}
			
			// Zur Sicherheit falls reste vom Tagging geblieben sind.
			token = rTagRests.matcher(token).replaceAll("");
			
			boolean upper;
			if(token.length() > 0) {
				upper = Character.isUpperCase(token.codePointAt(0));
			}
			else {
				upper = false;
			}
			
			// Wenn groß geschrieben und nicht aus der "NoNames"-Liste.
			if(upper && !pNoNames.matcher(token).matches()) {
				
				// Check ob das Wort in Versalien geschrieben ist.
				boolean isVers = true;
				for (char c : token.toCharArray()) {
				    if (!Character.isUpperCase(c)) {
				    	isVers = false;
				        break;
				    }
				}
				
				// Nur nehmen wenn nicht in Versalien.
				if(!isVers) {
					names.add(token);
				}
			}
			else {
				complete = true;
				continue;
			}
		}
		
	}
	
	/**
	 * Übergibt den aktuellen Status.
	 * 
	 * @return boolean
	 */
	private boolean updateStatus(int all, int ready, int subAll, int subReady) {
		if(taggerCallback == null) return false;
		
		TaggerMessage tm = new TaggerMessage(all, ready, subAll, subReady);
		taggerCallback.sendStatus(tm);
		
		return true;
	}
	
}
