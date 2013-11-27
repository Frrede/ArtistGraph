import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;


public class dbRest {
	
	public static void main(String[] args) throws IOException {
		
		String path = "C:/Users/Frede/Desktop/fehler.txt";
		
		String file = readFile(path);
		
		String[] lines = file.split("\n");
		
		for(String line : lines) {
			
			if(line.contains("konnte nicht eingetragen werden.") && line.length() > 0) {
				
				line = line.replace(" konnte nicht eingetragen werden.", "");
				line = line.replace("\n", "").replace("\r", "");
				
				System.out.println(line);
				
			}
			
		}
		
	}

	
	
	static String readFile(String path) throws IOException {
		Charset encoding = Charset.defaultCharset();
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	
}
