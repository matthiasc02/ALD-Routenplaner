package search;

import java.util.List;

import Graph.Graph;
import command.GraphCommandValidator;
import data.Route;
import data.RouteConverter;
import data.Town;

public class RouteResolver {

	private SearchStrategyResolver searchStrategyResolver = new SearchStrategyResolver();
	private RouteConverter routeConverter = new RouteConverter();

	public SearchResultData doRouteSearch(Town startTown, Town destinationTown, List<Route> routeList,
			String searchStrategyCommand, String graphCommand, int numberOfTowns) {
		SearchStrategy searchStrategy = searchStrategyResolver.getSelectedSearchStrategy(searchStrategyCommand);
		boolean directedGraph = GraphCommandValidator.DIRECTED.equalsIgnoreCase(graphCommand);
		Graph graph = routeConverter.convertRoutesToGraph(routeList, numberOfTowns, directedGraph);
		SearchResultData searchResultData = searchStrategy.search(graph, startTown.getId(), destinationTown.getId());
		return searchResultData;
	}

}
