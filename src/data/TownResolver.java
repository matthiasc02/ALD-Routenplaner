package data;

import java.util.List;

import Graph.Node;

public class TownResolver {
	
	public Town getTownByName(List<Town> townList, String townName) {
		TownSearchTree townSearchTree = convertTownListToTree(townList);
		if (townSearchTree == null) {
			return null;
		}
		
		Town searchTown = new Town(-1, townName);
		Node<Town> node = townSearchTree.find(searchTown);
		return node != null ? node.getValue() : null;
	}
	
	private TownSearchTree convertTownListToTree(List<Town> townList) {
		if (townList == null || townList.isEmpty()) {
			return null;
		}
		
		TownSearchTree townSearchTree = new TownSearchTree();
		for (Town town : townList) {
			townSearchTree.add(town);
		}
		
		return townSearchTree;
	}
}
