package variables;

import lombok.Getter;
import model.User;
import view.Menu;
import view.mainMenu.LoginMenu;

public class Session {
    @Getter
    private static User currentUser;
    @Getter
    private static Menu currentMenu = Menu.LOGIN;

    public Session() {
    }

    public static void setCurrentUser(User currentUser) {
        Session.currentUser = currentUser;
    }

    public static void setCurrentMenu(Menu currentMenu) {
        Session.currentMenu = currentMenu;
    }
}
