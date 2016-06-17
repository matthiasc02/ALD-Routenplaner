package search;

import java.util.HashMap;
import java.util.Map;

import command.SearchStrategyCommandValidator;


public class SearchStrategyResolver {
	
	public static final Map<String, SearchStrategy> SEARCH_STRATEGY_COMMAND_MAP = new HashMap<String, SearchStrategy>();
	
	static {
		SEARCH_STRATEGY_COMMAND_MAP.put(SearchStrategyCommandValidator.SEARCH_STRATEGY_COMMAND_DIJKSTRA, new DijkstraSearchStrategy());
		SEARCH_STRATEGY_COMMAND_MAP.put(SearchStrategyCommandValidator.SEARCH_STRATEGY_COMMAND_BREADTHFIRST, new BreadthFirstSearchStrategy());
		SEARCH_STRATEGY_COMMAND_MAP.put(SearchStrategyCommandValidator.SEARCH_STRATEGY_COMMAND_DEPTHFIRST, new DepthFirstSearchStrategy());
	}
	
	public SearchStrategy getSelectedSearchStrategy(String searchStrategyCommand) {
		return SEARCH_STRATEGY_COMMAND_MAP.get(searchStrategyCommand);
	}
}
