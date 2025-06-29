package io.github.some_example_name.view.mainMenu;

import io.github.some_example_name.command.CommandClass;
import io.github.some_example_name.controller.AccountMenuController;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.view.CommandProcessor;

import java.util.Map;
import java.util.function.Function;

import static io.github.some_example_name.command.AccountCommands.*;

public class LoginMenu implements CommandProcessor {
    private static LoginMenu instance;

    private LoginMenu() {
        super();
    }

    public static LoginMenu getInstance() {
        if (instance == null) {
            instance = new LoginMenu();
        }
        return instance;
    }

    private final AccountMenuController controller = new AccountMenuController();

    private final Map<CommandClass, Function<String[], Response>> commandsFunctionMap = Map.of(
            LOGIN_COMMANDS, controller::loginUser,
            REGISTER_COMMANDS, controller::registerUser,
            REGISTER_COMMANDS_RANDOM_PASSWORD, controller::registerUserRandomPass,
            FORGET_PASSWORD,controller::forgetPassword,
            EXIT, controller::exit,
            SHOW_CURRENT_MENU, controller::showCurrentMenu

    );


    @Override
    public Map<CommandClass, Function<String[], Response>> getFunctionsMap() {
        return commandsFunctionMap;
    }
}
