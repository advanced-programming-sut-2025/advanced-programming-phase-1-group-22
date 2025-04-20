package view.gameMenu;

import controller.gameMenu.StoreMenuController;

import java.util.Scanner;
import java.util.regex.Matcher;

import controller.gameMenu.StoreMenuController;
import model.exception.InvalidInputException;

public class StoreMenu  extends  GameMenu{
    private final String build = "^\\s*build\\s+-a\\s+(?<buildingName>.+)\\s+-l\\s+(?<X>\\d+)\\s*" +
            ",\\s*(?<Y>\\d+)\\s*$";
    private final String buyAnimal = "^\\s*buy\\s+animal\\s+-a\\s+(?<animal>.+)\\s+-n\\s+(?<name>.+)\\s*$";
    private final String showAllProducts = "^\\s*show\\s+all\\s+products\\s*$";
    private final String purchase = "^\\s*purchase\\s+(?<name>.+)\\s+-n\\s+(?<count>\\d+)\\s*$";
    private final String purchase1 = "^\\s*purchase\\s+(?<name>.+)\\s*$";

    private final StoreMenuController controller = new StoreMenuController();

    @Override
    public void checkCommand(Scanner scanner) {
        String input = scanner.nextLine();

        Matcher matcher;

        if (super.check(input)) {
            return;
        } else if (isMatched(input, showAllProducts) != null) {
            controller.showAllProducts();
        } else if ((matcher = isMatched(input, purchase)) != null) {
            controller.purchase(matcher.group("name"), matcher.group("count"));
        } else if ((matcher = isMatched(input, purchase1)) != null) {
            controller.purchase(matcher.group("name"), "1");
        } else if ((matcher = isMatched(input, build)) != null) {
            controller.build(matcher.group("buildingName"), matcher.group("X"), matcher.group("Y"));
        } else if ((matcher = isMatched(input, buyAnimal)) != null) {
            controller.buy(matcher.group("animal"), matcher.group("name"));
        } else {
            throw new InvalidInputException("Invalid Command");
        }
    }
}
