package com.kdt.programmers.forum;

import com.kdt.programmers.forum.exception.NotFoundException;
import com.kdt.programmers.forum.exception.PostNotFoundException;
import com.kdt.programmers.forum.transfer.PageDto;
import com.kdt.programmers.forum.transfer.request.PostRequest;
import com.kdt.programmers.forum.transfer.response.ApiResponse;
import com.kdt.programmers.forum.transfer.PostDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("")
    public ApiResponse<PageDto> getPosts(Pageable pageable) {
        Page<PostDto> posts = postService.findPostsByPage(pageable);
        PageDto dto = new PageDto(posts.getContent(), posts.getTotalPages(), posts.getTotalElements());
        return ApiResponse.response(dto);
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostDto> getPost(@PathVariable Long postId) throws NotFoundException {
        try {
            PostDto post = postService.findPostById(postId);
            return ApiResponse.response(post);
        } catch (PostNotFoundException e) {
            throw new NotFoundException("requested post was not found", e);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public ApiResponse<PostDto> savePost(@RequestBody final PostRequest postRequest) {
        PostDto post = postService.savePost(postRequest);
        return ApiResponse.response(post);
    }

    @PutMapping("/{postId}")
    public ApiResponse<PostDto> updatePost(
        @PathVariable Long postId,
        @RequestBody final PostRequest postRequest
    ) {
        PostDto post = postService.updatePost(postId, postRequest);
        return ApiResponse.response(post);
    }
}
