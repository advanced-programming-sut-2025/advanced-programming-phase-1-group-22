package view.gameMenu;

import command.CommandClass;
import controller.gameMenu.GameMenuController;
import controller.gameMenu.TradeMenuController;
import model.records.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static command.GameCommands.*;
import static command.TradeCommands.*;

public class TradeMenu extends GameMenu {

    private static TradeMenu instance;
    private final TradeMenuController controller = new TradeMenuController();
    private final Map<CommandClass, Function<String[], Response>> commandsFunctionMap = new HashMap<>();

    private TradeMenu() {
        commandsFunctionMap.putAll(super.getFunctionsMap());
        commandsFunctionMap.put(START_TRADE, controller::startTrade);
        commandsFunctionMap.put(TRADE, controller::trade);
        commandsFunctionMap.put(TRADE_LIST, controller::tradeList);
        commandsFunctionMap.put(TRADE_RESPONSE, controller::tradeResponse);
        commandsFunctionMap.put(TRADE_HISTORY, controller::tradeHistory);
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

