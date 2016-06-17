package output;

import java.util.List;

public class RouteFormatter {

	public String getFormattedWay(List<Integer> townIds) {
		//TOOD: use stringbuilder
		String formattedWay = "";
		
		for(Integer townId : townIds) {
			formattedWay += townId + " to ";
		}
		
		return formattedWay;
	}
	
}
