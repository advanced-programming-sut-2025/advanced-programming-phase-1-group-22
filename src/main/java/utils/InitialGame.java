package utils;

import model.*;
import model.relations.NPC;
import model.relations.NPCType;
import model.relations.Player;
import variables.Session;
import view.Menu;

import java.util.ArrayList;

public class InitialGame {
    public void initial(ArrayList<User> users) {
        Game game = new Game();
        App app = App.getInstance();
        app.setCurrentGame(game);
        game.start();
        Village village = new Village();
        game.setVillage(village);
        for (NPCType npc : NPCType.values()) {
            npc.setMissions();
            game.addNPC(new NPC(npc));
        }
        for (int i = 0; i < users.size(); i++) {
            Player player = new Player(users.get(i));
            game.addPlayer(player);

        }
        game.setCurrentPlayer(game.getPlayers().getFirst());
        Session.setCurrentMenu(Menu.MAP_SELECTION);
    }
}
