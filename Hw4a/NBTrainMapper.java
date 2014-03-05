// package edu.cmu.runyunz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


/* output */
/* <*, CCAT>: number of training instances of class CCAT */
/* <*, *>: number of training instances of all classes, i.e. total number of training instances */
/* <**, y>: number of classes, in this case will be 4: CCAT, ECAT, GCAT, MCAT */
/* <**, w>: number of different words appeared, i.e. vocabulary size */
/* <CCAT,*>: number of words in class CCAT */
/* <CCAT,word>: number of a word in class CCAT */

public class NBTrainMapper {
	private BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
	ArrayList<String> ys = new ArrayList<String>();
	ArrayList<String> ws = new ArrayList<String>();
	/* global statics */
//	private HashMap<String, Integer> insByClass = new HashMap<String, Double>();
//	int insAllClasses = 0;
//	private HashSet<String> vocab = new HashSet<String>();
//	private HashSet<String> label = new HashSet<String>();
	
	public void parse(BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			String[] res = line.split("\\t"); 
			ys = filterLables(res[0]);
			ws = tokenizeDoc(res[1]);
			aggregate();
		}

	}	

	public void aggregate() throws IOException {
		for (String y : ys) {
			/* count */
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for (String w : ws) {
				if (map.containsKey(w)) {
					int oldCount = map.get(w);
					map.put(w, oldCount+1);
				}
				else map.put(w, 1);
			}
			/* print */
			Iterator<String> it = map.keySet().iterator();
		    while (it.hasNext()) {
		        String w = it.next();
		        print(y, w, map.get(w));
		    }
		    print(y, "*", ws.size());
		}
	}
	
	public ArrayList<String> filterLables(String str) {
		String[] ylabs = str.split(",");
		ArrayList<String> res = new ArrayList<String>();
		for(String w: ylabs) {
			if (w.matches("[CEGM]CAT")) {
				res.add(w);
			}
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
	
	public void print(String y, String w, int count) throws IOException {
		writer.write(y + "," + w + "\t" + count + "\n");
		writer.flush();	
	}	
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		NBTrainMapper mapper = new NBTrainMapper();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		mapper.parse(reader);
	}

}
