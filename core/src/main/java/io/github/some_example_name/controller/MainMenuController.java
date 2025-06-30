package io.github.some_example_name.controller;

import io.github.some_example_name.repository.UserRepo;
import jakarta.persistence.EntityManager;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.repository.UserRepositoryImpl;
import io.github.some_example_name.service.AccountService;
import io.github.some_example_name.utils.HibernateUtil;
import io.github.some_example_name.utils.PasswordHasher;
import io.github.some_example_name.view.ViewRender;

public class MainMenuController extends MenuController {
    //EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
    private final AccountService accountService = new AccountService(new UserRepo(),new ViewRender(), new PasswordHasher());

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
