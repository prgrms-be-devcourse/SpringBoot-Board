package com.programmers.board.service;

import com.programmers.board.constant.AuthErrorMessage;
import com.programmers.board.domain.Post;
import com.programmers.board.domain.User;
import com.programmers.board.service.response.PostResponse;
import com.programmers.board.exception.AuthorizationException;
import com.programmers.board.repository.PostRepository;
import com.programmers.board.repository.UserRepository;
import com.programmers.board.service.request.post.PostCreateCommand;
import com.programmers.board.service.request.post.PostDeleteCommand;
import com.programmers.board.service.request.post.PostGetCommand;
import com.programmers.board.service.request.post.PostUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {
    private static final String NO_SUCH_USER = "존재하지 않는 회원입니다";
    private static final String NO_SUCH_POST = "존재하지 않은 게시글입니다";

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createPost(PostCreateCommand command) {
        User user = findUserOrElseThrow(command.getLoginUserId());
        Post post = new Post(
                command.getTitle(),
                command.getContent(),
                user
        );
        postRepository.save(post);
        return post.getId();
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> findPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAllWithUser(pageable);
        return posts.map(PostResponse::from);
    }

    @Transactional(readOnly = true)
    public PostResponse findPost(PostGetCommand command) {
        Post post = postRepository.findByIdWithUser(command.getPostId())
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_POST));
        return PostResponse.from(post);
    }

    @Transactional
    public void updatePost(PostUpdateCommand command) {
        User loginUser = findUserOrElseThrow(command.getLoginUserId());
        Post post = findPostOrElseThrow(command.getPostId());
        checkAuthority(post, loginUser);
        post.updatePost(
                command.getTitle(),
                command.getContent()
        );
    }

    @Transactional
    public void deletePost(PostDeleteCommand command) {
        User loginUser = findUserOrElseThrow(command.getLoginUserId());
        Post post = findPostOrElseThrow(command.getPostId());
        checkAuthority(post, loginUser);
        postRepository.delete(post);
    }

    private Post findPostOrElseThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_POST));
    }

    private User findUserOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_USER));
    }

    private void checkAuthority(Post post, User loginUser) {
        if (!post.isWriter(loginUser)) {
            throw new AuthorizationException(AuthErrorMessage.NO_AUTHORIZATION.getMessage());
        }
    }
}
