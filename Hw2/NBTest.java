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

public class NBTest {
	public static int HASH_SIZE = 40000;//40000;	
	public static int WS_SIZE = 100000;
	public static int VOCAB_SIZE;
	public static int LABEL_SIZE = 14;
	private HashSet<String> vocab = new HashSet<String>();	
	private enum LABEL {sl, hr, ca, tr, hu, ga, de, el, pt, pl, fr, ru, es, nl}
	private HashSet<String> need = new HashSet<String>();
	private HashMap<String, Integer> countMap = new HashMap<String, Integer>(); /* Double */
	private ArrayList<String> ws;
	private ArrayList<ArrayList<String>> wsList = new ArrayList<ArrayList<String>>();

	// private static double vocabSize;
	
	public void saveCounts(String file) throws Exception{
		need.clear();
		countMap.clear();

		for (ArrayList<String> ws : wsList) {
			for (String w : ws) {
				need.add(w);
			}			
		}

		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		if (reader != null) {
			while ((line = reader.readLine()) != null) {
				String[] res = line.split("\\t");
				String[] yw = res[0].split(",");
				if (yw.length == 1) {
					countMap.put(res[0], Integer.parseInt(res[1])); //Double.parseDouble(res[1]));										
				}
				else if (need.contains(yw[1])) {
					countMap.put(res[0], Integer.parseInt(res[1])); //Double.parseDouble(res[1]));					
				}
			}
		}
		reader.close();		
	}
	
	public void parse(String line) throws Exception {
		String[] res = line.split("\\t");
		ws = tokenizeDoc(res[1]);
		wsList.add(ws);
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
	
	public void run() throws Exception{
		double maxPred = -Double.MAX_VALUE;
		String maxLabel = "";
		for (LABEL label : LABEL.values()) {
			double res = compute(label);
			if (res > maxPred) {
				maxPred = res;
				maxLabel = label.name();
			}
		}
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));		
		writer.write(maxLabel + "\t" + String.valueOf(maxPred) + "\n");		
		// System.out.println(maxLabel + "\t" + String.valueOf(maxPred));
		writer.flush();												
	}
	
	public double compute(LABEL label) {
		double resY = computeY(label);
		double resW = computeW(label);
		return (resW + resY);
	}
	
	public double computeY(LABEL label) {
		String someY = label.name();
		String allY = "*";
		double allYcount = countMap.containsKey(allY) ? countMap.get(allY) : 0;		
		double someYcount = countMap.containsKey(someY) ? countMap.get(someY) : 0;

		double res = Math.log((someYcount + 1) / (allYcount + LABEL_SIZE));		
		return res;
		
	}
	
	public double computeW(LABEL label) {
		double res = 0.0;
		for (String w: ws) {
			String someW = label.name() + "," + w;
			String allW = label.name() + "," + "*";
			
			double someWcount = countMap.containsKey(someW) ? countMap.get(someW) : 0; /* double */
			double allWcount = countMap.containsKey(allW) ? countMap.get(allW) : 0; /* double */
			res += Math.log((someWcount + 1) / (allWcount + VOCAB_SIZE));
		}
		return res;
	}

	public void checkMap() throws Exception{
		if (countMap.size() > HASH_SIZE) {
			countMap.clear();
			// count++:
		}
	}

	public void checkWs() throws Exception{
		if (wsList.size() > WS_SIZE) {
			// saveCounts("MergeCounts.res");
			// for(ArrayList<String> str : wsList) {
			// 	ws = str;
			// 	run();
			// }
			batch();
			wsList.clear();
			// count++:
		}
	}

	public void batch() throws Exception{
		saveCounts("MergeCounts.res");
		for(ArrayList<String> lst : wsList) {
			ws = lst;
			run();
		}		
	}

	public void vocab(String line) {
		String[] res = line.split("\\t");
		String[] yw = res[0].split(",");
		if (yw.length == 2) {
			String w = yw[1];
			vocab.add(w);
		}
	}

	public int vocabSize() {
		return vocab.size();
	}	

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		NBTest nbTest = new NBTest();				
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter writer = new BufferedWriter(new FileWriter("MergeCounts.res"));			
		String line;
		if (reader != null) {
			while ((line = reader.readLine()) != null) {
				nbTest.vocab(line);
				writer.write(line + '\n');
			}
		}
		reader.close();
		writer.close();
		VOCAB_SIZE = nbTest.vocabSize() - 1;

		reader = new BufferedReader(new FileReader(args[0]));
		if (reader != null) {
			while ((line = reader.readLine()) != null) {
				nbTest.parse(line);
				// nbTest.saveCounts("MergeCounts.res");
				// nbTest.run();
				nbTest.checkMap();
			}
		}
		reader.close();
		nbTest.batch();
		// System.out.println(VOCAB_SIZE);	
	}

}
