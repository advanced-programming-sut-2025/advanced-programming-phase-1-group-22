package io.github.some_example_name.client.controller.mainMenu;

import io.github.some_example_name.server.repository.UserRepo;
import io.github.some_example_name.server.service.AccountService;
import io.github.some_example_name.common.utils.PasswordHasher;
import io.github.some_example_name.client.view.mainMenu.MainMenu;

public class MainMenuController {
    private final MainMenu view;
    // EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
    private final AccountService service = new AccountService(new UserRepo(), new PasswordHasher());

    public MainMenuController(MainMenu view) {
        this.view = view;
    }

    public void logout() {
        service.logout();
    }
}
