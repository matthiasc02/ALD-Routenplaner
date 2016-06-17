package search;

import java.util.ArrayList;
import java.util.List;

import Graph.Graph;
import Graph.VertexHeap;
import Graph.WeightedEdge;

public class DijkstraSearchStrategy implements SearchStrategy {

	@Override
	public List<Integer> search(Graph graph, int startId, int destinationId) {
		return dijkstra(graph, startId, destinationId);
	}

	// Variante mit Heap für lichte Graphen
	private static List<Integer> dijkstra(Graph g, int von, int nach) {

		int[] pred = new int[g.numVertices()];
		int[] dist = new int[g.numVertices()];
		boolean[] visited = new boolean[g.numVertices()];

		VertexHeap heap = new VertexHeap(g.numVertices());
		for (int i = 0; i < dist.length; i++) {
			dist[i] = 99999;
			heap.insert(new WeightedEdge(i, 99999));
			pred[i] = -1;
		}

		dist[von] = 0;
		heap.setPriority(0, 0);

		while (!heap.isEmpty()) {

			WeightedEdge cur = heap.remove();

			if (cur.vertex == nach)
				break;

			List<WeightedEdge> nachbarn = g.getEdges(cur.vertex);

			for (WeightedEdge nachbar : nachbarn) {
				int distBisHier = cur.weight;
				int distZumNachbar = nachbar.weight;

				int distInsg = distBisHier + distZumNachbar;

				if (distInsg < dist[nachbar.vertex]) {

					dist[nachbar.vertex] = distInsg;
					heap.setPriority(nachbar.vertex, distInsg);

					pred[nachbar.vertex] = cur.vertex;
				}
			}
		}

		// pred ausgeben
		for (int i = 0; i < pred.length; i++) {
			System.out.println(i + " über " + pred[i]);
		}

		// Way ausgeben
	/*	System.out.println();
		ArrayList<Integer> way = predToWay(pred, von, nach);
		for (int vertexNumber : way) {
			System.out.print(vertexNumber + " ");
		}
		System.out.println(); */
		return SearchUtils.predToWay(pred, von, nach);
	}

	// Variante ohne Heap für dichte Graphen
	private static void dijkstra2(Graph g, int von, int nach) {

		int[] pred = new int[g.numVertices()];
		int[] dist = new int[g.numVertices()];
		boolean[] visited = new boolean[g.numVertices()];

		for (int i = 0; i < dist.length; i++) {
			dist[i] = 99999;
			pred[i] = -1;
		}
		dist[von] = 0;

		while (true) {
			int curIndex = nextVertex(dist, visited);
			if (curIndex == -1)
				break;

			visited[curIndex] = true;

			List<WeightedEdge> nachbarn = g.getEdges(curIndex);
			for (WeightedEdge nachbar : nachbarn) {
				int distBisHier = dist[curIndex];
				int distZumNachbar = nachbar.weight;

				int distInsg = distBisHier + distZumNachbar;

				if (dist[nachbar.vertex] > distInsg) {
					dist[nachbar.vertex] = distInsg;
					pred[nachbar.vertex] = curIndex;

				}
			}
		}

		// Pred ausgeben
		for (int i = 0; i < pred.length; i++) {
			System.out.println(i + " über " + pred[i]);
		}

		// Way ausgeben
		System.out.println();
		ArrayList<Integer> way = SearchUtils.predToWay(pred, von, nach);
		for (int vertexNumber : way) {
			System.out.print(vertexNumber + " ");
		}
		System.out.println();
	}

	private static int nextVertex(int[] dist, boolean[] visited) {

		int minValue = 99999;
		int minIndex = -1;

		for (int i = 0; i < dist.length; i++) {
			if (!visited[i] && dist[i] < minValue) {
				minValue = dist[i];
				minIndex = i;
			}
		}
		return minIndex;
	}

}
