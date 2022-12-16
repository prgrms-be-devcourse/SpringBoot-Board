package com.programmers.jpaboard.web.post.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequestDto {

	@NotBlank
	@Size(max = 100)
	private String title;

	@NotNull
	private String content;

	@NotNull
	@Positive
	private Long userId;
}
