package search;

import java.util.ArrayList;

public class SearchUtils {
	public static ArrayList<Integer> predToWay(int[] pred, int from, int to) {
		ArrayList<Integer> way = new ArrayList<Integer>();

		int i = to;
		while (i != from) {
			way.add(0, i);
			i = pred[i];
		}
		way.add(0, from);

		return way;
	}
}
