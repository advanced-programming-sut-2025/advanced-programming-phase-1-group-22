package io.github.some_example_name.view.gameMenu;

import io.github.some_example_name.command.CommandClass;
import io.github.some_example_name.controller.gameMenu.GameMenuController;
import io.github.some_example_name.controller.gameMenu.StoreMenuController;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;

import io.github.some_example_name.controller.gameMenu.StoreMenuController;
import io.github.some_example_name.model.exception.InvalidInputException;
import io.github.some_example_name.model.records.Response;

import static io.github.some_example_name.command.GameCommands.*;
import static io.github.some_example_name.command.GameCommands.cookingPrepare;

public class StoreMenu extends GameMenu {
    private static StoreMenu instance;
    private final GameMenuController controller = new GameMenuController();
    private final Map<CommandClass, Function<String[], Response>> commandsFunctionMap = new HashMap<>();

    private StoreMenu() {
        commandsFunctionMap.putAll(super.getFunctionsMap());
        commandsFunctionMap.put(BUILD_FARM_BUILDING, controller::build);
        commandsFunctionMap.put(BUY_ANIMAL, controller::buyAnimal);
        commandsFunctionMap.put(showAllProducts, controller::showAllProducts);
        commandsFunctionMap.put(showAllAvailableProducts, controller::showAllAvailableProducts);
        commandsFunctionMap.put(purchase, controller::purchase);
        commandsFunctionMap.put(purchase1, controller::purchase1);
    }

    public static StoreMenu getInstance() {
        if (instance == null) {
            instance = new StoreMenu();
        }
        return instance;
    }

    @Override
    public Map<CommandClass, Function<String[], Response>> getFunctionsMap() {
        return commandsFunctionMap;
    }

}
