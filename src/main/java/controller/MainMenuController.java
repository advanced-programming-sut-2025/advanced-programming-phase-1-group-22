package controller;

import model.records.Response;
import service.AccountService;

public class MainMenuController extends MenuController {
    private final AccountService accountService = AccountService.getInstance();

    public Response logout(String... params) {
        return accountService.logout();
    }

    public Response switchMenu(String... params) {
        String menu = params[0];
        return accountService.switchMenu(menu);
    }

    public Response showCurrentMenu(String... params) {
        return accountService.showCurrentMenu();
    }
}
