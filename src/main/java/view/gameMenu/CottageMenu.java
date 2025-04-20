package view.gameMenu;

import controller.gameMenu.CottageMenuController;
import controller.gameMenu.StoreMenuController;
import model.exception.InvalidInputException;

import java.util.Scanner;
import java.util.regex.Matcher;

public class CottageMenu extends GameMenu {
    private final String craftingShowRecipes = "^\\s*crafting\\s+show\\s+recipes\\s*$";
    private final String craftingCraft = "^\\s*crafting\\s+craft\\s+(?<itemName>.+)\\s*$";
    private final String cookingRefrigeratorPut = "^\\s*cooking\\s+refrigerator\\s+put\\s+(?<itemName>.+)\\s*$";
    private final String cookingRefrigeratorPick = "^\\s*cooking\\s+refrigerator\\s+pick\\s+(?<itemName>.+)\\s*$";
    private final String cookingPrepare = "^\\s*cooking\\s+prepare\\s+(?<recipeName>.+)\\s*$";
    private final String cookingShowRecipes = "^\\s*cooking\\s+show\\s+recipes\\s*$";
    private final CottageMenuController controller = new CottageMenuController();

    @Override
    public void checkCommand(Scanner scanner) {
        String input = scanner.nextLine();

        Matcher matcher;

        if (super.check(input)) {
            return;
        } else if (isMatched(input, craftingShowRecipes) != null) {
            controller.craftingShowRecipes();
        } else if (isMatched(input, cookingShowRecipes) != null) {
            controller.cookingShowRecipes();
        } else if ((matcher = isMatched(input, craftingCraft)) != null) {
            controller.craftingCraft(matcher.group("itemName"));
        } else if ((matcher = isMatched(input, cookingRefrigeratorPick)) != null) {
            controller.cookingRefrigeratorPick(matcher.group("itemName"));
        } else if ((matcher = isMatched(input, cookingRefrigeratorPut)) != null) {
            controller.cookingRefrigeratorPut(matcher.group("itemName"));
        } else if ((matcher = isMatched(input, cookingPrepare)) != null) {
            controller.cookingPrepare(matcher.group("recipeName"));
        } else {
            throw new InvalidInputException("Invalid Command");
        }
    }
}
