package search;

import java.util.List;
import java.util.Stack;

import Graph.Graph;
import Graph.WeightedEdge;

public class DepthFirstSearchStrategy implements SearchStrategy {

	@Override
	public List<Integer> search(Graph graph, int startId, int destinationId) {
		return findByDepthSearch(graph, startId, destinationId);
	}

	protected List<Integer> findByDepthSearch(Graph graph, int startId, int destinationId) {
		Stack<Integer> nodes = new Stack<Integer>();

		boolean[] visited = new boolean[graph.numVertices()];
		int[] pred = new int[graph.numVertices()];
		boolean found = false;

		for (int i = 0; i < pred.length; i++) {
			pred[i] = -1;
		}

		nodes.push(startId);

		while (!nodes.isEmpty()) {

			int current = nodes.pop();
			visited[current] = true;

			if (current == destinationId) {
				found = true;
				break;
			}

			List<WeightedEdge> neighbours = graph.getEdges(current);
			for (WeightedEdge neighbour : neighbours) {
				if (!visited[neighbour.vertex]) {
					pred[neighbour.vertex] = current;
					nodes.push(neighbour.vertex);
				}
			}
		}

		return SearchUtils.predToWay(pred, startId, destinationId);
	}

	protected void findByDepthSearchRecursive(Graph graph, int startId, int destinationId) {

		boolean[] visited = new boolean[graph.numVertices()];
		int[] pred = new int[graph.numVertices()];

		// pred[5] = 0
		// Wir besuchen 5 über 0

		_findByDepthSearchRecursive(graph, startId, destinationId, visited, pred);

		for (int i = 0; i < pred.length; i++) {
			System.out.println(i + " über " + pred[i]);
		}
	}

	private boolean _findByDepthSearchRecursive(Graph graph, int current, int destinationId, boolean[] visited,
			int[] pred) {

		if (current == destinationId)
			return true;

		visited[current] = true;

		List<WeightedEdge> nachbarn = graph.getEdges(current);
		for (WeightedEdge n : nachbarn) {

			if (!visited[n.vertex]) {
				pred[n.vertex] = current;

				boolean found = _findByDepthSearchRecursive(graph, n.vertex, destinationId, visited, pred);
				if (found)
					return true;

			}
		}
		return false;
	}

}
