package service;

import command.UserCommands;
import model.User;
import model.dto.UserDto;
import model.enums.Gender;
import model.records.Response;
import repository.UserRepository;
import repository.UserRepositoryImpl;
import utils.GeneratePassword;
import utils.GenerateQuestion;
import variables.Session;
import view.Menu;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Random;

import static command.UserCommands.EMAIL;
import static command.UserCommands.USERNAME;
import static utils.PasswordHasher.hashPassword;
import static utils.PasswordHasher.verifyPassword;
import static view.ViewRender.getResponse;
import static view.ViewRender.showResponse;

public class AccountService {
    private final UserRepository userRepository = new UserRepositoryImpl();
    private static AccountService instance;

    private AccountService() {
    }

    public static AccountService getInstance() {
        if (instance == null) {
            instance = new AccountService();
        }
        return instance;
    }

    public Response loginUser(String username, String password, String stayedLoggedIn) {
        Response response = null;
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return new Response("User not found");
        }
        try {
            if (!user.get().getPassword().equals(password) && !verifyPassword(password, user.get().getPassword())) {
                return new Response("Wrong password");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Session.setCurrentUser(user.get());
        response = new Response("Logged in successfully");
        if (stayedLoggedIn != null && stayedLoggedIn.contains("stay-logged-in")) {
            Session.setStayedLoggedIn(true);
        }
        Session.setCurrentMenu(Menu.MAIN);
        return response;
    }

    public Response registerUser(UserDto dto) {
        Response response = null;
        String username = dto.username();
        String password = dto.password();
        String email = dto.email();
        String nickName = dto.nickName();
        String gender = dto.gender();
        String passwordRepeat = dto.passwordConfirmation();
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            showResponse(new Response("does this username is ok (yes):(no) ? " + username + "-", true));
            Response response1 = getResponse();
            if (response1.message().equals("no")) {
                return Response.empty();
            }

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
            showResponse(new Response("enter password again or be back in register menu", true));
            Response response1 = getResponse();
            if (!UserCommands.PASSWORD.matches(response1.message())) {
                return Response.empty();
            }
        }
        if (!UserCommands.EMAIL.matches(email)) {
            return new Response("invalid email");
        }
        String hashedPassword = null;
        try {
            hashedPassword = hashPassword(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        if (hashedPassword != null) {
            password = hashedPassword;
        }
        Optional<User> user = userRepository.save(new User(username, password, email, nickName, Gender.valueOf(gender)));

        if (user.isPresent()) {
            response = new Response("success");
            Session.setCurrentUser(user.get());
            Session.getCurrentUser().setId(user.get().getId());
            Session.setCurrentMenu(Menu.MAIN);
        }
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
//            showResponse(new Response("do you like this password %s ? (yes):(no)" + password, true));
//            Response response1 = getResponse();
//            if (response1.message().equals("no")) {
//                showResponse(new Response("do you want to see another password ? (yes):(no)", true));
//                Response response2 = getResponse();
//                while (!response2.message().equals("no")) {
//                    password = GeneratePassword.generatePassword();
//                    showResponse(new Response("do you like this password %s? (yes):(no)" + password, true));
//                    response2 = getResponse();
//                }
//            }
//
//        }
//        return new Response("answer is not correct");
//    }

    public Response forgetPassword(String username) {
//        if (Session.getCurrentUser() == null) {
//            return new Response("you are not logged in");
//        }
//        if (!Session.getCurrentUser().getUsername().equals(username)) {
//            return new Response("invalid username");
//        }
        Random random = new Random();
        int randQuestion = random.nextInt(GenerateQuestion.values().length);
        GenerateQuestion question = GenerateQuestion.values()[randQuestion];
        showResponse(new Response(question.getQuestion()));
        Response response = getResponse();
        if (!response.message().equals(question.getAnswer())) {
            return new Response("answer is not correct");
        }
        showResponse(new Response("Dou you want to enter new password or we generate a password? (1):(2)", true));
        Response response1 = getResponse();
        if (response1.message().equals("1")) {
            showResponse(new Response("enter new password", true));
            response1 = getResponse();
            User user = (User) userRepository.findByUsername(username).get();
            Session.setCurrentUser(user);
            return changePassword(response1.message(), Session.getCurrentUser().getPassword());
        } else if (response1.message().equals("2")) {
            String newPassword = GeneratePassword.generatePassword();
            showResponse(new Response("your password is : " + newPassword, true));
            User user = (User) userRepository.findByUsername(username).get();
            Session.setCurrentUser(user);
            return changePassword(newPassword, Session.getCurrentUser().getPassword());
        }
        return new Response("invalid input!");
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
        Session.setCurrentMenu(Menu.LOGIN);
        return new Response("logout successfully");
    }

    public Response registerUserWithRandomPass(UserDto user) {
        Random random = new Random();
        int randQuestion = random.nextInt(GenerateQuestion.values().length);
        GenerateQuestion question = GenerateQuestion.values()[randQuestion];
        showResponse(new Response(question.getQuestion()));
        Response response = getResponse();
        if (!response.message().equals(question.getAnswer())) {
            return new Response("answer is not correct");
        }
        String password = GeneratePassword.generatePassword();
        showResponse(new Response("Do you want to set {%s} as your password? (yes):(no)".formatted(password), true));
        Response response1 = getResponse();
        if (response1.message().equals("no")) {
            return Response.empty();
        }
        if (response1.message().equals("yes")) {
            UserDto build = UserDto.builder().username(user.username()).password(password).email(user.email())
                    .nickName(user.nickName()).gender(user.gender()).build();
            return registerUser(build);
        }
        return new Response("invalid input!");
    }
}
