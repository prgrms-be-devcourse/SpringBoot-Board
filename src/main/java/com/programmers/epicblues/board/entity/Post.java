package com.programmers.epicblues.board.entity;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String title;
  private String content;
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Builder
  public Post(String title, String content) {
    this.title = title;
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Post)) {
      return false;
    }
    Post post = (Post) o;
    return Objects.equals(getId(), post.getId()) && getTitle().equals(post.getTitle()) && getContent().equals(post.getContent());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getTitle(), getContent());
  }

  public void removeUser() {
    if (this.user != null) {
      User removeTarget = this.user;
      this.user = null;
      removeTarget.removePostById(id);
    }
  }

  public void assignUser(User user) {
    this.user = user;
    this.createdBy = user.getName();
    if (!user.contains(this)) {
      user.addPost(this);
    }
  }

  public void updateTitle(String title) {
    this.title = title;
  }

  public void updateContent(String content) {
    this.content = content;
  }
}
