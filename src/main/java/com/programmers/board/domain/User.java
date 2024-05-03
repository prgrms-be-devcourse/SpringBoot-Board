package com.programmers.board.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private static final Pattern NAME_PATTERN = Pattern.compile("^(?=.*[A-Za-z])[A-Za-z\\d]{1,30}$");
    private static final Pattern HOBBY_PATTERN = Pattern.compile("^[A-Za-z가-힣]{1,50}$");

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String name;
    private int age;

    @Column(nullable = false, length = 50)
    private String hobby;

    public User(String name, int age, String hobby) {
        validateNotNullArguments(name, hobby);
        validateName(name);
        validateHobby(hobby);
        this.name = name;
        this.age = age;
        this.hobby = hobby;
    }

    private void validateNotNullArguments(String name, String hobby) {
        if (isNull(name)) {
            throw new IllegalArgumentException("사용자 이름은 필수입니다");
        }
        if (isNull(hobby)) {
            throw new IllegalArgumentException("사용자 취미는 필수입니다");
        }
    }

    public void updateMember(String name, Integer age, String hobby) {
        if (nonNull(name)) {
            validateName(name);
            this.name = name;
        }
        if (nonNull(age)) {
            this.age = age;
        }
        if (nonNull(hobby)) {
            validateHobby(hobby);
            this.hobby = hobby;
        }
    }

    private void validateName(String name) {
        if (invalidName(name)) {
            throw new IllegalArgumentException("이름은 30자 이하의 영어, 숫자로 구성됩니다");
        }
    }

    private boolean invalidName(String name) {
        return !NAME_PATTERN.matcher(name).matches();
    }

    private void validateHobby(String hobby) {
        if (invalidHobby(hobby)) {
            throw new IllegalArgumentException("취미는 50자 이하의 영어, 한글로 구성됩니다");
        }
    }

    private boolean invalidHobby(String hobby) {
        return !HOBBY_PATTERN.matcher(hobby).matches();
    }
}

