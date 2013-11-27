import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;



public class artmLesen {
	
	public static void main(String[] args) throws InterruptedException {
	
		String find = "div class='ergebnisbox'";
		String error = "headline in";
		
		String urlPath = "http://www.art-magazin.de/div/heftarchiv";
		
		int start = 1980;
		int end = 2012;
		
		for(int y = start; y <= end; y++) {
			
			for(int m = 1; m <= 12; m++) {
				
				String path = "C:/Users/Frede/Desktop/ART/" + y;
				File theDir = new File(path);
				if (!theDir.exists()) theDir.mkdir();
				
				path = "C:/Users/Frede/Desktop/ART/" + y + "/" + m;
				theDir = new File(path);
				if (!theDir.exists()) theDir.mkdir();
				
				int count = 0;
				boolean ok = false;
				boolean err = false;
				while(count < 10 && !ok) {
					
					err = false;
					
					System.out.println(count);
					
					URL url;
					File file = null;
					 
					try {
						// get URL content
						url = new URL(urlPath + "/" + y + "/" + m);
						URLConnection conn = url.openConnection();
			 
						// open the stream and put it into BufferedReader
						BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "ISO-8859-1"));
			 
						String inputLine;
			 
						//save to this filename
						String fileName = "C:/Users/Frede/Desktop/ART/" + y + "/" + m + "/index.html";
						file = new File(fileName);
			 
						if (!file.exists()) {
							file.createNewFile();
						}
			 
						//use FileWriter to write file
						//FileWriter fw = new FileWriter(file.getAbsoluteFile());
						//BufferedWriter bw = new BufferedWriter(fw);
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile()), "ISO-8859-1"));
			 
						while ((inputLine = br.readLine()) != null) {
							if(inputLine.contains(error)) {
								err = true;
								ok = false;
							}
							if(inputLine.contains(find) && !err) {
								ok = true;
							}
									
							bw.write(inputLine);
							
						}
			 
						bw.close();
						br.close();
			 
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					if(!ok && file != null) {
						file.delete();
						Thread.sleep(1000);
					}
					count++;
				}
				
				if(!ok) {
					System.out.println("!!!!! " + path + " !!!!!");
				}
				else {
					System.out.println(path + " OK!");
				}
				
			}
			
		}
		
		
	}

}
