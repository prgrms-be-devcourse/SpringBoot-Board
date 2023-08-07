package com.example.springbootjpa.ui;


import com.example.springbootjpa.application.PostService;
import com.example.springbootjpa.ui.dto.post.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostFindResponse>> findAll(@PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok().body(postService.findAllPosts(pageable));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostFindResponse> findById(@PathVariable Long postId) {

        return ResponseEntity.ok(postService.find(postId));
    }

    @PostMapping
    public ResponseEntity<PostSaveResponse> createPost(@RequestBody PostSaveRequest postSaveRequest) {
        long postId = postService.createPost(postSaveRequest.userId(), postSaveRequest.title(), postSaveRequest.content());

        return ResponseEntity.created(URI.create("/api/v1/posts/" + postId)).body(new PostSaveResponse(postId));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostUpdateResponse> updatePost(
            @RequestBody PostUpdateRequest postUpdateRequest,
            @PathVariable Long postId
    ) {
        postService.updatePost(postId, postUpdateRequest.title(), postUpdateRequest.content());

        return ResponseEntity.ok().body(new PostUpdateResponse(postId));
    }
}
