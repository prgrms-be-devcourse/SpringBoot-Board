package prgrms.project.post.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import prgrms.project.post.controller.response.DefaultApiResponse;
import prgrms.project.post.service.DefaultPage;
import prgrms.project.post.service.post.PostDto;
import prgrms.project.post.service.post.PostService;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    @PostMapping
    public DefaultApiResponse<Long> uploadPost(@RequestBody @Validated PostDto postDto) {
        return DefaultApiResponse.ok(postService.uploadPost(postDto));
    }

    @GetMapping("/{postId}")
    public DefaultApiResponse<PostDto> searchPost(@PathVariable Long postId) {
        return DefaultApiResponse.ok(postService.searchById(postId));
    }

    @GetMapping
    public DefaultApiResponse<DefaultPage<PostDto>> searchAllPosts(Pageable pageable) {
        return DefaultApiResponse.ok(postService.searchAll(pageable));
    }

    @PutMapping("/{postId}")
    public DefaultApiResponse<Long> updatePost(@PathVariable Long postId, @RequestBody @Validated PostDto postDto) {
        return DefaultApiResponse.ok(postService.updatePost(postId, postDto));
    }

    @DeleteMapping("/{postId}")
    public DefaultApiResponse<Boolean> deletePost(@PathVariable Long postId) {
        postService.deleteById(postId);

        return DefaultApiResponse.ok(true);
    }
}
