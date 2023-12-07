package com.prgrms.dev.springbootboardjpa.domain.user;

import com.prgrms.dev.springbootboardjpa.domain.BaseEntity;
import com.prgrms.dev.springbootboardjpa.domain.post.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Post> posts = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "name", length = 20, nullable = false)
    private String name;
    @Column(name = "hobby", nullable = false)
    @Enumerated(EnumType.STRING)
    private Hobby hobby;
    @Column(name = "age", nullable = false)
    private int age;

    @Builder
    public User(String name, String hobby, int age) {
        validateNull(name);
        validateAge(age);

        this.name = name;
        this.hobby = Hobby.getHobby(hobby);
        this.age = age;
    }

    private void validateNull(String input) {
        if (input.isEmpty() || input.isBlank()) throw new IllegalArgumentException("값을 입력 해야 합니다.");
    }

    private void validateAge(int age) {
        if (age <= 0 || age >= 200) throw new IllegalArgumentException("나이는 1~199세 사이의 값 이어야 합니다.");
    }

}
