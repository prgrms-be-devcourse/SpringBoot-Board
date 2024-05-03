package com.programmers.board.service.response;

import com.programmers.board.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {
    private final Long userId;
    private final String name;
    private final int age;
    private final String hobby;

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getAge(), user.getHobby());
    }
}
