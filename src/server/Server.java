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

	private static final int PORT = 9994;

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
			/*
			 * ProcessBuilder builder = new ProcessBuilder("CMD", "/C",
			 * "start"); try { builder.start(); } catch (IOException e) {
			 * e.printStackTrace(); }
			 */
			while (true) {
				if (!serverSocket.isClosed()) {
					socket = serverSocket.accept();
					os = socket.getOutputStream();
					pw = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.ISO_8859_1), true);
					br = new BufferedReader(
							new InputStreamReader(socket.getInputStream(), StandardCharsets.ISO_8859_1));

					pw.println("##########################");
					pw.println("Welcome to Routeplaner 1.0");
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

	private void printCommandOptionText(PrintWriter pw) {
		pw.println("Please enter one of the following commands:");
		pw.println(String.format("[%s] search for route", OptionCommandValidator.OPTION_COMMAND_SEARCH));
		pw.println(String.format("[%s] shutdown routeplaner", OptionCommandValidator.OPTION_COMMAND_SHUTDOWN));
	}

	private void printSearchStrategyCommandText(PrintWriter pw) {
		pw.println("Please enter one of the following search strategies:");
		pw.println(String.format("[%s] dijkstra", SearchStrategyCommandValidator.SEARCH_STRATEGY_COMMAND_DIJKSTRA));
		pw.println(String.format("[%s] breadthfirst",
				SearchStrategyCommandValidator.SEARCH_STRATEGY_COMMAND_BREADTHFIRST));
		pw.println(String.format("[%s] depthfirst", SearchStrategyCommandValidator.SEARCH_STRATEGY_COMMAND_DEPTHFIRST));
		pw.println(String.format("[%s] shutdown routeplaner", OptionCommandValidator.OPTION_COMMAND_SHUTDOWN));
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
				pw.println("You can select from the following cities:");
				for (Town town : townList) {
					pw.printf(town.getName());
					if (townList.indexOf(town) != (townList.size() - 1))
						pw.printf(", ");
				}
				pw.println();
				pw.println();
				Town startTown = null;
				pw.printf("Please enter the name of your start town: ");
				while (startTown == null) {
					String startTownInput = br.readLine();
					startTown = townResolver.getTownByName(townList, startTownInput);
					if (optionCommandValidator.isShutDownOptionCommand(startTownInput)) {
						shutDownServerOnInput(pw, socket);
						return;
					}
					if (startTown == null) {
						pw.printf("Your starting point couldn't be recognized. Please enter an other town! ");
					}
				}

				Town destinationTown = null;
				pw.printf("Please enter the name of your destination town: ");
				while (destinationTown == null) {
					String destinationTownInput = br.readLine();
					destinationTown = townResolver.getTownByName(townList, destinationTownInput);
					if (optionCommandValidator.isShutDownOptionCommand(destinationTownInput)) {
						shutDownServerOnInput(pw, socket);
						return;
					}
					if (destinationTown == null) {
						pw.printf("Your destination couldn't be recognized. Please enter an other town! ");
					}

					Graph graph = routeConverter.convertRoutesToGraph(routeList);
					SearchStrategy searchStrategy = searchStrategyResolver
							.getSelectedSearchStrategy(searchStrategyCommand);
					if (startTown != null && destinationTown != null) {
						List<Integer> townIds = searchStrategy.search(graph, startTown.getId(),
								destinationTown.getId());
						if (townIds != null) {
							String formattedWay = routeFormatter.getFormattedWay(townIds);
							pw.println(formattedWay);
						} else {
							pw.println("townID was NULL");
						}
						pw.println("");
						pw.println("");
						pw.println("##########################");
						pw.println("New route search");
						pw.println("##########################");
						runStartup();
						// TODO: format way and print it out

					}
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

}
