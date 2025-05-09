package utils;

import model.Game;
import model.User;

import java.util.ArrayList;
import java.util.List;

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