package com.programmers.springboard.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostCreateRequest(@NotBlank (message = "제목을 입력해주세요")
								@Size(max = 60, message = "제목은 60자 미만입니다")
								String title,
								@NotBlank (message = "내용을 입력해주세요")
								String content,
								@NotNull (message = "멤버 아이디를 입력해주세요") Long memberId) {
}
