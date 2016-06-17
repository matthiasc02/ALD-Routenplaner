package Graph;

import java.util.List;

import data.Town;

public class TownTree extends BaseTree<Town> {
	
	private List<Town> towns;

	@Override
	protected int compare(Town a, Town b) {
		// TODO Auto-generated method stub
		return(a.getName().compareTo(b.getName()));
		
	}

	
	
	
}
