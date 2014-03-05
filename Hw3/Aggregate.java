import java.util.*;
import java.io.*;

public class Aggregate {
	private double vocab, allFg, allBg;
	private String lastKey;
	private double fg, bg;
	private BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
	
	public void parse(BufferedReader reader, String opt) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			String[] res = line.split("\\t"); /* word year count */
			aggregate(res[0], res[1], Integer.parseInt(res[2]));
		}
		aggregate();
		
		if(opt.equals("0")) print("*", allFg, allBg, vocab);		
		if(opt.equals("1")) print("* *", allFg, allBg, vocab);		
	}
	
	public void aggregate(String key, String year, int count) throws IOException {		
		if (!key.equals(lastKey)) {
			if (lastKey != null) {
				print(lastKey, fg, bg);
				vocab++; allFg += fg; allBg += bg;
				fg = 0; bg = 0;
			}
			lastKey = key;
		}
		if (year.equals("1990")) fg += count;
		else bg += count;		
	}
	
	public void aggregate() throws IOException {
		print(lastKey, fg, bg);
		vocab++; allFg += fg; allBg += bg;
		fg = 0; bg = 0;		
	}
	
	public void print(String key, double fg, double bg) throws IOException {
		writer.write(key + "\t" + fg + " " + bg + "\n");
		writer.flush();	
	}
	
	public void print(String key, double fg, double bg, double vocab) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));		
		writer.write(key + "\t" + fg + " " + bg + " " + vocab + "\n");
		writer.flush();	
	}	
	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Aggregate aggregate = new Aggregate();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		if (reader != null) aggregate.parse(reader, args[0]);
	}

}
