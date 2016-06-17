package output;

import java.util.List;

public class RouteFormatter {

	public String getFormattedWay(List<Integer> townIds) {
		if (townIds == null || townIds.isEmpty()) {
			return "No route found!";
		}

		StringBuilder sb = new StringBuilder("Route: ");
		for (int i = 0; i < townIds.size(); i++) {
			sb.append(townIds.get(i));
			if (i < townIds.size() - 1) {
				sb.append(" -> ");
			}
		}

		return sb.toString();
	}

}
