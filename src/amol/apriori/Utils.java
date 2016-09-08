package amol.apriori;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Utils {

	static ArrayList<String> getLinesFromFile(String filename) throws Exception{
		BufferedReader br;
		try{
			br = new BufferedReader(new FileReader(filename));
		}
		catch(FileNotFoundException e){
			return null;
		}
		ArrayList<String> lines = new ArrayList<String>();
		
		String s = br.readLine();
		while((s != null) && (!s.equals(""))){
			lines.add(s);
			s = br.readLine();
		}
		br.close();
		return lines;
	}
	
}
