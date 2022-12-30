package com.prgrms.dto;

import com.prgrms.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDto {

    public record Request(@NotBlank String name, @NotBlank String hobby, @NotNull int age, @NotBlank String email, @NotBlank String password) {

        public User toUser() {
            return new User(name, hobby, age, email, password);
        }

        public record Login(@NotBlank String email, @NotBlank String password) {
            public User toUser(User user) {
                return new User(user.getName(), user.getHobby(), user.getAge(), user.getEmail(), user.getPassword());
            }
        }
    }

    public record Response(User user) {

        public User toUser() {
            return new User(user.getId(), user.getName(), user.getHobby(), user.getAge(), user.getEmail(), user.getPassword());
        }

        public long getUserId() {
            return user().getId();
        }
    }

}
