package com.programmers.boardjpa.post.service;

import com.programmers.boardjpa.post.dto.PostInsertRequestDto;
import com.programmers.boardjpa.post.dto.PostPageResponseDto;
import com.programmers.boardjpa.post.dto.PostResponseDto;
import com.programmers.boardjpa.post.dto.PostUpdateRequestDto;
import com.programmers.boardjpa.post.entity.Post;
import com.programmers.boardjpa.post.exception.PostErrorCode;
import com.programmers.boardjpa.post.exception.PostException;
import com.programmers.boardjpa.post.mapper.PostMapper;
import com.programmers.boardjpa.post.repository.PostRepository;
import com.programmers.boardjpa.user.entity.User;
import com.programmers.boardjpa.user.exception.UserErrorCode;
import com.programmers.boardjpa.user.exception.UserException;
import com.programmers.boardjpa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND, id));

        return PostMapper.toPostResponseDto(post);
    }

    @Transactional
    public PostResponseDto insertPost(PostInsertRequestDto postInsertRequestDto) {
        User user = userRepository.findById(postInsertRequestDto.userId())
                .orElseThrow(() -> {
                    log.error("해당하는 User가 존재하지 않아 Post를 생성할 수 없습니다.");
                    return new UserException(UserErrorCode.NOT_FOUND, postInsertRequestDto.userId());
                });



        Post post = PostMapper.postInsertRequestDtoToPost(postInsertRequestDto, user);

        postRepository.save(post);

        return PostMapper.toPostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostUpdateRequestDto postUpdateRequestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException(PostErrorCode.NOT_FOUND, id));

        post.changeTitleAndContent(postUpdateRequestDto.title(), postUpdateRequestDto.content());

        return PostMapper.toPostResponseDto(post);
    }

    public PostPageResponseDto getPosts(PageRequest pageRequest) {
        Page<Post> postList = postRepository.findAllWithUser(pageRequest);

        return PostMapper.toPostPageResponseDto(postList);
    }

    @Transactional
    public void deleteAllPosts() {
        postRepository.deleteAll();
    }
}
