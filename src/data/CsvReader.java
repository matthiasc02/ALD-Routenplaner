package data;

import java.util.List;

public abstract class CsvReader<T> {
	
	public static final String CSV_DELIMITER = ";";
	
	public List<T> parseFile() {
		return null;
	}
	
	public abstract T parseLine(String line) throws FileNotParseableException;
	
}
