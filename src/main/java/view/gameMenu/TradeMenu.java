package view.gameMenu;

import command.CommandClass;
import controller.gameMenu.GameMenuController;
import model.records.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static command.GameCommands.*;

public class TradeMenu extends GameMenu {

    private static TradeMenu instance;
    private final GameMenuController controller = new GameMenuController();
    private final Map<CommandClass, Function<String[], Response>> commandsFunctionMap = new HashMap<>();

    private TradeMenu() {
        commandsFunctionMap.putAll(super.getFunctionsMap());
        commandsFunctionMap.put(trade, controller::trade);
        commandsFunctionMap.put(tradeList, controller::tradeList);
        commandsFunctionMap.put(tradeResponse, controller::tradeResponse);
        commandsFunctionMap.put(tradeHistory, controller::tradeHistory);
    }

    public static TradeMenu getInstance() {
        if (instance == null) {
            instance = new TradeMenu();
        }
        return instance;
    }

    @Override
    public Map<CommandClass, Function<String[], Response>> getFunctionsMap() {
        return commandsFunctionMap;
    }
}

