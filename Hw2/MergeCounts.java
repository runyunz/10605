import java.io.*;
import java.util.*;

/**
 * @author runyunz
 *
 */

public class MergeCounts {
	private String prevKey;
	private int prevCount;

	public void run(String line) throws Exception{
		String[] res = line.split("\\t");
		String key = res[0];
		int count = Integer.parseInt(res[1]);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));		


		if (key == prevKey) {
			prevCount += count;
		}
		else {
			if (prevKey != null) {
				if (prevCount > 0) {
					// System.out.println(prevKey + "\t" + prevCount);	
					writer.write(prevKey + "\t" + prevCount + "\n");
					writer.flush();										
				}
			}
			prevKey = key;
			prevCount = count;
		}		
	}

	public void printKeyCount() throws Exception{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));		
		writer.write(prevKey + "\t" + prevCount + "\n");
		writer.flush();										

		// System.out.println(prevKey + "\t" + prevCount);
	}

	public static void main(String[] args) throws Exception {
		MergeCounts mergeCounts = new MergeCounts();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String line;
		if (reader != null) {
			while ((line = reader.readLine()) != null) {
				mergeCounts.run(line);
			}
		}
		reader.close();	
		mergeCounts.printKeyCount();

	}
}