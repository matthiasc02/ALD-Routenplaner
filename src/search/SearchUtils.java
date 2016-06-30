package search;

import java.util.ArrayList;

public class SearchUtils {
	public static ArrayList<Integer> predToWay(int[] pred, int from, int to) {
		ArrayList<Integer> way = new ArrayList<Integer>();

		int i = to;
		while (i != from && i != -1) {
			way.add(0, i);
			i = pred[i];
		}
		if (i != -1) {
			way.add(0, from);
		} else {
			// no route to target found
			way.clear();
		}

		return way;
	}
}