package model;

import lombok.Getter;
import lombok.ToString;
import view.Menu;
import view.gameMenu.*;
import view.mainMenu.LoginView;
import view.mainMenu.MainMenu;
import view.mainMenu.MapSelectionMenu;
import view.mainMenu.ProfileMenu;

@Getter
@ToString
public enum Menus {
    Login(new LoginView()), MainMenu(new MainMenu()), MapSelection(new MapSelectionMenu()), Profile(new ProfileMenu()),
    Cottage(new CottageMenu()), Farming(new FarmingMenu()), GameMainMenu(new GameMainMenu()), Store(new StoreMenu()),
    Trade(new TradeMenu()), Exit(null);

    Menus(Menu menu) {
        this.menu = menu;
    }
    private final Menu menu;
}
