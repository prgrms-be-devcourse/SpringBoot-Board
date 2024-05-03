package com.programmers.board.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateRequest {
    private static final String TITLE_VALIDATE = "게시글 제목은 1자 이상, 100자 이하여야 합니다";
    private static final String CONTENT_VALIDATE = "게시글 본문은 1자 이상이어야 합니다";
    private static final String TITLE_MANDATORY = "게시글 제목은 필수입니다";
    private static final String CONTENT_MANDATORY = "게시글 본문은 필수입니다";

    @NotBlank(message = TITLE_MANDATORY)
    @Size(min = 1, max = 100, message = TITLE_VALIDATE)
    private final String title;

    @NotBlank(message = CONTENT_MANDATORY)
    @Size(min = 1, message = CONTENT_VALIDATE)
    private final String content;
}
