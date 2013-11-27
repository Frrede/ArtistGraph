import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import fu.inf.artgraph.crawler.CrawledData;
import fu.inf.artgraph.crawler.Crawler;
import fu.inf.artgraph.crawler.SPContainer;
import fu.inf.artgraph.crawler.ScriptParser;
import fu.inf.artgraph.tagger.NameData;
import fu.inf.artgraph.tagger.Tagger;
import fu.inf.artgraph.tagger.TaggerGer;


public class tagTest {
	
	public static void main(String[] args) throws Exception {
		
		
		ScriptParser sp = new ScriptParser();
		
		String jsFile = readFile("test.js");
		
		SPContainer spc = sp.parseJSConfig(jsFile);
		
		Crawler cr = new Crawler(spc);
		
		CrawledData[] cd = cr.getData();
		
		Tagger tag = new TaggerGer(cd, null);
		
		NameData[] nd = tag.getNameData();
		
		System.out.println("asd");
		
	}

	static String readFile(String path) throws IOException {
		Charset encoding = Charset.defaultCharset();
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	
}
