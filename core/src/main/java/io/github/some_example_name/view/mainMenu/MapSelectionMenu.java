package io.github.some_example_name.view.mainMenu;

import io.github.some_example_name.command.CommandClass;
import io.github.some_example_name.controller.mainMenu.MainMenuController;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.view.CommandProcessor;

import java.util.Map;
import java.util.function.Function;

import static io.github.some_example_name.command.GameInitCommands.gameMap;

public class MapSelectionMenu implements CommandProcessor {
    private static MapSelectionMenu instance;

    private MapSelectionMenu() {
    }

    public static MapSelectionMenu getInstance() {
        if (instance == null) {
            instance = new MapSelectionMenu();
        }
        return instance;
    }

    private final MainMenuController controller = new MainMenuController();
    private final Map<CommandClass, Function<String[], Response>> commandsFunctionMap = Map.of(
            gameMap, controller::gameMap
    );


    @Override
    public Map<CommandClass, Function<String[], Response>> getFunctionsMap() {
        return commandsFunctionMap;
    }
}
