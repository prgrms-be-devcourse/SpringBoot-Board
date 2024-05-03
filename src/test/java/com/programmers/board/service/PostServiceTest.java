package com.programmers.board.service;

import com.programmers.board.domain.Post;
import com.programmers.board.domain.User;
import com.programmers.board.service.response.PostResponse;
import com.programmers.board.service.response.UserResponse;
import com.programmers.board.exception.AuthorizationException;
import com.programmers.board.repository.PostRepository;
import com.programmers.board.repository.UserRepository;
import com.programmers.board.service.request.post.PostCreateCommand;
import com.programmers.board.service.request.post.PostDeleteCommand;
import com.programmers.board.service.request.post.PostGetCommand;
import com.programmers.board.service.request.post.PostUpdateCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks
    PostService postService;
    @Mock
    PostRepository postRepository;
    @Mock
    UserRepository userRepository;

    User givenUser;
    Post givenPost;

    @BeforeEach
    void setUp() {
        givenUser = new User("name", 20, "hobby");
        givenPost = new Post("title", "content", givenUser);
    }

    @Nested
    @DisplayName("중첩: 게시글 생성")
    class CreatePostTest {
        @Test
        @DisplayName("성공")
        void createPost() {
            //given
            Long userId = 1L;
            String title = "title";
            String content = "content";
            PostCreateCommand command = PostCreateCommand.of(userId, title, content);

            given(userRepository.findById(any())).willReturn(Optional.ofNullable(givenUser));

            //when
            postService.createPost(command);

            //then
            then(postRepository).should().save(any());
        }

        @Test
        @DisplayName("예외: 존재하지 않는 회원")
        void createPost_ButNoSuchUser_Then_Exception() {
            //given
            Long userId = 1L;
            String title = "title";
            String content = "content";
            PostCreateCommand command = PostCreateCommand.of(userId, title, content);

            given(userRepository.findById(any())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> postService.createPost(command))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    @Test
    @DisplayName("성공: 게시글 목록 조회")
    void findPosts() {
        //given
        int page = 0;
        int size = 1;
        PageRequest pageRequest = PageRequest.of(page, size);
        PageImpl<Post> posts = new PageImpl<>(List.of(givenPost), pageRequest, 1);

        given(postRepository.findAllWithUser(pageRequest)).willReturn(posts);

        //when
        Page<PostResponse> postPage = postService.findPosts(pageRequest);

        //then
        assertThat(postPage.getContent()).hasSize(1);
    }

    @Nested
    @DisplayName("중첩: 게시글 단건 조회")
    class FindPostTest {
        @Test
        @DisplayName("성공")
        void findPost() {
            //given
            PostGetCommand command = PostGetCommand.of(1L);

            given(postRepository.findByIdWithUser(any())).willReturn(Optional.ofNullable(givenPost));

            //when
            PostResponse findPost = postService.findPost(command);

            //then
            UserResponse findUser = findPost.getUser();
            assertThat(findPost.getTitle()).isEqualTo(givenPost.getTitle());
            assertThat(findPost.getContent()).isEqualTo(givenPost.getContent());
            assertThat(findUser.getName()).isEqualTo(givenUser.getName());
            assertThat(findUser.getHobby()).isEqualTo(givenUser.getHobby());
            assertThat(findUser.getAge()).isEqualTo(givenUser.getAge());
        }

        @Test
        @DisplayName("예외: 존재하지 않는 게시글")
        void findPost_ButEmpty() {
            //given
            PostGetCommand command = PostGetCommand.of(1L);

            given(postRepository.findByIdWithUser(any())).willReturn(Optional.empty());

            //when
            //then
            assertThatThrownBy(() -> postService.findPost(command))
                    .isInstanceOf(NoSuchElementException.class);
        }
    }

    @Nested
    @DisplayName("중첩: 게시글 수정")
    class UpdatePostTest {
        @Test
        @DisplayName("성공")
        void updatePost() {
            //given
            String updateTitle = "updateTitle";
            String updateContent = "updateContent";
            PostUpdateCommand command = PostUpdateCommand.of(1L, 1L, updateTitle, updateContent);

            given(postRepository.findById(any())).willReturn(Optional.ofNullable(givenPost));
            given(userRepository.findById(any())).willReturn(Optional.ofNullable(givenUser));

            //when
            postService.updatePost(command);

            //then
            assertThat(givenPost.getTitle()).isEqualTo(updateTitle);
            assertThat(givenPost.getContent()).isEqualTo(updateContent);
        }

        @Test
        @DisplayName("예외: 게시글 작성자와 로그인 회원 불일치")
        void updatePost_ButNotEqualUser() {
            //given
            Long userId = 1L;
            Long loginUserId = 2L;
            User user = new User("newUser", 20, "newHobby");
            ReflectionTestUtils.setField(givenUser, "id", userId);
            ReflectionTestUtils.setField(user, "id", loginUserId);
            PostUpdateCommand command = PostUpdateCommand.of(1L, loginUserId, "tile", "request");

            given(postRepository.findById(any())).willReturn(Optional.ofNullable(givenPost));
            given(userRepository.findById(any())).willReturn(Optional.of(user));

            //when
            //then
            assertThatThrownBy(() -> postService.updatePost(command))
                    .isInstanceOf(AuthorizationException.class);
        }
    }

    @Nested
    @DisplayName("중첩: 게시글 삭제")
    class DeletePostTest {
        @Test
        @DisplayName("성공")
        void deletePost() {
            //given
            PostDeleteCommand command = PostDeleteCommand.of(1L, 1L);

            given(userRepository.findById(any())).willReturn(Optional.ofNullable(givenUser));
            given(postRepository.findById(any())).willReturn(Optional.ofNullable(givenPost));

            //when
            postService.deletePost(command);

            //then
            then(postRepository).should().delete(any());
        }

        @Test
        @DisplayName("예외: 게시글 작성자와 로그인 회원 불일치")
        void deletePost_ButNotEqualUser() {
            //given
            Long userId = 1L;
            Long loginUserId = 2L;
            User user = new User("newUser", 20, "newHobby");
            ReflectionTestUtils.setField(givenUser, "id", userId);
            ReflectionTestUtils.setField(user, "id", loginUserId);
            PostDeleteCommand command = PostDeleteCommand.of(userId, loginUserId);

            given(userRepository.findById(any())).willReturn(Optional.of(user));
            given(postRepository.findById(any())).willReturn(Optional.ofNullable(givenPost));

            //when
            //then
            assertThatThrownBy(() -> postService.deletePost(command))
                    .isInstanceOf(AuthorizationException.class);
        }
    }
}