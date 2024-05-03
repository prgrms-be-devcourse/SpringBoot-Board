package com.programmers.board.service.request.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDeleteCommand {
    private final Long userId;
    private final Long loginUserId;

    public static UserDeleteCommand of(Long userId, Long loginUserId) {
        return new UserDeleteCommand(userId, loginUserId);
    }
}
