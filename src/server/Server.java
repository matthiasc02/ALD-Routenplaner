package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
	
	private List<Town> townList;
	private List<Route> routeList;
	
	public Server(List<Town> townList, List<Route> routeList) {
		this.townList = townList;
		this.routeList = routeList;
	}
	

	public void run() {
		try {
			serverSocket = new ServerSocket(PORT);
			while (true) {
				Socket socket = serverSocket.accept();

				OutputStream os = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(os, true);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				pw.println("##########################");
				pw.println("Welcome to Routeplaner 1.0");
				pw.println("##########################");
				printCommandOptionText(pw);

				String optionCommand = br.readLine();
				while (!optionCommandValidator.isValidOptionCommand(optionCommand)) {
					pw.println("Command '" + optionCommand + "' not found.");
					printCommandOptionText(pw);
					optionCommand = br.readLine();
				}

				if (optionCommandValidator.isShutDownOptionCommand(optionCommand)) {
					pw.println("Routeplaner is going to shutdown...");
					pw.close();
					socket.close();
					shutDown();
					return;
				} else {
					printSearchStrategyCommandText(pw);

					String searchStrategyCommand = br.readLine();
					while (!searchStrategyCommandValidator.isValidSearchStrategyCommand(searchStrategyCommand)) {
						pw.println("Command '" + searchStrategyCommand + "' not found.");
						printSearchStrategyCommandText(pw);
						searchStrategyCommand = br.readLine();
					}
					
					
					Town startTown = null;
					pw.println("Please enter the name of your start town:");
					while (startTown == null) {
						String startTownInput = br.readLine();
						startTown = townResolver.getTownByName(townList, startTownInput);
						if (startTown == null) {
							pw.println("Your starting point couldn't be recognized. Please try again!");
						}
					}
					
					Town destinationTown = null;
					pw.println("Please enter the name of your destination town:");
					while (destinationTown == null) {
						String destinationTownInput = br.readLine();
						destinationTown = townResolver.getTownByName(townList, destinationTownInput);
						if (destinationTown == null) {
							pw.println("Your destination couldn't be recognized. Please try again!");
					}
					
					Graph graph = routeConverter.convertRoutesToGraph(routeList);
					SearchStrategy searchStrategy = searchStrategyResolver.getSelectedSearchStrategy(searchStrategyCommand);
					List<Integer> townIds = searchStrategy.search(graph, startTown.getId(), destinationTown.getId());
					String formattedWay = routeFormatter.getFormattedWay(townIds);
					pw.println(formattedWay);
					//TODO: format way and print it out
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
	}

	public boolean isRunning() {
		return !serverSocket.isClosed();
	}

	public void shutDown() throws IOException {
		if (serverSocket != null) {
			serverSocket.close();
		}
	}
}