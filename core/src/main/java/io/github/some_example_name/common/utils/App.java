package io.github.some_example_name.common.utils;

import io.github.some_example_name.common.model.Game;
import io.github.some_example_name.common.model.Lobby;
import io.github.some_example_name.common.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class App {
    private static App instance;
    public static Integer tileWidth = 80;
    public static Integer tileHeight = 80;
    public static Integer PORT = 0;
    private final List<User> users = new ArrayList<>();
    private final List<Game> games = new ArrayList<>();
    private Game currentGame = new Game();
    private final List<Lobby> lobbies = new ArrayList<>();

    private App() {
    }

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }
}
