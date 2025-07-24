package io.github.some_example_name.common.variables;

import io.github.some_example_name.common.model.User;

public class Session {
    private static User currentUser;
    private static boolean stayedLoggedIn = false;

    public Session() {
    }

    public static void setCurrentUser(User currentUser) {
        Session.currentUser = currentUser;
    }

    public static void setStayedLoggedIn(boolean stayedLoggedIn) {
        Session.stayedLoggedIn = stayedLoggedIn;
    }

    public static User getCurrentUser() {
        return Session.currentUser;
    }

    public static boolean isStayedLoggedIn() {
        return Session.stayedLoggedIn;
    }
}
