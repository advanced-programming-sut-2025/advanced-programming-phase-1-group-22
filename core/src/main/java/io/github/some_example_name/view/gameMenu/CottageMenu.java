package io.github.some_example_name.view.gameMenu;

import io.github.some_example_name.command.CommandClass;
import io.github.some_example_name.controller.gameMenu.GameMenuController;
import io.github.some_example_name.model.records.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.github.some_example_name.command.GameCommands.*;

public class CottageMenu extends GameMenu {
    private static CottageMenu instance;
    private final GameMenuController controller = new GameMenuController();
    private final Map<CommandClass, Function<String[], Response>> commandsFunctionMap = new HashMap<>();

    private CottageMenu() {
        commandsFunctionMap.put(craftingShowRecipes, controller::craftingShowRecipes);
        commandsFunctionMap.put(cookingShowRecipes, controller::cookingShowRecipes);
        commandsFunctionMap.put(cookingRefrigeratorPick, controller::cookingRefrigeratorPick);
        commandsFunctionMap.put(cookingRefrigeratorShow, controller::cookingRefrigeratorShow);
        commandsFunctionMap.put(craftingCraft, controller::craftingCraft);
        commandsFunctionMap.put(cookingRefrigeratorPut, controller::cookingRefrigeratorPut);
        commandsFunctionMap.put(cookingPrepare, controller::cookingPrepare);
        commandsFunctionMap.putAll(super.getFunctionsMap());

    }

    public static CottageMenu getInstance() {
        if (instance == null) {
            instance = new CottageMenu();
        }
        return instance;
    }
    @Override
    public Map<CommandClass, Function<String[], Response>> getFunctionsMap() {
        return commandsFunctionMap;
    }

}
