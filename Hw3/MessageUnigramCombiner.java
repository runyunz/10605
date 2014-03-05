import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MessageUnigramCombiner {
	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
	private double fg, bg;
	private String lastKey;

	public void parse(BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			String[] res = line.split("\\t"); /* <x fgbg> or <x xy> or <* counts> */
			if (res[0].equals("*")) print(res[0], res[1]);
			else combine(res[0], res[1]);
		}
	}
	
	public void combine(String key, String val) throws IOException {		
		String[] res = val.split(" ");	/* res= <fb bg> or <x y> */	
		if (!key.equals(lastKey)) {
			lastKey = key;
			fg = Double.parseDouble(res[0]); bg = Double.parseDouble(res[1]);
		}
		else {
			if (key.equals(res[0])) print(val, "X", fg + " " + bg);
			if (key.equals(res[1])) print(val, "Y", fg + " " + bg);
		}
	}	
	
	public void print(String key, String mark, String val) throws IOException {
		writer.write(key + "\t" + mark + " " + val + "\n");
		writer.flush();	
	}
	
	public void print(String key, String val) throws IOException {
		writer.write(key + "\t" + val + "\n");
		writer.flush();	
	}
	
	public static void main(String[] args) throws IOException {		
		// TODO Auto-generated method stub
		MessageUnigramCombiner messageUnigramCombiner = new MessageUnigramCombiner();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		if (reader != null) messageUnigramCombiner.parse(reader);
	}

}
