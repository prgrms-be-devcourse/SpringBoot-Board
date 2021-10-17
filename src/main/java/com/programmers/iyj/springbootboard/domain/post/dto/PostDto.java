package com.programmers.iyj.springbootboard.domain.post.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@EqualsAndHashCode
public class PostDto {
    @NotNull
    private Long id;

    @NotEmpty
    @Size(min = 1, max = 50)
    private String title;

    @NotEmpty
    @Size(min = 1, max = 10000000)
    private String content;
}
