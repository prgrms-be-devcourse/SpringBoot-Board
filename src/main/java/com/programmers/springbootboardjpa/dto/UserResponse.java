package com.programmers.springbootboardjpa.dto;

import com.programmers.springbootboardjpa.domain.user.Hobby;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponse {

    private final Long id;
    private final String name;
    private final Integer age;
    private final Hobby hobby;
    private final LocalDateTime createdAt;
    private final String createdBy;

    @Builder
    public UserResponse(Long id, String name, Integer age, Hobby hobby, LocalDateTime createdAt, String createdBy) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.hobby = hobby;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

}
