package com.example.board.domain.comment.entity;

import com.example.board.domain.common.entity.BaseEntity;
import com.example.board.domain.member.entity.Member;
import com.example.board.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ColumnDefault("FALSE")
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Comment parent;

    public Comment(String content , Post post, Member writer, Comment parent) {
        this.content = content;
        this.isDeleted = false;
        this.writer = writer;
        this.post = post;
        this.parent = parent;
    }

    public void delete() {
        this.content = "(삭제된 댓글입니다.)";
        this.isDeleted = true;
    }

    public void update(String content) {
        this.content = content;
    }
}
