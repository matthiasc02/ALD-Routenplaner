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
}
