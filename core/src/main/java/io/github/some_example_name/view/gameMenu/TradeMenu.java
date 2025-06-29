package io.github.some_example_name.view.gameMenu;

import io.github.some_example_name.command.CommandClass;
import io.github.some_example_name.controller.gameMenu.GameMenuController;
import io.github.some_example_name.controller.gameMenu.TradeMenuController;
import io.github.some_example_name.model.records.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.github.some_example_name.command.GameCommands.*;
import static io.github.some_example_name.command.TradeCommands.*;

public class TradeMenu extends GameMenu {

    private static TradeMenu instance;
    private final TradeMenuController controller = new TradeMenuController();
    private final Map<CommandClass, Function<String[], Response>> commandsFunctionMap = new HashMap<>();

    private TradeMenu() {
        commandsFunctionMap.put(START_TRADE, controller::startTrade);
        commandsFunctionMap.put(TRADE, controller::trade);
        commandsFunctionMap.put(TRADE_LIST, controller::tradeList);
        commandsFunctionMap.put(TRADE_RESPONSE, controller::tradeResponse);
        commandsFunctionMap.put(TRADE_HISTORY, controller::tradeHistory);
        commandsFunctionMap.putAll(super.getFunctionsMap());
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

