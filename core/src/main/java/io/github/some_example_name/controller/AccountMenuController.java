package io.github.some_example_name.controller;

import io.github.some_example_name.repository.UserRepo;
import jakarta.persistence.EntityManager;
import io.github.some_example_name.model.dto.UserDto;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.repository.UserRepositoryImpl;
import io.github.some_example_name.service.AccountService;
import io.github.some_example_name.utils.HibernateUtil;
import io.github.some_example_name.utils.PasswordHasher;
import io.github.some_example_name.view.ViewRender;

public class AccountMenuController extends MenuController {
   // EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
    private final AccountService accountService = new AccountService(new UserRepo(),new ViewRender(),new PasswordHasher());

    public Response loginUser(String... params) {
        String username = params[0];
        String password = params[1];
        String stayedLoggedIn = params.length > 2 ? params[2] : null;
        return accountService.loginUser(username, password, stayedLoggedIn);
    }

    public Response registerUser(String... params) {
        String username = params[0];
        String password = params[1];
        String passwordConfirm = params[2];
        String nickName = params[3];
        String email = params[4];
        String gender = params[5];
        UserDto dto = new UserDto(username, password, passwordConfirm, nickName, email, gender);
        return accountService.registerUser(dto);
    }

    public Response registerUserRandomPass(String... params) {
        String username = params[0];
        String nickName = params[1];
        String email = params[2];
        String gender = params[3];
        UserDto user = UserDto.builder().username(username).nickName(nickName).email(email).gender(gender).build();
        return accountService.registerUserWithRandomPass(user);
    }

    public Response changeUserName(String... params) {
        String username = params[0];
        return accountService.changeUsername(username);
    }

    public Response changePassword(String... params) {
        String newPassword = params[0];
        String oldPassword = params[1];
        return accountService.changePassword(newPassword, oldPassword);
    }

    public Response changeEmail(String... params) {
        String email = params[0];
        return accountService.changeEmail(email);
    }

    public Response changeNickName(String... params) {
        String nickName = params[0];
        return accountService.changeNickName(nickName);
    }

    public Response userInfo(String... params) {
        return accountService.getUserInfo();
    }

//    public Response pickQuestion(String... params) {
//        String questionNumber = params[0];
//        String answer = params[1];
//        String answerConfirm = params[2];
//        QuestionDto questionDto = new QuestionDto(questionNumber, answer, answerConfirm);
//        return accountService.pickQuestion(questionDto);
////    }


    public Response exit(String... strings) {
        return accountService.exit();
    }

    public Response showCurrentMenu(String... params) {
        return accountService.showCurrentMenu();
    }

    public Response forgetPassword(String... params) {
        String username = params[0];
        return accountService.forgetPassword(username);
    }
}
