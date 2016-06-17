package search;

import java.util.ArrayDeque;
import java.util.List;

import Graph.Graph;
import Graph.WeightedEdge;

public class BreadthFirstSearchStrategy implements SearchStrategy {

	@Override
	public List<Integer> search(Graph graph, int startId, int destinationId) {
		return findByBreitenSuche(graph, startId, destinationId);
	}

	private static List<Integer> findByBreitenSuche(Graph g, int von, int nach) {

		ArrayDeque<Integer> nodes = new ArrayDeque<Integer>();

		boolean[] visited = new boolean[g.numVertices()];
		int[] pred = new int[g.numVertices()];
		boolean found = false;

		for (int i = 0; i < pred.length; i++) {
			pred[i] = -1;
		}

		nodes.add(von);

		outer: while (!nodes.isEmpty()) {

			int current = nodes.poll();
			visited[current] = true;

			List<WeightedEdge> nachbarn = g.getEdges(current);
			for (WeightedEdge nachbar : nachbarn) {
				if (!visited[nachbar.vertex]) {
					nodes.add(nachbar.vertex);
					pred[nachbar.vertex] = current;

					if (nachbar.vertex == nach) {
						found = true;
						break outer;
					}
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

}
