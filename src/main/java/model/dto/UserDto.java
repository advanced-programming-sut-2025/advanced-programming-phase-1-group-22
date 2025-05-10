package model.dto;

import lombok.Builder;

@Builder
public record UserDto(String username, String password, String passwordConfirmation, String nickName, String email,
                      String gender) {
}