package fu.inf.artgraph.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Liest mit Hilfe von JSOUP Daten aus einer Webseite aus und benötigt hierfür einen SPContainer.
 */
public class Crawler {
	
	private static int RETRY_CONNECTION = 10;
	private static int TIMEOUT_MS = 5000;
	
	CrawlerCallback crawlerCallback;
	private int urlCount = 0;
	private int readyCount = 0;
	private int errorCount = 0;
	
	private SPContainer spContainer;
	private String spContName;
	private CrawledData[] cachedCD = null;
	
	/**
	 * @param spContainer SPContainer beschreibt, wie und wo welche Daten ausgelesen werden sollen.
	 */
	public Crawler(SPContainer spContainer) {
		this(spContainer, null);
	}
	
	/**
	 * @param spContainer SPContainer beschreibt, wie und wo welche Daten ausgelesen werden sollen.
	 * @param crawlerCallback CrawlerCallback Wird aufgerufen um aktuellen Status zu aktualisieren.
	 */
	public Crawler(SPContainer spContainer, CrawlerCallback crawlerCallback) {
		this.spContainer = spContainer;
		this.spContName = spContainer.getName();
		this.crawlerCallback = crawlerCallback;
	}
	
	/**
	 * Crawlt die Daten anhand von spContainer von einer Webseite.
	 * 
	 * @param ignoreCache boolean Daten neu laden obwohl sie schon vorliegen.
	 * @return CrawledData
	 * @throws IOException 
	 */
	public CrawledData[] getData(boolean ignoreCache) throws IOException {
		
		// Gecachte Daten zurückgeben
		if(cachedCD != null && ignoreCache == false) {
			return cachedCD;
		}
		
		SPElements[] spElements = spContainer.getSPElements();
		ArrayList<CrawlerTask> crawle = new ArrayList<CrawlerTask>();
		ArrayList<CrawledData> crawledData = new ArrayList<CrawledData>();
		
		String spUrl = spContainer.getUrl();
		TagRange[] spTr = spContainer.getTagRange();
		
		if(spTr == null) {
			crawle.add(new CrawlerTask(spUrl, -1, -1, spElements));
			urlCount++;
		}
		else {
			// URL Generator
			URLGenerator ug = new URLGenerator(spContainer.getUrl(), spContainer.getTagRange());
			while(ug.hasNext()) {
				String url = ug.next();
				crawle.add(new CrawlerTask(url, -1, -1, spElements));
				urlCount++;
			}
		}
		
		// Alles abarbeiten und neue Elemente hinzufügen.
		while(!crawle.isEmpty()) {
			// Status senden
			updateStatus();
			CrawlerTask ct = crawle.remove(0);
			
			String url = ct.getUrl();
			int prevYear = ct.getYear();
			int prevMonth = ct.getMonth();
			SPElements[] spEles = ct.getSPElements();
			
			// Lade die URL!
			boolean docNotReady = true;
			Document doc = null;
			for(int r = 0; r < RETRY_CONNECTION && docNotReady == true; r++) {
				docNotReady = false;
				try {
					doc = Jsoup.connect(url).timeout(TIMEOUT_MS).get();
				}
				catch(IOException e) {
					docNotReady = true;
					System.out.println(url + " nicht geladen (" + r + ")");
				}
			}
			
			// Weitermachen wenn Verbindung nicht geklappt hat.
			if(doc == null)  {
				errorCount++;
				continue;
			}
			
			for(SPElements spEle : spEles) {
				
				Elements eles = doc.select(spEle.getSelectorAll());
				
				// Elemente validieren.
				Pattern valPat = spEle.getValidateRegex();
				String valSubSel = spEle.getValidateSubSelector();
				if(valPat != null && valSubSel != null) {
					
					for(int i = 0; i < eles.size(); i++) {
						Element ele = eles.get(i);
						Elements subEles = subSelect(ele, valSubSel);
						if(subEles.size() > 0) {
							String valText = subEles.get(0).outerHtml();
							Matcher m = valPat.matcher(valText); 
							// Elemente entfernen auf die Pattern nicht passt.
							if(!m.matches()) {
								eles.remove(i);
								i--;
							}
						}
					}
					
				}
				
				if(eles.size() == 0) {
					continue;
				}
				
				// Datum vom übergeordneten CrawlerTask.
				int globYear = prevYear;
				int globMonth = prevMonth;
				
				// Jahr und Monat überschreiben wenn vorhanden. Wird im gesamten Dokument gesucht.
				SPSelector spGlobYear = spEle.getGlobalYear();
				if(spGlobYear != null) {
					globYear = getIntFromElement(doc, spGlobYear);
				}
				
				SPSelector spGlobMonth = spEle.getGlobalMonth();
				if(spGlobMonth != null) {
					globMonth = getIntFromElement(doc, spGlobMonth);
				}
				
				// Elemente abarbeiten.
				for(Element ele : eles) {
					
					SPTask[] tasks = spEle.getSPTask();
					
					for(SPTask task : tasks) {
						
						int year = globYear;
						int month = globMonth;
						
						// Datum überschreiben falls vorhanden.
						SPSelector spYear = task.getTaskYear();
						if(spYear != null) {
							year = getIntFromElement(ele, spYear);
						}
						
						SPSelector spMonth = task.getTaskMonth();
						if(spMonth != null) {
							month = getIntFromElement(ele, spMonth);
						}
						
						// Parsen. Nur wenn Jahr gesetzt wurde.
						SPSelector[] spParse = task.getTaskParse();
						if(year != -1 && spParse != null) {
							
							ArrayList<String> parsedText = new ArrayList<String>();
							
							for(SPSelector spp : spParse) {
								
								String pSel = spp.getSelector();
								Modifier pMod = spp.getModifier();
								
								Elements parsEles;
								if(pSel.equals("")) {
									parsEles = new Elements(ele);
								}
								else {
									parsEles = ele.select(pSel);
								}
								
								for(Element parsEle : parsEles) {
									String text = parsEle.text();
									if(pMod != null) {
										text = pMod.getResult(text);
									}
									if(text.length() > 3) {
										parsedText.add(text);
									}
								}
								
							}
							
							// Geparsten Text hinzufügen
							if(parsedText.size() > 0) {
								String[] ptArray = parsedText.toArray(new String[parsedText.size()]);
								crawledData.add(new CrawledData(spContName, year, month, ptArray));
							}
						}
						
						// Follow
						SPSelector[] spFollow = task.getTaskFollow();
						SPElements[][] spFolEles = task.getSPElements();
						if(spFollow != null && spFolEles != null) {
							
							int folLength = spFollow.length;
							if(folLength == spFolEles.length) {
								
								for(int i = 0; i < folLength; i++) {
									
									SPSelector spf = spFollow[i];
									String fSel = spf.getSelector();
									Modifier fMod = spf.getModifier();
									Elements parsEles = ele.select(fSel);
									
									for(Element parsEle : parsEles) {
										String fUrl = parsEle.absUrl("href");
										if(fMod != null) {
											fUrl = fMod.getResult(fUrl);
										}
										crawle.add(new CrawlerTask(fUrl, year, month, spFolEles[i]));
										urlCount++;
									}
									
								}
								
							}
						}
						
					}
				}
			}
			
			// Fertig!
			readyCount++;
			
		}
		
		// Status senden
		updateStatus();
		cachedCD = crawledData.toArray(new CrawledData[crawledData.size()]);
		return cachedCD;
	}
	
	/**
	 * Per default ist der Cache aktiviert.
	 * 
	 * @return CrawledData
	 */
	public CrawledData[] getData() throws IOException {
		return getData(false);
	}
	
	/**
	 * Sucht mit Selektor in JSOUP Elements und gib Element selber zurück, wenn der Selektor
	 * ein leerer String oder null ist.
	 * 
	 * @param eles Elements JSOUP Elemente in dem nach Selektor gesucht werden soll.
	 * @param subSelector String Der Selektor.
	 * @return Elements Neue JSOUP Elemente.
	 */
	private Elements subSelect(Elements eles, String subSelector) {
		if(subSelector.equals("") || subSelector == "null" || subSelector == null) {
			return eles;
		}
		else {
			return eles.select(subSelector);
		}
	}
	
	/**
	 * subSelect für einzelnes Element.
	 * 
	 * @param ele Element Einzelnes JSOUP Element.
	 * @param subSelector String Der Selektor.
	 * @return Elements Neue JSOUP Elemente.
	 */
	private Elements subSelect(Element ele, String subSelector) {
		Elements eles = new Elements(ele);
		return subSelect(eles, subSelector);
	}
	
	/**
	 * Parst int aus HTML-Code eines JSOUP Elements.
	 * 
	 * @param ele Element JSOUP Element
	 * @param sel SPSelector
	 * @return int
	 */
	private int getIntFromElement(Element ele, SPSelector sel) {
		
		Elements els = subSelect(ele, sel.getSelector());
		if(els.size() == 0) {
			return -1;
		}
		
		String text = els.get(0).outerHtml();
		Modifier mod = sel.getModifier();
		if(mod != null) {
			text = mod.getResult(text);
		}
		
		int res = -1;
		try {
			res = Integer.parseInt(text);
		}
		catch(NumberFormatException e) {
			return -1;
		}
		return res;
	}
	
	/**
	 * Übergibt den aktuellen Status.
	 * 
	 * @return boolean
	 */
	private boolean updateStatus() {
		if(crawlerCallback == null) return false;
		
		CrawlerMessage cm = new CrawlerMessage(urlCount, readyCount, errorCount);
		crawlerCallback.sendStatus(cm);
		
		return true;
	}
}
