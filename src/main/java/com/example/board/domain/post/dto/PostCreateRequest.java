package com.example.board.domain.post.dto;

import com.example.board.domain.member.entity.Member;
import com.example.board.domain.post.entity.Post;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record PostCreateRequest(
        @Length(min = 2, max = 50, message = "게시글 제목은 최소 2자, 최대 50자 입니다.")
        @NotBlank(message = "게시글 제목은 필수 입력 항목입니다.")
        String title,

        @Length(min = 2, max = 200, message = "게시글 내용은 최소 2자, 최대 200자 입니다.")
        @NotBlank(message = "게시글 내용은 필수 입력 항목입니다.")
        String content
) {

    public Post toEntity(Member member) {
        return Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }
}
