package com.programmers.board.controller.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequest {
    private static final String NAME_PATTERN = "^(?=.*[A-Za-z])[A-Za-z\\d]{1,30}$";
    private static final String HOBBY_PATTERN = "^[A-Za-z가-힣]{1,50}$";
    private static final String NAME_VALIDATE = "사용자 이름은 1자 이상, 30자 이하의 영문자 또는 영문자와 숫자로 구성되어야 합니다";
    private static final String AGE_VALIDATE = "사용자 나이는 0 이상이어야 합니다";
    private static final String HOBBY_VALIDATE = "사용자 취미는 1자 이상, 50자 이하의 영문자여야 합니다";

    @Pattern(regexp = NAME_PATTERN, message = NAME_VALIDATE)
    private final String name;

    @PositiveOrZero(message = AGE_VALIDATE)
    private final int age;

    @Pattern(regexp = HOBBY_PATTERN, message = HOBBY_VALIDATE)
    private final String hobby;
}
