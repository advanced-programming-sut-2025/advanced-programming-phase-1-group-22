package io.github.some_example_name.view.mainMenu;

import io.github.some_example_name.command.CommandClass;
import io.github.some_example_name.controller.AccountMenuController;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.view.CommandProcessor;

import java.util.Map;
import java.util.function.Function;

import static io.github.some_example_name.command.AccountCommands.SHOW_CURRENT_MENU;

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
