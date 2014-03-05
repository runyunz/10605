import java.io.IOException;
import java.util.*;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class NB_train_hadoop {
	public static class Map extends Mapper<LongWritable, Text, Text, DoubleWritable> {
		private Text resKey = new Text();
		private DoubleWritable resVal = new DoubleWritable();		
		private ArrayList<String> ys = new ArrayList<String>();
		private ArrayList<String> ws = new ArrayList<String>();		
		
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			String[] res = line.split("\\t");
			ys = filterLables(res[0]);
			ws = tokenizeDoc(res[1]);
			aggregate(context);
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
		
		public void aggregate(Context context) throws IOException, InterruptedException {
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
				print(y, 1, context);
				print("*", 1, context);
				
				Iterator<String> it = map.keySet().iterator();
			    while (it.hasNext()) {
			        String w = it.next();
			        print(y, w, map.get(w), context);
			    }
			    print(y, "*", ws.size(), context);
			}
		}

		public void print(String y, double count, Context context) throws IOException, InterruptedException {
			resKey.set("Y=" + y);
			resVal.set(count);
			context.write(resKey, resVal);
		}		
		
		public void print(String y, String w, double count, Context context) throws IOException, InterruptedException {
			resKey.set("Y=" + y + "," + "W=" + w);
			resVal.set(count);
			context.write(resKey, resVal);
		}
	
	}
	
	public static class Reduce extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
		private DoubleWritable resVal = new DoubleWritable();
		public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
			double sum = 0.0;
			for (DoubleWritable val : values) {
				sum += val.get();
			}
			resVal.set(sum);
			context.write(key, resVal);
		}

	}

}
