package com.programmers.jpaboard.domain.post.util;

import com.programmers.jpaboard.domain.post.dto.PostCreateRequestDto;
import com.programmers.jpaboard.domain.post.dto.PostResponseDto;
import com.programmers.jpaboard.domain.post.entity.Post;
import com.programmers.jpaboard.domain.user.entity.User;

public class PostConverter {

	private PostConverter() {
	}

	public static Post toPost(PostCreateRequestDto postCreateRequestDto, User user) {
		return new Post(postCreateRequestDto.getTitle(), postCreateRequestDto.getContent(), user);
	}

	public static PostResponseDto toPostResponseDto(Post post) {
		return new PostResponseDto(
			post.getId(),
			post.getTitle(),
			post.getContent(),
			post.getUser().getId(),
			post.getCreatedAt(),
			post.getLastModifiedAt()
		);
	}
}
