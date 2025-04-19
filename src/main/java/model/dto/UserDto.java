package model.dto;

public record UserDto(String username, String password, String passwordConfirmation, String nickName, String email,
                      String gender) {
}
