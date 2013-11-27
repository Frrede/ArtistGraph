import fu.inf.artgraph.crawler.TagRange;
import fu.inf.artgraph.crawler.URLGenerator;


public class URLGenTest {
	
	public static void main(String[] args) {
		
		String url = "Erst $a$, dann $b$ und dann noch $c$.";
		
		TagRange[] tr = {
				new TagRange('a', 2, 3),
				new TagRange('b', 3, 5),
				new TagRange('c', 4, 7)
		};
		
		URLGenerator ug = new URLGenerator(url, tr);
		
		while(ug.hasNext()) {
			
			System.out.println(ug.next());
			
		}
		
	}

}
