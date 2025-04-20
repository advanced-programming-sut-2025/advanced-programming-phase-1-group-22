package view.gameMenu;

import controller.gameMenu.StoreMenuController;
import controller.gameMenu.TradeMenuController;
import model.exception.InvalidInputException;

import java.util.Scanner;
import java.util.regex.Matcher;

public class TradeMenu extends GameMenu{
    private final String trade = "^\\s*trade\\s+-u\\s+(?<username>.+)\\s+-t\\s+(?<type>\\boffer|request)\\s+-i\\s+" +
            "(?<item>.+)\\s+-a\\s+(?<amount>\\d+)(\\s+-p\\s+(?<price>\\d+))?(\\s+-ti\\s+(?<targetItem>.+)\\s+-ta\\" +
            "s+(?<targetAmount>\\d+))?\\s*$"; //TODO the possibility to offer target item first and price next
    private final String tradeList = "^\\s*trade\\s+list\\s*$";
    private final String tradeResponse = "^\\s*trade\\s+respond\\s+(?<respond>\\baccept|reject)\\s+-i\\s+(?<id>\\d+)\\s*$";
    private final String tradeHistory = "^\\s*trade\\s+history\\s*$";
    private final TradeMenuController controller = new TradeMenuController();

    @Override
    public void checkCommand(Scanner scanner) {
        String input = scanner.nextLine();

        Matcher matcher;

        if (super.check(input)) {
            return;
        } else if (isMatched(input, tradeList) != null) {
            controller.tradeList();
        } else if (isMatched(input, tradeHistory) != null) {
            controller.tradeHistory();
        } else if ((matcher = isMatched(input, tradeResponse)) != null) {
            controller.tradeResponse(matcher.group("respond"), matcher.group("id"));
        } else if ((matcher = isMatched(input, trade)) != null) {
            controller.trade(matcher);
        } else {
            throw new InvalidInputException("Invalid Command");
        }
    }
}
