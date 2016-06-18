package output;

import java.util.List;
import java.util.Map;

import data.Town;
import data.TownResolver;

public class RouteFormatter {

	TownResolver townResolver = new TownResolver();

	public String getFormattedWayByIds(List<Integer> townIds) {
		if (townIds == null || townIds.isEmpty()) {
			return "No route found!";
		}

		StringBuilder sb = new StringBuilder("Route by town ids: ");
		for (int i = 0; i < townIds.size(); i++) {
			sb.append(townIds.get(i));
			if (i < townIds.size() - 1) {
				sb.append(" -> ");
			}
		}

		return sb.toString();
	}

	public String getFormattedWayByNames(List<Integer> townIds, List<Town> towns) {
		if (townIds == null || townIds.isEmpty()) {
			return "No route found!";
		}

		Map<Integer, Town> townMap = townResolver.convertListToMap(towns);

		StringBuilder sb = new StringBuilder("Route by town names: ");
		for (int i = 0; i < townIds.size(); i++) {
			Town town = townMap.get(townIds.get(i));
			sb.append(town.getName());
			if (i < townIds.size() - 1) {
				sb.append(" -> ");
			}
		}

		return sb.toString();
	}

}
