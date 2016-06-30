package data;

import java.util.List;

import Graph.Graph;
import Graph.ListGraph;

public class RouteConverter {

	public Graph convertRoutesToGraph(List<Route> routeList, int numberOfTowns) {
		if (routeList == null || routeList.isEmpty()) {
			return null;
		}

		Graph graph = new ListGraph(numberOfTowns, false);
		for (Route route : routeList) {
			graph.addEdge(route.getFromTownId(), route.getToTownId(), route.getDistance());
		}

		return graph;
	}
}
