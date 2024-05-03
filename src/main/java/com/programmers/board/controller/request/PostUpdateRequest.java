package com.programmers.board.controller.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUpdateRequest {
    private static final String TITLE_VALIDATE = "게시글 제목은 1자 이상, 100자 이하여야 합니다";
    private static final String CONTENT_VALIDATE = "게시글 본문은 1자 이상이어야 합니다";

    @Size(min = 1, max = 100, message = TITLE_VALIDATE)
    private final String title;

    @Size(min = 1, message = CONTENT_VALIDATE)
    private final String content;
}
