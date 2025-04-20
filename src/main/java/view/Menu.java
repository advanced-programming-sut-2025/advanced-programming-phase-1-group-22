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
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Menu {
    protected Matcher isMatched(String input, String pattern) {
        Matcher matcher = Pattern.compile(pattern).matcher(input);

        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }

    public void checkCommand(Scanner scanner) {

    }

}
