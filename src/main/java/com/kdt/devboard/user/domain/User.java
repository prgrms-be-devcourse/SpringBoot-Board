package com.kdt.devboard.user.domain;

import com.kdt.devboard.Common.BaseEntity;
import com.kdt.devboard.post.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

/*    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;*/

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "age", nullable = false)
    int age;

    @Column(name = "hobby")
    String hobby;

    @Builder
    public User(Long id, String createBy, String name, int age, String hobby, LocalDateTime createAt) {
        super(createBy, createAt);
        this.id = id;
        this.name = name;
        this.age = age;
        this.hobby = hobby;
    }

    public void changeInfo(String name, int age, String hobby) {
        this.name = name;
        this.age = age;
        this.hobby = hobby;
    }

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    public void addPost(Post post) { post.setUser(this); }

}