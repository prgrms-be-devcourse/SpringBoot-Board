package com.juwoong.springbootboardjpa.post.application;

import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.juwoong.springbootboardjpa.post.application.model.PostDto;
import com.juwoong.springbootboardjpa.post.domain.Post;
import com.juwoong.springbootboardjpa.post.domain.repository.PostRepository;
import com.juwoong.springbootboardjpa.user.application.UserProvider;
import com.juwoong.springbootboardjpa.user.domain.User;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final UserProvider userProvider;
    private final PostRepository postRepository;

    public PostService(UserProvider userProvider, PostRepository postRepository) {
        this.userProvider = userProvider;
        this.postRepository = postRepository;
    }

    @Transactional
    public PostDto createPost(Long userId, String postTitle, String postContent) {
        User user = userProvider.getAuthor(userId);

        Post post = new Post(user, postTitle, postContent);
        post = postRepository.save(post);

        return toDto(post);
    }

    @Transactional
    public PostDto updatePost(Long id, String title, String content) {
        Post post = findById(id);
        post.update(title, content);
        post = postRepository.save(post);

        return toDto(post);
    }

    public PostDto getPost(Long id) {
        Post post = findById(id);

        return toDto(post);
    }

    public Page<PostDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(this::toDto);
    }

    private Post findById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("게시물이 존재하지 않습니다."));
    }

    private PostDto toDto(Post post) {
        return new PostDto(post);
    }

}
