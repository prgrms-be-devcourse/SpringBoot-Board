package com.prgrms.springbootboardjpa.post.service;

import com.prgrms.springbootboardjpa.post.dto.PostConverter;
import com.prgrms.springbootboardjpa.post.dto.PostDto;
import com.prgrms.springbootboardjpa.post.dto.PostResponse;
import com.prgrms.springbootboardjpa.post.entity.Post;
import com.prgrms.springbootboardjpa.post.repository.PostRepository;
import com.prgrms.springbootboardjpa.user.entity.User;
import com.prgrms.springbootboardjpa.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final PostConverter postConverter;

    public PostService(PostRepository postRepository, UserService userService, PostConverter postConverter) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.postConverter = postConverter;
    }

    @Transactional
    public PostResponse save(PostDto postDto){
        User user = userService.login(postDto.getEmail(), postDto.getPassword());
        Post post = postConverter.convertToPost(postDto, user);
        Post savedPost = postRepository.save(post);
        return postConverter.convertToPostResponse(savedPost);
    }

    public Page<PostResponse> findAll(Pageable pageable){
        return postRepository.findAll(pageable).map(post -> postConverter.convertToPostResponse(post));
    }

    @Transactional
    public PostResponse update(PostDto postDto){
        if (postDto.getId() == null){
            throw new IllegalStateException("id를 입력해주세요");
        }
        userService.login(postDto.getEmail(), postDto.getPassword());

        Post post = postRepository.findById(postDto.getId()).get();
        post.updatePost(postDto);

        return postConverter.convertToPostResponse(post);
    }

}
