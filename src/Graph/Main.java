package Graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Graph g = new ListGraph(10, false);
		
		// 0: Graz
		// 1: Wien
		// 2: Klagenfurt
		// 3: Salzburg
		// 4: Inschbruck
		// 5: St. Pölten
		// 6: Linz
		
		g.addEdge(0, 1, 200);
		g.addEdge(0, 2, 130);
		g.addEdge(2, 3, 230);
		g.addEdge(1, 6, 150);
		g.addEdge(6, 3, 140);
		g.addEdge(3, 4, 200);
		g.addEdge(2, 4, 200);
		g.addEdge(0, 4, 1000);
		
		findByTiefenSucheRekursiv(g, 0, 4);
		
		//findByTiefenSuche(g, 0, 4);
		//findByBreitenSuche(g, 0, 4);
		//dijkstra(g, 0, 4);
		
	}
	
	private static void findByTiefenSucheRekursiv(Graph g, int von, int nach) {
		
		boolean[] visited = new boolean[g.numVertices()];
		int[] pred = new int[g.numVertices()];
		
		// pred[5] = 0
		// Wir besuchen 5 über 0
		
		_findByTiefenSucheRekursiv(g, von, nach, visited, pred);
		
		for(int i=0; i<pred.length; i++) {
			System.out.println(i + " über " + pred[i]);
		}
	}
	
	private static boolean _findByTiefenSucheRekursiv(
			Graph g, int current, int nach, 
			boolean[] visited, int[] pred) {
		
		if (current == nach) return true;
	
		visited[current] = true;
		
		List<WeightedEdge> nachbarn = g.getEdges(current);
		for(WeightedEdge n: nachbarn) {
			
			if (!visited[n.vertex]) {
				pred[n.vertex] = current;
				
				boolean found = _findByTiefenSucheRekursiv(g, n.vertex, nach, visited, pred);
				if (found) return true;
				
			}
		}
		return false;
	}
	
	
	private static void findByTiefenSuche(
					Graph g, int von, int nach) {
		
		Stack<Integer> nodes = new Stack<Integer>();
	
		boolean[] visited = new boolean[g.numVertices()];
		int[] pred = new int[g.numVertices()];
		boolean found = false;
		
		for(int i=0; i<pred.length; i++) {
			pred[i] = -1; 
		}
		
		nodes.push(von);
		
		while(!nodes.isEmpty()) {
	
			int current = nodes.pop();
			visited[current] = true;
	
			if (current == nach) {
				found = true;
				break;
			}
			
			List<WeightedEdge> nachbarn = g.getEdges(current);
			for(WeightedEdge nachbar: nachbarn) {
				if (!visited[nachbar.vertex]) {
					pred[nachbar.vertex] = current;
					nodes.push(nachbar.vertex);
				}
			}
		}
		
		if (found) {
			// Route ausgeben
			for(int i=0; i<pred.length; i++) {
				System.out.println(i + " über " + pred[i]);
			}
		}
		else {
			System.out.println("Keine Verbindung gefunden");
		}
		
	}
	
	private static int nextVertex(int[] dist, boolean[] visited) {
		
		int minValue = 99999;
		int minIndex = -1;
		
		for(int i=0; i<dist.length; i++) {
			if (!visited[i] && dist[i] < minValue) {
				minValue = dist[i];
				minIndex = i;
			}
		}
		return minIndex;
	}
	
	// Variante ohne Heap für dichte Graphen
	private static void dijkstra2(Graph g, int von, int nach) {
		
		int[] pred = new int[g.numVertices()];
		int[] dist = new int[g.numVertices()];
		boolean[] visited = new boolean[g.numVertices()];
		
		for(int i=0; i<dist.length; i++) {
			dist[i] = 99999;
			pred[i] = -1;
		}
		dist[von] = 0;
		
		while(true) {
			int curIndex = nextVertex(dist, visited);
			if (curIndex == -1) break;
			
			visited[curIndex] = true;
			
			List<WeightedEdge> nachbarn = g.getEdges(curIndex);
			for(WeightedEdge nachbar: nachbarn) {
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
		for(int i=0; i<pred.length; i++) {
			System.out.println(i + " über " + pred[i]);
		}
		
		// Way ausgeben
		System.out.println();
		ArrayList<Integer> way = predToWay(pred, von, nach);
		for(int vertexNumber: way) {
			System.out.print(vertexNumber + " ");
		}
		System.out.println();
	}
	
	
	private static ArrayList<Integer> predToWay(int[] pred, int from, int to) {
		
		ArrayList<Integer> way = new ArrayList<Integer>(); 
		
		int i = to;
		while (i != from) {
			way.add(0, i);
			i = pred[i];
		}
		way.add(0, from);
		
		return way;
	}
	
	// Variante mit Heap für lichte Graphen
	private static void dijkstra(Graph g, int von, int nach) {
		
		int[] pred = new int[g.numVertices()];
		int[] dist = new int[g.numVertices()];
		boolean[] visited = new boolean[g.numVertices()];
		
	
		VertexHeap heap = new VertexHeap(g.numVertices());
		for(int i=0; i<dist.length; i++) {
			dist[i] = 99999;
			heap.insert(new WeightedEdge(i, 99999));
			pred[i] = -1;
		}
	
		dist[von] = 0;
		heap.setPriority(0, 0);
		
		while(!heap.isEmpty()) {
			
			WeightedEdge cur = heap.remove();
			
			if (cur.vertex == nach) break;
			
			List<WeightedEdge> nachbarn = g.getEdges(cur.vertex);
			
			for(WeightedEdge nachbar: nachbarn) {
				int distBisHier = cur.weight;
				int distZumNachbar = nachbar.weight;
				
				int distInsg = distBisHier + distZumNachbar;
				
				if (distInsg < dist[nachbar.vertex] ) {
					
					dist[nachbar.vertex] = distInsg;
					heap.setPriority(nachbar.vertex, distInsg);
					
					pred[nachbar.vertex] = cur.vertex;
				}
			}
		}
		
		// pred ausgeben
		for(int i=0; i<pred.length; i++) {
			System.out.println(i + " über " + pred[i]);
		}
		
		
		// Way ausgeben
		System.out.println();
		ArrayList<Integer> way = predToWay(pred, von, nach);
		for(int vertexNumber: way) {
			System.out.print(vertexNumber + " ");
		}
		System.out.println();
		
	}
	
	
	private static void findByBreitenSuche(
			Graph g, int von, int nach) {
	
	ArrayDeque<Integer> nodes = new ArrayDeque<Integer>();
	
	boolean[] visited = new boolean[g.numVertices()];
	int[] pred = new int[g.numVertices()];
	boolean found = false;
	
	for(int i=0; i<pred.length; i++) {
		pred[i] = -1; 
	}
	
	nodes.add(von);
	
	outer: while(!nodes.isEmpty()) {
	
		int current = nodes.poll();
		visited[current] = true;
	
		List<WeightedEdge> nachbarn = g.getEdges(current);
		for(WeightedEdge nachbar: nachbarn) {
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
	for(int i=0; i<pred.length; i++) {
		System.out.println(i + " über " + pred[i]);
	}
}
else {
	System.out.println("Keine Verbindung gefunden");
}

}

}
