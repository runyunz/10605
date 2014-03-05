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
	public static int HASH_SIZE = 40000;//40000;
	// public int count = 0;
	private ArrayList<String> ys;
	private ArrayList<String> ws;
	private HashMap<String, Integer> countMap = new HashMap<String, Integer>(); /* double */
	// public HashSet<String> label = new HashSet<String>();
	public HashSet<String> vocab = new HashSet<String>();
	
	public void parse(String line) throws Exception {
		String[] res = line.split("\\t");
		ys = filterLables(res[0]);
		ws = tokenizeDoc(res[1]);
	
	}
	
	public ArrayList<String> filterLables(String str) {
		String[] ylabs = str.split(",");
		ArrayList<String> res = new ArrayList<String>();
		for(String w: ylabs) {
			res.add(w);
		}
		return res;
	}

	public ArrayList<String> tokenizeDoc (String cur_doc) {
		String[] words = cur_doc.split("\\s+");
        ArrayList<String> tokens = new ArrayList<String>();
        for (int i = 0; i < words.length; i++) {
        	words[i] = words[i].replaceAll("\\W", "");
        	if (words[i].length() > 0) {
        		tokens.add(words[i]);
        	}
        }
        	return tokens;
	}	
	
	public void run() {
		for (String y : ys) {
			countNumbers(y, ws);
			// label.add(y);
		}
	}
	
	public void countNumbers(String y, ArrayList<String> ws) {
		String someY = y;
		String allY = "*";

		updateMap(someY, 1);
		updateMap(allY, 1);
		
		for (String w: ws) {
			String someYsomeW = y + "," + w;
			updateMap(someYsomeW, 1);
			// computeVocab(w);
		}		

		String someYallW = y + "," + "*";		
		updateMap(someYallW, ws.size());
		
	}
	
	public void computeVocab(String w) {
		vocab.add(w);
	}
	
	public void updateMap(String key, int i) { /* double */
		if (countMap.containsKey(key)) {
			int oldCount = countMap.get(key); /* double */
			countMap.put(key, oldCount + i);
		}
		else {
			countMap.put(key, i);
		}
	}
	
	public void printMap() throws Exception{		
		Iterator<String> iter = countMap.keySet().iterator();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));		
		while (iter.hasNext()) {
			String key = iter.next();
			writer.write(key + "\t" + countMap.get(key) + "\n");
			writer.flush();		
			// System.out.println(key + "\t" + countMap.get(key));
		}
		// System.out.println("VOCAB"+ "\t" + vocab.size());
		// System.out.println("LABEL"+ "\t" + label.size());		
	}

	public void checkMap() throws Exception{
		if (countMap.size() > HASH_SIZE) {
			printMap();			
			countMap.clear();
			// count++;
		}
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		NBTrain nbTrain = new NBTrain();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String line;
		if (reader != null) {
			while ((line = reader.readLine()) != null) {
				nbTrain.parse(line);
				nbTrain.run();
				nbTrain.checkMap();
			}
		}
		reader.close();
		nbTrain.printMap();		
					
		// System.out.println(Runtime.getRuntime().totalMemory());
		// System.out.println(Runtime.getRuntime().freeMemory());	
		// System.out.println(nbTrain.vocab.size());
		// System.out.println(nbTrain.label);		

	}

}
