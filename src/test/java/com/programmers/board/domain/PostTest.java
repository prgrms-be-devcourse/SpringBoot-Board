package com.programmers.board.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostTest {
    User givenUser;

    @BeforeEach
    void setUp() {
        givenUser = new User("name", 20, "hobby");
    }

    @Nested
    @DisplayName("중첩: post 생성")
    class NewPostTest {
        @Test
        @DisplayName("성공")
        void newPost() {
            //given
            String title = "title";
            String content = "content";

            //when
            Post post = new Post(title, content, givenUser);

            //then
            assertThat(post.getTitle()).isEqualTo(title);
            assertThat(post.getContent()).isEqualTo(content);
        }

        @Test
        @DisplayName("예외: 잘못된 범위의 게시글 제목")
        void newPost_ButTitleOutOfRange() {
            //given
            String titleOutOfRange = "a".repeat(101);

            //when
            //then
            assertThatThrownBy(() -> new Post(titleOutOfRange, "content", givenUser))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: 잘못된 범위의 게시글 본문")
        void newPost_ButContentOutOfRange() {
            //given
            String contentOutOfRange = "";

            //when
            //then
            assertThatThrownBy(() -> new Post("title", contentOutOfRange, givenUser))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: 게시글 제목 null")
        void newPost_ButTitleIsNull() {
            //given
            String nullTitle = null;

            //when
            //then
            assertThatThrownBy(() -> new Post(nullTitle, "content", givenUser))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: 게시글 본문 null")
        void newPost_ButContentNull() {
            //given
            String nullContent = null;

            //when
            //then
            assertThatThrownBy(() -> new Post("title", nullContent, givenUser))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: 게시글 작성자 null")
        void newPost_ButUserNull() {
            //given
            User nullUser = null;

            //when
            //then
            assertThatThrownBy(() -> new Post("title", "content", nullUser))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("중첩: post 업데이트")
    class UpdatePostTest {
        Post givenPost;

        @BeforeEach
        void setUp() {
            givenPost = new Post("title", "content", givenUser);
        }

        @Test
        @DisplayName("성공")
        void updatePost() {
            //given
            String updateTitle = "updateTitle";
            String updateContent = "updateContent";

            //when
            givenPost.updatePost(updateTitle, updateContent);

            //then
            assertThat(givenPost.getTitle()).isEqualTo(updateTitle);
            assertThat(givenPost.getContent()).isEqualTo(updateContent);
        }

        @Test
        @DisplayName("예외: 잘못된 범위의 게시글 제목")
        void updatePost_ButTitleOutOfRange() {
            //given
            String titleOutOfRange = "a".repeat(101);

            //when
            //then
            assertThatThrownBy(() -> givenPost.updatePost(titleOutOfRange, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: 잘못된 범위의 게시글 본문")
        void updatePost_ButContentOutOfRange() {
            //given
            String contentOutOfRange = "";

            //when
            //then
            assertThatThrownBy(() -> givenPost.updatePost(null, contentOutOfRange))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}