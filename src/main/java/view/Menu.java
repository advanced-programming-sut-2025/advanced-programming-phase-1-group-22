package view;

import view.mainMenu.ExitMenu;
import view.mainMenu.LoginMenu;
import view.mainMenu.MainMenu;
import view.mainMenu.ProfileMenu;

import java.util.Scanner;

public enum Menu {
    LOGIN(LoginMenu.getInstance()),
    PROFILE(ProfileMenu.getInstance()),
    EXIT(ExitMenu.getInstance()),
    MAIN(MainMenu.getInstance());
    private final CommandProcessor commandProcessor;

    Menu(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    public void checkCommand(Scanner scanner) {
        this.commandProcessor.processCommand(scanner.nextLine().trim());
    }
}
