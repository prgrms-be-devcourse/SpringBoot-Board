package com.programmers.board.controller.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginRequest {
    private static final String NAME_PATTERN = "^(?=.*[A-Za-z])[A-Za-z\\d]{1,30}$";
    private static final String NAME_VALIDATE = "사용자 이름은 1자 이상, 30자 이하의 영문자 또는 영문자와 숫자로 구성되어야 합니다";
    private static final String NAME_NOT_BLANK = "사용자 이름은 필수입니다";

    @NotBlank(message = NAME_NOT_BLANK)
    @Pattern(regexp = NAME_PATTERN, message = NAME_VALIDATE)
    private final String name;

    @JsonCreator
    public LoginRequest(String name) {
        this.name = name;
    }
}
