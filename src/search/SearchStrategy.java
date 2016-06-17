package search;

import java.util.List;

import Graph.Graph;

public interface SearchStrategy {
	
	List<Integer> search(Graph graph, int startId, int destinationId);
	
}
