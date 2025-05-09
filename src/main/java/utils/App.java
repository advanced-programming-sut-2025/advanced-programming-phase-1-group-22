package utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import model.Game;
import model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class App implements Serializable {
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
}