package io.github.some_example_name.variables;

import io.github.some_example_name.model.User;
import io.github.some_example_name.model.enums.Gender;
import io.github.some_example_name.model.enums.SecurityQuestion;
import io.github.some_example_name.view.Menu;

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
