package view.mainMenu;

import command.CommandClass;
import model.records.Response;
import view.CommandProcessor;

import java.util.Map;
import java.util.function.Function;

public class ExitMenu implements CommandProcessor {
    private static ExitMenu instance;
    private ExitMenu() {}
    public static ExitMenu getInstance() {
        if (instance == null) {
            instance = new ExitMenu();
        }
        return instance;
    }
    @Override
    public Map<CommandClass, Function<String[], Response>> getFunctionsMap() {
        return Map.of();
    }
}
