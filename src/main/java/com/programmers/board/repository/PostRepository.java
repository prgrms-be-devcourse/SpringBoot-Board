package com.programmers.board.repository;

import com.programmers.board.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "select p from Post p join fetch p.user",
            countQuery = "select count(p) from Post p")
    Page<Post> findAllWithUser(Pageable pageable);

    @Query(value = "select p from Post p join fetch p.user where p.id = :postId",
            countQuery = "select count(p) from Post p")
    Optional<Post> findByIdWithUser(@Param("postId") Long postId);
}
