package view.mainMenu;

import command.CommandClass;
import controller.AccountMenuController;
import model.records.Response;
import view.CommandProcessor;
import view.Menu;

import java.util.Map;
import java.util.function.Function;

import static command.AccountCommands.*;

public class ProfileMenu  implements CommandProcessor {
    private static ProfileMenu instance;

    private ProfileMenu() {
    }

    public static ProfileMenu getInstance() {
        if (instance == null) {
            instance = new ProfileMenu();
        }
        return instance;
    }

    private final AccountMenuController controller = new AccountMenuController();

    private final Map<CommandClass, Function<String[], Response>> commandsFunctionMap = Map.of(
            CHANGE_USERNAME, controller::changeUserName,
            CHANGE_PASSWORD, controller::changePassword,
            CHANGE_NICKNAME, controller::changeNickName,
            CHANGE_EMAIL, controller::changeEmail,
            USER_INFO,controller::userInfo,
            SHOW_CURRENT_MENU, controller::showCurrentMenu

    );


    @Override
    public Map<CommandClass, Function<String[], Response>> getFunctionsMap() {
        return commandsFunctionMap;
    }
}
