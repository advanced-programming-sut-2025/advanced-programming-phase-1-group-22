package io.github.some_example_name.client.controller.mainMenu;

import io.github.some_example_name.common.model.dto.UserDto;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.server.repository.UserRepo;
import io.github.some_example_name.server.service.AccountService;
import io.github.some_example_name.common.utils.GeneratePassword;
import io.github.some_example_name.common.utils.PasswordHasher;
import io.github.some_example_name.client.view.mainMenu.RegisterMenu;
import lombok.Setter;

import java.util.Objects;

@Setter
public class RegisterMenuController {
    private final RegisterMenu view;
    // EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
    private final AccountService service = new AccountService(new UserRepo(), new PasswordHasher());

    public RegisterMenuController(RegisterMenu view) {
        this.view = view;
    }

    public String getRandomPassword() {
        return GeneratePassword.generatePassword();
    }

    public boolean handleRegisterBeforeSecurityQuestion() {
        if (notFilled(view.getUsername().getText(), view.getPassword().getText(), view.getConfirmPassword().getText(),
            view.getNickName().getText(), view.getEmail().getText(), view.getGender())) {
            view.alert("You have to fill all fields", 5);
            return false;
        }
        UserDto userDto = new UserDto(view.getUsername().getText(), view.getPassword().getText(), view.getConfirmPassword().getText(),
            view.getNickName().getText(), view.getEmail().getText(), view.getGender());
        Response response = service.controlBeforeQuestion(userDto);
        if (response.shouldBeBack()) return true;
        view.alert(response.message(), 5);
        return false;
    }

    public boolean register() {
        if (view.getSecurityQuestionValue() == null || notFilled(view.getAnswer().getText(), view.getConfirmAnswer().getText())) {
            view.alert("You have to fill all fields", 5);
            return false;
        }
        UserDto userDto = new UserDto(view.getUsername().getText(), view.getPassword().getText(), view.getConfirmPassword().getText(),
            view.getNickName().getText(), view.getEmail().getText(), view.getGender());
        Response response = service.register(userDto, view.getSecurityQuestionValue(), view.getAnswer().getText(), view.getConfirmAnswer().getText());
        if (response.shouldBeBack()) return true;
        view.alert(response.message(), 5);
        return false;
    }

    private boolean notFilled(String... strings) {
        for (String string : strings) {
            if (Objects.equals(string, "")) return true;
        }
        return false;
    }
}
