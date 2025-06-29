package io.github.some_example_name.view.mainMenu;

import io.github.some_example_name.command.CommandClass;
import io.github.some_example_name.controller.mainMenu.MainMenuController;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.view.CommandProcessor;

import java.util.Map;
import java.util.function.Function;

import static io.github.some_example_name.command.AccountCommands.*;
import static io.github.some_example_name.command.GameInitCommands.*;

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
            SHOW_CURRENT_MENU, controller::showCurrentMenu,
            newGame, controller::newGame,
            loadGame, controller::loadGame

    );


    @Override
    public Map<CommandClass, Function<String[], Response>> getFunctionsMap() {
        return commandsFunctionMap;
    }
}
