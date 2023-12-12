package com.programmers.boardjpa.post.controller;

import com.programmers.boardjpa.post.dto.PostInsertRequestDto;
import com.programmers.boardjpa.post.dto.PostPageResponseDto;
import com.programmers.boardjpa.post.dto.PostResponseDto;
import com.programmers.boardjpa.post.dto.PostUpdateRequestDto;
import com.programmers.boardjpa.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable(name = "id") Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @GetMapping("/posts")
    public ResponseEntity<PostPageResponseDto> getPosts(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(postService.getPosts(PageRequest.of(page, size)));
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> insertPost(@RequestBody PostInsertRequestDto postInsertRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.insertPost(postInsertRequestDto));
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable(name = "id") Long postId, @RequestBody PostUpdateRequestDto postUpdateRequestDto){
        return ResponseEntity.ok(postService.updatePost(postId, postUpdateRequestDto));
    }
}
