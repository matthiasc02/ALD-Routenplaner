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
		BufferedReader bufferedReader = null;
		try {
			InputStream ressource = CsvReader.class.getResourceAsStream(getFilePath());
			bufferedReader = new BufferedReader(new InputStreamReader(ressource));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				T obj = parseLine(line);
				objects.add(obj);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file!");
			e.printStackTrace();
		} catch (FileNotParseableException e) {
			System.out.println("Could not parse file!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("An error occured while reading file!");
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					System.out.println("An error occured closing reader!");
					e.printStackTrace();
				}
			}
		}
		return objects;
	}

	public abstract T parseLine(String line) throws FileNotParseableException;

	public abstract String getFilePath();
}
