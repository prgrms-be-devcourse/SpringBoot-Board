package com.programmers.springbootboard.member.domain;

import com.programmers.springbootboard.common.domain.BaseEntity;
import com.programmers.springbootboard.member.domain.vo.Age;
import com.programmers.springbootboard.member.domain.vo.Hobby;
import com.programmers.springbootboard.member.domain.vo.Name;
import com.programmers.springbootboard.member.dto.MemberUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
public class Member extends BaseEntity<Long> {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Age age;

    @Embedded
    private Hobby hobby;

    protected Member() {

    }

    public void update(MemberUpdateRequest request) {
        this.name = request.getName();
        this.age = request.getAge();
        this.hobby = request.getHobby();
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Age getAge() {
        return age;
    }

    public Hobby getHobby() {
        return hobby;
    }
}
