package utils;

import lombok.Getter;
import lombok.Setter;
import model.Game;
import model.Menus;
import model.Tile;
import model.User;
import view.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Getter
@Setter
public class App {
    private static App instance;

    private App() {

    }

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    private final List<User> users = new ArrayList<>();
    private final List<Game> games = new ArrayList<>();
    private Game currentGame;
    private Menus currentMenu = Menus.Login;

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) return user;
        }
        return null;
    }
}