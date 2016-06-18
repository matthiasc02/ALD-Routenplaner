package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class CsvReader<T> {

	public static final String CSV_DELIMITER = ";";

	public Collection<T> parseFile() {
		List<T> objects = new ArrayList<T>();
		BufferedReader br = null;
		try {
			InputStream res = CsvReader.class.getResourceAsStream(getFilePath());
			br = new BufferedReader(new InputStreamReader(res));
			String line = null;
			while ((line = br.readLine()) != null) {
				T obj = parseLine(line);
				objects.add(obj);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (FileNotParseableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return objects;
	}

	public abstract T parseLine(String line) throws FileNotParseableException;

	public abstract String getFilePath();
}
