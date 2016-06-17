package command;

import java.util.ArrayList;
import java.util.List;

public class SearchStrategyCommandValidator {

	public static final String SEARCH_STRATEGY_COMMAND_DIJKSTRA = "d";
	public static final String SEARCH_STRATEGY_COMMAND_BREADTHFIRST = "bf";
	public static final String SEARCH_STRATEGY_COMMAND_DEPTHFIRST = "df";
	
	public static final List<String> ALLOWED_SEARCH_STRATEGY_COMMAND_LIST = new ArrayList<String>();
	
	static {
		ALLOWED_SEARCH_STRATEGY_COMMAND_LIST.add(SEARCH_STRATEGY_COMMAND_DIJKSTRA);
		ALLOWED_SEARCH_STRATEGY_COMMAND_LIST.add(SEARCH_STRATEGY_COMMAND_BREADTHFIRST);
		ALLOWED_SEARCH_STRATEGY_COMMAND_LIST.add(SEARCH_STRATEGY_COMMAND_DEPTHFIRST);
	}
	
	public boolean isValidSearchStrategyCommand(String searchStrategyCommand) {
		return ALLOWED_SEARCH_STRATEGY_COMMAND_LIST.contains(searchStrategyCommand);
	}
}