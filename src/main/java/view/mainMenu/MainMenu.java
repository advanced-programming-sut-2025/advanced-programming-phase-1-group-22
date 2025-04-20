package view.mainMenu;

import controller.mainMenu.MainMenuController;
import model.exception.InvalidInputException;
import view.Menu;

import java.util.Scanner;
import java.util.regex.Matcher;

public class MainMenu extends Menu {
    private final String newGame = "^\\s*game\\s+new\\s+-u\\s+(?<players>.*)\\s*$";
    private final String loadGame = "^\\s*load\\s+game\\s*$";

    private final MainMenuController controller = new MainMenuController();

    @Override
    public void checkCommand(Scanner scanner) {
        String input = scanner.nextLine();
        Matcher matcher;
        if ((matcher = isMatched(input, newGame)) != null) {
            controller.newGame(matcher.group("players"));
        } else if (isMatched(input, loadGame) != null) {
            controller.loadGame();
        } else {
            throw new InvalidInputException("Command Not Found!");
        }
    }
}
