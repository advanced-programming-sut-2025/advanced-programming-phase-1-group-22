package io.github.some_example_name.view.gameMenu;

import io.github.some_example_name.command.CommandClass;
import io.github.some_example_name.controller.gameMenu.GameMainMenuController;
import io.github.some_example_name.controller.gameMenu.GameMenuController;
import io.github.some_example_name.model.exception.InvalidInputException;
import io.github.some_example_name.model.records.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;

import static io.github.some_example_name.command.GameCommands.*;
import static io.github.some_example_name.command.GameCommands.cookingPrepare;

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
