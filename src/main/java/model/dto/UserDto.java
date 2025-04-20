package model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public record UserDto(String username, String password, String passwordConfirmation, String nickName, String email,
                      String gender) {
}
