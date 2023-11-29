package com.programmers.springboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmers.springboard.entity.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m join fetch m.groups g left join fetch g.groupPermissions gp join fetch gp.permission p where m.loginId = :loginId")
    Optional<Member> findByLoginId(@Param("loginId") String LoginId);
}

