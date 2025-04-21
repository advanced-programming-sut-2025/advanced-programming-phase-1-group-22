package utils;

import model.*;
import model.structure.Structure;

import java.util.ArrayList;
import java.util.Random;

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
            game.addPlayer(new Player(i, users.get(i)));
        }
        App.getInstance().setCurrentMenu(Menus.MapSelection);
    }
}
