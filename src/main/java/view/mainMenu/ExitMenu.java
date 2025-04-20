package view.mainMenu;

import command.CommandClass;
import controller.AccountMenuController;
import model.records.Response;
import view.CommandProcessor;

import java.util.Map;
import java.util.function.Function;

import static command.AccountCommands.SHOW_CURRENT_MENU;

public class ExitMenu implements CommandProcessor {
    private static ExitMenu instance;

    private ExitMenu() {
    }

    public static ExitMenu getInstance() {
        if (instance == null) {
            instance = new ExitMenu();
        }
        return instance;
    }

    private final AccountMenuController controller = new AccountMenuController();

    @Override
    public Map<CommandClass, Function<String[], Response>> getFunctionsMap() {
        return Map.of(
                SHOW_CURRENT_MENU, controller::showCurrentMenu

        );
    }
}
