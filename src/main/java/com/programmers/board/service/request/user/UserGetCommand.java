package com.programmers.board.service.request.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserGetCommand {
    private final Long userId;

    public static UserGetCommand of(Long userId) {
        return new UserGetCommand(userId);
    }
}
