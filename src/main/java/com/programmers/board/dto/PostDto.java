package com.programmers.board.dto;

import com.programmers.board.domain.Post;
import lombok.Getter;

@Getter
public class PostDto {
    private Long postId;
    private String title;
    private String content;
    private UserDto user;

    PostDto(Long postId, String title, String content, UserDto user) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static PostDto from(Post post) {
        UserDto userDto = UserDto.from(post.getUser());
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                userDto);
    }
}
