package com.programmers.board.controller;

import com.programmers.board.constant.SessionConst;
import com.programmers.board.constant.AuthErrorMessage;
import com.programmers.board.controller.response.PageResult;
import com.programmers.board.controller.response.Result;
import com.programmers.board.service.response.PostResponse;
import com.programmers.board.controller.request.PostCreateRequest;
import com.programmers.board.controller.request.PostUpdateRequest;
import com.programmers.board.exception.AuthenticationException;
import com.programmers.board.service.PostService;
import com.programmers.board.service.request.post.PostCreateCommand;
import com.programmers.board.service.request.post.PostDeleteCommand;
import com.programmers.board.service.request.post.PostGetCommand;
import com.programmers.board.service.request.post.PostUpdateCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public PageResult<PostResponse> findPosts(Pageable pageable) {
        Page<PostResponse> posts = postService.findPosts(pageable);
        return new PageResult<>(posts);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Result<Long> createPost(@SessionAttribute(name = SessionConst.LOGIN_USER_ID, required = false) Long loginUserId,
                                   @RequestBody @Valid PostCreateRequest request) {
        checkLogin(loginUserId);
        PostCreateCommand command = PostCreateCommand.of(loginUserId, request);
        Long postId = postService.createPost(command);
        return new Result<>(postId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{postId}")
    public Result<PostResponse> findPost(@PathVariable("postId") Long postId) {
        PostGetCommand command = PostGetCommand.of(postId);
        PostResponse post = postService.findPost(command);
        return new Result<>(post);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{postId}")
    public void updatePost(@PathVariable("postId") Long postId,
                           @SessionAttribute(name = SessionConst.LOGIN_USER_ID, required = false) Long loginUserId,
                           @RequestBody @Valid PostUpdateRequest request) {
        checkLogin(loginUserId);
        PostUpdateCommand command = PostUpdateCommand.of(postId, loginUserId, request);
        postService.updatePost(command);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable("postId") Long postId,
                           @SessionAttribute(name = SessionConst.LOGIN_USER_ID, required = false) Long loginUserId) {
        checkLogin(loginUserId);
        PostDeleteCommand command = PostDeleteCommand.of(postId, loginUserId);
        postService.deletePost(command);
    }

    private void checkLogin(Long loginUserId) {
        if (Objects.isNull(loginUserId)) {
            throw new AuthenticationException(AuthErrorMessage.NO_LOGIN.getMessage());
        }
    }
}
