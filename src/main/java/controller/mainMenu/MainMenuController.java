package controller.mainMenu;

import controller.MenuController;
import model.User;
import model.exception.InvalidInputException;
import model.records.Response;
import service.AccountService;
import service.GameInitService;
import utils.App;
import utils.InitialGame;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenuController extends MenuController {
    private final AccountService accountService = AccountService.getInstance();
    private final GameInitService gameInitService = GameInitService.getInstance();

    public Response logout(String[] params) {
        return accountService.logout();
    }

    public Response switchMenu(String[] params) {
        String menu = params[0];
        return accountService.switchMenu(menu);
    }

    public Response showCurrentMenu(String[] params) {
        return accountService.showCurrentMenu();
    }

    public Response newGame(String[] params) {
        return gameInitService.newGame(params[1]);
    }

    public Response loadGame(String[] params) {
        return gameInitService.loadGame();
    }

    public Response gameMap(String[] params) {
        return gameInitService.gameMap(params[0]);
    }
}
