package data;

import Graph.BaseTree;
import Graph.Node;

public class TownSearchTree extends BaseTree<Town> {

	@Override
	protected int compare(Town a, Town b) {
		String temp_a = "";
		String temp_b = "";
		if (a == null)
			return -1;
		if (b == null)
			return 1;
		if (a.getName() != null & b.getName() != null) {
			temp_a = a.getName();
			temp_b = b.getName();
			if (a.getName().equalsIgnoreCase(b.getName()))
				return 0;
		}
		return temp_a.compareTo(temp_b);
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
