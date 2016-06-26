package data;

public class RouteMappingCsvReader extends CsvReader {

	private static final String ROUTE_FILE_PATH = "/routes.csv";

	@Override
	public Object parseLine(String line) throws FileNotParseableException {
		if (line == null || line.isEmpty()) {
			throw new FileNotParseableException();
		}

		String[] routeFields = line.split(CSV_DELIMITER);

		if (routeFields != null && routeFields.length != 4) {
			throw new FileNotParseableException();
		}

		int fromTownId = Integer.valueOf(routeFields[0]);
		int toTownId = Integer.valueOf(routeFields[1]);
		String streetName = routeFields[2];
		int weight = Integer.valueOf(routeFields[3]);

		Route route = new Route(fromTownId, toTownId, streetName, weight);
		return route;
	}

	@Override
	public String getFilePath() {
		return ROUTE_FILE_PATH;
	}

}
