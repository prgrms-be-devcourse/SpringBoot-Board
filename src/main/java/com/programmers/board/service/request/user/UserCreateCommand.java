package com.programmers.board.service.request.user;

import com.programmers.board.controller.request.UserCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreateCommand {
    private final String name;
    private final int age;
    private final String hobby;

    public static UserCreateCommand from(UserCreateRequest request) {
        return new UserCreateCommand(
                request.getName(),
                request.getAge(),
                request.getHobby()
        );
    }

    public static UserCreateCommand of(String name, int age, String hobby) {
        return new UserCreateCommand(name, age, hobby);
    }
}
