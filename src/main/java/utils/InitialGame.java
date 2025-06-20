package utils;

import model.Game;
import model.User;
import model.Village;
import model.relations.Player;
import variables.Session;
import view.Menu;

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
        game.setCurrentPlayer(game.getPlayers().getFirst());
        Session.setCurrentMenu(Menu.MAP_SELECTION);
    }
}
