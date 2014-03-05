// package edu.cmu.runyunz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class NBTrainReducer {
	private BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));	
	private String lastKey;
	private double valCount;
	
	public void parse(BufferedReader reader) throws IOException {
		String line;
		while ((line = reader.readLine()) != null) {
			String[] res = line.split("\\t"); 
			aggregate(res[0], Double.parseDouble(res[1]));
		}
		print(lastKey, valCount);
	}
	
	public void aggregate(String key, double count) throws IOException {		
		if (!key.equals(lastKey)) {
			if (lastKey != null) print(lastKey, valCount);
			lastKey = key;
			valCount = 0;
		}
		valCount += count;		
	}
	
	public void print(String key, double count) throws IOException {
		writer.write(key + "\t" + count + "\n");
		writer.flush();	
	}	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		NBTrainReducer reducer = new NBTrainReducer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reducer.parse(reader);
	}

}
