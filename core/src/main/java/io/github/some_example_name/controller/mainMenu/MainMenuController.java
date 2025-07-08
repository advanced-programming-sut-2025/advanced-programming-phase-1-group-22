package io.github.some_example_name.controller.mainMenu;

import io.github.some_example_name.repository.UserRepo;
import io.github.some_example_name.service.AccountService;
import io.github.some_example_name.utils.PasswordHasher;
import io.github.some_example_name.view.mainMenu.MainMenu;

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
