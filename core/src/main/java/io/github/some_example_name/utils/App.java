package io.github.some_example_name.utils;

import io.github.some_example_name.model.Game;
import io.github.some_example_name.model.User;

import java.util.ArrayList;
import java.util.List;

public class App {
    private static App instance;
    public static Integer tileWidth = 160;
    public static Integer tileHeight = 160;

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
    private Game currentGame = new Game();

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public List<Game> getGames() {
        return this.games;
    }

    public Game getCurrentGame() {
        return this.currentGame;
    }
}
