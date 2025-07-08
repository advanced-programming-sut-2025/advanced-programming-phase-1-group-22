package io.github.some_example_name.controller.mainMenu;

import io.github.some_example_name.repository.UserRepo;
import io.github.some_example_name.service.AccountService;
import io.github.some_example_name.utils.PasswordHasher;
import io.github.some_example_name.view.ViewRender;
import io.github.some_example_name.view.mainMenu.Main;

public class MainController {
    private final Main view;
    // EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
    private final AccountService service = new AccountService(new UserRepo(), new ViewRender(), new PasswordHasher());

    public MainController(Main view) {
        this.view = view;
    }

    public void logout() {
        service.logout();
    }
}
