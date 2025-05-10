package controller;

import jakarta.persistence.EntityManager;
import model.records.Response;
import repository.UserRepositoryImpl;
import service.AccountService;
import utils.HibernateUtil;
import utils.PasswordHasher;
import view.ViewRender;

public class MainMenuController extends MenuController {
    EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
    private final AccountService accountService = new AccountService(new UserRepositoryImpl(em),new ViewRender(), new PasswordHasher());

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
