package io.github.some_example_name.service;

import io.github.some_example_name.command.UserCommands;
import io.github.some_example_name.model.User;
import io.github.some_example_name.model.dto.UserDto;
import io.github.some_example_name.model.enums.Gender;
import io.github.some_example_name.model.enums.SecurityQuestion;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.repository.UserRepository;
import io.github.some_example_name.utils.GeneratePassword;
import io.github.some_example_name.utils.GenerateQuestion;
import io.github.some_example_name.utils.PasswordHasher;
import io.github.some_example_name.variables.Session;
import io.github.some_example_name.view.Menu;
import io.github.some_example_name.view.ViewRender;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Random;

import static io.github.some_example_name.command.UserCommands.EMAIL;
import static io.github.some_example_name.command.UserCommands.USERNAME;

public class AccountService {
    private static AccountService instance;
    private final ViewRender viewRender;
    private final UserRepository<User> userRepository;
    private final PasswordHasher passwordHasher;

    public AccountService(UserRepository<User> userRepository, ViewRender viewRender, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.viewRender = viewRender;
        this.passwordHasher = passwordHasher;
    }

    public Response controlBeforeQuestion(UserDto dto) {
        Response response;
        String username = dto.username();
        String password = dto.password();
        String email = dto.email();
        String passwordRepeat = dto.passwordConfirmation();
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return new Response("this username already taken. try " + username + "-" + "or something else");
        }
        if (!USERNAME.matches(username)) {
            return new Response("invalid username");
        }
        if (!UserCommands.PASSWORD.matches(password)) {
            return new Response("invalid password");
        }
        if (!UserCommands.STRONG_PASSWORD.matches(password)) {
            return new Response("weak password");
        }
        if (!password.equals(passwordRepeat)) {
            return new Response("confirm password is not correct");
        }
        if (!UserCommands.EMAIL.matches(email)) {
            return new Response("invalid email");
        }
        response = new Response("Ok", true);
        return response;
    }

    public Response register(UserDto dto, SecurityQuestion securityQuestion, String answer, String confirmAnswer) {
        Response response = null;
        String username = dto.username();
        String password = dto.password();
        String email = dto.email();
        String nickName = dto.nickName();
        String gender = dto.gender();
        if (!answer.equals(confirmAnswer)) {
            return new Response("confirm answer is wrong");
        }
        String hashedPassword;
        try {
            hashedPassword = passwordHasher.hashPassword(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        if (hashedPassword != null) {
            password = hashedPassword;
        }

        Optional<User> user = userRepository.save(new User(username, password, email, nickName, Gender.valueOf(gender), securityQuestion, answer));

        if (user.isPresent()) {
            response = new Response("success", true);
            Session.setCurrentUser(user.get());
            Session.getCurrentUser().setId(user.get().getId());
            Session.setCurrentMenu(Menu.MAIN);
        }
        return response;
    }

    public Response loginUser(String username, String password) {
        Response response;
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new Response("User not found");
        }
        try {
            if (!passwordHasher.verifyPassword(password, user.get().getPassword()) && !user.get().getPassword().equals(password)) {
                return new Response("Wrong password");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Session.setCurrentUser(user.get());
        response = new Response("Logged in successfully", true);
        Session.setCurrentMenu(Menu.MAIN);
        return response;
    }


    public Response changeUsername(String username) {
        if (Session.getCurrentUser() == null) {
            return new Response("you are not logged in");
        }
        if (Session.getCurrentUser().getUsername().equals(username)) {
            return new Response("Username is already taken");
        }
        if (!USERNAME.matches(username)) {
            return new Response("invalid username");
        }
        userRepository.changeUsername(Session.getCurrentUser().getUsername(), username);
        Session.getCurrentUser().setUsername(username);
        return new Response("username changed successfully");

    }

    public Response changePassword(String newPassword, String oldPassword) {
        if (Session.getCurrentUser() == null) {
            return new Response("you are not logged in");
        }
        if (!Session.getCurrentUser().getPassword().equals(oldPassword)) {
            return new Response("your current password is not this: " + oldPassword);
        }
        if (!UserCommands.STRONG_PASSWORD.matches(newPassword)) {
            return new Response("invalid new password");
        }
        if (newPassword.equals(oldPassword)) {
            return new Response("your new password is the same");
        }
        userRepository.changePassword(Session.getCurrentUser().getUsername(), newPassword);
        Session.getCurrentUser().setPassword(newPassword);
        return new Response("password changed successfully");
    }

    public Response changeEmail(String email) {
        if (Session.getCurrentUser() == null) {
            return new Response("you are not logged in");
        }
        if (Session.getCurrentUser().getEmail().equals(email)) {
            return new Response("your email is already taken");
        }
        if (!EMAIL.matches(email)) {
            return new Response("invalid email");
        }
        userRepository.changeEmail(Session.getCurrentUser().getUsername(), email);
        Session.getCurrentUser().setEmail(email);
        return new Response("email changed successfully");
    }

    public Response changeNickName(String nickName) {
        if (Session.getCurrentUser() == null) {
            return new Response("you are not logged in");
        }
        if (Session.getCurrentUser().getNickname().equals(nickName)) {
            return new Response("your new nickname is already taken");
        }
        userRepository.changeNickname(Session.getCurrentUser().getUsername(), nickName);
        Session.getCurrentUser().setNickname(nickName);
        return new Response("nickname changed successfully");
    }

    public Response getUserInfo() {
        if (Session.getCurrentUser() == null) {
            return new Response("you are not logged in");
        }
        return new Response(Session.getCurrentUser().toString());
    }

//    public Response pickQuestion(QuestionDto questionDto) {
//        String questionNumber = questionDto.questionNumber();
//        String answer = questionDto.answer();
//        String answerConfirm = questionDto.answerConfirm();
//        if (answerConfirm.equals(answer)) {
//            return new Response("response and repeated response should are not same");
//        }
//        if (GenerateQuestion.values()[Integer.parseInt(questionNumber) - 1].getAnswer().equals(answer)) {
//            String password = GeneratePassword.generatePassword();
//            viewRender.showResponse(new Response("do you like this password %s ? (yes):(no)" + password, true));
//            Response response1 = viewRender.getResponse();
//            if (response1.message().equals("no")) {
//                viewRender.showResponse(new Response("do you want to see another password ? (yes):(no)", true));
//                Response response2 = viewRender.getResponse();
//                while (!response2.message().equals("no")) {
//                    password = GeneratePassword.generatePassword();
//                    viewRender.showResponse(new Response("do you like this password %s? (yes):(no)" + password, true));
//                    response2 = viewRender.getResponse();
//                }
//            }
//
//        }
//        return new Response("answer is not correct");
//    }

    public Response forgetPassword(String username, String answer, String newPassword) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new Response("User not found");
        }
        User currentUser = user.get();
        if (!currentUser.getAnswer().equals(answer)) {
            return new Response("Your Answer is wrong");
        }
        String oldPassword = currentUser.getPassword();
        if (!UserCommands.STRONG_PASSWORD.matches(newPassword)) {
            return new Response("invalid new password");
        }
        if (newPassword.equals(oldPassword)) {
            return new Response("your new password is the same");
        }
        userRepository.changePassword(currentUser.getUsername(), newPassword);
        currentUser.setPassword(newPassword);
        return new Response("password changed successfully", true);
    }

    public Response switchMenu(String menu) {
        if (Session.getCurrentUser() == null) {
            return new Response("you are not logged in");
        }
        if (Session.getCurrentMenu().equals(Menu.PROFILE)) {
            return new Response("switching menu is impossible!");
        }

        Session.setCurrentMenu(Menu.valueOf(menu));
        return new Response("menu changed successfully");
    }

    public Response exit() {
        if (Session.getCurrentMenu().equals(Menu.LOGIN)) {
            Session.setCurrentMenu(Menu.EXIT);
            if (!Session.isStayedLoggedIn()) {
                Session.setCurrentUser(null);
            }
            return new Response("exited successfully");
        }
        return new Response("exit is not possible!");
    }

    public Response showCurrentMenu() {
        return new Response(Session.getCurrentMenu().toString());
    }

    public Response logout() {
        Session.setCurrentUser(null);
        Session.setStayedLoggedIn(false);
        Session.setCurrentMenu(Menu.LOGIN);
        return new Response("logout successfully", true);
    }
}
