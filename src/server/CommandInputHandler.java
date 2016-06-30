package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import command.GraphCommandValidator;
import command.OptionCommandValidator;
import command.SearchStrategyCommandValidator;
import data.Town;
import data.TownResolver;

public class CommandInputHandler {

	private PrintWriter printWriter;
	private BufferedReader bufferedReader;
	private Server server;

	private OptionCommandValidator optionCommandValidator = new OptionCommandValidator();
	private SearchStrategyCommandValidator searchStrategyCommandValidator = new SearchStrategyCommandValidator();
	private TownResolver townResolver = new TownResolver();
	// private Server server = new Server();
	private CommandPrintHandler commandPrintHandler;
	private GraphCommandValidator graphCommandValdiator = new GraphCommandValidator();

	public CommandInputHandler(Server server, PrintWriter printWriter, BufferedReader bufferedReader) {
		this.server = server;
		this.printWriter = printWriter;
		this.bufferedReader = bufferedReader;

		commandPrintHandler = new CommandPrintHandler(printWriter);
	}

	public String handleCommandOptionInput() throws IOException {
		String optionCommand = bufferedReader.readLine();
		while (!optionCommandValidator.isValidOptionCommand(optionCommand)) {
			commandPrintHandler.printCommandNotFoundText(optionCommand);
			commandPrintHandler.printCommandOptionText();
			optionCommand = bufferedReader.readLine();
		}

		if (optionCommandValidator.isShutDownOptionCommand(optionCommand)) {
			server.shutDownServerOnInput();
		}

		return optionCommand;
	}

	public String handleSearchStrategyCommandInput() throws IOException {
		String searchStrategyCommand = bufferedReader.readLine();
		while (!searchStrategyCommandValidator.isValidSearchStrategyCommand(searchStrategyCommand)) {
			commandPrintHandler.printCommandNotFoundText(searchStrategyCommand);
			commandPrintHandler.printSearchStrategyCommandText();
			searchStrategyCommand = bufferedReader.readLine();
		}

		if (optionCommandValidator.isShutDownOptionCommand(searchStrategyCommand)) {
			server.shutDownServerOnInput();
		}

		return searchStrategyCommand;
	}
	
	public String handleGraphCommandInput() throws IOException {
		String graphCommand = bufferedReader.readLine();
		while (!graphCommandValdiator.isValidGraphCommand(graphCommand)) {
			commandPrintHandler.printCommandNotFoundText(graphCommand);
			commandPrintHandler.printGraphCommandText();
			graphCommand = bufferedReader.readLine();
		}

		if (optionCommandValidator.isShutDownOptionCommand(graphCommand)) {
			server.shutDownServerOnInput();
		}

		return graphCommand;
	}

	public Town handleTownInput(String inputText, List<Town> townList) throws IOException {
		Town town = null;
		printWriter.printf(inputText);
		while (town == null) {
			String startTownInput = bufferedReader.readLine();
			town = townResolver.getTownByName(townList, startTownInput);
			if (optionCommandValidator.isShutDownOptionCommand(startTownInput)) {
				server.shutDownServerOnInput();
			}
			if (town == null) {
				commandPrintHandler.printTownNotRecognizedText();
			}
		}
		return town;
	}

}
