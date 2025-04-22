package view.gameMenu;

import command.CommandClass;
import controller.gameMenu.GameMainMenuController;
import controller.gameMenu.GameMenuController;
import model.exception.InvalidInputException;
import model.records.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;

import static command.GameCommands.*;
import static command.GameCommands.cookingPrepare;

public class GameMainMenu extends GameMenu {
    private static GameMainMenu instance;
    private final GameMenuController controller = new GameMenuController();
    private final Map<CommandClass, Function<String[], Response>> commandsFunctionMap = new HashMap<>();

    private GameMainMenu() {
        commandsFunctionMap.putAll(super.getFunctionsMap());
    }

    public static GameMainMenu getInstance() {
        if (instance == null) {
            instance = new GameMainMenu();
        }
        return instance;
    }

}
