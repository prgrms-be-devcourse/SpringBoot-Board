package prgrms.project.post.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import prgrms.project.post.domain.BaseEntity;
import prgrms.project.post.domain.post.Post;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.GenerationType.AUTO;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "user_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "age", nullable = false)
    private int age;

    @OneToMany(mappedBy = "user", cascade = PERSIST)
    private final Set<Hobby> hobbies = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = PERSIST)
    private final List<Post> posts = new ArrayList<>();

    @Builder
    public User(Long id, String name, int age, Set<Hobby> hobbies) {
        this.id = id;
        this.name = name;
        this.age = age;
        registerHobby(hobbies);
    }

    public User updateUserInfo(String name, int age, Set<Hobby> hobbies) {
        this.name = name;
        this.age = age;
        registerHobby(hobbies);

        return this;
    }

    private void registerHobby(Set<Hobby> hobbies) {
        this.hobbies.clear();
        hobbies.forEach(h -> h.assignUser(this));
    }
}
