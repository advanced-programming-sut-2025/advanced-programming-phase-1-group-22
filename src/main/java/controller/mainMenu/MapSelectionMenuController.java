package controller.mainMenu;

import model.*;
import model.exception.InvalidInputException;
import utils.App;

import java.util.Random;

public class MapSelectionMenuController {
    App app = App.getInstance();
    public void gameMap(String number) {
        int mapNumber = Integer.parseInt(number) - 1;
        if (mapNumber < 0 || mapNumber > 4) throw new InvalidInputException("""
                Farm not found:\s
                 1 : Grassy Farm\s
                 2 : Flower Farm\s
                 3 : Blue Farm\s
                 4 : Rocky Farm\s
                 5 : Desert Farm""");
        Player player = app.getCurrentGame().getCurrentPlayer();
        player.setFarmType(FarmType.values()[mapNumber]);
        app.getCurrentGame().nextPlayer();
        if (app.getCurrentGame().getCurrentPlayer().getFarmType() != null) {
            app.setCurrentMenu(Menus.GameMainMenu);
            completeMap();
        }
    }


    private void completeMap() {
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
}
