package search;

import java.util.List;
import java.util.Stack;

import Graph.Graph;
import Graph.WeightedEdge;

public class DepthFirstSearchStrategy implements SearchStrategy {

	@Override
	public List<Integer> search(Graph graph, int startId, int destinationId) {
		return findByTiefenSuche(graph, startId, destinationId);
	}

	private static List<Integer> findByTiefenSuche(Graph g, int von, int nach) {

		Stack<Integer> nodes = new Stack<Integer>();

		boolean[] visited = new boolean[g.numVertices()];
		int[] pred = new int[g.numVertices()];
		boolean found = false;

		for (int i = 0; i < pred.length; i++) {
			pred[i] = -1;
		}

		nodes.push(von);

		while (!nodes.isEmpty()) {

			int current = nodes.pop();
			visited[current] = true;

			if (current == nach) {
				found = true;
				break;
			}

			List<WeightedEdge> nachbarn = g.getEdges(current);
			for (WeightedEdge nachbar : nachbarn) {
				if (!visited[nachbar.vertex]) {
					pred[nachbar.vertex] = current;
					nodes.push(nachbar.vertex);
				}
			}
		}

		if (found) {
			// Route ausgeben
			for (int i = 0; i < pred.length; i++) {
				System.out.println(i + " über " + pred[i]);
			}
		} else {
			System.out.println("Keine Verbindung gefunden");
		}
		return SearchUtils.predToWay(pred, von, nach);
	}

	private static void findByTiefenSucheRekursiv(Graph g, int von, int nach) {

		boolean[] visited = new boolean[g.numVertices()];
		int[] pred = new int[g.numVertices()];

		// pred[5] = 0
		// Wir besuchen 5 über 0

		_findByTiefenSucheRekursiv(g, von, nach, visited, pred);

		for (int i = 0; i < pred.length; i++) {
			System.out.println(i + " über " + pred[i]);
		}
	}

	private static boolean _findByTiefenSucheRekursiv(Graph g, int current, int nach, boolean[] visited, int[] pred) {

		if (current == nach)
			return true;

		visited[current] = true;

		List<WeightedEdge> nachbarn = g.getEdges(current);
		for (WeightedEdge n : nachbarn) {

			if (!visited[n.vertex]) {
				pred[n.vertex] = current;

				boolean found = _findByTiefenSucheRekursiv(g, n.vertex, nach, visited, pred);
				if (found)
					return true;

			}
		}
		return false;
	}

}
