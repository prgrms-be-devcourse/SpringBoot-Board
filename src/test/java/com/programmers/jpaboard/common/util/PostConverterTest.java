package com.programmers.jpaboard.common.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.programmers.jpaboard.domain.post.dto.PostCreateRequestDto;
import com.programmers.jpaboard.domain.post.dto.PostResponseDto;
import com.programmers.jpaboard.domain.post.entity.Post;
import com.programmers.jpaboard.domain.post.util.PostConverter;
import com.programmers.jpaboard.domain.user.entity.User;

class PostConverterTest {

	private User user = new User(1L, "권성준", "google@gmail.com", 26, "취미");
	private Post post = new Post(1L, "제목", "내용입니다", user);

	@Test
	@DisplayName("Post 생성 DTO 를 Post 엔티티로 변환하는 것에 성공한다.")
	void toPost() {
		// given
		PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto("제목", "내용입니다", post.getUser().getId());

		// when
		Post convertPost = PostConverter.toPost(postCreateRequestDto, user);

		// then
		assertThat(convertPost)
			.hasFieldOrPropertyWithValue("title", postCreateRequestDto.getTitle())
			.hasFieldOrPropertyWithValue("content", postCreateRequestDto.getContent());
		assertThat(convertPost.getUser().getId()).isEqualTo(postCreateRequestDto.getUserId());
	}

	@Test
	@DisplayName("Post 엔티티를 Post 응답 DTO 로 변환하는 것에 성공한다.")
	void toPostResponseDto() {
		// given & when
		PostResponseDto postResponseDto = PostConverter.toPostResponseDto(post);

		// then
		assertThat(postResponseDto)
			.hasFieldOrPropertyWithValue("id", post.getId())
			.hasFieldOrPropertyWithValue("title", post.getTitle())
			.hasFieldOrPropertyWithValue("content", post.getContent())
			.hasFieldOrPropertyWithValue("userId", post.getUser().getId())
			.hasFieldOrPropertyWithValue("createdAt", post.getCreatedAt())
			.hasFieldOrPropertyWithValue("lastModifiedAt", post.getLastModifiedAt());
	}
}