package search;

import Graph.Graph;

public interface SearchStrategy {
	
	SearchResultData search(Graph graph, int startId, int destinationId);
	
}
