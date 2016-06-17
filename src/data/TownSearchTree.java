package data;

import Graph.BaseTree;

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
}
