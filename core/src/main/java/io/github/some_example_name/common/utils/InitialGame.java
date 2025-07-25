package io.github.some_example_name.common.utils;

import io.github.some_example_name.common.model.Game;
import io.github.some_example_name.common.model.User;
import io.github.some_example_name.common.model.Village;
import io.github.some_example_name.common.model.relations.Player;

import java.util.ArrayList;

public class InitialGame {
    public void initial() {
        Game game = new Game();
//        try {
//            game = GameSaver.loadGame("game.json");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        App app = App.getInstance();
        app.setCurrentGame(game);
        game.start();
        Village village = new Village();
        village.initAfterLoad();
        game.setVillage(village);
    }
}
