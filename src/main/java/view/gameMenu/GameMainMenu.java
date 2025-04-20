package view.gameMenu;

import controller.gameMenu.GameMainMenuController;
import model.exception.InvalidInputException;

import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMainMenu extends GameMenu {
    private  final GameMainMenuController controller = new GameMainMenuController();

    @Override
    public void checkCommand(Scanner scanner) {
        String input = scanner.nextLine();
        Matcher matcher;
        if (super.check(input)) {
            return;
        } else {
            throw new InvalidInputException("Command Not Found!");
        }
    }
}
