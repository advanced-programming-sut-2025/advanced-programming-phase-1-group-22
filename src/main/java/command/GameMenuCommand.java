package command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommand implements Command{
	SHOW_ENERGY("show\\s+energy"),
	ENERGY_SET("set\\+energy\\s+-v\\s+(\\d+)"),
	SET_ENERGY_UNLIMITED("energy\\s+unlimited"),
	SHOW_INVENTORY("inventory\\s+show"),
	REMOVE_FROM_INVENTORY("inventory\\s+trash\\s+-i\\s+([a-zA-Z ]+)\\s*(\\d+)?"),
	TOOL_EQUIP("tool\\s+equip\\s+()"),
	SHOW_CURRENT_TOOL("tools\\s+show\\s+current"),
	SHOW_AVAILABLE_TOOLS("tools\\s+show\\s+available"),
	UPGRADE_TOOL("tools\\s+upgrade\\s+([a-zA-Z ]+)"),
	USE_TOOL("tool\\s+use\\s+-d\\s+(north|south|west|east|northwest|northeast|southeast|southwest)")
	;
	private final String pattern;

	GameMenuCommand(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public Matcher getMatcher(String input) {
		Matcher matcher = Pattern.compile(this.pattern).matcher(input);

		if (matcher.matches()) {
			return matcher;
		}
		return null;
	}
}
