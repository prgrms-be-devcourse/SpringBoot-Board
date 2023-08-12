package com.example.jpaboard.member.domain;

import com.example.jpaboard.global.BaseEntity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "members")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Age age;

    private String hobby;

    protected Member() {
    }

    public Member(Name name, Age age, String hobby) {
        this.name = name;
        this.age = age;
        this.hobby = hobby;
    }

    public Member(Long id, Name name, Age age, String hobby) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.hobby = hobby;
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

    public String getHobby() {
        return hobby;
    }

}
