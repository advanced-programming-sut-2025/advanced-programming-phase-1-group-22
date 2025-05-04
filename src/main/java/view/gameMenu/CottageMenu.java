package view.gameMenu;

import command.CommandClass;
import controller.gameMenu.GameMenuController;
import model.records.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static command.GameCommands.*;

public class CottageMenu extends GameMenu {
    private static CottageMenu instance;
    private final GameMenuController controller = new GameMenuController();
    private final Map<CommandClass, Function<String[], Response>> commandsFunctionMap = new HashMap<>();

    private CottageMenu() {
        commandsFunctionMap.put(craftingShowRecipes, controller::craftingShowRecipes);
        commandsFunctionMap.put(cookingShowRecipes, controller::cookingShowRecipes);
        commandsFunctionMap.put(cookingRefrigeratorPick, controller::cookingRefrigeratorPick);
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
