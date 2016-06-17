package Graph;
import java.util.ArrayList;
import java.util.List;


public class ArrayGraph extends Graph {

	private int[][] graph;
	private int numVertices;
	private boolean directed;
	
	public ArrayGraph(int numVertices, boolean directed) {
		graph = new int[numVertices][numVertices];
		this.numVertices = numVertices;
		this.directed = directed;
	}
	
	public int numVertices() {
		return numVertices;
	}

	public boolean hasEdge(int u, int v) {
		return (graph[u][v] > 0);
	}
	
	public int getEdgeWeight(int u, int v) {
		return graph[u][v];
	}
	
	public void addEdge(int u, int v) {
		addEdge(u, v, 1);
	}
	
	public void addEdge(int u, int v, int weight) {
		graph[u][v] = weight;
		if (!directed)
			graph[v][u] = weight;
	}
	
	public void removeEdge(int u, int v) {
		graph[u][v] = 0;
		if (!directed)
			graph[v][u] = 0;
	}
	
	public List<WeightedEdge> getEdges(int v) {
		ArrayList<WeightedEdge> edges = new ArrayList<WeightedEdge>();
		for (int i=0; i < numVertices; i++) {
			if (graph[v][i] > 0) {
				edges.add(new WeightedEdge(i, graph[v][i]));
			}
		}
		return edges;
	}
}
