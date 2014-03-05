import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.TreeMap;


public class PhraseGenerator {
	private double vocab, allFg, allBg;
	private double vocabXY, allFgXY, allBgXY;
	private double fgX, fgY, fgXY, bgX, bgY, bgXY;
	private double phrase, phraseness, informativeness;
	private String lastKey;
	private BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
	private TreeMap<Double, String> top20 = new TreeMap<Double, String>();
	
	public void parse(BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			String[] res = line.split("\\t"); 
			if (res[0].equals("*") || res[0].equals("* *")) init(res[0], res[1]);
			else combine(res[0], res[1]);
		}
		compute();
		
		printResult();
	}
	
	public void init(String key, String val) {
		String[] tmp = val.split(" "); /* allFg, allBg, vocab */
		if (key.equals("*")) {
			vocab = Double.parseDouble(tmp[0]);
			allFg = Double.parseDouble(tmp[1]);
			allBg = Double.parseDouble(tmp[2]);
		}
		if (key.equals("* *")) {
			vocabXY = Double.parseDouble(tmp[0]);
			allFgXY = Double.parseDouble(tmp[1]);
			allBgXY = Double.parseDouble(tmp[2]);			
		}
	}
	
	public void combine(String key, String val) throws IOException {		
		String[] res = val.split(" ");	/* res= <fb bg> or <X/Y fg bg> */	
		if (!key.equals(lastKey)) {
			if (lastKey != null) compute();
			lastKey = key;
			fgXY = Double.parseDouble(res[0]); bgXY = Double.parseDouble(res[1]);
		}
		else {
			if (res[0].equals("X")) {fgX = Double.parseDouble(res[1]); bgX = Double.parseDouble(res[2]);}
			if (res[0].equals("Y")) {fgY = Double.parseDouble(res[1]); bgY = Double.parseDouble(res[2]);}
		}
	}
	
	public void compute() {
//		double pfgXY =  (fgXY + 1/vocabXY) / (allFgXY + 1);
//		double pbgXY = (bgXY + 1/vocabXY) / (allBgXY + 1);
//		double pfgX =  (fgX + 1/vocab) / (allFg + 1);
//		double pfgY =  (fgY + 1/vocab) / (allFg + 1);
		
		double pfgXY =  (fgXY + 1) / (allFgXY + vocabXY);
		double pbgXY = (bgXY + 1) / (allBgXY + vocabXY);
		double pfgX =  (fgX + 1) / (allFg + vocab);
		double pfgY =  (fgY + 1) / (allFg + vocab);		
		
		phraseness = divergence(pfgXY, pfgX*pfgY); 
		informativeness = divergence(pfgXY, pbgXY); 
		phrase = phraseness + informativeness;
		
		String record = lastKey + "\t" + phrase + "\t" + phraseness + "\t" + informativeness;
		compare(phrase, record);
	}
	
	public double divergence(double p, double q) {
		return p * (Math.log(p) - Math.log(q));
	}
	
	public void compare(double score, String record) {
		if (top20.size() < 20) top20.put(score, record);
		else {
			double lowest = top20.firstKey();
			if(score > lowest) {
				top20.remove(lowest);
				top20.put(score, record);
			}
		}
	}
	
	public void printResult() throws IOException {
		for (double res : top20.descendingKeySet()) {
			print(top20.get(res));
		}
	}
	
	public void print(String str) throws IOException {
		writer.write(str + "\n");
		writer.flush();	
	}	
	
	public static void main(String[] args) throws IOException {			
		// TODO Auto-generated method stub
		PhraseGenerator phraseGenerator = new PhraseGenerator();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		if (reader != null) phraseGenerator.parse(reader);
	}
}
