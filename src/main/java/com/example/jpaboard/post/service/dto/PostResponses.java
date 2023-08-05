package com.example.jpaboard.post.service.dto;

import com.example.jpaboard.post.domain.Post;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public record PostResponses(List<PostResponse> postResponse) {

    public static PostResponses of(Slice<Post> posts){
        return new PostResponses(posts.stream()
                                .map(PostResponse::new)
                                .collect(Collectors.toList()));
    }

}
