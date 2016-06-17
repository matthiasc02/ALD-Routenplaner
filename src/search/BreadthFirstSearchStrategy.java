package search;

import java.util.ArrayDeque;
import java.util.List;

import Graph.Graph;
import Graph.WeightedEdge;

public class BreadthFirstSearchStrategy implements SearchStrategy {

	@Override
	public List<Integer> search(Graph graph, int startId, int destinationId) {
		return findByBreadthFirst(graph, startId, destinationId);
	}

	protected List<Integer> findByBreadthFirst(Graph graph, int startId, int destinationId) {
		ArrayDeque<Integer> nodes = new ArrayDeque<Integer>();

		boolean[] visited = new boolean[graph.numVertices()];
		int[] pred = new int[graph.numVertices()];
		boolean found = false;

		for (int i = 0; i < pred.length; i++) {
			pred[i] = -1;
		}

		nodes.add(startId);

		outer: while (!nodes.isEmpty()) {

			int current = nodes.poll();
			visited[current] = true;

			List<WeightedEdge> neighbours = graph.getEdges(current);
			for (WeightedEdge neighbour : neighbours) {
				if (!visited[neighbour.vertex]) {
					nodes.add(neighbour.vertex);
					pred[neighbour.vertex] = current;

					if (neighbour.vertex == destinationId) {
						found = true;
						break outer;
					}
				}
			}
		}

		return SearchUtils.predToWay(pred, startId, destinationId);
	}

}
