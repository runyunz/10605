import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MessageGenerator {
	private BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
	
	public void parse(BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			String[] res = line.split("\\t"); /* word year count */
			if (!res[0].equals("* *")) {
				String[] xy = res[0].split(" ");
				print(xy[0], xy[1], res[0]);
			}
		}		
	}
	
	public void print(String x, String y, String xy) throws IOException {
		writer.write(x + "\t" + xy + "\n");
		writer.write(y + "\t" + xy + "\n");		
		writer.flush();	
	}	

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		MessageGenerator messageGenerator = new MessageGenerator();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		if (reader != null) messageGenerator.parse(reader);
	}

}
