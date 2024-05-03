package com.programmers.board.service.request.post;

import com.programmers.board.controller.request.PostUpdateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostUpdateCommand {
    private final Long postId;
    private final Long loginUserId;
    private final String title;
    private final String content;

    public static PostUpdateCommand of(Long postId, Long loginUserId, PostUpdateRequest request) {
        return new PostUpdateCommand(
                postId,
                loginUserId,
                request.getTitle(),
                request.getContent()
        );
    }

    public static PostUpdateCommand of(Long postId, Long loginUserId, String title, String content) {
        return new PostUpdateCommand(
                postId,
                loginUserId,
                title,
                content
        );
    }
}
