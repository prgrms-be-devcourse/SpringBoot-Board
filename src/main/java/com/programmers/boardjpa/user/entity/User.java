package com.programmers.boardjpa.user.entity;

import com.programmers.boardjpa.global.common.BaseEntity;
import com.programmers.boardjpa.user.entity.vo.Age;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    private static final int MIN_AGE = 0;
    private static final int MAX_AGE = 110;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", columnDefinition = "text")
    private String name;

    @Embedded
    @Column(name = "age")
    private Age age;

    @Column(name = "hobby")
    private String hobby;

    @Builder
    public User(String name, int age, String hobby) {
        this.name = name;
        this.age = new Age(age);
        this.hobby = hobby;
    }
}
