package com.programmers.jpa_board.post.application;

import com.programmers.jpa_board.post.domain.Post;
import com.programmers.jpa_board.post.domain.dto.PostDto;
import com.programmers.jpa_board.global.exception.NotFoundException;
import com.programmers.jpa_board.post.infra.PostRepository;
import com.programmers.jpa_board.post.util.PostConverter;
import com.programmers.jpa_board.user.application.UserProviderService;
import com.programmers.jpa_board.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.programmers.jpa_board.global.exception.ExceptionMessage.NOT_FOUND_POST;

@Transactional(readOnly = true)
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserProviderService userService;

    public PostServiceImpl(PostRepository postRepository, UserProviderService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Transactional
    @Override
    public PostDto.CommonResponse save(PostDto.CreatePostRequest request) {
        Post post = PostConverter.toEntity(request);
        User user = userService.getOne(request.userId());
        post.addUser(user);

        Post saved = postRepository.save(post);

        return PostConverter.toDto(saved);
    }

    @Override
    public PostDto.CommonResponse getOne(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_POST.getMessage()));

        return PostConverter.toDto(post);
    }

    @Override
    public Page<PostDto.CommonResponse> getPage(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostConverter::toDto);
    }

    @Transactional
    @Override
    public PostDto.CommonResponse update(Long postId, PostDto.UpdatePostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_POST.getMessage()));

        post.update(request.title(), request.content());

        return PostConverter.toDto(post);
    }
}
