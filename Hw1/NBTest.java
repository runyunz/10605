/**
 * 
 */
// package streaming.nb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

/**
 * @author runyunz
 *
 */



public class NBTest {
	private enum LABEL {CCAT, ECAT, GCAT, MCAT}
	private static HashMap<String, Double> countMap = new HashMap<String, Double>();
	private static Vector<String> ws;
	private static Vector<Vector<String>> wsList = new Vector<Vector<String>>();
	private static double vocabSize;
	
	public static void saveCounts(String str) {
		String[] res = str.split("\\t");
		countMap.put(res[0], Double.parseDouble(res[1]));
	}
	
	public static void parseW(String line) throws Exception {
		String[] res = line.split("\\t");
		ws = tokenizeDoc(res[1]);
	}
	
	public static void run() {
		double maxPred = -Double.MAX_VALUE;
		String maxLabel = "";
		for (LABEL label : LABEL.values()) {
			double res = compute(label);
			if (res > maxPred) {
				maxPred = res;
				maxLabel = label.name();
			}
		}
		System.out.println(maxLabel + "\t" + String.valueOf(maxPred));
	}
	
	public static double compute(LABEL label) {
		double resY = computeY(label);
		double resW = computeW(label);
		return (resW + resY);
	}
	
	public static double computeY(LABEL label) {
		String someY = "Y=" + label.name();
		String allY = "Y=*";
		double res = Math.log((countMap.get(someY) + 1) / (countMap.get(allY) + 4));
		return res;
		
	}
	
	public static double computeW(LABEL label) {
		double res = 0.0;
		for (String w: ws) {
			String someW = "Y=" + label.name() + "," + "W=" + w;
			String allW = "Y=" + label.name() + "," + "W=*";
			
			double lookUp = countMap.containsKey(someW) ? countMap.get(someW) : 0;
			res += Math.log((lookUp + 1) / (countMap.get(allW) + vocabSize));
		}
		return res;
	}
	
	public static void computeVocab() {
		vocabSize = countMap.get("VOCAB");
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
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		
		if (reader != null) {
			while ((line = reader.readLine()) != null) {
				saveCounts(line);
			}
			reader.close();
		}

		computeVocab();
		
		reader = new BufferedReader(new FileReader(args[0]));
		if (reader != null) {
			while ((line = reader.readLine()) != null) {
				parseW(line);
				run();
			}
			reader.close();
		}
	
	}

}
