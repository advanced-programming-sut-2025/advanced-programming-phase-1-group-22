package view.mainMenu;

import controller.mainMenu.MapSelectionMenuController;
import model.exception.InvalidInputException;
import view.Menu;

import java.util.Scanner;
import java.util.regex.Matcher;

public class MapSelectionMenu extends Menu {
    private final String gameMap = "^\\s*game\\s+map\\s+(?<mapNumber>\\d+)\\s*$";

    private final MapSelectionMenuController controller = new MapSelectionMenuController();

    @Override
    public void checkCommand(Scanner scanner) {
        String input = scanner.nextLine();
        Matcher matcher;
        if ((matcher = isMatched(input, gameMap)) != null) {
            controller.gameMap(matcher.group("players"));
        } else {
            throw new InvalidInputException("Please Select Your Maps!");
        }
    }
}
