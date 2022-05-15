package com.study.board.domain.post.domain;

import com.study.board.domain.user.domain.User;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.study.board.domain.post.domain.Post.POST_TITLE_MAX_LENGTH;
import static com.study.board.fixture.Fixture.createUser;
import static org.assertj.core.api.Assertions.*;

class PostTest {

    @Test
    void 생성_성공() {
        User writer = createUser();
        Post post = Post.create("제목", "내용", writer);

        assertThat(post.getId()).isNull();
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
        assertThat(post.getWrittenDateTime()).isNotNull();
        assertThat(post.getWriter().getId()).isEqualTo(writer.getId());
        assertThat(post.getWriter().getName()).isEqualTo(writer.getName());
        assertThat(post.getWriter().getHobby()).isEqualTo(writer.getHobby());
    }

    @NullAndEmptySource
    @ParameterizedTest
    void 제목이_null_이거나_비어있으면_생성실패(String title) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Post.create(title, "내용", createUser()));
    }

    @Test
    void 제목의_길이가_제한_보다_크면_생성실패() {
        String title = RandomString.make(POST_TITLE_MAX_LENGTH + 1);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> Post.create(title, "내용", createUser()));
    }

    @NullAndEmptySource
    @ParameterizedTest
    void 내용이_null_이거나_비어있으면_생성실패(String content) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Post.create("제목", content, createUser()));
    }

    @Test
    void 작성자가_null_이면_생성_실패() {
        assertThatNullPointerException()
                .isThrownBy(() -> Post.create("제목", "내용", null));
    }

}