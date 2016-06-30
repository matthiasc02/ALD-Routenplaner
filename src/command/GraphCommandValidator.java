package command;

import java.util.ArrayList;
import java.util.List;

public class GraphCommandValidator {

	public static final String DIRECTED = "d";
	public static final String NOT_DIRECTED = "u";

	public static final List<String> ALLOWED_GRAPH_COMMAND_LIST = new ArrayList<String>();

	static {
		ALLOWED_GRAPH_COMMAND_LIST.add(DIRECTED);
		ALLOWED_GRAPH_COMMAND_LIST.add(NOT_DIRECTED);
		ALLOWED_GRAPH_COMMAND_LIST.add(OptionCommandValidator.OPTION_COMMAND_SHUTDOWN);
	}

	public boolean isValidGraphCommand(String graphCommand) {
		return ALLOWED_GRAPH_COMMAND_LIST.contains(graphCommand);
	}
}
