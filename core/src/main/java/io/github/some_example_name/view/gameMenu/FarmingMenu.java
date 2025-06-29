package io.github.some_example_name.view.gameMenu;

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
