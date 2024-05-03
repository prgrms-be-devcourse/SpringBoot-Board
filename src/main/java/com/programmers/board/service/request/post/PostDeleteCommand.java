package com.programmers.board.service.request.post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostDeleteCommand {
    private final Long postId;
    private final Long loginUserId;

    public static PostDeleteCommand of(Long postId, Long loginUserId) {
        return new PostDeleteCommand(
                postId,
                loginUserId
        );
    }
}
