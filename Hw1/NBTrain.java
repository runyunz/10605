/**
 * 
 */
// package streaming.nb;
import java.io.*;
import java.util.*;

/**
 * @author runyunz
 *
 */

public class NBTrain {

	private static Vector<String> ys;
	private static Vector<String> ws;
	private static HashMap<String, Double> countMap = new HashMap<String, Double>();
	private static HashSet<String> vocab = new HashSet<String>();
	
	public static void parse(String line) throws Exception {
		String[] res = line.split("\\t");
		
		ys = filterLables(res[0]);
		ws = tokenizeDoc(res[1]);
	
	}
	
	public static Vector<String> filterLables(String str) {
		String[] ylabs = str.split(",");
		Vector<String> res = new Vector<String>();
		for(String w: ylabs) {
			if (w.matches("[CEGM]CAT")) {
				res.add(w);
			}
		}
		return res;
	}
	
	public static void run() {
		for (String y : ys) {
			countNumbers(y, ws);
		}
	}
	
	public static void countNumbers(String y, Vector<String> ws) {
		String allY = "Y=*";
		String someY = "Y=" + y;
		String someYallW = someY + "," + "W=*";
		
		updateMap(someY, 1);
		updateMap(allY, 1);
		
		for (String w: ws) {
			String someYsomeW = someY + "," + "W=" + w;
			updateMap(someYsomeW, 1);
			computeVocab(w);
		}
		
		updateMap(someYallW, ws.size());
		
	}
	
	public static void computeVocab(String w) {
		vocab.add(w);
	}
	
	public static void updateMap(String key, double i) {
		if (countMap.containsKey(key)) {
			double oldCount = countMap.get(key);
			countMap.put(key, oldCount+i);
		}
		else {
			countMap.put(key, i);
		}
	}
	
	public static void printMap() throws Exception{		
		Iterator<String> iter = countMap.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			System.out.println(key + "\t" + countMap.get(key));
		}
		System.out.println("VOCAB"+ "\t" + (double)vocab.size());
	}

	public static Vector<String> tokenizeDoc (String cur_doc) {
		String[] words = cur_doc.split("\\s+");
        Vector<String> tokens = new Vector<String>();
        for (int i = 0; i < words.length; i++) {
        	words[i] = words[i].replaceAll("\\W", "");
        	if (words[i].length() > 0) {
        		tokens.add(words[i]);
        	}
        }
        	return tokens;
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String line;
		if (reader != null) {
			while ((line = reader.readLine()) != null) {
				parse(line);
				run();
			}
		}
		reader.close();
		printMap();
	}

}
