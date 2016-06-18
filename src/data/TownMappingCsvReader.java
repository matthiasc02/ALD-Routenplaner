package data;

public class TownMappingCsvReader extends CsvReader {

	private static final String TOWN_FILE_PATH = "/towns.csv";

	@Override
	public Object parseLine(String line) throws FileNotParseableException {
		if (line == null || line.isEmpty()) {
			throw new FileNotParseableException();
		}

		String[] townFields = line.split(CSV_DELIMITER);

		if (townFields != null && townFields.length != 2) {
			throw new FileNotParseableException();
		}

		int townId = Integer.valueOf(townFields[0]);
		String townName = townFields[1];

		Town town = new Town(townId, townName);
		return town;
	}

	@Override
	public String getFilePath() {
		return TOWN_FILE_PATH;
	}

}
