package io.github.some_example_name.controller.mainMenu;

import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.repository.UserRepo;
import io.github.some_example_name.service.AccountService;
import io.github.some_example_name.utils.GeneratePassword;
import io.github.some_example_name.utils.PasswordHasher;
import io.github.some_example_name.view.mainMenu.Profile;

public class ProfileController {
    private final Profile view;
    // EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
    private final AccountService service = new AccountService(new UserRepo(), new PasswordHasher());


    public ProfileController(Profile view) {
        this.view = view;
    }

    public boolean changeUsername(String username) {
        if (username.equals("")) {
            view.alert("You should fill all fields", 5);
            return false;
        }
        Response response = service.changeUsername(username);
        if (response.shouldBeBack()) return true;
        view.alert(response.message(), 5);
        return false;
    }

    public boolean changeNickname(String nickname) {
        if (nickname.equals("")) {
            view.alert("You should fill all fields", 5);
            return false;
        }
        Response response = service.changeNickName(nickname);
        if (response.shouldBeBack()) return true;
        view.alert(response.message(), 5);
        return false;
    }

    public boolean changePassword(String password, String oldPassword) {
        if (password.equals("")) {
            view.alert("You should fill all fields", 5);
            return false;
        }
        Response response = service.changePassword(password, oldPassword);
        if (response.shouldBeBack()) return true;
        view.alert(response.message(), 5);
        return false;
    }

    public boolean changeEmail(String email) {
        if (email.equals("")) {
            view.alert("You should fill all fields", 5);
            return false;
        }
        Response response = service.changeEmail(email);
        if (response.shouldBeBack()) return true;
        view.alert(response.message(), 5);
        return false;
    }

    public String getRandomPassword() {
        return GeneratePassword.generatePassword();
    }
}
