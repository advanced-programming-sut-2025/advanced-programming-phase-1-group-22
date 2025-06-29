package io.github.some_example_name.view;

import io.github.some_example_name.view.gameMenu.*;
import io.github.some_example_name.view.mainMenu.*;

import java.util.Scanner;

public enum Menu {
    LOGIN(LoginMenu.getInstance()),
    PROFILE(ProfileMenu.getInstance()),
    EXIT(ExitMenu.getInstance()),
    MAIN(MainMenu.getInstance()),
    MAP_SELECTION(MapSelectionMenu.getInstance()),
    COTTAGE(CottageMenu.getInstance()),
    GAME_MAIN_MENU(GameMainMenu.getInstance()),
    STORE_MENU(StoreMenu.getInstance()),
    TRADE_MENU(TradeMenu.getInstance()),
    RELATION_MENU(RelationShipMenu.getInstance());

    private final CommandProcessor commandProcessor;

    Menu(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    public void checkCommand(Scanner scanner) {
        this.commandProcessor.processCommand(scanner.nextLine().trim());
    }

}
