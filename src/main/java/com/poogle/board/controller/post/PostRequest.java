package com.poogle.board.controller.post;

import com.poogle.board.model.post.Post;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class PostRequest {
    private @NotEmpty String title;
    private @NotEmpty String content;

    protected PostRequest() {
    }

    public Post newPost() {
        return Post.of(title, content);
    }
}
