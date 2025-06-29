package io.github.some_example_name.variables;

import io.github.some_example_name.model.User;
import io.github.some_example_name.view.Menu;

public class Session {
    private static User currentUser;
    private static Menu currentMenu = Menu.LOGIN;
    private static boolean stayedLoggedIn = false;

    public Session() {
    }

    public static void setCurrentUser(User currentUser) {
        Session.currentUser = currentUser;
    }

    public static void setCurrentMenu(Menu currentMenu) {
        Session.currentMenu = currentMenu;
    }

    public static void setStayedLoggedIn(boolean stayedLoggedIn) {
        Session.stayedLoggedIn = stayedLoggedIn;
    }

    public static User getCurrentUser() {
        return Session.currentUser;
    }

    public static Menu getCurrentMenu() {
        return Session.currentMenu;
    }

    public static boolean isStayedLoggedIn() {
        return Session.stayedLoggedIn;
    }
}
