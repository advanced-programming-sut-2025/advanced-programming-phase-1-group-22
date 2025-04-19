package utils;

import model.*;

public class InitialGame {
    public void initial() {
        Game game = new Game();
        App app = App.getInstance();
        app.setCurrentGame(game);
        game.start();
        Village village = new Village();
        game.setVillage(village);
        for (NPCType npc : NPCType.values()) {
            npc.setMissions();
        }
        for (int i = 0; i < 4; i++) {
            village.getFarms().add(new Farm(new Player(), FarmType.values()[i]));
        }
        village.fillFarms();
    }
}
