package com.programmers.board.service.request.post;

import com.programmers.board.controller.request.PostCreateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostCreateCommand {
    private final Long loginUserId;
    private final String title;
    private final String content;

    public static PostCreateCommand of(Long loginUserId, PostCreateRequest request) {
        return new PostCreateCommand(
                loginUserId,
                request.getTitle(),
                request.getContent()
        );
    }

    public static PostCreateCommand of(Long loginUserId, String title, String content) {
        return new PostCreateCommand(
                loginUserId,
                title,
                content
        );
    }
}
