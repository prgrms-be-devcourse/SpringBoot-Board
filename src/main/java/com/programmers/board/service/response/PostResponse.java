package com.programmers.board.service.response;

import com.programmers.board.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostResponse {
    private final Long postId;
    private final String title;
    private final String content;
    private final UserResponse user;

    public static PostResponse from(Post post) {
        UserResponse userDto = UserResponse.from(post.getUser());
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                userDto);
    }
}
