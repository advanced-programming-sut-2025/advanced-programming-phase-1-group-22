package service;

import model.dto.QuestionDto;
import model.dto.UserDto;
import model.records.Response;

public class AccountService {
    private static AccountService instance;
    private AccountService() {}
    public static AccountService getInstance() {
        if (instance == null) {
            instance = new AccountService();
        }
        return instance;
    }
    public Response loginUser(String email, String password) {

        return null;
    }

    public Response registerUser(UserDto dto) {
        return null;
    }

    public Response changeUsername(String username) {
        return null;
    }

    public Response changePassword(String newPassword, String oldPassword) {
        return null;
    }

    public Response changeEmail(String email) {
        return null;
    }

    public Response changeNickName(String nickName) {
        return null;
    }

    public Response getUserInfo() {
        return null;
    }

    public Response pickQuestion(QuestionDto questionDto) {
        return null;
    }

    public Response getPasswordByUsername(String username) {
        return null;
    }

    public Response confirmQuestion(String answer) {
        return null;
    }

    public Response switchMenu(String menu) {
        return null;
    }

    public Response exit() {
        return null;
    }

    public Response showCurrentMenu() {
        return null;
    }

    public Response logout() {
        return null;
    }
}
