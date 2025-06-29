package io.github.some_example_name.utils;

import io.github.some_example_name.model.Game;
import io.github.some_example_name.model.User;
import io.github.some_example_name.model.Village;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.variables.Session;
import io.github.some_example_name.view.Menu;

import java.util.ArrayList;

public class InitialGame {
    public void initial(ArrayList<User> users) {
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
        for (int i = 0; i < users.size(); i++) {
            Player player = new Player(users.get(i));
            game.addPlayer(player);

        }
        game.setCurrentPlayer(game.getPlayers().get(0));
        Session.setCurrentMenu(Menu.MAP_SELECTION);
    }
}
