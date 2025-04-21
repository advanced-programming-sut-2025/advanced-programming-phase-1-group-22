package utils;

import model.*;
import model.structure.Structure;
import model.structure.farmInitialElements.Cottage;

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
        }
        for (int i = 0; i < users.size(); i++) {
            game.addPlayer(new Player(i, users.get(i)));
        }
        App.getInstance().setCurrentMenu(Menus.MapSelection);
//        printMap(game);
    }

    public void completeMap() {
        Game game = App.getInstance().getCurrentGame();
        Village village = game.getVillage();
        for (int i = 0; i < 4; i++) {
            Player player = game.getPlayers().size() <= i ? null : game.getPlayers().get(i);
            Farm farm;
            if (player != null) {
                farm = new Farm(player, player.getFarmType());
                Tile tile = farm.getCottage().getTiles().getFirst();
                player.getTiles().add(tile);
            } else {
                Random random = new Random();
                farm = new Farm(null, FarmType.values()[random.nextInt(0,4)]);
            }
            village.getFarms().add(farm);
        }
        village.fillFarms();
    }

    private void printMap(Game game) {
        Character[][] str = new Character[160][120];
        Tile[][] tiles = game.tiles;
        for (int i = 0; i < 160; i++) {
            for (int i1 = 0; i1 < 120; i1++) {
                str[i][i1] = tiles[i][i1].getTileType().StringToCharacter();
            }
        }

        for (Structure structure : game.getVillage().getStructures()) {
            for (Tile tile : structure.getTiles()) {
                str[tile.getX()][tile.getY()] = '2';
            }
        }
        for (Farm farm : game.getVillage().getFarms()) {
            for (Structure structure : farm.getStructures()) {
                for (Tile tile : structure.getTiles()) {
                    str[tile.getX()][tile.getY()] = '1';
                }
            }
        }
        for (int i = 0; i < 120; i++) {
            for (int j1 = 0; j1 < 160; j1++) {
                System.out.print(str[j1][119 - i]);
            }
            System.out.println();
        }
    }
}
