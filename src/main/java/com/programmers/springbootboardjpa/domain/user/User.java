package com.programmers.springbootboardjpa.domain.user;

import com.programmers.springbootboardjpa.domain.BaseEntity;
import com.programmers.springbootboardjpa.domain.post.Post;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "age")
    private Long age;

    @Column(name = "hobby")
    private String hobby;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    protected User() {}

    public User(String createdBy, LocalDateTime cratedAt, String name, Long age, String hobby, List<Post> posts) {
        super(createdBy, cratedAt);
        this.name = name;
        this.age = age;
        this.hobby = hobby;
        this.posts = posts;
    }

    public void addPost(Post post) {
        post.setUser(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getAge() {
        return age;
    }

    public String getHobby() {
        return hobby;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
