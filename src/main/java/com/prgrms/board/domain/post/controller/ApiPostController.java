package com.prgrms.board.domain.post.controller;

import static com.prgrms.board.global.common.SuccessMessage.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.board.domain.post.dto.request.PostsRequest;
import com.prgrms.board.domain.post.entity.Post;
import com.prgrms.board.domain.post.service.PostService;
import com.prgrms.board.global.common.BaseResponse;
import com.prgrms.board.global.common.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class ApiPostController {

	private final PostService postService;

	@GetMapping
	public BaseResponse<PageResponse<Post>> getPosts(@Valid PostsRequest request) {
		PageResponse<Post> data = postService.getPosts(request);
		return BaseResponse.ok(GET_POSTS_SUCCESS, data);
	}

	// @GetMapping("/{postId}")
	// public Post getPost() {
	// 	return null;
	// }
	//
	// @PostMapping
	// public Post createPost() {
	// 	return null;
	// }
	//
	// @PatchMapping("/{postId}")
	// public Post updatePost() {
	// 	return null;
	// }
}
