package com.example.springbootboardjpa.domain.post.entity;

import com.example.springbootboardjpa.domain.member.entity.Member;
import com.example.springbootboardjpa.global.entity.BaseTimeEntity;
import com.example.springbootboardjpa.global.exception.CustomException;
import com.example.springbootboardjpa.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(nullable = false)
  private String title;
  @Column
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  public void setMember(Member member) {
    if (this.member != null) {
      this.member.getPosts().remove(this);
    }
    this.member = member;
    member.getPosts().add(this);
  }

  public void update(Post updatePost) {
    this.title = updatePost.getTitle();
    this.content = updatePost.getContent();
  }

  public void isOwner(long memberId) {
    this.member.isId(memberId);
  }
}
