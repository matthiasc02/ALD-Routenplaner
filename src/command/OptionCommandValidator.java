package command;

import java.util.ArrayList;
import java.util.List;

public class OptionCommandValidator {
	
	public static final String OPTION_COMMAND_SEARCH = "s";
	public static final String OPTION_COMMAND_SHUTDOWN = "c";
	
	public static final List<String> ALLOWED_OPTION_COMMAND_LIST = new ArrayList<String>();
	
	static {
		ALLOWED_OPTION_COMMAND_LIST.add(OPTION_COMMAND_SEARCH);
		ALLOWED_OPTION_COMMAND_LIST.add(OPTION_COMMAND_SHUTDOWN);
	}
	
	public boolean isValidOptionCommand(String optionCommand) {
		return ALLOWED_OPTION_COMMAND_LIST.contains(optionCommand);
	}
	
	public boolean isSearchOptionCommand(String optionCommand) {
		return isValidOptionCommand(optionCommand) && OPTION_COMMAND_SEARCH.equalsIgnoreCase(optionCommand);
	}
	
	public boolean isShutDownOptionCommand(String optionCommand) {
		return isValidOptionCommand(optionCommand) && OPTION_COMMAND_SHUTDOWN.equalsIgnoreCase(optionCommand);
	}
}
