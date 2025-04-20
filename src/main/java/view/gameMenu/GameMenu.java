package view.gameMenu;

import command.GameMenuCommand;
import controller.GameMenuController;
import controller.gameMenu.GameMenuController;
import view.Menu;

import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenu {
	private final GameMenuController gameMenuController = new GameMenuController();

	Matcher matcher;

	public void check(Scanner scanner) {
		String input = scanner.nextLine();
		if ((matcher = GameMenuCommand.SHOW_ENERGY.getMatcher(input.trim()))!=null) {
			System.out.println(gameMenuController.showPlayerEnergy());
		} else if ((matcher = GameMenuCommand.ENERGY_SET.getMatcher(input.trim()))!=null) {
			gameMenuController.setPlayerEnergy(Integer.parseInt(matcher.group(1)));
		} else if ((matcher = GameMenuCommand.SET_ENERGY_UNLIMITED.getMatcher(input.trim()))!=null) {
			gameMenuController.setPlayerUnlimitedEnergy();
		} else if ((matcher = GameMenuCommand.SHOW_INVENTORY.getMatcher(input.trim()))!=null) {
			System.out.println(gameMenuController.showPlayerInventory());
		} else if ((matcher = GameMenuCommand.REMOVE_FROM_INVENTORY.getMatcher(input.trim()))!=null) {
			if (matcher.groupCount()==2) {
				System.out.println(gameMenuController.removeFromPlayerInventory(matcher.group(1),
						true, Integer.parseInt(matcher.group(2))));
			} else {
				System.out.println(gameMenuController.removeFromPlayerInventory(matcher.group(1), false));
			}
		} else if ((matcher = GameMenuCommand.TOOL_EQUIP.getMatcher(input.trim()))!=null) {
			System.out.println(gameMenuController.toolEquip(matcher.group(1)));
		} else if ((matcher = GameMenuCommand.SHOW_CURRENT_TOOL.getMatcher(input.trim()))!=null) {
			System.out.println(gameMenuController.showCurrentTool());
		} else if ((matcher = GameMenuCommand.SHOW_AVAILABLE_TOOLS.getMatcher(input.trim()))!=null) {
			System.out.println(gameMenuController.showAvailableTools());
		} else if ((matcher = GameMenuCommand.UPGRADE_TOOL.getMatcher(input.trim()))!=null) {
			System.out.println(gameMenuController.upgradeTool(matcher.group(1)));
		} else if ((matcher = GameMenuCommand.USE_TOOL.getMatcher(input.trim()))!=null) {
			System.out.println(gameMenuController.useTool(matcher.group(1)));
		} else if ((matcher = GameMenuCommand.PICK_PRODUCT.getMatcher(input.trim()))!=null) {
			System.out.println(gameMenuController.pickFromFloor());
		}
	}
import java.util.regex.Matcher;

public abstract class GameMenu extends Menu {
    private final String exitGame = "^\\s*exit\\s+game\\s*$";
    private final String terminateGame = "^\\s*terminate\\s+game\\s*$";
    private final String nextTurn = "^\\s*next\\s+Turn\\s*$";
    private final String time = "^\\s*time\\s*$";
    private final String date = "^\\s*date\\s*$";
    private final String dateTime = "^\\s*dateTime\\s*$";
    private final String dayOfTheWeek = "^\\s*day\\s+of\\s+the\\s+week\\s*$";
    private final String season = "^\\s*season\\s*$";
    private final String C_AdvanceTime = "^\\s*cheat\\s+advance\\s+time\\s+(?<X>\\d+)h\\s*$";
    private final String C_AdvanceDate = "^\\s*cheat\\s+advance\\s+date\\s+(?<X>\\d+)d\\s*$";
    private final String C_Thor = "^\\s*cheat\\s+thor\\s+(?<X>\\d+)\\s+(?<Y>\\d+)\\s*$";
    private final String weather = "^\\s*weather\\s*$";
    private final String weatherForecast = "^\\s*weather\\s+forecast\\s*$";
    private final String C_WeatherSet = "^\\s*cheat\\s+weather\\s+set\\s+(?<Type>\\S+)\\s*$";
    private final String greenhouseBuild = "^\\s*greenhouse\\s+build\\s*$";
    private final String walk = "^\\s*walk\\s+-l\\s+(?<X>\\d+)\\s*,\\s*(?<Y>\\d+)\\s*$";
    private final String printMap = "^\\s*print\\s+map\\s+-l\\s+(?<X>\\d+)\\s*,\\s*(?<Y>\\d+)\\s+" +
            "-s\\s+(?<size>\\d+)\\s*$";
    private final String helpReadingMap = "^\\s*help\\s+reading\\s+map\\s*$";




    private final GameMenuController controller = new GameMenuController();

    public boolean check(String input) {
        Matcher matcher = null;
        if (isMatched(input, exitGame) != null) {
            controller.exitGame();
        } else if (isMatched(input, terminateGame) != null) {
            controller.terminateGame();
        } else if (isMatched(input, nextTurn) != null) {
            controller.nextTurn();
        } else if (isMatched(input, time) != null) {
            controller.time();
        } else if (isMatched(input, date) != null) {
            controller.date();
        } else if (isMatched(input, dateTime) != null) {
            controller.dateTime();
        } else if (isMatched(input, dayOfTheWeek) != null) {
            controller.dayOfTheWeek();
        } else if (isMatched(input, season) != null) {
            controller.season();
        } else if (isMatched(input, weather) != null) {
            controller.weather();
        } else if (isMatched(input, weatherForecast) != null) {
            controller.weatherForecast();
        } else if (isMatched(input, greenhouseBuild) != null) {
            controller.greenhouseBuild();
        } else if (isMatched(input, helpReadingMap) != null) {
            controller.helpReadingMap();
        } else if ((matcher = isMatched(input, C_Thor)) != null) {
            controller.C_Thor(matcher.group("X"), matcher.group("Y"));
        } else if ((matcher = isMatched(input, C_WeatherSet)) != null) {
            controller.C_WeatherSet(matcher.group("Type"));
        } else if ((matcher = isMatched(input, walk)) != null) {
            controller.walk(matcher.group("X"), matcher.group("Y"));
        } else if ((matcher = isMatched(input, printMap)) != null) {
            controller.printMap(matcher.group("X"), matcher.group("Y"), matcher.group("size"));
        } else if ((matcher = isMatched(input, C_AdvanceTime)) != null) {
            controller.C_AdvanceTime(matcher.group("X"));
        } else if ((matcher = isMatched(input, C_AdvanceDate)) != null) {
            controller.C_AdvanceDate(matcher.group("X"));
        } else {
            return false;
        }
        return true;
    }
}
