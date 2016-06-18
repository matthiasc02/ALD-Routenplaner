package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

import Graph.Graph;
import command.OptionCommandValidator;
import command.SearchStrategyCommandValidator;
import data.Route;
import data.RouteConverter;
import data.Town;
import data.TownResolver;
import output.RouteFormatter;
import search.SearchStrategy;
import search.SearchStrategyResolver;

public class Server {

	private static final int PORT = 9995;

	private ServerSocket serverSocket = null;
	private OptionCommandValidator optionCommandValidator = new OptionCommandValidator();
	private SearchStrategyCommandValidator searchStrategyCommandValidator = new SearchStrategyCommandValidator();
	private SearchStrategyResolver searchStrategyResolver = new SearchStrategyResolver();
	private RouteConverter routeConverter = new RouteConverter();
	private TownResolver townResolver = new TownResolver();
	private RouteFormatter routeFormatter = new RouteFormatter();
	private PrintWriter pw;
	private BufferedReader br;
	private Socket socket;
	private OutputStream os;

	private List<Town> townList;
	private List<Route> routeList;

	public Server(List<Town> townList, List<Route> routeList) {
		this.townList = townList;
		this.routeList = routeList;
	}

	public static int getPort() {
		return PORT;
	}

	public void run() {

		try {
			serverSocket = new ServerSocket(PORT);
			// as we are lazy we can start the cmd shell by the program itself

			ProcessBuilder builder = new ProcessBuilder("CMD", "/C", "start");
			try {
				builder.start();
			} catch (IOException e) {
				e.printStackTrace();
			}

			while (true) {
				if (!serverSocket.isClosed()) {
					socket = serverSocket.accept();
					os = socket.getOutputStream();
					pw = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.ISO_8859_1), true);
					br = new BufferedReader(
							new InputStreamReader(socket.getInputStream(), StandardCharsets.ISO_8859_1));

					printStartText(pw);
					runStartup();
				}
			}
		} catch (IOException e) {
			System.out.println("Could not startup Server.");
			e.printStackTrace();
		} finally {
			try {
				shutDown();
			} catch (IOException e) {
				System.out.println("Could not shutdown Server. Maybe server is already down.");
				e.printStackTrace();
			}
		}

	}
	
	private void printStartText(PrintWriter pw) {
		pw.println("##########################");
		pw.println("Welcome to Routeplaner 1.0");
		pw.println("##########################");
	}

	private void printCommandOptionText(PrintWriter pw) {
		pw.println();
		pw.println(String.format("[%s] search for route", OptionCommandValidator.OPTION_COMMAND_SEARCH));
		pw.println(String.format("[%s] shutdown routeplaner", OptionCommandValidator.OPTION_COMMAND_SHUTDOWN));
		pw.printf("Please enter one of the above commands: ");
	}

	private void printSearchStrategyCommandText(PrintWriter pw) {
		pw.println();
		pw.println(String.format("[%s] dijkstra", SearchStrategyCommandValidator.SEARCH_STRATEGY_COMMAND_DIJKSTRA));
		pw.println(String.format("[%s] breadthfirst",
				SearchStrategyCommandValidator.SEARCH_STRATEGY_COMMAND_BREADTHFIRST));
		pw.println(String.format("[%s] depthfirst", SearchStrategyCommandValidator.SEARCH_STRATEGY_COMMAND_DEPTHFIRST));
		pw.println(String.format("[%s] shutdown routeplaner", OptionCommandValidator.OPTION_COMMAND_SHUTDOWN));
		pw.printf("Please enter one of the above search strategies: ");
	}

	public boolean isRunning() {
		return !serverSocket.isClosed();
	}

	public void shutDown() throws IOException {
		if (serverSocket != null) {
			serverSocket.close();
		}
	}

	private void shutDownServerOnInput(PrintWriter pw, Socket socket) throws IOException {
		pw.println("Routeplaner is going to shutdown...");
		pw.close();
		socket.close();
		shutDown();
	}

	private void runStartup() {
		try {
			printCommandOptionText(pw);

			String optionCommand = br.readLine();
			while (!optionCommandValidator.isValidOptionCommand(optionCommand)) {
				pw.println("Command '" + optionCommand + "' not found.");
				printCommandOptionText(pw);
				optionCommand = br.readLine();
			}

			// prüfe auf Eingabe von C (Close)

			if (optionCommandValidator.isShutDownOptionCommand(optionCommand)) {
				shutDownServerOnInput(pw, socket);
				return;
			} else {
				printSearchStrategyCommandText(pw);

				String searchStrategyCommand = br.readLine();

				while (!searchStrategyCommandValidator.isValidSearchStrategyCommand(searchStrategyCommand)) {
					// prüfe auf Eingabe von C (Close)
					if (optionCommandValidator.isShutDownOptionCommand(searchStrategyCommand)) {
						shutDownServerOnInput(pw, socket);
						return; // return zum rausgehen aus der Methode
					} else {
						pw.println("Command '" + searchStrategyCommand + "' not found.");
						printSearchStrategyCommandText(pw);
						searchStrategyCommand = br.readLine();
					}
				}

				printTownList();

				Town startTown = null;
				String startTownInputText = "Please enter the name of your start town: ";
				startTown = handleTownInput(startTownInputText, br);
				if (startTown == null) {
					shutDownServerOnInput(pw, socket);
					return;
				}

				Town destinationTown = null;
				String destinationTownInputText = "Please enter the name of your destination town: ";
				destinationTown = handleTownInput(destinationTownInputText, br);
				if (destinationTown == null) {
					shutDownServerOnInput(pw, socket);
					return;
				}

				Graph graph = routeConverter.convertRoutesToGraph(routeList);
				SearchStrategy searchStrategy = searchStrategyResolver.getSelectedSearchStrategy(searchStrategyCommand);
				if (startTown != null && destinationTown != null) {
					List<Integer> townIds = searchStrategy.search(graph, startTown.getId(), destinationTown.getId());
					if (townIds != null) {
						String formattedWayByIds = routeFormatter.getFormattedWayByIds(townIds);
						String formattedWayByNames = routeFormatter.getFormattedWayByNames(townIds, townList);
						pw.println();
						pw.println(formattedWayByIds);
						pw.println(formattedWayByNames);
					} else {
						pw.println("townID was NULL");
					}
					pw.println();
					pw.println();
					pw.println("##########################");
					pw.println("New route search");
					pw.println("##########################");
					runStartup();

				}
			}
		} catch (IOException e) {
			System.out.println("Could not startup Server.");
			e.printStackTrace();
		} finally {
			try {
				shutDown();
			} catch (IOException e) {
				System.out.println("Could not shutdown Server. Maybe server is already down.");
				e.printStackTrace();
			}
		}
	}

	private Town handleTownInput(String inputText, BufferedReader br) throws IOException {
		Town town = null;
		pw.printf(inputText);
		while (town == null) {
			String startTownInput = br.readLine();
			town = townResolver.getTownByName(townList, startTownInput);
			if (optionCommandValidator.isShutDownOptionCommand(startTownInput)) {
				return null;
			}
			if (town == null) {
				pw.printf("Your town couldn't be recognized. Please enter other town: ");
			}
		}
		return town;
	}

	private void printTownList() {
		pw.println();
		pw.println("You can select from the following cities:");
		for (Town town : townList) {
			pw.printf(town.getName());
			if (townList.indexOf(town) != (townList.size() - 1))
				pw.printf(", ");
		}
		pw.println();
		pw.println();
	}

}
