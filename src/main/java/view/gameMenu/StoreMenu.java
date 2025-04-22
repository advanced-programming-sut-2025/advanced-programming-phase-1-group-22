package view.gameMenu;

import command.CommandClass;
import controller.gameMenu.GameMenuController;
import controller.gameMenu.StoreMenuController;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;

import controller.gameMenu.StoreMenuController;
import model.exception.InvalidInputException;
import model.records.Response;

import static command.GameCommands.*;
import static command.GameCommands.cookingPrepare;

public class StoreMenu  extends  GameMenu {
    private static StoreMenu instance;
    private final GameMenuController controller = new GameMenuController();
    private final Map<CommandClass, Function<String[], Response>> commandsFunctionMap = new HashMap<>();

    private StoreMenu() {
        commandsFunctionMap.putAll(super.getFunctionsMap());
        commandsFunctionMap.put(build, controller::build);
        commandsFunctionMap.put(buyAnimal, controller::buyAnimal);
        commandsFunctionMap.put(showAllProducts, controller::showAllProducts);
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
