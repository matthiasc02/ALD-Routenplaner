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

import data.Route;
import data.RouteMappingCsvReader;
import data.Town;
import data.TownMappingCsvReader;
import search.RouteResolver;
import search.SearchResultData;

public class Server {

	private static final int PORT = 9995;

	private PrintWriter printWriter;
	private BufferedReader bufferedReader;
	private Socket socket;
	private OutputStream outputStream;

	private ServerSocket serverSocket = null;
	private TownMappingCsvReader townReader = new TownMappingCsvReader();
	private RouteMappingCsvReader routeReader = new RouteMappingCsvReader();
	private RouteResolver routeResolver = new RouteResolver();

	private List<Town> townList;
	private List<Route> routeList;

	private CommandInputHandler commandInputHandler;
	private CommandPrintHandler commandPrintHandler;

	public void run() {
		try {
			serverSocket = new ServerSocket(PORT);

			// as we are lazy we can start the cmd shell by the program itself
			ProcessBuilder builder = new ProcessBuilder("CMD", "/C", "start", "telnet", "localhost", "" + PORT);
			try {
				builder.start();
			} catch (IOException e) {
				e.printStackTrace();
			}

			townList = (List<Town>) townReader.parseFile();
			routeList = (List<Route>) routeReader.parseFile();

			while (true) {
				if (!serverSocket.isClosed()) {
					socket = serverSocket.accept();
					outputStream = socket.getOutputStream();
					printWriter = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.ISO_8859_1),
							true);
					bufferedReader = new BufferedReader(
							new InputStreamReader(socket.getInputStream(), StandardCharsets.ISO_8859_1));

					commandPrintHandler = new CommandPrintHandler(printWriter);
					commandInputHandler = new CommandInputHandler(this, printWriter, bufferedReader);

					commandPrintHandler.printStartText();

					startRoutePlannerRoutine();

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

	public boolean isRunning() {
		return !serverSocket.isClosed();
	}

	public void shutDown() throws IOException {
		if (serverSocket != null) {
			serverSocket.close();
		}
	}

	public void shutDownServerOnInput() throws IOException {
		printWriter.println("Routeplaner is going to shutdown...");
		printWriter.close();
		socket.close();
		shutDown();
		System.exit(0);
	}

	private void startRoutePlannerRoutine() throws IOException {
		commandPrintHandler.printCommandOptionText();
		commandInputHandler.handleCommandOptionInput();

		commandPrintHandler.printSearchStrategyCommandText();
		String searchStrategyCommand = commandInputHandler.handleSearchStrategyCommandInput();

		commandPrintHandler.printTownList(townList);
		Town startTown = commandInputHandler.handleTownInput("Please enter the name of your start town: ", townList);
		Town destinationTown = commandInputHandler.handleTownInput("Please enter the name of your destination town: ",
				townList);

		SearchResultData searchResultData = routeResolver.doRouteSearch(startTown, destinationTown, routeList,
				searchStrategyCommand, townList.size());
		commandPrintHandler.printSearchResult(searchResultData, townList);

		startNewRouteSearch();
	}

	private void startNewRouteSearch() throws IOException {
		commandPrintHandler.printNewRouteSearch();
		startRoutePlannerRoutine();
	}
}
