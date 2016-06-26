package data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Graph.Node;

public class TownResolver {

	public Town getTownByName(List<Town> townList, String townName) {
		TownSearchTree townSearchTree = convertTownListToTree(townList);
		if (townSearchTree == null) {
			return null;
		}

		Node<Town> node = townSearchTree.find(townName);
		return node != null ? node.getValue() : null;
	}

	public Map<Integer, Town> convertListToMap(List<Town> townList) {
		Map<Integer, Town> townMap = null;
		if (townList != null && !townList.isEmpty()) {
			townMap = new HashMap<Integer, Town>();
			for (Town town : townList) {
				townMap.put(town.getId(), town);
			}
		}
		return townMap;

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
