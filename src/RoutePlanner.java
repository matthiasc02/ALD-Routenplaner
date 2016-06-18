import java.util.List;

import data.Route;
import data.RouteMappingCsvReader;
import data.Town;
import data.TownMappingCsvReader;
import server.Server;

public class RoutePlanner {

	public static void main(String[] args) {
		TownMappingCsvReader townReader = new TownMappingCsvReader();
		List<Town> townList = (List<Town>) townReader.parseFile();

		RouteMappingCsvReader routeReader = new RouteMappingCsvReader();
		List<Route> routeList = (List<Route>) routeReader.parseFile();

		Server server = new Server(townList, routeList);
		server.run();
	}
}