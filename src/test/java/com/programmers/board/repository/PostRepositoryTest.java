package com.programmers.board.repository;

import com.programmers.board.domain.Post;
import com.programmers.board.domain.User;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostRepositoryTest {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManagerFactory emf;

    User givenUser;
    Post givenPost;

    @BeforeEach
    void setUp() {
        givenUser = new User("name", 20, "hobby");
        givenPost = new Post("title", "content", givenUser);
    }

    @Nested
    @DisplayName("중첩: findAllWithUser 호출")
    class FindAllWithUserTest {
        Page<Post> postsWithUser;

        @BeforeEach
        void setUp() {
            //given
            userRepository.save(givenUser);
            postRepository.save(givenPost);
            PageRequest pageRequest = PageRequest.of(0, 1);

            //when
            postsWithUser = postRepository.findAllWithUser(pageRequest);
        }

        @Test
        @DisplayName("성공: post 목록 조회")
        void findAllWithUser() {
            //then
            List<Post> content = postsWithUser.getContent();
            assertThat(content).hasSize(1);
        }

        @Test
        @DisplayName("성공: user 로딩")
        void findAllWithUser_userLoaded() {
            //then
            List<Post> content = postsWithUser.getContent();
            assertThat(content).allSatisfy(post -> {
                boolean userLoaded = emf.getPersistenceUnitUtil().isLoaded(post.getUser());
                assertThat(userLoaded).isTrue();
            });
        }

    }

    @Nested
    @DisplayName("중첩: findByIdWithUser 호출")
    class FindByIdWithUserTest {
        Optional<Post> optionalPostWithUser;

        @BeforeEach
        void setUp() {
            //given
            userRepository.save(givenUser);
            postRepository.save(givenPost);

            //when
            optionalPostWithUser = postRepository.findByIdWithUser(givenPost.getId());
        }

        @Test
        @DisplayName("성공: post 단건 조회")
        void findAllWithUser() {
            //then
            assertThat(optionalPostWithUser).isNotEmpty();
            Post post = optionalPostWithUser.get();
            assertThat(post).isEqualTo(givenPost);
        }

        @Test
        @DisplayName("성공: user 로딩")
        void findAllWithUser_userLoaded() {
            //then
            Post postWithUser = optionalPostWithUser.get();
            boolean userLoaded = emf.getPersistenceUnitUtil().isLoaded(postWithUser.getUser());
            assertThat(userLoaded).isTrue();
        }

    }
}