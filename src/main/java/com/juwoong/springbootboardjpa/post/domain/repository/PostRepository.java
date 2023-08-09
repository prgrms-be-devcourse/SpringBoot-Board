package com.juwoong.springbootboardjpa.post.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.juwoong.springbootboardjpa.post.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
