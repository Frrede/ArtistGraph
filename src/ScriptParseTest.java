import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.script.ScriptException;

import fu.inf.artgraph.crawler.CrawledData;
import fu.inf.artgraph.crawler.Crawler;
import fu.inf.artgraph.crawler.SPContainer;
import fu.inf.artgraph.crawler.ScriptParser;


public class ScriptParseTest {
	
	public static void main(String[] args) throws IOException, ScriptException, NoSuchMethodException {
		
		
		ScriptParser sp = new ScriptParser();
		
		String js = readFile("jsobject3.js", Charset.defaultCharset());
		
		SPContainer spd = sp.parseJSConfig(js);
		
		Crawler cr = new Crawler(spd);
		CrawledData[] cd = cr.getData();
		
		for(CrawledData c : cd) {
			
			String[] str = c.getTextBlocks();
			for(String s : str) {
				System.out.println(s);
			}
		}
		
	}

	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	
}
