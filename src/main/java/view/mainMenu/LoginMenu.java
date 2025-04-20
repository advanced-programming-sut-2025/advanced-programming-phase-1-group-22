package view.mainMenu;

import command.CommandClass;
import controller.AccountMenuController;
import model.records.Response;
import view.CommandProcessor;

import java.util.Map;
import java.util.function.Function;

import static command.AccountCommands.*;

public class LoginMenu implements CommandProcessor {
    private static LoginMenu instance;

    private LoginMenu() {
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
            ENTER_MENU, controller::switchMenu,
            EXIT, controller::exit,
            SHOW_CURRENT_MENU, controller::showCurrentMenu
    );


    @Override
    public Map<CommandClass, Function<String[], Response>> getFunctionsMap() {
        return commandsFunctionMap;
    }
}
