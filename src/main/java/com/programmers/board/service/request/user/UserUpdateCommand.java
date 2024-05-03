package com.programmers.board.service.request.user;

import com.programmers.board.controller.request.UserUpdateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUpdateCommand {
    private final Long userId;
    private final Long loginUserId;
    private final String name;
    private final Integer age;
    private final String hobby;

    public static UserUpdateCommand of(Long userId, Long loginUserId, UserUpdateRequest request) {
        return new UserUpdateCommand(
                userId,
                loginUserId,
                request.getName(),
                request.getAge(),
                request.getHobby()
        );
    }

    public static UserUpdateCommand of(Long userId, Long loginUserId, String name, Integer age, String hobby) {
        return new UserUpdateCommand(
                userId,
                loginUserId,
                name,
                age,
                hobby
        );
    }
}
