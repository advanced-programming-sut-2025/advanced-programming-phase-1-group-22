package view.mainMenu;

import command.CommandClass;
import controller.MainMenuController;
import model.records.Response;
import view.CommandProcessor;

import java.util.Map;
import java.util.function.Function;
import controller.mainMenu.MainMenuController;
import model.exception.InvalidInputException;
import view.Menu;

import static command.AccountCommands.*;

public class MainMenu implements CommandProcessor {
    private static MainMenu menu;

    private MainMenu() {
    }

    public static MainMenu getInstance() {
        if (menu == null) {
            menu = new MainMenu();
        }
        return menu;
    }

    private final MainMenuController controller = new MainMenuController();

    private final Map<CommandClass, Function<String[], Response>> commandsFunctionMap = Map.of(
            USER_LOGOUT, controller::logout,
            ENTER_MENU, controller::switchMenu,
            SHOW_CURRENT_MENU, controller::showCurrentMenu

    );


    @Override
    public Map<CommandClass, Function<String[], Response>> getFunctionsMap() {
        return commandsFunctionMap;
    }
import java.util.Scanner;
import java.util.regex.Matcher;

public class MainMenu extends Menu {
    private final String newGame = "^\\s*game\\s+new\\s+-u\\s+(?<players>.*)\\s*$";
    private final String loadGame = "^\\s*load\\s+game\\s*$";

    private final MainMenuController controller = new MainMenuController();

    @Override
    public void checkCommand(Scanner scanner) {
        String input = scanner.nextLine();
        Matcher matcher;
        if ((matcher = isMatched(input, newGame)) != null) {
            controller.newGame(matcher.group("players"));
        } else if (isMatched(input, loadGame) != null) {
            controller.loadGame();
        } else {
            throw new InvalidInputException("Command Not Found!");
        }
    }
}
