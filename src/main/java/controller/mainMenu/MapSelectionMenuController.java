package controller.mainMenu;

import model.FarmType;
import model.Menus;
import model.Player;
import model.exception.InvalidInputException;
import utils.App;

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
        if (app.getCurrentGame().getCurrentPlayer().getFarmType() != null) app.setCurrentMenu(Menus.GameMainMenu);
    }
}
