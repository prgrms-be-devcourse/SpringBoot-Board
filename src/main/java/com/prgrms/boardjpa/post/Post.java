package com.prgrms.boardjpa.post;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.util.Assert;

import com.prgrms.boardjpa.commons.domain.BaseEntity;
import com.prgrms.boardjpa.commons.exception.CreationFailException;
import com.prgrms.boardjpa.user.User;

import lombok.Getter;

@Getter
@Entity
public class Post extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", columnDefinition = "TEXT", nullable = false)
	private String content;

	@Column(name = "writer", nullable = false)
	private String createdBy;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User writer;

	protected Post() {
	}

	private Post(String title, User writer, String content) {
		validateTitle(title);
		validateContent(content);
		validateWriter(writer);

		this.title = title;
		this.content = content;
		this.writer = writer;
		this.createdBy = writer.getName();
	}

	public static PostBuilder builder() {
		return new PostBuilder();
	}

	public static class PostBuilder {
		private String title;
		private String content;
		private User writer;

		private PostBuilder() {
		}

		public PostBuilder title(String title) {
			this.title = title;

			return this;
		}

		public PostBuilder content(String content) {
			this.content = content;

			return this;
		}

		public PostBuilder writer(User user) {
			this.writer = user;

			return this;
		}

		public Post build() {
			try {
				return new Post(this.title, this.writer, this.content);
			} catch (IllegalArgumentException e) {
				throw new CreationFailException(Post.class);
			}
		}
	}

	public void setWriter(User writer) {
		validateWriter(writer);

		this.writer = writer;
		this.createdBy = writer.getName();
	}

	public void edit(String title, String content) {
		validateTitle(title);
		validateContent(content);

		this.title = title;
		this.content = content;
	}

	private void validateTitle(String title) {
		Assert.hasText(title, "제목은 비어있을 수 없습니다");
	}

	private void validateContent(String content) {
		Assert.hasText(content, "본문은 비어있을 수 없습니다");
	}

	private void validateWriter(User writer) {
		Assert.notNull(writer, "작성자가 null 일 수 없습니다");
	}
}
