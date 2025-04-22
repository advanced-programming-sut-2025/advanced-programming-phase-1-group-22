package view.mainMenu;

import command.CommandClass;
import controller.MainMenuController;
import model.records.Response;
import view.CommandProcessor;

import java.util.Map;
import java.util.function.Function;

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
}
