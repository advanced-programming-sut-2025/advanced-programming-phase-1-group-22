package utils;

import lombok.Getter;
import lombok.Setter;
import model.Game;
import model.User;

import java.util.ArrayList;
import java.util.List;

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

}