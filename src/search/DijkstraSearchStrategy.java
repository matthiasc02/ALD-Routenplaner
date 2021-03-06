package search;

import java.util.List;

import Graph.Graph;
import Graph.VertexHeap;
import Graph.WeightedEdge;

public class DijkstraSearchStrategy implements SearchStrategy {

	@Override
	public SearchResultData search(Graph graph, int startId, int destinationId) {
		return dijkstra(graph, startId, destinationId);
	}

	// Variante mit Heap f�r lichte Graphen
	protected SearchResultData dijkstra(Graph graph, int startId, int destinationId) {
		SearchResultData searchResultData = new SearchResultData();
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
		heap.setPriority(startId, 0);

		while (!heap.isEmpty()) {

			WeightedEdge cur = heap.remove();

			if (cur.vertex == destinationId)
				break;

			List<WeightedEdge> neighbours = graph.getEdges(cur.vertex);

			for (WeightedEdge neighbour : neighbours) {
				int distanceTilHere = dist[cur.vertex];
				int distanceToNeighbour = neighbour.weight;

				int distanceTotal = distanceTilHere + distanceToNeighbour;

				if (distanceTotal < dist[neighbour.vertex]) {

					dist[neighbour.vertex] = distanceTotal;
					heap.setPriority(neighbour.vertex, distanceTotal);

					pred[neighbour.vertex] = cur.vertex;
				}
			}
		}

		List<Integer> townIdList = SearchUtils.predToWay(pred, startId, destinationId);
		searchResultData.setTownIdList(townIdList);
		return searchResultData;
	}

	// Variante ohne Heap f�r dichte Graphen
	private SearchResultData dijkstra2(Graph graph, int startId, int destinationId) {
		SearchResultData searchResultData = new SearchResultData();
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
		
		List<Integer> townIdList = SearchUtils.predToWay(pred, startId, destinationId);
		searchResultData.setTownIdList(townIdList);
		return searchResultData;
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
