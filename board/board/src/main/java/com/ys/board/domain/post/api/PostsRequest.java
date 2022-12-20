package com.ys.board.domain.post.api;

import javax.validation.constraints.Max;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostsRequest {

    private Long cursorId;

    @Max(100)
    private int pageSize;

}
