package io.github.some_example_name.controller.mainMenu;

import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.repository.UserRepo;
import io.github.some_example_name.service.AccountService;
import io.github.some_example_name.utils.GeneratePassword;
import io.github.some_example_name.utils.PasswordHasher;
import io.github.some_example_name.view.mainMenu.LoginMenu;

import java.util.Objects;

public class LoginMenuController {
    private final LoginMenu view;
    // EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
    private final AccountService service = new AccountService(new UserRepo(), new PasswordHasher());

    public LoginMenuController(LoginMenu view) {
        this.view = view;
    }

    public boolean login() {
        if (notFilled(view.getUsername().getText(), view.getPassword().getText())) {
            view.alert("You should fill all fields", 5);
            return false;
        }
        Response response = service.loginUser(view.getUsername().getText(), view.getPassword().getText());
        if (response.shouldBeBack()) return true;
        view.alert(response.message(), 5);
        return false;
    }

    public boolean forgetPassword() {
        if (notFilled(view.getAnswer().getText(), view.getUsernameForDialog().getText(), view.getNewPassword().getText())) {
            view.alert("You should fill all fields", 5);
            return false;
        }
        Response response = service.forgetPassword(view.getUsernameForDialog().getText(), view.getAnswer().getText(), view.getNewPassword().getText());
        if (response.shouldBeBack()){
            view.alert(response.message(),5);
            return true;
        }
        view.alert(response.message(), 5);
        return false;
    }

    public String getRandomPassword() {
        return GeneratePassword.generatePassword();
    }

    private boolean notFilled(String... strings) {
        for (String string : strings) {
            if (Objects.equals(string, "")) return true;
        }
        return false;
    }
}
