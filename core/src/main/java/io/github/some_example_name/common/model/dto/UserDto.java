package io.github.some_example_name.common.model.dto;

import lombok.Builder;

@Builder
public record UserDto(String username, String password, String passwordConfirmation, String nickName, String email,
                      String gender) {
}
