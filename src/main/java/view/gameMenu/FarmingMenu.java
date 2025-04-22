package view.gameMenu;

import command.CommandClass;
import model.records.Response;

import java.util.Map;
import java.util.function.Function;

public class FarmingMenu {
    private static FarmingMenu menu;

    private FarmingMenu() {
    }

    public static FarmingMenu getInstance() {
        if (menu == null) {
            menu = new FarmingMenu();
        }
        return menu;
    }
}
