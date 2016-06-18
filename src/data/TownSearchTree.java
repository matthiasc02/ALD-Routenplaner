package data;

import Graph.BaseTree;
import Graph.Node;

public class TownSearchTree extends BaseTree<Town> {

	@Override
	protected int compare(Town a, Town b) {
		if (a == null)
			return -1;
		if (b == null)
			return 1;
		if (a.getName().equalsIgnoreCase(b.getName()))
			return 0;
		return a.getName().compareTo(b.getName());
	}

	public Node<Town> find(String townName) {
		return find(root, townName);
	}

	
	public Node<Town> find(Node<Town> current, String townName) {
		// TODO Auto-generated method stub
		if (current == null) {
			return null;
		}
		int vgl = compare(townName, current.getValue());
		if (vgl == 0) {		// Gefunden
			return current;
		}
		else if (vgl < 0) {	// Links
			return find(current.getLeft(), townName);
		}
		else {				// Rechts
			return find(current.getRight(), townName);
		}
		//return super.find(current, needle);
	
	
	
	}
	public int compare(String a, Town b)
	{
		if (a == null)
			return -1;
		if (b == null)
			return 1;
		if (a.equalsIgnoreCase(b.getName()))
			return 0;
		return a.compareToIgnoreCase(b.getName());
	}
	
}
