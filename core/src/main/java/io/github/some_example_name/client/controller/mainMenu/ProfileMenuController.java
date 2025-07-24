package io.github.some_example_name.client.controller.mainMenu;

import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.server.repository.UserRepo;
import io.github.some_example_name.server.service.AccountService;
import io.github.some_example_name.common.utils.GeneratePassword;
import io.github.some_example_name.common.utils.PasswordHasher;
import io.github.some_example_name.client.view.mainMenu.ProfileMenu;

public class ProfileMenuController {
    private final ProfileMenu view;
    // EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
    private final AccountService service = new AccountService(new UserRepo(), new PasswordHasher());


    public ProfileMenuController(ProfileMenu view) {
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
