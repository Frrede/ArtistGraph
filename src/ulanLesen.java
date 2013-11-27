import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class ulanLesen {
	
	public static void main(String[] args) {
		
		String bss[] = {"E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
		int ends[] = {32, 67, 109, 111, 13, 37, 69, 87, 180, 37, 26, 109, 4, 91, 164, 61, 8, 43, 69, 1, 8, 17};
		
		
		
		for(int m = 0; m < ends.length; m++) {
			
			
			int start = 1;
			int end = ends[m];
			String bs = bss[m];
			
			String theUrl = "http://www.getty.edu/vow/ULANHierarchy?find=&role=&nation=&topid=500000002&subjectid=-1&letter=" + bs + "&page=";
			
			for(int i = start; i <= end; i++) {
				
				
				URL url;
				 
				try {
					// get URL content
					url = new URL(theUrl + i);
					URLConnection conn = url.openConnection();
		 
					// open the stream and put it into BufferedReader
					BufferedReader br = new BufferedReader(
		                               new InputStreamReader(conn.getInputStream()));
		 
					String inputLine;
		 
					//save to this filename
					String fileName = "C:/Users/Frede/Desktop/ULAN/" + bs + "/" + i + ".html";
					File file = new File(fileName);
		 
					if (!file.exists()) {
						file.createNewFile();
					}
		 
					//use FileWriter to write file
					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
		 
					while ((inputLine = br.readLine()) != null) {
						bw.write(inputLine);
					}
		 
					bw.close();
					br.close();
		 
					System.out.println(bs + ": " + i + " done");
		 
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
			}
			
		}

		
		
		
		
		
		
		

		
		
		
		
	}

}
