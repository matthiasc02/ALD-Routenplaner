package server;

import java.io.PrintWriter;
import java.util.List;

import command.OptionCommandValidator;
import command.SearchStrategyCommandValidator;
import data.Town;
import output.RouteFormatter;
import search.SearchResultData;

public class CommandPrintHandler {

	private PrintWriter printWriter;

	private RouteFormatter routeFormatter = new RouteFormatter();

	public CommandPrintHandler(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}

	public void printStartText() {
		printWriter.println("##########################");
		printWriter.println("Welcome to Routeplaner 1.0");
		printWriter.println("##########################");
	}

	public void printCommandOptionText() {
		printWriter.println();
		printWriter.println(String.format("[%s] search for route", OptionCommandValidator.OPTION_COMMAND_SEARCH));
		printWriter.println(String.format("[%s] shutdown routeplaner", OptionCommandValidator.OPTION_COMMAND_SHUTDOWN));
		printWriter.printf("Please enter one of the above commands: ");
	}

	public void printSearchStrategyCommandText() {
		printWriter.println();
		printWriter.println(
				String.format("[%s] dijkstra", SearchStrategyCommandValidator.SEARCH_STRATEGY_COMMAND_DIJKSTRA));
		printWriter.println(String.format("[%s] breadthfirst",
				SearchStrategyCommandValidator.SEARCH_STRATEGY_COMMAND_BREADTHFIRST));
		printWriter.println(
				String.format("[%s] depthfirst", SearchStrategyCommandValidator.SEARCH_STRATEGY_COMMAND_DEPTHFIRST));
		printWriter.println(String.format("[%s] shutdown routeplaner", OptionCommandValidator.OPTION_COMMAND_SHUTDOWN));
		printWriter.printf("Please enter one of the above search strategies: ");
	}

	public void printCommandNotFoundText(String command) {
		printWriter.println("Command '" + command + "' not found.");
	}

	public void printTownNotRecognizedText() {
		printWriter.printf("Your town couldn't be recognized. Please enter other town: ");
	}

	public void printTownList(List<Town> townList) {
		printWriter.println();
		printWriter.println("You can select from the following cities:");
		for (Town town : townList) {
			printWriter.printf(town.getName());
			if (townList.indexOf(town) != (townList.size() - 1))
				printWriter.printf(", ");
		}
		printWriter.println();
		printWriter.println();
	}

	public void printSearchResult(SearchResultData searchResultData, List<Town> townList) {
		List<Integer> townIds = searchResultData.getTownIdList();
		if (townIds != null) {
			printWay(townIds, townList);
		} else {
			printWriter.println("No route found.");
		}
	}

	public void printWay(List<Integer> townIds, List<Town> townList) {
		String formattedWayByIds = routeFormatter.getFormattedWayByIds(townIds);
		String formattedWayByNames = routeFormatter.getFormattedWayByNames(townIds, townList);
		printWriter.println();
		printWriter.println(formattedWayByIds);
		printWriter.println(formattedWayByNames);
	}

	public void printNewRouteSearch() {
		printWriter.println();
		printWriter.println();
		printWriter.println("##########################");
		printWriter.println("New route search");
		printWriter.println("##########################");
	}

}
