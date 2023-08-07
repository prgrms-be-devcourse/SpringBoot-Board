package com.example.springbootjpa.domain.user;

import com.example.springbootjpa.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "USERS_TBL")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 25)
    private String name;

    @Column(name = "age", nullable = false)
    private int age;

    @Enumerated(value = EnumType.STRING)
    private Hobby hobby;

    protected User() {}

    public User(String name, int age, Hobby hobby) {
        this.name = name;
        this.age = age;
        this.hobby = hobby;
        this.setCreatedBy(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Hobby getHobby() {
        return hobby;
    }
}
