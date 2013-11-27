import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fu.inf.artgraph.db.NameDBUtils;
import fu.inf.artgraph.export.GraphExporter;
import fu.inf.artgraph.tagger.NameData;

public class test {
	
	public static void main(String[] args) throws Exception {
		
		
		String path = "C:/Users/Frede/Desktop/allDates";
		
		GraphExporter.saveAllAsGML(path, 1973, 3, 2013, 12);
		
	}
	

}
