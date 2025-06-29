package io.github.some_example_name.controller.mainMenu;

import io.github.some_example_name.controller.MenuController;
import jakarta.persistence.EntityManager;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.repository.UserRepositoryImpl;
import io.github.some_example_name.service.AccountService;
import io.github.some_example_name.service.GameInitService;
import io.github.some_example_name.utils.HibernateUtil;
import io.github.some_example_name.utils.PasswordHasher;
import io.github.some_example_name.view.ViewRender;

public class MainMenuController extends MenuController {
    EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
    private final AccountService accountService = new AccountService(new UserRepositoryImpl(em),new ViewRender(),new PasswordHasher());
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
