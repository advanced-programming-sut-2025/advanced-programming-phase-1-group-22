package utils;

import model.Game;
import model.User;
import model.Village;
import model.relations.Player;
import save3.GameSaver;
import variables.Session;
import view.Menu;

import java.io.IOException;
import java.util.ArrayList;

public class InitialGame {
    public void initial(ArrayList<User> users) {
        Game game = new Game();
//        Game game = null;
//        try {
//            game = GameSaver.loadGame("game.json");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        App app = App.getInstance();
        app.setCurrentGame(game);
        game.start();
        Village village = new Village();
        game.setVillage(village);
        for (int i = 0; i < users.size(); i++) {
            Player player = new Player(users.get(i));
            game.addPlayer(player);

        }
        game.setCurrentPlayer(game.getPlayers().getFirst());
        Session.setCurrentMenu(Menu.MAP_SELECTION);
    }
}
