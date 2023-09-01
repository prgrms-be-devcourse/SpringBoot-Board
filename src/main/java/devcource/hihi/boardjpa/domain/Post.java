package devcource.hihi.boardjpa.domain;

import devcource.hihi.boardjpa.dto.post.CreateRequestDto;
import devcource.hihi.boardjpa.dto.post.ResponsePostDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @Column(name="post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title",nullable = false,updatable = true)
    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }


    public static ResponsePostDto toResponseDto(Post post) {
        return new ResponsePostDto(post.getId(),post.getTitle(), post.getContent(), post.getUser().getId(),post.getCreated_at(),post.getUpdated_at());
    }
    public static CreateRequestDto toCreateDto(Post post) {
        return new CreateRequestDto(post.getTitle(), post.getContent());
    }

    public void allocateUser(User user) {
        this.user = user;
    }
}
