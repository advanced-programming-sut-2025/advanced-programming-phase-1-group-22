package view.mainMenu;

import command.CommandClass;
import controller.mainMenu.MainMenuController;
import model.records.Response;
import view.CommandProcessor;

import java.util.Map;
import java.util.function.Function;

import static command.GameInitCommands.gameMap;

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
