package search;

import java.util.List;

import Graph.Graph;
import data.Route;
import data.RouteConverter;
import data.Town;

public class RouteResolver {

	private SearchStrategyResolver searchStrategyResolver = new SearchStrategyResolver();
	private RouteConverter routeConverter = new RouteConverter();

	public List<Integer> doRouteSearch(Town startTown, Town destinationTown, List<Route> routeList,
			String searchStrategyCommand) {
		Graph graph = routeConverter.convertRoutesToGraph(routeList);
		SearchStrategy searchStrategy = searchStrategyResolver.getSelectedSearchStrategy(searchStrategyCommand);
		List<Integer> townIds = searchStrategy.search(graph, startTown.getId(), destinationTown.getId());
		return townIds;
	}

}
