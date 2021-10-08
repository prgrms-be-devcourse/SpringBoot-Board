package org.prgms.board.domain.entity;

import lombok.Builder;
import lombok.Getter;
import org.prgms.board.common.BaseTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "post")
public class Post extends BaseTime {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 500, nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "author", nullable = false)
    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    protected Post() {
    }

    @Builder
    private Post(Long id, String title, String content, String author, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.user = user;
    }

    public void changeInfo(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addComment(Comment comment) {
        this.getComments().add(comment);
    }

}
