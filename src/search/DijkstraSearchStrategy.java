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
	protected List<Integer> dijkstra(Graph graph, int startId, int destinationId) {
		int[] pred = new int[graph.numVertices()];
		int[] dist = new int[graph.numVertices()];
		boolean[] visited = new boolean[graph.numVertices()];

		VertexHeap heap = new VertexHeap(graph.numVertices());
		for (int i = 0; i < dist.length; i++) {
			dist[i] = 99999;
			heap.insert(new WeightedEdge(i, 99999));
			pred[i] = -1;
		}

		dist[startId] = 0;
		heap.setPriority(0, 0);

		while (!heap.isEmpty()) {

			WeightedEdge cur = heap.remove();

			if (cur.vertex == destinationId)
				break;

			List<WeightedEdge> neighbours = graph.getEdges(cur.vertex);

			for (WeightedEdge neighbour : neighbours) {
				int distanceTilHere = cur.weight;
				int distanceToNeighbour = neighbour.weight;

				int distanceTotal = distanceTilHere + distanceToNeighbour;

				if (distanceTotal < dist[neighbour.vertex]) {

					dist[neighbour.vertex] = distanceTotal;
					heap.setPriority(neighbour.vertex, distanceTotal);

					pred[neighbour.vertex] = cur.vertex;
				}
			}
		}

		return SearchUtils.predToWay(pred, startId, destinationId);
	}

	// Variante ohne Heap für dichte Graphen
	private void dijkstra2(Graph graph, int startId, int destinationId) {
		int[] pred = new int[graph.numVertices()];
		int[] dist = new int[graph.numVertices()];
		boolean[] visited = new boolean[graph.numVertices()];

		for (int i = 0; i < dist.length; i++) {
			dist[i] = 99999;
			pred[i] = -1;
		}
		dist[startId] = 0;

		while (true) {
			int curIndex = nextVertex(dist, visited);
			if (curIndex == -1)
				break;

			visited[curIndex] = true;

			List<WeightedEdge> neighbours = graph.getEdges(curIndex);
			for (WeightedEdge neighbour : neighbours) {
				int distanceTilHere = dist[curIndex];
				int distanceToNeighbour = neighbour.weight;

				int distanceTotal = distanceTilHere + distanceToNeighbour;

				if (dist[neighbour.vertex] > distanceTotal) {
					dist[neighbour.vertex] = distanceTotal;
					pred[neighbour.vertex] = curIndex;

				}
			}
		}
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
