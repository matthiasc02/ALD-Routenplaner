package data;

import java.util.List;

public class TownResolver {
	
	public Town getTownByName(List<Town> townList, String townName) {
		if (townList == null || townList.isEmpty()) {
			return null;
		}
		
		for (Town town : townList) {
			if (town.getName() != null && town.getName().equalsIgnoreCase(townName)) {
				return town;
			}
		}
			
		return null;
	}
}
